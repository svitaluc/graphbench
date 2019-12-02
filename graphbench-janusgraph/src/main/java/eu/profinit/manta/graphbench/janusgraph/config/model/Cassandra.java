package eu.profinit.manta.graphbench.janusgraph.config.model;


import java.util.List;
import java.util.Map;

public class Cassandra {
    public static String DATA_DIRECTORY_NAME = "data";
    public static String COMMITLOG_DIRECTORY_NAME = "commitlog";
    public static String SAVED_CACHES_DIRECTORY_NAME = "saved_caches";

    private String cluster_name;
    private String disk_access_mode; //enum
    private Integer num_tokens;
    private boolean hinted_handoff_enabled;
    private Long max_hint_window_in_ms;
    private Long hinted_handoff_throttle_in_kb;
    private Integer max_hints_delivery_threads;
    private Long batchlog_replay_throttle_in_kb;
    private String authenticator;
    private String authorizer;
    private Long permissions_validity_in_ms;
    private String partitioner;
    private String[] data_file_directories;
    private String commitlog_directory;
    private String disk_failure_policy;
    private String commit_failure_policy;
    private Long key_cache_size_in_mb;
    private Long key_cache_save_period;
    private Long row_cache_size_in_mb;
    private Long row_cache_save_period;
    private String saved_caches_directory;
    private String commitlog_sync;
    private Long commitlog_sync_period_in_ms;
    private Long commitlog_segment_size_in_mb;
    private List<Map<String, Object>> seed_provider;
    private Integer concurrent_reads;
    private Integer concurrent_writes;
    private boolean trickle_fsync;
    private Long trickle_fsync_interval_in_kb;
    private Integer storage_port;
    private Integer ssl_storage_port;
    private String listen_address;
    private boolean start_native_transport;
    private Integer native_transport_port;
    private boolean start_rpc;
    private String rpc_address;
    private Integer rpc_port;
    private boolean rpc_keepalive;
    private String rpc_server_type;
    private Long thrift_framed_transport_size_in_mb;
    private boolean incremental_backups;
    private boolean snapshot_before_compaction;
    private boolean auto_snapshot;
    private Integer tombstone_warn_threshold;
    private Long tombstone_failure_threshold;
    private Long column_index_size_in_kb;
    private Long compaction_throughput_mb_per_sec;
    private Integer read_request_timeout_in_ms;
    private Integer range_request_timeout_in_ms;
    private Integer write_request_timeout_in_ms;
    private Integer cas_contention_timeout_in_ms;
    private Integer truncate_request_timeout_in_ms;
    private Integer request_timeout_in_ms;
    private boolean cross_node_timeout;
    private String endpoint_snitch;
    private Integer dynamic_snitch_update_interval_in_ms;
    private Long dynamic_snitch_reset_interval_in_ms;
    private Double dynamic_snitch_badness_threshold;
    private String request_scheduler;
    private Map<String, String> server_encryption_options;
    private Map<String, String> client_encryption_options;
    private String internode_compression;
    private boolean inter_dc_tcp_nodelay;

    public String getCluster_name() {
        return cluster_name;
    }

    public void setCluster_name(String cluster_name) {
        this.cluster_name = cluster_name;
    }

    public String getDisk_access_mode() {
        return disk_access_mode;
    }

    public void setDisk_access_mode(String disk_access_mode) {
        this.disk_access_mode = disk_access_mode;
    }

    public Integer getNum_tokens() {
        return num_tokens;
    }

    public void setNum_tokens(Integer num_tokens) {
        this.num_tokens = num_tokens;
    }

    public boolean isHinted_handoff_enabled() {
        return hinted_handoff_enabled;
    }

    public void setHinted_handoff_enabled(boolean hinted_handoff_enabled) {
        this.hinted_handoff_enabled = hinted_handoff_enabled;
    }

    public Long getMax_hint_window_in_ms() {
        return max_hint_window_in_ms;
    }

    public void setMax_hint_window_in_ms(Long max_hint_window_in_ms) {
        this.max_hint_window_in_ms = max_hint_window_in_ms;
    }

    public Long getHinted_handoff_throttle_in_kb() {
        return hinted_handoff_throttle_in_kb;
    }

    public void setHinted_handoff_throttle_in_kb(Long hinted_handoff_throttle_in_kb) {
        this.hinted_handoff_throttle_in_kb = hinted_handoff_throttle_in_kb;
    }

    public Integer getMax_hints_delivery_threads() {
        return max_hints_delivery_threads;
    }

    public void setMax_hints_delivery_threads(Integer max_hints_delivery_threads) {
        this.max_hints_delivery_threads = max_hints_delivery_threads;
    }

    public String getAuthenticator() {
        return authenticator;
    }

    public void setAuthenticator(String authenticator) {
        this.authenticator = authenticator;
    }

    public Long getBatchlog_replay_throttle_in_kb() {
        return batchlog_replay_throttle_in_kb;
    }

    public void setBatchlog_replay_throttle_in_kb(Long batchlog_replay_throttle_in_kb) {
        this.batchlog_replay_throttle_in_kb = batchlog_replay_throttle_in_kb;
    }

    public String getAuthorizer() {
        return authorizer;
    }

    public void setAuthorizer(String authorizer) {
        this.authorizer = authorizer;
    }

    public Long getPermissions_validity_in_ms() {
        return permissions_validity_in_ms;
    }

    public void setPermissions_validity_in_ms(Long permissions_validity_in_ms) {
        this.permissions_validity_in_ms = permissions_validity_in_ms;
    }

    public String getPartitioner() {
        return partitioner;
    }

    public void setPartitioner(String partitioner) {
        this.partitioner = partitioner;
    }

    public String[] getData_file_directories() {
        return data_file_directories;
    }

    public void setData_file_directories(String[] data_file_directories) {
        this.data_file_directories = data_file_directories;
    }

    public String getCommitlog_directory() {
        return commitlog_directory;
    }

    public void setCommitlog_directory(String commitlog_directory) {
        this.commitlog_directory = commitlog_directory;
    }

    public String getDisk_failure_policy() {
        return disk_failure_policy;
    }

    public void setDisk_failure_policy(String disk_failure_policy) {
        this.disk_failure_policy = disk_failure_policy;
    }

    public String getCommit_failure_policy() {
        return commit_failure_policy;
    }

    public void setCommit_failure_policy(String commit_failure_policy) {
        this.commit_failure_policy = commit_failure_policy;
    }

    public Long getKey_cache_size_in_mb() {
        return key_cache_size_in_mb;
    }

    public void setKey_cache_size_in_mb(Long key_cache_size_in_mb) {
        this.key_cache_size_in_mb = key_cache_size_in_mb;
    }

    public Long getKey_cache_save_period() {
        return key_cache_save_period;
    }

    public void setKey_cache_save_period(Long key_cache_save_period) {
        this.key_cache_save_period = key_cache_save_period;
    }

    public Long getRow_cache_size_in_mb() {
        return row_cache_size_in_mb;
    }

    public void setRow_cache_size_in_mb(Long row_cache_size_in_mb) {
        this.row_cache_size_in_mb = row_cache_size_in_mb;
    }

    public Long getRow_cache_save_period() {
        return row_cache_save_period;
    }

    public void setRow_cache_save_period(Long row_cache_save_period) {
        this.row_cache_save_period = row_cache_save_period;
    }

    public String getSaved_caches_directory() {
        return saved_caches_directory;
    }

    public void setSaved_caches_directory(String saved_caches_directory) {
        this.saved_caches_directory = saved_caches_directory;
    }

    public String getCommitlog_sync() {
        return commitlog_sync;
    }

    public void setCommitlog_sync(String commitlog_sync) {
        this.commitlog_sync = commitlog_sync;
    }

    public Long getCommitlog_sync_period_in_ms() {
        return commitlog_sync_period_in_ms;
    }

    public void setCommitlog_sync_period_in_ms(Long commitlog_sync_period_in_ms) {
        this.commitlog_sync_period_in_ms = commitlog_sync_period_in_ms;
    }

    public Long getCommitlog_segment_size_in_mb() {
        return commitlog_segment_size_in_mb;
    }

    public void setCommitlog_segment_size_in_mb(Long commitlog_segment_size_in_mb) {
        this.commitlog_segment_size_in_mb = commitlog_segment_size_in_mb;
    }

    public List<Map<String, Object>> getSeed_provider() {
        return seed_provider;
    }

    public void setSeed_provider(List<Map<String, Object>> seed_provider) {
        this.seed_provider = seed_provider;
    }

    public Integer getConcurrent_reads() {
        return concurrent_reads;
    }

    public void setConcurrent_reads(Integer concurrent_reads) {
        this.concurrent_reads = concurrent_reads;
    }

    public Integer getConcurrent_writes() {
        return concurrent_writes;
    }

    public void setConcurrent_writes(Integer concurrent_writes) {
        this.concurrent_writes = concurrent_writes;
    }

    public boolean isTrickle_fsync() {
        return trickle_fsync;
    }

    public void setTrickle_fsync(boolean trickle_fsync) {
        this.trickle_fsync = trickle_fsync;
    }

    public Long getTrickle_fsync_interval_in_kb() {
        return trickle_fsync_interval_in_kb;
    }

    public void setTrickle_fsync_interval_in_kb(Long trickle_fsync_interval_in_kb) {
        this.trickle_fsync_interval_in_kb = trickle_fsync_interval_in_kb;
    }

    public Integer getStorage_port() {
        return storage_port;
    }

    public void setStorage_port(Integer storage_port) {
        this.storage_port = storage_port;
    }

    public Integer getSsl_storage_port() {
        return ssl_storage_port;
    }

    public void setSsl_storage_port(Integer ssl_storage_port) {
        this.ssl_storage_port = ssl_storage_port;
    }

    public String getListen_address() {
        return listen_address;
    }

    public void setListen_address(String listen_address) {
        this.listen_address = listen_address;
    }

    public boolean isStart_native_transport() {
        return start_native_transport;
    }

    public void setStart_native_transport(boolean start_native_transport) {
        this.start_native_transport = start_native_transport;
    }

    public Integer getNative_transport_port() {
        return native_transport_port;
    }

    public void setNative_transport_port(Integer native_transport_port) {
        this.native_transport_port = native_transport_port;
    }

    public boolean isStart_rpc() {
        return start_rpc;
    }

    public void setStart_rpc(boolean start_rpc) {
        this.start_rpc = start_rpc;
    }

    public String getRpc_address() {
        return rpc_address;
    }

    public void setRpc_address(String rpc_address) {
        this.rpc_address = rpc_address;
    }

    public Integer getRpc_port() {
        return rpc_port;
    }

    public void setRpc_port(Integer rpc_port) {
        this.rpc_port = rpc_port;
    }

    public boolean isRpc_keepalive() {
        return rpc_keepalive;
    }

    public void setRpc_keepalive(boolean rpc_keepalive) {
        this.rpc_keepalive = rpc_keepalive;
    }

    public String getRpc_server_type() {
        return rpc_server_type;
    }

    public void setRpc_server_type(String rpc_server_type) {
        this.rpc_server_type = rpc_server_type;
    }

    public Long getThrift_framed_transport_size_in_mb() {
        return thrift_framed_transport_size_in_mb;
    }

    public void setThrift_framed_transport_size_in_mb(Long thrift_framed_transport_size_in_mb) {
        this.thrift_framed_transport_size_in_mb = thrift_framed_transport_size_in_mb;
    }

    public boolean isIncremental_backups() {
        return incremental_backups;
    }

    public void setIncremental_backups(boolean incremental_backups) {
        this.incremental_backups = incremental_backups;
    }

    public boolean isSnapshot_before_compaction() {
        return snapshot_before_compaction;
    }

    public void setSnapshot_before_compaction(boolean snapshot_before_compaction) {
        this.snapshot_before_compaction = snapshot_before_compaction;
    }

    public boolean isAuto_snapshot() {
        return auto_snapshot;
    }

    public void setAuto_snapshot(boolean auto_snapshot) {
        this.auto_snapshot = auto_snapshot;
    }

    public Integer getTombstone_warn_threshold() {
        return tombstone_warn_threshold;
    }

    public void setTombstone_warn_threshold(Integer tombstone_warn_threshold) {
        this.tombstone_warn_threshold = tombstone_warn_threshold;
    }

    public Long getTombstone_failure_threshold() {
        return tombstone_failure_threshold;
    }

    public void setTombstone_failure_threshold(Long tombstone_failure_threshold) {
        this.tombstone_failure_threshold = tombstone_failure_threshold;
    }

    public Long getColumn_index_size_in_kb() {
        return column_index_size_in_kb;
    }

    public void setColumn_index_size_in_kb(Long column_index_size_in_kb) {
        this.column_index_size_in_kb = column_index_size_in_kb;
    }

    public Long getCompaction_throughput_mb_per_sec() {
        return compaction_throughput_mb_per_sec;
    }

    public void setCompaction_throughput_mb_per_sec(Long compaction_throughput_mb_per_sec) {
        this.compaction_throughput_mb_per_sec = compaction_throughput_mb_per_sec;
    }

    public Integer getRead_request_timeout_in_ms() {
        return read_request_timeout_in_ms;
    }

    public void setRead_request_timeout_in_ms(Integer read_request_timeout_in_ms) {
        this.read_request_timeout_in_ms = read_request_timeout_in_ms;
    }

    public Integer getRange_request_timeout_in_ms() {
        return range_request_timeout_in_ms;
    }

    public void setRange_request_timeout_in_ms(Integer range_request_timeout_in_ms) {
        this.range_request_timeout_in_ms = range_request_timeout_in_ms;
    }

    public Integer getWrite_request_timeout_in_ms() {
        return write_request_timeout_in_ms;
    }

    public void setWrite_request_timeout_in_ms(Integer write_request_timeout_in_ms) {
        this.write_request_timeout_in_ms = write_request_timeout_in_ms;
    }

    public Integer getCas_contention_timeout_in_ms() {
        return cas_contention_timeout_in_ms;
    }

    public void setCas_contention_timeout_in_ms(Integer cas_contention_timeout_in_ms) {
        this.cas_contention_timeout_in_ms = cas_contention_timeout_in_ms;
    }

    public Integer getTruncate_request_timeout_in_ms() {
        return truncate_request_timeout_in_ms;
    }

    public void setTruncate_request_timeout_in_ms(Integer truncate_request_timeout_in_ms) {
        this.truncate_request_timeout_in_ms = truncate_request_timeout_in_ms;
    }

    public Integer getRequest_timeout_in_ms() {
        return request_timeout_in_ms;
    }

    public void setRequest_timeout_in_ms(Integer request_timeout_in_ms) {
        this.request_timeout_in_ms = request_timeout_in_ms;
    }

    public boolean isCross_node_timeout() {
        return cross_node_timeout;
    }

    public void setCross_node_timeout(boolean cross_node_timeout) {
        this.cross_node_timeout = cross_node_timeout;
    }

    public String getEndpoint_snitch() {
        return endpoint_snitch;
    }

    public void setEndpoint_snitch(String endpoint_snitch) {
        this.endpoint_snitch = endpoint_snitch;
    }

    public Integer getDynamic_snitch_update_interval_in_ms() {
        return dynamic_snitch_update_interval_in_ms;
    }

    public void setDynamic_snitch_update_interval_in_ms(Integer dynamic_snitch_update_interval_in_ms) {
        this.dynamic_snitch_update_interval_in_ms = dynamic_snitch_update_interval_in_ms;
    }

    public Long getDynamic_snitch_reset_interval_in_ms() {
        return dynamic_snitch_reset_interval_in_ms;
    }

    public void setDynamic_snitch_reset_interval_in_ms(Long dynamic_snitch_reset_interval_in_ms) {
        this.dynamic_snitch_reset_interval_in_ms = dynamic_snitch_reset_interval_in_ms;
    }

    public Double getDynamic_snitch_badness_threshold() {
        return dynamic_snitch_badness_threshold;
    }

    public void setDynamic_snitch_badness_threshold(Double dynamic_snitch_badness_threshold) {
        this.dynamic_snitch_badness_threshold = dynamic_snitch_badness_threshold;
    }

    public String getRequest_scheduler() {
        return request_scheduler;
    }

    public void setRequest_scheduler(String request_scheduler) {
        this.request_scheduler = request_scheduler;
    }

    public Map<String, String> getServer_encryption_options() {
        return server_encryption_options;
    }

    public void setServer_encryption_options(Map<String, String> server_encryption_options) {
        this.server_encryption_options = server_encryption_options;
    }

    public Map<String, String> getClient_encryption_options() {
        return client_encryption_options;
    }

    public void setClient_encryption_options(Map<String, String> client_encryption_options) {
        this.client_encryption_options = client_encryption_options;
    }

    public String getInternode_compression() {
        return internode_compression;
    }

    public void setInternode_compression(String internode_compression) {
        this.internode_compression = internode_compression;
    }

    public boolean isInter_dc_tcp_nodelay() {
        return inter_dc_tcp_nodelay;
    }

    public void setInter_dc_tcp_nodelay(boolean inter_dc_tcp_nodelay) {
        this.inter_dc_tcp_nodelay = inter_dc_tcp_nodelay;
    }
}
