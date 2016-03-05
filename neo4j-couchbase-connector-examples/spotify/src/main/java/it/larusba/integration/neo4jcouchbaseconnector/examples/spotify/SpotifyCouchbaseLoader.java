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

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonObject;

import it.larusba.integration.neo4jcouchbaseconnector.examples.spotify.service.SpotifyLoaderService;
import it.larusba.integration.neo4jcouchbaseconnector.examples.spotify.service.impl.SpotifyLoaderServiceImpl;

/**
 * A simple Couchbase loader that store some JSON documents
 * in a local Couchbase instance (version 4.0.0)
 * 
 * @author Riccardo Birello
 */
public class SpotifyCouchbaseLoader
{
	public void loadJson(Bucket bucket, String artistId) 
	{
		SpotifyLoaderService loaderService = new SpotifyLoaderServiceImpl();
		
		String jsonU2Artist = loaderService.getArtist(artistId);
		
		JsonObject json = JsonObject.fromJson(jsonU2Artist);
		JsonDocument docs = JsonDocument.create("artist", json);
		bucket.upsert(docs);
		
		String jsonU2Albums = loaderService.getAlbums(artistId);
		
		json = JsonObject.fromJson(jsonU2Albums);
		docs = JsonDocument.create("albums", json);
		bucket.upsert(docs);
		
		String jsonU2RelatedArtists = loaderService.getRelatedArtists(artistId);
		
		json = JsonObject.fromJson(jsonU2RelatedArtists);
		docs = JsonDocument.create("relArtists", json);
		bucket.upsert(docs);
	}
}

