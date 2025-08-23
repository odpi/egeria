/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openwatchdog.handlers;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.opengovernance.client.OpenGovernanceClient;
import org.odpi.openmetadata.frameworks.openmetadata.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.GovernanceDefinitionHandler;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.NewActionTarget;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.actions.NotificationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.*;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworks.openwatchdog.ffdc.OWFAuditCode;

import java.util.List;
import java.util.Map;


/**
 * NotificationHandler is the handler for managing notifications for notification types.
 */
public class NotificationHandler extends GovernanceDefinitionHandler
{
    private final OpenGovernanceClient openGovernanceClient;

    /**
     * Create a new handler.
     *
     * @param localServerName        name of this server (view server)
     * @param auditLog               logging destination
     * @param serviceName            local service name
     * @param openMetadataClient     access to open metadata
     */
    public NotificationHandler(String               localServerName,
                               AuditLog             auditLog,
                               String               serviceName,
                               OpenMetadataClient   openMetadataClient,
                               OpenGovernanceClient openGovernanceClient)
    {
        super(localServerName, auditLog, serviceName, openMetadataClient, OpenMetadataType.NOTIFICATION_TYPE.typeName);

        this.openGovernanceClient = openGovernanceClient;
    }



    /**
     * Return the list of resources linked to the supplied notification type.
     *
     * @param userId                 userId of user making request.
     * @param notificationTypeGUID unique identifier of the notification type
     * @param queryOptions options to control the query
     * @return list of resources to monitor
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<OpenMetadataRootElement> getMonitoredResources(String       userId,
                                                               String       notificationTypeGUID,
                                                               QueryOptions queryOptions) throws InvalidParameterException,
                                                                                                 PropertyServerException,
                                                                                                 UserNotAuthorizedException
    {
        final String methodName        = "getMonitoredResources";
        final String guidParameterName = "notificationTypeGUID";

        return super.getRelatedRootElements(userId,
                                            notificationTypeGUID,
                                            guidParameterName,
                                            1,
                                            OpenMetadataType.MONITORED_RESOURCE_RELATIONSHIP.typeName,
                                            queryOptions,
                                            methodName);
    }


    /**
     * Create a notification/action for each of the subscribers.
     *
     * @param userId caller's userId
     * @param subscriberGUID  unique identifier of the subscriber
     * @param properties properties of the action
     * @param notificationTypeGUID unique identifier of the cause for the action to be raised
     * @param requestParameters properties to pass to any governance action subscriber
     * @param actionRequesterGUID unique identifier of the source of the action
     * @param actionTargets the list of elements that should be acted upon
     * @return unique identifier of the action
     *
     * @throws InvalidParameterException the completion status is null
     * @throws UserNotAuthorizedException the governance action service is not authorized to update the governance action service status
     * @throws PropertyServerException there is a problem connecting to the metadata store
     */
    public String notifySubscriber(String                 userId,
                                   String                 subscriberGUID,
                                   NotificationProperties properties,
                                   String                 notificationTypeGUID,
                                   Map<String, String>    requestParameters,
                                   String                 actionRequesterGUID,
                                   List<NewActionTarget>  actionTargets) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        final String methodName = "notifySubscriber";

        OpenMetadataElement subscriber =  openMetadataClient.getMetadataElementByGUID(userId, subscriberGUID, new GetOptions());

        List<String> actionCauseGUIDs = List.of(notificationTypeGUID);

        if (propertyHelper.isTypeOf(subscriber, OpenMetadataType.ACTOR.typeName))
        {
            return openMetadataClient.createActorAction(userId,
                                                        properties.getTypeName(),
                                                        elementBuilder.getNewElementProperties(properties),
                                                        actionRequesterGUID,
                                                        actionCauseGUIDs,
                                                        subscriber.getElementGUID(),
                                                        actionTargets);
        }
        else if (propertyHelper.isTypeOf(subscriber, OpenMetadataType.NOTE_LOG.typeName))
        {
            return openMetadataClient.createNoteLogEntry(userId,
                                                         properties.getTypeName(),
                                                         elementBuilder.getNewElementProperties(properties),
                                                         actionRequesterGUID,
                                                         actionCauseGUIDs,
                                                         subscriber.getElementGUID(),
                                                         actionTargets);
        }
        else if (propertyHelper.isTypeOf(subscriber, OpenMetadataType.GOVERNANCE_ACTION.typeName))
        {
            String qualifiedName = propertyHelper.getStringProperty(localServiceName,
                                                                    OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                    subscriber.getElementProperties(),
                                                                    methodName);

            List<String> actionSourceGUIDs = List.of(actionRequesterGUID);

            if (propertyHelper.isTypeOf(subscriber, OpenMetadataType.GOVERNANCE_ACTION_PROCESS.typeName))
            {
                return openGovernanceClient.initiateGovernanceActionProcess(userId,
                                                                            qualifiedName,
                                                                            actionSourceGUIDs,
                                                                            actionCauseGUIDs,
                                                                            actionTargets,
                                                                            null,
                                                                            requestParameters,
                                                                            localServiceName,
                                                                            null);

            }
            else if (propertyHelper.isTypeOf(subscriber, OpenMetadataType.GOVERNANCE_ACTION_TYPE.typeName))
            {
                return openGovernanceClient.initiateGovernanceActionType(userId,
                                                                         qualifiedName,
                                                                         actionSourceGUIDs,
                                                                         actionCauseGUIDs,
                                                                         actionTargets,
                                                                         null,
                                                                         requestParameters,
                                                                         localServiceName,
                                                                         null);

            }
        }
        else
        {
            auditLog.logMessage(methodName, OWFAuditCode.WRONG_TYPE_OF_SUBSCRIBER.getMessageDefinition(subscriber.getElementGUID(),
                                                                                                       subscriber.getType().getTypeName(),
                                                                                                       localServiceName,
                                                                                                       List.of(OpenMetadataType.ACTOR.typeName, OpenMetadataType.GOVERNANCE_ACTION.typeName).toString()));
        }

        return null;
    }


    /**
     * Create a notification/action for each of the subscribers.
     *
     * @param userId caller's userId
     * @param properties properties of the action
     * @param notificationTypeGUID unique identifier of the cause for the action to be raised
     * @param requestParameters properties to pass to the next governance service
     * @param actionRequesterGUID unique identifier of the source of the action
     * @param actionTargets the list of elements that should be acted upon
     *
     * @throws InvalidParameterException the completion status is null
     * @throws UserNotAuthorizedException the governance action service is not authorized to update the governance action service status
     * @throws PropertyServerException there is a problem connecting to the metadata store
     */
    public void notifySubscribers(String                 userId,
                                  NotificationProperties properties,
                                  String                 notificationTypeGUID,
                                  Map<String, String>    requestParameters,
                                  String                 actionRequesterGUID,
                                  List<NewActionTarget>  actionTargets) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        final String methodName = "createAction";
        final String propertiesParameterName = "properties";

        propertyHelper.validateObject(properties, propertiesParameterName, methodName);

        List<OpenMetadataRootElement> subscribers = this.getNotificationSubscribers(userId,
                                                                                    notificationTypeGUID,
                                                                                    new QueryOptions());

        if (subscribers != null)
        {
            for (OpenMetadataRootElement subscriber : subscribers)
            {
                if (subscriber != null)
                {
                    notifySubscriber(userId,
                                     subscriber.getElementHeader().getGUID(),
                                     properties,
                                     notificationTypeGUID,
                                     requestParameters,
                                     actionRequesterGUID,
                                     actionTargets);
                }
            }
        }
    }


    /**
     * Return the list of subscribers linked to the supplied notification type.
     *
     * @param userId                 userId of user making request.
     * @param notificationTypeGUID unique identifier of the notification type
     * @param queryOptions options to control the query
     * @return list of resources to monitor
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<OpenMetadataRootElement> getNotificationSubscribers(String        userId,
                                                                    String notificationTypeGUID,
                                                                    QueryOptions queryOptions) throws InvalidParameterException,
                                                                                                      PropertyServerException,
                                                                                                      UserNotAuthorizedException
    {
        final String methodName        = "getNotificationSubscribers";
        final String guidParameterName = "notificationTypeGUID";

        return super.getRelatedRootElements(userId,
                                            notificationTypeGUID,
                                            guidParameterName,
                                            1,
                                            OpenMetadataType.NOTIFICATION_SUBSCRIBER_RELATIONSHIP.typeName,
                                            queryOptions,
                                            methodName);
    }
}
