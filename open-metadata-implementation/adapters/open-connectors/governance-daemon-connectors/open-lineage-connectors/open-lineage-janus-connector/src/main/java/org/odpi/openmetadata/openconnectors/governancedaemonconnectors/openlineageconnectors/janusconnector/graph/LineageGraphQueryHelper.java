/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.graph;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.tinkerpop.gremlin.process.traversal.P;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Direction;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.Property;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.structure.VertexProperty;
import org.odpi.openmetadata.governanceservers.openlineage.model.LineageEdge;
import org.odpi.openmetadata.governanceservers.openlineage.model.LineageVertex;
import org.odpi.openmetadata.governanceservers.openlineage.model.LineageVerticesAndEdges;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.bothE;
import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.hasLabel;
import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.inE;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.*;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.EDGE_LABEL_CLASSIFICATION;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.EDGE_LABEL_CONDENSED;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.NODE_LABEL_SUB_PROCESS;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.PROCESS_NODES;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.PROPERTY_KEY_DISPLAY_NAME;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.PROPERTY_KEY_ENTITY_GUID;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.PROPERTY_KEY_INSTANCEPROP_DISPLAY_NAME;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.PROPERTY_KEY_INSTANCE_PROP_ADDITIONAL_PROPERTIES;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.PROPERTY_KEY_LABEL;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.PROPERTY_KEY_PREFIX_ELEMENT;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.PROPERTY_KEY_PREFIX_VERTEX_INSTANCE_PROPERTY;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.PROPERTY_KEY_PROCESS_GUID;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.PROPERTY_NAME_INSTANCEPROP_QUALIFIED_NAME;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.immutableReturnedPropertiesWhiteList;

public class LineageGraphQueryHelper {

    private final GraphHelper graphHelper;
    private static final Logger log = LoggerFactory.getLogger(LineageGraphQueryHelper.class);

    public LineageGraphQueryHelper(GraphHelper graphHelper) {
        this.graphHelper = graphHelper;
    }

    /**
     * Map a tinkerpop Graph object to an Open Lineage specific format.
     *
     * @param subGraph The graph to be mapped.
     * @return The graph in an Open Lineage specific format.
     */
    public LineageVerticesAndEdges getLineageVerticesAndEdges(Graph subGraph, boolean includeProcesses) {
        Set<LineageVertex> lineageVertices = getLineageVertices(subGraph);
        Set<LineageEdge> lineageEdges = getLineageEdges(subGraph);
        condenseProcesses(includeProcesses, lineageVertices, lineageEdges);
        return new LineageVerticesAndEdges(lineageVertices, lineageEdges);
    }

    public Set<LineageVertex> getLineageVertices(Graph subGraph) {
        Iterator<Vertex> originalVertices = subGraph.vertices();
        Set<LineageVertex> lineageVertices = new LinkedHashSet<>();
        while (originalVertices.hasNext()) {
            LineageVertex newVertex = abstractVertex(originalVertices.next());
            lineageVertices.add(newVertex);
        }
        return lineageVertices;
    }


    /**
     * Convert a list of vertices from the janusgraph model to the list of vertices model used by egeria
     *
     * @param vertexList vertices to transform
     * @return list of lineage vertices converted
     */
    public Set<LineageVertex> getLineageVertices(List<Vertex> vertexList) {
        if (CollectionUtils.isNotEmpty(vertexList)) {
            return vertexList.stream().map(this::abstractVertex).collect(Collectors.toSet());
        }
        return Collections.emptySet();
    }

    public Set<LineageEdge> getLineageEdges(Graph subGraph) {
        Iterator<Edge> originalEdges = subGraph.edges();
        Set<LineageEdge> lineageEdges = new HashSet<>();
        while (originalEdges.hasNext()) {
            Edge next = originalEdges.next();
            LineageEdge newLineageEdge = new LineageEdge(getEdgeID(next), next.label(), getNodeID(next.outVertex()), getNodeID(next.inVertex()));
            lineageEdges.add(newLineageEdge);
        }
        return lineageEdges;
    }

    private String getEdgeID(Edge edge) {
        String edgeID;
        if (edge.property("edge--guid").isPresent()) {
            edgeID = edge.property("edge--guid").value().toString();
        } else {
            edgeID = edge.id().toString();
        }
        return edgeID;
    }

    private void condenseProcesses(boolean includeProcesses, Set<LineageVertex> lineageVertices, Set<LineageEdge> lineageEdges) {
        if (!includeProcesses) {
            Set<LineageVertex> verticesToRemove = lineageVertices.stream()
                    .filter(this::isProcessOrSubprocessNode)
                    .collect(Collectors.toSet());
            Set<String> verticesToRemoveIDs = verticesToRemove.stream().map(LineageVertex::getNodeID).collect(Collectors.toSet());
            Set<LineageEdge> edgesToRemove = lineageEdges.stream().filter(edge ->
                    isInVertexesToRemove(verticesToRemoveIDs, edge)).collect(Collectors.toSet());
            List<LineageEdge> edgesToReplaceProcesses = createEdgesThatReplaceProcesses(lineageEdges, verticesToRemoveIDs);
            lineageEdges.addAll(edgesToReplaceProcesses);
            lineageEdges.removeAll(edgesToRemove);
            lineageVertices.removeAll(verticesToRemove);
        }
    }

    private boolean isProcessOrSubprocessNode(LineageVertex vertex) {
        return PROCESS_NODES.stream().anyMatch(vertex.getNodeType()::equalsIgnoreCase);
    }

    private List<LineageEdge> createEdgesThatReplaceProcesses(Set<LineageEdge> lineageEdges, Set<String> verticesToRemoveNames) {
        List<LineageEdge> edgesToReplaceProcesses = new ArrayList<>();
        for (String vertexName : verticesToRemoveNames) {
            for (LineageEdge edge : lineageEdges) {
                if (edge.getDestinationNodeID().equalsIgnoreCase(vertexName)) {
                    for (LineageEdge destinationEdge : lineageEdges) {
                        if (destinationEdge.getSourceNodeID().equalsIgnoreCase(vertexName)) {
                            edgesToReplaceProcesses.add(new LineageEdge(edge.getId(), EDGE_LABEL_CONDENSED,
                                    edge.getSourceNodeID(), destinationEdge.getDestinationNodeID()));
                        }
                    }
                }
            }
        }
        return edgesToReplaceProcesses;
    }

    private boolean isInVertexesToRemove(Set<String> verticesToRemoveNames, LineageEdge edge) {
        return verticesToRemoveNames.contains(edge.getSourceNodeID()) || verticesToRemoveNames.contains(edge.getDestinationNodeID());
    }

    /**
     * Map a Tinkerpop vertex to the Open Lineage format.
     *
     * @param originalVertex The vertex to be mapped.
     * @return The vertex in the Open Lineage format.
     */
    public LineageVertex abstractVertex(Vertex originalVertex) {
        String nodeType = originalVertex.label();
        String nodeID = getNodeID(originalVertex);
        LineageVertex lineageVertex = new LineageVertex(nodeID, nodeType);
        lineageVertex.setId(originalVertex.id());
        if (originalVertex.property(PROPERTY_KEY_DISPLAY_NAME).isPresent()) {
            String displayName = originalVertex.property(PROPERTY_KEY_DISPLAY_NAME).value().toString();
            lineageVertex.setDisplayName(displayName);
        } else if (originalVertex.property(PROPERTY_KEY_INSTANCEPROP_DISPLAY_NAME).isPresent()) {
            //Displayname key is stored inconsistently in the graphDB.
            String displayName = originalVertex.property(PROPERTY_KEY_INSTANCEPROP_DISPLAY_NAME).value().toString();
            lineageVertex.setDisplayName(displayName);
        } else if (originalVertex.property(PROPERTY_KEY_LABEL).isPresent()) {
            // if no display props exist use the label. every vertex should have it
            String displayName = originalVertex.property(PROPERTY_KEY_LABEL).value().toString();
            lineageVertex.setDisplayName(displayName);
        } else {
            // if all else fails
            lineageVertex.setDisplayName("undefined");
        }

        if (originalVertex.property(PROPERTY_KEY_ENTITY_GUID).isPresent()) {
            String guid = originalVertex.property(PROPERTY_KEY_ENTITY_GUID).value().toString();
            lineageVertex.setGuid(guid);
        }
        if (originalVertex.property(PROPERTY_NAME_INSTANCEPROP_QUALIFIED_NAME).isPresent()) {
            String qualifiedName = originalVertex.property(PROPERTY_NAME_INSTANCEPROP_QUALIFIED_NAME).value().toString();
            lineageVertex.setQualifiedName(qualifiedName);
        }
        if (NODE_LABEL_SUB_PROCESS.equals(nodeType) && originalVertex.property((PROPERTY_KEY_PROCESS_GUID)).isPresent()) {
            lineageVertex.setGuid(originalVertex.property((PROPERTY_KEY_PROCESS_GUID)).value().toString());
            lineageVertex.setNodeID(originalVertex.property((PROPERTY_KEY_PROCESS_GUID)).value().toString());

        }
        Map<String, String> properties = retrieveProperties(originalVertex);
        lineageVertex.setProperties(properties);
        return lineageVertex;
    }

    private String getNodeID(Vertex vertex) {
        String nodeID;
        if (vertex.label().equalsIgnoreCase(NODE_LABEL_SUB_PROCESS)) {
            nodeID = vertex.property(PROPERTY_KEY_PROCESS_GUID).value().toString();
        } else {
            nodeID = vertex.property(PROPERTY_KEY_ENTITY_GUID).value().toString();
        }
        return nodeID;
    }

    /**
     * Retrieve all properties of the vertex from the db and return the ones that match the whitelist. This will filter out irrelevant
     * properties that should not be returned to a UI.
     *
     * @param vertex the vertex to de mapped
     * @return the filtered properties of the vertex
     */
    private Map<String, String> retrieveProperties(Vertex vertex) {
        boolean isClassificationVertex = vertex.edges(Direction.IN, EDGE_LABEL_CLASSIFICATION).hasNext();
        Map<String, String> newNodeProperties = new HashMap<>();
        Iterator<VertexProperty<Object>> originalProperties = vertex.properties();
        while (originalProperties.hasNext()) {
            Property<Object> originalProperty = originalProperties.next();
            if (immutableReturnedPropertiesWhiteList.contains(originalProperty.key()) || isClassificationVertex) {
                String newPropertyKey = originalProperty.key().
                        replace(PROPERTY_KEY_PREFIX_VERTEX_INSTANCE_PROPERTY, EMPTY_STRING).
                        replace(PROPERTY_KEY_PREFIX_ELEMENT, EMPTY_STRING);

                String newPropertyValue = originalProperty.value().toString();
                newNodeProperties.put(newPropertyKey, newPropertyValue);
            }
        }
        return newNodeProperties;
    }

    public void addColumnProperties(LineageVerticesAndEdges lineageVerticesAndEdges) {
        Set<LineageVertex> lineageVertices = lineageVerticesAndEdges.getLineageVertices();
        if (CollectionUtils.isEmpty(lineageVertices)) {
            return;
        }
        lineageVertices.stream().filter(this::needsAdditionalNodeContext).forEach(vertex -> vertex.setProperties(getProperties(vertex)));
    }

    private boolean needsAdditionalNodeContext(LineageVertex lineageVertex) {
        List<String> types = new ArrayList<>();
        types.addAll(DATA_FILE_AND_SUBTYPES);
        types.addAll(Arrays.asList(RELATIONAL_TABLE, GLOSSARY_TERM, GLOSSARY_CATEGORY, GLOSSARY, PROCESS,
                TABULAR_COLUMN, TABULAR_FILE_COLUMN, RELATIONAL_COLUMN, NODE_LABEL_SUB_PROCESS, TOPIC, EVENT_SCHEMA_ATTRIBUTE));
        return types.contains(lineageVertex.getNodeType());
    }

    private Map<String, String> getProperties(LineageVertex vertex) {
        String type = vertex.getNodeType();
        Object vertexId = vertex.getId();
        Map<String, String> properties = new HashMap<>();
        switch (type) {
            case TABULAR_FILE_COLUMN:
            case TABULAR_COLUMN:
                properties = graphHelper.getResult(this::getTabularColumnProperties, vertexId, this::handlePropertiesNotFound);
                break;
            case RELATIONAL_COLUMN:
                properties = graphHelper.getResult(this::getRelationalColumnProperties, vertexId, this::handlePropertiesNotFound);
                break;
            case EVENT_SCHEMA_ATTRIBUTE:
                properties = graphHelper.getResult(this::getEventSchemaAttributeProperties, vertexId, this::handlePropertiesNotFound);
                break;
            case RELATIONAL_TABLE:
                properties = graphHelper.getResult(this::getRelationalTableProperties, vertexId, this::handlePropertiesNotFound);
                break;
            case TOPIC:
                properties = graphHelper.getResult(this::getTopicProperties, vertexId, this::handlePropertiesNotFound);
                break;
            case DATA_FILE:
            case AVRO_FILE:
            case CSV_FILE:
            case JSON_FILE:
            case KEYSTORE_FILE:
            case LOG_FILE:
            case MEDIA_FILE:
            case DOCUMENT:
                properties = graphHelper.getResult(this::getDataFileProperties, vertexId, this::handlePropertiesNotFound);
                break;
            case PROCESS:
            case NODE_LABEL_SUB_PROCESS:
                properties = graphHelper.getResult(this::getProcessProperties, vertexId, this::handlePropertiesNotFound);
                break;
            case GLOSSARY_TERM:
            case GLOSSARY_CATEGORY:
                properties = graphHelper.getResult(this::getGlossaryTermProperties, vertexId, this::handlePropertiesNotFound);
                break;
        }
        return properties;
    }

    private Map<String, String> getRelationalColumnProperties(GraphTraversalSource g, Object vertexId) {
        Map<String, String> properties = new HashMap<>();
        GraphTraversal<Vertex, Vertex> tableAsset = g.V(vertexId).emit().repeat(bothE().otherV().simplePath()).times(1).or(hasLabel(RELATIONAL_TABLE));
        if (tableAsset.hasNext()) {
            Vertex tableAssetVertex = tableAsset.next();
            properties.put(RELATIONAL_TABLE_KEY, getDisplayNameForVertex(tableAssetVertex));
            properties.putAll(getRelationalTableProperties(g, tableAssetVertex.id()));
        }
        return properties;
    }

    private Map<String, String> getTabularColumnProperties(GraphTraversalSource g, Object vertexId) {
        Map<String, String> properties = new HashMap<>();
        GraphTraversal<Vertex, Map<Object, List<String>>> tabularSchemaType =
                g.V(vertexId).emit().repeat(bothE().outV().simplePath()).times(1).or(hasLabel(TABULAR_SCHEMA_TYPE)).valueMap();
        if (tabularSchemaType.hasNext()) {
            properties.put(SCHEMA_TYPE_KEY, getDisplayNameForVertex(tabularSchemaType.next()));
        }

        GraphTraversal<Vertex, Object> dataFileAsset = g.V(vertexId).emit().repeat(bothE().otherV().simplePath()).times(2)
                .or(hasLabel(P.within(DATA_FILE_AND_SUBTYPES))).id();

        if (dataFileAsset.hasNext()) {
            properties.putAll(getDataFileProperties(g, dataFileAsset.next()));
        }

        return properties;
    }

    private String getFoldersPath(List<Vertex> folderVertices) {
        Collections.reverse(folderVertices);
        return folderVertices.stream().map(folderVertex -> folderVertex.property(PROPERTY_KEY_INSTANCEPROP_DISPLAY_NAME).value().toString())
                .collect(Collectors.joining("/"));
    }

    private List<Vertex> getFolderVertices(GraphTraversalSource g, Object dataFileAssetId) {
        GraphTraversal<Vertex, Vertex> fileFolders =
                g.V(dataFileAssetId).emit().repeat(bothE().otherV().simplePath()).until(inE(FOLDER_HIERARCHY).count().is(0))
                        .or(hasLabel(FILE_FOLDER));
        List<Vertex> folderVertices = new ArrayList<>();
        while (fileFolders.hasNext()) {
            folderVertices.add(fileFolders.next());
        }
        return folderVertices;
    }

    private Map<String, String> getRelationalTableProperties(GraphTraversalSource g, Object vertexId) {
        Map<String, String> properties = new HashMap<>();
        Iterator<Vertex> outVertices = g.V(vertexId).repeat(inE(RELATIONAL_TABLE_CONTEXT_IN_EDGES).otherV()).emit().times(4);
        while (outVertices.hasNext()) {
            Vertex vertex = outVertices.next();
            String vertexLabel = vertex.label();
            switch (vertexLabel) {
                case RELATIONAL_DB_SCHEMA_TYPE:
                    properties.put(SCHEMA_TYPE_KEY, getDisplayNameForVertex(vertex));
                    break;
                case DATABASE:
                    properties.put(DATABASE_KEY, getDisplayNameForVertex(vertex));
                    break;
                case CONNECTION:
                    properties.put(CONNECTION_KEY, getDisplayNameForVertex(vertex));
                    break;
                default:
                    break;
            }
        }
        return properties;

    }

    private String getDisplayNameForVertex(Vertex vertex) {

        if (vertex.properties(PROPERTY_KEY_INSTANCEPROP_DISPLAY_NAME).hasNext()) {
            return (String) vertex.properties(PROPERTY_KEY_INSTANCEPROP_DISPLAY_NAME).next().value();
        }

        if (vertex.properties(PROPERTY_NAME_INSTANCEPROP_QUALIFIED_NAME).hasNext()) {
            return (String) vertex.properties(PROPERTY_NAME_INSTANCEPROP_QUALIFIED_NAME).next().value();
        }

        Map<Object, List<String>> vertexMap = this.graphHelper.getResult(this::getVertexPropertiesMap, vertex, this::handlePropertiesNotFound);

        if (vertexMap.containsKey(PROPERTY_KEY_INSTANCEPROP_DISPLAY_NAME)) {
            return vertexMap.get(PROPERTY_KEY_INSTANCEPROP_DISPLAY_NAME).get(0);
        } else if (vertexMap.containsKey(PROPERTY_NAME_INSTANCEPROP_QUALIFIED_NAME)) {
            return vertexMap.get(PROPERTY_NAME_INSTANCEPROP_QUALIFIED_NAME).get(0);
        }
        return null;
    }

    private Map<Object, List<String>> getVertexPropertiesMap(GraphTraversalSource g, Vertex v) {
        GraphTraversal<Vertex, Map<Object, List<String>>> vertexMapGraphTraversal = g.V(v.id()).valueMap();
        if (!vertexMapGraphTraversal.hasNext()) {
            return null;
        }
        return vertexMapGraphTraversal.next();
    }

    private String getDisplayNameForVertex(Map<Object, List<String>> vertex) {
        if (vertex.containsKey(PROPERTY_KEY_INSTANCEPROP_DISPLAY_NAME)) {
            return vertex.get(PROPERTY_KEY_INSTANCEPROP_DISPLAY_NAME).get(0);
        } else if (vertex.containsKey(PROPERTY_NAME_INSTANCEPROP_QUALIFIED_NAME)) {
            return vertex.get(PROPERTY_NAME_INSTANCEPROP_QUALIFIED_NAME).get(0);
        }
        return null;
    }

    private Map<String, String> getDataFileProperties(GraphTraversalSource g, Object vertexId) {
        Map<String, String> properties = extractPropertiesFromNeighborhood(g, vertexId);
        if (!properties.containsKey(FILE_FOLDER_KEY)) {
            Optional<String> path = extractPathFromVertexProperties(g, vertexId);
            path.ifPresent(s -> properties.put(FILE_FOLDER_KEY, "/" + s.trim()));
        }
        return properties;
    }

    private Map<String, String> extractPropertiesFromNeighborhood(GraphTraversalSource g, Object vertexId) {
        Map<String, String> properties = new HashMap<>();

        List<Vertex> folderVertices = getFolderVertices(g, vertexId);
        if (CollectionUtils.isEmpty(folderVertices)) {
            return properties;
        }
        Object lastFolderVertexId = folderVertices.get(folderVertices.size() - 1).id();
        properties.put(FILE_FOLDER_KEY, String.join("/", getFoldersPath(folderVertices)));

        Optional<String> connectionDetails = getConnectionDetailsFromNeighborhood(g, vertexId);
        if (connectionDetails.isEmpty()) {
            connectionDetails = getConnectionDetailsFromNeighborhood(g, lastFolderVertexId);
        }
        connectionDetails.ifPresent(s -> properties.put(CONNECTION_KEY, s));

        return properties;
    }

    private Optional<String> getConnectionDetailsFromNeighborhood(GraphTraversalSource g, Object vertexId) {
        Iterator<Vertex> connection = g.V(vertexId).emit().repeat(bothE().otherV().simplePath()).times(1).or(hasLabel(CONNECTION));
        if (connection.hasNext()) {
            return Optional.ofNullable(this.getDisplayNameForVertex(connection.next()));
        }
        return Optional.empty();
    }

    private Optional<String> extractPathFromVertexProperties(GraphTraversalSource g, Object vertexId) {

        VertexProperty<String> additionalProperties =
                g.V(vertexId).next().property(PROPERTY_KEY_INSTANCE_PROP_ADDITIONAL_PROPERTIES);
        if (!additionalProperties.isPresent()) {
            return Optional.empty();
        }

        String additionalPropertiesValue = additionalProperties.value();
        return Arrays.stream(additionalPropertiesValue.split(","))
                .filter(s -> s.trim().startsWith("path"))
                .map(s -> s.split("=")[1])
                .findFirst();
    }

    private Map<String, String> getProcessProperties(GraphTraversalSource g, Object vertexId) {
        Map<String, String> properties = new HashMap<>();
        GraphTraversal<Vertex, Map<Object, Object>> transformationProject = g.V(vertexId).emit().repeat(bothE().otherV().simplePath()).times(1).or(hasLabel(COLLECTION)).valueMap();
        if (transformationProject.hasNext()) {
            Map<Object, Object> transformationProjectValueMap = transformationProject.next();
            if (transformationProjectValueMap.containsKey(PROPERTY_KEY_INSTANCEPROP_DISPLAY_NAME)) {
                properties.put(TRANSFORMATION_PROJECT_KEY,
                        ((List<String>) transformationProjectValueMap.get(PROPERTY_KEY_INSTANCEPROP_DISPLAY_NAME)).get(0));
            }
        }
        return properties;
    }

    private Map<String, String> getGlossaryTermProperties(GraphTraversalSource g, Object vertexId) {
        Map<String, String> properties = new HashMap<>();
        GraphTraversal<Vertex, Map<Object, List<String>>> glossary = g.V(vertexId).emit().repeat(bothE().otherV().simplePath()).times(1).or(hasLabel(GLOSSARY)).valueMap(PROPERTY_KEY_INSTANCEPROP_DISPLAY_NAME);
        if (glossary.hasNext()) {
            properties.put(GLOSSARY_KEY, glossary.next().get(PROPERTY_KEY_INSTANCEPROP_DISPLAY_NAME).get(0));
        }
        return properties;
    }

    private Map<String, String> getTopicProperties(GraphTraversalSource g, Object vertexId) {
        Map<String, String> properties = new HashMap<>();
        Iterator<Vertex> eventTypeList = g.V(vertexId).emit().repeat(bothE().otherV().simplePath()).times(1).or(hasLabel(EVENT_TYPE_LIST));
        if (eventTypeList.hasNext()) {
            properties.put(EVENT_TYPE_LIST_KEY, getDisplayNameForVertex(eventTypeList.next()));
        }

        return properties;
    }

    private Map<String, String> getEventSchemaAttributeProperties(GraphTraversalSource g, Object vertexId) {
        Map<String, String> properties = new HashMap<>();
        Iterator<Vertex> eventType = g.V(vertexId).emit().repeat(bothE().otherV().simplePath()).times(1).or(hasLabel(EVENT_TYPE));

        if (eventType.hasNext()) {
            properties.put(EVENT_TYPE_KEY, getDisplayNameForVertex(eventType.next()));
        }

        Iterator<Vertex> eventTypeList = g.V(vertexId).emit().repeat(bothE().otherV().simplePath()).times(2).or(hasLabel(EVENT_TYPE_LIST));
        if (eventTypeList.hasNext()) {
            properties.put(EVENT_TYPE_LIST_KEY, getDisplayNameForVertex(eventTypeList.next()));
        }

        Iterator<Vertex> topic = g.V(vertexId).emit().repeat(bothE().otherV().simplePath()).times(3).or(hasLabel(TOPIC));
        if (topic.hasNext()) {
            properties.put(TOPIC_KEY, getDisplayNameForVertex(topic.next()));
        }

        return properties;
    }

    private void handlePropertiesNotFound(Exception e, Object vertexId) {
        log.error("Could not retrieve properties {}", vertexId, e);
    }
}
