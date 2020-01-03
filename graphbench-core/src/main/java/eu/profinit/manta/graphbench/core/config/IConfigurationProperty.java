package eu.profinit.manta.graphbench.core.config;

/**
 * Interface declaring basic methods required by all the classes representing enumeration of configuration properties.
 */
public interface IConfigurationProperty {
    String getName();
    Class getClazz();
}
