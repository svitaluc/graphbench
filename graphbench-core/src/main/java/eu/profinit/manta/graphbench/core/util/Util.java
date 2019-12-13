package eu.profinit.manta.graphbench.core.util;

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
}
