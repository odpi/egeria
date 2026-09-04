/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.baudot;


import org.odpi.openmetadata.adapters.connectors.EgeriaOpenConnectorDefinition;
import org.odpi.openmetadata.adapters.connectors.baudot.controls.BaudotCatalogTarget;
import org.odpi.openmetadata.frameworks.integration.connectors.IntegrationConnectorProvider;


/**
 * BaudotSubscriptionManagementProvider is the OCF connector provider for the Baudot Open Metadata Digital
 * Product Subscription Manager.  This is a dynamic integration connector whose catalog targets are the
 * notification types whose subscribers it notifies.
 */
public class BaudotSubscriptionManagementProvider extends IntegrationConnectorProvider
{
    /*
     * This is the name of the connector that this provider will create
     */
    private static final String connectorClassName = BaudotSubscriptionManagementConnector.class.getName();

    /**
     * Constructor used to initialize the ConnectorProvider with the Java class name of the specific
     * store implementation.
     */
    public BaudotSubscriptionManagementProvider()
    {
        super(EgeriaOpenConnectorDefinition.BAUDOT_SUBSCRIPTION_MANAGER,
              connectorClassName,
              null);

        super.catalogTargets = BaudotCatalogTarget.getCatalogTargetTypes();
    }
}
