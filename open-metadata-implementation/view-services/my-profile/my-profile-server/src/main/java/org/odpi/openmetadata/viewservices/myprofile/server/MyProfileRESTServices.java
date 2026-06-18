/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.myprofile.server;


import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.ActorProfileHandler;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.AssetHandler;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.NoteLogHandler;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.UserIdentityHandler;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedMetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ClassificationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.ActorProfileProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.UserIdentityProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.actions.ActivityEntryProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.actions.BlogEntryProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.actions.JournalEntryProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.actions.NotificationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.feedback.NoteLogProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.security.ZoneMembershipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.GovernanceZoneName;
import org.odpi.openmetadata.frameworks.openmetadata.search.MakeAnchorOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.NewElementOptions;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.tokencontroller.TokenController;
import org.odpi.openmetadata.viewservices.myprofile.ffdc.MyProfileErrorCode;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


/**
 * The MyProfileRESTServices provides the server-side implementation of the My Profile Open Metadata
 * View Service (OMVS).  This interface provides access to a person's profile, roles and network.
 */
public class MyProfileRESTServices extends TokenController
{
    private static final MyProfileInstanceHandler instanceHandler = new MyProfileInstanceHandler();

    private static final RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();

    private static final RESTCallLogger restCallLogger = new RESTCallLogger(LoggerFactory.getLogger(MyProfileRESTServices.class),
                                                                            instanceHandler.getServiceName());
    /**
     * Default constructor
     */
    public MyProfileRESTServices()
    {
    }


    /**
     * Return the profile for this user.
     *
     * @param serverName name of the server instances for this request
     * @param requestBody optional properties to restrict search by and control how the results are formatted
     *
     * @return profile response object or null or
     * InvalidParameterException the userId is null or invalid or
     * PropertyServerException a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public OpenMetadataRootElementResponse getMyProfile(String         serverName,
                                                        GetRequestBody requestBody)
    {
        final String methodName = "getMyProfile";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        OpenMetadataRootElementResponse response = new OpenMetadataRootElementResponse();
        AuditLog                        auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            ActorProfileHandler client = instanceHandler.getActorProfileHandler(userId, serverName, methodName);

            response.setElement(client.getActorProfileByUserId(userId, userId, requestBody));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);

        return response;
    }


    /**
     * Return the list of actions that have been assigned to the user's profile, roles, or user identity.
     *
     * @param serverName     name of the server instances for this request
     * @param includeUserIds get actions for linked userIds
     * @param includeRoles   get actions for linked roles
     * @param requestBody    optional properties to restrict search by and control how the results are formatted
     * @return profile response object or null or
     * InvalidParameterException the userId is null or invalid or
     * PropertyServerException a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public OpenMetadataRootElementsResponse getMyAssignedActions(String                    serverName,
                                                                 boolean                   includeUserIds,
                                                                 boolean                   includeRoles,
                                                                 ActivityStatusRequestBody requestBody)
    {
        final String methodName = "getMyAssignedActions";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog                         auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            ActorProfileHandler client = instanceHandler.getActorProfileHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElements(client.getAssignedActionsByUserId(userId, userId, includeUserIds, includeRoles, requestBody.getActivityStatusList(), requestBody));
            }
            else
            {
                response.setElements(client.getAssignedActionsByUserId(userId, userId, includeUserIds, includeRoles, null, null));
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);

        return response;
    }


    /**
     * Return the list of actions linked to the user's profile, roles, or user identity that this user has sponsored.
     *
     * @param serverName     name of the server instances for this request
     * @param includeUserIds get actions for linked userIds
     * @param includeRoles   get actions for linked roles
     * @param requestBody    optional properties to restrict search by and control how the results are formatted
     * @return profile response object or null or
     * InvalidParameterException the userId is null or invalid or
     * PropertyServerException a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public OpenMetadataRootElementsResponse getMySponsoredActions(String                    serverName,
                                                                 boolean                   includeUserIds,
                                                                 boolean                   includeRoles,
                                                                 ActivityStatusRequestBody requestBody)
    {
        final String methodName = "getMySponsoredActions";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog                         auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            ActorProfileHandler client = instanceHandler.getActorProfileHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElements(client.getSponsoredActionsByUserId(userId, userId, includeUserIds, includeRoles, requestBody.getActivityStatusList(), requestBody));
            }
            else
            {
                response.setElements(client.getSponsoredActionsByUserId(userId, userId, includeUserIds, includeRoles, null, null));
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);

        return response;
    }


    /**
     * Return the list of actions linked to the user's profile, roles, or user identity that this user has sponsored.
     *
     * @param serverName     name of the server instances for this request
     * @param includeUserIds get actions for linked userIds
     * @param includeRoles   get actions for linked roles
     * @param requestBody    optional properties to restrict search by and control how the results are formatted
     * @return profile response object or null or
     * InvalidParameterException the userId is null or invalid or
     * PropertyServerException a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public OpenMetadataRootElementsResponse getMyRequestedActions(String                    serverName,
                                                                  boolean                   includeUserIds,
                                                                  boolean                   includeRoles,
                                                                  ActivityStatusRequestBody requestBody)
    {
        final String methodName = "getMyRequestedActions";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog                         auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            ActorProfileHandler client = instanceHandler.getActorProfileHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElements(client.getRequestedActionsByUserId(userId, userId, includeUserIds, includeRoles, requestBody.getActivityStatusList(), requestBody));
            }
            else
            {
                response.setElements(client.getRequestedActionsByUserId(userId, userId, includeUserIds, includeRoles, null, null));
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);

        return response;
    }


    /**
     * Return the list of actors (roles and userIds) linked to the user's profile.
     *
     * @param serverName name of the server instances for this request
     * @param requestBody    optional properties to restrict search by and control how the results are formatted
     *
     * @return profile response object or null or
     * InvalidParameterException the userId is null or invalid or
     * PropertyServerException a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public OpenMetadataRootElementsResponse getMyActors(String             serverName,
                                                        ResultsRequestBody requestBody)
    {
        final String methodName = "getMyActors";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog                         auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            ActorProfileHandler client = instanceHandler.getActorProfileHandler(userId, serverName, methodName);

            response.setElements(client.getActorsByUserId(userId, userId, true, true, requestBody));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);

        return response;
    }


    /**
     * Return the list of user identities linked to the user's profile.
     *
     * @param serverName name of the server instances for this request
     * @param requestBody    optional properties to restrict search by and control how the results are formatted
     *
     * @return profile response object or null or
     * InvalidParameterException the userId is null or invalid or
     * PropertyServerException a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public OpenMetadataRootElementsResponse getMyUserIdentities(String             serverName,
                                                                ResultsRequestBody requestBody)
    {
        final String methodName = "getMyUserIdentities";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog                         auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            ActorProfileHandler client = instanceHandler.getActorProfileHandler(userId, serverName, methodName);

            response.setElements(client.getActorsByUserId(userId, userId, true, false, requestBody));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);

        return response;
    }


    /**
     * Return the list of assigned roles linked to the user's profile.
     *
     * @param serverName name of the server instances for this request
     * @param requestBody    optional properties to restrict search by and control how the results are formatted
     *
     * @return profile response object or null or
     * InvalidParameterException the userId is null or invalid or
     * PropertyServerException a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public OpenMetadataRootElementsResponse getMyRoles(String             serverName,
                                                       ResultsRequestBody requestBody)
    {
        final String methodName = "getMyRoles";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog                         auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            ActorProfileHandler client = instanceHandler.getActorProfileHandler(userId, serverName, methodName);

            response.setElements(client.getActorsByUserId(userId, userId, false, true, requestBody));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);

        return response;
    }


    /**
     * Return the list of assigned resources linked to the user's profile.
     *
     * @param serverName name of the server instances for this request
     * @param includeUserIds get actions for linked userIds
     * @param includeRoles   get actions for linked roles
     * @param requestBody    optional properties to restrict search by and control how the results are formatted
     *
     * @return profile response object or null or
     * InvalidParameterException the userId is null or invalid or
     * PropertyServerException a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public OpenMetadataRootElementsResponse getMyResources(String             serverName,
                                                           boolean            includeUserIds,
                                                           boolean            includeRoles,
                                                           ResultsRequestBody requestBody)
    {
        final String methodName = "getMyResources";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog                         auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            ActorProfileHandler client = instanceHandler.getActorProfileHandler(userId, serverName, methodName);

            response.setElements(client.getResourcesByUserId(userId, userId, includeUserIds, includeRoles, requestBody));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);

        return response;
    }


    /**
     * Log details of an activity performed by the user.
     *
     * @param serverName name of the server instances for this request
     * @param requestBody properties for the profile
     *
     * @return unique identifier of new notification or
     * InvalidParameterException the userId is null or invalid or
     * PropertyServerException a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public GUIDResponse logMyActivity(String                   serverName,
                                      NewAttachmentRequestBody requestBody)
    {
        final String methodName = "logMyActivity";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            ActorProfileHandler actorProfileHandler = instanceHandler.getActorProfileHandler(userId, serverName, methodName);
            AssetHandler        assetHandler        = instanceHandler.getAssetHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                OpenMetadataRootElement actorProfile = actorProfileHandler.getActorProfileByUserId(userId, userId, null);

                if (actorProfile == null)
                {
                    throw new InvalidParameterException(MyProfileErrorCode.PROFILE_DOESNT_EXISTS.getMessageDefinition(userId),
                                                        this.getClass().getName(),
                                                        methodName,
                                                        OpenMetadataProperty.USER_ID.name);
                }
                else if (actorProfile.getProperties() instanceof ActorProfileProperties actorProfileProperties)
                {
                    String noteLogGUID = this.getNoteLogGUID(actorProfile,
                                                             actorProfileProperties.getDisplayName() + " Activities",
                                                             "Record of activities performed by " + actorProfileProperties.getDisplayName(),
                                                             userId,
                                                             false,
                                                             serverName);

                    if (requestBody.getProperties() instanceof NotificationProperties notificationProperties)
                    {
                        String notificationGUID = assetHandler.createNoteLogEntry(userId, null, new ActivityEntryProperties(notificationProperties), actorProfile.getElementHeader().getGUID(), null,  null, noteLogGUID, null);

                        response.setGUID(notificationGUID);
                    }
                    else
                    {
                        restExceptionHandler.handleInvalidPropertiesObject(NotificationProperties.class.getName(), methodName);
                    }
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(ActorProfileProperties.class.getName(), methodName);
                }
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);

        return response;
    }


    /**
     * Add the note log for this user.
     *
     * @param actorProfile profile of this user
     * @param noteLogName short name of note log
     * @param noteLogDescription description of note log
     * @param userId calling user
     * @param makePrivate should the note log be private?
     * @param serverName server name
     * @return unique identifier of the note log
     * @throws InvalidParameterException invalid parameter
     * @throws PropertyServerException repository error
     * @throws UserNotAuthorizedException not authorized
     */
    private String getNoteLogGUID(OpenMetadataRootElement actorProfile,
                                  String                  noteLogName,
                                  String                  noteLogDescription,
                                  String                  userId,
                                  boolean                 makePrivate,
                                  String                  serverName) throws InvalidParameterException,
                                                                             PropertyServerException,
                                                                             UserNotAuthorizedException
    {
        final String methodName = "getNoteLogGUID";

        String noteLogQName = OpenMetadataType.NOTE_LOG.typeName + "::" + actorProfile.getElementHeader().getGUID() + "::" + noteLogName;

        if (actorProfile.getNoteLogs() != null)
        {
            for (RelatedMetadataElementSummary noteLog : actorProfile.getNoteLogs())
            {
                if (noteLog.getRelatedElement().getProperties() instanceof NoteLogProperties noteLogProperties)
                {
                    if (noteLogQName.equals(noteLogProperties.getQualifiedName()))
                    {
                        return noteLog.getRelatedElement().getElementHeader().getGUID();
                    }
                }
            }
        }

        /*
         * The note log is not found, so create it.
         */
        NoteLogHandler noteLogHandler = instanceHandler.getNoteLogHandler(userId, serverName, methodName);

        NoteLogProperties noteLogProperties = new NoteLogProperties();

        noteLogProperties.setQualifiedName(noteLogQName);
        noteLogProperties.setDisplayName(noteLogName);

        Map<String, ClassificationProperties> initialClassifications = null;
        if (makePrivate)
        {
            ZoneMembershipProperties zoneMembershipProperties = new ZoneMembershipProperties();

            zoneMembershipProperties.setZoneMembership(Collections.singletonList(userId));

            initialClassifications = new HashMap<>();
            initialClassifications.put(OpenMetadataType.ZONE_MEMBERSHIP_CLASSIFICATION.typeName, zoneMembershipProperties);
        }

        return noteLogHandler.createNoteLog(userId,
                                            actorProfile.getElementHeader().getGUID(),
                                            null,
                                            initialClassifications,
                                            noteLogProperties);
    }


    /**
     * Add the supplied notification to the user's blog.
     *
     * @param serverName name of the server instances for this request
     * @param requestBody properties for the profile
     *
     * @return unique identifier of new notification or
     * InvalidParameterException the userId is null or invalid or
     * PropertyServerException a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public GUIDResponse blogMyActivity(String                   serverName,
                                       NewAttachmentRequestBody requestBody)
    {
        final String methodName = "blogMyActivity";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            ActorProfileHandler actorProfileHandler = instanceHandler.getActorProfileHandler(userId, serverName, methodName);
            AssetHandler        assetHandler        = instanceHandler.getAssetHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                OpenMetadataRootElement actorProfile = actorProfileHandler.getActorProfileByUserId(userId, userId, null);

                if (actorProfile == null)
                {
                    throw new InvalidParameterException(MyProfileErrorCode.PROFILE_DOESNT_EXISTS.getMessageDefinition(userId),
                                                        this.getClass().getName(),
                                                        methodName,
                                                        OpenMetadataProperty.USER_ID.name);
                }
                else if (actorProfile.getProperties() instanceof ActorProfileProperties actorProfileProperties)
                {
                    String noteLogGUID = this.getNoteLogGUID(actorProfile,
                                                             actorProfileProperties.getDisplayName() + " Blog",
                                                             "Personal blog by " + actorProfileProperties.getDisplayName(),
                                                             userId,
                                                             false,
                                                             serverName);

                    if (requestBody.getProperties() instanceof NotificationProperties notificationProperties)
                    {
                        String notificationGUID = assetHandler.createNoteLogEntry(userId, null, new BlogEntryProperties(notificationProperties), actorProfile.getElementHeader().getGUID(), null, null, noteLogGUID, null);

                        response.setGUID(notificationGUID);
                    }
                    else
                    {
                        restExceptionHandler.handleInvalidPropertiesObject(NotificationProperties.class.getName(), methodName);
                    }
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(ActorProfileProperties.class.getName(), methodName);
                }
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);

        return response;
    }


    /**
     * Add the supplied notification to the user's journal.
     *
     * @param serverName name of the server instances for this request
     * @param requestBody properties for the profile
     *
     * @return unique identifier of new notification or
     * InvalidParameterException the userId is null or invalid or
     * PropertyServerException a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public GUIDResponse journalMyActivity(String                   serverName,
                                          NewAttachmentRequestBody requestBody)
    {
        final String methodName = "journalMyActivity";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            ActorProfileHandler actorProfileHandler = instanceHandler.getActorProfileHandler(userId, serverName, methodName);
            AssetHandler        assetHandler        = instanceHandler.getAssetHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                OpenMetadataRootElement actorProfile = actorProfileHandler.getActorProfileByUserId(userId, userId, null);

                if (actorProfile == null)
                {
                    throw new InvalidParameterException(MyProfileErrorCode.PROFILE_DOESNT_EXISTS.getMessageDefinition(userId),
                                                        this.getClass().getName(),
                                                        methodName,
                                                        OpenMetadataProperty.USER_ID.name);
                }

                String noteLogGUID = this.getNoteLogGUID(actorProfile,
                                                         "MyJournal",
                                                         "My Private Journal",
                                                         userId,
                                                         true,
                                                         serverName);

                if (requestBody.getProperties() instanceof NotificationProperties notificationProperties)
                {
                    Map<String, ClassificationProperties> initialClassifications = new HashMap<>();
                    ZoneMembershipProperties zoneMembershipProperties = new ZoneMembershipProperties();
                    zoneMembershipProperties.setZoneMembership(Collections.singletonList(userId));
                    initialClassifications.put(OpenMetadataType.ZONE_MEMBERSHIP_CLASSIFICATION.typeName, zoneMembershipProperties);

                    String notificationGUID = assetHandler.createNoteLogEntry(userId, initialClassifications, new JournalEntryProperties(notificationProperties), actorProfile.getElementHeader().getGUID(), null, null, noteLogGUID, null);

                    response.setGUID(notificationGUID);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(NotificationProperties.class.getName(), methodName);
                }
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);

        return response;
    }


    /**
     * Add the profile for this user.
     *
     * @param serverName name of the server instances for this request
     * @param requestBody properties for the profile
     *
     * @return profile response object or null or
     * InvalidParameterException the userId is null or invalid or
     * PropertyServerException a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public GUIDResponse addMyProfile(String                serverName,
                                     NewElementRequestBody requestBody)
    {
        final String methodName = "addMyProfile";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        GUIDResponse response = new GUIDResponse();
        AuditLog                    auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            ActorProfileHandler actorProfileHandler = instanceHandler.getActorProfileHandler(userId, serverName, methodName);

            OpenMetadataRootElement actorProfile = actorProfileHandler.getActorProfileByUserId(userId, userId, null);

            if (actorProfile != null)
            {
                throw new InvalidParameterException(MyProfileErrorCode.PROFILE_ALREADY_EXISTS.getMessageDefinition(userId),
                                                    this.getClass().getName(),
                                                    methodName,
                                                    OpenMetadataProperty.USER_ID.name);
            }

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof ActorProfileProperties actorProfileProperties)
                {
                    UserIdentityHandler userIdentityHandler = instanceHandler.getUserIdentityHandler(userId, serverName, methodName);

                    String profileGUID = actorProfileHandler.createActorProfile(userId,
                                                                                requestBody,
                                                                                requestBody.getInitialClassifications(),
                                                                                actorProfileProperties,
                                                                                requestBody.getParentRelationshipProperties());

                    OpenMetadataRootElement userIdentity = userIdentityHandler.getUserIdentityByUserId(userId, userId, null);

                    if (userIdentity == null)
                    {
                        UserIdentityProperties userIdentityProperties = new UserIdentityProperties();

                        userIdentityProperties.setQualifiedName(actorProfileProperties.getQualifiedName() + "_userId");
                        userIdentityProperties.setUserId(userId);

                        NewElementOptions newElementOptions = new NewElementOptions(requestBody);

                        newElementOptions.setAnchorGUID(profileGUID);
                        newElementOptions.setParentGUID(profileGUID);
                        newElementOptions.setParentAtEnd1(true);
                        newElementOptions.setParentRelationshipTypeName(OpenMetadataType.PROFILE_IDENTITY_RELATIONSHIP.typeName);

                        ZoneMembershipProperties zoneMembershipProperties = new ZoneMembershipProperties();
                        ArrayList<String>        zoneMembership           = new ArrayList<>();

                        zoneMembership.add(userId); // Make userIdentity visible to the associated user
                        zoneMembership.add(GovernanceZoneName.SECURITY.getZoneName());
                        zoneMembershipProperties.setZoneMembership(zoneMembership);

                        Map<String, ClassificationProperties> initialClassifications = new HashMap<>();

                        initialClassifications.put(OpenMetadataType.ZONE_MEMBERSHIP_CLASSIFICATION.typeName, zoneMembershipProperties);

                        userIdentityHandler.createUserIdentity(userId, newElementOptions, initialClassifications, userIdentityProperties, null);

                        response.setGUID(profileGUID);
                    }
                    else
                    {
                        MakeAnchorOptions makeAnchorOptions = new MakeAnchorOptions(requestBody);

                        makeAnchorOptions.setMakeAnchor(true);

                        userIdentityHandler.linkIdentityToProfile(userId,
                                                                  userIdentity.getElementHeader().getGUID(),
                                                                  profileGUID,
                                                                  makeAnchorOptions,
                                                                  null);
                    }
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(ActorProfileProperties.class.getName(), methodName);
                }
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);

        return response;
    }
}
