/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.itinfrastructure.server;


import org.odpi.openmetadata.accessservices.itinfrastructure.metadataelements.ConnectionElement;
import org.odpi.openmetadata.accessservices.itinfrastructure.metadataelements.ConnectorTypeElement;
import org.odpi.openmetadata.accessservices.itinfrastructure.metadataelements.EndpointElement;
import org.odpi.openmetadata.accessservices.itinfrastructure.properties.ConnectionProperties;
import org.odpi.openmetadata.accessservices.itinfrastructure.properties.ConnectorTypeProperties;
import org.odpi.openmetadata.accessservices.itinfrastructure.properties.EndpointProperties;
import org.odpi.openmetadata.accessservices.itinfrastructure.rest.AssetConnectionRequestBody;
import org.odpi.openmetadata.accessservices.itinfrastructure.rest.ConnectionRequestBody;
import org.odpi.openmetadata.accessservices.itinfrastructure.rest.ConnectionResponse;
import org.odpi.openmetadata.accessservices.itinfrastructure.rest.ConnectionsResponse;
import org.odpi.openmetadata.accessservices.itinfrastructure.rest.ConnectorTypeRequestBody;
import org.odpi.openmetadata.accessservices.itinfrastructure.rest.ConnectorTypeResponse;
import org.odpi.openmetadata.accessservices.itinfrastructure.rest.ConnectorTypesResponse;
import org.odpi.openmetadata.accessservices.itinfrastructure.rest.EmbeddedConnectionRequestBody;
import org.odpi.openmetadata.accessservices.itinfrastructure.rest.EndpointRequestBody;
import org.odpi.openmetadata.accessservices.itinfrastructure.rest.EndpointResponse;
import org.odpi.openmetadata.accessservices.itinfrastructure.rest.EndpointsResponse;
import org.odpi.openmetadata.accessservices.itinfrastructure.rest.MetadataSourceRequestBody;
import org.odpi.openmetadata.accessservices.itinfrastructure.rest.TemplateRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.NameRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.SearchStringRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.commonservices.generichandlers.ConnectionHandler;
import org.odpi.openmetadata.commonservices.generichandlers.ConnectorTypeHandler;
import org.odpi.openmetadata.commonservices.generichandlers.EndpointHandler;
import org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;


/**
 * The ITInfrastructureRESTServices provides the server-side implementation of the IT Infrastructure Open Metadata
 * Assess Service (OMAS).  This interface provides support for defining infrastructure assets and their supporting
 * elements.
 */
public class ITInfrastructureRESTServices
{
    private static ITInfrastructureInstanceHandler instanceHandler = new ITInfrastructureInstanceHandler();

    private static RESTCallLogger restCallLogger = new RESTCallLogger(LoggerFactory.getLogger(ITInfrastructureRESTServices.class),
                                                                      instanceHandler.getServiceName());

    private RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();

    /**
     * Default constructor
     */
    public ITInfrastructureRESTServices()
    {
    }


    /* ========================================================
     * The connection carries the information to create a connector
     */

    /**
     * Create a new metadata element to represent a connection.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param requestBody properties to store
     *
     * @return unique identifier of the new metadata element or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GUIDResponse createConnection(String                serverName,
                                         String                userId,
                                         ConnectionRequestBody requestBody)
    {
        final String methodName = "createConnection";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ConnectionHandler<ConnectionElement> handler = instanceHandler.getConnectionHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                String connectionGUID = handler.createConnection(userId,
                                                                 requestBody.getExternalSourceGUID(),
                                                                 requestBody.getExternalSourceName(),
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
                                                                 methodName);

                if (connectionGUID != null)
                {
                    handler.setVendorProperties(userId,
                                                connectionGUID,
                                                requestBody.getVendorProperties(),
                                                methodName);
                }

                response.setGUID(connectionGUID);
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
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param templateGUID unique identifier of the metadata element to copy
     * @param requestBody properties that override the template
     *
     * @return unique identifier of the new metadata element or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GUIDResponse createConnectionFromTemplate(String              serverName,
                                                     String              userId,
                                                     String              templateGUID,
                                                     TemplateRequestBody requestBody)
    {
        final String methodName                   = "createConnectionFromTemplate";
        final String templateGUIDParameterName    = "templateGUID";
        final String qualifiedNameParameterName   = "qualifiedName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ConnectionHandler<ConnectionElement> handler = instanceHandler.getConnectionHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                String connectionGUID;

                connectionGUID = handler.createConnectionFromTemplate(userId,
                                                                      requestBody.getExternalSourceGUID(),
                                                                      requestBody.getExternalSourceName(),
                                                                      templateGUID,
                                                                      templateGUIDParameterName,
                                                                      requestBody.getQualifiedName(),
                                                                      qualifiedNameParameterName,
                                                                      requestBody.getDisplayName(),
                                                                      requestBody.getDescription(),
                                                                      methodName);

                response.setGUID(connectionGUID);
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
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param connectionGUID unique identifier of the metadata element to update
     * @param isMergeUpdate should the new properties be merged with the existing properties of overlay them?
     * @param requestBody new properties for this element
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse updateConnection(String                serverName,
                                         String                userId,
                                         String                connectionGUID,
                                         boolean               isMergeUpdate,
                                         ConnectionRequestBody requestBody)
    {
        final String methodName = "updateConnection";
        final String connectionGUIDParameterName = "connectionGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ConnectionHandler<ConnectionElement> handler = instanceHandler.getConnectionHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.updateConnection(userId,
                                         requestBody.getExternalSourceGUID(),
                                         requestBody.getExternalSourceName(),
                                         connectionGUID,
                                         connectionGUIDParameterName,
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
                                         methodName);

                if ((!isMergeUpdate) || (requestBody.getVendorProperties() != null))
                {
                    handler.setVendorProperties(userId,
                                                connectionGUID,
                                                requestBody.getVendorProperties(),
                                                methodName);
                }
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
    public VoidResponse setupConnectorType(String                    serverName,
                                           String                    userId,
                                           String                    connectionGUID,
                                           String                    connectorTypeGUID,
                                           MetadataSourceRequestBody requestBody)
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
                                                   requestBody.getExternalSourceGUID(),
                                                   requestBody.getExternalSourceName(),
                                                   connectionGUID,
                                                   connectionGUIDParameterName,
                                                   connectorTypeGUID,
                                                   connectorTypeGUIDParameterName,
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
    public VoidResponse clearConnectorType(String                    serverName,
                                           String                    userId,
                                           String                    connectionGUID,
                                           String                    connectorTypeGUID,
                                           MetadataSourceRequestBody requestBody)
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
                                                      requestBody.getExternalSourceGUID(),
                                                      requestBody.getExternalSourceName(),
                                                      connectionGUID,
                                                      connectionGUIDParameterName,
                                                      connectorTypeGUID,
                                                      connectorTypeGUIDParameterName,
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
    public VoidResponse setupEndpoint(String                    serverName,
                                      String                    userId,
                                      String                    connectionGUID,
                                      String                    endpointGUID,
                                      MetadataSourceRequestBody requestBody)
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
                                              requestBody.getExternalSourceGUID(),
                                              requestBody.getExternalSourceName(),
                                              connectionGUID,
                                              connectionGUIDParameterName,
                                              endpointGUID,
                                              endpointGUIDParameterName,
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
    public VoidResponse clearEndpoint(String                    serverName,
                                      String                    userId,
                                      String                    connectionGUID,
                                      String                    endpointGUID,
                                      MetadataSourceRequestBody requestBody)
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
                                                 requestBody.getExternalSourceGUID(),
                                                 requestBody.getExternalSourceName(),
                                                 connectionGUID,
                                                 connectionGUIDParameterName,
                                                 endpointGUID,
                                                 endpointGUIDParameterName,
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
                                              requestBody.getExternalSourceGUID(),
                                              requestBody.getExternalSourceName(),
                                              connectionGUID,
                                              connectionGUIDParameterName,
                                              requestBody.getPosition(),
                                              requestBody.getDisplayName(),
                                              requestBody.getArguments(),
                                              embeddedConnectionGUID,
                                              embeddedConnectionGUIDParameterName,
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
    public VoidResponse clearEmbeddedConnection(String                    serverName,
                                                String                    userId,
                                                String                    connectionGUID,
                                                String                    embeddedConnectionGUID,
                                                MetadataSourceRequestBody requestBody)
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
                                                 requestBody.getExternalSourceGUID(),
                                                 requestBody.getExternalSourceName(),
                                                 connectionGUID,
                                                 connectionGUIDParameterName,
                                                 embeddedConnectionGUID,
                                                 embeddedConnectionGUIDParameterName,
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
    public VoidResponse setupAssetConnection(String                     serverName,
                                             String                     userId,
                                             String                     assetGUID,
                                             String                     connectionGUID,
                                             AssetConnectionRequestBody requestBody)
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
                                             requestBody.getExternalSourceGUID(),
                                             requestBody.getExternalSourceName(),
                                             connectionGUID,
                                             connectionGUIDParameterName,
                                             assetGUID,
                                             assetGUIDParameterName,
                                             requestBody.getAssetSummary(),
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
    public VoidResponse clearAssetConnection(String                    serverName,
                                             String                    userId,
                                             String                    assetGUID,
                                             String                    connectionGUID,
                                             MetadataSourceRequestBody requestBody)
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
                                                requestBody.getExternalSourceGUID(),
                                                requestBody.getExternalSourceName(),
                                                connectionGUID,
                                                connectionGUIDParameterName,
                                                assetGUID,
                                                assetGUIDParameterName,
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
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param connectionGUID unique identifier of the metadata element to remove
     * @param requestBody external source identifiers
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse removeConnection(String                    serverName,
                                         String                    userId,
                                         String                    connectionGUID,
                                         MetadataSourceRequestBody requestBody)
    {
        final String methodName = "removeConnection";
        final String connectionGUIDParameterName = "connectionGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            if (requestBody != null)
            {
                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

                ConnectionHandler<ConnectionElement> handler = instanceHandler.getConnectionHandler(userId, serverName, methodName);

                handler.removeConnection(userId,
                                         requestBody.getExternalSourceGUID(),
                                         requestBody.getExternalSourceName(),
                                         connectionGUID,
                                         connectionGUIDParameterName,
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
     * Retrieve the list of connection metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param requestBody string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public ConnectionsResponse findConnections(String                  serverName,
                                               String                  userId,
                                               SearchStringRequestBody requestBody,
                                               int                     startFrom,
                                               int                     pageSize)
    {
        final String methodName = "findConnections";
        final String searchStringParameterName = "searchString";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ConnectionsResponse response = new ConnectionsResponse();
        AuditLog          auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                ConnectionHandler<ConnectionElement> handler = instanceHandler.getConnectionHandler(userId, serverName, methodName);

                List<ConnectionElement> connections = handler.findConnections(userId,
                                                                              requestBody.getSearchString(),
                                                                              searchStringParameterName,
                                                                              startFrom,
                                                                              pageSize,
                                                                              new Date(),
                                                                              methodName);

                response.setElementList(setUpVendorProperties(userId, connections, handler, methodName));
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
     * Retrieve the list of connection metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param requestBody name to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public ConnectionsResponse   getConnectionsByName(String          serverName,
                                                      String          userId,
                                                      NameRequestBody requestBody,
                                                      int             startFrom,
                                                      int             pageSize)
    {
        final String methodName = "getConnectionsByName";
        final String nameParameterName = "name";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ConnectionsResponse response = new ConnectionsResponse();
        AuditLog          auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                ConnectionHandler<ConnectionElement> handler = instanceHandler.getConnectionHandler(userId, serverName, methodName);

                List<ConnectionElement> connections = handler.getConnectionsByName(userId,
                                                                                   requestBody.getName(),
                                                                                   nameParameterName,
                                                                                   startFrom,
                                                                                   pageSize,
                                                                                   new Date(),
                                                                                   methodName);

                response.setElementList(setUpVendorProperties(userId, connections, handler, methodName));
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
     * Retrieve the connection metadata element with the supplied unique identifier.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param guid unique identifier of the requested metadata element
     *
     * @return matching metadata element or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public ConnectionResponse getConnectionByGUID(String serverName,
                                                  String userId,
                                                  String guid)
    {
        final String methodName = "getConnectionByGUID";
        final String guidParameterName = "connectionGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ConnectionResponse response = new ConnectionResponse();
        AuditLog         auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ConnectionHandler<ConnectionElement> handler = instanceHandler.getConnectionHandler(userId, serverName, methodName);

            ConnectionElement connection = handler.getConnectionByGUID(userId, guid, guidParameterName, new Date(), methodName);

            response.setElement(setUpVendorProperties(userId, connection, handler, methodName));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }




    /* ===============================================================================
     * A connector type defines the implementation of a connector.
     */



    /**
     * Create a new metadata element to represent a connector type.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param requestBody properties about the endpoint
     *
     * @return unique identifier of the new endpoint or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GUIDResponse createConnectorType(String                   serverName,
                                            String                   userId,
                                            ConnectorTypeRequestBody requestBody)
    {
        final String methodName = "createConnectorType";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ConnectorTypeHandler<ConnectorTypeElement> handler = instanceHandler.getConnectorTypeHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                String connectorTypeGUID = handler.createConnectorType(userId,
                                                                       null,
                                                                       null,
                                                                       null,
                                                                       requestBody.getQualifiedName(),
                                                                       requestBody.getDisplayName(),
                                                                       requestBody.getDescription(),
                                                                       requestBody.getSupportedAssetTypeName(),
                                                                       requestBody.getExpectedDataFormat(),
                                                                       requestBody.getConnectorProviderClassName(),
                                                                       requestBody.getConnectorFrameworkName(),
                                                                       requestBody.getConnectorInterfaceLanguage(),
                                                                       requestBody.getConnectorInterfaces(),
                                                                       requestBody.getTargetTechnologySource(),
                                                                       requestBody.getTargetTechnologyName(),
                                                                       requestBody.getTargetTechnologyInterfaces(),
                                                                       requestBody.getTargetTechnologyVersions(),
                                                                       requestBody.getRecognizedAdditionalProperties(),
                                                                       requestBody.getRecognizedSecuredProperties(),
                                                                       requestBody.getRecognizedConfigurationProperties(),
                                                                       requestBody.getAdditionalProperties(),
                                                                       requestBody.getTypeName(),
                                                                       requestBody.getExtendedProperties(),
                                                                       methodName);

                if (connectorTypeGUID != null)
                {
                    handler.setVendorProperties(userId, connectorTypeGUID, requestBody.getVendorProperties(), methodName);
                }

                response.setGUID(connectorTypeGUID);
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
     * Create a new metadata element to represent a connector type using an existing metadata element as a template.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param templateGUID unique identifier of the metadata element to copy
     * @param requestBody properties that override the template
     *
     * @return unique identifier of the new endpoint or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GUIDResponse createConnectorTypeFromTemplate(String              serverName,
                                                        String              userId,
                                                        String              templateGUID,
                                                        TemplateRequestBody requestBody)
    {
        final String methodName = "createConnectorTypeFromTemplate";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ConnectorTypeHandler<ConnectorTypeElement> handler = instanceHandler.getConnectorTypeHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                String connectorTypeGUID = handler.createConnectorTypeFromTemplate(userId,
                                                                                   requestBody.getExternalSourceGUID(),
                                                                                   requestBody.getExternalSourceName(),
                                                                                   templateGUID,
                                                                                   requestBody.getQualifiedName(),
                                                                                   requestBody.getDisplayName(),
                                                                                   requestBody.getDescription(),
                                                                                   methodName);

                response.setGUID(connectorTypeGUID);
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
     * Update the metadata element representing a connector type.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param connectorTypeGUID unique identifier of the metadata element to update
     * @param isMergeUpdate are unspecified properties unchanged (true) or removed?
     * @param requestBody new properties for the metadata element
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse updateConnectorType(String              serverName,
                                            String              userId,
                                            String              connectorTypeGUID,
                                            boolean             isMergeUpdate,
                                            ConnectorTypeRequestBody requestBody)
    {
        final String methodName = "updateConnectorType";
        final String connectorTypeGUIDParameterName = "connectorTypeGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ConnectorTypeHandler<ConnectorTypeElement> handler = instanceHandler.getConnectorTypeHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.updateConnectorType(userId,
                                            null,
                                            null,
                                            connectorTypeGUID,
                                            connectorTypeGUIDParameterName,
                                            requestBody.getQualifiedName(),
                                            requestBody.getDisplayName(),
                                            requestBody.getDescription(),
                                            requestBody.getSupportedAssetTypeName(),
                                            requestBody.getExpectedDataFormat(),
                                            requestBody.getConnectorProviderClassName(),
                                            requestBody.getConnectorFrameworkName(),
                                            requestBody.getConnectorInterfaceLanguage(),
                                            requestBody.getConnectorInterfaces(),
                                            requestBody.getTargetTechnologySource(),
                                            requestBody.getTargetTechnologyName(),
                                            requestBody.getTargetTechnologyInterfaces(),
                                            requestBody.getTargetTechnologyVersions(),
                                            requestBody.getRecognizedAdditionalProperties(),
                                            requestBody.getRecognizedSecuredProperties(),
                                            requestBody.getRecognizedConfigurationProperties(),
                                            requestBody.getAdditionalProperties(),
                                            requestBody.getTypeName(),
                                            requestBody.getExtendedProperties(),
                                            isMergeUpdate,
                                            methodName);

                if ((!isMergeUpdate) || (requestBody.getVendorProperties() != null))
                {
                    handler.setVendorProperties(userId,
                                                connectorTypeGUID,
                                                requestBody.getVendorProperties(),
                                                methodName);
                }
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
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param requestBody external source identifiers
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse removeConnectorType(String                    serverName,
                                            String                    userId,
                                            String                    connectorTypeGUID,
                                            MetadataSourceRequestBody requestBody)
    {
        final String methodName = "removeConnectorType";
        final String connectorTypeGUIDParameterName = "connectorTypeGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ConnectorTypeHandler<ConnectorTypeElement> handler = instanceHandler.getConnectorTypeHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.removeConnectorType(userId,
                                            requestBody.getExternalSourceGUID(),
                                            requestBody.getExternalSourceName(),
                                            connectorTypeGUID,
                                            connectorTypeGUIDParameterName,
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
     * Retrieve the list of connector type metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param requestBody string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public ConnectorTypesResponse findConnectorTypes(String                  serverName,
                                                     String                  userId,
                                                     SearchStringRequestBody requestBody,
                                                     int                     startFrom,
                                                     int                     pageSize)
    {
        final String methodName = "findConnectorTypes";
        final String searchStringParameterName = "searchString";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ConnectorTypesResponse response = new ConnectorTypesResponse();
        AuditLog               auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                ConnectorTypeHandler<ConnectorTypeElement> handler = instanceHandler.getConnectorTypeHandler(userId, serverName, methodName);

                List<ConnectorTypeElement> elements = handler.findConnectorTypes(userId,
                                                                                 requestBody.getSearchString(),
                                                                                 searchStringParameterName,
                                                                                 startFrom,
                                                                                 pageSize,
                                                                                 methodName);

                response.setElementList(setUpVendorProperties(userId, elements, handler, methodName));
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
     * Retrieve the list of connector type metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param requestBody name to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public ConnectorTypesResponse getConnectorTypesByName(String          serverName,
                                                          String          userId,
                                                          NameRequestBody requestBody,
                                                          int             startFrom,
                                                          int             pageSize)
    {
        final String methodName = "getConnectorTypesByName";
        final String nameParameterName = "name";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ConnectorTypesResponse response = new ConnectorTypesResponse();
        AuditLog               auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                ConnectorTypeHandler<ConnectorTypeElement> handler = instanceHandler.getConnectorTypeHandler(userId, serverName, methodName);

                List<ConnectorTypeElement> elements = handler.getConnectorTypesByName(userId,
                                                                                      requestBody.getName(),
                                                                                      nameParameterName,
                                                                                      startFrom,
                                                                                      pageSize,
                                                                                      methodName);

                response.setElementList(setUpVendorProperties(userId, elements, handler, methodName));
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
     * Retrieve the connector type metadata element with the supplied unique identifier.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param guid unique identifier of the requested metadata element
     *
     * @return requested metadata element or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public ConnectorTypeResponse getConnectorTypeByGUID(String serverName,
                                                        String userId,
                                                        String guid)
    {
        final String methodName = "getConnectorTypeByGUID";
        final String connectorTypeGUIDParameterName = "connectorTypeGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ConnectorTypeResponse response = new ConnectorTypeResponse();
        AuditLog              auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ConnectorTypeHandler<ConnectorTypeElement> handler = instanceHandler.getConnectorTypeHandler(userId, serverName, methodName);

            ConnectorTypeElement element = handler.getConnectorTypeByGUID(userId, guid, connectorTypeGUIDParameterName, methodName);

            response.setElement(setUpVendorProperties(userId, element, handler, methodName));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /* ============================================================================
     * An endpoint defines where an infrastructure element connects to the network.
     */

    /**
     * Create a new metadata element to represent a endpoint.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param infrastructureGUID unique identifier of the infrastructure to connect it to (optional)
     * @param requestBody properties about the endpoint
     *
     * @return unique identifier of the new endpoint or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GUIDResponse createEndpoint(String              serverName,
                                       String              userId,
                                       String              infrastructureGUID,
                                       EndpointRequestBody requestBody)
    {
        final String methodName = "createEndpoint";
        final String infrastructureGUIDParameterName = "infrastructureGUID";
        final String endpointGUIDParameterName = "endpointGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            EndpointHandler<EndpointElement> handler = instanceHandler.getEndpointHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                String endpointGUID = handler.createEndpoint(userId,
                                                             requestBody.getExternalSourceGUID(),
                                                             requestBody.getExternalSourceName(),
                                                             infrastructureGUID,
                                                             requestBody.getQualifiedName(),
                                                             requestBody.getDisplayName(),
                                                             requestBody.getDescription(),
                                                             requestBody.getAddress(),
                                                             requestBody.getProtocol(),
                                                             requestBody.getEncryptionMethod(),
                                                             requestBody.getAdditionalProperties(),
                                                             requestBody.getTypeName(),
                                                             requestBody.getExtendedProperties(),
                                                             methodName);

                if (endpointGUID != null)
                {
                    handler.setVendorProperties(userId, endpointGUID, requestBody.getVendorProperties(), methodName);


                }

                if (infrastructureGUID != null)
                {
                    handler.linkElementToElement(userId,
                                                 requestBody.getExternalSourceGUID(),
                                                 requestBody.getExternalSourceName(),
                                                 infrastructureGUID,
                                                 infrastructureGUIDParameterName,
                                                 OpenMetadataAPIMapper.IT_INFRASTRUCTURE_TYPE_NAME,
                                                 endpointGUID,
                                                 endpointGUIDParameterName,
                                                 OpenMetadataAPIMapper.ENDPOINT_TYPE_NAME,
                                                 false,
                                                 false,
                                                 OpenMetadataAPIMapper.SERVER_ENDPOINT_TYPE_GUID,
                                                 OpenMetadataAPIMapper.SERVER_ENDPOINT_TYPE_NAME,
                                                 null,
                                                 methodName);
                }

                response.setGUID(endpointGUID);
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
     * Create a new metadata element to represent a endpoint using an existing metadata element as a template.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param infrastructureGUID unique identifier of the infrastructure to connect it to (optional)
     * @param networkAddress location of the endpoint
     * @param templateGUID unique identifier of the metadata element to copy
     * @param requestBody properties that override the template
     *
     * @return unique identifier of the new endpoint or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GUIDResponse createEndpointFromTemplate(String              serverName,
                                                   String              userId,
                                                   String              infrastructureGUID,
                                                   String              networkAddress,
                                                   String              templateGUID,
                                                   TemplateRequestBody requestBody)
    {
        final String methodName = "createEndpointFromTemplate";
        final String infrastructureGUIDParameterName = "infrastructureGUID";
        final String endpointGUIDParameterName = "endpointGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            EndpointHandler<EndpointElement> handler = instanceHandler.getEndpointHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                String endpointGUID = handler.createEndpointFromTemplate(userId,
                                                                         requestBody.getExternalSourceGUID(),
                                                                         requestBody.getExternalSourceName(),
                                                                         templateGUID,
                                                                         requestBody.getQualifiedName(),
                                                                         requestBody.getDisplayName(),
                                                                         requestBody.getDescription(),
                                                                         networkAddress,
                                                                         methodName);

                if (infrastructureGUID != null)
                {
                    handler.linkElementToElement(userId,
                                                 requestBody.getExternalSourceGUID(),
                                                 requestBody.getExternalSourceName(),
                                                 infrastructureGUID,
                                                 infrastructureGUIDParameterName,
                                                 OpenMetadataAPIMapper.IT_INFRASTRUCTURE_TYPE_NAME,
                                                 endpointGUID,
                                                 endpointGUIDParameterName,
                                                 OpenMetadataAPIMapper.ENDPOINT_TYPE_NAME,
                                                 false,
                                                 false,
                                                 OpenMetadataAPIMapper.SERVER_ENDPOINT_TYPE_GUID,
                                                 OpenMetadataAPIMapper.SERVER_ENDPOINT_TYPE_NAME,
                                                 null,
                                                 methodName);
                }

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
     * Update the metadata element representing a endpoint.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param endpointGUID unique identifier of the metadata element to update
     * @param isMergeUpdate are unspecified properties unchanged (true) or removed?
     * @param requestBody new properties for the metadata element
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse updateEndpoint(String              serverName,
                                       String              userId,
                                       String              endpointGUID,
                                       boolean             isMergeUpdate,
                                       EndpointRequestBody requestBody)
    {
        final String methodName = "updateEndpoint";
        final String endpointGUIDParameterName = "endpointGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            EndpointHandler<EndpointElement> handler = instanceHandler.getEndpointHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.updateEndpoint(userId,
                                       requestBody.getExternalSourceGUID(),
                                       requestBody.getExternalSourceName(),
                                       endpointGUID,
                                       endpointGUIDParameterName,
                                       requestBody.getQualifiedName(),
                                       requestBody.getDisplayName(),
                                       requestBody.getDescription(),
                                       requestBody.getAddress(),
                                       requestBody.getProtocol(),
                                       requestBody.getEncryptionMethod(),
                                       requestBody.getAdditionalProperties(),
                                       requestBody.getTypeName(),
                                       requestBody.getExtendedProperties(),
                                       isMergeUpdate,
                                       null,
                                       null,
                                       methodName);

                if ((!isMergeUpdate) || (requestBody.getVendorProperties() != null))
                {
                    handler.setVendorProperties(userId,
                                                endpointGUID,
                                                requestBody.getVendorProperties(),
                                                methodName);
                }
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
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param endpointGUID unique identifier of the metadata element to remove
     * @param requestBody external source identifiers
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse removeEndpoint(String                    serverName,
                                       String                    userId,
                                       String                    endpointGUID,
                                       MetadataSourceRequestBody requestBody)
    {
        final String methodName = "removeEndpoint";
        final String endpointGUIDParameterName = "endpointGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            EndpointHandler<EndpointElement> handler = instanceHandler.getEndpointHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.removeEndpoint(userId,
                                       requestBody.getExternalSourceGUID(),
                                       requestBody.getExternalSourceName(),
                                       endpointGUID,
                                       endpointGUIDParameterName,
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
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param guid unique identifier of the requested metadata element
     *
     * @return requested metadata element or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public EndpointResponse getEndpointByGUID(String serverName,
                                              String userId,
                                              String guid)
    {
        final String methodName = "getEndpointByGUID";
        final String endpointGUIDParameterName = "endpointGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        EndpointResponse response = new EndpointResponse();
        AuditLog         auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            EndpointHandler<EndpointElement> handler = instanceHandler.getEndpointHandler(userId, serverName, methodName);

            EndpointElement element = handler.getEndpointByGUID(userId, guid, endpointGUIDParameterName, methodName);

            response.setElement(setUpVendorProperties(userId, element, handler, methodName));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Set up the vendor properties in the retrieved elements.
     *
     * @param userId calling user
     * @param retrievedResults results from the repositories
     * @param handler handler used to retrieve the vendor properties
     * @param methodName calling method
     *
     * @return updated results
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    private List<ConnectionElement> setUpVendorProperties(String                               userId,
                                                          List<ConnectionElement>              retrievedResults,
                                                          ConnectionHandler<ConnectionElement> handler,
                                                          String                               methodName) throws InvalidParameterException,
                                                                                                                  UserNotAuthorizedException,
                                                                                                                  PropertyServerException
    {
        if (retrievedResults != null)
        {
            for (ConnectionElement element : retrievedResults)
            {
                if (element != null)
                {
                    setUpVendorProperties(userId, element, handler, methodName);
                }
            }
        }

        return retrievedResults;
    }


    /**
     * Set up the vendor properties in the retrieved element.
     *
     * @param userId calling user
     * @param element results from the repositories
     * @param handler handler used to retrieve the vendor properties
     * @param methodName calling method
     *
     * @return updated results
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    private ConnectionElement setUpVendorProperties(String                               userId,
                                                    ConnectionElement                    element,
                                                    ConnectionHandler<ConnectionElement> handler,
                                                    String                               methodName) throws InvalidParameterException,
                                                                                                            UserNotAuthorizedException,
                                                                                                            PropertyServerException
    {
        final String elementGUIDParameterName = "element.getElementHeader().getGUID()";

        if (element != null)
        {
            ConnectionProperties properties = element.getConnectionProperties();

            properties.setVendorProperties(handler.getVendorProperties(userId,
                                                                       element.getElementHeader().getGUID(),
                                                                       elementGUIDParameterName,
                                                                       methodName));
        }

        return element;
    }




    /**
     * Set up the vendor properties in the retrieved elements.
     *
     * @param userId calling user
     * @param retrievedResults results from the repositories
     * @param handler handler used to retrieve the vendor properties
     * @param methodName calling method
     *
     * @return updated results
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    private List<EndpointElement> setUpVendorProperties(String                           userId,
                                                        List<EndpointElement>            retrievedResults,
                                                        EndpointHandler<EndpointElement> handler,
                                                        String                           methodName) throws InvalidParameterException,
                                                                                                            UserNotAuthorizedException,
                                                                                                            PropertyServerException
    {
        if (retrievedResults != null)
        {
            for (EndpointElement element : retrievedResults)
            {
                if (element != null)
                {
                    setUpVendorProperties(userId, element, handler, methodName);
                }
            }
        }

        return retrievedResults;
    }


    /**
     * Set up the vendor properties in the retrieved element.
     *
     * @param userId calling user
     * @param element results from the repositories
     * @param handler handler used to retrieve the vendor properties
     * @param methodName calling method
     *
     * @return updated results
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    private EndpointElement setUpVendorProperties(String                           userId,
                                                  EndpointElement                  element,
                                                  EndpointHandler<EndpointElement> handler,
                                                  String                           methodName) throws InvalidParameterException,
                                                                                                      UserNotAuthorizedException,
                                                                                                      PropertyServerException
    {
        final String elementGUIDParameterName = "element.getElementHeader().getGUID()";

        if (element != null)
        {
            EndpointProperties properties = element.getEndpointProperties();

            properties.setVendorProperties(handler.getVendorProperties(userId,
                                                                       element.getElementHeader().getGUID(),
                                                                       elementGUIDParameterName,
                                                                       methodName));
        }

        return element;
    }


    /**
     * Set up the vendor properties in the retrieved elements.
     *
     * @param userId calling user
     * @param retrievedResults results from the repositories
     * @param handler handler used to retrieve the vendor properties
     * @param methodName calling method
     *
     * @return updated results
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    private List<ConnectorTypeElement> setUpVendorProperties(String                                     userId,
                                                             List<ConnectorTypeElement>                 retrievedResults,
                                                             ConnectorTypeHandler<ConnectorTypeElement> handler,
                                                             String                                     methodName) throws InvalidParameterException,
                                                                                                                           UserNotAuthorizedException,
                                                                                                                           PropertyServerException
    {
        if (retrievedResults != null)
        {
            for (ConnectorTypeElement element : retrievedResults)
            {
                if (element != null)
                {
                    setUpVendorProperties(userId, element, handler, methodName);
                }
            }
        }

        return retrievedResults;
    }


    /**
     * Set up the vendor properties in the retrieved element.
     *
     * @param userId calling user
     * @param element results from the repositories
     * @param handler handler used to retrieve the vendor properties
     * @param methodName calling method
     *
     * @return updated results
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    private ConnectorTypeElement setUpVendorProperties(String                                     userId,
                                                       ConnectorTypeElement                       element,
                                                       ConnectorTypeHandler<ConnectorTypeElement> handler,
                                                       String                                     methodName) throws InvalidParameterException,
                                                                                                                     UserNotAuthorizedException,
                                                                                                                     PropertyServerException
    {
        final String elementGUIDParameterName = "element.getElementHeader().getGUID()";

        if (element != null)
        {
            ConnectorTypeProperties properties = element.getConnectorTypeProperties();

            properties.setVendorProperties(handler.getVendorProperties(userId,
                                                                       element.getElementHeader().getGUID(),
                                                                       elementGUIDParameterName,
                                                                       methodName));
        }

        return element;
    }
}