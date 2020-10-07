package cz.cvut.fit.manta.graphbench.repository;

import com.orientechnologies.orient.core.db.ODatabaseSession;
import com.orientechnologies.orient.core.db.OrientDB;
import com.orientechnologies.orient.core.db.OrientDBConfig;
import com.orientechnologies.orient.core.record.OVertex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author dbucek
 */
public class OrientDbImportRepository implements ImportRepository {
    private final static Logger LOGGER = LoggerFactory.getLogger(OrientDbImportRepository.class);

    private final static String ORIENTDB_URL = "remote:localhost";
    private final static String ORIENTDB_DATABASE = "import-neo";
    private final static String ORIENTDB_USERNAME = "root";
    private final static String ORIENTDB_PASSWORD = "admin";

    /**
     *
     */
    private final OrientDB orientdb;

    /**
     *
     */
    private final ODatabaseSession databaseSession;

    // OPEN/CREATE DATABASE
    public OrientDbImportRepository() {

        // Initialize Server Connection
        orientdb = new OrientDB(ORIENTDB_URL, OrientDBConfig.defaultConfig());

        // Open database
        databaseSession = orientdb.open(ORIENTDB_DATABASE, ORIENTDB_USERNAME, ORIENTDB_PASSWORD);
    }

    @Override
    public void init() {

    }

    @Override
    public void storeVertices(String[][] vertices) {
        // Create schema first
        databaseSession.command("CREATE CLASS Person IF NOT EXISTS EXTENDS V");
        databaseSession.command("CREATE PROPERTY Person.personId IF NOT EXISTS STRING");
        databaseSession.command("CREATE INDEX Person.personId IF NOT EXISTS UNIQUE");

        // Import itself
        OVertex vertexToSave;

        databaseSession.begin();

        for (String[] vertexArray : vertices) {
            vertexToSave = databaseSession.newVertex("Person");
            vertexToSave.setProperty("personId", vertexArray[0]);
            vertexToSave.setProperty("name", vertexArray[1]);
            vertexToSave.setProperty("year", vertexArray[2]);
            vertexToSave.save();
        }

        // Commit after whole run
        databaseSession.commit();
    }

    @Override
    public void storeEdges(String[][] edges) {
//        // Create schema first
//        databaseSession.command("CREATE CLASS IsFriend IF NOT EXISTS EXTENDS E");
//
//        // Import itself
//        int batchSize = 1_000;
//        int roundCounter = 1;
//
//        String createEdgeStatement = "CREATE EDGE IsFriend FROM (SELECT FROM Person WHERE personId = ?) TO (SELECT FROM Person WHERE personId = ?)";
//        int counter = 1;
//        long start = System.nanoTime();
//        databaseSession.begin();
//
//        for (String[] edgeArray : edges) {
//            databaseSession.command(createEdgeStatement, edgeArray[0], edgeArray[1]);
//
//            if (counter % batchSize == 0) {
//                // Commit previous bunch
//                databaseSession.commit();
//                LOGGER.info("Total of " + (batchSize * roundCounter++) + " processed in time: " + (((double) System.nanoTime() - start) / 1_000_000_000) + " seconds");
//
//                // Open new transaction
//                start = System.nanoTime();
//                databaseSession.begin();
//            }
//            counter++;
//        }
//
//        // Commit after whole run
//        databaseSession.commit();
//        LOGGER.info("Total of " + (batchSize * roundCounter) + " processed in time: " + (((double) System.nanoTime() - start) / 1_000_000_000) + " seconds");
    }

    @Override
    public void close() {
        databaseSession.close();
        orientdb.close();
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
