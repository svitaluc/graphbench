package eu.profinit.manta.graphbench.janusgraph.config;

import eu.profinit.manta.graphbench.core.config.Configuration;
import eu.profinit.manta.graphbench.core.config.GraphDBConfiguration;
import eu.profinit.manta.graphbench.core.util.Util;
import eu.profinit.manta.graphbench.janusgraph.config.model.JanusGraphProperty;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;

import java.io.File;
/**
 * Class representing local configuration file janusgraph.properties.
 * Only one instance of the class is allowed within the application.
 */
public class JanusGraphProperties extends Configuration implements GraphDBConfiguration {

    private static JanusGraphProperties INSTANCE;
    private final static String propertiesPath = "conf" + File.separator + "janusgraph" + File.separator + "janusgraph.properties";

    private JanusGraphProperties(String configPath) {
        Configurations configs = new Configurations();
        try {
            String jarPath = Util.getJarPath();
            String absoluteConfigPath = jarPath + File.separator + configPath;
            PropertiesConfiguration config = configs.properties(new File(absoluteConfigPath));
            setConfig(config, absoluteConfigPath);
        } catch (ConfigurationException e) {
            LOG.error("Configuration file" + configPath + " cannot be opened.", e);
        }
    }

    /**
     * Get instance of the config.properties representation.
     * @return content of the config.properties file represented as an instance of {@link JanusGraphProperties} class
     */
    public static JanusGraphProperties getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new JanusGraphProperties(propertiesPath);
        }
        return INSTANCE;
    }

    @Override
    public String getIndexBackend() {
        return getStringProperty(JanusGraphProperty.INDEX_SEARCH_BACKEND);
    }

    @Override
    public String getStorageBackend() {
        return getStringProperty(JanusGraphProperty.STORAGE_BACKEND);
    }
}
