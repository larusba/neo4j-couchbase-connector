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
package it.larusba.integration.neo4jcouchbaseconnector.couchbase.document.transformer.bean;

/**
*
* @author Mauro Roiter
*/
public class JsonDocument {

	private String content;
	private String type;
	
	public JsonDocument() {
	}
	
	public JsonDocument(String content, String type) {
		this.content = content;
		this.type = type;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}

