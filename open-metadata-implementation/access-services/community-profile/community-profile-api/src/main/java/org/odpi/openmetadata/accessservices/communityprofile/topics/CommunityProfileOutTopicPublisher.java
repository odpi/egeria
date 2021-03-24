/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.topics;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.odpi.openmetadata.accessservices.communityprofile.events.*;
import org.odpi.openmetadata.accessservices.communityprofile.ffdc.CommunityProfileErrorCode;
import org.odpi.openmetadata.accessservices.communityprofile.properties.PersonalProfile;
import org.odpi.openmetadata.accessservices.communityprofile.properties.UserIdentity;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.OpenMetadataTopicConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * CommunityProfileOutTopicPublisher sends events to the Community Profile OMAS Out Topic
 */
public abstract class CommunityProfileOutTopicPublisher
{
    private OpenMetadataTopicConnector  openMetadataTopicConnector;
    private InvalidParameterHandler     invalidParameterHandler;
    private ObjectMapper                objectMapper = new ObjectMapper();

    private static final Logger log = LoggerFactory.getLogger(CommunityProfileOutTopicPublisher.class);


    /**
     * Constructor to use the default JSON messages and the Open Metadata Topic Connector.
     *
     * @param connector initialized OpenMetadataTopicConnector object
     * @param invalidParameterHandler error handler
     */
    public CommunityProfileOutTopicPublisher(OpenMetadataTopicConnector connector,
                                             InvalidParameterHandler    invalidParameterHandler)
    {
        this.openMetadataTopicConnector = connector;
        this.invalidParameterHandler = invalidParameterHandler;
    }


    /**
     * Send an event to the Community Profile OMAS Out Topic.
     *
     * @param event event to send
     * @throws InvalidParameterException no event provided
     * @throws ConnectorCheckedException unable to send the event due to connectivity issue
     */
    private void sendEvent(CommunityProfileOutboundEvent event) throws InvalidParameterException,
                                                                       ConnectorCheckedException
    {
        final String  parameterName = "event";
        final String  methodName = "sendEvent";

        invalidParameterHandler.validateObject(event, parameterName, methodName);


        try
        {
            log.debug("Sending Event: " + event.getEventType().getEventTypeName());

            openMetadataTopicConnector.sendEvent(objectMapper.writeValueAsString(event));

            log.debug("Event sent: " + event.getEventType().getEventTypeName());
        }
        catch (ConnectorCheckedException error)
        {
            throw error;
        }
        catch (Exception  error)
        {
            String                    eventType = "<null>";

            if (event.getEventType() != null)
            {
                eventType = event.getEventType().getEventTypeName();
            }

            throw new InvalidParameterException(CommunityProfileErrorCode.PARSE_EVENT_ERROR.getMessageDefinition(eventType,
                                                                                                                 error.getClass().getName(),
                                                                                                                 error.getMessage(),
                                                                                                                 event.toString()),
                                                this.getClass().getName(),
                                                methodName,
                                                error,
                                                parameterName);
        }
    }


    /**
     * Send an event to the Community Profile OMAS Out Topic.
     *
     * @param bean object to send
     * @throws InvalidParameterException no event provided
     * @throws ConnectorCheckedException unable to send the event due to connectivity issue
     */
    public void sendNewUserIdentityEvent(UserIdentity  bean) throws InvalidParameterException,
                                                                    ConnectorCheckedException
    {
        final String  parameterName = "bean";
        final String  methodName = "sendNewUserIdentityEvent";

        invalidParameterHandler.validateObject(bean, parameterName, methodName);

        UserIdentityOutboundEvent  event = new UserIdentityOutboundEvent();

        event.setEventType(CommunityProfileOutboundEventType.NEW_USER_IDENTITY_EVENT);
        event.setUserIdentity(bean);

        this.sendEvent(event);
    }


    /**
     * Send an event to the Community Profile OMAS Out Topic.
     *
     * @param bean object to send
     * @throws InvalidParameterException no event provided
     * @throws ConnectorCheckedException unable to send the event due to connectivity issue
     */
    public void sendNewExternalUserIdentityEvent(UserIdentity  bean) throws InvalidParameterException,
                                                                            ConnectorCheckedException
    {
        final String  parameterName = "bean";
        final String  methodName = "sendNewExternalUserIdentityEvent";

        invalidParameterHandler.validateObject(bean, parameterName, methodName);

        UserIdentityOutboundEvent  event = new UserIdentityOutboundEvent();

        event.setEventType(CommunityProfileOutboundEventType.NEW_REF_USER_IDENTITY_EVENT);
        event.setUserIdentity(bean);

        this.sendEvent(event);
    }


    /**
     * Send an event to the Community Profile OMAS Out Topic.
     *
     * @param bean object to send
     * @throws InvalidParameterException no event provided
     * @throws ConnectorCheckedException unable to send the event due to connectivity issue
     */
    public void sendUpdatedUserIdentityEvent(UserIdentity  bean) throws InvalidParameterException,
                                                                        ConnectorCheckedException
    {
        final String  parameterName = "bean";
        final String  methodName = "sendUpdatedUserIdentityEvent";

        invalidParameterHandler.validateObject(bean, parameterName, methodName);

        UserIdentityOutboundEvent  event = new UserIdentityOutboundEvent();

        event.setEventType(CommunityProfileOutboundEventType.UPDATED_USER_IDENTITY_EVENT);
        event.setUserIdentity(bean);

        this.sendEvent(event);
    }


    /**
     * Send an event to the Community Profile OMAS Out Topic.
     *
     * @param bean object to send
     * @throws InvalidParameterException no event provided
     * @throws ConnectorCheckedException unable to send the event due to connectivity issue
     */
    public void sendDeletedUserIdentityEvent(UserIdentity  bean) throws InvalidParameterException,
                                                                        ConnectorCheckedException
    {
        final String  parameterName = "bean";
        final String  methodName = "sendDeletedUserIdentityEvent";

        invalidParameterHandler.validateObject(bean, parameterName, methodName);

        UserIdentityOutboundEvent  event = new UserIdentityOutboundEvent();

        event.setEventType(CommunityProfileOutboundEventType.DELETED_USER_IDENTITY_EVENT);
        event.setUserIdentity(bean);

        this.sendEvent(event);
    }


    /**
     * Send an event to the Community Profile OMAS Out Topic.
     *
     * @param bean object to send
     * @throws InvalidParameterException no event provided
     * @throws ConnectorCheckedException unable to send the event due to connectivity issue
     */
    public void sendNewPersonalProfileEvent(PersonalProfile bean) throws InvalidParameterException,
                                                                         ConnectorCheckedException
    {
        final String  parameterName = "bean";
        final String  methodName = "sendNewPersonalProfileEvent";

        invalidParameterHandler.validateObject(bean, parameterName, methodName);

        PersonalProfileOutboundEvent event = new PersonalProfileOutboundEvent();

        event.setEventType(CommunityProfileOutboundEventType.NEW_PERSONAL_PROFILE_EVENT);
        event.setPersonalProfile(bean);

        this.sendEvent(event);
    }


    /**
     * Send an event to the Community Profile OMAS Out Topic.
     *
     * @param bean object to send
     * @throws InvalidParameterException no event provided
     * @throws ConnectorCheckedException unable to send the event due to connectivity issue
     */
    public void sendNewExternalPersonalProfileEvent(PersonalProfile bean) throws InvalidParameterException,
                                                                                 ConnectorCheckedException
    {
        final String  parameterName = "bean";
        final String  methodName = "sendNewExternalPersonalProfileEvent";

        invalidParameterHandler.validateObject(bean, parameterName, methodName);

        PersonalProfileOutboundEvent event = new PersonalProfileOutboundEvent();

        event.setEventType(CommunityProfileOutboundEventType.NEW_REF_PERSONAL_PROFILE_EVENT);
        event.setPersonalProfile(bean);

        this.sendEvent(event);
    }


    /**
     * Send an event to the Community Profile OMAS Out Topic.
     *
     * @param bean object to send
     * @throws InvalidParameterException no event provided
     * @throws ConnectorCheckedException unable to send the event due to connectivity issue
     */
    public void sendUpdatedPersonalProfileEvent(PersonalProfile bean) throws InvalidParameterException,
                                                                             ConnectorCheckedException
    {
        final String  parameterName = "bean";
        final String  methodName = "sendUpdatedPersonalProfileEvent";

        invalidParameterHandler.validateObject(bean, parameterName, methodName);

        PersonalProfileOutboundEvent event = new PersonalProfileOutboundEvent();

        event.setEventType(CommunityProfileOutboundEventType.UPDATED_PERSONAL_PROFILE_EVENT);
        event.setPersonalProfile(bean);

        this.sendEvent(event);
    }


    /**
     * Send an event to the Community Profile OMAS Out Topic.
     *
     * @param bean object to send
     * @throws InvalidParameterException no event provided
     * @throws ConnectorCheckedException unable to send the event due to connectivity issue
     */
    public void sendDeletedPersonalProfileEvent(PersonalProfile bean) throws InvalidParameterException,
                                                                             ConnectorCheckedException
    {
        final String  parameterName = "bean";
        final String  methodName = "sendDeletedPersonalProfileEvent";

        invalidParameterHandler.validateObject(bean, parameterName, methodName);

        PersonalProfileOutboundEvent event = new PersonalProfileOutboundEvent();

        event.setEventType(CommunityProfileOutboundEventType.DELETED_PERSONAL_PROFILE_EVENT);
        event.setPersonalProfile(bean);

        this.sendEvent(event);
    }


    /**
     * Send an event to the Community Profile OMAS Out Topic.
     *
     * @param bean object to send
     * @param userId qualifying user identifier
     * @param plateau new plateau just achieved
     * @param totalPoints total number of karma points for this individual
     * @throws InvalidParameterException no event provided
     * @throws ConnectorCheckedException unable to send the event due to connectivity issue
     */
    public void sendKarmaPointPlateauEvent(PersonalProfile bean,
                                           String          userId,
                                           int             plateau,
                                           int             totalPoints) throws InvalidParameterException,
                                                                               ConnectorCheckedException
    {
        final String  parameterName = "bean";
        final String  methodName = "sendKarmaPointPlateauEvent";

        invalidParameterHandler.validateObject(bean, parameterName, methodName);

        KarmaPointOutboundEvent event = new KarmaPointOutboundEvent();

        event.setEventType(CommunityProfileOutboundEventType.KARMA_POINT_PLATEAU_EVENT);
        event.setPersonalProfile(bean);
        event.setUserId(userId);
        event.setPlateau(plateau);
        event.setPointsTotal(totalPoints);

        this.sendEvent(event);
    }


    /**
     * Send an event to the Community Profile OMAS Out Topic.
     *
     * @param bean object to send
     * @param userId calling user
     * @param memberGUID unique identifier of collection member
     * @param memberTypeName type of collection member
     * @throws InvalidParameterException no event provided
     * @throws ConnectorCheckedException unable to send the event due to connectivity issue
     */
    public void sendNewAssetInCollectionEvent(PersonalProfile bean,
                                              String          userId,
                                              String          memberGUID,
                                              String          memberTypeName) throws InvalidParameterException,
                                                                                     ConnectorCheckedException
    {
        final String  parameterName = "bean";
        final String  methodName = "sendNewAssetInCollectionEvent";

        invalidParameterHandler.validateObject(bean, parameterName, methodName);

        FavouriteCollectionOutboundEvent event = new FavouriteCollectionOutboundEvent(CommunityProfileOutboundEventType.NEW_ASSET_IN_COLLECTION_EVENT,
                                                                                      bean,
                                                                                      userId,
                                                                                      memberGUID,
                                                                                      memberTypeName);

        this.sendEvent(event);
    }


    /**
     * Send an event to the Community Profile OMAS Out Topic.
     *
     * @param bean object to send
     * @param userId calling user
     * @param memberGUID unique identifier of collection member
     * @param memberTypeName type of collection member
     * @throws InvalidParameterException no event provided
     * @throws ConnectorCheckedException unable to send the event due to connectivity issue
     */
    public void sendAssetRemovedFromCollectionEvent(PersonalProfile bean,
                                                    String          userId,
                                                    String          memberGUID,
                                                    String          memberTypeName) throws InvalidParameterException,
                                                                                           ConnectorCheckedException
    {
        final String  parameterName = "bean";
        final String  methodName = "sendAssetRemovedFromCollectionEvent";

        invalidParameterHandler.validateObject(bean, parameterName, methodName);

        FavouriteCollectionOutboundEvent event = new FavouriteCollectionOutboundEvent(CommunityProfileOutboundEventType.ASSET_REMOVED_FROM_COLLECTION_EVENT,
                                                                                      bean,
                                                                                      userId,
                                                                                      memberGUID,
                                                                                      memberTypeName);

        this.sendEvent(event);
    }


    /**
     * Send an event to the Community Profile OMAS Out Topic.
     *
     * @param bean object to send
     * @param userId calling user
     * @param memberGUID unique identifier of collection member
     * @param memberTypeName type of collection member
     * @throws InvalidParameterException no event provided
     * @throws ConnectorCheckedException unable to send the event due to connectivity issue
     */
    public void sendNewProjectInCollectionEvent(PersonalProfile bean,
                                                String          userId,
                                                String          memberGUID,
                                                String          memberTypeName) throws InvalidParameterException,
                                                                                       ConnectorCheckedException
    {
        final String  parameterName = "bean";
        final String  methodName = "sendNewProjectInCollectionEvent";

        invalidParameterHandler.validateObject(bean, parameterName, methodName);

        FavouriteCollectionOutboundEvent event = new FavouriteCollectionOutboundEvent(CommunityProfileOutboundEventType.NEW_PROJECT_IN_COLLECTION_EVENT,
                                                                                      bean,
                                                                                      userId,
                                                                                      memberGUID,
                                                                                      memberTypeName);

        this.sendEvent(event);
    }



    /**
     * Send an event to the Community Profile OMAS Out Topic.
     *
     * @param bean object to send
     * @param userId calling user
     * @param memberGUID unique identifier of collection member
     * @param memberTypeName type of collection member
     * @throws InvalidParameterException no event provided
     * @throws ConnectorCheckedException unable to send the event due to connectivity issue
     */
    public void sendProjectRemovedFromCollectionEvent(PersonalProfile bean,
                                                      String          userId,
                                                      String          memberGUID,
                                                      String          memberTypeName) throws InvalidParameterException,
                                                                                             ConnectorCheckedException
    {
        final String  parameterName = "bean";
        final String  methodName = "sendProjectRemovedFromCollectionEvent";

        invalidParameterHandler.validateObject(bean, parameterName, methodName);

        FavouriteCollectionOutboundEvent event = new FavouriteCollectionOutboundEvent(CommunityProfileOutboundEventType.PROJECT_REMOVED_FROM_COLLECTION_EVENT,
                                                                                      bean,
                                                                                      userId,
                                                                                      memberGUID,
                                                                                      memberTypeName);

        this.sendEvent(event);
    }


    /**
     * Send an event to the Community Profile OMAS Out Topic.
     *
     * @param bean object to send
     * @param userId calling user
     * @param memberGUID unique identifier of collection member
     * @param memberTypeName type of collection member
     * @throws InvalidParameterException no event provided
     * @throws ConnectorCheckedException unable to send the event due to connectivity issue
     */
    public void sendNewCommunityInCollectionEvent(PersonalProfile bean,
                                                  String          userId,
                                                  String          memberGUID,
                                                  String          memberTypeName) throws InvalidParameterException,
                                                                                         ConnectorCheckedException
    {
        final String  parameterName = "bean";
        final String  methodName = "sendNewCommunityInCollectionEvent";

        invalidParameterHandler.validateObject(bean, parameterName, methodName);

        FavouriteCollectionOutboundEvent event = new FavouriteCollectionOutboundEvent(CommunityProfileOutboundEventType.NEW_COMMUNITY_IN_COLLECTION_EVENT,
                                                                                      bean,
                                                                                      userId,
                                                                                      memberGUID,
                                                                                      memberTypeName);

        this.sendEvent(event);
    }



    /**
     * Send an event to the Community Profile OMAS Out Topic.
     *
     * @param bean object to send
     * @param userId calling user
     * @param memberGUID unique identifier of collection member
     * @param memberTypeName type of collection member
     * @throws InvalidParameterException no event provided
     * @throws ConnectorCheckedException unable to send the event due to connectivity issue
     */
    public void sendCommunityRemovedFromCollectionEvent(PersonalProfile bean,
                                                        String          userId,
                                                        String          memberGUID,
                                                        String          memberTypeName) throws InvalidParameterException,
                                                                                               ConnectorCheckedException
    {
        final String  parameterName = "bean";
        final String  methodName = "sendCommunityRemovedFromCollectionEvent";

        invalidParameterHandler.validateObject(bean, parameterName, methodName);

        FavouriteCollectionOutboundEvent event = new FavouriteCollectionOutboundEvent(CommunityProfileOutboundEventType.COMMUNITY_REMOVED_FROM_COLLECTION_EVENT,
                                                                                      bean,
                                                                                      userId,
                                                                                      memberGUID,
                                                                                      memberTypeName);

        this.sendEvent(event);
    }
}
