/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.maingraph;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.janusgraph.core.JanusGraph;
import org.janusgraph.core.JanusGraphFactory;
import org.junit.BeforeClass;
import org.junit.Test;
import org.odpi.openmetadata.governanceservers.openlineage.ffdc.OpenLineageException;
import org.odpi.openmetadata.governanceservers.openlineage.model.LineageVertex;
import org.odpi.openmetadata.governanceservers.openlineage.model.LineageVerticesAndEdges;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.*;

public class MainGraphConnectorTest {

    private static Graph cyclicGraph;
    private final MainGraphConnector mainGraphConnector = new MainGraphConnector();

//    @BeforeClass
//    public static void beforeClass() {
//        cyclicGraph = JanusGraphFactory.build().set("storage.backend", "inmemory").open();
//        GraphTraversalSource g = cyclicGraph.traversal();
//
//        Vertex c11 = g.addV(NODE_LABEL_COLUMN).property(PROPERTY_KEY_ENTITY_NODE_ID, "c11").next();
//        Vertex c12 = g.addV(NODE_LABEL_COLUMN).property(PROPERTY_KEY_ENTITY_NODE_ID, "c12").next();
//        Vertex c21 = g.addV(NODE_LABEL_COLUMN).property(PROPERTY_KEY_ENTITY_NODE_ID, "c21").next();
//        Vertex c22 = g.addV(NODE_LABEL_COLUMN).property(PROPERTY_KEY_ENTITY_NODE_ID, "c22").next();
//        Vertex c31 = g.addV(NODE_LABEL_COLUMN).property(PROPERTY_KEY_ENTITY_NODE_ID, "c31").next();
//        Vertex c32 = g.addV(NODE_LABEL_COLUMN).property(PROPERTY_KEY_ENTITY_NODE_ID, "c32").next();
//        Vertex c41 = g.addV(NODE_LABEL_COLUMN).property(PROPERTY_KEY_ENTITY_NODE_ID, "c41").next();
//        Vertex c42 = g.addV(NODE_LABEL_COLUMN).property(PROPERTY_KEY_ENTITY_NODE_ID, "c42").next();
//
//        Vertex p1 = g.addV(NODE_LABEL_SUB_PROCESS).property(PROPERTY_KEY_ENTITY_NODE_ID, "p1").next();
//        Vertex p2 = g.addV(NODE_LABEL_SUB_PROCESS).property(PROPERTY_KEY_ENTITY_NODE_ID, "p2").next();
//        Vertex p3 = g.addV(NODE_LABEL_SUB_PROCESS).property(PROPERTY_KEY_ENTITY_NODE_ID, "p3").next();
//        Vertex p4 = g.addV(NODE_LABEL_SUB_PROCESS).property(PROPERTY_KEY_ENTITY_NODE_ID, "p4").next();
//
//        g.addE(EDGE_LABEL_COLUMN_AND_PROCESS).from(c11).to(p1).next();
//        g.addE(EDGE_LABEL_COLUMN_AND_PROCESS).from(c12).to(p1).next();
//
//        g.addE(EDGE_LABEL_COLUMN_AND_PROCESS).from(p1).to(c21).next();
//        g.addE(EDGE_LABEL_COLUMN_AND_PROCESS).from(p1).to(c21).next();
//
//        g.addE(EDGE_LABEL_COLUMN_AND_PROCESS).from(c21).to(p2).next();
//        g.addE(EDGE_LABEL_COLUMN_AND_PROCESS).from(c22).to(p2).next();
//
//        g.addE(EDGE_LABEL_COLUMN_AND_PROCESS).from(p2).to(c31).next();
//        g.addE(EDGE_LABEL_COLUMN_AND_PROCESS).from(p2).to(c32).next();
//
//        //p3 branch causes the cycle
//        g.addE(EDGE_LABEL_COLUMN_AND_PROCESS).from(c31).to(p3).next();
//        g.addE(EDGE_LABEL_COLUMN_AND_PROCESS).from(c32).to(p3).next();
//
//        g.addE(EDGE_LABEL_COLUMN_AND_PROCESS).from(p3).to(c21).next();
//        g.addE(EDGE_LABEL_COLUMN_AND_PROCESS).from(p3).to(c22).next();
//
//        //p4 branch leads to the destination
//        g.addE(EDGE_LABEL_COLUMN_AND_PROCESS).from(c31).to(p4).next();
//        g.addE(EDGE_LABEL_COLUMN_AND_PROCESS).from(c32).to(p4).next();
//
//        g.addE(EDGE_LABEL_COLUMN_AND_PROCESS).from(p4).to(c41).next();
//        g.addE(EDGE_LABEL_COLUMN_AND_PROCESS).from(p4).to(c42).next();
//    }
//

//    @Test
//    public void ultimateSource() throws OpenLineageException {
//        HashSet<String> expectedNodeIDs = new HashSet<>();
//        final String queriedNodeID = "c32";
//        expectedNodeIDs.add("c11");
//        expectedNodeIDs.add("c12");
//        expectedNodeIDs.add(queriedNodeID);
//        expectedNodeIDs.add(PROPERTY_VALUE_NODE_ID_CONDENSED_SOURCE);
//
//        LineageVerticesAndEdges lineageVerticesAndEdges = mainGraphConnector.ultimateSource(EDGE_LABEL_COLUMN_AND_PROCESS, queriedNodeID);
//        Set<LineageVertex> lineageVertices = lineageVerticesAndEdges.getLineageVertices();
//        validateResponse(expectedNodeIDs, lineageVertices);
//    }
//
//    @Test
//    public void ultimateDestination() throws OpenLineageException {
//        HashSet<String> expectedNodeIDs = new HashSet<>();
//        final String queriedNodeID = "c11";
//        expectedNodeIDs.add("c41");
//        expectedNodeIDs.add("c42");
//        expectedNodeIDs.add(queriedNodeID);
//        expectedNodeIDs.add(PROPERTY_VALUE_NODE_ID_CONDENSED_DESTINATION);
//
//        LineageVerticesAndEdges lineageVerticesAndEdges = mainGraphConnector.ultimateDestination(EDGE_LABEL_COLUMN_AND_PROCESS, queriedNodeID);
//        Set<LineageVertex> lineageVertices = lineageVerticesAndEdges.getLineageVertices();
//
//        validateResponse(expectedNodeIDs, lineageVertices);
//    }
//
//    @Test
//    public void sourceAndDestination() throws OpenLineageException {
//        HashSet<String> expectedNodeIDs = new HashSet<>();
//        final String queriedNodeID = "c21";
//        expectedNodeIDs.add("c11");
//        expectedNodeIDs.add("c12");
//        expectedNodeIDs.add("c41");
//        expectedNodeIDs.add("c42");
//        expectedNodeIDs.add(queriedNodeID);
//        expectedNodeIDs.add(PROPERTY_VALUE_NODE_ID_CONDENSED_SOURCE);
//        expectedNodeIDs.add(PROPERTY_VALUE_NODE_ID_CONDENSED_DESTINATION);
//
//        LineageVerticesAndEdges lineageVerticesAndEdges = mainGraphConnector.sourceAndDestination(EDGE_LABEL_COLUMN_AND_PROCESS, queriedNodeID);
//        Set<LineageVertex> lineageVertices = lineageVerticesAndEdges.getLineageVertices();
//
//        validateResponse(expectedNodeIDs, lineageVertices);
//    }
//
//        @Test
//        public void endToEnd () {
//            HashSet<String> expectedNodeIDs = new HashSet<>();
//            final String queriedNodeID = "c22";
//            expectedNodeIDs.add("c11");
//            expectedNodeIDs.add("c12");
//            expectedNodeIDs.add("c21");
//            expectedNodeIDs.add("c22");
//            expectedNodeIDs.add("c31");
//            expectedNodeIDs.add("c32");
//            expectedNodeIDs.add("c41");
//            expectedNodeIDs.add("c42");
//            expectedNodeIDs.add("p1");
//            expectedNodeIDs.add("p2");
//            expectedNodeIDs.add("p3");
//            expectedNodeIDs.add("p4");
//
//            LineageVerticesAndEdges lineageVerticesAndEdges = mainGraphConnector.endToEnd(EDGE_LABEL_COLUMN_AND_PROCESS, queriedNodeID);
//            Set<LineageVertex> lineageVertices = lineageVerticesAndEdges.getLineageVertices();
//
//            validateResponse(expectedNodeIDs, lineageVertices);
//        }
//
//    @Test
//    public void glossary() {
//        JanusGraph cyclicGlossaryGraph = JanusGraphFactory.build().set("storage.backend", "inmemory").open();
//        GraphTraversalSource g = cyclicGlossaryGraph.traversal();
//        Vertex g1 = g.addV(NODE_LABEL_COLUMN).property(PROPERTY_KEY_ENTITY_NODE_ID, "g1").next();
//        Vertex g2 = g.addV(NODE_LABEL_COLUMN).property(PROPERTY_KEY_ENTITY_NODE_ID, "g2").next();
//        Vertex g3 = g.addV(NODE_LABEL_COLUMN).property(PROPERTY_KEY_ENTITY_NODE_ID, "g3").next();
//
//        Vertex c1 = g.addV(NODE_LABEL_COLUMN).property(PROPERTY_KEY_ENTITY_NODE_ID, "c1").next();
//        Vertex c2 = g.addV(NODE_LABEL_COLUMN).property(PROPERTY_KEY_ENTITY_NODE_ID, "c2").next();
//        Vertex c3 = g.addV(NODE_LABEL_COLUMN).property(PROPERTY_KEY_ENTITY_NODE_ID, "c3").next();
//
//        g.addE(EDGE_LABEL_GLOSSARYTERM_TO_GLOSSARYTERM).from(g1).to(g2).next();
//        g.addE(EDGE_LABEL_GLOSSARYTERM_TO_GLOSSARYTERM).from(g2).to(g3).next();
//        g.addE(EDGE_LABEL_GLOSSARYTERM_TO_GLOSSARYTERM).from(g3).to(g1).next();
//
//        g.addE(EDGE_LABEL_SEMANTIC).from(c1).to(g1).next();
//        g.addE(EDGE_LABEL_SEMANTIC).from(c2).to(g2).next();
//        g.addE(EDGE_LABEL_SEMANTIC).from(c3).to(g3).next();
//
//        HashSet<String> expectedNodeIDs = new HashSet<>();
//        final String queriedNodeID = "g2";
//        expectedNodeIDs.add("g1");
//        expectedNodeIDs.add("g2");
//        expectedNodeIDs.add("g3");
//        expectedNodeIDs.add("c1");
//        expectedNodeIDs.add("c2");
//        expectedNodeIDs.add("c3");
//
//        LineageVerticesAndEdges lineageVerticesAndEdges = mainGraphConnector.glossary(queriedNodeID);
//        Set<LineageVertex> lineageVertices = lineageVerticesAndEdges.getLineageVertices();
//
//        validateResponse(expectedNodeIDs, lineageVertices);
//    }
//
//    private void validateResponse(HashSet<String> expectedNodeIDs, Set<LineageVertex> lineageVertices) {
//        assertEquals(expectedNodeIDs.size(), lineageVertices.size());
//        for (LineageVertex returnedVertex : lineageVertices) {
//            assertTrue(expectedNodeIDs.contains(returnedVertex.getNodeID()));
//        }
//    }
//
//    @Test
//    public void problematicCyclicGraphSourceDestination() throws Exception {
//        //A triangle of three nodes
//        JanusGraph problematicCyclicGraph = JanusGraphFactory.build().set("storage.backend", "inmemory").open();
//        GraphTraversalSource g = problematicCyclicGraph.traversal();
//        Vertex c1 = g.addV(NODE_LABEL_COLUMN).property(PROPERTY_KEY_ENTITY_NODE_ID, "c1").next();
//        Vertex c2 = g.addV(NODE_LABEL_COLUMN).property(PROPERTY_KEY_ENTITY_NODE_ID, "c2").next();
//        Vertex c3 = g.addV(NODE_LABEL_COLUMN).property(PROPERTY_KEY_ENTITY_NODE_ID, "c3").next();
//
//        g.addE(EDGE_LABEL_COLUMN_AND_PROCESS).from(c1).to(c2).next();
//        g.addE(EDGE_LABEL_COLUMN_AND_PROCESS).from(c2).to(c3).next();
//        g.addE(EDGE_LABEL_COLUMN_AND_PROCESS).from(c3).to(c1).next();
//        final String queriedNodeID = "c32";
//        try{
//            mainGraphConnector.ultimateSource(EDGE_LABEL_COLUMN_AND_PROCESS, queriedNodeID);
//            mainGraphConnector.ultimateDestination(EDGE_LABEL_COLUMN_AND_PROCESS, queriedNodeID);
//            mainGraphConnector.sourceAndDestination(EDGE_LABEL_COLUMN_AND_PROCESS, queriedNodeID);
//        } catch (OpenLineageException e) {
//            return;
//        }
//        throw new Exception();
//    }
}