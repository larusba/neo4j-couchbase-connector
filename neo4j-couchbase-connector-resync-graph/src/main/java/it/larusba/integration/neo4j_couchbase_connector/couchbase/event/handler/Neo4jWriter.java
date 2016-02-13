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
package it.larusba.integration.neo4j_couchbase_connector.couchbase.event.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.couchbase.client.core.message.dcp.MutationMessage;
import com.couchbase.client.core.message.dcp.RemoveMessage;
import com.couchbase.client.deps.com.lmax.disruptor.EventHandler;
import com.couchbase.client.deps.io.netty.buffer.ByteBuf;

import it.larusba.integration.neo4j_couchbase_connector.couchbase.event.dispatcher.CouchbaseEvent;
import it.larusba.integration.neo4j_couchbase_connector.couchbase.event.dispatcher.CouchbaseEventFilter;

/**
 * This class is responsible for filtering and routing events to the Neo4j in
 * the form of Cypher statements.
 *
 * @author Lorenzo Speranzoni
 */
public class Neo4jWriter implements EventHandler<CouchbaseEvent> {

	private static Logger LOGGER = LoggerFactory.getLogger(Neo4jWriter.class);

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
		
		LOGGER.debug("Document with key = '" + mutationMessage.key() + "' has been updated.");
		LOGGER.debug("New content is: ");
		ByteBuf buffer = mutationMessage.content();
		String content = "";
		for (int i = 0; i < buffer.capacity(); i++) {
			byte b = buffer.getByte(i);
			content += (char) b;
		}
		LOGGER.debug(content);
	}

	/**
	 * TODO convert message into a list of Cypher statemen(s and send them to
	 * Neo4j).
	 * 
	 * @param removeMessage
	 */
	private void buildAndSendRemoveCommandToNeo4j(RemoveMessage removeMessage) {
		
		LOGGER.debug("Document with key = '" + removeMessage.key() + "' has been removed.");
	}
}
