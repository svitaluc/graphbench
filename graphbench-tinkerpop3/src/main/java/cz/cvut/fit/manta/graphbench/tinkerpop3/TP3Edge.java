package cz.cvut.fit.manta.graphbench.tinkerpop3;

import cz.cvut.fit.manta.graphbench.core.access.Edge;
import cz.cvut.fit.manta.graphbench.core.access.Property;
import cz.cvut.fit.manta.graphbench.core.access.direction.Direction;
import cz.cvut.fit.manta.graphbench.tinkerpop3.direction.TP3Direction;
import cz.cvut.fit.manta.graphbench.tinkerpop3.iterator.TP3VertexIterator;

/**
 * Wrapper for an edge of a graph in the TinkerPop 2 framework.
 *
 * @author Lucie Svitáková (svitaluc@fit.cvut.cz)
 */
public class TP3Edge implements Edge<org.apache.tinkerpop.gremlin.structure.Vertex, Object> {
    /** Edge of the TinkerPop 3 framework wrapped in the {@link TP3Edge}. */
    private org.apache.tinkerpop.gremlin.structure.Edge edge;
    /** Utility for translation of local and TinkerPop 3 direction. */
    private TP3Direction tp3Direction = new TP3Direction();

    /**
     * Constructor of the {@link TP3Edge}.
     */
    public TP3Edge() {
        this.edge = null;
    }

    /**
     * Constructor of the {@link TP3Edge}.
     * @param edge TinkerPop 3 edge that is wrapped in the {@link TP3Edge}
     */
    public TP3Edge(org.apache.tinkerpop.gremlin.structure.Edge edge) {
        this.edge = edge;
    }

    @Override
    public TP3VertexIterator vertices(Direction direction) {
        org.apache.tinkerpop.gremlin.structure.Direction originalDirection = tp3Direction.mapToOriginal(direction);
        return new TP3VertexIterator(edge.vertices(originalDirection));
    }

    @Override
    public <P> Property<P> property(String key) {
        return new TP3Property<>(edge.property(key));
    }

    @Override
    public <P> Property<P> property(String name, P value) {
        return new TP3Property<>(edge.property(name, value));
    }

    @Override
    public TP3Vertex inVertex() {
        return new TP3Vertex(edge.inVertex());
    }

    @Override
    public Object getId() {
        return edge.id();
    }
}
