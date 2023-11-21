/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.discoveryservices;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.frameworks.discovery.DiscoveryServiceProvider;

/**
 * SequentialDiscoveryPipelineProvider is the provider for the SequentialDiscoveryPipeline - an ODF discovery pipeline connector.
 */
public class SequentialDiscoveryPipelineProvider extends DiscoveryServiceProvider
{
    static final String  connectorTypeGUID = "c535fb7c-ac7c-40bb-bcf5-c7465c56c8d7";
    static final String  connectorTypeName = "Sequential Discovery Pipeline Connector";
    static final String  connectorTypeDescription = "Connector supports the sequential execution of discovery services.";

    static final String openPipelineServiceAssetTypeName = "OpenDiscoveryPipeline";

    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * discovery service implementation.
     */
    public SequentialDiscoveryPipelineProvider()
    {
        Class<?> connectorClass = SequentialDiscoveryPipeline.class;

        super.setConnectorClassName(connectorClass.getName());

        ConnectorType connectorType = new ConnectorType();
        connectorType.setType(ConnectorType.getConnectorTypeType());
        connectorType.setGUID(connectorTypeGUID);
        connectorType.setQualifiedName(connectorTypeName);
        connectorType.setDisplayName(connectorTypeName);
        connectorType.setDescription(connectorTypeDescription);
        connectorType.setConnectorProviderClassName(this.getClass().getName());
        connectorType.setSupportedAssetTypeName(openPipelineServiceAssetTypeName);

        super.connectorTypeBean = connectorType;
    }
}
