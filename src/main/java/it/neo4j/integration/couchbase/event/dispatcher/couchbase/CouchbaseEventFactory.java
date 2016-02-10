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
package it.neo4j.integration.couchbase.event.dispatcher.couchbase;

import com.couchbase.client.deps.com.lmax.disruptor.EventFactory;

/**
 * Factory class for {@link CouchbaseEvent}s.
 *
 * @author Lorenzo Speranzoni
 * 
 * @see EventFactory
 */
public class CouchbaseEventFactory implements EventFactory<CouchbaseEvent> {
	
    @Override
    public CouchbaseEvent newInstance() {
        return new CouchbaseEvent();
    }
}
