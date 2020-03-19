package cz.cvut.fit.manta.graphbench.tinkerpop3;

import cz.cvut.fit.manta.graphbench.core.access.Edge;
import cz.cvut.fit.manta.graphbench.core.access.Property;
import cz.cvut.fit.manta.graphbench.core.access.Vertex;
import cz.cvut.fit.manta.graphbench.core.access.direction.Direction;
import cz.cvut.fit.manta.graphbench.tinkerpop3.direction.TP3Direction;
import cz.cvut.fit.manta.graphbench.tinkerpop3.iterator.TP3VertexIterator;

/**
 *
 *
 * @author Lucie Svitáková (svitaluc@fit.cvut.cz)
 */
public class TP3Edge implements Edge<org.apache.tinkerpop.gremlin.structure.Vertex, Object> {

    private org.apache.tinkerpop.gremlin.structure.Edge edge;
    private TP3Direction tp3Direction = new TP3Direction();

    public TP3Edge() {
        this.edge = null;
    }

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
