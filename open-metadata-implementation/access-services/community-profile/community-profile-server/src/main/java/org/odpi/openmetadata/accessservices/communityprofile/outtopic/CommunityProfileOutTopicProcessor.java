/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.outtopic;

import org.odpi.openmetadata.accessservices.communityprofile.events.*;
import org.odpi.openmetadata.accessservices.communityprofile.properties.PersonalProfile;
import org.odpi.openmetadata.accessservices.communityprofile.properties.UserIdentity;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * CommunityProfilePublisher is responsible for publishing events about personalProfiles.  It is called
 * when an interesting OMRS Event is added to the Enterprise OMRS Topic.  It adds events to the Community Profile OMAS
 * out topic.
 */
public class CommunityProfilePublisher
{
    private static final String personalProfileTypeName                  = "PersonalProfile";


    private Connection              communityProfileOutTopic;
    private String                  serviceName;
    private OMRSAuditLog            auditLog;


    /**
     * The constructor is given the connection to the out topic for Community Profile OMAS
     * along with classes for testing and manipulating instances.
     *
     * @param communityProfileOutTopic  connection to the out topic
     * @param serviceName  name of this service
     * @param auditLog logging destination
     */
    public CommunityProfilePublisher(Connection              communityProfileOutTopic,
                                     String                  serviceName,
                                     OMRSAuditLog            auditLog)
    {
        this.communityProfileOutTopic = communityProfileOutTopic;
        this.serviceName = serviceName;
        this.auditLog = auditLog;
    }


    private void logOutboundEvent()
    {
        if (auditLog != null)
        {

        }
    }


    /**
     * Send an event to the Community Profile OMAS Out Topic.
     *
     * @param bean object to send
     * @throws InvalidParameterException no event provided
     * @throws ConnectorCheckedException unable to send the event due to connectivity issue
     */
    public void sendNewUserIdentityEvent(UserIdentity bean) throws InvalidParameterException,
                                                                   ConnectorCheckedException
    {
        final String  parameterName = "bean";
        final String  methodName = "sendNewUserIdentityEvent";

        invalidParameterHandler.validateObject(bean, parameterName, methodName);

        UserIdentityOutboundEvent event = new UserIdentityOutboundEvent();

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
