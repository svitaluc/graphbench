package cz.cvut.fit.manta.graphbench.tinkerpop3;

import cz.cvut.fit.manta.graphbench.tinkerpop3.iterator.TP3EdgeIterator;
import cz.cvut.fit.manta.graphbench.tinkerpop3.iterator.TP3VertexIterator;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import cz.cvut.fit.manta.graphbench.core.access.IProperty;
import cz.cvut.fit.manta.graphbench.core.access.IVertex;
import cz.cvut.fit.manta.graphbench.core.access.direction.Direction;
import cz.cvut.fit.manta.graphbench.tinkerpop3.direction.TP3Direction;

public class TP3Vertex implements IVertex<Vertex> {

    private Vertex vertex;

    public TP3Vertex() {
        this.vertex = null;
    }

    public TP3Vertex(Vertex vertex) {
        this.vertex = vertex;
    }

    @Override
    public TP3VertexIterator vertices(Direction direction, String... edgeLabels) {
        org.apache.tinkerpop.gremlin.structure.Direction originalDirection = TP3Direction.mapToOriginal(direction);
        if (vertex == null) {
            return null;
        }
        return new TP3VertexIterator(vertex.vertices(originalDirection, edgeLabels));
    }

    @Override
    public TP3EdgeIterator edges(Direction direction, String... edgeLabels) {
        org.apache.tinkerpop.gremlin.structure.Direction originalDirection = TP3Direction.mapToOriginal(direction);
        if (vertex == null) {
            return null;
        }
        return new TP3EdgeIterator(vertex.edges(originalDirection, edgeLabels));
    }

    @Override
    public <P> IProperty<P> property(String key) {
        return new TP3VertexProperty<>(vertex.property(key));
    }

    public <P> IProperty<P> property(String key, P value) {
        return new TP3VertexProperty<>(vertex.property(key, value));
    }

    @Override
    public Object id() {
        return vertex.id();
    }

    @Override
    public Vertex getVertex() {
        return vertex;
    }

    @Override
    public boolean isVertexNull() {
        return vertex == null;
    }

    @Override
    public TP3Edge addEdge(String label, IVertex inVertex, Object... keyValues) {
        return new TP3Edge(vertex.addEdge(label, (Vertex)inVertex.getVertex(), keyValues));
    }
}
