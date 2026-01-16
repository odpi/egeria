/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.myprofile.server;


import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.ActorProfileHandler;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.UserIdentityHandler;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.ActorProfileProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.UserIdentityProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.MakeAnchorOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.NewElementOptions;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.tokencontroller.TokenController;
import org.odpi.openmetadata.viewservices.myprofile.ffdc.MyProfileErrorCode;
import org.slf4j.LoggerFactory;


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
     *
     * @return profile response object or null or
     * InvalidParameterException the userId is null or invalid or
     * PropertyServerException a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public OpenMetadataRootElementResponse getMyProfile(String serverName)
    {
        final String methodName = "getMyProfile";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        OpenMetadataRootElementResponse response = new OpenMetadataRootElementResponse();
        AuditLog                        auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            ActorProfileHandler client = instanceHandler.getActorProfileHandler(userId, serverName, methodName);

            response.setElement(client.getActorProfileByUserId(userId, userId, null));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Return the list of actors linked to the user's profile.
     *
     * @param serverName name of the server instances for this request
     *
     * @return profile response object or null or
     * InvalidParameterException the userId is null or invalid or
     * PropertyServerException a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public OpenMetadataRootElementsResponse getMyActors(String         serverName,
                                                        GetRequestBody requestBody)
    {
        final String methodName = "getMyActors";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

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

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Return the list of user identities linked to the user's profile.
     *
     * @param serverName name of the server instances for this request
     *
     * @return profile response object or null or
     * InvalidParameterException the userId is null or invalid or
     * PropertyServerException a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public OpenMetadataRootElementsResponse getMyUserIdentities(String         serverName,
                                                                GetRequestBody requestBody)
    {
        final String methodName = "getMyUserIdentities";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

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

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Return the list of assigned roles linked to the user's profile.
     *
     * @param serverName name of the server instances for this request
     *
     * @return profile response object or null or
     * InvalidParameterException the userId is null or invalid or
     * PropertyServerException a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public OpenMetadataRootElementsResponse getMyRoles(String         serverName,
                                                       GetRequestBody requestBody)
    {
        final String methodName = "getMyRoles";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

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

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Return the list of assigned resources linked to the user's profile.
     *
     * @param serverName name of the server instances for this request
     *
     * @return profile response object or null or
     * InvalidParameterException the userId is null or invalid or
     * PropertyServerException a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public OpenMetadataRootElementsResponse getMyResources(String         serverName,
                                                           GetRequestBody requestBody)
    {
        final String methodName = "getMyResources";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog                         auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            ActorProfileHandler client = instanceHandler.getActorProfileHandler(userId, serverName, methodName);

            response.setElements(client.getResourcesByUserId(userId, userId, requestBody));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

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

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

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
                        newElementOptions.setParentAtEnd1(true);
                        newElementOptions.setParentRelationshipTypeName(OpenMetadataType.PROFILE_IDENTITY_RELATIONSHIP.typeName);

                        userIdentityHandler.createUserIdentity(userId, newElementOptions, null, userIdentityProperties, null);

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

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }
}
