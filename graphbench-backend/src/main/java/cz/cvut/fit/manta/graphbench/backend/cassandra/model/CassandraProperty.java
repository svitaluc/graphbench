package cz.cvut.fit.manta.graphbench.backend.cassandra.model;

import cz.cvut.fit.manta.graphbench.core.config.ConfigurationProperty;

/**
 * Properties of the cassandra configuration file. The enumeration here is not full, only those properties explicitly
 * used within the code are defined.
 *
 * @author Lucie Svitáková (svitaluc@fit.cvut.cz)
 */
public enum CassandraProperty implements ConfigurationProperty {

    COMMITLOG_DIRECTORY("commitlog_directory", String.class),
    SAVED_CACHES_DIRECTORY("saved_caches_directory", String.class),
    DATA_FILE_DIRECTORIES("data_file_directories", String.class);

    public static final String DATA_DIRECTORY_NAME = "data";
    public static final String COMMITLOG_DIRECTORY_NAME = "commitlog";
    public static final String SAVED_CACHES_DIRECTORY_NAME = "saved_caches";

    private String property;
    private Class clazz;

    /**
     * Constructor of the {@link CassandraProperty}.
     * @param property Name of the property
     * @param clazz Class of the property
     */
    CassandraProperty(String property, Class clazz) {
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
