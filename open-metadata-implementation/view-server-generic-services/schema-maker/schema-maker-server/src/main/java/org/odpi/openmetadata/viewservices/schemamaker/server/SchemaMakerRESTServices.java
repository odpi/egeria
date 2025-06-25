/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.schemamaker.server;


import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.enums.SequencingOrder;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.locations.ProfileLocationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.*;
import org.odpi.openmetadata.frameworks.openmetadata.search.TemplateFilter;
import org.odpi.openmetadata.frameworkservices.omf.client.handlers.*;
import org.odpi.openmetadata.frameworkservices.omf.rest.AnyTimeRequestBody;
import org.odpi.openmetadata.tokencontroller.TokenController;
import org.slf4j.LoggerFactory;

import java.util.Date;


/**
 * The SchemaMakerRESTServices provides the server-side implementation of the Schema Maker Open Metadata
 * View Service (OMVS).  This interface provides access to a person's profile, roles and network.
 */
public class SchemaMakerRESTServices extends TokenController
{
    private static final SchemaMakerInstanceHandler instanceHandler = new SchemaMakerInstanceHandler();

    private static final RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();

    private static final RESTCallLogger restCallLogger = new RESTCallLogger(LoggerFactory.getLogger(SchemaMakerRESTServices.class),
                                                                            instanceHandler.getServiceName());

    /**
     * Default constructor
     */
    public SchemaMakerRESTServices()
    {
    }


    /**
     * Create a schema type.
     *
     * @param serverName                 name of called server.
     * @param viewServiceURLMarker  view service URL marker
     * @param requestBody             properties for the schema type.
     *
     * @return unique identifier of the newly created element
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public GUIDResponse createSchemaType(String                serverName,
                                           String                viewServiceURLMarker,
                                           NewElementRequestBody requestBody)
    {
        final String methodName = "createSchemaType";

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
                SchemaTypeHandler handler = instanceHandler.getSchemaTypeHandler(userId, serverName, viewServiceURLMarker, methodName);

                if (requestBody.getProperties() instanceof SchemaTypeProperties schemaTypeProperties)
                {
                    response.setGUID(handler.createSchemaType(userId,
                                                                requestBody.getExternalSourceGUID(),
                                                                requestBody.getExternalSourceName(),
                                                                requestBody.getAnchorGUID(),
                                                                requestBody.getIsOwnAnchor(),
                                                                requestBody.getAnchorScopeGUID(),
                                                                schemaTypeProperties,
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
                    response.setGUID(handler.createSchemaType(userId,
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
                    restExceptionHandler.handleInvalidPropertiesObject(SchemaTypeProperties.class.getName(), methodName);
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
     * Create a new metadata element to represent a schema type using an existing metadata element as a template.
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
    public GUIDResponse createSchemaTypeFromTemplate(String              serverName,
                                                       String              viewServiceURLMarker,
                                                       TemplateRequestBody requestBody)
    {
        final String methodName = "createSchemaTypeFromTemplate";

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
                SchemaTypeHandler handler = instanceHandler.getSchemaTypeHandler(userId, serverName, viewServiceURLMarker, methodName);

                response.setGUID(handler.createSchemaTypeFromTemplate(userId,
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
     * Update the properties of a schema type.
     *
     * @param serverName         name of called server.
     * @param viewServiceURLMarker  view service URL marker
     * @param schemaTypeGUID unique identifier of the schema type (returned from create)
     * @param replaceAllProperties flag to indicate whether to completely replace the existing properties with the new properties, or just update
     *                          the individual properties specified on the request.
     * @param requestBody     properties for the new element.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse updateSchemaType(String                   serverName,
                                           String                   viewServiceURLMarker,
                                           String                   schemaTypeGUID,
                                           boolean                  replaceAllProperties,
                                           UpdateElementRequestBody requestBody)
    {
        final String methodName = "updateSchemaType";

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
                SchemaTypeHandler handler = instanceHandler.getSchemaTypeHandler(userId, serverName, viewServiceURLMarker, methodName);

                if (requestBody.getProperties() instanceof SchemaTypeProperties schemaTypeProperties)
                {
                    handler.updateSchemaType(userId,
                                               requestBody.getExternalSourceGUID(),
                                               requestBody.getExternalSourceName(),
                                               schemaTypeGUID,
                                               replaceAllProperties,
                                               schemaTypeProperties,
                                               requestBody.getForLineage(),
                                               requestBody.getForDuplicateProcessing(),
                                               requestBody.getEffectiveTime());
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.updateSchemaType(userId,
                                               requestBody.getExternalSourceGUID(),
                                               requestBody.getExternalSourceName(),
                                               schemaTypeGUID,
                                               replaceAllProperties,
                                               null,
                                               requestBody.getForLineage(),
                                               requestBody.getForDuplicateProcessing(),
                                               requestBody.getEffectiveTime());
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(SchemaTypeProperties.class.getName(), methodName);
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
     * @param schemaTypeGUID       unique identifier of the schema type
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
                                              String                  schemaTypeGUID,
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
            SchemaTypeHandler handler = instanceHandler.getSchemaTypeHandler(userId, serverName, viewServiceURLMarker, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof ProfileLocationProperties profileLocationProperties)
                {
                    handler.linkLocationToProfile(userId,
                                                  requestBody.getExternalSourceGUID(),
                                                  requestBody.getExternalSourceName(),
                                                  schemaTypeGUID,
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
                                                  schemaTypeGUID,
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
                                              schemaTypeGUID,
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
     * Detach a schema type from a location.
     *
     * @param serverName         name of called server
     * @param viewServiceURLMarker  view service URL marker
     * @param schemaTypeGUID       unique identifier of the schema type
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
                                                  String                    schemaTypeGUID,
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

            SchemaTypeHandler handler = instanceHandler.getSchemaTypeHandler(userId, serverName, viewServiceURLMarker, methodName);

            if (requestBody != null)
            {
                handler.detachLocationFromProfile(userId,
                                                  requestBody.getExternalSourceGUID(),
                                                  requestBody.getExternalSourceName(),
                                                  schemaTypeGUID,
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
                                                  schemaTypeGUID,
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
     * @param personOneGUID unique identifier of the first schema type
     * @param personTwoGUID unique identifier of the second schema type
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
            SchemaTypeHandler handler = instanceHandler.getSchemaTypeHandler(userId, serverName, viewServiceURLMarker, methodName);

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

            SchemaTypeHandler handler = instanceHandler.getSchemaTypeHandler(userId, serverName, viewServiceURLMarker, methodName);

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
            SchemaTypeHandler handler = instanceHandler.getSchemaTypeHandler(userId, serverName, viewServiceURLMarker, methodName);

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

            SchemaTypeHandler handler = instanceHandler.getSchemaTypeHandler(userId, serverName, viewServiceURLMarker, methodName);

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
            SchemaTypeHandler handler = instanceHandler.getSchemaTypeHandler(userId, serverName, viewServiceURLMarker, methodName);

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

            SchemaTypeHandler handler = instanceHandler.getSchemaTypeHandler(userId, serverName, viewServiceURLMarker, methodName);

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
            SchemaTypeHandler handler = instanceHandler.getSchemaTypeHandler(userId, serverName, viewServiceURLMarker, methodName);

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

            SchemaTypeHandler handler = instanceHandler.getSchemaTypeHandler(userId, serverName, viewServiceURLMarker, methodName);

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
            SchemaTypeHandler handler = instanceHandler.getSchemaTypeHandler(userId, serverName, viewServiceURLMarker, methodName);

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

            SchemaTypeHandler handler = instanceHandler.getSchemaTypeHandler(userId, serverName, viewServiceURLMarker, methodName);

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
     * Delete a schema type.
     *
     * @param serverName         name of called server
     * @param viewServiceURLMarker  view service URL marker
     * @param schemaTypeGUID  unique identifier of the element to delete
     * @param cascadedDelete ca schema types be deleted if data fields are attached?
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse deleteSchemaType(String                    serverName,
                                           String                    viewServiceURLMarker,
                                           String                    schemaTypeGUID,
                                           boolean                   cascadedDelete,
                                           MetadataSourceRequestBody requestBody)
    {
        final String methodName = "deleteSchemaType";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SchemaTypeHandler handler = instanceHandler.getSchemaTypeHandler(userId, serverName, viewServiceURLMarker, methodName);

            if (requestBody != null)
            {
                handler.deleteSchemaType(userId,
                                           requestBody.getExternalSourceGUID(),
                                           requestBody.getExternalSourceName(),
                                           schemaTypeGUID,
                                           cascadedDelete,
                                           requestBody.getForLineage(),
                                           requestBody.getForDuplicateProcessing(),
                                           requestBody.getEffectiveTime());
            }
            else
            {
                handler.deleteSchemaType(userId,
                                           null,
                                           null,
                                           schemaTypeGUID,
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
     * Retrieve the list of schema type metadata elements that contain the search string.
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
    public SchemaTypesResponse getSchemaTypesByName(String            serverName,
                                                        String            viewServiceURLMarker,
                                                        int               startFrom,
                                                        int               pageSize,
                                                        FilterRequestBody requestBody)
    {
        final String methodName = "getSchemaTypesByName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        SchemaTypesResponse response = new SchemaTypesResponse();
        AuditLog                        auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SchemaTypeHandler handler = instanceHandler.getSchemaTypeHandler(userId, serverName, viewServiceURLMarker, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.getSchemaTypesByName(userId,
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
     * Retrieve the list of schema type metadata elements that contain the search string.
     *
     * @param serverName name of the service to route the request to
     * @param viewServiceURLMarker  view service URL marker
     * @param schemaTypeGUID    unique identifier of the required element
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public SchemaTypeResponse getSchemaTypeByGUID(String             serverName,
                                                      String             viewServiceURLMarker,
                                                      String             schemaTypeGUID,
                                                      AnyTimeRequestBody requestBody)
    {
        final String methodName = "getSchemaTypeByGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        SchemaTypeResponse response = new SchemaTypeResponse();
        AuditLog                      auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SchemaTypeHandler handler = instanceHandler.getSchemaTypeHandler(userId, serverName, viewServiceURLMarker, methodName);

            if (requestBody != null)
            {
                response.setElement(handler.getSchemaTypeByGUID(userId,
                                                                  schemaTypeGUID,
                                                                  requestBody.getAsOfTime(),
                                                                  requestBody.getForLineage(),
                                                                  requestBody.getForDuplicateProcessing(),
                                                                  requestBody.getEffectiveTime()));
            }
            else
            {
                response.setElement(handler.getSchemaTypeByGUID(userId,
                                                                  schemaTypeGUID,
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
     * Retrieve the list of schema type metadata elements that contain the search string.
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
    public SchemaTypesResponse findSchemaTypes(String            serverName,
                                                   String            viewServiceURLMarker,
                                                   boolean           startsWith,
                                                   boolean           endsWith,
                                                   boolean           ignoreCase,
                                                   int               startFrom,
                                                   int               pageSize,
                                                   FilterRequestBody requestBody)
    {
        final String methodName = "findSchemaTypes";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        SchemaTypesResponse response = new SchemaTypesResponse();
        AuditLog                        auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SchemaTypeHandler handler = instanceHandler.getSchemaTypeHandler(userId, serverName, viewServiceURLMarker, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.findSchemaTypes(userId,
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
                response.setElements(handler.findSchemaTypes(userId,
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
     * Create a schemaAttribute.
     *
     * @param serverName                 name of called server.
     * @param viewServiceURLMarker  view service URL marker
     * @param requestBody             properties for the schema attribute.
     *
     * @return unique identifier of the newly created element
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public GUIDResponse createSchemaAttribute(String                serverName,
                                        String                viewServiceURLMarker,
                                        NewElementRequestBody requestBody)
    {
        final String methodName = "createSchemaAttribute";

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
                SchemaAttributeHandler handler = instanceHandler.getSchemaAttributeHandler(userId, serverName, viewServiceURLMarker, methodName);

                if (requestBody.getProperties() instanceof SchemaAttributeProperties schemaAttributeProperties)
                {
                    response.setGUID(handler.createSchemaAttribute(userId,
                                                             requestBody.getExternalSourceGUID(),
                                                             requestBody.getExternalSourceName(),
                                                             requestBody.getAnchorGUID(),
                                                             requestBody.getIsOwnAnchor(),
                                                             requestBody.getAnchorScopeGUID(),
                                                             schemaAttributeProperties,
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
                    response.setGUID(handler.createSchemaAttribute(userId,
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
                    restExceptionHandler.handleInvalidPropertiesObject(SchemaAttributeProperties.class.getName(), methodName);
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
     * Create a new metadata element to represent a schemaAttribute using an existing metadata element as a template.
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
    public GUIDResponse createSchemaAttributeFromTemplate(String              serverName,
                                                    String              viewServiceURLMarker,
                                                    TemplateRequestBody requestBody)
    {
        final String methodName = "createSchemaAttributeFromTemplate";

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
                SchemaAttributeHandler handler = instanceHandler.getSchemaAttributeHandler(userId, serverName, viewServiceURLMarker, methodName);

                response.setGUID(handler.createSchemaAttributeFromTemplate(userId,
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
     * Update the properties of a schemaAttribute.
     *
     * @param serverName         name of called server.
     * @param viewServiceURLMarker  view service URL marker
     * @param schemaAttributeGUID unique identifier of the schema attribute (returned from create)
     * @param replaceAllProperties flag to indicate whether to completely replace the existing properties with the new properties, or just update
     *                          the individual properties specified on the request.
     * @param requestBody     properties for the new element.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse updateSchemaAttribute(String                   serverName,
                                        String                   viewServiceURLMarker,
                                        String                   schemaAttributeGUID,
                                        boolean                  replaceAllProperties,
                                        UpdateElementRequestBody requestBody)
    {
        final String methodName = "updateSchemaAttribute";

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
                SchemaAttributeHandler handler = instanceHandler.getSchemaAttributeHandler(userId, serverName, viewServiceURLMarker, methodName);

                if (requestBody.getProperties() instanceof SchemaAttributeProperties schemaAttributeProperties)
                {
                    handler.updateSchemaAttribute(userId,
                                            requestBody.getExternalSourceGUID(),
                                            requestBody.getExternalSourceName(),
                                            schemaAttributeGUID,
                                            replaceAllProperties,
                                            schemaAttributeProperties,
                                            requestBody.getForLineage(),
                                            requestBody.getForDuplicateProcessing(),
                                            requestBody.getEffectiveTime());
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.updateSchemaAttribute(userId,
                                            requestBody.getExternalSourceGUID(),
                                            requestBody.getExternalSourceName(),
                                            schemaAttributeGUID,
                                            replaceAllProperties,
                                            null,
                                            requestBody.getForLineage(),
                                            requestBody.getForDuplicateProcessing(),
                                            requestBody.getEffectiveTime());
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(SchemaAttributeProperties.class.getName(), methodName);
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
     * Delete a schemaAttribute.
     *
     * @param serverName         name of called server
     * @param viewServiceURLMarker  view service URL marker
     * @param schemaAttributeGUID  unique identifier of the element to delete
     * @param cascadedDelete ca schemaAttributes be deleted if data fields are attached?
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse deleteSchemaAttribute(String                    serverName,
                                                   String                    viewServiceURLMarker,
                                                   String                    schemaAttributeGUID,
                                                   boolean                   cascadedDelete,
                                                   MetadataSourceRequestBody requestBody)
    {
        final String methodName = "deleteSchemaAttribute";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SchemaAttributeHandler handler = instanceHandler.getSchemaAttributeHandler(userId, serverName, viewServiceURLMarker, methodName);

            if (requestBody != null)
            {
                handler.deleteSchemaAttribute(userId,
                                                   requestBody.getExternalSourceGUID(),
                                                   requestBody.getExternalSourceName(),
                                                   schemaAttributeGUID,
                                                   cascadedDelete,
                                                   requestBody.getForLineage(),
                                                   requestBody.getForDuplicateProcessing(),
                                                   requestBody.getEffectiveTime());
            }
            else
            {
                handler.deleteSchemaAttribute(userId,
                                                   null,
                                                   null,
                                                   schemaAttributeGUID,
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
     * Retrieve the list of schema attribute metadata elements that contain the search string.
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
    public SchemaAttributesResponse getSchemaAttributesByName(String            serverName,
                                                                        String            viewServiceURLMarker,
                                                                        int               startFrom,
                                                                        int               pageSize,
                                                                        FilterRequestBody requestBody)
    {
        final String methodName = "getSchemaAttributesByName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        SchemaAttributesResponse response = new SchemaAttributesResponse();
        AuditLog                        auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SchemaAttributeHandler handler = instanceHandler.getSchemaAttributeHandler(userId, serverName, viewServiceURLMarker, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.getSchemaAttributesByName(userId,
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
     * Retrieve the list of schema attribute metadata elements that contain the search string.
     *
     * @param serverName name of the service to route the request to
     * @param viewServiceURLMarker  view service URL marker
     * @param schemaAttributeGUID    unique identifier of the required element
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public SchemaAttributeResponse getSchemaAttributeByGUID(String             serverName,
                                                                      String             viewServiceURLMarker,
                                                                      String             schemaAttributeGUID,
                                                                      AnyTimeRequestBody requestBody)
    {
        final String methodName = "getSchemaAttributeByGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        SchemaAttributeResponse response = new SchemaAttributeResponse();
        AuditLog                      auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SchemaAttributeHandler handler = instanceHandler.getSchemaAttributeHandler(userId, serverName, viewServiceURLMarker, methodName);

            if (requestBody != null)
            {
                response.setElement(handler.getSchemaAttributeByGUID(userId,
                                                                          schemaAttributeGUID,
                                                                          requestBody.getAsOfTime(),
                                                                          requestBody.getForLineage(),
                                                                          requestBody.getForDuplicateProcessing(),
                                                                          requestBody.getEffectiveTime()));
            }
            else
            {
                response.setElement(handler.getSchemaAttributeByGUID(userId,
                                                                          schemaAttributeGUID,
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
     * Retrieve the list of schema attribute metadata elements that contain the search string.
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
    public SchemaAttributesResponse findSchemaAttributes(String            serverName,
                                                                   String            viewServiceURLMarker,
                                                                   boolean           startsWith,
                                                                   boolean           endsWith,
                                                                   boolean           ignoreCase,
                                                                   int               startFrom,
                                                                   int               pageSize,
                                                                   FilterRequestBody requestBody)
    {
        final String methodName = "findSchemaAttributes";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        SchemaAttributesResponse response = new SchemaAttributesResponse();
        AuditLog                        auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SchemaAttributeHandler handler = instanceHandler.getSchemaAttributeHandler(userId, serverName, viewServiceURLMarker, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.findSchemaAttributes(userId,
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
                response.setElements(handler.findSchemaAttributes(userId,
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
