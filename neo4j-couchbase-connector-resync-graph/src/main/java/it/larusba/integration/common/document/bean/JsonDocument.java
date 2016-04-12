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
package it.larusba.integration.common.document.bean;

import java.util.List;

import it.larusba.integration.common.document.mapping.JsonMappingStrategy;

/**
 *
 * @author Lorenzo Speranzoni
 */
public class JsonDocument {

	private String id;
	private String type;
	private String content;

	private JsonMappingStrategy mappingStrategy;

	private List<JsonObjectDescriptor> objectDescriptors;

	public JsonDocument() {
	}

	public JsonDocument(String id, String type, String content) {
		this.id = id;
		this.type = type;
		this.content = content;
	}

	public JsonDocument(String id, String type, String content, JsonMappingStrategy mappingStrategy,
			List<JsonObjectDescriptor> objectDescriptors) {
		this(id, type, content);
		this.mappingStrategy = mappingStrategy;
		this.objectDescriptors = objectDescriptors;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public JsonMappingStrategy getMappingStrategy() {
		return mappingStrategy;
	}

	public void setMappingStrategy(JsonMappingStrategy mappingStrategy) {
		this.mappingStrategy = mappingStrategy;
	}

	public List<JsonObjectDescriptor> getObjectDescriptors() {
		return objectDescriptors;
	}

	public void setObjectDescriptors(List<JsonObjectDescriptor> objectDescriptors) {
		this.objectDescriptors = objectDescriptors;
	}

	@Override
	public String toString() {
		return this.content;
	}
}
