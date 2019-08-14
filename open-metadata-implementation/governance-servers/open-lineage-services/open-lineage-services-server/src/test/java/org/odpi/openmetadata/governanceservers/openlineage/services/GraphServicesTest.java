/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.openlineage.services;

import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.io.graphson.GraphSONReader;
import org.apache.tinkerpop.gremlin.structure.util.empty.EmptyGraph;
import org.janusgraph.core.JanusGraph;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.odpi.openmetadata.governanceservers.openlineage.admin.OpenLineageOperationalServices;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class GraphServicesTest {

    private GraphServices graphServices;

    @Mock
    private JanusGraph graph;

    @Before
    public void before(){
        MockitoAnnotations.initMocks(this);
        this.graphServices = new GraphServices();


        OpenLineageOperationalServices.mockGraph = graph;
    }

    @Test
    public void initialGraphTest() throws IOException {
        String graphAsString = graphServices.initialGraph("Columnview", "ultimate-source", "mock", "t1c1");
        GraphSONReader graphSONReader = GraphSONReader.build().create();
        Graph graph =  EmptyGraph.instance();

        graphSONReader.readGraph(new ByteArrayInputStream(graphAsString.getBytes()), graph);

    }
}
