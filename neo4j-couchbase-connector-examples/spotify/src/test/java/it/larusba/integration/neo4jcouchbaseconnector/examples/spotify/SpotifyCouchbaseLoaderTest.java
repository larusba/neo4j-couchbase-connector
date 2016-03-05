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
package it.larusba.integration.neo4jcouchbaseconnector.examples.spotify;

import org.junit.Assert;
import org.junit.Test;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.document.JsonDocument;

import it.larusba.integration.neo4jcouchbaseconnector.examples.spotify.SpotifyCouchbaseLoader;

/**
 *
 * @author Mauro Roiter
 */
public class SpotifyCouchbaseLoaderTest {

	@Test
	public void loadJSONDocumentsTest() 
	{
		System.out.println("starting db...");
		Cluster cluster = CouchbaseCluster.create();
		System.out.println("creted db...");
		
		System.out.println("opening bucket...");
		Bucket defaultBucket = cluster.openBucket("default");
		System.out.println("opened bucket...");
		
		SpotifyCouchbaseLoader loader = new SpotifyCouchbaseLoader();
		
		String u2ArtistId = "51Blml2LZPmy7TTiAg47vQ";
		
		loader.loadJson(defaultBucket, u2ArtistId);
		
		//Get JSON Document "albums"
		JsonDocument jsonDocument = defaultBucket.get("albums");
		
		cluster.disconnect();
		
		//Check if JSON Document "albums" is loaded
		Assert.assertNotNull(jsonDocument);
		Object items = jsonDocument.content().get("items");
		Assert.assertNotNull(items);
	}
}
