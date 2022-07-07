/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.datastore.relational;

import org.odpi.openmetadata.frameworks.connectors.ConnectorProviderBase;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;


/**
 * JdbcConnectorProvider is the OCF connector provider for the jdbc connector.
 */
public class JdbcConnectorProvider extends ConnectorProviderBase
{
    static final String connectorTypeGUID = "64463b01-92f6-4d7b-9737-f1d20b2654f4";
    static final String connectorQualifiedName = "Egeria::RelationalDbConnectors::Jdbc";
    static final String connectorDisplayName = "Relational Database JDBC Connector";
    static final String connectorTypeDescription = "Connector supports reading of metadata from relational databases using exclusively the JDBC API";

    private static final String  assetTypeName = "Jdbc";

    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * registry store implementation.
     */
    public JdbcConnectorProvider() {
        super();
        super.setConnectorClassName(JdbcConnector.class.getName());

        ConnectorType connectorType = new ConnectorType();
        connectorType.setType(ConnectorType.getConnectorTypeType());
        connectorType.setGUID(connectorTypeGUID);
        connectorType.setQualifiedName(connectorQualifiedName);
        connectorType.setDisplayName(connectorDisplayName);
        connectorType.setDescription(connectorTypeDescription);
        connectorType.setSupportedAssetTypeName(assetTypeName);
        connectorType.setConnectorProviderClassName(this.getClass().getName());

        connectorInterfaces.add(JdbcMetadata.class.getName());
        connectorType.setConnectorInterfaces(connectorInterfaces);

        super.connectorTypeBean = connectorType;
    }
}
