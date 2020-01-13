package eu.profinit.manta.graphbench.titan;

import com.thinkaurelius.titan.core.TitanIndexQuery.Result;
import com.thinkaurelius.titan.core.TitanGraph;
import com.thinkaurelius.titan.core.Parameter;
import com.thinkaurelius.titan.core.Mapping;
import com.thinkaurelius.titan.core.TitanFactory;
import com.thinkaurelius.titan.core.TitanKey;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import eu.profinit.manta.graphbench.backend.cassandra.CassandraVersion;
import eu.profinit.manta.graphbench.backend.cassandra.CassandraYaml;
import eu.profinit.manta.graphbench.core.config.GraphDBConfiguration;
import eu.profinit.manta.graphbench.titan.config.TitanProperties;
import eu.profinit.manta.graphbench.titan.config.model.TitanProperty;
import org.apache.commons.configuration.BaseConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.log4j.Logger;
import eu.profinit.manta.graphbench.core.config.model.ConfigProperty;
import eu.profinit.manta.graphbench.core.db.IGraphDBConnector;
import eu.profinit.manta.graphbench.core.db.Translator;
import eu.profinit.manta.graphbench.core.db.structure.EdgeProperty;
import eu.profinit.manta.graphbench.core.db.structure.NodeProperty;
import eu.profinit.manta.graphbench.core.util.Util;
import eu.profinit.manta.graphbench.tinkerpop2.TP2Edge;
import eu.profinit.manta.graphbench.tinkerpop2.TP2Vertex;

import java.io.File;
import java.net.URL;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class TitanDB implements IGraphDBConnector<TP2Vertex, TP2Edge> {

    private boolean connected = false;
    private String dbName = null;
    private Configuration configuration = new BaseConfiguration();
    private TitanGraph internalGraph;
    final static Logger LOG = Logger.getLogger(TitanDB.class);
    private TitanProperties titanProperties;
    private CassandraYaml cassandraProperties;

    public TitanDB() {
        titanProperties = TitanProperties.getInstance();
        cassandraProperties = CassandraYaml.getInstance(CassandraVersion.CASSANDRA_122);
    }

    private void setConfiguration(String dbPath) {
        Util.setConfiguration(configuration, titanProperties);

        configuration.setProperty(TitanProperty.STORAGE_DIRECTORY.getName(), dbPath);
        String cassandraYamlPath = File.separator + cassandraProperties.getRelativePropertiesPath();
        configuration.setProperty(TitanProperty.STORAGE_CASSANDRA_CONFIG_DIR.getName(), cassandraYamlPath);

        String storageDir = Paths.get("src", "main", "resources", "storage").toFile().getAbsolutePath();
        configuration.setProperty(TitanProperty.STORAGE_CASSANDRA_STORAGEDIR.getName(), storageDir);

        configuration.setProperty(TitanProperty.STORAGE_INDEX_SEARCH_DIRECTORY.getName(), dbPath + "/" + TitanProperty.INDEX_SEARCH_DIRECTORY_NAME);
    }

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

        internalGraph.makeLabel(eu.profinit.manta.graphbench.core.db.structure.EdgeLabel.HAS_PARENT.t()).sortKey(childKey).make();
        internalGraph.makeLabel(eu.profinit.manta.graphbench.core.db.structure.EdgeLabel.DIRECT.t()).sortKey(childKey).make();
        internalGraph.makeLabel(eu.profinit.manta.graphbench.core.db.structure.EdgeLabel.FILTER.t()).sortKey(childKey).make();
    }

    @Override
    public GraphDBConfiguration getGraphDBConfiguration() {
        return titanProperties;
    }

    @Override
    public void connect(String dbPath, String userName, String userPassword) {
        Util.clearDirectory(dbPath, LOG);
        setConfiguration(dbPath);

        internalGraph = TitanFactory.open(configuration);
        setSchema();

        dbName = dbPath;
        connected = true;
    }

    @Override
    public void disconnect() {
        internalGraph.shutdown();
        connected = false;
    }

    @Override
    public String getDBName() {
        return dbName;
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
    public TP2Vertex addVertex() {
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
            //System.out.println(v.getElement());
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
    public void addVertexNode(String[] parts, Translator trans) {
        TP2Vertex node = new TP2Vertex(addVertex().getVertex());

        trans.putTemp(parts[config.getIntegerProperty(ConfigProperty.NODE_I_ID)], node);

        node.property(NodeProperty.NODE_NAME.t(), parts[config.getIntegerProperty(ConfigProperty.NODE_I_NAME)]);
        node.property(NodeProperty.NODE_TYPE.t(), config.getStringProperty(ConfigProperty.VERTEX_NODE_TYPE));


        //Parent edge
        if (parts[config.getIntegerProperty(ConfigProperty.NODE_I_PARENT)].length() > 0) {
            String parentString = trans.get(parts[config.getIntegerProperty(ConfigProperty.NODE_I_PARENT)]);

            TP2Vertex parentNode = (TP2Vertex) trans.getTemp(parts[config.getIntegerProperty(ConfigProperty.NODE_I_PARENT)]);
            if(parentNode == null || parentNode.isVertexNull()) {
                parentNode = getVertex(parentString);
            }

            if (!parentNode.isVertexNull()) {
                TP2Edge edge = addEdge(node, parentNode, eu.profinit.manta.graphbench.core.db.structure.EdgeLabel.HAS_PARENT.t());
            } else {
                LOG.warn(MessageFormat.format(
                        "Database didn't return a node to set a parent. Original node id: \"{0}\", new node id: \"{1}\"",
                        parts[config.getIntegerProperty(ConfigProperty.NODE_I_PARENT)], node.id().toString()));
            }
        }
    }
}
