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
package it.larusba.integration.neo4jcouchbaseconnector.neo4j.event.handler;

import java.util.List;

import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.event.LabelEntry;
import org.neo4j.graphdb.event.PropertyEntry;
import org.neo4j.graphdb.event.TransactionData;
import org.neo4j.graphdb.event.TransactionEventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.document.JsonDocument;

import it.larusba.integration.neo4jcouchbaseconnector.neo4j.transformer.Neo4jToCouchbaseTransformer;

/**
 * This class is responsible for filtering and routing events to the Couchbase
 * in the form of N1QL statements.
 * <p/>
 * PLEASE NOTICE: this class just meant to showcase how the
 * {@link TransactionEventHandler} works. <br/>
 * It implements a {@link TransactionEventHandler} that prints the data that has
 * changed during the course of one transaction. <br/>
 * TODO print all other info provided by the {@link TransactionData} parameter
 *
 * @author Lorenzo Speranzoni
 * 
 * @see TransactionEventHandler, TransactionData
 */
public class CouchbaseWriter implements TransactionEventHandler<Void> {

	private static Logger LOGGER = LoggerFactory.getLogger(CouchbaseWriter.class);
	
	/**
	 * @see org.neo4j.graphdb.event.TransactionEventHandler#beforeCommit(org.neo4j.graphdb.event.TransactionData)
	 */
	public Void beforeCommit(TransactionData data) throws Exception {
		LOGGER.debug("Transaction is about to be committed.");
//		printTransactionData(data);
		
		List<JsonDocument> jsonDocument = buildJSONDocument(data);
		
		// Connect to localhost
		Cluster cluster = CouchbaseCluster.create();
		
		// Open the default bucket 
		Bucket defaultBucket = cluster.openBucket();

		for (JsonDocument document : jsonDocument) {
			
			defaultBucket.upsert(document);
		}
		

		// Disconnect and clear all allocated resources
		cluster.disconnect();
		return null;
	}

	/**
	 * @see org.neo4j.graphdb.event.TransactionEventHandler#afterCommit(org.neo4j.graphdb.event.TransactionData,
	 *      java.lang.Object)
	 */
	public void afterCommit(TransactionData data, Void state) {
		LOGGER.debug("Transaction has been committed successfully.");
//		printTransactionData(data);
		buildJSONDocument(data);
	}

	/**
	 * @see org.neo4j.graphdb.event.TransactionEventHandler#afterRollback(org.neo4j.graphdb.event.TransactionData,
	 *      java.lang.Object)
	 */
	public void afterRollback(TransactionData data, Void state) {
		LOGGER.debug("Transaction has rolled back for some reason.");
		printTransactionData(data);
	}

	/**
	 * 
	 * @param data
	 *            the data that has changed during the course of one transaction
	 */
	public void printTransactionData(TransactionData data) {
		printLabelAssigned(data);
		printNodePropertiesAssigned(data);
	}

	/**
	 * It prints all new labels that have been assigned during the transaction.
	 * 
	 * @param data
	 *            the data that has changed during the course of one transaction
	 */
	public void printLabelAssigned(TransactionData data) {
		LOGGER.debug("Labels assigned:");
		Iterable<LabelEntry> assignedLabels = data.assignedLabels();
		for (LabelEntry assignedLabel : assignedLabels) {
			LOGGER.debug(assignedLabel.label().name());
		}
	}

	/**
	 * 
	 * @param data
	 *            the data that has changed during the course of one transaction
	 */
	public void printNodePropertiesAssigned(TransactionData data) {
		LOGGER.debug("Node properties assigned:");
		Iterable<PropertyEntry<Node>> assignedNodeProperties = data.assignedNodeProperties();
		for (PropertyEntry<Node> assignedNodeProperty : assignedNodeProperties) {
			LOGGER.debug(assignedNodeProperty.key() + ": " + assignedNodeProperty.value());
		}
	}
	
	/**
	 * 
	 * @param data
	 * 			  the data that has changed during the course of one transaction
	 */
	public List<JsonDocument> buildJSONDocument(TransactionData data) {

		LOGGER.debug("Starting transofrm data from Cypher to JSON...");
				
		Neo4jToCouchbaseTransformer transformer = new Neo4jToCouchbaseTransformer();
		
		transformer.transform(data);
		
		return null;
	}
}
