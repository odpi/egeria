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
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.ArrayList;
import java.util.List;


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
 *         integration daemon's REST API.  The daemon's schedule is therefore the only clock here; this
 *         connector keeps no timers of its own.</li>
 *     <li>Notifications about changes to a notification type's monitored resources are sent as the change
 *         events arrive on the metadata access server's out topic.  The refresh keeps a cache of which
 *         notification types monitor which resources; the event listener consults it.</li>
 * </ul>
 * This connector was previously a watchdog governance action service that slept between refreshes on a
 * schedule of its own.  Moving it to the integration daemon puts the refresh interval in configuration, and
 * lets an operator - or a test - bring a refresh forward on demand.
 */
public class BaudotSubscriptionManagementConnector extends DynamicIntegrationConnectorBase implements OpenMetadataEventListener
{
    private final MonitoredResources monitoredResources = new MonitoredResources();


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

        auditLog.logMessage(methodName, BaudotAuditCode.STARTING_CONNECTOR.getMessageDefinition(connectorName,
                                                                                                integrationContext.getMetadataAccessServer(),
                                                                                                integrationContext.getMetadataAccessServerPlatformURLRoot()));

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
            auditLog.logException(methodName,
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
        auditLog.logMessage(methodName,
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

                if (monitoredResourceList != null)
                {
                    for (MonitoredResource monitoredResource : monitoredResourceList)
                    {
                        if (monitoredResource != null)
                        {
                            this.notifyMonitoredResourceChanged(monitoredResource, event);
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
                }
            }
        }
        catch (Exception error)
        {
            auditLog.logException(methodName,
                                  BaudotAuditCode.EVENT_PROCESSING_FAILED.getMessageDefinition(connectorName,
                                                                                               error.getClass().getName(),
                                                                                               event.getElementHeader().getGUID(),
                                                                                               error.getMessage()),
                                  error);
        }
    }


    /**
     * Tell a notification type's subscribers that one of its monitored resources has changed.
     *
     * @param monitoredResource the notification type's interest in the changed resource
     * @param event the change
     */
    private void notifyMonitoredResourceChanged(MonitoredResource        monitoredResource,
                                                OpenMetadataOutTopicEvent event)
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

            queryOptions.setGraphQueryDepth(1);
            queryOptions.setIncludeOnlyRelationships(List.of(OpenMetadataType.MONITORED_RESOURCE_RELATIONSHIP.typeName));

            OpenMetadataRootElement notificationType = governanceDefinitionClient.getGovernanceDefinitionByGUID(notificationTypeGUID, queryOptions);

            if ((notificationType != null) && (notificationType.getProperties() instanceof NotificationTypeProperties notificationTypeProperties))
            {
                long                  notificationCount = notificationTypeProperties.getNotificationCount() + 1;
                List<NewActionTarget> newActionTargets  = new ArrayList<>();
                NewActionTarget       newActionTarget   = new NewActionTarget();

                newActionTarget.setActionTargetGUID(event.getElementHeader().getGUID());
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
                auditLog.logMessage(methodName,
                                    BaudotAuditCode.UNREADABLE_NOTIFICATION_TYPE.getMessageDefinition(connectorName,
                                                                                                      monitoredResource.getNotificationTypeDisplayName(),
                                                                                                      notificationTypeGUID));
            }
        }
        catch (Exception error)
        {
            auditLog.logException(methodName,
                                  BaudotAuditCode.NOTIFICATION_TYPE_REFRESH_FAILED.getMessageDefinition(connectorName,
                                                                                                        monitoredResource.getNotificationTypeDisplayName(),
                                                                                                        notificationTypeGUID,
                                                                                                        error.getClass().getName(),
                                                                                                        error.getMessage()),
                                  error);
        }
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

        if (auditLog != null)
        {
            auditLog.logMessage(methodName, BaudotAuditCode.CONNECTOR_STOPPING.getMessageDefinition(connectorName));
        }

        super.disconnect();
    }
}
