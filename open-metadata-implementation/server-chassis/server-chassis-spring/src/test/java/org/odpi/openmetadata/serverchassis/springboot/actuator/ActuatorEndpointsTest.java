/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.serverchassis.springboot.actuator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.odpi.openmetadata.serverchassis.springboot.OMAGServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalManagementPort;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.client.HttpClientErrorException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {
                OMAGServer.class
        })
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource(properties = "spring.config.additional-location=classpath:application-test.properties")
public class ActuatorEndpointsTest {
    public static final String URL = "http://localhost:";
    public static final String MANAGMENT_CONTEXT_PATH = "/observability/actuator";

    @Autowired
    RestTemplateBuilder restTemplateBuilder;

    @LocalManagementPort
    int managementPort;

    @Test
    public void testMetricsActuator() throws JSONException {

        ResponseEntity<String> response = restTemplateBuilder
                .rootUri(URL + managementPort + MANAGMENT_CONTEXT_PATH)
                .build().exchange("/metrics", HttpMethod.GET, new HttpEntity<>(null), String.class);

        assertInstanceOf(ResponseEntity.class, response);
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());

        JSONObject body = new JSONObject(response.getBody());
        assertNotNull(body);

        JSONArray metricsNamesArray = (JSONArray) body.get("names");
        assertEquals(49, metricsNamesArray.length());

    }

    @ParameterizedTest
    @ValueSource(strings = {
            "/metrics/application.started.time",
            "/metrics/application.ready.time",
            "/health"})
    public void testMetricsActuatorCustomisableHealthEndpoint(String args) throws JSONException {

        ResponseEntity<String> response = restTemplateBuilder
                .rootUri(URL + managementPort + MANAGMENT_CONTEXT_PATH)
                .build().exchange(args, HttpMethod.GET, new HttpEntity<>(null), String.class);

        assertInstanceOf(ResponseEntity.class, response);
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());

        JSONObject body = new JSONObject(response.getBody());
        assertNotNull(body);

    }

    @Test
    public void testMetricsActuatorBeans() throws JSONException {

        assertThrows(HttpClientErrorException.class, () -> {
            restTemplateBuilder
                    .rootUri(URL + managementPort + MANAGMENT_CONTEXT_PATH)
                    .build().exchange("/beans", HttpMethod.GET, new HttpEntity<>(null), String.class);
        });
    }

    @Test
    public void testMetricsActuatorContexts() throws JSONException {

        assertThrows(HttpClientErrorException.class, () -> {
            restTemplateBuilder
                    .rootUri(URL + managementPort + MANAGMENT_CONTEXT_PATH)
                    .build().exchange("/contexts", HttpMethod.GET, new HttpEntity<>(null), String.class);
        });
    }

}
