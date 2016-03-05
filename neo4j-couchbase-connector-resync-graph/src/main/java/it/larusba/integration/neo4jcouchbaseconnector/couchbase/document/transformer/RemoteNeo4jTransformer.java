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
package it.larusba.integration.neo4jcouchbaseconnector.couchbase.document.transformer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.ws.rs.core.MediaType;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONObject;

import it.larusba.integration.neo4jcouchbaseconnector.couchbase.document.transformer.bean.JsonDocument;

/**
 * {@link DocumentTransformer} a REST call to a Neo4j Server Extension which is responsible
 * for the conversion of a JSON string to a Cypher statement
 * 
 * @author Mauro Roiter
 */
public class RemoteNeo4jTransformer implements DocumentTransformer<String> {
	
	private InputStream inputStream;
	
	@Override
	public String transform(String documentKey, String documentType, String jsonDocument) {

		String returnMessage = "JSON successfully transformed";
		
		try {
			
			transformJson2Cypher(documentType, jsonDocument);
		} catch (IOException e) {
			
			returnMessage = "Error during JSON transformation";
			e.printStackTrace();
		}
		
		return returnMessage;
	}
	
	/**
	 * It perform the REST call to the URL configured in the transofrmer.properties file
	 * @throws IOException 
	 */
	private void transformJson2Cypher(String documentType, String jsonDocument) throws IOException{
		
		Properties prop = new Properties();
		String propFileName = "transformer.properties";

		inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);

		if (inputStream != null) {
			prop.load(inputStream);
		} else {
			throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
		}

		// get the URL property value
		String urlJson2Cypher = prop.getProperty("transformer.url");
		
		JsonDocument documentToTransform = new JsonDocument(jsonDocument, documentType);
		
		HttpClient httpClient = HttpClientBuilder.create().build();
		
		HttpPut putRequest = new HttpPut(urlJson2Cypher);
		putRequest.addHeader("content-type", MediaType.APPLICATION_JSON);

		JSONObject json = new JSONObject(documentToTransform);
		
		StringEntity entity = new StringEntity(json.toString());
		
		putRequest.setEntity(entity);
		
		HttpResponse response = httpClient.execute(putRequest);
		
		if (response.getStatusLine().getStatusCode() != 200) {
			
            throw new RuntimeException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
        }
	}
}