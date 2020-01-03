package eu.profinit.manta.graphbench.janusgraph;

import eu.profinit.manta.graphbench.core.config.ConfigProperties;
import eu.profinit.manta.graphbench.core.config.Configuration;
import eu.profinit.manta.graphbench.core.config.model.ConfigProperty;
import eu.profinit.manta.graphbench.core.db.IGraphDBConnector;
import eu.profinit.manta.graphbench.core.db.Translator;
import eu.profinit.manta.graphbench.core.db.structure.EdgeProperty;
import eu.profinit.manta.graphbench.core.db.structure.NodeProperty;
import eu.profinit.manta.graphbench.janusgraph.config.CassandraYaml;
import eu.profinit.manta.graphbench.janusgraph.config.JanusGraphProperties;
import eu.profinit.manta.graphbench.janusgraph.config.model.CassandraProperty;
import eu.profinit.manta.graphbench.janusgraph.config.model.JanusGraphProperty;
import org.apache.commons.configuration.BaseConfiguration;
import org.apache.log4j.Logger;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.janusgraph.core.*;
import org.janusgraph.core.JanusGraphIndexQuery.Result;
import org.janusgraph.core.schema.JanusGraphManagement;
import org.janusgraph.core.schema.Mapping;
import eu.profinit.manta.graphbench.core.util.Util;
import eu.profinit.manta.graphbench.tinkerpop3.TP3Edge;
import eu.profinit.manta.graphbench.tinkerpop3.TP3Vertex;

import java.net.URL;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;


public class JanusGraphDB implements IGraphDBConnector<TP3Vertex, TP3Edge> {

	private boolean connected = false;
	private String dbName = null;
	private org.apache.commons.configuration.Configuration configuration = new BaseConfiguration();
	private JanusGraph internalGraph;
	private CassandraYaml cassandraProperties;
	private Configuration janusGraphProperties;
	private final Logger LOG = Logger.getLogger(JanusGraphDB.class);

	public JanusGraphDB() {
		cassandraProperties = CassandraYaml.getInstance();
		janusGraphProperties = JanusGraphProperties.getInstance();
	}

	private void setCassandraPaths(String dbPath) {
		cassandraProperties.setProperty(CassandraProperty.COMMITLOG_DIRECTORY, dbPath + "/" + CassandraProperty.COMMITLOG_DIRECTORY_NAME);
		cassandraProperties.setProperty(CassandraProperty.SAVED_CACHES_DIRECTORY, dbPath + "/" + CassandraProperty.SAVED_CACHES_DIRECTORY_NAME);
		cassandraProperties.setProperty(CassandraProperty.DATA_FILE_DIRECTORIES, dbPath + "/" + CassandraProperty.DATA_DIRECTORY_NAME);
	}

	private void setConfiguration(String dbPath) {
		Util.setConfiguration(configuration, janusGraphProperties);

		String cassandraYamlPath = CassandraYaml.getAbsolutePropertiesPath();
		configuration.setProperty(JanusGraphProperty.STORAGE_CONF_FILE.getName(), "file:\\\\\\" + cassandraYamlPath);

		String storageDir = Paths.get("src", "main", "resources", "storage").toFile().getAbsolutePath();
		configuration.setProperty(JanusGraphProperty.STORAGE_CASSANDRA_STORAGEDIR.getName(), storageDir);

		configuration.setProperty(JanusGraphProperty.INDEX_SEARCH_DIRECTORY.getName(), dbPath + "/" + JanusGraphProperty.INDEX_SEARCH_DIRECTORY_NAME);
	}

	private void setSchema() {
		JanusGraphManagement mgmt = internalGraph.openManagement();

		VertexLabel lbl = mgmt.makeVertexLabel("lbl").make();
		mgmt.makePropertyKey(NodeProperty.NODE_TYPE.t()).dataType(String.class).make();

		//create properties
		PropertyKey nodeName = mgmt.makePropertyKey(NodeProperty.NODE_NAME.t()).dataType(String.class).make();

		mgmt.makePropertyKey(EdgeProperty.EDGE_ATTRIBUTE.t()).dataType(String.class).make();

		EdgeLabel parLabel = mgmt.makeEdgeLabel(eu.profinit.manta.graphbench.core.db.structure.EdgeLabel.HAS_PARENT.t()).make();
		mgmt.makeEdgeLabel(eu.profinit.manta.graphbench.core.db.structure.EdgeLabel.DIRECT.t()).make();
		mgmt.makeEdgeLabel(eu.profinit.manta.graphbench.core.db.structure.EdgeLabel.FILTER.t()).make();

		mgmt.buildIndex("nodeName", Vertex.class).addKey(nodeName, Mapping.TEXT.asParameter()).buildMixedIndex("search");

		mgmt.commit();
	}

	@Override
	public void connect(String dbPath, String userName, String userPassword) {
		setCassandraPaths(dbPath);
		Util.clearDirectory(dbPath, LOG);

		setConfiguration(dbPath);
		internalGraph = JanusGraphFactory.open(configuration);

		setSchema();

		dbName = dbPath;
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
	public String getDBName() {
		return dbName;
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
	public TP3Vertex addVertex() {
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
	        ex.printStackTrace();
	        return null;
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
	public void addVertexNode(String[] parts, Translator trans) {
		TP3Vertex node = new TP3Vertex(addVertex().getVertex());

		trans.putTemp(parts[config.getIntegerProperty(ConfigProperty.NODE_I_ID)], node);

		node.property(NodeProperty.NODE_NAME.t(), parts[config.getIntegerProperty(ConfigProperty.NODE_I_NAME)]);
		node.property(NodeProperty.NODE_TYPE.t(), config.getStringProperty(ConfigProperty.VERTEX_NODE_TYPE));

		//Parent edge
		if (parts[config.getIntegerProperty(ConfigProperty.NODE_I_PARENT)].length() > 0) {
			String parentString = trans.get(parts[config.getIntegerProperty(ConfigProperty.NODE_I_PARENT)]);

			TP3Vertex parentNode = (TP3Vertex) trans.getTemp(parts[config.getIntegerProperty(ConfigProperty.NODE_I_PARENT)]);
			if(parentNode == null || parentNode.isVertexNull()) {
				parentNode = getVertex(parentString);
			}

			if (!parentNode.isVertexNull()) {
				TP3Edge edge = addEdge(node, parentNode, eu.profinit.manta.graphbench.core.db.structure.EdgeLabel.HAS_PARENT.t());
			} else {
				LOG.warn(MessageFormat.format(
						"Database didn't return a node to set a parent. Original node id: \"{0}\", new node id: \"{1}\"",
						parts[config.getIntegerProperty(ConfigProperty.NODE_I_PARENT)], node.id().toString()));
			}
		}
	}

	@Override
	 public List<TP3Vertex> getVertexByName(String name) {
	     long startTime = System.nanoTime();
	     Iterable<Result<JanusGraphVertex>> it = internalGraph.indexQuery("nodeName", "lbl." + NodeProperty.NODE_NAME.t() + ":(/" + name + "/)").vertexStream().collect(Collectors.toList());;

	     List<TP3Vertex> result = new ArrayList<TP3Vertex>();

	     for (Result<JanusGraphVertex> v : it) {
	         result.add(new TP3Vertex(v.getElement()));
	         //System.out.println(v.getElement());
	     }

	     long endTime = System.nanoTime();
	     System.out.println("getVertexByName Total time = " + (endTime - startTime));
	     return result;
	}

	// DbIterators -------------------------------------------------------------------

	abstract class JanusDbIterator<V, T> extends IGraphDBConnector.DbIterator<V, T> {

		JanusDbIterator(Iterator<T> iterator) {
			super(iterator);
		}

		@Override
		protected boolean hasNext() {
			return iterator.hasNext();
		}

	}

	class JanusDbVertexIterator extends JanusDbIterator<TP3Vertex, Vertex> {
		JanusDbVertexIterator(Iterator<Vertex> iterator) {
			super(iterator);
		}
		@Override
		protected TP3Vertex next() {
			return new TP3Vertex(iterator.next());
		}
	}

	class JanusDbEdgeIterator extends JanusDbIterator<TP3Edge, Edge> {
		JanusDbEdgeIterator(Iterator<Edge> iterator) {
			super(iterator);
		}
		@Override
		protected TP3Edge next() {
			return new TP3Edge(iterator.next());
		}
	}

}
