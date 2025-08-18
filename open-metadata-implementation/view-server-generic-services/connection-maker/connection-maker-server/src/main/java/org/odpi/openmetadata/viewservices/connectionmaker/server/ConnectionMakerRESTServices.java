/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.connectionmaker.server;


import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.ConnectionHandler;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.ConnectorTypeHandler;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.EndpointHandler;
import org.odpi.openmetadata.frameworks.openmetadata.properties.connections.*;
import org.odpi.openmetadata.commonservices.ffdc.rest.GetRequestBody;
import org.odpi.openmetadata.tokencontroller.TokenController;
import org.slf4j.LoggerFactory;


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
     * @param urlMarker  view service URL marker
     * @param requestBody             properties for the connection.
     *
     * @return unique identifier of the newly created element
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public GUIDResponse createConnection(String                serverName,
                                         String                urlMarker,
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
                ConnectionHandler handler = instanceHandler.getConnectionHandler(userId, serverName, urlMarker, methodName);

                if (requestBody.getProperties() instanceof ConnectionProperties connectionProperties)
                {
                    response.setGUID(handler.createConnection(userId,
                                                              requestBody,
                                                              requestBody.getInitialClassifications(),
                                                              connectionProperties,
                                                              requestBody.getParentRelationshipProperties()));
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
     * @param urlMarker  view service URL marker
     * @param requestBody properties that override the template
     *
     * @return unique identifier of the new metadata element
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GUIDResponse createConnectionFromTemplate(String              serverName,
                                                     String              urlMarker,
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
                ConnectionHandler handler = instanceHandler.getConnectionHandler(userId, serverName, urlMarker, methodName);

                response.setGUID(handler.createConnectionFromTemplate(userId,
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
     * Update the properties of a connection.
     *
     * @param serverName         name of called server.
     * @param urlMarker  view service URL marker
     * @param connectionGUID unique identifier of the connection (returned from create)
     * @param requestBody     properties for the new element.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse updateConnection(String                   serverName,
                                         String                   urlMarker,
                                         String                   connectionGUID,
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
                ConnectionHandler handler = instanceHandler.getConnectionHandler(userId, serverName, urlMarker, methodName);

                if (requestBody.getProperties() instanceof ConnectionProperties connectionProperties)
                {
                    handler.updateConnection(userId,
                                             connectionGUID,
                                             requestBody,
                                             connectionProperties);
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.updateConnection(userId,
                                             connectionGUID,
                                             requestBody,
                                             null);
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
     * Create a ConnectionConnectorType relationship between a connection and a connector type.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
     * @param connectionGUID       unique identifier of the connection
     * @param connectorTypeGUID           unique identifier of the connector type
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse linkConnectionConnectorType(String                  serverName,
                                                    String                  urlMarker,
                                                    String                  connectionGUID,
                                                    String                  connectorTypeGUID,
                                                    NewRelationshipRequestBody requestBody)
    {
        final String methodName = "linkConnectionConnectorType";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            ConnectionHandler handler = instanceHandler.getConnectionHandler(userId, serverName, urlMarker, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof ConnectionConnectorTypeProperties properties)
                {
                    handler.linkConnectionConnectorType(userId,
                                                        connectionGUID,
                                                        connectorTypeGUID,
                                                        requestBody,
                                                        properties);
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.linkConnectionConnectorType(userId,
                                                        connectionGUID,
                                                        connectorTypeGUID,
                                                        requestBody,
                                                        null);
                }
                else
                {
                    /*
                     * Wrong type of properties ...
                     */
                    restExceptionHandler.handleInvalidPropertiesObject(ConnectionConnectorTypeProperties.class.getName(), methodName);
                }
            }
            else
            {
                handler.linkConnectionConnectorType(userId,
                                                    connectionGUID,
                                                    connectorTypeGUID,
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
     * Remove the ConnectionConnectorType relationship between a connection and a connector type.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
     * @param connectionGUID       unique identifier of the connection
     * @param connectorTypeGUID           unique identifier of the connector type
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse detachConnectionConnectorType(String                   serverName,
                                                      String                   urlMarker,
                                                      String                   connectionGUID,
                                                      String                   connectorTypeGUID,
                                                      DeleteRequestBody requestBody)
    {
        final String methodName = "detachConnectionConnectorType";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ConnectionHandler handler = instanceHandler.getConnectionHandler(userId, serverName, urlMarker, methodName);

            handler.detachConnectionConnectorType(userId,
                                                  connectionGUID,
                                                  connectorTypeGUID,
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
     * Create a ConnectToEndpoint relationship between a connection and an endpoint.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
     * @param connectionGUID unique identifier of the connection
     * @param endpointGUID unique identifier of the endpoint
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse linkConnectionEndpoint(String                  serverName,
                                               String                  urlMarker,
                                               String                  connectionGUID,
                                               String                  endpointGUID,
                                               NewRelationshipRequestBody requestBody)
    {
        final String methodName = "linkConnectionEndpoint";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            ConnectionHandler handler = instanceHandler.getConnectionHandler(userId, serverName, urlMarker, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof ConnectionEndpointProperties properties)
                {
                    handler.linkConnectionEndpoint(userId,
                                                   connectionGUID,
                                                   endpointGUID,
                                                   requestBody,
                                                   properties);
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.linkConnectionEndpoint(userId,
                                                   connectionGUID,
                                                   endpointGUID,
                                                   requestBody,
                                                   null);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(ConnectionEndpointProperties.class.getName(), methodName);
                }
            }
            else
            {
                handler.linkConnectionEndpoint(userId,
                                               connectionGUID,
                                               endpointGUID,
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
     * Remove the ConnectToEndpoint relationship between a connection and an endpoint.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
     * @param connectionGUID          unique identifier of the connection
     * @param endpointGUID          unique identifier of the endpoint
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse detachConnectionEndpoint(String                   serverName,
                                                 String                   urlMarker,
                                                 String                   connectionGUID,
                                                 String                   endpointGUID,
                                                 DeleteRequestBody requestBody)
    {
        final String methodName = "detachConnectionEndpoint";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ConnectionHandler handler = instanceHandler.getConnectionHandler(userId, serverName, urlMarker, methodName);

            handler.detachConnectionEndpoint(userId,
                                             connectionGUID,
                                             endpointGUID,
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
     * Create an EmbeddedConnection relationship between a virtual connection and an embedded connection.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
     * @param connectionGUID unique identifier of the virtual connection
     * @param embeddedConnectionGUID unique identifier of the embedded connection
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse linkEmbeddedConnection(String                  serverName,
                                               String                  urlMarker,
                                               String                  connectionGUID,
                                               String                  embeddedConnectionGUID,
                                               NewRelationshipRequestBody requestBody)
    {
        final String methodName = "linkEmbeddedConnection";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            ConnectionHandler handler = instanceHandler.getConnectionHandler(userId, serverName, urlMarker, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof EmbeddedConnectionProperties properties)
                {
                    handler.linkEmbeddedConnection(userId,
                                                   connectionGUID,
                                                   embeddedConnectionGUID,
                                                   requestBody,
                                                   properties);
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.linkEmbeddedConnection(userId,
                                                   connectionGUID,
                                                   embeddedConnectionGUID,
                                                   requestBody,
                                                   null);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(EmbeddedConnectionProperties.class.getName(), methodName);
                }
            }
            else
            {
                handler.linkEmbeddedConnection(userId,
                                               connectionGUID,
                                               embeddedConnectionGUID,
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
     * Remove an EmbeddedConnection relationship between a virtual connection and an embedded connection.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
     * @param connectionGUID unique identifier of the virtual connection
     * @param embeddedConnectionGUID unique identifier of the embedded connection
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse detachEmbeddedConnection(String                   serverName,
                                                 String                   urlMarker,
                                                 String                   connectionGUID,
                                                 String                   embeddedConnectionGUID,
                                                 DeleteRequestBody requestBody)
    {
        final String methodName = "detachEmbeddedConnection";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ConnectionHandler handler = instanceHandler.getConnectionHandler(userId, serverName, urlMarker, methodName);

            handler.detachEmbeddedConnection(userId,
                                             connectionGUID,
                                             embeddedConnectionGUID,
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
     * Create an AssetConnection relationship between an asset and its connection.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
     * @param assetGUID       unique identifier of the asset
     * @param connectionGUID            unique identifier of the connection
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse linkAssetToConnection(String                  serverName,
                                              String                  urlMarker,
                                              String                  assetGUID,
                                              String                  connectionGUID,
                                              NewRelationshipRequestBody requestBody)
    {
        final String methodName = "linkAssetToConnection";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            ConnectionHandler handler = instanceHandler.getConnectionHandler(userId, serverName, urlMarker, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof AssetConnectionProperties properties)
                {
                    handler.linkAssetToConnection(userId,
                                                  assetGUID,
                                                  connectionGUID,
                                                  requestBody,
                                                  properties);
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.linkAssetToConnection(userId,
                                                  assetGUID,
                                                  connectionGUID,
                                                  requestBody,
                                                  null);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(AssetConnectionProperties.class.getName(), methodName);
                }
            }
            else
            {
                handler.linkAssetToConnection(userId,
                                              assetGUID,
                                              connectionGUID,
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
     * Detach an asset from one of its connections.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
     * @param assetGUID       unique identifier of the asset
     * @param connectionGUID            unique identifier of the IT profile
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse detachAssetFromConnection(String                   serverName,
                                                  String                   urlMarker,
                                                  String                   assetGUID,
                                                  String                   connectionGUID,
                                                  DeleteRequestBody requestBody)
    {
        final String methodName = "detachAssetFromConnection";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ConnectionHandler handler = instanceHandler.getConnectionHandler(userId, serverName, urlMarker, methodName);

            handler.detachAssetFromConnection(userId,
                                              assetGUID,
                                              connectionGUID,
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
     * Attach an endpoint to an infrastructure asset.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
     * @param itAssetGUID             unique identifier of the infrastructure asset
     * @param endpointGUID            unique identifier of the endpoint
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse linkEndpointToITAsset(String                  serverName,
                                              String                  urlMarker,
                                              String                  itAssetGUID,
                                              String                  endpointGUID,
                                              NewRelationshipRequestBody requestBody)
    {
        final String methodName = "linkEndpointToITAsset";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            EndpointHandler handler = instanceHandler.getEndpointHandler(userId, serverName, urlMarker, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof ServerEndpointProperties properties)
                {
                    handler.linkEndpointToITAsset(userId,
                                                  itAssetGUID,
                                                  endpointGUID,
                                                  requestBody,
                                                  properties);
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.linkEndpointToITAsset(userId,
                                                  itAssetGUID,
                                                  endpointGUID,
                                                  requestBody,
                                                  null);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(ServerEndpointProperties.class.getName(), methodName);
                }
            }
            else
            {
                handler.linkEndpointToITAsset(userId,
                                              itAssetGUID,
                                              endpointGUID,
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
     * Detach an endpoint from an infrastructure asset.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
     * @param itAssetGUID            unique identifier of the infrastructure asset
     * @param endpointGUID       unique identifier of the endpoint
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse detachEndpointFromITAsset(String                   serverName,
                                                  String                   urlMarker,
                                                  String                   itAssetGUID,
                                                  String                   endpointGUID,
                                                  DeleteRequestBody requestBody)
    {
        final String methodName = "detachEndpointFromITAsset";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            EndpointHandler handler = instanceHandler.getEndpointHandler(userId, serverName, urlMarker, methodName);

            handler.detachEndpointFromITAsset(userId,
                                              itAssetGUID,
                                              endpointGUID,
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
     * Delete a connection.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
     * @param connectionGUID  unique identifier of the element to delete
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse deleteConnection(String            serverName,
                                         String            urlMarker,
                                         String            connectionGUID,
                                         DeleteRequestBody requestBody)
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

            ConnectionHandler handler = instanceHandler.getConnectionHandler(userId, serverName, urlMarker, methodName);

            handler.deleteConnection(userId, connectionGUID, requestBody);
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
     * @param urlMarker  view service URL marker
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public OpenMetadataRootElementsResponse getConnectionsByName(String            serverName,
                                                                 String            urlMarker,
                                                                 FilterRequestBody requestBody)
    {
        final String methodName = "getConnectionsByName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog                         auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ConnectionHandler handler = instanceHandler.getConnectionHandler(userId, serverName, urlMarker, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.getConnectionsByName(userId, requestBody.getFilter(), requestBody));
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
     * @param urlMarker  view service URL marker
     * @param connectionGUID    unique identifier of the required element
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public OpenMetadataRootElementResponse getConnectionByGUID(String             serverName,
                                                               String             urlMarker,
                                                               String             connectionGUID,
                                                               GetRequestBody requestBody)
    {
        final String methodName = "getConnectionByGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        OpenMetadataRootElementResponse response = new OpenMetadataRootElementResponse();
        AuditLog                        auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ConnectionHandler handler = instanceHandler.getConnectionHandler(userId, serverName, urlMarker, methodName);

            response.setElement(handler.getConnectionByGUID(userId, connectionGUID, requestBody));
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
     * @param urlMarker  view service URL marker
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public OpenMetadataRootElementsResponse findConnections(String                  serverName,
                                                            String                  urlMarker,
                                                            SearchStringRequestBody requestBody)
    {
        final String methodName = "findConnections";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog                         auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ConnectionHandler handler = instanceHandler.getConnectionHandler(userId, serverName, urlMarker, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.findConnections(userId, requestBody.getSearchString(), requestBody));
            }
            else
            {
                response.setElements(handler.findConnections(userId, null, null));
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
     * @param urlMarker  view service URL marker
     * @param requestBody             properties for the connectorType.
     *
     * @return unique identifier of the newly created element
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public GUIDResponse createConnectorType(String                serverName,
                                            String                urlMarker,
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
                ConnectorTypeHandler handler = instanceHandler.getConnectorTypeHandler(userId, serverName, urlMarker, methodName);

                if (requestBody.getProperties() instanceof ConnectorTypeProperties connectorTypeProperties)
                {
                    response.setGUID(handler.createConnectorType(userId,
                                                                 requestBody,
                                                                 requestBody.getInitialClassifications(),
                                                                 connectorTypeProperties,
                                                                 requestBody.getParentRelationshipProperties()));
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
     * @param urlMarker  view service URL marker
     * @param requestBody properties that override the template
     *
     * @return unique identifier of the new metadata element
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GUIDResponse createConnectorTypeFromTemplate(String              serverName,
                                                        String              urlMarker,
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
                ConnectorTypeHandler handler = instanceHandler.getConnectorTypeHandler(userId, serverName, urlMarker, methodName);

                response.setGUID(handler.createConnectorTypeFromTemplate(userId,
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
     * Update the properties of a connectorType.
     *
     * @param serverName         name of called server.
     * @param urlMarker  view service URL marker
     * @param connectorTypeGUID unique identifier of the connectorType (returned from create)
     * @param requestBody     properties for the new element.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse updateConnectorType(String                   serverName,
                                            String                   urlMarker,
                                            String                   connectorTypeGUID,
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
                ConnectorTypeHandler handler = instanceHandler.getConnectorTypeHandler(userId, serverName, urlMarker, methodName);

                if (requestBody.getProperties() instanceof ConnectorTypeProperties connectorTypeProperties)
                {
                    handler.updateConnectorType(userId, connectorTypeGUID, requestBody, connectorTypeProperties);
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.updateConnectorType(userId, connectorTypeGUID, requestBody, null);
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
     * Delete a connectorType.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
     * @param connectorTypeGUID  unique identifier of the element to delete
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse deleteConnectorType(String                   serverName,
                                            String                   urlMarker,
                                            String                   connectorTypeGUID,
                                            DeleteRequestBody requestBody)
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

            ConnectorTypeHandler handler = instanceHandler.getConnectorTypeHandler(userId, serverName, urlMarker, methodName);

            handler.deleteConnectorType(userId, connectorTypeGUID, requestBody);
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
     * @param urlMarker  view service URL marker
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public OpenMetadataRootElementsResponse getConnectorTypesByName(String            serverName,
                                                                    String            urlMarker,
                                                                    FilterRequestBody requestBody)
    {
        final String methodName = "getConnectorTypesByName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog                         auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ConnectorTypeHandler handler = instanceHandler.getConnectorTypeHandler(userId, serverName, urlMarker, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.getConnectorTypesByName(userId, requestBody.getFilter(), requestBody));
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
     * @param urlMarker  view service URL marker
     * @param connectorTypeGUID    unique identifier of the required element
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public OpenMetadataRootElementResponse getConnectorTypeByGUID(String             serverName,
                                                                  String             urlMarker,
                                                                  String             connectorTypeGUID,
                                                                  GetRequestBody requestBody)
    {
        final String methodName = "getConnectorTypeByGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        OpenMetadataRootElementResponse response = new OpenMetadataRootElementResponse();
        AuditLog                        auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ConnectorTypeHandler handler = instanceHandler.getConnectorTypeHandler(userId, serverName, urlMarker, methodName);

            response.setElement(handler.getConnectorTypeByGUID(userId, connectorTypeGUID, requestBody));
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
     * @param urlMarker  view service URL marker
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public OpenMetadataRootElementsResponse findConnectorTypes(String                  serverName,
                                                               String                  urlMarker,
                                                               SearchStringRequestBody requestBody)
    {
        final String methodName = "findConnectorTypes";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog                         auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ConnectorTypeHandler handler = instanceHandler.getConnectorTypeHandler(userId, serverName, urlMarker, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.findConnectorTypes(userId, requestBody.getSearchString(), requestBody));
            }
            else
            {
                response.setElements(handler.findConnectorTypes(userId, null, null));
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
     * Create an endpoint.
     *
     * @param serverName                 name of called server.
     * @param urlMarker  view service URL marker
     * @param requestBody             properties for the endpoint.
     *
     * @return unique identifier of the newly created element
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public GUIDResponse createEndpoint(String                serverName,
                                       String                urlMarker,
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
                EndpointHandler handler = instanceHandler.getEndpointHandler(userId, serverName, urlMarker, methodName);

                if (requestBody.getProperties() instanceof EndpointProperties endpointProperties)
                {
                    response.setGUID(handler.createEndpoint(userId,
                                                            requestBody,
                                                            requestBody.getInitialClassifications(),
                                                            endpointProperties,
                                                            requestBody.getParentRelationshipProperties()));
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
     * Create a new metadata element to represent an endpoint using an existing metadata element as a template.
     * The template defines additional classifications and relationships that should be added to the new element.
     *
     * @param serverName             calling user
     * @param urlMarker  view service URL marker
     * @param requestBody properties that override the template
     *
     * @return unique identifier of the new metadata element
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GUIDResponse createEndpointFromTemplate(String              serverName,
                                                   String              urlMarker,
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
                EndpointHandler handler = instanceHandler.getEndpointHandler(userId, serverName, urlMarker, methodName);

                response.setGUID(handler.createEndpointFromTemplate(userId,
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
     * Update the properties of an endpoint.
     *
     * @param serverName         name of called server.
     * @param urlMarker  view service URL marker
     * @param endpointGUID unique identifier of the endpoint (returned from create)
     * @param requestBody     properties for the new element.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse updateEndpoint(String                   serverName,
                                       String                   urlMarker,
                                       String                   endpointGUID,
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
                EndpointHandler handler = instanceHandler.getEndpointHandler(userId, serverName, urlMarker, methodName);

                if (requestBody.getProperties() instanceof EndpointProperties endpointProperties)
                {
                    handler.updateEndpoint(userId,
                                           endpointGUID,
                                           requestBody,
                                           endpointProperties);
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.updateEndpoint(userId,
                                           endpointGUID,
                                           requestBody,
                                           null);
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
     * Delete a endpoint.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
     * @param endpointGUID  unique identifier of the element to delete
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse deleteEndpoint(String                   serverName,
                                       String                   urlMarker,
                                       String                   endpointGUID,
                                       DeleteRequestBody requestBody)
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

            EndpointHandler handler = instanceHandler.getEndpointHandler(userId, serverName, urlMarker, methodName);

            handler.deleteEndpoint(userId, endpointGUID, requestBody);
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
     * @param urlMarker  view service URL marker
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public OpenMetadataRootElementsResponse getEndpointsByName(String            serverName,
                                                               String            urlMarker,
                                                               FilterRequestBody requestBody)
    {
        final String methodName = "getEndpointsByName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog                         auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            EndpointHandler handler = instanceHandler.getEndpointHandler(userId, serverName, urlMarker, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.getEndpointsByName(userId, requestBody.getFilter(), requestBody));
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
     * Retrieve the list of endpoint metadata elements that are attached to a specific infrastructure element.
     *
     * @param serverName name of the service to route the request to
     * @param urlMarker  view service URL marker
     * @param infrastructureGUID element to search for
     * @param requestBody time parameters
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public OpenMetadataRootElementsResponse getEndpointsForInfrastructure(String             serverName,
                                                                          String             urlMarker,
                                                                          String             infrastructureGUID,
                                                                          ResultsRequestBody requestBody)
    {
        final String methodName = "getEndpointsForInfrastructure";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog                         auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            EndpointHandler handler = instanceHandler.getEndpointHandler(userId, serverName, urlMarker, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.getEndpointsForInfrastructure(userId, infrastructureGUID, requestBody));
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
     * Retrieve the list of endpoint metadata elements with a matching networkAddress.
     * There are no wildcards supported on this request.
     *
     * @param serverName name of the service to route the request to
     * @param urlMarker  view service URL marker
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public OpenMetadataRootElementsResponse getEndpointsByNetworkAddress(String            serverName,
                                                                         String            urlMarker,
                                                                         FilterRequestBody requestBody)
    {
        final String methodName = "getEndpointsByNetworkAddress";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog                         auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            EndpointHandler handler = instanceHandler.getEndpointHandler(userId, serverName, urlMarker, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.getEndpointsByNetworkAddress(userId, requestBody.getFilter(), requestBody));
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
     * @param urlMarker  view service URL marker
     * @param endpointGUID    unique identifier of the required element
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public OpenMetadataRootElementResponse getEndpointByGUID(String             serverName,
                                                             String             urlMarker,
                                                             String             endpointGUID,
                                                             GetRequestBody requestBody)
    {
        final String methodName = "getEndpointByGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        OpenMetadataRootElementResponse response = new OpenMetadataRootElementResponse();
        AuditLog                        auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            EndpointHandler handler = instanceHandler.getEndpointHandler(userId, serverName, urlMarker, methodName);

            response.setElement(handler.getEndpointByGUID(userId, endpointGUID, requestBody));
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
     * @param urlMarker  view service URL marker
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public OpenMetadataRootElementsResponse findEndpoints(String                  serverName,
                                                          String                  urlMarker,
                                                          SearchStringRequestBody requestBody)
    {
        final String methodName = "findEndpoints";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog                         auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            EndpointHandler handler = instanceHandler.getEndpointHandler(userId, serverName, urlMarker, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.findEndpoints(userId, requestBody.getSearchString(), requestBody));
            }
            else
            {
                response.setElements(handler.findEndpoints(userId, null, null));
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
