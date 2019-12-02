package eu.profinit.manta.graphbench.tinkerpop2;

import eu.profinit.manta.graphbench.core.access.IProperty;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

public class TP2Property<P> implements IProperty<P> {

    private Map<String, P> property;

    public TP2Property(String key, P value) {
        this.property = new HashMap<>();
        this.property.put(key, value);
    }

    @Override
    public P value() throws NoSuchElementException {
        return property.values().iterator().next();
    }
}
