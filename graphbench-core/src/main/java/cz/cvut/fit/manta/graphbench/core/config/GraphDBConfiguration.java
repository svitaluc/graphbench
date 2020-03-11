package cz.cvut.fit.manta.graphbench.core.config;

/**
 * Interface for graph database configuration containing its specific methods.
 */
public interface GraphDBConfiguration {
        /**
         * @return Name of a used index.
         */
        String getIndexBackend();

        /**
         * @return Version number of the used index.
         */
        String getIndexVersion();

        /**
         * @return Name of a used storage backend.
         */
        String getStorageBackend();

        /**
         * @return Version of the database used.
         */
        String getDatabaseVersion();
}
