/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.openconnectors.governancedaemonconnectors.lineagewarehouseconnectors.janusconnector.graph;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.governanceservers.lineagewarehouse.graph.LineageGraphProviderBase;

import java.util.ArrayList;
import java.util.List;

public class LineageGraphRemoteConnectorProvider extends LineageGraphProviderBase
{
    static final String CONNECTOR_TYPE_GUID = "e2f657d6-e5bd-11e9-81b4-2a2ae2dbcce4";
    static final String CONNECTOR_TYPE_NAME = "Janus Graph Connector for Janus Server remote connection";
    static final String CONNECTOR_TYPE_DESCRIPTION = "Connector supports storing and retrieving entities for lineage using graph hosted on Janus Graph Server.";


    public static final String CLUSTER_PORT = "gremlin.remote.driver.cluster.port";
    public static final String CLUSTER_HOSTS = "gremlin.remote.driver.cluster.hosts";
    public static final String SOURCE_NAME = "gremlin.remote.driver.sourceName";
    public static final String CLUSTER_MIN_CONNECTION_POOL_SIZE = "gremlin.remote.driver.cluster.minConnectionPoolSize";
    public static final String CLUSTER_MAX_CONNECTION_POOL_SIZE = "gremlin.remote.driver.cluster.maxConnectionPoolSize";
    public static final String CLUSTER_MAX_IN_PROCESS_PER_CONNECTION = "gremlin.remote.driver.cluster.maxInProcessPerConnection";
    public static final String CLUSTER_MAX_SIMULTANEOUS_USAGE_PER_CONNECTION = "gremlin.remote.driver.cluster.maxSimultaneousUsagePerConnection";
    public static final String CLUSTER_CREDENTIALS_USERNAME = "gremlin.remote.driver.cluster.credentials.username";
    public static final String CLUSTER_CREDENTIALS_PASSWORD = "gremlin.remote.driver.cluster.credentials.password";
    public static final String CLUSTER_SSL_ENABLE = "gremlin.remote.driver.cluster.ssl.enable";
    public static final String CLUSTER_SSL_SKIP_VALIDATION = "gremlin.remote.driver.cluster.sslSkipCertValidation";
    public static final String CLUSTER_KEYSTORE = "gremlin.remote.driver.cluster.keyStore";
    public static final String CLUSTER_KEYSTORE_TYPE = "gremlin.remote.driver.cluster.keyStoreType";// JKS or PKCS#12
    public static final String CLUSTER_KEYSTORE_PASSWORD = "gremlin.remote.driver.cluster.keyStorePassword";
    public static final String CLUSTER_TRUST_STORE = "gremlin.remote.driver.cluster.trustStore";
    public static final String CLUSTER_TRUST_STORE_PASSWORD = "gremlin.remote.driver.cluster.trustStorePassword";
    public static final String SCHEMA_MANAGEMENT_ENABLE = "remote.schemaManagement.enable";

    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * registry store implementation.
     */
    public LineageGraphRemoteConnectorProvider() {
        super();
        Class connectorClass = LineageGraphConnector.class;
        super.setConnectorClassName(connectorClass.getName());

        ConnectorType connectorType = new ConnectorType();
        connectorType.setType(ConnectorType.getConnectorTypeType());
        connectorType.setGUID(CONNECTOR_TYPE_GUID);
        connectorType.setQualifiedName(CONNECTOR_TYPE_NAME);
        connectorType.setDisplayName(CONNECTOR_TYPE_NAME);
        connectorType.setDescription(CONNECTOR_TYPE_DESCRIPTION);
        connectorType.setConnectorProviderClassName(this.getClass().getName());

        List<String> recognizedPropertyNames = new ArrayList<>();
        recognizedPropertyNames.add(SOURCE_NAME);
        recognizedPropertyNames.add(CLUSTER_HOSTS);
        recognizedPropertyNames.add(CLUSTER_PORT);
        recognizedPropertyNames.add(CLUSTER_MIN_CONNECTION_POOL_SIZE);
        recognizedPropertyNames.add(CLUSTER_MAX_CONNECTION_POOL_SIZE);
        recognizedPropertyNames.add(CLUSTER_MAX_IN_PROCESS_PER_CONNECTION);
        recognizedPropertyNames.add(CLUSTER_MAX_SIMULTANEOUS_USAGE_PER_CONNECTION);
        recognizedPropertyNames.add(CLUSTER_CREDENTIALS_USERNAME);
        recognizedPropertyNames.add(CLUSTER_CREDENTIALS_PASSWORD);
        recognizedPropertyNames.add(CLUSTER_SSL_ENABLE);
        recognizedPropertyNames.add(CLUSTER_SSL_SKIP_VALIDATION);
        recognizedPropertyNames.add(CLUSTER_KEYSTORE);
        recognizedPropertyNames.add(CLUSTER_KEYSTORE_PASSWORD);
        recognizedPropertyNames.add(CLUSTER_KEYSTORE_TYPE);
        recognizedPropertyNames.add(CLUSTER_TRUST_STORE);
        recognizedPropertyNames.add(CLUSTER_TRUST_STORE_PASSWORD);
        recognizedPropertyNames.add(SCHEMA_MANAGEMENT_ENABLE);


        connectorType.setRecognizedConfigurationProperties(recognizedPropertyNames);

        super.connectorTypeBean = connectorType;
    }

}
