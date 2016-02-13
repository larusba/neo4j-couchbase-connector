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
package it.larusba.integration.neo4j_couchbase_connector.examples.spotify;

import org.junit.Assert;
import org.junit.Test;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.document.JsonDocument;

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
