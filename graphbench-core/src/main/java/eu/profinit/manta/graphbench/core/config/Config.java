package eu.profinit.manta.graphbench.core.config;

import eu.profinit.manta.graphbench.core.util.Util;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.log4j.Logger;
import eu.profinit.manta.graphbench.core.db.product.GraphDBType;
import eu.profinit.manta.graphbench.core.test.TestType;

import java.io.File;
import java.io.IOException;

public class Config {
    private static Config INSTANCE;
    static PropertiesConfiguration config;
    final static Logger LOG = Logger.getLogger(Config.class);

    private Config() {
        Configurations configs = new Configurations();
        try {
            String jarPath = Util.getJarPath();
            config = configs.properties(new File(jarPath + File.separator + "conf" + File.separator + "config.properties"));
            LOG.info("CONFIG: " + config.toString());
        } catch (ConfigurationException e) {
            LOG.error("Properties file cannot be opened.", e);
        }
    }

    public static Config getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new Config();
        }
        return INSTANCE;
    }

    public String getStringProperty(Property property) {
        getPropertyCheck(property, String.class.toString());
        return config.getString(property.getName());
    }

    /**
     * Loads path property from the config file. It always returns absolute path, although a relative path
     * is stated in the config file.
     * @param property property to be read
     * @return String representing absolute path to a file/directory in the property
     */
    public String getPathProperty(Property property) throws IOException {
        String originalPath = getStringProperty(property);
        File tmp = new File(originalPath);
        return tmp.getCanonicalPath();
    }

    public boolean getBooleanProperty(Property property) {
        getPropertyCheck(property, boolean.class.toString());
        return config.getBoolean(property.getName());
    }

    public Integer getIntegerProperty(Property property) {
        getPropertyCheck(property, Integer.class.toString());
        return config.getInt(property.getName());
    }

    public TestType getTestTypeProperty(Property property) {
        getPropertyCheck(property, String.class.toString());
        String testOption = config.getString(property.getName());
        return TestType.getTestType(testOption);
    }

    public GraphDBType getGraphDBTypeProperty(Property property) {
        getPropertyCheck(property, String.class.toString());
        String graphOption = config.getString(property.getName());
        return GraphDBType.getGraphDBType(graphOption);
    }

    private void getPropertyCheck(Property property, String clazz) {
        String propertyClazz = property.getClazz().toString();

        if (!propertyClazz.equals(clazz)) {
            LOG.error("The property " + property.getName() + " is not " + clazz + ".");
        }
    }
}
