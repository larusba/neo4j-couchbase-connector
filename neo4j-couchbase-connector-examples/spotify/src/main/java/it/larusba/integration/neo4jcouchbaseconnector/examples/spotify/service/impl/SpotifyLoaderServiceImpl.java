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
package it.larusba.integration.neo4jcouchbaseconnector.examples.spotify.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.larusba.integration.neo4jcouchbaseconnector.examples.spotify.service.SpotifyLoaderService;

/**
 * This class implements the interface {@link SpotifyLoaderService}
 * and expose some methods for extracting data from Spotify endpoints.
 * In this example, methods extract data about the Irish rock band U2
 * 
 * @author Mauro Roiter
 */
public class SpotifyLoaderServiceImpl implements SpotifyLoaderService
{
	private static Logger LOGGER = LoggerFactory.getLogger(SpotifyLoaderServiceImpl.class);
	
	@Override
	public String getArtist(String artistId) 
	{
		String jsonResult = "";
		
		try {
			jsonResult = getRequest("https://api.spotify.com/v1/artists/"+artistId);
		} catch (IOException e) {
			LOGGER.error("Error while getting the Artist: " + e.getMessage());
		}

		return jsonResult.toString();
	}
	
	@Override
	public String getAlbums(String artistId) 
	{
		String jsonResult = "";
		
		try {
			jsonResult = getRequest("https://api.spotify.com/v1/artists/"+artistId+"/albums");
		} catch (IOException e) {
			LOGGER.error("Error while getting albums: " + e.getMessage());
		}
		
		return jsonResult;
	}
	
	@Override
	public String getRelatedArtists(String artistId)
	{
		String jsonResult = "";
		
		try {
			jsonResult = getRequest("https://api.spotify.com/v1/artists/"+artistId+"/related-artists");
		} catch (IOException e) {
			LOGGER.error("Error while getting related artists: " + e.getMessage());
		}
		
		return jsonResult;
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
