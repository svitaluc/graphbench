package cz.cvut.fit.manta.graphbench.titan.config.model;

import cz.cvut.fit.manta.graphbench.core.config.ConfigurationProperty;

/**
 * Class representing properties set in a pom file for the graphbench-titan module.
 *
 * @author Lucie Svitáková (svitaluc@fit.cvut.cz)
 */
public enum TitanPomProperty implements ConfigurationProperty {
    /** Property setting a version of the Titan. */
    DATABASE_VERSION("titan.version", String.class);

    /** Property name. */
    private String property;
    /** Class of the property. */
    private Class clazz;

    /**
     * Constructor of the {@link TitanPomProperty}.
     * @param property Name of the property
     * @param clazz CLass of the property
     */
    TitanPomProperty(String property, Class clazz) {
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
