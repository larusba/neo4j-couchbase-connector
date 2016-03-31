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
