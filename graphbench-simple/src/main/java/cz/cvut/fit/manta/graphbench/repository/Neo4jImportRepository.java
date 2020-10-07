package cz.cvut.fit.manta.graphbench.repository;

import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.exceptions.ClientException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.neo4j.driver.Values.parameters;

/**
 * Neo4J Import Repository
 *
 * @author dbucek
 */
public class Neo4jImportRepository implements ImportRepository {
    private final static Logger LOGGER = LoggerFactory.getLogger(Neo4jImportRepository.class);

    private final static String NEO4J_URL = "neo4j://localhost:7687";
    private final static String NEO4J_USERNAME = "neo4j";
    private final static String NEO4J_PASSWORD = "admin";

    private final Driver driver;
    private final Session session;

    public Neo4jImportRepository() {
        driver = GraphDatabase.driver(NEO4J_URL, AuthTokens.basic(NEO4J_USERNAME, NEO4J_PASSWORD));
        session = driver.session();
    }

    @Override
    public void close() {
        session.close();
        driver.close();
    }

    @Override
    public void init() {
        // Create index
        Result run = session.run("CALL db.constraints()");
        if (!run.keys().contains("unique_personId_index")) {
            try {
                session.writeTransaction(tx -> {
                    tx.run("\n" +
                            "CREATE CONSTRAINT unique_personId_index\n" +
                            "ON (n:Person)\n" +
                            "ASSERT n.personId IS UNIQUE");

                    return 1;
                });
            } catch (ClientException e) {
                LOGGER.info("Constraint unique_personId_index already exists");
            }
        }

        // Check if contains any data
        LOGGER.info("Removing current data");
        session.run("MATCH (p) DETACH DELETE p");
        LOGGER.info("All data removed");
    }

    @Override
    public void storeVertices(String[][] vertices) {
        // Data import
        session.writeTransaction(tx -> {
            tx.run("\n" +
                    "UNWIND $csvLine AS line \n" +
                    "CREATE (n:Person { personId: line[0], name: line[1], year: toInteger(line[2])})", parameters("csvLine", vertices));

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
    public int getDefaultVertexBatchSize() {
        return 500_000;
    }

    @Override
    public int getDefaultEdgeBatchSize() {
        return 500_000;
    }
}
