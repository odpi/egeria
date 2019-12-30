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

            guidList.stream().forEach(process -> findInputColumns(g,process));
            g.tx().commit();
        }catch (Exception e){
            log.debug(e.getMessage());
            g.tx().rollback();
        }

    }

    private void findInputColumns(GraphTraversalSource g,String guid){

        //TODO change Tabular column and Relational column with the supertupe SchemaElement when AssetLineage is ready
        List<Vertex> inputPath = g.V().has(PROPERTY_KEY_ENTITY_GUID, guid).out("ProcessPort").out("PortDelegation")
                .has("PortImplementation", PROPERTY_KEY_PREFIX_INSTANCE_PROPERTY, "INPUT_PORT")
                .out("PortSchema").in("AttributeForSchema").out("LineageMapping").toList();

        Vertex process = g.V().has(PROPERTY_KEY_ENTITY_GUID, guid).next();
        inputPath.stream().forEach(columnIn ->
                    findOutputColumn(g, columnIn, process));
//        inputPath.parallelStream().forEach(columnIn -> findOutputColumn(g,columnIn,process));

    }

    private void findOutputColumn(GraphTraversalSource g,Vertex columnIn,Vertex process){
//        if(process.property("veguid").value().equals("08f2d481-79d2-48e3-ae02-6366cf8deede")) {
            List<Vertex> schemaElementVertex = g.V()
                    .has(PROPERTY_KEY_ENTITY_GUID, columnIn.property(PROPERTY_KEY_ENTITY_GUID).value())
                    .in("LineageMapping")
                    .toList();

            Vertex vertexToStart = null;
            if (schemaElementVertex != null) {
                for (Vertex v : schemaElementVertex) {
                    List<Vertex> initialProcess = g.V(v.id())
                            .bothE("AttributeForSchema")
                            .otherV().inE("PortSchema").otherV()
                            .inE("PortDelegation").otherV().
                                    inE("ProcessPort").otherV().has(PROPERTY_KEY_ENTITY_GUID, process.property(PROPERTY_KEY_ENTITY_GUID).value()).toList();

                    if (!initialProcess.isEmpty()) {
                        vertexToStart = v;
                        break;
                    }

                }


//                    Vertex startingVertex = g.V().has(PROPERTY_KEY_ENTITY_GUID, columnIn.property(PROPERTY_KEY_ENTITY_GUID).value()).out("SchemaAttributeType").next();
                    Iterator<Vertex> columnOut = null;
                    if (vertexToStart != null) {
                        columnOut = findPathForOutputAsset(vertexToStart, g, columnIn);

                    }

                    moveColumnProcessColumn(columnIn, columnOut, process);
                }


//        }
    }

    private void moveColumnProcessColumn(Vertex columnIn,Iterator<Vertex> columnOut,Vertex process){
        if (columnOut != null && columnOut.hasNext()) {
            String columnOutGuid = columnOut.next().values(PROPERTY_KEY_ENTITY_GUID).next().toString();
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
        lineageEvent.getAssetContext().entrySet().stream().forEach(entry ->
                {
                    if(entry.getValue().size()>1){
                        verticesToBeAdded.addAll(entry.getValue());
                    }else {
                        verticesToBeAdded.add(entry.getValue().stream().findFirst().get());
                    }
                }
        );

        verticesToBeAdded.stream().forEach(entry -> addVerticesAndRelationship(g,entry));
    }

    private void addVerticesAndRelationship(GraphTraversalSource g, GraphContext nodeToNode)  throws JanusConnectorException{
        LineageEntity fromEntity = nodeToNode.getFromVertex();
        LineageEntity toEntity = nodeToNode.getToVertex();

        Vertex vertexFrom = addVertex(g,fromEntity);
        Vertex vertexTo = addVertex(g,toEntity);

        //add check gia null vertex
        addRelationship(nodeToNode.getRelationshipGuid(),nodeToNode.getRelationshipType(),vertexFrom,vertexTo);

    }

    private  Vertex addVertex(GraphTraversalSource g,LineageEntity lineageEntity) throws JanusConnectorException{
        final String methodName = "addVertex";

        Iterator<Vertex> vertexIt = g.V().has(PROPERTY_KEY_ENTITY_GUID, lineageEntity.getGuid());
        Vertex vertex;

        if(!vertexIt.hasNext()){
            vertex = g.addV(lineageEntity.getTypeDefName()).next();
            addPropertiesToVertex(g,vertex,lineageEntity);
            g.tx().commit();
        }
        else {
            vertex = vertexIt.next();
            log.debug("{} found existing vertex {}", methodName, vertex);
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

        if (relationshipType == null) {
            log.error("{} Relationship type name is missing", methodName);
            throwException(JanusConnectorErrorCode.RELATIONSHIP_TYPE_NAME_NOT_KNOWN,relationshipGuid,methodName);
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
        fromVertex.addEdge(relationshipType, toVertex).property(PROPERTY_KEY_RELATIONSHIP_GUID,relationshipGuid);
        g.tx().commit();
    }

    /**
     * Creates a new vertex if it does not exist
     * @param lineageEntity - LineageEntity object to be created
     */
    private void addPropertiesToVertex(GraphTraversalSource g,Vertex vertex, LineageEntity lineageEntity) throws JanusConnectorException{
        final String methodName = "addPropertiesToVertex";

        try {
            graphVertexMapper.mapEntityToVertex(lineageEntity, vertex);
        }catch (Exception e) {
            log.error("{} Caught exception from entity mapper {}", methodName, e.getMessage());
            g.tx().rollback();
            throwException(JanusConnectorErrorCode.ENTITY_NOT_CREATED,lineageEntity.getGuid(),methodName);

        }
    }

    @Override
    public void updateEntity(LineageEvent lineageEvent){

    }

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

    private Iterator<Vertex> findPathForOutputAsset(Vertex v, GraphTraversalSource g,Vertex startingVertex)  {

        try{
            Iterator<Vertex> end =  g.V(v.id())
                    .or(__.out("AttributeForSchema").out("AssetSchemaType")
                            .has(PROPERTY_KEY_LABEL,"DataFile").store("vertex"),
                            __.out("NestedSchemaAttribute").has(PROPERTY_KEY_LABEL,"RelationalTable")
                                    .store("vertex")).select("vertex").unfold();

            if (!end.hasNext()) {
                List<Vertex> next = g.V(v.id()).both("LineageMapping").toList();
                Vertex nextVertex = null;
                for(Vertex vert: next){
                    if(vert.equals(startingVertex)){
                        continue;
                    }
                    nextVertex = vert;
                }


                return findPathForOutputAsset(nextVertex, g,v);
            }
            return end;}
        catch (Exception e){
            log.debug("Vertex does not exitst + {}",startingVertex.id());
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
}
