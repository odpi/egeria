/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.factory;

import org.apache.tinkerpop.gremlin.driver.Client;
import org.apache.tinkerpop.gremlin.driver.Cluster;
import org.apache.tinkerpop.gremlin.driver.remote.DriverRemoteConnection;
import org.apache.tinkerpop.gremlin.driver.ser.GryoMessageSerializerV3d0;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.structure.io.gryo.GryoMapper;
import org.janusgraph.core.JanusGraph;
import org.janusgraph.core.schema.JanusGraphManagement;
import org.janusgraph.graphdb.tinkerpop.JanusGraphIoRegistry;
import org.odpi.openmetadata.frameworks.connectors.properties.ConnectionProperties;
import org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.graph.LineageGraphConnectorProvider;
import org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.graph.LineageGraphRemoteConnectorProvider;
import org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.model.JanusConnectorErrorCode;
import org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.model.ffdc.JanusConnectorException;
import org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.EdgeLabelsLineageGraph;
import org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.VertexLabelsLineageGraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
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

    protected Graph graph;
    protected GraphTraversalSource g;
    protected boolean supportingTransactions;
    protected Map<String,Object> properties;
    private Cluster cluster;
    private Client client;

    /**
     * Set the config for Janus Graph.
     *
     * @param providerClass        - Provider Class name to be used
     * @param connectionProperties - POJO for the configuration used to create the connector.
     * @return GraphTraversalSource DSL of Gremlin
     *
     * @throws JanusConnectorException if init fails
     */
    public GraphTraversalSource openGraph(String providerClass,ConnectionProperties connectionProperties) throws JanusConnectorException {

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
    private GraphTraversalSource openEmbeddedGraph(Map<String,Object> properties) throws JanusConnectorException{
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
            throw new JanusConnectorException(GraphFactory.class.getName(),
                                              methodName,
                                              errorMessage,
                                              errorCode.getSystemAction(),
                                              errorCode.getUserAction());
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
    private GraphTraversalSource openRemoteGraph(Map<String,Object> properties) throws JanusConnectorException{
        String methodName = "openRemoteGraph";
        this.supportingTransactions = true;
        this.properties = properties;

        try {
            cluster = createCluster();
            client = cluster.connect();

            return traversal().withRemote(DriverRemoteConnection.using(cluster, this.properties.get(LineageGraphRemoteConnectorProvider.SOURCE_NAME).toString()));
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
     *
     * @throws JanusConnectorException if initialization fails
     */
    private void initializeGraph(JanusGraph janusGraph) throws JanusConnectorException {

        final String methodName = "initializeGraph";
        log.info("Updating graph schema, if necessary");
        try {
            JanusGraphManagement management = janusGraph.openManagement();

            Set<String> vertexLabels = schemaBasedOnGraphType(VertexLabelsLineageGraph.class);
            Set<String> relationshipsLabels = schemaBasedOnGraphType(EdgeLabelsLineageGraph.class);

            checkAndAddLabelVertex(management, vertexLabels);
            checkAndAddLabelEdge(management, relationshipsLabels);

            management.commit();

            createIndexes(janusGraph);
        } catch (Exception e) {
            log.error("{} failed  during graph initialize operation with error: ", e);
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

    /**
     * Initiates a Cluster connection with the provided configuration
     *
     */
    private Cluster createCluster() throws JanusConnectorException {
        String methodName = "createCluster";
        try {

            GryoMapper.Builder builder = GryoMapper.build().addRegistry(JanusGraphIoRegistry.getInstance()); //TODO: Check for replacement
            Cluster.Builder clusterBuilder = Cluster.build()
                    .port(Integer.parseInt(properties.get(LineageGraphRemoteConnectorProvider.CLUSTER_PORT).toString()))
                    .addContactPoints(((List<String>) properties.get(LineageGraphRemoteConnectorProvider.CLUSTER_HOSTS)).toArray(new String[0]))
                    .serializer(new GryoMessageSerializerV3d0(builder)); //TODO: Check this setting. Binary serializer was not working.

            if (properties.get(LineageGraphRemoteConnectorProvider.CLUSTER_CREDENTIALS_USERNAME) != null && properties.get(LineageGraphRemoteConnectorProvider.CLUSTER_CREDENTIALS_USERNAME) != null)
                clusterBuilder.credentials(properties.get(LineageGraphRemoteConnectorProvider.CLUSTER_CREDENTIALS_USERNAME).toString(), properties.get(LineageGraphRemoteConnectorProvider.CLUSTER_TRUST_STORE).toString());


            if (properties.get(LineageGraphRemoteConnectorProvider.CLUSTER_MIN_CONNECTION_POOL_SIZE) != null)
                clusterBuilder.minConnectionPoolSize(Integer.parseInt(properties.get(LineageGraphRemoteConnectorProvider.CLUSTER_MIN_CONNECTION_POOL_SIZE).toString()));
            if (properties.get(LineageGraphRemoteConnectorProvider.CLUSTER_MAX_CONNECTION_POOL_SIZE) != null)
                clusterBuilder.maxConnectionPoolSize(Integer.parseInt(properties.get(LineageGraphRemoteConnectorProvider.CLUSTER_MAX_CONNECTION_POOL_SIZE).toString()));
            if (properties.get(LineageGraphRemoteConnectorProvider.CLUSTER_MAX_SIMULTANEOUS_USAGE_PER_CONNECTION) != null)
                clusterBuilder.maxSimultaneousUsagePerConnection(Integer.parseInt(properties.get(LineageGraphRemoteConnectorProvider.CLUSTER_MAX_SIMULTANEOUS_USAGE_PER_CONNECTION).toString()));
            if (properties.get(LineageGraphRemoteConnectorProvider.CLUSTER_MAX_IN_PROCESS_PER_CONNECTION) != null)
                clusterBuilder.maxInProcessPerConnection(Integer.parseInt(properties.get(LineageGraphRemoteConnectorProvider.CLUSTER_MAX_IN_PROCESS_PER_CONNECTION).toString()));

            if (properties.get(LineageGraphRemoteConnectorProvider.CLUSTER_SSL_ENABLE) != null && properties.get(LineageGraphRemoteConnectorProvider.CLUSTER_SSL_ENABLE).toString().equalsIgnoreCase("true")) {
                clusterBuilder.enableSsl(true);
                if (properties.get(LineageGraphRemoteConnectorProvider.CLUSTER_KEYSTORE_TYPE) != null)
                    clusterBuilder.keyStoreType(properties.get(LineageGraphRemoteConnectorProvider.CLUSTER_KEYSTORE_TYPE).toString());
                if (properties.get(LineageGraphRemoteConnectorProvider.CLUSTER_KEYSTORE) != null)
                    clusterBuilder.keyStore(properties.get(LineageGraphRemoteConnectorProvider.CLUSTER_KEYSTORE).toString());
                if (properties.get(LineageGraphRemoteConnectorProvider.CLUSTER_KEYSTORE_PASSWORD) != null)
                    clusterBuilder.keyStorePassword(properties.get(LineageGraphRemoteConnectorProvider.CLUSTER_KEYSTORE_PASSWORD).toString());
                if (properties.get(LineageGraphRemoteConnectorProvider.CLUSTER_SSL_SKIP_VALIDATION) != null)
                    clusterBuilder.sslSkipCertValidation(properties.get(LineageGraphRemoteConnectorProvider.CLUSTER_SSL_SKIP_VALIDATION).toString().equalsIgnoreCase("true"));
                if (properties.get(LineageGraphRemoteConnectorProvider.CLUSTER_TRUST_STORE) != null)
                    clusterBuilder.trustStore(properties.get(LineageGraphRemoteConnectorProvider.CLUSTER_TRUST_STORE).toString());
                if (properties.get(LineageGraphRemoteConnectorProvider.CLUSTER_TRUST_STORE_PASSWORD) != null)
                    clusterBuilder.trustStorePassword(properties.get(LineageGraphRemoteConnectorProvider.CLUSTER_TRUST_STORE_PASSWORD).toString());
            }

            return clusterBuilder.create();
        }
        catch (Exception e){
            log.error("Cluster initiation for remote connection to the graph  failed with error: ", e);
            JanusConnectorErrorCode errorCode = JanusConnectorErrorCode.GRAPH_CLUSTER_INIT_FAILED;
            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(e.getMessage(), methodName, GraphFactory.class.getName());
            throw new JanusConnectorException(GraphFactory.class.getName(),
                                              methodName,
                                              errorMessage,
                                              errorCode.getSystemAction(),
                                              errorCode.getUserAction());
        }
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


}
