/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.opengovernance.handlers;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.opengovernance.client.OpenGovernanceClient;
import org.odpi.openmetadata.frameworks.opengovernance.ffdc.OGFAuditCode;import org.odpi.openmetadata.frameworks.openmetadata.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.openmetadata.converters.OpenMetadataPropertyConverterBase;import org.odpi.openmetadata.frameworks.openmetadata.enums.ActivityStatus;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.AssetHandler;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.GovernanceDefinitionHandler;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementControlHeader;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.actions.NotificationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.NotificationSubscriberProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.security.ZoneMembershipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.AssignmentType;
import org.odpi.openmetadata.frameworks.openmetadata.search.*;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.*;


/**
 * NotificationHandler is the handler for managing notifications for notification types.
 */
public class NotificationHandler extends GovernanceDefinitionHandler
{
    private final OpenGovernanceClient              openGovernanceClient;
    private final AssetHandler                      assetHandler;
    private final OpenMetadataPropertyConverterBase openMetadataConverter;

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

        this.openGovernanceClient  = openGovernanceClient;
        this.assetHandler          = new AssetHandler(localServerName, auditLog, serviceName, openMetadataClient);
        this.openMetadataConverter = new OpenMetadataPropertyConverterBase(propertyHelper, serviceName);
    }



    /**
     * Return the list of resources linked to the supplied notification type.
     *
     * @param userId                 userId of user making request.
     * @param notificationTypeGUID unique identifier of the notification type
     * @param queryOptions options to control the query
     * @return list of resources to monitor
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the watchdog action service is not authorized to continue
     * @throws PropertyServerException    a problem connecting to the metadata store
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
                                            OpenMetadataType.REFERENCEABLE.typeName,
                                            queryOptions,
                                            methodName);
    }


    /**
     * Create a notification/action for the subscribers.  The caller determines if a notification is required.
     * This method determines if the subscriber is eligible to receive the notification.
     *
     * @param userId                         caller's userId
     * @param firstNotification              is this the first notification sent to this subscriber by this governance service instance?
     * @param outboundNotificationProperties properties of the action
     * @param notificationTypeGUID           unique identifier of the cause for the action to be raised
     * @param initialClassifications         initial classifications to add to the action
     * @param requestParameters              properties to pass to the next governance service
     * @param actionRequesterGUID            unique identifier of the source of the action
     * @param actionTargets                  the list of elements that should be acted upon
     * @param minimumNotificationInterval    minimum time between notifications
     * @param nextScheduledNotificationTime  next notification trigger time - either from the notification type or monitored resource activity
     * @param newSubscriberStatus            set the subscriber relationship to this value after a successful notification; null means leave it alone
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the watchdog action service is not authorized to continue
     * @throws PropertyServerException    a problem connecting to the metadata store
     */
    public void notifySubscribers(String                                userId,
                                  boolean                               firstNotification,
                                  Map<String, ClassificationProperties> initialClassifications,
                                  NotificationProperties                outboundNotificationProperties,
                                  String                                notificationTypeGUID,
                                  Map<String, String>                   requestParameters,
                                  String                                actionRequesterGUID,
                                  List<NewActionTarget>                 actionTargets,
                                  long                                  minimumNotificationInterval,
                                  Date                                  nextScheduledNotificationTime,
                                  ActivityStatus                        newSubscriberStatus) throws InvalidParameterException,
                                                                                                    UserNotAuthorizedException,
                                                                                                    PropertyServerException
    {
        final String methodName              = "notifySubscribers";
        final String propertiesParameterName = "outboundNotificationProperties";

        propertyHelper.validateObject(outboundNotificationProperties, propertiesParameterName, methodName);

        /*
         * First, notify all eligible subscribers
         */
        List<OpenMetadataRootElement> subscribers = this.getNotificationSubscribers(userId,
                                                                                    notificationTypeGUID,
                                                                                    new QueryOptions());

        if (subscribers != null)
        {
            for (OpenMetadataRootElement subscriber : subscribers)
            {
                if ((subscriber != null) &&
                        (subscriber.getRelatedBy() != null) &&
                        (subscriber.getRelatedBy().getRelationshipProperties() instanceof NotificationSubscriberProperties notificationSubscriberProperties))
                {
                    if ((! firstNotification) || (notificationSubscriberProperties.getLastNotification() == null))
                    {
                        notifySubscriber(userId,
                                         subscriber.getElementHeader().getGUID(),
                                         subscriber.getElementHeader(),
                                         subscriber.getRelatedBy().getRelationshipHeader().getGUID(),
                                         notificationSubscriberProperties,
                                         initialClassifications,
                                         outboundNotificationProperties,
                                         notificationTypeGUID,
                                         requestParameters,
                                         actionRequesterGUID,
                                         actionTargets,
                                         minimumNotificationInterval,
                                         nextScheduledNotificationTime,
                                         newSubscriberStatus);
                    }
                }
            }
        }
    }


    /**
     * Create a notification/action for the new subscriber.
     *
     * @param userId               caller's userId
     * @param subscriberGUID       unique identifier of the subscriber
     * @param outboundNotificationProperties           properties of the action
     * @param initialClassifications initial classifications to add to the action
     * @param notificationTypeGUID unique identifier of the cause for the action to be raised
     * @param requestParameters    properties to pass to any governance action subscriber
     * @param actionRequesterGUID  unique identifier of the source of the action
     * @param actionTargets        the list of elements that should be acted upon
     * @param minimumNotificationInterval    minimum time between notifications
     * @param newSubscriberStatus    set the subscriber relationship to this value after a successful notification; null means leave it alone
     * @return unique identifier of the action
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to continue
     * @throws PropertyServerException    a problem connecting to the metadata store
     */
    public String welcomeSubscriber(String                                userId,
                                    String                                subscriberGUID,
                                    Map<String, ClassificationProperties> initialClassifications,
                                    NotificationProperties                outboundNotificationProperties,
                                    String                                notificationTypeGUID,
                                    Map<String, String>                   requestParameters,
                                    String                                actionRequesterGUID,
                                    List<NewActionTarget>                 actionTargets,
                                    long                                  minimumNotificationInterval,
                                    ActivityStatus                        newSubscriberStatus) throws InvalidParameterException,
                                                                                                      UserNotAuthorizedException,
                                                                                               PropertyServerException
    {
        final String methodName = "welcomeSubscriber";

        try
        {
            OpenMetadataRelationshipList subscriberList = openMetadataClient.getMetadataElementRelationships(userId,
                                                                                                             notificationTypeGUID,
                                                                                                             subscriberGUID,
                                                                                                             OpenMetadataType.NOTIFICATION_SUBSCRIBER_RELATIONSHIP.typeName,
                                                                                                             null);

            if ((subscriberList != null) && (subscriberList.getRelationships() != null))
            {
                for (OpenMetadataRelationship subscriber : subscriberList.getRelationships())
                {
                    if (subscriber != null)
                    {
                        RelationshipBeanProperties subscriberProperties = openMetadataConverter.getRelationshipProperties(subscriber);

                        if (subscriberProperties instanceof NotificationSubscriberProperties notificationSubscriberProperties)
                        {
                            return this.notifySubscriber(userId,
                                                         subscriber.getElementGUIDAtEnd2(),
                                                         subscriber.getElementAtEnd2(),
                                                         subscriber.getRelationshipGUID(),
                                                         notificationSubscriberProperties,
                                                         initialClassifications,
                                                         outboundNotificationProperties,
                                                         notificationTypeGUID,
                                                         requestParameters,
                                                         actionRequesterGUID,
                                                         actionTargets,
                                                         minimumNotificationInterval,
                                                         null,
                                                         newSubscriberStatus);
                        }
                    }
                }
            }
        }
        catch (Exception error)
        {
            auditLog.logException(methodName,
                                  OGFAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(actionRequesterGUID,
                                                                                         error.getClass().getName(),
                                                                                         methodName,
                                                                                         error.getMessage()),
                                  error);
        }

        return null;
    }



    /**
     * Create a notification/action for an unsubscribed subscriber.
     *
     * @param userId               caller's userId
     * @param subscriberGUID       unique identifier of the subscriber
     * @param outboundNotificationProperties           properties of the action
     * @param initialClassifications initial classifications to add to the action
     * @param notificationTypeGUID unique identifier of the cause for the action to be raised
     * @param requestParameters    properties to pass to any governance action subscriber
     * @param actionRequesterGUID  unique identifier of the source of the action
     * @param actionTargets        the list of elements that should be acted upon
     * @return unique identifier of the action
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to continue
     * @throws PropertyServerException    a problem connecting to the metadata store
     */
    public String dismissSubscriber(String                                userId,
                                    String                                subscriberGUID,
                                    Map<String, ClassificationProperties> initialClassifications,
                                    NotificationProperties                outboundNotificationProperties,
                                    String                                notificationTypeGUID,
                                    Map<String, String>                   requestParameters,
                                    String                                actionRequesterGUID,
                                    List<NewActionTarget>                 actionTargets) throws InvalidParameterException,
                                                                                                UserNotAuthorizedException,
                                                                                                PropertyServerException
    {
        final String methodName = "dismissSubscriber";

        try
        {
            OpenMetadataRelationshipList subscriberList = openMetadataClient.getMetadataElementRelationships(userId,
                                                                                                             notificationTypeGUID,
                                                                                                             subscriberGUID,
                                                                                                             OpenMetadataType.NOTIFICATION_SUBSCRIBER_RELATIONSHIP.typeName,
                                                                                                             null);

            if ((subscriberList != null) && (subscriberList.getRelationships() != null))
            {
                for (OpenMetadataRelationship subscriber : subscriberList.getRelationships())
                {
                    if (subscriber != null)
                    {
                        RelationshipBeanProperties subscriberProperties = openMetadataConverter.getRelationshipProperties(subscriber);

                        if (subscriberProperties instanceof NotificationSubscriberProperties notificationSubscriberProperties)
                        {
                            return this.notifySubscriber(userId,
                                                         subscriber.getElementGUIDAtEnd2(),
                                                         subscriber.getElementAtEnd2(),
                                                         subscriber.getRelationshipGUID(),
                                                         notificationSubscriberProperties,
                                                         initialClassifications,
                                                         outboundNotificationProperties,
                                                         notificationTypeGUID,
                                                         requestParameters,
                                                         actionRequesterGUID,
                                                         actionTargets,
                                                         0,
                                                         new Date(0),
                                                         ActivityStatus.CANCELLED);
                        }
                    }
                }
            }
        }
        catch (Exception error)
        {
            auditLog.logException(methodName,
                                  OGFAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(actionRequesterGUID,
                                                                                         error.getClass().getName(),
                                                                                         methodName,
                                                                                         error.getMessage()),
                                  error);
        }

        return null;
    }


    /**
     * Create a notification/action for the supplied subscriber, providing it is in the IN_PROGRESS state and the
     * subscriber is of the appropriate type.
     *
     * @param userId               caller's userId
     * @param subscriberHeader           header of the subscriber
     * @param outboundNotificationProperties           properties of the action
     * @param initialClassifications initial classifications to add to the action
     * @param notificationTypeGUID unique identifier of the cause for the action to be raised
     * @param requestParameters    properties to pass to any governance action subscriber
     * @param actionRequesterGUID  unique identifier of the source of the action
     * @param actionTargets        the list of elements that should be acted upon
     * @param minimumNotificationInterval    minimum time between notifications
     * @param nextScheduledNotificationTime  next notification trigger time - either from the notification type or monitored resource activity
     * @param newSubscriberStatus    set the subscriber relationship to this value after a successful notification; null means leave it alone
     * @return unique identifier of the action
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to continue
     * @throws PropertyServerException    a problem connecting to the metadata store
     */
    private String notifySubscriber(String                                userId,
                                    String                                subscriberGUID,
                                    ElementControlHeader                  subscriberHeader,
                                    String                                notificationSubscriberGUID,
                                    NotificationSubscriberProperties      notificationSubscriberProperties,
                                    Map<String, ClassificationProperties> initialClassifications,
                                    NotificationProperties                outboundNotificationProperties,
                                    String                                notificationTypeGUID,
                                    Map<String, String>                   requestParameters,
                                    String                                actionRequesterGUID,
                                    List<NewActionTarget>                 actionTargets,
                                    long                                  minimumNotificationInterval,
                                    Date                                  nextScheduledNotificationTime,
                                    ActivityStatus                        newSubscriberStatus) throws InvalidParameterException,
                                                                                                UserNotAuthorizedException,
                                                                                                PropertyServerException
    {
        final String methodName = "notifySubscriber";

        /*
        * Check the conditions for sending a notification.  The notification is sent if the subscriber is in the
        * IN_PROGRESS state, and there has been enough time since the last notification.
         */
        if ((notificationSubscriberProperties != null) &&
                (ActivityStatus.IN_PROGRESS == notificationSubscriberProperties.getActivityStatus()))
        {
            /*
             * Subscriber is in a good state.  If it never has had a notification, or if it has been long enough since the last notification,
             * and if the next scheduled notification time is in the past, then send the notification.
             */
            if ((notificationSubscriberProperties.getLastNotification() == null) ||
                    ((System.currentTimeMillis() <= notificationSubscriberProperties.getLastNotification().getTime() + minimumNotificationInterval)) &&
                     (nextScheduledNotificationTime != null) &&
                     (new Date().after(nextScheduledNotificationTime)))
            {
                NotificationProperties subscriberNotificationProperties = new NotificationProperties(outboundNotificationProperties);

                subscriberNotificationProperties.setQualifiedName(outboundNotificationProperties.getQualifiedName() + "::" + subscriberGUID);

                /*
                 * Subscribers are called if the status is IN_PROGRESS.  This is the default value.
                 * Zone membership controls the visibility of the notification.
                 */
                List<String>   zoneMembership = null;

                if (notificationSubscriberProperties.getZoneMembership() != null)
                {
                    zoneMembership = notificationSubscriberProperties.getZoneMembership();
                }

                /*
                 * Combine the supplied classifications with the zone membership classification (if any).
                 */
                Map<String, ClassificationProperties> combinedClassifications = initialClassifications;

                if (zoneMembership != null)
                {
                    if (combinedClassifications == null)
                    {
                        combinedClassifications = new HashMap<>();
                    }

                    ZoneMembershipProperties zoneMembershipProperties = new ZoneMembershipProperties();
                    zoneMembershipProperties.setZoneMembership(zoneMembership);

                    combinedClassifications.put(OpenMetadataType.ZONE_MEMBERSHIP_CLASSIFICATION.typeName, zoneMembershipProperties);
                }

                /*
                 * Issue notification.  This depends on the type of subscriber.
                 */
                if (propertyHelper.isTypeOf(subscriberHeader, OpenMetadataType.ACTOR.typeName))
                {
                    return assetHandler.createActorAction(userId,
                                                          subscriberNotificationProperties.getTypeName(),
                                                          initialClassifications,
                                                          subscriberNotificationProperties,
                                                          actionRequesterGUID,
                                                          Collections.singletonList(notificationTypeGUID),
                                                          subscriberGUID,
                                                          AssignmentType.REVIEWER,
                                                          actionTargets);
                }
                else if (propertyHelper.isTypeOf(subscriberHeader, OpenMetadataType.NOTE_LOG.typeName))
                {
                    return assetHandler.createNoteLogEntry(userId,
                                                           subscriberNotificationProperties.getTypeName(),
                                                           initialClassifications,
                                                           subscriberNotificationProperties,
                                                           actionRequesterGUID,
                                                           Collections.singletonList(notificationTypeGUID),
                                                           subscriberGUID,
                                                           actionTargets);
                }
                else if (propertyHelper.isTypeOf(subscriberHeader, OpenMetadataType.GOVERNANCE_ACTION.typeName))
                {
                    if (propertyHelper.isTypeOf(subscriberHeader, OpenMetadataType.GOVERNANCE_ACTION_PROCESS.typeName))
                    {
                        return openGovernanceClient.initiateGovernanceActionProcess(userId,
                                                                                    subscriberNotificationProperties.getQualifiedName(),
                                                                                    Collections.singletonList(actionRequesterGUID),
                                                                                    Collections.singletonList(notificationTypeGUID),
                                                                                    actionTargets,
                                                                                    null,
                                                                                    requestParameters,
                                                                                    localServiceName,
                                                                                    null);

                    }
                    else if (propertyHelper.isTypeOf(subscriberHeader, OpenMetadataType.GOVERNANCE_ACTION_TYPE.typeName))
                    {
                        return openGovernanceClient.initiateGovernanceActionType(userId,
                                                                                 subscriberNotificationProperties.getQualifiedName(),
                                                                                 Collections.singletonList(actionRequesterGUID),
                                                                                 Collections.singletonList(notificationTypeGUID),
                                                                                 actionTargets,
                                                                                 null,
                                                                                 requestParameters,
                                                                                 localServiceName,
                                                                                 null);
                    }
                }
                else
                {
                    /*
                     * The subscriber is not supported by the notification manager, so set its status to INVALID.
                     */
                    auditLog.logMessage(methodName, OGFAuditCode.WRONG_TYPE_OF_SUBSCRIBER.getMessageDefinition(subscriberGUID,
                                                                                                               subscriberHeader.getType().getTypeName(),
                                                                                                               localServiceName,
                                                                                                               List.of(OpenMetadataType.ACTOR.typeName,
                                                                                                                       OpenMetadataType.GOVERNANCE_ACTION.typeName).toString()));
                    newSubscriberStatus = ActivityStatus.INVALID;
                }


                /*
                 * Record the lastNotification time.
                 * If the caller requests that the subscriber status be updated, also make the change.
                 */
                ElementProperties elementProperties = propertyHelper.addDateProperty(null,
                                                                                     OpenMetadataProperty.LAST_NOTIFICATION.name,
                                                                                     new Date());
                if (newSubscriberStatus != null)
                {
                    elementProperties = propertyHelper.addEnumProperty(null,
                                                                       OpenMetadataProperty.ACTIVITY_STATUS.name,
                                                                       ActivityStatus.getOpenTypeName(),
                                                                       newSubscriberStatus.getName());
                }

                UpdateOptions updateOptions = new UpdateOptions();
                updateOptions.setMergeUpdate(true);
                openMetadataClient.updateRelationshipInStore(userId,
                                                             notificationSubscriberGUID,
                                                             updateOptions,
                                                             elementProperties);
            }
        }

        return null;
    }


    /**
     * Return the list of subscribers linked to the supplied notification type.
     *
     * @param userId                 userId of user making request.
     * @param notificationTypeGUID unique identifier of the notification type
     * @param queryOptions options to control the query
     * @return list of resources to monitor
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<OpenMetadataRootElement> getNotificationSubscribers(String       userId,
                                                                    String       notificationTypeGUID,
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
                                            OpenMetadataType.REFERENCEABLE.typeName,
                                            queryOptions,
                                            methodName);
    }
}
