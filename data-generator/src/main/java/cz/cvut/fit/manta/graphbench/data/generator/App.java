package cz.cvut.fit.manta.graphbench.data.generator;

import org.apache.log4j.Logger;

import java.io.IOException;

/**
 * Main class for running data generation. Three attributes must be set
 *  <ul>
 *      <li> OUTPUT_PATH - directory in which the generated data will be stored </li>
 *      <li> VERTICES_AMOUNT - number of vertices to be generated </li>
 *      <li> EDGES_AMOUNT - number of edges to be generated </li>
 *  </ul>
 *
 * @author Lucie Svitáková (svitaluc@fit.cvut.cz)
 */
public class App {
    /** ! FILL IN AN OUTPUT PATH !
      * Directory in which the generated data will be stored. */
    private final static String OUTPUT_PATH = "";
    /** Number of vertices to be generated. */
    private final static Long VERTICES_AMOUNT = 112L;
    /** Number of edges to be generated. */
    private final static Long EDGES_AMOUNT = 112L;

    /** Logger. */
    private final static Logger LOGGER = Logger.getLogger(App.class);

    /**
     * The main method to be run to generate the data.
     * @param args Main arguments
     */
    public static void main(String[] args) {
        DataGenerator generator = new DataGenerator(OUTPUT_PATH);

        try {
            generator.generate(VERTICES_AMOUNT, EDGES_AMOUNT);
        } catch (IOException e) {
            LOGGER.error("Problem when generating data.", e);
        }
    }
}
