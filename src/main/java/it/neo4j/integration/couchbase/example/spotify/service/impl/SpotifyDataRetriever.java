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
package it.neo4j.integration.couchbase.example.spotify.service.impl;

import org.springframework.web.client.RestTemplate;

import it.neo4j.integration.couchbase.example.spotify.service.SpotifyDataRetrieverAbstract;

/**
 * This class implements the interface {@link SpotifyDataRetrieverAbstract}
 * and expose some methods for extracting data from Spotify endpoints.
 * In this example, methods extract data about the Irish rock band U2
 * 
 * @author Mauro Roiter
 */
public class SpotifyDataRetriever implements SpotifyDataRetrieverAbstract
{
	public RestTemplate restTemplate = new RestTemplate();
	
	@Override
	public String getU2Artist() 
	{
		String artistId = "51Blml2LZPmy7TTiAg47vQ";
		
		String jsonResult = restTemplate.getForObject("https://api.spotify.com/v1/artists/"+artistId, String.class);
		
		return jsonResult;
	}
	
	@Override
	public String getU2Albums() 
	{
		String artistId = "51Blml2LZPmy7TTiAg47vQ";
		
		String jsonResult = restTemplate.getForObject("https://api.spotify.com/v1/artists/"+artistId+"/albums", String.class);
		
		return jsonResult;
	}
	
	@Override
	public String getU2RelatedArtists()
	{
		String artistId = "51Blml2LZPmy7TTiAg47vQ";
		
		String jsonResult = restTemplate.getForObject("https://api.spotify.com/v1/artists/"+artistId+"/related-artists", String.class);
		
		return jsonResult;
	}
}
