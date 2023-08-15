/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.serverchassis.springboot;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.odpi.openmetadata.serverchassis.springboot.config.OMAGServerProperties;
import org.odpi.openmetadata.serverchassis.springboot.config.SSLEnvironmentConfiguration;
import org.odpi.openmetadata.serveroperations.server.OMAGServerOperationalServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@AutoConfigureAfter(SSLEnvironmentConfiguration.class)
@TestPropertySource(properties = "spring.config.additional-location=classpath:application-test.properties")
class OMAGServerTests {

    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    SSLEnvironmentConfiguration   initializingBeanConfig;
    @Autowired
    OMAGServerOperationalServices operationalServices;
    @Autowired
    OMAGServerProperties          omagServerProperties;

    @Test
    void contextLoads() {
        assertNotNull(objectMapper);
        assertNotNull(initializingBeanConfig);
        assertNotNull(omagServerProperties);
        assertNotNull(operationalServices);
    }

}
