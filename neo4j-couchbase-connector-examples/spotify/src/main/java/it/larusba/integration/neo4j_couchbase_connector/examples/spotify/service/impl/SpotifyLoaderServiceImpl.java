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
package it.larusba.integration.neo4j_couchbase_connector.examples.spotify.service.impl;

import org.springframework.web.client.RestTemplate;

import it.larusba.integration.neo4j_couchbase_connector.examples.spotify.service.SpotifyLoaderService;

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
