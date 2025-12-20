/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.governanceactions.notifications;


import org.odpi.openmetadata.adapters.connectors.governanceactions.ffdc.GovernanceActionConnectorsAuditCode;
import org.odpi.openmetadata.adapters.connectors.governanceactions.ffdc.GovernanceActionConnectorsErrorCode;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.MessageDefinition;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.opengovernance.ffdc.GovernanceServiceException;
import org.odpi.openmetadata.frameworks.openmetadata.events.OpenMetadataEventType;
import org.odpi.openmetadata.frameworks.openmetadata.events.OpenMetadataOutTopicEvent;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataTypeDefCategory;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.NotificationTypeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworks.openwatchdog.GenericWatchdogActionListener;
import org.odpi.openmetadata.frameworks.openwatchdog.WatchdogActionServiceConnector;
import org.odpi.openmetadata.frameworks.openwatchdog.controls.WatchdogActionGuard;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


/**
 * PeriodicRefreshNotificationService is a Watchdog Governance Action Service that periodically calls the
 * subscribers.  It listens for changes to its subscribers and send welcome and cancellation notifications.
 * It is designed to run continuously and so does not set up completion status or guards.  This means its
 * Governance Action entity is never completed and this service is restarted each time the hosting engine is restarted.
 */
public class PeriodicRefreshNotificationService extends WatchdogActionServiceConnector
{
    volatile boolean completed = false;

    private final GenericWatchdogActionListener listener             = new GenericWatchdogActionListener(this);
    private final MonitoredElements             monitoredSubscribers = new MonitoredElements();
    private       String                        notificationTypeName = "<unknown>";


    /**
     * Indicates that the governance action service is completely configured and can begin processing.
     * This is a standard method from the Open Connector Framework (OCF) so
     * be sure to call super.start() at the start of your overriding version.
     *
     * @throws ConnectorCheckedException there is a problem within the governance action service.
     * @throws UserNotAuthorizedException the connector was disconnected before/during start
     */
    @Override
    public void start() throws ConnectorCheckedException, UserNotAuthorizedException
    {
        super.start();

        final String methodName = "start";

        try
        {
            monitoredSubscribers.setMonitoredElements(watchdogContext.getNotificationSubscribers());

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

            /*
             * Will need to filter manually in the listener - only interested in changes to the subscribers
             */
            watchdogContext.registerListener(listener,
                                             null,
                                             null,
                                             null);

            int refreshPeriod = watchdogContext.getIntRequestParameter(PeriodicRefreshRequestParameter.REFRESH_INTERVAL.getName(), 86400000); // 1 day;

            Timer timer = new Timer();
            TimerTask task = new TimerTask()
            {
                @Override
                public void run()
                {
                    try
                    {
                        MessageDefinition notificationDescription = OpenMetadataNotificationMessageSet.PERIODIC_NOTIFICATION.getMessageDefinition(notificationTypeName,
                                                                                                                                                  watchdogContext.getNotificationTypeGUID(),
                                                                                                                                                  Integer.toString(refreshPeriod));

                        watchdogContext.notifySubscribers(watchdogContext.getNotificationProperties(notificationDescription),
                                                          watchdogContext.getRequestParameters(),
                                                          null,
                                                          null);
                    }
                    catch (Exception cancelled)
                    {
                        completed = true;
                        timer.cancel();

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
                    }
                }
            };

            timer.schedule(task, 0, refreshPeriod); // Schedule the task to run every refresh period
        }
        catch (Exception error)
        {
            completed = true;

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
                                      GovernanceActionConnectorsAuditCode.UNABLE_TO_REGISTER_LISTENER.getMessageDefinition(watchdogActionServiceName,
                                                                                                                           error.getClass().getName(),
                                                                                                                           error.getMessage()),
                                      error);
            }

            throw new GovernanceServiceException(GovernanceActionConnectorsErrorCode.UNABLE_TO_REGISTER_LISTENER.getMessageDefinition(watchdogActionServiceName,
                                                                                                                                      error.getClass().getName(),
                                                                                                                                      error.getMessage()),
                                                 error.getClass().getName(),
                                                 methodName,
                                                 error);
        }
    }


    /**
     * This method is called each time a metadata change is received.  If the element is one of the
     * monitored resources, it notifies the subscribers.
     *
     * @param event event containing details of a change to an open metadata element.
     */
    public void processEvent(OpenMetadataOutTopicEvent event)
    {
        final String methodName = "processEvent";

        if (! completed)
        {
            try
            {
                if (event.getElementHeader().getType().getTypeCategory() == OpenMetadataTypeDefCategory.RELATIONSHIP_DEF)
                        // Relationship event - check subscribers
                {
                    if ((propertyHelper.isTypeOf(event.getElementHeader(), OpenMetadataType.NOTIFICATION_SUBSCRIBER_RELATIONSHIP.typeName)) &&
                            (event.getEndOneElementHeader().getGUID().equals(watchdogContext.getNotificationTypeGUID())))
                    {
                        if (event.getEventType() == OpenMetadataEventType.NEW_ELEMENT_CREATED)
                        {
                            monitoredSubscribers.addMonitoredElement(event.getEndTwoElementHeader());

                            MessageDefinition notificationDescription = OpenMetadataNotificationMessageSet.NEW_SUBSCRIBER.getMessageDefinition(notificationTypeName,
                                                                                                                                               watchdogContext.getNotificationTypeGUID());
                            watchdogContext.notifySubscriber(event.getEndTwoElementHeader().getGUID(),
                                                             watchdogContext.getNotificationProperties(notificationDescription),
                                                             watchdogContext.getRequestParameters(),
                                                             null);
                        }
                        else if (event.getEventType() == OpenMetadataEventType.ELEMENT_DELETED)
                        {
                            monitoredSubscribers.removeMonitoredElement(event.getEndTwoElementHeader());

                            MessageDefinition notificationDescription = OpenMetadataNotificationMessageSet.CANCELLED_SUBSCRIBER.getMessageDefinition(notificationTypeName,
                                                                                                                                                     watchdogContext.getNotificationTypeGUID());
                            watchdogContext.notifySubscriber(event.getEndTwoElementHeader().getGUID(),
                                                             watchdogContext.getNotificationProperties(notificationDescription),
                                                             watchdogContext.getRequestParameters(),
                                                             null);
                        }
                    }
                }
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
            }
        }
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
     * @throws ConnectorCheckedException there is a problem within the governance action service.
     */
    @Override
    public  void disconnect() throws ConnectorCheckedException
    {
        watchdogContext.disconnectListener();

        super.disconnect();
    }
}
