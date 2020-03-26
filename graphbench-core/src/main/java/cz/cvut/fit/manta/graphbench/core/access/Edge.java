package cz.cvut.fit.manta.graphbench.core.access;

import cz.cvut.fit.manta.graphbench.core.access.direction.Direction;
import cz.cvut.fit.manta.graphbench.core.access.iterator.VertexIterator;

/**
 * Interface representing an edge of a graph.
 * @param <V> Vertex type of a concrete language used
 * @param <I> Class type of the edge id
 *
 * @author Lucie Svitáková (svitaluc@fit.cvut.cz)
 */
public interface Edge<V, I> extends Element<I> {
    /**
     * Retrieve the vertex (or vertices) associated with this edge as defined by the direction.
     * If the direction is {@link Direction#BOTH} then the iterator order is:
     * {@link Direction#OUT} then {@link Direction#IN}.
     * @param direction get the incoming vertex, outgoing vertex, or both vertices
     * @return an iterator with 1 or 2 vertices
     */
    VertexIterator<?,?> vertices(final Direction direction);

    /**
     * Get the {@link Property} for the provided key. It calls the corresponding method of the specific
     * TinkerPop vertex.
     * @param key the key of the edge property to get
     * @param <P> the type of the edge property value
     * @return the retrieved edge property
     */
    <P> Property<P> property(final String key);

    /**
     * Set the provided key to the provided value.
     * @param key the key of the edge property
     * @param value the value of the edge property
     * @param <P> the type of the value of the edge property
     * @return the newly created property
     */
    <P> Property<P> property(String key, P value);

    /**
     * Returns the incoming vertex of the edge.
     * @return the incoming vertex of the edge
     */
    Vertex<V, I> inVertex();
}
