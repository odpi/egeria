/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.uichassis.springboot.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.governanceservers.lineagewarehouse.client.LineageWarehouseClientWarehouse;
import org.odpi.openmetadata.governanceservers.lineagewarehouse.ffdc.LineageWarehouseException;
import org.odpi.openmetadata.governanceservers.lineagewarehouse.model.LineageEdge;
import org.odpi.openmetadata.governanceservers.lineagewarehouse.model.LineageVertex;
import org.odpi.openmetadata.governanceservers.lineagewarehouse.model.LineageVerticesAndEdges;
import org.odpi.openmetadata.governanceservers.lineagewarehouse.model.Scope;
import org.odpi.openmetadata.governanceservers.lineagewarehouse.requests.LineageSearchRequest;
import org.odpi.openmetadata.userinterface.uichassis.springboot.beans.Graph;
import org.odpi.openmetadata.userinterface.uichassis.springboot.beans.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;


public class OpenLineageServiceTest {

    private static final Logger LOG = LoggerFactory.getLogger(OpenLineageServiceTest.class);
    public static final String RESPONSE_JSON = "./src/test/resources/openLineageServicesResponse.json";
    public static final String guid = "test-guid";
    private static String USER_ID = "userId";
    private static ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static LineageVerticesAndEdges lineageVerticesAndEdges;

    @Mock
    private LineageGraphDisplayService lineageGraphDisplayService;

    @Mock
    private LineageWarehouseClientWarehouse lineageWarehouseClient;

    @InjectMocks
    private OpenLineageService openLineageService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @BeforeEach
    public void readResource() throws IOException {
        String payload = FileUtils.readFileToString(new File(RESPONSE_JSON), "UTF-8");
        lineageVerticesAndEdges = OBJECT_MAPPER.readValue(payload, LineageVerticesAndEdges.class);
    }

    @Test
    @DisplayName("Ultimate Source")
    public void testUltimateSource() throws PropertyServerException, InvalidParameterException, LineageWarehouseException
    {
        when(lineageWarehouseClient.lineage(USER_ID, Scope.ULTIMATE_SOURCE, guid, true))
                .thenReturn(lineageVerticesAndEdges);
        Graph ultimateSource = openLineageService.getUltimateSource(USER_ID, guid, true);
        checkResponse(ultimateSource);
    }

    @Test
    @DisplayName("End To End")
    public void testEndToEnd() throws PropertyServerException, InvalidParameterException, LineageWarehouseException
    {
        when(lineageWarehouseClient.lineage(USER_ID, Scope.END_TO_END, guid, true))
                .thenReturn(lineageVerticesAndEdges);
        Graph response = openLineageService.getEndToEndLineage(USER_ID, guid, true);
        checkResponse(response);
    }


    @Test
    @DisplayName("Ultimate Destination")
    public void testUltimateDestination() throws PropertyServerException, InvalidParameterException, LineageWarehouseException
    {
        when(lineageWarehouseClient.lineage(USER_ID, Scope.ULTIMATE_DESTINATION, guid, true))
                .thenReturn(lineageVerticesAndEdges);
        Graph response = openLineageService.getUltimateDestination(USER_ID, guid, true);
        checkResponse(response);
    }

    @Test
    @DisplayName("GlossaryLineage")
    public void testGlossaryLineage() throws PropertyServerException, InvalidParameterException, LineageWarehouseException
    {
        when(lineageWarehouseClient.lineage(USER_ID, Scope.VERTICAL, guid, true))
                .thenReturn(lineageVerticesAndEdges);
        Graph response = openLineageService.getVerticalLineage(USER_ID, guid, true);
        checkResponse(response);
    }

    @Test
    @DisplayName("Search")
    public void search() throws PropertyServerException, InvalidParameterException, LineageWarehouseException
    {
        LineageSearchRequest searchRequest = new LineageSearchRequest();
        List<LineageVertex> lineageVertices = new ArrayList<>(lineageVerticesAndEdges.getLineageVertices());
        when(lineageWarehouseClient.search(USER_ID, searchRequest))
                .thenReturn(lineageVertices);
        List<LineageVertex> response = openLineageService.search(USER_ID, searchRequest);
        checkSearchResponse(response);
    }

    private void checkSearchResponse(List<LineageVertex> nodes) {
        assertNotNull("List of nodes is null", nodes);
        assertEquals("Response should contain 4 nodes", 4, nodes.size());
        List<String> nodesIds = nodes.stream().map(LineageVertex::getNodeID).collect(Collectors.toList());
        assertTrue("Response doesn't contain all nodes", nodesIds.containsAll(Arrays.asList("p0","p30", "p2")));
    }

    @SuppressWarnings("unchecked")
    private void checkResponse(Graph responseGraph) {
        assertNotNull("Response is null", responseGraph);
        assertNotNull("Response should contain nodes", responseGraph.getNodes());
        assertNotNull("Response should contain edges", responseGraph.getEdges());
        List<Node> nodes = responseGraph.getNodes();
        assertNotNull("List of nodes is null", nodes);
        List<String> nodesIds = nodes.stream().map(Node::getId).collect(Collectors.toList());
        assertEquals("Response should contain 4 nodes", 4, nodes.size());
        assertTrue("Response doesn't contain all nodes", nodesIds.containsAll(Arrays.asList("p0","p30", "p2")));

    }

    /**
     * Generates the graph:
     *
     *  1-->2-->3 --\                          /  13-->14-->15
     *                 7 --               12 --
     *  4-->5-->6 --/       \            /     \  16-->17-->18
     *                        -->11 -->
     *        8-->9-->10 -->/            \
     *                                     19-->20-->21
     */
    private void setupLineageVerticesAndEdges(){
        lineageVerticesAndEdges.getLineageVertices().clear();
        lineageVerticesAndEdges.getLineageEdges().clear();;

        for(int i = 1; i < 22; i++) {
            lineageVerticesAndEdges.getLineageVertices()
                    .add(new LineageVertex("n"+i , "node"));
        }

        AtomicInteger counter = new AtomicInteger(0);
        Set<LineageEdge> edges = lineageVerticesAndEdges.getLineageEdges();
        edges.add( new LineageEdge(counter.incrementAndGet(), "edge","n1","n2"));
        edges.add( new LineageEdge(counter.incrementAndGet(), "edge","n2","n3"));
        edges.add( new LineageEdge(counter.incrementAndGet(), "edge","n3","n7"));

        edges.add( new LineageEdge(counter.incrementAndGet(), "edge","n4","n5"));
        edges.add( new LineageEdge(counter.incrementAndGet(), "edge","n5","n6"));
        edges.add( new LineageEdge(counter.incrementAndGet(), "edge","n6","n7"));

        edges.add( new LineageEdge(counter.incrementAndGet(), "edge","n8","n9"));
        edges.add( new LineageEdge(counter.incrementAndGet(), "edge","n9","n10"));

        edges.add( new LineageEdge(counter.incrementAndGet(), "edge","n10","n11"));
        edges.add( new LineageEdge(counter.incrementAndGet(), "edge","n7","n11"));

        edges.add( new LineageEdge(counter.incrementAndGet(), "edge","n11","n12"));
        edges.add( new LineageEdge(counter.incrementAndGet(), "edge","n11","n19"));

        edges.add( new LineageEdge(counter.incrementAndGet(), "edge","n12","n13"));
        edges.add( new LineageEdge(counter.incrementAndGet(), "edge","n13","n14"));
        edges.add( new LineageEdge(counter.incrementAndGet(), "edge","n14","n15"));

        edges.add( new LineageEdge(counter.incrementAndGet(), "edge","n12","n16"));
        edges.add( new LineageEdge(counter.incrementAndGet(), "edge","n16","n17"));
        edges.add( new LineageEdge(counter.incrementAndGet(), "edge","n17","n18"));

        edges.add( new LineageEdge(counter.incrementAndGet(), "edge","n19","n20"));
        edges.add( new LineageEdge(counter.incrementAndGet(), "edge","n20","n21"));

    }

}
