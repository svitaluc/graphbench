package cz.cvut.fit.manta.graphbench.backend.cassandra;

/**
 * Enumeration of possible Cassandra versions within the project.
 */
public enum CassandraVersion {

    CASSANDRA_3110("cassandra.yaml"),
    CASSANDRA_122("cassandra-1.2.2.yaml");

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
