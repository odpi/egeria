/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.governanceactions.notifications;


import org.odpi.openmetadata.adapters.connectors.governanceactions.ffdc.GovernanceActionConnectorsAuditCode;
import org.odpi.openmetadata.adapters.connectors.governanceactions.ffdc.GovernanceActionConnectorsErrorCode;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.MessageDefinition;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.opengovernance.ffdc.GovernanceServiceException;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ActivityStatus;
import org.odpi.openmetadata.frameworks.openmetadata.events.OpenMetadataOutTopicEvent;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.NotificationTypeProperties;
import org.odpi.openmetadata.frameworks.openwatchdog.WatchdogActionServiceConnector;
import org.odpi.openmetadata.frameworks.openwatchdog.controls.WatchdogActionGuard;

import java.util.ArrayList;
import java.util.List;


/**
 * OneTimeNotificationService is a Watchdog Governance Action Service notifies its subscribers once and then completes.
 */
public class OneTimeNotificationService extends WatchdogActionServiceConnector
{

    /**
     * Indicates that the governance action service is completely configured and can begin processing.
     * This is a standard method from the Open Connector Framework (OCF) so
     * be sure to call super.start() at the start of your overriding version.
     *
     * @throws ConnectorCheckedException a problem within the governance action service.
     * @throws UserNotAuthorizedException the connector was disconnected before/during start
     */
    @Override
    public void start() throws ConnectorCheckedException, UserNotAuthorizedException
    {
        super.start();

        final String methodName = "start";

        try
        {
            String notificationTypeName = "<unknown>";

            OpenMetadataRootElement notificationType = watchdogContext.getNotificationType();

            if ((notificationType != null) && (notificationType.getProperties() instanceof NotificationTypeProperties notificationTypeProperties))
            {
                if (notificationTypeProperties.getDisplayName() != null)
                {
                    notificationTypeName = notificationTypeProperties.getDisplayName();
                }
                else if (notificationTypeProperties.getQualifiedName() != null)
                {
                    notificationTypeName = notificationTypeProperties.getQualifiedName();
                }
            }

            MessageDefinition notificationDescription = OpenMetadataNotificationMessageSet.ONE_TIME_NOTIFICATION.getMessageDefinition(notificationTypeName,
                                                                                                                                      watchdogContext.getNotificationTypeGUID());

            watchdogContext.notifySubscribers(null,
                                              watchdogContext.getNotificationProperties(notificationDescription),
                                              watchdogContext.getRequestParameters(),
                                              null,
                                              ActivityStatus.COMPLETED);
        }
        catch (Exception error)
        {
            try
            {
                List<String> outputGuards = new ArrayList<>();

                outputGuards.add(WatchdogActionGuard.MONITORING_FAILED.getName());

                AuditLogMessageDefinition completionMessage = GovernanceActionConnectorsAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(watchdogActionServiceName,
                                                                                                                                            error.getClass().getName(),
                                                                                                                                            methodName,
                                                                                                                                            error.getMessage());
                auditLog.logException(methodName, completionMessage, error);

                watchdogContext.recordCompletionStatus(WatchdogActionGuard.MONITORING_FAILED.getCompletionStatus(),
                                                       outputGuards,
                                                       null,
                                                       null,
                                                       completionMessage);
            }
            catch (Exception nestedError)
            {
                if (auditLog != null)
                {
                    auditLog.logException(methodName,
                                          GovernanceActionConnectorsAuditCode.UNABLE_TO_SET_COMPLETION_STATUS.getMessageDefinition(watchdogActionServiceName,
                                                                                                                                   nestedError.getClass().getName(),
                                                                                                                                   nestedError.getMessage()),
                                          nestedError);
                }
            }

            if (auditLog != null)
            {
                auditLog.logException(methodName,
                                      GovernanceActionConnectorsAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(watchdogActionServiceName,
                                                                                                                    error.getClass().getName(),
                                                                                                                    methodName,
                                                                                                                    error.getMessage()),
                                      error);
            }

            throw new GovernanceServiceException(GovernanceActionConnectorsErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(watchdogActionServiceName,
                                                                                                                                      error.getClass().getName(),
                                                                                                                                      methodName,
                                                                                                                                      error.getMessage()),
                                                 error.getClass().getName(),
                                                 methodName,
                                                 error);
        }


        try
        {
            List<String> outputGuards = new ArrayList<>();
            outputGuards.add(WatchdogActionGuard.MONITORING_COMPLETED.getName());

            AuditLogMessageDefinition completionMessage = GovernanceActionConnectorsAuditCode.SERVICE_COMPLETED_SUCCESSFULLY.getMessageDefinition(watchdogActionServiceName);
            auditLog.logMessage(methodName, completionMessage);

            watchdogContext.recordCompletionStatus(WatchdogActionGuard.MONITORING_COMPLETED.getCompletionStatus(),
                                                   outputGuards,
                                                   null,
                                                   null,
                                                   completionMessage);
        }
        catch (Exception error)
        {
            if (auditLog != null)
            {
                auditLog.logException(methodName,
                                      GovernanceActionConnectorsAuditCode.UNABLE_TO_SET_COMPLETION_STATUS.getMessageDefinition(watchdogActionServiceName,
                                                                                                                               error.getClass().getName(),
                                                                                                                               error.getMessage()),
                                      error);
            }
        }
    }


    /**
     * This method is called each time a metadata change is received.  It triggers a governance action process to validate and
     * enrich the asset as required.
     *
     * @param event event containing details of a change to an open metadata element.
     */
    public void processEvent(OpenMetadataOutTopicEvent event)
    {
        // Nothing to do because this notification service does not work with events
    }


    /**
     * Disconnect is called either because this governance action service called governanceContext.recordCompletionStatus()
     * or the administrator requested this governance action service stop running or the hosting server is shutting down.
     * If disconnect completes before the governance action service records
     * its completion status then the governance action service is restarted either at the administrator's request or the next time the server starts.
     * If you do not want this governance action service restarted, be sure to record the completion status in disconnect().
     * The disconnect() method is a standard method from the Open Connector Framework (OCF).  If you need to override this method
     * be sure to call super.disconnect() in your version.
     *
     * @throws ConnectorCheckedException a problem within the governance action service.
     */
    @Override
    public  void disconnect() throws ConnectorCheckedException
    {
        watchdogContext.disconnectListener();

        super.disconnect();
    }
}
