/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.smartcollections;

import org.odpi.openmetadata.adapters.connectors.EgeriaOpenConnectorDefinition;
import org.odpi.openmetadata.adapters.connectors.integration.smartcollections.controls.SmartCollectionsCatalogTarget;
import org.odpi.openmetadata.frameworks.integration.connectors.IntegrationConnectorProvider;

/**
 * SmartCollectionsIntegrationConnectorProvider is the connector provider for the Smart Collections Integration Connector that
 * maintains the membership of results set collections based on their saved query.
 */
public class SmartCollectionsIntegrationConnectorProvider extends IntegrationConnectorProvider
{
    /**
     * Class of the connector.
     */
    private static final String connectorClassName = "org.odpi.openmetadata.adapters.connectors.integration.smartcollections.SmartCollectionsIntegrationConnector";


    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * connector implementation.
     */
    public SmartCollectionsIntegrationConnectorProvider()
    {
        super(EgeriaOpenConnectorDefinition.SMART_COLLECTIONS_INTEGRATION_CONNECTOR,
              connectorClassName,
              null);

        super.catalogTargets = SmartCollectionsCatalogTarget.getCatalogTargetTypes();
    }
}
