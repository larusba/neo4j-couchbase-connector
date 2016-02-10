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
package it.neo4j.integration.couchbase.event.handler.neo4j;

import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.event.LabelEntry;
import org.neo4j.graphdb.event.PropertyEntry;
import org.neo4j.graphdb.event.TransactionData;
import org.neo4j.graphdb.event.TransactionEventHandler;

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

	/**
	 * @see org.neo4j.graphdb.event.TransactionEventHandler#beforeCommit(org.neo4j.graphdb.event.TransactionData)
	 */
	public Void beforeCommit(TransactionData data) throws Exception {
		System.out.println("Transaction is about to be committed.");
		printTransactionData(data);
		return null;
	}

	/**
	 * @see org.neo4j.graphdb.event.TransactionEventHandler#afterCommit(org.neo4j.graphdb.event.TransactionData,
	 *      java.lang.Object)
	 */
	public void afterCommit(TransactionData data, Void state) {
		System.out.println("Transaction has been committed successfully.");
		printTransactionData(data);
	}

	/**
	 * @see org.neo4j.graphdb.event.TransactionEventHandler#afterRollback(org.neo4j.graphdb.event.TransactionData,
	 *      java.lang.Object)
	 */
	public void afterRollback(TransactionData data, Void state) {
		System.out.println("Transaction has rolled back for some reason.");
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
		System.out.println("Labels assigned:");
		Iterable<LabelEntry> assignedLabels = data.assignedLabels();
		for (LabelEntry assignedLabel : assignedLabels) {
			System.out.println(assignedLabel.label());
		}
	}

	/**
	 * 
	 * @param data
	 *            the data that has changed during the course of one transaction
	 */
	public void printNodePropertiesAssigned(TransactionData data) {
		System.out.println("Node properties assigned:");
		Iterable<PropertyEntry<Node>> assignedNodeProperties = data.assignedNodeProperties();
		for (PropertyEntry<Node> assignedNodeProperty : assignedNodeProperties) {
			System.out.println(assignedNodeProperty.key() + ": " + assignedNodeProperty.value());
		}
	}
}
