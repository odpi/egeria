/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.itinfrastructure.server;


import org.odpi.openmetadata.accessservices.itinfrastructure.rest.TemplateRequestBody;

import org.odpi.openmetadata.commonservices.generichandlers.ElementStubConverter;
import org.odpi.openmetadata.commonservices.ffdc.rest.ConnectionResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.ConnectionsResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.ConnectorTypeResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.ConnectorTypesResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.EmbeddedConnectionRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.EndpointResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.EndpointsResponse;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.*;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.connections.ConnectionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.connections.ConnectorTypeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.connections.EndpointProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.infrastructure.ServerAssetUseProperties;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ServerAssetUseType;
import org.odpi.openmetadata.frameworks.openmetadata.properties.softwarecapabilities.SoftwareCapabilityProperties;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.generichandlers.AssetHandler;
import org.odpi.openmetadata.commonservices.generichandlers.ConnectionHandler;
import org.odpi.openmetadata.commonservices.generichandlers.ConnectorTypeHandler;
import org.odpi.openmetadata.commonservices.generichandlers.EndpointHandler;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.commonservices.generichandlers.SoftwareCapabilityHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementStub;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException;
import org.slf4j.LoggerFactory;


import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;


/**
 * The ITInfrastructureRESTServices provides the server-side implementation of the IT Infrastructure Open Metadata
 * Assess Service (OMAS).  This interface provides support for defining infrastructure assets and their supporting
 * elements.
 */
public class ITInfrastructureRESTServices
{
    private static final ITInfrastructureInstanceHandler instanceHandler = new ITInfrastructureInstanceHandler();

    private static final RESTCallLogger restCallLogger = new RESTCallLogger(LoggerFactory.getLogger(ITInfrastructureRESTServices.class),
                                                                            instanceHandler.getServiceName());

    private final RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();


    /**
     * Default constructor
     */
    public ITInfrastructureRESTServices()
    {
    }


    /**
     * Return the client side connection object for the IT Infrastructure OMAS's out topic.
     *
     * @param serverName name of the service to route the request to.
     * @param userId identifier of calling user.
     * @param callerId unique identifier of the caller
     *
     * @return connection object for the out topic or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem retrieving the discovery engine definition.
     */
    public OCFConnectionResponse getOutTopicConnection(String serverName,
                                                       String userId,
                                                       String callerId)
    {
        final String methodName = "getOutTopicConnection";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        OCFConnectionResponse
                response = new OCFConnectionResponse();
        AuditLog auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            response.setConnection(instanceHandler.getOutTopicConnection(userId, serverName, methodName, callerId));
        }
        catch (InvalidParameterException error)
        {
            restExceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException error)
        {
            restExceptionHandler.capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
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
                                                                 false,
                                                                 false,
                                                                 handler.getSupportedZones(),
                                                                 new Date(),
                                                                 methodName);

                if (connectionGUID != null)
                {
                    handler.setVendorProperties(userId,
                                                connectionGUID,
                                                requestBody.getVendorProperties(),
                                                false,
                                                false,
                                                new Date(),
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
                                         false,
                                         false,
                                         new Date(),
                                         methodName);

                if ((!isMergeUpdate) || (requestBody.getVendorProperties() != null))
                {
                    handler.setVendorProperties(userId,
                                                connectionGUID,
                                                requestBody.getVendorProperties(),
                                                false,
                                                false,
                                                new Date(),
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
                                           ExternalSourceRequestBody requestBody)
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
    public VoidResponse clearConnectorType(String                    serverName,
                                           String                    userId,
                                           String                    connectionGUID,
                                           String                    connectorTypeGUID,
                                           ExternalSourceRequestBody requestBody)
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
    public VoidResponse setupEndpoint(String                    serverName,
                                      String                    userId,
                                      String                    connectionGUID,
                                      String                    endpointGUID,
                                      ExternalSourceRequestBody requestBody)
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
    public VoidResponse clearEndpoint(String                    serverName,
                                      String                    userId,
                                      String                    connectionGUID,
                                      String                    endpointGUID,
                                      ExternalSourceRequestBody requestBody)
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
                                              requestBody.getExternalSourceGUID(),
                                              requestBody.getExternalSourceName(),
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
                                              handler.getSupportedZones(),
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
    public VoidResponse clearEmbeddedConnection(String                    serverName,
                                                String                    userId,
                                                String                    connectionGUID,
                                                String                    embeddedConnectionGUID,
                                                ExternalSourceRequestBody requestBody)
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
                                                 false,
                                                 false,
                                                 handler.getSupportedZones(),
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
    public VoidResponse clearAssetConnection(String                    serverName,
                                             String                    userId,
                                             String                    assetGUID,
                                             String                    connectionGUID,
                                             ExternalSourceRequestBody requestBody)
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
                                         ExternalSourceRequestBody requestBody)
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
                                                                              false,
                                                                              false,
                                                                              new Date(),
                                                                              methodName);

                response.setElements(setUpVendorProperties(userId, connections, handler, methodName));
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
        AuditLog            auditLog = null;

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
                                                                                   false,
                                                                                   false,
                                                                                   new Date(),
                                                                                   methodName);

                response.setElements(setUpVendorProperties(userId, connections, handler, methodName));
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

            ConnectionElement connection = handler.getConnectionByGUID(userId,
                                                                       guid,
                                                                       guidParameterName,
                                                                       false,
                                                                       false,
                                                                       new Date(),
                                                                       methodName);

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
                                                                       null,
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
                                                                       requestBody.getEffectiveFrom(),
                                                                       requestBody.getEffectiveTo(),
                                                                       new Date(),
                                                                       methodName);

                if (connectorTypeGUID != null)
                {
                    handler.setVendorProperties(userId,
                                                connectorTypeGUID,
                                                requestBody.getVendorProperties(),
                                                false,
                                                false,
                                                new Date(),
                                                methodName);
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
                                            null,
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
                                            requestBody.getEffectiveFrom(),
                                            requestBody.getEffectiveTo(),
                                            isMergeUpdate,
                                            false,
                                            false,
                                            new Date(),
                                            methodName);

                if ((!isMergeUpdate) || (requestBody.getVendorProperties() != null))
                {
                    handler.setVendorProperties(userId,
                                                connectorTypeGUID,
                                                requestBody.getVendorProperties(),
                                                false,
                                                false,
                                                new Date(),
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
     * Remove the metadata element representing a connector type.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param connectorTypeGUID unique identifier of the element to remove
     *
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
                                            ExternalSourceRequestBody requestBody)
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
                                                                                 false,
                                                                                 false,
                                                                                 new Date(),
                                                                                 methodName);

                response.setElements(setUpVendorProperties(userId, elements, handler, methodName));
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
                                                                                      false,
                                                                                      false,
                                                                                      new Date(),
                                                                                      methodName);

                response.setElements(setUpVendorProperties(userId, elements, handler, methodName));
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

            ConnectorTypeElement element = handler.getConnectorTypeByGUID(userId,
                                                                          guid,
                                                                          connectorTypeGUIDParameterName,
                                                                          false,
                                                                          false,
                                                                          new Date(),
                                                                          methodName);

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
                                                             requestBody.getName(),
                                                             requestBody.getResourceDescription(),
                                                             requestBody.getAddress(),
                                                             requestBody.getProtocol(),
                                                             requestBody.getEncryptionMethod(),
                                                             requestBody.getAdditionalProperties(),
                                                             requestBody.getTypeName(),
                                                             requestBody.getExtendedProperties(),
                                                             null,
                                                             null,
                                                             new Date(),
                                                             methodName);

                if (endpointGUID != null)
                {
                    handler.setVendorProperties(userId,
                                                endpointGUID,
                                                requestBody.getVendorProperties(),
                                                false,
                                                false,
                                                new Date(), methodName);
                }

                if (infrastructureGUID != null)
                {
                    handler.linkElementToElement(userId,
                                                 requestBody.getExternalSourceGUID(),
                                                 requestBody.getExternalSourceName(),
                                                 infrastructureGUID,
                                                 infrastructureGUIDParameterName,
                                                 OpenMetadataType.IT_INFRASTRUCTURE.typeName,
                                                 endpointGUID,
                                                 endpointGUIDParameterName,
                                                 OpenMetadataType.ENDPOINT.typeName,
                                                 false,
                                                 false,
                                                 OpenMetadataType.SERVER_ENDPOINT_RELATIONSHIP.typeGUID,
                                                 OpenMetadataType.SERVER_ENDPOINT_RELATIONSHIP.typeName,
                                                 (InstanceProperties)null,
                                                 null,
                                                 null,
                                                 new Date(),
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
                                                 OpenMetadataType.IT_INFRASTRUCTURE.typeName,
                                                 endpointGUID,
                                                 endpointGUIDParameterName,
                                                 OpenMetadataType.ENDPOINT.typeName,
                                                 false,
                                                 false,
                                                 OpenMetadataType.SERVER_ENDPOINT_RELATIONSHIP.typeGUID,
                                                 OpenMetadataType.SERVER_ENDPOINT_RELATIONSHIP.typeName,
                                                 (InstanceProperties) null,
                                                 null,
                                                 null,
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
                                       requestBody.getName(),
                                       requestBody.getResourceDescription(),
                                       requestBody.getAddress(),
                                       requestBody.getProtocol(),
                                       requestBody.getEncryptionMethod(),
                                       requestBody.getAdditionalProperties(),
                                       requestBody.getTypeName(),
                                       requestBody.getExtendedProperties(),
                                       isMergeUpdate,
                                       null,
                                       null,
                                       false,
                                       false,
                                       new Date(),
                                       methodName);

                if ((!isMergeUpdate) || (requestBody.getVendorProperties() != null))
                {
                    handler.setVendorProperties(userId,
                                                endpointGUID,
                                                requestBody.getVendorProperties(),
                                                false,
                                                false,
                                                new Date(),
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
                                       ExternalSourceRequestBody requestBody)
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

                response.setElements(setUpVendorProperties(userId, endpoints, handler, methodName));
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
                response.setElements(setUpVendorProperties(userId, endpoints, handler, methodName));
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
     * Retrieve the list of endpoint metadata elements with a matching network address.
     *
     * @param serverName name of calling server
     * @param userId calling user
     * @param requestBody url to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements or
     * InvalidParameterException one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to make this request or
     * PropertyServerException the repository is not available or not working properly.
     */
    public EndpointsResponse getEndpointsByNetworkAddress(String          serverName,
                                                          String          userId,
                                                          NameRequestBody requestBody,
                                                          int             startFrom,
                                                          int             pageSize)
    {
        final String methodName = "getEndpointsByNetworkAddress";
        final String parameterName = "networkAddress";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        EndpointsResponse response = new EndpointsResponse();
        AuditLog          auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                EndpointHandler<EndpointElement> handler = instanceHandler.getEndpointHandler(userId, serverName, methodName);

                List<EndpointElement> endpoints = handler.getEndpointsByNetworkAddress(userId,
                                                                                       requestBody.getName(),
                                                                                       parameterName,
                                                                                       startFrom,
                                                                                       pageSize,
                                                                                       false,
                                                                                       false,
                                                                                       new Date(),
                                                                                       methodName);

                response.setElements(setUpVendorProperties(userId, endpoints, handler, methodName));
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
     * Retrieve the list of endpoint metadata elements that are attached to a specific infrastructure element.
     *
     * @param serverName name of calling server
     * @param userId calling user
     * @param infrastructureGUID element to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public EndpointsResponse getEndpointsForInfrastructure(String serverName,
                                                           String userId,
                                                           String infrastructureGUID,
                                                           int    startFrom,
                                                           int    pageSize)
    {
        final String methodName        = "getEndpointsForInfrastructure";
        final String guidParameterName = "infrastructureGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        EndpointsResponse response = new EndpointsResponse();
        AuditLog          auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            EndpointHandler<EndpointElement> handler = instanceHandler.getEndpointHandler(userId, serverName, methodName);

            List<EndpointElement> endpoints = handler.getAttachedElements(userId,
                                                                          infrastructureGUID,
                                                                          guidParameterName,
                                                                          OpenMetadataType.IT_INFRASTRUCTURE.typeName,
                                                                          OpenMetadataType.SERVER_ENDPOINT_RELATIONSHIP.typeGUID,
                                                                          OpenMetadataType.SERVER_ENDPOINT_RELATIONSHIP.typeName,
                                                                          OpenMetadataType.ENDPOINT.typeName,
                                                                          null,
                                                                          null,
                                                                          0,
                                                                          false,
                                                                          false,
                                                                          startFrom,
                                                                          pageSize,
                                                                          new Date(),
                                                                          methodName);

            response.setElements(setUpVendorProperties(userId, endpoints, handler, methodName));
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

            EndpointElement element = handler.getEndpointByGUID(userId, guid, endpointGUIDParameterName, false, false, new Date(), methodName);

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
                                                                       false,
                                                                       false,
                                                                       new Date(),
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
                                                                       false,
                                                                       false,
                                                                       new Date(),
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
                                                                       false,
                                                                       false,
                                                                       new Date(),
                                                                       methodName));
        }

        return element;
    }



    /* =====================================================================================================================
     * The software capability links assets to the hosting server.
     */


    /**
     * Create a new metadata element to represent a software capability.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param infrastructureManagerIsHome should the software capability be marked as owned by the infrastructure manager so others can not update?
     * @param requestBody properties to store
     *
     * @return unique identifier of the new metadata element or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GUIDResponse createSoftwareCapability(String                              serverName,
                                                       String                              userId,
                                                       boolean                             infrastructureManagerIsHome,
                                                       SoftwareCapabilityRequestBody requestBody)
    {
        final String methodName = "createSoftwareCapability";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SoftwareCapabilityHandler<SoftwareCapabilityElement> handler = instanceHandler.getSoftwareCapabilityHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                String capabilityGUID;

                if (infrastructureManagerIsHome)
                {
                    capabilityGUID = handler.createSoftwareCapability(userId,
                                                                      requestBody.getExternalSourceGUID(),
                                                                      requestBody.getExternalSourceName(),
                                                                      requestBody.getTypeName(),
                                                                      requestBody.getClassificationName(),
                                                                      requestBody.getQualifiedName(),
                                                                      requestBody.getResourceName(),
                                                                      requestBody.getResourceDescription(),
                                                                      requestBody.getDeployedImplementationType(),
                                                                      requestBody.getVersion(),
                                                                      requestBody.getPatchLevel(),
                                                                      requestBody.getSource(),
                                                                      requestBody.getAdditionalProperties(),
                                                                      requestBody.getExtendedProperties(),
                                                                      requestBody.getVendorProperties(),
                                                                      requestBody.getEffectiveFrom(),
                                                                      requestBody.getEffectiveTo(),
                                                                      false,
                                                                      false,
                                                                      new Date(),
                                                                      methodName);
                }
                else
                {
                    capabilityGUID = handler.createSoftwareCapability(userId,
                                                                      null,
                                                                      null,
                                                                      requestBody.getTypeName(),
                                                                      requestBody.getClassificationName(),
                                                                      requestBody.getQualifiedName(),
                                                                      requestBody.getResourceName(),
                                                                      requestBody.getResourceDescription(),
                                                                      requestBody.getDeployedImplementationType(),
                                                                      requestBody.getVersion(),
                                                                      requestBody.getPatchLevel(),
                                                                      requestBody.getSource(),
                                                                      requestBody.getAdditionalProperties(),
                                                                      requestBody.getExtendedProperties(),
                                                                      requestBody.getVendorProperties(),
                                                                      requestBody.getEffectiveFrom(),
                                                                      requestBody.getEffectiveTo(),
                                                                      false,
                                                                      false,
                                                                      new Date(),
                                                                      methodName);
                }

                response.setGUID(capabilityGUID);
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
     * Create a new metadata element to represent a software capability using an existing metadata element as a template.
     *
     * @param serverName name of the service to route the request to.
     *
     * @param userId calling user
     * @param infrastructureManagerIsHome should the software capability be marked as owned by the infrastructure manager so others can not update?
     * @param templateGUID unique identifier of the metadata element to copy
     * @param requestBody properties that override the template
     *
     * @return unique identifier of the new metadata element or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GUIDResponse createSoftwareCapabilityFromTemplate(String              serverName,
                                                                   String              userId,
                                                                   String              templateGUID,
                                                                   boolean             infrastructureManagerIsHome,
                                                                   TemplateRequestBody requestBody)
    {
        final String methodName                 = "createSoftwareCapabilityFromTemplate";
        final String templateGUIDParameterName  = "templateGUID";
        final String qualifiedNameParameterName = "qualifiedName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SoftwareCapabilityHandler<SoftwareCapabilityElement> handler = instanceHandler.getSoftwareCapabilityHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                String capabilityGUID;

                if (infrastructureManagerIsHome)
                {
                    capabilityGUID = handler.createSoftwareCapabilityFromTemplate(userId,
                                                                                        requestBody.getExternalSourceGUID(),
                                                                                        requestBody.getExternalSourceName(),
                                                                                        templateGUID,
                                                                                        templateGUIDParameterName,
                                                                                        requestBody.getQualifiedName(),
                                                                                        qualifiedNameParameterName,
                                                                                        requestBody.getDisplayName(),
                                                                                        requestBody.getDescription(),
                                                                                        methodName);
                }
                else
                {
                    capabilityGUID = handler.createSoftwareCapabilityFromTemplate(userId,
                                                                                        null,
                                                                                        null,
                                                                                        templateGUID,
                                                                                        templateGUIDParameterName,
                                                                                        requestBody.getQualifiedName(),
                                                                                        qualifiedNameParameterName,
                                                                                        requestBody.getDisplayName(),
                                                                                        requestBody.getDescription(),
                                                                                        methodName);
                }

                response.setGUID(capabilityGUID);
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
     * Update the metadata element representing a software capability.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param capabilityGUID unique identifier of the metadata element to update
     * @param isMergeUpdate are unspecified properties unchanged (true) or removed?
     * @param requestBody new properties for this element
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse updateSoftwareCapability(String                        serverName,
                                                 String                        userId,
                                                 String                        capabilityGUID,
                                                 boolean                       isMergeUpdate,
                                                 SoftwareCapabilityRequestBody requestBody)
    {
        final String methodName                  = "updateSoftwareCapability";
        final String elementGUIDParameterName    = "capabilityGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SoftwareCapabilityHandler<SoftwareCapabilityElement> handler = instanceHandler.getSoftwareCapabilityHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                Date effectiveDate = new Date();

                handler.updateSoftwareCapability(userId,
                                               requestBody.getExternalSourceGUID(),
                                               requestBody.getExternalSourceName(),
                                               capabilityGUID,
                                               elementGUIDParameterName,
                                               requestBody.getQualifiedName(),
                                               requestBody.getResourceName(),
                                               requestBody.getResourceDescription(),
                                               requestBody.getDeployedImplementationType(),
                                               requestBody.getVersion(),
                                               requestBody.getPatchLevel(),
                                               requestBody.getSource(),
                                               requestBody.getAdditionalProperties(),
                                               requestBody.getExtendedProperties(),
                                               requestBody.getVendorProperties(),
                                               isMergeUpdate,
                                               requestBody.getEffectiveFrom(),
                                               requestBody.getEffectiveTo(),
                                               false,
                                               false,
                                               effectiveDate,
                                               methodName);

                if ((!isMergeUpdate) || (requestBody.getVendorProperties() != null))
                {
                    handler.setVendorProperties(userId,
                                                capabilityGUID,
                                                requestBody.getVendorProperties(),
                                                false,
                                                false,
                                                effectiveDate,
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
     * Remove the metadata element representing a software capability.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param capabilityGUID unique identifier of the metadata element to remove
     * @param requestBody unique identifier of software capability representing the caller
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse removeSoftwareCapability(String                    serverName,
                                                       String                    userId,
                                                       String                    capabilityGUID,
                                                       ExternalSourceRequestBody requestBody)
    {
        final String methodName = "removeSoftwareCapability";
        final String elementGUIDParameterName    = "capabilityGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SoftwareCapabilityHandler<SoftwareCapabilityElement> handler = instanceHandler.getSoftwareCapabilityHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.deleteBeanInRepository(userId,
                                               requestBody.getExternalSourceGUID(),
                                               requestBody.getExternalSourceName(),
                                               capabilityGUID,
                                               elementGUIDParameterName,
                                               OpenMetadataType.SOFTWARE_CAPABILITY.typeGUID,
                                               OpenMetadataType.SOFTWARE_CAPABILITY.typeName,
                                               null,
                                               null,
                                               false,
                                               false,
                                               null,
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
     * Retrieve the list of software capability metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public SoftwareCapabilitiesResponse findSoftwareCapabilities(String                  serverName,
                                                                 String                  userId,
                                                                 int                     startFrom,
                                                                 int                     pageSize,
                                                                 SearchStringRequestBody requestBody)
    {
        final String methodName                = "findSoftwareCapabilities";
        final String searchStringParameterName = "searchString";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        SoftwareCapabilitiesResponse response = new SoftwareCapabilitiesResponse();
        AuditLog                     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                SoftwareCapabilityHandler<SoftwareCapabilityElement> handler = instanceHandler.getSoftwareCapabilityHandler(userId, serverName, methodName);

                List<SoftwareCapabilityElement> capabilities = handler.findBeans(userId,
                                                                                 requestBody.getSearchString(),
                                                                                 searchStringParameterName,
                                                                                 OpenMetadataType.SOFTWARE_CAPABILITY.typeGUID,
                                                                                 OpenMetadataType.SOFTWARE_CAPABILITY.typeName,
                                                                                 null,
                                                                                 startFrom,
                                                                                 pageSize,
                                                                                 false,
                                                                                 false,
                                                                                 new Date(),
                                                                                 methodName);

                response.setElements(setUpVendorProperties(userId, capabilities, handler, methodName));
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
     * Retrieve the list of software capability metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody values to search for
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public SoftwareCapabilitiesResponse getSoftwareCapabilitiesByName(String          serverName,
                                                                      String          userId,
                                                                      int             startFrom,
                                                                      int             pageSize,
                                                                      NameRequestBody requestBody)
    {
        final String methodName        = "getSoftwareCapabilitiesByName";
        final String nameParameterName = "name";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        SoftwareCapabilitiesResponse response = new SoftwareCapabilitiesResponse();
        AuditLog                     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                SoftwareCapabilityHandler<SoftwareCapabilityElement> handler = instanceHandler.getSoftwareCapabilityHandler(userId, serverName, methodName);

                List<String> specificMatchPropertyNames = new ArrayList<>();
                specificMatchPropertyNames.add(OpenMetadataProperty.QUALIFIED_NAME.name);
                specificMatchPropertyNames.add(OpenMetadataProperty.NAME.name);
                specificMatchPropertyNames.add(OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.name);
                specificMatchPropertyNames.add(OpenMetadataProperty.CAPABILITY_TYPE.name);

                List<SoftwareCapabilityElement> capabilities = handler.getBeansByValue(userId,
                                                                                       requestBody.getName(),
                                                                                       nameParameterName,
                                                                                       OpenMetadataType.SOFTWARE_CAPABILITY.typeGUID,
                                                                                       OpenMetadataType.SOFTWARE_CAPABILITY.typeName,
                                                                                       specificMatchPropertyNames,
                                                                                       true,
                                                                                       null,
                                                                                       null,
                                                                                       false,
                                                                                       false,
                                                                                       null,
                                                                                       startFrom,
                                                                                       pageSize,
                                                                                       new Date(),
                                                                                       methodName);

                response.setElements(setUpVendorProperties(userId, capabilities, handler, methodName));
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
     * Retrieve the software capability metadata element with the supplied unique identifier.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param guid unique identifier of the requested metadata element
     *
     * @return matching metadata element or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public SoftwareCapabilityResponse getSoftwareCapabilityByGUID(String serverName,
                                                                        String userId,
                                                                        String guid)
    {
        final String methodName = "getSoftwareCapabilityByGUID";
        final String guidParameterName = "guid";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        SoftwareCapabilityResponse response = new SoftwareCapabilityResponse();
        AuditLog                   auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SoftwareCapabilityHandler<SoftwareCapabilityElement> handler = instanceHandler.getSoftwareCapabilityHandler(userId, serverName, methodName);

            SoftwareCapabilityElement capability = handler.getBeanFromRepository(userId,
                                                                                 guid,
                                                                                 guidParameterName,
                                                                                 OpenMetadataType.SOFTWARE_CAPABILITY.typeName,
                                                                                 false,
                                                                                 false,
                                                                                 new Date(),
                                                                                 methodName);

            response.setElement(setUpVendorProperties(userId, capability, handler, methodName));
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
    private List<SoftwareCapabilityElement> setUpVendorProperties(String                                               userId,
                                                                  List<SoftwareCapabilityElement>                      retrievedResults,
                                                                  SoftwareCapabilityHandler<SoftwareCapabilityElement> handler,
                                                                  String                                               methodName) throws InvalidParameterException,
                                                                                                                                          UserNotAuthorizedException,
                                                                                                                                          PropertyServerException
    {
        if (retrievedResults != null)
        {
            for (SoftwareCapabilityElement element : retrievedResults)
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
    private SoftwareCapabilityElement setUpVendorProperties(String                                               userId,
                                                            SoftwareCapabilityElement                            element,
                                                            SoftwareCapabilityHandler<SoftwareCapabilityElement> handler,
                                                            String                                               methodName) throws InvalidParameterException,
                                                                                                                                    UserNotAuthorizedException,
                                                                                                                                    PropertyServerException
    {
        final String elementGUIDParameterName = "element.getElementHeader().getGUID()";

        if (element != null)
        {
            SoftwareCapabilityProperties properties = element.getProperties();

            properties.setVendorProperties(handler.getVendorProperties(userId,
                                                                       element.getElementHeader().getGUID(),
                                                                       elementGUIDParameterName,
                                                                       false,
                                                                       false,
                                                                       new Date(),
                                                                       methodName));
        }

        return element;
    }


    /*
     * A software capability works with assets
     */

    /**
     * Create a new metadata relationship to represent the use of an asset by a software capability.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param infrastructureManagerIsHome should the software capability be marked as owned by the infrastructure manager so others can not update?
     * @param capabilityGUID unique identifier of a software capability
     * @param assetGUID unique identifier of an asset
     * @param requestBody properties about the ServerAssetUse relationship
     *
     * @return unique identifier of the new ServerAssetUse relationship or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GUIDResponse createServerAssetUse(String                    serverName,
                                             String                    userId,
                                             String                    capabilityGUID,
                                             String                    assetGUID,
                                             boolean                   infrastructureManagerIsHome,
                                             ServerAssetUseRequestBody requestBody)
    {
        final String methodName                  = "createServerAssetUse";
        final String capabilityGUIDParameterName = "capabilityGUID";
        final String assetGUIDParameterName      = "assetGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SoftwareCapabilityHandler<SoftwareCapabilityElement> handler = instanceHandler.getSoftwareCapabilityHandler(userId, serverName, methodName);

            String guid;

            if (infrastructureManagerIsHome)
            {
                guid = handler.linkElementToElement(userId,
                                                    requestBody.getExternalSourceGUID(),
                                                    requestBody.getExternalSourceName(),
                                                    capabilityGUID,
                                                    capabilityGUIDParameterName,
                                                    OpenMetadataType.SOFTWARE_CAPABILITY.typeName,
                                                    assetGUID,
                                                    assetGUIDParameterName,
                                                    OpenMetadataType.ASSET.typeName,
                                                    false,
                                                    false,
                                                    OpenMetadataType.SERVER_ASSET_USE_RELATIONSHIP.typeGUID,
                                                    OpenMetadataType.SERVER_ASSET_USE_RELATIONSHIP.typeName,
                                                    this.getServerAssetUseProperties(requestBody.getProperties(),
                                                                                     instanceHandler.getRepositoryHelper(userId,
                                                                                                                         serverName,
                                                                                                                         methodName),
                                                                                     instanceHandler.getServiceName(),
                                                                                     methodName),
                                                    null,
                                                    null,
                                                    new Date(),
                                                    methodName);
            }
            else
            {
                guid = handler.linkElementToElement(userId,
                                                    null,
                                                    null,
                                                    capabilityGUID,
                                                    capabilityGUIDParameterName,
                                                    OpenMetadataType.SOFTWARE_CAPABILITY.typeName,
                                                    assetGUID,
                                                    assetGUIDParameterName,
                                                    OpenMetadataType.ASSET.typeName,
                                                    false,
                                                    false,
                                                    OpenMetadataType.SERVER_ASSET_USE_RELATIONSHIP.typeGUID,
                                                    OpenMetadataType.SERVER_ASSET_USE_RELATIONSHIP.typeName,
                                                    this.getServerAssetUseProperties(requestBody.getProperties(),
                                                                                     instanceHandler.getRepositoryHelper(userId,
                                                                                                                         serverName,
                                                                                                                         methodName),
                                                                                     instanceHandler.getServiceName(),
                                                                                     methodName),
                                                    null,
                                                    null,
                                                    new Date(),
                                                    methodName);
            }

            response.setGUID(guid);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Set up the properties for the server asset use type relationship.
     *
     * @param properties properties from caller
     * @param repositoryHelper repository helper
     * @param serviceName this service name
     * @param methodName calling method
     * @return instance properties object
     * @throws InvalidParameterException useType is invalid
     */
    private InstanceProperties getServerAssetUseProperties(ServerAssetUseProperties properties,
                                                           OMRSRepositoryHelper     repositoryHelper,
                                                           String                   serviceName,
                                                           String                   methodName) throws InvalidParameterException
    {
        InstanceProperties instanceProperties = null;

        if (properties != null)
        {
            instanceProperties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                              null,
                                                                              OpenMetadataProperty.DESCRIPTION.name,
                                                                              properties.getDescription(),
                                                                              methodName);

            if (properties.getMaximumInstancesSet())
            {
                instanceProperties = repositoryHelper.addIntPropertyToInstance(serviceName,
                                                                               null,
                                                                               OpenMetadataProperty.MAXIMUM_INSTANCES.name,
                                                                               properties.getMaximumInstances(),
                                                                               methodName);
            }

            if (properties.getMinimumInstancesSet())
            {
                instanceProperties = repositoryHelper.addIntPropertyToInstance(serviceName,
                                                                               null,
                                                                               OpenMetadataProperty.MINIMUM_INSTANCES.name,
                                                                               properties.getMinimumInstances(),
                                                                               methodName);
            }

            if (properties.getUseType() != null)
            {
                try
                {
                    instanceProperties = repositoryHelper.addEnumPropertyToInstance(serviceName,
                                                                                    instanceProperties,
                                                                                    OpenMetadataProperty.USE_TYPE.name,
                                                                                    ServerAssetUseType.getOpenTypeGUID(),
                                                                                    ServerAssetUseType.getOpenTypeName(),
                                                                                    properties.getUseType().getOrdinal(),
                                                                                    methodName);
                }
                catch (TypeErrorException error)
                {
                    throw new InvalidParameterException(error, OpenMetadataProperty.USE_TYPE.name);
                }
            }

            if ((properties.getEffectiveFrom() != null) || (properties.getEffectiveTo() != null))
            {
                if (instanceProperties == null)
                {
                    instanceProperties = new InstanceProperties();
                }

                instanceProperties.setEffectiveFromTime(properties.getEffectiveFrom());
                instanceProperties.setEffectiveToTime(properties.getEffectiveTo());
            }
        }

        return instanceProperties;
    }


    /**
     * Update the metadata relationship to represent the use of an asset by a software capability.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param serverAssetUseGUID unique identifier of the relationship between a software capability and an asset
     * @param isMergeUpdate are unspecified properties unchanged (true) or removed?
     * @param requestBody new properties for the ServerAssetUse relationship
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse updateServerAssetUse(String                    serverName,
                                             String                    userId,
                                             String                    serverAssetUseGUID,
                                             boolean                   isMergeUpdate,
                                             ServerAssetUseRequestBody requestBody)
    {
        final String methodName               = "updateServerAssetUse";
        final String elementGUIDParameterName = "serverAssetUseGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SoftwareCapabilityHandler<SoftwareCapabilityElement> handler = instanceHandler.getSoftwareCapabilityHandler(userId,
                                                                                                                        serverName,
                                                                                                                        methodName);

            handler.updateRelationshipProperties(userId,
                                                 requestBody.getExternalSourceGUID(),
                                                 requestBody.getExternalSourceName(),
                                                 serverAssetUseGUID,
                                                 elementGUIDParameterName,
                                                 OpenMetadataType.SERVER_ASSET_USE_RELATIONSHIP.typeName,
                                                 isMergeUpdate,
                                                 this.getServerAssetUseProperties(requestBody.getProperties(),
                                                                                  instanceHandler.getRepositoryHelper(userId,
                                                                                                                      serverName,
                                                                                                                      methodName),
                                                                                  instanceHandler.getServiceName(),
                                                                                  methodName),
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
     * Remove the metadata relationship to represent the use of an asset by a software capability.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param serverAssetUseGUID unique identifier of the relationship between a software capability and an asset
     * @param requestBody unique identifier of software capability representing the caller
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse removeServerAssetUse(String                    serverName,
                                             String                    userId,
                                             String                    serverAssetUseGUID,
                                             ExternalSourceRequestBody requestBody)
    {
        final String methodName               = "removeServerAssetUse";
        final String elementGUIDParameterName = "serverAssetUseGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SoftwareCapabilityHandler<SoftwareCapabilityElement> handler = instanceHandler.getSoftwareCapabilityHandler(userId, serverName, methodName);

            handler.deleteRelationship(userId,
                                       requestBody.getExternalSourceGUID(),
                                       requestBody.getExternalSourceName(),
                                       serverAssetUseGUID,
                                       elementGUIDParameterName,
                                       OpenMetadataType.SERVER_ASSET_USE_RELATIONSHIP.typeName,
                                       false,
                                       false,
                                       null,
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
     * Return the list of server asset use relationships associated with a software capability.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param capabilityGUID unique identifier of the software capability to query
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody values to search for.
     *
     * @return list of matching relationships or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public ServerAssetUsesResponse getServerAssetUsesForCapability(String             serverName,
                                                                   String             userId,
                                                                   String             capabilityGUID,
                                                                   int                startFrom,
                                                                   int                pageSize,
                                                                   UseTypeRequestBody requestBody)
    {
        final String methodName               = "getServerAssetUsesForCapability";
        final String elementGUIDParameterName = "capabilityGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ServerAssetUsesResponse response = new ServerAssetUsesResponse();
        AuditLog                auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SoftwareCapabilityHandler<SoftwareCapabilityElement> handler      = instanceHandler.getSoftwareCapabilityHandler(userId, serverName, methodName);
            AssetHandler<AssetElement>                           assetHandler = instanceHandler.getAssetHandler(userId, serverName, methodName);

            List<Relationship> relationships = handler.getAttachmentLinks(userId,
                                                                          capabilityGUID,
                                                                          elementGUIDParameterName,
                                                                          OpenMetadataType.SOFTWARE_CAPABILITY.typeName,
                                                                          OpenMetadataType.SERVER_ASSET_USE_RELATIONSHIP.typeGUID,
                                                                          OpenMetadataType.SERVER_ASSET_USE_RELATIONSHIP.typeName,
                                                                          null,
                                                                          OpenMetadataType.ASSET.typeName,
                                                                          0,
                                                                          false,
                                                                          false,
                                                                          startFrom,
                                                                          pageSize,
                                                                          requestBody.getEffectiveTime(),
                                                                          methodName);

            response.setElements(getServerAssetUseElements(userId,
                                                           relationships,
                                                           requestBody.getUseType(),
                                                           assetHandler,
                                                           handler.getRepositoryHelper(),
                                                           handler.getServiceName(),
                                                           serverName,
                                                           methodName));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Return the list of software capabilities that make use of a specific asset.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param assetGUID unique identifier of the asset to query
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody values to search for
     *
     * @return list of matching relationships or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public ServerAssetUsesResponse getCapabilityUsesForAsset(String             serverName,
                                                             String             userId,
                                                             String             assetGUID,
                                                             int                startFrom,
                                                             int                pageSize,
                                                             UseTypeRequestBody requestBody)
    {
        final String methodName               = "getCapabilityUsesForAsset";
        final String elementGUIDParameterName = "assetGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ServerAssetUsesResponse response = new ServerAssetUsesResponse();
        AuditLog                auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SoftwareCapabilityHandler<SoftwareCapabilityElement> handler      = instanceHandler.getSoftwareCapabilityHandler(userId, serverName, methodName);
            AssetHandler<AssetElement>                           assetHandler = instanceHandler.getAssetHandler(userId, serverName, methodName);

            List<Relationship> relationships = handler.getAttachmentLinks(userId,
                                                                          assetGUID,
                                                                          elementGUIDParameterName,
                                                                          OpenMetadataType.ASSET.typeName,
                                                                          OpenMetadataType.SERVER_ASSET_USE_RELATIONSHIP.typeGUID,
                                                                          OpenMetadataType.SERVER_ASSET_USE_RELATIONSHIP.typeName,
                                                                          null,
                                                                          OpenMetadataType.SOFTWARE_CAPABILITY.typeName,
                                                                          0,
                                                                          false,
                                                                          false,
                                                                          startFrom,
                                                                          pageSize,
                                                                          requestBody.getEffectiveTime(),
                                                                          methodName);

            response.setElements(getServerAssetUseElements(userId,
                                                           relationships,
                                                           requestBody.getUseType(),
                                                           assetHandler,
                                                           handler.getRepositoryHelper(),
                                                           handler.getServiceName(),
                                                           serverName,
                                                           methodName));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Retrieve the list of relationships between a specific software capability and a specific asset.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param capabilityGUID unique identifier of a software capability
     * @param assetGUID unique identifier of an asset
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody effective time for the query
     *
     * @return list of matching relationships or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public ServerAssetUsesResponse getServerAssetUsesForElements(String                   serverName,
                                                                 String                   userId,
                                                                 String                   capabilityGUID,
                                                                 String                   assetGUID,
                                                                 int                      startFrom,
                                                                 int                      pageSize,
                                                                 EffectiveTimeRequestBody requestBody)
    {
        final String methodName                  = "getServerAssetUsesForElements";
        final String capabilityGUIDParameterName = "capabilityGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ServerAssetUsesResponse response = new ServerAssetUsesResponse();
        AuditLog                auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SoftwareCapabilityHandler<SoftwareCapabilityElement> handler      = instanceHandler.getSoftwareCapabilityHandler(userId, serverName, methodName);
            AssetHandler<AssetElement>                           assetHandler = instanceHandler.getAssetHandler(userId, serverName, methodName);

            List<Relationship> relationships = handler.getAttachmentLinks(userId,
                                                                          capabilityGUID,
                                                                          capabilityGUIDParameterName,
                                                                          OpenMetadataType.SOFTWARE_CAPABILITY.typeName,
                                                                          OpenMetadataType.SERVER_ASSET_USE_RELATIONSHIP.typeGUID,
                                                                          OpenMetadataType.SERVER_ASSET_USE_RELATIONSHIP.typeName,
                                                                          assetGUID,
                                                                          OpenMetadataType.ASSET.typeName,
                                                                          0,
                                                                          false,
                                                                          false,
                                                                          startFrom,
                                                                          pageSize,
                                                                          requestBody.getEffectiveTime(),
                                                                          methodName);

            response.setElements(getServerAssetUseElements(userId,
                                                           relationships,
                                                           null,
                                                           assetHandler,
                                                           handler.getRepositoryHelper(),
                                                           handler.getServiceName(),
                                                           serverName,
                                                           methodName));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Retrieve the server asset use type relationship with the supplied unique identifier.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param guid unique identifier of the requested metadata element
     *
     * @return requested relationship or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public ServerAssetUseResponse getServerAssetUseByGUID(String serverName,
                                                          String userId,
                                                          String guid)
    {
        final String methodName               = "getServerAssetUseByGUID";
        final String elementGUIDParameterName = "guid";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ServerAssetUseResponse response = new ServerAssetUseResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SoftwareCapabilityHandler<SoftwareCapabilityElement> handler      = instanceHandler.getSoftwareCapabilityHandler(userId, serverName, methodName);
            AssetHandler<AssetElement>                           assetHandler = instanceHandler.getAssetHandler(userId, serverName, methodName);

            Relationship relationship = handler.getAttachmentLink(userId,
                                                                  guid,
                                                                  elementGUIDParameterName,
                                                                  OpenMetadataType.SERVER_ASSET_USE_RELATIONSHIP.typeName,
                                                                  null,
                                                                  methodName);

            if (relationship != null)
            {
                response.setElement(getServerAssetUseElement(userId,
                                                             relationship,
                                                             null,
                                                             assetHandler,
                                                             handler.getRepositoryHelper(),
                                                             handler.getServiceName(),
                                                             serverName,
                                                             methodName));
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
     * Build the server asset use elements from the retrieved relationships.
     *
     * @param userId calling user
     * @param relationships results from the repositories
     * @param useType useType to match on
     * @param assetHandler handler used to retrieve the asset element
     * @param repositoryHelper repository helper
     * @param serviceName name of this service
     * @param serverName name of called server
     * @param methodName calling method
     *
     * @return list of elements
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    private List<ServerAssetUseElement> getServerAssetUseElements(String                     userId,
                                                                  List<Relationship>         relationships,
                                                                  ServerAssetUseType         useType,
                                                                  AssetHandler<AssetElement> assetHandler,
                                                                  OMRSRepositoryHelper       repositoryHelper,
                                                                  String                     serviceName,
                                                                  String                     serverName,
                                                                  String                     methodName) throws InvalidParameterException,
                                                                                                                UserNotAuthorizedException,
                                                                                                                PropertyServerException
    {
        List<ServerAssetUseElement> results = new ArrayList<>();

        if (relationships != null)

        {
            for (Relationship relationship : relationships)
            {
                if (relationship != null)
                {
                    ServerAssetUseElement result = getServerAssetUseElement(userId,
                                                                            relationship,
                                                                            useType,
                                                                            assetHandler,
                                                                            repositoryHelper,
                                                                            serviceName,
                                                                            serverName,
                                                                            methodName);

                    if (result != null)
                    {
                        results.add(result);
                    }
                }
            }
        }

        if (! results.isEmpty())
        {
            return results;
        }

        return null;
    }


    /**
     * Build the server asset use element from the retrieved relationship.
     *
     * @param userId calling user
     * @param relationship result from the repositories
     * @param requestedUseType useType to match on
     * @param assetHandler handler used to retrieve the asset element
     * @param repositoryHelper repository helper
     * @param serviceName name of this service
     * @param serverName name of called server
     * @param methodName calling method
     *
     * @return element
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    private ServerAssetUseElement getServerAssetUseElement(String                     userId,
                                                           Relationship               relationship,
                                                           ServerAssetUseType         requestedUseType,
                                                           AssetHandler<AssetElement> assetHandler,
                                                           OMRSRepositoryHelper       repositoryHelper,
                                                           String                     serviceName,
                                                           String                     serverName,
                                                           String                     methodName) throws InvalidParameterException,
                                                                                                         UserNotAuthorizedException,
                                                                                                         PropertyServerException
    {
        final String assetGUIDParameterName = "relationship.entityTwoProxy.guid";

        boolean matchingUseType = false;

        InstanceProperties instanceProperties = relationship.getProperties();

        int propertyUseTypeOrdinal = repositoryHelper.removeEnumPropertyOrdinal(serviceName,
                                                                                OpenMetadataProperty.USE_TYPE.name,
                                                                                instanceProperties,
                                                                                methodName);

        if (requestedUseType == null)
        {
            matchingUseType = true;
        }
        else
        {
            if (requestedUseType.getOrdinal() == propertyUseTypeOrdinal)
            {
                matchingUseType = true;
            }
        }

        if (matchingUseType)
        {
            ServerAssetUseElement element = new ServerAssetUseElement();

            element.setAsset(assetHandler.getBeanFromRepository(userId,
                                                                relationship.getEntityTwoProxy().getGUID(),
                                                                assetGUIDParameterName,
                                                                OpenMetadataType.ASSET.typeName,
                                                                false,
                                                                false,
                                                                new Date(),
                                                                methodName));

            ElementStubConverter<ElementStub> converter = new ElementStubConverter<>(repositoryHelper, serviceName, serverName);
            element.setCapabilityStub(converter.getElementStub(ElementStub.class,
                                                               relationship.getEntityOneProxy(),
                                                               methodName));


            if (instanceProperties != null)
            {
                ServerAssetUseProperties properties = new ServerAssetUseProperties();


                Iterator<String> propertyNames = instanceProperties.getPropertyNames();
                if (propertyNames != null)
                {
                    while (propertyNames.hasNext())
                    {
                        String propertyName = propertyNames.next();

                        if (OpenMetadataProperty.MAXIMUM_INSTANCES.name.equals(propertyName))
                        {
                            properties.setMaximumInstancesSet(true);

                            properties.setMaximumInstances(repositoryHelper.getIntProperty(serviceName,
                                                                                           OpenMetadataProperty.MAXIMUM_INSTANCES.name,
                                                                                           relationship.getProperties(),
                                                                                           methodName));
                        }
                        else if (OpenMetadataProperty.MINIMUM_INSTANCES.name.equals(propertyName))
                        {
                            properties.setMinimumInstancesSet(true);

                            properties.setMinimumInstances(repositoryHelper.getIntProperty(serviceName,
                                                                                           OpenMetadataProperty.MAXIMUM_INSTANCES.name,
                                                                                           relationship.getProperties(),
                                                                                           methodName));
                        }
                        else if (OpenMetadataProperty.DESCRIPTION.name.equals(propertyName))
                        {
                            properties.setDescription(repositoryHelper.getStringProperty(serviceName,
                                                                                         OpenMetadataProperty.DESCRIPTION.name,
                                                                                         relationship.getProperties(),
                                                                                         methodName));
                        }
                        else if (OpenMetadataProperty.USE_TYPE.name.equals(propertyName))
                        {

                            for (ServerAssetUseType useTypeValue : ServerAssetUseType.values())
                            {
                                if (useTypeValue.getOrdinal() == propertyUseTypeOrdinal)
                                {
                                    properties.setUseType(useTypeValue);
                                }
                            }
                        }
                    }
                }

                element.setServerAssetUse(properties);
            }

            return element;
        }

        return null;
    }
}