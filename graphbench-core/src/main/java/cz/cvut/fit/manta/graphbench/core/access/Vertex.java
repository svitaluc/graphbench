package cz.cvut.fit.manta.graphbench.core.access;

import cz.cvut.fit.manta.graphbench.core.access.direction.Direction;
import cz.cvut.fit.manta.graphbench.core.access.iterator.EdgeIterator;
import cz.cvut.fit.manta.graphbench.core.access.iterator.VertexIterator;

/**
 * Interface representing any vertex, no matter from which version of the TinkerPop framework.
 * @param <V> Vertex of the specific language used
 * @param <I> Class type of the vertex id
 *
 * @author Lucie Svitáková (svitaluc@fit.cvut.cz)
 */
public interface Vertex<V, I> extends Element<I> {
    /**
     * Gets an {@link VertexIterator} of adjacent vertices.
     * @param direction in which direction the adjacent vertices should be acquired
     * @param edgeLabels only edges with these edge labels should be traversed when acquiring the adjacent vertices.
     *                   If no labels are provided, then get all edges.
     * @return An iterator of vertices meeting the provided specification.
     */
    VertexIterator vertices(final Direction direction, final String... edgeLabels);

    /**
     * Gets an {@link EdgeIterator} of incident edges.
     * @param direction edges of that direction should be acquired.
     * @param edgeLabels only edges with these edge labels should be acquired. If no labels are provided,
     *                   then get all edges.
     * @return An iterator of edges meeting the provided specification.
     */
    EdgeIterator edges(final Direction direction, final String... edgeLabels);

    /**
     * Get the {@link Property} for the provided key. It calls the corresponding method of the specific
     * TinkerPop vertex.
     * @param key the key of the vertex property to get
     * @param <P> the type of the vertex property value
     * @return the retrieved vertex property
     */
    <P> Property<P> property(final String key);

    /**
     * Set the provided key to the provided value.
     * @param key the key of the vertex property
     * @param value the value of the vertex property
     * @param <P> the type of the value of the vertex property
     * @return the newly created property
     */
    <P> Property<P> property(String key, P value);

    /**
     * Returns true if the provided vertex wrapping a TinkerPop vertex has this TP vertex equal to null.
     * @return true if the TinkerPop vertex is null otherwise false
     */
    boolean isVertexNull();

    /**
     * Add an outgoing edge to the vertex with provided label and edge properties as key/value pairs.
     * These key/values must be provided in an even number where the odd numbered arguments are {@link String}
     * property keys and the even numbered arguments are the related property values.
     * @param label the label of the edge
     * @param inVertex the vertex to receive an incoming edge from the current vertex
     * @param keyValues the key/value pairs to turn into edge properties
     * @return the newly created edge
     */
    Edge<V, I> addEdge(String label, Vertex<V, I> inVertex, Object... keyValues);

    /**
     * Gets the wrapped vertex of the specific TinkerPop version.
     * @return vertex of the specific TinkerPop implementation
     */
    V getVertex();
}
