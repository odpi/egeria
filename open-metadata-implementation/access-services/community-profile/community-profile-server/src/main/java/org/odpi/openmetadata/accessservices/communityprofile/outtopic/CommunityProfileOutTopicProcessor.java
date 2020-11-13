/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.outtopic;

import org.odpi.openmetadata.accessservices.communityprofile.ffdc.CommunityProfileAuditCode;
import org.odpi.openmetadata.accessservices.communityprofile.events.*;
import org.odpi.openmetadata.accessservices.communityprofile.properties.PersonalProfile;
import org.odpi.openmetadata.accessservices.communityprofile.properties.UserIdentity;
import org.odpi.openmetadata.accessservices.communityprofile.topics.CommunityProfileOutTopicPublisher;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.OpenMetadataTopicConnector;


/**
 * CommunityProfileOutTopicProcessor is responsible for publishing events about changes to personal profiles,
 * communities and related elements.  It is called when an interesting OMRS Event is added to the Enterprise
 * OMRS Topic (see CommunityProfileOMRSTopicProcessor).
 *
 * The actual sending of events is done by the super class CommunityProfileOutTopicPublisher.
 * This class logs a message to the OMRS Audit Log before calling the super class.
 */
public class CommunityProfileOutTopicProcessor extends CommunityProfileOutTopicPublisher
{
    private InvalidParameterHandler     invalidParameterHandler;
    private AuditLog                    auditLog;


    /**
     * The constructor is given the connection to the out topic for Community Profile OMAS
     * along with classes for testing and manipulating instances.
     *
     * @param connector initialized OpenMetadataTopicConnector object
     * @param invalidParameterHandler error handler
     * @param auditLog logging destination
     */
    public CommunityProfileOutTopicProcessor(OpenMetadataTopicConnector connector,
                                             InvalidParameterHandler    invalidParameterHandler,
                                             AuditLog                   auditLog)
    {
        super(connector, invalidParameterHandler);

        this.invalidParameterHandler = invalidParameterHandler;
        this.auditLog = auditLog;
    }


    /**
     * Log an audit log.
     *
     * @param actionDescription calling method
     * @param eventTypeName type of event
     * @param subjectGUID unique identifier for object that the event is about
     */
    private void logOutboundEvent(String                        actionDescription,
                                  String                        eventTypeName,
                                  String                        subjectGUID)
    {
        if (auditLog != null)
        {
            auditLog.logMessage(actionDescription, CommunityProfileAuditCode.OUTBOUND_EVENT.getMessageDefinition(eventTypeName, subjectGUID));
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

        logOutboundEvent(methodName,
                         CommunityProfileOutboundEventType.NEW_USER_IDENTITY_EVENT.getEventTypeName(),
                         bean.getGUID());

        super.sendNewUserIdentityEvent(bean);
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

        logOutboundEvent(methodName,
                         CommunityProfileOutboundEventType.NEW_REF_USER_IDENTITY_EVENT.getEventTypeName(),
                         bean.getGUID());

        super.sendNewExternalUserIdentityEvent(bean);
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

        logOutboundEvent(methodName,
                         CommunityProfileOutboundEventType.UPDATED_USER_IDENTITY_EVENT.getEventTypeName(),
                         bean.getGUID());

        super.sendUpdatedUserIdentityEvent(bean);
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

        logOutboundEvent(methodName,
                         CommunityProfileOutboundEventType.DELETED_USER_IDENTITY_EVENT.getEventTypeName(),
                         bean.getGUID());

        super.sendDeletedUserIdentityEvent(bean);
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

        logOutboundEvent(methodName,
                         CommunityProfileOutboundEventType.NEW_PERSONAL_PROFILE_EVENT.getEventTypeName(),
                         bean.getGUID());

        super.sendNewPersonalProfileEvent(bean);
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

        logOutboundEvent(methodName,
                         CommunityProfileOutboundEventType.NEW_REF_PERSONAL_PROFILE_EVENT.getEventTypeName(),
                         bean.getGUID());

        super.sendNewExternalPersonalProfileEvent(bean);
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

        logOutboundEvent(methodName,
                         CommunityProfileOutboundEventType.UPDATED_PERSONAL_PROFILE_EVENT.getEventTypeName(),
                         bean.getGUID());

        super.sendUpdatedPersonalProfileEvent(bean);
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

        logOutboundEvent(methodName,
                         CommunityProfileOutboundEventType.DELETED_PERSONAL_PROFILE_EVENT.getEventTypeName(),
                         bean.getGUID());

        super.sendDeletedPersonalProfileEvent(bean);
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

        logOutboundEvent(methodName,
                         CommunityProfileOutboundEventType.KARMA_POINT_PLATEAU_EVENT.getEventTypeName(),
                         bean.getGUID());

        super.sendKarmaPointPlateauEvent(bean, userId, plateau, totalPoints);
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

        logOutboundEvent(methodName,
                         CommunityProfileOutboundEventType.NEW_ASSET_IN_COLLECTION_EVENT.getEventTypeName(),
                         bean.getGUID());

        super.sendNewAssetInCollectionEvent(bean, userId, memberGUID, memberTypeName);
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

        logOutboundEvent(methodName,
                         CommunityProfileOutboundEventType.ASSET_REMOVED_FROM_COLLECTION_EVENT.getEventTypeName(),
                         bean.getGUID());

        super.sendAssetRemovedFromCollectionEvent(bean, userId, memberGUID, memberTypeName);
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

        logOutboundEvent(methodName,
                         CommunityProfileOutboundEventType.NEW_PROJECT_IN_COLLECTION_EVENT.getEventTypeName(),
                         bean.getGUID());

        super.sendNewProjectInCollectionEvent(bean, userId, memberGUID, memberTypeName);
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

        logOutboundEvent(methodName,
                         CommunityProfileOutboundEventType.PROJECT_REMOVED_FROM_COLLECTION_EVENT.getEventTypeName(),
                         bean.getGUID());

        super.sendProjectRemovedFromCollectionEvent(bean, userId, memberGUID, memberTypeName);
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

        logOutboundEvent(methodName,
                         CommunityProfileOutboundEventType.NEW_COMMUNITY_IN_COLLECTION_EVENT.getEventTypeName(),
                         bean.getGUID());

        super.sendNewCommunityInCollectionEvent(bean, userId, memberGUID, memberTypeName);
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

        logOutboundEvent(methodName,
                         CommunityProfileOutboundEventType.COMMUNITY_REMOVED_FROM_COLLECTION_EVENT.getEventTypeName(),
                         bean.getGUID());

        super.sendCommunityRemovedFromCollectionEvent(bean, userId, memberGUID, memberTypeName);
    }
}
