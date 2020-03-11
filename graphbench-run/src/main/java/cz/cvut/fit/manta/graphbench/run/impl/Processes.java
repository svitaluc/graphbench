package cz.cvut.fit.manta.graphbench.run.impl;

import cz.cvut.fit.manta.graphbench.all.App;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public final class Processes {
    private static final String NEWLINE = System.getProperty("line.separator");

    /**
     * @param command the command to run
     * @return the output of the command
     * @throws IOException if an I/O error occurs
     */
    public static String run(String... command) throws IOException {
        ProcessBuilder pb = new ProcessBuilder(command).redirectErrorStream(true);
        Process process = pb.start();
        StringBuilder result = new StringBuilder(1024);
        try (BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            while (true) {
                String line = in.readLine();
                if (line == null || line.contains(App.BENCHMARK_FINISHED))
                    break;
                System.out.println(line);
                result.append(line).append(NEWLINE);

            }
        }
        process.destroy();
        return result.toString();
    }

    /**
     * Prevent construction.
     */
    private Processes() {
    }
}