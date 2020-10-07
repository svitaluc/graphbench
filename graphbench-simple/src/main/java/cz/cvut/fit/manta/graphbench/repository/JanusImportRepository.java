package cz.cvut.fit.manta.graphbench.repository;

import org.apache.commons.configuration.BaseConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.io.FileUtils;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.janusgraph.core.JanusGraph;
import org.janusgraph.core.JanusGraphFactory;
import org.janusgraph.core.JanusGraphVertex;
import org.janusgraph.core.PropertyKey;
import org.janusgraph.core.schema.JanusGraphManagement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

public class JanusImportRepository implements ImportRepository {

    /**
     * Logger.
     */
    private final static Logger LOGGER = LoggerFactory.getLogger(JanusImportRepository.class);

    /**
     * Graph stored in the Titan database.
     */
    private final JanusGraph graph;
    private final JanusGraphManagement management;

    private final String dbDirectory = System.getProperty("java.io.tmpdir");

    public JanusImportRepository() {
        clearDirectory();

        graph = JanusGraphFactory.open(createConfiguration());
        management = graph.openManagement();
    }

    @Override
    public void init() {
        // Create schema
        try {
            setSchema(management);
        } catch (Exception e) {
            graph.tx().rollback();
        }
    }

    @Override
    public void storeVertices(String[][] vertices) {
        for (String[] line : vertices) {
            Vertex vertex = graph.addVertex(line[0]); // provided id is only a recommendation, it's not guaranteed to be used
            vertex.property("type", line[3]); // Titan 0.4.4 has labels only on edges, notion of vertex labels is added in version 0.5.0
            vertex.property("personId", line[0]);
            vertex.property("name", line[1]);
            vertex.property("year", Integer.parseInt(line[2]));
        }

        graph.tx().commit();
    }

    @Override
    public void storeEdges(String[][] partition) {
        for (String[] line : partition) {
            // Search by index
            Iterator<JanusGraphVertex> outVertex = graph.query().has("personId", line[0]).vertices().iterator();
            Iterator<JanusGraphVertex> inVertex = graph.query().has("personId", line[1]).vertices().iterator();

            if (outVertex.hasNext() && inVertex.hasNext()) {
                outVertex.next().addEdge(line[0] + "_" + line[1], inVertex.next());
            } else {
               // LOGGER.warn("Out or In vertices not found!!. OutPersonId: " + line[0] + ", InPersonId " + line[1]);
            }
        }

        graph.tx().commit();
    }

    @Override
    public int getDefaultVertexBatchSize() {
        return 10_000;
    }

    @Override
    public int getDefaultEdgeBatchSize() {
        return 10_000;
    }

    @Override
    public void close() {
        graph.close();
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

        // For Berkeley
        configuration.setProperty("storage.backend", "berkeleyje");
        configuration.setProperty("storage.directory", dbDirectory);
        // For Cassandra
//        configuration.setProperty("storage.backend", "cql");
//        configuration.setProperty("storage.hostname", "127.0.0.1");

        return configuration;
    }

    private void setSchema(JanusGraphManagement management) {
        management.makePropertyKey("type").dataType(String.class).make();
        PropertyKey personId = management.makePropertyKey("personId").dataType(String.class).make();
        management.makePropertyKey("name").dataType(String.class).make();
        management.makePropertyKey("year").dataType(Integer.class).make();

        management.makeEdgeLabel("FRIEND").make();

        // Create indices
        management.buildIndex("byPersonId", Vertex.class).addKey(personId).unique().buildCompositeIndex();

        management.commit();
    }
}
