/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.connectionmaker.server;


import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.enums.SequencingOrder;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.connections.ConnectionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.connections.ConnectorTypeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.connections.EndpointProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.SupportingDefinitionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.locations.ProfileLocationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.TemplateFilter;
import org.odpi.openmetadata.frameworkservices.omf.client.handlers.ConnectionHandler;
import org.odpi.openmetadata.frameworkservices.omf.client.handlers.ConnectorTypeHandler;
import org.odpi.openmetadata.frameworkservices.omf.client.handlers.EndpointHandler;
import org.odpi.openmetadata.frameworkservices.omf.rest.AnyTimeRequestBody;
import org.odpi.openmetadata.tokencontroller.TokenController;
import org.slf4j.LoggerFactory;

import java.util.Date;


/**
 * The ConnectionMakerRESTServices provides the server-side implementation of the Connection Maker Open Metadata
 * View Service (OMVS).  This interface provides access to a person's profile, roles and network.
 */
public class ConnectionMakerRESTServices extends TokenController
{
    private static final ConnectionMakerInstanceHandler instanceHandler = new ConnectionMakerInstanceHandler();

    private static final RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();

    private static final RESTCallLogger restCallLogger = new RESTCallLogger(LoggerFactory.getLogger(ConnectionMakerRESTServices.class),
                                                                            instanceHandler.getServiceName());

    /**
     * Default constructor
     */
    public ConnectionMakerRESTServices()
    {
    }


    /**
     * Create a connection.
     *
     * @param serverName                 name of called server.
     * @param viewServiceURLMarker  view service URL marker
     * @param requestBody             properties for the connection.
     *
     * @return unique identifier of the newly created element
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public GUIDResponse createConnection(String                serverName,
                                           String                viewServiceURLMarker,
                                           NewElementRequestBody requestBody)
    {
        final String methodName = "createConnection";

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
                ConnectionHandler handler = instanceHandler.getConnectionHandler(userId, serverName, viewServiceURLMarker, methodName);

                if (requestBody.getProperties() instanceof ConnectionProperties connectionProperties)
                {
                    response.setGUID(handler.createConnection(userId,
                                                                requestBody.getExternalSourceGUID(),
                                                                requestBody.getExternalSourceName(),
                                                                requestBody.getAnchorGUID(),
                                                                requestBody.getIsOwnAnchor(),
                                                                requestBody.getAnchorScopeGUID(),
                                                                connectionProperties,
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
                    response.setGUID(handler.createConnection(userId,
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
                    restExceptionHandler.handleInvalidPropertiesObject(ConnectionProperties.class.getName(), methodName);
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
     * Create a new metadata element to represent a connection using an existing metadata element as a template.
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
    public GUIDResponse createConnectionFromTemplate(String              serverName,
                                                       String              viewServiceURLMarker,
                                                       TemplateRequestBody requestBody)
    {
        final String methodName = "createConnectionFromTemplate";

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
                ConnectionHandler handler = instanceHandler.getConnectionHandler(userId, serverName, viewServiceURLMarker, methodName);

                response.setGUID(handler.createConnectionFromTemplate(userId,
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
     * Update the properties of a connection.
     *
     * @param serverName         name of called server.
     * @param viewServiceURLMarker  view service URL marker
     * @param connectionGUID unique identifier of the connection (returned from create)
     * @param replaceAllProperties flag to indicate whether to completely replace the existing properties with the new properties, or just update
     *                          the individual properties specified on the request.
     * @param requestBody     properties for the new element.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse updateConnection(String                   serverName,
                                           String                   viewServiceURLMarker,
                                           String                   connectionGUID,
                                           boolean                  replaceAllProperties,
                                           UpdateElementRequestBody requestBody)
    {
        final String methodName = "updateConnection";

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
                ConnectionHandler handler = instanceHandler.getConnectionHandler(userId, serverName, viewServiceURLMarker, methodName);

                if (requestBody.getProperties() instanceof ConnectionProperties connectionProperties)
                {
                    handler.updateConnection(userId,
                                               requestBody.getExternalSourceGUID(),
                                               requestBody.getExternalSourceName(),
                                               connectionGUID,
                                               replaceAllProperties,
                                               connectionProperties,
                                               requestBody.getForLineage(),
                                               requestBody.getForDuplicateProcessing(),
                                               requestBody.getEffectiveTime());
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.updateConnection(userId,
                                               requestBody.getExternalSourceGUID(),
                                               requestBody.getExternalSourceName(),
                                               connectionGUID,
                                               replaceAllProperties,
                                               null,
                                               requestBody.getForLineage(),
                                               requestBody.getForDuplicateProcessing(),
                                               requestBody.getEffectiveTime());
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(ConnectionProperties.class.getName(), methodName);
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
     * @param connectionGUID       unique identifier of the connection
     * @param locationGUID           unique identifier of the location
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse linkConnectionToAsset(String                  serverName,
                                              String                  viewServiceURLMarker,
                                              String                  connectionGUID,
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
            ConnectionHandler handler = instanceHandler.getConnectionHandler(userId, serverName, viewServiceURLMarker, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof ProfileLocationProperties profileLocationProperties)
                {
                    handler.linkLocationToProfile(userId,
                                                  requestBody.getExternalSourceGUID(),
                                                  requestBody.getExternalSourceName(),
                                                  connectionGUID,
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
                                                  connectionGUID,
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
                                              connectionGUID,
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
     * Detach a connection from a location.
     *
     * @param serverName         name of called server
     * @param viewServiceURLMarker  view service URL marker
     * @param connectionGUID       unique identifier of the connection
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
                                                  String                    connectionGUID,
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

            ConnectionHandler handler = instanceHandler.getConnectionHandler(userId, serverName, viewServiceURLMarker, methodName);

            if (requestBody != null)
            {
                handler.detachLocationFromProfile(userId,
                                                  requestBody.getExternalSourceGUID(),
                                                  requestBody.getExternalSourceName(),
                                                  connectionGUID,
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
                                                  connectionGUID,
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
     * @param personOneGUID unique identifier of the first connection
     * @param personTwoGUID unique identifier of the second connection
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
            ConnectionHandler handler = instanceHandler.getConnectionHandler(userId, serverName, viewServiceURLMarker, methodName);

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

            ConnectionHandler handler = instanceHandler.getConnectionHandler(userId, serverName, viewServiceURLMarker, methodName);

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
            ConnectionHandler handler = instanceHandler.getConnectionHandler(userId, serverName, viewServiceURLMarker, methodName);

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

            ConnectionHandler handler = instanceHandler.getConnectionHandler(userId, serverName, viewServiceURLMarker, methodName);

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
    public VoidResponse linkAssetToConnection(String                  serverName,
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
            ConnectionHandler handler = instanceHandler.getConnectionHandler(userId, serverName, viewServiceURLMarker, methodName);

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
    public VoidResponse detachAssetFromConnection(String                    serverName,
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

            ConnectionHandler handler = instanceHandler.getConnectionHandler(userId, serverName, viewServiceURLMarker, methodName);

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
            ConnectionHandler handler = instanceHandler.getConnectionHandler(userId, serverName, viewServiceURLMarker, methodName);

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

            ConnectionHandler handler = instanceHandler.getConnectionHandler(userId, serverName, viewServiceURLMarker, methodName);

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
            ConnectionHandler handler = instanceHandler.getConnectionHandler(userId, serverName, viewServiceURLMarker, methodName);

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

            ConnectionHandler handler = instanceHandler.getConnectionHandler(userId, serverName, viewServiceURLMarker, methodName);

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
     * Delete a connection.
     *
     * @param serverName         name of called server
     * @param viewServiceURLMarker  view service URL marker
     * @param connectionGUID  unique identifier of the element to delete
     * @param cascadedDelete ca connections be deleted if data fields are attached?
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse deleteConnection(String                    serverName,
                                           String                    viewServiceURLMarker,
                                           String                    connectionGUID,
                                           boolean                   cascadedDelete,
                                           MetadataSourceRequestBody requestBody)
    {
        final String methodName = "deleteConnection";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ConnectionHandler handler = instanceHandler.getConnectionHandler(userId, serverName, viewServiceURLMarker, methodName);

            if (requestBody != null)
            {
                handler.deleteConnection(userId,
                                           requestBody.getExternalSourceGUID(),
                                           requestBody.getExternalSourceName(),
                                           connectionGUID,
                                           cascadedDelete,
                                           requestBody.getForLineage(),
                                           requestBody.getForDuplicateProcessing(),
                                           requestBody.getEffectiveTime());
            }
            else
            {
                handler.deleteConnection(userId,
                                           null,
                                           null,
                                           connectionGUID,
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
     * Retrieve the list of connection metadata elements that contain the search string.
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
    public ConnectionsResponse getConnectionsByName(String            serverName,
                                                        String            viewServiceURLMarker,
                                                        int               startFrom,
                                                        int               pageSize,
                                                        FilterRequestBody requestBody)
    {
        final String methodName = "getConnectionsByName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        ConnectionsResponse response = new ConnectionsResponse();
        AuditLog                        auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ConnectionHandler handler = instanceHandler.getConnectionHandler(userId, serverName, viewServiceURLMarker, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.getConnectionsByName(userId,
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
     * Retrieve the list of connection metadata elements that contain the search string.
     *
     * @param serverName name of the service to route the request to
     * @param viewServiceURLMarker  view service URL marker
     * @param connectionGUID    unique identifier of the required element
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public ConnectionResponse getConnectionByGUID(String             serverName,
                                                      String             viewServiceURLMarker,
                                                      String             connectionGUID,
                                                      AnyTimeRequestBody requestBody)
    {
        final String methodName = "getConnectionByGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        ConnectionResponse response = new ConnectionResponse();
        AuditLog                      auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ConnectionHandler handler = instanceHandler.getConnectionHandler(userId, serverName, viewServiceURLMarker, methodName);

            if (requestBody != null)
            {
                response.setElement(handler.getConnectionByGUID(userId,
                                                                  connectionGUID,
                                                                  requestBody.getAsOfTime(),
                                                                  requestBody.getForLineage(),
                                                                  requestBody.getForDuplicateProcessing(),
                                                                  requestBody.getEffectiveTime()));
            }
            else
            {
                response.setElement(handler.getConnectionByGUID(userId,
                                                                  connectionGUID,
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
     * Retrieve the list of connection metadata elements that contain the search string.
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
    public ConnectionsResponse findConnections(String            serverName,
                                                   String            viewServiceURLMarker,
                                                   boolean           startsWith,
                                                   boolean           endsWith,
                                                   boolean           ignoreCase,
                                                   int               startFrom,
                                                   int               pageSize,
                                                   FilterRequestBody requestBody)
    {
        final String methodName = "findConnections";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        ConnectionsResponse response = new ConnectionsResponse();
        AuditLog                        auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ConnectionHandler handler = instanceHandler.getConnectionHandler(userId, serverName, viewServiceURLMarker, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.findConnections(userId,
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
                response.setElements(handler.findConnections(userId,
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
     * Create a connectorType.
     *
     * @param serverName                 name of called server.
     * @param viewServiceURLMarker  view service URL marker
     * @param requestBody             properties for the connectorType.
     *
     * @return unique identifier of the newly created element
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public GUIDResponse createConnectorType(String                serverName,
                                        String                viewServiceURLMarker,
                                        NewElementRequestBody requestBody)
    {
        final String methodName = "createConnectorType";

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
                ConnectorTypeHandler handler = instanceHandler.getConnectorTypeHandler(userId, serverName, viewServiceURLMarker, methodName);

                if (requestBody.getProperties() instanceof ConnectorTypeProperties connectorTypeProperties)
                {
                    response.setGUID(handler.createConnectorType(userId,
                                                             requestBody.getExternalSourceGUID(),
                                                             requestBody.getExternalSourceName(),
                                                             requestBody.getAnchorGUID(),
                                                             requestBody.getIsOwnAnchor(),
                                                             requestBody.getAnchorScopeGUID(),
                                                             connectorTypeProperties,
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
                    response.setGUID(handler.createConnectorType(userId,
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
                    restExceptionHandler.handleInvalidPropertiesObject(ConnectorTypeProperties.class.getName(), methodName);
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
     * Create a new metadata element to represent a connectorType using an existing metadata element as a template.
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
    public GUIDResponse createConnectorTypeFromTemplate(String              serverName,
                                                    String              viewServiceURLMarker,
                                                    TemplateRequestBody requestBody)
    {
        final String methodName = "createConnectorTypeFromTemplate";

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
                ConnectorTypeHandler handler = instanceHandler.getConnectorTypeHandler(userId, serverName, viewServiceURLMarker, methodName);

                response.setGUID(handler.createConnectorTypeFromTemplate(userId,
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
     * Update the properties of a connectorType.
     *
     * @param serverName         name of called server.
     * @param viewServiceURLMarker  view service URL marker
     * @param connectorTypeGUID unique identifier of the connectorType (returned from create)
     * @param replaceAllProperties flag to indicate whether to completely replace the existing properties with the new properties, or just update
     *                          the individual properties specified on the request.
     * @param requestBody     properties for the new element.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse updateConnectorType(String                   serverName,
                                        String                   viewServiceURLMarker,
                                        String                   connectorTypeGUID,
                                        boolean                  replaceAllProperties,
                                        UpdateElementRequestBody requestBody)
    {
        final String methodName = "updateConnectorType";

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
                ConnectorTypeHandler handler = instanceHandler.getConnectorTypeHandler(userId, serverName, viewServiceURLMarker, methodName);

                if (requestBody.getProperties() instanceof ConnectorTypeProperties connectorTypeProperties)
                {
                    handler.updateConnectorType(userId,
                                            requestBody.getExternalSourceGUID(),
                                            requestBody.getExternalSourceName(),
                                            connectorTypeGUID,
                                            replaceAllProperties,
                                            connectorTypeProperties,
                                            requestBody.getForLineage(),
                                            requestBody.getForDuplicateProcessing(),
                                            requestBody.getEffectiveTime());
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.updateConnectorType(userId,
                                            requestBody.getExternalSourceGUID(),
                                            requestBody.getExternalSourceName(),
                                            connectorTypeGUID,
                                            replaceAllProperties,
                                            null,
                                            requestBody.getForLineage(),
                                            requestBody.getForDuplicateProcessing(),
                                            requestBody.getEffectiveTime());
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(ConnectorTypeProperties.class.getName(), methodName);
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
            ConnectorTypeHandler handler = instanceHandler.getConnectorTypeHandler(userId, serverName, viewServiceURLMarker, methodName);

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

            ConnectorTypeHandler handler = instanceHandler.getConnectorTypeHandler(userId, serverName, viewServiceURLMarker, methodName);

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
            ConnectorTypeHandler handler = instanceHandler.getConnectorTypeHandler(userId, serverName, viewServiceURLMarker, methodName);

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

            ConnectorTypeHandler handler = instanceHandler.getConnectorTypeHandler(userId, serverName, viewServiceURLMarker, methodName);

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
            
            ConnectorTypeHandler handler = instanceHandler.getConnectorTypeHandler(userId, serverName, viewServiceURLMarker, methodName);

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

            ConnectorTypeHandler handler = instanceHandler.getConnectorTypeHandler(userId, serverName, viewServiceURLMarker, methodName);

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
     * Delete a connectorType.
     *
     * @param serverName         name of called server
     * @param viewServiceURLMarker  view service URL marker
     * @param connectorTypeGUID  unique identifier of the element to delete
     * @param cascadedDelete ca connectorTypes be deleted if data fields are attached?
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse deleteConnectorType(String                    serverName,
                                                   String                    viewServiceURLMarker,
                                                   String                    connectorTypeGUID,
                                                   boolean                   cascadedDelete,
                                                   MetadataSourceRequestBody requestBody)
    {
        final String methodName = "deleteConnectorType";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ConnectorTypeHandler handler = instanceHandler.getConnectorTypeHandler(userId, serverName, viewServiceURLMarker, methodName);

            if (requestBody != null)
            {
                handler.deleteConnectorType(userId,
                                                   requestBody.getExternalSourceGUID(),
                                                   requestBody.getExternalSourceName(),
                                                   connectorTypeGUID,
                                                   cascadedDelete,
                                                   requestBody.getForLineage(),
                                                   requestBody.getForDuplicateProcessing(),
                                                   requestBody.getEffectiveTime());
            }
            else
            {
                handler.deleteConnectorType(userId,
                                                   null,
                                                   null,
                                                   connectorTypeGUID,
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
     * Retrieve the list of connectorType metadata elements that contain the search string.
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
    public ConnectorTypesResponse getConnectorTypesByName(String            serverName,
                                                                        String            viewServiceURLMarker,
                                                                        int               startFrom,
                                                                        int               pageSize,
                                                                        FilterRequestBody requestBody)
    {
        final String methodName = "getConnectorTypesByName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        ConnectorTypesResponse response = new ConnectorTypesResponse();
        AuditLog                        auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ConnectorTypeHandler handler = instanceHandler.getConnectorTypeHandler(userId, serverName, viewServiceURLMarker, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.getConnectorTypesByName(userId,
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
     * Retrieve the list of connectorType metadata elements that contain the search string.
     *
     * @param serverName name of the service to route the request to
     * @param viewServiceURLMarker  view service URL marker
     * @param connectorTypeGUID    unique identifier of the required element
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public ConnectorTypeResponse getConnectorTypeByGUID(String             serverName,
                                                                      String             viewServiceURLMarker,
                                                                      String             connectorTypeGUID,
                                                                      AnyTimeRequestBody requestBody)
    {
        final String methodName = "getConnectorTypeByGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        ConnectorTypeResponse response = new ConnectorTypeResponse();
        AuditLog                      auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ConnectorTypeHandler handler = instanceHandler.getConnectorTypeHandler(userId, serverName, viewServiceURLMarker, methodName);

            if (requestBody != null)
            {
                response.setElement(handler.getConnectorTypeByGUID(userId,
                                                                          connectorTypeGUID,
                                                                          requestBody.getAsOfTime(),
                                                                          requestBody.getForLineage(),
                                                                          requestBody.getForDuplicateProcessing(),
                                                                          requestBody.getEffectiveTime()));
            }
            else
            {
                response.setElement(handler.getConnectorTypeByGUID(userId,
                                                                          connectorTypeGUID,
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
     * Retrieve the list of connectorType metadata elements that contain the search string.
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
    public ConnectorTypesResponse findConnectorTypes(String            serverName,
                                                                   String            viewServiceURLMarker,
                                                                   boolean           startsWith,
                                                                   boolean           endsWith,
                                                                   boolean           ignoreCase,
                                                                   int               startFrom,
                                                                   int               pageSize,
                                                                   FilterRequestBody requestBody)
    {
        final String methodName = "findConnectorTypes";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        ConnectorTypesResponse response = new ConnectorTypesResponse();
        AuditLog                        auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ConnectorTypeHandler handler = instanceHandler.getConnectorTypeHandler(userId, serverName, viewServiceURLMarker, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.findConnectorTypes(userId,
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
                response.setElements(handler.findConnectorTypes(userId,
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
     * Create a endpoint.
     *
     * @param serverName                 name of called server.
     * @param viewServiceURLMarker  view service URL marker
     * @param requestBody             properties for the endpoint.
     *
     * @return unique identifier of the newly created element
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public GUIDResponse createEndpoint(String                serverName,
                                           String                viewServiceURLMarker,
                                           NewElementRequestBody requestBody)
    {
        final String methodName = "createEndpoint";

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
                EndpointHandler handler = instanceHandler.getEndpointHandler(userId, serverName, viewServiceURLMarker, methodName);

                if (requestBody.getProperties() instanceof EndpointProperties endpointProperties)
                {
                    response.setGUID(handler.createEndpoint(userId,
                                                                requestBody.getExternalSourceGUID(),
                                                                requestBody.getExternalSourceName(),
                                                                requestBody.getAnchorGUID(),
                                                                requestBody.getIsOwnAnchor(),
                                                                requestBody.getAnchorScopeGUID(),
                                                                endpointProperties,
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
                    response.setGUID(handler.createEndpoint(userId,
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
                    restExceptionHandler.handleInvalidPropertiesObject(EndpointProperties.class.getName(), methodName);
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
     * Create a new metadata element to represent a endpoint using an existing metadata element as a template.
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
    public GUIDResponse createEndpointFromTemplate(String              serverName,
                                                       String              viewServiceURLMarker,
                                                       TemplateRequestBody requestBody)
    {
        final String methodName = "createEndpointFromTemplate";

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
                EndpointHandler handler = instanceHandler.getEndpointHandler(userId, serverName, viewServiceURLMarker, methodName);

                response.setGUID(handler.createEndpointFromTemplate(userId,
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
     * Update the properties of a endpoint.
     *
     * @param serverName         name of called server.
     * @param viewServiceURLMarker  view service URL marker
     * @param endpointGUID unique identifier of the endpoint (returned from create)
     * @param replaceAllProperties flag to indicate whether to completely replace the existing properties with the new properties, or just update
     *                          the individual properties specified on the request.
     * @param requestBody     properties for the new element.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse updateEndpoint(String                   serverName,
                                           String                   viewServiceURLMarker,
                                           String                   endpointGUID,
                                           boolean                  replaceAllProperties,
                                           UpdateElementRequestBody requestBody)
    {
        final String methodName = "updateEndpoint";

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
                EndpointHandler handler = instanceHandler.getEndpointHandler(userId, serverName, viewServiceURLMarker, methodName);

                if (requestBody.getProperties() instanceof EndpointProperties endpointProperties)
                {
                    handler.updateEndpoint(userId,
                                               requestBody.getExternalSourceGUID(),
                                               requestBody.getExternalSourceName(),
                                               endpointGUID,
                                               replaceAllProperties,
                                               endpointProperties,
                                               requestBody.getForLineage(),
                                               requestBody.getForDuplicateProcessing(),
                                               requestBody.getEffectiveTime());
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.updateEndpoint(userId,
                                               requestBody.getExternalSourceGUID(),
                                               requestBody.getExternalSourceName(),
                                               endpointGUID,
                                               replaceAllProperties,
                                               null,
                                               requestBody.getForLineage(),
                                               requestBody.getForDuplicateProcessing(),
                                               requestBody.getEffectiveTime());
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(EndpointProperties.class.getName(), methodName);
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
     * Attach a profile to a endpoint.
     *
     * @param serverName         name of called server
     * @param viewServiceURLMarker  view service URL marker
     * @param endpointGUID unique identifier of the parent
     * @param profileGUID     unique identifier of the connection
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse linkIdentityToProfile(String                  serverName,
                                              String                  viewServiceURLMarker,
                                              String                  endpointGUID,
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
            EndpointHandler handler = instanceHandler.getEndpointHandler(userId, serverName, viewServiceURLMarker, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof ProfileIdentityProperties profileIdentityProperties)
                {
                    handler.linkEndpointToConnection(userId,
                                                  requestBody.getExternalSourceGUID(),
                                                  requestBody.getExternalSourceName(),
                                                  endpointGUID,
                                                  profileGUID,
                                                  profileIdentityProperties,
                                                  requestBody.getForLineage(),
                                                  requestBody.getForDuplicateProcessing(),
                                                  requestBody.getEffectiveTime());
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.linkEndpointToConnection(userId,
                                                  requestBody.getExternalSourceGUID(),
                                                  requestBody.getExternalSourceName(),
                                                  endpointGUID,
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
                handler.linkEndpointToConnection(userId,
                                              null,
                                              null,
                                              endpointGUID,
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
     * Detach a connection from a endpoint.
     *
     * @param serverName         name of called server
     * @param viewServiceURLMarker  view service URL marker
     * @param endpointGUID    unique identifier of the parent connection
     * @param profileGUID    unique identifier of the nested connection
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse detachProfileIdentity(String                    serverName,
                                              String                    viewServiceURLMarker,
                                              String                    endpointGUID,
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

            EndpointHandler handler = instanceHandler.getEndpointHandler(userId, serverName, viewServiceURLMarker, methodName);

            if (requestBody != null)
            {
                handler.detachEndpointFromConnection(userId,
                                              requestBody.getExternalSourceGUID(),
                                              requestBody.getExternalSourceName(),
                                              endpointGUID,
                                              profileGUID,
                                              requestBody.getForLineage(),
                                              requestBody.getForDuplicateProcessing(),
                                              requestBody.getEffectiveTime());
            }
            else
            {
                handler.detachEndpointFromConnection(userId,
                                              null,
                                              null,
                                              endpointGUID,
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
     * Delete a endpoint.
     *
     * @param serverName         name of called server
     * @param viewServiceURLMarker  view service URL marker
     * @param endpointGUID  unique identifier of the element to delete
     * @param cascadedDelete can endpoints be deleted if data fields are attached?
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse deleteEndpoint(String                    serverName,
                                           String                    viewServiceURLMarker,
                                           String                    endpointGUID,
                                           boolean                   cascadedDelete,
                                           MetadataSourceRequestBody requestBody)
    {
        final String methodName = "deleteEndpoint";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            EndpointHandler handler = instanceHandler.getEndpointHandler(userId, serverName, viewServiceURLMarker, methodName);

            if (requestBody != null)
            {
                handler.deleteEndpoint(userId,
                                           requestBody.getExternalSourceGUID(),
                                           requestBody.getExternalSourceName(),
                                           endpointGUID,
                                           cascadedDelete,
                                           requestBody.getForLineage(),
                                           requestBody.getForDuplicateProcessing(),
                                           requestBody.getEffectiveTime());
            }
            else
            {
                handler.deleteEndpoint(userId,
                                           null,
                                           null,
                                           endpointGUID,
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
     * Retrieve the list of endpoint metadata elements that contain the search string.
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
    public EndpointsResponse getEndpointsByName(String            serverName,
                                                          String            viewServiceURLMarker,
                                                          int               startFrom,
                                                          int               pageSize,
                                                          FilterRequestBody requestBody)
    {
        final String methodName = "getEndpointsByName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        EndpointsResponse response = new EndpointsResponse();
        AuditLog                        auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            EndpointHandler handler = instanceHandler.getEndpointHandler(userId, serverName, viewServiceURLMarker, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.getEndpointsByName(userId,
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
     * Retrieve the list of endpoint metadata elements that contain the search string.
     *
     * @param serverName name of the service to route the request to
     * @param viewServiceURLMarker  view service URL marker
     * @param endpointGUID    unique identifier of the required element
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public EndpointResponse getEndpointByGUID(String             serverName,
                                                      String             viewServiceURLMarker,
                                                      String             endpointGUID,
                                                      AnyTimeRequestBody requestBody)
    {
        final String methodName = "getEndpointByGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        EndpointResponse response = new EndpointResponse();
        AuditLog                      auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            EndpointHandler handler = instanceHandler.getEndpointHandler(userId, serverName, viewServiceURLMarker, methodName);

            if (requestBody != null)
            {
                response.setElement(handler.getEndpointByGUID(userId,
                                                                  endpointGUID,
                                                                  requestBody.getAsOfTime(),
                                                                  requestBody.getForLineage(),
                                                                  requestBody.getForDuplicateProcessing(),
                                                                  requestBody.getEffectiveTime()));
            }
            else
            {
                response.setElement(handler.getEndpointByGUID(userId,
                                                                  endpointGUID,
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
     * Retrieve the list of endpoint metadata elements that contain the search string.
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
    public EndpointsResponse findEndpoints(String            serverName,
                                                     String            viewServiceURLMarker,
                                                     boolean           startsWith,
                                                     boolean           endsWith,
                                                     boolean           ignoreCase,
                                                     int               startFrom,
                                                     int               pageSize,
                                                     FilterRequestBody requestBody)
    {
        final String methodName = "findEndpoints";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        EndpointsResponse response = new EndpointsResponse();
        AuditLog                        auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            EndpointHandler handler = instanceHandler.getEndpointHandler(userId, serverName, viewServiceURLMarker, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.findEndpoints(userId,
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
                response.setElements(handler.findEndpoints(userId,
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
