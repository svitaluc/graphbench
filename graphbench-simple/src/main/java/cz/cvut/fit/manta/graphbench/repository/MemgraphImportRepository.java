package cz.cvut.fit.manta.graphbench.repository;

import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.neo4j.driver.v1.Values.parameters;

public class MemgraphImportRepository implements ImportRepository {
    private final static Logger LOGGER = LoggerFactory.getLogger(MemgraphImportRepository.class);

    private final static String MEMGRAPH_URL = "bolt://localhost:7687";
    private final static String MEMGRAPH_USERNAME = "";
    private final static String MEMGRAPH_PASSWORD = "";

    private final Driver driver;
    private final Session session;

    public MemgraphImportRepository() {
        driver = GraphDatabase.driver(MEMGRAPH_URL, AuthTokens.basic(MEMGRAPH_USERNAME, MEMGRAPH_PASSWORD));
        session = driver.session();
    }

    @Override
    public void init() {
        session.run("CREATE CONSTRAINT ON (n:Person) ASSERT n.personId IS UNIQUE");
        session.run("CREATE INDEX ON :Person(personId)");
    }

    @Override
    public void storeVertices(String[][] vertices) {
        session.writeTransaction(tx -> {
            tx.run("\n" +
                            "UNWIND $csvLine AS line \n" +
                            "CREATE (n:Person { personId: line[0], name: line[1], year: toInteger(line[2])})",
                    parameters("csvLine", vertices));
            return 1;
        });
    }

    @Override
    public void storeEdges(String[][] edges) {
        session.writeTransaction(tx -> {
            tx.run("\n" +
                    "UNWIND $csvLine AS line \n" +
                    "MATCH (personFrom:Person {personId: line[0]}),(personTo:Person {personId: line[1]})\n" +
                    "CREATE (personFrom)-[:IS_FRIEND]->(personTo)\n", parameters("csvLine", edges));

            return 1;
        });
    }

    @Override
    public void close() {
        session.close();
        driver.close();
    }

    @Override
    public int getDefaultVertexBatchSize() {
        return 500_000;
    }

    @Override
    public int getDefaultEdgeBatchSize() {
        return 500_000;
    }
}