/**
 * Copyright (c) 2004-2016 LARUS Business Automation [http://www.larus-ba.it]
 *
 * This file is part of the "LARUS Integration Framework for Neo4j".
 *
 * The "LARUS Integration Framework for Neo4j" is free software: you can
 * redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 */
package it.larusba.integration.neo4jcouchbaseconnector.neo4j.event.handler;

import org.junit.Test;
import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.event.TransactionEventHandler;
import org.neo4j.test.TestGraphDatabaseFactory;

import it.larusba.integration.neo4jcouchbaseconnector.neo4j.event.handler.CouchbaseWriter;

/**
 * This test class registers a {@link TransactionEventHandler} that prints all
 * the events that has happened in each transaction through the
 * {@link CouchbaseWriter} implementation and creates a
 * {@link Node} with <code>Artist</code> {@link Label} and three properties
 * <code>firstName</code>, <code>secondName>/code> and <code>lastName</code>.
 * <p/>
 * When executed it should result in printing all that has changed during the
 * course of the transaction in the standard output data.
 * 
 * @author Lorenzo Speranzoni
 */
public class Neo4jEventHandlerTest {

	@Test
	public void shouldTraceChangesOnGraph() {

		GraphDatabaseService database = new TestGraphDatabaseFactory().newImpermanentDatabase();

		database.registerTransactionEventHandler(new CouchbaseWriter());

		try (Transaction tx = database.beginTx()) {

			Node vanGogh = database.createNode(DynamicLabel.label("Artist"));

			vanGogh.setProperty("firstName", "Vincent");
			vanGogh.setProperty("secondName", "Willem");
			vanGogh.setProperty("lastName", "Van Gogh");

			tx.success();
		}
	}
}
