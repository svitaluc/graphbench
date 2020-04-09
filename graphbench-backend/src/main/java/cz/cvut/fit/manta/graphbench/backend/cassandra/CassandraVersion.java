package cz.cvut.fit.manta.graphbench.backend.cassandra;

/**
 * Enumeration of possible Cassandra versions within the project.
 *
 * @author Lucie Svitáková (svitaluc@fit.cvut.cz)
 */
public enum CassandraVersion {
    /** Name of the configuration file for the Cassandra, version 3.1.1.0 */
    CASSANDRA_3110("cassandra.yaml"),
    /** Name of the configuration file for the Cassandra, version 1.2.2 */
    CASSANDRA_122("cassandra-1.2.2.yaml");

    /** Name of the configuration file for the given version. */
    private String fileName;

    /**
     * Constructor of the {@link CassandraVersion}.
     * @param fileName Name of the configuration file for the given version.
     */
    CassandraVersion(String fileName) {
        this.fileName = fileName;
    }

    /**
     * @return Name of the configuration file for the given version.
     */
    public String getFileName() {
        return fileName;
    }
}
