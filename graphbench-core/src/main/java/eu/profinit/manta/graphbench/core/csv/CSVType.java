package eu.profinit.manta.graphbench.core.csv;

import eu.profinit.manta.graphbench.core.config.ConfigProperties;
import eu.profinit.manta.graphbench.core.config.model.ConfigProperty;

public enum CSVType {
    NODE(ConfigProperties.getInstance().getStringProperty(ConfigProperty.CSV_NODE_DIR)),
    EDGE(ConfigProperties.getInstance().getStringProperty(ConfigProperty.CSV_EDGE_DIR)),
    EDGE_ATTR(ConfigProperties.getInstance().getStringProperty(ConfigProperty.CSV_EDGE_ATTR_DIR));

    private final String filename;

    private CSVType(String filename) {
        this.filename = filename;
    }

    public String getFileName() {
        return filename;
    }
}
