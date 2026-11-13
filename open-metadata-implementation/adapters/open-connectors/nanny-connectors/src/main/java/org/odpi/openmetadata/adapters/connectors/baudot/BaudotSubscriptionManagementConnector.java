/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.baudot;

import org.odpi.openmetadata.adapters.connectors.baudot.ffdc.BaudotAuditCode;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.integration.connectors.DynamicIntegrationConnectorBase;
import org.odpi.openmetadata.frameworks.integration.context.CatalogTargetContext;
import org.odpi.openmetadata.frameworks.integration.ffdc.OIFAuditCode;
import org.odpi.openmetadata.frameworks.integration.ffdc.OIFErrorCode;
import org.odpi.openmetadata.frameworks.integration.properties.RequestedCatalogTarget;
import org.odpi.openmetadata.frameworks.opengovernance.controls.ActionTarget;
import org.odpi.openmetadata.frameworks.opengovernance.properties.CatalogTarget;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.GovernanceDefinitionClient;
import org.odpi.openmetadata.frameworks.openmetadata.events.OpenMetadataEventListener;
import org.odpi.openmetadata.frameworks.openmetadata.events.OpenMetadataEventType;
import org.odpi.openmetadata.frameworks.openmetadata.events.OpenMetadataOutTopicEvent;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.NewActionTarget;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataTypeDefCategory;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.NotificationTypeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.QueryOptions;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;


/**
 * BaudotSubscriptionManagementConnector is the Baudot Open Metadata Digital Product Subscription Manager: a
 * dynamic integration connector that notifies the subscribers of digital products.  Each notification type
 * it is to look after is given to it as a catalog target - Jacquard does this for every notification type it
 * creates for a product - and a {@link BaudotNotificationTypeProcessor} is created for each.
 * <br><br>
 * There are two distinct parts to the processing, and they are driven differently.
 * <ul>
 *     <li>Welcome, one-time and periodic notifications are sent when the integration daemon refreshes this
 *         connector, which happens on the configured interval and whenever a refresh is requested through the
 *         integration daemon's REST API.</li>
 *     <li>Notifications about changes to a notification type's monitored resources are sent as the change
 *         events arrive on the metadata access server's out topic.  The refresh keeps a cache of which
 *         notification types monitor which resources; the event listener consults it.  A change that arrives
 *         while its notification type is inside its minimum notification interval is held back and sent
 *         the moment the interval ends, by a timer this connector keeps for that alone.</li>
 * </ul>
 * This connector was previously a watchdog governance action service that slept between refreshes on a
 * schedule of its own.  Moving it to the integration daemon puts the refresh interval in configuration, and
 * lets an operator - or a test - bring a refresh forward on demand.
 */
public class BaudotSubscriptionManagementConnector extends DynamicIntegrationConnectorBase implements OpenMetadataEventListener
{
    private final MonitoredResources            monitoredResources = new MonitoredResources();
    private final MonitoredResourceChangeFilter changeFilter       = new MonitoredResourceChangeFilter();

    /*
     * Changes that arrived while their notification type was inside its minimum notification interval, by
     * notification type GUID.  Each is sent when the interval ends.  Only the latest change is kept for each
     * notification type: a subscriber is told that the resource has changed, and the delivery that follows
     * picks up all the changes made since the last one.
     */
    private final Map<String, DeferredChange> deferredChanges = new ConcurrentHashMap<>();

    /*
     * The pending send of each notification type's deferred change, by notification type GUID.
     * <br>
     * Leaving deferred changes to the next refresh made the wait the interval plus up to a whole refresh
     * interval, and the refresh interval has nothing to do with the notification type.  With a five-minute
     * notification interval and a ten-minute refresh, a change could wait fifteen minutes, so a notification
     * type's interval had to be set well below what its service level objective allowed - which is what made
     * the busiest subscriptions deliver many times an hour.  Sending when the interval ends makes the
     * interval the longest wait, so it can be set to what the objective allows.
     */
    private final Map<String, ScheduledFuture<?>> scheduledDeliveries = new ConcurrentHashMap<>();

    /*
     * Margin added to the end of an interval before a deferred change is sent, so that the notification type
     * is certainly outside its interval when it is read again.
     */
    private static final long DEFERRED_DELIVERY_MARGIN_MILLIS = 1000;

    private ScheduledExecutorService deliveryScheduler = null;

    /**
     * A change to a monitored resource that has not been sent yet.
     *
     * @param monitoredResource the notification type's interest in the changed resource
     * @param changedElementGUID the element the change event was for
     */
    private record DeferredChange(MonitoredResource monitoredResource,
                                  String            changedElementGUID) {}


    /**
     * Indicates that the connector is completely configured and can begin processing.
     *
     * @throws ConnectorCheckedException there is a problem within the connector.
     * @throws UserNotAuthorizedException the connector was disconnected before/during start
     */
    @Override
    public void start() throws ConnectorCheckedException, UserNotAuthorizedException
    {
        super.start();

        final String methodName = "start";

        logRecord(methodName, BaudotAuditCode.STARTING_CONNECTOR.getMessageDefinition(connectorName,
                                                                                      integrationContext.getMetadataAccessServer(),
                                                                                      integrationContext.getMetadataAccessServerPlatformURLRoot()));

        deliveryScheduler = Executors.newSingleThreadScheduledExecutor(runnable ->
        {
            Thread thread = new Thread(runnable, connectorName + " deferred deliveries");

            thread.setDaemon(true);
            return thread;
        });

        /*
         * Listening starts now, before the first refresh, rather than being left to the base class - which
         * registers a listener only after the first refresh has completed.
         *
         * Monitored resources can be attached to a notification type while this connector is starting, and a
         * refresh that runs before the listener is in place loses every one attached in between: the refresh
         * does not see them because they are not there yet, and the listener does not see them because it is
         * not listening when the change events are sent.  Listening first means anything the first refresh
         * misses arrives as an event.
         */
        try
        {
            integrationContext.registerListener(this);
        }
        catch (Exception error)
        {
            logExceptionRecord(methodName,
                               OIFAuditCode.UNABLE_TO_REGISTER_LISTENER.getMessageDefinition(connectorName,
                                                                                             error.getClass().getName(),
                                                                                             error.getMessage()),
                               error);

            throw new ConnectorCheckedException(OIFErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                       error.getClass().getName(),
                                                                                                       methodName,
                                                                                                       error.getMessage()),
                                                this.getClass().getName(),
                                                methodName,
                                                error);
        }
    }


    /**
     * Refresh every notification type this connector has been given, then say what is being looked after.
     * The per-notification-type work is in {@link BaudotNotificationTypeProcessor#refresh()}.
     *
     * @throws ConnectorCheckedException there is a problem with the connector.  It is not able to refresh the metadata.
     * @throws UserNotAuthorizedException the connector was disconnected before/during the refresh
     */
    @Override
    public void refresh() throws ConnectorCheckedException, UserNotAuthorizedException
    {
        super.refresh();

        final String methodName = "refresh";

        /*
         * Deferred changes are normally sent by their own timer when their interval ends.  Any still waiting -
         * one whose timer could not be set, say - are tried here too.  One that is still too early stays
         * deferred.
         */
        for (String notificationTypeGUID : new ArrayList<>(deferredChanges.keySet()))
        {
            DeferredChange deferredChange = deferredChanges.get(notificationTypeGUID);

            if (deferredChange != null)
            {
                this.notifyMonitoredResourceChanged(deferredChange.monitoredResource(), deferredChange.changedElementGUID());
            }
        }

        int notificationTypeCount = 0;

        List<RequestedCatalogTarget> requestedCatalogTargets = catalogTargetsManager.getRequestedCatalogTargets();

        if (requestedCatalogTargets != null)
        {
            notificationTypeCount = requestedCatalogTargets.size();
        }

        /*
         * Said on every refresh.  The counts are what tell an operator whether this connector is doing anything:
         * a subscription that is never delivered looks the same from outside as one that has not been asked
         * for, and the difference is visible here and nowhere else.
         */
        logRecord(methodName,
                  BaudotAuditCode.REFRESH_COMPLETE.getMessageDefinition(connectorName,
                                                                        Integer.toString(notificationTypeCount),
                                                                        Integer.toString(monitoredResources.size())));
    }


    /**
     * Create a processor for a newly discovered catalog target - a notification type.
     *
     * @param retrievedCatalogTarget details of the catalog target
     * @param catalogTargetContext context for the catalog target
     * @param connectorToTarget connector to the target - null, a notification type is not an asset
     * @return the processor
     */
    @Override
    public RequestedCatalogTarget getNewRequestedCatalogTargetSkeleton(CatalogTarget        retrievedCatalogTarget,
                                                                       CatalogTargetContext catalogTargetContext,
                                                                       Connector            connectorToTarget)
    {
        return new BaudotNotificationTypeProcessor(retrievedCatalogTarget,
                                                   catalogTargetContext,
                                                   connectorToTarget,
                                                   connectorName,
                                                   auditLog,
                                                   monitoredResources);
    }


    /**
     * This method is called each time a metadata change is received.  Its job is the notifications that are
     * driven by change rather than by the refresh cycle: a change to a monitored resource tells the resource's
     * notification types' subscribers, and a change to the set of monitored resources - or of notification
     * types - keeps the cache the first case depends on current.
     *
     * @param event event containing details of a change to an open metadata element.
     */
    @Override
    public void processEvent(OpenMetadataOutTopicEvent event)
    {
        final String methodName = "processEvent";

        if ((! this.isActive()) || (event == null) || (event.getElementHeader() == null))
        {
            return;
        }

        try
        {
            if (event.getElementHeader().getType().getTypeCategory() == OpenMetadataTypeDefCategory.ENTITY_DEF)
            {
                /*
                 * Only entity events for monitored resources are of interest.  This includes changes to the
                 * classifications - one classification is significant, the LatestChange classification, which
                 * causes events on the monitored resource when any of its anchored elements change.
                 */
                List<MonitoredResource> monitoredResourceList = monitoredResources.isMonitored(event.getElementHeader());

                /*
                 * A record of activity on the resource - it was read, analysed or observed - is not a change to
                 * it.  Counting these as changes made each subscription delivery trigger the next.
                 */
                if ((monitoredResourceList != null) && (changeFilter.isChangeToResource(event)))
                {
                    for (MonitoredResource monitoredResource : monitoredResourceList)
                    {
                        if (monitoredResource != null)
                        {
                            this.notifyMonitoredResourceChanged(monitoredResource, event.getElementHeader().getGUID());
                        }
                    }
                }
            }
            else if (event.getElementHeader().getType().getTypeCategory() == OpenMetadataTypeDefCategory.RELATIONSHIP_DEF)
            {
                if (propertyHelper.isTypeOf(event.getElementHeader(), OpenMetadataType.MONITORED_RESOURCE_RELATIONSHIP.typeName))
                {
                    /*
                     * A monitored resource has been linked to, or unlinked from, a notification type.  It only
                     * matters if the notification type is one of this connector's catalog targets; one that is
                     * not yet is picked up whole - resources and all - by the refresh that first sees it.
                     */
                    String                          notificationTypeGUID = event.getEndOneElementHeader().getGUID();
                    BaudotNotificationTypeProcessor processor            = this.getProcessor(notificationTypeGUID);

                    if (processor != null)
                    {
                        if (event.getEventType() == OpenMetadataEventType.NEW_ELEMENT_CREATED)
                        {
                            monitoredResources.addMonitoredElement(event.getElementHeader().getGUID(),
                                                                   event.getEndTwoElementHeader(),
                                                                   event.getElementProperties(),
                                                                   notificationTypeGUID,
                                                                   processor.getNotificationTypeDisplayName(),
                                                                   integrationContext);
                        }
                        else if (event.getEventType() == OpenMetadataEventType.ELEMENT_DELETED)
                        {
                            monitoredResources.removeMonitoredElement(event.getEndTwoElementHeader(), notificationTypeGUID);
                        }
                    }
                }
                else if ((propertyHelper.isTypeOf(event.getElementHeader(), OpenMetadataType.CATALOG_TARGET_RELATIONSHIP.typeName)) &&
                         (event.getEventType() == OpenMetadataEventType.ELEMENT_DELETED) &&
                         (event.getEndOneElementHeader() != null) &&
                         (event.getEndOneElementHeader().getGUID().equals(integrationContext.getIntegrationConnectorGUID())))
                {
                    /*
                     * A notification type has been taken away from this connector.  Its subscribers are no
                     * longer told about changes; the next refresh retires its processor.
                     */
                    monitoredResources.removeMonitoredNotificationType(event.getEndTwoElementHeader().getGUID());
                    deferredChanges.remove(event.getEndTwoElementHeader().getGUID());
                    this.cancelDeferredDelivery(event.getEndTwoElementHeader().getGUID());
                }
            }
        }
        catch (Exception error)
        {
            logExceptionRecord(methodName,
                               BaudotAuditCode.EVENT_PROCESSING_FAILED.getMessageDefinition(connectorName,
                                                                                            error.getClass().getName(),
                                                                                            event.getElementHeader().getGUID(),
                                                                                            error.getMessage()),
                               error);
        }
    }


    /**
     * Tell a notification type's subscribers that one of its monitored resources has changed.  If the
     * notification type is still within its minimum notification interval, the change is deferred until the
     * interval ends rather than dropped: otherwise the last change in a burst would never reach the
     * subscribers, and a subscription would stay out of date until the resource next changed.
     * <br>
     * It is called from the event listener, the refresh and the deferred delivery timer, so it is synchronized:
     * two of them deciding at the same moment that the interval has passed would each notify the subscribers.
     *
     * @param monitoredResource the notification type's interest in the changed resource
     * @param changedElementGUID the element that the change event was for
     */
    private synchronized void notifyMonitoredResourceChanged(MonitoredResource monitoredResource,
                                                String            changedElementGUID)
    {
        final String methodName = "notifyMonitoredResourceChanged";

        String notificationTypeGUID = monitoredResource.getNotificationTypeGUID();

        try
        {
            /*
             * Only the notification type's properties are needed here - its notification count in particular -
             * so no related elements are asked for: it carries every notification ever sent to its subscribers.
             */
            GovernanceDefinitionClient governanceDefinitionClient = integrationContext.getGovernanceDefinitionClient();
            QueryOptions               queryOptions               = governanceDefinitionClient.getQueryOptions();

            queryOptions.setGraphQueryDepth(0);

            OpenMetadataRootElement notificationType = governanceDefinitionClient.getGovernanceDefinitionByGUID(notificationTypeGUID, queryOptions);

            if ((notificationType != null) && (notificationType.getProperties() instanceof NotificationTypeProperties notificationTypeProperties))
            {
                Date now = new Date();

                if (isWithinMinimumInterval(notificationTypeProperties, now))
                {
                    deferredChanges.put(notificationTypeGUID, new DeferredChange(monitoredResource, changedElementGUID));
                    this.scheduleDeferredDelivery(notificationTypeGUID, getDeferredDeliveryDelay(notificationTypeProperties, now));
                    return;
                }

                deferredChanges.remove(notificationTypeGUID);
                this.cancelDeferredDelivery(notificationTypeGUID);

                long                  notificationCount = OpenMetadataProperty.NOTIFICATION_COUNT.longValueOrDefault(notificationTypeProperties.getNotificationCount()) + 1;
                List<NewActionTarget> newActionTargets  = new ArrayList<>();
                NewActionTarget       newActionTarget   = new NewActionTarget();

                newActionTarget.setActionTargetGUID(changedElementGUID);
                newActionTarget.setActionTargetName(ActionTarget.CHANGED_ELEMENT.name);
                newActionTargets.add(newActionTarget);

                integrationContext.getNotificationManager().notifySubscribers(notificationTypeGUID,
                                                                              null,
                                                                              BaudotNotifications.getNotificationProperties(monitoredResource.getFirstNotificationDescription(), connectorName, notificationTypeGUID, notificationCount),
                                                                              BaudotNotifications.getNotificationProperties(monitoredResource.getNextNotificationDescription(), connectorName, notificationTypeGUID, notificationCount),
                                                                              BaudotNotifications.getNotificationProperties(monitoredResource.getLastNotificationDescription(), connectorName, notificationTypeGUID, notificationCount),
                                                                              null,
                                                                              integrationContext.getIntegrationConnectorGUID(),
                                                                              newActionTargets);
            }
            else
            {
                deferredChanges.remove(notificationTypeGUID);
                this.cancelDeferredDelivery(notificationTypeGUID);

                logRecord(methodName,
                          BaudotAuditCode.UNREADABLE_NOTIFICATION_TYPE.getMessageDefinition(connectorName,
                                                                                            monitoredResource.getNotificationTypeDisplayName(),
                                                                                            notificationTypeGUID));
            }
        }
        catch (Exception error)
        {
            logExceptionRecord(methodName,
                               BaudotAuditCode.NOTIFICATION_TYPE_REFRESH_FAILED.getMessageDefinition(connectorName,
                                                                                                     monitoredResource.getNotificationTypeDisplayName(),
                                                                                                     notificationTypeGUID,
                                                                                                     error.getClass().getName(),
                                                                                                     error.getMessage()),
                               error);
        }
    }


    /**
     * Arrange for a notification type's deferred change to be sent when its interval ends.  A notification type
     * that already has a send pending keeps it: the deferred change it will send is whichever is latest by then.
     *
     * @param notificationTypeGUID unique identifier of the notification type
     * @param delayMillis how long to wait
     */
    private void scheduleDeferredDelivery(String notificationTypeGUID,
                                          long   delayMillis)
    {
        final String methodName = "scheduleDeferredDelivery";

        ScheduledExecutorService scheduler = deliveryScheduler;

        if ((scheduler == null) || (scheduler.isShutdown()))
        {
            return;
        }

        try
        {
            scheduledDeliveries.compute(notificationTypeGUID,
                                        (guid, pending) -> ((pending != null) && (! pending.isDone())) ? pending :
                                                scheduler.schedule(() -> this.sendDeferredChange(guid), delayMillis, TimeUnit.MILLISECONDS));
        }
        catch (Exception error)
        {
            /*
             * The change stays deferred, and the next refresh sends it instead.
             */
            logExceptionRecord(methodName,
                               BaudotAuditCode.EVENT_PROCESSING_FAILED.getMessageDefinition(connectorName,
                                                                                            error.getClass().getName(),
                                                                                            notificationTypeGUID,
                                                                                            error.getMessage()),
                               error);
        }
    }


    /**
     * Send a notification type's deferred change, if it still has one.  Called by the timer set in
     * {@link #scheduleDeferredDelivery}.
     *
     * @param notificationTypeGUID unique identifier of the notification type
     */
    private void sendDeferredChange(String notificationTypeGUID)
    {
        scheduledDeliveries.remove(notificationTypeGUID);

        DeferredChange deferredChange = deferredChanges.get(notificationTypeGUID);

        if ((deferredChange != null) && (this.isActive()))
        {
            this.notifyMonitoredResourceChanged(deferredChange.monitoredResource(), deferredChange.changedElementGUID());
        }
    }


    /**
     * Cancel the pending send of a notification type's deferred change, if there is one.
     *
     * @param notificationTypeGUID unique identifier of the notification type
     */
    private void cancelDeferredDelivery(String notificationTypeGUID)
    {
        ScheduledFuture<?> pending = scheduledDeliveries.remove(notificationTypeGUID);

        if (pending != null)
        {
            pending.cancel(false);
        }
    }


    /**
     * Return how long to wait before sending a change that arrived inside a notification type's minimum
     * notification interval: until the interval ends, plus a small margin so that the notification type is
     * certainly outside it when read again.
     *
     * @param notificationTypeProperties properties of the notification type
     * @param now the time the change arrived
     * @return delay in milliseconds - never negative
     */
    static long getDeferredDeliveryDelay(NotificationTypeProperties notificationTypeProperties,
                                         Date                       now)
    {
        Date lastNotification = notificationTypeProperties.getLastNotification();
        long minimumInterval  = OpenMetadataProperty.MINIMUM_NOTIFICATION_INTERVAL.longValueOrDefault(notificationTypeProperties.getMinimumNotificationInterval());

        if ((lastNotification == null) || (minimumInterval <= 0))
        {
            return DEFERRED_DELIVERY_MARGIN_MILLIS;
        }

        long intervalEnds = lastNotification.getTime() + (minimumInterval * 60 * 1000);

        return Math.max(intervalEnds - now.getTime(), 0) + DEFERRED_DELIVERY_MARGIN_MILLIS;
    }


    /**
     * Return whether a notification type sent its last notification less than its minimum notification
     * interval ago.  The notification manager makes the same check and sends nothing in that case.
     *
     * @param notificationTypeProperties properties of the notification type
     * @param now the time to test
     * @return true if it is too early to notify the subscribers again
     */
    static boolean isWithinMinimumInterval(NotificationTypeProperties notificationTypeProperties,
                                           Date                       now)
    {
        Date lastNotification = notificationTypeProperties.getLastNotification();
        long minimumInterval  = OpenMetadataProperty.MINIMUM_NOTIFICATION_INTERVAL.longValueOrDefault(notificationTypeProperties.getMinimumNotificationInterval());

        if ((lastNotification == null) || (minimumInterval <= 0))
        {
            return false;
        }

        return now.getTime() < lastNotification.getTime() + (minimumInterval * 60 * 1000);
    }


    /**
     * Return the processor looking after a notification type, if it is one of this connector's catalog targets.
     *
     * @param notificationTypeGUID unique identifier of the notification type
     * @return processor or null
     */
    private BaudotNotificationTypeProcessor getProcessor(String notificationTypeGUID)
    {
        if ((catalogTargetsManager != null) && (notificationTypeGUID != null))
        {
            List<RequestedCatalogTarget> requestedCatalogTargets = catalogTargetsManager.getRequestedCatalogTargets();

            if (requestedCatalogTargets != null)
            {
                for (RequestedCatalogTarget requestedCatalogTarget : requestedCatalogTargets)
                {
                    if ((requestedCatalogTarget instanceof BaudotNotificationTypeProcessor processor) &&
                        (notificationTypeGUID.equals(processor.getNotificationTypeGUID())))
                    {
                        return processor;
                    }
                }
            }
        }

        return null;
    }


    /**
     * Shutdown the connector.
     *
     * @throws ConnectorCheckedException something failed in the super class
     */
    @Override
    public void disconnect() throws ConnectorCheckedException
    {
        final String methodName = "disconnect";

        logRecord(methodName, BaudotAuditCode.CONNECTOR_STOPPING.getMessageDefinition(connectorName));

        if (deliveryScheduler != null)
        {
            deliveryScheduler.shutdownNow();
        }

        scheduledDeliveries.clear();

        super.disconnect();
    }
}
