/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.buffergraph;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.janusgraph.core.JanusGraph;
import org.odpi.openmetadata.accessservices.assetlineage.model.GraphContext;
import org.odpi.openmetadata.accessservices.assetlineage.model.LineageEntity;
import org.odpi.openmetadata.accessservices.assetlineage.event.LineageEvent;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.properties.ConnectionProperties;
import org.odpi.openmetadata.governanceservers.openlineage.buffergraph.BufferGraphConnectorBase;
import org.odpi.openmetadata.governanceservers.openlineage.ffdc.OpenLineageException;
import org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.factory.GraphFactory;
import org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.model.JanusConnectorErrorCode;
import org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.model.ffdc.JanusConnectorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.RELATIONAL_COLUMN;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.TABULAR_COLUMN;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.*;

public class BufferGraphConnector extends BufferGraphConnectorBase {

    private static final Logger log = LoggerFactory.getLogger(BufferGraphConnector.class);
    private JanusGraph bufferGraph;
    private GraphVertexMapper graphVertexMapper = new GraphVertexMapper();
    private JanusGraph mainGraph;


    public void initializeGraphDB() throws OpenLineageException {
        String graphDB = connectionProperties.getConfigurationProperties().get("graphDB").toString();
        GraphFactory graphFactory = new GraphFactory();
        try {
            this.bufferGraph = graphFactory.openGraph(graphDB, connectionProperties);
        } catch (JanusConnectorException error) {
            throw new OpenLineageException(500,
                    error.getReportingClassName(),
                    error.getReportingActionDescription(),
                    error.getReportedErrorMessage(),
                    error.getReportedSystemAction(),
                    error.getReportedUserAction()
            );
        }
    }

    @Override
    public void setMainGraph(Object mainGraph) {
        this.mainGraph = (JanusGraph) mainGraph;
    }

    @Override
    public void addEntity(LineageEvent lineageEvent) {

        GraphTraversalSource g = bufferGraph.traversal();

        Set<GraphContext> verticesToBeAdded = new HashSet<>();
        lineageEvent.getAssetContext().entrySet().stream().forEach(entry ->
                {
                    if (entry.getValue().size() > 1) {
                        verticesToBeAdded.addAll(entry.getValue());
                    } else {
                        verticesToBeAdded.add(entry.getValue().stream().findFirst().get());
                    }
                }
        );

        verticesToBeAdded.stream().forEach(entry -> {
            try {
                addVerticesAndRelationship(g, entry);
            } catch (JanusConnectorException e) {
                log.error("An exception occured", e);
            }
        });
    }

    @Override
    public void schedulerTask() {
        GraphTraversalSource g = bufferGraph.traversal();
        try {
            List<Vertex> vertices = g.V().has(PROPERTY_KEY_ENTITY_NAME, "Process").toList();

            List<String> guidList = vertices.stream().map(v -> (String) v.property(PROPERTY_KEY_ENTITY_GUID).value()).collect(Collectors.toList());

            guidList.stream().forEach(process -> findInputColumns(g, process));
            g.tx().commit();
        } catch (Exception e) {
            log.debug(e.getMessage());
            g.tx().rollback();
        }

    }

    private void findInputColumns(GraphTraversalSource g, String guid) {

        //TODO change Tabular column and Relational column with the supertupe SchemaElement when AssetLineage is ready
        List<Vertex> inputPath = g.V().has(PROPERTY_KEY_ENTITY_GUID, guid).out("ProcessPort").out("PortDelegation").has("PortImplementation", "vepropportType", "INPUT_PORT")
                .out("PortSchema").out("AttributeForSchema").out("SchemaAttributeType").out("LineageMapping").in("SchemaAttributeType")
                .or(__.has("vename", "TabularColumn"), __.has("vename", "RelationalColumn"))
                .toList();

        Vertex process = g.V().has(PROPERTY_KEY_ENTITY_GUID, guid).next();
        inputPath.stream().forEach(columnIn -> findOutputColumn(g, columnIn, process));
    }

    private void findOutputColumn(GraphTraversalSource g, Vertex columnIn, Vertex process) {
        List<Vertex> schemaElementVertex = g.V()
                .has(PROPERTY_KEY_ENTITY_GUID, columnIn.property(PROPERTY_KEY_ENTITY_GUID).value())
                .out("SchemaAttributeType")
                .in("LineageMapping")
                .toList();

        Vertex vertexToStart = null;
        if (schemaElementVertex != null) {
            for (Vertex v : schemaElementVertex) {
                List<Vertex> initialProcess = g.V(v.id())
                        .bothE("SchemaAttributeType")
                        .otherV().bothE("AttributeForSchema")
                        .otherV().inE("PortSchema").otherV()
                        .inE("PortDelegation").otherV().
                                inE("ProcessPort").otherV().has("veguid", process.property(PROPERTY_KEY_ENTITY_GUID).value()).toList();

                if (!initialProcess.isEmpty()) {
                    vertexToStart = v;
                    break;
                }


                Vertex startingVertex = g.V().has(PROPERTY_KEY_ENTITY_GUID, columnIn.property(PROPERTY_KEY_ENTITY_GUID).value()).out("SchemaAttributeType").next();
                Iterator<Vertex> columnOut = null;
                if (vertexToStart != null) {
                    columnOut = findPathForOutputAsset(vertexToStart, g, startingVertex);

                }

                moveColumnProcessColumn(columnIn, columnOut, process);
            }
        }

    }

    private void moveColumnProcessColumn(Vertex columnIn, Iterator<Vertex> columnOut, Vertex process) {
        if (columnOut != null && columnOut.hasNext()) {
            String columnOutGuid = columnOut.next().values(PROPERTY_KEY_ENTITY_GUID).next().toString();
            String columnInGuid = columnIn.values(PROPERTY_KEY_ENTITY_GUID).next().toString();
            if (!columnOutGuid.isEmpty() && !columnInGuid.isEmpty()) {
                MainGraphMapper mainGraphMapper = new MainGraphMapper(bufferGraph, mainGraph);
                mainGraphMapper.checkBufferGraph(columnInGuid, columnOutGuid, process);
            }
        }
    }

    private void addVerticesAndRelationship(GraphTraversalSource g, GraphContext nodeToNode) throws JanusConnectorException {
        LineageEntity fromEntity = nodeToNode.getFromVertex();
        LineageEntity toEntity = nodeToNode.getToVertex();

        Vertex vertexFrom = addVertex(g, fromEntity);
        Vertex vertexTo = addVertex(g, toEntity);

        //add check gia null vertex
        addRelationship(nodeToNode.getRelationshipGuid(), nodeToNode.getRelationshipType(), vertexFrom, vertexTo);

    }

    private Vertex addVertex(GraphTraversalSource g, LineageEntity lineageEntity) throws JanusConnectorException {
        final String methodName = "addVertex";

        Iterator<Vertex> vertexIt = g.V().has(PROPERTY_KEY_ENTITY_GUID, lineageEntity.getGuid());
        Vertex vertex;

        if (!vertexIt.hasNext()) {
            vertex = g.addV(lineageEntity.getTypeDefName()).next();
            addPropertiesToVertex(g, vertex, lineageEntity);
            g.tx().commit();
        } else {
            vertex = vertexIt.next();
            log.debug("{} found existing vertex {}", methodName, vertex);
            g.tx().rollback();
        }
        return vertex;
    }

    /**
     * Creates new Relationships and it's properties in bufferGraph and mainGraph related to Lineage.
     */
    private void addRelationship(String relationshipGuid, String relationshipType, Vertex fromVertex, Vertex toVertex) throws JanusConnectorException {
        String methodName = "addRelationship";

        if (relationshipType == null) {
            log.error("{} Relationship type name is missing", methodName);
            throwException(JanusConnectorErrorCode.RELATIONSHIP_TYPE_NAME_NOT_KNOWN, relationshipGuid, methodName);
        }

        GraphTraversalSource g = bufferGraph.traversal();

        Iterator<Edge> edgeIt = g.E().has(PROPERTY_KEY_RELATIONSHIP_GUID, relationshipGuid);
        if (edgeIt.hasNext()) {
            g.tx().rollback();
//            throwException(JanusConnectorErrorCode.RELATIONSHIP_ALREADY_EXISTS,relationshipGuid,methodName);
            log.debug("{} found existing edge {}", methodName, edgeIt);

            return;
        }
        //TODO add try catch
        fromVertex.addEdge(relationshipType, toVertex).property("edguid", relationshipGuid);
        g.tx().commit();
    }

    private void addPropertiesToVertex(GraphTraversalSource g, Vertex vertex, LineageEntity lineageEntity) throws JanusConnectorException {
        final String methodName = "addPropertiesToVertex";

        try {
            graphVertexMapper.mapEntityToVertex(lineageEntity, vertex);
        } catch (Exception e) {
            log.error("{} Caught exception from entity mapper {}", methodName, e.getMessage());
            g.tx().rollback();
            throwException(JanusConnectorErrorCode.ENTITY_NOT_CREATED, lineageEntity.getGuid(), methodName);

        }
    }

    private Iterator<Vertex> findPathForOutputAsset(Vertex v, GraphTraversalSource g, Vertex startingVertex) {

        try {
            Iterator<Vertex> end = g.V(v.id()).both("SchemaAttributeType").or(__.has(PROPERTY_KEY_ENTITY_NAME, RELATIONAL_COLUMN),
                    __.has(PROPERTY_KEY_ENTITY_NAME, TABULAR_COLUMN));

            if (!end.hasNext()) {

                List<Vertex> next = g.V(v.id()).both("LineageMapping").toList();
                Vertex nextVertex = null;
                for (Vertex vert : next) {
                    if (vert.equals(startingVertex)) {
                        continue;
                    }
                    nextVertex = vert;
                }


                return findPathForOutputAsset(nextVertex, g, v);
            }
            return end;
        } catch (Exception e) {
            log.debug("Vertex does not exitst + {}", startingVertex.id());
            return null;
        }
    }

    private void throwException(JanusConnectorErrorCode errorCode, String guid, String methodName) throws JanusConnectorException {

        String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(guid, methodName,
                this.getClass().getName());

        throw new JanusConnectorException(this.getClass().getName(),
                methodName,
                errorMessage,
                errorCode.getSystemAction(),
                errorCode.getUserAction());
    }
}
