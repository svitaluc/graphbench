package cz.cvut.fit.manta.graphbench.core.config;

import org.apache.commons.configuration2.AbstractConfiguration;
import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.io.FileHandler;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Class for reading and setting properties of a configuration file.
 *
 * @author Lucie Svitáková (svitaluc@fit.cvut.cz)
 */
public abstract class Configuration {
    /** Representation of configuration properties. */
    private AbstractConfiguration config;
    /** Handler of the configuration file. */
    private FileHandler fileHandler;
    /** Logger. */
    private final static Logger LOG = Logger.getLogger(Configuration.class);

    /**
     * Sets configuration variable.
     * @param config configuration from a file
     */
    public void setConfig(AbstractConfiguration config, String configPath) {
        this.config = config;
        setFileHandler(config, configPath);
    }

    /**
     * Setter for a handler of the configuration file.
     * @param configLoaded Already loaded configuration
     * @param configPath Path to the configuration file
     */
    protected void setFileHandler(AbstractConfiguration configLoaded, String configPath) {
        fileHandler = new FileHandler((FileBasedConfiguration)configLoaded);
        fileHandler.setFile(new File(configPath));
    }

    /**
     * Gets all properties as a map of key - value pairs.
     * @return map of key - value ({@link String} - {@link Object}) pairs
     */
    public Map<String, Object> getAllProperties() {
        Iterator<String> keys = config.getKeys();
        Map<String, Object> properties = new HashMap<>();
        while (keys.hasNext()) {
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
    public String getStringProperty(ConfigurationProperty property) {
        propertyCheck(property, String.class.toString());
        return config.getString(property.getName());
    }

    /**
     * Gets a property of {@link String} type. Usage only for cases
     * when {@link ConfigurationProperty} cannot be used.
     * @param property property to be acquired
     * @return value of the property in the config
     */
    public String getStringProperty(String property) {
        return config.getString(property);
    }

    /**
     * Gets a property of {@link String} array type.
     * @param property property to be acquired
     * @return value of the property
     */
    public String[] getStringArrayProperty(ConfigurationProperty property) {
        propertyCheck(property, String[].class.toString());
        return config.getStringArray(property.getName());
    }

    /**
     * Loads path property from the config file. It always returns absolute path, although a relative path
     * is stated in the config file.
     * @param property property to be read
     * @return String representing absolute path to a file/directory in the property
     */
    public String getPathProperty(ConfigurationProperty property) throws IOException {
        String originalPath = getStringProperty(property);
        File tmp = new File(originalPath);
        return tmp.getCanonicalPath();
    }

    /**
     * Gets a property of boolean type.
     * @param property property to be acquired
     * @return value of the property
     */
    public boolean getBooleanProperty(ConfigurationProperty property) {
        propertyCheck(property, boolean.class.toString());
        return config.getBoolean(property.getName());
    }

    /**
     * Gets a property of {@link Integer} type.
     * @param property property to be acquired
     * @return value of the property
     */
    public Integer getIntegerProperty(ConfigurationProperty property) {
        propertyCheck(property, Integer.class.toString());
        return config.getInt(property.getName());
    }

    /**
     * Sets given {@code property} and its {@code value} in the configuration file.
     * @param property property to be stored
     * @param value its value
     */
    public void setProperty(ConfigurationProperty property, Object value) {
        config.setProperty(property.getName(), value);
    }

    /**
     * Checks whether the {@code property} is indeed defined as an instance of the class {@code clazz}.
     * Logs an error if the classes do not match.
     * @param property property which type is checked
     * @param clazz class to be checked with
     */
    private void propertyCheck(ConfigurationProperty property, String clazz) {
        String propertyClazz = property.getClazz().toString();

        if (!propertyClazz.equals(clazz)) {
            LOG.error("The property " + property.getName() + " is not " + clazz + ".");
        }
    }
}
