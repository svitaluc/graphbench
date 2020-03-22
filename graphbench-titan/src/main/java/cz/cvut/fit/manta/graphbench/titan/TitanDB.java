package cz.cvut.fit.manta.graphbench.titan;

import com.thinkaurelius.titan.core.TitanIndexQuery.Result;
import com.thinkaurelius.titan.core.TitanGraph;
import com.thinkaurelius.titan.core.Parameter;
import com.thinkaurelius.titan.core.Mapping;
import com.thinkaurelius.titan.core.TitanFactory;
import com.thinkaurelius.titan.core.TitanKey;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import cz.cvut.fit.manta.graphbench.backend.cassandra.CassandraVersion;
import cz.cvut.fit.manta.graphbench.backend.cassandra.CassandraYaml;
import cz.cvut.fit.manta.graphbench.core.db.structure.EdgeLabel;
import cz.cvut.fit.manta.graphbench.core.config.GraphDBConfiguration;
import cz.cvut.fit.manta.graphbench.titan.config.model.TitanProperty;
import cz.cvut.fit.manta.graphbench.titan.config.TitanProperties;
import org.apache.commons.configuration.BaseConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.log4j.Logger;
import cz.cvut.fit.manta.graphbench.core.config.model.ConfigProperty;
import cz.cvut.fit.manta.graphbench.core.db.GraphDBConnector;
import cz.cvut.fit.manta.graphbench.core.db.Translator;
import cz.cvut.fit.manta.graphbench.core.db.structure.EdgeProperty;
import cz.cvut.fit.manta.graphbench.core.db.structure.NodeProperty;
import cz.cvut.fit.manta.graphbench.core.util.Util;
import cz.cvut.fit.manta.graphbench.tinkerpop2.TP2Edge;
import cz.cvut.fit.manta.graphbench.tinkerpop2.TP2Vertex;

import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 *
 *
 * @author Lucie Svitáková (svitaluc@fit.cvut.cz)
 */
public class TitanDB implements GraphDBConnector<TP2Vertex, TP2Edge> {
    /** Flag holding information, whether the database is connected. */
    private boolean connected = false;
    /** Path to the database. */
    private String dbPath = null;
    /** Configuration of the Titan database. */
    private Configuration configuration = new BaseConfiguration();
    /** Graph stored in the Titan database. */
    private TitanGraph internalGraph;
    /** Logger. */
    private final static Logger LOG = Logger.getLogger(TitanDB.class);
    /** Properties of the Titan, immutable. */
    private final static TitanProperties TITAN_PROPERTIES = TitanProperties.getInstance();
    /** Properties of the Cassandra backend storage. */
    private CassandraYaml cassandraProperties;

    /**
     * Constructor of the {@link TitanDB}.
     */
    public TitanDB() {
        cassandraProperties = CassandraYaml.getInstance(CassandraVersion.CASSANDRA_122);
    }

    /**
     * Sets the inner configuration with the properties set in the titan.properties file
     * and important directory paths with the {@code dbPath} parameter.
     *
     * @param dbPath Directory in which supporting Titan data will be stored
     */
    private void setConfiguration(String dbPath) {
        Util.setConfiguration(configuration, TITAN_PROPERTIES);

        configuration.setProperty(TitanProperty.STORAGE_DIRECTORY.getName(), dbPath);
        String cassandraYamlUrl = "file:\\\\\\" + cassandraProperties.getAbsolutePropertiesPath();
        configuration.setProperty(TitanProperty.STORAGE_CASSANDRA_CONFIG_DIR.getName(), cassandraYamlUrl);

        String storageDir = Paths.get("src", "main", "resources", "storage").toFile().getAbsolutePath();
        configuration.setProperty(TitanProperty.STORAGE_CASSANDRA_STORAGEDIR.getName(), storageDir);

        configuration.setProperty(TitanProperty.STORAGE_INDEX_SEARCH_DIRECTORY.getName(), dbPath + "/" + TitanProperty.INDEX_SEARCH_DIRECTORY_NAME);
    }

    /**
     * Sets schema of the Titan database.
     */
    private void setSchema() {
        internalGraph.makeKey(NodeProperty.NODE_NAME.t())
                .dataType(String.class)
                .indexed("search", Vertex.class, Parameter.of(Mapping.MAPPING_PREFIX, Mapping.TEXT))
                .make();

        internalGraph.makeKey(NodeProperty.NODE_TYPE.t())
                .dataType(String.class)
                .indexed(Vertex.class)
                .make();

        //Create Child name index
        TitanKey childKey = internalGraph.makeKey(EdgeProperty.EDGE_ATTRIBUTE.t())
                .dataType(String.class)
                .indexed(Edge.class)
                .make();

        internalGraph.makeLabel(EdgeLabel.HAS_PARENT.t()).sortKey(childKey).make();
        internalGraph.makeLabel(EdgeLabel.DIRECT.t()).sortKey(childKey).make();
        internalGraph.makeLabel(EdgeLabel.FILTER.t()).sortKey(childKey).make();
    }

    @Override
    public GraphDBConfiguration getGraphDBConfiguration() {
        return TITAN_PROPERTIES;
    }

    @Override
    public void connect(String dbPath, String userName, String userPassword) {
        Util.clearDirectory(dbPath, LOG);
        setConfiguration(dbPath);

        internalGraph = TitanFactory.open(configuration);
        setSchema();

        this.dbPath = dbPath;
        connected = true;
    }

    @Override
    public void disconnect() {
        internalGraph.shutdown();
        connected = false;
    }

    @Override
    public String getDBPath() {
        return dbPath;
    }

    @Override
    public void commit() {
        internalGraph.commit();
    }

    @Override
    public void rollback() {
        internalGraph.rollback();
    }

    @Override
    public boolean isConnected() {
        return connected;
    }

    @Override
    public void deleteAllNodes() {
        for (Vertex v : internalGraph.getVertices()) {
            v.remove();
        }
    }

    @Override
    public long getNodesCount() {
        LOG.warn("Not yet implemented.");
        return 0;
    }

    @Override
    public TP2Vertex addEmptyVertex() {
        Vertex vertex = internalGraph.addVertex(null);
        return new TP2Vertex(vertex);
    }

    @Override
    public TP2Vertex getVertex(Object id) {
        Vertex vertex = internalGraph.getVertex(id);
        return new TP2Vertex(vertex);
    }

    @Override
    public TP2Edge addEdge(TP2Vertex outVertex, TP2Vertex inVertex, String label) {
        Edge edge = internalGraph.addEdge(null, outVertex.getVertex(), inVertex.getVertex(), label);
        return new TP2Edge(edge);
    }

    @Override
    public TP2Edge getEdge(Object id) {
        Edge edge = internalGraph.getEdge(id);
        return new TP2Edge(edge);
    }

    @Override
    public void setEdgeProperty(TP2Edge edge, String name, Object value) {
        edge.property(name, value);
    }

    @Override
    public List<TP2Vertex> getVertexByName(String name) {
        Iterable<Result<Vertex>> it = searchVertexWithIndexQuery(name);
        List<TP2Vertex> result = new ArrayList<>();

        System.out.println("Get vertex by name:");
        for (Result<Vertex> v : it) {
            result.add(new TP2Vertex(v.getElement()));
        }
        return result;
    }

    private Iterable<Result<Vertex>> searchVertexWithIndexQuery(String name) {
        return internalGraph.indexQuery("search", "v." + NodeProperty.NODE_NAME.t() + ":(/" + name + "/)").vertices();
    }

    @Override
    public <T> T getTraversal() {
        throw new UnsupportedOperationException("Traversal of Titan DB is not available.");
    }

    @Override
    public void addVertex(String[] parts, Translator trans) {
        TP2Vertex node = new TP2Vertex(addEmptyVertex().getVertex());

        trans.putTempNode(parts[CONFIG.getIntegerProperty(ConfigProperty.NODE_I_ID)], node);

        node.property(NodeProperty.NODE_NAME.t(), parts[CONFIG.getIntegerProperty(ConfigProperty.NODE_I_NAME)]);
        node.property(NodeProperty.NODE_TYPE.t(), CONFIG.getStringProperty(ConfigProperty.VERTEX_NODE_TYPE));


        //Parent edge
        if (parts[CONFIG.getIntegerProperty(ConfigProperty.NODE_I_PARENT)].length() > 0) {
            String parentString = trans.getNode(parts[CONFIG.getIntegerProperty(ConfigProperty.NODE_I_PARENT)]);

            TP2Vertex parentNode = (TP2Vertex) trans.getTempNode(parts[CONFIG.getIntegerProperty(ConfigProperty.NODE_I_PARENT)]);
            if (parentNode == null || parentNode.isVertexNull()) {
                try {
                    parentNode = getVertex(parentString);
                } catch (IllegalArgumentException e) {
                    LOG.error("Could not find parent with id: " + parentString);
                    parentNode = new TP2Vertex();
                }
            }

            if (!parentNode.isVertexNull()) {
                TP2Edge edge = addEdge(node, parentNode, EdgeLabel.HAS_PARENT.t());
            } else {
                LOG.warn(MessageFormat.format(
                        "Database didn't return a node to set a parent. Original node id: \"{0}\", new node id: \"{1}\"",
                        parts[CONFIG.getIntegerProperty(ConfigProperty.NODE_I_PARENT)], node.getId().toString()));
            }
        }
    }
}
