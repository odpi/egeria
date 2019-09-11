/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.cassandra;

import org.odpi.openmetadata.frameworks.connectors.ConnectorProviderBase;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;

/**
 * The type Cassandra store provider.
 */
public class CassandraStoreProvider extends ConnectorProviderBase {

    /**
     * The Connector type guid.
     */
    static final String  connectorTypeGUID = "d970c3db-46ba-4e96-8d7f-d36e7ed9e84d";
    /**
     * The Connector type name.
     */
    static final String  connectorTypeName = "Apache Cassandra Data Store Connector";
    /**
     * The Connector type description.
     */
    static final String  connectorTypeDescription = "Connector supports retrieving data assets from Cassandra clusters.";


    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * registry store implementation.
     */
    public CassandraStoreProvider() {
        Class connectorClass = CassandraStoreConnector.class;
        super.setConnectorClassName(connectorClass.getName());

        ConnectorType connectorType = new ConnectorType();
        connectorType.setType(ConnectorType.getConnectorTypeType());
        connectorType.setGUID(connectorTypeGUID);
        connectorType.setQualifiedName(connectorTypeName);
        connectorType.setDisplayName(connectorTypeName);
        connectorType.setDescription(connectorTypeDescription);
        connectorType.setConnectorProviderClassName(this.getClass().getName());

        super.connectorTypeBean = connectorType;
    }
}
