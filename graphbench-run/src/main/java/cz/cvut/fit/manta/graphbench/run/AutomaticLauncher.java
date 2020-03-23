package cz.cvut.fit.manta.graphbench.run;

import cz.cvut.fit.manta.graphbench.run.impl.Launcher;
import cz.cvut.manta.graphbench.db.GraphDBType;

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
        String datasetDir = "";

        Launcher launcher = new Launcher();

        launcher.runSetting(1, datasetDir, GraphDBType.TITAN);
//        launcher.runSetting(3, datasetDir, GraphDBType.TITAN);
    }
}
