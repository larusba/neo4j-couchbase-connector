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
