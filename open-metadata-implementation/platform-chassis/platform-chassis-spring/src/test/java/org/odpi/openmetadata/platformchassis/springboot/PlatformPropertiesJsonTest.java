/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.platformchassis.springboot;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Some platform properties hold JSON documents spread over many lines.  A single wrong character in one of
 * them (a full stop where a comma belongs, say) is only found when a server configured from it fails to
 * start, so the JSON in both copies of the platform's application.properties is parsed here.
 */
public class PlatformPropertiesJsonTest
{
    private static final List<String> PROPERTY_FILES = List.of("application.properties",
                                                               "container.application.properties");

    private static final List<String> JSON_PROPERTIES = List.of("platform.placeholder.variables");

    private final ObjectMapper objectMapper = new ObjectMapper();


    @Test
    public void jsonPropertiesParse() throws Exception
    {
        String repositoryRoot = System.getProperty("egeria.repository.root");

        assertNotNull(repositoryRoot, "egeria.repository.root is not set by the build");

        for (String propertyFile : PROPERTY_FILES)
        {
            Path       path       = Path.of(repositoryRoot, propertyFile);
            Properties properties = new Properties();

            assertTrue(Files.exists(path), "Missing " + path);

            try (Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8))
            {
                properties.load(reader);
            }

            for (String jsonProperty : JSON_PROPERTIES)
            {
                String value = properties.getProperty(jsonProperty);

                assertNotNull(value, jsonProperty + " is missing from " + propertyFile);
                assertDoesNotThrow(() -> objectMapper.readTree(value),
                                   jsonProperty + " in " + propertyFile + " is not valid JSON");
            }
        }
    }
}
