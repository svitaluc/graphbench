package cz.cvut.fit.manta.graphbench.core.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.javaprop.JavaPropsFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
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
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
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
                if ((new File("pom.xml")).exists())
                    model = reader.read(new FileReader("pom.xml"));
                else
                    model = reader.read(new InputStreamReader(Util.class.getResourceAsStream(
                             "/META-INF/maven/cz.cvut.fit.manta/graphbench-all/pom.xml")));
                version = model.getVersion();
            } catch (IOException | XmlPullParserException e) {
                logger.error("Couldn't read the project version from a pom file.", e);
            }
        }
        return version;
    }

    /**
     * Loads a configuration file into an object of the specified class.
     * @param configRepresentation class representing all properties of the configuration
     * @param configPath relative path of the configuration file within the target directory
     * @param LOG logger for event logging
     * @return object representing all properties of the configuration file with those properties loaded
     */
    public static <T> T getConfigurationObject(Class<T> configRepresentation, String configPath, Logger LOG) {
        ObjectMapper mapper = createObjectMapper(configPath);
        String jarPath = Util.getJarPath();
        File configFile = new File(jarPath + File.separator + configPath);
        try {
            return mapper.readValue(configFile, configRepresentation);
        } catch (Exception e) {
            LOG.error("Couldn't read a configuration file at '" + configPath + "'.", e);
        }
        return null;
    }

    /**
     * Creates a general object mapper corresponding to a type of the file.
     * @param path file name or file path
     * @return object mapper for a file type specified by the path.
     */
    private static ObjectMapper createObjectMapper(String path) {
        String[] splitPath = path.split("\\.");
        if (splitPath.length < 1) {
            throw new IllegalArgumentException("The path '" + path +
                    "' does not contain a file with a specified file type.");
        }
        String fileType = splitPath[1].toLowerCase();
        switch (fileType) {
            case "yaml":
                return new ObjectMapper(new YAMLFactory());
            case "properties":
                return new ObjectMapper(new JavaPropsFactory());
            default:
                throw new UnsupportedOperationException("The file type " + fileType +
                        " is not yet supported for object mapper.");
        }
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

    /**
     * Finds all proprietary getter methods of a given class (all getter methods recognized
     * by @link{JanusGraphPropertyFile.isProprietaryGetter}).
     * @param clazz class of which we want to obtain all getters
     * @return list of getter methods
     */
    private static List<Method> getGetterMethods(Class clazz) {
        Method[] methods = clazz.getMethods();
        List<Method> getters = new ArrayList<>();

        for(Method method : methods){
            if (isProprietaryGetter(method)) {
                getters.add(method);
            }
        }
        return getters;
    }

    /**
     * A method is considered to be a getter if it
     *  - starts with "get"
     *  - has no parameter
     *  - returns void type
     *  - Object method getClass is not a proprietary getter
     * @param method
     * @return
     */
    private static boolean isProprietaryGetter(Method method){
        if (!method.getName().startsWith("get")) return false;
        if (method.getParameterTypes().length != 0) return false;
        if (void.class.equals(method.getReturnType())) return false;
        if (method.getName().startsWith("getClass")) return false;
        return true;
    }

    /**
     * Decides whether the given object is an end property (boolean, string, integer,...).
     * @param value potential property value
     * @return true if the object is a class of java.lang library
     */
    private static boolean isEndProperty(Object value) {
        Class clazz = value.getClass();
        return clazz.getName().contains("java.lang");
    }
}
