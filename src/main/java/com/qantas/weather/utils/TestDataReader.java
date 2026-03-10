package com.qantas.weather.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qantas.weather.models.StateMetadata;

import java.io.InputStream;

/**
 * Reads metadata / fixture files from the test classpath.
 *
 * Files are placed under src/test/resources/ and are therefore
 * available on the test classpath at runtime.
 *
 * Usage:
 *   StateMetadata meta = TestDataReader.loadUsStates("testdata/us_states.json");
 */
public class TestDataReader {

    // Reusable ObjectMapper — ObjectMapper is thread-safe after configuration
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private TestDataReader() {
        // Utility class — no instances needed
    }

    public static StateMetadata loadUsStates(String classpathPath) {
        // getClassLoader().getResourceAsStream looks in the test classpath root
        try (InputStream is = TestDataReader.class
                .getClassLoader()
                .getResourceAsStream(classpathPath)) {

            if (is == null) {
                throw new RuntimeException(
                        "Metadata file not found on classpath: " + classpathPath +
                                ". Ensure the file exists under src/test/resources/" + classpathPath);
            }
            return MAPPER.readValue(is, StateMetadata.class);

        } catch (Exception e) {
            throw new RuntimeException(
                    "Failed to load or parse metadata file: " + classpathPath, e);
        }
    }
}