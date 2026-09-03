/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.opengovernance.connectorcontext;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.opengovernance.client.OpenGovernanceClient;
import org.odpi.openmetadata.frameworks.opengovernance.handlers.NotificationHandler;
import org.odpi.openmetadata.frameworks.openmetadata.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.ConnectorContextBase;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.ConnectorContextClientBase;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ClassificationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.NewActionTarget;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.actions.NotificationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.QueryOptions;

import java.util.List;
import java.util.Map;


/**
 * NotificationHandler is the handler for managing notifications for notification types.
 */
public class NotificationManagerClient extends ConnectorContextClientBase
{
    private final NotificationHandler  notificationHandler;

    /**
     * Constructor for connector context client.
     *
     * @param parentContext        connector's context
     * @param localServerName      local server where this client is running - used for error handling
     * @param localServiceName     local service that his connector is hosted by - used for error handling
     * @param connectorUserId      the userId to use with all requests for open metadata
     * @param connectorGUID        the unique identifier that represents this connector in open metadata
     * @param externalSourceGUID   unique identifier of the software server capability for the source of metadata
     * @param externalSourceName   unique name of the software server capability for the source of metadata
     * @param openMetadataClient   client to access open metadata
     * @param openGovernanceClient client to access open governance services
     * @param auditLog             logging destination
     * @param maxPageSize          max number of elements that can be returned on a query
     */
    public NotificationManagerClient(ConnectorContextBase parentContext,
                                     String               localServerName,
                                     String               localServiceName,
                                     String               connectorUserId,
                                     String               connectorGUID,
                                     String               externalSourceGUID,
                                     String               externalSourceName,
                                     OpenMetadataClient   openMetadataClient,
                                     OpenGovernanceClient openGovernanceClient,
                                     AuditLog             auditLog,
                                     int                  maxPageSize)
    {
        super(parentContext,
              localServerName,
              localServiceName,
              connectorUserId,
              connectorGUID,
              externalSourceGUID,
              externalSourceName,
              auditLog,
              maxPageSize);

        this.notificationHandler = new NotificationHandler(localServerName,
                                                           auditLog,
                                                           localServiceName,
                                                           openMetadataClient,
                                                           openGovernanceClient);
    }


    /**
     * Return the list of resources linked to the supplied notification type.
     *
     * @param notificationTypeGUID unique identifier of the notification type
     * @param queryOptions options to control the query
     * @return list of resources to monitor
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<OpenMetadataRootElement> getMonitoredResources(String       notificationTypeGUID,
                                                               QueryOptions queryOptions) throws InvalidParameterException,
                                                                                                 PropertyServerException,
                                                                                                 UserNotAuthorizedException
    {
        return notificationHandler.getMonitoredResources(connectorUserId, notificationTypeGUID, queryOptions);
    }


    /**
     * Create a notification/action for the subscribers.
     *
     * @param notificationTypeGUID   unique identifier of the cause for the action to be raised
     * @param firstNotificationProperties  properties for the first notification sent to this subscriber by this governance service instance
     * @param nextNotificationProperties   properties for a follow-on notification sent to this subscriber by this governance service instance
     * @param lastNotificationProperties   properties for the last notification sent to this subscriber by this governance service instance
     * @param initialClassifications initial classifications to add to the action
     * @param requestParameters      firstNotificationProperties to pass to the next governance service
     * @param actionRequesterGUID    unique identifier of the source of the action
     * @param actionTargets          the list of elements that should be acted upon
     * @throws InvalidParameterException  the completion status is null
     * @throws UserNotAuthorizedException the governance action service is not authorized to update the governance action service status
     * @throws PropertyServerException    a problem connecting to the metadata store
     */
    public void notifySubscribers(String                                notificationTypeGUID,
                                  Map<String, ClassificationProperties> initialClassifications,
                                  NotificationProperties                firstNotificationProperties,
                                  NotificationProperties                nextNotificationProperties,
                                  NotificationProperties                lastNotificationProperties,
                                  Map<String, String>                   requestParameters,
                                  String                                actionRequesterGUID,
                                  List<NewActionTarget>                 actionTargets) throws InvalidParameterException,
                                                                                                    UserNotAuthorizedException,
                                                                                                    PropertyServerException
    {
        notificationHandler.notifySubscribers(externalSourceGUID,
                                              externalSourceName,
                                              connectorUserId,
                                              initialClassifications,
                                              firstNotificationProperties,
                                              nextNotificationProperties,
                                              lastNotificationProperties,
                                              notificationTypeGUID,
                                              requestParameters,
                                              actionRequesterGUID,
                                              actionTargets,
                                              parentContext.getConnectorName());
    }


    /**
     * Return the list of subscribers linked to the supplied notification type.
     *
     * @param notificationTypeGUID unique identifier of the notification type
     * @param queryOptions options to control the query
     * @return list of resources to monitor
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<OpenMetadataRootElement> getNotificationSubscribers(String       notificationTypeGUID,
                                                                    QueryOptions queryOptions) throws InvalidParameterException,
                                                                                                      PropertyServerException,
                                                                                                      UserNotAuthorizedException
    {
        return notificationHandler.getNotificationSubscribers(connectorUserId, notificationTypeGUID, queryOptions);
    }
}
