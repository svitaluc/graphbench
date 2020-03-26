package cz.cvut.fit.manta.graphbench.core.util;

import cz.cvut.fit.manta.graphbench.core.config.Configuration;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;


/**
 * Class containing various utility functions used across the whole project.
 *
 * @author Lucie Svitáková (svitaluc@fit.cvut.cz)
 */
public class Util {
    /** Name of the graphbench-run module. */
    private final static String GRAPHBENCH_RUN = "graphbench-run";

    /**
     * Clears given directory out of all insides.
     * @param directory path to a directory to be cleared
     * @param LOG {@link Logger} instance
     */
    public static void clearDirectory(String directory, Logger LOG) {
        File directoryPathFile = new File(directory);
        LOG.debug("Directory to be deleted: " + directoryPathFile.getAbsolutePath());
        try {
            FileUtils.cleanDirectory(directoryPathFile);
        } catch (IOException e) {
            LOG.error(e.getStackTrace());
        }
    }

    /**
     * Gets path of the directory from which the jar file was executed.
     * @return an absolute path of the directory from which the jar file was executed.
     */
    public static String getJarPath() {
        String userDir = System.getProperty("user.dir");
        String version = getProjectVersion();
        String graphbenchRunWithVersion = GRAPHBENCH_RUN + "-" + version;
        return userDir + File.separator + GRAPHBENCH_RUN + File.separator + "target" +
                File.separator + graphbenchRunWithVersion + File.separator + graphbenchRunWithVersion;
    }

    /**
     * Gets a version of the project set in the pom.xml file.
     * @return project version
     */
    private static String getProjectVersion() {
        Logger logger = Logger.getLogger(Util.class);
        String version = Util.class.getPackage().getImplementationVersion();
        // case when the method is called outside of a jar file (f.e. a Launcher)
        // - the addDefaultImplementationEntries does not have an effect then
        if (version == null) {
            MavenXpp3Reader reader = new MavenXpp3Reader();
            Model model;
            try {
                if ((new File("pom.xml")).exists()) {
                    model = reader.read(new FileReader("pom.xml"));
                }
                else {
                    model = reader.read(new InputStreamReader(Util.class.getResourceAsStream(
                            "/META-INF/maven/cz.cvut.fit.manta/graphbench-all/pom.xml")));
                }
                version = model.getVersion();
            } catch (IOException | XmlPullParserException e) {
                logger.error("Couldn't read the project version from a pom file.", e);
            }
        }
        return version;
    }

    /**
     * Sets all the properties of the {@link Configuration} object into the
     * {@link org.apache.commons.configuration.Configuration} object.
     * @param configuration to be set
     * @param loaded containing properties to be copied
     */
    public static void setConfiguration(org.apache.commons.configuration.Configuration configuration, Configuration loaded) {
        Map<String, Object> configurationMap = loaded.getAllProperties();
        configurationMap.keySet().forEach(k -> {
            configuration.setProperty(k, configurationMap.get(k));
        });
    }
}
