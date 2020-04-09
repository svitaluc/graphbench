package cz.cvut.fit.manta.graphbench.core.db;

import cz.cvut.fit.manta.graphbench.core.access.Edge;
import cz.cvut.fit.manta.graphbench.core.access.Vertex;
import cz.cvut.fit.manta.graphbench.core.config.GraphDBConfiguration;
import org.apache.log4j.Logger;

import java.util.List;

/**
 * Basic implementation of the {@link GraphDBConnector} that contains an attribute of another
 * {@link GraphDBConnector} instance.
 * The reason is that this class serves as a decorator
 * (without a decorator interface that would only extend the {@link GraphDBConnector} and wouldn't
 * add any other methods). It adds behavior such as a check whether the database is connected, or logging.
 *
 * @author Lucie Svitáková (svitaluc@fit.cvut.cz)
 */
public class GraphDBCommonImpl implements GraphDBConnector<Vertex<?,?>, Edge<?,?>> {

	/** Flag whether the database is connected. */
	private boolean connected = false;
	/** Instance of a specific database connector. */
	private GraphDBConnector<Vertex<?,?>, Edge<?,?>> iFace;
	/** Logger. */
	private final static Logger LOG = Logger.getLogger(GraphDBCommonImpl.class);

	/**
	 * Constructor of the {@link GraphDBCommonImpl}.
	 * @param db Instance of a specific database connector.
	 */
	public GraphDBCommonImpl(GraphDBConnector<Vertex<?,?>, Edge<?,?>> db) {
		iFace = db;
	}

	@Override
	public GraphDBConfiguration getGraphDBConfiguration() {
		return iFace.getGraphDBConfiguration();
	}

	@Override
	public void connect(String dbPath, String userName, String userPassword) {
		if (connected) { throw new IllegalStateException("Tried to connect already connected DB."); }
		iFace.connect(dbPath, userName, userPassword);
		connected = true;
	}

	@Override
	public void disconnect() {
		if (connected) {
			LOG.debug("Disconnecting the graph database.");
			iFace.disconnect();
		} else {
			throw new IllegalStateException("Tried to disconnect already disconnected DB.");
		}
		connected = false;
	}

	@Override
	public String getDBPath() {
		return iFace.getDBPath();
	}

	@Override
	public void commit() {
		if (connected) {
			LOG.debug("Committing in the graph database.");
			iFace.commit();
		} else {
			throw new IllegalStateException("Tried to commit in disconnected DB.");
		}
	}

	@Override
	public void rollback() {
		if (connected) {
			LOG.debug("Rollback of the graph database.");
			iFace.rollback();
		} else {
			throw new IllegalStateException("Tried to rollback in disconnected DB.");
		}
	}

	@Override
	public boolean isConnected() {
		return connected;
	}

	@Override
	public void deleteAllNodes() {
		if (connected) {
			LOG.debug("Deleting all nodes in the graph database.");
			iFace.deleteAllNodes();
		} else {
			throw new IllegalStateException("Tried to delete all nodes in disconnected DB.");
		}
	}

	@Override
	public long getNodesCount() {
		if (connected) {
			LOG.debug("Receiving the number of nodes in the graph database.");
			return iFace.getNodesCount();
		} else {
			throw new IllegalStateException("Tried to count nodes in disconnected DB.");
		}
	}

	@Override
	public Vertex<?,?> addEmptyVertex() {
		if (connected) {
			return iFace.addEmptyVertex();
		} else {
			throw new IllegalStateException("Tried to add vertex in disconnected DB.");
		}
	}

	@Override
	public Vertex<?,?> getVertex(Object id) {
		if (connected) {
			return iFace.getVertex(id);
		} else {
			throw new IllegalStateException("Tried to get vertex in disconnected DB.");
		}
	}

	@Override
	public Edge<?,?> addEdge(Vertex<?,?> outVertex, Vertex<?,?> inVertex, String label) {
		if (connected) {
			return iFace.addEdge(outVertex, inVertex, label);
		} else {
			throw new IllegalStateException("Tried to add edge in disconnected DB.");
		}
	}

	@Override
	public Edge<?,?> getEdge(Object id) {
		if (connected) {
			return iFace.getEdge(id);
		} else {
			throw new IllegalStateException("Tried to get edge in disconnected DB.");
		}
	}

	@Override
	public void setEdgeProperty(Edge<?,?> edge, String name, Object value) {
		if (connected) {
			iFace.setEdgeProperty(edge, name, value);
		} else {
			throw new IllegalStateException("Tried to set edge property in disconnected DB.");
		}
	}

    @Override
    public Object getTraversal() {
        if (connected) {
            return iFace.getTraversal();
        } else {
            throw new IllegalStateException("Tried to get edge in disconnected DB.");
        }
    }

	@Override
	public void addVertex(String[] parts, Translator trans) {
		if (connected) {
			iFace.addVertex(parts, trans);
		} else {
			throw new IllegalStateException("Tried to add a vertex node in disconnected DB.");
		}
	}

	@Override
    public List<Vertex<?,?>> getVertexByName(String name) {
        if (connected) {
            return iFace.getVertexByName(name);
        } else {
            throw new IllegalStateException("Tried to get edge in disconnected DB.");
        }
    }

}
