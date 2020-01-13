package eu.profinit.manta.graphbench.core.db;

import eu.profinit.manta.graphbench.core.access.IEdge;
import eu.profinit.manta.graphbench.core.access.IVertex;
import eu.profinit.manta.graphbench.core.config.GraphDBConfiguration;
import org.apache.commons.configuration.Configuration;

import java.util.List;


public class GraphDBCommonImpl implements IGraphDBConnector<IVertex, IEdge> {

	private boolean connected = false;
	private IGraphDBConnector<IVertex, IEdge> iFace;

	public GraphDBCommonImpl(IGraphDBConnector<IVertex, IEdge> db) {
		iFace = db;
	}

	@Override
	public GraphDBConfiguration getGraphDBConfiguration() {
		return iFace.getGraphDBConfiguration();
	}

	@Override
	public void connect(String dbPath, String userName, String userPassword) {
		//Username a Password neni pri plocal pripojeni vyzadovano
		if (connected) { throw new IllegalStateException("Tried to connect already connected DB."); }
		iFace.connect(dbPath, userName, userPassword);
		connected = true;
	}

	@Override
	public void disconnect() {
		if (connected) {
			iFace.disconnect();
		} else {
			throw new IllegalStateException("Tried to disconnect already disconnected DB.");
		}
		connected = false;
	}

	@Override
	public String getDBName() {
		return iFace.getDBName();
	}

	@Override
	public void commit() {
		if (connected) {
			iFace.commit();
		} else {
			throw new IllegalStateException("Tried to commit in disconnected DB.");
		}
	}

	@Override
	public void rollback() {
		if (connected) {
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
			iFace.deleteAllNodes();
		} else {
			throw new IllegalStateException("Tried to delete all nodes in disconnected DB.");
		}
	}

	@Override
	public long getNodesCount() {
		if (connected) {
			return iFace.getNodesCount();
		} else {
			throw new IllegalStateException("Tried to count nodes in disconnected DB.");
		}
	}

	@Override
	public IVertex addVertex() {
		if (connected) {
			return iFace.addVertex();
		} else {
			throw new IllegalStateException("Tried to add vertex in disconnected DB.");
		}
	}

	@Override
	public IVertex getVertex(Object id) {
		if (connected) {
			return iFace.getVertex(id);
		} else {
			throw new IllegalStateException("Tried to get vertex in disconnected DB.");
		}
	}

//	@Override
//	public void setVertexProperty(IVertex vertex, String name, Object value) {
//		if (connected) {
//			vertex.property(name, value);
//		} else {
//			throw new IllegalStateException("Tried to set vertex property in disconnected DB.");
//		}
//	}

	@Override
	public IEdge addEdge(IVertex outVertex, IVertex inVertex, String label) {
		if (connected) {
			return iFace.addEdge(outVertex, inVertex, label);
		} else {
			throw new IllegalStateException("Tried to add edge in disconnected DB.");
		}
	}

	@Override
	public IEdge getEdge(Object id) {
		if (connected) {
			return iFace.getEdge(id);
		} else {
			throw new IllegalStateException("Tried to get edge in disconnected DB.");
		}
	}

	@Override
	public void setEdgeProperty(IEdge edge, String name, Object value) {
		if (connected) {
			iFace.setEdgeProperty(edge, name, value);
		} else {
			throw new IllegalStateException("Tried to set edge property in disconnected DB.");
		}
	}

    @Override
    public <T> T getTraversal() {
        if (connected) {
            return iFace.getTraversal();
        } else {
            throw new IllegalStateException("Tried to get edge in disconnected DB.");
        }
    }

	@Override
	public void addVertexNode(String[] parts, Translator trans) {
		if (connected) {
			iFace.addVertexNode(parts, trans);
		} else {
			throw new IllegalStateException("Tried to add a vertex node in disconnected DB.");
		}
	}

	@Override
    public List<IVertex> getVertexByName(String name) {
        if (connected) {
            return iFace.getVertexByName(name);
        } else {
            throw new IllegalStateException("Tried to get edge in disconnected DB.");
        }
    }

	public IGraphDBConnector<IVertex, IEdge> getiFace()
	{
		return iFace;
	}

}
