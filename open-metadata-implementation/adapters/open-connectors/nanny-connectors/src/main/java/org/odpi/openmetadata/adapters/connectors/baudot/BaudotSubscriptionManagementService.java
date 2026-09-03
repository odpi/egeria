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
import org.odpi.openmetadata.frameworks.openmetadata.properties.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.actions.NotificationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.NotificationTypeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.ElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworks.openwatchdog.GenericWatchdogActionListener;
import org.odpi.openmetadata.frameworks.openwatchdog.WatchdogActionServiceConnector;
import org.odpi.openmetadata.frameworks.openwatchdog.WatchdogContext;
import org.odpi.openmetadata.frameworks.openwatchdog.controls.WatchdogActionGuard;

import java.util.*;


/**
 * BaudotSubscriptionManagementService is a Watchdog Governance Action Service that listens for changes to its
 * monitored resources and notifies its subscribers when a change occurs.  There are 2 distinct parts to this processing.
 * It listens for changes to its subscribers and sends welcome and cancellation notifications.
 * It is designed to run continuously and so does not set up completion status or guards unless it fails.  This means its
 * Engine Action entity is never (rarely) completed and as a result, this service is restarted each time the hosting engine is restarted.
 */
public class BaudotSubscriptionManagementService extends WatchdogActionServiceConnector
{
    volatile boolean completed = false;

    private final GenericWatchdogActionListener listener             = new GenericWatchdogActionListener(this);
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

        try
        {
            /*
             * Listening starts before the first cache refresh, not after it.
             *
             * The notification types managed by this service are attached to its engine action using ActionTarget
             * relationships, and that can happen while this service is starting - so the two can race.
             * Refreshing first and then listening loses every notification type attached in between: the
             * refresh does not see them because they are not there yet, and the listener does not see them
             * because it is not listening when the change events are sent.  They would then go unmonitored
             * until the next scheduled refresh, an hour later.  Listening first means anything missed by
             * the first scan arrives as an event and can be skipped.
             */
            watchdogContext.registerListener(listener,
                                             null,
                                             null,
                                             null);

            while (! completed)
            {
                /*
                 * Set up the caches. Begin by extracting the notification types from the action targets.
                 * Once the caches are set up, send out first time (monitor resources), periodic and
                 * onte-time notifications.
                 */
                Date nextRefresh = performPeriodicNotifications();

                /*
                 * Wait for the next time the caches need to be refreshed.
                 */

                long sleepTime = nextRefresh.getTime() - System.currentTimeMillis();

                while (sleepTime > 0)
                {
                    try
                    {
                        Thread.sleep(sleepTime);
                        if (! super.isActive())
                        {
                            completed = true;
                            break;
                        }
                    }
                    catch (InterruptedException ignored)
                    {
                    }

                    sleepTime = nextRefresh.getTime() - System.currentTimeMillis();
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

            completed = true;

            throw new GovernanceServiceException(GovernanceActionConnectorsErrorCode.UNABLE_TO_REGISTER_LISTENER.getMessageDefinition(watchdogActionServiceName,
                                                                                                                                      error.getClass().getName(),
                                                                                                                                      error.getMessage()),
                                                 error.getClass().getName(),
                                                 methodName,
                                                 error);
        }
    }


    /**
     * Review the notification types and deliver the notifications. Maintain the caches for the event processing.
     *
     * @return the next time that the caches should be refreshed.
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the watchdog action service is not authorized to continue
     * @throws PropertyServerException    a problem connecting to the metadata store
     */
    Date performPeriodicNotifications() throws InvalidParameterException,
                                               PropertyServerException,
                                               UserNotAuthorizedException
    {
        final String methodName = "performPeriodicNotifications";

        /*
         * The default next refresh is 1 hour from now.  This will be modified if a periodic notification
         * needs to be processed before that.
         */
        Date nextRefresh = new Date(System.currentTimeMillis() + (60 * 60 * 1000));

        /*
         * Locate all the action targets that are notification types.
         */
        List<ActionTargetElement> notificationTypeTargetElements = watchdogContext.getNotificationTypesFromActionTargets();

        /*
         * These counts are used for messages.
         */
        int monitoredNotificationTypeCount = 0;
        int newNotificationTypeCount       = 0;

        if ((notificationTypeTargetElements != null) && (! notificationTypeTargetElements.isEmpty()))
        {
            /*
             * Loop through the notification types and process the ones that need processing.
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
                        monitoredNotificationTypeCount++;

                        if (notificationTypeProperties.getLastNotification() == null)
                        {
                            newNotificationTypeCount ++;
                        }

                        if (notificationTypeElement.getMonitoredResources() == null)
                        {
                            long notificationCount = notificationTypeProperties.getNotificationCount() + 1;
                            MessageDefinition newNotificationDescription = BaudotNotificationMessageSet.NEW_SUBSCRIBER.getMessageDefinition(watchdogContext.getDisplayName(notificationTypeProperties), notificationTypeElement.getElementHeader().getGUID());

                            NotificationProperties nextNotificationProperties;

                            if (notificationTypeProperties.getMultipleNotificationsPermitted())
                            {
                                auditLog.logMessage(methodName,
                                                    BaudotAuditCode.PERIODIC_NOTIFICATION_TYPE.getMessageDefinition(watchdogActionServiceName,
                                                                                                                    notificationTypeProperties.getDisplayName(),
                                                                                                                    notificationTypeElement.getElementHeader().getGUID(),
                                                                                                                    Long.toString(notificationTypeProperties.getMinimumNotificationInterval()),
                                                                                                                    nextRefresh.toString()));
                                nextNotificationProperties = watchdogContext.getNotificationProperties(BaudotNotificationMessageSet.PERIODIC_NOTIFICATION.getMessageDefinition(notificationTypeProperties.getDisplayName(),
                                                                                                                                                                               notificationTypeElement.getElementHeader().getGUID(),
                                                                                                                                                                               Long.toString(notificationTypeProperties.getMinimumNotificationInterval())),
                                                                                                       notificationTypeElement.getElementHeader().getGUID(),
                                                                                                       notificationCount);
                            }
                            else
                            {
                                auditLog.logMessage(methodName,
                                                    BaudotAuditCode.ONE_TIME_NOTIFICATION_TYPE.getMessageDefinition(watchdogActionServiceName,
                                                                                                                    notificationTypeProperties.getDisplayName(),
                                                                                                                    notificationTypeElement.getElementHeader().getGUID()));

                                nextNotificationProperties = watchdogContext.getNotificationProperties(BaudotNotificationMessageSet.ONE_TIME_NOTIFICATION.getMessageDefinition(notificationTypeProperties.getDisplayName(),
                                                                                                                                                                               notificationTypeElement.getElementHeader().getGUID()),
                                                                                                                          notificationTypeElement.getElementHeader().getGUID(),
                                                                                                                          notificationCount);
                            }

                            watchdogContext.notifySubscribers(actionTargetElement.getActionTargetGUID(),
                                                              null,
                                                              watchdogContext.getNotificationProperties(newNotificationDescription,
                                                                                                        notificationTypeElement.getElementHeader().getGUID(),
                                                                                                        notificationCount),
                                                              nextNotificationProperties,
                                                              watchdogContext.getNotificationProperties(BaudotNotificationMessageSet.CANCELLED_SUBSCRIBER.getMessageDefinition(notificationTypeProperties.getDisplayName(), notificationTypeElement.getElementHeader().getGUID()),
                                                                                                        notificationTypeElement.getElementHeader().getGUID(),
                                                                                                        notificationCount),
                                                              watchdogContext.getRequestParameters(),
                                                              null);
                        }
                        else
                        {
                            /*
                             * These resources are monitored by the event listener.
                             */
                            monitoredResources.setMonitoredResources(notificationTypeElement.getMonitoredResources(),
                                                                     notificationTypeElement.getElementHeader().getGUID(),
                                                                     notificationTypeProperties.getDisplayName(),
                                                                     watchdogContext);

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
                        auditLog.logMessage(methodName,
                                            BaudotAuditCode.UNREADABLE_NOTIFICATION_TYPE.getMessageDefinition(watchdogActionServiceName,
                                                                                                              actionTargetElement.getActionTargetName(),
                                                                                                              actionTargetElement.getActionTargetGUID()));
                    }
                }
            }
        }

        /*
         * Said on every refresh.  The counts are what tell an operator whether this service is doing anything:
         * a subscription that is never delivered looks the same from outside as one that has not been asked
         * for, and the difference is visible here and nowhere else.
         */
        auditLog.logMessage(methodName,
                            BaudotAuditCode.CACHE_REFRESHED.getMessageDefinition(watchdogActionServiceName,
                                                                                  Integer.toString(monitoredNotificationTypeCount),
                                                                                  Integer.toString(newNotificationTypeCount)));

        return nextRefresh;
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
                    List<MonitoredResource> monitoredResourceList = monitoredResources.isMonitored(event.getElementHeader());

                    if (monitoredResourceList != null)
                    {
                        for (MonitoredResource monitoredResource : monitoredResourceList)
                        {
                            if (monitoredResource != null)
                            {
                                try
                                {
                                    OpenMetadataRootElement notificationType = watchdogContext.getNotificationType(monitoredResource.getNotificationTypeGUID());

                                    if ((notificationType != null) && (notificationType.getProperties() instanceof NotificationTypeProperties notificationTypeProperties))
                                    {
                                        List<NewActionTarget> newActionTargets = new ArrayList<>();
                                        NewActionTarget       newActionTarget  = new NewActionTarget();

                                        newActionTarget.setActionTargetGUID(event.getElementHeader().getGUID());
                                        newActionTarget.setActionTargetName(ActionTarget.CHANGED_ELEMENT.name);

                                        newActionTargets.add(newActionTarget);

                                        watchdogContext.notifySubscribers(monitoredResource.getNotificationTypeGUID(),
                                                                          null,
                                                                          watchdogContext.getNotificationProperties(monitoredResource.getFirstNotificationDescription(), monitoredResource.getNotificationTypeGUID(), notificationTypeProperties.getNotificationCount() + 1),
                                                                          watchdogContext.getNotificationProperties(monitoredResource.getNextNotificationDescription(), monitoredResource.getNotificationTypeGUID(), notificationTypeProperties.getNotificationCount() + 1),
                                                                          watchdogContext.getNotificationProperties(monitoredResource.getLastNotificationDescription(), monitoredResource.getNotificationTypeGUID(), notificationTypeProperties.getNotificationCount() + 1),
                                                                          watchdogContext.getRequestParameters(),
                                                                          newActionTargets);
                                    }
                                }
                                catch (Exception error)
                                {
                                    auditLog.logMessage(methodName, BaudotAuditCode.UNREADABLE_NOTIFICATION_TYPE.getMessageDefinition(watchdogActionServiceName,
                                                                                                                                      monitoredResource.getNotificationTypeDisplayName(),
                                                                                                                                      monitoredResource.getNotificationTypeGUID()));
                                }
                            }
                        }
                    }
                }
                else if (event.getElementHeader().getType().getTypeCategory() == OpenMetadataTypeDefCategory.RELATIONSHIP_DEF)
                        // Relationship event - check our action targets and monitored resources
                {
                    if (propertyHelper.isTypeOf(event.getElementHeader(), OpenMetadataType.MONITORED_RESOURCE_RELATIONSHIP.typeName))
                    {
                        String changedNotificationGUID = event.getEndOneElementHeader().getGUID();

                        List<ActionTargetElement> notificationTypeTargetElements = watchdogContext.getNotificationTypesFromActionTargets();

                        if (notificationTypeTargetElements != null)
                        {
                            for (ActionTargetElement notificationTypeTargetElement : notificationTypeTargetElements)
                            {
                                if ((notificationTypeTargetElement != null) && (notificationTypeTargetElement.getActionTargetGUID().equals(changedNotificationGUID)))
                                {
                                    if (event.getEventType() == OpenMetadataEventType.NEW_ELEMENT_CREATED)
                                    {
                                        monitoredResources.addMonitoredElement(event.getElementHeader().getGUID(),
                                                                               event.getEndTwoElementHeader(),
                                                                               event.getElementProperties(),
                                                                               changedNotificationGUID,
                                                                               watchdogContext.getDisplayName(notificationTypeTargetElement.getTargetElement().getElementProperties()),
                                                                               watchdogContext);
                                    }
                                    else if (event.getEventType() == OpenMetadataEventType.ELEMENT_DELETED)
                                    {
                                        monitoredResources.removeMonitoredElement(event.getEndTwoElementHeader(), changedNotificationGUID);
                                    }
                                }
                            }
                        }
                    }
                    else if ((propertyHelper.isTypeOf(event.getElementHeader(), OpenMetadataType.ACTION_TARGET_RELATIONSHIP.typeName)) &&
                            (event.getEventType() == OpenMetadataEventType.ELEMENT_DELETED) &&
                            (event.getEndOneElementHeader().getGUID().equals(watchdogContext.getEngineActionGUID())))
                    {
                        monitoredResources.removeMonitoredNotificationType(event.getEndTwoElementHeader().getGUID());
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
     * Manage the list of monitored resources linked to this service's action targets, and support queries.
     */
    static class MonitoredResources
    {
        /*
         * Map from resourceGUID to the list of associated notification types.  This is used to rapidly navigate
         * to the notification types that are monitoring a given resource.
         */
        private final Map<String, List<MonitoredResource>> monitoredResources = new HashMap<>();

        /**
         * Constructor
         */
        public MonitoredResources()
        {
        }


        /**
         * Set up the monitored elements for a specific notification type.
         *
         * @param initialResources list of elements for the notification type.
         */
        public synchronized void setMonitoredResources(List<RelatedMetadataElementSummary> initialResources,
                                                       String                              notificationTypeGUID,
                                                       String                              notificationTypeDisplayName,
                                                       WatchdogContext                     watchdogContext)
        {
            if (initialResources != null)
            {
                for (RelatedMetadataElementSummary initialResource : initialResources)
                {
                    if (initialResource != null)
                    {
                        this.addMonitoredElement(initialResource, notificationTypeGUID, notificationTypeDisplayName, watchdogContext);
                    }
                }
            }
        }


        /**
         * Add a new element to the map.
         *
         * @param monitoredElement            new resource
         * @param notificationTypeGUID        unique identifier of the notification type
         * @param notificationTypeDisplayName name of the notification type
         * @param watchdogContext             context for the watchdog
         */
        public synchronized void addMonitoredElement(RelatedMetadataElementSummary monitoredElement,
                                                     String                        notificationTypeGUID,
                                                     String                        notificationTypeDisplayName,
                                                     WatchdogContext               watchdogContext)
        {
            if (monitoredElement != null)
            {
                List<MonitoredResource> resourceList = monitoredResources.get(monitoredElement.getRelatedElement().getElementHeader().getGUID());

                if (resourceList == null)
                {
                    resourceList = new ArrayList<>();
                }

                resourceList.add(new MonitoredResource(monitoredElement, notificationTypeGUID, notificationTypeDisplayName, watchdogContext));
                monitoredResources.put(monitoredElement.getRelatedElement().getElementHeader().getGUID(), resourceList);
            }
        }


        /**
         * Adds a monitored element along with its related properties, relationship GUID, and notification type details
         * to the monitored resources map. If the monitored element is already present, it updates the monitored resource list
         * for the given element.
         *
         * @param monitoredResourceRelationshipGUID the unique identifier for the monitored resource relationship
         * @param monitoredElement                  the element header of the monitored resource being added
         * @param monitoredElementProperties        the properties of the monitored resource element
         * @param notificationTypeGUID              the unique identifier for the notification type
         * @param notificationTypeDisplayName       the display name of the notification type
         * @param watchdogContext                   the watchdog context for the notification type
         */
        public synchronized void addMonitoredElement(String            monitoredResourceRelationshipGUID,
                                                     ElementHeader     monitoredElement,
                                                     ElementProperties monitoredElementProperties,
                                                     String            notificationTypeGUID,
                                                     String            notificationTypeDisplayName,
                                                     WatchdogContext   watchdogContext)
        {
            if (monitoredElement != null)
            {
                List<MonitoredResource> resourceList = monitoredResources.get(monitoredElement.getGUID());

                if (resourceList == null)
                {
                    resourceList = new ArrayList<>();
                }

                resourceList.add(new MonitoredResource(monitoredResourceRelationshipGUID,
                                                       monitoredElement.getGUID(),
                                                       watchdogContext.getDisplayName(monitoredElementProperties),
                                                       monitoredElement.getType().getTypeName(),
                                                       notificationTypeGUID, notificationTypeDisplayName));

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
         * Remove an element from the map.
         *
         * @param notificationTypeGUID old notification type
         */
        public synchronized void removeMonitoredNotificationType(String notificationTypeGUID)
        {
            for (String resourceGUID : monitoredResources.keySet())
            {
                List<MonitoredResource> resourceList = monitoredResources.get(resourceGUID);
                if (resourceList != null)
                {
                    for (MonitoredResource monitoredResource : resourceList)
                    {
                        if ((monitoredResource != null) && (monitoredResource.getNotificationTypeGUID().equals(notificationTypeGUID)))
                        {
                            resourceList.remove(monitoredResource);

                            if (resourceList.isEmpty())
                            {
                                monitoredResources.remove(resourceGUID);
                            }
                        }
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
                    return new ArrayList<>(resourceList);
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
        private final String            monitoredResourceRelationshipGUID;
        private final String            notificationTypeGUID;
        private final String            notificationTypeDisplayName;
        private final MessageDefinition firstNotificationDescription;
        private final MessageDefinition nextNotificationDescription;
        private final MessageDefinition lastNotificationDescription;

        /**
         * Constructor using resource information from refreshing the caches.
         *
         * @param retrievedResource resource from the repository
         * @param notificationTypeGUID associates notification type
         * @param notificationTypeDisplayName the display name of the notification type
         */
        public MonitoredResource(RelatedMetadataElementSummary retrievedResource,
                                 String                        notificationTypeGUID,
                                 String                        notificationTypeDisplayName,
                                 WatchdogContext               watchdogContext)
        {
            this.notificationTypeGUID = notificationTypeGUID;
            this.notificationTypeDisplayName = notificationTypeDisplayName;

            this.monitoredResourceRelationshipGUID = retrievedResource.getRelationshipHeader().getGUID();

            this.firstNotificationDescription = BaudotNotificationMessageSet.NEW_SUBSCRIBER.getMessageDefinition(notificationTypeDisplayName, notificationTypeGUID);

            this.nextNotificationDescription = BaudotNotificationMessageSet.MONITORED_RESOURCE_CHANGED.getMessageDefinition(retrievedResource.getRelatedElement().getElementHeader().getType().getTypeName(),
                                                                                                                            watchdogContext.getDisplayName(retrievedResource.getRelatedElement().getProperties()),
                                                                                                                            retrievedResource.getRelatedElement().getElementHeader().getGUID(),
                                                                                                                            notificationTypeDisplayName,
                                                                                                                            notificationTypeGUID);

            this.lastNotificationDescription = BaudotNotificationMessageSet.CANCELLED_SUBSCRIBER.getMessageDefinition(notificationTypeDisplayName, notificationTypeGUID);
        }


        /**
         * Constructor using resource information from a change event.
         *
         * @param monitoredResourceRelationshipGUID relationship GUID for the monitored resource
         * @param notificationTypeGUID              associates notification type
         */
        public MonitoredResource(String monitoredResourceRelationshipGUID,
                                 String monitoredResourceGUID,
                                 String monitoredResourceDisplayName,
                                 String monitoredResourceTypeName,
                                 String notificationTypeGUID,
                                 String notificationTypeDisplayName)
        {
            this.notificationTypeGUID = notificationTypeGUID;
            this.notificationTypeDisplayName = notificationTypeDisplayName;

            this.monitoredResourceRelationshipGUID = monitoredResourceRelationshipGUID;

            this.firstNotificationDescription = BaudotNotificationMessageSet.NEW_SUBSCRIBER.getMessageDefinition(notificationTypeDisplayName, notificationTypeGUID);

            this.nextNotificationDescription = BaudotNotificationMessageSet.MONITORED_RESOURCE_CHANGED.getMessageDefinition(monitoredResourceTypeName,
                                                                                                                            monitoredResourceDisplayName,
                                                                                                                            monitoredResourceGUID,
                                                                                                                            notificationTypeDisplayName,
                                                                                                                            notificationTypeGUID);

            this.lastNotificationDescription = BaudotNotificationMessageSet.CANCELLED_SUBSCRIBER.getMessageDefinition(notificationTypeDisplayName, notificationTypeGUID);
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
         * Return the notification type's display name for this resource.
         *
         * @return string
         */
        public String getNotificationTypeDisplayName()
        {
            return notificationTypeDisplayName;
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


        /**
         * Retrieves the first notification description associated with the monitored resource.
         *
         * @return MessageDefinition containing details about the first notification.
         */
        public MessageDefinition getFirstNotificationDescription()
        {
            return firstNotificationDescription;
        }


        /**
         * Return the notification description for this resource.
         *
         * @return MessageDefinition
         */
        public MessageDefinition getNextNotificationDescription()
        {
            return nextNotificationDescription;
        }


        /**
         * Retrieves the last notification description associated with the monitored resource.
         *
         * @return MessageDefinition containing details about the last notification.
         */
        public MessageDefinition getLastNotificationDescription()
        {
            return lastNotificationDescription;
        }
    }
}
