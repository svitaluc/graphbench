package cz.cvut.fit.manta.graphbench.titan.config.model;

import cz.cvut.fit.manta.graphbench.core.config.ConfigurationProperty;

/**
 *
 *
 * @author Lucie Svitáková (svitaluc@fit.cvut.cz)
 */
public enum TitanPomProperty implements ConfigurationProperty {
    DATABASE_VERSION("titan.version", String.class);

    private String property;
    private Class clazz;

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
