/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.serverchassis.springboot;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.odpi.openmetadata.serverchassis.springboot.config.SSLEnvironmentConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@AutoConfigureAfter(SSLEnvironmentConfiguration.class)
class OMAGServerTests {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    SSLEnvironmentConfiguration initializingBeanConfig;

    @Test
    void contextLoads() {
        assertNotNull(objectMapper);
        assertNotNull(initializingBeanConfig);

    }

}
