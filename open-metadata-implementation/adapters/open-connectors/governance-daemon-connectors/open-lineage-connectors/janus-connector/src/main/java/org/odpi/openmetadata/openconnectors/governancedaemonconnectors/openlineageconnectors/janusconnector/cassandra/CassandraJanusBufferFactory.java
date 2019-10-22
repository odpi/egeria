/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.cassandra;

import org.janusgraph.core.JanusGraph;
import org.janusgraph.core.JanusGraphFactory;
import org.janusgraph.core.schema.JanusGraphManagement;
import org.odpi.openmetadata.frameworks.connectors.properties.ConnectionProperties;
import org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.model.OpenLineageErrorCode;
import org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.model.ffdc.OpenLineageException;
import org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.EdgeLabels;
import org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.VertexLabels;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.*;

public class CassandraJanusBufferFactory extends IndexingFactory {


    private static final Logger log = LoggerFactory.getLogger(CassandraJanusBufferFactory.class);


    public static JanusGraph openBufferGraph(ConnectionProperties connectionProperties){

        final String methodName = "open";
        JanusGraph janusGraph;

        JanusGraphFactory.Builder config = JanusGraphFactory.build().
                set("storage.backend",connectionProperties.getConfigurationProperties().get("storageBackend")).
                set("storage.hostname", connectionProperties.getConfigurationProperties().get("storageHostname")).
                set("storage.cql.keyspace",connectionProperties.getConfigurationProperties().get("storageCqlKeyspace")).
                set("index.search.backend",connectionProperties.getConfigurationProperties().get("indexSearchBackend")).
                set("index.search.hostname",connectionProperties.getConfigurationProperties().get("indexSearchHostname"));

        try {

            janusGraph = config.open();

        } catch (Exception e) {
            log.error("{} could not open graph stored", e);
            OpenLineageErrorCode errorCode = OpenLineageErrorCode.CANNOT_OPEN_GRAPH_DB;

            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(e.getMessage(), methodName, CassandraJanusBufferFactory.class.getName());

            throw new OpenLineageException(400,
                    CassandraJanusBufferFactory.class.getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        }


        try {
            log.info("Updating graph schema, if necessary");
            initializeGraph(janusGraph);
        }
        catch (OpenLineageException e) {
            log.error("{} Caught exception during graph initialize operation", "open");
            throw e;
        }


        return janusGraph;
    }

    private static void initializeGraph(JanusGraph graph){

        final String methodName = "initializeGraph";
        /*
         * Create labels for vertex and edge uses
         */

        try {
            JanusGraphManagement management = graph.openManagement();

            Set<String> vertexLabels = Stream.of(VertexLabels.values())
                    .map(Enum::name)
                    .collect(Collectors.toSet());


            Set<String> relationshipsLabels = Stream.of(EdgeLabels.values())
                    .map(Enum::name)
                    .collect(Collectors.toSet());

            // Each vertex has a label that reflects the Asset
            management = checkAndAddLabelVertexOrEdge(vertexLabels, management);
            // Each edge has a label that reflects the Relational Type
            management = checkAndAddLabelVertexOrEdge(relationshipsLabels, management);

            management.commit();

            createCompositeIndexForVertexProperty(PROPERTY_NAME_GUID,PROPERTY_KEY_ENTITY_GUID,true,graph);
            createCompositeIndexForVertexProperty(PROPERTY_NAME_NAME,PROPERTY_KEY_ENTITY_NAME,false,graph);

            createCompositeIndexForEdgeProperty(PROPERTY_NAME_LABEL,PROPERTY_KEY_RELATIONSHIP_LABEL,graph);


        } catch (Exception e) {

            OpenLineageErrorCode errorCode = OpenLineageErrorCode.GRAPH_INITIALIZATION_ERROR;
            String errorMessage = errorCode.getErrorMessageId();
            throw new OpenLineageException(400,
                    CassandraJanusBufferFactory.class.getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        }
    }

    private static JanusGraphManagement checkAndAddLabelVertexOrEdge(Set<String> labels, JanusGraphManagement management){
        for (String label: labels) {

            if (management.getVertexLabel(label) == null)
                management.makeVertexLabel(label).make();
        }

        return management;

    }
}
