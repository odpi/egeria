/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.openlineage.services;


import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.structure.VertexProperty;
import org.apache.tinkerpop.gremlin.structure.io.IoCore;
import org.apache.tinkerpop.gremlin.structure.io.graphson.GraphSONMapper;
import org.apache.tinkerpop.gremlin.structure.io.graphson.GraphSONWriter;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph;
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
import java.util.Iterator;
import java.util.List;

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
     * @param scope        The scope queried by the user: hostview, tableview, columnview.
     * @param lineageQuery ultimate-source, ultimate-destination, glossary.
     * @param graphName    main, buffer, mock, history.
     * @param guid         The guid of the node of which the lineage is queried from.
     * @return A subgraph containing all relevant paths, in graphSON format.
     */
    public String queryLineage(String scope, String lineageQuery, String graphName, String guid) {
        String response = "";

        scope = reformatArg(scope);
        lineageQuery = reformatArg(lineageQuery);
        graphName = reformatArg(graphName);

        Graph graph = getJanusGraph(graphName);
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
     * Returns a subgraph containing all paths leading from any root node to the queried node, but condensed so that
     * the path itself between the root nodes and the queried node has been abstracted.
     *
     * @param scope The scope queried by the user: tableview, columnview.
     * @param graph MAIN, BUFFER, MOCK, HISTORY.
     * @param guid  The guid of the node of which the lineage is queried of. This can be a column or a table.
     * @return a subgraph in the GraphSON format.
     */
    private String ultimateSource(String scope, Graph graph, String guid) {
        GraphTraversalSource g = graph.traversal();
        String edgeLabel = getEdgeLabel(scope);

        List<Vertex> sourcesList = g.V().has(GraphConstants.PROPERTY_KEY_ENTITY_GUID, guid).
                until(inE(edgeLabel).count().is(0)).
                repeat(inE(edgeLabel).outV()).dedup().toList();
        Vertex originalQueriedVertex = g.V().has(GraphConstants.PROPERTY_KEY_ENTITY_GUID, guid).next();

        Graph responseGraph = TinkerGraph.open();
        g = responseGraph.traversal();

        Vertex condensation = g.addV(NODE_LABEL_CONDENSED).next();
        Vertex queriedVertex = g.addV(originalQueriedVertex.label()).next();

        copyVertexProperties(originalQueriedVertex, queriedVertex);
        condensation.addEdge(EDGE_LABEL_CONDENSED, queriedVertex);

        for (Vertex originalVertex : sourcesList) {
            Vertex vertex = g.addV(originalVertex.label()).next();
            copyVertexProperties(originalVertex, vertex);
            vertex.addEdge(EDGE_LABEL_CONDENSED, condensation);
        }
        return janusGraphToGraphson(responseGraph);
    }

    /**
     * Returns a subgraph containing all paths leading from the queried node to any leaf nodes. The queried node can be a
     * column, table, or host.
     *
     * @param scope The scope queried by the user: tableview, columnview.
     * @param graph MAIN, BUFFER, MOCK, HISTORY.
     * @param guid  The guid of the node of which the lineage is queried of. This can be a column, table, or host node.
     * @return a subgraph in the GraphSON format.
     */
    private String ultimateDestination(String scope, Graph graph, String guid) {
        GraphTraversalSource g = graph.traversal();
        String edgeLabel = getEdgeLabel(scope);
        List<Vertex> destinationsList = g.V().has(GraphConstants.PROPERTY_KEY_ENTITY_GUID, guid).
                until(outE(edgeLabel).count().is(0)).
                repeat(outE(edgeLabel).inV()).dedup().toList();

        Vertex originalQueriedVertex = g.V().has(GraphConstants.PROPERTY_KEY_ENTITY_GUID, guid).next();

        Graph responseGraph = TinkerGraph.open();
        g = responseGraph.traversal();

        Vertex condensation = g.addV(NODE_LABEL_CONDENSED).next();
        Vertex queriedVertex = g.addV(originalQueriedVertex.label()).next();

        copyVertexProperties(originalQueriedVertex, queriedVertex);
        queriedVertex.addEdge(EDGE_LABEL_CONDENSED, condensation);

        for (Vertex originalVertex : destinationsList) {
            Vertex vertex = g.addV(originalVertex.label()).next();
            copyVertexProperties(originalVertex, vertex);
            condensation.addEdge(EDGE_LABEL_CONDENSED, vertex);
        }
        try {
            responseGraph.io(IoCore.graphml()).writeGraph("wololo.graphml");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return janusGraphToGraphson(responseGraph);
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
     * Copy properties from one vertex to another.
     *
     * @param originalVertex The vertex to be copied from.
     * @param newVertex      The vertex to be copied to.
     */
    private void copyVertexProperties(Vertex originalVertex, Vertex newVertex) {
        final Iterator<VertexProperty<Object>> iterator = originalVertex.properties();
        while (iterator.hasNext()) {
            VertexProperty oldVertexProperty = iterator.next();
            newVertex.property(oldVertexProperty.key(), oldVertexProperty.value());
        }
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
        GraphSONWriter writer = GraphSONWriter.build().mapper(mapper).wrapAdjacencyList(true).create();

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
     * @param graphName The name of the queried graph.
     * @return The Graph object.
     */
    private JanusGraph getJanusGraph(String graphName) {
        JanusGraph graph = null;
        switch (Graphs.valueOf(graphName)) {
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
                log.error(graphName + " is not a valid graph");
        }
        return graph;
    }

}
