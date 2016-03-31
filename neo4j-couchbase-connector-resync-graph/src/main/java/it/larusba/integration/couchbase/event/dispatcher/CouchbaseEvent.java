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
package it.larusba.integration.couchbase.event.dispatcher;

import com.couchbase.client.core.message.CouchbaseMessage;
import com.couchbase.client.deps.com.lmax.disruptor.EventTranslatorOneArg;
import com.couchbase.client.deps.com.lmax.disruptor.RingBuffer;

/**
 * An event that transports a {@link CouchbaseMessage} with its assigned
 * <code>sequence</code> number.
 *
 * @author Lorenzo Speranzoni
 */

public class CouchbaseEvent {

	/**
	 * Default {@link EventTranslator} that can be used when you want to publish
	 * a {@link CouchbaseMessage} in the form of a {@link CouchbaseEvent} into
	 * the {@link RingBuffer}.
	 */
	public static final EventTranslatorOneArg<CouchbaseEvent, CouchbaseMessage> DEFAULT_COUCHBASE_MESSAGE_TRANSLATOR = new EventTranslatorOneArg<CouchbaseEvent, CouchbaseMessage>() {
		@Override
		public void translateTo(final CouchbaseEvent event, final long sequence, final CouchbaseMessage message) {
			event.setSequence(sequence).setMessage(message);
		}
	};

	/**
	 * The transported message 
	 */
	private CouchbaseMessage message;

	/**
	 * The message sequence number;
	 */
	private long sequence;

	/**
	 * Set the new message to be carried by this event.
	 *
	 * @param message
	 *            the message to carried.
	 * @return the {@link CouchbaseEvent} for method chaining.
	 */
	public CouchbaseEvent setMessage(final CouchbaseMessage message) {
		this.message = message;
		return this;
	}

	/**
	 * Get the current message transported by this event.
	 *
	 * @return the current message transported by this event.
	 */
	public CouchbaseMessage getMessage() {
		return message;
	}

	/**
	 * Set the new sequence to be carried by this event.
	 * 
	 * @param sequence
	 *            the sequence to be carried.
	 * @return the {@link CouchbaseEvent} for method chaining.
	 */
	public CouchbaseEvent setSequence(long sequence) {
		this.sequence = sequence;
		return this;
	}

	/**
	 * Get the current sequence transported by this event
	 *
	 * @return the current sequence transported by this event.
	 */
	public long getSequence() {
		return sequence;
	}
}
