package cz.cvut.fit.manta.graphbench.janusgraph.config;

import cz.cvut.fit.manta.graphbench.core.config.Configuration;
import cz.cvut.fit.manta.graphbench.core.config.GraphDBConfiguration;
import cz.cvut.fit.manta.graphbench.core.config.PomProperties;
import cz.cvut.fit.manta.graphbench.core.util.Util;
import cz.cvut.fit.manta.graphbench.janusgraph.config.model.JanusGraphPomProperty;
import cz.cvut.fit.manta.graphbench.janusgraph.config.model.JanusGraphProperty;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.log4j.Logger;

import java.io.File;

/**
 * Class representing local configuration file janusgraph.properties.
 * Only one instance of the class is allowed within the application.
 *
 * @author Lucie Svitáková (svitaluc@fit.cvut.cz)
 */
public class JanusGraphProperties extends Configuration implements GraphDBConfiguration {
    /** {@link JanusGraphProperties} instance representing configuration properties of the JanusGraph. */
    private static JanusGraphProperties instance;
    /** Properties of the pom file in the graphbench-janusgraph module. */
    private PomProperties pomProperties;
    /** Relative path to JanusGraph properties */
    private final static String PROPERTIES_PATH = "conf" + File.separator + "janusgraph" + File.separator + "janusgraph.properties";
    /** Relative path to the file containing pom properties of the graphbench-janusgraph module. */
    private final static String POM_PROPERTIES_PATH = "pom-properties" + File.separator + "janusgraph-pom.properties";
    /** Logger. */
    private final static Logger LOG = Logger.getLogger(JanusGraphProperties.class);

    /**
     * Constructor of the {@link JanusGraphProperties}
     */
    private JanusGraphProperties() {
        Configurations configs = new Configurations();
        try {
            String jarPath = Util.getJarPath();
            // janusgraph properties
            String absoluteConfigPath = jarPath + File.separator + PROPERTIES_PATH;
            PropertiesConfiguration config = configs.properties(new File(absoluteConfigPath));
            setConfig(config, absoluteConfigPath);

            //janusgraph pom properties
//            String absolutePomPropertiesPath = jarPath + File.separator + pomPropertiesPath;
            this.pomProperties = new PomProperties(POM_PROPERTIES_PATH);
        } catch (ConfigurationException e) {
            LOG.error("Configuration file" + PROPERTIES_PATH + " cannot be opened.", e);
        }
    }

    /**
     * Get instance of the config.properties representation.
     * @return content of the config.properties file represented as an instance of {@link JanusGraphProperties} class
     */
    public static JanusGraphProperties getInstance() {
        if (instance == null) {
            instance = new JanusGraphProperties();
        }
        return instance;
    }

    @Override
    public String getIndexBackend() {
        return getStringProperty(JanusGraphProperty.INDEX_SEARCH_BACKEND);
    }

    @Override
    public String getIndexVersion() {
        String backend = getIndexBackend().toLowerCase();
        return pomProperties.getStringProperty("janusgraph." + backend + ".version");
    }

    @Override
    public String getStorageBackend() {
        return getStringProperty(JanusGraphProperty.STORAGE_BACKEND);
    }

    @Override
    public String getDatabaseVersion() {
        return pomProperties.getStringProperty(JanusGraphPomProperty.DATABASE_VERSION);
    }
}
