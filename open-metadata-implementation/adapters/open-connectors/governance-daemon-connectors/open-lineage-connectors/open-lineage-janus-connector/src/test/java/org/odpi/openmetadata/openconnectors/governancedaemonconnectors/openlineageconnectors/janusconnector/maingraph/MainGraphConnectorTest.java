/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.maingraph;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.janusgraph.core.JanusGraphFactory;
import org.junit.BeforeClass;
import org.junit.Test;
import org.odpi.openmetadata.governanceservers.openlineage.ffdc.OpenLineageException;
import org.odpi.openmetadata.governanceservers.openlineage.responses.LineageResponse;

import java.util.List;

import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.inE;
import static org.junit.Assert.assertEquals;
import static org.odpi.openmetadata.governanceservers.openlineage.model.View.COLUMN_VIEW;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.*;

public class MainGraphConnectorTest {

    private static Graph aCyclicGraph;
    private static Graph cyclicGraph;
    private static Graph problematicCyclicGraph;

    private final MainGraphConnector mainGraphConnector = new MainGraphConnector();

    @BeforeClass
    public static void beforeClass() {
        aCyclicGraph = JanusGraphFactory.build().set("storage.backend", "inmemory").open();
        cyclicGraph = JanusGraphFactory.build().set("storage.backend", "inmemory").open();
        problematicCyclicGraph = JanusGraphFactory.build().set("storage.backend", "inmemory").open();

        GraphTraversalSource g = cyclicGraph.traversal();
        g = cyclicGraph.traversal();


        Vertex c11 = g.addV(NODE_LABEL_COLUMN.toString()).property(PROPERTY_KEY_ENTITY_NODE_ID, "c11").next();
        Vertex c12 = g.addV(NODE_LABEL_COLUMN.toString()).property(PROPERTY_KEY_ENTITY_NODE_ID, "c12").next();
        Vertex c21 = g.addV(NODE_LABEL_COLUMN.toString()).property(PROPERTY_KEY_ENTITY_NODE_ID, "c21").next();
        Vertex c22 = g.addV(NODE_LABEL_COLUMN.toString()).property(PROPERTY_KEY_ENTITY_NODE_ID, "c22").next();
        Vertex c31 = g.addV(NODE_LABEL_COLUMN.toString()).property(PROPERTY_KEY_ENTITY_NODE_ID, "c31").next();
        Vertex c32 = g.addV(NODE_LABEL_COLUMN.toString()).property(PROPERTY_KEY_ENTITY_NODE_ID, "c32").next();

        Vertex p1 = g.addV(NODE_LABEL_SUB_PROCESS.toString()).property(PROPERTY_KEY_ENTITY_NODE_ID, "p1").next();
        Vertex p2 = g.addV(NODE_LABEL_SUB_PROCESS.toString()).property(PROPERTY_KEY_ENTITY_NODE_ID, "p2").next();
        Vertex p3 = g.addV(NODE_LABEL_SUB_PROCESS.toString()).property(PROPERTY_KEY_ENTITY_NODE_ID, "p3").next();

        g.addE(COLUMN_VIEW.toString()).from(c11).to(p1).next();
        g.addE(COLUMN_VIEW.toString()).from(c12).to(p1).next();
        g.addE(COLUMN_VIEW.toString()).from(p1).to(c21).next();
        g.addE(COLUMN_VIEW.toString()).from(p1).to(c22).next();
        g.addE(COLUMN_VIEW.toString()).from(c21).to(p2).next();
        g.addE(COLUMN_VIEW.toString()).from(c22).to(p2).next();
        g.addE(COLUMN_VIEW.toString()).from(p2).to(c31).next();
        g.addE(COLUMN_VIEW.toString()).from(p2).to(c32).next();
        g.addE(COLUMN_VIEW.toString()).from(c31).to(p3).next();
        g.addE(COLUMN_VIEW.toString()).from(c32).to(p3).next();
        g.addE(COLUMN_VIEW.toString()).from(p3).to(c21).next();
        g.addE(COLUMN_VIEW.toString()).from(p3).to(c22).next();
        g.tx().close();

        g = problematicCyclicGraph.traversal();
    }

    @Test
    public void ultimateSource() throws Exception {
        LineageResponse lineageResponse = mainGraphConnector.ultimateSource(cyclicGraph, COLUMN_VIEW.toString(), "c32");
        System.out.println(lineageResponse);

    }

}