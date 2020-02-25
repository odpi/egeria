/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.factory;

import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.janusgraph.core.JanusGraph;
import org.janusgraph.core.JanusGraphFactory;
import org.janusgraph.core.schema.JanusGraphManagement;
import org.odpi.openmetadata.frameworks.connectors.properties.ConnectionProperties;
import org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.model.JanusConnectorErrorCode;
import org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.model.ffdc.JanusConnectorException;
import org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.EdgeLabelsBufferGraph;
import org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.EdgeLabelsMainGraph;
import org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.VertexLabelsBufferGraph;
import org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.VertexLabelsMainGraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.*;

public class GraphFactory extends IndexingFactory {


    private static final Logger log = LoggerFactory.getLogger(GraphFactory.class);

    private JanusFactoryBeans janusFactoryBeans = new JanusFactoryBeans();

    /**
     * Set the config for Janus Graph.
     *
     * @param graphDB              - type of backend to use for Janus Graph
     * @param connectionProperties - POJO for the configuration used to create the connector.
     * @return JanusGraph instance with schema and indexes
     */
    public JanusGraph openGraph(String graphDB, ConnectionProperties connectionProperties) throws JanusConnectorException {
        final String methodName = "openGraph";
        JanusGraph janusGraph;

        String graphType = (String) connectionProperties.getConfigurationProperties().get("graphType");
        JanusGraphFactory.Builder config = janusFactoryBeans.getJanusFactory(graphDB, connectionProperties, graphType);

        try {
            janusGraph = config.open();
            return initializeGraph(janusGraph, graphType);
        } catch (Exception e) {
            log.error("A connection with the graph database could not be established with the provided configuration", e);
            JanusConnectorErrorCode errorCode = JanusConnectorErrorCode.CANNOT_OPEN_GRAPH_DB;
            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(e.getMessage(), methodName, GraphFactory.class.getName());
            throw new JanusConnectorException(GraphFactory.class.getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        }
    }


    /**
     * Set up the schema for the Janus Graph instance
     *
     * @param janusGraph - Janus Graph instance
     * @param graphType  - type of the graph to be initiated
     */
    private JanusGraph initializeGraph(JanusGraph janusGraph, String graphType) throws JanusConnectorException {

        final String methodName = "initializeGraph";
        log.info("Updating graph schema, if necessary");
        try {
            JanusGraphManagement management = janusGraph.openManagement();

            Set<String> vertexLabels = new HashSet<>();
            Set<String> relationshipsLabels = new HashSet<>();

            if (graphType.equals("bufferGraph")) {
                vertexLabels = schemaBasedOnGraphType(VertexLabelsBufferGraph.class);
                relationshipsLabels = schemaBasedOnGraphType(EdgeLabelsBufferGraph.class);
            }

            if (graphType.equals("mainGraph")) {
                vertexLabels = schemaBasedOnGraphType(VertexLabelsMainGraph.class);
                relationshipsLabels = schemaBasedOnGraphType(EdgeLabelsMainGraph.class);
            }

            management = checkAndAddLabelVertexOrEdge(vertexLabels, management, "vertex");
            management = checkAndAddLabelVertexOrEdge(relationshipsLabels, management, "edge");

            //TODO define properties
            management.commit();

            createIndexes(janusGraph, graphType);
            return janusGraph;
        } catch (Exception e) {
            log.error("{} failed  during graph initialize operation with error: {}", graphType, e);
            JanusConnectorErrorCode errorCode = JanusConnectorErrorCode.GRAPH_INITIALIZATION_ERROR;
            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(e.getMessage(), methodName, GraphFactory.class.getName());
            throw new JanusConnectorException(GraphFactory.class.getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        }
    }


    private <T extends Enum<T>> Set<String> schemaBasedOnGraphType(Class<T> aEnum) {
        return Stream.of(aEnum.getEnumConstants())
                .map(Enum::name)
                .collect(Collectors.toSet());
    }

    /**
     * Set up the labels of the schema for the Janus Graph instance
     *
     * @param labels     - set of labels
     * @param management - management instance of Janus Graph
     * @param type       - type to be labeled
     */
    private JanusGraphManagement checkAndAddLabelVertexOrEdge(Set<String> labels, JanusGraphManagement management, String type) {
        if (type.equals("vertex")) {
            for (String label : labels) {
                if (management.getVertexLabel(label) == null)
                    management.makeVertexLabel(label).make();
            }
        }

        if (type.equals("edge")) {
            for (String label : labels) {
                if (management.getEdgeLabel(label) == null)
                    management.makeEdgeLabel(label).make();
            }
        }

        return management;

    }

    private void createIndexes(JanusGraph janusGraph, String graphType) {
        if (graphType.equals("bufferGraph")) {
            createIndexesBuffer(janusGraph);
        }

        if (graphType.equals("mainGraph")) {
            createIndexesMainGraph(janusGraph);
        }
    }

    /**
     * Set up the indexes for the Janus Graph instance when type is bufferGraph
     *
     * @param janusGraph - Janus Graph instance
     */
    private void createIndexesBuffer(JanusGraph janusGraph) {

        createCompositeIndexForProperty(PROPERTY_NAME_GUID, PROPERTY_KEY_ENTITY_GUID, true, janusGraph, Vertex.class);
        createCompositeIndexForProperty(PROPERTY_NAME_LABEL, PROPERTY_KEY_LABEL, false, janusGraph, Vertex.class);
        createCompositeIndexForProperty(PROPERTY_NAME_LABEL, PROPERTY_KEY_RELATIONSHIP_LABEL, false, janusGraph, Edge.class);
        createCompositeIndexForProperty(PROPERTY_NAME_GUID, PROPERTY_KEY_RELATIONSHIP_GUID, false, janusGraph, Edge.class);

    }

    /**
     * Set up the indexes for the Janus Graph instance when type is mainGraph
     *
     * @param janusGraph - Janus Graph instance
     */
    private void createIndexesMainGraph(JanusGraph janusGraph) {
        createCompositeIndexForProperty(PROPERTY_NAME_NODE_ID, PROPERTY_KEY_ENTITY_NODE_ID, true, janusGraph, Vertex.class);
        createCompositeIndexForProperty(PROPERTY_NAME_GUID, PROPERTY_KEY_ENTITY_GUID, false, janusGraph, Vertex.class);
        createCompositeIndexForProperty(PROPERTY_NAME_LABEL, PROPERTY_KEY_LABEL, false, janusGraph, Vertex.class);
        createCompositeIndexForProperty(PROPERTY_NAME_LABEL, PROPERTY_KEY_RELATIONSHIP_LABEL, false, janusGraph, Edge.class);

    }
}
