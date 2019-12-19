package eu.profinit.manta.graphbench.core.db;

import eu.profinit.manta.graphbench.core.access.IEdge;
import eu.profinit.manta.graphbench.core.access.IVertex;
import eu.profinit.manta.graphbench.core.config.Config;
import eu.profinit.manta.graphbench.core.test.ITest;
import org.apache.commons.configuration.BaseConfiguration;
import org.apache.commons.configuration.Configuration;

import java.util.Iterator;
import java.util.List;

/**
 * Interface declaring all the methods for graph database communication.
 * @param <V> implementation of {@link IVertex}
 * @param <E> implementation of {@link IEdge}
 */
public interface IGraphDBConnector<V extends IVertex, E extends IEdge> {

	/** Instance for reading properties from a config file. */
	public Config config = Config.getInstance();

	/**
	 * Creates connection with the database.
	 * @param dbPath directory where to store the database
	 * @param userName database username
	 * @param userPassword database password
	 */
	public void connect(String dbPath, String userName, String userPassword);

	/**
	 * Closes connection with the database.
	 */
	public void disconnect();

	/**
	 * Gets name of the database.
	 * @return database name.
	 */
	public String getDBName();

	/**
	 * Executes a commit.
	 */
	public void commit();

	/**
	 * Executes a rollback.
	 */
	public void rollback();

	/**
	 * Returns information whether the database is connected or not.
	 * @return true if the database is connected, false otherwise
	 */
	public boolean isConnected();

	/**
	 * Deletes all the nodes in the database.
	 */
	public void deleteAllNodes();

	/**
	 * Calculates all the nodes in the database.
	 * @return number of nodes in the database
	 */
	public long getNodesCount();

	/**
	 * Adds an empty vertex.
	 * @return the newly added vertex
	 */
	public V addVertex();

	/**
	 * Returns a vertex with the provided id.
	 * @param id id of a vertex
	 * @return vertex with the given id
	 */
	public V getVertex(Object id);


	/**
	 * returns database configuration
	 * @return database configuration
	 */
	public org.apache.commons.configuration.Configuration getConfiguration();


	/**
	 * Adds an edge with given out and in vertex, and a label.
	 * @param outVertex outgoing vertex
	 * @param inVertex ingoing vertex
	 * @param label label of the edge
	 * @return the newly added edge
	 */
	public E addEdge(V outVertex, V inVertex, String label);

	/**
	 * Returns an edge with the provided id.
	 * @param id id of an edge
	 * @return edge with the given id
	 */
	public E getEdge(Object id);

	/**
	 * Sets property of a given edge.
	 * @param edge edge to be assigned with the new property
	 * @param name property name
	 * @param value property value
	 */
	public void setEdgeProperty(E edge, String name, Object value);

	/**
	 * Gets a list of vertices with given name.
	 * @param name name of a vertex
	 * @return list of vertices with given name
	 */
	public List<V> getVertexByName(String name);

	/**
	 * Gets a database traversal source.
	 * @return database traversal
	 */
	public <T> T getTraversal();

	/**
	 * Adds a vertex in the database.
	 * @param parts individual items of one record (one line of a csv file, already split)
	 * @param trans translator from logical to physical ids and vice versa
	 */
	public void addVertexNode(String[] parts, Translator trans);

	/**
	 * Iterator for iterating over vertices of the database.
	 * The iterator returns the vertex of the {@link IVertex} interface, but it
	 * iterates over the lower level vertices of tinkerpop2/3 framework.
	 * @param <V> vertex of the database (implementing {@link IVertex} interface)
	 * @param <T> vertex of the database (specific tinkerpop or blueprints vertex)
	 */
	abstract class DbIterator<V, T> {
		/** Iterator over vertices of the TinkerPop framework. */
		protected Iterator<T> iterator;

		protected DbIterator(){}

		protected DbIterator(Iterator<T> iterator) {
			this.iterator = iterator;
		}

		/**
		 * Returns true if this iterator has another vertex in its input.
		 * @return true if and only if this iterator has another vertex
		 */
		protected abstract boolean hasNext();

		/**
		 * Finds and returns the next vertex of this iterator.
		 * @return the next vertex
		 */
		protected abstract V next();
	}
}
