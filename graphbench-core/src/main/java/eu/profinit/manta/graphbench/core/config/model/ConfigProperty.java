package eu.profinit.manta.graphbench.core.config.model;

import eu.profinit.manta.graphbench.core.config.IConfigurationProperty;

/**
 * All properties of the configuration file config.properties.
 */
public enum ConfigProperty implements IConfigurationProperty {
    /**  */
    EDGE_PARENT_LABEL("EDGE_PARENT_LABEL", String.class),
    EDGE_CHILD_NAME("EDGE_CHILD_NAME", String.class),
    EDGE_RESOURCE_LABEL("EDGE_RESOURCE_LABEL", String.class),
    NODE_NAME("NODE_NAME", String.class),
    WITH_IMPORT("WITH_IMPORT", boolean.class),
    CSV_ENCODING("CSV_ENCODING", String.class),
    COMMIT_EVERY_COUNT("COMMIT_EVERY_COUNT", Integer.class),
    SUPER_ROOT_NAME("SUPER_ROOT_NAME", String.class),
    SUPER_ROOT_DESC("SUPER_ROOT_DESC", String.class),
    SUPER_ROOT_TYPE("SUPER_ROOT_TYPE", String.class),
    VERTEX_NODE_TYPE("VERTEX_NODE_TYPE", String.class),

    EDGE_I_ID("EDGE_I_ID", Integer.class),
    EDGE_I_START("EDGE_I_START", Integer.class),
    EDGE_I_END("EDGE_I_END", Integer.class),
    EDGE_I_TYPE("EDGE_I_TYPE", Integer.class),

    EDGE_ATTRIBUTE_I_EDGE("EDGE_ATTRIBUTE_I_EDGE", Integer.class),
    EDGE_ATTRIBUTE_I_KEY("EDGE_ATTRIBUTE_I_KEY", Integer.class),
    EDGE_ATTRIBUTE_I_VALUE("EDGE_ATTRIBUTE_I_VALUE", Integer.class),

    NODE_I_ID("NODE_I_ID", Integer.class),
    NODE_I_PARENT("NODE_I_PARENT", Integer.class),
    NODE_I_NAME("NODE_I_NAME", Integer.class),
    NODE_I_DESC("NODE_I_DESC", Integer.class),

    CSV_DIR("CSV_DIR", String.class),
    CSV_NODE_DIR("CSV_NODE_DIR", String.class),
    CSV_EDGE_DIR("CSV_EDGE_DIR", String.class),
    CSV_EDGE_ATTR_DIR("CSV_EDGE_ATTR_DIR", String.class),

    DATABASE_DIR("DATABASE_DIR", String.class),
    DATABASE_TYPE("DATABASE_TYPE", String.class),

    DATASET_DIR("DATASET_DIR", String.class),

    LOAD_PROGRESS_INFO_COUNT("LOAD_PROGRESS_INFO_COUNT", Integer.class),

    TEST_TYPE("TEST_TYPE", String.class),

    MAX_RES_LOAD_COUNT("MAX_RES_LOAD_COUNT", int.class);

    private final String property;
    private final Class clazz;

    ConfigProperty(String property, Class clazz) {
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
