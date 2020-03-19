package cz.cvut.fit.manta.graphbench.core.config;

/**
 * Interface declaring basic methods required by all the classes representing enumeration of configuration properties.
 *
 * @author Lucie Svitáková (svitaluc@fit.cvut.cz)
 */
public interface ConfigurationProperty {
    /**
     * @return Name of the configuration property.
     */
    String getName();

    /**
     * @return Class type of the property value.
     */
    Class getClazz();
}
