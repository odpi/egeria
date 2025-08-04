/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.actormanager.server;


import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.SupportingDefinitionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.locations.ProfileLocationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.security.SecurityGroupMembershipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.MetadataSourceOptions;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.ActorProfileHandler;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.ActorRoleHandler;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.UserIdentityHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.GetRequestBody;
import org.odpi.openmetadata.tokencontroller.TokenController;
import org.slf4j.LoggerFactory;

import java.util.Date;


/**
 * The ActorManagerRESTServices provides the server-side implementation of the Actor Manager Open Metadata
 * View Service (OMVS).  This interface provides access to a person's profile, roles and network.
 */
public class ActorManagerRESTServices extends TokenController
{
    private static final ActorManagerInstanceHandler instanceHandler = new ActorManagerInstanceHandler();

    private static final RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();

    private static final RESTCallLogger restCallLogger = new RESTCallLogger(LoggerFactory.getLogger(ActorManagerRESTServices.class),
                                                                            instanceHandler.getServiceName());

    /**
     * Default constructor
     */
    public ActorManagerRESTServices()
    {
    }


    /**
     * Create an actor profile.
     *
     * @param serverName                 name of called server.
     * @param viewServiceURLMarker  view service URL marker
     * @param requestBody             properties for the actor profile.
     *
     * @return unique identifier of the newly created element
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public GUIDResponse createActorProfile(String                serverName,
                                           String                viewServiceURLMarker,
                                           NewElementRequestBody requestBody)
    {
        final String methodName = "createActorProfile";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                ActorProfileHandler handler = instanceHandler.getActorProfileHandler(userId, serverName, viewServiceURLMarker, methodName);

                if (requestBody.getProperties() instanceof ActorProfileProperties actorProfileProperties)
                {
                    response.setGUID(handler.createActorProfile(userId,
                                                                requestBody,
                                                                requestBody.getInitialClassifications(),
                                                                actorProfileProperties,
                                                                requestBody.getParentRelationshipProperties()));
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


    /**
     * Create a new metadata element to represent an actor profile using an existing metadata element as a template.
     * The template defines additional classifications and relationships that should be added to the new element.
     *
     * @param serverName             calling user
     * @param viewServiceURLMarker  view service URL marker
     * @param requestBody properties that override the template
     *
     * @return unique identifier of the new metadata element
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GUIDResponse createActorProfileFromTemplate(String              serverName,
                                                       String              viewServiceURLMarker,
                                                       TemplateRequestBody requestBody)
    {
        final String methodName = "createActorProfileFromTemplate";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                ActorProfileHandler handler = instanceHandler.getActorProfileHandler(userId, serverName, viewServiceURLMarker, methodName);

                response.setGUID(handler.createActorProfileFromTemplate(userId,
                                                                        requestBody,
                                                                        requestBody.getTemplateGUID(),
                                                                        requestBody.getReplacementProperties(),
                                                                        requestBody.getPlaceholderPropertyValues(),
                                                                        requestBody.getParentRelationshipProperties()));
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


    /**
     * Update the properties of an actor profile.
     *
     * @param serverName         name of called server.
     * @param viewServiceURLMarker  view service URL marker
     * @param actorProfileGUID unique identifier of the actor profile (returned from create)
     * @param requestBody     properties for the new element.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse updateActorProfile(String                   serverName,
                                           String                   viewServiceURLMarker,
                                           String                   actorProfileGUID,
                                           UpdateElementRequestBody requestBody)
    {
        final String methodName = "updateActorProfile";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                ActorProfileHandler handler = instanceHandler.getActorProfileHandler(userId, serverName, viewServiceURLMarker, methodName);

                if (requestBody.getProperties() instanceof ActorProfileProperties actorProfileProperties)
                {
                    handler.updateActorProfile(userId,
                                               actorProfileGUID,
                                               requestBody,
                                               actorProfileProperties);
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


    /**
     * Attach a profile to a location.
     *
     * @param serverName         name of called server
     * @param viewServiceURLMarker  view service URL marker
     * @param actorProfileGUID       unique identifier of the actor profile
     * @param locationGUID           unique identifier of the location
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse linkLocationToProfile(String                  serverName,
                                              String                  viewServiceURLMarker,
                                              String                  actorProfileGUID,
                                              String                  locationGUID,
                                              NewRelationshipRequestBody requestBody)
    {
        final String methodName = "linkLocationToProfile";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            ActorProfileHandler handler = instanceHandler.getActorProfileHandler(userId, serverName, viewServiceURLMarker, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof ProfileLocationProperties profileLocationProperties)
                {
                    handler.linkLocationToProfile(userId,
                                                  actorProfileGUID,
                                                  locationGUID,
                                                  requestBody,
                                                  profileLocationProperties);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(ProfileLocationProperties.class.getName(), methodName);
                }
            }
            else
            {
                MetadataSourceOptions metadataSourceOptions = new MetadataSourceOptions();
                metadataSourceOptions.setEffectiveTime(new Date());

                handler.linkLocationToProfile(userId,
                                              actorProfileGUID,
                                              locationGUID,
                                              metadataSourceOptions,
                                              null);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Detach an actor profile from a location.
     *
     * @param serverName         name of called server
     * @param viewServiceURLMarker  view service URL marker
     * @param actorProfileGUID       unique identifier of the actor profile
     * @param locationGUID           unique identifier of the location
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse detachLocationFromProfile(String                   serverName,
                                                  String                   viewServiceURLMarker,
                                                  String                   actorProfileGUID,
                                                  String                   locationGUID,
                                                  DeleteRequestBody requestBody)
    {
        final String methodName = "detachLocationFromProfile";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ActorProfileHandler handler = instanceHandler.getActorProfileHandler(userId, serverName, viewServiceURLMarker, methodName);

            handler.detachLocationFromProfile(userId, actorProfileGUID, locationGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Attach a person profile to one of its peers.
     *
     * @param serverName         name of called server
     * @param viewServiceURLMarker  view service URL marker
     * @param personOneGUID unique identifier of the first actor profile
     * @param personTwoGUID unique identifier of the second actor profile
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse linkPeerPerson(String                  serverName,
                                       String                  viewServiceURLMarker,
                                       String                  personOneGUID,
                                       String                  personTwoGUID,
                                       NewRelationshipRequestBody requestBody)
    {
        final String methodName = "linkPeerPerson";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            ActorProfileHandler handler = instanceHandler.getActorProfileHandler(userId, serverName, viewServiceURLMarker, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof PeerProperties peerProperties)
                {
                    handler.linkPeerPerson(userId,
                                           personOneGUID,
                                           personTwoGUID,
                                           requestBody,
                                           peerProperties);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(PeerProperties.class.getName(), methodName);
                }
            }
            else
            {
                MetadataSourceOptions metadataSourceOptions = new MetadataSourceOptions();
                metadataSourceOptions.setEffectiveTime(new Date());

                handler.linkPeerPerson(userId,
                                       personOneGUID,
                                       personTwoGUID,
                                       metadataSourceOptions,
                                       null);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Detach a person profile from one of its peers.
     *
     * @param serverName         name of called server
     * @param viewServiceURLMarker  view service URL marker
     * @param personOneGUID          unique identifier of the first person profile
     * @param personTwoGUID          unique identifier of the second person profile
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse detachPeerPerson(String                   serverName,
                                         String                   viewServiceURLMarker,
                                         String                   personOneGUID,
                                         String                   personTwoGUID,
                                         DeleteRequestBody requestBody)
    {
        final String methodName = "detachPeerPerson";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ActorProfileHandler handler = instanceHandler.getActorProfileHandler(userId, serverName, viewServiceURLMarker, methodName);

            handler.detachPeerPerson(userId, personOneGUID, personTwoGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Attach a super team to a subteam.
     *
     * @param serverName         name of called server
     * @param viewServiceURLMarker  view service URL marker
     * @param superTeamGUID          unique identifier of the super team
     * @param subteamGUID            unique identifier of the subteam
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse linkTeamStructure(String                  serverName,
                                          String                  viewServiceURLMarker,
                                          String                  superTeamGUID,
                                          String                  subteamGUID,
                                          NewRelationshipRequestBody requestBody)
    {
        final String methodName = "linkTeamStructure";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            ActorProfileHandler handler = instanceHandler.getActorProfileHandler(userId, serverName, viewServiceURLMarker, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof TeamStructureProperties teamStructureProperties)
                {
                    handler.linkTeamStructure(userId,
                                              superTeamGUID,
                                              subteamGUID,
                                              requestBody,
                                              teamStructureProperties);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(TeamStructureProperties.class.getName(), methodName);
                }
            }
            else
            {
                MetadataSourceOptions metadataSourceOptions = new MetadataSourceOptions();
                metadataSourceOptions.setEffectiveTime(new Date());

                handler.linkTeamStructure(userId,
                                          superTeamGUID,
                                          subteamGUID,
                                          metadataSourceOptions,
                                          null);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Detach a super team from a subteam.
     *
     * @param serverName         name of called server
     * @param viewServiceURLMarker  view service URL marker
     * @param superTeamGUID          unique identifier of the super team
     * @param subteamGUID            unique identifier of the subteam
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse detachTeamStructure(String                   serverName,
                                            String                   viewServiceURLMarker,
                                            String                   superTeamGUID,
                                            String                   subteamGUID,
                                            DeleteRequestBody requestBody)
    {
        final String methodName = "detachTeamStructure";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ActorProfileHandler handler = instanceHandler.getActorProfileHandler(userId, serverName, viewServiceURLMarker, methodName);

            handler.detachTeamStructure(userId, superTeamGUID, subteamGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Attach an asset to an IT profile.
     *
     * @param serverName         name of called server
     * @param viewServiceURLMarker  view service URL marker
     * @param assetGUID       unique identifier of the asset
     * @param itProfileGUID            unique identifier of the IT profile
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse linkAssetToProfile(String                  serverName,
                                           String                  viewServiceURLMarker,
                                           String                  assetGUID,
                                           String                  itProfileGUID,
                                           NewRelationshipRequestBody requestBody)
    {
        final String methodName = "linkAssetToProfile";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            ActorProfileHandler handler = instanceHandler.getActorProfileHandler(userId, serverName, viewServiceURLMarker, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof ITInfrastructureProfileProperties itInfrastructureProfileProperties)
                {
                    handler.linkAssetToProfile(userId,
                                               assetGUID,
                                               itProfileGUID,
                                               requestBody,
                                               itInfrastructureProfileProperties);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(ITInfrastructureProfileProperties.class.getName(), methodName);
                }
            }
            else
            {
                MetadataSourceOptions metadataSourceOptions = new MetadataSourceOptions();
                metadataSourceOptions.setEffectiveTime(new Date());

                handler.linkAssetToProfile(userId,
                                           assetGUID,
                                           itProfileGUID,
                                           metadataSourceOptions,
                                           null);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Detach an asset from an IT profile.
     *
     * @param serverName         name of called server
     * @param viewServiceURLMarker  view service URL marker
     * @param assetGUID       unique identifier of the asset
     * @param itProfileGUID            unique identifier of the IT profile
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse detachAssetFromProfile(String                   serverName,
                                               String                   viewServiceURLMarker,
                                               String                   assetGUID,
                                               String                   itProfileGUID,
                                               DeleteRequestBody requestBody)
    {
        final String methodName = "detachAssetFromProfile";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ActorProfileHandler handler = instanceHandler.getActorProfileHandler(userId, serverName, viewServiceURLMarker, methodName);

            handler.detachAssetFromProfile(userId, assetGUID, itProfileGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Attach a team to its membership role.
     *
     * @param serverName         name of called server
     * @param viewServiceURLMarker  view service URL marker
     * @param teamGUID               unique identifier of the team
     * @param personRoleGUID         unique identifier of the associated person role
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse linkTeamToMembershipRole(String                  serverName,
                                                 String                  viewServiceURLMarker,
                                                 String                  teamGUID,
                                                 String                  personRoleGUID,
                                                 NewRelationshipRequestBody requestBody)
    {
        final String methodName = "linkTeamToMembershipRole";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            ActorProfileHandler handler = instanceHandler.getActorProfileHandler(userId, serverName, viewServiceURLMarker, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof TeamMembershipProperties teamMembershipProperties)
                {
                    handler.linkTeamToMembershipRole(userId,
                                                     teamGUID,
                                                     personRoleGUID,
                                                     requestBody,
                                                     teamMembershipProperties);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(TeamMembershipProperties.class.getName(), methodName);
                }
            }
            else
            {
                MetadataSourceOptions metadataSourceOptions = new MetadataSourceOptions();
                metadataSourceOptions.setEffectiveTime(new Date());

                handler.linkTeamToMembershipRole(userId,
                                                 teamGUID,
                                                 personRoleGUID,
                                                 metadataSourceOptions,
                                                 null);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Detach a team profile from its membership role.
     *
     * @param serverName         name of called server
     * @param viewServiceURLMarker  view service URL marker
     * @param teamGUID               unique identifier of the team
     * @param personRoleGUID         unique identifier of the associated person role
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse detachTeamFromMembershipRole(String                   serverName,
                                                     String                   viewServiceURLMarker,
                                                     String                   teamGUID,
                                                     String                   personRoleGUID,
                                                     DeleteRequestBody requestBody)
    {
        final String methodName = "detachTeamFromMembershipRole";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ActorProfileHandler handler = instanceHandler.getActorProfileHandler(userId, serverName, viewServiceURLMarker, methodName);

            handler.detachTeamFromMembershipRole(userId, teamGUID, personRoleGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Attach a team to its leadership role.
     *
     * @param serverName         name of called server
     * @param viewServiceURLMarker  view service URL marker
     * @param teamGUID               unique identifier of the team
     * @param personRoleGUID         unique identifier of the associated person role
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse linkTeamToLeadershipRole(String                  serverName,
                                                 String                  viewServiceURLMarker,
                                                 String                  teamGUID,
                                                 String                  personRoleGUID,
                                                 NewRelationshipRequestBody requestBody)
    {
        final String methodName = "attachSupportingDefinition";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            ActorProfileHandler handler = instanceHandler.getActorProfileHandler(userId, serverName, viewServiceURLMarker, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof TeamLeadershipProperties teamLeadershipProperties)
                {
                    handler.linkTeamToLeadershipRole(userId,
                                                     teamGUID,
                                                     personRoleGUID,
                                                     requestBody,
                                                     teamLeadershipProperties);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(TeamLeadershipProperties.class.getName(), methodName);
                }
            }
            else
            {
                MetadataSourceOptions metadataSourceOptions = new MetadataSourceOptions();
                metadataSourceOptions.setEffectiveTime(new Date());

                handler.linkTeamToLeadershipRole(userId,
                                                 teamGUID,
                                                 personRoleGUID,
                                                 metadataSourceOptions,
                                                 null);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Detach a team profile from its leadership role.
     *
     * @param serverName         name of called server
     * @param viewServiceURLMarker  view service URL marker
     * @param teamGUID               unique identifier of the team
     * @param personRoleGUID         unique identifier of the associated person role
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse detachTeamFromLeadershipRole(String                   serverName,
                                                     String                   viewServiceURLMarker,
                                                     String                   teamGUID,
                                                     String                   personRoleGUID,
                                                     DeleteRequestBody requestBody)
    {
        final String methodName = "detachTeamFromLeadershipRole";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ActorProfileHandler handler = instanceHandler.getActorProfileHandler(userId, serverName, viewServiceURLMarker, methodName);

            handler.detachTeamFromLeadershipRole(userId, teamGUID, personRoleGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Delete an actor profile.
     *
     * @param serverName         name of called server
     * @param viewServiceURLMarker  view service URL marker
     * @param actorProfileGUID  unique identifier of the element to delete
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse deleteActorProfile(String                   serverName,
                                           String                   viewServiceURLMarker,
                                           String                   actorProfileGUID,
                                           DeleteRequestBody requestBody)
    {
        final String methodName = "deleteActorProfile";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ActorProfileHandler handler = instanceHandler.getActorProfileHandler(userId, serverName, viewServiceURLMarker, methodName);

            handler.deleteActorProfile(userId, actorProfileGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Retrieve the list of actor profile metadata elements that contain the search string.
     *
     * @param serverName name of the service to route the request to
     * @param viewServiceURLMarker  view service URL marker
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public OpenMetadataRootElementsResponse getActorProfilesByName(String            serverName,
                                                                   String            viewServiceURLMarker,
                                                                   FilterRequestBody requestBody)
    {
        final String methodName = "getActorProfilesByName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog                         auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ActorProfileHandler handler = instanceHandler.getActorProfileHandler(userId, serverName, viewServiceURLMarker, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.getActorProfilesByName(userId, requestBody.getFilter(), requestBody));
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


    /**
     * Retrieve the list of actor profile metadata elements that contain the search string.
     *
     * @param serverName name of the service to route the request to
     * @param viewServiceURLMarker  view service URL marker
     * @param actorProfileGUID    unique identifier of the required element
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public OpenMetadataRootElementResponse getActorProfileByGUID(String             serverName,
                                                                 String             viewServiceURLMarker,
                                                                 String             actorProfileGUID,
                                                                 GetRequestBody requestBody)
    {
        final String methodName = "getActorProfileByGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        OpenMetadataRootElementResponse response = new OpenMetadataRootElementResponse();
        AuditLog                         auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ActorProfileHandler handler = instanceHandler.getActorProfileHandler(userId, serverName, viewServiceURLMarker, methodName);

            response.setElement(handler.getActorProfileByGUID(userId, actorProfileGUID, requestBody));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Retrieve the list of actor profile metadata elements that contain the search string.
     *
     * @param serverName name of the service to route the request to
     * @param viewServiceURLMarker  view service URL marker
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public OpenMetadataRootElementsResponse findActorProfiles(String                  serverName,
                                                              String                  viewServiceURLMarker,
                                                              SearchStringRequestBody requestBody)
    {
        final String methodName = "findActorProfiles";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog                         auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ActorProfileHandler handler = instanceHandler.getActorProfileHandler(userId, serverName, viewServiceURLMarker, methodName);

            response.setElements(handler.findActorProfiles(userId, requestBody.getSearchString(), requestBody));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Create an actor role.
     *
     * @param serverName                 name of called server.
     * @param viewServiceURLMarker  view service URL marker
     * @param requestBody             properties for the actor role.
     *
     * @return unique identifier of the newly created element
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public GUIDResponse createActorRole(String                serverName,
                                        String                viewServiceURLMarker,
                                        NewElementRequestBody requestBody)
    {
        final String methodName = "createActorRole";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                ActorRoleHandler handler = instanceHandler.getActorRoleHandler(userId, serverName, viewServiceURLMarker, methodName);

                if (requestBody.getProperties() instanceof ActorRoleProperties actorRoleProperties)
                {
                    response.setGUID(handler.createActorRole(userId,
                                                             requestBody,
                                                             requestBody.getInitialClassifications(),
                                                             actorRoleProperties,
                                                             requestBody.getParentRelationshipProperties()));
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(ActorRoleProperties.class.getName(), methodName);
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


    /**
     * Create a new metadata element to represent an actor role using an existing metadata element as a template.
     * The template defines additional classifications and relationships that should be added to the new element.
     *
     * @param serverName             calling user
     * @param viewServiceURLMarker  view service URL marker
     * @param requestBody properties that override the template
     *
     * @return unique identifier of the new metadata element
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GUIDResponse createActorRoleFromTemplate(String              serverName,
                                                    String              viewServiceURLMarker,
                                                    TemplateRequestBody requestBody)
    {
        final String methodName = "createActorRoleFromTemplate";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                ActorRoleHandler handler = instanceHandler.getActorRoleHandler(userId, serverName, viewServiceURLMarker, methodName);

                response.setGUID(handler.createActorRoleFromTemplate(userId,
                                                                     requestBody,
                                                                     requestBody.getTemplateGUID(),
                                                                     requestBody.getReplacementProperties(),
                                                                     requestBody.getPlaceholderPropertyValues(),
                                                                     requestBody.getParentRelationshipProperties()));
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


    /**
     * Update the properties of an actor role.
     *
     * @param serverName         name of called server.
     * @param viewServiceURLMarker  view service URL marker
     * @param actorRoleGUID unique identifier of the actor role (returned from create)
     * @param requestBody     properties for the new element.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse updateActorRole(String                   serverName,
                                        String                   viewServiceURLMarker,
                                        String                   actorRoleGUID,
                                        UpdateElementRequestBody requestBody)
    {
        final String methodName = "updateActorRole";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                ActorRoleHandler handler = instanceHandler.getActorRoleHandler(userId, serverName, viewServiceURLMarker, methodName);

                if (requestBody.getProperties() instanceof ActorRoleProperties actorRoleProperties)
                {
                    handler.updateActorRole(userId,
                                            actorRoleGUID,
                                            requestBody,
                                            actorRoleProperties);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(ActorRoleProperties.class.getName(), methodName);
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


    /**
     * Attach a team role to a team profile.
     *
     * @param serverName         name of called server
     * @param viewServiceURLMarker  view service URL marker
     * @param personRoleGUID       unique identifier of the person role
     * @param personProfileGUID            unique identifier of the person profile
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse linkPersonRoleToProfile(String                  serverName,
                                                String                  viewServiceURLMarker,
                                                String                  personRoleGUID,
                                                String                  personProfileGUID,
                                                NewRelationshipRequestBody requestBody)
    {
        final String methodName = "linkPersonRoleToProfile";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            ActorRoleHandler handler = instanceHandler.getActorRoleHandler(userId, serverName, viewServiceURLMarker, methodName);

            if (requestBody != null)
            {

                if (requestBody.getProperties() instanceof PersonRoleAppointmentProperties peerDefinitionProperties)
                {
                    handler.linkPersonRoleToProfile(userId,
                                                    personRoleGUID,
                                                    personProfileGUID,
                                                    requestBody,
                                                    peerDefinitionProperties);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(PersonRoleAppointmentProperties.class.getName(), methodName);
                }
            }
            else
            {
                MetadataSourceOptions metadataSourceOptions = new MetadataSourceOptions();
                metadataSourceOptions.setEffectiveTime(new Date());

                handler.linkPersonRoleToProfile(userId,
                                                personRoleGUID,
                                                personProfileGUID,
                                                metadataSourceOptions,
                                                null);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Detach a team role from a team profile.
     *
     * @param serverName         name of called server
     * @param viewServiceURLMarker  view service URL marker
     * @param personRoleGUID       unique identifier of the person role
     * @param personProfileGUID            unique identifier of the person profile
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse detachPersonRoleFromProfile(String                   serverName,
                                                    String                   viewServiceURLMarker,
                                                    String                   personRoleGUID,
                                                    String                   personProfileGUID,
                                                    DeleteRequestBody requestBody)
    {
        final String methodName = "detachPersonRoleFromProfile";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ActorRoleHandler handler = instanceHandler.getActorRoleHandler(userId, serverName, viewServiceURLMarker, methodName);

            handler.detachPersonRoleFromProfile(userId, personRoleGUID, personProfileGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Attach a team role to a team profile.
     *
     * @param serverName         name of called server
     * @param viewServiceURLMarker  view service URL marker
     * @param teamRoleGUID           unique identifier of the team role
     * @param teamProfileGUID        unique identifier of the team profile
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse linkTeamRoleToProfile(String                  serverName,
                                              String                  viewServiceURLMarker,
                                              String                  teamRoleGUID,
                                              String                  teamProfileGUID,
                                              NewRelationshipRequestBody requestBody)
    {
        final String methodName = "linkTeamRoleToProfile";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            ActorRoleHandler handler = instanceHandler.getActorRoleHandler(userId, serverName, viewServiceURLMarker, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof TeamRoleAppointmentProperties peerDefinitionProperties)
                {
                    handler.linkTeamRoleToProfile(userId,
                                                  teamRoleGUID,
                                                  teamProfileGUID,
                                                  requestBody,
                                                  peerDefinitionProperties);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(TeamRoleAppointmentProperties.class.getName(), methodName);
                }
            }
            else
            {
                MetadataSourceOptions metadataSourceOptions = new MetadataSourceOptions();
                metadataSourceOptions.setEffectiveTime(new Date());

                handler.linkTeamRoleToProfile(userId,
                                              teamRoleGUID,
                                              teamProfileGUID,
                                              metadataSourceOptions,
                                              null);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Detach a team role from a team profile.
     *
     * @param serverName         name of called server
     * @param viewServiceURLMarker  view service URL marker
     * @param teamRoleGUID           unique identifier of the team role
     * @param teamProfileGUID        unique identifier of the team profile
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse detachTeamRoleFromProfile(String                   serverName,
                                                  String                   viewServiceURLMarker,
                                                  String                   teamRoleGUID,
                                                  String                   teamProfileGUID,
                                                  DeleteRequestBody requestBody)
    {
        final String methodName = "detachTeamRoleFromProfile";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ActorRoleHandler handler = instanceHandler.getActorRoleHandler(userId, serverName, viewServiceURLMarker, methodName);

            handler.detachTeamRoleFromProfile(userId, teamRoleGUID, teamProfileGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Attach an IT profile role to an IT profile.
     *
     * @param serverName         name of called server
     * @param viewServiceURLMarker  view service URL marker
     * @param itProfileRoleGUID      unique identifier of the IT profile role
     * @param itProfileGUID          unique identifier of the IT profile
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse linkITProfileRoleToProfile(String                  serverName,
                                                   String                  viewServiceURLMarker,
                                                   String                  itProfileRoleGUID,
                                                   String                  itProfileGUID,
                                                   NewRelationshipRequestBody requestBody)
    {
        final String methodName = "linkITProfileRoleToProfile";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ActorRoleHandler handler = instanceHandler.getActorRoleHandler(userId, serverName, viewServiceURLMarker, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof ITProfileRoleAppointmentProperties properties)
                {

                    handler.linkITProfileRoleToProfile(userId,
                                                       itProfileRoleGUID,
                                                       itProfileGUID,
                                                       requestBody,
                                                       properties);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(SupportingDefinitionProperties.class.getName(), methodName);
                }
            }
            else
            {
                MetadataSourceOptions metadataSourceOptions = new MetadataSourceOptions();
                metadataSourceOptions.setEffectiveTime(new Date());

                handler.linkITProfileRoleToProfile(userId,
                                                   itProfileRoleGUID,
                                                   itProfileGUID,
                                                   metadataSourceOptions,
                                                   null);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Detach an IT profile role from an IT profile.
     *
     * @param serverName         name of called server
     * @param viewServiceURLMarker  view service URL marker
     * @param itProfileRoleGUID      unique identifier of the IT profile role
     * @param itProfileGUID          unique identifier of the IT profile
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse detachITProfileRoleFromProfile(String                   serverName,
                                                       String                   viewServiceURLMarker,
                                                       String                   itProfileRoleGUID,
                                                       String                   itProfileGUID,
                                                       DeleteRequestBody requestBody)
    {
        final String methodName = "detachITProfileRoleFromProfile";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ActorRoleHandler handler = instanceHandler.getActorRoleHandler(userId, serverName, viewServiceURLMarker, methodName);

            handler.detachITProfileRoleFromProfile(userId, itProfileRoleGUID, itProfileGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Delete an actor role.
     *
     * @param serverName         name of called server
     * @param viewServiceURLMarker  view service URL marker
     * @param actorRoleGUID  unique identifier of the element to delete
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse deleteActorRole(String                   serverName,
                                        String                   viewServiceURLMarker,
                                        String                   actorRoleGUID,
                                        DeleteRequestBody requestBody)
    {
        final String methodName = "deleteActorRole";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ActorRoleHandler handler = instanceHandler.getActorRoleHandler(userId, serverName, viewServiceURLMarker, methodName);

            handler.deleteActorRole(userId, actorRoleGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Retrieve the list of actor role metadata elements that contain the search string.
     *
     * @param serverName name of the service to route the request to
     * @param viewServiceURLMarker  view service URL marker
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public OpenMetadataRootElementsResponse getActorRolesByName(String            serverName,
                                                                String            viewServiceURLMarker,
                                                                FilterRequestBody requestBody)
    {
        final String methodName = "getActorRolesByName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog                         auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ActorRoleHandler handler = instanceHandler.getActorRoleHandler(userId, serverName, viewServiceURLMarker, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.getActorRolesByName(userId, requestBody.getFilter(), requestBody));
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


    /**
     * Retrieve the list of actor role metadata elements that contain the search string.
     *
     * @param serverName name of the service to route the request to
     * @param viewServiceURLMarker  view service URL marker
     * @param actorRoleGUID    unique identifier of the required element
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public OpenMetadataRootElementResponse getActorRoleByGUID(String             serverName,
                                                              String             viewServiceURLMarker,
                                                              String             actorRoleGUID,
                                                              GetRequestBody requestBody)
    {
        final String methodName = "getActorRoleByGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        OpenMetadataRootElementResponse response = new OpenMetadataRootElementResponse();
        AuditLog                        auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ActorRoleHandler handler = instanceHandler.getActorRoleHandler(userId, serverName, viewServiceURLMarker, methodName);

            response.setElement(handler.getActorRoleByGUID(userId, actorRoleGUID, requestBody));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Retrieve the list of actor role metadata elements that contain the search string.
     *
     * @param serverName name of the service to route the request to
     * @param viewServiceURLMarker  view service URL marker
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public OpenMetadataRootElementsResponse findActorRoles(String                  serverName,
                                                           String                  viewServiceURLMarker,
                                                           SearchStringRequestBody requestBody)
    {
        final String methodName = "findActorRoles";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog                        auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ActorRoleHandler handler = instanceHandler.getActorRoleHandler(userId, serverName, viewServiceURLMarker, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.findActorRoles(userId,
                                                            requestBody.getSearchString(),
                                                            requestBody));
            }
            else
            {
                response.setElements(handler.findActorRoles(userId,
                                                            null,
                                                            null));
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Create a user identity.
     *
     * @param serverName                 name of called server.
     * @param viewServiceURLMarker  view service URL marker
     * @param requestBody             properties for the user identity.
     *
     * @return unique identifier of the newly created element
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public GUIDResponse createUserIdentity(String                serverName,
                                           String                viewServiceURLMarker,
                                           NewElementRequestBody requestBody)
    {
        final String methodName = "createUserIdentity";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                UserIdentityHandler handler = instanceHandler.getUserIdentityHandler(userId, serverName, viewServiceURLMarker, methodName);

                if (requestBody.getProperties() instanceof UserIdentityProperties userIdentityProperties)
                {
                    response.setGUID(handler.createUserIdentity(userId,
                                                                requestBody,
                                                                requestBody.getInitialClassifications(),
                                                                userIdentityProperties,
                                                                requestBody.getParentRelationshipProperties()));
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(UserIdentityProperties.class.getName(), methodName);
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


    /**
     * Create a new metadata element to represent a user identity using an existing metadata element as a template.
     * The template defines additional classifications and relationships that should be added to the new element.
     *
     * @param serverName             calling user
     * @param viewServiceURLMarker  view service URL marker
     * @param requestBody properties that override the template
     *
     * @return unique identifier of the new metadata element
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GUIDResponse createUserIdentityFromTemplate(String              serverName,
                                                       String              viewServiceURLMarker,
                                                       TemplateRequestBody requestBody)
    {
        final String methodName = "createUserIdentityFromTemplate";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                UserIdentityHandler handler = instanceHandler.getUserIdentityHandler(userId, serverName, viewServiceURLMarker, methodName);

                response.setGUID(handler.createUserIdentityFromTemplate(userId,
                                                                        requestBody,
                                                                        requestBody.getTemplateGUID(),
                                                                        requestBody.getReplacementProperties(),
                                                                        requestBody.getPlaceholderPropertyValues(),
                                                                        requestBody.getParentRelationshipProperties()));
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


    /**
     * Update the properties of a user identity.
     *
     * @param serverName         name of called server.
     * @param viewServiceURLMarker  view service URL marker
     * @param userIdentityGUID unique identifier of the user identity (returned from create)
     * @param requestBody     properties for the new element.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse updateUserIdentity(String                   serverName,
                                           String                   viewServiceURLMarker,
                                           String                   userIdentityGUID,
                                           UpdateElementRequestBody requestBody)
    {
        final String methodName = "updateUserIdentity";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                UserIdentityHandler handler = instanceHandler.getUserIdentityHandler(userId, serverName, viewServiceURLMarker, methodName);

                if (requestBody.getProperties() instanceof UserIdentityProperties userIdentityProperties)
                {
                    handler.updateUserIdentity(userId,
                                               userIdentityGUID,
                                               requestBody,
                                               userIdentityProperties);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(UserIdentityProperties.class.getName(), methodName);
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


    /**
     * Attach a profile to a user identity.
     *
     * @param serverName         name of called server
     * @param viewServiceURLMarker  view service URL marker
     * @param userIdentityGUID unique identifier of the parent
     * @param profileGUID     unique identifier of the actor profile
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse linkIdentityToProfile(String                  serverName,
                                              String                  viewServiceURLMarker,
                                              String                  userIdentityGUID,
                                              String                  profileGUID,
                                              NewRelationshipRequestBody requestBody)
    {
        final String methodName = "linkIdentityToProfile";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            UserIdentityHandler handler = instanceHandler.getUserIdentityHandler(userId, serverName, viewServiceURLMarker, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof ProfileIdentityProperties profileIdentityProperties)
                {
                    handler.linkIdentityToProfile(userId,
                                                  userIdentityGUID,
                                                  profileGUID,
                                                  requestBody,
                                                  profileIdentityProperties);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(ProfileIdentityProperties.class.getName(), methodName);
                }
            }
            else
            {
                MetadataSourceOptions metadataSourceOptions = new MetadataSourceOptions();
                metadataSourceOptions.setEffectiveTime(new Date());

                handler.linkIdentityToProfile(userId,
                                              userIdentityGUID,
                                              profileGUID,
                                              metadataSourceOptions,
                                              null);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Detach an actor profile from a user identity.
     *
     * @param serverName         name of called server
     * @param viewServiceURLMarker  view service URL marker
     * @param userIdentityGUID    unique identifier of the parent actor profile
     * @param profileGUID    unique identifier of the nested actor profile
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse detachProfileIdentity(String                   serverName,
                                              String                   viewServiceURLMarker,
                                              String                   userIdentityGUID,
                                              String                   profileGUID,
                                              DeleteRequestBody requestBody)
    {
        final String methodName = "detachProfileIdentity";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            UserIdentityHandler handler = instanceHandler.getUserIdentityHandler(userId, serverName, viewServiceURLMarker, methodName);

            handler.detachProfileIdentity(userId, userIdentityGUID, profileGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Add the SecurityGroupMembership classification to the user identity.
     *
     * @param serverName         name of called server
     * @param viewServiceURLMarker  view service URL marker
     * @param userIdentityGUID unique identifier of the user identity
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse addSecurityGroupMembership(String                    serverName,
                                                   String                    viewServiceURLMarker,
                                                   String                    userIdentityGUID,
                                                   NewClassificationRequestBody requestBody)
    {
        final String methodName = "addSecurityGroupMembership";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            UserIdentityHandler handler = instanceHandler.getUserIdentityHandler(userId, serverName, viewServiceURLMarker, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof SecurityGroupMembershipProperties properties)
                {
                    handler.addSecurityGroupMembership(userId, userIdentityGUID, properties, requestBody);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(SecurityGroupMembershipProperties.class.getName(), methodName);
                }
            }
            else
            {
                MetadataSourceOptions metadataSourceOptions = new MetadataSourceOptions();
                metadataSourceOptions.setEffectiveTime(new Date());

                handler.addSecurityGroupMembership(userId, userIdentityGUID, null, metadataSourceOptions);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Update the SecurityGroupMembership classification to the user identity.
     *
     * @param serverName         name of called server
     * @param viewServiceURLMarker  view service URL marker
     * @param userIdentityGUID unique identifier of the user identity
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse updateSecurityGroupMembership(String                          serverName,
                                                      String                          viewServiceURLMarker,
                                                      String                          userIdentityGUID,
                                                      UpdateClassificationRequestBody requestBody)
    {
        final String methodName = "updateSecurityGroupMembership";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            UserIdentityHandler handler = instanceHandler.getUserIdentityHandler(userId, serverName, viewServiceURLMarker, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof SecurityGroupMembershipProperties properties)
                {
                    handler.updateSecurityGroupMembership(userId, userIdentityGUID, requestBody, properties);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(SecurityGroupMembershipProperties.class.getName(), methodName);
                }
            }
            else
            {
                MetadataSourceOptions metadataSourceOptions = new MetadataSourceOptions();
                metadataSourceOptions.setEffectiveTime(new Date());

                handler.addSecurityGroupMembership(userId, userIdentityGUID, null, metadataSourceOptions);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Detach a user identity from a supporting user identity.
     *
     * @param serverName         name of called server
     * @param viewServiceURLMarker  view service URL marker
     * @param userIdentityGUID unique identifier of the first user identity
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse removeAllSecurityGroupMembership(String                    serverName,
                                                         String                    viewServiceURLMarker,
                                                         String                    userIdentityGUID,
                                                         MetadataSourceRequestBody requestBody)
    {
        final String methodName = "removeAllSecurityGroupMembership";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            UserIdentityHandler handler = instanceHandler.getUserIdentityHandler(userId, serverName, viewServiceURLMarker, methodName);

            handler.removeAllSecurityGroupMembership(userId, userIdentityGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Delete a user identity.
     *
     * @param serverName         name of called server
     * @param viewServiceURLMarker  view service URL marker
     * @param userIdentityGUID  unique identifier of the element to delete
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse deleteUserIdentity(String                    serverName,
                                           String                    viewServiceURLMarker,
                                           String                    userIdentityGUID,
                                           DeleteRequestBody requestBody)
    {
        final String methodName = "deleteUserIdentity";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            UserIdentityHandler handler = instanceHandler.getUserIdentityHandler(userId, serverName, viewServiceURLMarker, methodName);

            handler.deleteUserIdentity(userId, userIdentityGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Retrieve the list of user identity metadata elements that contain the search string.
     *
     * @param serverName name of the service to route the request to
     * @param viewServiceURLMarker  view service URL marker
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public OpenMetadataRootElementsResponse getUserIdentitiesByName(String            serverName,
                                                                    String            viewServiceURLMarker,
                                                                    FilterRequestBody requestBody)
    {
        final String methodName = "getUserIdentitiesByName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog                         auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            UserIdentityHandler handler = instanceHandler.getUserIdentityHandler(userId, serverName, viewServiceURLMarker, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.getUserIdentitiesByName(userId, requestBody.getFilter(), requestBody));
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


    /**
     * Retrieve the list of user identity metadata elements that contain the search string.
     *
     * @param serverName name of the service to route the request to
     * @param viewServiceURLMarker  view service URL marker
     * @param userIdentityGUID    unique identifier of the required element
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public OpenMetadataRootElementResponse getUserIdentityByGUID(String             serverName,
                                                                 String             viewServiceURLMarker,
                                                                 String             userIdentityGUID,
                                                                 GetRequestBody requestBody)
    {
        final String methodName = "getUserIdentityByGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        OpenMetadataRootElementResponse response = new OpenMetadataRootElementResponse();
        AuditLog                        auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            UserIdentityHandler handler = instanceHandler.getUserIdentityHandler(userId, serverName, viewServiceURLMarker, methodName);

            response.setElement(handler.getUserIdentityByGUID(userId, userIdentityGUID, requestBody));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Retrieve the list of user identity metadata elements that contain the search string.
     *
     * @param serverName name of the service to route the request to
     * @param viewServiceURLMarker  view service URL marker
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public OpenMetadataRootElementsResponse findUserIdentities(String                  serverName,
                                                               String                  viewServiceURLMarker,
                                                               SearchStringRequestBody requestBody)
    {
        final String methodName = "findUserIdentities";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog               auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            UserIdentityHandler handler = instanceHandler.getUserIdentityHandler(userId, serverName, viewServiceURLMarker, methodName);


            if (requestBody != null)
            {
                response.setElements(handler.findUserIdentities(userId, requestBody.getSearchString(), requestBody));
            }
            else
            {
                response.setElements(handler.findUserIdentities(userId, null, null));
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
