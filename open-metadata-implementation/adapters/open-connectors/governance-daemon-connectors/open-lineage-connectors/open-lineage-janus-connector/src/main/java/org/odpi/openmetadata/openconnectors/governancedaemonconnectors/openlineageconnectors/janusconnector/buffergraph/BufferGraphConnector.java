/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.buffergraph;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.janusgraph.core.JanusGraph;
import org.odpi.openmetadata.accessservices.assetlineage.event.LineageEvent;
import org.odpi.openmetadata.accessservices.assetlineage.model.GraphContext;
import org.odpi.openmetadata.accessservices.assetlineage.model.LineageEntity;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
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

import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.Constants.*;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.*;

public class BufferGraphConnector extends BufferGraphConnectorBase {

    private static final Logger log = LoggerFactory.getLogger(BufferGraphConnector.class);
    private JanusGraph bufferGraph;
    private GraphVertexMapper graphVertexMapper = new GraphVertexMapper();
    private JanusGraph mainGraph;

    /**
     * Instantiates the graph based on the configuration passed.
     *
     */
    public void initializeGraphDB() throws OpenLineageException {
        GraphFactory graphFactory = new GraphFactory();
        try {
            this.bufferGraph = graphFactory.openGraph(connectionProperties);
        } catch (JanusConnectorException error) {
            log.error("The Buffer graph could not be initialized due to an error", error);
            throw new OpenLineageException(500,
                    error.getReportingClassName(),
                    error.getReportingActionDescription(),
                    error.getReportedErrorMessage(),
                    error.getReportedSystemAction(),
                    error.getReportedUserAction()
            );
        }
    }

    /**
     * Retrieves the mainGraph instance.
     *
     */
    @Override
    public void setMainGraph(Object mainGraph) {
        this.mainGraph = (JanusGraph) mainGraph;
    }


    @Override
    public void schedulerTask(){
        GraphTraversalSource g = bufferGraph.traversal();
        try {
            List<Vertex> vertices = g.V().has(PROPERTY_KEY_LABEL, "Process").toList();
            List<String> guidList = vertices.stream().map(v -> (String) v.property(PROPERTY_KEY_ENTITY_GUID).value()).collect(Collectors.toList());

            guidList.forEach(process -> findInputColumns(g,process));
            g.tx().commit();
        }catch (Exception e){
            log.error("Something went wrong when trying to map a process from bufferGraph to the mainGraph. The error is {}",e.getMessage());
            g.tx().rollback();
        }
    }

    /**
     * Finds the paths to the input columns from all the processes in the graph.
     * @param g - Graph traversal object
     * @param guid - The unique identifier of a Process
     */
    private void findInputColumns(GraphTraversalSource g,String guid) {
            List<Vertex> inputPathsForColumns = g.V().has(PROPERTY_KEY_ENTITY_GUID, guid).out(PROCESS_PORT).out(PORT_DELEGATION)
                    .has(PORT_IMPLEMENTATION, PROPERTY_NAME_PORT_TYPE, "INPUT_PORT")
                    .out(PORT_SCHEMA).in(ATTRIBUTE_FOR_SCHEMA).out(LINEAGE_MAPPING)
                    .or(__.out(ATTRIBUTE_FOR_SCHEMA).out(ASSET_SCHEMA_TYPE)
                                    .has(PROPERTY_KEY_LABEL,DATA_FILE),
                            __.out(NESTED_SCHEMA_ATTRIBUTE).has(PROPERTY_KEY_LABEL,RELATIONAL_TABLE)).toList();

        Vertex process = g.V().has(PROPERTY_KEY_ENTITY_GUID, guid).next();
        inputPathsForColumns.forEach(columnIn -> findOutputColumn(g, columnIn, process));
    }

    /**
     * Finds the output column of a Process based on the input.
     * @param g - Graph traversal object
     * @param columnIn - THe vertex of the schema element before processing.
     * @param process - The vertex of the process.
     */
    private void findOutputColumn(GraphTraversalSource g,Vertex columnIn,Vertex process){
        List<Vertex> schemaElementVertex = g.V()
                                            .has(PROPERTY_KEY_ENTITY_GUID, columnIn.property(PROPERTY_KEY_ENTITY_GUID).value())
                                            .in(LINEAGE_MAPPING)
                                            .toList();

        Vertex vertexToStart = null;
        if (schemaElementVertex != null) {
            Vertex columnOut = null;
            vertexToStart = getProcessForTheSchemaElement(g,schemaElementVertex,process);
            if (vertexToStart != null) {
                columnOut = findPathForOutputAsset(vertexToStart, g, columnIn);
            }
            moveColumnProcessColumn(columnIn, columnOut, process);
        }
    }

    /**
     * Returns the vertex from where the searching for the output column will start.
     * @param g - Graph traversal object
     * @param schemaElementVertex - THe vertex of the column before processing.
     * @param process - The vertex of the process.
     * @return Return the vertex of the initial column
     */
    private Vertex getProcessForTheSchemaElement(GraphTraversalSource g,List<Vertex> schemaElementVertex,Vertex process){
        Vertex vertexToStart = null;
        for (Vertex v : schemaElementVertex) {
            List<Vertex> initialProcess = g.V(v.id())
                    .bothE(ATTRIBUTE_FOR_SCHEMA)
                    .otherV().inE(PORT_SCHEMA).otherV()
                    .inE(PORT_DELEGATION).otherV()
                    .inE(PROCESS_PORT).otherV()
                    .has(PROPERTY_KEY_ENTITY_GUID, process.property(PROPERTY_KEY_ENTITY_GUID).value()).toList();

            if (!initialProcess.isEmpty()) {
                vertexToStart = v;
                break;
            }
        }
        return vertexToStart;
    }

    /**
     * Initiates the process of copying the input and output vertices to the MainGraph.
     * @param columnIn - The vertex of the input schema element
     * @param columnOut - THe vertex of the output schema element
     * @param process - The vertex of the process.
     */
    private void moveColumnProcessColumn(Vertex columnIn,Vertex columnOut,Vertex process){
        if (columnOut != null) {
            String columnOutGuid = columnOut.values(PROPERTY_KEY_ENTITY_GUID).next().toString();
            String columnInGuid = columnIn.values(PROPERTY_KEY_ENTITY_GUID).next().toString();
            if (!columnOutGuid.isEmpty() && !columnInGuid.isEmpty()) {
                MainGraphMapper mainGraphMapper = new MainGraphMapper(bufferGraph,mainGraph);
                mainGraphMapper.checkBufferGraph(columnInGuid,columnOutGuid,process);
            }
        }
    }

    /**
     * Creates a new vertex if it does not exist
     * @param lineageEvent - LineageEntity object to be created
     */
    @Override
    public void addEntity(LineageEvent lineageEvent){

        GraphTraversalSource g =  bufferGraph.traversal();

        Set<GraphContext> verticesToBeAdded = new HashSet<>();
        lineageEvent.getAssetContext().forEach((key, value) -> {
            if (value.size() > 1) {
                verticesToBeAdded.addAll(value);
            } else {
                verticesToBeAdded.add(value.stream().findFirst().get());
            }
        });

        verticesToBeAdded.forEach(entry -> {
            try {
                addVerticesAndRelationship(g, entry);
            } catch (JanusConnectorException e) {
                log.error("An exception happened when trying to create vertices and relationships in BufferGraph. The error is", e);
            }
        });
    }


    private void addVerticesAndRelationship(GraphTraversalSource g, GraphContext nodeToNode)  throws JanusConnectorException{
        LineageEntity fromEntity = nodeToNode.getFromVertex();
        LineageEntity toEntity = nodeToNode.getToVertex();

        Vertex vertexFrom = addVertex(g,fromEntity);
        Vertex vertexTo = addVertex(g,toEntity);

        if(vertexFrom != null && vertexTo != null){
            addRelationship(nodeToNode.getRelationshipGuid(),nodeToNode.getRelationshipType(),vertexFrom,vertexTo);
        }
    }

    /**
     * Creates a new vertex in the graph.
     * @param g - Graph traversal object
     * @param lineageEntity - Entity to be created
     */
    private  Vertex addVertex(GraphTraversalSource g,LineageEntity lineageEntity) throws JanusConnectorException{
        Iterator<Vertex> vertexIt = g.V().has(PROPERTY_KEY_ENTITY_GUID, lineageEntity.getGuid());
        Vertex vertex;

        if(!vertexIt.hasNext()){
            vertex = g.addV(lineageEntity.getTypeDefName()).next();
            addPropertiesToVertex(g,vertex,lineageEntity);
            g.tx().commit();
        }
        else {
            vertex = vertexIt.next();
            if (log.isDebugEnabled()) {
                log.debug("found existing vertex {} when trying to add it in bufferGraph", vertex);
            }
            g.tx().rollback();
        }
        return vertex;
    }

    /**
     * Creates new Relationships and it's properties in bufferGraph and mainGraph related to Lineage.
     *
     */
    private void addRelationship(String relationshipGuid,String relationshipType,Vertex fromVertex,Vertex toVertex) throws JanusConnectorException{
        String methodName = "addRelationship";
        GraphTraversalSource g = bufferGraph.traversal();

        if (relationshipType == null) {
            log.error("Relationship type name is missing");
            throwException(JanusConnectorErrorCode.RELATIONSHIP_TYPE_NAME_NOT_KNOWN,relationshipGuid,methodName);
        }

        try {
            Iterator<Edge> edgeIt = g.E().has(PROPERTY_KEY_RELATIONSHIP_GUID, relationshipGuid);
            if (edgeIt.hasNext()) {
                g.tx().rollback();
                log.debug("found existing edge {}", edgeIt);
                return;
            }

            fromVertex.addEdge(relationshipType, toVertex).property(PROPERTY_KEY_RELATIONSHIP_GUID,relationshipGuid);
            g.tx().commit();
        }
        catch (Exception e){
            g.tx().rollback();

        }

    }

    /**
     * Add properties to vertex.
     * @param g - Graph traversal object
     * @param lineageEntity - LineageEntity object to be created
     */
    private void addPropertiesToVertex(GraphTraversalSource g,Vertex vertex, LineageEntity lineageEntity) throws JanusConnectorException{
        final String methodName = "addPropertiesToVertex";

        try {
            graphVertexMapper.mapEntityToVertex(lineageEntity, vertex);
        }catch (Exception e) {
            log.error("Caught exception from entity mapper {}", e.getMessage());
            g.tx().rollback();
            throwException(JanusConnectorErrorCode.ENTITY_NOT_CREATED,lineageEntity.getGuid(),methodName);
        }
    }

    @Override
    public void updateEntity(LineageEvent lineageEvent){}

    @Override
    public void deleteEntity(String guid){
        GraphTraversalSource g = bufferGraph.traversal();

        //TODO add check when we will have classifications to delete classifications first
        if(checkIfVertexExist(g,guid)){
            g.V().has(PROPERTY_KEY_ENTITY_GUID,guid).drop();
            g.tx().commit();
            log.debug("Vertex with guid {} deleted",guid);
        }
        g.tx().rollback();
        log.debug("Vertex with guid did not delete {}",guid);

    }

    /**
     * Returns the vertex of the schema element in the output of a process.
     * @param endingVertex - The vertex that is being checked if it is the output schema element
     * @param g - Graph traversal object
     * @param startingVertex - The vertex of the input schema element
     * @return Return a vertex of the output schema element
     */
    private Vertex findPathForOutputAsset(Vertex endingVertex, GraphTraversalSource g,Vertex startingVertex)  {
        final String VERTEX = "vertex";

        try{
            Iterator<Vertex> end =  g.V(endingVertex.id())
                    .or(__.out(ATTRIBUTE_FOR_SCHEMA).out(ASSET_SCHEMA_TYPE)
                            .has(PROPERTY_KEY_LABEL,DATA_FILE).store(VERTEX),
                            __.out(NESTED_SCHEMA_ATTRIBUTE).has(PROPERTY_KEY_LABEL,RELATIONAL_TABLE)
                                    .store(VERTEX)).select(VERTEX).unfold();

            if (!end.hasNext()) {
                List<Vertex> next = g.V(endingVertex.id()).both(LINEAGE_MAPPING).toList();
                Vertex nextVertex = null;
                for(Vertex vert: next){
                    if(vert.equals(startingVertex)){
                        continue;
                    }
                    nextVertex = vert;
                }


                return findPathForOutputAsset(nextVertex, g,endingVertex);
            }

            return endingVertex;
        }
        catch (Exception e){
            if (log.isDebugEnabled()) {
                log.debug("Vertex does not exist {}",startingVertex.id());
            }
            return null;
        }
    }

    private boolean checkIfVertexExist(GraphTraversalSource g,String guid){
        return g.V().has(PROPERTY_KEY_ENTITY_GUID,guid).hasNext();
    }

    private void throwException(JanusConnectorErrorCode errorCode,String guid,String methodName) throws JanusConnectorException {

        String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(guid, methodName,
                this.getClass().getName());

        throw new JanusConnectorException(this.getClass().getName(),
                                          methodName,
                                          errorMessage,
                                          errorCode.getSystemAction(),
                                          errorCode.getUserAction());
    }

    @Override
    public void disconnect() throws ConnectorCheckedException {
        this.bufferGraph.close();
        super.disconnect();
    }
}
