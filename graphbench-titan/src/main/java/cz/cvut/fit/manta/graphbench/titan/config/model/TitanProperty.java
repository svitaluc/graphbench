package cz.cvut.fit.manta.graphbench.titan.config.model;

import cz.cvut.fit.manta.graphbench.core.config.ConfigurationProperty;

/**
 * Properties of the titan configuration file. The enumeration here is not full, only those properties explicitly
 * used within the code are defined.
 *
 * The particular properties of the Titan (version 0.4.4) are described at
 * http://titan.thinkaurelius.com/wikidoc/0.4.4/Graph-Configuration.html
 * The comments here are mostly copied from this source.
 *
 * @author Lucie Svitáková (svitaluc@fit.cvut.cz)
 */
public enum TitanProperty implements ConfigurationProperty {
    /** Full class name of the StorageManager implementation defining the storage backend
     * to be used for persistence or one of the following pre-defined storage backends:
     * cassandra, hbase, hazelcastcache, persistit, berkeleyje
    */
    STORAGE_BACKEND("storage.backend", String.class),
    /** Storage directory for those storage backends that require local storage */
    STORAGE_DIRECTORY("storage.directory", String.class),
    /** Specifies the yaml file as a full url, e.g. storage.cassandra-config-dir = file:///home/cassandra.yaml */
    STORAGE_CASSANDRA_CONFIG_DIR("storage.cassandra-config-dir", String.class),
    /** Directory to store index data locally. */
    STORAGE_INDEX_SEARCH_DIRECTORY("storage.index.search.directory", String.class),
    /** Implementation of the index backend configured for INDEX-NAME.
     * Additional configuration options specific to this index are configured
     * in the same configuration namespace. Please refer to the documentation page of the index
     * backend for more information
    */
    STORAGE_INDEX_SEARCH_BACKEND("storage.index.search.backend", String.class),
    /** Storage directory for Cassandra files. */
    STORAGE_CASSANDRA_STORAGEDIR("storage.cassandra.storagedir", String.class);

    /** Name of the directory in which data about the index search is stored. */
    public static final String INDEX_SEARCH_DIRECTORY_NAME = "searchindex";

    /** Property name. */
    private String property;
    /** Class of the property. */
    private Class clazz;

    /**
     * Constructor of the {@link TitanProperty}.
     * @param property Name of the property
     * @param clazz Class of the property
     */
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
