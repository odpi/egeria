/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.openconnectors.governancedaemonconnectors.lineagewarehouseconnectors.janusconnector.factory;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.ConfigurationConverter;
import org.apache.tinkerpop.gremlin.driver.Client;
import org.apache.tinkerpop.gremlin.driver.Cluster;
import org.apache.tinkerpop.gremlin.driver.remote.DriverRemoteConnection;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Element;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.janusgraph.core.JanusGraph;
import org.janusgraph.core.JanusGraphFactory;
import org.odpi.openmetadata.openconnectors.governancedaemonconnectors.lineagewarehouseconnectors.janusconnector.graph.LineageGraphConnectorProvider;
import org.odpi.openmetadata.openconnectors.governancedaemonconnectors.lineagewarehouseconnectors.janusconnector.graph.LineageGraphRemoteConnectorProvider;
import org.odpi.openmetadata.openconnectors.governancedaemonconnectors.lineagewarehouseconnectors.janusconnector.model.GraphDetails;
import org.odpi.openmetadata.openconnectors.governancedaemonconnectors.lineagewarehouseconnectors.janusconnector.model.JanusConnectorErrorCode;
import org.odpi.openmetadata.openconnectors.governancedaemonconnectors.lineagewarehouseconnectors.janusconnector.model.ffdc.JanusConnectorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Properties;

import static org.apache.tinkerpop.gremlin.process.traversal.AnonymousTraversalSource.traversal;
import static org.janusgraph.util.system.ConfigurationUtil.loadMapConfiguration;

public class GraphFactory {

    private static final Logger log = LoggerFactory.getLogger(GraphFactory.class);
    private Graph graph;
    private boolean supportingTransactions;
    private Cluster cluster;
    private Client client;
    private OpenLineageSchemaHelper<Element> openLineageSchemaHelper;

    /**
     * Set the config for Janus Graph.
     *
     * @param providerClass           - Provider Class name to be used
     * @param configurationProperties - The configuration properties for janusGraph
     * @return
     * @throws JanusConnectorException if init fails
     */
    public GraphDetails openGraph(String providerClass, Map<String, Object> configurationProperties) throws JanusConnectorException {
        openLineageSchemaHelper = new OpenLineageSchemaHelper<>();
        GraphTraversalSource graphTraversalSource = null;
        boolean supportingTransactions = false;
        boolean isRemoteGraph = false;
        if (providerClass.equals(LineageGraphConnectorProvider.class.getName())) {
            graphTraversalSource = openEmbeddedGraph(configurationProperties);
            supportingTransactions = true;
            isRemoteGraph = false;
        }

        if (providerClass.equals(LineageGraphRemoteConnectorProvider.class.getName())) {
            graphTraversalSource = openRemoteGraph(configurationProperties);
            supportingTransactions = false;
            isRemoteGraph = true;
        }
        return new GraphDetails(graphTraversalSource, supportingTransactions, isRemoteGraph);

    }


    /**
     * Open the graph when embedded mode is used.
     *
     * @param properties - POJO for the configuration used to create the connector.
     * @return JanusGraph graph Gremlin
     * @throws JanusConnectorException if open fails
     */
    private GraphTraversalSource openEmbeddedGraph(Map<String, Object> properties) throws JanusConnectorException {
        final String methodName = "openEmbeddedGraph";
        try {
            graph = JanusGraphFactory.open(loadMapConfiguration(properties));
            JanusGraph janusGraph = (JanusGraph) graph;
            initializeGraph(janusGraph);
            return janusGraph.traversal();
        } catch (Exception e) {
            log.error("A connection with the graph database could not be established with the provided configuration", e);
            JanusConnectorErrorCode errorCode    = JanusConnectorErrorCode.CANNOT_OPEN_GRAPH_DB;
            String                  errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(e.getMessage(), methodName, GraphFactory.class.getName());
            throw mapConnectorException(methodName, errorMessage, errorCode);
        }
    }

    /**
     * Open the graph when remote mode is used.
     *
     * @param properties - POJO for the configuration used to create the connector.
     * @return GraphTraversalSource DSL of Gremlin
     * @throws JanusConnectorException if open fails
     */
    private GraphTraversalSource openRemoteGraph(Map<String, Object> properties) throws JanusConnectorException {
        String methodName = "openRemoteGraph";
        this.supportingTransactions = false;

        try {
            cluster = openCluster(properties);
            client = cluster.connect(); // client is used to access JanusGraph management API
            if (isSchemaManagementEnabled(properties)) {
                initializeRemoteGraph(client);
            }
            return traversal().withRemote(DriverRemoteConnection.using(cluster, properties.getOrDefault(LineageGraphRemoteConnectorProvider.SOURCE_NAME, "g").toString()));
        } catch (Exception e) {
            log.error("A connection with the graph database could not be established with the provided configuration", e);
            JanusConnectorErrorCode errorCode = JanusConnectorErrorCode.CANNOT_OPEN_GRAPH_DB;
            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(e.getMessage(), methodName, GraphFactory.class.getName());
            throw mapConnectorException(methodName, errorMessage, errorCode);
        }
    }

    private boolean isSchemaManagementEnabled(Map<String, Object> properties) {
        return properties.get(LineageGraphRemoteConnectorProvider.SCHEMA_MANAGEMENT_ENABLE) != null &&
                "true".equalsIgnoreCase(properties.get(LineageGraphRemoteConnectorProvider.SCHEMA_MANAGEMENT_ENABLE).toString());
    }

    /**
     * Set up the schema for the Janus Graph instance
     *
     * @param janusGraph - Janus Graph instance
     * @throws JanusConnectorException if initialization fails
     */
    private void initializeGraph(JanusGraph janusGraph) throws JanusConnectorException {

        final String methodName = "initializeGraph";
        log.debug("Initializing graph. Updating schema, if necessary.");
        try {
            openLineageSchemaHelper.createSchemas(janusGraph);
            openLineageSchemaHelper.createIndexes(janusGraph);
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
     * @throws JanusConnectorException if initialization fails
     */
    private void initializeRemoteGraph(Client client) throws JanusConnectorException {
        final String methodName = "initializeRemoteGraph";
        log.debug("Initializing graph remotely. Updating schema, if necessary.");
        try {
            openLineageSchemaHelper.createLabels(client);
            openLineageSchemaHelper.createIndexes(client);

        } catch (Exception e) {
            log.error("Failed  during graph initialize operation ", e);
            JanusConnectorErrorCode errorCode = JanusConnectorErrorCode.GRAPH_INITIALIZATION_ERROR;
            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(e.getMessage(), methodName, GraphFactory.class.getName());
            throw mapConnectorException(methodName, errorMessage, errorCode);
        }
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
     * Closes the graph instance.
     */
    public void closeGraph() {

        try {
            if (graph != null) {
                graph.close();
            }
            if (cluster != null) {
                cluster.close();
            }
        } catch (Exception e) {
            log.error("Exception while closing the graph.", e);
        } finally {
            graph = null;
            client = null;
            cluster = null;
        }

    }

    private JanusConnectorException mapConnectorException(String methodName, String errorMessage, JanusConnectorErrorCode errorCode) {
        return new JanusConnectorException(GraphFactory.class.getName(),
                methodName,
                errorMessage,
                errorCode.getSystemAction(),
                errorCode.getUserAction());
    }
}
