/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.maingraph;

import org.apache.tinkerpop.gremlin.process.traversal.Traversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.structure.VertexProperty;
import org.apache.tinkerpop.gremlin.structure.io.IoCore;
import org.apache.tinkerpop.gremlin.structure.io.graphson.GraphSONMapper;
import org.apache.tinkerpop.gremlin.structure.io.graphson.GraphSONWriter;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph;
import org.janusgraph.core.JanusGraph;
import org.janusgraph.graphdb.tinkerpop.io.graphson.JanusGraphSONModuleV2d0;
import org.odpi.openmetadata.accessservices.assetlineage.model.event.LineageEvent;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.properties.ConnectionProperties;
import org.odpi.openmetadata.governanceservers.openlineage.MainGraphStore;
import org.odpi.openmetadata.governanceservers.openlineage.OpenLineageConnectorBase;
import org.odpi.openmetadata.governanceservers.openlineage.model.GraphName;
import org.odpi.openmetadata.governanceservers.openlineage.model.Scope;
import org.odpi.openmetadata.governanceservers.openlineage.model.View;
import org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.berkeleydb.BerkeleyBufferJanusFactory;
import org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.berkeleydb.BerkeleyJanusFactory;
import org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.buffergraph.GraphVertexMapper;
import org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;

import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.*;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.*;

public class MainGraphConnector extends OpenLineageConnectorBase implements MainGraphStore {

    private static final Logger log = LoggerFactory.getLogger(MainGraphConnector.class);
    private JanusGraph bufferGraph;
    private GraphVertexMapper graphVertexMapper = new GraphVertexMapper();
    private JanusGraph mainGraph;
    private JanusGraph historyGraph;

    private JanusGraph mockGraph;

    /**
     * Initialize the connector.
     *
     * @param connectorInstanceId  - unique id for the connector instance - useful for messages etc
     * @param connectionProperties - POJO for the configuration used to create the connector.
     */
    @Override
    public void initialize(String connectorInstanceId, ConnectionProperties connectionProperties) {

        super.initialize(connectorInstanceId, connectionProperties);
        initializeGraphDB();
    }

    /**
     * Indicates that the connector is completely configured and can begin processing.
     *
     * @throws ConnectorCheckedException there is a problem within the connector.
     */
    public void start() throws ConnectorCheckedException
    {
        super.start();
    }


    private void initializeGraphDB(){

        String graphDB = connectionProperties.getConfigurationProperties().get("graphDB").toString();
        switch (graphDB){
            case "berkeleydb":
                try {
                    this.mainGraph = BerkeleyJanusFactory.openMainGraph();
                    this.bufferGraph = BerkeleyBufferJanusFactory.openBufferGraph();
                    this.historyGraph = BerkeleyJanusFactory.openHistoryGraph();
                    this.mockGraph = BerkeleyJanusFactory.openMockGraph();
                } catch (Exception e) {
                    log.error("{} Could not open graph database", "JanusConnector"); //TODO  elaborate error
                }
                break;
            case "cassandra":
                FactoryForTesting factoryForTesting = new FactoryForTesting();
                this.mainGraph = factoryForTesting.openBufferGraph(connectionProperties);
                break;

            default:
                break;
        }
    }

    @Override
    public void addEntity(LineageEvent lineageEvent) {

    }

    /**
     * Returns a lineage subgraph.
     *
     * @param graphName    main, buffer, mock, history.
     * @param scope source-and-destination, end-to-end, ultimate-source, ultimate-destination, glossary.
     * @param view        The view queried by the user: hostview, tableview, columnview.
     * @param guid         The guid of the node of which the lineage is queried from.
     * @return A subgraph containing all relevant paths, in graphSON format.
     */
    public String lineage(String graphName, Scope scope, View view, String guid) {
        String response = "";

        Graph graph = getJanusGraph(graphName);
        switch (scope) {
            case SOURCE_AND_DESTINATION:
                response = sourceAndDestination(graph, view, guid);
                break;
            case END_TO_END:
                response = endToEnd(graph, view, guid);
                break;
            case ULTIMATE_SOURCE:
                response = ultimateSource(graph, view, guid);
                break;
            case ULTIMATE_DESTINATION:
                response = ultimateDestination(graph, view, guid);
                break;
            case GLOSSARY:
                response = glossary(graph, guid);
                break;
            default:
                log.error(scope + " is not a valid lineage query");
        }
        return response;
    }

    /**
     * Returns a subgraph containing all root and leaf nodes of the full graph that are connected with the queried node.
     * The queried node can be a column or table.
     *
     * @param graph MAIN, BUFFER, MOCK, HISTORY.
     * @param view The view queried by the user: tableview, columnview.
     * @param guid  The guid of the node of which the lineage is queried of. This can be a column or a table.
     * @return a subgraph in the GraphSON format.
     */
    private String sourceAndDestination(Graph graph, View view, String guid) {
        GraphTraversalSource g = graph.traversal();
        String edgeLabel = getEdgeLabel(view);

        List<Vertex> sourcesList = g.V().has(GraphConstants.PROPERTY_KEY_ENTITY_GUID, guid).
                until(inE(edgeLabel).count().is(0)).
                repeat(inE(edgeLabel).outV()).dedup().toList();

        List<Vertex> destinationsList = g.V().has(GraphConstants.PROPERTY_KEY_ENTITY_GUID, guid).
                until(outE(edgeLabel).count().is(0)).
                repeat(outE(edgeLabel).inV()).dedup().toList();

        Vertex originalQueriedVertex = g.V().has(GraphConstants.PROPERTY_KEY_ENTITY_GUID, guid).next();

        Graph responseGraph = TinkerGraph.open();
        g = responseGraph.traversal();

        Vertex queriedVertex = g.addV(originalQueriedVertex.label()).next();
        copyVertexProperties(originalQueriedVertex, queriedVertex);

        addSourceCondensationNode(g, sourcesList, originalQueriedVertex, queriedVertex);

        addDestinationCondensationNode(g, destinationsList, originalQueriedVertex, queriedVertex);
        return janusGraphToGraphson(responseGraph);
    }

    /**
     * Returns a subgraph containing all paths leading from any root node to the queried node, and all of the paths
     * leading from the queried node to any leaf nodes. The queried node can be a column or table.
     *
     * @param graph MAIN, BUFFER, MOCK, HISTORY.
     * @param view The view queried by the user: tableview, columnview.
     * @param guid  The guid of the node of which the lineage is queried of. This can be a column or a table.
     * @return a subgraph in the GraphSON format.
     */
    private String endToEnd(Graph graph, View view, String guid) {
        GraphTraversalSource g = graph.traversal();
        String edgeLabel = getEdgeLabel(view);

        Graph endToEndGraph = (Graph)
                g.V().has(PROPERTY_KEY_ENTITY_GUID, guid).
                        union(
                                until(inE(edgeLabel).count().is(0)).
                                        repeat((Traversal)inE(edgeLabel).subgraph("subGraph").outV()),
                                until(outE(edgeLabel).count().is(0)).
                                        repeat((Traversal)outE(edgeLabel).subgraph("subGraph").inV())
                        ).cap("subGraph").next();

        return janusGraphToGraphson(endToEndGraph);
    }

    /**
     * Returns a subgraph containing all root of the full graph that are connected with the queried node.
     * The queried node can be a column or table.
     *
     * @param graph MAIN, BUFFER, MOCK, HISTORY.
     * @param view The view queried by the user: tableview, columnview.
     * @param guid  The guid of the node of which the lineage is queried of. This can be a column or a table.
     * @return a subgraph in the GraphSON format.
     */
    private String ultimateSource(Graph graph, View view, String guid) {
        GraphTraversalSource g = graph.traversal();
        String edgeLabel = getEdgeLabel(view);

        List<Vertex> sourcesList = g.V().has(GraphConstants.PROPERTY_KEY_ENTITY_GUID, guid).
                until(inE(edgeLabel).count().is(0)).
                repeat(inE(edgeLabel).outV()).dedup().toList();

        Vertex originalQueriedVertex = g.V().has(GraphConstants.PROPERTY_KEY_ENTITY_GUID, guid).next();

        Graph responseGraph = TinkerGraph.open();
        g = responseGraph.traversal();

        Vertex queriedVertex = g.addV(originalQueriedVertex.label()).next();
        copyVertexProperties(originalQueriedVertex, queriedVertex);

        addSourceCondensationNode(g, sourcesList, originalQueriedVertex, queriedVertex);

        return janusGraphToGraphson(responseGraph);
    }

    /**
     * Returns a subgraph containing all leaf nodes of the full graph that are connected with the queried node.
     * The queried node can be a column or table.
     *
     * @param graph MAIN, BUFFER, MOCK, HISTORY.
     * @param view The view queried by the user: tableview, columnview.
     * @param guid  The guid of the node of which the lineage is queried of. This can be a column or table node.
     * @return a subgraph in the GraphSON format.
     */
    private String ultimateDestination(Graph graph, View view, String guid) {
        GraphTraversalSource g = graph.traversal();
        String edgeLabel = getEdgeLabel(view);
        List<Vertex> destinationsList = g.V().has(GraphConstants.PROPERTY_KEY_ENTITY_GUID, guid).
                until(outE(edgeLabel).count().is(0)).
                repeat(outE(edgeLabel).inV()).dedup().toList();

        Vertex originalQueriedVertex = g.V().has(GraphConstants.PROPERTY_KEY_ENTITY_GUID, guid).next();

        Graph responseGraph = TinkerGraph.open();
        g = responseGraph.traversal();

        Vertex queriedVertex = g.addV(originalQueriedVertex.label()).next();
        copyVertexProperties(originalQueriedVertex, queriedVertex);

        addDestinationCondensationNode(g, destinationsList, originalQueriedVertex, queriedVertex);

        return janusGraphToGraphson(responseGraph);
    }

    /**
     * In order not to clutter the user's screen with too many information, only the source nodes and the queried node
     * are returned, the nodes in between are abstracted by means of a node labeled "condensed". If there are no
     * ultimate sources, the gremlin query will return the originally queried node. Therefore, this method checks
     * whether the originally queried node is being returned by the Gremlin query. Only if the queried node has any
     * ultimate sources, should the condensation node be created.
     *
     *
     * @param g The GraphTraversal of the graph in which the condensation node will be added.
     * @param sourcesList The list of ultimate sources for the queried node.
     * @param originalQueriedVertex The vertex originally queried by the user.
     * @param queriedVertex A copy of originalQueriedVertex that is present in the response graph instead of the
     *                      original graph.
     */
    private void addSourceCondensationNode(GraphTraversalSource g, List<Vertex> sourcesList, Vertex originalQueriedVertex, Vertex queriedVertex) {
        if (!sourcesList.get(0).property(PROPERTY_KEY_ENTITY_GUID).equals(originalQueriedVertex.property(PROPERTY_KEY_ENTITY_GUID))) {
            Vertex condensation = g.addV(NODE_LABEL_CONDENSED).next();
            condensation.addEdge(EDGE_LABEL_CONDENSED, queriedVertex);

            for (Vertex originalVertex : sourcesList) {
                Vertex vertex = g.addV(originalVertex.label()).next();
                copyVertexProperties(originalVertex, vertex);
                vertex.addEdge(EDGE_LABEL_CONDENSED, condensation);
            }
        }
    }

    /**
     * In order not to clutter the user's screen with too many information, only the destination nodes and the queried
     * node are returned, the nodes in between are abstracted by means of a node labeled "condensed". If there are no
     * ultimate destinations, the gremlin query will return the originally queried node. Therefore, this method checks
     * whether the originally queried node is being returned by the Gremlin query. Only if the queried node has any
     * ultimate destination, should the condensation node be created.
     *
     *
     * @param g The GraphTraversal of the graph in which the condensation node will be added.
     * @param destinationsList The list of ultimate sources for the queried node.
     * @param originalQueriedVertex The vertex originally queried by the user.
     * @param queriedVertex A copy of originalQueriedVertex that is present in the response graph instead of the
     *                      original graph.
     */
    private void addDestinationCondensationNode(GraphTraversalSource g, List<Vertex> destinationsList, Vertex originalQueriedVertex, Vertex queriedVertex) {
        if (!destinationsList.get(0).property(PROPERTY_KEY_ENTITY_GUID).equals(originalQueriedVertex.property(PROPERTY_KEY_ENTITY_GUID))) {
            Vertex condensation = g.addV(NODE_LABEL_CONDENSED).next();
            queriedVertex.addEdge(EDGE_LABEL_CONDENSED, condensation);
            for (Vertex originalVertex : destinationsList) {
                Vertex vertex = g.addV(originalVertex.label()).next();
                copyVertexProperties(originalVertex, vertex);
                condensation.addEdge(EDGE_LABEL_CONDENSED, vertex);
            }
        }
    }

    /**
     * Returns a subgraph containing all columns or tables connected to the queried glossary term, as well as all
     * columns or tables connected to synonyms of the queried glossary term.
     *
     * @param graph MAIN, BUFFER, MOCK, HISTORY.
     * @param guid  The guid of the glossary term of which the lineage is queried of.
     * @return a subgraph in the GraphSON format.
     */
    private String glossary(Graph graph, String guid) {
        GraphTraversalSource g = graph.traversal();

        Graph subGraph = (Graph)
                g.V().has(GraphConstants.PROPERTY_KEY_ENTITY_GUID, guid)
                        .emit().
                        repeat(bothE(EDGE_LABEL_GLOSSARYTERM_TO_GLOSSARYTERM).subgraph("subGraph").simplePath().bothV())
                        .inE(EDGE_LABEL_SEMANTIC).subgraph("subGraph").outV()
                        .cap("subGraph").next();
        return janusGraphToGraphson(subGraph);
    }

    /**
     * Retrieve the label of the edges that are to be traversed with the gremlin query.
     *
     * @param view The view queried by the user: table-view, column-view.
     * @return The label of the edges that are to be traversed with the gremlin query.
     */
    private String getEdgeLabel(View view) {
        String edgeLabel = "";
        switch (view) {
            case TABLE_VIEW:
                edgeLabel = EDGE_LABEL_TABLE_AND_PROCESS;
                break;
            case COLUMN_VIEW:
                edgeLabel = EDGE_LABEL_COLUMN_AND_PROCESS;
                break;
            default:
                log.error(view + " is not a valid lineage view");
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
     * @param graphName MAIN, BUFFER, MOCK, HISTORY.
     */
    public void dumpGraph(String graphName) {
        JanusGraph graph = getJanusGraph(graphName);
        try {
            graph.io(IoCore.graphml()).writeGraph("graph-" + graphName + ".graphml");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * Return an entire graph, in GraphSon format.
     *
     * @param graphName MAIN, BUFFER, MOCK, HISTORY.
     * @return The queried graph, in graphSON format.
     */
    public String exportGraph(String graphName) {
        JanusGraph graph = getJanusGraph(graphName);
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
     * @param graphNameText The name of the queried graph.
     * @return The Graph object.
     */
    private JanusGraph getJanusGraph(String graphNameText) {
        JanusGraph graph = null;
        GraphName graphName = GraphName.fromString(graphNameText);
        switch (graphName) {
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
                log.error(graphNameText + " is not a valid graph");
        }
        return graph;
    }

    public Object getMainGraph() {
        return mainGraph;
    }


}
