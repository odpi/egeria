/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetcatalog.connectors.outtopic;

import org.odpi.openmetadata.frameworks.connectors.ConnectorProviderBase;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;

/**
 * The AssetCatalogOutTopicClientProvider provides a base class for the connector provider supporting
 * AssetCatalogOutTopicClientConnector Connectors.
 * <p>
 * It extends ConnectorProviderBase which does the creation of connector instances.  The subclasses of
 * AssetCatalogOutTopicClientProvider must initialize ConnectorProviderBase with the Java class
 * name of their Connector implementation (by calling super.setConnectorClassName(className)).
 * Then the connector provider will work.
 */
public class AssetCatalogOutTopicClientProvider extends ConnectorProviderBase {
    static final String connectorTypeGUID = "785fdb3d-34b7-47e0-bc73-5969610331d4";
    static final String connectorTypeName = "Asset Catalog Out Topic Client Connector";
    static final String connectorTypeDescription = "Connector supports the receipt of events on the Asset Catalog OMAS Out Topic.";

    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * discovery service implementation.
     */
    public AssetCatalogOutTopicClientProvider() {
        Class<?> connectorClass = AssetCatalogOutTopicClientConnector.class;

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
