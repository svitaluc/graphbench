package cz.cvut.fit.manta.graphbench.tinkerpop2;

import cz.cvut.fit.manta.graphbench.core.access.IProperty;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

public class TP2VertexProperty<P> implements IProperty<P> {

    private Map<String, P> property;

    public TP2VertexProperty(String key, P value) {
        this.property = new HashMap<>();
        this.property.put(key, value);
    }

    @Override
    public P value() throws NoSuchElementException {
        return property.values().iterator().next();
    }
}
