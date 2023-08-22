/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.discoveryservices;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.frameworks.discovery.DiscoveryServiceProvider;

/**
 * DuplicateSuspectDiscoveryProvider is the provider for the DuplicateSuspectDiscoveryService - an ODF discovery service connector.
 */
public class DuplicateSuspectDiscoveryProvider extends DiscoveryServiceProvider
{
    static final String  connectorTypeGUID = "721dd52e-bf25-4841-badb-aa3b6b4fa6ea";
    static final String  connectorTypeName = "Duplicate Suspect Discovery Service Connector";
    static final String  connectorTypeDescription = "Connector supports the detection of potentially duplicate assets.";

    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * discovery service implementation.
     */
    public DuplicateSuspectDiscoveryProvider()
    {
        Class<?> connectorClass = DuplicateSuspectDiscoveryService.class;

        super.setConnectorClassName(connectorClass.getName());

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
