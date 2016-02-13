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

import org.junit.Test;

import it.larusba.integration.neo4jcouchbaseconnector.couchbase.document.transformer.DocumentToCyhperTransformer;
import it.larusba.integration.neo4jcouchbaseconnector.couchbase.document.transformer.DocumentTransformer;

/**
 *
 * @author Lorenzo Speranzoni
 */
public class DocumentToCyhperTransformerTest {

	@Test
	public void shouldTransformJsonDocumentIntoACypherStatement() {

		DocumentTransformer<String> documentTransformer = new DocumentToCyhperTransformer();

		String jsonPersonDocument = "{\"firstname\": \"Lorenzo\", \"lastname\": \"Speranzoni\", \"age\": 41, \"job\": \"CEO @ LARUS Business Automation\"}";
		
		System.out.println(documentTransformer.transform("inserpio", jsonPersonDocument));
	}

	@Test
	public void shouldTransformNestedJsonDocumentIntoACypherStatement() {
		
		DocumentTransformer<String> documentTransformer = new DocumentToCyhperTransformer();
		
		String jsonAddressDocument = "{\"street\": \"Via B. Maderna, 7\", \"zipCode\": 30174, \"city\": \"Mestre\", \"province\": \"Venice\", \"country\": \"Italy\"}";
		
		String jsonCompanyDocument = "{\"name\": \"LARUS Business Automation\", \"vat\": \"03540680273\", \"address\": " + jsonAddressDocument  + "}";
		
		String jsonJobDocument = "{\"role\": \"CEO\", \"company\": " + jsonCompanyDocument  + "}";
		
		String jsonPersonDocument = "{\"firstname\": \"Lorenzo\", \"lastname\": \"Speranzoni\", \"age\": 41, \"job\": " + jsonJobDocument + "}";
		
		System.out.println(documentTransformer.transform("inserpio", jsonPersonDocument));
	}
	
}
