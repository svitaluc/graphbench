package cz.cvut.fit.manta.graphbench.core.db.product;

/**
 * Type of the graph database.
 */
public enum GraphDBType {
    JANUSGRAPH("JANUSGRAPH"),
    TITAN("TITAN");

    /** Name of the graph database that should correspond with a value in the config file. */
    String name;

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
        throw new UnsupportedOperationException("The required graph type " + type + " is not supported.");
    }
}
