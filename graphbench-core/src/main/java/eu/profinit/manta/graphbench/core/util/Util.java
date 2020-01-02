package eu.profinit.manta.graphbench.core.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.javaprop.JavaPropsFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.hibernate.validator.internal.util.ReflectionHelper.getPropertyName;

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

    /**
     * Gets path of the directory from which the jar file was executed.
     * @return
     */
    public static String getJarPath() {
        File tmp = new File(Util.class.getProtectionDomain().getCodeSource().getLocation().getPath());
        return tmp.getParentFile().getAbsolutePath();
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
            throw new IllegalArgumentException("The path '" + path + "' does not contain a file with a specified file type.");
        }
        String fileType = splitPath[1].toLowerCase();
        switch (fileType) {
            case "yaml": return new ObjectMapper(new YAMLFactory());
            case "properties": return new ObjectMapper(new JavaPropsFactory());
            default: throw new UnsupportedOperationException("The file type " + fileType + " is not yet supported for object mapper.");
        }
    }

    public static void setConfiguration(Configuration configuration, String dbPath, Object clazz) {
        List<Method> getters = getGetterMethods(clazz.getClass());
        Map<String, Object> configurationMap = new HashMap<>();

        getters.forEach(getter -> {
            Object value = null;
            try {
                value = getter.invoke(clazz);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            if(value != null) {
                getEndProperty(getPropertyName(getter), value, configurationMap);
            }
        });

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
            if(isProprietaryGetter(method)) {
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
        if(!method.getName().startsWith("get")) return false;
        if(method.getParameterTypes().length != 0) return false;
        if(void.class.equals(method.getReturnType())) return false;
        if(method.getName().startsWith("getClass")) return false;
        return true;
    }

    private static Map<String, Object> getEndProperty(String name, Object value, Map<String, Object> configurationMap) {
        if (isEndProperty(value)) {
            Map<String, Object> propertyMap = new HashMap<>();
            propertyMap.put(name, value);
            return propertyMap;
        }
        List<Method> getters = getGetterMethods(value.getClass());
        getters.forEach(getter -> {
            Object getterValue = null;
            try {
                getterValue = getter.invoke(value);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            if(getterValue != null) {
                Map<String, Object> endProperty = getEndProperty(name + "." + getPropertyName(getter), getterValue, configurationMap);
                if (endProperty != null) {
                    endProperty.forEach(configurationMap::putIfAbsent);
                }
            }
        });
        return null;
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
