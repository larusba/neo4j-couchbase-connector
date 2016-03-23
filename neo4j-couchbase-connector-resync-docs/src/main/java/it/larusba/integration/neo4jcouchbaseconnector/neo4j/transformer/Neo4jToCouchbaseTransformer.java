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

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.event.LabelEntry;
import org.neo4j.graphdb.event.PropertyEntry;
import org.neo4j.graphdb.event.TransactionData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is responsible for transforming data from Neo4j Graph
 * into JSON documents for Couchbase
 * 
 * @author Mauro Roiter
 *
 */
public class Neo4jToCouchbaseTransformer {

	private static Logger LOGGER = LoggerFactory.getLogger(Neo4jToCouchbaseTransformer.class);
	
	private static final String COUCHBASE_ID_PROPERTY_KEY = "couchbaseId";
	
	public void transform(TransactionData data) {
		
		Set<Object> setCouchbaseIds = new HashSet<Object>();
		
		Iterable<Node> createdNodes = data.createdNodes();
		
		for (Node node : createdNodes) {
			
			extractPropertyFromNode(setCouchbaseIds, node);
		}
		
		Iterable<PropertyEntry<Node>> assignedNodeProperties = data.assignedNodeProperties();
		
		for (PropertyEntry<Node> propertyEntry : assignedNodeProperties) {
			
			Node nodeWithAssignedProperties = propertyEntry.entity();
			
			extractPropertyFromNode(setCouchbaseIds, nodeWithAssignedProperties);
		}
		
		Iterable<LabelEntry> assignedLabels = data.assignedLabels();
		
		for (LabelEntry labelEntry : assignedLabels) {
			
			Node nodeWithAssignedLabel = labelEntry.node();
			
			extractPropertyFromNode(setCouchbaseIds, nodeWithAssignedLabel);
		}
		
		Iterable<PropertyEntry<Relationship>> assignedRelationshipProperties = data.assignedRelationshipProperties();
		
		for (PropertyEntry<Relationship> propertyEntry : assignedRelationshipProperties) {
			
			Relationship relationshipWithAssignedProperties = propertyEntry.entity();
			
			extractPropertyFromRelationship(setCouchbaseIds, relationshipWithAssignedProperties);
		}
		
		Iterable<Relationship> createdRelationships = data.createdRelationships();
		
		for (Relationship relationship : createdRelationships) {
			
			extractPropertyFromRelationship(setCouchbaseIds, relationship);
		}
		
		Iterable<Node> deletedNodes = data.deletedNodes();
		
		for (Node node : deletedNodes) {
			
			extractPropertyFromNode(setCouchbaseIds, node);
		}
		
		Iterable<Relationship> deletedRelationships = data.deletedRelationships();
		
		for (Relationship relationship : deletedRelationships) {
			
			extractPropertyFromRelationship(setCouchbaseIds, relationship);	
		}
		
		Iterable<LabelEntry> removedLabels = data.removedLabels();
		
		for (LabelEntry labelEntry : removedLabels) {
			
			Node nodeWithRemovedLabel = labelEntry.node();
			
			extractPropertyFromNode(setCouchbaseIds, nodeWithRemovedLabel);	
		}
		
		Iterable<PropertyEntry<Node>> removedNodeProperties = data.removedNodeProperties();
		
		for (PropertyEntry<Node> propertyEntry : removedNodeProperties) {
			
			Node nodeWithRemovedProperties = propertyEntry.entity();
			
			extractPropertyFromNode(setCouchbaseIds, nodeWithRemovedProperties);
		}
		
		Iterable<PropertyEntry<Relationship>> removedRelationshipProperties = data.removedRelationshipProperties();
		
		for (PropertyEntry<Relationship> propertyEntry : removedRelationshipProperties) {
			
			Relationship relationshipWithRemovedProperties = propertyEntry.entity();
			
			extractPropertyFromRelationship(setCouchbaseIds, relationshipWithRemovedProperties);
		}
		
		LOGGER.debug("Couchbase ids");
		
		for (Object couchbaseId : setCouchbaseIds) {
			
			LOGGER.debug(couchbaseId.toString());
		}	
	}

	private void extractPropertyFromRelationship(Set<Object> setCouchbaseIds, Relationship relationshipWithAssignedProperties) {
		
		Map<String, Object> allProperties = relationshipWithAssignedProperties.getAllProperties();
		
		for (String property : allProperties.keySet()) {
			
			if (property.equals(COUCHBASE_ID_PROPERTY_KEY)) {
				
				setCouchbaseIds.add(allProperties.get(property));
			}
		}
	}

	private void extractPropertyFromNode(Set<Object> setCouchbaseIds, Node node) {
		
		Map<String, Object> allProperties = node.getAllProperties();
		
		for (String property : allProperties.keySet()) {
			
			if (property.equals(COUCHBASE_ID_PROPERTY_KEY)) {
				
				setCouchbaseIds.add(allProperties.get(property));
			}
		}
	}
}
