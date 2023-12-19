/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.discoveryservices.discovercsv;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.frameworks.discovery.DiscoveryServiceProvider;

/**
 * CSVDiscoveryServiceProvider provides the connector provider for the CSV Discovery Service
 */
public class CSVDiscoveryServiceProvider extends DiscoveryServiceProvider
{
    static final String  connectorTypeGUID = "d9748e7d-cc1b-476d-8162-b0fe7e5ffe83";
    static final String  connectorTypeName = "CSV Discovery Service Connector";
    static final String  connectorTypeDescription = "Connector supports the discovery of metadata about a CSV file.";

    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * OMRS Connector implementation.
     */
    public CSVDiscoveryServiceProvider()
    {
        super();

        String   connectorClass = "org.odpi.openmetadata.adapters.connectors.discoveryservices.discovercsv.CSVDiscoveryService";

        super.setConnectorClassName(connectorClass);

        ConnectorType connectorType = new ConnectorType();
        connectorType.setType(ConnectorType.getConnectorTypeType());
        connectorType.setGUID(connectorTypeGUID);
        connectorType.setQualifiedName(connectorTypeName);
        connectorType.setDisplayName(connectorTypeName);
        connectorType.setDescription(connectorTypeDescription);
        connectorType.setConnectorProviderClassName(this.getClass().getName());
        connectorType.setSupportedAssetTypeName(supportedAssetTypeName);

        super.connectorTypeBean = connectorType;
    }
}
