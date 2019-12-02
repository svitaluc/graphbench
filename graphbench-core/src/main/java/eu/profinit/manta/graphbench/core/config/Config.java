package eu.profinit.manta.graphbench.core.config;

import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.log4j.Logger;
import eu.profinit.manta.graphbench.core.db.product.GraphDBType;
import eu.profinit.manta.graphbench.core.test.TestType;

public class Config {
    private static Config INSTANCE;
    static PropertiesConfiguration config;
    final static Logger LOG = Logger.getLogger(Config.class);

    private Config() {
        Configurations configs = new Configurations();
        try {
            config = configs.properties(Config.class.getClassLoader().getResource("config.properties"));
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
