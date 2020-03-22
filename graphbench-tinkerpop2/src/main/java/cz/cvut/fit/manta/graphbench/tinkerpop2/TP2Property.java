package cz.cvut.fit.manta.graphbench.tinkerpop2;

import cz.cvut.fit.manta.graphbench.core.access.Property;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Wrapper for a property of the TinkerPop 2 framework.
 * @param <P> Type of the property value
 *
 * @author Lucie Svitáková (svitaluc@fit.cvut.cz)
 */
public class TP2Property<P> implements Property<P> {

    /** Map of keeping the property as a value with the map key being the key of the property */
    private Map<String, P> property;

    /**
     * Constructor of the {@link TP2Property}.
     * @param key Key of the property
     * @param value Value of the property
     */
    public TP2Property(String key, P value) {
        this.property = new HashMap<>();
        this.property.put(key, value);
    }

    @Override
    public P value() throws NoSuchElementException {
        return property.values().iterator().next();
    }
}
