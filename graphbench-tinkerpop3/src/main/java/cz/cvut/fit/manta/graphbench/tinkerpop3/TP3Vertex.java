package cz.cvut.fit.manta.graphbench.tinkerpop3;

import cz.cvut.fit.manta.graphbench.core.access.Property;
import cz.cvut.fit.manta.graphbench.core.access.Vertex;
import cz.cvut.fit.manta.graphbench.core.access.direction.Direction;
import cz.cvut.fit.manta.graphbench.tinkerpop3.direction.TP3Direction;
import cz.cvut.fit.manta.graphbench.tinkerpop3.iterator.TP3EdgeIterator;
import cz.cvut.fit.manta.graphbench.tinkerpop3.iterator.TP3VertexIterator;

/**
 * Wrapper for a vertex of the TinkerPop 3 framework.
 *
 * @author Lucie Svitáková (svitaluc@fit.cvut.cz)
 */
public class TP3Vertex implements Vertex<org.apache.tinkerpop.gremlin.structure.Vertex, Object> {

    /** Vertex of the TinkerPop 3 framework wrapped by the {@link TP3Vertex} */
    private org.apache.tinkerpop.gremlin.structure.Vertex vertex;
    /** Utility for translation of local and TinkerPop 3 direction. */
    private TP3Direction tp3Direction = new TP3Direction();

    /**
     * Constructor of the {@link TP3Vertex}.
     */
    public TP3Vertex() {
        this.vertex = null;
    }

    /**
     * Constructor of the {@link TP3Vertex}.
     * @param vertex Vertex of the TinkerPop 3 framework that will be wrapped by the {@link TP3Vertex}
     */
    public TP3Vertex(org.apache.tinkerpop.gremlin.structure.Vertex vertex) {
        this.vertex = vertex;
    }

    @Override
    public TP3VertexIterator vertices(Direction direction, String... edgeLabels) {
        org.apache.tinkerpop.gremlin.structure.Direction originalDirection = tp3Direction.mapToOriginal(direction);
        if (vertex == null) {
            return null;
        }
        return new TP3VertexIterator(vertex.vertices(originalDirection, edgeLabels));
    }

    @Override
    public TP3EdgeIterator edges(Direction direction, String... edgeLabels) {
        org.apache.tinkerpop.gremlin.structure.Direction originalDirection = tp3Direction.mapToOriginal(direction);
        if (vertex == null) {
            return null;
        }
        return new TP3EdgeIterator(vertex.edges(originalDirection, edgeLabels));
    }

    @Override
    public <P> Property<P> property(String key) {
        return new TP3VertexProperty<>(vertex.property(key));
    }

    public <P> Property<P> property(String key, P value) {
        return new TP3VertexProperty<>(vertex.property(key, value));
    }

    @Override
    public Object getId() {
        return vertex.id();
    }

    @Override
    public org.apache.tinkerpop.gremlin.structure.Vertex getVertex() {
        return vertex;
    }

    @Override
    public boolean isVertexNull() {
        return vertex == null;
    }

    @Override
    public TP3Edge addEdge(String label, Vertex<org.apache.tinkerpop.gremlin.structure.Vertex, Object> inVertex,
                           Object... keyValues) {
        return new TP3Edge(vertex.addEdge(label, inVertex.getVertex(), keyValues));
    }
}
