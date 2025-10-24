/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.governanceactions.notifications;


import org.odpi.openmetadata.adapters.connectors.governanceactions.watchdog.GenericWatchdogGuard;
import org.odpi.openmetadata.frameworks.auditlog.AuditLogReportingComponent;
import org.odpi.openmetadata.frameworks.auditlog.ComponentDevelopmentStatus;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.frameworks.openwatchdog.WatchdogActionServiceProvider;
import org.odpi.openmetadata.frameworks.openwatchdog.controls.WatchdogActionTarget;


/**
 * MonitoredResourceNotificationServiceProvider is the OCF connector provider for the MonitoredResourceNotificationService.
 * This is a WatchDog Action Service.
 */
public class MonitoredResourceNotificationServiceProvider extends WatchdogActionServiceProvider
{
    /*
     * Unique identifier of the connector for the audit log.
     */
    private static final int    connectorComponentId       = 704;
    private static final String connectorTypeGUID          = "2d69894c-ebbf-4f9b-b57a-cc2ac05fdc29";
    private static final String connectorTypeQualifiedName = "Egeria:GovernanceService:Watchdog:MonitoredResourceNotificationService";
    private static final String connectorTypeDisplayName   = "Open Metadata Watchdog Service";
    private static final String connectorTypeDescription   = "A Watchdog Action Service that detects changes to elements linked to a notification type.  When changed occur, all subscribers to the notification type are informed.";
    private static final String connectorWikiPage          = "https://egeria-project.org/connectors/watchdog/open-metadata-notification-service/";

    /*
     * This is the name of the connector that this provider will create
     */
    private static final String connectorClassName = MonitoredResourceNotificationService.class.getName();


    public MonitoredResourceNotificationServiceProvider()
    {
        super();
        super.setConnectorClassName(connectorClassName);

        supportedRequestTypes = null;
        supportedRequestParameters = null;
        supportedActionTargetTypes = WatchdogActionTarget.getNotificationActionTargetTypes();
        producedGuards = GenericWatchdogGuard.getGuardTypes();

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
