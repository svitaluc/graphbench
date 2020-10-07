package cz.cvut.fit.manta.graphbench.repository;

import com.thinkaurelius.titan.core.TitanFactory;
import com.thinkaurelius.titan.core.TitanGraph;
import com.tinkerpop.blueprints.Vertex;
import org.apache.commons.configuration.BaseConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import static com.tinkerpop.blueprints.Compare.EQUAL;

public class TitanImportRepository implements ImportRepository {

    /**
     * Logger.
     */
    private final static Logger LOGGER = LoggerFactory.getLogger(TitanImportRepository.class);

    /**
     * Graph stored in the Titan database.
     */
    private final TitanGraph internalGraph;

    private final String dbDirectory = System.getProperty("java.io.tmpdir");

    public TitanImportRepository() {
        clearDirectory();

        internalGraph = TitanFactory.open(createConfiguration());
        try {
            setSchema(internalGraph);
        } catch (Exception e) {
            internalGraph.rollback();
        }
    }

    @Override
    public void init() {

    }

    @Override
    public void storeVertices(String[][] vertices) {
        for (String[] line : vertices) {
            Vertex vertex = internalGraph.addVertex(line[0]); // provided id is only a recommendation, it's not guaranteed to be used
            vertex.setProperty("type", line[3]); // Titan 0.4.4 has labels only on edges, notion of vertex labels is added in version 0.5.0
            vertex.setProperty("personId", line[0]);
            vertex.setProperty("name", line[1]);
            vertex.setProperty("year", Integer.parseInt(line[2]));

            //idTranslator.putId(line[0], vertex.getId().toString());
        }

        internalGraph.commit();
    }

    @Override
    public void storeEdges(String[][] partition) {
        for (String[] line : partition) {
            // Search by index
            Iterator<Vertex> outVertex = internalGraph.query().has("personId", EQUAL, line[0]).vertices().iterator();
            Iterator<Vertex> inVertex = internalGraph.query().has("personId", EQUAL, line[1]).vertices().iterator();

            if (outVertex.hasNext() && inVertex.hasNext()) {
                internalGraph.addEdge(line[0] + "_" + line[1], outVertex.next(), inVertex.next(), line[2]);
            } else {
//                LOGGER.warn("Out or in vertex not found!!. OutPersonId: " + line[0] + ", InPersonId " + line[1]);
            }

            if (outVertex.hasNext() || inVertex.hasNext()) {
                LOGGER.warn("Out or in vertices are more than one!!. OutPersonId: " + line[0] + ", InPersonId " + line[1]);
            }
        }

        internalGraph.commit();
    }

    @Override
    public void close() {
        internalGraph.shutdown();
    }

    @Override
    public int getDefaultVertexBatchSize() {
        return 100_000;
    }

    @Override
    public int getDefaultEdgeBatchSize() {
        return 100_000;
    }

    private void clearDirectory() {
        File directoryPathFile = new File(dbDirectory);
        LOGGER.debug("Directory to be deleted: " + directoryPathFile.getAbsolutePath());
        try {
            FileUtils.cleanDirectory(directoryPathFile);
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
    }

    private Configuration createConfiguration() {
        Configuration configuration = new BaseConfiguration();

        configuration.setProperty("storage.directory", dbDirectory);
        configuration.setProperty("storage.backend", "persistit");
        configuration.setProperty("storage.buffercount", "5000");
        configuration.setProperty("cache.db-cache", "false");
        configuration.setProperty("cache.db-cache-size", "0");
        configuration.setProperty("cache.tx-cache-size", "0");
        configuration.setProperty("cache.db-cache-time", "0");
        configuration.setProperty("cache.db-cache-clean-wait", "0");

        configuration.setProperty("storage.parallel-backend-ops", "false");

        configuration.setProperty("storage.batch-loading", "true");
        configuration.setProperty("query.fast-property", "true");
        configuration.setProperty("query.batch", "true");

//        configuration.setProperty("storage.cassandra-config-dir", "file:\\\\\\C:\\manta\\DB\\Titan\\cassandra-1.2.2.yaml");
//        configuration.setProperty("storage.cassandra.write-consistency-level", "ONE");
//        configuration.setProperty("storage.cassandra.compression", "true");
//        configuration.setProperty("storage.cassandra.replication-factor", "1");
//        configuration.setProperty("storage.cassandra.read-consistency-level", "ONE");
//        configuration.setProperty("storage.cassandra.logger.level", "ERROR");
//        configuration.setProperty("storage.hostname", "127.0.0.1");

        configuration.setProperty("storage.index.search.backend", "lucene");
        configuration.setProperty("storage.index.search.directory", dbDirectory + "/searchindex");

        return configuration;
    }

    private void setSchema(TitanGraph titanGraph) {
        titanGraph.makeKey("type").dataType(String.class).make();
        titanGraph.makeKey("personId").dataType(String.class).indexed(Vertex.class).unique().make();
//        titanGraph.makeKey("personId").dataType(String.class).indexed("personIdIdx", Vertex.class).unique().make();
        titanGraph.makeKey("name").dataType(String.class).make();
        titanGraph.makeKey("year").dataType(Integer.class).make();

        titanGraph.makeLabel("FRIEND").make();

    }
}
