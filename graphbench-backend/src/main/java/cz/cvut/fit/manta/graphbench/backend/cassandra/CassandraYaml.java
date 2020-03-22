package cz.cvut.fit.manta.graphbench.backend.cassandra;

import cz.cvut.fit.manta.graphbench.core.config.Configuration;
import cz.cvut.fit.manta.graphbench.core.util.Util;
import org.apache.commons.configuration2.YAMLConfiguration;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

/**
 * Class representing a Cassandra yaml file containing configuration of a given Cassandra instance.
 * The class has a private constructor as it's supposed to be used as a Singleton, a single instance within
 * the whole project acquired with the {@code getInstance} method.
 *
 * @author Lucie Svitáková (svitaluc@fit.cvut.cz)
 */
public class CassandraYaml extends Configuration {

    /** {@link CassandraYaml} instance representing configuration properties of the Cassandra yaml file. */
    private static CassandraYaml instance;
    /** Relative path to the cassandra properties file. */
    private static String propertiesPath;
    /** Logger. */
    private static final Logger LOG = Logger.getLogger(CassandraYaml.class);

    /**
     * Constructor of the {@link CassandraYaml}. It's private as the class
     * @param configPath Path to the configuration yaml file.
     */
    private CassandraYaml(String configPath) {
        String jarPath = Util.getJarPath();
        YAMLConfiguration config = new YAMLConfiguration();
        String absoluteConfigPath = jarPath + File.separator + configPath;

        try {
            config.read(new FileReader(absoluteConfigPath));
            setConfig(config, absoluteConfigPath);
        } catch (FileNotFoundException e) {
            LOG.error("Configuration file " + absoluteConfigPath + " was not found.", e);
        } catch (ConfigurationException e) {
            LOG.error("Configuration file" + absoluteConfigPath + " cannot be opened.", e);
        }
    }

    /**
     * @param cassandraVersion Version of the actually used Cassandra instance.
     * @return An instance of the {@link CassandraYaml} class.
     */
    public static CassandraYaml getInstance(CassandraVersion cassandraVersion) {
        if (instance == null) {
            propertiesPath = "conf" + File.separator + "cassandra" + File.separator + cassandraVersion.getFileName();
            instance = new CassandraYaml(propertiesPath);
        }
        return instance;
    }

    /**
     * @return Absolute path of the Cassandra yaml file with properties.
     */
    public String getAbsolutePropertiesPath() {
        return Util.getJarPath() + File.separator + propertiesPath;
    }
}
