/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.governanceactions.notifications;


import org.odpi.openmetadata.adapters.connectors.governanceactions.ffdc.GovernanceActionConnectorsAuditCode;
import org.odpi.openmetadata.adapters.connectors.governanceactions.ffdc.GovernanceActionConnectorsErrorCode;
import org.odpi.openmetadata.adapters.connectors.governanceactions.watchdog.GenericWatchdogGuard;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.MessageDefinition;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.opengovernance.controls.ActionTarget;
import org.odpi.openmetadata.frameworks.opengovernance.ffdc.GovernanceServiceException;
import org.odpi.openmetadata.frameworks.openmetadata.events.OpenMetadataEventType;
import org.odpi.openmetadata.frameworks.openmetadata.events.OpenMetadataOutTopicEvent;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementHeader;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.AnchorsProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.NewActionTarget;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataTypeDefCategory;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.NotificationTypeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworks.openwatchdog.GenericWatchdogActionListener;
import org.odpi.openmetadata.frameworks.openwatchdog.WatchdogActionServiceConnector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * OpenMetadataNotificationService is a Watchdog Governance Action Service that listens for new or refreshed Assets
 * and kicks off a governance action process to validate that this Asset is correctly set up.
 * It is designed to run continuously and so does not set up completion status or guards.  This means its
 * Governance Action entity is never completed and this service is restarted each time the hosting engine is restarted.
 */
public class OpenMetadataNotificationService extends WatchdogActionServiceConnector
{
    volatile boolean completed = false;

    private final GenericWatchdogActionListener listener             = new GenericWatchdogActionListener(this);
    private final MonitoredElements             monitoredResources   = new MonitoredElements();
    private final MonitoredElements monitoredSubscribers = new MonitoredElements();
    private       String            notificationTypeName = "<unknown>";


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
            monitoredResources.setMonitoredElements(watchdogContext.getMonitoredResources());
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
             * Will need to filter manually in the listener
             */
            watchdogContext.registerListener(listener,
                                             null,
                                             null,
                                             null);
        }
        catch (Exception error)
        {
            try
            {
                List<String> outputGuards = new ArrayList<>();

                outputGuards.add(GenericWatchdogGuard.MONITORING_FAILED.getName());

                AuditLogMessageDefinition completionMessage = GovernanceActionConnectorsAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(watchdogActionServiceName,
                                                                                                                                            error.getClass().getName(),
                                                                                                                                            methodName,
                                                                                                                                            error.getMessage());
                auditLog.logException(methodName, completionMessage, error);

                watchdogContext.recordCompletionStatus(GenericWatchdogGuard.MONITORING_FAILED.getCompletionStatus(),
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
     * This method is called each time a metadata change is received.  It triggers a governance action process to validate and
     * enrich the asset as required.
     *
     * @param event event containing details of a change to an open metadata element.
     *
     * @throws GovernanceServiceException reports that the event can not be processed (this is logged but
     *                                    no other action is taken).  The listener will continue to be
     *                                    called until the watchdog governance action service declares it is complete
     *                                    or administrator action shuts down the service.
     */
    public void processEvent(OpenMetadataOutTopicEvent event) throws GovernanceServiceException
    {
        final String methodName = "processEvent";

        if (! completed)
        {
            try
            {
                if (event.getElementHeader().getType().getTypeCategory() == OpenMetadataTypeDefCategory.ENTITY_DEF)
                {
                    if (monitoredResources.isMonitored(event.getElementHeader()))
                    {
                        MessageDefinition notificationDescription = OpenMetadataNotificationMessageSet.MONITORED_RESOURCE_CHANGED.getMessageDefinition(event.getElementHeader().getType().getTypeName(),
                                                                                                                                                       propertyHelper.getStringProperty(watchdogActionServiceName,
                                                                                                                                                                                        OpenMetadataProperty.DISPLAY_NAME.name,
                                                                                                                                                                                        event.getElementProperties(),
                                                                                                                                                                                        methodName),
                                                                                                                                                       event.getElementHeader().getGUID(),
                                                                                                                                                       notificationTypeName,
                                                                                                                                                       watchdogContext.getNotificationTypeGUID());

                        List<NewActionTarget> newActionTargets = new ArrayList<>();
                        NewActionTarget newActionTarget = new NewActionTarget();

                        newActionTarget.setActionTargetGUID(event.getElementHeader().getGUID());
                        newActionTarget.setActionTargetName(ActionTarget.CHANGED_ELEMENT.name);

                        newActionTargets.add(newActionTarget);

                        watchdogContext.notifySubscribers(watchdogContext.getNotificationProperties(notificationDescription),
                                                          watchdogContext.getRequestParameters(),
                                                          newActionTargets);
                    }
                }
                else if (event.getElementHeader().getType().getTypeCategory() == OpenMetadataTypeDefCategory.RELATIONSHIP_DEF)
                        // Relationship event - check monitored resources and subscribers
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
                    else if ((propertyHelper.isTypeOf(event.getElementHeader(), OpenMetadataType.MONITORED_RESOURCE_RELATIONSHIP.typeName)) &&
                            (event.getEndOneElementHeader().getGUID().equals(watchdogContext.getNotificationTypeGUID())))
                    {
                        if (event.getEventType() == OpenMetadataEventType.NEW_ELEMENT_CREATED)
                        {
                            monitoredResources.addMonitoredElement(event.getEndTwoElementHeader());
                        }
                        else if (event.getEventType() == OpenMetadataEventType.ELEMENT_DELETED)
                        {
                            monitoredResources.removeMonitoredElement(event.getEndTwoElementHeader());
                        }
                    }
                }
            }
            catch (Exception error)
            {
                try
                {
                    List<String> outputGuards = new ArrayList<>();
                    outputGuards.add(GenericWatchdogGuard.MONITORING_FAILED.getName());

                    AuditLogMessageDefinition completionMessage = GovernanceActionConnectorsAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(watchdogActionServiceName,
                                                                                                                                                error.getClass().getName(),
                                                                                                                                                methodName,
                                                                                                                                                error.getMessage());
                    auditLog.logException(methodName, completionMessage, error);

                    watchdogContext.recordCompletionStatus(GenericWatchdogGuard.MONITORING_FAILED.getCompletionStatus(),
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

            if (completed)
            {
                try
                {
                    List<String> outputGuards = new ArrayList<>();
                    outputGuards.add(GenericWatchdogGuard.MONITORING_STOPPED.getName());

                    AuditLogMessageDefinition completionMessage = GovernanceActionConnectorsAuditCode.SERVICE_COMPLETED_SUCCESSFULLY.getMessageDefinition(watchdogActionServiceName);
                    auditLog.logMessage(methodName, completionMessage);

                    watchdogContext.recordCompletionStatus(GenericWatchdogGuard.MONITORING_STOPPED.getCompletionStatus(),
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


    /**
     * Manage the list of monitored elements and support queries.
     */
    static class MonitoredElements
    {
        private final Map<String, ElementHeader> monitoredElements = new HashMap<>();

        /**
         * Set up the monitored elements.
         *
         * @param initialResources list of elements for the notification type.
         */
        public synchronized void setMonitoredElements(List<OpenMetadataRootElement> initialResources)
        {
            monitoredElements.clear();

            if (initialResources != null)
            {
                for (OpenMetadataRootElement initialResource : initialResources)
                {
                    if (initialResource != null)
                    {
                        this.addMonitoredElement(initialResource.getElementHeader());
                    }
                }
            }
        }


        /**
         * Add a new element to the map.
         *
         * @param monitoredElement new resource
         */
        public synchronized void addMonitoredElement(ElementHeader monitoredElement)
        {
            if (monitoredElement != null)
            {
                monitoredElements.put(monitoredElement.getGUID(), monitoredElement);
            }
        }


        /**
         * Remove a element from the map.
         *
         * @param monitoredElement old resource
         */
        public synchronized void removeMonitoredElement(ElementHeader monitoredElement)
        {
            if (monitoredElement != null)
            {
                monitoredElements.remove(monitoredElement.getGUID());
            }
        }


        /**
         * Is this one of the monitored elements?
         *
         * @param potentialElement resource header
         * @return boolean - true for monitored element
         */
        public synchronized boolean isMonitored(ElementHeader potentialElement)
        {
            if (potentialElement != null)
            {
                if (monitoredElements.containsKey(potentialElement.getGUID()))
                {
                    return true;
                }

                if ((potentialElement.getAnchor() != null) &&
                        (potentialElement.getAnchor().getClassificationProperties() instanceof AnchorsProperties anchorsProperties) &&
                        (anchorsProperties.getAnchorGUID() != null))
                {
                    return monitoredElements.containsKey(anchorsProperties.getAnchorGUID());
                }
            }

            return false;
        }
    }
}
