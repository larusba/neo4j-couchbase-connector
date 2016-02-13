# Neo4j Couchbase Connector - Resync Neo4j Module

This module is part of the [Neo4j - Couchbase Connector](https://github.com/larusba/neo4j-couchbase-connector) project and the "LARUS Integration Framework for Neo4j".

**It is responsible for resynching Neo4j every time a Couchbase document is updated**. It accomplishes this task by implementing an event listener for Couchbase mutations. All mutation are converted into a proper Cypher statement that is submitted to Neo4j via REST API.

Please be aware that this project is still under development and at this very first stage it will work only for testing.

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