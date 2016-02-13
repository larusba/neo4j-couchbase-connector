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
package it.larusba.integration.neo4jcouchbaseconnector.couchbase.event.handler;

import java.util.Arrays;

import org.junit.Test;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonObject;
import com.couchbase.client.java.env.CouchbaseEnvironment;
import com.couchbase.client.java.env.DefaultCouchbaseEnvironment;

import it.larusba.integration.neo4jcouchbaseconnector.couchbase.event.dispatcher.CouchbaseEventDispatcher;
import it.larusba.integration.neo4jcouchbaseconnector.couchbase.event.handler.Neo4jWriter;

/**
 * Test class to check whether the event handler
 * {@link CouchbaseEventDispatcher} is capturing Couchbase mutations.
 *
 * @author Lorenzo Speranzoni
 */
public class CouchbaseEventHandlerTest {

	@Test
	public void shouldTraceChangesOnCouchbase() {

		CouchbaseEnvironment env = DefaultCouchbaseEnvironment.builder().dcpEnabled(true).build();

		Cluster cluster = CouchbaseCluster.create(env);

		CouchbaseEventDispatcher couchbaseEventHandler = new CouchbaseEventDispatcher(cluster.core(), "default",
				Arrays.asList("127.0.0.1"), "", new Neo4jWriter());
		couchbaseEventHandler.start();

		Bucket defaultBucket = cluster.openBucket();

		JsonObject user = JsonObject.empty().put("firstname", "Lorenzo").put("lastname", "Speranzoni")
				.put("job", "CEO @ LARUS Business Automation").put("age", 41);

		JsonDocument doc = JsonDocument.create("inserpio", user);

		JsonDocument response = defaultBucket.upsert(doc);

		System.out.println(response);

		cluster.disconnect();
	}
}
