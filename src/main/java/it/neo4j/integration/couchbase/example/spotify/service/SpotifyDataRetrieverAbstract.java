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
package it.neo4j.integration.couchbase.example.spotify.service;

/**
 * A simple interface for the service that retrieve data
 * from the Spotify endpoints
 * 
 * @author Mauro Roiter
 */
public interface SpotifyDataRetrieverAbstract 
{
	public String getU2Artist();

	public String getU2Albums();

	public String getU2RelatedArtists();
}
