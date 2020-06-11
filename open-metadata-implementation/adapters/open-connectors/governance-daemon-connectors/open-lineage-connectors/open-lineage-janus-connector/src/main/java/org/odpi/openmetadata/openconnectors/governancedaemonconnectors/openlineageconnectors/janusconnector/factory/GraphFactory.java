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
import org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.EdgeLabelsLineageGraph;
import org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.VertexLabelsLineageGraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.PROPERTY_KEY_ENTITY_GUID;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.PROPERTY_KEY_ENTITY_VERSION;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.PROPERTY_KEY_LABEL;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.PROPERTY_KEY_METADATA_ID;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.PROPERTY_KEY_RELATIONSHIP_GUID;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.PROPERTY_KEY_RELATIONSHIP_LABEL;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.PROPERTY_NAME_GUID;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.PROPERTY_NAME_LABEL;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.PROPERTY_NAME_METADATA_ID;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.PROPERTY_NAME_VERSION;

public class GraphFactory extends IndexingFactory {

    private static final Logger log = LoggerFactory.getLogger(GraphFactory.class);

    private JanusFactoryBeans janusFactoryBeans = new JanusFactoryBeans();

    /**
     * Set the config for Janus Graph.
     *
     * @param connectionProperties - POJO for the configuration used to create the connector.
     * @return JanusGraph instance with schema and indexes
     *
     * @throws JanusConnectorException if init fails
     */
    public JanusGraph openGraph(ConnectionProperties connectionProperties) throws JanusConnectorException {
        final String methodName = "openGraph";

        JanusGraph janusGraph;
        JanusGraphFactory.Builder config = janusFactoryBeans.getJanusFactory(connectionProperties);

        try {
            janusGraph = config.open();
            return initializeGraph(janusGraph);
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
     */
    private JanusGraph initializeGraph(JanusGraph janusGraph) throws JanusConnectorException {

        final String methodName = "initializeGraph";
        log.info("Updating graph schema, if necessary");
        try {
            JanusGraphManagement management = janusGraph.openManagement();

            Set<String> vertexLabels = schemaBasedOnGraphType(VertexLabelsLineageGraph.class);
            Set<String> relationshipsLabels = schemaBasedOnGraphType(EdgeLabelsLineageGraph.class);

            checkAndAddLabelVertex(management, vertexLabels);
            checkAndAddLabelEdge(management, relationshipsLabels);

            //TODO define properties
            management.commit();

            createIndexes(janusGraph);
            return janusGraph;
        } catch (Exception e) {
            log.error("{} failed  during graph initialize operation with error: {}", e);
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
     * Set up the vertex labels of the schema for the Janus Graph instance
     *
     * @param labels     - set of labels
     * @param management - management instance of Janus Graph
     */
    private void checkAndAddLabelVertex(final JanusGraphManagement management, Set<String> labels) {
        for (String label : labels) {
            if (management.getVertexLabel(label) == null)
                management.makeVertexLabel(label).make();
        }
    }

    /**
     * Set up the edge labels of the schema for the Janus Graph instance
     *
     * @param labels     - set of labels
     * @param management - management instance of Janus Graph
     */
    private void checkAndAddLabelEdge(final JanusGraphManagement management, Set<String> labels) {
        for (String label : labels) {
            if (management.getEdgeLabel(label) == null)
                management.makeEdgeLabel(label).make();
        }
    }

    /**
     * Set up the indexes for the Janus Graph instance
     *
     * @param janusGraph - Janus Graph instance
     */
    private void createIndexes(JanusGraph janusGraph) {
        createCompositeIndexForProperty(PROPERTY_NAME_GUID, PROPERTY_KEY_ENTITY_GUID, true, janusGraph, Vertex.class);
        createCompositeIndexForProperty(PROPERTY_NAME_LABEL, PROPERTY_KEY_LABEL, false, janusGraph, Vertex.class);
        createCompositeIndexForProperty(PROPERTY_NAME_VERSION, PROPERTY_KEY_ENTITY_VERSION, false, janusGraph, Vertex.class);
        createCompositeIndexForProperty(PROPERTY_NAME_METADATA_ID, PROPERTY_KEY_METADATA_ID, false, janusGraph, Vertex.class);
        createCompositeIndexForProperty(PROPERTY_NAME_LABEL, PROPERTY_KEY_RELATIONSHIP_LABEL, false, janusGraph, Edge.class);
        createCompositeIndexForProperty(PROPERTY_NAME_GUID, PROPERTY_KEY_RELATIONSHIP_GUID, false, janusGraph, Edge.class);
    }

}
