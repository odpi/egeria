///* SPDX-License-Identifier: Apache-2.0 */
///* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.graph;

import org.apache.commons.lang3.StringUtils;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__;
import org.apache.tinkerpop.gremlin.structure.Column;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.odpi.openmetadata.accessservices.assetlineage.model.GraphContext;
import org.odpi.openmetadata.accessservices.assetlineage.model.LineageEntity;
import org.odpi.openmetadata.accessservices.assetlineage.model.LineageRelationship;
import org.odpi.openmetadata.accessservices.assetlineage.model.LineageSyncUpdateContext;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.governanceservers.openlineage.graph.LineageGraph;
import org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.model.GraphRelationship;
import org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.model.ffdc.JanusConnectorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.addE;
import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.addV;
import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.inE;
import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.inV;
import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.outV;
import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.unfold;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.model.JanusConnectorErrorCode.COULD_NOT_RETRIEVE_LAST_UPDATE_TIME;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.model.JanusConnectorErrorCode.COULD_NOT_SAVE_LAST_UPDATE_TIME;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.model.JanusConnectorErrorCode.DELETE_CLASSIFICATION_EXCEPTION;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.model.JanusConnectorErrorCode.DELETE_ENTITY_EXCEPTION;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.model.JanusConnectorErrorCode.DELETE_RELATIONSHIP_EXCEPTION;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.model.JanusConnectorErrorCode.ENTITY_NOT_FOUND;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.model.JanusConnectorErrorCode.ERROR_REMOVING_OBSOLETE_EDGES;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.model.JanusConnectorErrorCode.FAILED_TO_UPDATE_CLASSIFICATION_WITH_GUID;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.model.JanusConnectorErrorCode.GET_ALL_NEIGHBOURS;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.model.JanusConnectorErrorCode.PROPERTIES_UPDATE_EXCEPTION;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.model.JanusConnectorErrorCode.UNABLE_TO_ADD_PROPERTIES_ON_EDGE_FROM_RELATIONSHIP_WITH_TYPE;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.model.JanusConnectorErrorCode.VERTICES_AND_RELATIONSHIP_CREATION_EXCEPTION;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.ASSET_LINEAGE_VARIABLES;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.CLASSIFICATION_WITH_GUID_NOT_FOUND;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.EDGE;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.EDGE_GUID_NOT_FOUND_WHEN_UPDATE;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.EDGE_WITH_GUID_DELETED;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.EDGE_WITH_GUID_DID_NOT_DELETE;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.FROM;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.KV;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.PROPERTIES;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.PROCESS;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.S;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.V;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.VERTEX_GUID_NOT_FOUND_WHEN_UPDATE;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.VERTEX_WITH_GUID_DELETED;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.VERTEX_WITH_GUID_IS_NOT_PRESENT;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.EDGE_LABEL_CLASSIFICATION;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.PROPERTY_KEY_ENTITY_CREATED_BY;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.PROPERTY_KEY_ENTITY_CREATE_TIME;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.PROPERTY_KEY_ENTITY_GUID;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.PROPERTY_KEY_ENTITY_UPDATED_BY;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.PROPERTY_KEY_ENTITY_UPDATE_TIME;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.PROPERTY_KEY_ENTITY_VERSION;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.PROPERTY_KEY_LABEL;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.PROPERTY_KEY_METADATA_ID;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.PROPERTY_KEY_PREFIX_ELEMENT;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.PROPERTY_KEY_PREFIX_INSTANCE_PROPERTY;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.PROPERTY_KEY_RELATIONSHIP_GUID;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.VARIABLE_NAME_ASSET_LINEAGE_LAST_UPDATE_TIME;

public class LineageGraphStorageService implements LineageGraph {

    private static final Logger log = LoggerFactory.getLogger(LineageGraphStorageService.class);

    private LineageGraphQueryService helper;
    private GraphHelper graphHelper;
    private AuditLog auditLog;

    public LineageGraphStorageService(GraphHelper graphHelper, AuditLog auditLog) {
        this.graphHelper = graphHelper;
        this.auditLog = auditLog;
        this.helper = new LineageGraphQueryService(graphHelper, auditLog);
    }

    /**
     * Creates vertices and the relationships between them
     *
     * @param graphContext - graph Collection that contains vertices and edges to be stored
     */
    @Override
    public void storeToGraph(Set<GraphContext> graphContext) {
        graphContext.stream().map(this::getGraphRelationship).forEach(this::storeRelationship);
    }

    private GraphRelationship getGraphRelationship(GraphContext entry) {
        LineageEntity fromEntity = entry.getFromVertex();
        LineageEntity toEntity = entry.getToVertex();
        return new GraphRelationship(fromEntity, toEntity, entry.getRelationshipType(), entry.getRelationshipGuid());
    }

    private void storeRelationship(GraphRelationship relationship) {
        BiConsumer<GraphTraversalSource, GraphRelationship> upsertToGraph = this::upsertToGraph;
        graphHelper.commit(upsertToGraph, relationship, this::handleStoreRelationshipError);
    }

    private void handleStoreRelationshipError(Exception e) throws JanusConnectorException {
        this.auditLog.logException(VERTICES_AND_RELATIONSHIP_CREATION_EXCEPTION.getErrorMessage(),
                VERTICES_AND_RELATIONSHIP_CREATION_EXCEPTION.getMessageDefinition(), e);
        throw new JanusConnectorException(this.getClass().getName(), "upsertToGraph", VERTICES_AND_RELATIONSHIP_CREATION_EXCEPTION);
    }

    private void upsertToGraph(GraphTraversalSource g, GraphRelationship graphRelationship) {
        Vertex to = addVertex(g, graphRelationship.getToEntity());
        Vertex from = addVertex(g, graphRelationship.getFromEntity());
        addEdge(g, graphRelationship.getRelationshipLabel(), graphRelationship.getRelationshipGuid(), to, from);
    }

    private Vertex addVertex(GraphTraversalSource g, LineageEntity toEntity) {
        Map<String, Object> toEntityProperties = getProperties(toEntity);
        GraphTraversal<Object, Vertex> objectVertexGraphTraversal = addV(toEntity.getTypeDefName());
        objectVertexGraphTraversal.property(PROPERTY_KEY_ENTITY_GUID, toEntity.getGuid());
        for (Map.Entry<String, Object> propertiesEntry : toEntityProperties.entrySet()) {
            objectVertexGraphTraversal.property(propertiesEntry.getKey(), propertiesEntry.getValue());
        }
        return g.V().has(PROPERTY_KEY_ENTITY_GUID, toEntity.getGuid())
                .fold()
                .coalesce(unfold(), objectVertexGraphTraversal)
                .next();
    }

    private void addEdge(GraphTraversalSource g, String relationshipLabel, String relationshipGuid, Vertex to, Vertex from) {
        g.V(from.id()).as(FROM).V(to.id())
                .coalesce(inE(relationshipLabel).where(outV().as(FROM)),
                        addE(relationshipLabel).from(FROM)).property(PROPERTY_KEY_RELATIONSHIP_GUID, relationshipGuid).next();
    }

    private Map<String, Object> getProperties(LineageEntity lineageEntity) {
        Map<String, Object> properties = lineageEntity.getProperties().entrySet().stream()
                .filter(e -> StringUtils.isNotEmpty(e.getValue()))
                .collect(Collectors.toMap(
                        e -> PROPERTY_KEY_PREFIX_ELEMENT + PROPERTY_KEY_PREFIX_INSTANCE_PROPERTY + e.getKey(),
                        Map.Entry::getValue));

        properties.computeIfAbsent(PROPERTY_KEY_ENTITY_CREATE_TIME, val -> lineageEntity.getCreateTime());
        properties.computeIfAbsent(PROPERTY_KEY_ENTITY_CREATED_BY, val -> lineageEntity.getCreatedBy());
        properties.computeIfAbsent(PROPERTY_KEY_ENTITY_UPDATE_TIME, val -> lineageEntity.getUpdateTime());
        properties.computeIfAbsent(PROPERTY_KEY_ENTITY_UPDATED_BY, val -> lineageEntity.getUpdatedBy());
        properties.computeIfAbsent(PROPERTY_KEY_LABEL, val -> lineageEntity.getTypeDefName());
        properties.computeIfAbsent(PROPERTY_KEY_ENTITY_VERSION, val -> lineageEntity.getVersion());
        properties.computeIfAbsent(PROPERTY_KEY_METADATA_ID, val -> lineageEntity.getMetadataCollectionId());
        return properties;
    }

    /**
     * Updates the neighbours of a node by removing all the entities that no longer have a relationship with it.
     * <p>
     * SyncUpdateContext contains the context for syncing the relationships of a node after an update.
     */
    @Override
    public void updateNeighbours(LineageSyncUpdateContext syncUpdateContext) {
        List<String> existingNeighboursGUIDs =
                this.graphHelper.getResult(this::getAllNeighbours, syncUpdateContext.getEntityGUID(), this::handleErrorGetAllNeighbours);
        if (isDifferentGraphContext(syncUpdateContext.getNeighboursGUID(), existingNeighboursGUIDs)) {
            this.graphHelper.commit(this::removeObsoleteEdges, syncUpdateContext, existingNeighboursGUIDs, this::handleErrorRemoveObsoleteEdges);
        }
    }

    private List<String> getAllNeighbours(GraphTraversalSource g, String entityGUID) {
        GraphTraversal<Vertex, Vertex> exitingVertices = g.V().has(PROPERTY_KEY_ENTITY_GUID, entityGUID).bothE().otherV();
        List<String> existingGUIDs = new ArrayList<>();
        while (exitingVertices.hasNext()) {
            Map<Object, Object> valueMap = g.V(exitingVertices.next().id()).valueMap(PROPERTY_KEY_ENTITY_GUID).next();
            if (valueMap.containsKey(PROPERTY_KEY_ENTITY_GUID)) {
                existingGUIDs.addAll((List<String>) valueMap.get(PROPERTY_KEY_ENTITY_GUID));
            }
        }
        return existingGUIDs;
    }

    private void handleErrorGetAllNeighbours(Exception e, String entityGuid) {
        this.auditLog.logException(GET_ALL_NEIGHBOURS.getErrorMessage() + " " + entityGuid,
                GET_ALL_NEIGHBOURS.getMessageDefinition(), e);
        throw new JanusConnectorException(this.getClass().getName(), "getAllNeighbours", GET_ALL_NEIGHBOURS);
    }

    private boolean isDifferentGraphContext(Set<String> newVertices, List<String> neighboursGUIDs) {
        return neighboursGUIDs.size() != newVertices.size() || !neighboursGUIDs.containsAll(newVertices);
    }

    private void removeObsoleteEdges(GraphTraversalSource g, LineageSyncUpdateContext context, List<String> neighboursGUIDs) {
        String entityGUID = context.getEntityGUID();
        Set<String> newVertices = context.getNeighboursGUID();
        List<String> obsoleteNeighbours = neighboursGUIDs.stream().filter(existingVertex ->
                !newVertices.contains(existingVertex)).collect(Collectors.toList());
        if (obsoleteNeighbours.isEmpty()) {
            return;
        }
        Iterator<Edge> existingEdges = g.V().has(PROPERTY_KEY_ENTITY_GUID, entityGUID).bothE();
        while (existingEdges.hasNext()) {
            Edge edge = existingEdges.next();
            List<String> inVertexGuid = (List<String>) g.V(edge.inVertex()).valueMap(PROPERTY_KEY_ENTITY_GUID).next().get(PROPERTY_KEY_ENTITY_GUID);
            List<String> outVertexGuid = (List<String>) g.V(edge.outVertex()).valueMap(PROPERTY_KEY_ENTITY_GUID).next().get(PROPERTY_KEY_ENTITY_GUID);
            if (obsoleteNeighbours.containsAll(inVertexGuid) || obsoleteNeighbours.containsAll(outVertexGuid)) {
                g.E(edge.id()).drop().iterate();
            }
        }
    }

    private void handleErrorRemoveObsoleteEdges(Exception e) {
        this.auditLog.logException(ERROR_REMOVING_OBSOLETE_EDGES.getErrorMessage(),
                ERROR_REMOVING_OBSOLETE_EDGES.getMessageDefinition(), e);
        throw new JanusConnectorException(this.getClass().getName(), "removeObsoleteEdges", ERROR_REMOVING_OBSOLETE_EDGES);
    }

    /**
     * Updates the properties of a vertex
     *
     * @param lineageEntity - LineageEntity object that has the updated values
     */
    @Override
    public void updateEntity(LineageEntity lineageEntity) {
        this.graphHelper.commit(this::updateEntityInGraph, lineageEntity, this::handleUpdateEntityError);

    }

    private void updateEntityInGraph(GraphTraversalSource g, LineageEntity entity) {
        Iterator<Vertex> vertex = g.V().has(PROPERTY_KEY_ENTITY_GUID, entity.getGuid());
        if (!vertex.hasNext()) {
            if (entity.getTypeDefName().equalsIgnoreCase(PROCESS)) {
                addVertex(g, entity);
            } else {
                log.debug(VERTEX_GUID_NOT_FOUND_WHEN_UPDATE, entity.getGuid());
            }
            return;
        }
        addOrUpdatePropertiesVertex(g, vertex.next(), entity);
    }

    private void handleUpdateEntityError(Exception e) {
        this.auditLog.logException(PROPERTIES_UPDATE_EXCEPTION.getErrorMessage(),
                PROPERTIES_UPDATE_EXCEPTION.getMessageDefinition(), e);
        throw new JanusConnectorException(this.getClass().getName(), "updateEntity", PROPERTIES_UPDATE_EXCEPTION);
    }

    /**
     * Adds or updates properties of a vertex.
     *
     * @param vertex        - vertex to be updated
     * @param lineageEntity - LineageEntity object that has the updates values
     * @return
     */
    private void addOrUpdatePropertiesVertex(GraphTraversalSource g, Vertex vertex, LineageEntity lineageEntity) {
        Map<String, Object> properties = getProperties(lineageEntity);
        g.inject(properties)
                .unfold()
                .as(PROPERTIES)
                .V(vertex.id())
                .as(V)
                .sideEffect(__.select(PROPERTIES)
                        .unfold()
                        .as(KV)
                        .select(V)
                        .property(__.select(KV).by(Column.keys), __.select(KV).by(Column.values))).iterate();
    }

    /**
     * Create or update the relationship between two edges
     * In case the vertexes are not created, they are firstly created
     *
     * @param lineageRelationship relationship to be updated or created
     */
    @Override
    public void upsertRelationship(LineageRelationship lineageRelationship) {

        GraphRelationship graphRelationship = new GraphRelationship(lineageRelationship);

        BiConsumer<GraphTraversalSource, GraphRelationship> upsertToGraph = this::upsertToGraph;
        graphHelper.commit(upsertToGraph, graphRelationship, this::handleStoreRelationshipError);

        BiConsumer<GraphTraversalSource, LineageRelationship> addOrUpdatePropertiesEdge = this::addOrUpdatePropertiesEdge;
        graphHelper.commit(addOrUpdatePropertiesEdge, lineageRelationship, this::handlePropertiesEdgeException);
    }

    /**
     * Adds or updates properties of an edge.
     *
     * @param lineageRelationship - LineageEntity object that has the updates values
     */
    private void addOrUpdatePropertiesEdge(GraphTraversalSource g, LineageRelationship lineageRelationship) {
        Map<String, Object> properties = lineageRelationship.getProperties().entrySet().stream().collect(Collectors.toMap(
                e -> PROPERTY_KEY_PREFIX_ELEMENT + PROPERTY_KEY_PREFIX_INSTANCE_PROPERTY + e.getKey(),
                Map.Entry::getValue
        ));

        properties.values().remove(null);
        properties.computeIfAbsent(PROPERTY_KEY_ENTITY_CREATE_TIME, val -> lineageRelationship.getCreateTime());
        properties.computeIfAbsent(PROPERTY_KEY_ENTITY_CREATED_BY, val -> lineageRelationship.getCreatedBy());
        properties.computeIfAbsent(PROPERTY_KEY_ENTITY_UPDATE_TIME, val -> lineageRelationship.getUpdateTime());
        properties.computeIfAbsent(PROPERTY_KEY_ENTITY_UPDATED_BY, val -> lineageRelationship.getUpdatedBy());
        properties.computeIfAbsent(PROPERTY_KEY_LABEL, val -> lineageRelationship.getTypeDefName());
        properties.computeIfAbsent(PROPERTY_KEY_ENTITY_VERSION, val -> lineageRelationship.getVersion());
        properties.computeIfAbsent(PROPERTY_KEY_METADATA_ID, val -> lineageRelationship.getMetadataCollectionId());

        g.inject(properties)
                .as(PROPERTIES)
                .V(lineageRelationship.getSourceEntity().getGuid())
                .outE()
                .where(inV().hasId(lineageRelationship.getTargetEntity().getGuid()))
                .as(EDGE)
                .sideEffect(__.select(PROPERTIES)
                        .unfold()
                        .as(KV)
                        .select(EDGE)
                        .property(__.select(KV).by(Column.keys), __.select(KV).by(Column.values))).iterate();
    }

    private void handlePropertiesEdgeException(Exception e) {
        this.auditLog.logException(UNABLE_TO_ADD_PROPERTIES_ON_EDGE_FROM_RELATIONSHIP_WITH_TYPE.getErrorMessage(),
                UNABLE_TO_ADD_PROPERTIES_ON_EDGE_FROM_RELATIONSHIP_WITH_TYPE.getMessageDefinition(), e);
        throw new JanusConnectorException(this.getClass().getName(), "addOrUpdatePropertiesEdge", UNABLE_TO_ADD_PROPERTIES_ON_EDGE_FROM_RELATIONSHIP_WITH_TYPE);
    }

    /**
     * Updates the properties of an edge
     *
     * @param lineageRelationship - lineageRelationship object that has the updated values
     */
    @Override
    public void updateRelationship(LineageRelationship lineageRelationship) {
        BiConsumer<GraphTraversalSource, LineageRelationship> updateEdgeIfItExists = this::updateEdgeIfItExists;
        this.graphHelper.commit(updateEdgeIfItExists, lineageRelationship, this::handlePropertiesEdgeException);
    }

    private void updateEdgeIfItExists(GraphTraversalSource g, LineageRelationship lineageRelationship) {
        Iterator<Edge> edge = g.E().has(PROPERTY_KEY_RELATIONSHIP_GUID, lineageRelationship.getGuid());
        if (!edge.hasNext()) {
            log.debug(EDGE_GUID_NOT_FOUND_WHEN_UPDATE, lineageRelationship.getGuid());
            return;
        }
        addOrUpdatePropertiesEdge(g, lineageRelationship);
    }

    /**
     * Updates the classification of a vertex
     *
     * @param classificationContext - LineageEntity object that has the updated values
     */
    @Override
    public void updateClassification(Set<GraphContext> classificationContext) {
        classificationContext.forEach(context -> this.graphHelper.commit(this::updateClassification, context, this::handleUpdateClassificationException));
    }

    private void updateClassification(GraphTraversalSource g, GraphContext graphContext) {
        String classificationGuid = graphContext.getToVertex().getGuid();
        Iterator<Vertex> vertexIterator = g.V().has(PROPERTY_KEY_ENTITY_GUID, classificationGuid);
        if (!vertexIterator.hasNext()) {
            log.debug(CLASSIFICATION_WITH_GUID_NOT_FOUND, classificationGuid);
            return;
        }
        Vertex storedClassification = vertexIterator.next();
        long storedClassificationVersion = (long) g.V(storedClassification.id()).elementMap(PROPERTY_KEY_ENTITY_VERSION)
                .toList().get(0).get(PROPERTY_KEY_ENTITY_VERSION);
        if (storedClassificationVersion < graphContext.getToVertex().getVersion()) {
            addOrUpdatePropertiesVertex(g, storedClassification, graphContext.getToVertex());
        }
    }

    private void handleUpdateClassificationException(Exception e) {
        this.auditLog.logException(FAILED_TO_UPDATE_CLASSIFICATION_WITH_GUID.getErrorMessage(),
                FAILED_TO_UPDATE_CLASSIFICATION_WITH_GUID.getMessageDefinition(), e);
        throw new JanusConnectorException(this.getClass().getName(), "updateClassification", FAILED_TO_UPDATE_CLASSIFICATION_WITH_GUID);

    }

    /**
     * Deletes a classification of a vertex
     *
     * @param classificationContext - any remaining classifications, empty map if none
     */
    @Override
    public void deleteClassification(Set<GraphContext> classificationContext) {
        BiConsumer<GraphTraversalSource, GraphContext> deleteClassification = this::deleteClassification;
        classificationContext.forEach(classification -> graphHelper.commit(deleteClassification, classification, this::handleDeleteClassificationException));
    }

    private void deleteClassification(GraphTraversalSource g, GraphContext context) {
        Graph entityAndClassificationsGraph = (Graph) g.V().has(PROPERTY_KEY_ENTITY_GUID, context.getFromVertex().getGuid())
                .bothE(EDGE_LABEL_CLASSIFICATION).subgraph(S).cap(S).next();

        Iterator<Edge> edges = entityAndClassificationsGraph.edges();

        while (edges.hasNext()) {
            Edge edge = edges.next();
            String storedClassificationGuid =
                    (String) g.E(edge.id()).inV().elementMap(PROPERTY_KEY_ENTITY_GUID).toList().get(0).get(PROPERTY_KEY_ENTITY_GUID);
            if (context.getToVertex().getGuid().equals(storedClassificationGuid)) {
                dropEdge(g, edge, storedClassificationGuid);
                break;
            }
        }
    }

    private void handleDeleteClassificationException(Exception e) {
        this.auditLog.logException(DELETE_CLASSIFICATION_EXCEPTION.getErrorMessage(),
                DELETE_CLASSIFICATION_EXCEPTION.getMessageDefinition(), e);
        throw new JanusConnectorException(this.getClass().getName(), "deleteClassification", DELETE_CLASSIFICATION_EXCEPTION);
    }


    private void dropEdge(GraphTraversalSource g, Edge edge, String storedClassificationGuid) {
        g.V().has(PROPERTY_KEY_ENTITY_GUID, storedClassificationGuid).drop().iterate();
        g.E(edge.id()).drop().iterate();
    }

    @Override
    public void deleteRelationship(String guid) {
        BiConsumer<GraphTraversalSource, String> deleteRelationship = (g, edgeGuid) -> {
            Iterator<Edge> edge = g.E().has(PROPERTY_KEY_RELATIONSHIP_GUID, edgeGuid);
            if (!edge.hasNext()) {
                log.debug(EDGE_WITH_GUID_DID_NOT_DELETE, edgeGuid);
                return;
            }
            g.E(edge.next().id()).drop().iterate();
            log.debug(EDGE_WITH_GUID_DELETED, edgeGuid);
        };
        graphHelper.commit(deleteRelationship, guid, this::handleDeleteRelationshipException);
    }

    private void handleDeleteRelationshipException(Exception e) {
        this.auditLog.logException(DELETE_RELATIONSHIP_EXCEPTION.getErrorMessage(),
                DELETE_RELATIONSHIP_EXCEPTION.getMessageDefinition(), e);
        throw new JanusConnectorException(this.getClass().getName(), "deleteRelationship", DELETE_RELATIONSHIP_EXCEPTION);
    }


    @Override
    public void deleteEntity(String guid, Object version) {
        /*
         * TODO need to take into account the version of the entity once we have history
         * */
        BiConsumer<GraphTraversalSource, String> deleteEntity = this::deleteEntity;
        this.graphHelper.commit(deleteEntity, guid, this::handleDeleteEntityException);
        log.debug(VERTEX_WITH_GUID_DELETED, guid);
    }

    private void deleteEntity(GraphTraversalSource g, String guid) {
        Iterator<Vertex> vertex = g.V().has(PROPERTY_KEY_ENTITY_GUID, guid);
        //TODO add check when we will have classifications to delete classifications first
        if (!vertex.hasNext()) {
            log.debug(VERTEX_WITH_GUID_IS_NOT_PRESENT, guid);
            return;
        }
        g.V().has(PROPERTY_KEY_ENTITY_GUID, guid).drop().iterate();
    }

    private void handleDeleteEntityException(Exception e) {
        this.auditLog.logException(DELETE_ENTITY_EXCEPTION.getErrorMessage(),
                DELETE_ENTITY_EXCEPTION.getMessageDefinition(), e);
        throw new JanusConnectorException(this.getClass().getName(), "deleteEntity", DELETE_ENTITY_EXCEPTION);
    }

    @Override
    public void saveAssetLineageUpdateTime(Long lastUpdateTime) {
        BiConsumer<GraphTraversalSource, Long> saveUpdateTime = this::saveUpdateTime;
        graphHelper.commit(saveUpdateTime, lastUpdateTime, this::handleSaveAssetLineageUpdateTimeException);
    }

    public void handleSaveAssetLineageUpdateTimeException(Exception e) {
        this.auditLog.logException(COULD_NOT_SAVE_LAST_UPDATE_TIME.getErrorMessage(),
                COULD_NOT_SAVE_LAST_UPDATE_TIME.getMessageDefinition(), e);
        throw new JanusConnectorException(this.getClass().getName(), "saveAssetLineageUpdateTime", COULD_NOT_SAVE_LAST_UPDATE_TIME);
    }

    private void saveUpdateTime(GraphTraversalSource g, Long updateTime) {
        GraphTraversal<Vertex, Vertex> lineageVariables = g.V().has(PROPERTY_KEY_LABEL, ASSET_LINEAGE_VARIABLES);
        if (!lineageVariables.hasNext()) {
            g.addV(ASSET_LINEAGE_VARIABLES)
                    .property(PROPERTY_KEY_LABEL, ASSET_LINEAGE_VARIABLES)
                    .property(VARIABLE_NAME_ASSET_LINEAGE_LAST_UPDATE_TIME, updateTime).next();
        } else {
            g.V(lineageVariables.next().id()).property(VARIABLE_NAME_ASSET_LINEAGE_LAST_UPDATE_TIME, updateTime).next();
        }
    }

    @Override
    public Optional<Long> getAssetLineageUpdateTime() {
        Function<GraphTraversalSource, Optional<Long>> getUpdateTime = this::getUpdateTime;
        return graphHelper.getResult(getUpdateTime, this::handleRetrievingError);
    }

    private Optional<Long> getUpdateTime(GraphTraversalSource g) {
        GraphTraversal<Vertex, Map<Object, List<Long>>> lineageVariables = g.V().has(PROPERTY_KEY_LABEL, ASSET_LINEAGE_VARIABLES).valueMap();
        if (lineageVariables.hasNext()) {

            Map<Object, List<Long>> next = lineageVariables.next();
            if (next.containsKey(VARIABLE_NAME_ASSET_LINEAGE_LAST_UPDATE_TIME)) {
                return Optional.of(next.get(VARIABLE_NAME_ASSET_LINEAGE_LAST_UPDATE_TIME).get(0));
            }
        } else {
            return getLineageUpdateTimeFromGraphVariables(g);

        }
        return Optional.empty();
    }

    private Optional<Long> getLineageUpdateTimeFromGraphVariables(GraphTraversalSource g) {
        try {
            return g.getGraph().variables().get(VARIABLE_NAME_ASSET_LINEAGE_LAST_UPDATE_TIME);
        } catch (UnsupportedOperationException e) {
            return Optional.empty();
        }
    }

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    public Boolean isEntityInGraph(String guid) {
        return graphHelper.getResult(this::entityExists, guid, this::handleRetrievingError);
    }

    private boolean entityExists(GraphTraversalSource g, String guid) {
        return !g.V().has(PROPERTY_KEY_ENTITY_GUID, guid).toList().isEmpty();
    }

    private void handleRetrievingError(Exception e) {
        log.error(COULD_NOT_RETRIEVE_LAST_UPDATE_TIME.getErrorMessage(), COULD_NOT_RETRIEVE_LAST_UPDATE_TIME.getMessageDefinition(), e);
    }

    private void handleRetrievingError(Exception e, String guid) {
        log.error(ENTITY_NOT_FOUND.getFormattedErrorMessage(guid), ENTITY_NOT_FOUND.getMessageDefinition(), e);
    }
}
