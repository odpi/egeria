/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.graph;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.tinkerpop.gremlin.process.traversal.Traversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.FOLDER_HIERARCHY;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.RELATIONAL_COLUMN;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.RELATIONAL_DB_SCHEMA_TYPE;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.RELATIONAL_TABLE;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.TABULAR_COLUMN;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.TABULAR_SCHEMA_TYPE;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.CONDENSED_NODE_DISPLAY_NAME;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.DESTINATION_CONDENSATION;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.EDGE_LABEL_ANTONYM;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.EDGE_LABEL_CONDENSED;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.EDGE_LABEL_IS_A_RELATIONSHIP;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.EDGE_LABEL_RELATED_TERM;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.EDGE_LABEL_REPLACEMENT_TERM;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.EDGE_LABEL_SEMANTIC_ASSIGNMENT;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.EDGE_LABEL_SYNONYM;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.EDGE_LABEL_TRANSLATION;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.NODE_LABEL_CONDENSED;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.PROPERTY_KEY_CONNECTION_NAME;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.PROPERTY_KEY_DATABASE_DISPLAY_NAME;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.PROPERTY_KEY_DISPLAY_NAME;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.PROPERTY_KEY_ENTITY_GUID;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.PROPERTY_KEY_ENTITY_NODE_ID;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.PROPERTY_KEY_INSTANCEPROP_DISPLAY_NAME;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.PROPERTY_KEY_PATH;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.PROPERTY_KEY_PREFIX_ELEMENT;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.PROPERTY_KEY_PREFIX_VERTEX_INSTANCE_PROPERTY;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.PROPERTY_KEY_SCHEMA_DISPLAY_NAME;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.PROPERTY_KEY_SCHEMA_TYPE_DISPLAY_NAME;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.PROPERTY_KEY_TABLE_DISPLAY_NAME;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.PROPERTY_VALUE_NODE_ID_CONDENSED_DESTINATION;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.PROPERTY_VALUE_NODE_ID_CONDENSED_SOURCE;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.SOURCE_CONDENSATION;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.immutableReturnedPropertiesWhiteList;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.NODE_LABEL_SUB_PROCESS;

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
     * @return a subgraph in an Open Lineage specific format.
     */
    LineageVerticesAndEdges ultimateSource(String guid, String... edgeLabels) throws OpenLineageException {
        String methodName = "MainGraphConnector.ultimateSource";
        GraphTraversalSource g = lineageGraph.traversal();

        List<Vertex> vertexList = g.V().has(PROPERTY_KEY_ENTITY_GUID, guid).until(inE(edgeLabels).count().is(0))
                .repeat(inE(edgeLabels).outV().simplePath()).dedup().toList();

        detectProblematicCycle(methodName, vertexList);

        return getCondensedLineage(guid, g, vertexList, SOURCE_CONDENSATION);
    }

    /**
     * Returns a subgraph containing all leaf nodes of the full graph that are connected with the queried node.
     * The queried node can be a column or table.
     *
     * @param guid       The guid of the node of which the lineage is queried of. This can be a column or table node.
     * @param edgeLabels Traversed edges
     *
     * @return a subgraph in an Open Lineage specific format.
     */
    LineageVerticesAndEdges ultimateDestination(String guid, String... edgeLabels) throws OpenLineageException {
        String methodName = "MainGraphConnector.ultimateDestination";
        GraphTraversalSource g = lineageGraph.traversal();

        List<Vertex> vertexList = g.V().has(PROPERTY_KEY_ENTITY_GUID, guid).until(outE(edgeLabels).count().is(0))
                .repeat(outE(edgeLabels).inV().simplePath()).dedup().toList();

        detectProblematicCycle(methodName, vertexList);

        return getCondensedLineage(guid, g, vertexList, DESTINATION_CONDENSATION);
    }

    /**
     * Returns a subgraph containing all paths leading from any root node to the queried node, and all of the paths
     * leading from the queried node to any leaf nodes. The queried node can be a column or table.
     *
     * @param guid       The guid of the node of which the lineage is queried of. This can be a column or a table.
     * @param edgeLabels Traversed edges
     *
     * @return a subgraph in an Open Lineage specific format.
     */
    LineageVerticesAndEdges endToEnd(String guid, boolean includeProcesses, String... edgeLabels) {
        GraphTraversalSource g = lineageGraph.traversal();

        Graph endToEndGraph = (Graph)
                g.V().has(PROPERTY_KEY_ENTITY_GUID, guid).
                        union(
                                until(inE(edgeLabels).count().is(0)).
                                        repeat((Traversal) inE(edgeLabels).subgraph("subGraph").outV().simplePath()),
                                until(outE(edgeLabels).count().is(0)).
                                        repeat((Traversal) outE(edgeLabels).subgraph("subGraph").inV().simplePath())
                        ).cap("subGraph").next();

        return getLineageVerticesAndEdges(endToEndGraph, includeProcesses);
    }

    /**
     * Returns a subgraph containing all root and leaf nodes of the full graph that are connected with the queried node.
     * The queried node can be a column or table.
     *
     * @param guid       The guid of the node of which the lineage is queried of. This can be a column or a table.
     * @param edgeLabels Traversed edges
     *
     * @return a subgraph in an Open Lineage specific format
     */
    LineageVerticesAndEdges sourceAndDestination(String guid, String... edgeLabels) throws OpenLineageException {
        LineageVerticesAndEdges ultimateSourceResponse = ultimateSource(guid, edgeLabels);
        LineageVerticesAndEdges ultimateDestinationResponse = ultimateDestination(guid, edgeLabels);

        Set<LineageVertex> sourceAndDestinationVertices = Stream.concat(ultimateSourceResponse.getLineageVertices().stream(),
                ultimateDestinationResponse.getLineageVertices().stream()).collect(Collectors.toSet());

        Set<LineageEdge> sourceAndDestinationEdges = Stream.concat(ultimateSourceResponse.getLineageEdges().stream(),
                ultimateDestinationResponse.getLineageEdges().stream()).collect(Collectors.toSet());

        return new LineageVerticesAndEdges(sourceAndDestinationVertices, sourceAndDestinationEdges);
    }

    /**
     * Returns a subgraph containing all columns or tables connected to the queried glossary term, as well as all
     * columns or tables connected to synonyms of the queried glossary term.
     *
     * @param guid The guid of the glossary term of which the lineage is queried of.
     *
     * @return a subgraph in an Open Lineage specific format.
     */
    LineageVerticesAndEdges glossary(String guid, boolean includeProcesses) {
        GraphTraversalSource g = lineageGraph.traversal();

        Graph subGraph = (Graph) g.V().has(PROPERTY_KEY_ENTITY_GUID, guid).bothE(EDGE_LABEL_SEMANTIC_ASSIGNMENT,
                EDGE_LABEL_RELATED_TERM, EDGE_LABEL_SYNONYM, EDGE_LABEL_ANTONYM, EDGE_LABEL_REPLACEMENT_TERM,
                EDGE_LABEL_TRANSLATION, EDGE_LABEL_IS_A_RELATIONSHIP).subgraph("s").cap("s").next();

        return getLineageVerticesAndEdges(subGraph, includeProcesses);
    }

    /**
     * Remove all nodes which displayname does not include the provided String. Any connected edges will also be removed.
     *
     * @param lineageVerticesAndEdges The list of vertices and edges which should be filtered on displayname.
     * @param displayNameMustContain  The substring that must be part of a node's displayname in order for that node to be returned.
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
     * * Returns a subgraph containing all root and leaf nodes of the full graph that are connected with the queried node.
     * * The queried node can be a column or table.
     *
     * @param guid             the guid of the queried node
     * @param g                graph traversal object
     * @param vertexList       list of vertexes to be condensed
     * @param condensationType the type of the condensation
     *
     * @return the subgraph in an Open Lineage specific format
     */
    private LineageVerticesAndEdges getCondensedLineage(String guid, GraphTraversalSource g, List<Vertex> vertexList, String condensationType) {
        Vertex originalQueriedVertex = g.V().has(PROPERTY_KEY_ENTITY_GUID, guid).next();

        Set<LineageVertex> lineageVertices = new HashSet<>();

        Set<LineageEdge> lineageEdges = new HashSet<>();

        LineageVertex queriedVertex = abstractVertex(originalQueriedVertex);
        lineageVertices.add(queriedVertex);

        addCondensation(vertexList, lineageVertices, lineageEdges, originalQueriedVertex, queriedVertex, condensationType);

        addColumnProperties(lineageVertices);

        return new LineageVerticesAndEdges(lineageVertices, lineageEdges);
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
        if (vertex.label().equalsIgnoreCase(NODE_LABEL_SUB_PROCESS)) {
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
        String destinationNodeId = getNodeID(originalEdge.inVertex());

        return new LineageEdge(originalEdge.label(), sourceNodeID, destinationNodeId);
    }

    /**
     * Retrieve all properties of the vertex from the db and return the ones that match the whitelist. This will filter out irrelevant
     * properties that should not be returned to a UI.
     *
     * @param vertex the vertex to de mapped
     *
     * @return the filtered properties of the vertex
     */
    private Map<String, String> retrieveProperties(Vertex vertex) {
        Map<String, String> newNodeProperties = new HashMap<>();
        Iterator<VertexProperty<Object>> originalProperties = vertex.properties();
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
     * @throws OpenLineageException encoded exception from the server
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
     * Remove all nodes and edges from the response graph that are in between the sources and the queried node
     * and replace them by a single "condensed" node.
     *
     * @param vertexList            The list of vertexes to be condensed.
     * @param lineageVertices       The list of all vertices returned by the Gremlin query.
     * @param lineageEdges          The list of all edges returned by the Gremlin query.
     * @param originalQueriedVertex The vertex which guid was queried by the user as the original Tinkerpop object.
     * @param queriedVertex         The vertex which guid was queried by the user as an Open Lineage vertex object.
     * @param condensationType      the type of the condensed node
     */
    private void addCondensation(List<Vertex> vertexList,
                                 Set<LineageVertex> lineageVertices,
                                 Set<LineageEdge> lineageEdges,
                                 Vertex originalQueriedVertex,
                                 LineageVertex queriedVertex,
                                 String condensationType) {
        //Only add condensed node if there is something to condense in the first place. The gremlin query returns the queried node
        //when there isn't any.
        if (vertexList.get(0).property(PROPERTY_KEY_ENTITY_GUID).equals(originalQueriedVertex.property(PROPERTY_KEY_ENTITY_GUID))) {
            return;
        }

        LineageVertex condensedVertex = new LineageVertex(getCondensedNodeId(condensationType), NODE_LABEL_CONDENSED);
        condensedVertex.setDisplayName(CONDENSED_NODE_DISPLAY_NAME);
        lineageVertices.add(condensedVertex);

        for (Vertex originalVertex : vertexList) {
            LineageVertex newVertex = abstractVertex(originalVertex);

            LineageEdge newEdge = getEdge(condensedVertex, newVertex, condensationType);
            lineageVertices.add(newVertex);
            lineageEdges.add(newEdge);
        }
        LineageEdge sourceEdge = getEdge(queriedVertex, condensedVertex, condensationType);
        lineageEdges.add(sourceEdge);
    }

    /**
     * Map a tinkerpop Graph object to an Open Lineage specific format.
     *
     * @param subGraph The graph to be mapped.
     *
     * @return The graph in an Open Lineage specific format.
     */
    private LineageVerticesAndEdges getLineageVerticesAndEdges(Graph subGraph, boolean includeProcesses) {
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

        if (!includeProcesses) {
            Set<LineageVertex> verticesToRemove = lineageVertices.stream()
                    .filter(this::isSubProcess)
                    .collect(Collectors.toSet());
            Set<String> verticesToRemoveIDs = verticesToRemove.stream().map(LineageVertex::getNodeID).collect(Collectors.toSet());
            Set<LineageEdge> edgesToRemove = lineageEdges.stream().filter(edge ->
                    isInVertexesToRemove(verticesToRemoveIDs, edge)).collect(Collectors.toSet());
            List<LineageEdge> edgesToReplaceProcesses = createEdgesThatReplaceProcesses(lineageEdges, verticesToRemoveIDs);
            lineageEdges.addAll(edgesToReplaceProcesses);
            lineageEdges.removeAll(edgesToRemove);
            lineageVertices.removeAll(verticesToRemove);
        }
        addColumnProperties(lineageVertices);

        return new LineageVerticesAndEdges(lineageVertices, lineageEdges);
    }

    private List<LineageEdge> createEdgesThatReplaceProcesses(Set<LineageEdge> lineageEdges, Set<String> verticesToRemoveNames) {
        List<LineageEdge> edgesToReplaceProcesses = new ArrayList<>();
        for (String vertexName : verticesToRemoveNames) {
            for (LineageEdge edge : lineageEdges) {
                if (edge.getDestinationNodeID().equalsIgnoreCase(vertexName)) {
                    for (LineageEdge destinationEdge : lineageEdges) {
                        if (destinationEdge.getSourceNodeID().equalsIgnoreCase(vertexName)) {
                            edgesToReplaceProcesses.add(new LineageEdge(EDGE_LABEL_CONDENSED,
                                    edge.getSourceNodeID(), destinationEdge.getDestinationNodeID()));
                        }
                    }
                }
            }
        }
        return edgesToReplaceProcesses;
    }

    private boolean isSubProcess(LineageVertex vertex) {
        return vertex.getNodeType().equalsIgnoreCase(NODE_LABEL_SUB_PROCESS);
    }

    private boolean isInVertexesToRemove(Set<String> verticesToRemoveNames, LineageEdge edge) {
        return verticesToRemoveNames.contains(edge.getSourceNodeID()) || verticesToRemoveNames.contains(edge.getDestinationNodeID());
    }

    private void addColumnProperties(Set<LineageVertex> lineageVertices) {
        if (CollectionUtils.isEmpty(lineageVertices)) {
            return;
        }

        GraphTraversalSource g = lineageGraph.traversal();
        lineageVertices.stream().filter(this::isColumnVertex).forEach(lineageVertex -> {
            Vertex graphVertex = g.V().has(PROPERTY_KEY_ENTITY_GUID, lineageVertex.getGuid()).next();
            Object vertexId = graphVertex.id();
            Map<String, String> properties = new HashMap<>();

            switch (lineageVertex.getNodeType()) {
                case TABULAR_COLUMN:
                    properties = getDataFileProperties(g, vertexId);
                    break;
                case RELATIONAL_COLUMN:
                    properties = getRelationalTableProperties(g, vertexId);
                    break;
            }
            lineageVertex.setProperties(properties);
        });
    }

    private Map<String, String> getRelationalTableProperties(GraphTraversalSource g, Object vertexId) {
        Map<String, String> properties = new HashMap<>();

        Iterator<Vertex> tableAsset = g.V(vertexId).emit().repeat(bothE().otherV().simplePath()).times(1).or(hasLabel(RELATIONAL_TABLE));
        if (tableAsset.hasNext()) {
            properties.put(PROPERTY_KEY_TABLE_DISPLAY_NAME, tableAsset.next().property(PROPERTY_KEY_INSTANCEPROP_DISPLAY_NAME).value().toString());
        }

        Iterator<Vertex> relationalDBSchemaType =
                g.V(vertexId).emit().repeat(bothE().inV().simplePath()).times(2).or(hasLabel(RELATIONAL_DB_SCHEMA_TYPE));
        if (relationalDBSchemaType.hasNext()) {
            properties.put(PROPERTY_KEY_SCHEMA_TYPE_DISPLAY_NAME,
                    relationalDBSchemaType.next().property(PROPERTY_KEY_INSTANCEPROP_DISPLAY_NAME).value().toString());
        }

        Iterator<Vertex> deployedDatabaseSchema =
                g.V(vertexId).emit().repeat(bothE().inV().simplePath()).times(3).or(hasLabel(DEPLOYED_DB_SCHEMA_TYPE));
        if (deployedDatabaseSchema.hasNext()) {
            properties.put(PROPERTY_KEY_SCHEMA_DISPLAY_NAME,
                    deployedDatabaseSchema.next().property(PROPERTY_KEY_INSTANCEPROP_DISPLAY_NAME).value().toString());
        }

        Iterator<Vertex> database = g.V(vertexId).emit().repeat(bothE().inV().simplePath()).times(4).or(hasLabel(DATABASE));
        if (database.hasNext()) {
            properties.put(PROPERTY_KEY_DATABASE_DISPLAY_NAME,
                    database.next().property(PROPERTY_KEY_INSTANCEPROP_DISPLAY_NAME).value().toString());
        }

        Iterator<Vertex> connection = g.V(vertexId).emit().repeat(bothE().inV().simplePath()).times(5).hasLabel(CONNECTION);
        if (connection.hasNext()) {
            properties.put(PROPERTY_KEY_CONNECTION_NAME, connection.next().property(PROPERTY_KEY_INSTANCEPROP_DISPLAY_NAME).value().toString());
        }

        return properties;
    }

    private Map<String, String> getDataFileProperties(GraphTraversalSource g, Object vertexId) {
        Map<String, String> properties = new HashMap<>();

        Iterator<Vertex> tabularSchemaType = g.V(vertexId).emit().repeat(bothE().inV().simplePath()).times(1).or(hasLabel(TABULAR_SCHEMA_TYPE));
        if (tabularSchemaType.hasNext()) {
            properties.put(PROPERTY_KEY_SCHEMA_TYPE_DISPLAY_NAME,
                    tabularSchemaType.next().property(PROPERTY_KEY_INSTANCEPROP_DISPLAY_NAME).value().toString());
        }

        Iterator<Vertex> dataFileAsset = g.V(vertexId).emit().repeat(bothE().otherV().simplePath()).times(2).or(hasLabel(DATA_FILE));
        if (dataFileAsset.hasNext()) {
            Vertex dataFileVertex = dataFileAsset.next();
            properties.put(PROPERTY_KEY_TABLE_DISPLAY_NAME, dataFileVertex.property(PROPERTY_KEY_INSTANCEPROP_DISPLAY_NAME).value().toString());

            List<Vertex> folderVertices = getFolderVertices(g, dataFileVertex);
            if (CollectionUtils.isEmpty(folderVertices)) {
                return properties;
            }
            Object lastFolderVertexId = folderVertices.get(folderVertices.size() - 1).id();

            Iterator<Vertex> connection = g.V(lastFolderVertexId).emit().repeat(bothE().otherV().simplePath()).times(1).or(hasLabel(CONNECTION));
            if (connection.hasNext()) {
                properties.put(PROPERTY_KEY_CONNECTION_NAME, connection.next().property(PROPERTY_KEY_INSTANCEPROP_DISPLAY_NAME).value().toString());
            }

            properties.put(PROPERTY_KEY_PATH, String.join("/", getFoldersPath(folderVertices)));
        }

        return properties;
    }

    private String getFoldersPath(List<Vertex> folderVertices) {
        Collections.reverse(folderVertices);
        return folderVertices.stream().map(folderVertex -> folderVertex.property(PROPERTY_KEY_INSTANCEPROP_DISPLAY_NAME).value().toString())
                .collect(Collectors.joining("/"));
    }

    private List<Vertex> getFolderVertices(GraphTraversalSource g, Vertex dataFileAsset) {
        GraphTraversal<Vertex, Vertex> fileFolders =
                g.V(dataFileAsset.id()).emit().repeat(bothE().otherV().simplePath()).until(outE(FOLDER_HIERARCHY).count().is(0)).or(hasLabel(FILE_FOLDER));
        List<Vertex> folderVertices = new ArrayList<>();
        while (fileFolders.hasNext()) {
            folderVertices.add(fileFolders.next());
        }
        return folderVertices;
    }

    private boolean isColumnVertex(LineageVertex lineageVertex) {
        return Arrays.asList(Constants.TABULAR_COLUMN, Constants.RELATIONAL_COLUMN).contains(lineageVertex.getNodeType());
    }

    private String getCondensedNodeId(String condensationType) {
        if (SOURCE_CONDENSATION.equalsIgnoreCase(condensationType)) {
            return PROPERTY_VALUE_NODE_ID_CONDENSED_SOURCE;
        } else {
            return PROPERTY_VALUE_NODE_ID_CONDENSED_DESTINATION;
        }
    }

    private LineageEdge getEdge(LineageVertex firstVertex, LineageVertex secondVertex, String condensationType) {
        if (SOURCE_CONDENSATION.equalsIgnoreCase(condensationType)) {
            return new LineageEdge(EDGE_LABEL_CONDENSED, firstVertex.getNodeID(), secondVertex.getNodeID());
        } else {
            return new LineageEdge(EDGE_LABEL_CONDENSED, secondVertex.getNodeID(), firstVertex.getNodeID());
        }

    }
}
