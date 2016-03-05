# Neo4j Couchbase Connector - Resync Neo4j Module

This module is part of the [Neo4j - Couchbase Connector](https://github.com/larusba/neo4j-couchbase-connector) project and the "LARUS Integration Framework for Neo4j".

**It is responsible for resynching Neo4j every time a Couchbase document is updated**. It accomplishes this task by implementing an event listener for Couchbase mutations. All mutation are converted into a proper Cypher statement that is submitted to Neo4j via REST API.

Please be aware that this project is still under development and at this very first stage it will work only for testing.

## Thank you

We'd also like to thank for their precious support:
* [Michael Hunger](https://twitter.com/mesirii), NEO TECHNOLOGY - Head of Developer Relations, Caretaker Neo4j Community
* [Michael Nitschinger](https://twitter.com/daschl), COUCHBASE - Lead Developer of the Couchbase Java SDK

## License

Copyright (c) 2016 http://www.larus-ba.it[LARUS Business Automation]

This file is part of the "LARUS Integration Framework for Neo4j".

The "LARUS Integration Framework for Neo4j" is licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.

You may obtain a copy of the License at
http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.

See the License for the specific language governing permissions and limitations under the License.