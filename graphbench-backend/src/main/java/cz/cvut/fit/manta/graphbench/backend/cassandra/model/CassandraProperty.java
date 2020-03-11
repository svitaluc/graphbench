package cz.cvut.fit.manta.graphbench.backend.cassandra.model;

import cz.cvut.fit.manta.graphbench.core.config.IConfigurationProperty;

/**
 * Properties of the cassandra configuration file. The enumeration here is not full, only those properties explicitly
 * used within the code are defined.
 */
public enum CassandraProperty implements IConfigurationProperty {

    COMMITLOG_DIRECTORY("commitlog_directory", String.class),
    SAVED_CACHES_DIRECTORY("saved_caches_directory", String.class),
    DATA_FILE_DIRECTORIES("data_file_directories", String.class);

    public static String DATA_DIRECTORY_NAME = "data";
    public static String COMMITLOG_DIRECTORY_NAME = "commitlog";
    public static String SAVED_CACHES_DIRECTORY_NAME = "saved_caches";

    private final String property;
    private final Class clazz;

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
