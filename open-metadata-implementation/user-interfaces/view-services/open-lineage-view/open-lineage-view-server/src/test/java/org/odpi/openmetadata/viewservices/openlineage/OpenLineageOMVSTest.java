/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.openlineage;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.odpi.openmetadata.adapters.connectors.restclients.RESTClientConnector;
import org.odpi.openmetadata.adapters.connectors.restclients.ffdc.exceptions.RESTServerException;
import org.odpi.openmetadata.commonservices.ffdc.exceptions.UserNotAuthorizedException;
import org.odpi.openmetadata.commonservices.ffdc.rest.FFDCRESTClientBase;
import org.odpi.openmetadata.commonservices.multitenant.OMAGServerPlatformInstanceMap;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.governanceservers.openlineage.client.OpenLineageClient;
import org.odpi.openmetadata.governanceservers.openlineage.ffdc.OpenLineageException;
import org.odpi.openmetadata.governanceservers.openlineage.model.*;
import org.odpi.openmetadata.governanceservers.openlineage.responses.LineageResponse;
import org.odpi.openmetadata.userinterface.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.viewservices.openlineage.admin.serviceinstances.OpenLineageViewServicesInstance;
import org.odpi.openmetadata.viewservices.openlineage.objects.graph.Node;
import org.odpi.openmetadata.viewservices.openlineage.services.OpenLineageViewRESTServices;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.eq;


public class OpenLineageOMVSTest {


    public static final String guid = "test-guid";
    private static String USER_ID = "userId";
    private static String METADATA_SERVER_NAME = "server2";
    private static final String METADATA_SERVER_URL = "http://localhost:8080";
    private static String LINEAGE_SERVER_NAME = "server1";
    private static final String LINEAGE_SERVER_URL = "http://localhost:8081";
    private static final String UI_SERVER_NAME = "UITestServer";
    private static String VERTEX1_GUID = "123-123";
    private static String VERTEX1_NODETYPE = "subProcess";
    private static String VERTEX1_NODEID = "1";
    private static String VERTEX1_DISPLAYNAME = "First Vertex";

    private static String VERTEX2_GUID = "456-456";
    private static String VERTEX2_NODETYPE = "subProcess";
    private static String VERTEX2_NODEID = "2";
    private static String VERTEX2_DISPLAYNAME = "Second Vertex";

    private static String VERTEX3_GUID = "789-789";
    private static String VERTEX3_NODETYPE = "subProcess";
    private static String VERTEX3_NODEID = "3";
    private static String VERTEX3_DISPLAYNAME = "Third Vertex";


    private final static LineageVerticesAndEdges lineageVerticesAndEdges = getPayload();
    private final static LineageResponse lineageResponse = new LineageResponse(lineageVerticesAndEdges);

    private OpenLineageClient openLineageClient;

    @InjectMocks
    private OpenLineageViewRESTServices openLineageViewService;

    private GraphName graphName = GraphName.MAIN;

    @Mock
    private RESTClientConnector connector;


    @BeforeEach
    void setup() throws NoSuchFieldException, IllegalAccessException, InvalidParameterException {
        MockitoAnnotations.initMocks(this);
        openLineageClient = initializeOpenLineageClient();

        Field graphName = OpenLineageViewRESTServices.class.getDeclaredField("graphName");
        if (graphName !=null) {
            graphName.setAccessible(true);
            graphName.set(openLineageViewService, this.graphName);
            graphName.setAccessible(false);
        }
        Field connectorField = FFDCRESTClientBase.class.getDeclaredField("clientConnector");
        if (connectorField != null) {
            connectorField.setAccessible(true);
            connectorField.set(openLineageClient, connector);
            connectorField.setAccessible(false);
        }
    }

    static LineageVerticesAndEdges getPayload()  {
        LineageVerticesAndEdges payload = new LineageVerticesAndEdges();
        Set<LineageEdge> edges = new HashSet<>();
        Set<LineageVertex> vertices =  new HashSet<>();

        LineageVertex lineageVertex1 = new LineageVertex(VERTEX1_NODEID, VERTEX1_NODETYPE);
        lineageVertex1.setDisplayName(VERTEX1_DISPLAYNAME);
        lineageVertex1.setGuid(VERTEX1_GUID);

        vertices.add(lineageVertex1);

        LineageVertex lineageVertex2 = new LineageVertex(VERTEX2_NODEID, VERTEX2_NODETYPE);
        lineageVertex2.setDisplayName(VERTEX2_DISPLAYNAME);
        lineageVertex2.setGuid(VERTEX2_GUID);

        vertices.add(lineageVertex2);

        LineageVertex lineageVertex3 = new LineageVertex(VERTEX3_NODEID, VERTEX3_NODETYPE);
        lineageVertex3.setDisplayName(VERTEX3_DISPLAYNAME);
        lineageVertex3.setGuid(VERTEX3_GUID);

        vertices.add(lineageVertex3);

        payload.setLineageEdges(edges);
        payload.setLineageVertices(vertices);
        return payload;
    }

    @Test
    @DisplayName("Ultimate Source")
    public void testUltimateSource() throws PropertyServerException, InvalidParameterException, RESTServerException {

        Mockito.when(connector.callGetRESTCall(eq("lineage"), eq( LineageResponse.class), Mockito.anyString(), eq(LINEAGE_SERVER_NAME),
                eq(USER_ID), eq(graphName.getValue()), eq(Scope.ULTIMATE_SOURCE.getValue()), eq(View.COLUMN_VIEW.getValue()), eq(guid), Mockito.anyString(), eq(true))).thenReturn(lineageResponse);

        Map<String, List> ultimateSource =openLineageViewService.getUltimateSource(UI_SERVER_NAME, USER_ID, View.COLUMN_VIEW, guid);
        checkResponse(ultimateSource);
    }

    @Test
    @DisplayName("End To End")
    public void testEndToEnd() throws PropertyServerException, InvalidParameterException, OpenLineageException, RESTServerException {

        Mockito.when(connector.callGetRESTCall(eq("lineage"), eq( LineageResponse.class), Mockito.anyString(), eq(LINEAGE_SERVER_NAME),
                eq(USER_ID), eq(graphName.getValue()), eq(Scope.END_TO_END.getValue()), eq(View.COLUMN_VIEW.getValue()), eq(guid), Mockito.anyString(), eq(true))).thenReturn(lineageResponse);

        Map<String, List> response = openLineageViewService.getEndToEndLineage(UI_SERVER_NAME, USER_ID, View.COLUMN_VIEW, guid);
        checkResponse(response);
    }


    @Test
    @DisplayName("Ultimate Destination")
    public void testUltimateDestination() throws PropertyServerException, InvalidParameterException, OpenLineageException, RESTServerException {

        Mockito.when(connector.callGetRESTCall(eq("lineage"), eq( LineageResponse.class), Mockito.anyString(), eq(LINEAGE_SERVER_NAME),
                eq(USER_ID), eq(graphName.getValue()), eq(Scope.ULTIMATE_DESTINATION.getValue()), eq(View.COLUMN_VIEW.getValue()), eq(guid), Mockito.anyString(), eq(true))).thenReturn(lineageResponse);
        Map<String, List> response = openLineageViewService.getUltimateDestination(UI_SERVER_NAME, USER_ID, View.COLUMN_VIEW, guid);
        checkResponse(response);
    }

    @Test
    @DisplayName("Source and Destination")
    public void testSourceAndDestination() throws PropertyServerException, InvalidParameterException, OpenLineageException, RESTServerException {

        Mockito.when(connector.callGetRESTCall(eq("lineage"), eq( LineageResponse.class), Mockito.anyString(), eq(LINEAGE_SERVER_NAME),
                eq(USER_ID), eq(graphName.getValue()), eq(Scope.SOURCE_AND_DESTINATION.getValue()), eq(View.COLUMN_VIEW.getValue()), eq(guid), Mockito.anyString(), eq(true))).thenReturn(lineageResponse);
        Map<String, List> response = openLineageViewService.getSourceAndDestination(UI_SERVER_NAME, USER_ID, View.COLUMN_VIEW, guid);
        checkResponse(response);
    }

    @Test
    @DisplayName("GlossaryLineage")
    public void testGlossaryLineage() throws PropertyServerException, InvalidParameterException, OpenLineageException, RESTServerException {

        Mockito.when(connector.callGetRESTCall(eq("lineage"), eq( LineageResponse.class), Mockito.anyString(), eq(LINEAGE_SERVER_NAME),
                eq(USER_ID), eq(graphName.getValue()), eq(Scope.GLOSSARY.getValue()), eq(View.COLUMN_VIEW.getValue()), eq(guid), Mockito.anyString(), eq(true))).thenReturn(lineageResponse);

        Map<String, List> response = openLineageViewService.getGlossaryLineage(UI_SERVER_NAME, USER_ID, View.COLUMN_VIEW, guid);
        checkResponse(response);
    }

    private void checkResponse(Map<String, List> ultimateSource) {
        assertNotNull("Response is null", ultimateSource);
        assertEquals("Response should contain only nodes and edges",2, ultimateSource.size());
        assertTrue("Response should contain nodes", ultimateSource.containsKey("nodes"));
        assertTrue("Response should contain edges", ultimateSource.containsKey("edges"));
        List nodes = ultimateSource.get("nodes");
        assertNotNull("List of nodes is null", nodes);
        List<String> nodesIds = (List)nodes.stream().map(e -> ((Node) e).getId()).collect(Collectors.toList());
        assertEquals("Response should contain 3 nodes", 3, nodes.size());
        assertTrue("Response doesn't contain all nodes", nodesIds.containsAll(Arrays.asList(VERTEX1_NODEID, VERTEX2_NODEID, VERTEX3_NODEID)));
    }

    private OpenLineageClient initializeOpenLineageClient() throws InvalidParameterException {
        OMAGServerPlatformInstanceMap map = new OMAGServerPlatformInstanceMap();

        try {
            // shutdown the server if it is active.
            if (map.isServerActive(USER_ID,UI_SERVER_NAME)) {
                map.shutdownServerInstance(USER_ID, UI_SERVER_NAME, "initializeOpenLineageClient");
            }
        } catch (org.odpi.openmetadata.commonservices.ffdc.exceptions.PropertyServerException e) {
            // ignore the error. This call is just to ensure we have a clean static map before we start the test.
        } catch (UserNotAuthorizedException e) {
            // ignore the error. This call is just to ensure we have a clean static map before we start the test.
        }

        map.startUpServerInstance(USER_ID, UI_SERVER_NAME,null,null);
        OpenLineageViewServicesInstance instance =new OpenLineageViewServicesInstance(UI_SERVER_NAME,null, USER_ID,-1, METADATA_SERVER_NAME, METADATA_SERVER_URL,LINEAGE_SERVER_NAME, LINEAGE_SERVER_URL);
        map.addServiceInstanceToPlatform(LINEAGE_SERVER_NAME, ViewServiceDescription.ASSET_SEARCH.getViewServiceName(),instance);
        return instance.getOpenLineageClient();
    }
}
