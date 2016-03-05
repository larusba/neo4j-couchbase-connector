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

import org.springframework.web.client.RestTemplate;

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
	public RestTemplate restTemplate = new RestTemplate();
	
	@Override
	public String getArtist(String artistId) 
	{
		String jsonResult = restTemplate.getForObject("https://api.spotify.com/v1/artists/"+artistId, String.class);
		
		return jsonResult;
	}
	
	@Override
	public String getAlbums(String artistId) 
	{
		String jsonResult = restTemplate.getForObject("https://api.spotify.com/v1/artists/"+artistId+"/albums", String.class);
		
		return jsonResult;
	}
	
	@Override
	public String getRelatedArtists(String artistId)
	{
		String jsonResult = restTemplate.getForObject("https://api.spotify.com/v1/artists/"+artistId+"/related-artists", String.class);
		
		return jsonResult;
	}
}
