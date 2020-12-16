/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.lineagegraph;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.janusgraph.core.JanusGraph;
import org.janusgraph.core.JanusGraphFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.odpi.openmetadata.governanceservers.openlineage.model.LineageVertex;
import org.odpi.openmetadata.governanceservers.openlineage.model.LineageVerticesAndEdges;
import org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.graph.LineageGraphConnectorHelper;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.DATA_FILE;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.GLOSSARY_TERM;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.PROCESS;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.RELATIONAL_TABLE;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.TABULAR_COLUMN;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.EDGE_LABEL_COLUMN_DATA_FLOW;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.EDGE_LABEL_RELATED_TERM;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.EDGE_LABEL_SEMANTIC_ASSIGNMENT;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.EDGE_LABEL_TABLE_DATA_FLOW;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.NODE_LABEL_SUB_PROCESS;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.PROPERTY_KEY_ENTITY_GUID;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.PROPERTY_KEY_ENTITY_NODE_ID;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.PROPERTY_KEY_PROCESS_GUID;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.PROPERTY_VALUE_NODE_ID_CONDENSED_DESTINATION;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.PROPERTY_VALUE_NODE_ID_CONDENSED_SOURCE;

public class LineageGraphConnectorHelperTest {

    static LineageGraphConnectorHelper mainGraphConnector;

    @BeforeAll
    public static void beforeClass() {
        Graph graph = JanusGraphFactory.build().set("storage.backend", "inmemory").open();
        GraphTraversalSource g = graph.traversal();
        mainGraphConnector = new LineageGraphConnectorHelper(g, true);

        addColumnLineageData(g);

        addGlossaryLineageData(g);

        addTableLineageData(g);
    }


    @Test
    public void ultimateSourceColumnLevel() {
        HashSet<String> expectedNodeIDs = new HashSet<>();
        final String queriedNodeID = "c32";
        expectedNodeIDs.add("c11");
        expectedNodeIDs.add("c12");
        expectedNodeIDs.add(queriedNodeID);
        expectedNodeIDs.add(PROPERTY_VALUE_NODE_ID_CONDENSED_SOURCE);

        LineageVerticesAndEdges lineageVerticesAndEdges = mainGraphConnector.ultimateSource(queriedNodeID, true).get();
        Set<LineageVertex> lineageVertices = lineageVerticesAndEdges.getLineageVertices();
        validateResponse(expectedNodeIDs, lineageVertices);
    }

    @Test
    public void ultimateDestinationColumnLevel() {
        HashSet<String> expectedNodeIDs = new HashSet<>();
        final String queriedNodeID = "c11";
        expectedNodeIDs.add("c41");
        expectedNodeIDs.add("c42");
        expectedNodeIDs.add(queriedNodeID);
        expectedNodeIDs.add(PROPERTY_VALUE_NODE_ID_CONDENSED_DESTINATION);

        LineageVerticesAndEdges lineageVerticesAndEdges = mainGraphConnector.ultimateDestination(queriedNodeID, true).get();
        Set<LineageVertex> lineageVertices = lineageVerticesAndEdges.getLineageVertices();

        validateResponse(expectedNodeIDs, lineageVertices);
    }

    @Test
    public void sourceAndDestinationColumnLevel() {
        HashSet<String> expectedNodeIDs = new HashSet<>();
        final String queriedNodeID = "c21";
        expectedNodeIDs.add("c11");
        expectedNodeIDs.add("c12");
        expectedNodeIDs.add("c41");
        expectedNodeIDs.add("c42");
        expectedNodeIDs.add(queriedNodeID);
        expectedNodeIDs.add(PROPERTY_VALUE_NODE_ID_CONDENSED_SOURCE);
        expectedNodeIDs.add(PROPERTY_VALUE_NODE_ID_CONDENSED_DESTINATION);
        System.out.println(mainGraphConnector);
        LineageVerticesAndEdges lineageVerticesAndEdges = mainGraphConnector.sourceAndDestination(queriedNodeID, true).get();
        Set<LineageVertex> lineageVertices = lineageVerticesAndEdges.getLineageVertices();

        validateResponse(expectedNodeIDs, lineageVertices);
    }

    @Test
    public void endToEndColumnLevel() {
        HashSet<String> expectedNodeIDs = new HashSet<>();
        final String queriedNodeID = "c22";
        expectedNodeIDs.add("c11");
        expectedNodeIDs.add("c12");
        expectedNodeIDs.add("c21");
        expectedNodeIDs.add("c22");
        expectedNodeIDs.add("c31");
        expectedNodeIDs.add("c32");
        expectedNodeIDs.add("c41");
        expectedNodeIDs.add("c42");
        expectedNodeIDs.add("p1");
        expectedNodeIDs.add("p2");
        expectedNodeIDs.add("p3");
        expectedNodeIDs.add("p4");

        LineageVerticesAndEdges lineageVerticesAndEdges = mainGraphConnector.endToEnd(queriedNodeID, true).get();
        Set<LineageVertex> lineageVertices = lineageVerticesAndEdges.getLineageVertices();

        validateResponse(expectedNodeIDs, lineageVertices);
    }

    @Test
    public void verticalLineage() {
        JanusGraph cyclicGlossaryGraph = JanusGraphFactory.build().set("storage.backend", "inmemory").open();
        GraphTraversalSource g = cyclicGlossaryGraph.traversal();
        HashSet<String> expectedNodeIDs = new HashSet<>();
        final String queriedNodeID = "g2";
        expectedNodeIDs.add("g1");
        expectedNodeIDs.add("g2");
        expectedNodeIDs.add("g3");
        expectedNodeIDs.add("c2");

        LineageVerticesAndEdges lineageVerticesAndEdges = mainGraphConnector.verticalLineage(queriedNodeID).get();

        Set<LineageVertex> lineageVertices = lineageVerticesAndEdges.getLineageVertices();

        lineageVerticesAndEdges.getLineageVertices().forEach(s -> System.out.println(s.getNodeID()));

        validateResponse(expectedNodeIDs, lineageVertices);
    }

    @Test
    public void problematicCyclicGraphSourceDestination() {
        //A triangle of three nodes
        JanusGraph problematicCyclicGraph = JanusGraphFactory.build().set("storage.backend", "inmemory").open();
        GraphTraversalSource g = problematicCyclicGraph.traversal();
        Vertex c1 = g.addV(TABULAR_COLUMN).property(PROPERTY_KEY_ENTITY_NODE_ID, "c1").next();
        Vertex c2 = g.addV(TABULAR_COLUMN).property(PROPERTY_KEY_ENTITY_NODE_ID, "c2").next();
        Vertex c3 = g.addV(TABULAR_COLUMN).property(PROPERTY_KEY_ENTITY_NODE_ID, "c3").next();

        g.addE(EDGE_LABEL_COLUMN_DATA_FLOW).from(c1).to(c2).next();
        g.addE(EDGE_LABEL_COLUMN_DATA_FLOW).from(c2).to(c3).next();
        g.addE(EDGE_LABEL_COLUMN_DATA_FLOW).from(c3).to(c1).next();
        final String queriedNodeID = "c32";
        mainGraphConnector.ultimateSource(queriedNodeID, true);
        mainGraphConnector.ultimateDestination(queriedNodeID, true);
        mainGraphConnector.sourceAndDestination(queriedNodeID, true);

    }

    @Test
    public void ultimateSourceTableLevel() {
        HashSet<String> expectedNodeIDs = new HashSet<>();
        final String queriedNodeID = "t1";
        expectedNodeIDs.add("d1");
        expectedNodeIDs.add("p1");
        expectedNodeIDs.add(queriedNodeID);

        LineageVerticesAndEdges lineageVerticesAndEdges = mainGraphConnector.ultimateSource(queriedNodeID, true).get();
        Set<LineageVertex> lineageVertices = lineageVerticesAndEdges.getLineageVertices();
        validateResponse(expectedNodeIDs, lineageVertices);
    }

    @Test
    public void ultimateDestinationTableLevel() {
        HashSet<String> expectedNodeIDs = new HashSet<>();
        final String queriedNodeID = "t1";
        expectedNodeIDs.add("p2");
        expectedNodeIDs.add("t2");
        expectedNodeIDs.add(queriedNodeID);

        LineageVerticesAndEdges lineageVerticesAndEdges = mainGraphConnector.ultimateDestination(queriedNodeID, true).get();
        Set<LineageVertex> lineageVertices = lineageVerticesAndEdges.getLineageVertices();

        validateResponse(expectedNodeIDs, lineageVertices);
    }

    @Test
    public void sourceAndDestinationTableLevel() {
        HashSet<String> expectedNodeIDs = new HashSet<>();
        final String queriedNodeID = "t1";
        expectedNodeIDs.add("d1");
        expectedNodeIDs.add("p1");
        expectedNodeIDs.add("p2");
        expectedNodeIDs.add("t2");
        expectedNodeIDs.add(queriedNodeID);

        System.out.println(mainGraphConnector);
        LineageVerticesAndEdges lineageVerticesAndEdges = mainGraphConnector.sourceAndDestination(queriedNodeID, true).get();
        Set<LineageVertex> lineageVertices = lineageVerticesAndEdges.getLineageVertices();

        validateResponse(expectedNodeIDs, lineageVertices);
    }

    @Test
    public void endToEndTableLevel() {
        HashSet<String> expectedNodeIDs = new HashSet<>();
        final String queriedNodeID = "t1";
        expectedNodeIDs.add("d1");
        expectedNodeIDs.add("p1");
        expectedNodeIDs.add("p2");
        expectedNodeIDs.add("t2");
        expectedNodeIDs.add(queriedNodeID);

        LineageVerticesAndEdges lineageVerticesAndEdges = mainGraphConnector.endToEnd(queriedNodeID, true).get();
        Set<LineageVertex> lineageVertices = lineageVerticesAndEdges.getLineageVertices();

        validateResponse(expectedNodeIDs, lineageVertices);
    }

    private void validateResponse(HashSet<String> expectedNodeIDs, Set<LineageVertex> lineageVertices) {
        assertEquals(expectedNodeIDs.size(), lineageVertices.size());
        for (LineageVertex returnedVertex : lineageVertices) {
            assertTrue(expectedNodeIDs.contains(returnedVertex.getNodeID()));
        }
    }

    private static void addGlossaryLineageData(GraphTraversalSource g) {
        Vertex g1 = getVertex(g, GLOSSARY_TERM, "g1", "g1");
        Vertex g2 = getVertex(g, GLOSSARY_TERM, "g2", "g2");
        Vertex g3 = getVertex(g, GLOSSARY_TERM, "g3", "g3");

        Vertex c1 = getVertex(g, TABULAR_COLUMN, "c1", "c1");
        Vertex c2 = getVertex(g, TABULAR_COLUMN, "c2", "c2");
        Vertex c3 = getVertex(g, TABULAR_COLUMN, "c3", "c3");

        g.addE(EDGE_LABEL_RELATED_TERM).from(g1).to(g2).next();
        g.addE(EDGE_LABEL_RELATED_TERM).from(g2).to(g3).next();
        g.addE(EDGE_LABEL_RELATED_TERM).from(g3).to(g1).next();

        g.addE(EDGE_LABEL_SEMANTIC_ASSIGNMENT).from(c1).to(g1).next();
        g.addE(EDGE_LABEL_SEMANTIC_ASSIGNMENT).from(c2).to(g2).next();
        g.addE(EDGE_LABEL_SEMANTIC_ASSIGNMENT).from(c3).to(g3).next();
    }

    private static void addColumnLineageData(GraphTraversalSource g) {
        Vertex c11 = getVertex(g, TABULAR_COLUMN, "c11", "c11");
        Vertex c12 = getVertex(g, TABULAR_COLUMN, "c12", "c12");
        Vertex c21 = getVertex(g, TABULAR_COLUMN, "c21", "c21");
        Vertex c22 = getVertex(g, TABULAR_COLUMN, "c22", "c22");
        Vertex c31 = getVertex(g, TABULAR_COLUMN, "c31", "c31");
        Vertex c32 = getVertex(g, TABULAR_COLUMN, "c32", "c32");
        Vertex c41 = getVertex(g, TABULAR_COLUMN, "c41", "c41");
        Vertex c42 = getVertex(g, TABULAR_COLUMN, "c42", "c42");

        Vertex p1 = getVertex(g, NODE_LABEL_SUB_PROCESS, "p1", "p1");
        Vertex p2 = getVertex(g, NODE_LABEL_SUB_PROCESS, "p2", "p2");
        Vertex p3 = getVertex(g, NODE_LABEL_SUB_PROCESS, "p3", "p3");
        Vertex p4 = getVertex(g, NODE_LABEL_SUB_PROCESS, "p4", "p4");

        g.addE(EDGE_LABEL_COLUMN_DATA_FLOW).from(c11).to(p1).next();
        g.addE(EDGE_LABEL_COLUMN_DATA_FLOW).from(c12).to(p1).next();

        g.addE(EDGE_LABEL_COLUMN_DATA_FLOW).from(p1).to(c21).next();
        g.addE(EDGE_LABEL_COLUMN_DATA_FLOW).from(p1).to(c21).next();

        g.addE(EDGE_LABEL_COLUMN_DATA_FLOW).from(c21).to(p2).next();
        g.addE(EDGE_LABEL_COLUMN_DATA_FLOW).from(c22).to(p2).next();

        g.addE(EDGE_LABEL_COLUMN_DATA_FLOW).from(p2).to(c31).next();
        g.addE(EDGE_LABEL_COLUMN_DATA_FLOW).from(p2).to(c32).next();

        //p3 branch causes the cycle
        g.addE(EDGE_LABEL_COLUMN_DATA_FLOW).from(c31).to(p3).next();
        g.addE(EDGE_LABEL_COLUMN_DATA_FLOW).from(c32).to(p3).next();

        g.addE(EDGE_LABEL_COLUMN_DATA_FLOW).from(p3).to(c21).next();
        g.addE(EDGE_LABEL_COLUMN_DATA_FLOW).from(p3).to(c22).next();

        //p4 branch leads to the destination
        g.addE(EDGE_LABEL_COLUMN_DATA_FLOW).from(c31).to(p4).next();
        g.addE(EDGE_LABEL_COLUMN_DATA_FLOW).from(c32).to(p4).next();

        g.addE(EDGE_LABEL_COLUMN_DATA_FLOW).from(p4).to(c41).next();
        g.addE(EDGE_LABEL_COLUMN_DATA_FLOW).from(p4).to(c42).next();
    }

    private static void addTableLineageData(GraphTraversalSource g) {
        Vertex d1 = getVertex(g, DATA_FILE, "d1", "d1");
        Vertex t1 = getVertex(g, RELATIONAL_TABLE, "t1", "t1");
        Vertex t2 = getVertex(g, RELATIONAL_TABLE, "t2", "t2");

        Vertex p1 = getVertex(g, PROCESS, "p1", "p1");
        Vertex p2 = getVertex(g, PROCESS, "p2", "p2");

        g.addE(EDGE_LABEL_TABLE_DATA_FLOW).from(d1).to(p1).next();
        g.addE(EDGE_LABEL_TABLE_DATA_FLOW).from(p1).to(t1).next();

        g.addE(EDGE_LABEL_TABLE_DATA_FLOW).from(t1).to(p2).next();
        g.addE(EDGE_LABEL_TABLE_DATA_FLOW).from(p2).to(t2).next();

    }

    private static Vertex getVertex(GraphTraversalSource g, String nodeType, String guid, String nodeId) {
        Vertex vertex;
        if (NODE_LABEL_SUB_PROCESS.equals(nodeType)) {
            vertex = g.addV(nodeType).property(PROPERTY_KEY_ENTITY_GUID, guid)
                    .property(PROPERTY_KEY_PROCESS_GUID, guid)
                    .property(PROPERTY_KEY_ENTITY_NODE_ID, nodeId)
                    .next();

        } else {
            vertex = g.addV(nodeType).property(PROPERTY_KEY_ENTITY_GUID, guid)
                    .property(PROPERTY_KEY_ENTITY_NODE_ID, nodeId)
                    .next();
        }
        return vertex;
    }

}