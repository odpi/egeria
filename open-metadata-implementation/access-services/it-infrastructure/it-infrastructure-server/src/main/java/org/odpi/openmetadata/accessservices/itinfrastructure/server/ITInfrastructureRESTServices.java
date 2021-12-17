/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.itinfrastructure.server;


import org.odpi.openmetadata.accessservices.itinfrastructure.converters.ElementStubConverter;
import org.odpi.openmetadata.accessservices.itinfrastructure.metadataelements.AssetElement;
import org.odpi.openmetadata.accessservices.itinfrastructure.metadataelements.ConnectionElement;
import org.odpi.openmetadata.accessservices.itinfrastructure.metadataelements.ConnectorTypeElement;
import org.odpi.openmetadata.accessservices.itinfrastructure.metadataelements.ElementStub;
import org.odpi.openmetadata.accessservices.itinfrastructure.metadataelements.EndpointElement;
import org.odpi.openmetadata.accessservices.itinfrastructure.metadataelements.ServerAssetUseElement;
import org.odpi.openmetadata.accessservices.itinfrastructure.metadataelements.SoftwareServerCapabilityElement;
import org.odpi.openmetadata.accessservices.itinfrastructure.properties.ConnectionProperties;
import org.odpi.openmetadata.accessservices.itinfrastructure.properties.ConnectorTypeProperties;
import org.odpi.openmetadata.accessservices.itinfrastructure.properties.EndpointProperties;
import org.odpi.openmetadata.accessservices.itinfrastructure.properties.ServerAssetUseProperties;
import org.odpi.openmetadata.accessservices.itinfrastructure.properties.ServerAssetUseType;
import org.odpi.openmetadata.accessservices.itinfrastructure.properties.SoftwareServerCapabilityProperties;
import org.odpi.openmetadata.accessservices.itinfrastructure.rest.*;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.EffectiveTimeRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.NameRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.SearchStringRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.commonservices.generichandlers.AssetHandler;
import org.odpi.openmetadata.commonservices.generichandlers.ConnectionHandler;
import org.odpi.openmetadata.commonservices.generichandlers.ConnectorTypeHandler;
import org.odpi.openmetadata.commonservices.generichandlers.EndpointHandler;
import org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper;
import org.odpi.openmetadata.commonservices.generichandlers.SoftwareServerCapabilityHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
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

                response.setElementList(setUpVendorProperties(userId, endpoints, handler, methodName));
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
                response.setElementList(setUpVendorProperties(userId, endpoints, handler, methodName));
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



    /* =====================================================================================================================
     * The software server capability links assets to the hosting server.
     */


    /**
     * Create a new metadata element to represent a software server capability.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param infrastructureManagerIsHome should the software server capability be marked as owned by the infrastructure manager so others can not update?
     * @param requestBody properties to store
     *
     * @return unique identifier of the new metadata element or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GUIDResponse createSoftwareServerCapability(String                              serverName,
                                                       String                              userId,
                                                       boolean                             infrastructureManagerIsHome,
                                                       SoftwareServerCapabilityRequestBody requestBody)
    {
        final String methodName = "createSoftwareServerCapability";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SoftwareServerCapabilityHandler<SoftwareServerCapabilityElement> handler = instanceHandler.getSoftwareServerCapabilityHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                String capabilityGUID;

                if (infrastructureManagerIsHome)
                {
                    capabilityGUID = handler.createSoftwareServerCapability(userId,
                                                                            requestBody.getExternalSourceGUID(),
                                                                            requestBody.getExternalSourceName(),
                                                                            requestBody.getTypeName(),
                                                                            requestBody.getClassificationName(),
                                                                            requestBody.getQualifiedName(),
                                                                            requestBody.getDisplayName(),
                                                                            requestBody.getDescription(),
                                                                            requestBody.getTypeDescription(),
                                                                            requestBody.getVersion(),
                                                                            requestBody.getPatchLevel(),
                                                                            requestBody.getSource(),
                                                                            requestBody.getAdditionalProperties(),
                                                                            requestBody.getExtendedProperties(),
                                                                            requestBody.getVendorProperties(),
                                                                            requestBody.getEffectiveFrom(),
                                                                            requestBody.getEffectiveTo(),
                                                                            methodName);
                }
                else
                {
                    capabilityGUID = handler.createSoftwareServerCapability(userId,
                                                                            null,
                                                                            null,
                                                                            requestBody.getTypeName(),
                                                                            requestBody.getClassificationName(),
                                                                            requestBody.getQualifiedName(),
                                                                            requestBody.getDisplayName(),
                                                                            requestBody.getDescription(),
                                                                            requestBody.getTypeDescription(),
                                                                            requestBody.getVersion(),
                                                                            requestBody.getPatchLevel(),
                                                                            requestBody.getSource(),
                                                                            requestBody.getAdditionalProperties(),
                                                                            requestBody.getExtendedProperties(),
                                                                            requestBody.getVendorProperties(),
                                                                            requestBody.getEffectiveFrom(),
                                                                            requestBody.getEffectiveTo(),
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
     * Create a new metadata element to represent a software server capability using an existing metadata element as a template.
     *
     * @param serverName name of the service to route the request to.
     *
     * @param userId calling user
     * @param infrastructureManagerIsHome should the software server capability be marked as owned by the infrastructure manager so others can not update?
     * @param templateGUID unique identifier of the metadata element to copy
     * @param requestBody properties that override the template
     *
     * @return unique identifier of the new metadata element or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GUIDResponse createSoftwareServerCapabilityFromTemplate(String              serverName,
                                                                   String              userId,
                                                                   String              templateGUID,
                                                                   boolean             infrastructureManagerIsHome,
                                                                   TemplateRequestBody requestBody)
    {
        final String methodName                 = "createSoftwareServerCapabilityFromTemplate";
        final String templateGUIDParameterName  = "templateGUID";
        final String qualifiedNameParameterName = "qualifiedName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SoftwareServerCapabilityHandler<SoftwareServerCapabilityElement> handler = instanceHandler.getSoftwareServerCapabilityHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                String capabilityGUID;

                if (infrastructureManagerIsHome)
                {
                    capabilityGUID = handler.createSoftwareServerCapabilityFromTemplate(userId,
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
                    capabilityGUID = handler.createSoftwareServerCapabilityFromTemplate(userId,
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
     * Update the metadata element representing a software server capability.
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
    public VoidResponse updateSoftwareServerCapability(String                              serverName,
                                                       String                              userId,
                                                       String                              capabilityGUID,
                                                       boolean                             isMergeUpdate,
                                                       SoftwareServerCapabilityRequestBody requestBody)
    {
        final String methodName                  = "updateSoftwareServerCapability";
        final String elementGUIDParameterName    = "capabilityGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SoftwareServerCapabilityHandler<SoftwareServerCapabilityElement> handler = instanceHandler.getSoftwareServerCapabilityHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.updateSoftwareServerCapability(userId,
                                               requestBody.getExternalSourceGUID(),
                                               requestBody.getExternalSourceName(),
                                               capabilityGUID,
                                               elementGUIDParameterName,
                                               requestBody.getQualifiedName(),
                                               requestBody.getDisplayName(),
                                               requestBody.getDescription(),
                                               requestBody.getTypeDescription(),
                                               requestBody.getVersion(),
                                               requestBody.getPatchLevel(),
                                               requestBody.getSource(),
                                               requestBody.getAdditionalProperties(),
                                               requestBody.getExtendedProperties(),
                                               requestBody.getVendorProperties(),
                                               isMergeUpdate,
                                               requestBody.getEffectiveFrom(),
                                               requestBody.getEffectiveTo(),
                                               methodName);

                if ((!isMergeUpdate) || (requestBody.getVendorProperties() != null))
                {
                    handler.setVendorProperties(userId,
                                                capabilityGUID,
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
     * Remove the metadata element representing a software server capability.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param capabilityGUID unique identifier of the metadata element to remove
     * @param requestBody unique identifier of software server capability representing the caller
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse removeSoftwareServerCapability(String                    serverName,
                                                       String                    userId,
                                                       String                    capabilityGUID,
                                                       MetadataSourceRequestBody requestBody)
    {
        final String methodName = "removeSoftwareServerCapability";
        final String elementGUIDParameterName    = "capabilityGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SoftwareServerCapabilityHandler<SoftwareServerCapabilityElement> handler = instanceHandler.getSoftwareServerCapabilityHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.deleteBeanInRepository(userId,
                                               requestBody.getExternalSourceGUID(),
                                               requestBody.getExternalSourceName(),
                                               capabilityGUID,
                                               elementGUIDParameterName,
                                               OpenMetadataAPIMapper.SOFTWARE_SERVER_CAPABILITY_TYPE_GUID,
                                               OpenMetadataAPIMapper.SOFTWARE_SERVER_CAPABILITY_TYPE_NAME,
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
     * Retrieve the list of software server capability metadata elements that contain the search string.
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
    public SoftwareServerCapabilityListResponse findSoftwareServerCapabilities(String                  serverName,
                                                                               String                  userId,
                                                                               int                     startFrom,
                                                                               int                     pageSize,
                                                                               SearchStringRequestBody requestBody)
    {
        final String methodName                = "findSoftwareServerCapabilities";
        final String searchStringParameterName = "searchString";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        SoftwareServerCapabilityListResponse response = new SoftwareServerCapabilityListResponse();
        AuditLog                             auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                SoftwareServerCapabilityHandler<SoftwareServerCapabilityElement> handler = instanceHandler.getSoftwareServerCapabilityHandler(userId, serverName, methodName);

                List<SoftwareServerCapabilityElement> capabilities = handler.findBeans(userId,
                                                                                       requestBody.getSearchString(),
                                                                                       searchStringParameterName,
                                                                                       OpenMetadataAPIMapper.SOFTWARE_SERVER_CAPABILITY_TYPE_GUID,
                                                                                       OpenMetadataAPIMapper.SOFTWARE_SERVER_CAPABILITY_TYPE_NAME,
                                                                                       null,
                                                                                       startFrom,
                                                                                       pageSize,
                                                                                       new Date(),
                                                                                       methodName);

                response.setElementList(setUpVendorProperties(userId, capabilities, handler, methodName));
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
     * Retrieve the list of software server capability metadata elements with a matching qualified or display name.
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
    public SoftwareServerCapabilityListResponse getSoftwareServerCapabilitiesByName(String          serverName,
                                                                                    String          userId,
                                                                                    int             startFrom,
                                                                                    int             pageSize,
                                                                                    NameRequestBody requestBody)
    {
        final String methodName        = "getSoftwareServerCapabilitiesByName";
        final String nameParameterName = "name";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        SoftwareServerCapabilityListResponse response = new SoftwareServerCapabilityListResponse();
        AuditLog                             auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                SoftwareServerCapabilityHandler<SoftwareServerCapabilityElement> handler = instanceHandler.getSoftwareServerCapabilityHandler(userId, serverName, methodName);

                List<String> specificMatchPropertyNames = new ArrayList<>();
                specificMatchPropertyNames.add(OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME);
                specificMatchPropertyNames.add(OpenMetadataAPIMapper.NAME_PROPERTY_NAME);
                specificMatchPropertyNames.add(OpenMetadataAPIMapper.DEPLOYED_IMPLEMENTATION_TYPE_PROPERTY_NAME);

                List<SoftwareServerCapabilityElement> capabilities = handler.getBeansByValue(userId,
                                                                                             requestBody.getName(),
                                                                                             nameParameterName,
                                                                                             OpenMetadataAPIMapper.SOFTWARE_SERVER_CAPABILITY_TYPE_GUID,
                                                                                             OpenMetadataAPIMapper.SOFTWARE_SERVER_CAPABILITY_TYPE_NAME,
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

                response.setElementList(setUpVendorProperties(userId, capabilities, handler, methodName));
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
     * Retrieve the software server capability metadata element with the supplied unique identifier.
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
    public SoftwareServerCapabilityResponse getSoftwareServerCapabilityByGUID(String serverName,
                                                                              String userId,
                                                                              String guid)
    {
        final String methodName = "getSoftwareServerCapabilityByGUID";
        final String guidParameterName = "guid";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        SoftwareServerCapabilityResponse response = new SoftwareServerCapabilityResponse();
        AuditLog         auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SoftwareServerCapabilityHandler<SoftwareServerCapabilityElement> handler = instanceHandler.getSoftwareServerCapabilityHandler(userId, serverName, methodName);

            SoftwareServerCapabilityElement capability = handler.getBeanFromRepository(userId,
                                                                                       guid,
                                                                                       guidParameterName,
                                                                                       OpenMetadataAPIMapper.SOFTWARE_SERVER_CAPABILITY_TYPE_NAME,
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
    private List<SoftwareServerCapabilityElement> setUpVendorProperties(String                                                           userId,
                                                                        List<SoftwareServerCapabilityElement>                            retrievedResults,
                                                                        SoftwareServerCapabilityHandler<SoftwareServerCapabilityElement> handler,
                                                                        String                                                           methodName) throws InvalidParameterException,
                                                                                                                                                            UserNotAuthorizedException,
                                                                                                                                                            PropertyServerException
    {
        if (retrievedResults != null)
        {
            for (SoftwareServerCapabilityElement element : retrievedResults)
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
    private SoftwareServerCapabilityElement setUpVendorProperties(String                                                           userId,
                                                                  SoftwareServerCapabilityElement                                  element,
                                                                  SoftwareServerCapabilityHandler<SoftwareServerCapabilityElement> handler,
                                                                  String                                                           methodName) throws InvalidParameterException,
                                                                                                                                                      UserNotAuthorizedException,
                                                                                                                                                      PropertyServerException
    {
        final String elementGUIDParameterName = "element.getElementHeader().getGUID()";

        if (element != null)
        {
            SoftwareServerCapabilityProperties properties = element.getProperties();

            properties.setVendorProperties(handler.getVendorProperties(userId,
                                                                       element.getElementHeader().getGUID(),
                                                                       elementGUIDParameterName,
                                                                       methodName));
        }

        return element;
    }


    /*
     * A software server capability works with assets
     */

    /**
     * Create a new metadata relationship to represent the use of an asset by a software server capability.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param infrastructureManagerIsHome should the software server capability be marked as owned by the infrastructure manager so others can not update?
     * @param capabilityGUID unique identifier of a software server capability
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

            SoftwareServerCapabilityHandler<SoftwareServerCapabilityElement> handler = instanceHandler.getSoftwareServerCapabilityHandler(userId, serverName, methodName);

            String guid;

            if (infrastructureManagerIsHome)
            {
                guid = handler.linkElementToElement(userId,
                                                    requestBody.getExternalSourceGUID(),
                                                    requestBody.getExternalSourceName(),
                                                    capabilityGUID,
                                                    capabilityGUIDParameterName,
                                                    OpenMetadataAPIMapper.SOFTWARE_SERVER_CAPABILITY_TYPE_NAME,
                                                    assetGUID,
                                                    assetGUIDParameterName,
                                                    OpenMetadataAPIMapper.ASSET_TYPE_NAME,
                                                    false,
                                                    false,
                                                    OpenMetadataAPIMapper.SERVER_ASSET_USE_TYPE_GUID,
                                                    OpenMetadataAPIMapper.SERVER_ASSET_USE_TYPE_NAME,
                                                    this.getServerAssetUseProperties(requestBody.getProperties(),
                                                                                     instanceHandler.getRepositoryHelper(userId, serverName,
                                                                                                                         methodName),
                                                                                     instanceHandler.getServiceName(),
                                                                                     methodName),
                                                    methodName);
            }
            else
            {
                guid = handler.linkElementToElement(userId,
                                                    null,
                                                    null,
                                                    capabilityGUID,
                                                    capabilityGUIDParameterName,
                                                    OpenMetadataAPIMapper.SOFTWARE_SERVER_CAPABILITY_TYPE_NAME,
                                                    assetGUID,
                                                    assetGUIDParameterName,
                                                    OpenMetadataAPIMapper.ASSET_TYPE_NAME,
                                                    false,
                                                    false,
                                                    OpenMetadataAPIMapper.SERVER_ASSET_USE_TYPE_GUID,
                                                    OpenMetadataAPIMapper.SERVER_ASSET_USE_TYPE_NAME,
                                                    this.getServerAssetUseProperties(requestBody.getProperties(),
                                                                                     instanceHandler.getRepositoryHelper(userId, serverName,
                                                                                                                         methodName),
                                                                                     instanceHandler.getServiceName(),
                                                                                     methodName),
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
                                                                              OpenMetadataAPIMapper.DESCRIPTION_PROPERTY_NAME,
                                                                              properties.getDescription(),
                                                                              methodName);

            if (properties.getMaximumInstancesSet())
            {
                instanceProperties = repositoryHelper.addIntPropertyToInstance(serviceName,
                                                                               null,
                                                                               OpenMetadataAPIMapper.MAXIMUM_INSTANCES_PROPERTY_NAME,
                                                                               properties.getMaximumInstances(),
                                                                               methodName);
            }

            if (properties.getMinimumInstancesSet())
            {
                instanceProperties = repositoryHelper.addIntPropertyToInstance(serviceName,
                                                                               null,
                                                                               OpenMetadataAPIMapper.MINIMUM_INSTANCES_PROPERTY_NAME,
                                                                               properties.getMinimumInstances(),
                                                                               methodName);
            }

            if (properties.getUseType() != null)
            {
                try
                {
                    instanceProperties = repositoryHelper.addEnumPropertyToInstance(serviceName,
                                                                                    instanceProperties,
                                                                                    OpenMetadataAPIMapper.USE_TYPE_PROPERTY_NAME,
                                                                                    OpenMetadataAPIMapper.SERVER_ASSET_USE_TYPE_TYPE_GUID,
                                                                                    OpenMetadataAPIMapper.SERVER_ASSET_USE_TYPE_TYPE_NAME,
                                                                                    properties.getUseType().getOpenTypeOrdinal(),
                                                                                    methodName);
                }
                catch (TypeErrorException error)
                {
                    throw new InvalidParameterException(error, OpenMetadataAPIMapper.USE_TYPE_PROPERTY_NAME);
                }
            }

            if (instanceProperties != null)
            {
                instanceProperties.setEffectiveFromTime(properties.getEffectiveFrom());
                instanceProperties.setEffectiveToTime(properties.getEffectiveTo());
            }
        }

        return instanceProperties;
    }


    /**
     * Update the metadata relationship to represent the use of an asset by a software server capability.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param serverAssetUseGUID unique identifier of the relationship between a software server capability and an asset
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

            SoftwareServerCapabilityHandler<SoftwareServerCapabilityElement> handler = instanceHandler.getSoftwareServerCapabilityHandler(userId, serverName, methodName);

            handler.updateRelationshipProperties(userId,
                                                 requestBody.getExternalSourceGUID(),
                                                 requestBody.getExternalSourceName(),
                                                 serverAssetUseGUID,
                                                 elementGUIDParameterName,
                                                 OpenMetadataAPIMapper.SERVER_ASSET_USE_TYPE_NAME,
                                                 isMergeUpdate,
                                                 this.getServerAssetUseProperties(requestBody.getProperties(),
                                                                                  instanceHandler.getRepositoryHelper(userId, serverName, methodName),
                                                                                  instanceHandler.getServiceName(),
                                                                                  methodName),
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
     * Remove the metadata relationship to represent the use of an asset by a software server capability.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param serverAssetUseGUID unique identifier of the relationship between a software server capability and an asset
     * @param requestBody unique identifier of software server capability representing the caller
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse removeServerAssetUse(String                    serverName,
                                             String                    userId,
                                             String                    serverAssetUseGUID,
                                             MetadataSourceRequestBody requestBody)
    {
        final String methodName               = "removeServerAssetUse";
        final String elementGUIDParameterName = "serverAssetUseGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SoftwareServerCapabilityHandler<SoftwareServerCapabilityElement> handler = instanceHandler.getSoftwareServerCapabilityHandler(userId, serverName, methodName);

            handler.deleteRelationship(userId,
                                       requestBody.getExternalSourceGUID(),
                                       requestBody.getExternalSourceName(),
                                       serverAssetUseGUID,
                                       elementGUIDParameterName,
                                       OpenMetadataAPIMapper.SERVER_ASSET_USE_TYPE_NAME,
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
     * Return the list of server asset use relationships associated with a software server capability.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param capabilityGUID unique identifier of the software server capability to query
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody values to search for.
     *
     * @return list of matching relationships or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public ServerAssetUseListResponse getServerAssetUsesForCapability(String             serverName,
                                                                      String             userId,
                                                                      String             capabilityGUID,
                                                                      int                startFrom,
                                                                      int                pageSize,
                                                                      UseTypeRequestBody requestBody)
    {
        final String methodName               = "getServerAssetUsesForCapability";
        final String elementGUIDParameterName = "capabilityGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ServerAssetUseListResponse response = new ServerAssetUseListResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SoftwareServerCapabilityHandler<SoftwareServerCapabilityElement> handler = instanceHandler.getSoftwareServerCapabilityHandler(userId, serverName, methodName);
            AssetHandler<AssetElement>                                       assetHandler = instanceHandler.getAssetHandler(userId, serverName, methodName);

            List<Relationship> relationships = handler.getAttachmentLinks(userId,
                                                                          capabilityGUID,
                                                                          elementGUIDParameterName,
                                                                          OpenMetadataAPIMapper.SOFTWARE_SERVER_CAPABILITY_TYPE_NAME,
                                                                          OpenMetadataAPIMapper.SERVER_ASSET_USE_TYPE_GUID,
                                                                          OpenMetadataAPIMapper.SERVER_ASSET_USE_TYPE_NAME,
                                                                          OpenMetadataAPIMapper.ASSET_TYPE_NAME,
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
     * Return the list of software server capabilities that make use of a specific asset.
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
    public ServerAssetUseListResponse getCapabilityUsesForAsset(String             serverName,
                                                                String             userId,
                                                                String             assetGUID,
                                                                int                startFrom,
                                                                int                pageSize,
                                                                UseTypeRequestBody requestBody)
    {
        final String methodName               = "getCapabilityUsesForAsset";
        final String elementGUIDParameterName = "assetGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ServerAssetUseListResponse response = new ServerAssetUseListResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SoftwareServerCapabilityHandler<SoftwareServerCapabilityElement> handler = instanceHandler.getSoftwareServerCapabilityHandler(userId, serverName, methodName);
            AssetHandler<AssetElement>                                       assetHandler = instanceHandler.getAssetHandler(userId, serverName, methodName);

            List<Relationship> relationships = handler.getAttachmentLinks(userId,
                                                                          assetGUID,
                                                                          elementGUIDParameterName,
                                                                          OpenMetadataAPIMapper.ASSET_TYPE_NAME,
                                                                          OpenMetadataAPIMapper.SERVER_ASSET_USE_TYPE_GUID,
                                                                          OpenMetadataAPIMapper.SERVER_ASSET_USE_TYPE_NAME,
                                                                          OpenMetadataAPIMapper.SOFTWARE_SERVER_CAPABILITY_TYPE_NAME,
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
     * Retrieve the list of relationships between a specific software server capability and a specific asset.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param capabilityGUID unique identifier of a software server capability
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
    public ServerAssetUseListResponse getServerAssetUsesForElements(String                   serverName,
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

        ServerAssetUseListResponse response = new ServerAssetUseListResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SoftwareServerCapabilityHandler<SoftwareServerCapabilityElement> handler = instanceHandler.getSoftwareServerCapabilityHandler(userId, serverName, methodName);
            AssetHandler<AssetElement>                                       assetHandler = instanceHandler.getAssetHandler(userId, serverName, methodName);

            List<Relationship> relationships = handler.getAttachmentLinks(userId,
                                                                          capabilityGUID,
                                                                          capabilityGUIDParameterName,
                                                                          OpenMetadataAPIMapper.SOFTWARE_SERVER_CAPABILITY_TYPE_NAME,
                                                                          OpenMetadataAPIMapper.SERVER_ASSET_USE_TYPE_GUID,
                                                                          OpenMetadataAPIMapper.SERVER_ASSET_USE_TYPE_NAME,
                                                                          assetGUID,
                                                                          OpenMetadataAPIMapper.ASSET_TYPE_NAME,
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

            SoftwareServerCapabilityHandler<SoftwareServerCapabilityElement> handler = instanceHandler.getSoftwareServerCapabilityHandler(userId, serverName, methodName);
            AssetHandler<AssetElement>                                       assetHandler = instanceHandler.getAssetHandler(userId, serverName, methodName);

            Relationship relationship = handler.getAttachmentLink(userId,
                                                                  guid,
                                                                  elementGUIDParameterName,
                                                                  OpenMetadataAPIMapper.SERVER_ASSET_USE_TYPE_NAME,
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
                    ServerAssetUseElement result = getServerAssetUseElement(userId, relationship, useType, assetHandler, repositoryHelper, serviceName, serverName, methodName);

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
                                                                                OpenMetadataAPIMapper.USE_TYPE_PROPERTY_NAME,
                                                                                instanceProperties,
                                                                                methodName);

        if (requestedUseType == null)
        {
            matchingUseType = true;
        }
        else
        {
            if (requestedUseType.getOpenTypeOrdinal() == propertyUseTypeOrdinal)
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
                                                                OpenMetadataAPIMapper.ASSET_TYPE_NAME,
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

                        if (OpenMetadataAPIMapper.MAXIMUM_INSTANCES_PROPERTY_NAME.equals(propertyName))
                        {
                            properties.setMaximumInstancesSet(true);

                            properties.setMaximumInstances(repositoryHelper.getIntProperty(serviceName,
                                                                                           OpenMetadataAPIMapper.MAXIMUM_INSTANCES_PROPERTY_NAME,
                                                                                           relationship.getProperties(),
                                                                                           methodName));
                        }
                        else if (OpenMetadataAPIMapper.MINIMUM_INSTANCES_PROPERTY_NAME.equals(propertyName))
                        {
                            properties.setMinimumInstancesSet(true);

                            properties.setMinimumInstances(repositoryHelper.getIntProperty(serviceName,
                                                                                           OpenMetadataAPIMapper.MAXIMUM_INSTANCES_PROPERTY_NAME,
                                                                                           relationship.getProperties(),
                                                                                           methodName));
                        }
                        else if (OpenMetadataAPIMapper.DESCRIPTION_PROPERTY_NAME.equals(propertyName))
                        {
                            properties.setDescription(repositoryHelper.getStringProperty(serviceName,
                                                                                         OpenMetadataAPIMapper.DESCRIPTION_PROPERTY_NAME,
                                                                                         relationship.getProperties(),
                                                                                         methodName));
                        }
                        else if (OpenMetadataAPIMapper.USE_TYPE_PROPERTY_NAME.equals(propertyName))
                        {

                            for (ServerAssetUseType useTypeValue : ServerAssetUseType.values())
                            {
                                if (useTypeValue.getOpenTypeOrdinal() == propertyUseTypeOrdinal)
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