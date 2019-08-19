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
import org.odpi.openmetadata.governanceservers.openlineage.model.Scopes;
import org.odpi.openmetadata.governanceservers.openlineage.util.GraphConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.inE;
import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.outE;
import static org.odpi.openmetadata.governanceservers.openlineage.admin.OpenLineageOperationalServices.*;
import static org.odpi.openmetadata.governanceservers.openlineage.util.GraphConstants.*;

public class GraphServices {

    private static final Logger log = LoggerFactory.getLogger(GraphServices.class);

    /**
     * Returns the graph that the user will initially see when querying lineage. In the future, this method will be
     * extended to condense large paths to prevent cluttering of the users screen. The user will be able to extended
     * the condensed path by querying a different method.
     *
     *
     * @param scope The scope queried by the user: hostview, tableview, columnview.
     * @param lineageQuery ultimate-source, ultimate-destination, glossary.
     * @param graphString  main, buffer, mock, history.
     * @param guid         The guid of the node of which the lineage is queried from.
     * @return A subgraph containing all relevant paths, in graphSON format.
     */
    public String getInitialGraph(String scope, String lineageQuery, String graphString, String guid) {
        String response = "";

        scope = reformatArg(scope);
        lineageQuery = reformatArg(lineageQuery);
        graphString = reformatArg(graphString);

        Graph graph = getJanusGraph(graphString);
        switch (Queries.valueOf(lineageQuery)) {
            case ULTIMATESOURCE:
                response = ultimateSource(scope, graph, guid);
                break;
            case ULTIMATEDESTINATION:
                response = ultimateDestination(scope, graph, guid);
                break;
            case GLOSSARY:
                response = glossary(graph, guid);
                break;
            default:
                log.error(lineageQuery + " is not a valid lineage query");
        }
        return response;
    }

    /**
     * Map http parameter to enum value by converting to uppercase and removing - characters.
     *
     * @param string main, buffer, mock, history.
     * @return String which corresponds to enum format.
     */
    private String reformatArg(String string) {
        string = string.toUpperCase();
        string = string.replaceAll("-", "");
        return string;
    }


    /**
     * Returns a subgraph containing all columns or tables connected to the queried glossary term.
     *
     * @param graph MAIN, BUFFER, MOCK, HISTORY.
     * @param guid  The guid of the glossary term of which the lineage is queried of.
     * @return a subgraph in the GraphSON format.
     */
    private String glossary(Graph graph, String guid) {
        GraphTraversalSource g = graph.traversal();
        Graph subGraph = (Graph)
                g.V().has(GraphConstants.PROPERTY_KEY_ENTITY_GUID, guid).
                        inE(EDGE_LABEL_SEMANTIC).subgraph("subGraph").outV().
                        inE(EDGE_LABEL_SEMANTIC).subgraph("subGraph").outV().
                        cap("subGraph").next();
        return janusGraphToGraphson(subGraph);
    }

    /**
     * Returns a subgraph containing all paths leading from any root node to the queried node. The queried node can be a
     * column, table, or host.
     *
     * @param scope The scope queried by the user: hostview, tableview, columnview.
     * @param graph MAIN, BUFFER, MOCK, HISTORY.
     * @param guid  The guid of the node of which the lineage is queried of. This can be a column, table, or host node.
     * @return a subgraph in the GraphSON format.
     */
    private String ultimateSource(String scope, Graph graph, String guid) {
        GraphTraversalSource g = graph.traversal();
        String edgeLabel = getEdgeLabel(scope);

        Graph subGraph = (Graph)
                g.V().has(GraphConstants.PROPERTY_KEY_ENTITY_GUID, guid).
                        until(inE(edgeLabel).count().is(0)).
                        repeat(inE(edgeLabel).subgraph("subGraph").outV()).
                        cap("subGraph").next();
        return janusGraphToGraphson(subGraph);
    }

    /**
     * Returns a subgraph containing all paths leading from the queried node to any leaf nodes. The queried node can be a
     * column, table, or host.
     *
     * @param scope The scope queried by the user: hostview, tableview, columnview.
     * @param graph MAIN, BUFFER, MOCK, HISTORY.
     * @param guid  The guid of the node of which the lineage is queried of. This can be a column, table, or host node.
     * @return a subgraph in the GraphSON format.
     */
    private String ultimateDestination(String scope, Graph graph, String guid) {
        GraphTraversalSource g = graph.traversal();
        String edgeLabel = getEdgeLabel(scope);
        Graph subGraph = (Graph)
                g.V().has(GraphConstants.PROPERTY_KEY_ENTITY_GUID, guid).
                        until(outE(edgeLabel).count().is(0)).
                        repeat(outE(edgeLabel).subgraph("subGraph").inV()).
                        cap("subGraph").next();

        return janusGraphToGraphson(subGraph);
    }

    /**
     * Retrieve the label of the edges that are to be traversed with the gremlin query.
     *
     * @param scope The scope queried by the user: hostview, tableview, columnview.
     * @return The label of the edges that are to be traversed with the gremlin query.
     */
    private String getEdgeLabel(String scope) {
        String edgeLabel = "";
        switch (Scopes.valueOf(scope)) {
            case HOSTVIEW:
                edgeLabel = EDGE_LABEL_HOST_AND_PROCESS;
                break;
            case TABLEVIEW:
                edgeLabel = EDGE_LABEL_TABLE_AND_PROCESS;
                break;
            case COLUMNVIEW:
                edgeLabel = EDGE_LABEL_COLUMN_AND_PROCESS;
                break;
            default:
                log.error(scope + " is not a valid lineage scope");
        }
        return edgeLabel;
    }

    /**
     * Write an entire graph to disc in the Egeria root folder, in the .GraphMl format.
     *
     * @param graphString MAIN, BUFFER, MOCK, HISTORY.
     */
    public void dumpGraph(String graphString) {
        graphString = reformatArg(graphString);
        JanusGraph graph = getJanusGraph(graphString);
        try {
            graph.io(IoCore.graphml()).writeGraph("graph-" + graphString + ".graphml");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * Return an entire graph, in GraphSon format.
     *
     * @param graphString MAIN, BUFFER, MOCK, HISTORY.
     * @return The queried graph, in graphSON format.
     */
    public String exportGraph(String graphString) {
        graphString = reformatArg(graphString);
        JanusGraph graph = getJanusGraph(graphString);
        return janusGraphToGraphson(graph);
    }

    /**
     * Convert a Graph object which is originally created by a Janusgraph writer to a String in GraphSON format.
     *
     * @param graph The Graph object to be converted.
     * @return The provided Graph as a String, in the GraphSON format.
     */
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

    /**
     * Retrieve an Open Lineage Services graph.
     *
     * @param graphString The name of the queried graph.
     * @return The Graph object.
     */
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
