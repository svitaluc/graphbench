package cz.cvut.fit.manta.graphbench.titan.config.model;

import cz.cvut.fit.manta.graphbench.core.config.IConfigurationProperty;

public enum TitanPomProperty implements IConfigurationProperty {
    DATABASE_VERSION("titan.version", String.class);

    private final String property;
    private final Class clazz;

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
