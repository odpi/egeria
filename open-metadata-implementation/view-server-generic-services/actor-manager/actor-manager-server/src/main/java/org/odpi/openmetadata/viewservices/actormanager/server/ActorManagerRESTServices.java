/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.actormanager.server;


import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.security.SecurityGroupMembershipProperties;
import org.odpi.openmetadata.commonservices.ffdc.rest.GetRequestBody;
import org.odpi.openmetadata.tokencontroller.TokenController;
import org.slf4j.LoggerFactory;


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
     * @param serverName  name of called server.
     * @param urlMarker   view service URL marker
     * @param requestBody properties for the actor profile.
     * @return unique identifier of the newly created element
     * InvalidParameterException  one of the parameters is invalid.
     * PropertyServerException    there is a problem retrieving information from the property server(s).
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public GUIDResponse createActorProfile(String                serverName,
                                           String                urlMarker,
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
                ActorProfileHandler handler = instanceHandler.getActorProfileHandler(userId, serverName, urlMarker, methodName);

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
     * @param serverName  calling user
     * @param urlMarker   view service URL marker
     * @param requestBody properties that override the template
     * @return unique identifier of the new metadata element
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GUIDResponse createActorProfileFromTemplate(String              serverName,
                                                       String              urlMarker,
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
                ActorProfileHandler handler = instanceHandler.getActorProfileHandler(userId, serverName, urlMarker, methodName);

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
     * @param serverName       name of called server.
     * @param urlMarker        view service URL marker
     * @param actorProfileGUID unique identifier of the actor profile (returned from create)
     * @param requestBody      properties for the new element.
     * @return boolean or
     * InvalidParameterException  one of the parameters is invalid.
     * PropertyServerException    there is a problem retrieving information from the property server(s).
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public BooleanResponse updateActorProfile(String                   serverName,
                                              String                   urlMarker,
                                              String                   actorProfileGUID,
                                              UpdateElementRequestBody requestBody)
    {
        final String methodName = "updateActorProfile";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        BooleanResponse response = new BooleanResponse();
        AuditLog        auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                ActorProfileHandler handler = instanceHandler.getActorProfileHandler(userId, serverName, urlMarker, methodName);

                if (requestBody.getProperties() instanceof ActorProfileProperties actorProfileProperties)
                {
                    response.setFlag(handler.updateActorProfile(userId,
                                                                actorProfileGUID,
                                                                requestBody,
                                                                actorProfileProperties));
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
     * Attach an asset to an IT profile.
     *
     * @param serverName    name of called server
     * @param urlMarker     view service URL marker
     * @param assetGUID     unique identifier of the asset
     * @param itProfileGUID unique identifier of the IT profile
     * @param requestBody   description of the relationship.
     * @return void or
     * InvalidParameterException  one of the parameters is null or invalid.
     * PropertyServerException    there is a problem retrieving information from the property server(s).
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse linkAssetToProfile(String                     serverName,
                                           String                     urlMarker,
                                           String                     assetGUID,
                                           String                     itProfileGUID,
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
            ActorProfileHandler handler = instanceHandler.getActorProfileHandler(userId, serverName, urlMarker, methodName);

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
                else if (requestBody.getProperties() == null)
                {
                    handler.linkAssetToProfile(userId,
                                               assetGUID,
                                               itProfileGUID,
                                               requestBody,
                                               null);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(ITInfrastructureProfileProperties.class.getName(), methodName);
                }
            }
            else
            {
                handler.linkAssetToProfile(userId,
                                           assetGUID,
                                           itProfileGUID,
                                           null,
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
     * @param serverName    name of called server
     * @param urlMarker     view service URL marker
     * @param assetGUID     unique identifier of the asset
     * @param itProfileGUID unique identifier of the IT profile
     * @param requestBody   description of the relationship.
     * @return void or
     * InvalidParameterException  one of the parameters is null or invalid.
     * PropertyServerException    there is a problem retrieving information from the property server(s).
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse detachAssetFromProfile(String                        serverName,
                                               String                        urlMarker,
                                               String                        assetGUID,
                                               String                        itProfileGUID,
                                               DeleteRelationshipRequestBody requestBody)
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

            ActorProfileHandler handler = instanceHandler.getActorProfileHandler(userId, serverName, urlMarker, methodName);

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
     * Delete an actor profile.
     *
     * @param serverName       name of called server
     * @param urlMarker        view service URL marker
     * @param actorProfileGUID unique identifier of the element to delete
     * @param requestBody      description of the relationship.
     * @return void or
     * InvalidParameterException  one of the parameters is null or invalid.
     * PropertyServerException    there is a problem retrieving information from the property server(s).
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse deleteActorProfile(String                   serverName,
                                           String                   urlMarker,
                                           String                   actorProfileGUID,
                                           DeleteElementRequestBody requestBody)
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

            ActorProfileHandler handler = instanceHandler.getActorProfileHandler(userId, serverName, urlMarker, methodName);

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
     * @param serverName  name of the service to route the request to
     * @param urlMarker   view service URL marker
     * @param requestBody string to find in the properties
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public OpenMetadataRootElementsResponse getActorProfilesByName(String            serverName,
                                                                   String            urlMarker,
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

            ActorProfileHandler handler = instanceHandler.getActorProfileHandler(userId, serverName, urlMarker, methodName);

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
     * @param serverName       name of the service to route the request to
     * @param urlMarker        view service URL marker
     * @param actorProfileGUID unique identifier of the required element
     * @param requestBody      string to find in the properties
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public OpenMetadataRootElementResponse getActorProfileByGUID(String         serverName,
                                                                 String         urlMarker,
                                                                 String         actorProfileGUID,
                                                                 GetRequestBody requestBody)
    {
        final String methodName = "getActorProfileByGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        OpenMetadataRootElementResponse response = new OpenMetadataRootElementResponse();
        AuditLog                        auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ActorProfileHandler handler = instanceHandler.getActorProfileHandler(userId, serverName, urlMarker, methodName);

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
     * @param serverName  name of the service to route the request to
     * @param urlMarker   view service URL marker
     * @param requestBody string to find in the properties
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public OpenMetadataRootElementsResponse findActorProfiles(String                  serverName,
                                                              String                  urlMarker,
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

            ActorProfileHandler handler = instanceHandler.getActorProfileHandler(userId, serverName, urlMarker, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.findActorProfiles(userId, requestBody.getSearchString(), requestBody));
            }
            else
            {
                response.setElements(handler.findActorProfiles(userId, null, null));
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
     * @param serverName  name of called server.
     * @param urlMarker   view service URL marker
     * @param requestBody properties for the actor role.
     * @return unique identifier of the newly created element
     * InvalidParameterException  one of the parameters is invalid.
     * PropertyServerException    there is a problem retrieving information from the property server(s).
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public GUIDResponse createActorRole(String                serverName,
                                        String                urlMarker,
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
                ActorRoleHandler handler = instanceHandler.getActorRoleHandler(userId, serverName, urlMarker, methodName);

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
     * @param serverName  calling user
     * @param urlMarker   view service URL marker
     * @param requestBody properties that override the template
     * @return unique identifier of the new metadata element
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GUIDResponse createActorRoleFromTemplate(String              serverName,
                                                    String              urlMarker,
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
                ActorRoleHandler handler = instanceHandler.getActorRoleHandler(userId, serverName, urlMarker, methodName);

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
     * @param serverName    name of called server.
     * @param urlMarker     view service URL marker
     * @param actorRoleGUID unique identifier of the actor role (returned from create)
     * @param requestBody   properties for the new element.
     * @return boolean or
     * InvalidParameterException  one of the parameters is invalid.
     * PropertyServerException    there is a problem retrieving information from the property server(s).
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public BooleanResponse updateActorRole(String                   serverName,
                                           String                   urlMarker,
                                           String                   actorRoleGUID,
                                           UpdateElementRequestBody requestBody)
    {
        final String methodName = "updateActorRole";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        BooleanResponse response = new BooleanResponse();
        AuditLog        auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                ActorRoleHandler handler = instanceHandler.getActorRoleHandler(userId, serverName, urlMarker, methodName);

                if (requestBody.getProperties() instanceof ActorRoleProperties actorRoleProperties)
                {
                    response.setFlag(handler.updateActorRole(userId,
                                                             actorRoleGUID,
                                                             requestBody,
                                                             actorRoleProperties));
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
     * @param serverName        name of called server
     * @param urlMarker         view service URL marker
     * @param personRoleGUID    unique identifier of the person role
     * @param personProfileGUID unique identifier of the person profile
     * @param requestBody       description of the relationship.
     * @return void or
     * InvalidParameterException  one of the parameters is null or invalid.
     * PropertyServerException    there is a problem retrieving information from the property server(s).
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse linkPersonRoleToProfile(String                     serverName,
                                                String                     urlMarker,
                                                String                     personRoleGUID,
                                                String                     personProfileGUID,
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
            ActorRoleHandler handler = instanceHandler.getActorRoleHandler(userId, serverName, urlMarker, methodName);

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
                handler.linkPersonRoleToProfile(userId,
                                                personRoleGUID,
                                                personProfileGUID,
                                                null,
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
     * @param serverName        name of called server
     * @param urlMarker         view service URL marker
     * @param personRoleGUID    unique identifier of the person role
     * @param personProfileGUID unique identifier of the person profile
     * @param requestBody       description of the relationship.
     * @return void or
     * InvalidParameterException  one of the parameters is null or invalid.
     * PropertyServerException    there is a problem retrieving information from the property server(s).
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse detachPersonRoleFromProfile(String                        serverName,
                                                    String                        urlMarker,
                                                    String                        personRoleGUID,
                                                    String                        personProfileGUID,
                                                    DeleteRelationshipRequestBody requestBody)
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

            ActorRoleHandler handler = instanceHandler.getActorRoleHandler(userId, serverName, urlMarker, methodName);

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
     * @param serverName      name of called server
     * @param urlMarker       view service URL marker
     * @param teamRoleGUID    unique identifier of the team role
     * @param teamProfileGUID unique identifier of the team profile
     * @param requestBody     description of the relationship.
     * @return void or
     * InvalidParameterException  one of the parameters is null or invalid.
     * PropertyServerException    there is a problem retrieving information from the property server(s).
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse linkTeamRoleToProfile(String                  serverName,
                                              String                  urlMarker,
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
            ActorRoleHandler handler = instanceHandler.getActorRoleHandler(userId, serverName, urlMarker, methodName);

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
                handler.linkTeamRoleToProfile(userId,
                                              teamRoleGUID,
                                              teamProfileGUID,
                                              null,
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
     * @param serverName      name of called server
     * @param urlMarker       view service URL marker
     * @param teamRoleGUID    unique identifier of the team role
     * @param teamProfileGUID unique identifier of the team profile
     * @param requestBody     description of the relationship.
     * @return void or
     * InvalidParameterException  one of the parameters is null or invalid.
     * PropertyServerException    there is a problem retrieving information from the property server(s).
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse detachTeamRoleFromProfile(String                        serverName,
                                                  String                        urlMarker,
                                                  String                        teamRoleGUID,
                                                  String                        teamProfileGUID,
                                                  DeleteRelationshipRequestBody requestBody)
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

            ActorRoleHandler handler = instanceHandler.getActorRoleHandler(userId, serverName, urlMarker, methodName);

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
     * @param serverName        name of called server
     * @param urlMarker         view service URL marker
     * @param itProfileRoleGUID unique identifier of the IT profile role
     * @param itProfileGUID     unique identifier of the IT profile
     * @param requestBody       description of the relationship.
     * @return void or
     * InvalidParameterException  one of the parameters is null or invalid.
     * PropertyServerException    there is a problem retrieving information from the property server(s).
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse linkITProfileRoleToProfile(String                    serverName,
                                                   String                     urlMarker,
                                                   String                     itProfileRoleGUID,
                                                   String                     itProfileGUID,
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

            ActorRoleHandler handler = instanceHandler.getActorRoleHandler(userId, serverName, urlMarker, methodName);

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
                else if (requestBody.getProperties() == null)
                {
                    handler.linkITProfileRoleToProfile(userId,
                                                       itProfileRoleGUID,
                                                       itProfileGUID,
                                                       requestBody,
                                                       null);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(ITProfileRoleAppointmentProperties.class.getName(), methodName);
                }
            }
            else
            {
                handler.linkITProfileRoleToProfile(userId,
                                                   itProfileRoleGUID,
                                                   itProfileGUID,
                                                   null,
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
     * @param serverName        name of called server
     * @param urlMarker         view service URL marker
     * @param itProfileRoleGUID unique identifier of the IT profile role
     * @param itProfileGUID     unique identifier of the IT profile
     * @param requestBody       description of the relationship.
     * @return void or
     * InvalidParameterException  one of the parameters is null or invalid.
     * PropertyServerException    there is a problem retrieving information from the property server(s).
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse detachITProfileRoleFromProfile(String                        serverName,
                                                       String                        urlMarker,
                                                       String                        itProfileRoleGUID,
                                                       String                        itProfileGUID,
                                                       DeleteRelationshipRequestBody requestBody)
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

            ActorRoleHandler handler = instanceHandler.getActorRoleHandler(userId, serverName, urlMarker, methodName);

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
     * @param serverName    name of called server
     * @param urlMarker     view service URL marker
     * @param actorRoleGUID unique identifier of the element to delete
     * @param requestBody   description of the relationship.
     * @return void or
     * InvalidParameterException  one of the parameters is null or invalid.
     * PropertyServerException    there is a problem retrieving information from the property server(s).
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse deleteActorRole(String                   serverName,
                                        String                   urlMarker,
                                        String                   actorRoleGUID,
                                        DeleteElementRequestBody requestBody)
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

            ActorRoleHandler handler = instanceHandler.getActorRoleHandler(userId, serverName, urlMarker, methodName);

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
     * @param serverName  name of the service to route the request to
     * @param urlMarker   view service URL marker
     * @param requestBody string to find in the properties
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public OpenMetadataRootElementsResponse getActorRolesByName(String            serverName,
                                                                String            urlMarker,
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

            ActorRoleHandler handler = instanceHandler.getActorRoleHandler(userId, serverName, urlMarker, methodName);

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
     * @param serverName    name of the service to route the request to
     * @param urlMarker     view service URL marker
     * @param actorRoleGUID unique identifier of the required element
     * @param requestBody   string to find in the properties
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public OpenMetadataRootElementResponse getActorRoleByGUID(String         serverName,
                                                              String         urlMarker,
                                                              String         actorRoleGUID,
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

            ActorRoleHandler handler = instanceHandler.getActorRoleHandler(userId, serverName, urlMarker, methodName);

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
     * @param serverName  name of the service to route the request to
     * @param urlMarker   view service URL marker
     * @param requestBody string to find in the properties
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public OpenMetadataRootElementsResponse findActorRoles(String                  serverName,
                                                           String                  urlMarker,
                                                           SearchStringRequestBody requestBody)
    {
        final String methodName = "findActorRoles";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog                         auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ActorRoleHandler handler = instanceHandler.getActorRoleHandler(userId, serverName, urlMarker, methodName);

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
     * @param serverName  name of called server.
     * @param urlMarker   view service URL marker
     * @param requestBody properties for the user identity.
     * @return unique identifier of the newly created element
     * InvalidParameterException  one of the parameters is invalid.
     * PropertyServerException    there is a problem retrieving information from the property server(s).
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public GUIDResponse createUserIdentity(String                serverName,
                                           String                urlMarker,
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
                UserIdentityHandler handler = instanceHandler.getUserIdentityHandler(userId, serverName, urlMarker, methodName);

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
     * @param serverName  calling user
     * @param urlMarker   view service URL marker
     * @param requestBody properties that override the template
     * @return unique identifier of the new metadata element
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GUIDResponse createUserIdentityFromTemplate(String              serverName,
                                                       String              urlMarker,
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
                UserIdentityHandler handler = instanceHandler.getUserIdentityHandler(userId, serverName, urlMarker, methodName);

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
     * @param serverName       name of called server.
     * @param urlMarker        view service URL marker
     * @param userIdentityGUID unique identifier of the user identity (returned from create)
     * @param requestBody      properties for the new element.
     * @return boolean or
     * InvalidParameterException  one of the parameters is invalid.
     * PropertyServerException    there is a problem retrieving information from the property server(s).
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public BooleanResponse updateUserIdentity(String                   serverName,
                                              String                   urlMarker,
                                              String                   userIdentityGUID,
                                              UpdateElementRequestBody requestBody)
    {
        final String methodName = "updateUserIdentity";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        BooleanResponse response = new BooleanResponse();
        AuditLog        auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                UserIdentityHandler handler = instanceHandler.getUserIdentityHandler(userId, serverName, urlMarker, methodName);

                if (requestBody.getProperties() instanceof UserIdentityProperties userIdentityProperties)
                {
                    response.setFlag(handler.updateUserIdentity(userId,
                                                                userIdentityGUID,
                                                                requestBody,
                                                                userIdentityProperties));
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
     * @param serverName       name of called server
     * @param urlMarker        view service URL marker
     * @param userIdentityGUID unique identifier of the parent
     * @param profileGUID      unique identifier of the actor profile
     * @param requestBody      description of the relationship.
     * @return void or
     * InvalidParameterException  one of the parameters is null or invalid.
     * PropertyServerException    there is a problem retrieving information from the property server(s).
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse linkIdentityToProfile(String                     serverName,
                                              String                     urlMarker,
                                              String                     userIdentityGUID,
                                              String                     profileGUID,
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
            UserIdentityHandler handler = instanceHandler.getUserIdentityHandler(userId, serverName, urlMarker, methodName);

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
                handler.linkIdentityToProfile(userId,
                                              userIdentityGUID,
                                              profileGUID,
                                              null,
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
     * @param serverName       name of called server
     * @param urlMarker        view service URL marker
     * @param userIdentityGUID unique identifier of the parent actor profile
     * @param profileGUID      unique identifier of the nested actor profile
     * @param requestBody      description of the relationship.
     * @return void or
     * InvalidParameterException  one of the parameters is null or invalid.
     * PropertyServerException    there is a problem retrieving information from the property server(s).
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse detachProfileIdentity(String                        serverName,
                                              String                        urlMarker,
                                              String                        userIdentityGUID,
                                              String                        profileGUID,
                                              DeleteRelationshipRequestBody requestBody)
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

            UserIdentityHandler handler = instanceHandler.getUserIdentityHandler(userId, serverName, urlMarker, methodName);

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
     * @param serverName       name of called server
     * @param urlMarker        view service URL marker
     * @param userIdentityGUID unique identifier of the user identity
     * @param requestBody      description of the relationship.
     * @return void or
     * InvalidParameterException  one of the parameters is null or invalid.
     * PropertyServerException    there is a problem retrieving information from the property server(s).
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse addSecurityGroupMembership(String                       serverName,
                                                   String                       urlMarker,
                                                   String                       userIdentityGUID,
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
            UserIdentityHandler handler = instanceHandler.getUserIdentityHandler(userId, serverName, urlMarker, methodName);

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
                handler.addSecurityGroupMembership(userId, userIdentityGUID, null, null);
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
     * @param serverName       name of called server
     * @param urlMarker        view service URL marker
     * @param userIdentityGUID unique identifier of the user identity
     * @param requestBody      description of the relationship.
     * @return void or
     * InvalidParameterException  one of the parameters is null or invalid.
     * PropertyServerException    there is a problem retrieving information from the property server(s).
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse updateSecurityGroupMembership(String                          serverName,
                                                      String                          urlMarker,
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
            UserIdentityHandler handler = instanceHandler.getUserIdentityHandler(userId, serverName, urlMarker, methodName);

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
                handler.addSecurityGroupMembership(userId, userIdentityGUID, null, null);
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
     * @param serverName       name of called server
     * @param urlMarker        view service URL marker
     * @param userIdentityGUID unique identifier of the first user identity
     * @param requestBody      description of the relationship.
     * @return void or
     * InvalidParameterException  one of the parameters is null or invalid.
     * PropertyServerException    there is a problem retrieving information from the property server(s).
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse removeAllSecurityGroupMembership(String                    serverName,
                                                         String                    urlMarker,
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

            UserIdentityHandler handler = instanceHandler.getUserIdentityHandler(userId, serverName, urlMarker, methodName);

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
     * @param serverName       name of called server
     * @param urlMarker        view service URL marker
     * @param userIdentityGUID unique identifier of the element to delete
     * @param requestBody      description of the relationship.
     * @return void or
     * InvalidParameterException  one of the parameters is null or invalid.
     * PropertyServerException    there is a problem retrieving information from the property server(s).
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse deleteUserIdentity(String                   serverName,
                                           String                   urlMarker,
                                           String                   userIdentityGUID,
                                           DeleteElementRequestBody requestBody)
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

            UserIdentityHandler handler = instanceHandler.getUserIdentityHandler(userId, serverName, urlMarker, methodName);

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
     * @param serverName  name of the service to route the request to
     * @param urlMarker   view service URL marker
     * @param requestBody string to find in the properties
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public OpenMetadataRootElementsResponse getUserIdentitiesByName(String            serverName,
                                                                    String            urlMarker,
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

            UserIdentityHandler handler = instanceHandler.getUserIdentityHandler(userId, serverName, urlMarker, methodName);

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
     * @param serverName       name of the service to route the request to
     * @param urlMarker        view service URL marker
     * @param userIdentityGUID unique identifier of the required element
     * @param requestBody      string to find in the properties
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public OpenMetadataRootElementResponse getUserIdentityByGUID(String         serverName,
                                                                 String         urlMarker,
                                                                 String         userIdentityGUID,
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

            UserIdentityHandler handler = instanceHandler.getUserIdentityHandler(userId, serverName, urlMarker, methodName);

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
     * @param serverName  name of the service to route the request to
     * @param urlMarker   view service URL marker
     * @param requestBody string to find in the properties
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public OpenMetadataRootElementsResponse findUserIdentities(String                  serverName,
                                                               String                  urlMarker,
                                                               SearchStringRequestBody requestBody)
    {
        final String methodName = "findUserIdentities";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog                         auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            UserIdentityHandler handler = instanceHandler.getUserIdentityHandler(userId, serverName, urlMarker, methodName);


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


    /**
     * Attach a actor to an element such as a team, project, community, that defines its scope.
     *
     * @param serverName       name of called server
     * @param urlMarker        view service URL marker
     * @param scopeElementGUID unique identifier of the element
     * @param actorGUID        unique identifier of the actor
     * @param requestBody      description of the relationship.
     * @return void or
     * InvalidParameterException  one of the parameters is null or invalid.
     * PropertyServerException    there is a problem retrieving information from the property server(s).
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse linkAssignmentScope(String                     serverName,
                                            String                     urlMarker,
                                            String                     scopeElementGUID,
                                            String                     actorGUID,
                                            NewRelationshipRequestBody requestBody)
    {
        final String methodName = "linkAssignmentScope";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            GovernanceDefinitionHandler handler = instanceHandler.getGovernanceDefinitionHandler(userId, serverName, urlMarker, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof AssignmentScopeProperties properties)
                {
                    handler.linkAssignmentScope(userId,
                                                scopeElementGUID,
                                                actorGUID,
                                                requestBody,
                                                properties);
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.linkAssignmentScope(userId,
                                                scopeElementGUID,
                                                actorGUID,
                                                requestBody,
                                                null);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(AssignmentScopeProperties.class.getName(), methodName);
                }
            }
            else
            {
                handler.linkAssignmentScope(userId,
                                            scopeElementGUID,
                                            actorGUID,
                                            requestBody,
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
     * Detach an actor from its scope.
     *
     * @param serverName       name of called server
     * @param urlMarker        view service URL marker
     * @param scopeElementGUID unique identifier of the element
     * @param actorGUID        unique identifier of the actor
     * @param requestBody      description of the relationship.
     * @return void or
     * InvalidParameterException  one of the parameters is null or invalid.
     * PropertyServerException    there is a problem retrieving information from the property server(s).
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse detachAssignmentScope(String                        serverName,
                                              String                        urlMarker,
                                              String                        scopeElementGUID,
                                              String                        actorGUID,
                                              DeleteRelationshipRequestBody requestBody)
    {
        final String methodName = "detachAssignmentScope";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            GovernanceDefinitionHandler handler = instanceHandler.getGovernanceDefinitionHandler(userId, serverName, urlMarker, methodName);

            handler.detachAssignmentScope(userId,
                                          scopeElementGUID,
                                          actorGUID,
                                          requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Create contact details for a profile.
     *
     * @param serverName  name of called server.
     * @param urlMarker   view service URL marker
     * @param requestBody properties for the contact details.
     * @return unique identifier of the newly created element
     * InvalidParameterException  one of the parameters is invalid.
     * PropertyServerException    there is a problem retrieving information from the property server(s).
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public GUIDResponse createContactDetails(String                serverName,
                                             String                urlMarker,
                                             NewElementRequestBody requestBody)
    {
        final String methodName = "createContactDetails";

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
                ContactDetailsHandler handler = instanceHandler.getContactDetailsHandler(userId, serverName, urlMarker, methodName);

                if (requestBody.getProperties() instanceof ContactDetailsProperties contactDetailsProperties)
                {
                    response.setGUID(handler.createContactDetails(userId,
                                                                  requestBody,
                                                                  requestBody.getInitialClassifications(),
                                                                  contactDetailsProperties,
                                                                  requestBody.getParentRelationshipProperties()));
                }
                else if (requestBody.getProperties() == null)
                {
                    response.setGUID(handler.createContactDetails(userId,
                                                                  requestBody,
                                                                  requestBody.getInitialClassifications(),
                                                                  null,
                                                                  requestBody.getParentRelationshipProperties()));
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(ContactDetailsProperties.class.getName(), methodName);
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
     * Create a new metadata element to represent a contact details using an existing metadata element as a template.
     * The template defines additional classifications and relationships that should be added to the new element.
     *
     * @param serverName  calling user
     * @param urlMarker   view service URL marker
     * @param requestBody properties that override the template
     * @return unique identifier of the new metadata element
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GUIDResponse createContactDetailsFromTemplate(String              serverName,
                                                         String              urlMarker,
                                                         TemplateRequestBody requestBody)
    {
        final String methodName = "createContactDetailsFromTemplate";

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
                ContactDetailsHandler handler = instanceHandler.getContactDetailsHandler(userId, serverName, urlMarker, methodName);

                response.setGUID(handler.createContactDetailsFromTemplate(userId,
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
     * Update the properties of a contact details.
     *
     * @param serverName         name of called server.
     * @param urlMarker          view service URL marker
     * @param contactDetailsGUID unique identifier of the contact details (returned from create)
     * @param requestBody        properties for the new element.
     * @return boolean or
     * InvalidParameterException  one of the parameters is invalid.
     * PropertyServerException    there is a problem retrieving information from the property server(s).
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public BooleanResponse updateContactDetails(String                   serverName,
                                                String                   urlMarker,
                                                String                   contactDetailsGUID,
                                                UpdateElementRequestBody requestBody)
    {
        final String methodName = "updateContactDetails";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        BooleanResponse response = new BooleanResponse();
        AuditLog        auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                ContactDetailsHandler handler = instanceHandler.getContactDetailsHandler(userId, serverName, urlMarker, methodName);

                if (requestBody.getProperties() instanceof ContactDetailsProperties contactDetailsProperties)
                {
                    response.setFlag(handler.updateContactDetails(userId,
                                                                  contactDetailsGUID,
                                                                  requestBody,
                                                                  contactDetailsProperties));
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(ContactDetailsProperties.class.getName(), methodName);
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
     * Attach an element to its contact details.
     *
     * @param serverName         name of called server
     * @param urlMarker          view service URL marker
     * @param elementGUID unique identifier of the parent element.
     * @param contactDetailsGUID      unique identifier of the nested data field.
     * @param requestBody        description of the relationship.
     * @return void or
     * InvalidParameterException  one of the parameters is null or invalid.
     * PropertyServerException    there is a problem retrieving information from the property server(s).
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse linkContactDetails(String                     serverName,
                                           String                     urlMarker,
                                           String                     elementGUID,
                                           String                     contactDetailsGUID,
                                           NewRelationshipRequestBody requestBody)
    {
        final String methodName = "linkContactDetails";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            ContactDetailsHandler handler = instanceHandler.getContactDetailsHandler(userId, serverName, urlMarker, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof ContactThroughProperties memberDataFieldProperties)
                {
                    handler.linkContactDetails(userId,
                                                elementGUID,
                                                contactDetailsGUID,
                                                requestBody,
                                                memberDataFieldProperties);
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.linkContactDetails(userId,
                                                elementGUID,
                                                contactDetailsGUID,
                                                requestBody,
                                                null);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(ContactThroughProperties.class.getName(), methodName);
                }
            }
            else
            {
                handler.linkContactDetails(userId,
                                            elementGUID,
                                            contactDetailsGUID,
                                            requestBody,
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
     * Detach an element from its contact details.
     *
     * @param serverName         name of called server
     * @param urlMarker          view service URL marker
     * @param elementGUID unique identifier of the parent element
     * @param contactDetailsGUID      unique identifier of the contact details
     * @param requestBody        description of the relationship.
     * @return void or
     * InvalidParameterException  one of the parameters is null or invalid.
     * PropertyServerException    there is a problem retrieving information from the property server(s).
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse detachContactDetails(String                        serverName,
                                              String                        urlMarker,
                                              String                        elementGUID,
                                              String                        contactDetailsGUID,
                                              DeleteRelationshipRequestBody requestBody)
    {
        final String methodName = "detachContactDetails";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ContactDetailsHandler handler = instanceHandler.getContactDetailsHandler(userId, serverName, urlMarker, methodName);

            handler.detachContactDetails(userId, elementGUID, contactDetailsGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Delete a contact details.
     *
     * @param serverName         name of called server
     * @param urlMarker          view service URL marker
     * @param contactDetailsGUID unique identifier of the element to delete
     * @param requestBody        description of the relationship.
     * @return void or
     * InvalidParameterException  one of the parameters is null or invalid.
     * PropertyServerException    there is a problem retrieving information from the property server(s).
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse deleteContactDetails(String                   serverName,
                                             String                   urlMarker,
                                             String                   contactDetailsGUID,
                                             DeleteElementRequestBody requestBody)
    {
        final String methodName = "deleteContactDetails";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ContactDetailsHandler handler = instanceHandler.getContactDetailsHandler(userId, serverName, urlMarker, methodName);

            handler.deleteContactDetails(userId, contactDetailsGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Retrieve the list of contact details metadata elements that contain the search string.
     *
     * @param serverName  name of the service to route the request to
     * @param urlMarker   view service URL marker
     * @param requestBody string to find in the properties
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public OpenMetadataRootElementsResponse getContactDetailsByName(String            serverName,
                                                                    String            urlMarker,
                                                                    FilterRequestBody requestBody)
    {
        final String methodName = "getContactDetailsByName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog                         auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ContactDetailsHandler handler = instanceHandler.getContactDetailsHandler(userId, serverName, urlMarker, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.getContactDetailsByName(userId, requestBody.getFilter(), requestBody));
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
     * Retrieve the list of contact details metadata elements that contain the search string.
     *
     * @param serverName         name of the service to route the request to
     * @param urlMarker          view service URL marker
     * @param contactDetailsGUID unique identifier of the required element
     * @param requestBody        string to find in the properties
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public OpenMetadataRootElementResponse getContactDetailsByGUID(String         serverName,
                                                                   String         urlMarker,
                                                                   String         contactDetailsGUID,
                                                                   GetRequestBody requestBody)
    {
        final String methodName = "getContactDetailsByGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        OpenMetadataRootElementResponse response = new OpenMetadataRootElementResponse();
        AuditLog                        auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ContactDetailsHandler handler = instanceHandler.getContactDetailsHandler(userId, serverName, urlMarker, methodName);

            response.setElement(handler.getContactDetailsByGUID(userId, contactDetailsGUID, requestBody));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Retrieve the list of contact details metadata elements that contain the search string.
     *
     * @param serverName  name of the service to route the request to
     * @param urlMarker   view service URL marker
     * @param requestBody string to find in the properties
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public OpenMetadataRootElementsResponse findContactDetails(String                  serverName,
                                                               String                  urlMarker,
                                                               SearchStringRequestBody requestBody)
    {
        final String methodName = "findContactDetails";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog                         auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ContactDetailsHandler handler = instanceHandler.getContactDetailsHandler(userId, serverName, urlMarker, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.findContactDetails(userId,
                                                                requestBody.getSearchString(),
                                                                requestBody));
            }
            else
            {
                response.setElements(handler.findContactDetails(userId,
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
}
