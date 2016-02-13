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
package it.larusba.integration.neo4jcouchbaseconnector.couchbase.event.dispatcher;

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
