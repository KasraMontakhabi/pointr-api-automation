package com.pointr.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigManager {

    private static final Properties properties = new Properties();

    static {
        String env = System.getProperty("env", "dev");
        String fileName = "config/" + env + ".properties";

        try (InputStream input = ConfigManager.class
                .getClassLoader()
                .getResourceAsStream(fileName)) {

            if (input == null) {
                throw new RuntimeException("Config file not found: " + fileName);
            }
            properties.load(input);

        } catch (IOException e) {
            throw new RuntimeException("Failed to load config: " + fileName, e);
        }
    }

    public static String getBaseUrl() {
        return properties.getProperty("base.url");
    }

    public static int getPort() {
        return Integer.parseInt(properties.getProperty("port"));
    }

    public static String getBasePath() {
        return properties.getProperty("api.basePath");
    }

    public static String getSitesEndpoint() {
        return properties.getProperty("api.basePath") + "/sites";
    }

    public static String getBuildingsEndpoint() {
        return properties.getProperty("api.basePath") + "/buildings";
    }

    public static String getLevelsEndpoint() {
        return properties.getProperty("api.basePath") + "/levels";
    }
}