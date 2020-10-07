package cz.cvut.fit.manta.graphbench.repository;

import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

import static org.neo4j.driver.v1.Values.parameters;

/**
 * Neo4J Import Repository
 *
 * @author dbucek
 */
public class MemgraphQueryRepository implements QueryRepository {
    private final static Logger LOGGER = LoggerFactory.getLogger(MemgraphQueryRepository.class);

    private final static String MEMGRAPH_URL = "bolt://localhost:7687";
    private final static String MEMGRAPH_USERNAME = "";
    private final static String MEMGRAPH_PASSWORD = "";

    private final Driver driver;
    private final Session session;

    public MemgraphQueryRepository() {
        driver = GraphDatabase.driver(MEMGRAPH_URL, AuthTokens.basic(MEMGRAPH_USERNAME, MEMGRAPH_PASSWORD));
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
            StatementResult result = tx.run("\n" +
                            "UNWIND $csvLine AS line \n" +
                            "MATCH (p:Person)\n" +
                            "WHERE p.personId = line[0]\n" +
                            "RETURN p",
                    parameters("csvLine", vertices));

            return processResult(result);
        });
    }

    @Override
    public Set<String> queryTraverse(String[][] vertices) {
        return session.readTransaction(tx -> {
            StatementResult result = tx.run("\n" +
                            "UNWIND $csvLine AS line \n" +
                            "MATCH (p1:Person)-[:IS_FRIEND]->(p2)\n" +
                            "WHERE p1.personId = line[0]\n" +
                            "RETURN p2",
                    parameters("csvLine", vertices));

            return processResult(result);
        });
    }

    private Set<String> processResult(StatementResult result) {
        Set<String> output = new HashSet<>();

        while (result.hasNext()) {
            Record record = result.next();

            output.add(record.get(0).get("personId").asString());
        }

        return output;
    }
}
