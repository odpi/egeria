/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetconsumer.connectors.outtopic;

import org.odpi.openmetadata.frameworks.connectors.ConnectorProviderBase;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;

/**
 * The AssetConsumerOutTopicClientProvider provides a base class for the connector provider supporting
 * AssetConsumerOutTopicClientConnector Connectors.
 *
 * It extends ConnectorProviderBase which does the creation of connector instances.  The subclasses of
 * AssetConsumerOutTopicClientProvider must initialize ConnectorProviderBase with the Java class
 * name of their Connector implementation (by calling super.setConnectorClassName(className)).
 * Then the connector provider will work.
 */
public class AssetConsumerOutTopicClientProvider extends ConnectorProviderBase
{
    static final String  connectorTypeGUID = "32e1812b-1c10-4e44-b75e-cd3d3930a8b1";
    static final String  connectorTypeName = "Asset Consumer Out Topic Client Connector";
    static final String  connectorTypeDescription = "Connector supports the receipt of events on the Asset Consumer OMAS Out Topic.";

    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * discovery service implementation.
     */
    public AssetConsumerOutTopicClientProvider()
    {
        Class<?> connectorClass = AssetConsumerOutTopicClientConnector.class;

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