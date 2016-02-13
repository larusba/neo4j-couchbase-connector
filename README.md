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

## Sources of Inspiration

We'd like to thank the authors of:
* The [Neo4j - Mongo Connector](https://github.com/neo4j-contrib/neo4j_doc_manager), for the ideas for mapping the two different data models;
* The [Couchbase - Kafka Connector](http://developer.couchbase.com/documentation/server/4.1/connectors/kafka-1.2/kafka-intro.html), where we took inspiration on how to implement the mutation event dispatcher.

## Thank you

We'd also like to thank for their precious support:
* [Michael Hunger](https://twitter.com/mesirii), NEO TECHNOLOGY - Head of Developer Relations, Caretaker Neo4j Community
* [Michael Nitschinger](https://twitter.com/daschl), COUCHBASE - Lead Developer of the Couchbase Java SDK


## License

Copyright (c) 2013-16 [LARUS Business Automation S.r.l.](http://www.larus-ba.it)

"LARUS Integration Framework for Neo4j" is free software: you can redistribute it and/or
modify it under the terms of the GNU General Public License as published by the Free Software Foundation,
either version 3 of the License, or (at your option) any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
See the GNU General Public License for more details.
You should have received a copy of the GNU General Public License along with this program.
If not, see <http://www.gnu.org/licenses/>.