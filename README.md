# Neo4j Couchbase Connector

Welcome to the [Neo4j](http://neo4j.com/) - [Couchbase](http://couchbase.com/) Connector project.  It provides functionality to direct a stream of events from Neo4j to Couchbase and viceversa.

This project is part of the "LARUS Integration Framework for Neo4j".

Please be aware that this project is still under development and at this very first stage it will work only for testing.

## Modules

Current implementation provides 2 main modules:

* [Resync Couchbase Module](./neo4j-couchbase-connector-resync-docs): responsible for resynching Couchbase every time the Neo4j graph is updated;
* [Resync Neo4j Module](./neo4j-couchbase-connector-resync-graph): responsible for resynching Neo4j every time a Couchbase document is updated;

and 1 examples folder:

* [Examples](neo4j-couchbase-connector-examples): containing a set of examples showing how to use the [Neo4j - Couchbase Connector](https://github.com/larusba/neo4j-couchbase-connector). 

<center>
<img src="https://raw.githubusercontent.com/larusba/neo4j-couchbase-connector/master/neo4j-couchbase-connector-architecture.png" width="500" />
</center>

## Sources of Inspiration

We'd like to thank the authors of:
* The [Neo4j - Mongo Connector](https://github.com/neo4j-contrib/neo4j_doc_manager), for the ideas for mapping the two different data models;
* The [Couchbase - Kafka Connector](http://developer.couchbase.com/documentation/server/4.1/connectors/kafka-1.2/kafka-intro.html), where we took inspiration on how to implement the mutation event dispatcher.

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