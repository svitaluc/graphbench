package cz.cvut.fit.manta.graphbench.repository;

import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;
import java.util.stream.Collectors;

import static org.neo4j.driver.Values.parameters;

/**
 * Neo4J Import Repository
 *
 * @author dbucek
 */
public class Neo4jQueryRepository implements QueryRepository {
    private final static Logger LOGGER = LoggerFactory.getLogger(Neo4jQueryRepository.class);

    private final static String NEO4J_URL = "neo4j://localhost:7687";
    private final static String NEO4J_USERNAME = "neo4j";
    private final static String NEO4J_PASSWORD = "admin";

    private final Driver driver;
    private final Session session;

    public Neo4jQueryRepository() {
        driver = GraphDatabase.driver(NEO4J_URL, AuthTokens.basic(NEO4J_USERNAME, NEO4J_PASSWORD));
        session = driver.session();
    }

    @Override
    public void close() {
        session.close();
        driver.close();
    }

    @Override
    public Set<String> queryDirect(String[][] vertices) {
        return session.readTransaction(tx -> {
            Result result = tx.run("\n" +
                            "UNWIND $csvLine AS line \n" +
                            "MATCH (p:Person)\n" +
                            "WHERE p.personId = line[0]\n" +
                            "RETURN p",
                    parameters("csvLine", vertices));

            return result.stream()
                    .map(r -> r.get(0).get("personId").asString())
                    .collect(Collectors.toSet());
        });
    }

    @Override
    public Set<String> queryTraverse(String[][] vertices) {
        return session.readTransaction(tx -> {
            Result result = tx.run("\n" +
                            "UNWIND $csvLine AS line \n" +
                            "MATCH (p1:Person)-[:IS_FRIEND]->(p2)\n" +
                            "WHERE p1.personId = line[0]\n" +
                            "RETURN p2",
                    parameters("csvLine", vertices));

            return result.stream()
                    .map(r -> r.get(0).get("personId").asString())
                    .collect(Collectors.toSet());
        });
    }
}
