package cz.cvut.fit.manta.graphbench.run;

import cz.cvut.fit.manta.graphbench.run.impl.Launcher;
import cz.cvut.fit.manta.graphbench.db.GraphDBType;

/**
 * Class intended for automatic launch of the benchmark tests.
 * Dataset directory must be set. You can call any number of the
 * {@link Launcher#runSetting(Integer, String, GraphDBType)}
 * method, with any settings.
 *
 * @author Lucie Svitáková (svitaluc@fit.cvut.cz)
 */
public class AutomaticLauncher {

    /**
     * The main method to be called to run the benchmarks.
     * @param args Main method arguments
     */
    public static void main(String[] args) {
        // ! Fill in the dataset directory !
        String datasetDir = "C:\\Users\\lsvitakova\\tmp\\data-generator\\v-1k-e-1k";

        Launcher launcher = new Launcher();

        launcher.runSetting(1, datasetDir, GraphDBType.JANUSGRAPH);
//        launcher.runSetting(3, datasetDir, GraphDBType.TITAN);
    }
}
