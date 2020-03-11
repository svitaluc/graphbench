package cz.cvut.fit.manta.graphbench.tinkerpop2;

import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import cz.cvut.fit.manta.graphbench.core.access.IEdge;
import cz.cvut.fit.manta.graphbench.core.access.IProperty;
import cz.cvut.fit.manta.graphbench.core.access.IVertex;
import cz.cvut.fit.manta.graphbench.core.access.direction.Direction;
import cz.cvut.fit.manta.graphbench.tinkerpop2.direction.TP2Direction;
import cz.cvut.fit.manta.graphbench.tinkerpop2.iterator.TP2VertexIterator;

import java.util.Iterator;
import java.util.stream.Stream;

public class TP2Edge implements IEdge<Vertex> {

    private Edge edge;

    public TP2Edge() {
        this.edge = null;
    }

    public TP2Edge(Edge edge) {
        this.edge = edge;
    }

    @Override
    public TP2VertexIterator vertices(Direction direction) {
        return new TP2VertexIterator(getIterableOfEdgeVertices(edge, direction));
    }

    @Override
    public <P> IProperty<P> property(String key) {
        return new TP2Property<>(key, edge.getProperty(key));
    }

    @Override
    public <P> IProperty<P> property(String name, P value) {
        edge.setProperty(name,value);
        return new TP2Property<>(name, edge.getProperty(name));
    }

    @Override
    public IVertex inVertex() {
        return new TP2Vertex(edge.getVertex(com.tinkerpop.blueprints.Direction.IN));
    }

    @Override
    public Object id() {
        return edge.getId();
    }

    private Iterable<Vertex> getIterableOfEdgeVertices(Edge edge, Direction direction) {
        Iterator<Vertex> iterator;
        com.tinkerpop.blueprints.Direction originalDirection = TP2Direction.mapToOriginal(direction);

        if(direction.equals(Direction.BOTH)) {
            iterator = Stream.of(
                    edge.getVertex(com.tinkerpop.blueprints.Direction.OUT),
                    edge.getVertex(com.tinkerpop.blueprints.Direction.IN)).iterator();
        } else {
            iterator = Stream.of(edge.getVertex(originalDirection)).iterator();
        }
        return () -> iterator;
    }
}
