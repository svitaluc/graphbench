package cz.cvut.fit.manta.graphbench.data.generator;

import org.apache.log4j.Logger;

import java.io.IOException;

/**
 *
 *
 * @author Lucie Svitáková (svitaluc@fit.cvut.cz)
 */
public class App {
    private static final Logger LOGGER = Logger.getLogger(App.class);

    public static void main(String[] args) {
        // ! Fill in the output path !
        final String OUTPUT_PATH = "";
        final Long VERTICES_AMOUNT = 112L;
        final Long EDGES_AMOUNT = 112L;

        DataGenerator generator = new DataGenerator(OUTPUT_PATH);

        try {
            generator.generate(VERTICES_AMOUNT, EDGES_AMOUNT);
        } catch (IOException e) {
            LOGGER.error("Problem when generating data.", e);
        }
    }
}
