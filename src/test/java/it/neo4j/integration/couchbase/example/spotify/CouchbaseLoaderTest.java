package it.neo4j.integration.couchbase.example.spotify;

import org.junit.Assert;
import org.junit.Test;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.document.JsonDocument;

import it.neo4j.integration.couchbase.example.CouchbaseLoader;

public class CouchbaseLoaderTest {

	@Test
	public void loadJSONDocumentsTest() 
	{
		System.out.println("starting db...");
		Cluster cluster = CouchbaseCluster.create();
		System.out.println("creted db...");
		
		System.out.println("opening bucket...");
		Bucket defaultBucket = cluster.openBucket("default");
		System.out.println("opened bucket...");
		
		CouchbaseLoader loader = new CouchbaseLoader();
		
		loader.loadJson(cluster, defaultBucket);
		
		//Get JSON Document "albums"
		JsonDocument jsonDocument = defaultBucket.get("albums");
		
		cluster.disconnect();
		
		//Check if JSON Document "albums" is loaded
		Assert.assertNotNull(jsonDocument);
		Object items = jsonDocument.content().get("items");
		Assert.assertNotNull(items);
	}
}
