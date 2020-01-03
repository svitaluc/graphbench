package eu.profinit.manta.graphbench.janusgraph.config;

import eu.profinit.manta.graphbench.core.config.Configuration;
import eu.profinit.manta.graphbench.core.util.Util;
import org.apache.commons.configuration2.YAMLConfiguration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class CassandraYaml extends Configuration {

    private static CassandraYaml INSTANCE;
    private final static String propertiesPath = "conf" + File.separator + "cassandra" + File.separator + "cassandra.yaml";

    private CassandraYaml(String configPath) {
        Configurations configs = new Configurations();
        try {
            String jarPath = Util.getJarPath();
            YAMLConfiguration config = new YAMLConfiguration();
            config.read(new FileReader(jarPath + File.separator + configPath));
            setConfig(config);
        } catch (FileNotFoundException e) {
            LOG.error("Configuration file " + configPath + " was not found.", e);
        } catch (ConfigurationException e) {
            LOG.error("Configuration file" + configPath + " cannot be opened.", e);
        }
    }

    public static CassandraYaml getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new CassandraYaml(propertiesPath);
        }
        return INSTANCE;
    }

    public static String getAbsolutePropertiesPath() {
        return Util.getJarPath() + File.separator + propertiesPath;
    }
}
