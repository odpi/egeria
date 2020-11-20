/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.factory;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationConverter;
import org.apache.tinkerpop.gremlin.driver.Client;
import org.apache.tinkerpop.gremlin.driver.Cluster;
import org.apache.tinkerpop.gremlin.driver.remote.DriverRemoteConnection;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.janusgraph.core.JanusGraph;
import org.janusgraph.core.schema.JanusGraphManagement;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.properties.ConnectionProperties;
import org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.graph.LineageGraphConnectorProvider;
import org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.graph.LineageGraphRemoteConnectorProvider;
import org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.model.JanusConnectorErrorCode;
import org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.model.ffdc.JanusConnectorException;
import org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.EdgeLabelsLineageGraph;
import org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.VertexLabelsLineageGraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.apache.tinkerpop.gremlin.process.traversal.AnonymousTraversalSource.traversal;
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
    private Graph graph;
    private GraphTraversalSource g;
    private boolean supportingTransactions;
    private Cluster cluster;
    private Client client;

    private static final String ADD_VERTEX_LABEL_IF_MISSING_FORMAT =
            "if (management.getVertexLabel(\"%s\") == null ) { management.makeVertexLabel(\"%s\").make(); }\n";
    private static final String ADD_EDGE_LABEL_IF_MISSING_FORMAT =
            "if (management.getEdgeLabel(\"%s\") == null ) { management.makeEdgeLabel(\"%s\").make(); }\n";
    private static final String VERTEX = "Vertex";
    private static final String EDGE = "Edge";

    /**
     * Set the config for Janus Graph.
     *
     * @param providerClass        - Provider Class name to be used
     * @param connectionProperties - POJO for the configuration used to create the connector.
     * @param auditLog             - Used for logging errors
     * @return GraphTraversalSource DSL of Gremlin
     *
     * @throws JanusConnectorException if init fails
     */
    public GraphTraversalSource openGraph(String providerClass, ConnectionProperties connectionProperties, AuditLog auditLog) throws JanusConnectorException {
        super.auditLog = auditLog;
        if (providerClass.equals(LineageGraphConnectorProvider.class.getName())) {
          return openEmbeddedGraph(connectionProperties.getConfigurationProperties());
        }

        if (providerClass.equals(LineageGraphRemoteConnectorProvider.class.getName())) {
            return openRemoteGraph(connectionProperties.getConfigurationProperties());
        }

        return null;
    }


    /**
     * Open the graph when embedded mode is used.
     *
     * @param properties - POJO for the configuration used to create the connector.
     * @return GraphTraversalSource DSL of Gremlin
     *
     * @throws JanusConnectorException if open fails
     */
    private GraphTraversalSource openEmbeddedGraph(Map<String, Object> properties) throws JanusConnectorException{
        final String methodName = "openEmbeddedGraph";

        try {
            graph = org.apache.tinkerpop.gremlin.structure.util.GraphFactory.open(properties);
            g = graph.traversal();
            JanusGraph janusGraph = (JanusGraph) graph;
            initializeGraph(janusGraph);
            supportingTransactions = true;
            return g;
        } catch (Exception e) {
            log.error("A connection with the graph database could not be established with the provided configuration", e);
            JanusConnectorErrorCode errorCode = JanusConnectorErrorCode.CANNOT_OPEN_GRAPH_DB;
            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(e.getMessage(), methodName, GraphFactory.class.getName());
            throw mapConnectorException(methodName, errorMessage, errorCode);
        }
    }

    /**
     * Open the graph when remote mode is used.
     *
     * @param properties - POJO for the configuration used to create the connector.
     * @return GraphTraversalSource DSL of Gremlin
     *
     * @throws JanusConnectorException if open fails
     */
    private GraphTraversalSource openRemoteGraph(Map<String, Object> properties) throws JanusConnectorException{
        String methodName = "openRemoteGraph";
        this.supportingTransactions = false;

        try {
            cluster = openCluster(properties);
            client = cluster.connect(); // client is used to access JanusGraph management API
            if (properties.get(LineageGraphRemoteConnectorProvider.SCHEMA_MANAGEMENT_ENABLE)!=null && properties.get(LineageGraphRemoteConnectorProvider.SCHEMA_MANAGEMENT_ENABLE).toString().equalsIgnoreCase("true")) {
                initializeRemoteGraph(client);
            }
            return traversal().withRemote(DriverRemoteConnection.using(cluster, properties.getOrDefault(LineageGraphRemoteConnectorProvider.SOURCE_NAME,"g").toString()));
        } catch (Exception e) {
            log.error("A connection with the graph database could not be established with the provided configuration", e);
            JanusConnectorErrorCode errorCode = JanusConnectorErrorCode.CANNOT_OPEN_GRAPH_DB;
            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(e.getMessage(), methodName, GraphFactory.class.getName());
            throw mapConnectorException(methodName, errorMessage, errorCode);
        }
    }

    /**
     * Set up the schema for the Janus Graph instance
     *
     * @param janusGraph - Janus Graph instance
     *
     * @throws JanusConnectorException if initialization fails
     */
    private void initializeGraph(JanusGraph janusGraph) throws JanusConnectorException {

        final String methodName = "initializeGraph";
        log.debug("Initializing graph. Updating schema, if necessary.");
        try {
            JanusGraphManagement management = janusGraph.openManagement();

            Set<String> vertexLabels = schemaBasedOnGraphType(VertexLabelsLineageGraph.class);
            Set<String> relationshipsLabels = schemaBasedOnGraphType(EdgeLabelsLineageGraph.class);

            checkAndAddLabelVertex(management, vertexLabels);
            checkAndAddLabelEdge(management, relationshipsLabels);

            management.commit();

            createIndexes(janusGraph);
        } catch (Exception e) {
            log.error("Failed  during graph initialize operation", e);
            JanusConnectorErrorCode errorCode = JanusConnectorErrorCode.GRAPH_INITIALIZATION_ERROR;
            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(e.getMessage(), methodName, GraphFactory.class.getName());
            throw mapConnectorException(methodName, errorMessage, errorCode);
        }
    }

    /**
     * Set up the schema for the Janus Graph instance
     *
     * @param client - instance of the client for remote cluster
     *
     * @throws JanusConnectorException if initialization fails
     */
    private void initializeRemoteGraph(Client client) throws JanusConnectorException {
        final String methodName = "initializeRemoteGraph";
        log.debug("Initializing graph remotely. Updating schema, if necessary.");
        try {

            String createLabels = createLabelsCommand();
            log.debug("Checking labels...");
            client.submit(createLabels);

            String indexCommandGuid = createIndexCommand("vertexIndexCompositevertex--guid", PROPERTY_KEY_ENTITY_GUID, true, VERTEX);
            String indexCommandLabel = createIndexCommand("vertexIndexCompositevertex--label", PROPERTY_KEY_LABEL, false, VERTEX);
            String indexCommandVersion = createIndexCommand("vertexIndexCompositevertex--version", PROPERTY_KEY_ENTITY_VERSION, false, VERTEX);
            String indexCommandMetadataCollectionId = createIndexCommand("vertexIndexCompositevertex--metadataCollectionId", PROPERTY_KEY_METADATA_ID, false, VERTEX);
            String indexCommandEdgeGuid = createIndexCommand("edgeIndexCompositeedge--guid", PROPERTY_KEY_RELATIONSHIP_GUID, false, EDGE);
            String indexCommandEdgeLabel = createIndexCommand("edgeIndexCompositeedge--label", PROPERTY_KEY_RELATIONSHIP_LABEL, false, EDGE);

            log.debug("Checking indices...");
            client.submit(indexCommandGuid);
            client.submit(indexCommandLabel);
            client.submit(indexCommandVersion);
            client.submit(indexCommandMetadataCollectionId);
            client.submit(indexCommandEdgeGuid);
            client.submit(indexCommandEdgeLabel);

        } catch (Exception e) {
            log.error("Failed  during graph initialize operation ", e);
            JanusConnectorErrorCode errorCode = JanusConnectorErrorCode.GRAPH_INITIALIZATION_ERROR;
            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(e.getMessage(), methodName, GraphFactory.class.getName());
            throw mapConnectorException(methodName, errorMessage, errorCode);
        }
    }

    /**
     *
     * @param aEnum Enum with label names
     * @param <T> Vertex or Edge gremlin type
     * @return Set of gremlin label names for enumerated label types
     */
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

    /**
     * Creates cluster instance by directly mapping connector configuration properties to commons Configuration that uses standardized Thinker-pop Settings.
     * More information on how to configure Gremlin Driver: https://tinkerpop.apache.org/docs/current/reference/#_configuration
     *
     * @return Cluster object to be used by gremlin driver client.
     */
    private Cluster openCluster(Map configurationProperties) {
        Properties p = new Properties();
        p.putAll(configurationProperties);
        Configuration config = ConfigurationConverter.getConfiguration(p);
        return Cluster.open(config);
    }

    /**
     *
     * @return String representation of gremlin command to create vertex and edge labels using janus graph management API.
     */
    private String createLabelsCommand() {
        StringBuilder managementCommand = new StringBuilder();
        managementCommand.append("JanusGraphManagement management = graph.openManagement();\n");
        for (VertexLabelsLineageGraph vertexLabel : VertexLabelsLineageGraph.values()) {
            managementCommand.append(String.format(ADD_VERTEX_LABEL_IF_MISSING_FORMAT, vertexLabel, vertexLabel));
        }

        for (EdgeLabelsLineageGraph edgeLabel : EdgeLabelsLineageGraph.values()) {
            managementCommand.append(String.format(ADD_EDGE_LABEL_IF_MISSING_FORMAT, edgeLabel, edgeLabel));
        }
        managementCommand.append("management.commit();");
        return managementCommand.toString();
    }

    /**
     *
     * @param indexName - name of the index
     * @param propertyName - name of the property being indexed
     * @param hasPropertyUniqueAndConsistency - is the unique constraint index
     * @param className - the type of the index class to be used Vertex or Edge
     * @return String representation of gremlin command to create index for property name using janus graph management API.
     */
    private static String createIndexCommand(String indexName, String propertyName, boolean hasPropertyUniqueAndConsistency, String className) {
        StringBuilder indexCommand = new StringBuilder();
        indexCommand.append("management = graph.openManagement();\n");
        indexCommand.append("vertexIndex = management.getGraphIndex(\"").append(indexName).append("\");\n");
        indexCommand.append("if (vertexIndex != null ){   management.rollback(); }\n");
        indexCommand.append(" else { ");
        indexCommand.append("propertyKeyGuid = management.makePropertyKey(\"").append(propertyName).append("\").dataType(String.class).make();\n");
        indexCommand.append("indexBuilderGuidVertex = management.buildIndex(\"").append(indexName).append("\", ").append(className).append(".class).addKey(propertyKeyGuid);\n");
        if (hasPropertyUniqueAndConsistency) {
            indexCommand.append("indexBuilderGuidVertex.unique();\n");
        }
        indexCommand.append("indexGuidVertex = indexBuilderGuidVertex.buildCompositeIndex();\n");
        if (hasPropertyUniqueAndConsistency) {
            indexCommand.append("management.setConsistency(indexGuidVertex, ConsistencyModifier.LOCK);\n");
        }
        indexCommand.append("management.commit();\n");
        indexCommand.append("management = graph.openManagement();\n");
        indexCommand.append("ManagementSystem.awaitGraphIndexStatus(graph,\"").append(indexName).append("\").timeout(15, ChronoUnit.SECONDS).call();\n");
        indexCommand.append("management.updateIndex(management.getGraphIndex(\"").append(indexName).append("\"), SchemaAction.REINDEX).get();\n");
        indexCommand.append("management.commit();\n");
        indexCommand.append("}\n");
        return indexCommand.toString();
    }


    /**
     * Closes the graph instance.
     */
    public void closeGraph() {

        try {
            if (g != null) {
                g.close();
            }
            if (graph != null) {
                graph.close();
            }
            if (cluster != null) {
                cluster.close();
            }
        } catch(Exception e) {
            log.error("Exception while closing the graph.",e);
        } finally {
            g = null;
            graph = null;
            client = null;
            cluster = null;
        }

    }

    public boolean isSupportingTransactions() {
        return supportingTransactions;
    }

    private JanusConnectorException mapConnectorException(String methodName, String errorMessage, JanusConnectorErrorCode errorCode) {
        return new JanusConnectorException(GraphFactory.class.getName(),
                methodName,
                errorMessage,
                errorCode.getSystemAction(),
                errorCode.getUserAction());
    }


}
