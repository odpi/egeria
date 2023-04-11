/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.graph;

import org.apache.commons.lang3.StringUtils;
import org.apache.tinkerpop.gremlin.process.traversal.P;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.model.SubProcessDetails;
import org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.model.ffdc.JanusConnectorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.bothE;
import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.hasLabel;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.graph.LineageGraphConnector.INPUT_PORT;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.graph.LineageGraphConnector.OLS_HAS_CORRESPONDING_ELEMENTS;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.graph.LineageGraphConnector.VERTEX_NOT_FOUND;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.model.JanusConnectorErrorCode.PROCESS_MAPPING_ERROR;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.model.JanusConnectorErrorCode.VERTICES_AND_RELATIONSHIP_CREATION_EXCEPTION;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.ASSET_SCHEMA_TYPE;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.ATTRIBUTE_FOR_SCHEMA;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.DATA_FILE_AND_SUBTYPES;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.DATA_FLOW;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.EVENT_SCHEMA_ATTRIBUTE;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.NESTED_SCHEMA_ATTRIBUTE;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.PORT_IMPLEMENTATION;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.PORT_SCHEMA;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.PROCESS;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.PROCESS_HIERARCHY;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.PROCESS_PORT;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.RELATIONAL_COLUMN;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.RELATIONAL_TABLE;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.SCHEMA_TYPE_OPTION;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.TABULAR_COLUMN;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.TABULAR_FILE_COLUMN;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.TOPIC;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.EDGE_LABEL_COLUMN_DATA_FLOW;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.EDGE_LABEL_INCLUDED_IN;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.EDGE_LABEL_TABLE_DATA_FLOW;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.NODE_LABEL_SUB_PROCESS;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.PROPERTY_KEY_COLUMN_IN_GUID;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.PROPERTY_KEY_COLUMN_OUT_GUID;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.PROPERTY_KEY_DISPLAY_NAME;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.PROPERTY_KEY_ENTITY_GUID;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.PROPERTY_KEY_ENTITY_NODE_ID;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.PROPERTY_KEY_INSTANCEPROP_DISPLAY_NAME;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.PROPERTY_KEY_LABEL;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.PROPERTY_KEY_PROCESS_GUID;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.PROPERTY_NAME_PORT_TYPE;

public class LineageJobHelper {

    private static final Logger log = LoggerFactory.getLogger(LineageJobHelper.class);
    public static final String SOMETHING_WENT_WRONG_WHEN_TRYING_TO_MAP_A_PROCESS = "Something went wrong when trying to map a process.";
    public static final String SOMETHING_WENT_WRONG_WHEN_TRYING_TO_MAP_A_PROCESS_THE_ERROR_IS = SOMETHING_WENT_WRONG_WHEN_TRYING_TO_MAP_A_PROCESS + " The error is: ";

    private GraphHelper graphHelper;
    private AuditLog auditLog;

    public LineageJobHelper(GraphHelper graphHelper, AuditLog auditLog) {
        this.graphHelper = graphHelper;
        this.auditLog = auditLog;
    }

    public void performLineageGraphJob() {
        try {
            //TODO investigate possibility of adding the PROPERTY_KEY_PROCESS_LINEAGE_COMPLETED_FLAG again
            List<String> guidList = this.graphHelper.getResult(this::getProcessGuids, this::handleRetrieveProcessGuids);
            for (String guid : guidList) {
                findInputColumns(guid);
            }
        } catch (Exception e) {
            log.error(SOMETHING_WENT_WRONG_WHEN_TRYING_TO_MAP_A_PROCESS_THE_ERROR_IS, e);
            auditLog.logException(SOMETHING_WENT_WRONG_WHEN_TRYING_TO_MAP_A_PROCESS, PROCESS_MAPPING_ERROR.getMessageDefinition(), e);
        }
    }

    private List<String> getProcessGuids(GraphTraversalSource g) {
        List<String> guidList = new ArrayList<>();
        List<Vertex> vertices = g.V().has(PROPERTY_KEY_LABEL, PROCESS).toList();
        for (Vertex v : vertices) {
            String s = g.V(v.id()).elementMap(PROPERTY_KEY_ENTITY_GUID).toList().get(0).get(PROPERTY_KEY_ENTITY_GUID).toString();
            guidList.add(s);
        }
        return guidList;
    }

    /**
     * Finds the paths to the input columns from all the processes in the graph.
     *
     * @param guid - The unique identifier of a Process
     */
    private void findInputColumns(String guid) {
        List<Vertex> inputPathsForColumns = this.graphHelper.getResult(this::getInputPathsForColumns, guid, this::handleRetrieveResultError);

        Vertex process = this.graphHelper.getResult(this::getNodeByGuid, guid, this::handleRetrieveResultError);
        inputPathsForColumns.forEach(columnIn -> findOutputColumns(columnIn, process));
    }

    private Vertex getNodeByGuid(GraphTraversalSource g, String guid) {
        return g.V().has(PROPERTY_KEY_ENTITY_GUID, guid).next();
    }

    private List<Vertex> getInputPathsForColumns(GraphTraversalSource g, String guid) {
        List<Vertex> inputPathsForColumns = g.V().has(PROPERTY_KEY_ENTITY_GUID, guid).out(PROCESS_HIERARCHY).out(PROCESS_PORT)
                .has(PORT_IMPLEMENTATION, PROPERTY_NAME_PORT_TYPE, INPUT_PORT)
                .out(PORT_SCHEMA).out(ATTRIBUTE_FOR_SCHEMA).in(DATA_FLOW)
                .or(__.in(ATTRIBUTE_FOR_SCHEMA).in(ASSET_SCHEMA_TYPE).has(PROPERTY_KEY_LABEL, P.within(DATA_FILE_AND_SUBTYPES)),
                        __.in(NESTED_SCHEMA_ATTRIBUTE).has(PROPERTY_KEY_LABEL, RELATIONAL_TABLE),
                        __.in(ATTRIBUTE_FOR_SCHEMA).in(SCHEMA_TYPE_OPTION).in(ASSET_SCHEMA_TYPE).has(PROPERTY_KEY_LABEL, TOPIC)).toList();
        return inputPathsForColumns;
    }

    /**
     * Finds the output columns of a Process based on the input.
     *
     * @param columnIn - THe vertex of the schema element before processing.
     * @param process  - The vertex of the process.
     */
    private void findOutputColumns(Vertex columnIn, Vertex process) {
        List<Vertex> schemaElementVertices = this.graphHelper.getResult(this::getSchemaElementVertices, columnIn, this::handleRetrieveResultError);

        Vertex vertexToStart;
        if (schemaElementVertices != null) {
            List<Vertex> columnOutList = new ArrayList<>();
            for (Vertex schemaElementVertex : schemaElementVertices) {
                vertexToStart = this.graphHelper.getResult(this::isSchemaElementLinkedToProcess, schemaElementVertex, process, this::handleRetrieveResultError);
                if (vertexToStart != null) {
                    columnOutList.addAll(this.graphHelper.getResult(this::findPathForOutputAsset, vertexToStart, columnIn, this::handleRetrieveResultError));
                }
                for (Vertex columnOut : columnOutList) {
                    addNodesAndEdgesForQuerying(columnIn, columnOut, process);
                }
            }
        }
    }

    private List<Vertex> getSchemaElementVertices(GraphTraversalSource g, Vertex columnIn) {
        List<Vertex> schemaElementVertices = g.V()
                .has(PROPERTY_KEY_ENTITY_GUID, g.V(columnIn.id()).elementMap(PROPERTY_KEY_ENTITY_GUID).toList().get(0).get(PROPERTY_KEY_ENTITY_GUID))
                .out(DATA_FLOW)
                .toList();
        return schemaElementVertices;
    }

    /**
     * Returns true if the schemaElementVertex is linked to a process using the lineage related relationships
     *
     * @param g                   - Graph traversal object
     * @param schemaElementVertex - THe vertex of the column before processing.
     * @param process             - The vertex of the process.
     * @return Return the vertex of the initial column
     */
    private Vertex isSchemaElementLinkedToProcess(GraphTraversalSource g, Vertex schemaElementVertex, Vertex process) {
        List<Vertex> initialProcess = g.V(schemaElementVertex.id())
                .bothE(ATTRIBUTE_FOR_SCHEMA)
                .otherV().inE(PORT_SCHEMA).otherV()
                .inE(PROCESS_PORT).otherV()
                .inE(PROCESS_HIERARCHY).otherV()
                .has(PROPERTY_KEY_ENTITY_GUID,
                        g.V(process.id()).elementMap(PROPERTY_KEY_ENTITY_GUID).toList().get(0).get(PROPERTY_KEY_ENTITY_GUID)).toList();


        if (!initialProcess.isEmpty()) {
            return schemaElementVertex;
        }
        return null;
    }

    /**
     * Returns a list of vertices of output schema elements
     *
     * @param endingVertex   - The vertex that is being checked if it is the output schema element
     * @param g              - Graph traversal object
     * @param startingVertex - The vertex of the input schema element
     * @return Return a list of vertices of output schema elements
     */
    private List<Vertex> findPathForOutputAsset(GraphTraversalSource g, Vertex endingVertex, Vertex startingVertex) {
        //add null check for endingVertex
        if (endingVertex == null) {
            return null;
        }

        List<Vertex> endVertices = new ArrayList<>();
        try {
            if (isEndColumn(g, endingVertex)) {
                endVertices.add(endingVertex);
            } else {
                List<Vertex> nextVertices = g.V(endingVertex.id()).out(DATA_FLOW).toList();

                for (Vertex vertex : nextVertices) {
                    if (vertex.equals(startingVertex)) {
                        continue;
                    }
                    Optional.ofNullable(findPathForOutputAsset(g, vertex, endingVertex)).ifPresent(endVertices::addAll);
                }

            }
            return endVertices;
        } catch (Exception e) {
            if (log.isDebugEnabled()) {
                log.debug(VERTEX_NOT_FOUND, startingVertex.id(),
                        startingVertex.property(PROPERTY_KEY_DISPLAY_NAME).value());
            }
            return null;
        }
    }

    /**
     * Returns true if the vertex is part of an asset
     *
     * @param g      - Graph traversal object
     * @param vertex - The vertex of the schema element
     */
    private boolean isEndColumn(GraphTraversalSource g, Vertex vertex) {
        final String VERTEX = "vertex";

        Iterator<Vertex> end = g.V(vertex.id())
                .or(__.in(ATTRIBUTE_FOR_SCHEMA).in(ASSET_SCHEMA_TYPE).has(PROPERTY_KEY_LABEL, P.within(DATA_FILE_AND_SUBTYPES))
                                .aggregate(org.apache.tinkerpop.gremlin.process.traversal.Scope.local, VERTEX),
                        __.in(NESTED_SCHEMA_ATTRIBUTE).has(PROPERTY_KEY_LABEL, RELATIONAL_TABLE).
                                aggregate(org.apache.tinkerpop.gremlin.process.traversal.Scope.local, VERTEX),
                        __.in(ATTRIBUTE_FOR_SCHEMA).in(SCHEMA_TYPE_OPTION).in(ASSET_SCHEMA_TYPE).has(PROPERTY_KEY_LABEL, TOPIC)
                                .aggregate(org.apache.tinkerpop.gremlin.process.traversal.Scope.local, VERTEX))
                .select(VERTEX).unfold();

        return end.hasNext();
    }

    /**
     * Add nodes and edges that are going to be used for lineage UI
     *
     * @param columnIn  - The vertex of the input schema element
     * @param columnOut - THe vertex of the output schema element
     * @param process   - The vertex of the process.
     */
    private void addNodesAndEdgesForQuerying(Vertex columnIn, Vertex columnOut, Vertex process) {
        if (isColumnEmpty(columnIn) || isColumnEmpty(columnOut)) {
            return;
        }
        final SubProcessDetails subProcessDetails = new SubProcessDetails();
        subProcessDetails.setProcessGuid(this.graphHelper.getResult(this::getGuid, process, this::handleRetrieveResultError));
        subProcessDetails.setColumnInGuid(this.graphHelper.getResult(this::getGuid, columnIn, this::handleRetrieveResultError));
        subProcessDetails.setColumnOutGuid(this.graphHelper.getResult(this::getGuid, columnOut, this::handleRetrieveResultError));
        subProcessDetails.setProcessName(this.graphHelper.getResult(this::getProcessName, process, this::handleRetrieveResultError));

        subProcessDetails.setColumnIn(columnIn);
        subProcessDetails.setColumnOut(columnOut);
        subProcessDetails.setProcess(process);


        Iterator<Vertex> existingSubProcess = this.graphHelper.getResult(this::findExistingConnection, subProcessDetails, this::handleFindExistingSubprocess);

        if (!existingSubProcess.hasNext()) {
            this.graphHelper.commit(this::connectNodes, subProcessDetails, this::handleCouldNotAddEdge);
            addAssetToProcessEdges(columnIn, process, columnOut);
            log.info(OLS_HAS_CORRESPONDING_ELEMENTS, subProcessDetails.getColumnInGuid(), subProcessDetails.getColumnOutGuid(), subProcessDetails.getProcessGuid());
        }
    }

    private Iterator<Vertex> findExistingConnection(GraphTraversalSource g, SubProcessDetails subProcessDetails) {
        Iterator<Vertex> existingSubProcess = g.V(subProcessDetails.getColumnIn().id()).outE(EDGE_LABEL_COLUMN_DATA_FLOW).inV()
                .has(PROPERTY_KEY_COLUMN_OUT_GUID, subProcessDetails.getColumnOutGuid())
                .has(PROPERTY_KEY_PROCESS_GUID, subProcessDetails.getProcessGuid());
        return existingSubProcess;
    }

    private void connectNodes(GraphTraversalSource g, SubProcessDetails subProcessDetails) {
        Vertex subProcess = g.addV(NODE_LABEL_SUB_PROCESS)
                .property(PROPERTY_KEY_ENTITY_NODE_ID, UUID.randomUUID().toString())
                .property(PROPERTY_KEY_DISPLAY_NAME, subProcessDetails.getProcessName())
                .property(PROPERTY_KEY_PROCESS_GUID, subProcessDetails.getProcessGuid())
                .property(PROPERTY_KEY_COLUMN_IN_GUID, subProcessDetails.getColumnInGuid())
                .property(PROPERTY_KEY_COLUMN_OUT_GUID, subProcessDetails.getColumnOutGuid())
                .next();

        g.V(subProcessDetails.getColumnIn().id()).addE(EDGE_LABEL_COLUMN_DATA_FLOW).to(__.V(subProcess.id())).next();
        g.V(subProcess.id()).addE(EDGE_LABEL_COLUMN_DATA_FLOW).to(__.V(subProcessDetails.getColumnOut().id())).next();
        g.V(subProcess.id()).addE(EDGE_LABEL_INCLUDED_IN).to(__.V(subProcessDetails.getProcess().id())).next();
    }

    private String getProcessName(GraphTraversalSource g, Vertex process) {
        final String processName =
                g.V(process.id()).elementMap(PROPERTY_KEY_INSTANCEPROP_DISPLAY_NAME).toList().get(0).get(PROPERTY_KEY_INSTANCEPROP_DISPLAY_NAME).toString();
        return processName;
    }

    /**
     * Returns true if the vertex is null or doesn't have a valid guid
     *
     * @param column - The vertex to of the schema element
     */
    private boolean isColumnEmpty(Vertex column) {
        return (column == null || !StringUtils.isNotEmpty(this.graphHelper.getResult(this::getGuid, column, this::handleRetrieveResultError)));
    }

    /**
     * Retrieves vertex--guid property from a vertex
     *
     * @param vertex - the queried vertex
     */
    private String getGuid(GraphTraversalSource g, Vertex vertex) {
        String guid = g.V(vertex.id()).elementMap(PROPERTY_KEY_ENTITY_GUID).toList().get(0).get(PROPERTY_KEY_ENTITY_GUID).toString();
        return guid;
    }

    /**
     * Connects the tables and the processes with edges
     *
     * @param columnIn  - The vertex of the input schema element
     * @param process   - The vertex of the process.
     * @param columnOut - The vertex of the output schema element
     */
    private void addAssetToProcessEdges(Vertex columnIn, Vertex process, Vertex columnOut) {

        Optional<Vertex> assetIn = this.graphHelper.getResult(this::getAsset, columnIn, this::handleRetrieveResultError);
        this.graphHelper.commit(this::addEdgeFromColumnToProcess, assetIn, process, this::handleCouldNotAddEdge);

        Optional<Vertex> assetOut = this.graphHelper.getResult(this::getAsset, columnOut, this::handleRetrieveResultError);
        this.graphHelper.commit(this::addEdgeFromProcessToColumn, process, assetOut, this::handleCouldNotAddEdge);

    }

    private void addEdgeFromColumnToProcess(GraphTraversalSource g, Optional<Vertex> assetIn, Vertex process) {

        if (assetIn.isPresent()) {
            Iterator<Vertex> tableVertex = g.V(assetIn.get().id()).outE(EDGE_LABEL_TABLE_DATA_FLOW).inV().hasId(process.id());
            if (!tableVertex.hasNext()) {
                g.V(assetIn.get().id()).addE(EDGE_LABEL_TABLE_DATA_FLOW).to(__.V(process.id())).next();
            }
        }
    }

    private void addEdgeFromProcessToColumn(GraphTraversalSource g, Vertex process, Optional<Vertex> assetOut) {

        if (assetOut.isPresent()) {
            Iterator<Vertex> tableVertex = g.V(assetOut.get().id()).inE(EDGE_LABEL_TABLE_DATA_FLOW).outV().hasId(process.id());
            if (!tableVertex.hasNext()) {
                g.V(process.id()).addE(EDGE_LABEL_TABLE_DATA_FLOW).to(__.V(assetOut.get().id())).next();
            }
        }
    }

    private void handleCouldNotAddEdge(Exception e) {
        log.error(VERTICES_AND_RELATIONSHIP_CREATION_EXCEPTION.getErrorMessage(), e);
        throw new JanusConnectorException(this.getClass().getName(), "addAssetToProcessEdges", VERTICES_AND_RELATIONSHIP_CREATION_EXCEPTION);
    }


    /**
     * Retrieves the table or the data file node for a schemaElement
     *
     * @param asset - The vertex of the input schema element
     */
    private Optional<Vertex> getAsset(GraphTraversalSource g, Vertex asset) {
        Object vertexGuid = g.V(asset.id()).elementMap(PROPERTY_KEY_ENTITY_GUID).toList().get(0).get(PROPERTY_KEY_ENTITY_GUID);
        Vertex graphVertex = g.V().has(PROPERTY_KEY_ENTITY_GUID, vertexGuid).next();
        Object vertexId = graphVertex.id();
        Iterator<Vertex> result = null;
        if (RELATIONAL_COLUMN.equalsIgnoreCase(asset.label())) {
            result = g.V(vertexId).emit().repeat(bothE().otherV().simplePath()).times(1).or(hasLabel(RELATIONAL_TABLE));
        }
        if (TABULAR_COLUMN.equalsIgnoreCase(asset.label()) || TABULAR_FILE_COLUMN.equalsIgnoreCase(asset.label())) {
            result = g.V(vertexId).emit().repeat(bothE().otherV().simplePath()).times(2).
                    or(hasLabel(P.within(DATA_FILE_AND_SUBTYPES)));
        }
        if (EVENT_SCHEMA_ATTRIBUTE.equalsIgnoreCase(asset.label())) {
            result = g.V(vertexId).emit().repeat(bothE().otherV().simplePath()).times(3).or(hasLabel(TOPIC));
        }
        if (result == null) {
            return Optional.empty();
        } else {
            return Optional.of(result.next());
        }

    }

    private void handleRetrieveProcessGuids(Exception e) {
        log.error("Could not retrieve guids from the database", e);
    }

    private void handleRetrieveResultError(Exception e, Vertex vertex) {
        log.error("Could not retrieve object from database {}", vertex, e);
    }

    private void handleRetrieveResultError(Exception e, String guid) {
        log.error("Could not retrieve object from database {}", guid, e);
    }

    private void handleRetrieveResultError(Exception e, Vertex vertex1, Vertex vertex2) {
        log.error("Could not retrieve object from database {}, {}", vertex1, vertex2, e);
    }

    private void handleFindExistingSubprocess(Exception e, SubProcessDetails subProcessDetails) {
        log.error("Could not find connection {}", subProcessDetails, e);
    }
}
