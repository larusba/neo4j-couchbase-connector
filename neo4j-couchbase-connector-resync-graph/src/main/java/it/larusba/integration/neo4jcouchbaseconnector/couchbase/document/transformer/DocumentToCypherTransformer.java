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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.couchbase.client.java.document.json.JsonObject;

/**
 * {@link DocumentTransformer} implementation converting a JSON document into a
 * Cypher statement
 * 
 * @author Lorenzo Speranzoni
 * 
 * @see <a href="http://neo4j.com/blog/cypher-load-json-from-url/">http://neo4j.
 *      com/blog/cypher-load-json-from-url/</a>
 */
public class DocumentToCypherTransformer implements DocumentTransformer<String> {

	/**
	 * @see it.larusba.integration.neo4jcouchbaseconnector.couchbase.document.transformer.DocumentTransformer#transform(java.lang.String)
	 */
	@Override
	public String transform(String documentKey, String documentType, String jsonDocument) {

		return transform(documentKey, documentType.toLowerCase(), JsonObject.fromJson(jsonDocument).toMap());
	}

	/**
	 * It recursively traverses nested map representing the json document
	 * 
	 * @param documentKey
	 * @param jsonAsMap
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private String transform(String documentKey, String documentType, Map<String, Object> documentMap) {

		StringBuffer rootNode = new StringBuffer();
		List<String> childNodes = new ArrayList<String>();
		List<String> childRelationships = new ArrayList<String>();

		rootNode.append("MERGE (").append(documentType).append(":").append(StringUtils.capitalize(documentType))
				.append(" { couchbaseId: '").append(documentKey).append("' })\n");

		boolean firstAttr = true;

		for (String attributeName : documentMap.keySet()) {

			Object attributeValue = documentMap.get(attributeName);

			if (attributeValue instanceof Map) {

				childNodes.add(transform(documentKey, (String) attributeName, (Map<String, Object>) attributeValue));

				childRelationships.add(new StringBuffer().append("MERGE (").append(documentType).append(")-[")
						.append(":").append(documentType.toUpperCase()).append("_").append(attributeName.toUpperCase())
						.append("]->(").append(attributeName).append(")").toString());
			} else {

				if (firstAttr) {
					rootNode.append("ON CREATE SET ");
					firstAttr = false;
				} else {
					rootNode.append(", ");
				}

				rootNode.append(documentType).append(".").append(attributeName).append(" = ");

				if (attributeValue instanceof String) {
					rootNode.append("'").append(attributeValue).append("'");
				} else {
					rootNode.append(attributeValue);
				}
			}
		}

		StringBuffer cypher = new StringBuffer();

		cypher.append(rootNode);

		for (String childNode : childNodes) {

			cypher.append("\n").append(childNode);
		}

		for (String childRelationship : childRelationships) {

			cypher.append("\n").append(childRelationship);
		}

		return cypher.toString();
	}
}
