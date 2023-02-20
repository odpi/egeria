/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.digitalarchitecture.server;

import org.odpi.openmetadata.accessservices.digitalarchitecture.metadataelements.ConnectionElement;
import org.odpi.openmetadata.accessservices.digitalarchitecture.metadataelements.EndpointElement;
import org.odpi.openmetadata.accessservices.digitalarchitecture.metadataelements.ConnectorTypeElement;
import org.odpi.openmetadata.accessservices.digitalarchitecture.properties.ConnectionProperties;
import org.odpi.openmetadata.accessservices.digitalarchitecture.properties.EndpointProperties;
import org.odpi.openmetadata.accessservices.digitalarchitecture.properties.ConnectorTypeProperties;
import org.odpi.openmetadata.accessservices.digitalarchitecture.properties.TemplateProperties;
import org.odpi.openmetadata.accessservices.digitalarchitecture.rest.*;
import org.odpi.openmetadata.accessservices.digitalarchitecture.rest.ConnectionResponse;
import org.odpi.openmetadata.accessservices.digitalarchitecture.rest.ConnectorTypeResponse;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.commonservices.generichandlers.ConnectionHandler;
import org.odpi.openmetadata.commonservices.generichandlers.EndpointHandler;
import org.odpi.openmetadata.commonservices.generichandlers.ConnectorTypeHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;

/**
 * ConnectionRESTServices provides the ability to manage connection information.
 */
public class ConnectionRESTServices
{
    private static final DigitalArchitectureInstanceHandler   instanceHandler     = new DigitalArchitectureInstanceHandler();
    private static final RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();
    private static final RESTCallLogger restCallLogger = new RESTCallLogger(LoggerFactory.getLogger(ConnectionRESTServices.class),
                                                                            instanceHandler.getServiceName());


    /**
     * Default constructor
     */
    public ConnectionRESTServices()
    {
    }


    /*
     * ==============================================
     * Manage Connections
     * ==============================================
     */


    /**
     * Create a new metadata element to represent a connection.
     *
     * @param serverName name of calling server
     * @param userId             calling user
     * @param requestBody properties to store
     * @return unique identifier of the new metadata element
     * InvalidParameterException one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to make this request or
     * PropertyServerException the repository is not available or not working properly.
     */
    public GUIDResponse createConnection(String               serverName,
                                         String               userId,
                                         ConnectionProperties requestBody)
    {
        final String methodName = "createConnection";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                ConnectionHandler<ConnectionElement> handler = instanceHandler.getConnectionHandler(userId, serverName, methodName);

                response.setGUID(handler.createConnection(userId,
                                                          null,
                                                          null,
                                                          null,
                                                          null,
                                                          null,
                                                          requestBody.getQualifiedName(),
                                                          requestBody.getDisplayName(),
                                                          requestBody.getDescription(),
                                                          requestBody.getAdditionalProperties(),
                                                          requestBody.getSecuredProperties(),
                                                          requestBody.getConfigurationProperties(),
                                                          requestBody.getUserId(),
                                                          requestBody.getClearPassword(),
                                                          requestBody.getEncryptedPassword(),
                                                          requestBody.getTypeName(),
                                                          requestBody.getExtendedProperties(),
                                                          null,
                                                          null,
                                                          null,
                                                          null,
                                                          null,
                                                          null,
                                                          false,
                                                          false,
                                                          new Date(),
                                                          methodName));
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Create a new metadata element to represent a connection using an existing metadata element as a template.
     * The template defines additional classifications and relationships that should be added to the new connection.
     *
     * @param serverName name of calling server
     * @param userId             calling user
     * @param templateGUID       unique identifier of the metadata element to copy
     * @param templateProperties properties that override the template
     * @return unique identifier of the new metadata element
     * InvalidParameterException one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to make this request or
     * PropertyServerException the repository is not available or not working properly.
     */
    public GUIDResponse createConnectionFromTemplate(String             serverName,
                                                     String             userId,
                                                     String             templateGUID,
                                                     TemplateProperties templateProperties)
    {
        final String methodName = "createConnectionFromTemplate";
        final String templateGUIDParameterName = "templateGUID";
        final String qualifiedNameParameterName   = "qualifiedName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (templateProperties != null)
            {
                ConnectionHandler<ConnectionElement> handler = instanceHandler.getConnectionHandler(userId, serverName, methodName);

                response.setGUID(handler.createConnectionFromTemplate(userId,
                                                                      null,
                                                                      null,
                                                                      templateGUID,
                                                                      templateGUIDParameterName,
                                                                      templateProperties.getQualifiedName(),
                                                                      qualifiedNameParameterName,
                                                                      templateProperties.getDisplayName(),
                                                                      templateProperties.getDescription(),
                                                                      methodName));
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Update the metadata element representing a connection.
     *
     * @param serverName name of calling server
     * @param userId             calling user
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param connectionGUID       unique identifier of the metadata element to update
     * @param requestBody new properties for this element
     * @return void or
     * InvalidParameterException one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to make this request or
     * PropertyServerException the repository is not available or not working properly.
     */
    public VoidResponse updateConnection(String               serverName,
                                         String               userId,
                                         String               connectionGUID,
                                         boolean              isMergeUpdate,
                                         ConnectionProperties requestBody)
    {
        final String methodName = "updateConnection";
        final String guidParameter = "connectionGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                ConnectionHandler<ConnectionElement> handler = instanceHandler.getConnectionHandler(userId, serverName, methodName);

                handler.updateConnection(userId,
                                         null,
                                         null,
                                         connectionGUID,
                                         guidParameter,
                                         requestBody.getQualifiedName(),
                                         requestBody.getDisplayName(),
                                         requestBody.getDescription(),
                                         requestBody.getAdditionalProperties(),
                                         requestBody.getSecuredProperties(),
                                         requestBody.getConfigurationProperties(),
                                         requestBody.getUserId(),
                                         requestBody.getClearPassword(),
                                         requestBody.getEncryptedPassword(),
                                         requestBody.getTypeName(),
                                         requestBody.getExtendedProperties(),
                                         isMergeUpdate,
                                         null,
                                         null,
                                         false,
                                         false,
                                         new Date(),
                                         methodName);
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }



    /**
     * Create a relationship between a connection and a connector type.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param connectionGUID unique identifier of the connection in the external data manager
     * @param connectorTypeGUID unique identifier of the connector type in the external data manager
     * @param requestBody data manager identifiers
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse setupConnectorType(String          serverName,
                                           String          userId,
                                           String          connectionGUID,
                                           String          connectorTypeGUID,
                                           NullRequestBody requestBody)
    {
        final String methodName                     = "setupConnectorType";
        final String connectionGUIDParameterName    = "connectionGUID";
        final String connectorTypeGUIDParameterName = "connectorTypeGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ConnectionHandler<ConnectionElement> handler = instanceHandler.getConnectionHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.addConnectionConnectorType(userId,
                                                   null,
                                                   null,
                                                   connectionGUID,
                                                   connectionGUIDParameterName,
                                                   connectorTypeGUID,
                                                   connectorTypeGUIDParameterName,
                                                   null,
                                                   null,
                                                   false,
                                                   false,
                                                   new Date(),
                                                   methodName);
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Remove a relationship between a connection and a connector type.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param connectionGUID unique identifier of the connection in the external data manager
     * @param connectorTypeGUID unique identifier of the connector type in the external data manager
     * @param requestBody data manager identifiers
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse clearConnectorType(String          serverName,
                                           String          userId,
                                           String          connectionGUID,
                                           String          connectorTypeGUID,
                                           NullRequestBody requestBody)
    {
        final String methodName                     = "clearConnectorType";
        final String connectionGUIDParameterName    = "connectionGUID";
        final String connectorTypeGUIDParameterName = "connectorTypeGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ConnectionHandler<ConnectionElement> handler = instanceHandler.getConnectionHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.removeConnectionConnectorType(userId,
                                                      null,
                                                      null,
                                                      connectionGUID,
                                                      connectionGUIDParameterName,
                                                      connectorTypeGUID,
                                                      connectorTypeGUIDParameterName,
                                                      false,
                                                      false,
                                                      new Date(),
                                                      methodName);
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Create a relationship between a connection and an endpoint.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param connectionGUID unique identifier of the connection in the external data manager
     * @param endpointGUID unique identifier of the endpoint in the external data manager
     * @param requestBody data manager identifiers
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse setupEndpoint(String          serverName,
                                      String          userId,
                                      String          connectionGUID,
                                      String          endpointGUID,
                                      NullRequestBody requestBody)
    {
        final String methodName                  = "setupEndpoint";
        final String connectionGUIDParameterName = "connectionGUID";
        final String endpointGUIDParameterName   = "endpointGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ConnectionHandler<ConnectionElement> handler = instanceHandler.getConnectionHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.addConnectionEndpoint(userId,
                                              null,
                                              null,
                                              connectionGUID,
                                              connectionGUIDParameterName,
                                              endpointGUID,
                                              endpointGUIDParameterName,
                                              null,
                                              null,
                                              false,
                                              false,
                                              new Date(),
                                              methodName);
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Remove a relationship between a connection and an endpoint.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param connectionGUID unique identifier of the connection in the external data manager
     * @param endpointGUID unique identifier of the endpoint in the external data manager
     * @param requestBody data manager identifiers
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse clearEndpoint(String          serverName,
                                      String          userId,
                                      String          connectionGUID,
                                      String          endpointGUID,
                                      NullRequestBody requestBody)
    {
        final String methodName                  = "clearEndpoint";
        final String connectionGUIDParameterName = "connectionGUID";
        final String endpointGUIDParameterName   = "endpointGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ConnectionHandler<ConnectionElement> handler = instanceHandler.getConnectionHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.removeConnectionEndpoint(userId,
                                                 null,
                                                 null,
                                                 connectionGUID,
                                                 connectionGUIDParameterName,
                                                 endpointGUID,
                                                 endpointGUIDParameterName,
                                                 false,
                                                 false,
                                                 new Date(),
                                                 methodName);
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Create a relationship between a virtual connection and an embedded connection.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param connectionGUID unique identifier of the virtual connection in the external data manager
     * @param embeddedConnectionGUID unique identifier of the embedded connection in the external data manager
     * @param requestBody data manager identifiers and properties for the embedded connection
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse setupEmbeddedConnection(String                        serverName,
                                                String                        userId,
                                                String                        connectionGUID,
                                                String                        embeddedConnectionGUID,
                                                EmbeddedConnectionRequestBody requestBody)
    {
        final String methodName                          = "setupEmbeddedConnection";
        final String connectionGUIDParameterName         = "connectionGUID";
        final String embeddedConnectionGUIDParameterName = "embeddedConnectionGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ConnectionHandler<ConnectionElement> handler = instanceHandler.getConnectionHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.addEmbeddedConnection(userId,
                                              null,
                                              null,
                                              connectionGUID,
                                              connectionGUIDParameterName,
                                              requestBody.getPosition(),
                                              requestBody.getDisplayName(),
                                              requestBody.getArguments(),
                                              embeddedConnectionGUID,
                                              embeddedConnectionGUIDParameterName,
                                              null,
                                              null,
                                              false,
                                              false,
                                              new Date(),
                                              methodName);
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Remove a relationship between a virtual connection and an embedded connection.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param connectionGUID unique identifier of the virtual connection in the external data manager
     * @param embeddedConnectionGUID unique identifier of the embedded connection in the external data manager
     * @param requestBody data manager identifiers
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse clearEmbeddedConnection(String          serverName,
                                                String          userId,
                                                String          connectionGUID,
                                                String          embeddedConnectionGUID,
                                                NullRequestBody requestBody)
    {
        final String methodName                          = "clearEmbeddedConnection";
        final String connectionGUIDParameterName         = "connectionGUID";
        final String embeddedConnectionGUIDParameterName = "embeddedConnectionGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ConnectionHandler<ConnectionElement> handler = instanceHandler.getConnectionHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.removeEmbeddedConnection(userId,
                                                 null,
                                                 null,
                                                 connectionGUID,
                                                 connectionGUIDParameterName,
                                                 embeddedConnectionGUID,
                                                 embeddedConnectionGUIDParameterName,
                                                 false,
                                                 false,
                                                 new Date(),
                                                 methodName);
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Create a relationship between an asset and its connection.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param assetGUID unique identifier of the asset
     * @param connectionGUID unique identifier of the  connection
     * @param requestBody data manager identifiers and asset summary
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse setupAssetConnection(String            serverName,
                                             String            userId,
                                             String            assetGUID,
                                             String            connectionGUID,
                                             StringRequestBody requestBody)
    {
        final String methodName                  = "setupAssetConnection";
        final String connectionGUIDParameterName = "connectionGUID";
        final String assetGUIDParameterName      = "assetGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ConnectionHandler<ConnectionElement> handler = instanceHandler.getConnectionHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.addConnectionToAsset(userId,
                                             null,
                                             null,
                                             connectionGUID,
                                             connectionGUIDParameterName,
                                             assetGUID,
                                             assetGUIDParameterName,
                                             requestBody.getString(),
                                             null,
                                             null,
                                             false,
                                             false,
                                             new Date(),
                                             methodName);
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Remove a relationship between an asset and its connection.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param assetGUID unique identifier of the asset
     * @param connectionGUID unique identifier of the connection
     * @param requestBody data manager identifiers
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse clearAssetConnection(String          serverName,
                                             String          userId,
                                             String          assetGUID,
                                             String          connectionGUID,
                                             NullRequestBody requestBody)
    {
        final String methodName                  = "clearAssetConnection";
        final String connectionGUIDParameterName = "connectionGUID";
        final String assetGUIDParameterName      = "assetGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ConnectionHandler<ConnectionElement> handler = instanceHandler.getConnectionHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.removeConnectionToAsset(userId,
                                                null,
                                                null,
                                                connectionGUID,
                                                connectionGUIDParameterName,
                                                assetGUID,
                                                assetGUIDParameterName,
                                                false,
                                                false,
                                                new Date(),
                                                methodName);
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }



    /**
     * Remove the metadata element representing a connection.
     *
     * @param serverName name of calling server
     * @param userId calling user
     * @param connectionGUID unique identifier of the metadata element to remove
     * @param requestBody null request body
     *
     * @return void or
     * InvalidParameterException one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to make this request or
     * PropertyServerException the repository is not available or not working properly.
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse removeConnection(String          serverName,
                                         String          userId,
                                         String          connectionGUID,
                                         NullRequestBody requestBody)
    {
        final String   methodName = "removeConnection";
        final String   guidParameter = "connectionGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ConnectionHandler<ConnectionElement> handler = instanceHandler.getConnectionHandler(userId, serverName, methodName);

            handler.removeConnection(userId,
                                     null,
                                     null,
                                     connectionGUID,
                                     guidParameter,
                                     false,
                                     false,
                                     new Date(),
                                     methodName);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Retrieve the list of connection metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param serverName name of calling server
     * @param userId calling user
     * @param requestBody string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements or
     * InvalidParameterException one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to make this request or
     * PropertyServerException the repository is not available or not working properly.
     */
    public ConnectionsResponse findConnections(String                  serverName,
                                               String                  userId,
                                               SearchStringRequestBody requestBody,
                                               int                     startFrom,
                                               int                     pageSize)
    {
        final String methodName = "findConnections";
        final String parameterName = "searchString";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ConnectionsResponse response = new ConnectionsResponse();
        AuditLog            auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                ConnectionHandler<ConnectionElement> handler = instanceHandler.getConnectionHandler(userId, serverName, methodName);

                List<ConnectionElement> connections = handler.findConnections(userId,
                                                                              requestBody.getSearchString(),
                                                                              parameterName,
                                                                              startFrom,
                                                                              pageSize,
                                                                              false,
                                                                              false,
                                                                              new Date(),
                                                                              methodName);
                response.setElementList(connections);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Retrieve the list of connection metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param serverName name of calling server
     * @param userId calling user
     * @param requestBody name to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements or
     * InvalidParameterException one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to make this request or
     * PropertyServerException the repository is not available or not working properly.
     */
    public ConnectionsResponse getConnectionsByName(String          serverName,
                                                    String          userId,
                                                    NameRequestBody requestBody,
                                                    int             startFrom,
                                                    int             pageSize)
    {
        final String methodName = "getConnectionsByName";
        final String parameterName = "name";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ConnectionsResponse response = new ConnectionsResponse();
        AuditLog            auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                ConnectionHandler<ConnectionElement> handler = instanceHandler.getConnectionHandler(userId, serverName, methodName);

                List<ConnectionElement> connections = handler.getConnectionsByName(userId,
                                                                                   requestBody.getName(),
                                                                                   parameterName,
                                                                                   startFrom,
                                                                                   pageSize,
                                                                                   false,
                                                                                   false,
                                                                                   new Date(),
                                                                                   methodName);
                response.setElementList(connections);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Retrieve the connection metadata element with the supplied unique identifier.
     *
     * @param serverName name of calling server
     * @param userId calling user
     * @param connectionGUID unique identifier of the requested metadata element
     *
     * @return matching metadata element or
     * InvalidParameterException one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to make this request or
     * PropertyServerException the repository is not available or not working properly.
     */
    public ConnectionResponse getConnectionByGUID(String serverName,
                                                  String userId,
                                                  String connectionGUID)
    {
        final String methodName = "getConnectionByGUID";
        final String connectionGUIDParameter = "connectionGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ConnectionResponse response = new ConnectionResponse();
        AuditLog           auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ConnectionHandler<ConnectionElement> handler = instanceHandler.getConnectionHandler(userId, serverName, methodName);

            ConnectionElement connection = handler.getConnectionByGUID(userId,
                                                                       connectionGUID,
                                                                       connectionGUIDParameter,
                                                                       false,
                                                                       false,
                                                                       new Date(),
                                                                       methodName);
            response.setElement(connection);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }



    /*
     * ==============================================
     * Manage Endpoints
     * ==============================================
     */


    /**
     * Create a new metadata element to represent an endpoint. Classifications can be added later to define the
     * type of endpoint.
     *
     * @param serverName name of calling server
     * @param userId             calling user
     * @param endpointProperties properties to store
     * @return unique identifier of the new metadata element
     * InvalidParameterException one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to make this request or
     * PropertyServerException the repository is not available or not working properly.
     */
    public GUIDResponse createEndpoint(String             serverName,
                                       String             userId,
                                       EndpointProperties endpointProperties)
    {
        final String methodName = "createEndpoint";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (endpointProperties != null)
            {
                EndpointHandler<EndpointElement> handler = instanceHandler.getEndpointHandler(userId, serverName, methodName);

                String endpointGUID = handler.createEndpoint(userId,
                                                             null,
                                                             null,
                                                             null,
                                                             endpointProperties.getQualifiedName(),
                                                             endpointProperties.getResourceName(),
                                                             endpointProperties.getResourceDescription(),
                                                             endpointProperties.getAddress(),
                                                             endpointProperties.getProtocol(),
                                                             endpointProperties.getEncryptionMethod(),
                                                             endpointProperties.getAdditionalProperties(),
                                                             endpointProperties.getTypeName(),
                                                             endpointProperties.getExtendedProperties(),
                                                             null,
                                                             null,
                                                             new Date(),
                                                             methodName);

                response.setGUID(endpointGUID);
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Create a new metadata element to represent an endpoint using an existing metadata element as a template.
     * The template defines additional classifications and relationships that should be added to the new endpoint.
     *
     * @param serverName name of calling server
     * @param userId             calling user
     * @param networkAddress     location of the endpoint in the network
     * @param templateGUID       unique identifier of the metadata element to copy
     * @param templateProperties properties that override the template
     * @return unique identifier of the new metadata element
     * InvalidParameterException one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to make this request or
     * PropertyServerException the repository is not available or not working properly.
     */
    public GUIDResponse createEndpointFromTemplate(String             serverName,
                                                   String             userId,
                                                   String             networkAddress,
                                                   String             templateGUID,
                                                   TemplateProperties templateProperties)
    {
        final String methodName = "createEndpointFromTemplate";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (templateProperties != null)
            {
                EndpointHandler<EndpointElement> handler = instanceHandler.getEndpointHandler(userId, serverName, methodName);

                response.setGUID(handler.createEndpointFromTemplate(userId,
                                                                    null,
                                                                    null,
                                                                    templateGUID,
                                                                    templateProperties.getQualifiedName(),
                                                                    templateProperties.getDisplayName(),
                                                                    templateProperties.getDescription(),
                                                                    networkAddress,
                                                                    methodName));
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Update the metadata element representing a endpoint.
     *
     * @param serverName name of calling server
     * @param userId             calling user
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param endpointGUID       unique identifier of the metadata element to update
     * @param endpointProperties new properties for this element
     * @return void or
     * InvalidParameterException one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to make this request or
     * PropertyServerException the repository is not available or not working properly.
     */
    public VoidResponse updateEndpoint(String             serverName,
                                       String             userId,
                                       String             endpointGUID,
                                       boolean            isMergeUpdate,
                                       EndpointProperties endpointProperties)
    {
        final String methodName = "updateEndpoint";
        final String guidParameter = "endpointGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (endpointProperties != null)
            {
                EndpointHandler<EndpointElement> handler = instanceHandler.getEndpointHandler(userId, serverName, methodName);

                handler.updateEndpoint(userId,
                                       null,
                                       null,
                                       endpointGUID,
                                       guidParameter,
                                       endpointProperties.getQualifiedName(),
                                       endpointProperties.getResourceName(),
                                       endpointProperties.getResourceDescription(),
                                       endpointProperties.getAddress(),
                                       endpointProperties.getProtocol(),
                                       endpointProperties.getEncryptionMethod(),
                                       endpointProperties.getAdditionalProperties(),
                                       endpointProperties.getTypeName(),
                                       endpointProperties.getExtendedProperties(),
                                       isMergeUpdate,
                                       null,
                                       null,
                                       false,
                                       false,
                                       new Date(),
                                       methodName);
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Remove the metadata element representing a endpoint.
     *
     * @param serverName name of calling server
     * @param userId calling user
     * @param endpointGUID unique identifier of the metadata element to remove
     * @param requestBody null request body
     *
     * @return void or
     * InvalidParameterException one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to make this request or
     * PropertyServerException the repository is not available or not working properly.
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse removeEndpoint(String          serverName,
                                       String          userId,
                                       String          endpointGUID,
                                       NullRequestBody requestBody)
    {
        final String   methodName = "removeEndpoint";
        final String   guidParameter = "endpointGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            EndpointHandler<EndpointElement> handler = instanceHandler.getEndpointHandler(userId, serverName, methodName);

            handler.removeEndpoint(userId,
                                   null,
                                   null,
                                   endpointGUID,
                                   guidParameter,
                                   false,
                                   false,
                                   new Date(),
                                   methodName);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Retrieve the list of endpoint metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param serverName name of calling server
     * @param userId calling user
     * @param requestBody string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements or
     * InvalidParameterException one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to make this request or
     * PropertyServerException the repository is not available or not working properly.
     */
    public EndpointsResponse findEndpoints(String                  serverName,
                                           String                  userId,
                                           SearchStringRequestBody requestBody,
                                           int                     startFrom,
                                           int                     pageSize)
    {
        final String methodName = "findEndpoints";
        final String parameterName = "searchString";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        EndpointsResponse response = new EndpointsResponse();
        AuditLog          auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                EndpointHandler<EndpointElement> handler = instanceHandler.getEndpointHandler(userId, serverName, methodName);

                List<EndpointElement> endpoints = handler.findEndpoints(userId,
                                                                        requestBody.getSearchString(),
                                                                        parameterName,
                                                                        startFrom,
                                                                        pageSize,
                                                                        false,
                                                                        false,
                                                                        new Date(),
                                                                        methodName);
                response.setElementList(endpoints);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Retrieve the list of endpoint metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param serverName name of calling server
     * @param userId calling user
     * @param requestBody name to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements or
     * InvalidParameterException one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to make this request or
     * PropertyServerException the repository is not available or not working properly.
     */
    public EndpointsResponse getEndpointsByName(String          serverName,
                                                String          userId,
                                                NameRequestBody requestBody,
                                                int             startFrom,
                                                int             pageSize)
    {
        final String methodName = "getEndpointsByName";
        final String parameterName = "name";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        EndpointsResponse response = new EndpointsResponse();
        AuditLog          auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                EndpointHandler<EndpointElement> handler = instanceHandler.getEndpointHandler(userId, serverName, methodName);

                List<EndpointElement> endpoints = handler.getEndpointsByName(userId,
                                                                             requestBody.getName(),
                                                                             parameterName,
                                                                             startFrom,
                                                                             pageSize,
                                                                             false,
                                                                             false,
                                                                             new Date(),
                                                                             methodName);
                response.setElementList(endpoints);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Retrieve the endpoint metadata element with the supplied unique identifier.
     *
     * @param serverName name of calling server
     * @param userId calling user
     * @param endpointGUID unique identifier of the requested metadata element
     *
     * @return matching metadata element or
     * InvalidParameterException one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to make this request or
     * PropertyServerException the repository is not available or not working properly.
     */
    public EndpointResponse getEndpointByGUID(String serverName,
                                              String userId,
                                              String endpointGUID)
    {
        final String methodName = "getEndpointByGUID";
        final String endpointGUIDParameter = "endpointGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        EndpointResponse response = new EndpointResponse();
        AuditLog         auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            EndpointHandler<EndpointElement> handler = instanceHandler.getEndpointHandler(userId, serverName, methodName);

            EndpointElement endpoint = handler.getEndpointByGUID(userId,
                                                                 endpointGUID,
                                                                 endpointGUIDParameter,
                                                                 false,
                                                                 false,
                                                                 new Date(),
                                                                 methodName);

            response.setElement(endpoint);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }



    /*
     * ==============================================
     * Manage ConnectorTypes
     * ==============================================
     */


    /**
     * Create a new metadata element to represent a connectorType. Classifications can be added later to define the
     * type of connectorType.
     *
     * @param serverName name of calling server
     * @param userId             calling user
     * @param connectorTypeProperties properties to store
     * @return unique identifier of the new metadata element
     * InvalidParameterException one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to make this request or
     * PropertyServerException the repository is not available or not working properly.
     */
    public GUIDResponse createConnectorType(String                  serverName,
                                            String                  userId,
                                            ConnectorTypeProperties connectorTypeProperties)
    {
        final String methodName = "createConnectorType";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (connectorTypeProperties != null)
            {
                ConnectorTypeHandler<ConnectorTypeElement> handler = instanceHandler.getConnectorTypeHandler(userId, serverName, methodName);

                response.setGUID(handler.createConnectorType(userId,
                                                             null,
                                                             null,
                                                             null,
                                                             connectorTypeProperties.getQualifiedName(),
                                                             connectorTypeProperties.getDisplayName(),
                                                             connectorTypeProperties.getDescription(),
                                                             connectorTypeProperties.getSupportedAssetTypeName(),
                                                             connectorTypeProperties.getExpectedDataFormat(),
                                                             connectorTypeProperties.getConnectorProviderClassName(),
                                                             connectorTypeProperties.getConnectorFrameworkName(),
                                                             connectorTypeProperties.getConnectorInterfaceLanguage(),
                                                             connectorTypeProperties.getConnectorInterfaces(),
                                                             connectorTypeProperties.getTargetTechnologySource(),
                                                             connectorTypeProperties.getTargetTechnologyName(),
                                                             connectorTypeProperties.getTargetTechnologyInterfaces(),
                                                             connectorTypeProperties.getTargetTechnologyVersions(),
                                                             connectorTypeProperties.getRecognizedAdditionalProperties(),
                                                             connectorTypeProperties.getRecognizedSecuredProperties(),
                                                             connectorTypeProperties.getRecognizedConfigurationProperties(),
                                                             connectorTypeProperties.getAdditionalProperties(),
                                                             connectorTypeProperties.getTypeName(),
                                                             connectorTypeProperties.getExtendedProperties(),
                                                             null,
                                                             null,
                                                             new Date(),
                                                             methodName));
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Create a new metadata element to represent a connectorType using an existing metadata element as a template.
     * The template defines additional classifications and relationships that should be added to the new connectorType.
     *
     * @param serverName name of calling server
     * @param userId             calling user
     * @param templateGUID       unique identifier of the metadata element to copy
     * @param templateProperties properties that override the template
     * @return unique identifier of the new metadata element
     * InvalidParameterException one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to make this request or
     * PropertyServerException the repository is not available or not working properly.
     */
    public GUIDResponse createConnectorTypeFromTemplate(String             serverName,
                                                        String             userId,
                                                        String             templateGUID,
                                                        TemplateProperties templateProperties)
    {
        final String methodName = "createConnectorTypeFromTemplate";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (templateProperties != null)
            {
                ConnectorTypeHandler<ConnectorTypeElement> handler = instanceHandler.getConnectorTypeHandler(userId, serverName, methodName);

                response.setGUID(handler.createConnectorTypeFromTemplate(userId,
                                                                         null,
                                                                         null,
                                                                         templateGUID,
                                                                         templateProperties.getQualifiedName(),
                                                                         templateProperties.getDisplayName(),
                                                                         templateProperties.getDescription(),
                                                                         methodName));
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Update the metadata element representing a connectorType.
     *
     * @param serverName name of calling server
     * @param userId             calling user
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param connectorTypeGUID       unique identifier of the metadata element to update
     * @param connectorTypeProperties new properties for this element
     * @return void or
     * InvalidParameterException one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to make this request or
     * PropertyServerException the repository is not available or not working properly.
     */
    public VoidResponse updateConnectorType(String                  serverName,
                                            String                  userId,
                                            String                  connectorTypeGUID,
                                            boolean                 isMergeUpdate,
                                            ConnectorTypeProperties connectorTypeProperties)
    {
        final String methodName = "updateConnectorType";
        final String guidParameter = "connectorTypeGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (connectorTypeProperties != null)
            {
                ConnectorTypeHandler<ConnectorTypeElement> handler = instanceHandler.getConnectorTypeHandler(userId, serverName, methodName);

                handler.updateConnectorType(userId,
                                            null,
                                            null,
                                            connectorTypeGUID,
                                            guidParameter,
                                            connectorTypeProperties.getQualifiedName(),
                                            connectorTypeProperties.getDisplayName(),
                                            connectorTypeProperties.getDescription(),
                                            connectorTypeProperties.getSupportedAssetTypeName(),
                                            connectorTypeProperties.getExpectedDataFormat(),
                                            connectorTypeProperties.getConnectorProviderClassName(),
                                            connectorTypeProperties.getConnectorFrameworkName(),
                                            connectorTypeProperties.getConnectorInterfaceLanguage(),
                                            connectorTypeProperties.getConnectorInterfaces(),
                                            connectorTypeProperties.getTargetTechnologySource(),
                                            connectorTypeProperties.getTargetTechnologyName(),
                                            connectorTypeProperties.getTargetTechnologyInterfaces(),
                                            connectorTypeProperties.getTargetTechnologyVersions(),
                                            connectorTypeProperties.getRecognizedAdditionalProperties(),
                                            connectorTypeProperties.getRecognizedSecuredProperties(),
                                            connectorTypeProperties.getRecognizedConfigurationProperties(),
                                            connectorTypeProperties.getAdditionalProperties(),
                                            connectorTypeProperties.getTypeName(),
                                            connectorTypeProperties.getExtendedProperties(),
                                            null,
                                            null,
                                            isMergeUpdate,
                                            false,
                                            false,
                                            new Date(),
                                            methodName);
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Remove the metadata element representing a connectorType.
     *
     * @param serverName name of calling server
     * @param userId calling user
     * @param connectorTypeGUID unique identifier of the metadata element to remove
     * @param requestBody null request body
     *
     * @return void or
     * InvalidParameterException one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to make this request or
     * PropertyServerException the repository is not available or not working properly.
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse removeConnectorType(String          serverName,
                                            String          userId,
                                            String          connectorTypeGUID,
                                            NullRequestBody requestBody)
    {
        final String   methodName = "removeConnectorType";
        final String   guidParameter = "connectorTypeGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ConnectorTypeHandler<ConnectorTypeElement> handler = instanceHandler.getConnectorTypeHandler(userId, serverName, methodName);

            handler.removeConnectorType(userId,
                                        null,
                                        null,
                                        connectorTypeGUID,
                                        guidParameter,
                                        false,
                                        false,
                                        new Date(),
                                        methodName);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Retrieve the list of connectorType metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param serverName name of calling server
     * @param userId calling user
     * @param requestBody string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements or
     * InvalidParameterException one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to make this request or
     * PropertyServerException the repository is not available or not working properly.
     */
    public ConnectorTypesResponse findConnectorTypes(String                  serverName,
                                                     String                  userId,
                                                     SearchStringRequestBody requestBody,
                                                     int                     startFrom,
                                                     int                     pageSize)
    {
        final String methodName = "findConnectorTypes";
        final String parameterName = "searchString";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ConnectorTypesResponse response = new ConnectorTypesResponse();
        AuditLog          auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                ConnectorTypeHandler<ConnectorTypeElement> handler = instanceHandler.getConnectorTypeHandler(userId, serverName, methodName);

                List<ConnectorTypeElement> connectorTypes = handler.findConnectorTypes(userId,
                                                                                       requestBody.getSearchString(),
                                                                                       parameterName,
                                                                                       startFrom,
                                                                                       pageSize,
                                                                                       false,
                                                                                       false,
                                                                                       new Date(),
                                                                                       methodName);
                response.setElementList(connectorTypes);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Retrieve the list of connectorType metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param serverName name of calling server
     * @param userId calling user
     * @param requestBody name to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements or
     * InvalidParameterException one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to make this request or
     * PropertyServerException the repository is not available or not working properly.
     */
    public ConnectorTypesResponse getConnectorTypesByName(String          serverName,
                                                          String          userId,
                                                          NameRequestBody requestBody,
                                                          int             startFrom,
                                                          int             pageSize)
    {
        final String methodName = "getConnectorTypesByName";
        final String parameterName = "name";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ConnectorTypesResponse response = new ConnectorTypesResponse();
        AuditLog          auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                ConnectorTypeHandler<ConnectorTypeElement> handler = instanceHandler.getConnectorTypeHandler(userId, serverName, methodName);

                List<ConnectorTypeElement> connectorTypes = handler.getConnectorTypesByName(userId,
                                                                                            requestBody.getName(),
                                                                                            parameterName,
                                                                                            startFrom,
                                                                                            pageSize,
                                                                                            false,
                                                                                            false,
                                                                                            new Date(),
                                                                                            methodName);
                response.setElementList(connectorTypes);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Retrieve the connectorType metadata element with the supplied unique identifier.
     *
     * @param serverName name of calling server
     * @param userId calling user
     * @param connectorTypeGUID unique identifier of the requested metadata element
     *
     * @return matching metadata element or
     * InvalidParameterException one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to make this request or
     * PropertyServerException the repository is not available or not working properly.
     */
    public ConnectorTypeResponse getConnectorTypeByGUID(String serverName,
                                                        String userId,
                                                        String connectorTypeGUID)
    {
        final String methodName = "getConnectorTypeByGUID";
        final String connectorTypeGUIDParameter = "connectorTypeGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ConnectorTypeResponse response = new ConnectorTypeResponse();
        AuditLog         auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ConnectorTypeHandler<ConnectorTypeElement> handler = instanceHandler.getConnectorTypeHandler(userId, serverName, methodName);

            ConnectorTypeElement connectorType = handler.getConnectorTypeByGUID(userId,
                                                                                connectorTypeGUID,
                                                                                connectorTypeGUIDParameter,
                                                                                false,
                                                                                false,
                                                                                new Date(),
                                                                                methodName);
            response.setElement(connectorType);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }
}
