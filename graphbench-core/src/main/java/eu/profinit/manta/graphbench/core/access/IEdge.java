package eu.profinit.manta.graphbench.core.access;

import eu.profinit.manta.graphbench.core.access.direction.Direction;
import eu.profinit.manta.graphbench.core.access.iterator.IVertexIterator;

/**
 * Interface representing an edge of a graph.
 * @param <V> vertex type of the TinkerPop framework which is connected with the {@link IEdge}
 */
public interface IEdge<V> extends IElement {
    /**
     * Retrieve the vertex (or vertices) associated with this edge as defined by the direction.
     * If the direction is {@link Direction#BOTH} then the iterator order is:
     * {@link Direction#OUT} then {@link Direction#IN}.
     * @param direction get the incoming vertex, outgoing vertex, or both vertices
     * @return an iterator with 1 or 2 vertices
     */
    IVertexIterator vertices(final Direction direction);

    /**
     * Get the {@link IProperty} for the provided key. It calls the corresponding method of the specific
     * TinkerPop vertex.
     * @param key the key of the edge property to get
     * @param <P> the type of the edge property value
     * @return the retrieved edge property
     */
    <P> IProperty<P> property(final String key);

    /**
     * Set the provided key to the provided value.
     * @param key the key of the edge property
     * @param value the value of the edge property
     * @param <P> the type of the value of the edge property
     * @return the newly created property
     */
    <P> IProperty<P> property(String key, P value);

    /**
     * Returns the incoming vertex of the edge.
     * @return the incoming vertex of the edge
     */
    IVertex inVertex();
}
