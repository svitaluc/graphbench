package eu.profinit.manta.graphbench.janusgraph.config.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.javafx.fxml.PropertyNotFoundException;
import org.apache.commons.configuration.Configuration;
import org.apache.log4j.Logger;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

import static org.hibernate.validator.internal.util.ReflectionHelper.getPropertyName;

public class JanusGraphPropertyFile {
    final static Logger LOG = Logger.getLogger(JanusGraphPropertyFile.class);

    private Storage storage;
    private Query query;
    private Cache cache;
    private Ids ids;
    private Schema schema;
    private Index index;

    public Storage getStorage() {
        return storage;
    }

    public void setStorage(Storage storage) {
        this.storage = storage;
    }

    public Query getQuery() {
        return query;
    }

    public void setQuery(Query query) {
        this.query = query;
    }

    public Cache getCache() {
        return cache;
    }

    public void setCache(Cache cache) {
        this.cache = cache;
    }

    public Ids getIds() {
        return ids;
    }

    public void setIds(Ids ids) {
        this.ids = ids;
    }

    public Schema getSchema() {
        return schema;
    }

    public void setSchema(Schema schema) {
        this.schema = schema;
    }

    public Index getIndex() {
        return index;
    }

    public void setIndex(Index index) {
        this.index = index;
    }

    public void setConfiguration(Configuration configuration, String dbPath) {
        List<Method> getters = getGetterMethods(this.getClass());
        Map<String, Object> configurationMap = new HashMap<>();

        getters.forEach(getter -> {
            Object value = null;
            try {
                value = getter.invoke(this);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            if(value != null) {
                getEndProperty(getPropertyName(getter), value, configurationMap);
            }
        });

        configurationMap.keySet().forEach(k -> {
            configuration.setProperty(k, configurationMap.get(k));
        });
    }

    private Map<String, Object> getEndProperty(String name, Object value, Map<String, Object> configurationMap) {
        if (isEndProperty(value)) {
            Map<String, Object> propertyMap = new HashMap<>();
            propertyMap.put(name, value);
            return propertyMap;
        }
        List<Method> getters = getGetterMethods(value.getClass());
        getters.forEach(getter -> {
            Object getterValue = null;
            try {
                getterValue = getter.invoke(value);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            if(getterValue != null) {
                Map<String, Object> endProperty = getEndProperty(name + "." + getPropertyName(getter), getterValue, configurationMap);
                if (endProperty != null) {
                    endProperty.forEach(configurationMap::putIfAbsent);
                }
            }
        });
        return null;
    }

    private boolean isEndProperty(Object value) {
        Class clazz = value.getClass();
        return clazz.getName().contains("java.lang");
    }

    private static List<Method> getGetterMethods(Class clazz) {
        Method[] methods = clazz.getMethods();
        List<Method> getters = new ArrayList<>();

        for(Method method : methods){
            if(isProprietaryGetter(method)) {
                getters.add(method);
            }
        }
        return getters;
    }

    /**
     * A method is considered to be a getter if it
     *  - starts with "get"
     *  - has no parameter
     *  - returns void type
     *  - Object method getClass is not a proprietary getter
     * @param method
     * @return
     */
    private static boolean isProprietaryGetter(Method method){
        if(!method.getName().startsWith("get")) return false;
        if(method.getParameterTypes().length != 0) return false;
        if(void.class.equals(method.getReturnType())) return false;
        if(method.getName().startsWith("getClass")) return false;
        return true;
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

    public class Cache {
        @JsonProperty("db-cache")
        Boolean dbCache;
        @JsonProperty("db-cache-size")
        Long dbCacheSize;
        @JsonProperty("db-cache-time")
        Long dbCacheTime;
        @JsonProperty("tx-cache-size")
        Long txCacheSize;
        @JsonProperty("tx-dirty-size")
        Long txDirtySize;
        @JsonProperty("db-cache-clean-wait")
        Long dbCacheCleanWait;

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

        public Long getDbCacheTime() {
            return dbCacheTime;
        }

        public void setDbCacheTime(Long dbCacheTime) {
            this.dbCacheTime = dbCacheTime;
        }

        public Long getTxCacheSize() {
            return txCacheSize;
        }

        public void setTxCacheSize(Long txCacheSize) {
            this.txCacheSize = txCacheSize;
        }

        public Long getTxDirtySize() {
            return txDirtySize;
        }

        public void setTxDirtySize(Long txDirtySize) {
            this.txDirtySize = txDirtySize;
        }

        public Long getDbCacheCleanWait() {
            return dbCacheCleanWait;
        }

        public void setDbCacheCleanWait(Long dbCacheCleanWait) {
            this.dbCacheCleanWait = dbCacheCleanWait;
        }
    }

    public class Ids {
        @JsonProperty("block-size")
        Integer blockSize;

        public Integer getBlockSize() {
            return blockSize;
        }

        public void setBlockSize(Integer blockSize) {
            this.blockSize = blockSize;
        }
    }

    public class Schema {
        @JsonProperty("default")
        String defaultProperty;

        public String getDefaultProperty() {
            return defaultProperty;
        }

        public void setDefaultProperty(String defaultProperty) {
            this.defaultProperty = defaultProperty;
        }
    }

    public class Storage {
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

        public Berkeleyje getBerkeleyje() {
            return berkeleyje;
        }

        public void setBerkeleyje(Berkeleyje berkeleyje) {
            this.berkeleyje = berkeleyje;
        }

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

        public String getHostname() {
            return hostname;
        }

        public void setHostname(String hostname) {
            this.hostname = hostname;
        }

        public Boolean getBatchLoading() {
            return batchLoading;
        }

        public void setBatchLoading(Boolean batchLoading) {
            this.batchLoading = batchLoading;
        }

        public Integer getBufferSize() {
            return bufferSize;
        }

        public void setBufferSize(Integer bufferSize) {
            this.bufferSize = bufferSize;
        }

        public Boolean getParallelBackendOps() {
            return parallelBackendOps;
        }

        public void setParallelBackendOps(Boolean parallelBackendOps) {
            this.parallelBackendOps = parallelBackendOps;
        }

        public String getConfFile() {
            return confFile;
        }

        public void setConfFile(String confFile) {
            this.confFile = confFile;
        }

        public Integer getBufferCount() {
            return bufferCount;
        }

        public void setBufferCount(Integer bufferCount) {
            this.bufferCount = bufferCount;
        }

        public class Cassandra {
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

            public Logger getLogger() {
                return logger;
            }

            public void setLogger(Logger logger) {
                this.logger = logger;
            }

            public String getCompactionStrategyClass() {
                return compactionStrategyClass;
            }

            public void setCompactionStrategyClass(String compactionStrategyClass) {
                this.compactionStrategyClass = compactionStrategyClass;
            }

            public String getWriteConsistencyLevel() {
                return writeConsistencyLevel;
            }

            public void setWriteConsistencyLevel(String writeConsistencyLevel) {
                this.writeConsistencyLevel = writeConsistencyLevel;
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

            public String getStoragedir() {
                return storagedir;
            }

            public void setStoragedir(String storagedir) {
                this.storagedir = storagedir;
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

        public class Berkeleyje {
            @JsonProperty("cache-percentage")
            Integer cachePrecentage;

            public Integer getCachePrecentage() {
                return cachePrecentage;
            }

            public void setCachePrecentage(Integer cachePrecentage) {
                this.cachePrecentage = cachePrecentage;
            }
        }
    }

    public class Index {
        private Search search;

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
