package cz.cvut.fit.manta.graphbench.tinkerpop3;

import cz.cvut.fit.manta.graphbench.core.access.Property;
import org.apache.tinkerpop.gremlin.structure.VertexProperty;

import java.util.NoSuchElementException;

/**
 *
 * @param <P>
 *
 * @author Lucie Svitáková (svitaluc@fit.cvut.cz)
 */
public class TP3VertexProperty<P> implements Property<P> {

    private VertexProperty<P> vertexProperty;

    public TP3VertexProperty(VertexProperty<P> vertexProperty) {
        this.vertexProperty = vertexProperty;
    }

    @Override
    public P value() throws NoSuchElementException {
        return vertexProperty.value();
    }
}
