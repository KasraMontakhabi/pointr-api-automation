package com.pointr.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TestDataLoader {

    private static final Logger log = LoggerFactory.getLogger(TestDataLoader.class);

    public static String load(String relativePath) {
        String fullPath = "src/test/resources/" + relativePath;
        try {
            String content = new String(Files.readAllBytes(Paths.get(fullPath)));
            log.info("Loaded test data from: {}", fullPath);
            return content;
        } catch (IOException e) {
            log.error("Failed to load test data from: {}", fullPath);
            throw new RuntimeException("Could not load test data file: " + fullPath, e);
        }
    }
}