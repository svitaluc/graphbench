package eu.profinit.manta.graphbench.core.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;

public class Util {
    public static void clearDirectory(String directory, Logger LOG) {
        File directoryPathFile = new File(directory);
        LOG.debug("Directory to be deleted: " + directoryPathFile.getAbsolutePath());
        try {
            FileUtils.cleanDirectory(directoryPathFile);
        } catch (IOException e) {
            LOG.error(e.getStackTrace());
        }
    }

    public static String getJarPath() {
        File tmp = new File(Util.class.getProtectionDomain().getCodeSource().getLocation().getPath());
        return tmp.getParentFile().getAbsolutePath();
    }

    /**
     *
     * @param configPath relative path of the config file within the resources directory
     * @return
     */
    public static <T> T getConfigFile(Class<T> configRepresentation, String configPath, Logger LOG) {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        String jarPath = Util.getJarPath();
        File yamlFile = new File(jarPath + File.separator + configPath);
        try {
            return mapper.readValue(yamlFile, configRepresentation);
        } catch (Exception e) {
            LOG.error("Couldn't read a config yaml file at '" + configPath + "'.", e);
        }
        return null;
    }
}
