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
package it.neo4j.integration.couchbase.event.handler.couchbase;

import com.couchbase.client.core.message.dcp.MutationMessage;
import com.couchbase.client.core.message.dcp.RemoveMessage;
import com.couchbase.client.deps.com.lmax.disruptor.EventHandler;
import com.couchbase.client.deps.io.netty.buffer.ByteBuf;

import it.neo4j.integration.couchbase.event.dispatcher.couchbase.CouchbaseEvent;
import it.neo4j.integration.couchbase.event.dispatcher.couchbase.CouchbaseEventFilter;

/**
 * This class is responsible for filtering and routing events to the Neo4j in
 * the form of Cypher statements.
 *
 * @author Lorenzo Speranzoni
 */
public class Neo4jWriter implements EventHandler<CouchbaseEvent> {

	public Neo4jWriter() {
	}

	/**
	 * Handles {@link CouchbaseEvent}s that come into the response RingBuffer.
	 */
	@Override
	public void onEvent(final CouchbaseEvent event, final long sequence, final boolean endOfBatch) throws Exception {

		if (CouchbaseEventFilter.accept(event)) {
			if (event.getMessage() instanceof MutationMessage) {
				buildAndSendMutationCommandToNeo4j((MutationMessage) event.getMessage());
			} else if (event.getMessage() instanceof RemoveMessage) {
				buildAndSendRemoveCommandToNeo4j((RemoveMessage) event.getMessage());
			}
		}
	}

	/**
	 * TODO convert message into a list of Cypher statemen(s and send them to
	 * Neo4j).
	 * 
	 * @param mutationMessage
	 */
	private void buildAndSendMutationCommandToNeo4j(MutationMessage mutationMessage) {
		
		System.out.println("Document with key = " + mutationMessage.key() + " has been updated.");
		System.out.println("New content is: ");
		ByteBuf buffer = mutationMessage.content();
		for (int i = 0; i < buffer.capacity(); i++) {
			byte b = buffer.getByte(i);
			System.out.print((char) b);
		}
		System.out.println();
	}

	/**
	 * TODO convert message into a list of Cypher statemen(s and send them to
	 * Neo4j).
	 * 
	 * @param removeMessage
	 */
	private void buildAndSendRemoveCommandToNeo4j(RemoveMessage removeMessage) {
		
		System.out.println("Document with key = " + removeMessage.key() + " has been removed.");
	}
}