/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.factory;

import org.apache.tinkerpop.gremlin.driver.Client;
import org.apache.tinkerpop.gremlin.driver.Cluster;
import org.apache.tinkerpop.gremlin.driver.Result;
import org.apache.tinkerpop.gremlin.driver.ResultSet;
import org.apache.tinkerpop.gremlin.driver.remote.DriverRemoteConnection;
import org.apache.tinkerpop.gremlin.driver.ser.GryoMessageSerializerV3d0;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.io.gryo.GryoMapper;
import org.janusgraph.graphdb.tinkerpop.JanusGraphIoRegistry;
import org.odpi.openmetadata.frameworks.connectors.properties.ConnectionProperties;
import org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.graph.LineageGraphRemoteConnectorProvider;
import org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.EdgeLabelsLineageGraph;
import org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.VertexLabelsLineageGraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Stream;

import static org.apache.tinkerpop.gremlin.process.traversal.AnonymousTraversalSource.traversal;
import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils.GraphConstants.*;

public class JanusGraphRemote extends GraphGremlinBase {

    private static final String ADD_VERTEX_LABEL_IF_MISSING_FORMAT =
            "if (management.getVertexLabel(\"%s\") == null ) { management.makeVertexLabel(\"%s\").make(); }\n";

    private static final String ADD_EDGE_LABEL_IF_MISSING_FORMAT =
            "if (management.getEdgeLabel(\"%s\") == null ) { management.makeEdgeLabel(\"%s\").make(); }\n";

    private static final String VERTEX = "Vertex";
    private static final String EDGE = "Edge";

    private static final Logger log = LoggerFactory.getLogger(JanusGraphRemote.class);

    private Cluster cluster;
    private Client client;

    public JanusGraphRemote(ConnectionProperties connectionProperties) {
        super(connectionProperties);
        this.supportingTransactions = false;
    }


    @Override
    public GraphTraversalSource openGraph() {
        cluster = createCluster();
        client = cluster.connect();
        GraphTraversalSource g = traversal().withRemote(DriverRemoteConnection.using(cluster, this.properties.get(LineageGraphRemoteConnectorProvider.SOURCE_NAME).toString()));
        createSchema();
        return g;
    }

    private Cluster createCluster() {

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

    @Override
    public void createSchema() {

        String createLabels = createLabels();
        client.submit(createLabels);

        String indexCommandGuid = createIndex("vertexIndexCompositevertex--guid", PROPERTY_KEY_ENTITY_GUID, true, VERTEX);
        String indexCommandLabel = createIndex("vertexIndexCompositevertex--label", PROPERTY_KEY_LABEL, false, VERTEX);
        String indexCommandVersion = createIndex("vertexIndexCompositevertex--version", PROPERTY_KEY_ENTITY_VERSION, false, VERTEX);
        String indexCommandMetadataCollectionId = createIndex("vertexIndexCompositevertex--metadataCollectionId", PROPERTY_KEY_METADATA_ID, false, VERTEX);
        String indexCommandEdgeGuid = createIndex("edgeIndexCompositeedge--guid", PROPERTY_KEY_RELATIONSHIP_GUID, false, EDGE);
        String indexCommandEdgeLabel = createIndex("edgeIndexCompositeedge--label", PROPERTY_KEY_RELATIONSHIP_LABEL, false, EDGE);

        client.submit(indexCommandGuid);
        client.submit(indexCommandLabel);
        client.submit(indexCommandVersion);
        client.submit(indexCommandMetadataCollectionId);
        client.submit(indexCommandEdgeGuid);
        client.submit(indexCommandEdgeLabel);

    }

    private String createLabels() {
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

    private static String createIndex(String indexName, String propertyName, boolean hasPropertyUniqueAndConsistency, String className) {
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

    @Override
    public void closeGraph() {

        try {
            if (g != null) {
                g.close();
            }
            if (cluster != null) {
                cluster.close();
            }
        } catch (Exception e) {
            log.error("Exception while closing.", e);
        } finally {
            g = null;
            graph = null;
            client = null;
            cluster = null;
        }

    }
}
