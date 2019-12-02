package eu.profinit.manta.graphbench.core.db.product;

public enum GraphDBType {
    JANUSGRAPH("JANUSGRAPH"),
    TITAN("TITAN");

    String name;

    GraphDBType(String name) {
        this.name = name;
    }

    public static GraphDBType getGraphDBType(String type) {
        for (GraphDBType item : values()) {
            if (item.name.equals(type)) {
                return item;
            }
        }
        throw new UnsupportedOperationException("The required graph type " + type + " is not supported.");
    }
}
