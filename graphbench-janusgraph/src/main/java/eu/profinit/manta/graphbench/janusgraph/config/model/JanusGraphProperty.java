package eu.profinit.manta.graphbench.janusgraph.config.model;

import eu.profinit.manta.graphbench.core.config.IConfigurationProperty;

/**
 * Properties of the janusGraph configuration file. The enumeration here is not full, only those properties explicitly
 * used within the code are defined.
 */
public enum JanusGraphProperty implements IConfigurationProperty {

    STORAGE_BACKEND("storage.backend", String.class),
    STORAGE_CONF_FILE("storage.conf-file", String.class),
    STORAGE_CASSANDRA_STORAGEDIR("storage.cassandra.storagedir", String.class),
    INDEX_SEARCH_DIRECTORY("index.search.directory", String.class),
    INDEX_SEARCH_BACKEND("index.search.backend", String.class);

    public static String INDEX_SEARCH_DIRECTORY_NAME = "searchindex";

    private final String property;
    private final Class clazz;

    JanusGraphProperty(String property, Class clazz) {
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
