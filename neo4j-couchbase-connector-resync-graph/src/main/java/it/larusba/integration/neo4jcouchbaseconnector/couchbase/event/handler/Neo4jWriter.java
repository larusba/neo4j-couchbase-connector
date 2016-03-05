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
package it.larusba.integration.neo4jcouchbaseconnector.couchbase.event.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.couchbase.client.core.message.dcp.MutationMessage;
import com.couchbase.client.core.message.dcp.RemoveMessage;
import com.couchbase.client.deps.com.lmax.disruptor.EventHandler;
import com.couchbase.client.deps.io.netty.buffer.ByteBuf;

import it.larusba.integration.neo4jcouchbaseconnector.couchbase.event.dispatcher.CouchbaseEvent;
import it.larusba.integration.neo4jcouchbaseconnector.couchbase.event.dispatcher.CouchbaseEventFilter;

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
