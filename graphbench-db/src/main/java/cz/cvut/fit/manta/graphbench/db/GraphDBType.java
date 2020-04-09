package cz.cvut.fit.manta.graphbench.db;

/**
 * Type of the graph database.
 *
 * @author Lucie Svitáková (svitaluc@fit.cvut.cz)
 */
public enum GraphDBType {
    /** JanusGraph graph database. */
    JANUSGRAPH("JANUSGRAPH"),
    /** Titan graph database. */
    TITAN("TITAN");

    /** Name of the graph database that should correspond with a value in the config file. */
    private String name;

    /**
     * Constructor of the {@link GraphDBType}.
     * @param name Name of the graph database that should correspond with a value in the config file
     */
    GraphDBType(String name) {
        this.name = name;
    }

    /**
     * @return Name of the graph database that should correspond with a value in the config file
     */
    public String getName() {
        return name;
    }

    /**
     * @param type Name of the graph database type.
     * @return Type of the graph database ({@link GraphDBType} based on its name ({@link String}).
     */
    public static GraphDBType getGraphDBType(String type) {
        for (GraphDBType item : values()) {
            if (item.name.equals(type)) {
                return item;
            }
        }
        throw new IllegalArgumentException ("The required graph type " + type + " is not supported.");
    }
}
