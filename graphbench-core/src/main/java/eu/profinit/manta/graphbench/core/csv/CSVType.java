package eu.profinit.manta.graphbench.core.csv;

import eu.profinit.manta.graphbench.core.config.Config;
import eu.profinit.manta.graphbench.core.config.Property;

public enum CSVType {
    NODE(Config.getInstance().getStringProperty(Property.CSV_NODE_DIR)),
    EDGE(Config.getInstance().getStringProperty(Property.CSV_EDGE_DIR)),
    EDGE_ATTR(Config.getInstance().getStringProperty(Property.CSV_EDGE_ATTR_DIR));

    private final String filename;

    private CSVType(String filename) {
        this.filename = filename;
    }

    public String getFileName() {
        return filename;
    }
}
