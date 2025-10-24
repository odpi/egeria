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
 * PeriodicRefreshNotificationServiceProvider is the OCF connector provider for the PeriodicRefreshNotificationService.
 * This is a WatchDog Action Service.
 */
public class PeriodicRefreshNotificationServiceProvider extends WatchdogActionServiceProvider
{
    /*
     * Unique identifier of the connector for the audit log.
     */
    private static final int    connectorComponentId       = 710;
    private static final String connectorTypeGUID          = "fcce903b-8e60-4d1e-969c-2802e0195b34";
    private static final String connectorTypeQualifiedName = "Egeria:GovernanceService:Watchdog:PeriodicRefreshNotificationService";
    private static final String connectorTypeDisplayName   = "Open Metadata Watchdog Service";
    private static final String connectorTypeDescription   = "A Watchdog Action Service that notifies all subscribers on a regular periodic basis.  The default is daily.";
    private static final String connectorWikiPage          = "https://egeria-project.org/connectors/watchdog/open-metadata-notification-service/";

    /*
     * This is the name of the connector that this provider will create
     */
    private static final String connectorClassName = PeriodicRefreshNotificationService.class.getName();


    public PeriodicRefreshNotificationServiceProvider()
    {
        super();
        super.setConnectorClassName(connectorClassName);

        supportedRequestTypes = null;
        supportedRequestParameters = PeriodicRefreshRequestParameter.getRequestParameterTypes();
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
