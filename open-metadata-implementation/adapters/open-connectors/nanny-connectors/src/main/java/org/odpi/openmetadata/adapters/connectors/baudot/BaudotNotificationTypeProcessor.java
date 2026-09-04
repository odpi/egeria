/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.baudot;

import org.odpi.openmetadata.adapters.connectors.baudot.ffdc.BaudotAuditCode;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.MessageDefinition;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.integration.connectors.CatalogTargetProcessorBase;
import org.odpi.openmetadata.frameworks.integration.context.CatalogTargetContext;
import org.odpi.openmetadata.frameworks.opengovernance.properties.CatalogTarget;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.GovernanceDefinitionClient;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.actions.NotificationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.NotificationTypeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.QueryOptions;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Date;
import java.util.List;


/**
 * BaudotNotificationTypeProcessor looks after one notification type on behalf of the Baudot Subscription
 * Manager.  Each notification type handed to Baudot as a catalog target gets one of these, and the
 * integration daemon calls {@link #refresh()} on it every refresh cycle - on the interval the connector is
 * configured with, or immediately when a refresh is requested through the integration daemon's REST API.
 * <br><br>
 * On each refresh the notification type is re-read.  A notification type with no monitored resources is one
 * whose subscribers are told things on a schedule: they are notified now, and the notification manager works
 * out for each subscriber whether that is a welcome, a one-time notification or a periodic one, and skips
 * anyone notified within the type's minimum interval.  A notification type with monitored resources is one
 * whose subscribers are told when something changes: its resources are (re)loaded into the cache that the
 * connector's event listener consults, and the notifications themselves are sent as the change events arrive.
 */
public class BaudotNotificationTypeProcessor extends CatalogTargetProcessorBase
{
    private final MonitoredResources monitoredResources;

    private String notificationTypeDisplayName = null;


    /**
     * Constructor.
     *
     * @param catalogTarget details of the catalog target - the notification type
     * @param catalogTargetContext context for this catalog target
     * @param connectorToTarget connector to the target - null, a notification type is not an asset
     * @param connectorName name of the Baudot connector
     * @param auditLog audit log
     * @param monitoredResources the cache of monitored resources shared with the connector's event listener
     */
    public BaudotNotificationTypeProcessor(CatalogTarget        catalogTarget,
                                           CatalogTargetContext catalogTargetContext,
                                           Connector            connectorToTarget,
                                           String               connectorName,
                                           AuditLog             auditLog,
                                           MonitoredResources   monitoredResources)
    {
        super(catalogTarget, catalogTargetContext, connectorToTarget, connectorName, auditLog);

        this.monitoredResources = monitoredResources;
    }


    /**
     * Return the unique identifier of the notification type this processor looks after.
     *
     * @return string guid, or null if the catalog target carries no element
     */
    public String getNotificationTypeGUID()
    {
        if ((this.getCatalogTargetElement() != null) && (this.getCatalogTargetElement().getElementHeader() != null))
        {
            return this.getCatalogTargetElement().getElementHeader().getGUID();
        }

        return null;
    }


    /**
     * Return the display name of the notification type this processor looks after.  Until the first refresh has
     * read the notification type's properties, it is taken from the catalog target element.
     *
     * @return string
     */
    public String getNotificationTypeDisplayName()
    {
        if ((notificationTypeDisplayName == null) && (this.getCatalogTargetElement() != null))
        {
            notificationTypeDisplayName = integrationContext.getGovernanceDefinitionClient().getDisplayName(this.getCatalogTargetElement());
        }

        return notificationTypeDisplayName;
    }


    /**
     * Notify the notification type's subscribers, or reload its monitored resources, as its properties dictate.
     *
     * @throws ConnectorCheckedException there is a problem with the connector.  It is not able to refresh the metadata.
     * @throws UserNotAuthorizedException the connector was disconnected before/during the refresh
     */
    @Override
    public void refresh() throws ConnectorCheckedException, UserNotAuthorizedException
    {
        super.refresh();

        final String methodName = "refresh";

        String notificationTypeGUID = this.getNotificationTypeGUID();

        if (notificationTypeGUID == null)
        {
            auditLog.logMessage(methodName,
                                BaudotAuditCode.UNREADABLE_NOTIFICATION_TYPE.getMessageDefinition(connectorName,
                                                                                                  this.getCatalogTargetName(),
                                                                                                  "null"));
            return;
        }

        try
        {
            /*
             * The full element is retrieved because it carries the notification type's properties in bean form
             * and the list of its monitored resources; the catalog target relationship does not.  Only the
             * monitored resource relationships are asked for: a notification type also carries every
             * notification ever sent to its subscribers, which grows without bound and is of no use here.
             */
            GovernanceDefinitionClient governanceDefinitionClient = integrationContext.getGovernanceDefinitionClient();
            QueryOptions               queryOptions               = governanceDefinitionClient.getQueryOptions();

            queryOptions.setIncludeOnlyRelationships(List.of(OpenMetadataType.MONITORED_RESOURCE_RELATIONSHIP.typeName));

            OpenMetadataRootElement notificationTypeElement = governanceDefinitionClient.getGovernanceDefinitionByGUID(notificationTypeGUID, queryOptions);

            if ((notificationTypeElement != null) && (notificationTypeElement.getProperties() instanceof NotificationTypeProperties notificationTypeProperties))
            {
                notificationTypeDisplayName = notificationTypeProperties.getDisplayName();

                if (notificationTypeElement.getMonitoredResources() == null)
                {
                    this.notifySubscribers(notificationTypeGUID, notificationTypeProperties);
                }
                else
                {
                    /*
                     * Changes to these resources are noticed by the connector's event listener, which needs
                     * to know which notification types care about each of them.
                     */
                    monitoredResources.setMonitoredResources(notificationTypeElement.getMonitoredResources(),
                                                             notificationTypeGUID,
                                                             notificationTypeDisplayName,
                                                             integrationContext);

                    auditLog.logMessage(methodName,
                                        BaudotAuditCode.MONITORED_RESOURCE_NOTIFICATION_TYPE.getMessageDefinition(connectorName,
                                                                                                                  notificationTypeDisplayName,
                                                                                                                  notificationTypeGUID,
                                                                                                                  Integer.toString(notificationTypeElement.getMonitoredResources().size())));
                }
            }
            else
            {
                auditLog.logMessage(methodName,
                                    BaudotAuditCode.UNREADABLE_NOTIFICATION_TYPE.getMessageDefinition(connectorName,
                                                                                                      this.getCatalogTargetName(),
                                                                                                      notificationTypeGUID));
            }
        }
        catch (UserNotAuthorizedException error)
        {
            /*
             * The connector is being disconnected - let the framework see it.
             */
            throw error;
        }
        catch (Exception error)
        {
            /*
             * A problem with one notification type is logged and left; the other notification types are still
             * refreshed, and this one is tried again on the next cycle.
             */
            auditLog.logException(methodName,
                                  BaudotAuditCode.NOTIFICATION_TYPE_REFRESH_FAILED.getMessageDefinition(connectorName,
                                                                                                        notificationTypeDisplayName,
                                                                                                        notificationTypeGUID,
                                                                                                        error.getClass().getName(),
                                                                                                        error.getMessage()),
                                  error);
        }
    }


    /**
     * Send the scheduled notifications for a notification type that has no monitored resources.  The
     * notification manager decides per subscriber which of the three notifications they receive - the first
     * for a new subscriber, the next for an existing one, the last for a subscription that has run its course -
     * and honours the notification type's minimum interval, so this can be called on every refresh.
     *
     * @param notificationTypeGUID unique identifier of the notification type
     * @param notificationTypeProperties its properties
     * @throws Exception problem communicating with the metadata store
     */
    private void notifySubscribers(String                     notificationTypeGUID,
                                   NotificationTypeProperties notificationTypeProperties) throws Exception
    {
        final String methodName = "notifySubscribers";

        long                   notificationCount          = notificationTypeProperties.getNotificationCount() + 1;
        NotificationProperties nextNotificationProperties;

        if (notificationTypeProperties.getMultipleNotificationsPermitted())
        {
            Date   nextRefresh     = integrationContext.getNextScheduledRefreshTime();
            String nextRefreshText = (nextRefresh == null) ? "the next refresh of this connector" : nextRefresh.toString();

            auditLog.logMessage(methodName,
                                BaudotAuditCode.PERIODIC_NOTIFICATION_TYPE.getMessageDefinition(connectorName,
                                                                                                notificationTypeDisplayName,
                                                                                                notificationTypeGUID,
                                                                                                Long.toString(notificationTypeProperties.getMinimumNotificationInterval()),
                                                                                                nextRefreshText));

            nextNotificationProperties = this.getNotificationProperties(BaudotNotificationMessageSet.PERIODIC_NOTIFICATION.getMessageDefinition(notificationTypeDisplayName,
                                                                                                                                                notificationTypeGUID,
                                                                                                                                                Long.toString(notificationTypeProperties.getMinimumNotificationInterval())),
                                                                        notificationTypeGUID,
                                                                        notificationCount);
        }
        else
        {
            auditLog.logMessage(methodName,
                                BaudotAuditCode.ONE_TIME_NOTIFICATION_TYPE.getMessageDefinition(connectorName,
                                                                                                notificationTypeDisplayName,
                                                                                                notificationTypeGUID));

            nextNotificationProperties = this.getNotificationProperties(BaudotNotificationMessageSet.ONE_TIME_NOTIFICATION.getMessageDefinition(notificationTypeDisplayName,
                                                                                                                                                notificationTypeGUID),
                                                                        notificationTypeGUID,
                                                                        notificationCount);
        }

        integrationContext.getNotificationManager().notifySubscribers(notificationTypeGUID,
                                                                      null,
                                                                      this.getNotificationProperties(BaudotNotificationMessageSet.NEW_SUBSCRIBER.getMessageDefinition(notificationTypeDisplayName,
                                                                                                                                                                      notificationTypeGUID),
                                                                                                     notificationTypeGUID,
                                                                                                     notificationCount),
                                                                      nextNotificationProperties,
                                                                      this.getNotificationProperties(BaudotNotificationMessageSet.CANCELLED_SUBSCRIBER.getMessageDefinition(notificationTypeDisplayName,
                                                                                                                                                                            notificationTypeGUID),
                                                                                                     notificationTypeGUID,
                                                                                                     notificationCount),
                                                                      null,
                                                                      integrationContext.getIntegrationConnectorGUID(),
                                                                      null);
    }


    /**
     * Build the properties for one of this notification type's notifications.
     *
     * @param notificationDescription what the notification says
     * @param notificationTypeGUID the notification type
     * @param notificationCount the notification type's running count
     * @return properties
     */
    private NotificationProperties getNotificationProperties(MessageDefinition notificationDescription,
                                                             String            notificationTypeGUID,
                                                             long              notificationCount)
    {
        return BaudotNotifications.getNotificationProperties(notificationDescription, connectorName, notificationTypeGUID, notificationCount);
    }
}
