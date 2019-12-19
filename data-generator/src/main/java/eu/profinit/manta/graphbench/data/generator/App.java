package eu.profinit.manta.graphbench.data.generator;

import java.io.IOException;

public class App {

    public static void main(String[] args) {
        String outputPath = "/media/veracrypt54/data/2019-12-02-materialy/data";
        final Long nodesAmount = 50000L;
        final Long edgesAmount = 200000L;

        DataGenerator generator = new DataGenerator(outputPath);

        try {
            generator.generate(nodesAmount, edgesAmount);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
