package eu.profinit.manta.graphbench.titan.config.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TitanPropertyFile {

    private Storage storage;
    private Cache cache;
    private Query query;

    public Storage getStorage() {
        return storage;
    }

    public void setStorage(Storage storage) {
        this.storage = storage;
    }

    public Cache getCache() {
        return cache;
    }

    public void setCache(Cache cache) {
        this.cache = cache;
    }

    public Query getQuery() {
        return query;
    }

    public void setQuery(Query query) {
        this.query = query;
    }

    public class Storage {
        String directory;
        String backend;
        Long buffercount;
        @JsonProperty("batch-loading")
        Boolean batchLoading;
        @JsonProperty("parallel-backend-ops")
        Boolean parallelBackendOps;
        Cassandra cassandra;
        Cql cql;
        @JsonProperty("conf-file")
        String confFile;
        @JsonProperty("cassandra-config-dir")
        String cassandraConfigDir;
        Index index;
        String hostname;

        public String getDirectory() {
            return directory;
        }

        public void setDirectory(String directory) {
            this.directory = directory;
        }

        public String getBackend() {
            return backend;
        }

        public void setBackend(String backend) {
            this.backend = backend;
        }

        public Long getBuffercount() {
            return buffercount;
        }

        public void setBuffercount(Long buffercount) {
            this.buffercount = buffercount;
        }

        public Boolean getBatchLoading() {
            return batchLoading;
        }

        public void setBatchLoading(Boolean batchLoading) {
            this.batchLoading = batchLoading;
        }

        public Boolean getParallelBackendOps() {
            return parallelBackendOps;
        }

        public void setParallelBackendOps(Boolean parallelBackendOps) {
            this.parallelBackendOps = parallelBackendOps;
        }

        public String getHostname() {
            return hostname;
        }

        public void setHostname(String hostname) {
            this.hostname = hostname;
        }

        public Cassandra getCassandra() {
            return cassandra;
        }

        public void setCassandra(Cassandra cassandra) {
            this.cassandra = cassandra;
        }

        public Cql getCql() {
            return cql;
        }

        public void setCql(Cql cql) {
            this.cql = cql;
        }

        public String getConfFile() {
            return confFile;
        }

        public void setConfFile(String confFile) {
            this.confFile = confFile;
        }

        public String getCassandraConfigDir() {
            return cassandraConfigDir;
        }

        public void setCassandraConfigDir(String cassandraConfigDir) {
            this.cassandraConfigDir = cassandraConfigDir;
        }

        public Index getIndex() {
            return index;
        }

        public void setIndex(Index index) {
            this.index = index;
        }

        public class Cassandra {
            @JsonProperty("write-consistency-level")
            String writeConsistencyLevel;
            @JsonProperty("compaction-strategy-class")
            String compactionStrategyClass;
            Boolean compression;
            @JsonProperty("replication-factor")
            Integer replicationFactor;
            @JsonProperty("read-consistency-level")
            String readConsistencyLevel;
            Logger logger;

            public String getWriteConsistencyLevel() {
                return writeConsistencyLevel;
            }

            public void setWriteConsistencyLevel(String writeConsistencyLevel) {
                this.writeConsistencyLevel = writeConsistencyLevel;
            }

            public String getCompactionStrategyClass() {
                return compactionStrategyClass;
            }

            public void setCompactionStrategyClass(String compactionStrategyClass) {
                this.compactionStrategyClass = compactionStrategyClass;
            }

            public Boolean getCompression() {
                return compression;
            }

            public void setCompression(Boolean compression) {
                this.compression = compression;
            }

            public Integer getReplicationFactor() {
                return replicationFactor;
            }

            public void setReplicationFactor(Integer replicationFactor) {
                this.replicationFactor = replicationFactor;
            }

            public String getReadConsistencyLevel() {
                return readConsistencyLevel;
            }

            public void setReadConsistencyLevel(String readConsistencyLevel) {
                this.readConsistencyLevel = readConsistencyLevel;
            }

            public Logger getLogger() {
                return logger;
            }

            public void setLogger(Logger logger) {
                this.logger = logger;
            }

            public class Logger {
                String level;

                public String getLevel() {
                    return level;
                }

                public void setLevel(String level) {
                    this.level = level;
                }
            }
        }

        public class Cql {
            Boolean compression;

            public Boolean getCompression() {
                return compression;
            }

            public void setCompression(Boolean compression) {
                this.compression = compression;
            }
        }

        public class Index {
            Search search;

            public Search getSearch() {
                return search;
            }

            public void setSearch(Search search) {
                this.search = search;
            }

            public class Search {
                String backend;
                String directory;

                public String getBackend() {
                    return backend;
                }

                public void setBackend(String backend) {
                    this.backend = backend;
                }

                public String getDirectory() {
                    return directory;
                }

                public void setDirectory(String directory) {
                    this.directory = directory;
                }
            }
        }
    }

    public class Cache {
        @JsonProperty("db-cache")
        Boolean dbCache;
        @JsonProperty("db-cache-size")
        Long dbCacheSize;
        @JsonProperty("tx-cache-size")
        Long txCacheSize;
        @JsonProperty("db-cache-clean-wait")
        Long dbCacheCleanWait;
        @JsonProperty("db-cache-time")
        Long dbCacheTime;

        public Boolean getDbCache() {
            return dbCache;
        }

        public void setDbCache(Boolean dbCache) {
            this.dbCache = dbCache;
        }

        public Long getDbCacheSize() {
            return dbCacheSize;
        }

        public void setDbCacheSize(Long dbCacheSize) {
            this.dbCacheSize = dbCacheSize;
        }

        public Long getTxCacheSize() {
            return txCacheSize;
        }

        public void setTxCacheSize(Long txCacheSize) {
            this.txCacheSize = txCacheSize;
        }

        public Long getDbCacheCleanWait() {
            return dbCacheCleanWait;
        }

        public void setDbCacheCleanWait(Long dbCacheCleanWait) {
            this.dbCacheCleanWait = dbCacheCleanWait;
        }

        public Long getDbCacheTime() {
            return dbCacheTime;
        }

        public void setDbCacheTime(Long dbCacheTime) {
            this.dbCacheTime = dbCacheTime;
        }
    }

    public class Query {
        @JsonProperty("fast-property")
        Boolean fastProperty;
        Boolean batch;

        public Boolean getFastProperty() {
            return fastProperty;
        }

        public void setFastProperty(Boolean fastProperty) {
            this.fastProperty = fastProperty;
        }

        public Boolean getBatch() {
            return batch;
        }

        public void setBatch(Boolean batch) {
            this.batch = batch;
        }
    }
}
