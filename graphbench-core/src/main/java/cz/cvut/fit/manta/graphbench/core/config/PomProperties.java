package cz.cvut.fit.manta.graphbench.core.config;

import cz.cvut.fit.manta.graphbench.core.util.Util;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.log4j.Logger;

import java.io.File;

/**
 * Class representing properties defined in a pom file.
 *
 * @author Lucie Svitáková (svitaluc@fit.cvut.cz)
 */
public class PomProperties extends Configuration {

    private static final Logger LOG = Logger.getLogger(PomProperties.class);

    /**
     * Constructor of the {@link PomProperties}.
     * @param propertiesPath Path to the file containing pom properties
     */
    public PomProperties(String propertiesPath) {
        Configurations configs = new Configurations();
        try {
            String jarPath = Util.getJarPath();
            String absoluteConfigPath = jarPath + File.separator + propertiesPath;

            FileBasedConfigurationBuilder<PropertiesConfiguration> builder = configs.propertiesBuilder(absoluteConfigPath);
            builder.setAutoSave(true);
            setConfig(builder.getConfiguration(), absoluteConfigPath);
        } catch (ConfigurationException e) {
            LOG.error("Configuration file" + propertiesPath + " cannot be opened.", e);
        }
    }
}
