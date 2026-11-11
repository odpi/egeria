/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.governanceactions.stewardship;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationType;
import org.odpi.openmetadata.frameworks.opensurvey.controls.SurveyActionTarget;
import org.odpi.openmetadata.frameworks.openwatchdog.WatchdogActionServiceProvider;

/**
 * WaitForStewardGovernanceActionProvider is the OCF connector provider for the "WaitForSteward"
 * Watchdog Action Service.  It runs in a watchdog action engine, such as EgeriaWatchdog, because it
 * listens for changes to the to dos that it is waiting for.
 */
public class WaitForStewardGovernanceActionProvider extends WatchdogActionServiceProvider
{
    private static final String  connectorTypeGUID = "18d2ee53-5f1c-440e-827d-e141cb7b53db";
    private static final String  connectorTypeQualifiedName = "Egeria:GovernanceActionService:Stewardship:WaitForSteward";
    private static final String  connectorTypeDisplayName = "Wait For Steward Watchdog Action Service";
    private static final String  connectorTypeDescription =
            """
                    A Watchdog Action Service that waits for stewards to finish the to dos supplied as its action targets.  A to do is any action target that is named stewardAction, as produced by the Evaluate Annotations Governance Action Service, or whose element is a ToDo, such as the newAsset action target produced by the Steward Informer Governance Action Service.

                    The service listens for updates to, and deletions of, these to dos.  A to do has finished when its activityStatus is set to COMPLETED, INVALID, IGNORED, FAILED, CANCELLED or ABANDONED, or when it is deleted.  The status of each to do is also checked when the service starts, so to dos that finished before it started, or while its engine was restarting, are not missed.

                    Once every to do has finished, the service completes with the to-dos-completed guard if they were all COMPLETED, and with the to-dos-not-completed guard otherwise.  If there are no to dos to wait for, it completes straight away with the no-to-dos guard.""";


    private static final String connectorClassName = WaitForStewardGovernanceActionConnector.class.getName();


    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * store implementation.
     */
    public WaitForStewardGovernanceActionProvider()
    {
        super();
        super.setConnectorClassName(connectorClassName);

        /*
         * The request parameters and produced action targets that the base provider sets up are for the generic
         * watchdogs.  This service has no request parameters, and it does not pass on any action targets.
         */
        supportedRequestParameters = null;
        supportedActionTargetTypes = SurveyActionTarget.getStewardshipHandoverActionTargetTypes();
        producedActionTargetTypes = null;
        producedGuards = WaitForStewardGuard.getGuardTypes();

        ConnectorType connectorType = new ConnectorType();
        connectorType.setGUID(connectorTypeGUID);
        connectorType.setQualifiedName(connectorTypeQualifiedName);
        connectorType.setDisplayName(connectorTypeDisplayName);
        connectorType.setDescription(connectorTypeDescription);
        connectorType.setConnectorProviderClassName(this.getClass().getName());
        connectorType.setSupportedAssetTypeName(DeployedImplementationType.WATCHDOG_ACTION_SERVICE_CONNECTOR.getAssociatedTypeName());
        connectorType.setSupportedDeployedImplementationType(DeployedImplementationType.WATCHDOG_ACTION_SERVICE_CONNECTOR.getDeployedImplementationType());

        super.connectorTypeBean = connectorType;
    }
}
