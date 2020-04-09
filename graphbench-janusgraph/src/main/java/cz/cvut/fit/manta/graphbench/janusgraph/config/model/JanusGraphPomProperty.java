package cz.cvut.fit.manta.graphbench.janusgraph.config.model;

import cz.cvut.fit.manta.graphbench.core.config.ConfigurationProperty;

/**
 * Class representing properties set in a pom file for the graphbench-janusgraph module.
 *
 * @author Lucie Svitáková (svitaluc@fit.cvut.cz)
 */
public enum JanusGraphPomProperty implements ConfigurationProperty {
    /** Property setting a version of the JanusGraph. */
    DATABASE_VERSION("janusgraph.version", String.class);

    /** Property name. */
    private String property;
    /** Class of the property. */
    private Class<?> clazz;

    /**
     * Constructor of the {@link JanusGraphPomProperty}.
     * @param property Name of the property
     * @param clazz CLass of the property
     */
    JanusGraphPomProperty(String property, Class<?> clazz) {
        this.property = property;
        this.clazz = clazz;
    }

    @Override
    public String getName() {
        return property;
    }

    @Override
    public Class<?> getClazz() {
        return clazz;
    }
}
