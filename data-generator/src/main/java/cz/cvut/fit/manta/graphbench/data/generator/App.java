package cz.cvut.fit.manta.graphbench.data.generator;

import java.io.IOException;

public class App {

    public static void main(String[] args) {
//        final String outputPath = "C:\\Users\\lsvitakova\\tmp\\data-generator\\v-10-e-30";
        final String outputPath = "C:\\Users\\lsvitakova\\tmp\\data-generator\\trash";
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
