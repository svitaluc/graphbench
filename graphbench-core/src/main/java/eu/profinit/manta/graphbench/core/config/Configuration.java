package eu.profinit.manta.graphbench.core.config;

import org.apache.commons.configuration2.AbstractConfiguration;
import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.YAMLConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.configuration2.io.FileHandler;
import org.apache.log4j.Logger;
import eu.profinit.manta.graphbench.core.db.product.GraphDBType;
import eu.profinit.manta.graphbench.core.test.TestType;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Class for reading and setting properties of a configuration file.
 */
public abstract class Configuration {
    private AbstractConfiguration config;
    private FileHandler fileHandler;
    protected final Logger LOG = Logger.getLogger(Configuration.class);

    /**
     * Sets configuration variable.
     * @param config configuration from a file
     */
    public void setConfig(AbstractConfiguration config, String configPath) {
        this.config = config;
        fileHandler = new FileHandler((FileBasedConfiguration)config);
        fileHandler.setFile(new File(configPath));
    }

    /**
     * Gets all properties as a map of key - value pairs.
     * @return map of key - value ({@link String} - {@link Object}) pairs
     */
    public Map<String, Object> getAllProperties() {
        Iterator<String> keys = config.getKeys();
        Map<String, Object> properties = new HashMap<>();
        while(keys.hasNext()) {
            String key = keys.next();
            properties.put(key, config.getProperty(key));
        }
        return properties;
    }

    /**
     * Gets a property of {@link String} type.
     * @param property property to be acquired
     * @return value of the property
     */
    public String getStringProperty(IConfigurationProperty property) {
        propertyCheck(property, String.class.toString());
        return config.getString(property.getName());
    }

    /**
     * Gets a property of {@link String} array type.
     * @param property property to be acquired
     * @return value of the property
     */
    public String[] getStringArrayProperty(IConfigurationProperty property) {
        propertyCheck(property, String[].class.toString());
        return config.getStringArray(property.getName());
    }

    /**
     * Loads path property from the config file. It always returns absolute path, although a relative path
     * is stated in the config file.
     * @param property property to be read
     * @return String representing absolute path to a file/directory in the property
     */
    public String getPathProperty(IConfigurationProperty property) throws IOException {
        String originalPath = getStringProperty(property);
        File tmp = new File(originalPath);
        return tmp.getCanonicalPath();
    }

    /**
     * Gets a property of boolean type.
     * @param property property to be acquired
     * @return value of the property
     */
    public boolean getBooleanProperty(IConfigurationProperty property) {
        propertyCheck(property, boolean.class.toString());
        return config.getBoolean(property.getName());
    }

    /**
     * Gets a property of {@link Integer} type.
     * @param property property to be acquired
     * @return value of the property
     */
    public Integer getIntegerProperty(IConfigurationProperty property) {
        propertyCheck(property, Integer.class.toString());
        return config.getInt(property.getName());
    }

    /**
     * Gets a property of {@link TestType} type.
     * @param property property to be acquired
     * @return value of the property
     */
    public TestType getTestTypeProperty(IConfigurationProperty property) {
        propertyCheck(property, String.class.toString());
        String testOption = config.getString(property.getName());
        return TestType.getTestType(testOption);
    }

    /**
     * Gets a property of {@link GraphDBType} type.
     * @param property property to be acquired
     * @return value of the property
     */
    public GraphDBType getGraphDBTypeProperty(IConfigurationProperty property) {
        propertyCheck(property, String.class.toString());
        String graphOption = config.getString(property.getName());
        return GraphDBType.getGraphDBType(graphOption);
    }

    /**
     * Sets given {@code property} and its {@code value} in the configuration file.
     * @param property property to be stored
     * @param value its value
     */
    public void setProperty(IConfigurationProperty property, Object value) {
        config.setProperty(property.getName(), value);
//        try {
//            fileHandler.save();
//        } catch (ConfigurationException e) {
//            LOG.error("Can't set property " + property.getName() + ".", e);
//        }
    }

    /**
     * Checks whether the {@code property} is indeed defined as an instance of the class {@code clazz}.
     * Logs an error if the classes do not match.
     * @param property property which type is checked
     * @param clazz class to be checked with
     */
    private void propertyCheck(IConfigurationProperty property, String clazz) {
        String propertyClazz = property.getClazz().toString();

        if (!propertyClazz.equals(clazz)) {
            LOG.error("The property " + property.getName() + " is not " + clazz + ".");
        }
    }
}
