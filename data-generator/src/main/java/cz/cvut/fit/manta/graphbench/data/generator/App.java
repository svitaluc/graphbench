package cz.cvut.fit.manta.graphbench.data.generator;

import java.io.IOException;

public class App {

    public static void main(String[] args) {
        // ! Fill in the output path !
        final String outputPath = "";
        final Long verticesAmount = 112L;
        final Long edgesAmount = 112L;

        DataGenerator generator = new DataGenerator(outputPath);

        try {
            generator.generate(verticesAmount, edgesAmount);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
