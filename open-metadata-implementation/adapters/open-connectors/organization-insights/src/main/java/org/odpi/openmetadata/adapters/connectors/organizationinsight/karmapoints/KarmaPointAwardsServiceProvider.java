/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.organizationinsight.karmapoints;


import org.odpi.openmetadata.frameworks.auditlog.AuditLogReportingComponent;
import org.odpi.openmetadata.frameworks.auditlog.ComponentDevelopmentStatus;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.frameworks.openwatchdog.WatchdogActionServiceProvider;
import org.odpi.openmetadata.frameworks.openwatchdog.controls.WatchdogActionGuard;
import org.odpi.openmetadata.frameworks.openwatchdog.controls.WatchdogActionTarget;


/**
 * KarmaPointAwardsServiceProvider is the OCF connector provider for the KarmaPointAwardsService.
 * This is a WatchDog Action Service.
 */
public class KarmaPointAwardsServiceProvider extends WatchdogActionServiceProvider
{
    /*
     * Unique identifier of the connector for the audit log.
     */
    private static final int    connectorComponentId       = 714;
    private static final String connectorTypeGUID          = "1154ed0b-6cc0-4576-937f-6c13689ed375";
    private static final String connectorTypeQualifiedName = "Egeria:GovernanceService:Watchdog:KarmaPointAwardsService";
    private static final String connectorTypeDisplayName   = "Karma Point Awards Service";
    private static final String connectorTypeDescription   = "A Watchdog Action Service that detects changes to elements, identifies the user who is performing the update and awards them a karma point for their contribution.";
    private static final String connectorWikiPage          = "https://egeria-project.org/connectors/watchdog/karma-point-awards-service/";

    /*
     * This is the name of the connector that this provider will create
     */
    private static final String connectorClassName = KarmaPointAwardsService.class.getName();


    public KarmaPointAwardsServiceProvider()
    {
        super();
        super.setConnectorClassName(connectorClassName);

        supportedRequestTypes = null;
        supportedRequestParameters = null;
        supportedActionTargetTypes = WatchdogActionTarget.getNotificationActionTargetTypes();
        producedGuards = WatchdogActionGuard.getSimpleWatchdogGuardTypes();

        super.setConnectorClassName(connectorClassName);

        ConnectorType connectorType = new ConnectorType();
        connectorType.setGUID(connectorTypeGUID);
        connectorType.setQualifiedName(connectorTypeQualifiedName);
        connectorType.setDisplayName(connectorTypeDisplayName);
        connectorType.setDescription(connectorTypeDescription);
        connectorType.setConnectorProviderClassName(this.getClass().getName());

        connectorType.setSupportedAssetTypeName(supportedAssetTypeName);
        connectorType.setSupportedDeployedImplementationType(supportedDeployedImplementationType);

        super.connectorTypeBean = connectorType;

        /*
         * Set up the component description used in the connector's audit log messages.
         */
        AuditLogReportingComponent componentDescription = new AuditLogReportingComponent();

        componentDescription.setComponentId(connectorComponentId);
        componentDescription.setComponentDevelopmentStatus(ComponentDevelopmentStatus.STABLE);
        componentDescription.setComponentName(connectorTypeDisplayName);
        componentDescription.setComponentDescription(connectorTypeDescription);
        componentDescription.setComponentWikiURL(connectorWikiPage);

        super.setConnectorComponentDescription(componentDescription);
    }
}
