package eu.profinit.manta.graphbench.tinkerpop3;

import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import eu.profinit.manta.graphbench.core.access.IEdge;
import eu.profinit.manta.graphbench.core.access.IProperty;
import eu.profinit.manta.graphbench.core.access.IVertex;
import eu.profinit.manta.graphbench.core.access.direction.Direction;
import eu.profinit.manta.graphbench.tinkerpop3.direction.TP3Direction;
import eu.profinit.manta.graphbench.tinkerpop3.iterator.TP3VertexIterator;

public class TP3Edge implements IEdge<Vertex> {

    private Edge edge;

    public TP3Edge() {
        this.edge = null;
    }

    public TP3Edge(Edge edge) {
        this.edge = edge;
    }

    @Override
    public TP3VertexIterator vertices(Direction direction) {
        org.apache.tinkerpop.gremlin.structure.Direction originalDirection = TP3Direction.mapToOriginal(direction);
        return new TP3VertexIterator(edge.vertices(originalDirection));
    }

    @Override
    public <P> IProperty<P> property(String key) {
        return new TP3Property<>(edge.property(key));
    }

    @Override
    public <P> IProperty<P> property(String name, P value) {
        return new TP3Property<>(edge.property(name, value));
    }

    @Override
    public IVertex inVertex() {
        return new TP3Vertex(edge.inVertex());
    }

    @Override
    public Object id() {
        return edge.id();
    }
}
