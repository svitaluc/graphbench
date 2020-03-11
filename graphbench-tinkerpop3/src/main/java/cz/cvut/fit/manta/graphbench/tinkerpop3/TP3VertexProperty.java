package cz.cvut.fit.manta.graphbench.tinkerpop3;

import org.apache.tinkerpop.gremlin.structure.VertexProperty;
import cz.cvut.fit.manta.graphbench.core.access.IProperty;

import java.util.NoSuchElementException;

public class TP3VertexProperty<P> implements IProperty<P> {

    private VertexProperty<P> vertexProperty;

    public TP3VertexProperty(VertexProperty<P> vertexProperty) {
        this.vertexProperty = vertexProperty;
    }

    @Override
    public P value() throws NoSuchElementException {
        return vertexProperty.value();
    }
}
