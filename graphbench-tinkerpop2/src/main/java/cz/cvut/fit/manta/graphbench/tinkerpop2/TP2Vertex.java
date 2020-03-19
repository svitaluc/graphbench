package cz.cvut.fit.manta.graphbench.tinkerpop2;

import com.tinkerpop.blueprints.Edge;
import org.apache.log4j.Logger;
import cz.cvut.fit.manta.graphbench.core.access.Property;
import cz.cvut.fit.manta.graphbench.core.access.Vertex;
import cz.cvut.fit.manta.graphbench.core.access.direction.Direction;
import cz.cvut.fit.manta.graphbench.tinkerpop2.direction.TP2Direction;
import cz.cvut.fit.manta.graphbench.tinkerpop2.iterator.TP2EdgeIterator;
import cz.cvut.fit.manta.graphbench.tinkerpop2.iterator.TP2VertexIterator;

/**
 *
 *
 * @author Lucie Svitáková (svitaluc@fit.cvut.cz)
 */
public class TP2Vertex implements Vertex<com.tinkerpop.blueprints.Vertex, Object> {

    private com.tinkerpop.blueprints.Vertex vertex;
    private final static Logger logger = Logger.getLogger(TP2Vertex.class);
    private TP2Direction tp2Direction = new TP2Direction();

    public TP2Vertex() {
        this.vertex = null;
    }

    public TP2Vertex(com.tinkerpop.blueprints.Vertex vertex) {
        this.vertex = vertex;
    }

    @Override
    public TP2VertexIterator vertices(Direction direction, String... edgeLabels) {
        com.tinkerpop.blueprints.Direction originalDirection = tp2Direction.mapToOriginal(direction);
        if (vertex == null) {
            return null;
        }
        return new TP2VertexIterator(vertex.getVertices(originalDirection, edgeLabels));
    }

    @Override
    public TP2EdgeIterator edges(Direction direction, String... edgeLabels) {
        com.tinkerpop.blueprints.Direction originalDirection = tp2Direction.mapToOriginal(direction);
        if (vertex == null) {
            return null;
        }
        return new TP2EdgeIterator(vertex.getEdges(originalDirection, edgeLabels));
    }

    @Override
    public <P> Property<P> property(String key) {
        return new TP2VertexProperty<>(key, vertex.getProperty(key));
    }

    @Override
    public <P> Property<P> property(String key, P value) {
        vertex.setProperty(key, value);
        return new TP2VertexProperty<>(key, vertex.getProperty(key));
    }

    @Override
    public boolean isVertexNull() {
        return vertex == null;
    }

    @Override
    public TP2Edge addEdge(String label, Vertex<com.tinkerpop.blueprints.Vertex, Object> inVertex, Object... keyValues) {
        Edge edge = vertex.addEdge(label, inVertex.getVertex());

        if (keyValues != null && keyValues.length % 2 == 1) {
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
    public com.tinkerpop.blueprints.Vertex getVertex() {
        return vertex;
    }

    @Override
    public Object getId() {
        return vertex.getId();
    }
}
