/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.graph;

import org.apache.tinkerpop.gremlin.process.traversal.Traversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.Property;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.structure.VertexProperty;
import org.janusgraph.core.JanusGraph;
import org.odpi.openmetadata.governanceservers.openlineage.ffdc.OpenLineageException;
import org.odpi.openmetadata.governanceservers.openlineage.ffdc.OpenLineageServerErrorCode;
import org.odpi.openmetadata.governanceservers.openlineage.model.LineageEdge;
import org.odpi.openmetadata.governanceservers.openlineage.model.LineageVertex;
import org.odpi.openmetadata.governanceservers.openlineage.model.LineageVerticesAndEdges;
import org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants;
import org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.bothE;
import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.hasLabel;
import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.inE;
import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.outE;
import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.until;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.CONNECTION;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.DATABASE;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.DATA_FILE;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.DEPLOYED_DB_SCHEMA_TYPE;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.FILE_FOLDER;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.RELATIONAL_DB_SCHEMA_TYPE;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.RELATIONAL_TABLE;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.TABULAR_SCHEMA_TYPE;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.CONDENSED_NODE_DISPLAY_NAME;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.EDGE_LABEL_CONDENSED;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.EDGE_LABEL_GLOSSARYTERM_TO_GLOSSARYTERM;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.EDGE_LABEL_SEMANTIC;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.NODE_LABEL_CONDENSED;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.PROPERTY_KEY_CONNECTION_NAME;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.PROPERTY_KEY_DATABASE_DISPLAY_NAME;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.PROPERTY_KEY_DISPLAY_NAME;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.PROPERTY_KEY_ENTITY_GUID;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.PROPERTY_KEY_ENTITY_NODE_ID;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.PROPERTY_KEY_INSTANCEPROP_DISPLAY_NAME;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.PROPERTY_KEY_PREFIX_ELEMENT;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.PROPERTY_KEY_PREFIX_VERTEX_INSTANCE_PROPERTY;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.PROPERTY_KEY_SCHEMA_DISPLAY_NAME;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.PROPERTY_KEY_SCHEMA_TYPE_DISPLAY_NAME;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.PROPERTY_KEY_TABLE_DISPLAY_NAME;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.PROPERTY_VALUE_NODE_ID_CONDENSED_DESTINATION;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.PROPERTY_VALUE_NODE_ID_CONDENSED_SOURCE;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.immutableReturnedPropertiesWhiteList;

public class LineageGraphConnectorHelper {

    private JanusGraph lineageGraph;

    public LineageGraphConnectorHelper(JanusGraph lineageGraph) {
        this.lineageGraph = lineageGraph;
    }

    /**
     * Returns a subgraph containing all root of the full graph that are connected with the queried node.
     * The queried node can be a column or table.
     *
     * @param guid       The guid of the node of which the lineage is queried of. This can be a column or a table.
     * @param edgeLabels Traversed edges
     *
     * @return a subgraph in the GraphSON format.
     */
    LineageVerticesAndEdges ultimateSource(String guid, String... edgeLabels) throws OpenLineageException {
        String methodName = "MainGraphConnector.ultimateSource";
        GraphTraversalSource g = lineageGraph.traversal();

        List<Vertex> sourcesList = g.V().has(PROPERTY_KEY_ENTITY_GUID, guid).
                until(inE(edgeLabels).count().is(0)).
                repeat(inE(edgeLabels).outV().simplePath()).
                dedup().toList();

        detectProblematicCycle(methodName, sourcesList);

        Vertex originalQueriedVertex = g.V().has(PROPERTY_KEY_ENTITY_GUID, guid).next();

        Set<LineageVertex> lineageVertices = new HashSet<>();

        Set<LineageEdge> lineageEdges = new HashSet<>();

        LineageVertex queriedVertex = abstractVertex(originalQueriedVertex);
        lineageVertices.add(queriedVertex);

        addSourceCondensation(sourcesList, lineageVertices, lineageEdges, originalQueriedVertex, queriedVertex);

        addColumnProperties(lineageVertices);

        return new LineageVerticesAndEdges(lineageVertices, lineageEdges);
    }

    /**
     * Returns a subgraph containing all leaf nodes of the full graph that are connected with the queried node.
     * The queried node can be a column or table.
     *
     * @param guid       The guid of the node of which the lineage is queried of. This can be a column or table node.
     * @param edgeLabels Traversed edges
     *
     * @return a subgraph in the GraphSON format.
     */
    LineageVerticesAndEdges ultimateDestination(String guid, String... edgeLabels) throws OpenLineageException {
        String methodName = "MainGraphConnector.ultimateDestination";
        GraphTraversalSource g = lineageGraph.traversal();

        List<Vertex> destinationsList = g.V().has(PROPERTY_KEY_ENTITY_GUID, guid).
                until(outE(edgeLabels).count().is(0)).
                repeat(outE(edgeLabels).inV().simplePath()).
                dedup().toList();

        detectProblematicCycle(methodName, destinationsList);

        Vertex originalQueriedVertex = g.V().has(PROPERTY_KEY_ENTITY_GUID, guid).next();
        LineageVertex queriedVertex = abstractVertex(originalQueriedVertex);

        Set<LineageVertex> lineageVertices = new HashSet<>();
        Set<LineageEdge> lineageEdges = new HashSet<>();

        lineageVertices.add(queriedVertex);

        addDestinationCondensation(destinationsList, lineageVertices, lineageEdges, originalQueriedVertex, queriedVertex);

        addColumnProperties(lineageVertices);

        return new LineageVerticesAndEdges(lineageVertices, lineageEdges);
    }

    /**
     * Returns a subgraph containing all paths leading from any root node to the queried node, and all of the paths
     * leading from the queried node to any leaf nodes. The queried node can be a column or table.
     *
     * @param guid       The guid of the node of which the lineage is queried of. This can be a column or a table.
     * @param edgeLabels Traversed edges
     *
     * @return a subgraph in the GraphSON format.
     */
    LineageVerticesAndEdges endToEnd(String guid, String... edgeLabels) {
        GraphTraversalSource g = lineageGraph.traversal();

        Graph endToEndGraph = (Graph)
                g.V().has(PROPERTY_KEY_ENTITY_GUID, guid).
                        union(
                                until(inE(edgeLabels).count().is(0)).
                                        repeat((Traversal) inE(edgeLabels).subgraph("subGraph").outV().simplePath()),
                                until(outE(edgeLabels).count().is(0)).
                                        repeat((Traversal) outE(edgeLabels).subgraph("subGraph").inV().simplePath())
                        ).cap("subGraph").next();

        return getLineageVerticesAndEdges(endToEndGraph);
    }

    /**
     * Returns a subgraph containing all root and leaf nodes of the full graph that are connected with the queried node.
     * The queried node can be a column or table.
     *
     * @param guid       The guid of the node of which the lineage is queried of. This can be a column or a table.
     * @param edgeLabels Traversed edges
     *
     * @return a subgraph in the GraphSON format.
     */
    LineageVerticesAndEdges sourceAndDestination(String guid, String... edgeLabels) throws OpenLineageException {
        String methodName = "MainGraphConnector.sourceAndDestination";
        GraphTraversalSource g = lineageGraph.traversal();

        List<Vertex> sourcesList = g.V().has(PROPERTY_KEY_ENTITY_GUID, guid).
                until(inE(edgeLabels).count().is(0)).
                repeat(inE(edgeLabels).outV().simplePath()).
                dedup().toList();

        List<Vertex> destinationsList = g.V().has(PROPERTY_KEY_ENTITY_GUID, guid).
                until(outE(edgeLabels).count().is(0)).
                repeat(outE(edgeLabels).inV().simplePath()).
                dedup().toList();

        detectProblematicCycle(methodName, sourcesList);
        detectProblematicCycle(methodName, destinationsList);


        Vertex originalQueriedVertex = g.V().has(PROPERTY_KEY_ENTITY_GUID, guid).next();
        LineageVertex queriedVertex = abstractVertex(originalQueriedVertex);

        Set<LineageVertex> lineageVertices = new HashSet<>();
        Set<LineageEdge> lineageEdges = new HashSet<>();
        lineageVertices.add(queriedVertex);
        addSourceCondensation(sourcesList, lineageVertices, lineageEdges, originalQueriedVertex, queriedVertex);

        addDestinationCondensation(destinationsList, lineageVertices, lineageEdges, originalQueriedVertex, queriedVertex);

        addColumnProperties(lineageVertices);

        return new LineageVerticesAndEdges(lineageVertices, lineageEdges);
    }

    /**
     * Returns a subgraph containing all columns or tables connected to the queried glossary term, as well as all
     * columns or tables connected to synonyms of the queried glossary term.
     *
     * @param guid The guid of the glossary term of which the lineage is queried of.
     *
     * @return a subgraph in the GraphSON format.
     */
    LineageVerticesAndEdges glossary(String guid) {
        GraphTraversalSource g = lineageGraph.traversal();

        Graph subGraph = (Graph)
                g.V().has(PROPERTY_KEY_ENTITY_GUID, guid)
                        .emit().
                        repeat(bothE(EDGE_LABEL_GLOSSARYTERM_TO_GLOSSARYTERM).subgraph("subGraph").simplePath().otherV())
                        .inE(EDGE_LABEL_SEMANTIC).subgraph("subGraph").outV()
                        .cap("subGraph").next();

        return getLineageVerticesAndEdges(subGraph);
    }

    /**
     * Remove all nodes which displayname does not include the provided String. Any connected edges will also be removed.
     *
     * @param lineageVerticesAndEdges The list of vertices and edges which should be filtered on displayname.
     * @param displayNameMustContain  The substring that must be part of a node's displayname in order for that node to
     *                                be returned.
     */
    void filterDisplayName(LineageVerticesAndEdges lineageVerticesAndEdges, String displayNameMustContain) {
        Set<LineageVertex> lineageVertices = lineageVerticesAndEdges.getLineageVertices();
        Set<LineageEdge> lineageEdges = lineageVerticesAndEdges.getLineageEdges();
        Set<LineageVertex> verticesToBeRemoved = new HashSet<>();
        Set<LineageEdge> edgesToBeRemoved = new HashSet<>();

        for (LineageVertex vertex : lineageVertices) {
            String nodeID = vertex.getNodeID();
            if (!vertex.getDisplayName().contains(displayNameMustContain)) {
                verticesToBeRemoved.add(vertex);
                for (LineageEdge edge : lineageEdges) {
                    if (edge.getSourceNodeID().equals(nodeID) || edge.getDestinationNodeID().equals(nodeID))
                        edgesToBeRemoved.add(edge);
                }
            }
        }
        lineageVertices.removeAll(verticesToBeRemoved);
        lineageEdges.removeAll(edgesToBeRemoved);
        lineageVerticesAndEdges.setLineageVertices(lineageVertices);
        lineageVerticesAndEdges.setLineageEdges(lineageEdges);
    }

    /**
     * Map a Tinkerpop vertex to the Open Lineage format.
     *
     * @param originalVertex The vertex to be mapped.
     *
     * @return The vertex in the Open Lineage format.
     */
    private LineageVertex abstractVertex(Vertex originalVertex) {
        String nodeType = originalVertex.label();
        String nodeID = getNodeID(originalVertex);
        LineageVertex lineageVertex = new LineageVertex(nodeID, nodeType);

        if (originalVertex.property(PROPERTY_KEY_DISPLAY_NAME).isPresent()) {
            String displayName = originalVertex.property(PROPERTY_KEY_DISPLAY_NAME).value().toString();
            lineageVertex.setDisplayName(displayName);
        }

        //Displayname key is stored inconsistently in the graphDB.
        else if (originalVertex.property(PROPERTY_KEY_INSTANCEPROP_DISPLAY_NAME).isPresent()) {
            String displayName = originalVertex.property(PROPERTY_KEY_INSTANCEPROP_DISPLAY_NAME).value().toString();
            lineageVertex.setDisplayName(displayName);
        }

        if (originalVertex.property(PROPERTY_KEY_ENTITY_GUID).isPresent()) {
            String guid = originalVertex.property(PROPERTY_KEY_ENTITY_GUID).value().toString();
            lineageVertex.setGuid(guid);
        }
        Map<String, String> properties = retrieveProperties(originalVertex);
        lineageVertex.setProperties(properties);
        return lineageVertex;
    }

    private String getNodeID(Vertex vertex) {
        String nodeID;
        if (vertex.label().equalsIgnoreCase("subProcess")) {
            nodeID = vertex.property(PROPERTY_KEY_ENTITY_NODE_ID).value().toString();
        } else {
            nodeID = vertex.property(PROPERTY_KEY_ENTITY_GUID).value().toString();
        }
        return nodeID;
    }

    /**
     * Map a Tinkerpop edge to the Open Lineage format.
     *
     * @param originalEdge The edge to be mapped
     *
     * @return The edge in the Open Lineage format.
     */
    private LineageEdge abstractEdge(Edge originalEdge) {
        String sourceNodeID = getNodeID(originalEdge.outVertex());
        String destinationNodeId =  getNodeID(originalEdge.inVertex());

        return new LineageEdge(originalEdge.label(), sourceNodeID, destinationNodeId);
    }

    /**
     * Retrieve all properties from the db and return the ones that match the whitelist. This will filter out irrelevant
     * properties that should not be returned to a UI.
     *
     * @param originalVertex
     *
     * @return
     */
    private Map<String, String> retrieveProperties(Vertex originalVertex) {
        Map<String, String> newNodeProperties = new HashMap<>();
        Iterator<VertexProperty<Object>> originalProperties = originalVertex.properties();
        while (originalProperties.hasNext()) {
            Property<Object> originalProperty = originalProperties.next();
            if (immutableReturnedPropertiesWhiteList.contains(originalProperty.key())) {
                String newPropertyKey = originalProperty.key().
                        replace(PROPERTY_KEY_PREFIX_VERTEX_INSTANCE_PROPERTY, "").
                        replace(PROPERTY_KEY_PREFIX_ELEMENT, "");

                String newPropertyValue = originalProperty.value().toString();
                newNodeProperties.put(newPropertyKey, newPropertyValue);
            }
        }
        return newNodeProperties;
    }

    /**
     * Check whether the ultimate sources/destinations of the queried node are included in a cyclic data flow.
     * This is not supported by Open lineage Services.
     *
     * @param methodName The name of the calling method.
     * @param vertexList The to be validated result of the Gremlin query.
     *
     * @throws OpenLineageException
     */
    private void detectProblematicCycle(String methodName, List<Vertex> vertexList) throws OpenLineageException {
        if (!vertexList.isEmpty())
            return;
        OpenLineageServerErrorCode errorCode = OpenLineageServerErrorCode.LINEAGE_CYCLE;
        throw new OpenLineageException(errorCode.getHTTPErrorCode(),
                this.getClass().getName(),
                methodName,
                errorCode.getFormattedErrorMessage(),
                errorCode.getSystemAction(),
                errorCode.getUserAction());
    }


    /**
     * Remove all nodes and edges from the response graph that are in between the ultimate sources and the queried node
     * and replace them by a single "condensed" node.
     *
     * @param sourcesList           The list of ultimate sources.
     * @param lineageVertices       The list of all vertices returned by the Gremlin query.
     * @param lineageEdges          The list of all edges returned by the Gremlin query.
     * @param originalQueriedVertex The vertex which guid was queried by the user as the original Tinkerpop object.
     * @param queriedVertex         The vertex which guid was queried by the user as an Open Lineage vertex object.
     */
    private void addSourceCondensation(List<Vertex> sourcesList,
                                       Set<LineageVertex> lineageVertices,
                                       Set<LineageEdge> lineageEdges,
                                       Vertex originalQueriedVertex,
                                       LineageVertex queriedVertex) {
        //Only add condensed node if there is something to condense in the first place. The gremlin query returns the queried node
        //when there isn't any.
        if (sourcesList.get(0).property(PROPERTY_KEY_ENTITY_GUID).equals(originalQueriedVertex.property(PROPERTY_KEY_ENTITY_GUID)))
            return;
        LineageVertex condensedVertex = new LineageVertex(PROPERTY_VALUE_NODE_ID_CONDENSED_SOURCE, NODE_LABEL_CONDENSED);
        condensedVertex.setDisplayName(CONDENSED_NODE_DISPLAY_NAME);
        lineageVertices.add(condensedVertex);

        for (Vertex originalVertex : sourcesList) {
            LineageVertex newVertex = abstractVertex(originalVertex);
            LineageEdge newEdge = new LineageEdge(
                    EDGE_LABEL_CONDENSED,
                    newVertex.getNodeID(),
                    condensedVertex.getNodeID()
            );
            lineageVertices.add(newVertex);
            lineageEdges.add(newEdge);
        }
        LineageEdge sourceEdge = new LineageEdge(
                EDGE_LABEL_CONDENSED,
                condensedVertex.getNodeID(),
                queriedVertex.getNodeID()
        );
        lineageEdges.add(sourceEdge);
    }

    /**
     * Remove all nodes and edges from the response graph that are in between the ultimate destinations and the queried node
     * and replace them by a single "condensed" node.
     *
     * @param destinationsList      The list of ultimate destinations.
     * @param lineageVertices       The list of all vertices returned by the Gremlin query.
     * @param lineageEdges          The list of all edges returned by the Gremlin query.
     * @param originalQueriedVertex The vertex which guid was queried by the user as the original Tinkerpop object.
     * @param queriedVertex         The vertex which guid was queried by the user as an Open Lineage vertex object.
     */
    private void addDestinationCondensation(List<Vertex> destinationsList,
                                            Set<LineageVertex> lineageVertices,
                                            Set<LineageEdge> lineageEdges,
                                            Vertex originalQueriedVertex,
                                            LineageVertex queriedVertex) {
        //Only add condensed node if there is something to condense in the first place. The gremlin query returns the queried node
        //when there isn't any.
        if (!destinationsList.get(0).property(PROPERTY_KEY_ENTITY_GUID).equals(originalQueriedVertex.property(PROPERTY_KEY_ENTITY_GUID))) {
            LineageVertex condensedDestinationVertex = new LineageVertex(PROPERTY_VALUE_NODE_ID_CONDENSED_DESTINATION, NODE_LABEL_CONDENSED);
            condensedDestinationVertex.setDisplayName(CONDENSED_NODE_DISPLAY_NAME);
            for (Vertex originalVertex : destinationsList) {
                LineageVertex newVertex = abstractVertex(originalVertex);
                LineageEdge newEdge = new LineageEdge(
                        EDGE_LABEL_CONDENSED,
                        condensedDestinationVertex.getNodeID(),
                        newVertex.getNodeID()
                );
                lineageVertices.add(newVertex);
                lineageEdges.add(newEdge);
            }
            LineageEdge destinationEdge = new LineageEdge(
                    EDGE_LABEL_CONDENSED,
                    queriedVertex.getNodeID(),
                    condensedDestinationVertex.getNodeID()
            );
            lineageVertices.add(condensedDestinationVertex);
            lineageEdges.add(destinationEdge);
        }
    }


    /**
     * Map a tinkerpop Graph object to an Open Lineage specific format.
     *
     * @param subGraph The graph to be mapped.
     *
     * @return The graph in in an Open Lineage specific format.
     */
    private LineageVerticesAndEdges getLineageVerticesAndEdges(Graph subGraph) {
        Iterator<Vertex> originalVertices = subGraph.vertices();
        Iterator<Edge> originalEdges = subGraph.edges();

        Set<LineageVertex> lineageVertices = new HashSet<>();
        Set<LineageEdge> lineageEdges = new HashSet<>();

        while (originalVertices.hasNext()) {
            LineageVertex newVertex = abstractVertex(originalVertices.next());
            lineageVertices.add(newVertex);
        }
        while (originalEdges.hasNext()) {
            LineageEdge newLineageEdge = abstractEdge(originalEdges.next());
            lineageEdges.add(newLineageEdge);
        }

        addColumnProperties(lineageVertices);

        return new LineageVerticesAndEdges(lineageVertices, lineageEdges);
    }

    private void addColumnProperties(Set<LineageVertex> lineageVertices) {
        //TODO  WIP - test and update queries
        GraphTraversalSource g = lineageGraph.traversal();
        lineageVertices.stream().filter(this::isColumnVertex).forEach(lineageVertex -> {
            Vertex graphVertex = g.V().has(PROPERTY_KEY_ENTITY_GUID, lineageVertex.getGuid()).next();

            Object vertexId = graphVertex.id();
            Iterator<Vertex> tableAsset = g.V(vertexId).emit().repeat(bothE().otherV().simplePath()).times(2).
                    or(hasLabel(RELATIONAL_TABLE), hasLabel(DATA_FILE));

            Iterator<Vertex> databaseSchemaType = g.V(vertexId).emit().repeat(bothE().inV().simplePath()).times(1).
                    or(hasLabel(RELATIONAL_DB_SCHEMA_TYPE), hasLabel(TABULAR_SCHEMA_TYPE));

            Iterator<Vertex> databaseSchema = g.V(vertexId).emit().repeat(bothE().inV().simplePath()).times(3).
                    or(hasLabel(DEPLOYED_DB_SCHEMA_TYPE), hasLabel(FILE_FOLDER));

            Iterator<Vertex> database = g.V(vertexId).emit().repeat(bothE().inV().simplePath()).times(4).
                    or(hasLabel(DATABASE), hasLabel(FILE_FOLDER));

            Iterator<Vertex> connection = g.V(vertexId).emit().repeat(bothE().inV().simplePath()).times(5).hasLabel(CONNECTION);

            Map<String, String> properties = new HashMap<>();
            if (tableAsset.hasNext()) {
                properties.put(PROPERTY_KEY_TABLE_DISPLAY_NAME,
                        tableAsset.next().property(PROPERTY_KEY_INSTANCEPROP_DISPLAY_NAME).value().toString());
            }

            if (databaseSchemaType.hasNext()) {
                properties.put(PROPERTY_KEY_SCHEMA_TYPE_DISPLAY_NAME,
                        databaseSchemaType.next().property(PROPERTY_KEY_INSTANCEPROP_DISPLAY_NAME).value().toString());
            }

            if (databaseSchema.hasNext()) {
                properties.put(PROPERTY_KEY_SCHEMA_DISPLAY_NAME,
                        databaseSchema.next().property(PROPERTY_KEY_INSTANCEPROP_DISPLAY_NAME).value().toString());
            }

            if (database.hasNext()) {
                properties.put(PROPERTY_KEY_DATABASE_DISPLAY_NAME,
                        database.next().property(PROPERTY_KEY_INSTANCEPROP_DISPLAY_NAME).value().toString());
            }

            if(connection.hasNext()) {
                properties.put(PROPERTY_KEY_CONNECTION_NAME,
                        connection.next().property(PROPERTY_KEY_INSTANCEPROP_DISPLAY_NAME).value().toString());
            }
            lineageVertex.setProperties(properties);
        });
    }

    private boolean isColumnVertex(LineageVertex lineageVertex) {
        return Arrays.asList(Constants.TABULAR_COLUMN, Constants.RELATIONAL_COLUMN).contains(lineageVertex.getNodeType());
    }
}
