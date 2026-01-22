/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.baudot;


import org.odpi.openmetadata.adapters.connectors.EgeriaOpenConnectorDefinition;
import org.odpi.openmetadata.adapters.connectors.governanceactions.watchdog.GenericWatchdogGuard;
import org.odpi.openmetadata.frameworks.openwatchdog.WatchdogActionServiceProvider;
import org.odpi.openmetadata.frameworks.openwatchdog.controls.WatchdogActionTarget;


/**
 * BaudotSubscriptionManagementProvider is the OCF connector provider for the Baudot Open Metadata Digital Product Subscription Manager.
 * This is a WatchDog Action Service.
 */
public class BaudotSubscriptionManagementProvider extends WatchdogActionServiceProvider
{
    /*
     * This is the name of the connector that this provider will create
     */
    private static final String connectorClassName = BaudotSubscriptionManagementService.class.getName();

    /**
     * Constructor used to initialize the ConnectorProvider with the Java class name of the specific
     * store implementation.
     */
    public BaudotSubscriptionManagementProvider()
    {
        super(EgeriaOpenConnectorDefinition.BAUDOT_SUBSCRIPTION_MANAGER,
              connectorClassName,
              null);

        supportedRequestTypes = null;
        supportedRequestParameters = null;
        supportedActionTargetTypes = WatchdogActionTarget.getNotificationActionTargetTypes();
        producedGuards = GenericWatchdogGuard.getGuardTypes();
    }
}
