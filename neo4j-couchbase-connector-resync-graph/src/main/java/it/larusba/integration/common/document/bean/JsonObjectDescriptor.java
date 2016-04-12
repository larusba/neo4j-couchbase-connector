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

/**
 *
 * @author Lorenzo Speranzoni
 */
public class JsonObjectDescriptor {

  private String entityName;

  private List<String> uniqueKeyAttributes;

  private String typeAttribute;

  public JsonObjectDescriptor() {
  }

  public JsonObjectDescriptor(String entityName, List<String> uniqueKeyAttributes, String typeAttribute) {
    this.entityName = entityName;
    this.uniqueKeyAttributes = uniqueKeyAttributes;
    this.typeAttribute = typeAttribute;
  }

  public String getEntityName() {
    return entityName;
  }

  public void setEntityName(String entityName) {
    this.entityName = entityName;
  }

  public List<String> getUniqueKeyAttributes() {
    return uniqueKeyAttributes;
  }

  public void setUniqueKeyAttributes(List<String> uniqueAttributes) {
    this.uniqueKeyAttributes = uniqueAttributes;
  }

  public String getTypeAttribute() {
    return typeAttribute;
  }

  public void setTypeAttribute(String typeAttribute) {
    this.typeAttribute = typeAttribute;
  }

	@Override
	public String toString() {
		return "JsonObjectDescriptor [entityName=" + entityName + ", uniqueKeyAttributes=" + uniqueKeyAttributes
		    + ", typeAttribute=" + typeAttribute + "]";
	}
}
