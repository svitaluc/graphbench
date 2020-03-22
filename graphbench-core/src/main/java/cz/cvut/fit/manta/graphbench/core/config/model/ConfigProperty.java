package cz.cvut.fit.manta.graphbench.core.config.model;

import cz.cvut.fit.manta.graphbench.core.config.ConfigurationProperty;

/**
 * All properties of the configuration file config.properties.
 *
 * @author Lucie Svitáková (svitaluc@fit.cvut.cz)
 */
public enum ConfigProperty implements ConfigurationProperty {
    /** Name of the edge label connecting a parent */
    EDGE_PARENT_LABEL("EDGE_PARENT_LABEL", String.class),
    /** Name of the edge property connecting a child */
    EDGE_CHILD_NAME("EDGE_CHILD_NAME", String.class),
    /** Sets whether the database should be created from dataset files (WITH_IMPORT=true) or
      * the database already exists and it should be read from the DATABASE_DIR directory
      * (WITH_IMPORT=false). */
    WITH_IMPORT("WITH_IMPORT", boolean.class),
    /** Encoding of the dataset */
    CSV_ENCODING("CSV_ENCODING", String.class),
    /** Number of processed rows (representing a node, an edge or an edge attribute)
      * after which the new structures are committed in the database. */
    COMMIT_EVERY_COUNT("COMMIT_EVERY_COUNT", Integer.class),
    /** Value of the vertex property NODE_NAME for a super root */
    SUPER_ROOT_NAME("SUPER_ROOT_NAME", String.class),
    /** Value of the vertex property NODE_TYPE for a super root */
    SUPER_ROOT_TYPE("SUPER_ROOT_TYPE", String.class),
    /** Value of the vertex property NODE_TYPE for an ordinary node */
    VERTEX_NODE_TYPE("VERTEX_NODE_TYPE", String.class),

    /** Description of edge data in a csv file. Column index of an edge id. */
    EDGE_I_ID("EDGE_I_ID", Integer.class),
    /** Description of edge data in a csv file. Column index of a starting vertex (its id) of the edge. */
    EDGE_I_START("EDGE_I_START", Integer.class),
    /** Description of edge data in a csv file. Column index of an ending vertex (its id) of the edge. */
    EDGE_I_END("EDGE_I_END", Integer.class),
    /** Description of edge data in a csv file. Column index of the edge type. */
    EDGE_I_TYPE("EDGE_I_TYPE", Integer.class),

    /** Description of edge-attribute data in a csv file. Column index of the edge id to which the
      * attribute belongs. */
    EDGE_ATTRIBUTE_I_EDGE("EDGE_ATTRIBUTE_I_EDGE", Integer.class),
    /** Description of edge-attribute data in a csv file. Column index of a key of the edge attribute. */
    EDGE_ATTRIBUTE_I_KEY("EDGE_ATTRIBUTE_I_KEY", Integer.class),
    /** Description of edge-attribute data in a csv file. Column index of a value of the edge attribute. */
    EDGE_ATTRIBUTE_I_VALUE("EDGE_ATTRIBUTE_I_VALUE", Integer.class),

    /** Description of vertex data in a csv file. Column index of a vertex id. */
    NODE_I_ID("NODE_I_ID", Integer.class),
    /** Description of vertex data in a csv file. Column index of a parent id of the vertex. */
    NODE_I_PARENT("NODE_I_PARENT", Integer.class),
    /** Description of vertex data in a csv file. Column index of a vertex name. */
    NODE_I_NAME("NODE_I_NAME", Integer.class),
    /** Description of vertex data in a csv file. Column index of a vertex description. */
    NODE_I_DESC("NODE_I_DESC", Integer.class),

    /** Path to a directory into which a file with results of the test will be written. */
    CSV_OUTPUT_DIRECTORY("CSV_OUTPUT_DIRECTORY", String.class),
    /** Name of the file containing data about vertices */
    CSV_NODE_NAME("CSV_NODE_NAME", String.class),
    /** Name of the file containing data about edges */
    CSV_EDGE_NAME("CSV_EDGE_NAME", String.class),
    /** Name of the file containing data about edge attributes */
    CSV_EDGE_ATTR_NAME("CSV_EDGE_ATTR_NAME", String.class),

    /** Directory of a database storage - a place where the database will be stored.
      * It's cleaned each time before a new test is run. */
    DATABASE_DIR("DATABASE_DIR", String.class),
    /** Type of database that must match one of supported databases listed in GraphDBType. */
    DATABASE_TYPE("DATABASE_TYPE", String.class),

    /** Directory of a dataset. The supported data model and expected files in the directory
      * are described in the README file. */
    DATASET_DIR("DATASET_DIR", String.class),

    /** Number of processed rows (representing a node, an edge or an edge attribute)
      * after which the new structures are logged */
    LOAD_PROGRESS_INFO_COUNT("LOAD_PROGRESS_INFO_COUNT", Integer.class),

    /** Type of database must match one of tests listed in TestType. If you want to run your own test,
      * see the README file. */
    TEST_TYPE("TEST_TYPE", String.class);

    /** Property name. */
    private String property;
    /** Class of the property. */
    private Class clazz;

    /**
     * Constructor of the {@link ConfigProperty}.
     * @param property Property name
     * @param clazz Class of the property
     */
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
