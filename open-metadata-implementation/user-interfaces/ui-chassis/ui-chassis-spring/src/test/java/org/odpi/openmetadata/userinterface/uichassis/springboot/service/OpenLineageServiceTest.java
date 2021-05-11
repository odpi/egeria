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
import org.odpi.openmetadata.governanceservers.openlineage.client.OpenLineageClient;
import org.odpi.openmetadata.governanceservers.openlineage.ffdc.OpenLineageException;
import org.odpi.openmetadata.governanceservers.openlineage.model.LineageEdge;
import org.odpi.openmetadata.governanceservers.openlineage.model.LineageVertex;
import org.odpi.openmetadata.governanceservers.openlineage.model.LineageVerticesAndEdges;
import org.odpi.openmetadata.governanceservers.openlineage.model.Scope;
import org.odpi.openmetadata.userinterface.uichassis.springboot.beans.Edge;
import org.odpi.openmetadata.userinterface.uichassis.springboot.beans.Graph;
import org.odpi.openmetadata.userinterface.uichassis.springboot.beans.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doCallRealMethod;
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
    private OpenLineageClient openLineageClient;

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
    public void testUltimateSource() throws PropertyServerException, InvalidParameterException {
        try {
            when(openLineageClient.lineage(eq(USER_ID), eq(Scope.ULTIMATE_SOURCE), eq(guid), eq(""), eq(true)))
                    .thenReturn(lineageVerticesAndEdges);
        } catch (OpenLineageException e) {
            e.printStackTrace();
        }
        Graph ultimateSource = openLineageService.getUltimateSource(USER_ID, guid, true);
        checkResponse(ultimateSource);
    }

    @Test
    @DisplayName("End To End")
    public void testEndToEnd() throws PropertyServerException, InvalidParameterException {
        try {
            when(openLineageClient.lineage(eq(USER_ID), eq(Scope.END_TO_END), eq(guid), eq(""), eq(true)))
                    .thenReturn(lineageVerticesAndEdges);
        } catch (OpenLineageException e) {
            e.printStackTrace();
        }
        Graph response = openLineageService.getEndToEndLineage(USER_ID, guid, true);
        checkResponse(response);
    }


    @Test
    @DisplayName("Ultimate Destination")
    public void testUltimateDestination() throws PropertyServerException, InvalidParameterException {
        try {
            when(openLineageClient.lineage(eq(USER_ID), eq(Scope.ULTIMATE_DESTINATION), eq(guid), eq(""), eq(true)))
                    .thenReturn(lineageVerticesAndEdges);
        } catch (OpenLineageException e) {
            e.printStackTrace();
        }
        Graph response = openLineageService.getUltimateDestination(USER_ID, guid, true);
        checkResponse(response);
    }

    @Test
    @DisplayName("Source and Destination")
    public void testSourceAndDestination() throws PropertyServerException, InvalidParameterException {
        try {
            when(openLineageClient.lineage(eq(USER_ID), eq(Scope.SOURCE_AND_DESTINATION), eq(guid), eq(""), eq(true)))
                    .thenReturn(lineageVerticesAndEdges);
        } catch (OpenLineageException e) {
            e.printStackTrace();
        }
        Graph response = openLineageService.getSourceAndDestination(USER_ID, guid, true);
        checkResponse(response);
    }

    @Test
    @DisplayName("GlossaryLineage")
    public void testGlossaryLineage() throws PropertyServerException, InvalidParameterException {
        try {
            when(openLineageClient.lineage(eq(USER_ID), eq(Scope.VERTICAL), eq(guid), eq(""), eq(true)))
                    .thenReturn(lineageVerticesAndEdges);
        } catch (OpenLineageException e) {
            e.printStackTrace();
        }
        Graph response = openLineageService.getVerticalLineage(USER_ID, guid, true);
        checkResponse(response);
    }


    @Test
    @DisplayName("TestNodesLevels")
    @SuppressWarnings("unchecked")
    public void testNodesLevels() throws PropertyServerException, InvalidParameterException, IOException {
        setupLineageVerticesAndEdges();
        try {
            when(openLineageClient.lineage(eq(USER_ID), eq(Scope.END_TO_END), eq("n11"), eq(""), eq(true)))
                    .thenReturn(lineageVerticesAndEdges);
            doCallRealMethod().when(lineageGraphDisplayService).setNodesLevel(anyList(), anyList(),anyList());
        } catch (OpenLineageException e) {
            e.printStackTrace();
        }

        Graph response = openLineageService.getEndToEndLineage(USER_ID, "n11", true);

        List<Node> nodes = response.getNodes();
        List<Edge> edges = response.getEdges();

        assertEquals("Response should contain 21 nodes", 21, nodes.size());
        assertEquals("Response should contain 20 edges", 20, edges.size());

        Map<String,Node> nodesMap = nodes.stream()
                .collect(Collectors.toMap(Node::getId, Function.identity()));
        assertEquals("Level of n11 should be 0", Integer.valueOf(0), nodesMap.get("n11").getLevel());
        assertEquals("Level of n7 should be -1", Integer.valueOf(-1), nodesMap.get("n7").getLevel());
        assertEquals("Level of n10 should be -1", Integer.valueOf(-1), nodesMap.get("n10").getLevel());
        assertEquals("Level of n1 should be -4", Integer.valueOf(-4), nodesMap.get("n1").getLevel());
        assertEquals("Level of n3 should be -2", Integer.valueOf(-4), nodesMap.get("n1").getLevel());
        assertEquals("Level of n19 should be1", Integer.valueOf(1), nodesMap.get("n19").getLevel());
        assertEquals("Level of n20 should be2", Integer.valueOf(2), nodesMap.get("n20").getLevel());
        assertEquals("Level of n17 should be 3", Integer.valueOf(3), nodesMap.get("n17").getLevel());
        assertEquals("Level of n15 should be 4", Integer.valueOf(4), nodesMap.get("n15").getLevel());

        readResource();
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

        Set<LineageEdge> edges = lineageVerticesAndEdges.getLineageEdges();
        edges.add( new LineageEdge("edge","n1","n2"));
        edges.add( new LineageEdge("edge","n2","n3"));
        edges.add( new LineageEdge("edge","n3","n7"));

        edges.add( new LineageEdge("edge","n4","n5"));
        edges.add( new LineageEdge("edge","n5","n6"));
        edges.add( new LineageEdge("edge","n6","n7"));

        edges.add( new LineageEdge("edge","n8","n9"));
        edges.add( new LineageEdge("edge","n9","n10"));

        edges.add( new LineageEdge("edge","n10","n11"));
        edges.add( new LineageEdge("edge","n7","n11"));

        edges.add( new LineageEdge("edge","n11","n12"));
        edges.add( new LineageEdge("edge","n11","n19"));

        edges.add( new LineageEdge("edge","n12","n13"));
        edges.add( new LineageEdge("edge","n13","n14"));
        edges.add( new LineageEdge("edge","n14","n15"));

        edges.add( new LineageEdge("edge","n12","n16"));
        edges.add( new LineageEdge("edge","n16","n17"));
        edges.add( new LineageEdge("edge","n17","n18"));

        edges.add( new LineageEdge("edge","n19","n20"));
        edges.add( new LineageEdge("edge","n20","n21"));

    }

}
