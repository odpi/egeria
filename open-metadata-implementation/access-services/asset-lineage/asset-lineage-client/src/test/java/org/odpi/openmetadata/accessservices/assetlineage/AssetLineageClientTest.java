/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetlineage;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.odpi.openmetadata.adapters.connectors.restclients.RESTClientConnector;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDListResponse;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertArrayEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

/**
 * The type Asset lineage client test.
 */
public class AssetLineageClientTest {

    private static final String SERVER_URL = "https://localhost:9444";
    private static final String SERVER_NAME = "TestServer";
    private static final String USER_ID = "zebra91";
    private static final String ENTITY_TYPE = "GlossaryTerm";
    private static final LocalDateTime UPDATED_AFTER_DATE = LocalDateTime.now().minusDays(1);

    private AssetLineage assetLineage;

    @Mock
    private RESTClientConnector connector;

    @Before
    public void before() throws Exception {
        MockitoAnnotations.initMocks(this);

        assetLineage = new AssetLineage(SERVER_NAME, SERVER_URL);
        Field connectorField = ReflectionUtils.findField(AssetLineage.class, "clientConnector");
        if (connectorField != null) {
            connectorField.setAccessible(true);
            ReflectionUtils.setField(connectorField, assetLineage, connector);
            connectorField.setAccessible(false);
        }
    }

    /**
     * Test publish entities
     *
     * @throws Exception the exception
     */
    @Test
    public void testPublishEntities() throws Exception {
        GUIDListResponse response = mockGUIDListResponse();

        when(connector.callGetRESTCall(eq("publishEntities"),
                eq(GUIDListResponse.class),
                anyString(),
                eq(SERVER_NAME),
                eq(USER_ID),
                eq(ENTITY_TYPE),
                eq(UPDATED_AFTER_DATE))).thenReturn(response);

        List<String> GUIDs = assetLineage.publishEntities(SERVER_NAME,
                USER_ID,
                ENTITY_TYPE,
                Optional.of(UPDATED_AFTER_DATE));


        assertArrayEquals(GUIDs.toArray(new String[0]), response.getGUIDs().toArray(new String[0]));
    }

    private GUIDListResponse mockGUIDListResponse() {
        GUIDListResponse response = new GUIDListResponse();
        response.setGUIDs(Arrays.asList("xyz", "abd", "abc"));
        return response;
    }

}
