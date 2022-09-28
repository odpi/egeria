/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.graph;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.tinkerpop.gremlin.process.traversal.Traversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.Property;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.structure.VertexProperty;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.governanceservers.openlineage.OpenLineageQueryService;
import org.odpi.openmetadata.governanceservers.openlineage.ffdc.OpenLineageException;
import org.odpi.openmetadata.governanceservers.openlineage.ffdc.OpenLineageServerErrorCode;
import org.odpi.openmetadata.governanceservers.openlineage.model.LineageEdge;
import org.odpi.openmetadata.governanceservers.openlineage.model.LineageVertex;
import org.odpi.openmetadata.governanceservers.openlineage.model.LineageVerticesAndEdges;
import org.odpi.openmetadata.governanceservers.openlineage.model.NodeNamesSearchCriteria;
import org.odpi.openmetadata.governanceservers.openlineage.model.Scope;
import org.odpi.openmetadata.governanceservers.openlineage.requests.LineageSearchRequest;
import org.odpi.openmetadata.governanceservers.openlineage.requests.Node;
import org.odpi.openmetadata.governanceservers.openlineage.responses.LineageNodeNamesResponse;
import org.odpi.openmetadata.governanceservers.openlineage.responses.LineageResponse;
import org.odpi.openmetadata.governanceservers.openlineage.responses.LineageTypesResponse;
import org.odpi.openmetadata.governanceservers.openlineage.responses.LineageSearchResponse;
import org.odpi.openmetadata.governanceservers.openlineage.responses.LineageVertexResponse;
import org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.model.ffdc.JanusConnectorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.hasLabel;
import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.inE;
import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.outE;
import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.until;
import static org.odpi.openmetadata.governanceservers.openlineage.ffdc.OpenLineageServerErrorCode.ERROR_ENTITY_NOT_FOUND;
import static org.odpi.openmetadata.governanceservers.openlineage.ffdc.OpenLineageServerErrorCode.ERROR_LINEAGE_NOT_FOUND;
import static org.odpi.openmetadata.governanceservers.openlineage.ffdc.OpenLineageServerErrorCode.ERROR_TYPES_NOT_FOUND;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.model.JanusConnectorErrorCode.CLASSIFICATION_NOT_FOUND;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.model.JanusConnectorErrorCode.COULD_NOT_RETRIEVE_VERTEX;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.model.JanusConnectorErrorCode.LINEAGE_NOT_FOUND;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.model.JanusConnectorErrorCode.NODES_NOT_FOUND;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.model.JanusConnectorErrorCode.TYPES_NOT_FOUND;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.model.JanusConnectorErrorCode.SEARCH_ERROR;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.ASSETS;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.ASSET_LINEAGE_VARIABLES;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.ASSET_SCHEMA_TYPE;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.AVRO_FILE;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.CLASSIFICATION_GRAPH;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.COLUMN_SPACE_DELIMITER;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.COMMA_SPACE_DELIMITER;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.CSV_FILE;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.DATA_FILE;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.DOCUMENT;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.EMBEDDED_PROPERTIES;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.EMPTY_STRING;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.EVENT_SCHEMA_ATTRIBUTE;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.GLOSSARY_TERM;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.GLOSSARY_TERM_AND_CLASSIFICATION_EDGES;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.INCOMPLETE;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.JSON_FILE;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.KEYSTORE_FILE;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.LINEAGE_MAPPING;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.LOG_FILE;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.MEDIA_FILE;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.PROCESS;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.RELATIONAL_COLUMN;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.RELATIONAL_COLUMN_AND_CLASSIFICATION_EDGES;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.RELATIONAL_TABLE;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.S;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.SEMANTIC_ASSIGNMENT;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.SUB_GRAPH;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.TABULAR_COLUMN;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.TABULAR_COLUMN_AND_CLASSIFICATION_EDGES;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.TABULAR_FILE_COLUMN;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.TERM_CATEGORIZATION;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.TOPIC;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.GLOSSARY;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.GLOSSARY_CATEGORY;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.CONDENSED_NODE_DISPLAY_NAME;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.DESTINATION_CONDENSATION;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.EDGE_LABEL_CLASSIFICATION;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.EDGE_LABEL_COLUMN_DATA_FLOW;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.EDGE_LABEL_CONDENSED;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.EDGE_LABEL_TABLE_DATA_FLOW;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.EDGE_LABEL_TERM_ANCHOR;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.NODE_LABEL_CONDENSED;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.NODE_LABEL_SUB_PROCESS;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.PROPERTY_KEY_ENTITY_GUID;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.PROPERTY_KEY_INSTANCEPROP_DISPLAY_NAME;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.PROPERTY_KEY_LABEL;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.PROPERTY_KEY_PREFIX_ELEMENT;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.PROPERTY_KEY_PREFIX_VERTEX_INSTANCE_PROPERTY;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.PROPERTY_VALUE_NODE_ID_CONDENSED_DESTINATION;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.PROPERTY_VALUE_NODE_ID_CONDENSED_SOURCE;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.SOURCE_CONDENSATION;

public class LineageGraphQueryService implements OpenLineageQueryService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LineageGraphQueryService.class);

    private final GraphHelper graphHelper;
    private final LineageGraphQueryHelper lineageGraphQueryHelper;
    private final AuditLog auditLog;

    public LineageGraphQueryService(GraphHelper graphHelper, AuditLog auditLog) {
        this.graphHelper = graphHelper;
        this.auditLog = auditLog;
        this.lineageGraphQueryHelper = new LineageGraphQueryHelper(graphHelper);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LineageResponse lineage(Scope scope, String guid, boolean includeProcesses) {

        LineageResponse response = graphHelper.getResult(this::checkEntityExists, guid, this::handleGetQueriedVertexException);
        if (response != null) {
            return response;
        }

        Optional<LineageVerticesAndEdges> lineageVerticesAndEdges = Optional.empty();

        switch (scope) {
            case END_TO_END:
                lineageVerticesAndEdges = endToEnd(guid, includeProcesses);
                break;
            case ULTIMATE_SOURCE:
                lineageVerticesAndEdges = ultimateSource(guid);
                break;
            case ULTIMATE_DESTINATION:
                lineageVerticesAndEdges = ultimateDestination(guid);
                break;
            case VERTICAL:
                lineageVerticesAndEdges = verticalLineage(guid);
                break;
        }
        if (lineageVerticesAndEdges.isEmpty()) {
            return getLineageResponse(guid, ERROR_LINEAGE_NOT_FOUND);
        }
        return new LineageResponse(lineageVerticesAndEdges.orElse(null));
    }

    private LineageResponse checkEntityExists(GraphTraversalSource g, String guid) {
        GraphTraversal<Vertex, Vertex> vertexGraphTraversal = g.V().has(PROPERTY_KEY_ENTITY_GUID, guid);
        if (!vertexGraphTraversal.hasNext()) {
            return getLineageResponse(guid, ERROR_ENTITY_NOT_FOUND);
        }
        return null;
    }

    private LineageResponse getLineageResponse(String guid, OpenLineageServerErrorCode errorEntityNotFound) {
        LineageResponse lineageResponse = new LineageResponse();
        lineageResponse.setRelatedHTTPCode(errorEntityNotFound.getHTTPErrorCode());
        lineageResponse.setExceptionErrorMessage(errorEntityNotFound.getFormattedErrorMessage(guid));
        lineageResponse.setActionDescription(errorEntityNotFound.getUserAction());
        lineageResponse.setExceptionUserAction(errorEntityNotFound.getUserAction());
        lineageResponse.setExceptionErrorMessageId(errorEntityNotFound.getErrorMessageId());
        lineageResponse.setExceptionErrorMessageId(errorEntityNotFound.getErrorMessageId());
        lineageResponse.setExceptionClassName(InvalidParameterException.class.getName());
        return lineageResponse;
    }

    /**
     * Returns the end to end graph of queried entity, which can be a column or a table. In case of tables, relationships
     * of type LineageMapping will be traversed backwards and forwards, all the way to the source and the destination,
     * respectively. If no vertices are found, than DataFlow relationships are used for traversal. In case of columns,
     * DataFlow relationships are directly used
     *
     * @param guid             queried entity
     * @param includeProcesses include processes
     * @return graph in an Open Lineage specific format
     */
    public Optional<LineageVerticesAndEdges> endToEnd(String guid, boolean includeProcesses) {

        Vertex queriedVertex = this.graphHelper.getResult(this::getQueriedVertex, guid, this::handleGetQueriedVertexException);
        String label = queriedVertex.label();

        Optional<List<String>> edgeLabelsOptional = getEdgeLabelsForDataFlow(label);
        if (edgeLabelsOptional.isEmpty()) {
            return Optional.empty();
        }
        List<String> edgeLabels = edgeLabelsOptional.get();

        Graph endToEndGraph = this.graphHelper.getResult(this::queryEndToEnd, guid, edgeLabels, this::handleLineageNotFoundException);
        if (endToEndGraph == null || !endToEndGraph.vertices().hasNext()) {
            return Optional.empty();
        }

        LineageVerticesAndEdges lineageVerticesAndEdges = this.lineageGraphQueryHelper.getLineageVerticesAndEdges(endToEndGraph, includeProcesses);
        addIncompleteClassifications(lineageVerticesAndEdges);
        this.lineageGraphQueryHelper.addColumnProperties(lineageVerticesAndEdges);
        return Optional.of(lineageVerticesAndEdges);
    }

    /**
     * Queries graph for end to end
     *
     * @param guid       queried entity
     * @param edgeLabels edge type to traverse
     * @return graph
     */
    private Graph queryEndToEnd(GraphTraversalSource g, String guid, List<String> edgeLabels) {
        String[] labels = edgeLabels.toArray(new String[0]);
        return (Graph) g.V().has(PROPERTY_KEY_ENTITY_GUID, guid).
                union(until(inE(labels).count().is(0)).
                                repeat((Traversal) inE(labels).subgraph(SUB_GRAPH).outV().simplePath().dedup()),
                        until(outE(labels).count().is(0)).
                                repeat((Traversal) outE(labels).subgraph(SUB_GRAPH).inV().simplePath().dedup())
                ).cap(SUB_GRAPH).next();
    }

    private void handleLineageNotFoundException(Exception e, String guid, List<String> edgeLabels) {
        auditLog.logException(LINEAGE_NOT_FOUND.getFormattedErrorMessage(guid, edgeLabels.toString()), LINEAGE_NOT_FOUND.getMessageDefinition(guid, edgeLabels.toString()), e);
    }

    private void handleTypesNotFoundException(Exception e) {
        auditLog.logException(TYPES_NOT_FOUND.getErrorMessage(), TYPES_NOT_FOUND.getMessageDefinition(), e);
    }

    private void handleNodeNamesNotFoundException(Exception e, NodeNamesSearchCriteria searchCriteria) {
        auditLog.logException(NODES_NOT_FOUND.getFormattedErrorMessage(searchCriteria.getType(), searchCriteria.getSearchValue()),
                NODES_NOT_FOUND.getMessageDefinition(), e);
    }

    /**
     * Returns the ultimate source graph of queried entity, which can be a column or a table. In case of tables,
     * relationships of type LineageMapping will be traversed backwards, all the way to the source. If no vertices are
     * found, than DataFlow relationships are used for traversal. In case of columns, DataFlow relationships are
     * directly used
     *
     * @param guid queried entity
     * @return graph in an Open Lineage specific format
     */
    public Optional<LineageVerticesAndEdges> ultimateSource(String guid) {

        Vertex queriedVertex = this.graphHelper.getResult(this::getQueriedVertex, guid, this::handleGetQueriedVertexException);
        String label = queriedVertex.label();

        Optional<List<String>> edgeLabelsOptional = getEdgeLabelsForDataFlow(label);

        if (edgeLabelsOptional.isEmpty()) {
            return Optional.empty();
        }
        List<String> edgeLabels = edgeLabelsOptional.get();
        List<Vertex> sourcesList = this.graphHelper.getResult(this::querySources, guid, edgeLabels, this::handleLineageNotFoundException);
        Set<LineageVertex> lineageVertices = this.lineageGraphQueryHelper.getLineageVertices(sourcesList);
        return Optional.of(getCondensedLineage(queriedVertex, lineageVertices, SOURCE_CONDENSATION));
    }

    /**
     * Query graph for sources
     *
     * @param guid       entity
     * @param edgeLabels edge type to traverse
     * @return sources
     */
    private List<Vertex> querySources(GraphTraversalSource g, String guid, List<String> edgeLabels) {
        String[] labels = edgeLabels.toArray(new String[0]);
        List<Vertex> sourceList = g.V().has(PROPERTY_KEY_ENTITY_GUID, guid).
                until(inE(labels).count().is(0)).
                repeat(inE(labels).outV().simplePath().dedup()).
                dedup().toList();
        return sourceList;
    }

    /**
     * Returns the ultimate destination graph of queried entity, which can be a column or a table. In case of tables,
     * relationships of type LineageMapping will be traversed forwards, all the way to the destination. If no vertices
     * are found, than DataFlow relationships are used for traversal. In case of columns, DataFlow relationships are
     * directly used
     *
     * @param guid queried entity
     * @return graph in an Open Lineage specific format
     */
    public Optional<LineageVerticesAndEdges> ultimateDestination(String guid) {
        Vertex queriedVertex = graphHelper.getResult(this::getQueriedVertex, guid, this::handleGetQueriedVertexException);
        String label = queriedVertex.label();
        Optional<List<String>> edgeLabelsOptional = getEdgeLabelsForDataFlow(label);
        if (edgeLabelsOptional.isEmpty()) {
            return Optional.empty();
        }
        List<String> edgeLabels = edgeLabelsOptional.get();
        List<Vertex> destinationsList = graphHelper.getResult(this::queryDestinations, guid, edgeLabels, this::handleLineageNotFoundException);
        Set<LineageVertex> lineageVertices = this.lineageGraphQueryHelper.getLineageVertices(destinationsList);
        return Optional.of(getCondensedLineage(queriedVertex, lineageVertices, DESTINATION_CONDENSATION));

    }

    /**
     * Query graph for destinations
     *
     * @param guid       entity
     * @param edgeLabels edge type to traverse
     * @return sources
     */
    private List<Vertex> queryDestinations(GraphTraversalSource g, String guid, List<String> edgeLabels) {
        String[] labels = edgeLabels.toArray(new String[0]);
        List<Vertex> destinationList = g.V().has(PROPERTY_KEY_ENTITY_GUID, guid).
                until(outE(labels).count().is(0)).
                repeat(outE(labels).inV().simplePath().dedup()).
                dedup().toList();

        return destinationList;
    }

    /**
     * Returns a subgraph navigating the edges of interest based on target node type. For more info, check
     * {@link #glossaryTermVerticalLineage}, {@link #tabularColumnVerticalLineage}, {@link #relationalColumnVerticalLineage}
     *
     * @param guid guid to extract vertical lineage for
     * @return a subgraph in an Open Lineage specific format
     */
    public Optional<LineageVerticesAndEdges> verticalLineage(String guid) {
        Vertex queriedVertex = this.graphHelper.getResult(this::getQueriedVertex, guid, this::handleGetQueriedVertexException);
        String label = queriedVertex.label();
        Graph graph;
        switch (label) {
            case GLOSSARY:
                graph = this.graphHelper.getResult(this::glossaryVerticalLineage, guid, this::handleVerticalLineageNotFoundException);
                break;
            case GLOSSARY_CATEGORY:
            case GLOSSARY_TERM:
                graph = this.graphHelper.getResult(this::glossaryTermVerticalLineage, guid, this::handleVerticalLineageNotFoundException);
                break;
            case RELATIONAL_COLUMN:
                graph = this.graphHelper.getResult(this::relationalColumnVerticalLineage, guid, this::handleVerticalLineageNotFoundException);
                break;
            case TABULAR_COLUMN:
            case TABULAR_FILE_COLUMN:
                graph = this.graphHelper.getResult(this::tabularColumnVerticalLineage, guid, this::handleVerticalLineageNotFoundException);
                break;
            default:
                return Optional.empty();
        }
        LineageVerticesAndEdges lineageVerticesAndEdges = this.lineageGraphQueryHelper.getLineageVerticesAndEdges(graph, true);
        addIncompleteClassifications(lineageVerticesAndEdges);
        this.lineageGraphQueryHelper.addColumnProperties(lineageVerticesAndEdges);
        return Optional.of(lineageVerticesAndEdges);
    }

    private Vertex getQueriedVertex(GraphTraversalSource g, String guid) {
        Vertex queriedVertex = g.V().has(PROPERTY_KEY_ENTITY_GUID, guid).next();
        if (queriedVertex != null) {
            return queriedVertex;
        }
        return null;
    }

    private void handleGetQueriedVertexException(Exception e, String guid) {
        auditLog.logException(COULD_NOT_RETRIEVE_VERTEX.getFormattedErrorMessage(guid), COULD_NOT_RETRIEVE_VERTEX.getMessageDefinition(guid), e);
        throw new JanusConnectorException(this.getClass().getName(), "getQueriedVertex", COULD_NOT_RETRIEVE_VERTEX);
    }

    /**
     * Returns a subgraph by navigating {@link org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants#EDGE_LABEL_TERM_ANCHOR}
     *
     * @param guid guid to extract vertical lineage for
     * @return a subgraph in an Open Lineage specific format.
     */
    private Graph glossaryVerticalLineage(GraphTraversalSource g, String guid) {
        return (Graph) g.V().has(PROPERTY_KEY_ENTITY_GUID, guid).bothE(EDGE_LABEL_TERM_ANCHOR)
                .subgraph(S).cap(S).next();
    }

    /**
     * Returns a subgraph by navigating edges specified in {@link org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants#GLOSSARY_TERM_AND_CLASSIFICATION_EDGES},
     * like semantic assignments and various relationships between glossary terms. Classifications are included
     *
     * @param guid guid to extract vertical lineage for
     * @return a subgraph in an Open Lineage specific format.
     */
    private Graph glossaryTermVerticalLineage(GraphTraversalSource g, String guid) {
        return (Graph) g.V().has(PROPERTY_KEY_ENTITY_GUID, guid).bothE(GLOSSARY_TERM_AND_CLASSIFICATION_EDGES)
                .subgraph(S).cap(S).next();
    }

    /**
     * Returns a subgraph by navigating edges specified in {@link org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants#RELATIONAL_COLUMN_AND_CLASSIFICATION_EDGES},
     * like semantic assignments. Classifications are included
     *
     * @param guid guid to extract vertical lineage for
     * @return a subgraph in an Open Lineage specific format.
     */
    private Graph relationalColumnVerticalLineage(GraphTraversalSource g, String guid) {
        return (Graph) g.V().has(PROPERTY_KEY_ENTITY_GUID, guid).bothE(RELATIONAL_COLUMN_AND_CLASSIFICATION_EDGES)
                .subgraph("s").cap("s").next();

    }

    /**
     * Returns a subgraph by navigating edges specified in {@link org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants#TABULAR_COLUMN_AND_CLASSIFICATION_EDGES},
     * like semantic assignments. Classifications are included
     *
     * @param guid guid to extract vertical lineage for
     * @return a subgraph in an Open Lineage specific format.
     */
    private Graph tabularColumnVerticalLineage(GraphTraversalSource g, String guid) {
        return (Graph) g.V().has(PROPERTY_KEY_ENTITY_GUID, guid).bothE(TABULAR_COLUMN_AND_CLASSIFICATION_EDGES)
                .subgraph("s").bothV().inE(ASSET_SCHEMA_TYPE).subgraph("s").cap("s").next();
    }

    private void handleVerticalLineageNotFoundException(Exception e, String guid) {
        auditLog.logException(LINEAGE_NOT_FOUND.getFormattedErrorMessage(guid), LINEAGE_NOT_FOUND.getMessageDefinition(guid), e);
    }

    /**
     * * Returns a subgraph containing all root and leaf nodes of the full graph that are connected with the queried node.
     * * The queried node can be a column or table.
     *
     * @param originalVertex   the starting vertex
     * @param lineageVertices  list of ultimate vertices
     * @param condensationType the type of the condensation
     * @return the subgraph in an Open Lineage specific format
     */
    private LineageVerticesAndEdges getCondensedLineage(Vertex originalVertex, Set<LineageVertex> lineageVertices,
                                                        String condensationType) {

        LineageVertex queriedVertex = this.lineageGraphQueryHelper.abstractVertex(originalVertex);

        Set<LineageEdge> lineageEdges = new HashSet<>();
        if (CollectionUtils.isNotEmpty(lineageVertices) && !Collections.singleton(queriedVertex).equals(lineageVertices)) {
            LineageVertex condensedVertex = getCondensedVertex(condensationType);
            lineageVertices.add(condensedVertex);
            lineageEdges = getCondensedLineageEdges(lineageVertices, queriedVertex, condensedVertex, condensationType);
            lineageVertices.add(queriedVertex);
        }
        LineageVerticesAndEdges lineageVerticesAndEdges = new LineageVerticesAndEdges(lineageVertices, lineageEdges);
        this.lineageGraphQueryHelper.addColumnProperties(lineageVerticesAndEdges);

        addIncompleteClassifications(lineageVerticesAndEdges);

        return lineageVerticesAndEdges;
    }

    private Set<LineageEdge> getCondensedLineageEdges(Set<LineageVertex> lineageVertices, LineageVertex queriedVertex,
                                                      LineageVertex condensedVertex, String condensationType) {
        Set<LineageEdge> lineageEdges = new HashSet<>();
        AtomicInteger counter = new AtomicInteger(0);

        if (SOURCE_CONDENSATION.equalsIgnoreCase(condensationType)) {
            lineageEdges.add(new LineageEdge(EDGE_LABEL_CONDENSED + "--" + counter.getAndIncrement(), EDGE_LABEL_CONDENSED,
                    condensedVertex.getNodeID(), queriedVertex.getNodeID()));
            lineageVertices.forEach(ultimateVertex -> lineageEdges.add(new LineageEdge(EDGE_LABEL_CONDENSED + "--" + counter.getAndIncrement(),
                    EDGE_LABEL_CONDENSED, ultimateVertex.getNodeID(), condensedVertex.getNodeID())));
        }
        if (DESTINATION_CONDENSATION.equalsIgnoreCase(condensationType)) {
            lineageEdges.add(new LineageEdge(EDGE_LABEL_CONDENSED + "--" + counter.getAndIncrement(), EDGE_LABEL_CONDENSED,
                    queriedVertex.getNodeID(), condensedVertex.getNodeID()));
            lineageVertices.forEach(ultimateVertex -> lineageEdges.add(new LineageEdge(EDGE_LABEL_CONDENSED + "--" + counter.getAndIncrement(),
                    EDGE_LABEL_CONDENSED, condensedVertex.getNodeID(), ultimateVertex.getNodeID())));
        }
        return lineageEdges;
    }

    private LineageVertex getCondensedVertex(String condensationType) {
        LineageVertex condensedVertex = new LineageVertex(getCondensedNodeId(condensationType), NODE_LABEL_CONDENSED);
        condensedVertex.setDisplayName(CONDENSED_NODE_DISPLAY_NAME);
        condensedVertex.setQualifiedName("");
        condensedVertex.setGuid("");
        return condensedVertex;
    }

    private void addIncompleteClassifications(LineageVerticesAndEdges edgesAndVertices) {
        Set<LineageVertex> lineageVertices = edgesAndVertices.getLineageVertices();
        Set<LineageEdge> lineageEdges = edgesAndVertices.getLineageEdges();

        List<Object> ids = lineageVertices.stream().map(LineageVertex::getId).collect(Collectors.toList());

        Graph graph = this.graphHelper.getResult(this::getIncompleteClassifications, ids, this::handleIncompleteClassificationException);

        Set<LineageVertex> incompleteVertices = new HashSet<>(this.lineageGraphQueryHelper.getLineageVertices(graph));
        Set<LineageEdge> incompleteEdges = new HashSet<>(this.lineageGraphQueryHelper.getLineageEdges(graph));

        // The processed vertices don't always have all properties, so here we need to check for the node id to avoid duplicates.
        // equals from LineageVertex takes into account all properties
        for (LineageVertex incompleteVertex : incompleteVertices) {
            Optional<LineageVertex> optionalLineageVertex = lineageVertices.stream()
                    .filter(lineageVertex -> lineageVertex.getNodeID().equals(incompleteVertex.getNodeID())).findAny();
            if (optionalLineageVertex.isEmpty()) {
                lineageVertices.add(incompleteVertex);
            }
        }
        lineageEdges.addAll(incompleteEdges);
    }

    private Graph getIncompleteClassifications(GraphTraversalSource g, List<Object> ids) {
        return (Graph) g.V(ids).outE(EDGE_LABEL_CLASSIFICATION).inV().hasLabel(INCOMPLETE).bothE()
                .subgraph(CLASSIFICATION_GRAPH).cap(CLASSIFICATION_GRAPH)
                .next();
    }

    private void handleIncompleteClassificationException(Exception e, List<Object> vertexIds) {
        auditLog.logException(CLASSIFICATION_NOT_FOUND.getFormattedErrorMessage(vertexIds.toString()), CLASSIFICATION_NOT_FOUND.getMessageDefinition(vertexIds.toString()), e);
    }

    private String getCondensedNodeId(String condensationType) {
        if (SOURCE_CONDENSATION.equalsIgnoreCase(condensationType)) {
            return PROPERTY_VALUE_NODE_ID_CONDENSED_SOURCE;
        } else {
            return PROPERTY_VALUE_NODE_ID_CONDENSED_DESTINATION;
        }
    }

    private Optional<List<String>> getEdgeLabelsForDataFlow(String label) {
        List<String> edgeLabels = new ArrayList<>();
        if (ASSETS.contains(label)) {
            edgeLabels.add(LINEAGE_MAPPING);
        }
        switch (label) {
            case TABULAR_FILE_COLUMN:
            case TABULAR_COLUMN:
            case RELATIONAL_COLUMN:
            case EVENT_SCHEMA_ATTRIBUTE:
                edgeLabels.add(EDGE_LABEL_COLUMN_DATA_FLOW);
                break;
            case DATA_FILE:
            case AVRO_FILE:
            case CSV_FILE:
            case JSON_FILE:
            case KEYSTORE_FILE:
            case LOG_FILE:
            case MEDIA_FILE:
            case DOCUMENT:
            case RELATIONAL_TABLE:
            case TOPIC:
                edgeLabels.add(EDGE_LABEL_TABLE_DATA_FLOW);
                break;
            case PROCESS:
                edgeLabels.add(EDGE_LABEL_COLUMN_DATA_FLOW);
                break;
            default:
                return Optional.empty();
        }
        return Optional.of(edgeLabels);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LineageVertexResponse getEntityDetails(String guid) {
        LineageVertex lineageVertex = this.graphHelper.getResult(this::getLineageVertexByGuid, guid, this::handleGetQueriedVertexException);
        if (lineageVertex.getGuid() == null) {
            LineageVertexResponse lineageResponse = new LineageVertexResponse();
            lineageResponse.setRelatedHTTPCode(ERROR_ENTITY_NOT_FOUND.getHTTPErrorCode());
            lineageResponse.setExceptionErrorMessage(ERROR_ENTITY_NOT_FOUND.getFormattedErrorMessage(guid));
            lineageResponse.setActionDescription(ERROR_ENTITY_NOT_FOUND.getUserAction());
            lineageResponse.setExceptionUserAction(ERROR_ENTITY_NOT_FOUND.getUserAction());
            lineageResponse.setExceptionErrorMessageId(ERROR_ENTITY_NOT_FOUND.getErrorMessageId());
            lineageResponse.setExceptionErrorMessageId(ERROR_ENTITY_NOT_FOUND.getErrorMessageId());
            lineageResponse.setExceptionClassName(InvalidParameterException.class.getName());
            return lineageResponse;
        }
        return new LineageVertexResponse(lineageVertex);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LineageTypesResponse getTypes() {
        List<String> types = this.graphHelper.getResult(this::getTypes, this::handleTypesNotFoundException);
        LineageTypesResponse typesResponse = new LineageTypesResponse();

        if(CollectionUtils.isEmpty(types)) {
            typesResponse.setRelatedHTTPCode(ERROR_TYPES_NOT_FOUND.getHTTPErrorCode());
            typesResponse.setExceptionErrorMessage(ERROR_TYPES_NOT_FOUND.getFormattedErrorMessage());
            typesResponse.setActionDescription(ERROR_TYPES_NOT_FOUND.getUserAction());
            typesResponse.setExceptionUserAction(ERROR_TYPES_NOT_FOUND.getUserAction());
            typesResponse.setExceptionErrorMessageId(ERROR_TYPES_NOT_FOUND.getErrorMessageId());
            typesResponse.setExceptionClassName(OpenLineageException.class.getName());
            return typesResponse;
        }

        typesResponse.setTypes(types);
        return typesResponse;
    }

    private List<String> getTypes(GraphTraversalSource g) {
       return g.V().not(hasLabel(ASSET_LINEAGE_VARIABLES, NODE_LABEL_SUB_PROCESS)).label().dedup().toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LineageNodeNamesResponse getNodes(NodeNamesSearchCriteria searchCriteria) {
        List<String> nodeNames = this.graphHelper.getResult(this::getNodes, searchCriteria,
                this::handleNodeNamesNotFoundException);
        LineageNodeNamesResponse nodeNamesResponse = new LineageNodeNamesResponse();
        nodeNamesResponse.setNames(nodeNames);
        return nodeNamesResponse;
    }

    private List<String> getNodes(GraphTraversalSource g, NodeNamesSearchCriteria searchCriteria) {
        return g.V().has(PROPERTY_KEY_LABEL, searchCriteria.getType()).values(PROPERTY_KEY_INSTANCEPROP_DISPLAY_NAME)
                .filter(x -> x.toString().toLowerCase().contains(searchCriteria.getSearchValue().toLowerCase()))
                .limit(searchCriteria.getLimit()).map(Object::toString).toList();

    }

    /**
     * Gets lineage vertex by guid.
     *
     * @param guid the guid
     * @return the lineage vertex by guid
     */
    LineageVertex getLineageVertexByGuid(GraphTraversalSource g, String guid) {

        GraphTraversal<Vertex, Vertex> vertexGraphTraversal = g.V().has(PROPERTY_KEY_ENTITY_GUID, guid);

        Map<String, String> properties = new HashMap<>();
        if (vertexGraphTraversal.hasNext()) {
            Vertex vertex = vertexGraphTraversal.next();
            properties = retrieveAllProperties(vertex);
        }

        LineageVertex lineageVertex = new LineageVertex();
        lineageVertex.setProperties(properties);
        return lineageVertex;
    }

    /**
     * Retrieve all properties of the vertex from the db without filtering.
     *
     * @param vertex the vertex to de mapped
     * @return the filtered properties of the vertex
     */
    private Map<String, String> retrieveAllProperties(Vertex vertex) {

        Map<String, String> newNodeProperties = new HashMap<>();
        Iterator<VertexProperty<Object>> originalProperties = vertex.properties();

        while (originalProperties.hasNext()) {
            Property<Object> originalProperty = originalProperties.next();

            String newPropertyKey = originalProperty.key().
                    replace(PROPERTY_KEY_PREFIX_VERTEX_INSTANCE_PROPERTY, EMPTY_STRING).
                    replace(PROPERTY_KEY_PREFIX_ELEMENT, EMPTY_STRING);

            String newPropertyValue = originalProperty.value().toString();

            if (EMBEDDED_PROPERTIES.contains(newPropertyKey)) {
                String[] propertyPairs = newPropertyValue.split(COMMA_SPACE_DELIMITER);
                for (String propertyPair : propertyPairs) {
                    String[] propertyItems = propertyPair.split(COLUMN_SPACE_DELIMITER);
                    newNodeProperties.put(propertyItems[0], propertyItems[1]);
                }
            } else {
                newNodeProperties.put(newPropertyKey, newPropertyValue);
            }
        }
        return newNodeProperties;
    }

    /**
     * Search the database for entities matching the search request
     *
     * @param lineageSearchRequest the criteria for the search
     * @return all the entities in the graph that match the criteria
     */
    public LineageSearchResponse search(LineageSearchRequest lineageSearchRequest) {
        List<LineageVertex> result = this.graphHelper.getResult(this::getSearchResult, lineageSearchRequest, this::handleSearchError);
        return new LineageSearchResponse(result);
    }

    private List<LineageVertex> getSearchResult(GraphTraversalSource g, LineageSearchRequest lineageSearchRequest) {
        Node queriedNode = lineageSearchRequest.getQueriedNode();
        List<Node> relatedNodes = lineageSearchRequest.getRelatedNodes();
        GraphTraversal<Vertex, Vertex> searchTraversal = g.V().has(PROPERTY_KEY_LABEL, queriedNode.getType());

        if (!StringUtils.isEmpty(queriedNode.getName())) {
            searchTraversal = searchTraversal.and(__.has(PROPERTY_KEY_INSTANCEPROP_DISPLAY_NAME, queriedNode.getName()));
        }
        List<String> edges;
        if (GLOSSARY_TERM.equalsIgnoreCase(queriedNode.getType())) {
            edges = Arrays.asList(SEMANTIC_ASSIGNMENT, TERM_CATEGORIZATION);
        } else {
            edges = List.of(LINEAGE_MAPPING);
        }
        searchTraversal = buildQueryWithRelatedNodes(searchTraversal, relatedNodes, edges);
        List<Vertex> results = searchTraversal.toList();
        return results.stream().map(lineageGraphQueryHelper::abstractVertex).collect(Collectors.toList());
    }

    /**
     * Add to the query the related nodes to filter results from graph
     *
     * @param searchTraversal existing query
     * @param relatedNodes    list of nodes that should be related to what is searched
     * @param edges           list of labels to travers the graph
     * @return the traversal with the added filtering needed
     */
    private GraphTraversal<Vertex, Vertex> buildQueryWithRelatedNodes(GraphTraversal<Vertex, Vertex> searchTraversal, List<Node> relatedNodes, List<String> edges) {
        if (CollectionUtils.isEmpty(relatedNodes)) {
            return searchTraversal;
        }
        String[] edgeLabels = edges.toArray(new String[0]);
        for (Node node : relatedNodes) {
            if (!StringUtils.isEmpty(node.getName())) {
                searchTraversal = searchTraversal.and(
                        __.repeat(__.both(edgeLabels).dedup())
                                .until(__.and(__.has(PROPERTY_KEY_LABEL, node.getType()), __.has(PROPERTY_KEY_INSTANCEPROP_DISPLAY_NAME, node.getName()))));
            } else {
                searchTraversal = searchTraversal.and(__.repeat(__.both(edgeLabels).dedup()).until(__.has(PROPERTY_KEY_LABEL, node.getType())));
            }
        }
        return searchTraversal;
    }

    private void handleSearchError(Exception e, LineageSearchRequest lineageSearchRequest) {
        auditLog.logException(SEARCH_ERROR.getFormattedErrorMessage(), SEARCH_ERROR.getMessageDefinition(lineageSearchRequest.toString()), e);
    }

}
