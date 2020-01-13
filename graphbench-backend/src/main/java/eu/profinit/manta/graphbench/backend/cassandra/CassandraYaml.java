package eu.profinit.manta.graphbench.backend.cassandra;

import eu.profinit.manta.graphbench.core.config.Configuration;
import eu.profinit.manta.graphbench.core.util.Util;
import org.apache.commons.configuration2.YAMLConfiguration;
import org.apache.commons.configuration2.ex.ConfigurationException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class CassandraYaml extends Configuration {

    private static CassandraYaml INSTANCE;
    private static String propertiesPath;

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

    public static CassandraYaml getInstance(CassandraVersion cassandraVersion) {
        if(INSTANCE == null) {
            propertiesPath = "conf" + File.separator + "cassandra" + File.separator + cassandraVersion.getFileName();
            INSTANCE = new CassandraYaml(propertiesPath);
        }
        return INSTANCE;
    }

    public String getRelativePropertiesPath() {
        return propertiesPath;
    }

    public String getAbsolutePropertiesPath() {
        return Util.getJarPath() + File.separator + propertiesPath;
    }
}
