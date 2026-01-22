/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.baudot;


import org.odpi.openmetadata.adapters.connectors.baudot.ffdc.BaudotAuditCode;
import org.odpi.openmetadata.adapters.connectors.governanceactions.ffdc.GovernanceActionConnectorsAuditCode;
import org.odpi.openmetadata.adapters.connectors.governanceactions.ffdc.GovernanceActionConnectorsErrorCode;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.MessageDefinition;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.opengovernance.controls.ActionTarget;
import org.odpi.openmetadata.frameworks.opengovernance.ffdc.GovernanceServiceException;
import org.odpi.openmetadata.frameworks.opengovernance.properties.ActionTargetElement;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ActivityStatus;
import org.odpi.openmetadata.frameworks.openmetadata.events.OpenMetadataEventType;
import org.odpi.openmetadata.frameworks.openmetadata.events.OpenMetadataOutTopicEvent;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementHeader;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedMetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.properties.AnchorsProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.NewActionTarget;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataTypeDefCategory;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.NotificationTypeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworks.openwatchdog.GenericWatchdogActionListener;
import org.odpi.openmetadata.frameworks.openwatchdog.WatchdogActionServiceConnector;
import org.odpi.openmetadata.frameworks.openwatchdog.controls.WatchdogActionGuard;

import java.util.*;


/**
 * MonitoredResourceNotificationService is a Watchdog Governance Action Service that listens for changes to its
 * monitored resources and notifies its subscribers when a change occurs.
 * It listens for changes to its subscribers and sends welcome and cancellation notifications.
 * It is designed to run continuously and so does not set up completion status or guards unless it fails.  This means its
 * Engine Action entity is never (rarely) completed and this service is restarted each time the hosting engine is restarted.
 */
public class BaudotSubscriptionManagementService extends WatchdogActionServiceConnector
{
    volatile boolean completed = false;

    private final GenericWatchdogActionListener listener             = new GenericWatchdogActionListener(this);
    private final NotificationTypes             notificationTypeMap = new NotificationTypes();
    private final MonitoredResources            monitoredResources   = new MonitoredResources();


    /**
     * Indicates that the watchdog action service is completely configured and can begin processing.
     * This is a standard method from the Open Connector Framework (OCF), so
     * be sure to call super.start() at the start of your overriding version.
     *
     * @throws ConnectorCheckedException a problem within the watchdog action service.
     * @throws UserNotAuthorizedException the connector was disconnected before/during start
     */
    @Override
    public void start() throws ConnectorCheckedException, UserNotAuthorizedException
    {
        super.start();

        final String methodName = "start";
        boolean listenerRegistered = false;

        try
        {
            while (! completed)
            {
                /*
                 * Set up the caches. Begin by extracting the notification types from the action targets.
                 * Once the caches are set up, send out first time (monitor resources), periodic and
                 * onte-time notifications.
                 */
                Date nextCacheRefresh = refreshCaches();

                /*
                 * Start listening once the caches have been refreshed.
                 */
                if (! listenerRegistered)
                {
                    /*
                     * Will need to filter manually in the listener
                     */
                    watchdogContext.registerListener(listener,
                                                     null,
                                                     null,
                                                     null);
                    listenerRegistered = true;
                }

                /*
                 * Wait for the next time the caches need to be refreshed.
                 */

                long sleepTime = nextCacheRefresh.getTime() - System.currentTimeMillis();

                while (sleepTime > 0)
                {
                    try
                    {
                        Thread.sleep(sleepTime);
                    }
                    catch (InterruptedException ignored)
                    {
                    }

                    sleepTime = nextCacheRefresh.getTime() - System.currentTimeMillis();
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
     * Refresh the caches for the notification types and notification subscribers.
     *
     * @return the next time that the caches should be refreshed.
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the watchdog action service is not authorized to continue
     * @throws PropertyServerException    a problem connecting to the metadata store
     */
    Date refreshCaches() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException
    {
        final String methodName = "refreshCaches";

        /*
         * The default next refresh is 1 hour from now.  This will be modified if a periodic notification
         * needs to be processed before that.
         */
        Date nextCacheRefresh = new Date(System.currentTimeMillis() + (60 * 60 * 1000));

        /*
         * Locate all the action targets that are notification types.
         */
        List<ActionTargetElement> notificationTypeTargetElements = watchdogContext.getNotificationTypesFromActionTargets();

        if ((notificationTypeTargetElements != null) && (! notificationTypeTargetElements.isEmpty()))
        {
            /*
             * Details of each notification type need to be organized in a map to allow easy look-up when
             * processing notifications
             */
            for (ActionTargetElement actionTargetElement : notificationTypeTargetElements)
            {
                if (actionTargetElement != null)
                {
                    /*
                     * A full root element is retrieved for the notification type since it has the details of
                     * the notification type properties in bean form, and the list of monitored resources.
                     */
                    OpenMetadataRootElement notificationTypeElement = watchdogContext.getNotificationType(actionTargetElement.getActionTargetGUID());

                    if ((notificationTypeElement != null) && (notificationTypeElement.getProperties() instanceof NotificationTypeProperties notificationTypeProperties))
                    {
                        /*
                         * The notification type has been successfully retrieved from open metadata.  It is
                         * added to the map.  True is returned if this is the first time the notification type
                         * has been added to the map.
                         */
                        boolean firstNotification = notificationTypeMap.setNotificationType(notificationTypeElement);

                        /*
                         * The notification type is now in the map. Also update any monitored resources.
                         * These resources are monitored by the event listener.
                         */
                        monitoredResources.setMonitoredResources(notificationTypeElement.getMonitoredResources(),
                                                                 notificationTypeElement.getElementHeader().getGUID());

                        /*
                         * The notification type's properties define the conditions for when notifications are
                         * sent to the subscribers and how many.  The next block of code determines which processing
                         * pattern to use.  This affects the notification properties sent and the status of the
                         * subscriber if the notification is sent.  If this is the first iteration for the notification
                         * type, then an audit log message is published to identify how the notification type is being
                         * interpreted.  This is to allow users to validate that they have set up the
                         * notification type correctly.
                         */
                        ActivityStatus    newSubscriberStatus = ActivityStatus.IN_PROGRESS;
                        MessageDefinition notificationDescription;

                        if (notificationTypeProperties.getMultipleNotificationsPermitted())
                        {
                            if (notificationTypeProperties.getNotificationInterval() == 0)
                            {
                                /*
                                 * A notification will only be sent from here for new subscribers - this is because
                                 * the notification handler call to notifySubscribers() requires the subscriber
                                 * to have lastNotification=null if it is not a periodic notification type.
                                 */
                                notificationDescription = BaudotNotificationMessageSet.NEW_SUBSCRIBER.getMessageDefinition(notificationTypeProperties.getDisplayName(), notificationTypeElement.getElementHeader().getGUID());

                                /*
                                 * Notifications for this notification type are based on the changing values in the
                                 * monitored resources.  A message is output for the notification type only on the first round.
                                 */
                                if (firstNotification)
                                {
                                    String size = "0";
                                    if (notificationTypeElement.getMonitoredResources() != null)
                                    {
                                        size = Integer.toString(notificationTypeElement.getMonitoredResources().size());
                                    }

                                    auditLog.logMessage(methodName,
                                                        BaudotAuditCode.MONITORED_RESOURCE_NOTIFICATION_TYPE.getMessageDefinition(watchdogActionServiceName,
                                                                                                                                  notificationTypeProperties.getDisplayName(),
                                                                                                                                  notificationTypeElement.getElementHeader().getGUID(),
                                                                                                                                  size));
                                }
                            }
                            else
                            {
                                /*
                                 * Notifications are sent periodically.
                                 */
                                notificationDescription = BaudotNotificationMessageSet.PERIODIC_NOTIFICATION.getMessageDefinition(notificationTypeProperties.getDisplayName(),
                                                                                                                                  notificationTypeElement.getElementHeader().getGUID(),
                                                                                                                                  Long.toString(notificationTypeProperties.getNotificationInterval()));

                                if (firstNotification)
                                {
                                    auditLog.logMessage(methodName,
                                                        BaudotAuditCode.PERIODIC_NOTIFICATION_TYPE.getMessageDefinition(watchdogActionServiceName,
                                                                                                                        notificationTypeProperties.getDisplayName(),
                                                                                                                        notificationTypeElement.getElementHeader().getGUID(),
                                                                                                                        Long.toString(notificationTypeProperties.getNotificationInterval()),
                                                                                                                        nextCacheRefresh.toString()));
                                }
                            }
                        }
                        else
                        {
                            newSubscriberStatus = ActivityStatus.COMPLETED;
                            notificationDescription = BaudotNotificationMessageSet.ONE_TIME_NOTIFICATION.getMessageDefinition(notificationTypeProperties.getDisplayName(),
                                                                                                                              notificationTypeElement.getElementHeader().getGUID());

                            if (firstNotification)
                            {
                                auditLog.logMessage(methodName,
                                                    BaudotAuditCode.ONE_TIME_NOTIFICATION_TYPE.getMessageDefinition(watchdogActionServiceName,
                                                                                                                    notificationTypeProperties.getDisplayName(),
                                                                                                                    notificationTypeElement.getElementHeader().getGUID()));
                            }
                        }

                        /*
                         * Notify subscribers for the notification type.
                         */
                        watchdogContext.notifySubscribers(notificationTypeElement.getElementHeader().getGUID(),
                                                          firstNotification,
                                                          null,
                                                          watchdogContext.getNotificationProperties(notificationDescription,
                                                                                                    notificationTypeElement.getElementHeader().getGUID(),
                                                                                                    notificationTypeMap.getNotificationType(notificationTypeElement.getElementHeader().getGUID()).getNotificationCount()),
                                                          watchdogContext.getRequestParameters(),
                                                          null,
                                                          notificationTypeProperties.getMinimumNotificationInterval(),
                                                          notificationTypeProperties.getNextScheduledNotification(),
                                                          newSubscriberStatus);

                        if (notificationTypeProperties.getNotificationInterval() != 0)
                        {
                            /*
                             * Notifications are sent periodically.  Set up the time for the next periodic notification
                             * for the notification type if it has expired.
                             */
                            Date nextNotificationTime = new Date(System.currentTimeMillis() + (notificationTypeProperties.getNotificationInterval() * 60 * 1000));
                            NotificationTypeProperties newNotificationTypeProperties = new NotificationTypeProperties();

                            newNotificationTypeProperties.setNextScheduledNotification(nextNotificationTime);

                            if ((notificationTypeProperties.getNextScheduledNotification() == null) ||
                                (notificationTypeProperties.getNextScheduledNotification().before(nextNotificationTime)))
                            {
                                watchdogContext.updateNotificationType(notificationTypeElement.getElementHeader().getGUID(), newNotificationTypeProperties);

                                /*
                                 * Adjust the time of the next cache refresh if this notification type needs to be
                                 * processed sooner.
                                 */
                                if (nextNotificationTime.before(nextCacheRefresh))
                                {
                                    nextCacheRefresh = nextNotificationTime;
                                }
                            }
                        }
                    }
                }
            }
        }

        return nextCacheRefresh;
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
                /*
                 * Only interested in entity events for monitored resources.  This includes changes to the classifications.
                 * One classification is significant - the LatestChange classification - which causes events on the
                 * monitored resource when any of its anchored elements change.
                 */
                if (event.getElementHeader().getType().getTypeCategory() == OpenMetadataTypeDefCategory.ENTITY_DEF)
                {
                    List<MonitoredResource> monitoredResourceRelationships = monitoredResources.isMonitored(event.getElementHeader());
                    if (monitoredResourceRelationships != null)
                    {
                        Date nextScheduledNotification = calculateNextNotificationTime(event);

                        for (MonitoredResource monitoredResourceRelationship : monitoredResourceRelationships)
                        {
                            if (monitoredResourceRelationship != null)
                            {
                                NotificationType notificationType = notificationTypeMap.getNotificationType(monitoredResourceRelationship.getNotificationTypeGUID());

                                MessageDefinition notificationDescription = BaudotNotificationMessageSet.MONITORED_RESOURCE_CHANGED.getMessageDefinition(event.getElementHeader().getType().getTypeName(),
                                                                                                                                                         propertyHelper.getStringProperty(watchdogActionServiceName,
                                                                                                                                                                                          OpenMetadataProperty.DISPLAY_NAME.name,
                                                                                                                                                                                          event.getElementProperties(),
                                                                                                                                                                                          methodName),
                                                                                                                                                         event.getElementHeader().getGUID(),
                                                                                                                                                         notificationType.getNotificationTypeName(),
                                                                                                                                                         notificationType.getNotificationTypeGUID());

                                List<NewActionTarget> newActionTargets = new ArrayList<>();
                                NewActionTarget       newActionTarget  = new NewActionTarget();

                                newActionTarget.setActionTargetGUID(event.getElementHeader().getGUID());
                                newActionTarget.setActionTargetName(ActionTarget.CHANGED_ELEMENT.name);

                                newActionTargets.add(newActionTarget);

                                watchdogContext.notifySubscribers(notificationType.getNotificationTypeGUID(),
                                                                  false,
                                                                  null,
                                                                  watchdogContext.getNotificationProperties(notificationDescription,
                                                                                                            notificationType.getNotificationTypeGUID(),
                                                                                                            notificationType.getNotificationCount()),
                                                                  watchdogContext.getRequestParameters(),
                                                                  newActionTargets,
                                                                  notificationType.getMinimumNotificationInterval(),
                                                                  nextScheduledNotification,
                                                                  null);
                            }
                        }
                    }
                }
                else if (event.getElementHeader().getType().getTypeCategory() == OpenMetadataTypeDefCategory.RELATIONSHIP_DEF)
                        // Relationship event - check our action targets, monitored resources and subscribers
                {
                    if (propertyHelper.isTypeOf(event.getElementHeader(), OpenMetadataType.NOTIFICATION_SUBSCRIBER_RELATIONSHIP.typeName))
                    {
                        String changedNotificationGUID = event.getEndOneElementHeader().getGUID();

                        NotificationType notificationType = notificationTypeMap.getNotificationType(changedNotificationGUID);

                        if (notificationType != null)
                        {
                            if (event.getEventType() == OpenMetadataEventType.NEW_ELEMENT_CREATED)
                            {
                                MessageDefinition notificationDescription = BaudotNotificationMessageSet.NEW_SUBSCRIBER.getMessageDefinition(notificationType.getNotificationTypeName(),
                                                                                                                                             notificationType.getNotificationTypeGUID());
                                watchdogContext.welcomeSubscriber(notificationType.getNotificationTypeGUID(),
                                                                  event.getEndTwoElementHeader().getGUID(),
                                                                  null,
                                                                  watchdogContext.getNotificationProperties(notificationDescription,
                                                                                                            notificationType.getNotificationTypeGUID(),
                                                                                                            notificationType.getNotificationCount()),
                                                                  watchdogContext.getRequestParameters(),
                                                                  null,
                                                                  notificationType.getMinimumNotificationInterval(),
                                                                  null);
                            }
                            else if (event.getEventType() == OpenMetadataEventType.ELEMENT_DELETED)
                            {
                                MessageDefinition notificationDescription = BaudotNotificationMessageSet.CANCELLED_SUBSCRIBER.getMessageDefinition(notificationType.getNotificationTypeName(),
                                                                                                                                                   notificationType.getNotificationTypeGUID());
                                watchdogContext.dismissSubscriber(notificationType.getNotificationTypeGUID(),
                                                                  event.getEndTwoElementHeader().getGUID(),
                                                                  null,
                                                                  watchdogContext.getNotificationProperties(notificationDescription,
                                                                                                            notificationType.getNotificationTypeGUID(),
                                                                                                            notificationType.getNotificationCount()),
                                                                  watchdogContext.getRequestParameters(),
                                                                  null);
                            }
                        }
                    }
                    else if (propertyHelper.isTypeOf(event.getElementHeader(), OpenMetadataType.MONITORED_RESOURCE_RELATIONSHIP.typeName))
                    {
                        String changedNotificationGUID = event.getEndOneElementHeader().getGUID();

                        NotificationType notificationType = notificationTypeMap.getNotificationType(changedNotificationGUID);

                        if (notificationType != null)
                        {
                            if (event.getEventType() == OpenMetadataEventType.NEW_ELEMENT_CREATED)
                            {
                                monitoredResources.addMonitoredElement(event.getElementHeader().getGUID(),
                                                                       event.getEndTwoElementHeader(),
                                                                       notificationType.getNotificationTypeGUID());
                            }
                            else if (event.getEventType() == OpenMetadataEventType.ELEMENT_DELETED)
                            {
                                monitoredResources.removeMonitoredElement(event.getEndTwoElementHeader(), notificationType.getNotificationTypeGUID());
                            }
                        }
                    }
                    else if ((propertyHelper.isTypeOf(event.getElementHeader(), OpenMetadataType.ACTION_TARGET_RELATIONSHIP.typeName)) &&
                            (event.getEndOneElementHeader().getGUID().equals(watchdogContext.getConnectorGUID())))
                    {
                        this.refreshCaches();
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

            if (completed)
            {
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
        }
    }


    /**
     * Calculate the next notification time for an event.  The notification handler will
     * notify subscribers if they have not yet received a notification or if the
     * nextScheduledNotification is not null and in the past.
     *
     * @param event event to process
     * @return date/time of next notification
     */
    private Date calculateNextNotificationTime(OpenMetadataOutTopicEvent event)
    {
        Date nextScheduledNotification = event.getElementHeader().getVersions().getUpdateTime();

        if (nextScheduledNotification == null)
        {
            nextScheduledNotification = event.getElementHeader().getVersions().getCreateTime();
        }

        if (event.getElementHeader().getLatestChange() != null)
        {
            nextScheduledNotification = event.getElementHeader().getLatestChange().getVersions().getUpdateTime();

            if (nextScheduledNotification == null)
            {
                nextScheduledNotification = event.getElementHeader().getLatestChange().getVersions().getCreateTime();
            }
        }

        return nextScheduledNotification;
    }


    /**
     * Disconnect is called either because this governance action service called governanceContext.recordCompletionStatus()
     * or the administrator requested this watchdog action service stop running, or the hosting server is shutting down.
     * If disconnect completes before this watchdog action service records
     * its completion status, then the watchdog action service is restarted either at the administrator's request or the next time the server starts.
     * If you do not want this governance action service restarted, be sure to record the completion status in the engine action.
     * <p>
     * The disconnect() method is a standard method from the Open Connector Framework (OCF).  If you need to override this method,
     * be sure to call super.disconnect() in your version.
     * </p>
     *
     * @throws ConnectorCheckedException a problem within the watchdog action service.
     */
    @Override
    public  void disconnect() throws ConnectorCheckedException
    {
        completed = true;

        watchdogContext.disconnectListener();

        super.disconnect();
    }


    /**
     * Manage the list of monitored resources and support queries.
     */
    static class MonitoredResources
    {
        private final Map<String, List<MonitoredResource>> monitoredResources = new HashMap<>();

        /**
         * Set up the monitored elements for a specific notification type.
         *
         * @param initialResources list of elements for the notification type.
         */
        public synchronized void setMonitoredResources(List<RelatedMetadataElementSummary> initialResources,
                                                       String                              notificationTypeGUID)
        {
            if (initialResources != null)
            {
                for (RelatedMetadataElementSummary initialResource : initialResources)
                {
                    if (initialResource != null)
                    {
                        this.addMonitoredElement(initialResource, notificationTypeGUID);
                    }
                }
            }
        }


        /**
         * Add a new element to the map.
         *
         * @param monitoredElement new resource
         */
        public synchronized void addMonitoredElement(RelatedMetadataElementSummary monitoredElement,
                                                     String                        notificationTypeGUID)
        {
            if (monitoredElement != null)
            {
                List<MonitoredResource> resourceList = monitoredResources.get(monitoredElement.getRelatedElement().getElementHeader().getGUID());

                if (resourceList == null)
                {
                    resourceList = new ArrayList<>();
                }

                resourceList.add(new MonitoredResource(monitoredElement, notificationTypeGUID));
                monitoredResources.put(monitoredElement.getRelatedElement().getElementHeader().getGUID(), resourceList);
            }
        }


        /**
         * Add a new element to the map.
         *
         * @param monitoredElement new resource
         */
        public synchronized void addMonitoredElement(String        monitoredResourceRelationshipGUID,
                                                     ElementHeader monitoredElement,
                                                     String        notificationTypeGUID)
        {
            if (monitoredElement != null)
            {
                List<MonitoredResource> resourceList = monitoredResources.get(monitoredElement.getGUID());

                if (resourceList == null)
                {
                    resourceList = new ArrayList<>();
                }

                resourceList.add(new MonitoredResource(monitoredResourceRelationshipGUID, notificationTypeGUID));
                monitoredResources.put(monitoredElement.getGUID(), resourceList);
            }
        }


        /**
         * Remove an element from the map.
         *
         * @param monitoredElement old resource
         */
        public synchronized void removeMonitoredElement(ElementHeader monitoredElement,
                                                        String        notificationTypeGUID)
        {
            if (monitoredElement != null)
            {
                List<MonitoredResource> resourceList = monitoredResources.get(monitoredElement.getGUID());
                if (resourceList != null)
                {
                    for (MonitoredResource resource : resourceList)
                    {
                        if (resource.getNotificationTypeGUID().equals(notificationTypeGUID))
                        {
                            resourceList.remove(resource);
                        }
                    }
                    if (resourceList.isEmpty())
                    {
                        monitoredResources.remove(monitoredElement.getGUID());
                    }
                }
            }
        }


        /**
         * Is this one of the monitored elements?
         *
         * @param potentialElement resource header
         * @return list of monitored resource relationships if monitored, null otherwise
         */
        public synchronized List<MonitoredResource> isMonitored(ElementHeader potentialElement)
        {
            if (potentialElement != null)
            {
                List<MonitoredResource> resourceList = monitoredResources.get(potentialElement.getGUID());

                if (resourceList == null)
                {
                    if ((potentialElement.getAnchor() != null) &&
                            (potentialElement.getAnchor().getClassificationProperties() instanceof AnchorsProperties anchorsProperties) &&
                            (anchorsProperties.getAnchorGUID() != null))
                    {
                        resourceList = monitoredResources.get(anchorsProperties.getAnchorGUID());
                    }
                }

                if ((resourceList != null) && (! resourceList.isEmpty()))
                {
                    return resourceList;
                }
            }

            return null;
        }
    }


    /**
     * Represents a monitored resource with its relationship GUID, properties, and element header.
     * Note: the same element may be monitored by multiple notification types.
     */
    static class MonitoredResource
    {
        private final String                      monitoredResourceRelationshipGUID;
        private final String                      notificationTypeGUID;

        /**
         * Constructor using resource information from refreshing the caches.
         *
         * @param retrievedResource resource from the repository
         * @param notificationTypeGUID associates notification type
         */
        public MonitoredResource(RelatedMetadataElementSummary retrievedResource,
                                 String                        notificationTypeGUID)
        {
            this.notificationTypeGUID = notificationTypeGUID;

            this.monitoredResourceRelationshipGUID = retrievedResource.getRelationshipHeader().getGUID();
        }


        /**
         * Constructor using resource information from a change event.
         *
         * @param monitoredResourceRelationshipGUID relationship GUID for the monitored resource
         * @param notificationTypeGUID              associates notification type
         */
        public MonitoredResource(String        monitoredResourceRelationshipGUID,
                                 String        notificationTypeGUID)
        {
            this.notificationTypeGUID = notificationTypeGUID;

            this.monitoredResourceRelationshipGUID = monitoredResourceRelationshipGUID;
        }


        /**
         * Return the notification type GUID for this resource.
         *
         * @return string
         */
        public String getNotificationTypeGUID()
        {
            return notificationTypeGUID;
        }


        /**
         * Return the relationship GUID for this resource.
         *
         * @return string
         */
        public String getMonitoredResourceRelationshipGUID()
        {
            return monitoredResourceRelationshipGUID;
        }
    }


    /**
     * Manage the list of action target notification types and support queries.
     */
    static class NotificationTypes
    {
        private final Map<String, NotificationType> notificationTypeMap = new HashMap<>();

        /**
         * Set up the details of a specific notification type.  If the notification type
         * is already in the map, it is replaced with new values.
         *
         * @param openMetadataRootElement details of a notification type.
         * @return true if the notification type is new
         */
        public synchronized boolean setNotificationType(OpenMetadataRootElement openMetadataRootElement)
        {
            if (openMetadataRootElement != null)
            {
                NotificationType notificationType = new NotificationType(openMetadataRootElement);

                return (notificationTypeMap.put(notificationType.getNotificationTypeGUID(), notificationType) == null);
            }

            return false;
        }


        /**
         * Retrieve a notification type from the map.
         *
         * @param notificationTypeGUID unique identifier of the notification type
         * @return notification type or null if not found
         */
        public synchronized NotificationType getNotificationType(String notificationTypeGUID)
        {
            return notificationTypeMap.get(notificationTypeGUID);
        }


        /**
         * Remove an element from the map.
         *
         * @param monitoredElement old resource
         */
        public synchronized void removeMonitoredElement(ElementHeader monitoredElement)
        {
            if (monitoredElement != null)
            {
                notificationTypeMap.remove(monitoredElement.getGUID());
            }
        }


        /**
         * Is this one of the monitored elements?
         *
         * @param potentialElement resource header
         * @return boolean - true for a monitored element
         */
        public synchronized boolean isMonitored(ElementHeader potentialElement)
        {
            if (potentialElement != null)
            {
                if (notificationTypeMap.containsKey(potentialElement.getGUID()))
                {
                    return true;
                }

                if ((potentialElement.getAnchor() != null) &&
                        (potentialElement.getAnchor().getClassificationProperties() instanceof AnchorsProperties anchorsProperties) &&
                        (anchorsProperties.getAnchorGUID() != null))
                {
                    return notificationTypeMap.containsKey(anchorsProperties.getAnchorGUID());
                }
            }

            return false;
        }
    }


    /**
     * Cached information about a notification type.
     */
    static class NotificationType
    {
        private String                     notificationTypeGUID;
        private String                     notificationTypeName;
        private NotificationTypeProperties notificationTypeProperties;
        private long                       notificationCount;

        /**
         * Constructor uses a root element to initialize the notification type properties.
         *
         * @param openMetadataRootElement full description of a notification type
         */
        public NotificationType(OpenMetadataRootElement openMetadataRootElement)
        {
            if ((openMetadataRootElement != null) && (openMetadataRootElement.getProperties() instanceof NotificationTypeProperties properties))
            {
                this.notificationTypeGUID = openMetadataRootElement.getElementHeader().getGUID();
                this.setNotificationTypeProperties(properties);
            }
        }


        /**
         * Refresh the properties for this notification type.
         *
         * @param notificationTypeProperties properties
         */
        public synchronized void setNotificationTypeProperties(NotificationTypeProperties notificationTypeProperties)
        {
            this.notificationTypeProperties = notificationTypeProperties;

            if (notificationTypeProperties.getDisplayName() != null)
            {
                notificationTypeName = notificationTypeProperties.getDisplayName();
            }
            else if (notificationTypeProperties.getQualifiedName() != null)
            {
                notificationTypeName = notificationTypeProperties.getQualifiedName();
            }
        }


        /**
         * Return the unique identifier of this notification type.
         *
         * @return string
         */
        public synchronized String getNotificationTypeGUID()
        {
            return notificationTypeGUID;
        }


        /**
         * Return the display name of this notification type.
         *
         * @return string
         */
        public synchronized String getNotificationTypeName()
        {
            return notificationTypeName;
        }


        /**
         * Return whether multiple notifications are permitted.  If false, only one notification will be sent out
         * to a subscriber.
         *
         * @return boolean flag
         */
        public synchronized boolean getMultipleNotificationsPermitted()
        {
            return notificationTypeProperties.getMultipleNotificationsPermitted();
        }


        /**
         * Return the minimum minutes between notifications.  If 0, notifications are sent out whenever the
         * appropriate condition is detected.
         *
         * @return minute count
         */
        public synchronized long getMinimumNotificationInterval()
        {
            return notificationTypeProperties.getMinimumNotificationInterval();
        }


        /**
         * Return the minutes between notifications.  If null, notifications are driven by other events,
         * such as a change to a monitored resource.
         *
         * @return minute count
         */
        public synchronized long getNotificationInterval()
        {
            return notificationTypeProperties.getNotificationInterval();
        }


        /**
         * Return the date/time that the notifications should be sent out if they are on a fixed schedule.
         * If notificationInterval is 0, then this field is null.
         *
         * @return date
         */
        public synchronized Date getNextScheduledNotification()
        {
            return notificationTypeProperties.getNextScheduledNotification();
        }


        /**
         * Return the notification count that is used to ensure unique qualified names for notifications.
         * (Millisecond time is not sufficiently granular - particularly for the note log.
         *
         * @return long
         */
        public synchronized long getNotificationCount()
        {
            return notificationCount++;
        }
    }
}
