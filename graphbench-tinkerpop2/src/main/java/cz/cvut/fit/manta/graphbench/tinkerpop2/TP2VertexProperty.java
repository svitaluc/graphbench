package cz.cvut.fit.manta.graphbench.tinkerpop2;

import cz.cvut.fit.manta.graphbench.core.access.Property;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 *
 * @param <P>
 *
 * @author Lucie Svitáková (svitaluc@fit.cvut.cz)
 */
public class TP2VertexProperty<P> implements Property<P> {

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
