/**
 * Copyright (c) 2016 LARUS Business Automation [http://www.larus-ba.it]
 * <p>
 * This file is part of the "LARUS Integration Framework for Neo4j".
 * <p>
 * The "LARUS Integration Framework for Neo4j" is licensed
 * under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package it.larusba.integration.neo4jcouchbaseconnector.neo4j.transformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.ArrayUtils;
import org.apache.lucene.util.CollectionUtil;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.event.LabelEntry;
import org.neo4j.graphdb.event.PropertyEntry;
import org.neo4j.graphdb.event.TransactionData;
import org.neo4j.helpers.collection.IteratorUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonArray;
import com.couchbase.client.java.document.json.JsonObject;

/**
 * This class is responsible for transforming data from Neo4j Graph
 * into JSON documents for Couchbase
 * 
 * @author Mauro Roiter
 *
 */
public class Neo4jToCouchbaseTransformer {

	private static Logger LOGGER = LoggerFactory.getLogger(Neo4jToCouchbaseTransformer.class);
	
	private static final String COUCHBASE_ID_PROPERTY_KEY = "documentId";
	
	@SuppressWarnings("unchecked")
	public void transform(TransactionData data, GraphDatabaseService db) {
		
		Set<Object> setDocumentIds = this.couchbaseDocumentFinder(data, db);
		
		LOGGER.debug("Document ids");
		
		Set<Node> setRoots = new HashSet<Node>();
		Map<Node, List<Node>> mapChildren = new HashMap<Node, List<Node>>();
		
		for (Object documentId : setDocumentIds) {
			
			LOGGER.debug(documentId.toString());
			
			try (Result result = 
			    		 db.execute( "MATCH (root {"+COUCHBASE_ID_PROPERTY_KEY+": '"+documentId+"'}) "
			    		 		   + "WHERE NOT ({"+COUCHBASE_ID_PROPERTY_KEY+": '"+documentId+"'})-[]->(root) "
			    		 		   + "WITH root "
			    		 		   + "MATCH (n {"+COUCHBASE_ID_PROPERTY_KEY+": '"+documentId+"'}) "
			    		 		   + "OPTIONAL MATCH (n)-[r {"+COUCHBASE_ID_PROPERTY_KEY+": '"+documentId+"'}]->(m {"+COUCHBASE_ID_PROPERTY_KEY+": '"+documentId+"'}) "
			    				   + "RETURN root, n, collect(DISTINCT m) as m" ) )
			{
			    while ( result.hasNext() )
			    {
			        Map<String,Object> row = result.next();
			        Node root = (Node) row.get("root");
			        Node parent = (Node) row.get("n");
			        List<Node> children = (List<Node>) row.get("m");
			        
			        setRoots.add(root);
			        mapChildren.put(parent, children);
			    }
			}
		}	
		
		Node root = null;
		
		LOGGER.debug("Try to find root node...");
		
		for (Node rootNode : setRoots) {
			
			/*String rootName;
			
			try (Transaction tx = db.beginTx()) {
				
				rootName = rootNode.getProperty("firstname").toString();
			}
			
			LOGGER.debug("Possible root: " + rootName);*/

			boolean rootNotFounded = false;
			
			for (List<Node> children : mapChildren.values()) {
				
				if (!children.isEmpty()) {
					
					for (Node nodeChildren : children) {
						
//						String childName;
//						
//						try (Transaction tx = db.beginTx()) {
//							
//							childName = nodeChildren.getProperty("firstname").toString();
//						}
//						
//						LOGGER.debug("child: " + childName);
						
						if (rootNode.getId() == nodeChildren.getId()) {
							
//							LOGGER.debug("root " + rootName + " is also a child");
							rootNotFounded = true;
							break;
						}
					}
				}
			}
			
			if (!rootNotFounded) {
				
//				LOGGER.debug(rootName + " IS THE ROOT");
				root = rootNode;
			}
//			else {
//				LOGGER.debug(rootName + " is NOT the root");
//			}
		}
		
//		LOGGER.trace("FOUND ROOT NODE");
//		try (Transaction tx = db.beginTx()) {
//			LOGGER.trace(root.getAllProperties().toString());
//		}

		JsonObject jsonObject = JsonObject.empty();

		createJsonObject(db, root, jsonObject, mapChildren);
		
		
		LOGGER.debug("JSON OBJECT TO PUT IN COUCHBASE");
		LOGGER.debug(jsonObject.toString());
		
//		jsonDocument = JsonDocument.create(propertyId, jsonCouchbase);
	}

	private Set<Object> couchbaseDocumentFinder(TransactionData data, GraphDatabaseService db) {
		
		Set<Object> setDocumentIds = new HashSet<Object>();
		
		Iterable<Node> createdNodes = data.createdNodes();
		
		for (Node node : createdNodes) {
			
			extractPropertyFromNode(setDocumentIds, node, db);
		}
		
		Iterable<PropertyEntry<Node>> assignedNodeProperties = data.assignedNodeProperties();
		
		for (PropertyEntry<Node> propertyEntry : assignedNodeProperties) {
			
			Node nodeWithAssignedProperties = propertyEntry.entity();
			
			extractPropertyFromNode(setDocumentIds, nodeWithAssignedProperties, db);
		}
		
		Iterable<LabelEntry> assignedLabels = data.assignedLabels();
		
		for (LabelEntry labelEntry : assignedLabels) {
			
			Node nodeWithAssignedLabel = labelEntry.node();
			
			extractPropertyFromNode(setDocumentIds, nodeWithAssignedLabel, db);
		}
		
		Iterable<PropertyEntry<Relationship>> assignedRelationshipProperties = data.assignedRelationshipProperties();
		
		for (PropertyEntry<Relationship> propertyEntry : assignedRelationshipProperties) {
			
			Relationship relationshipWithAssignedProperties = propertyEntry.entity();
			
			extractPropertyFromRelationship(setDocumentIds, relationshipWithAssignedProperties, db);
		}
		
		Iterable<Relationship> createdRelationships = data.createdRelationships();
		
		for (Relationship relationship : createdRelationships) {
			
			extractPropertyFromRelationship(setDocumentIds, relationship, db);
		}
		
		Iterable<Node> deletedNodes = data.deletedNodes();
		
		for (Node node : deletedNodes) {
			
			extractPropertyFromNode(setDocumentIds, node, db);
		}
		
		Iterable<Relationship> deletedRelationships = data.deletedRelationships();
		
		for (Relationship relationship : deletedRelationships) {
			
			extractPropertyFromRelationship(setDocumentIds, relationship, db);	
		}
		
		Iterable<LabelEntry> removedLabels = data.removedLabels();
		
		for (LabelEntry labelEntry : removedLabels) {
			
			Node nodeWithRemovedLabel = labelEntry.node();
			
			extractPropertyFromNode(setDocumentIds, nodeWithRemovedLabel, db);	
		}
		
		Iterable<PropertyEntry<Node>> removedNodeProperties = data.removedNodeProperties();
		
		for (PropertyEntry<Node> propertyEntry : removedNodeProperties) {
			
			Node nodeWithRemovedProperties = propertyEntry.entity();
			
			extractPropertyFromNode(setDocumentIds, nodeWithRemovedProperties, db);
		}
		
		Iterable<PropertyEntry<Relationship>> removedRelationshipProperties = data.removedRelationshipProperties();
		
		for (PropertyEntry<Relationship> propertyEntry : removedRelationshipProperties) {
			
			Relationship relationshipWithRemovedProperties = propertyEntry.entity();
			
			extractPropertyFromRelationship(setDocumentIds, relationshipWithRemovedProperties, db);
		}
		
		return setDocumentIds;
	}

	private void createJsonObject(GraphDatabaseService db, Node node, JsonObject jsonObject, Map<Node, List<Node>> mapChildren) {
		
		try (Transaction tx = db.beginTx()) {
			
			Map<String, Object> allProperties = node.getAllProperties();
			
//			String propertyId = "";
			
			LOGGER.debug("Node " + node.getId() + " properties...");
			
			for (String property : allProperties.keySet()) {
				
//				LOGGER.debug("* " + property + ", " + allProperties.get(property));
//				if (property.equals(COUCHBASE_ID_PROPERTY_KEY)) {
//					
//					propertyId = property;
//				}
				
				if (allProperties.get(property) instanceof Object[]) {
					
					Object[] arrayProperty = (Object[])allProperties.get(property);
					List<Object> listProperty = new ArrayList<>();
					
					for (int i = 0; i < arrayProperty.length; i++) {
						
						listProperty.add(arrayProperty[i]);
					}
					
					jsonObject.put(property, listProperty);

					LOGGER.debug("* " + property + ", " + listProperty);
				}
				else {
					LOGGER.debug("* " + property + ", " + allProperties.get(property));
					
					jsonObject.put(property, allProperties.get(property));
				}
			}
			
			if (node.hasRelationship()) {
				
				Iterable<Relationship> relationships = node.getRelationships(Direction.OUTGOING);
				
				Map<String, List<Relationship>> relationshipsByType = new HashMap<String, List<Relationship>>();
				
				for (Relationship relationship : relationships) {
					
					LOGGER.debug("TYPE REL: " + relationship.getType().name());
					
					if(relationshipsByType.get(relationship.getType().name()) == null) {
						
						List<Relationship> listRels = new ArrayList<>();
						listRels.add(relationship);
						
						relationshipsByType.put(relationship.getType().name(), listRels);
					}
					else {
						
						List<Relationship> listRels = relationshipsByType.get(relationship.getType().name());
						listRels.add(relationship);
					}
				}
				
				for (String relName : relationshipsByType.keySet()) {
					
					LOGGER.debug("Elaborating relationship " + relName);
					
					List<Relationship> listRels = relationshipsByType.get(relName);
					
					if (listRels.size() > 1) {
						
						JsonArray jsonChilds = JsonArray.empty();
						JsonObject jsonChild = JsonObject.empty();
						
						LOGGER.debug("i figli sono un JsonArray");
						for (Node childNode : mapChildren.get(node)) {
							
							LOGGER.debug("Parsing child Node "+ childNode.getId());
							
							Iterable<Relationship> incomingRelsInChild = childNode.getRelationships(Direction.INCOMING);
							
							for (Relationship relationship : incomingRelsInChild) {
								
								if (relationship.getType().name().equals(relName)) {
									
									createJsonObject(db, childNode, jsonChild, mapChildren);
									
									LOGGER.debug("Adding " + childNode.getId());
									jsonChilds.add(jsonChild);
								}
							}	
						}
						
						String label = mapChildren.get(node).iterator().next().getLabels().iterator().next().name();
						
						jsonObject.put(label, jsonChilds);
					}
					else if (listRels.size() == 1) {
						
						LOGGER.debug("il figlio è un JsonObject");
						
						JsonObject jsonChild = JsonObject.empty();
							
						for (Node childNode : mapChildren.get(node)) {
							
							LOGGER.debug("Parsing child Node "+ childNode.getId());
							
							createJsonObject(db, childNode, jsonChild, mapChildren);
							
							LOGGER.debug("Adding " + childNode.getId());
							jsonObject.put(childNode.getLabels().iterator().next().name(), jsonChild);
						}
					}
				}
			}
		}
	}

	private void extractPropertyFromRelationship(Set<Object> setCouchbaseIds, Relationship relationshipWithAssignedProperties, GraphDatabaseService db) {
		
		try (Transaction tx = db.beginTx()) {
			
			Map<String, Object> allProperties = relationshipWithAssignedProperties.getAllProperties();
			
			for (String property : allProperties.keySet()) {
				
				if (property.equals(COUCHBASE_ID_PROPERTY_KEY)) {
					
					setCouchbaseIds.add(allProperties.get(property));
				}
			}
		}
	}

	private void extractPropertyFromNode(Set<Object> setCouchbaseIds, Node node, GraphDatabaseService db) {
		
		try (Transaction tx = db.beginTx()) {
			
			Map<String, Object> allProperties = node.getAllProperties();
			
			for (String property : allProperties.keySet()) {
				
				if (property.equals(COUCHBASE_ID_PROPERTY_KEY)) {
					
					setCouchbaseIds.add(allProperties.get(property));
				}
			}
		}
	}
}
