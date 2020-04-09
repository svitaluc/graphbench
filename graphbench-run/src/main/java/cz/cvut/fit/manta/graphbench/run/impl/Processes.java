package cz.cvut.fit.manta.graphbench.run.impl;

import cz.cvut.fit.manta.graphbench.all.App;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import org.apache.log4j.Logger;

/**
 * Class for creating processes with their output provided in a standard output.
 *
 * @author Lucie Svitáková (svitaluc@fit.cvut.cz)
 */
final class Processes {
    /** New line of a given system. */
    private final static String NEWLINE = System.getProperty("line.separator");
    /** Logger */
    private final static Logger LOGGER = Logger.getLogger(Processes.class);

    /**
     * Creates and runs a process defined with the {@code command} parameter. It provides its output in a standard
     * output. The process is destroyed when there is nothing more in output or a special flag of a finished benchmark
     * is detected.
     * The usage of the flag is necessary because JanusGraph with embedded Cassandra does not cancel the Cassandra
     * process which keeps providing an output.
     * @param command The command to run. Each part of the command can be in a separate field of the {@link String} array
     * @return The output of the command
     * @throws IOException Ff an I/O error occurs
     */
    static String run(String... command) throws IOException {
        ProcessBuilder pb = new ProcessBuilder(command).redirectErrorStream(true);
        Process process = pb.start();
        StringBuilder result = new StringBuilder(1024);
        try (BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            while (true) {
                String line = in.readLine();
                if (line == null || line.contains(App.BENCHMARK_FINISHED)) {
                    break;
                }
                LOGGER.info(line);
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