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
package it.larusba.integration.common.document.mapping;

/**
 *
 * @author Lorenzo Speranzoni
 * @since Mar 4, 2016
 */
public enum JsonMappingStrategy {

  /**
   * Domain agnostic approach: we don't provide any information about domain
   * entities and the set of attributes that uniquely identify their instances.
   * <p/>
   * When primitive, JSON attributes become node properties and their names are
   * used as property names.<br/>
   * When object, JSON attributes become new nodes (in a recursive fashion)
   * connected to their own father node.
   * <p/>
   * As an example, the following JSON document
   * 
   * <pre>
   * Person: {
   *   "firstname": "Lorenzo",
   *   "lastname": "Speranzoni",
   *   "age": 41,
   *   "job":
   *   {
   *   	"role": "CEO",
   *   	"company": "LARUS Business Automation"
   *   }
   * }
   * </pre>
   * 
   * will be translated into this sub-graph:
   * 
   * <pre>
   * (:Person { "firstname": "Lorenzo", ... } )-[:PERSON_JOB]->(:JOB { "role": "CEO", .... })
   * </pre>
   */
  ATTRIBUTE_BASED("attribute"),

  /**
   * This option allow us to instruct the transformer about how the domain is
   * defined.</br>
   */
  DOMAIN_DRIVEN("domain"),

  /**
   * Super flexible future implementation about transformation rules.
   * Unfortunately it hasn't been invented yet. :-)
   */
  FULLY_FLEXIBLE_BUT_NOT_YET_INVENTED("tbd");

  private String mappingStrategy;

  JsonMappingStrategy(String mappingStrategy) {
    this.mappingStrategy = mappingStrategy;
  }

  public String getMappingStrategy() {
    return mappingStrategy;
  }

  public static JsonMappingStrategy toEnum(String mappingStrategy) {
    for (JsonMappingStrategy value : JsonMappingStrategy.values()) {
      if (value.toString().equals(mappingStrategy)) {
        return value;
      }
    }
    return ATTRIBUTE_BASED;
  }

  @Override
  public String toString() {
    return this.mappingStrategy;
  }
}
