package cz.cvut.fit.manta.graphbench.run.impl;

import cz.cvut.fit.manta.graphbench.core.config.ConfigProperties;
import cz.cvut.fit.manta.graphbench.core.config.model.ConfigProperty;
import cz.cvut.fit.manta.graphbench.core.util.Util;
import cz.cvut.manta.graphbench.db.GraphDBType;
import org.apache.log4j.Logger;

import java.io.*;

/**
 * Class running the benchmark jar file.
 *
 * @author Lucie Svitáková (svitaluc@fit.cvut.cz)
 */
public class Launcher {
    /** Logger. */
    private final static Logger LOG = Logger.getLogger(Launcher.class);
    /** Configuration properties set in the config.properties file. */
    private final ConfigProperties CONFIG_PROPERTIES = ConfigProperties.getInstance();

    /**
     * Calls the benchmark jar file, the number of times specified by {@code repetitions}, with dataset
     * set with {@code datasetDir} and on database determined by {@code dbType}.
     * @param repetitions Number of times the jar file will be called
     * @param datasetDir Directory of a dataset to be used
     * @param dbType Type of the graph database to be used
     */
    public void runSetting(Integer repetitions, String datasetDir, GraphDBType dbType) {
        CONFIG_PROPERTIES.setProperty(ConfigProperty.DATASET_DIR, datasetDir);
        CONFIG_PROPERTIES.setProperty(ConfigProperty.DATABASE_TYPE, dbType.getName());

        String jarPath = Util.getJarPath() + File.separator + "graphbench.jar";

        for (int i = 0; i < repetitions; i++) {
            try {
                String output = Processes.run("java", "-jar", "-Xmx4g", jarPath);
                LOG.info("Process " + i + " finished.");
            } catch (IOException e) {
                LOG.error("Cannot run the jar file at " + jarPath, e);
            }
        }
    }
}
