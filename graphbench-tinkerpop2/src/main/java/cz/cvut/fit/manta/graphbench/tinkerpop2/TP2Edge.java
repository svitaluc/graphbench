package cz.cvut.fit.manta.graphbench.tinkerpop2;

import cz.cvut.fit.manta.graphbench.core.access.Edge;
import cz.cvut.fit.manta.graphbench.core.access.Property;
import cz.cvut.fit.manta.graphbench.core.access.direction.Direction;
import cz.cvut.fit.manta.graphbench.tinkerpop2.direction.TP2Direction;
import cz.cvut.fit.manta.graphbench.tinkerpop2.iterator.TP2VertexIterator;

import java.util.Iterator;
import java.util.stream.Stream;

/**
 * Wrapper for an edge of a graph in the TinkerPop 2 framework.
 *
 * @author Lucie Svitáková (svitaluc@fit.cvut.cz)
 */
public class TP2Edge implements Edge<com.tinkerpop.blueprints.Vertex, Object> {
    /** Edge of the TinkerPop 2 framework wrapped in the {@link TP2Edge}. */
    private com.tinkerpop.blueprints.Edge edge;
    /** Utility for translation of local and TinkerPop 2 direction. */
    private TP2Direction tp2Direction = new TP2Direction();

    /**
     * Constructor of the {@link TP2Edge}.
     */
    public TP2Edge() {
        this.edge = null;
    }

    /**
     * Constructor of the {@link TP2Edge}.
     * @param edge TinkerPop 2 edge that is wrapped in the {@link TP2Edge}
     */
    public TP2Edge(com.tinkerpop.blueprints.Edge edge) {
        this.edge = edge;
    }

    @Override
    public TP2VertexIterator vertices(Direction direction) {
        return new TP2VertexIterator(getIterableOfEdgeVertices(edge, direction));
    }

    @Override
    public <P> Property<P> property(String key) {
        return new TP2Property<>(key, edge.getProperty(key));
    }

    @Override
    public <P> Property<P> property(String name, P value) {
        edge.setProperty(name,value);
        return new TP2Property<>(name, edge.getProperty(name));
    }

    @Override
    public TP2Vertex inVertex() {
        return new TP2Vertex(edge.getVertex(com.tinkerpop.blueprints.Direction.IN));
    }

    @Override
    public Object getId() {
        return edge.getId();
    }

    /**
     * Gets iterable of vertices on both sides of the edge.
     * @param edge The edge which vertices should be in the {@link Iterable}
     * @param direction {@link Direction} in which the vertices should be summoned
     * @return {@link Iterable} of the {@code edge} vertices in a given {@code direction}
     */
    private Iterable<com.tinkerpop.blueprints.Vertex> getIterableOfEdgeVertices(com.tinkerpop.blueprints.Edge edge,
                                                                                Direction direction) {
        Iterator<com.tinkerpop.blueprints.Vertex> iterator;
        com.tinkerpop.blueprints.Direction originalDirection = tp2Direction.mapToOriginal(direction);

        if (direction.equals(Direction.BOTH)) {
            iterator = Stream.of(
                    edge.getVertex(com.tinkerpop.blueprints.Direction.OUT),
                    edge.getVertex(com.tinkerpop.blueprints.Direction.IN)).iterator();
        } else {
            iterator = Stream.of(edge.getVertex(originalDirection)).iterator();
        }
        return () -> iterator;
    }
}
