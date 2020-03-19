package cz.cvut.fit.manta.graphbench.janusgraph.config.model;

import cz.cvut.fit.manta.graphbench.core.config.ConfigurationProperty;

/**
 *
 *
 * @author Lucie Svitáková (svitaluc@fit.cvut.cz)
 */
public enum JanusGraphPomProperty implements ConfigurationProperty {
    DATABASE_VERSION("janusgraph.version", String.class);

    private final String property;
    private final Class clazz;

    JanusGraphPomProperty(String property, Class clazz) {
        this.property = property;
        this.clazz = clazz;
    }

    @Override
    public String getName() {
        return property;
    }

    @Override
    public Class getClazz() {
        return clazz;
    }
}
