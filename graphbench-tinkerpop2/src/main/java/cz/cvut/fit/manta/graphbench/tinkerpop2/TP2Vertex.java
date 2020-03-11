package cz.cvut.fit.manta.graphbench.tinkerpop2;

import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import org.apache.log4j.Logger;
import cz.cvut.fit.manta.graphbench.core.access.IProperty;
import cz.cvut.fit.manta.graphbench.core.access.IVertex;
import cz.cvut.fit.manta.graphbench.core.access.direction.Direction;
import cz.cvut.fit.manta.graphbench.tinkerpop2.direction.TP2Direction;
import cz.cvut.fit.manta.graphbench.tinkerpop2.iterator.TP2EdgeIterator;
import cz.cvut.fit.manta.graphbench.tinkerpop2.iterator.TP2VertexIterator;

public class TP2Vertex implements IVertex<Vertex> {

    private Vertex vertex;
    private final Logger logger = Logger.getLogger(TP2Vertex.class);

    public TP2Vertex() {
        this.vertex = null;
    }

    public TP2Vertex(Vertex vertex) {
        this.vertex = vertex;
    }

    @Override
    public TP2VertexIterator vertices(Direction direction, String... edgeLabels) {
        com.tinkerpop.blueprints.Direction originalDirection = TP2Direction.mapToOriginal(direction);
        if (vertex == null) {
            return null;
        }
        return new TP2VertexIterator(vertex.getVertices(originalDirection, edgeLabels));
    }

    @Override
    public TP2EdgeIterator edges(Direction direction, String... edgeLabels) {
        com.tinkerpop.blueprints.Direction originalDirection = TP2Direction.mapToOriginal(direction);
        if (vertex == null) {
            return null;
        }
        return new TP2EdgeIterator(vertex.getEdges(originalDirection, edgeLabels));
    }

    @Override
    public <P> IProperty<P> property(String key) {
        return new TP2VertexProperty<>(key, vertex.getProperty(key));
    }

    @Override
    public <P> IProperty<P> property(String key, P value) {
        vertex.setProperty(key, value);
        return new TP2VertexProperty<>(key, vertex.getProperty(key));
    }

    @Override
    public boolean isVertexNull() {
        return vertex == null;
    }

    @Override
    public TP2Edge addEdge(String label, IVertex inVertex, Object... keyValues) {
        Edge edge = vertex.addEdge(label, (Vertex)inVertex.getVertex());

        if(keyValues != null && keyValues.length % 2 == 1) {
            logger.error("Invalid edge properties. No properties for edge " + label + " were stored.");
            return new TP2Edge(edge);
        }
        if (keyValues != null) {
            for (int i = 0; i < keyValues.length; i += 2) {
                edge.setProperty(keyValues[i].toString(), keyValues[i + 1]);
            }
        }

        return new TP2Edge(edge);
    }

    @Override
    public Vertex getVertex() {
        return vertex;
    }

    @Override
    public Object id() {
        return vertex.getId();
    }
}
