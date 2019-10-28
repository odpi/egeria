/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.buffergraph;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.janusgraph.core.JanusGraph;
import org.odpi.openmetadata.accessservices.assetlineage.GraphContext;
import org.odpi.openmetadata.accessservices.assetlineage.LineageEntity;
import org.odpi.openmetadata.accessservices.assetlineage.model.event.LineageEvent;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.properties.ConnectionProperties;
import org.odpi.openmetadata.governanceservers.openlineage.BufferGraphStore;
import org.odpi.openmetadata.governanceservers.openlineage.MainGraphStore;
import org.odpi.openmetadata.governanceservers.openlineage.OpenLineageConnectorBase;
import org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.cassandra.BufferGraphFactory;
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

public class BufferGraphConnector extends OpenLineageConnectorBase implements BufferGraphStore {

    private static final Logger log = LoggerFactory.getLogger(BufferGraphConnector.class);
    private JanusGraph bufferGraph;
    private GraphVertexMapper graphVertexMapper = new GraphVertexMapper();

    private JanusGraph mainGraph;


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
                break;
            case "cassandra":
                BufferGraphFactory bufferGraphFactory = new BufferGraphFactory();
                this.bufferGraph = bufferGraphFactory.openBufferGraph(connectionProperties);
                break;

                default:
                    break;
        }
    }

    @Override
    public void setMainGraph(Object mainGraph) {
        this.mainGraph = (JanusGraph) mainGraph;
    }

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

    @Override
    public void schedulerTask(){
        GraphTraversalSource g = bufferGraph.traversal();
        List<Vertex> vertices = g.V().has(PROPERTY_KEY_ENTITY_NAME, "Process").toList();

        List<String> guidList = vertices.stream().map(v -> (String) v.property(PROPERTY_KEY_ENTITY_GUID).value()).collect(Collectors.toList());

        for (String guid : guidList) {
            Iterator<Vertex> initial =  g.V().has(PROPERTY_KEY_ENTITY_GUID,guid).has("displayName","initial_load");
            if(!initial.hasNext()) {


                List<Vertex> inputPath = g.V().has(PROPERTY_KEY_ENTITY_GUID, guid).out("ProcessPort").out("PortDelegation").has("PortImplementation", "portType", "INPUT_PORT")
                        .out("PortSchema").out("AttributeForSchema").out("SchemaAttributeType").in("LineageMapping").in("SchemaAttributeType")
                        .toList();

                Vertex process = g.V().has(PROPERTY_KEY_ENTITY_GUID, guid).next();
                for (Vertex vertex : inputPath) {
                    String vertexGuid = vertex.value(PROPERTY_KEY_ENTITY_GUID);
                    Iterator<Vertex> r = g.V().has(PROPERTY_KEY_ENTITY_GUID, vertexGuid).out("SchemaAttributeType").out("LineageMapping");

                    Iterator<Vertex> columnOut = findPathForOutputAsset(r.next(), g);
                    if (columnOut != null && columnOut.hasNext()) {
                        String columnOutGuid = columnOut.next().values(PROPERTY_KEY_ENTITY_GUID).next().toString();
                        String columnInGuid = vertex.values(PROPERTY_KEY_ENTITY_GUID).next().toString();


                        if (!columnOutGuid.isEmpty() && !columnInGuid.isEmpty()) {
                            MainGraphMapper mainGraphMapper = new MainGraphMapper();
                            mainGraphMapper.mapStructure(columnInGuid, process, columnOutGuid,mainGraph);
                        }
                    }
                }
            }
        }
        g.tx().commit();
    }

    private void addVerticesAndRelationship(GraphTraversalSource g, GraphContext nodeToNode){
        LineageEntity fromEntity = nodeToNode.getFromVertex();
        LineageEntity toEntity = nodeToNode.getToVertex();

        Vertex vertexFrom = addVertex(g,fromEntity);
        Vertex vertexTo = addVertex(g,toEntity);

        //add check gia null vertex
        addRelationship(nodeToNode.getRelationshipGuid(),nodeToNode.getRelationshipType(),vertexFrom,vertexTo);

    }

    private  Vertex addVertex(GraphTraversalSource g,LineageEntity lineageEntity){
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
    private void addRelationship(String relationshipGuid,String relationshipType,Vertex fromVertex,Vertex toVertex){
        String methodName = "addRelationship";

        if (relationshipType == null) {
            log.error("{} Relationship type name is missing", methodName);
            throwException(JanusConnectorErrorCode.RELATIONSHIP_TYPE_NAME_NOT_KNOWN,relationshipGuid,methodName);
        }

        GraphTraversalSource g = bufferGraph.traversal();

        Iterator<Edge> edgeIt = g.E().has(PROPERTY_KEY_RELATIONSHIP_GUID, relationshipGuid);
        if (edgeIt.hasNext()) {
            g.tx().rollback();
            throwException(JanusConnectorErrorCode.RELATIONSHIP_ALREADY_EXISTS,relationshipGuid,methodName);
            return;
        }
        //TODO add try catch
        fromVertex.addEdge(relationshipType, toVertex);
        g.tx().commit();
    }
    private void addPropertiesToVertex(GraphTraversalSource g,Vertex vertex, LineageEntity lineageEntity){
        final String methodName = "addPropertiesToVertex";

        try {
            graphVertexMapper.mapEntityToVertex(lineageEntity, vertex);
        }catch (Exception e) {
            log.error("{} Caught exception from entity mapper {}", methodName, e.getMessage());
            g.tx().rollback();
            throwException(JanusConnectorErrorCode.ENTITY_NOT_CREATED,lineageEntity.getGuid(),methodName);

        }
    }

    private Iterator<Vertex> findPathForOutputAsset(Vertex v, GraphTraversalSource g)  {

        try{
            Iterator<Vertex> end = g.V(v.id()).both("SchemaAttributeType").or(__.has(PROPERTY_KEY_ENTITY_NAME, RELATIONAL_COLUMN),
                    __.has(PROPERTY_KEY_ENTITY_NAME, TABULAR_COLUMN));

            if (!end.hasNext()) {

                Iterator<Vertex> next = g.V(v.id()).out("LineageMapping");
                return findPathForOutputAsset(next.next(), g);
            }
            return end;}
        catch (Exception e){
            log.debug("Vertex does not exitst");
            return null;
        }
    }

    private void throwException(JanusConnectorErrorCode errorCode,String guid,String methodName){

        String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(guid, methodName,
                this.getClass().getName());

        throw new JanusConnectorException(this.getClass().getName(),
                                          methodName,
                                          errorMessage,
                                          errorCode.getSystemAction(),
                                          errorCode.getUserAction());
    }


}
