package cz.cvut.fit.manta.graphbench.janusgraph.config.model;

import cz.cvut.fit.manta.graphbench.core.config.ConfigurationProperty;

/**
 * Properties of the JanusGraph configuration file. The enumeration here is not full, only those properties explicitly
 * used within the code are defined.
 *
 * The particular properties of the JanusGraph are described at
 * https://docs.janusgraph.org/basics/configuration-reference/#schema
 * The comments here are mostly copied from this source.
 *
 * @author Lucie Svitáková (svitaluc@fit.cvut.cz)
 */
public enum JanusGraphProperty implements ConfigurationProperty {

    /**
     * The primary persistence provider used by JanusGraph. This is required.
     * It should be set one of JanusGraph's built-in shorthand names for its standard storage backends
     * (shorthands: berkeleyje, cassandrathrift, cassandra, astyanax, embeddedcassandra, cql, hbase, inmemory)
     * or to the full package and classname of a custom/third-party StoreManager implementation.
     */
    STORAGE_BACKEND("storage.backend", String.class),
    /**
     * Path to a configuration file for those storage backends
     * which require/support a single separate config file.
     */
    STORAGE_CONF_FILE("storage.conf-file", String.class),
    /**
     * Storage directory for Cassandra files.
     */
    STORAGE_CASSANDRA_STORAGEDIR("storage.cassandra.storagedir", String.class),
    /**
     * Directory to store index data locally.
     */
    INDEX_SEARCH_DIRECTORY("index.search.directory", String.class),
    /**
     * The indexing backend used to extend and optimize JanusGraph's query functionality.
     * This setting is optional. JanusGraph can use multiple heterogeneous index backends.
     * Hence, this option can appear more than once, so long as the user-defined name between
     * "index" and "backend" is unique among appearances.Similar to the storage backend,
     * this should be set to one of JanusGraph's built-in shorthand names for its standard
     * index backends (shorthands: lucene, elasticsearch, es, solr) or to the full package and
     * classname of a custom/third-party IndexProvider implementation.
     */
    INDEX_SEARCH_BACKEND("index.search.backend", String.class);

    /** Name of the directory in which data about the index search is stored. */
    public final static String INDEX_SEARCH_DIRECTORY_NAME = "searchindex";

    /** Property name. */
    private String property;
    /** Class of the property. */
    private Class<?> clazz;

    /**
     * Constructor of the {@link JanusGraphProperty}.
     * @param property Name of the property
     * @param clazz Class of the property
     */
    JanusGraphProperty(String property, Class<?> clazz) {
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
