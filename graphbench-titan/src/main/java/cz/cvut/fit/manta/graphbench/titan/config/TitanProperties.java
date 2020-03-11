package cz.cvut.fit.manta.graphbench.titan.config;

import cz.cvut.fit.manta.graphbench.core.config.Configuration;
import cz.cvut.fit.manta.graphbench.core.config.GraphDBConfiguration;
import cz.cvut.fit.manta.graphbench.core.config.PomProperties;
import cz.cvut.fit.manta.graphbench.core.util.Util;
import cz.cvut.fit.manta.graphbench.titan.config.model.TitanPomProperty;
import cz.cvut.fit.manta.graphbench.titan.config.model.TitanProperty;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;

import java.io.File;

/**
 * Class representing local configuration file titan.properties.
 * Only one instance of the class is allowed within the application.
 */
public class TitanProperties extends Configuration implements GraphDBConfiguration {

    private static TitanProperties INSTANCE;
    private PomProperties pomProperties;
    private final static String propertiesPath = "conf" + File.separator + "titan" + File.separator + "titan.properties";
    private final static String pomPropertiesPath = "pom-properties" + File.separator + "titan-pom.properties";

    private TitanProperties(String configPath) {
        Configurations configs = new Configurations();
        try {
            // titan properties
            String jarPath = Util.getJarPath();
            String absoluteConfigPath = jarPath + File.separator + configPath;
            PropertiesConfiguration config = configs.properties(new File(jarPath + File.separator + configPath));
            setConfig(config, absoluteConfigPath);

            //titan pom properties
            String absolutePomPropertiesPath = jarPath + File.separator + pomPropertiesPath;
            this.pomProperties = new PomProperties(absolutePomPropertiesPath);
        } catch (ConfigurationException e) {
            LOG.error("Configuration file" + configPath + " cannot be opened.", e);
        }
    }

    /**
     * Get instance of the config.properties representation.
     * @return content of the config.properties file represented as an instance of {@link TitanProperties} class
     */
    public static TitanProperties getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new TitanProperties(propertiesPath);
        }
        return INSTANCE;
    }

    @Override
    public String getIndexBackend() {
        return getStringProperty(TitanProperty.STORAGE_INDEX_SEARCH_BACKEND);
    }

    @Override
    public String getIndexVersion() {
        String backend = getIndexBackend().toLowerCase();
        return pomProperties.getStringProperty("titan." + backend + ".version");
    }

    @Override
    public String getStorageBackend() {
        return getStringProperty(TitanProperty.STORAGE_BACKEND);
    }

    @Override
    public String getDatabaseVersion() {
        return pomProperties.getStringProperty(TitanPomProperty.DATABASE_VERSION);
    }
}
