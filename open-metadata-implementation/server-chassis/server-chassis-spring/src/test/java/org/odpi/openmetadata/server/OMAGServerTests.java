/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.odpi.openmetadata.server.config.InitializingBeanConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@AutoConfigureAfter(InitializingBeanConfig.class)
class OMAGServerTests {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    InitializingBeanConfig initializingBeanConfig;

    @Test
    void contextLoads() {
        assertNotNull(objectMapper);
        assertNotNull(initializingBeanConfig);

    }

}
