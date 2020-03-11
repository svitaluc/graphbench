package cz.cvut.fit.manta.graphbench.run;

import cz.cvut.fit.manta.graphbench.core.db.product.GraphDBType;
import cz.cvut.fit.manta.graphbench.run.impl.Launcher;

/**
 * Class intended for automatic launch of the benchmark tests.
 * Dataset directory must be set. You can call any number of the {@link Launcher#runSetting(Integer, String, GraphDBType)}
 * method, with any settings.
 */
public class AutomaticLauncher {

    public static void main(String[] args) {
        // ! Fill in the dataset directory !
        String datasetDir = "";

        Launcher launcher = new Launcher();

        launcher.runSetting(3, datasetDir, GraphDBType.JANUSGRAPH);
//        launcher.runSetting(3, datasetDir, GraphDBType.TITAN);
    }
}
