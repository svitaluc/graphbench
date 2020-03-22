package cz.cvut.fit.manta.graphbench.janusgraph;

import cz.cvut.fit.manta.graphbench.backend.cassandra.CassandraVersion;
import cz.cvut.fit.manta.graphbench.backend.cassandra.CassandraYaml;
import cz.cvut.fit.manta.graphbench.backend.cassandra.model.CassandraProperty;
import cz.cvut.fit.manta.graphbench.core.config.GraphDBConfiguration;
import cz.cvut.fit.manta.graphbench.core.config.model.ConfigProperty;
import cz.cvut.fit.manta.graphbench.core.db.GraphDBConnector;
import cz.cvut.fit.manta.graphbench.core.db.Translator;
import cz.cvut.fit.manta.graphbench.core.db.structure.EdgeProperty;
import cz.cvut.fit.manta.graphbench.core.db.structure.NodeProperty;
import cz.cvut.fit.manta.graphbench.core.util.Util;
import cz.cvut.fit.manta.graphbench.janusgraph.config.JanusGraphProperties;
import cz.cvut.fit.manta.graphbench.janusgraph.config.model.JanusGraphProperty;
import cz.cvut.fit.manta.graphbench.tinkerpop3.TP3Edge;
import cz.cvut.fit.manta.graphbench.tinkerpop3.TP3Vertex;
import org.apache.commons.configuration.BaseConfiguration;
import org.apache.log4j.Logger;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.janusgraph.core.*;
import org.janusgraph.core.JanusGraphIndexQuery.Result;
import org.janusgraph.core.schema.JanusGraphManagement;
import org.janusgraph.core.schema.Mapping;

import java.io.File;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Connector for the JanusGraph database (https://janusgraph.org/).
 *
 * @author Lucie Svitáková (svitaluc@fit.cvut.cz)
 */
public class JanusGraphDB implements GraphDBConnector<TP3Vertex, TP3Edge> {
	/** Flag holding information, whether the database is connected. */
	private boolean connected = false;
	/** Path to the database. */
	private String dbPath = null;
	/** Configuration of the JanusGraph database. */
	private org.apache.commons.configuration.Configuration configuration = new BaseConfiguration();
	/** Graph stored in the JanusGraph database. */
	private JanusGraph internalGraph;
	/** Properties of the Cassandra backend storage. */
	private CassandraYaml cassandraProperties;
	/** Properties of the JansuGraph, immutable. */
	private final static JanusGraphProperties JANUSGRAPH_PROPERTIES = JanusGraphProperties.getInstance();
	/** Logger. */
	private final static Logger LOG = Logger.getLogger(JanusGraphDB.class);

	/**
	 * Constructor of the {@link JanusGraphDB}.
	 */
	public JanusGraphDB() {
		cassandraProperties = CassandraYaml.getInstance(CassandraVersion.CASSANDRA_3110);
	}

	/**
	 * Sets directory paths of the Cassandra configuration to the ones set
	 * by the {@code dbPath} parameter and specific names of the concrete directories.
	 * @param dbPath Directory path where to store Cassandra data
	 */
	private void setCassandraPaths(String dbPath) {
		cassandraProperties.setProperty(CassandraProperty.COMMITLOG_DIRECTORY, dbPath + File.separator + CassandraProperty.COMMITLOG_DIRECTORY_NAME);
		cassandraProperties.setProperty(CassandraProperty.SAVED_CACHES_DIRECTORY, dbPath + File.separator + CassandraProperty.SAVED_CACHES_DIRECTORY_NAME);
		cassandraProperties.setProperty(CassandraProperty.DATA_FILE_DIRECTORIES, dbPath + File.separator + CassandraProperty.DATA_DIRECTORY_NAME);
	}

	/**
	 * Sets the inner configuration with the properties set in the janusgraph.properties file
	 * and important directory paths with the {@code dbPath} parameter.
	 *
	 * @param dbPath Directory in which supporting JanusGraph data will be stored
	 */
	private void setConfiguration(String dbPath) {
		Util.setConfiguration(configuration, JANUSGRAPH_PROPERTIES);

		String cassandraYamlPath = cassandraProperties.getAbsolutePropertiesPath();
		configuration.setProperty(JanusGraphProperty.STORAGE_CONF_FILE.getName(), "file:\\\\\\" + cassandraYamlPath);

		String storageDir = Paths.get("src", "main", "resources", "storage").toFile().getAbsolutePath();
		configuration.setProperty(JanusGraphProperty.STORAGE_CASSANDRA_STORAGEDIR.getName(), storageDir);

		configuration.setProperty(JanusGraphProperty.INDEX_SEARCH_DIRECTORY.getName(), dbPath + "/"
				+ JanusGraphProperty.INDEX_SEARCH_DIRECTORY_NAME);
	}

	/**
	 * Sets schema of the JanusGraph database.
	 */
	private void setSchema() {
		JanusGraphManagement mgmt = internalGraph.openManagement();

		VertexLabel lbl = mgmt.makeVertexLabel("lbl").make();
		mgmt.makePropertyKey(NodeProperty.NODE_TYPE.t()).dataType(String.class).make();

		//create properties
		PropertyKey nodeName = mgmt.makePropertyKey(NodeProperty.NODE_NAME.t()).dataType(String.class).make();

		mgmt.makePropertyKey(EdgeProperty.EDGE_ATTRIBUTE.t()).dataType(String.class).make();

		EdgeLabel parLabel = mgmt.makeEdgeLabel(cz.cvut.fit.manta.graphbench.core.db.structure.EdgeLabel.HAS_PARENT.t()).make();
		mgmt.makeEdgeLabel(cz.cvut.fit.manta.graphbench.core.db.structure.EdgeLabel.DIRECT.t()).make();
		mgmt.makeEdgeLabel(cz.cvut.fit.manta.graphbench.core.db.structure.EdgeLabel.FILTER.t()).make();

		mgmt.buildIndex("nodeName", Vertex.class).addKey(nodeName, Mapping.TEXT.asParameter()).buildMixedIndex("search");

		mgmt.commit();
	}

	@Override
	public GraphDBConfiguration getGraphDBConfiguration() {
		return JANUSGRAPH_PROPERTIES;
	}

	@Override
	public void connect(String dbPath, String userName, String userPassword) {
		setCassandraPaths(dbPath);
		LOG.info("DB PATH " + dbPath);
		Util.clearDirectory(dbPath, LOG);

		setConfiguration(dbPath);
		internalGraph = JanusGraphFactory.open(configuration);

		setSchema();

		this.dbPath = dbPath;
		connected = true;
	}

	@Override
	public void disconnect() {
		commit();
		internalGraph.openManagement().commit();
		internalGraph.close();
		connected = false;
	}

	@Override
	public String getDBPath() {
		return dbPath;
	}

	@Override
	public void commit() {
		internalGraph.tx().commit();
	}

	@Override
	public void rollback() {
		internalGraph.tx().rollback();
	}

	@Override
	public boolean isConnected() {
		return connected;
	}

	@Override
	public void deleteAllNodes() {
		internalGraph.vertices().remove();
	}

	@Override
	public long getNodesCount() {
		int i = 0;

		LOG.warn("Not yet implemented.");
		return i;
	}

	@Override
	public TP3Vertex addEmptyVertex() {
		Vertex vertex = internalGraph.addVertex("lbl");
		return new TP3Vertex(vertex);
	    //return internalGraph.addVertex();
	}

	@Override
	public TP3Vertex getVertex(Object id) {
	    try {
	    	Iterator<Vertex> it = internalGraph.vertices(id);
	    	if (it == null || !it.hasNext()) {
	    		return new TP3Vertex(null);
			}
			Vertex vertex = it.next();
			return new TP3Vertex(vertex);
	    } catch (Exception ex) {
	        LOG.info("An unexpected exception when getting a vertex.", ex);
	        return new TP3Vertex(null);
	    }
	}

	@Override
	public TP3Edge addEdge(TP3Vertex outVertex, TP3Vertex inVertex, String label) {
		return outVertex.addEdge(label, inVertex);
	}

	@Override
	public TP3Edge getEdge(Object id) {
		return new TP3Edge(internalGraph.edges(id).next());
	}

	@Override
	public void setEdgeProperty(TP3Edge edge, String name, Object value) {
		edge.property(name, value);
	}
	
	@Override
	public GraphTraversalSource getTraversal() {
	    return internalGraph.traversal();
	}

	@Override
	public void addVertex(String[] parts, Translator trans) {
		TP3Vertex node = new TP3Vertex(addEmptyVertex().getVertex());

		trans.putTempNode(parts[CONFIG.getIntegerProperty(ConfigProperty.NODE_I_ID)], node);

		node.property(NodeProperty.NODE_NAME.t(), parts[CONFIG.getIntegerProperty(ConfigProperty.NODE_I_NAME)]);
		node.property(NodeProperty.NODE_TYPE.t(), CONFIG.getStringProperty(ConfigProperty.VERTEX_NODE_TYPE));

		//Parent edge
		if (parts[CONFIG.getIntegerProperty(ConfigProperty.NODE_I_PARENT)].length() > 0) {
			String parentString = trans.getNode(parts[CONFIG.getIntegerProperty(ConfigProperty.NODE_I_PARENT)]);

			TP3Vertex parentNode = (TP3Vertex) trans.getTempNode(parts[CONFIG.getIntegerProperty(ConfigProperty.NODE_I_PARENT)]);
			if (parentNode == null || parentNode.isVertexNull()) {
				parentNode = getVertex(parentString);
			}

			if (!parentNode.isVertexNull()) {
				TP3Edge edge = addEdge(node, parentNode, cz.cvut.fit.manta.graphbench.core.db.structure.EdgeLabel.HAS_PARENT.t());
			} else {
				LOG.warn(MessageFormat.format(
						"Database didn't return a node to set a parent. Original node id: \"{0}\", new node id: \"{1}\"",
						parts[CONFIG.getIntegerProperty(ConfigProperty.NODE_I_PARENT)], node.getId().toString()));
			}
		}
	}

	@Override
	 public List<TP3Vertex> getVertexByName(String name) {
	     long startTime = System.nanoTime();
	     Iterable<Result<JanusGraphVertex>> it = internalGraph.indexQuery("nodeName", "lbl." + NodeProperty.NODE_NAME.t() + ":(/" + name + "/)").vertexStream().collect(Collectors.toList());;

	     List<TP3Vertex> result = new ArrayList<>();

	     for (Result<JanusGraphVertex> v : it) {
	         result.add(new TP3Vertex(v.getElement()));
	     }

	     long endTime = System.nanoTime();
	     LOG.info("getVertexByName Total time = " + (endTime - startTime));
	     return result;
	}
}
