package eu.profinit.manta.graphbench.run.impl;

import eu.profinit.manta.graphbench.core.config.ConfigProperties;
import eu.profinit.manta.graphbench.core.config.model.ConfigProperty;
import eu.profinit.manta.graphbench.core.db.product.GraphDBType;
import org.apache.log4j.Logger;

import java.io.*;

public class Launcher {
    private final Logger LOG = Logger.getLogger(Launcher.class);
    private String datasetDir50 = "C:\\Users\\lsvitakova\\tmp\\data-generator\\v-10-e-30";
//    private String datasetDir50 = "C:\\Users\\lsvitakova\\tmp\\data-generator\\v-1000-e-1000";
    private ConfigProperties configProperties = ConfigProperties.getInstance();

    public void run() {
        runSetting(3, datasetDir50, GraphDBType.JANUSGRAPH);
//        runSetting(3, datasetDir50, GraphDBType.TITAN);
    }

    private void runSetting(Integer repetitions, String datasetDir, GraphDBType dbType) {
        configProperties.setProperty(ConfigProperty.DATASET_DIR, datasetDir);
        configProperties.setProperty(ConfigProperty.DATABASE_TYPE, dbType.getName());

        String jarPath = new File("graphbench-run/target/"+
                "graphbench-run-1.0-SNAPSHOT/graphbench-run-1.0-SNAPSHOT/" +
                "graphbench.jar").getAbsolutePath();

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
