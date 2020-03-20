package cz.cvut.fit.manta.graphbench.core.db;

import cz.cvut.fit.manta.graphbench.core.access.Edge;
import cz.cvut.fit.manta.graphbench.core.access.Vertex;
import cz.cvut.fit.manta.graphbench.core.config.ConfigProperties;
import cz.cvut.fit.manta.graphbench.core.config.GraphDBConfiguration;

import java.util.List;

/**
 * Interface declaring all the methods for graph database communication.
 * @param <V> implementation of {@link Vertex}
 * @param <E> implementation of {@link Edge}
 *
 * @author Lucie Svitáková (svitaluc@fit.cvut.cz)
 */
public interface GraphDBConnector<V extends Vertex, E extends Edge> {

	/** Instance for reading properties from a config file. */
	public final ConfigProperties CONFIG = ConfigProperties.getInstance();

	public GraphDBConfiguration getGraphDBConfiguration();
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
	public V addEmptyVertex();

	/**
	 * Returns a vertex with the provided id.
	 * Note, that this method doesn't use any optimization utilities such as the {@link Translator}.
	 * Such approach is left for functions calling the {@link GraphDBConnector} methods.
	 * The local getVertex method only accesses the database and tries to acquire a vertex of a given {@code id}.
	 * @param id id of a vertex
	 * @return vertex with the given id
	 */
	public V getVertex(Object id);

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
	public void addVertex(String[] parts, Translator trans);
}
