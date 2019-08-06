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
import org.odpi.openmetadata.governanceservers.openlineage.model.Graphs;
import org.odpi.openmetadata.governanceservers.openlineage.model.Queries;
import org.odpi.openmetadata.governanceservers.openlineage.util.GraphConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.inE;
import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.outE;
import static org.odpi.openmetadata.governanceservers.openlineage.admin.OpenLineageOperationalServices.*;
import static org.odpi.openmetadata.governanceservers.openlineage.util.GraphConstants.EDGE_LABEL_COLUMN_AND_PROCESS;
import static org.odpi.openmetadata.governanceservers.openlineage.util.GraphConstants.EDGE_LABEL_ENTITY_TO_GLOSSARYTERM;

public class GraphServices {

    private static final Logger log = LoggerFactory.getLogger(GraphServices.class);

    public String getInitialGraph(String lineageQuery, String graphString, String guid) {
        String response = "";

        graphString = reformatArg(graphString);
        lineageQuery = reformatArg(lineageQuery);

        Graph graph = getJanusGraph(graphString);
        switch (Queries.valueOf(lineageQuery)) {
            case ULTIMATESOURCE:
                response = ultimateSource(guid, graph);
                break;
            case ULTIMATEDESTINATION:
                response = ultimateDestination(guid, graph);
                break;
            case GLOSSARY:
                response = glossary(guid, graph);
                break;
            default:
                log.error(lineageQuery + " is not a valid lineage query");
        }
        return response;
    }

    private String reformatArg(String string) {
        string = string.toUpperCase();
        string = string.replaceAll("-", "");
        return string;
        
    }

    private String glossary(String guid, Graph graph) {
        GraphTraversalSource g = graph.traversal();
        Graph subGraph = (Graph)
                g.V().has(GraphConstants.PROPERTY_KEY_ENTITY_GUID, guid).
                inE(EDGE_LABEL_ENTITY_TO_GLOSSARYTERM).subgraph("subGraph").outV().
                cap("subGraph").next();
        return janusGraphToGraphson(subGraph);
    }

    private String ultimateSource(String guid, Graph graph) {
        GraphTraversalSource g = graph.traversal();
        Graph subGraph = (Graph)
                g.V().has(GraphConstants.PROPERTY_KEY_ENTITY_GUID, guid).
                until(inE(EDGE_LABEL_COLUMN_AND_PROCESS).count().is(0)).
                repeat(inE(EDGE_LABEL_COLUMN_AND_PROCESS).subgraph("subGraph").outV()).
                cap("subGraph").next();
        return janusGraphToGraphson(subGraph);
    }

    private String ultimateDestination(String guid, Graph graph) {
        GraphTraversalSource g = graph.traversal();
        Graph subGraph = (Graph)
                g.V().has(GraphConstants.PROPERTY_KEY_ENTITY_GUID, guid).
                until(outE(EDGE_LABEL_COLUMN_AND_PROCESS).count().is(0)).
                repeat(outE(EDGE_LABEL_COLUMN_AND_PROCESS).subgraph("subGraph").inV()).
                cap("subGraph").next();

        return janusGraphToGraphson(subGraph);
    }

    public void dumpGraph(String graphString) {
        graphString = reformatArg(graphString);
        JanusGraph graph = getJanusGraph(graphString);
        try {
            graph.io(IoCore.graphml()).writeGraph("graph-" + graphString + ".graphml");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public String exportGraph(String graphString) {
        graphString = reformatArg(graphString);
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
            case MAIN:
                graph = mainGraph;
                break;
            case BUFFER:
                graph = bufferGraph;
                break;
            case HISTORY:
                graph = historyGraph;
                break;
            case MOCK:
                graph = mockGraph;
                break;
            default:
                log.error(graphString + " is not a valid graph");
        }
        return graph;
    }

}
