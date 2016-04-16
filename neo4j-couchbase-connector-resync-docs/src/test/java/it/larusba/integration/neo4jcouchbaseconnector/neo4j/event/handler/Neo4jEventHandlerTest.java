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
package it.larusba.integration.neo4jcouchbaseconnector.neo4j.event.handler;

import java.sql.SQLException;

import org.junit.Test;
import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.event.TransactionEventHandler;
import org.neo4j.test.TestGraphDatabaseFactory;

/**
 * This test class registers a {@link TransactionEventHandler} that prints all
 * the events that has happened in each transaction through the
 * {@link Neo4jEventListener} implementation and creates a
 * {@link Node} with <code>Artist</code> {@link Label} and three properties
 * <code>firstName</code>, <code>secondName>/code> and <code>lastName</code>.
 * <p/>
 * When executed it should result in printing all that has changed during the
 * course of the transaction in the standard output data.
 * 
 * @author Lorenzo Speranzoni
 */
public class Neo4jEventHandlerTest {

	@Test
	public void shouldTraceChangesOnGraph() {

		GraphDatabaseService database = new TestGraphDatabaseFactory().newImpermanentDatabase();

		database.registerTransactionEventHandler(new Neo4jEventListener(database));

		try (Transaction tx = database.beginTx()) {

			Node vanGogh = database.createNode(DynamicLabel.label("Artist"));

			vanGogh.setProperty("firstName", "Vincent");
			vanGogh.setProperty("secondName", "Willem");
			vanGogh.setProperty("lastName", "Van Gogh");

			tx.success();
		}
	}
	
	@Test
	public void shouldTransformCypherToJSONDoc() throws SQLException {
		
		String cypherStatement = "MERGE (person:Person { firstname: 'Lorenzo' }) "
							   + "SET person.documentIds = ['documentKey'], "
							   + "person.birthdate = '01/04/1974', "
							   + "person.lastname = 'Speranzoni', "
							   + "person.age = 41, "
							   + "person.job = 'CEO @ LARUS Business Automation'"
							   + "RETURN person";
		
		String cypherStatement2 = "MERGE (person:Person { firstname: 'Mauro' }) "
				   + "SET person.documentIds = ['documentKey'], "
				   + "person.birthdate = '19/09/1986', "
				   + "person.lastname = 'Roiter', "
				   + "person.age = 29, "
				   + "person.job = 'Developer @ LARUS Business Automation'"
				   + "RETURN person";
		
		String cypherStatement4 = "MERGE (person:Person { firstname: 'Riccardo' }) "
				   + "SET person.documentIds = ['documentKey'], "
				   + "person.birthdate = '02/09/1985', "
				   + "person.lastname = 'Birello', "
				   + "person.age = 30, "
				   + "person.job = 'Developer @ LARUS Business Automation'"
				   + "RETURN person";
		
		String cypherStatement6 = "MERGE (person:Person { firstname: 'Marco' }) "
				   + "SET person.documentIds = ['documentKey'], "
				   + "person.birthdate = '01/05/1988', "
				   + "person.lastname = 'Falcier', "
				   + "person.age = 27, "
				   + "person.job = 'Developer @ LARUS Business Automation'"
				   + "RETURN person";
		
		String cypherStatement7 = "MERGE (company:Company { name: 'LARUS Business Automation' }) "
				   + "SET company.documentIds = ['documentKey'], "
				   + "company.city = 'Venezia', "
				   + "company.website = 'www.larus-ba.it', "
				   + "company.services = ['Sviluppo Software Custom', 'Consulenza e Coaching', 'Scuola di formazione']"
				   + "RETURN company";
		
		String cypherStatement3 = "MATCH (a:Person),(b:Person) "
		+ "WHERE a.firstname = 'Lorenzo' AND b.firstname = 'Mauro' "
		+ "MERGE (a)-[r:COLLEAGUE { documentIds : ['documentKey'], property: 'colleagues', depth: [1] }]->(b) "
		+ "RETURN r";
		
		String cypherStatement5 = "MATCH (a:Person),(b:Person) "
				+ "WHERE a.firstname = 'Mauro' AND b.firstname = 'Riccardo' "
				+ "MERGE (a)-[r:COLLEAGUE { documentIds : ['documentKey'], property: 'colleagues', depth: [2] }]->(b) "
				+ "RETURN r";
		
		String cypherStatement8 = "MATCH (a:Person),(b:Company) "
				+ "WHERE a.firstname = 'Lorenzo' AND b.name = 'LARUS Business Automation' "
				+ "MERGE (a)-[r:CEO { documentIds : ['documentKey'], property: 'company', depth: [1] }]->(b) "
				+ "RETURN r";
		
		String cypherStatement9 = "MATCH (a:Person),(b:Person) "
				+ "WHERE a.firstname = 'Lorenzo' AND b.firstname = 'Marco' "
				+ "MERGE (a)-[r:COLLEAGUE { documentIds : ['documentKey'], property: 'colleagues', depth: [1] }]->(b) "
				+ "RETURN r";
		
		GraphDatabaseService database = new TestGraphDatabaseFactory().newImpermanentDatabase();
//		GraphDatabaseService database = new GraphDatabaseFactory().newEmbeddedDatabase(new File("/Applications/Development/Neo4j-2.3.2/neo4j-community-2.3.2/data/graph.db"));
		
		database.registerTransactionEventHandler(new Neo4jEventListener(database));

		try (Transaction tx = database.beginTx()) {

			database.execute(cypherStatement);
			database.execute(cypherStatement2);
			database.execute(cypherStatement3);
			database.execute(cypherStatement4);
			database.execute(cypherStatement5);
			database.execute(cypherStatement6);
			database.execute(cypherStatement7);
			database.execute(cypherStatement8);
			database.execute(cypherStatement9);
			
			tx.success();
		}
	}
}
