package eu.profinit.manta.graphbench.backend.cassandra;

public enum CassandraVersion {

    CASSANDRA_3110("cassandra.yaml"),
    CASSANDRA_122("cassandra-1.2.2.yaml");

    private String fileName;

    CassandraVersion(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }
}
