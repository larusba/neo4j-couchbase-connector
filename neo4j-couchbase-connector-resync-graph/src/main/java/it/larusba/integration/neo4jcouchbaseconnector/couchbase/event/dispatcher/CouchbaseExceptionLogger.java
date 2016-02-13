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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.couchbase.client.deps.com.lmax.disruptor.ExceptionHandler;
import com.couchbase.client.deps.com.lmax.disruptor.dsl.Disruptor;
/**
 * {@link ExceptionHandler} that just logs the exception happening while
 * {@link CouchbaseEvent}s are managed by the {@link Disruptor}.
 * 
 * PLEASE NOTICE! Just logging is probably not enough!
 * 
 * @author Lorenzo Speranzoni
 */
public class CouchbaseExceptionLogger implements ExceptionHandler<Object> {

	private static Logger LOGGER = LoggerFactory.getLogger(CouchbaseExceptionLogger.class);

	@Override
	public void handleEventException(Throwable ex, long sequence, Object event) {
		LOGGER.error("Exception happended while managing event " + event + " with sequence number " + sequence, ex);
	}

	@Override
	public void handleOnStartException(Throwable ex) {
		LOGGER.error("Exception happended while boostrapping the event handler", ex);
	}

	@Override
	public void handleOnShutdownException(Throwable ex) {
		LOGGER.error("Exception happended while shutting down the event handler", ex);
	}
}
