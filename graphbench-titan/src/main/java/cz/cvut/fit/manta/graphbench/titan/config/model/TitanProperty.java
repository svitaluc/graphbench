package cz.cvut.fit.manta.graphbench.titan.config.model;

import cz.cvut.fit.manta.graphbench.core.config.ConfigurationProperty;

/**
 * Properties of the titan configuration file. The enumeration here is not full, only those properties explicitly
 * used within the code are defined.
 *
 * @author Lucie Svitáková (svitaluc@fit.cvut.cz)
 */
public enum TitanProperty implements ConfigurationProperty {

    STORAGE_BACKEND("storage.backend", String.class),
    STORAGE_DIRECTORY("storage.directory", String.class),
    STORAGE_CASSANDRA_CONFIG_DIR("storage.cassandra-config-dir", String.class),
    STORAGE_INDEX_SEARCH_DIRECTORY("storage.index.search.directory", String.class),
    STORAGE_INDEX_SEARCH_BACKEND("storage.index.search.backend", String.class),
    STORAGE_CASSANDRA_STORAGEDIR("storage.cassandra.storagedir", String.class);

    public static final String INDEX_SEARCH_DIRECTORY_NAME = "searchindex";

    private String property;
    private Class clazz;

    TitanProperty(String property, Class clazz) {
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
