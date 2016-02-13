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
package it.neo4j.integration.couchbase.example;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonArray;
import com.couchbase.client.java.document.json.JsonObject;

import it.neo4j.integration.couchbase.example.spotify.service.impl.SpotifyDataRetriever;

/**
 * A simple Couchbase loader that store some JSON documents
 * in a local Couchbase instance (version 4.0.0)
 * 
 * @author Riccardo Birello
 */
public class CouchbaseLoader
{
	private SpotifyDataRetriever dataRetriever = new SpotifyDataRetriever();
	
	public void loadJson(Cluster cluster, Bucket bucket) 
	{

		String jsonU2Artist = dataRetriever.getU2Artist();
		
		JsonObject json = JsonObject.fromJson(jsonU2Artist);
		JsonDocument docs = JsonDocument.create("artist", json);
		JsonDocument responses = bucket.upsert(docs);
		
		String jsonU2Albums = dataRetriever.getU2Albums();
		
		json = JsonObject.fromJson(jsonU2Albums);
		
//		JsonArray array = json.getArray("items");
//		Object object = array.getObject(0).get("name");
		
		docs = JsonDocument.create("albums", json);
		responses = bucket.upsert(docs);
		
		String jsonU2RelatedArtists = dataRetriever.getU2RelatedArtists();
		
		json = JsonObject.fromJson(jsonU2RelatedArtists);
		docs = JsonDocument.create("relArtists", json);
		responses = bucket.upsert(docs);
	}
	
}

