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
package it.larusba.integration.couchbase.document.transformer;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.larusba.integration.common.document.bean.JsonDocument;

/**
 * {@link DocumentTransformer} a REST call to a Neo4j Server Extension which is
 * responsible for the conversion of a JSON string to a Cypher statement
 * 
 * @author Mauro Roiter
 */
public class RemoteNeo4jTransformer implements DocumentTransformer<String> {

	private static Logger LOGGER = LoggerFactory.getLogger(RemoteNeo4jTransformer.class);

	private InputStream inputStream;

	@Override
	public String transform(String documentKey, String documentType, String jsonDocument) {

		String returnMessage = "JSON successfully transformed";

		try {

			transformJson2Cypher(documentKey, documentType, jsonDocument);
		} catch (IOException e) {

			returnMessage = "Error during JSON transformation";
			LOGGER.error("Error during JSON transformation: " + e.getMessage());
		}

		return returnMessage;
	}

	/**
	 * It perform the REST call to the URL configured in the
	 * transofrmer.properties file
	 * 
	 * @throws IOException
	 */
	private void transformJson2Cypher(String documentKey, String documentType, String jsonDocument) throws IOException {

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

		JsonDocument documentToTransform = new JsonDocument(documentKey, jsonDocument, documentType);

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
