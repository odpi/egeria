/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.openlineage.services;


import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.structure.VertexProperty;
import org.apache.tinkerpop.gremlin.structure.io.IoCore;
import org.apache.tinkerpop.gremlin.structure.io.graphson.GraphSONMapper;
import org.apache.tinkerpop.gremlin.structure.io.graphson.GraphSONWriter;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph;
import org.janusgraph.core.JanusGraph;
import org.janusgraph.graphdb.tinkerpop.io.graphson.JanusGraphSONModuleV2d0;
import org.odpi.openmetadata.governanceservers.openlineage.model.Graph;
import org.odpi.openmetadata.governanceservers.openlineage.model.Query;
import org.odpi.openmetadata.governanceservers.openlineage.model.Scope;
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
     * Returns a lineage subgraph.
     *
     * @param scope        The scope queried by the user: hostview, tableview, columnview.
     * @param lineageQuery source-and-destination, end-to-end, ultimate-source, ultimate-destination, glossary.
     * @param graphName    main, buffer, mock, history.
     * @param guid         The guid of the node of which the lineage is queried from.
     * @return A subgraph containing all relevant paths, in graphSON format.
     */
    public String queryLineage(String scope, String lineageQuery, String graphName, String guid) {
        String response = "";

        scope = reformatArg(scope);
        lineageQuery = reformatArg(lineageQuery);
        graphName = reformatArg(graphName);

        org.apache.tinkerpop.gremlin.structure.Graph graph = getJanusGraph(graphName);
        switch (Query.valueOf(lineageQuery)) {
            case SOURCEANDDESTINATION:
                response = sourceAndDestination(scope, graph, guid);
                break;
            case ENDTOEND:
                response = endToEnd(scope, graph, guid);
                break;
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
     * Returns a subgraph containing all root and leaf nodes of the full graph that are connected with the queried node.
     * The queried node can be a column or table.
     *
     * @param scope The scope queried by the user: tableview, columnview.
     * @param graph MAIN, BUFFER, MOCK, HISTORY.
     * @param guid  The guid of the node of which the lineage is queried of. This can be a column or a table.
     * @return a subgraph in the GraphSON format.
     */
    private String sourceAndDestination(String scope, org.apache.tinkerpop.gremlin.structure.Graph graph, String guid) {
        GraphTraversalSource g = graph.traversal();
        String edgeLabel = getEdgeLabel(scope);

        List<Vertex> sourcesList = g.V().has(GraphConstants.PROPERTY_KEY_ENTITY_GUID, guid).
                until(inE(edgeLabel).count().is(0)).
                repeat(inE(edgeLabel).outV()).dedup().toList();

        List<Vertex> destinationsList = g.V().has(GraphConstants.PROPERTY_KEY_ENTITY_GUID, guid).
                until(outE(edgeLabel).count().is(0)).
                repeat(outE(edgeLabel).inV()).dedup().toList();

        Vertex originalQueriedVertex = g.V().has(GraphConstants.PROPERTY_KEY_ENTITY_GUID, guid).next();

        org.apache.tinkerpop.gremlin.structure.Graph responseGraph = TinkerGraph.open();
        g = responseGraph.traversal();

        Vertex sourceCondensation = g.addV(NODE_LABEL_CONDENSED).next();
        Vertex destinationCondensation = g.addV(NODE_LABEL_CONDENSED).next();

        Vertex queriedVertex = g.addV(originalQueriedVertex.label()).next();
        copyVertexProperties(originalQueriedVertex, queriedVertex);

        sourceCondensation.addEdge(EDGE_LABEL_CONDENSED, queriedVertex);
        queriedVertex.addEdge(EDGE_LABEL_CONDENSED, destinationCondensation);

        for (Vertex originalVertex : sourcesList) {
            Vertex vertex = g.addV(originalVertex.label()).next();
            copyVertexProperties(originalVertex, vertex);
            vertex.addEdge(EDGE_LABEL_CONDENSED, sourceCondensation);
        }

        for (Vertex originalVertex : destinationsList) {
            Vertex vertex = g.addV(originalVertex.label()).next();
            copyVertexProperties(originalVertex, vertex);
            destinationCondensation.addEdge(EDGE_LABEL_CONDENSED, vertex);
        }
        return janusGraphToGraphson(responseGraph);
    }

    /**
     * Returns a subgraph containing all paths leading from any root node to the queried node, and all of the paths
     * leading from the queried node to any leaf nodes. The queried node can be a column or table.
     *
     * @param scope The scope queried by the user: tableview, columnview.
     * @param graph MAIN, BUFFER, MOCK, HISTORY.
     * @param guid  The guid of the node of which the lineage is queried of. This can be a column or a table.
     * @return a subgraph in the GraphSON format.
     */
    private String endToEnd(String scope, org.apache.tinkerpop.gremlin.structure.Graph graph, String guid) {
        GraphTraversalSource g = graph.traversal();
        String edgeLabel = getEdgeLabel(scope);

        final GraphTraversal<Vertex, Vertex> queriedNode = g.V().has(GraphConstants.PROPERTY_KEY_ENTITY_GUID, guid);
        final GraphTraversal<Vertex, Vertex> queriedNode2 = g.V().has(GraphConstants.PROPERTY_KEY_ENTITY_GUID, guid);
        final GraphTraversal<Vertex, Vertex> queriedNode3 = g.V().has(GraphConstants.PROPERTY_KEY_ENTITY_GUID, guid);

        org.apache.tinkerpop.gremlin.structure.Graph endToEndGraph = (org.apache.tinkerpop.gremlin.structure.Graph)
                queriedNode.union(
                        queriedNode2.
                                until(inE(edgeLabel).count().is(0)).
                                repeat(inE(edgeLabel).subgraph("subGraph").outV()),
                        queriedNode3.
                                until(outE(edgeLabel).count().is(0)).
                                repeat(outE(edgeLabel).subgraph("subGraph").inV())
                ).cap("subGraph").next();

        return janusGraphToGraphson(endToEndGraph);
    }

    /**
     * Returns a subgraph containing all root of the full graph that are connected with the queried node.
     * The queried node can be a column or table.
     *
     * @param scope The scope queried by the user: tableview, columnview.
     * @param graph MAIN, BUFFER, MOCK, HISTORY.
     * @param guid  The guid of the node of which the lineage is queried of. This can be a column or a table.
     * @return a subgraph in the GraphSON format.
     */
    private String ultimateSource(String scope, org.apache.tinkerpop.gremlin.structure.Graph graph, String guid) {
        GraphTraversalSource g = graph.traversal();
        String edgeLabel = getEdgeLabel(scope);

        List<Vertex> sourcesList = g.V().has(GraphConstants.PROPERTY_KEY_ENTITY_GUID, guid).
                until(inE(edgeLabel).count().is(0)).
                repeat(inE(edgeLabel).outV()).dedup().toList();
        Vertex originalQueriedVertex = g.V().has(GraphConstants.PROPERTY_KEY_ENTITY_GUID, guid).next();

        org.apache.tinkerpop.gremlin.structure.Graph responseGraph = TinkerGraph.open();
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
     * Returns a subgraph containing all leaf nodes of the full graph that are connected with the queried node.
     * The queried node can be a column or table.
     *
     * @param scope The scope queried by the user: tableview, columnview.
     * @param graph MAIN, BUFFER, MOCK, HISTORY.
     * @param guid  The guid of the node of which the lineage is queried of. This can be a column, table, or host node.
     * @return a subgraph in the GraphSON format.
     */
    private String ultimateDestination(String scope, org.apache.tinkerpop.gremlin.structure.Graph graph, String guid) {
        GraphTraversalSource g = graph.traversal();
        String edgeLabel = getEdgeLabel(scope);
        List<Vertex> destinationsList = g.V().has(GraphConstants.PROPERTY_KEY_ENTITY_GUID, guid).
                until(outE(edgeLabel).count().is(0)).
                repeat(outE(edgeLabel).inV()).dedup().toList();

        Vertex originalQueriedVertex = g.V().has(GraphConstants.PROPERTY_KEY_ENTITY_GUID, guid).next();

        org.apache.tinkerpop.gremlin.structure.Graph responseGraph = TinkerGraph.open();
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
        return janusGraphToGraphson(responseGraph);
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
    private String glossary(org.apache.tinkerpop.gremlin.structure.Graph graph, String guid) {
        GraphTraversalSource g = graph.traversal();
        org.apache.tinkerpop.gremlin.structure.Graph subGraph = (org.apache.tinkerpop.gremlin.structure.Graph)
                g.V().has(GraphConstants.PROPERTY_KEY_ENTITY_GUID, guid).
                        inE(EDGE_LABEL_SEMANTIC).subgraph("subGraph").outV().
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
        switch (Scope.valueOf(scope)) {
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
    private String janusGraphToGraphson(org.apache.tinkerpop.gremlin.structure.Graph graph) {
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
        switch (Graph.valueOf(graphName)) {
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
