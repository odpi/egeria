/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.openlineage.services;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.io.IoCore;
import org.apache.tinkerpop.gremlin.structure.io.graphson.GraphSONMapper;
import org.apache.tinkerpop.gremlin.structure.io.graphson.GraphSONWriter;
import org.janusgraph.core.JanusGraph;
import org.janusgraph.graphdb.tinkerpop.io.graphson.JanusGraphSONModuleV2d0;
import org.odpi.openmetadata.governanceservers.openlineage.util.GraphConstants;
import org.odpi.openmetadata.governanceservers.openlineage.util.Graphs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.inE;
import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.outE;
import static org.odpi.openmetadata.governanceservers.openlineage.admin.OpenLineageOperationalServices.*;

public class GraphServices {

    private static final Logger log = LoggerFactory.getLogger(GraphServices.class);

    public String getInitialGraph(String lineageQuery, String graphString, String guid) {

        String response = "";
        Graph graph = getJanusGraph(graphString);
        switch (Graphs.valueOf(lineageQuery)) {
            case ultimatesource:
                response = ultimateSource(guid, graph);
                break;
            case ultimatedestination:
                response = ultimateDestination(guid, graph);
                break;
                default:
                    log.error(lineageQuery + " is not a valid lineage query");
        }
        return response;
    }

    private String ultimateSource(String guid, Graph graph) {
        GraphTraversalSource g = graph.traversal();
        Graph subGraph = (Graph) g.V().has(GraphConstants.PROPERTY_KEY_ENTITY_GUID, guid).
                until(inE("ETL").count().is(0)).
                repeat(inE("ETL").subgraph("subGraph").outV()).
                cap("subGraph").next();
        return janusGraphToGraphson(subGraph);
    }

    private String ultimateDestination(String guid, Graph graph) {
        GraphTraversalSource g = graph.traversal();
        Graph subGraph = (Graph) g.V().has(GraphConstants.PROPERTY_KEY_ENTITY_GUID, guid).
                until(outE("ETL").count().is(0)).
                repeat(outE("ETL").subgraph("subGraph").inV()).
                cap("subGraph").next();

        return janusGraphToGraphson(subGraph);
    }

    public void dumpGraph(String graphString) {
        JanusGraph graph = getJanusGraph(graphString);
        try {
            graph.io(IoCore.graphml()).writeGraph("graph-" + Graphs.valueOf(graphString) + ".graphml");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public String exportGraph(String graphString) {
        JanusGraph graph = getJanusGraph(graphString);
        return janusGraphToGraphson(graph);
    }

    private String janusGraphToGraphson(Graph graph) {
        OutputStream out = new ByteArrayOutputStream();
        GraphSONMapper mapper = GraphSONMapper.build().addCustomModule(JanusGraphSONModuleV2d0.getInstance()).create();
        GraphSONWriter writer = GraphSONWriter.build().mapper(mapper).create();
        try {
            writer.writeGraph(out, graph);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return out.toString();
    }

    private JanusGraph getJanusGraph(String graphString) {
        JanusGraph graph = null;
        switch (Graphs.valueOf(graphString)) {
            case main:
                graph = mainGraph;
                break;
            case buffer:
                graph = bufferGraph;
                break;
            case history:
                graph = historyGraph;
                break;
            case mock:
                graph = mockGraph;
                break;
            default:
                log.error(graphString + " is not a valid graph");
        }
        return graph;
    }

}
