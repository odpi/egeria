/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.cassandra;

import org.apache.tinkerpop.gremlin.structure.Edge;
import org.janusgraph.core.JanusGraph;
import org.janusgraph.core.JanusGraphFactory;
import org.janusgraph.core.schema.JanusGraphManagement;
import org.odpi.openmetadata.accessservices.assetlineage.Vertex;
import org.odpi.openmetadata.frameworks.connectors.properties.ConnectionProperties;
import org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.buffergraph.IndexingFactory;
import org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.model.JanusConnectorErrorCode;
import org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.model.ffdc.JanusConnectorException;
import org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.EdgeLabelsBufferGraph;
import org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.VertexLabelsBufferGraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.*;

public class BufferGraphFactory extends IndexingFactory {


    private static final Logger log = LoggerFactory.getLogger(BufferGraphFactory.class);


    public JanusGraph openBufferGraph(ConnectionProperties connectionProperties){

        final String methodName = "open";
        JanusGraph janusGraph;

        String graphType = (String) connectionProperties.getConfigurationProperties().get("graphType");
        String listString = String.join(",", (ArrayList) connectionProperties.getConfigurationProperties().get("storageHostname"));

        JanusGraphFactory.Builder config = JanusGraphFactory.build().
                set("storage.backend",connectionProperties.getConfigurationProperties().get("storageBackend")).
                set("storage.username",connectionProperties.getConfigurationProperties().get("username")).
                set("storage.password",connectionProperties.getConfigurationProperties().get("password")).
                set("storage.hostname", listString).
                set("storage.cql.cluster-name",connectionProperties.getConfigurationProperties().get("clusterName")).
                set("storage.cql.keyspace",connectionProperties.getConfigurationProperties().get("storageCqlKeyspace")).
                set("index.search.backend",connectionProperties.getConfigurationProperties().get("indexSearchBackend")).
                set("index.search.hostname",connectionProperties.getConfigurationProperties().get("indexSearchHostname"));


        try {

            janusGraph = config.open();

        } catch (Exception e) {
            log.error("{} could not open graph stored", e);
            JanusConnectorErrorCode errorCode = JanusConnectorErrorCode.CANNOT_OPEN_GRAPH_DB;

            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(e.getMessage(), methodName, BufferGraphFactory.class.getName());
            System.out.println(e.getMessage());
            throw new JanusConnectorException(BufferGraphFactory.class.getName(),
                                              methodName,
                                              errorMessage,
                                              errorCode.getSystemAction(),
                                              errorCode.getUserAction());
        }



        try {
            log.info("Updating graph schema, if necessary");
            initializeGraph(janusGraph,graphType);
        }
        catch (JanusConnectorException e) {
            log.error("{} Caught exception during graph initialize operation", "open");

            JanusConnectorErrorCode errorCode = JanusConnectorErrorCode.GRAPH_INITIALIZATION_ERROR;
            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(e.getMessage(), methodName, BufferGraphFactory.class.getName());

            throw new JanusConnectorException(BufferGraphFactory.class.getName(),
                                              methodName,
                                              errorMessage,
                                              errorCode.getSystemAction(),
                                              errorCode.getUserAction());
        }


        return janusGraph;
    }

    private void initializeGraph(JanusGraph graph, String graphType){

        final String methodName = "initializeGraph";
        /*
         * Create labels for vertex and edge uses
         */


        try {
            JanusGraphManagement management = graph.openManagement();

            Set<String> vertexLabels = new HashSet<>();
            Set<String> relationshipsLabels = new HashSet<>();

            if (graphType.equals("bufferGraph")){
                vertexLabels = schemaBasedOnGraphType(VertexLabelsBufferGraph.class);
                relationshipsLabels = schemaBasedOnGraphType(EdgeLabelsBufferGraph.class);
            }

            if(graphType.equals("mainGraph")){
                //add schema for maingrapgh
            }

            management = checkAndAddLabelVertexOrEdge(vertexLabels, management);
            management = checkAndAddLabelVertexOrEdge(relationshipsLabels, management);

            management.commit();

            createIndexes(graph);



        } catch (Exception e) {

            JanusConnectorErrorCode errorCode = JanusConnectorErrorCode.GRAPH_INITIALIZATION_ERROR;
            String errorMessage = errorCode.getErrorMessageId();
            throw new JanusConnectorException(BufferGraphFactory.class.getName(),
                                              methodName,
                                              errorMessage,
                                              errorCode.getSystemAction(),
                                              errorCode.getUserAction());
        }
    }

    private <T extends Enum<T>> Set<String> schemaBasedOnGraphType(Class<T> aEnum){
           return Stream.of(aEnum.getEnumConstants())
                    .map(Enum::name)
                    .collect(Collectors.toSet());
    }

    private JanusGraphManagement checkAndAddLabelVertexOrEdge(Set<String> labels, JanusGraphManagement management){
        for (String label: labels) {

            if (management.getVertexLabel(label) == null)
                management.makeVertexLabel(label).make();
        }

        return management;

    }

    private void createIndexes(JanusGraph graph){

        createCompositeIndexForProperty(PROPERTY_NAME_GUID,PROPERTY_KEY_ENTITY_GUID,true,graph,Vertex.class);
        createCompositeIndexForProperty(PROPERTY_NAME_NAME,PROPERTY_KEY_ENTITY_NAME,false,graph,Vertex.class);
        createCompositeIndexForProperty(PROPERTY_NAME_LABEL,PROPERTY_KEY_RELATIONSHIP_LABEL,false,graph, Edge.class);
    }
}
