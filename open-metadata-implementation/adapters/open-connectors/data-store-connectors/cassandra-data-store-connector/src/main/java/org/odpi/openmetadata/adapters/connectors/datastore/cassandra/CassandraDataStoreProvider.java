/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.datastore.cassandra;

import org.odpi.openmetadata.frameworks.connectors.ConnectorProviderBase;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;

import java.util.ArrayList;
import java.util.List;

/**
 * CassandraDataStoreProvider is the OCF connector provider for the Apache Cassandra Database connector.
 */
public class CassandraDataStoreProvider extends ConnectorProviderBase
{
    static final String  connectorTypeGUID = "f8be6779-6b8d-4b38-9349-bbb23b253ebf";
    static final String  connectorTypeName = "Cassandra Data Store Connector";
    static final String  connectorTypeDescription = "Connector supports connection to Apache Cassandra Database";
    public static final String  port = "port";

    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * data store implementation.
     */
    public CassandraDataStoreProvider()
    {
        Class<?> connectorClass = CassandraDataStoreConnector.class;

        super.setConnectorClassName(connectorClass.getName());

        ConnectorType connectorType = new ConnectorType();
        connectorType.setType(ConnectorType.getConnectorTypeType());
        connectorType.setGUID(connectorTypeGUID);
        connectorType.setQualifiedName(connectorTypeName);
        connectorType.setDisplayName(connectorTypeName);
        connectorType.setDescription(connectorTypeDescription);
        connectorType.setConnectorProviderClassName(this.getClass().getName());

        List<String> recognizedConfigurationProperties = new ArrayList<>();
        recognizedConfigurationProperties.add(port);
        connectorType.setRecognizedConfigurationProperties(recognizedConfigurationProperties);

        super.connectorTypeBean = connectorType;
    }
}
