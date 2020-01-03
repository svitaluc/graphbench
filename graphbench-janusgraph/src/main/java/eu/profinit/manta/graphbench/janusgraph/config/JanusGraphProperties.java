package eu.profinit.manta.graphbench.janusgraph.config;

import eu.profinit.manta.graphbench.core.config.Configuration;
import eu.profinit.manta.graphbench.core.util.Util;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;

import java.io.File;
/**
 * Class representing local configuration file janusgraph.properties.
 * Only one instance of the class is allowed within the application.
 */
public class JanusGraphProperties extends Configuration {

    private static JanusGraphProperties INSTANCE;
    private final static String propertiesPath = "conf" + File.separator + "janusgraph" + File.separator + "janusgraph.properties";

    private JanusGraphProperties(String configPath) {
        Configurations configs = new Configurations();
        try {
            String jarPath = Util.getJarPath();
            PropertiesConfiguration config = configs.properties(new File(jarPath + File.separator + configPath));
            setConfig(config);
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
}
