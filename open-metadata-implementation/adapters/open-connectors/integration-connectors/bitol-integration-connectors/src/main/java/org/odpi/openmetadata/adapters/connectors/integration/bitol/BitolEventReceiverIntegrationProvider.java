/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.bitol;

import org.odpi.openmetadata.adapters.connectors.EgeriaOpenConnectorDefinition;
import org.odpi.openmetadata.frameworks.integration.connectors.IntegrationConnectorProvider;
import org.odpi.openmetadata.frameworks.integration.controls.CatalogTargetType;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.ArrayList;

/**
 * BitolEventReceiverIntegrationProvider is the connector provider for the BitolEventReceiverIntegrationConnector.
 */
public class BitolEventReceiverIntegrationProvider extends IntegrationConnectorProvider
{
    /**
     * The name of the catalog target that contains the topic to monitor.
     */
    public static final String CATALOG_TARGET_NAME = "topicToMonitor";

    private static final String connectorClassName = "org.odpi.openmetadata.adapters.connectors.integration.bitol.BitolEventReceiverIntegrationConnector";


    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * store implementation.
     */
    public BitolEventReceiverIntegrationProvider()
    {
        super(EgeriaOpenConnectorDefinition.BITOL_EVENT_RECEIVER_INTEGRATION_CONNECTOR,
              connectorClassName,
              null);

        CatalogTargetType catalogTargetType = new CatalogTargetType();

        catalogTargetType.setName(CATALOG_TARGET_NAME);
        catalogTargetType.setTypeName(OpenMetadataType.TOPIC.typeName);

        super.catalogTargets = new ArrayList<>();
        super.catalogTargets.add(catalogTargetType);
    }
}
