/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.dcat;

import org.odpi.openmetadata.adapters.connectors.EgeriaOpenConnectorDefinition;
import org.odpi.openmetadata.frameworks.integration.connectors.IntegrationConnectorProvider;
import org.odpi.openmetadata.frameworks.integration.controls.CatalogTargetType;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.ArrayList;

/**
 * DcatEventReceiverIntegrationProvider is the connector provider for the DcatEventReceiverIntegrationConnector.
 */
public class DcatEventReceiverIntegrationProvider extends IntegrationConnectorProvider
{
    /**
     * The name of the catalog target that contains the topic to monitor.
     */
    public static final String CATALOG_TARGET_NAME = "topicToMonitor";

    private static final String connectorClassName = "org.odpi.openmetadata.adapters.connectors.integration.dcat.DcatEventReceiverIntegrationConnector";


    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * store implementation.
     */
    public DcatEventReceiverIntegrationProvider()
    {
        super(EgeriaOpenConnectorDefinition.DCAT_EVENT_RECEIVER_INTEGRATION_CONNECTOR,
              connectorClassName,
              null);

        CatalogTargetType catalogTargetType = new CatalogTargetType();

        catalogTargetType.setName(CATALOG_TARGET_NAME);
        catalogTargetType.setTypeName(OpenMetadataType.TOPIC.typeName);

        super.catalogTargets = new ArrayList<>();
        super.catalogTargets.add(catalogTargetType);
    }
}
