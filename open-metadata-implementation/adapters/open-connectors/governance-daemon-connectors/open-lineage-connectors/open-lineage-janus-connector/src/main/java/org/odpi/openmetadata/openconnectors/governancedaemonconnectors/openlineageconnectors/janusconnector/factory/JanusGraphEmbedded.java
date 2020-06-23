/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.factory;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.janusgraph.core.JanusGraph;
import org.janusgraph.core.schema.JanusGraphManagement;
import org.odpi.openmetadata.frameworks.connectors.properties.ConnectionProperties;
import org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.EdgeLabelsLineageGraph;
import org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.VertexLabelsLineageGraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.*;

public class JanusGraphEmbedded extends GraphGremlinBase {

    private static final Logger log = LoggerFactory.getLogger(JanusGraphEmbedded.class);

    private JanusGraph janusGraph;

    public JanusGraphEmbedded(ConnectionProperties connectionProperties){
        super(connectionProperties);
        this.supportingTransactions = true;
    }

    @Override
    public GraphTraversalSource openGraph() {
        super.openGraph();
        janusGraph = getJanusGraph();
        createSchema();
        return g;
    }

    @Override
    protected void createSchema() {

        final String methodName = "initializeGraph";
        log.info("Updating graph schema, if necessary");
        try {
            JanusGraphManagement management = janusGraph.openManagement();

            Set<String> vertexLabels = schemaBasedOnGraphType(VertexLabelsLineageGraph.class);
            Set<String> relationshipsLabels = schemaBasedOnGraphType(EdgeLabelsLineageGraph.class);

            checkAndAddLabelVertex(management, vertexLabels);
            checkAndAddLabelEdge(management, relationshipsLabels);

            management.commit();

            createIndexes();
        } catch (Exception e) {
            log.error("failed  during graph schema creation operation with error: ", e);
//            JanusConnectorErrorCode errorCode = JanusConnectorErrorCode.GRAPH_INITIALIZATION_ERROR;
//            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(e.getMessage(), methodName, GraphFactory.class.getName());
//            throw new JanusConnectorException(GraphFactory.class.getName(),
//                    methodName,
//                    errorMessage,
//                    errorCode.getSystemAction(),
//                    errorCode.getUserAction());
        }
//        }
    }

    /**
     * Returns the JanusGraph instance.
     */
    public JanusGraph getJanusGraph() {
        return (JanusGraph) graph;
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
     **/
    private void createIndexes() {
        IndexingFactory indexingFactory = new IndexingFactory();
        indexingFactory.createCompositeIndexForProperty(PROPERTY_NAME_GUID, PROPERTY_KEY_ENTITY_GUID, true, janusGraph, Vertex.class);
        indexingFactory.createCompositeIndexForProperty(PROPERTY_NAME_LABEL, PROPERTY_KEY_LABEL, false, janusGraph, Vertex.class);
        indexingFactory.createCompositeIndexForProperty(PROPERTY_NAME_VERSION, PROPERTY_KEY_ENTITY_VERSION, false, janusGraph, Vertex.class);
        indexingFactory.createCompositeIndexForProperty(PROPERTY_NAME_METADATA_ID, PROPERTY_KEY_METADATA_ID, false, janusGraph, Vertex.class);
        indexingFactory.createCompositeIndexForProperty(PROPERTY_NAME_LABEL, PROPERTY_KEY_RELATIONSHIP_LABEL, false, janusGraph, Edge.class);
        indexingFactory.createCompositeIndexForProperty(PROPERTY_NAME_GUID, PROPERTY_KEY_RELATIONSHIP_GUID, false, janusGraph, Edge.class);
    }
}
