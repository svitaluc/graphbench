package cz.cvut.fit.manta.graphbench.backend.cassandra.model;

import cz.cvut.fit.manta.graphbench.core.config.ConfigurationProperty;

/**
 * Properties of the cassandra configuration file. The enumeration here is not full, only those properties explicitly
 * used within the code are defined.
 *
 * Description of individual cassandra properties is available at
 * http://cassandra.apache.org/doc/latest/configuration/cassandra_config_file.html?
 * Comments of the properties here are copied from this source.
 *
 * @author Lucie Svitáková (svitaluc@fit.cvut.cz)
 */
public enum CassandraProperty implements ConfigurationProperty {
    /**
     * Commit log.  When running on magnetic HDD, this should be a
     * separate spindle than the data directories.
     * If not set, the default directory is $CASSANDRA_HOME/data/commitlog.
     */
    COMMITLOG_DIRECTORY("commitlog_directory", String.class),
    /**
     * Saved caches. If not set, the default directory is $CASSANDRA_HOME/data/saved_caches.
     */
    SAVED_CACHES_DIRECTORY("saved_caches_directory", String.class),
    /**
     * Directories where Cassandra should store data on disk.  Cassandra
     * will spread data evenly across them, subject to the granularity of
     * the configured compaction strategy.
     * If not set, the default directory is $CASSANDRA_HOME/data/data.
     */
    DATA_FILE_DIRECTORIES("data_file_directories", String.class);

    /** Name of the directory where the Cassandra data will be stored. */
    public final static String DATA_DIRECTORY_NAME = "data";
    /** Name of the directory where the Cassandra commit logs will be stored. */
    public final static String COMMITLOG_DIRECTORY_NAME = "commitlog";
    /** Name of the directory where the Cassandra saved caches will be stored. */
    public final static String SAVED_CACHES_DIRECTORY_NAME = "saved_caches";

    /** Name of the property. */
    private String property;
    /** Class of the property. */
    private Class<?> clazz;

    /**
     * Constructor of the {@link CassandraProperty}.
     * @param property Name of the property
     * @param clazz Class of the property
     */
    CassandraProperty(String property, Class<?> clazz) {
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
