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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.Test;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonArray;
import com.couchbase.client.java.document.json.JsonObject;

/**
 * @author Riccardo Birello
 */
public class SpotifyRecursiveLoaderTest {

	@Test
	public void test() throws IOException {
		System.out.println("Starting DB...");
		Cluster cluster = CouchbaseCluster.create();
		System.out.println("Created DB");
		System.out.println("Opening bucket...");
		Bucket bucket = cluster.openBucket("default");
		System.out.println("Opened bucket");

		int docs = 0;
		String artistId = "51Blml2LZPmy7TTiAg47vQ";
		String albumsUrl = "https://api.spotify.com/v1/artists/" + artistId + "/albums";
		System.out.println("Getting the artist's albums...");
		String albumsJson = this.getRequest(albumsUrl);
		System.out.println("Got the artist's albums...");
		docs++;
		JsonObject albumsObject = JsonObject.fromJson(albumsJson);
		String albumsId = albumsObject.getString("id");
		albumsId = albumsId == null ? "albums" + artistId : albumsId;
		JsonDocument albumsDocument = JsonDocument.create(albumsId, albumsObject);
		bucket.upsert(albumsDocument);
		JsonArray albums = albumsObject.getArray("items");
		for (Object albumObj : albums) {
			JsonObject item = (JsonObject) albumObj;
			String itemHref = item.getString("href");
			System.out.println("Getting the album...");
			String albumJson = this.getRequest(itemHref);
			System.out.println("Got the album...");
			docs++;
			JsonObject albumObject = JsonObject.fromJson(albumJson);
			String albumId = albumObject.getString("id");
			JsonDocument albumDocument = JsonDocument.create(albumId, albumObject);
			bucket.upsert(albumDocument);
			JsonArray tracks = albumObject.getObject("tracks").getArray("items");
			for (Object trackObj : tracks) {
				JsonObject track = (JsonObject) trackObj;
				String trackHref = track.getString("href");
				System.out.println("Getting the track...");
				String trackJson = this.getRequest(trackHref);
				System.out.println("Got the track...");
				docs++;
				JsonObject trackObject = JsonObject.fromJson(trackJson);
				String trackId = trackObject.getString("id");
				JsonDocument trackDocument = JsonDocument.create(trackId, trackObject);
				bucket.upsert(trackDocument);
			}
		}
		System.out.println("Total documents " + docs);
	}
	
	
	private String getRequest(String uri) throws ClientProtocolException, IOException {
		
		HttpClient httpClient = HttpClientBuilder.create().build();
		HttpGet getRequest = new HttpGet(uri);
		HttpResponse response = httpClient.execute(getRequest);
		
		System.out.println("Response Code : " + response.getStatusLine().getStatusCode());

		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

		StringBuffer jsonResult = new StringBuffer();
		String line = "";
		
		while ((line = rd.readLine()) != null) {
			jsonResult.append(line);
		}
		
		return jsonResult.toString();
	}
}
