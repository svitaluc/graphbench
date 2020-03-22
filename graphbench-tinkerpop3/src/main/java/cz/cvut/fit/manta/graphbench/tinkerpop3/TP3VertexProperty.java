package cz.cvut.fit.manta.graphbench.tinkerpop3;

import cz.cvut.fit.manta.graphbench.core.access.Property;
import org.apache.tinkerpop.gremlin.structure.VertexProperty;

import java.util.NoSuchElementException;

/**
 * Wrapper for a vertex property of the TinkerPop 3 framework.
 *
 * @param <P> Type of the property value
 *
 * @author Lucie Svitáková (svitaluc@fit.cvut.cz)
 */
public class TP3VertexProperty<P> implements Property<P> {

    /** Vertex property of the TinkerPop 3 framework that is wrapped by the {@link TP3VertexProperty}. */
    private VertexProperty<P> vertexProperty;

    /**
     * Constructor of the {@link TP3VertexProperty}.
     * @param vertexProperty Vertex property of the TinkerPop 3 framework that will be
     *                      wrapped by the {@link TP3VertexProperty}
     */
    public TP3VertexProperty(VertexProperty<P> vertexProperty) {
        this.vertexProperty = vertexProperty;
    }

    @Override
    public P value() throws NoSuchElementException {
        return vertexProperty.value();
    }
}
