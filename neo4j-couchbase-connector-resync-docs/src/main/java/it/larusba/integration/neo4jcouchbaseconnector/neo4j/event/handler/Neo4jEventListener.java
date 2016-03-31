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

import org.neo4j.graphdb.GraphDatabaseService;
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
 * @author Lorenzo Speranzoni, Mauro Roiter
 * 
 * @see TransactionEventHandler, TransactionData
 */
public class Neo4jEventListener implements TransactionEventHandler<Void> {

	private static Logger LOGGER = LoggerFactory.getLogger(Neo4jEventListener.class);

	private GraphDatabaseService db;

	public Neo4jEventListener(GraphDatabaseService db) {

		this.db = db;
	}

	/**
	 * @see org.neo4j.graphdb.event.TransactionEventHandler#beforeCommit(org.neo4j.graphdb.event.TransactionData)
	 */
	public Void beforeCommit(TransactionData data) throws Exception {
		return null;
	}

	/**
	 * @see org.neo4j.graphdb.event.TransactionEventHandler#afterCommit(org.neo4j.graphdb.event.TransactionData,
	 *      java.lang.Object)
	 */
	public void afterCommit(TransactionData data, Void state) {

		List<JsonDocument> jsonDocuments = buildJSONDocument(data);

		if (jsonDocuments != null) {
			
			sendJSONDocumentsToCouchbase(jsonDocuments);
		}
	}

	/**
	 * @see org.neo4j.graphdb.event.TransactionEventHandler#afterRollback(org.neo4j.graphdb.event.TransactionData,
	 *      java.lang.Object)
	 */
	public void afterRollback(TransactionData data, Void state) {
	}

	/**
	 * 
	 * @param data
	 *          the data that has changed during the course of one transaction
	 */
	public List<JsonDocument> buildJSONDocument(TransactionData data) {

		LOGGER.debug("Starting transofrm data from Cypher to JSON...");

		Neo4jToCouchbaseTransformer transformer = new Neo4jToCouchbaseTransformer();

		transformer.transform(data, this.db);

		return null;
	}

	/**
	 * 
	 * @param jsonDocuments
	 */
	public void sendJSONDocumentsToCouchbase(List<JsonDocument> jsonDocuments) {
		
		Cluster cluster = CouchbaseCluster.create();

		Bucket defaultBucket = cluster.openBucket();

		for (JsonDocument jsonDocument : jsonDocuments) {

			defaultBucket.upsert(jsonDocument);
		}

		cluster.disconnect();
	}
}
