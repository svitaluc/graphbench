package eu.profinit.manta.graphbench.janusgraph.config.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.configuration.Configuration;

public class JanusGraphPropertyFile {

    private Storage storage;
    private Query query;
    private Cache cache;
    private Ids ids;
    private Schema schema;


    index.search.backend=lucene
#index.search.directory=THIS PROPERTY SHOULD BE SET IN JanusGraphDB class, based on directory from config.properties

    public static void setConfiguration(Configuration configuration, String dbPath) {

    }

    private class Query {
        @JsonProperty("fast-property")
        Boolean fastProperty;
        Boolean batch;
    }

    private class Cache {
        @JsonProperty("db-cache")
        Boolean dbCache;
        @JsonProperty("db-cache-size")
        Boolean dbCacheSize;
        @JsonProperty("db-cache-time")
        Boolean dbCacheTime;
        @JsonProperty("tx-cache-size")
        Boolean txCacheSize;
        @JsonProperty("tx-dirty-size")
        Boolean txDirtySize;
        @JsonProperty("db-cache-clean-wait")
        Boolean dbCacheCleanWait;
    }

    private class Ids {
        @JsonProperty("block-size")
        Integer blockSize;
    }

    private class Schema {
        @JsonProperty("default")
        String defaultProperty;
    }

    private class Storage{
        Cassandra cassandra;
        Cql cql;
        Berkeleyje berkeleyje;

        String directory;
        String backend;
        String hostname;
        @JsonProperty("batch-loading")
        Boolean batchLoading;
        @JsonProperty("buffer-size")
        Integer bufferSize;
        @JsonProperty("parallel-backend-ops")
        Boolean parallelBackendOps;
        @JsonProperty("conf-file")
        String confFile;
        Integer bufferCount;

        private class Cassandra {
            Logger logger;
            @JsonProperty("compaction-strategy-class")
            String compactionStrategyClass;
            @JsonProperty("write-consistency-level")
            String writeConsistencyLevel;
            Boolean compression;
            @JsonProperty("replication-factor")
            Integer replicationFactor;
            @JsonProperty("read-consistency-level")
            String readConsistencyLevel;
            String storagedir;

            private class Logger {
                String level;
            }
        }

        private class Cql {
            Boolean compression;
        }

        private class Berkeleyje {
            @JsonProperty("cache-precentage")
            Integer cachePrecentage;
        }
    }
}
