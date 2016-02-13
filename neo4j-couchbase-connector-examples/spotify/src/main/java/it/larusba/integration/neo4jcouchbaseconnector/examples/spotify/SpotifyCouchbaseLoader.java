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

