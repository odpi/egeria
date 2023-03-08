/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.client;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.odpi.openmetadata.accessservices.dataengine.model.DataFile;
import org.odpi.openmetadata.accessservices.dataengine.model.Database;
import org.odpi.openmetadata.accessservices.dataengine.model.DatabaseSchema;
import org.odpi.openmetadata.accessservices.dataengine.model.RelationalTable;
import org.odpi.openmetadata.adapters.connectors.restclients.RESTClientConnector;
import org.odpi.openmetadata.adapters.connectors.restclients.ffdc.exceptions.RESTServerException;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.PropertiesResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class DataEngineRESTClientTest {

    private static final String SERVER_URL = "https://localhost:9444";
    private static final String SERVER_NAME = "TestServer";
    private static final String USER_ID = "zebra91";
    private static final String GUID = "guid";
    private static final String EXTERNAL_SOURCE_NAME = "externalSourceName";
    private static final String QUALIFIED_NAME = "qualifiedName";

    @Mock
    private RESTClientConnector connector;

    private DataEngineRESTClient dataEngineRESTClient;

    @Before
    public void before() throws InvalidParameterException {
        MockitoAnnotations.openMocks(this);

        dataEngineRESTClient = new DataEngineRESTClient(SERVER_NAME, SERVER_URL);

        Field connectorField = ReflectionUtils.findField(DataEngineRESTClient.class, "clientConnector");
        if (connectorField != null) {
            connectorField.setAccessible(true);
            ReflectionUtils.setField(connectorField, dataEngineRESTClient, connector);
            connectorField.setAccessible(false);
        }

        dataEngineRESTClient.setExternalSourceName(EXTERNAL_SOURCE_NAME);
    }

    @Test
    public void upsertDatabase() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException, RESTServerException {
        GUIDResponse response = mockGUIDResponse();
        Database database = new Database();

        when(connector.callPostRESTCall(eq("upsertDatabase"), eq(GUIDResponse.class), anyString(), any(), any()))
                .thenReturn(response);
        dataEngineRESTClient.upsertDatabase(USER_ID, database);
        assertEquals(GUID, response.getGUID());
    }

    @Test
    public void upsertDatabaseSchema() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException, RESTServerException {
        GUIDResponse response = mockGUIDResponse();
        DatabaseSchema databaseSchema = new DatabaseSchema();

        when(connector.callPostRESTCall(eq("upsertDatabaseSchema"), eq(GUIDResponse.class), anyString(), any(), any()))
                .thenReturn(response);
        dataEngineRESTClient.upsertDatabaseSchema(USER_ID, databaseSchema, null);
        assertEquals(GUID, response.getGUID());
    }

    @Test
    public void upsertRelationalTable() throws RESTServerException, InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        GUIDResponse response = mockGUIDResponse();
        RelationalTable relationalTable = new RelationalTable();

        when(connector.callPostRESTCall(eq("upsertRelationalTable"), eq(GUIDResponse.class), anyString(), any(), any()))
                .thenReturn(response);
        dataEngineRESTClient.upsertRelationalTable(USER_ID, relationalTable, "databaseQualifiedName");
        assertEquals(GUID, response.getGUID());
    }

    @Test
    public void upsertDataFile() throws RESTServerException, InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        GUIDResponse response = mockGUIDResponse();
        DataFile dataFile = new DataFile();

        when(connector.callPostRESTCall(eq("upsertDataFile"), eq(GUIDResponse.class), anyString(), any(), any()))
                .thenReturn(response);
        dataEngineRESTClient.upsertDataFile(USER_ID, dataFile);
        assertEquals(GUID, response.getGUID());
    }

    @Test
    public void deleteDatabase() throws RESTServerException, InvalidParameterException, PropertyServerException {
        VoidResponse response = mockVoidResponse();

        when(connector.callDeleteRESTCall(eq("deleteDatabase"), eq(VoidResponse.class), anyString(), any(), any()))
                .thenReturn(response);
        dataEngineRESTClient.deleteDatabase(USER_ID, QUALIFIED_NAME, null);

        verify(connector, times(1)).callDeleteRESTCall(eq("deleteDatabase"), eq(VoidResponse.class), anyString(), any(), any());
    }

    @Test
    public void deleteDatabaseSchema() throws RESTServerException, InvalidParameterException, PropertyServerException {
        VoidResponse response = mockVoidResponse();

        when(connector.callDeleteRESTCall(eq("deleteDatabaseSchema"), eq(VoidResponse.class), anyString(), any(), any()))
                .thenReturn(response);
        dataEngineRESTClient.deleteDatabaseSchema(USER_ID, QUALIFIED_NAME, null);

        verify(connector, times(1)).callDeleteRESTCall(eq("deleteDatabaseSchema"),
                eq(VoidResponse.class), anyString(), any(), any());
    }

    @Test
    public void deleteRelationalTable() throws RESTServerException, InvalidParameterException, PropertyServerException {
        VoidResponse response = mockVoidResponse();

        when(connector.callDeleteRESTCall(eq("deleteRelationalTable"), eq(VoidResponse.class), anyString(), any(), any()))
                .thenReturn(response);
        dataEngineRESTClient.deleteRelationalTable(USER_ID, QUALIFIED_NAME, null);

        verify(connector, times(1)).callDeleteRESTCall(eq("deleteRelationalTable"), eq(VoidResponse.class), anyString(), any(), any());
    }

    @Test
    public void deleteDataFile() throws RESTServerException, InvalidParameterException, PropertyServerException {
        VoidResponse response = mockVoidResponse();

        when(connector.callDeleteRESTCall(eq("deleteDataFile"), eq(VoidResponse.class), anyString(), any(), any()))
                .thenReturn(response);
        dataEngineRESTClient.deleteDataFile(USER_ID, QUALIFIED_NAME, null);

        verify(connector, times(1)).callDeleteRESTCall(eq("deleteDataFile"), eq(VoidResponse.class), anyString(), any(), any());
    }

    @Test
    public void deleteFolder() throws RESTServerException, InvalidParameterException, PropertyServerException {
        VoidResponse response = mockVoidResponse();

        when(connector.callDeleteRESTCall(eq("deleteFolder"), eq(VoidResponse.class), anyString(), any(), any()))
                .thenReturn(response);
        dataEngineRESTClient.deleteFolder(USER_ID, QUALIFIED_NAME, null);

        verify(connector, times(1)).callDeleteRESTCall(eq("deleteFolder"), eq(VoidResponse.class), anyString(), any(), any());
    }

    @Test
    public void deleteConnection() throws RESTServerException, InvalidParameterException, PropertyServerException {
        VoidResponse response = mockVoidResponse();

        when(connector.callDeleteRESTCall(eq("deleteConnection"), eq(VoidResponse.class), anyString(), any(), any()))
                .thenReturn(response);
        dataEngineRESTClient.deleteConnection(USER_ID, QUALIFIED_NAME, null);

        verify(connector, times(1)).callDeleteRESTCall(eq("deleteConnection"), eq(VoidResponse.class), anyString(), any(), any());
    }

    @Test
    public void deleteEndpoint() throws RESTServerException, InvalidParameterException, PropertyServerException {
        VoidResponse response = mockVoidResponse();

        when(connector.callDeleteRESTCall(eq("deleteEndpoint"), eq(VoidResponse.class), anyString(), any(), any()))
                .thenReturn(response);
        dataEngineRESTClient.deleteEndpoint(USER_ID, QUALIFIED_NAME, null);

        verify(connector, times(1)).callDeleteRESTCall(eq("deleteEndpoint"), eq(VoidResponse.class), anyString(), any(), any());
    }

    @Test
    public void upsertProcessingState() throws RESTServerException, InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        VoidResponse response = mockVoidResponse();

        when(connector.callPostRESTCall(eq("upsertProcessingState"), eq(VoidResponse.class), anyString(), any(), any()))
                .thenReturn(response);
        dataEngineRESTClient.upsertProcessingState(USER_ID, new HashMap<>());

        verify(connector, times(1)).callPostRESTCall(eq("upsertProcessingState"), eq(VoidResponse.class), anyString(), any(), any());
    }


    @Test
    public void getProcessingState() throws RESTServerException, PropertyServerException {
        Map<String, Object> properties = new HashMap<>();
        properties.put("key", 100L);
        PropertiesResponse response = new PropertiesResponse();
        response.setProperties(properties);

        when(connector.callGetRESTCall(eq("getProcessingState"), eq(PropertiesResponse.class), anyString(), any(), any(),
                any())).thenReturn(response);
        dataEngineRESTClient.getProcessingState(USER_ID);

        verify(connector, times(1)).callGetRESTCall(eq("getProcessingState"),
                eq(PropertiesResponse.class), anyString(), any(), any(), any());
    }

    private GUIDResponse mockGUIDResponse() {
        GUIDResponse response = new GUIDResponse();
        response.setGUID(GUID);
        return response;
    }

    private VoidResponse mockVoidResponse() {
        return new VoidResponse();
    }
}
