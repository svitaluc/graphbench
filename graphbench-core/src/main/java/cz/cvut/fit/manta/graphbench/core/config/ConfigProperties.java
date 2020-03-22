package cz.cvut.fit.manta.graphbench.core.config;

import cz.cvut.fit.manta.graphbench.core.util.Util;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.log4j.Logger;

import java.io.File;

/**
 * Class representing local configuration file config.properties.
 * Only one instance of the class is allowed within the application.
 *
 * @author Lucie Svitáková (svitaluc@fit.cvut.cz)
 */
public class ConfigProperties extends Configuration {
    /** Logger */
    private static final Logger LOG = Logger.getLogger(ConfigProperties.class);
    /** Instance of configuration properties. */
    private static ConfigProperties instance;
    /** Relative path of the configuration properties. */
    private final static String PROPERTIES_PATH = "conf" + File.separator + "config.properties";

    /**
     * Constructor of the {@link ConfigProperties}.
     * The constructor is private to avoid its outer initialization and push the usage
     * of the {@link #getInstance()} method to use only one instance within the whole project.
     *
     * @param configPath Relative path of the configuration properties
     */
    private ConfigProperties(String configPath) {
        Configurations configs = new Configurations();
        try {
            String jarPath = Util.getJarPath();
            String absoluteConfigPath = jarPath + File.separator + configPath;

            FileBasedConfigurationBuilder<PropertiesConfiguration> builder = configs.propertiesBuilder(absoluteConfigPath);
            builder.setAutoSave(true);
            setConfig(builder.getConfiguration(), absoluteConfigPath);
        } catch (ConfigurationException e) {
            LOG.error("Configuration file" + configPath + " cannot be opened.", e);
        }
    }

    /**
     * Get instance of the config.properties representation.
     * @return content of the config.properties file represented as an instance of {@link ConfigProperties} class
     */
    public static ConfigProperties getInstance() {
        if (instance == null) {
            instance = new ConfigProperties(PROPERTIES_PATH);
        }
        return instance;
    }

    /**
     * Get path of the config.properties file, relative to the directory from which the application is launched.
     * @return {@link String} representing path of the config.properties files, relative to the directory
     * from which the application is launched
     */
    public static String getPropertiesPath() {
        return PROPERTIES_PATH;
    }
}
