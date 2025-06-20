/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.actormanager.server;


import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.enums.SequencingOrder;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.SupportingDefinitionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.locations.ProfileLocationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.security.SecurityGroupMembershipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.TemplateFilter;
import org.odpi.openmetadata.frameworkservices.omf.client.handlers.*;
import org.odpi.openmetadata.frameworkservices.omf.rest.AnyTimeRequestBody;
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
                                                                requestBody.getExternalSourceGUID(),
                                                                requestBody.getExternalSourceName(),
                                                                requestBody.getAnchorGUID(),
                                                                requestBody.getIsOwnAnchor(),
                                                                requestBody.getAnchorScopeGUID(),
                                                                actorProfileProperties,
                                                                requestBody.getParentGUID(),
                                                                requestBody.getParentRelationshipTypeName(),
                                                                requestBody.getParentRelationshipProperties(),
                                                                requestBody.getParentAtEnd1(),
                                                                requestBody.getForLineage(),
                                                                requestBody.getForDuplicateProcessing(),
                                                                requestBody.getEffectiveTime()));
                }
                else if (requestBody.getProperties() == null)
                {
                    response.setGUID(handler.createActorProfile(userId,
                                                                requestBody.getExternalSourceGUID(),
                                                                requestBody.getExternalSourceName(),
                                                                requestBody.getAnchorGUID(),
                                                                requestBody.getIsOwnAnchor(),
                                                                requestBody.getAnchorScopeGUID(),
                                                                null,
                                                                requestBody.getParentGUID(),
                                                                requestBody.getParentRelationshipTypeName(),
                                                                requestBody.getParentRelationshipProperties(),
                                                                requestBody.getParentAtEnd1(),
                                                                requestBody.getForLineage(),
                                                                requestBody.getForDuplicateProcessing(),
                                                                requestBody.getEffectiveTime()));
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
                                                                        requestBody.getExternalSourceGUID(),
                                                                        requestBody.getExternalSourceName(),
                                                                        requestBody.getAnchorGUID(),
                                                                        requestBody.getIsOwnAnchor(),
                                                                        requestBody.getAnchorScopeGUID(),
                                                                        null,
                                                                        null,
                                                                        requestBody.getTemplateGUID(),
                                                                        requestBody.getReplacementProperties(),
                                                                        requestBody.getPlaceholderPropertyValues(),
                                                                        requestBody.getParentGUID(),
                                                                        requestBody.getParentRelationshipTypeName(),
                                                                        requestBody.getParentRelationshipProperties(),
                                                                        requestBody.getParentAtEnd1(),
                                                                        requestBody.getForLineage(),
                                                                        requestBody.getForDuplicateProcessing(),
                                                                        requestBody.getEffectiveTime()));
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
     * @param replaceAllProperties flag to indicate whether to completely replace the existing properties with the new properties, or just update
     *                          the individual properties specified on the request.
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
                                           boolean                  replaceAllProperties,
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
                                               requestBody.getExternalSourceGUID(),
                                               requestBody.getExternalSourceName(),
                                               actorProfileGUID,
                                               replaceAllProperties,
                                               actorProfileProperties,
                                               requestBody.getForLineage(),
                                               requestBody.getForDuplicateProcessing(),
                                               requestBody.getEffectiveTime());
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.updateActorProfile(userId,
                                               requestBody.getExternalSourceGUID(),
                                               requestBody.getExternalSourceName(),
                                               actorProfileGUID,
                                               replaceAllProperties,
                                               null,
                                               requestBody.getForLineage(),
                                               requestBody.getForDuplicateProcessing(),
                                               requestBody.getEffectiveTime());
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
                                              RelationshipRequestBody requestBody)
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
                                                  requestBody.getExternalSourceGUID(),
                                                  requestBody.getExternalSourceName(),
                                                  actorProfileGUID,
                                                  locationGUID,
                                                  profileLocationProperties,
                                                  requestBody.getForLineage(),
                                                  requestBody.getForDuplicateProcessing(),
                                                  requestBody.getEffectiveTime());
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.linkLocationToProfile(userId,
                                                  requestBody.getExternalSourceGUID(),
                                                  requestBody.getExternalSourceName(),
                                                  actorProfileGUID,
                                                  locationGUID,
                                                  null,
                                                  requestBody.getForLineage(),
                                                  requestBody.getForDuplicateProcessing(),
                                                  requestBody.getEffectiveTime());
                }
                else
                {
                    /*
                     * Wrong type of properties ...
                     */
                    restExceptionHandler.handleInvalidPropertiesObject(ProfileLocationProperties.class.getName(), methodName);
                }
            }
            else
            {
                handler.linkLocationToProfile(userId,
                                              null,
                                              null,
                                              actorProfileGUID,
                                              locationGUID,
                                              null,
                                              false,
                                              false,
                                              new Date());
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
    public VoidResponse detachLocationFromProfile(String                    serverName,
                                                  String                    viewServiceURLMarker,
                                                  String                    actorProfileGUID,
                                                  String                    locationGUID,
                                                  MetadataSourceRequestBody requestBody)
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

            if (requestBody != null)
            {
                handler.detachLocationFromProfile(userId,
                                                  requestBody.getExternalSourceGUID(),
                                                  requestBody.getExternalSourceName(),
                                                  actorProfileGUID,
                                                  locationGUID,
                                                  requestBody.getForLineage(),
                                                  requestBody.getForDuplicateProcessing(),
                                                  requestBody.getEffectiveTime());
            }
            else
            {
                handler.detachLocationFromProfile(userId,
                                                  null,
                                                  null,
                                                  actorProfileGUID,
                                                  locationGUID,
                                                  false,
                                                  false,
                                                  new Date());
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
                                       RelationshipRequestBody requestBody)
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
                                           requestBody.getExternalSourceGUID(),
                                           requestBody.getExternalSourceName(),
                                           personOneGUID,
                                           personTwoGUID,
                                           peerProperties,
                                           requestBody.getForLineage(),
                                           requestBody.getForDuplicateProcessing(),
                                           requestBody.getEffectiveTime());
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.linkPeerPerson(userId,
                                           requestBody.getExternalSourceGUID(),
                                           requestBody.getExternalSourceName(),
                                           personOneGUID,
                                           personTwoGUID,
                                           null,
                                           requestBody.getForLineage(),
                                           requestBody.getForDuplicateProcessing(),
                                           requestBody.getEffectiveTime());
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(PeerProperties.class.getName(), methodName);
                }
            }
            else
            {
                handler.linkPeerPerson(userId,
                                       null,
                                       null,
                                       personOneGUID,
                                       personTwoGUID,
                                       null,
                                       false,
                                       false,
                                       new Date());
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
    public VoidResponse detachPeerPerson(String                    serverName,
                                         String                    viewServiceURLMarker,
                                         String                    personOneGUID,
                                         String                    personTwoGUID,
                                         MetadataSourceRequestBody requestBody)
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

            if (requestBody != null)
            {
                handler.detachPeerPerson(userId,
                                         requestBody.getExternalSourceGUID(),
                                         requestBody.getExternalSourceName(),
                                         personOneGUID,
                                         personTwoGUID,
                                         requestBody.getForLineage(),
                                         requestBody.getForDuplicateProcessing(),
                                         requestBody.getEffectiveTime());
            }
            else
            {
                handler.detachPeerPerson(userId,
                                         null,
                                         null,
                                         personOneGUID,
                                         personTwoGUID,
                                         false,
                                         false,
                                         new Date());
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
                                          RelationshipRequestBody requestBody)
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
                                              requestBody.getExternalSourceGUID(),
                                              requestBody.getExternalSourceName(),
                                              superTeamGUID,
                                              subteamGUID,
                                              teamStructureProperties,
                                              requestBody.getForLineage(),
                                              requestBody.getForDuplicateProcessing(),
                                              requestBody.getEffectiveTime());
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.linkTeamStructure(userId,
                                              requestBody.getExternalSourceGUID(),
                                              requestBody.getExternalSourceName(),
                                              superTeamGUID,
                                              subteamGUID,
                                              null,
                                              requestBody.getForLineage(),
                                              requestBody.getForDuplicateProcessing(),
                                              requestBody.getEffectiveTime());
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(TeamStructureProperties.class.getName(), methodName);
                }
            }
            else
            {
                handler.linkTeamStructure(userId,
                                          null,
                                          null,
                                          superTeamGUID,
                                          subteamGUID,
                                          null,
                                          false,
                                          false,
                                          new Date());
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
    public VoidResponse detachTeamStructure(String                    serverName,
                                            String                    viewServiceURLMarker,
                                            String                    superTeamGUID,
                                            String                    subteamGUID,
                                            MetadataSourceRequestBody requestBody)
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

            if (requestBody != null)
            {
                handler.detachTeamStructure(userId,
                                            requestBody.getExternalSourceGUID(),
                                            requestBody.getExternalSourceName(),
                                            superTeamGUID,
                                            subteamGUID,
                                            requestBody.getForLineage(),
                                            requestBody.getForDuplicateProcessing(),
                                            requestBody.getEffectiveTime());
            }
            else
            {
                handler.detachTeamStructure(userId,
                                            null,
                                            null,
                                            superTeamGUID,
                                            subteamGUID,
                                            false,
                                            false,
                                            new Date());
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
                                           RelationshipRequestBody requestBody)
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
                                               requestBody.getExternalSourceGUID(),
                                               requestBody.getExternalSourceName(),
                                               assetGUID,
                                               itProfileGUID,
                                               itInfrastructureProfileProperties,
                                               requestBody.getForLineage(),
                                               requestBody.getForDuplicateProcessing(),
                                               requestBody.getEffectiveTime());
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.linkAssetToProfile(userId,
                                               requestBody.getExternalSourceGUID(),
                                               requestBody.getExternalSourceName(),
                                               assetGUID,
                                               itProfileGUID,
                                               null,
                                               requestBody.getForLineage(),
                                               requestBody.getForDuplicateProcessing(),
                                               requestBody.getEffectiveTime());
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(ITInfrastructureProfileProperties.class.getName(), methodName);
                }
            }
            else
            {
                handler.linkAssetToProfile(userId,
                                           null,
                                           null,
                                           assetGUID,
                                           itProfileGUID,
                                           null,
                                           false,
                                           false,
                                           new Date());
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
    public VoidResponse detachAssetFromProfile(String                    serverName,
                                               String                    viewServiceURLMarker,
                                               String                    assetGUID,
                                               String                    itProfileGUID,
                                               MetadataSourceRequestBody requestBody)
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

            if (requestBody != null)
            {
                handler.detachAssetFromProfile(userId,
                                               requestBody.getExternalSourceGUID(),
                                               requestBody.getExternalSourceName(),
                                               assetGUID,
                                               itProfileGUID,
                                               requestBody.getForLineage(),
                                               requestBody.getForDuplicateProcessing(),
                                               requestBody.getEffectiveTime());
            }
            else
            {
                handler.detachAssetFromProfile(userId,
                                               null,
                                               null,
                                               assetGUID,
                                               itProfileGUID,
                                               false,
                                               false,
                                               new Date());
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
                                                 RelationshipRequestBody requestBody)
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
                                                     requestBody.getExternalSourceGUID(),
                                                     requestBody.getExternalSourceName(),
                                                     teamGUID,
                                                     personRoleGUID,
                                                     teamMembershipProperties,
                                                     requestBody.getForLineage(),
                                                     requestBody.getForDuplicateProcessing(),
                                                     requestBody.getEffectiveTime());
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.linkTeamToMembershipRole(userId,
                                                     requestBody.getExternalSourceGUID(),
                                                     requestBody.getExternalSourceName(),
                                                     teamGUID,
                                                     personRoleGUID,
                                                     null,
                                                     requestBody.getForLineage(),
                                                     requestBody.getForDuplicateProcessing(),
                                                     requestBody.getEffectiveTime());
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(TeamMembershipProperties.class.getName(), methodName);
                }
            }
            else
            {
                handler.linkTeamToMembershipRole(userId,
                                                 null,
                                                 null,
                                                 teamGUID,
                                                 personRoleGUID,
                                                 null,
                                                 false,
                                                 false,
                                                 new Date());
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
    public VoidResponse detachTeamFromMembershipRole(String                    serverName,
                                                     String                    viewServiceURLMarker,
                                                     String                    teamGUID,
                                                     String                    personRoleGUID,
                                                     MetadataSourceRequestBody requestBody)
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

            if (requestBody != null)
            {
                handler.detachTeamFromMembershipRole(userId,
                                                     requestBody.getExternalSourceGUID(),
                                                     requestBody.getExternalSourceName(),
                                                     teamGUID,
                                                     personRoleGUID,
                                                     requestBody.getForLineage(),
                                                     requestBody.getForDuplicateProcessing(),
                                                     requestBody.getEffectiveTime());
            }
            else
            {
                handler.detachTeamFromMembershipRole(userId,
                                                     null,
                                                     null,
                                                     teamGUID,
                                                     personRoleGUID,
                                                     false,
                                                     false,
                                                     new Date());
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
                                                 RelationshipRequestBody requestBody)
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
                                                     requestBody.getExternalSourceGUID(),
                                                     requestBody.getExternalSourceName(),
                                                     teamGUID,
                                                     personRoleGUID,
                                                     teamLeadershipProperties,
                                                     requestBody.getForLineage(),
                                                     requestBody.getForDuplicateProcessing(),
                                                     requestBody.getEffectiveTime());
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.linkTeamToLeadershipRole(userId,
                                                     requestBody.getExternalSourceGUID(),
                                                     requestBody.getExternalSourceName(),
                                                     teamGUID,
                                                     personRoleGUID,
                                                     null,
                                                     requestBody.getForLineage(),
                                                     requestBody.getForDuplicateProcessing(),
                                                     requestBody.getEffectiveTime());
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(TeamLeadershipProperties.class.getName(), methodName);
                }
            }
            else
            {
                handler.linkTeamToLeadershipRole(userId,
                                                 null,
                                                 null,
                                                 teamGUID,
                                                 personRoleGUID,
                                                 null,
                                                 false,
                                                 false,
                                                 new Date());
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
    public VoidResponse detachTeamFromLeadershipRole(String                    serverName,
                                                     String                    viewServiceURLMarker,
                                                     String                    teamGUID,
                                                     String                    personRoleGUID,
                                                     MetadataSourceRequestBody requestBody)
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

            if (requestBody != null)
            {
                handler.detachTeamFromLeadershipRole(userId,
                                                     requestBody.getExternalSourceGUID(),
                                                     requestBody.getExternalSourceName(),
                                                     teamGUID,
                                                     personRoleGUID,
                                                     requestBody.getForLineage(),
                                                     requestBody.getForDuplicateProcessing(),
                                                     requestBody.getEffectiveTime());
            }
            else
            {
                handler.detachTeamFromLeadershipRole(userId,
                                                     null,
                                                     null,
                                                     teamGUID,
                                                     personRoleGUID,
                                                     false,
                                                     false,
                                                     new Date());
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
     * Delete an actor profile.
     *
     * @param serverName         name of called server
     * @param viewServiceURLMarker  view service URL marker
     * @param actorProfileGUID  unique identifier of the element to delete
     * @param cascadedDelete can actor profiles be deleted if data fields are attached?
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse deleteActorProfile(String                    serverName,
                                           String                    viewServiceURLMarker,
                                           String                    actorProfileGUID,
                                           boolean                   cascadedDelete,
                                           MetadataSourceRequestBody requestBody)
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

            if (requestBody != null)
            {
                handler.deleteActorProfile(userId,
                                           requestBody.getExternalSourceGUID(),
                                           requestBody.getExternalSourceName(),
                                           actorProfileGUID,
                                           cascadedDelete,
                                           requestBody.getForLineage(),
                                           requestBody.getForDuplicateProcessing(),
                                           requestBody.getEffectiveTime());
            }
            else
            {
                handler.deleteActorProfile(userId,
                                           null,
                                           null,
                                           actorProfileGUID,
                                           cascadedDelete,
                                           false,
                                           false,
                                           new Date());
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
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public ActorProfilesResponse getActorProfilesByName(String            serverName,
                                                        String            viewServiceURLMarker,
                                                        int               startFrom,
                                                        int               pageSize,
                                                        FilterRequestBody requestBody)
    {
        final String methodName = "getActorProfilesByName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        ActorProfilesResponse response = new ActorProfilesResponse();
        AuditLog                        auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ActorProfileHandler handler = instanceHandler.getActorProfileHandler(userId, serverName, viewServiceURLMarker, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.getActorProfilesByName(userId,
                                                                    requestBody.getFilter(),
                                                                    requestBody.getTemplateFilter(),
                                                                    requestBody.getLimitResultsByStatus(),
                                                                    requestBody.getAsOfTime(),
                                                                    requestBody.getSequencingOrder(),
                                                                    requestBody.getSequencingProperty(),
                                                                    startFrom,
                                                                    pageSize,
                                                                    requestBody.getForLineage(),
                                                                    requestBody.getForDuplicateProcessing(),
                                                                    requestBody.getEffectiveTime()));
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
    public ActorProfileResponse getActorProfileByGUID(String             serverName,
                                                      String             viewServiceURLMarker,
                                                      String             actorProfileGUID,
                                                      AnyTimeRequestBody requestBody)
    {
        final String methodName = "getActorProfileByGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        ActorProfileResponse response = new ActorProfileResponse();
        AuditLog                      auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ActorProfileHandler handler = instanceHandler.getActorProfileHandler(userId, serverName, viewServiceURLMarker, methodName);

            if (requestBody != null)
            {
                response.setElement(handler.getActorProfileByGUID(userId,
                                                                  actorProfileGUID,
                                                                  requestBody.getAsOfTime(),
                                                                  requestBody.getForLineage(),
                                                                  requestBody.getForDuplicateProcessing(),
                                                                  requestBody.getEffectiveTime()));
            }
            else
            {
                response.setElement(handler.getActorProfileByGUID(userId,
                                                                  actorProfileGUID,
                                                                  null,
                                                                  false,
                                                                  false,
                                                                  new Date()));
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
     * @param startsWith does the value start with the supplied string?
     * @param endsWith does the value end with the supplied string?
     * @param ignoreCase should the search ignore case?
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public ActorProfilesResponse findActorProfiles(String            serverName,
                                                   String            viewServiceURLMarker,
                                                   boolean           startsWith,
                                                   boolean           endsWith,
                                                   boolean           ignoreCase,
                                                   int               startFrom,
                                                   int               pageSize,
                                                   FilterRequestBody requestBody)
    {
        final String methodName = "findActorProfiles";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        ActorProfilesResponse response = new ActorProfilesResponse();
        AuditLog                        auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ActorProfileHandler handler = instanceHandler.getActorProfileHandler(userId, serverName, viewServiceURLMarker, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.findActorProfiles(userId,
                                                               instanceHandler.getSearchString(requestBody.getFilter(), startsWith, endsWith, ignoreCase),
                                                               requestBody.getTemplateFilter(),
                                                               requestBody.getLimitResultsByStatus(),
                                                               requestBody.getAsOfTime(),
                                                               requestBody.getSequencingOrder(),
                                                               requestBody.getSequencingProperty(),
                                                               startFrom,
                                                               pageSize,
                                                               requestBody.getForLineage(),
                                                               requestBody.getForDuplicateProcessing(),
                                                               requestBody.getEffectiveTime()));
            }
            else
            {
                response.setElements(handler.findActorProfiles(userId,
                                                               instanceHandler.getSearchString(null, startsWith, endsWith, ignoreCase),
                                                               TemplateFilter.ALL,
                                                               null,
                                                               null,
                                                               SequencingOrder.CREATION_DATE_RECENT,
                                                               null,
                                                               startFrom,
                                                               pageSize,
                                                               false,
                                                               false,
                                                               new Date()));
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
                                                             requestBody.getExternalSourceGUID(),
                                                             requestBody.getExternalSourceName(),
                                                             requestBody.getAnchorGUID(),
                                                             requestBody.getIsOwnAnchor(),
                                                             requestBody.getAnchorScopeGUID(),
                                                             actorRoleProperties,
                                                             requestBody.getParentGUID(),
                                                             requestBody.getParentRelationshipTypeName(),
                                                             requestBody.getParentRelationshipProperties(),
                                                             requestBody.getParentAtEnd1(),
                                                             requestBody.getForLineage(),
                                                             requestBody.getForDuplicateProcessing(),
                                                             requestBody.getEffectiveTime()));
                }
                else if (requestBody.getProperties() == null)
                {
                    response.setGUID(handler.createActorRole(userId,
                                                             requestBody.getExternalSourceGUID(),
                                                             requestBody.getExternalSourceName(),
                                                             requestBody.getAnchorGUID(),
                                                             requestBody.getIsOwnAnchor(),
                                                             requestBody.getAnchorScopeGUID(),
                                                             null,
                                                             requestBody.getParentGUID(),
                                                             requestBody.getParentRelationshipTypeName(),
                                                             requestBody.getParentRelationshipProperties(),
                                                             requestBody.getParentAtEnd1(),
                                                             requestBody.getForLineage(),
                                                             requestBody.getForDuplicateProcessing(),
                                                             requestBody.getEffectiveTime()));
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
                                                                     requestBody.getExternalSourceGUID(),
                                                                     requestBody.getExternalSourceName(),
                                                                     requestBody.getAnchorGUID(),
                                                                     requestBody.getIsOwnAnchor(),
                                                                     requestBody.getAnchorScopeGUID(),
                                                                     null,
                                                                     null,
                                                                     requestBody.getTemplateGUID(),
                                                                     requestBody.getReplacementProperties(),
                                                                     requestBody.getPlaceholderPropertyValues(),
                                                                     requestBody.getParentGUID(),
                                                                     requestBody.getParentRelationshipTypeName(),
                                                                     requestBody.getParentRelationshipProperties(),
                                                                     requestBody.getParentAtEnd1(),
                                                                     requestBody.getForLineage(),
                                                                     requestBody.getForDuplicateProcessing(),
                                                                     requestBody.getEffectiveTime()));
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
     * @param replaceAllProperties flag to indicate whether to completely replace the existing properties with the new properties, or just update
     *                          the individual properties specified on the request.
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
                                        boolean                  replaceAllProperties,
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
                                            requestBody.getExternalSourceGUID(),
                                            requestBody.getExternalSourceName(),
                                            actorRoleGUID,
                                            replaceAllProperties,
                                            actorRoleProperties,
                                            requestBody.getForLineage(),
                                            requestBody.getForDuplicateProcessing(),
                                            requestBody.getEffectiveTime());
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.updateActorRole(userId,
                                            requestBody.getExternalSourceGUID(),
                                            requestBody.getExternalSourceName(),
                                            actorRoleGUID,
                                            replaceAllProperties,
                                            null,
                                            requestBody.getForLineage(),
                                            requestBody.getForDuplicateProcessing(),
                                            requestBody.getEffectiveTime());
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
                                            RelationshipRequestBody requestBody)
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
                                                requestBody.getExternalSourceGUID(),
                                                requestBody.getExternalSourceName(),
                                                personRoleGUID,
                                                personProfileGUID,
                                                peerDefinitionProperties,
                                                requestBody.getForLineage(),
                                                requestBody.getForDuplicateProcessing(),
                                                requestBody.getEffectiveTime());
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.linkPersonRoleToProfile(userId,
                                                requestBody.getExternalSourceGUID(),
                                                requestBody.getExternalSourceName(),
                                                personRoleGUID,
                                                personProfileGUID,
                                                null,
                                                requestBody.getForLineage(),
                                                requestBody.getForDuplicateProcessing(),
                                                requestBody.getEffectiveTime());
                }
                else
                {
                    /*
                     * Wrong type of properties ...
                     */
                    restExceptionHandler.handleInvalidPropertiesObject(PersonRoleAppointmentProperties.class.getName(), methodName);
                }
            }
            else
            {
                handler.linkPersonRoleToProfile(userId,
                                            null,
                                            null,
                                            personRoleGUID,
                                            personProfileGUID,
                                            null,
                                            false,
                                            false,
                                            new Date());
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
    public VoidResponse detachPersonRoleFromProfile(String                    serverName,
                                                  String                    viewServiceURLMarker,
                                                  String                    personRoleGUID,
                                                  String                    personProfileGUID,
                                                  MetadataSourceRequestBody requestBody)
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

            if (requestBody != null)
            {
                handler.detachPersonRoleFromProfile(userId,
                                              requestBody.getExternalSourceGUID(),
                                              requestBody.getExternalSourceName(),
                                              personRoleGUID,
                                              personProfileGUID,
                                              requestBody.getForLineage(),
                                              requestBody.getForDuplicateProcessing(),
                                              requestBody.getEffectiveTime());
            }
            else
            {
                handler.detachPersonRoleFromProfile(userId,
                                              null,
                                              null,
                                              personRoleGUID,
                                              personProfileGUID,
                                              false,
                                              false,
                                              new Date());
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
                                              RelationshipRequestBody requestBody)
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
                                                  requestBody.getExternalSourceGUID(),
                                                  requestBody.getExternalSourceName(),
                                                  teamRoleGUID,
                                                  teamProfileGUID,
                                                  peerDefinitionProperties,
                                                  requestBody.getForLineage(),
                                                  requestBody.getForDuplicateProcessing(),
                                                  requestBody.getEffectiveTime());
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.linkTeamRoleToProfile(userId,
                                                  requestBody.getExternalSourceGUID(),
                                                  requestBody.getExternalSourceName(),
                                                  teamRoleGUID,
                                                  teamProfileGUID,
                                                  null,
                                                  requestBody.getForLineage(),
                                                  requestBody.getForDuplicateProcessing(),
                                                  requestBody.getEffectiveTime());
                }
                else
                {
                    /*
                     * Wrong type of properties ...
                     */
                    restExceptionHandler.handleInvalidPropertiesObject(TeamRoleAppointmentProperties.class.getName(), methodName);
                }
            }
            else
            {
                handler.linkTeamRoleToProfile(userId,
                                              null,
                                              null,
                                              teamRoleGUID,
                                              teamProfileGUID,
                                              null,
                                              false,
                                              false,
                                              new Date());
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
    public VoidResponse detachTeamRoleFromProfile(String                    serverName,
                                                  String                    viewServiceURLMarker,
                                                  String                    teamRoleGUID,
                                                  String                    teamProfileGUID,
                                                  MetadataSourceRequestBody requestBody)
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

            if (requestBody != null)
            {
                handler.detachTeamRoleFromProfile(userId,
                                                  requestBody.getExternalSourceGUID(),
                                                  requestBody.getExternalSourceName(),
                                                  teamRoleGUID,
                                                  teamProfileGUID,
                                                  requestBody.getForLineage(),
                                                  requestBody.getForDuplicateProcessing(),
                                                  requestBody.getEffectiveTime());
            }
            else
            {
                handler.detachTeamRoleFromProfile(userId,
                                                  null,
                                                  null,
                                                  teamRoleGUID,
                                                  teamProfileGUID,
                                                  false,
                                                  false,
                                                  new Date());
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
                                                   RelationshipRequestBody requestBody)
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
                                                       requestBody.getExternalSourceGUID(),
                                                       requestBody.getExternalSourceName(),
                                                       itProfileRoleGUID,
                                                       itProfileGUID,
                                                       properties,
                                                       requestBody.getForLineage(),
                                                       requestBody.getForDuplicateProcessing(),
                                                       requestBody.getEffectiveTime());
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.linkITProfileRoleToProfile(userId,
                                                       requestBody.getExternalSourceGUID(),
                                                       requestBody.getExternalSourceName(),
                                                       itProfileRoleGUID,
                                                       itProfileGUID,
                                                       null,
                                                       requestBody.getForLineage(),
                                                       requestBody.getForDuplicateProcessing(),
                                                       requestBody.getEffectiveTime());
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(SupportingDefinitionProperties.class.getName(), methodName);
                }
            }
            else
            {
                handler.linkITProfileRoleToProfile(userId,
                                                   null,
                                                   null,
                                                   itProfileRoleGUID,
                                                   itProfileGUID,
                                                   null,
                                                   false,
                                                   false,
                                                   new Date());
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
    public VoidResponse detachITProfileRoleFromProfile(String                    serverName,
                                                       String                    viewServiceURLMarker,
                                                       String                    itProfileRoleGUID,
                                                       String                    itProfileGUID,
                                                       MetadataSourceRequestBody requestBody)
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

            if (requestBody != null)
            {
                handler.detachITProfileRoleFromProfile(userId,
                                                   requestBody.getExternalSourceGUID(),
                                                   requestBody.getExternalSourceName(),
                                                   itProfileRoleGUID,
                                                   itProfileGUID,
                                                   requestBody.getForLineage(),
                                                   requestBody.getForDuplicateProcessing(),
                                                   requestBody.getEffectiveTime());
            }
            else
            {
                handler.detachITProfileRoleFromProfile(userId,
                                                   null,
                                                   null,
                                                   itProfileRoleGUID,
                                                   itProfileGUID,
                                                   false,
                                                   false,
                                                   new Date());
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
     * Delete an actor role.
     *
     * @param serverName         name of called server
     * @param viewServiceURLMarker  view service URL marker
     * @param actorRoleGUID  unique identifier of the element to delete
     * @param cascadedDelete can actor roles be deleted if data fields are attached?
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse deleteActorRole(String                    serverName,
                                                   String                    viewServiceURLMarker,
                                                   String                    actorRoleGUID,
                                                   boolean                   cascadedDelete,
                                                   MetadataSourceRequestBody requestBody)
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

            if (requestBody != null)
            {
                handler.deleteActorRole(userId,
                                                   requestBody.getExternalSourceGUID(),
                                                   requestBody.getExternalSourceName(),
                                                   actorRoleGUID,
                                                   cascadedDelete,
                                                   requestBody.getForLineage(),
                                                   requestBody.getForDuplicateProcessing(),
                                                   requestBody.getEffectiveTime());
            }
            else
            {
                handler.deleteActorRole(userId,
                                                   null,
                                                   null,
                                                   actorRoleGUID,
                                                   cascadedDelete,
                                                   false,
                                                   false,
                                                   new Date());
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
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public ActorRolesResponse getActorRolesByName(String            serverName,
                                                                        String            viewServiceURLMarker,
                                                                        int               startFrom,
                                                                        int               pageSize,
                                                                        FilterRequestBody requestBody)
    {
        final String methodName = "getActorRolesByName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        ActorRolesResponse response = new ActorRolesResponse();
        AuditLog                        auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ActorRoleHandler handler = instanceHandler.getActorRoleHandler(userId, serverName, viewServiceURLMarker, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.getActorRolesByName(userId,
                                                                            requestBody.getFilter(),
                                                                            requestBody.getTemplateFilter(),
                                                                            requestBody.getLimitResultsByStatus(),
                                                                            requestBody.getAsOfTime(),
                                                                            requestBody.getSequencingOrder(),
                                                                            requestBody.getSequencingProperty(),
                                                                            startFrom,
                                                                            pageSize,
                                                                            requestBody.getForLineage(),
                                                                            requestBody.getForDuplicateProcessing(),
                                                                            requestBody.getEffectiveTime()));
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
    public ActorRoleResponse getActorRoleByGUID(String             serverName,
                                                                      String             viewServiceURLMarker,
                                                                      String             actorRoleGUID,
                                                                      AnyTimeRequestBody requestBody)
    {
        final String methodName = "getActorRoleByGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        ActorRoleResponse response = new ActorRoleResponse();
        AuditLog                      auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ActorRoleHandler handler = instanceHandler.getActorRoleHandler(userId, serverName, viewServiceURLMarker, methodName);

            if (requestBody != null)
            {
                response.setElement(handler.getActorRoleByGUID(userId,
                                                                          actorRoleGUID,
                                                                          requestBody.getAsOfTime(),
                                                                          requestBody.getForLineage(),
                                                                          requestBody.getForDuplicateProcessing(),
                                                                          requestBody.getEffectiveTime()));
            }
            else
            {
                response.setElement(handler.getActorRoleByGUID(userId,
                                                                          actorRoleGUID,
                                                                          null,
                                                                          false,
                                                                          false,
                                                                          new Date()));
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
     * @param startsWith does the value start with the supplied string?
     * @param endsWith does the value end with the supplied string?
     * @param ignoreCase should the search ignore case?
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public ActorRolesResponse findActorRoles(String            serverName,
                                                                   String            viewServiceURLMarker,
                                                                   boolean           startsWith,
                                                                   boolean           endsWith,
                                                                   boolean           ignoreCase,
                                                                   int               startFrom,
                                                                   int               pageSize,
                                                                   FilterRequestBody requestBody)
    {
        final String methodName = "findActorRoles";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        ActorRolesResponse response = new ActorRolesResponse();
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
                                                                       instanceHandler.getSearchString(requestBody.getFilter(), startsWith, endsWith, ignoreCase),
                                                                       requestBody.getTemplateFilter(),
                                                                       requestBody.getLimitResultsByStatus(),
                                                                       requestBody.getAsOfTime(),
                                                                       requestBody.getSequencingOrder(),
                                                                       requestBody.getSequencingProperty(),
                                                                       startFrom,
                                                                       pageSize,
                                                                       requestBody.getForLineage(),
                                                                       requestBody.getForDuplicateProcessing(),
                                                                       requestBody.getEffectiveTime()));
            }
            else
            {
                response.setElements(handler.findActorRoles(userId,
                                                                       instanceHandler.getSearchString(null, startsWith, endsWith, ignoreCase),
                                                                       TemplateFilter.ALL,
                                                                       null,
                                                                       null,
                                                                       SequencingOrder.CREATION_DATE_RECENT,
                                                                       null,
                                                                       startFrom,
                                                                       pageSize,
                                                                       false,
                                                                       false,
                                                                       new Date()));
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
                                                                requestBody.getExternalSourceGUID(),
                                                                requestBody.getExternalSourceName(),
                                                                requestBody.getAnchorGUID(),
                                                                requestBody.getIsOwnAnchor(),
                                                                requestBody.getAnchorScopeGUID(),
                                                                userIdentityProperties,
                                                                requestBody.getParentGUID(),
                                                                requestBody.getParentRelationshipTypeName(),
                                                                requestBody.getParentRelationshipProperties(),
                                                                requestBody.getParentAtEnd1(),
                                                                requestBody.getForLineage(),
                                                                requestBody.getForDuplicateProcessing(),
                                                                requestBody.getEffectiveTime()));
                }
                else if (requestBody.getProperties() == null)
                {
                    response.setGUID(handler.createUserIdentity(userId,
                                                                requestBody.getExternalSourceGUID(),
                                                                requestBody.getExternalSourceName(),
                                                                requestBody.getAnchorGUID(),
                                                                requestBody.getIsOwnAnchor(),
                                                                requestBody.getAnchorScopeGUID(),
                                                                null,
                                                                requestBody.getParentGUID(),
                                                                requestBody.getParentRelationshipTypeName(),
                                                                requestBody.getParentRelationshipProperties(),
                                                                requestBody.getParentAtEnd1(),
                                                                requestBody.getForLineage(),
                                                                requestBody.getForDuplicateProcessing(),
                                                                requestBody.getEffectiveTime()));
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
                                                                        requestBody.getExternalSourceGUID(),
                                                                        requestBody.getExternalSourceName(),
                                                                        requestBody.getAnchorGUID(),
                                                                        requestBody.getIsOwnAnchor(),
                                                                        requestBody.getAnchorScopeGUID(),
                                                                        null,
                                                                        null,
                                                                        requestBody.getTemplateGUID(),
                                                                        requestBody.getReplacementProperties(),
                                                                        requestBody.getPlaceholderPropertyValues(),
                                                                        requestBody.getParentGUID(),
                                                                        requestBody.getParentRelationshipTypeName(),
                                                                        requestBody.getParentRelationshipProperties(),
                                                                        requestBody.getParentAtEnd1(),
                                                                        requestBody.getForLineage(),
                                                                        requestBody.getForDuplicateProcessing(),
                                                                        requestBody.getEffectiveTime()));
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
     * @param replaceAllProperties flag to indicate whether to completely replace the existing properties with the new properties, or just update
     *                          the individual properties specified on the request.
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
                                           boolean                  replaceAllProperties,
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
                                               requestBody.getExternalSourceGUID(),
                                               requestBody.getExternalSourceName(),
                                               userIdentityGUID,
                                               replaceAllProperties,
                                               userIdentityProperties,
                                               requestBody.getForLineage(),
                                               requestBody.getForDuplicateProcessing(),
                                               requestBody.getEffectiveTime());
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.updateUserIdentity(userId,
                                               requestBody.getExternalSourceGUID(),
                                               requestBody.getExternalSourceName(),
                                               userIdentityGUID,
                                               replaceAllProperties,
                                               null,
                                               requestBody.getForLineage(),
                                               requestBody.getForDuplicateProcessing(),
                                               requestBody.getEffectiveTime());
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
                                              RelationshipRequestBody requestBody)
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
                                                  requestBody.getExternalSourceGUID(),
                                                  requestBody.getExternalSourceName(),
                                                  userIdentityGUID,
                                                  profileGUID,
                                                  profileIdentityProperties,
                                                  requestBody.getForLineage(),
                                                  requestBody.getForDuplicateProcessing(),
                                                  requestBody.getEffectiveTime());
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.linkIdentityToProfile(userId,
                                                  requestBody.getExternalSourceGUID(),
                                                  requestBody.getExternalSourceName(),
                                                  userIdentityGUID,
                                                  profileGUID,
                                                  null,
                                                  requestBody.getForLineage(),
                                                  requestBody.getForDuplicateProcessing(),
                                                  requestBody.getEffectiveTime());
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(ProfileIdentityProperties.class.getName(), methodName);
                }
            }
            else
            {
                handler.linkIdentityToProfile(userId,
                                              null,
                                              null,
                                              userIdentityGUID,
                                              profileGUID,
                                              null,
                                              false,
                                              false,
                                              new Date());
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
    public VoidResponse detachProfileIdentity(String                    serverName,
                                              String                    viewServiceURLMarker,
                                              String                    userIdentityGUID,
                                              String                    profileGUID,
                                              MetadataSourceRequestBody requestBody)
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

            if (requestBody != null)
            {
                handler.detachProfileIdentity(userId,
                                              requestBody.getExternalSourceGUID(),
                                              requestBody.getExternalSourceName(),
                                              userIdentityGUID,
                                              profileGUID,
                                              requestBody.getForLineage(),
                                              requestBody.getForDuplicateProcessing(),
                                              requestBody.getEffectiveTime());
            }
            else
            {
                handler.detachProfileIdentity(userId,
                                              null,
                                              null,
                                              userIdentityGUID,
                                              profileGUID,
                                              false,
                                              false,
                                              new Date());
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
                                                   ClassificationRequestBody requestBody)
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
                    handler.addSecurityGroupMembership(userId,
                                                       requestBody.getExternalSourceGUID(),
                                                       requestBody.getExternalSourceName(),
                                                       userIdentityGUID,
                                                       properties,
                                                       requestBody.getForLineage(),
                                                       requestBody.getForDuplicateProcessing(),
                                                       requestBody.getEffectiveTime());
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.addSecurityGroupMembership(userId,
                                                       requestBody.getExternalSourceGUID(),
                                                       requestBody.getExternalSourceName(),
                                                       userIdentityGUID,
                                                       null,
                                                       requestBody.getForLineage(),
                                                       requestBody.getForDuplicateProcessing(),
                                                       requestBody.getEffectiveTime());
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(SecurityGroupMembershipProperties.class.getName(), methodName);
                }
            }
            else
            {
                handler.addSecurityGroupMembership(userId,
                                                   null,
                                                   null,
                                                   userIdentityGUID,
                                                   null,
                                                   false,
                                                   false,
                                                   new Date());
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
    public VoidResponse updateSecurityGroupMembership(String                    serverName,
                                                      String                    viewServiceURLMarker,
                                                      String                    userIdentityGUID,
                                                      ClassificationRequestBody requestBody)
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
                    handler.updateSecurityGroupMembership(userId,
                                                          requestBody.getExternalSourceGUID(),
                                                          requestBody.getExternalSourceName(),
                                                          userIdentityGUID,
                                                          properties,
                                                          requestBody.getForLineage(),
                                                          requestBody.getForDuplicateProcessing(),
                                                          requestBody.getEffectiveTime());
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.updateSecurityGroupMembership(userId,
                                                          requestBody.getExternalSourceGUID(),
                                                          requestBody.getExternalSourceName(),
                                                          userIdentityGUID,
                                                          null,
                                                          requestBody.getForLineage(),
                                                          requestBody.getForDuplicateProcessing(),
                                                          requestBody.getEffectiveTime());
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(SecurityGroupMembershipProperties.class.getName(), methodName);
                }
            }
            else
            {
                handler.addSecurityGroupMembership(userId,
                                                   null,
                                                   null,
                                                   userIdentityGUID,
                                                   null,
                                                   false,
                                                   false,
                                                   new Date());
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

            if (requestBody != null)
            {
                handler.removeAllSecurityGroupMembership(userId,
                                                         requestBody.getExternalSourceGUID(),
                                                         requestBody.getExternalSourceName(),
                                                         userIdentityGUID,
                                                         requestBody.getForLineage(),
                                                         requestBody.getForDuplicateProcessing(),
                                                         requestBody.getEffectiveTime());
            }
            else
            {
                handler.removeAllSecurityGroupMembership(userId,
                                                         null,
                                                         null,
                                                         userIdentityGUID,
                                                         false,
                                                         false,
                                                         new Date());
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
     * Delete a user identity.
     *
     * @param serverName         name of called server
     * @param viewServiceURLMarker  view service URL marker
     * @param userIdentityGUID  unique identifier of the element to delete
     * @param cascadedDelete can user identities be deleted if data fields are attached?
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
                                           boolean                   cascadedDelete,
                                           MetadataSourceRequestBody requestBody)
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

            if (requestBody != null)
            {
                handler.deleteUserIdentity(userId,
                                           requestBody.getExternalSourceGUID(),
                                           requestBody.getExternalSourceName(),
                                           userIdentityGUID,
                                           cascadedDelete,
                                           requestBody.getForLineage(),
                                           requestBody.getForDuplicateProcessing(),
                                           requestBody.getEffectiveTime());
            }
            else
            {
                handler.deleteUserIdentity(userId,
                                           null,
                                           null,
                                           userIdentityGUID,
                                           cascadedDelete,
                                           false,
                                           false,
                                           new Date());
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
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public UserIdentitiesResponse getUserIdentitiesByName(String            serverName,
                                                          String            viewServiceURLMarker,
                                                          int               startFrom,
                                                          int               pageSize,
                                                          FilterRequestBody requestBody)
    {
        final String methodName = "getUserIdentitiesByName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        UserIdentitiesResponse response = new UserIdentitiesResponse();
        AuditLog                        auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            UserIdentityHandler handler = instanceHandler.getUserIdentityHandler(userId, serverName, viewServiceURLMarker, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.getUserIdentitiesByName(userId,
                                                                     requestBody.getFilter(),
                                                                     requestBody.getTemplateFilter(),
                                                                     requestBody.getLimitResultsByStatus(),
                                                                     requestBody.getAsOfTime(),
                                                                     requestBody.getSequencingOrder(),
                                                                     requestBody.getSequencingProperty(),
                                                                     startFrom,
                                                                     pageSize,
                                                                     requestBody.getForLineage(),
                                                                     requestBody.getForDuplicateProcessing(),
                                                                     requestBody.getEffectiveTime()));
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
    public UserIdentityResponse getUserIdentityByGUID(String             serverName,
                                                      String             viewServiceURLMarker,
                                                      String             userIdentityGUID,
                                                      AnyTimeRequestBody requestBody)
    {
        final String methodName = "getUserIdentityByGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        UserIdentityResponse response = new UserIdentityResponse();
        AuditLog                      auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            UserIdentityHandler handler = instanceHandler.getUserIdentityHandler(userId, serverName, viewServiceURLMarker, methodName);

            if (requestBody != null)
            {
                response.setElement(handler.getUserIdentityByGUID(userId,
                                                                  userIdentityGUID,
                                                                  requestBody.getAsOfTime(),
                                                                  requestBody.getForLineage(),
                                                                  requestBody.getForDuplicateProcessing(),
                                                                  requestBody.getEffectiveTime()));
            }
            else
            {
                response.setElement(handler.getUserIdentityByGUID(userId,
                                                                  userIdentityGUID,
                                                                  null,
                                                                  false,
                                                                  false,
                                                                  new Date()));
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
     * @param startsWith does the value start with the supplied string?
     * @param endsWith does the value end with the supplied string?
     * @param ignoreCase should the search ignore case?
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public UserIdentitiesResponse findUserIdentities(String            serverName,
                                                     String            viewServiceURLMarker,
                                                     boolean           startsWith,
                                                     boolean           endsWith,
                                                     boolean           ignoreCase,
                                                     int               startFrom,
                                                     int               pageSize,
                                                     FilterRequestBody requestBody)
    {
        final String methodName = "findUserIdentities";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        UserIdentitiesResponse response = new UserIdentitiesResponse();
        AuditLog                        auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            UserIdentityHandler handler = instanceHandler.getUserIdentityHandler(userId, serverName, viewServiceURLMarker, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.findUserIdentities(userId,
                                                                instanceHandler.getSearchString(requestBody.getFilter(), startsWith, endsWith, ignoreCase),
                                                                requestBody.getTemplateFilter(),
                                                                requestBody.getLimitResultsByStatus(),
                                                                requestBody.getAsOfTime(),
                                                                requestBody.getSequencingOrder(),
                                                                requestBody.getSequencingProperty(),
                                                                startFrom,
                                                                pageSize,
                                                                requestBody.getForLineage(),
                                                                requestBody.getForDuplicateProcessing(),
                                                                requestBody.getEffectiveTime()));
            }
            else
            {
                response.setElements(handler.findUserIdentities(userId,
                                                                instanceHandler.getSearchString(null, startsWith, endsWith, ignoreCase),
                                                                TemplateFilter.ALL,
                                                                null,
                                                                null,
                                                                SequencingOrder.CREATION_DATE_RECENT,
                                                                null,
                                                                startFrom,
                                                                pageSize,
                                                                false,
                                                                false,
                                                                new Date()));
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
