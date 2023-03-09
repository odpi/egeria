/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.assetmanager.server;


import org.odpi.openmetadata.accessservices.assetmanager.handlers.ConnectionExchangeHandler;
import org.odpi.openmetadata.accessservices.assetmanager.properties.AssetConnectionProperties;
import org.odpi.openmetadata.accessservices.assetmanager.properties.EmbeddedConnectionProperties;
import org.odpi.openmetadata.accessservices.assetmanager.rest.ConnectionRequestBody;
import org.odpi.openmetadata.accessservices.assetmanager.rest.ConnectionResponse;
import org.odpi.openmetadata.accessservices.assetmanager.rest.ConnectionsResponse;
import org.odpi.openmetadata.accessservices.assetmanager.rest.ConnectorTypeRequestBody;
import org.odpi.openmetadata.accessservices.assetmanager.rest.ConnectorTypeResponse;
import org.odpi.openmetadata.accessservices.assetmanager.rest.ConnectorTypesResponse;
import org.odpi.openmetadata.accessservices.assetmanager.rest.EffectiveTimeQueryRequestBody;
import org.odpi.openmetadata.accessservices.assetmanager.rest.EndpointRequestBody;
import org.odpi.openmetadata.accessservices.assetmanager.rest.EndpointResponse;
import org.odpi.openmetadata.accessservices.assetmanager.rest.EndpointsResponse;
import org.odpi.openmetadata.accessservices.assetmanager.rest.NameRequestBody;
import org.odpi.openmetadata.accessservices.assetmanager.rest.RelationshipRequestBody;
import org.odpi.openmetadata.accessservices.assetmanager.rest.SearchStringRequestBody;
import org.odpi.openmetadata.accessservices.assetmanager.rest.TemplateRequestBody;
import org.odpi.openmetadata.accessservices.assetmanager.rest.UpdateRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.slf4j.LoggerFactory;



/**
 * ConnectionExchangeRESTServices is the server-side for managing Connections, Endpoints and ConnectorTypes.
 */
public class ConnectionExchangeRESTServices
{
    private static final AssetManagerInstanceHandler instanceHandler = new AssetManagerInstanceHandler();
    private static final RESTCallLogger              restCallLogger  = new RESTCallLogger(LoggerFactory.getLogger(ConnectionExchangeRESTServices.class),
                                                                                          instanceHandler.getServiceName());

    private final RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();

    /**
     * Default constructor
     */
    public ConnectionExchangeRESTServices()
    {
    }


    /* ======================================================================================
     * The Connection entity is the top level element to describe a connection.
     */

    /**
     * Create a new metadata element to represent the root of a connection.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param assetManagerIsHome ensure that only the asset manager can update this connection
     * @param requestBody properties to store
     *
     * @return unique identifier of the new metadata element or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GUIDResponse createConnection(String                serverName,
                                         String                userId,
                                         boolean               assetManagerIsHome,
                                         ConnectionRequestBody requestBody)
    {
        final String  methodName = "createConnection";

        RESTCallToken token      = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                ConnectionExchangeHandler handler = instanceHandler.getConnectionExchangeHandler(userId, serverName, methodName);

                response.setGUID(handler.createConnection(userId,
                                                          requestBody.getMetadataCorrelationProperties(),
                                                          assetManagerIsHome,
                                                          requestBody.getElementProperties(),
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
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param templateGUID unique identifier of the metadata element to copy
     * @param assetManagerIsHome ensure that only the asset manager can update this connection
     * @param requestBody properties to store
     *
     * @return unique identifier of the new metadata element or
     *   InvalidParameterException  one of the parameters is invalid
     *   UserNotAuthorizedException the user is not authorized to issue this request
     *   PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GUIDResponse createConnectionFromTemplate(String              serverName,
                                                     String              userId,
                                                     String              templateGUID,
                                                     boolean             assetManagerIsHome,
                                                     TemplateRequestBody requestBody)
    {
        final String methodName = "createConnectionFromTemplate";

        RESTCallToken token      = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                ConnectionExchangeHandler handler = instanceHandler.getConnectionExchangeHandler(userId, serverName, methodName);

                response.setGUID(handler.createConnectionFromTemplate(userId,
                                                                      requestBody.getMetadataCorrelationProperties(),
                                                                      assetManagerIsHome,
                                                                      templateGUID,
                                                                      requestBody.getElementProperties(),
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
     * Update the metadata element representing an asset.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param connectionGUID unique identifier of the metadata element to update
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties to store
     *
     * @return void or
     *   InvalidParameterException  one of the parameters is invalid
     *   UserNotAuthorizedException the user is not authorized to issue this request
     *   PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse updateConnection(String                serverName,
                                         String                userId,
                                         String                connectionGUID,
                                         boolean               isMergeUpdate,
                                         boolean               forLineage,
                                         boolean               forDuplicateProcessing,
                                         ConnectionRequestBody requestBody)
    {
        final String methodName = "updateConnection";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                ConnectionExchangeHandler handler = instanceHandler.getConnectionExchangeHandler(userId, serverName, methodName);

                handler.updateConnection(userId,
                                         requestBody.getMetadataCorrelationProperties(),
                                         connectionGUID,
                                         isMergeUpdate,
                                         requestBody.getElementProperties(),
                                         forLineage,
                                         forDuplicateProcessing,
                                         requestBody.getEffectiveTime(),
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
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param connectionGUID unique identifier of the connection in the external asset manager
     * @param connectorTypeGUID unique identifier of the connector type in the external asset manager
     * @param assetManagerIsHome ensure that only the asset manager can update this relationship
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties to store
     *
     * @return void or
     *   InvalidParameterException  one of the parameters is invalid
     *   UserNotAuthorizedException the user is not authorized to issue this request
     *   PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse setupConnectorType(String                  serverName,
                                           String                  userId,
                                           String                  connectionGUID,
                                           String                  connectorTypeGUID,
                                           boolean                 assetManagerIsHome,
                                           boolean                 forLineage,
                                           boolean                 forDuplicateProcessing,
                                           RelationshipRequestBody requestBody)

    {
        final String methodName = "setupConnectorType";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                ConnectionExchangeHandler handler = instanceHandler.getConnectionExchangeHandler(userId, serverName, methodName);

                handler.setupConnectorType(userId,
                                           requestBody.getAssetManagerGUID(),
                                           requestBody.getAssetManagerName(),
                                           assetManagerIsHome,
                                           connectionGUID,
                                           connectorTypeGUID,
                                           requestBody.getProperties(),
                                           forLineage,
                                           forDuplicateProcessing,
                                           requestBody.getEffectiveTime(),
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
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param connectionGUID unique identifier of the connection in the external asset manager
     * @param connectorTypeGUID unique identifier of the connector type in the external asset manager
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties to store
     *
     * @return void or
     *   InvalidParameterException  one of the parameters is invalid
     *   UserNotAuthorizedException the user is not authorized to issue this request
     *   PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse clearConnectorType(String                        serverName,
                                           String                        userId,
                                           String                        connectionGUID,
                                           String                        connectorTypeGUID,
                                           boolean                       forLineage,
                                           boolean                       forDuplicateProcessing,
                                           EffectiveTimeQueryRequestBody requestBody)

    {
        final String methodName = "clearConnectorType";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                ConnectionExchangeHandler handler = instanceHandler.getConnectionExchangeHandler(userId, serverName, methodName);

                handler.clearConnectorType(userId,
                                           requestBody.getAssetManagerGUID(),
                                           requestBody.getAssetManagerName(),
                                           connectionGUID,
                                           connectorTypeGUID,
                                           forLineage,
                                           forDuplicateProcessing,
                                           requestBody.getEffectiveTime(),
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
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param connectionGUID unique identifier of the connection in the external asset manager
     * @param endpointGUID unique identifier of the endpoint in the external asset manager
     * @param assetManagerIsHome ensure that only the asset manager can update this relationship
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties to store
     *
     * @return void or
     *   InvalidParameterException  one of the parameters is invalid
     *   UserNotAuthorizedException the user is not authorized to issue this request
     *   PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse setupEndpoint(String                  serverName,
                                      String                  userId,
                                      String                  connectionGUID,
                                      String                  endpointGUID,
                                      boolean                 assetManagerIsHome,
                                      boolean                 forLineage,
                                      boolean                 forDuplicateProcessing,
                                      RelationshipRequestBody requestBody)
    {
        final String methodName = "setupEndpoint";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                ConnectionExchangeHandler handler = instanceHandler.getConnectionExchangeHandler(userId, serverName, methodName);

                handler.setupEndpoint(userId,
                                      requestBody.getAssetManagerGUID(),
                                      requestBody.getAssetManagerName(),
                                      assetManagerIsHome,
                                      connectionGUID,
                                      endpointGUID,
                                      requestBody.getProperties(),
                                      forLineage,
                                      forDuplicateProcessing,
                                      requestBody.getEffectiveTime(),
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
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param connectionGUID unique identifier of the connection in the external asset manager
     * @param endpointGUID unique identifier of the endpoint in the external asset manager
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties to store
     *
     * @return void or
     *   InvalidParameterException  one of the parameters is invalid
     *   UserNotAuthorizedException the user is not authorized to issue this request
     *   PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse clearEndpoint(String                        serverName,
                                      String                        userId,
                                      String                        connectionGUID,
                                      String                        endpointGUID,
                                      boolean                       forLineage,
                                      boolean                       forDuplicateProcessing,
                                      EffectiveTimeQueryRequestBody requestBody)
    {
        final String methodName = "clearEndpoint";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                ConnectionExchangeHandler handler = instanceHandler.getConnectionExchangeHandler(userId, serverName, methodName);

                handler.clearEndpoint(userId,
                                      requestBody.getAssetManagerGUID(),
                                      requestBody.getAssetManagerName(),
                                      connectionGUID,
                                      endpointGUID,
                                      forLineage,
                                      forDuplicateProcessing,
                                      requestBody.getEffectiveTime(),
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
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param connectionGUID unique identifier of the virtual connection in the external asset manager
     * @param embeddedConnectionGUID unique identifier of the embedded connection in the external asset manager
     * @param assetManagerIsHome ensure that only the asset manager can update this relationship
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties to store
     *
     * @return void or
     *   InvalidParameterException  one of the parameters is invalid
     *   UserNotAuthorizedException the user is not authorized to issue this request
     *   PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse setupEmbeddedConnection(String                  serverName,
                                                String                  userId,
                                                String                  connectionGUID,
                                                String                  embeddedConnectionGUID,
                                                boolean                 assetManagerIsHome,
                                                boolean                 forLineage,
                                                boolean                 forDuplicateProcessing,
                                                RelationshipRequestBody requestBody)
    {
        final String methodName = "setupEmbeddedConnection";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                ConnectionExchangeHandler handler = instanceHandler.getConnectionExchangeHandler(userId, serverName, methodName);

                if (requestBody.getProperties() instanceof EmbeddedConnectionProperties)
                {
                    handler.setupEmbeddedConnection(userId,
                                                    requestBody.getAssetManagerGUID(),
                                                    requestBody.getAssetManagerName(),
                                                    assetManagerIsHome,
                                                    connectionGUID,
                                                    embeddedConnectionGUID,
                                                    (EmbeddedConnectionProperties) requestBody.getProperties(),
                                                    forLineage,
                                                    forDuplicateProcessing,
                                                    requestBody.getEffectiveTime(),
                                                    methodName);
                }
                else
                {
                    handler.setupEmbeddedConnection(userId,
                                                    requestBody.getAssetManagerGUID(),
                                                    requestBody.getAssetManagerName(),
                                                    assetManagerIsHome,
                                                    connectionGUID,
                                                    embeddedConnectionGUID,
                                                    null,
                                                    forLineage,
                                                    forDuplicateProcessing,
                                                    requestBody.getEffectiveTime(),
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
     * Remove a relationship between a virtual connection and an embedded connection.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param connectionGUID unique identifier of the virtual connection in the external asset manager
     * @param embeddedConnectionGUID unique identifier of the embedded connection in the external asset manager
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties to store
     *
     * @return void or
     *   InvalidParameterException  one of the parameters is invalid
     *   UserNotAuthorizedException the user is not authorized to issue this request
     *   PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse clearEmbeddedConnection(String                        serverName,
                                                String                        userId,
                                                String                        connectionGUID,
                                                String                        embeddedConnectionGUID,
                                                boolean                       forLineage,
                                                boolean                       forDuplicateProcessing,
                                                EffectiveTimeQueryRequestBody requestBody)
    {
        final String methodName = "clearEmbeddedConnection";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                ConnectionExchangeHandler handler = instanceHandler.getConnectionExchangeHandler(userId, serverName, methodName);

                handler.clearEmbeddedConnection(userId,
                                                requestBody.getAssetManagerGUID(),
                                                requestBody.getAssetManagerName(),
                                                connectionGUID,
                                                embeddedConnectionGUID,
                                                forLineage,
                                                forDuplicateProcessing,
                                                requestBody.getEffectiveTime(),
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
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param assetManagerIsHome ensure that only the asset manager can update this relationship
     * @param assetGUID unique identifier of the asset
     * @param connectionGUID unique identifier of the  connection
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties to store
     *
     * @return void or
     *   InvalidParameterException  one of the parameters is invalid
     *   UserNotAuthorizedException the user is not authorized to issue this request
     *   PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse setupAssetConnection(String                  serverName,
                                             String                  userId,
                                             boolean                 assetManagerIsHome,
                                             String                  assetGUID,
                                             String                  connectionGUID,
                                             boolean                 forLineage,
                                             boolean                 forDuplicateProcessing,
                                             RelationshipRequestBody requestBody)
    {
        final String methodName                  = "setupAssetConnection";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                ConnectionExchangeHandler handler = instanceHandler.getConnectionExchangeHandler(userId, serverName, methodName);

                if (requestBody.getProperties() instanceof AssetConnectionProperties)
                {
                    handler.setupAssetConnection(userId,
                                                 requestBody.getAssetManagerGUID(),
                                                 requestBody.getAssetManagerName(),
                                                 assetManagerIsHome,
                                                 assetGUID,
                                                 connectionGUID,
                                                 (AssetConnectionProperties) requestBody.getProperties(),
                                                 forLineage,
                                                 forDuplicateProcessing,
                                                 requestBody.getEffectiveTime(),
                                                 methodName);
                }
                else
                {
                    handler.setupAssetConnection(userId,
                                                 requestBody.getAssetManagerGUID(),
                                                 requestBody.getAssetManagerName(),
                                                 assetManagerIsHome,
                                                 assetGUID,
                                                 connectionGUID,
                                                 null,
                                                 forLineage,
                                                 forDuplicateProcessing,
                                                 requestBody.getEffectiveTime(),
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
     * Remove a relationship between an asset and its connection.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param assetGUID unique identifier of the asset
     * @param connectionGUID unique identifier of the connection
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties to store
     *
     * @return void or
     *   InvalidParameterException  one of the parameters is invalid
     *   UserNotAuthorizedException the user is not authorized to issue this request
     *   PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse clearAssetConnection(String                        serverName,
                                             String                        userId,
                                             String                        assetGUID,
                                             String                        connectionGUID,
                                             boolean                       forLineage,
                                             boolean                       forDuplicateProcessing,
                                             EffectiveTimeQueryRequestBody requestBody)
    {
        final String methodName = "clearAssetConnection";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                ConnectionExchangeHandler handler = instanceHandler.getConnectionExchangeHandler(userId, serverName, methodName);

                handler.clearAssetConnection(userId,
                                             requestBody.getAssetManagerGUID(),
                                             requestBody.getAssetManagerName(),
                                             assetGUID,
                                             connectionGUID,
                                             forLineage,
                                             forDuplicateProcessing,
                                             requestBody.getEffectiveTime(),
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
     * Remove the metadata element representing a connection.  This will delete the connection and all anchored
     * elements such as schema and comments.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param connectionGUID unique identifier of the metadata element to remove
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties for query
     *
     * @return void or
     *   InvalidParameterException  one of the parameters is invalid
     *   UserNotAuthorizedException the user is not authorized to issue this request
     *   PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse removeConnection(String            serverName,
                                         String            userId,
                                         String            connectionGUID,
                                         boolean           forLineage,
                                         boolean           forDuplicateProcessing,
                                         UpdateRequestBody requestBody)
    {
        final String methodName = "removeConnection";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                ConnectionExchangeHandler handler = instanceHandler.getConnectionExchangeHandler(userId, serverName, methodName);

                handler.removeConnection(userId,
                                         requestBody.getMetadataCorrelationProperties(),
                                         connectionGUID,
                                         forLineage,
                                         forDuplicateProcessing,
                                         requestBody.getEffectiveTime(),
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
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties for query
     *
     * @return list of matching metadata elements or
     *   InvalidParameterException  one of the parameters is invalid
     *   UserNotAuthorizedException the user is not authorized to issue this request
     *   PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public ConnectionsResponse findConnections(String                  serverName,
                                               String                  userId,
                                               int                     startFrom,
                                               int                     pageSize,
                                               boolean                 forLineage,
                                               boolean                 forDuplicateProcessing,
                                               SearchStringRequestBody requestBody)
    {
        final String methodName = "findConnections";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ConnectionsResponse response = new ConnectionsResponse();
        AuditLog            auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                ConnectionExchangeHandler handler = instanceHandler.getConnectionExchangeHandler(userId, serverName, methodName);

                response.setElementList(handler.findConnections(userId,
                                                                requestBody.getAssetManagerGUID(),
                                                                requestBody.getAssetManagerName(),
                                                                requestBody.getSearchString(),
                                                                startFrom,
                                                                pageSize,
                                                                forLineage,
                                                                forDuplicateProcessing,
                                                                requestBody.getEffectiveTime(),
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
     * Retrieve the list of connection metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties for query
     *
     * @return list of matching metadata elements or
     *   InvalidParameterException  one of the parameters is invalid
     *   UserNotAuthorizedException the user is not authorized to issue this request
     *   PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public ConnectionsResponse getConnectionsByName(String          serverName,
                                                    String          userId,
                                                    int             startFrom,
                                                    int             pageSize,
                                                    boolean         forLineage,
                                                    boolean         forDuplicateProcessing,
                                                    NameRequestBody requestBody)
    {
        final String methodName = "getConnectionsByName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ConnectionsResponse response = new ConnectionsResponse();
        AuditLog            auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                ConnectionExchangeHandler handler = instanceHandler.getConnectionExchangeHandler(userId, serverName, methodName);

                response.setElementList(handler.getConnectionsByName(userId,
                                                                     requestBody.getAssetManagerGUID(),
                                                                     requestBody.getAssetManagerName(),
                                                                     requestBody.getName(),
                                                                     startFrom,
                                                                     pageSize,
                                                                     forLineage,
                                                                     forDuplicateProcessing,
                                                                     requestBody.getEffectiveTime(),
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
     * Retrieve the list of connections created on behalf of the named asset manager.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties for query
     *
     * @return list of matching metadata elements or
     *   InvalidParameterException  one of the parameters is invalid
     *   UserNotAuthorizedException the user is not authorized to issue this request
     *   PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public ConnectionsResponse getConnectionsForAssetManager(String                        serverName,
                                                             String                        userId,
                                                             int                           startFrom,
                                                             int                           pageSize,
                                                             boolean                       forLineage,
                                                             boolean                       forDuplicateProcessing,
                                                             EffectiveTimeQueryRequestBody requestBody)
    {
        final String methodName = "getConnectionsForAssetManager";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ConnectionsResponse response = new ConnectionsResponse();
        AuditLog            auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                ConnectionExchangeHandler handler = instanceHandler.getConnectionExchangeHandler(userId, serverName, methodName);

                response.setElementList(handler.getConnectionsForAssetManager(userId,
                                                                              requestBody.getAssetManagerGUID(),
                                                                              requestBody.getAssetManagerName(),
                                                                              startFrom,
                                                                              pageSize,
                                                                              forLineage,
                                                                              forDuplicateProcessing,
                                                                              requestBody.getEffectiveTime(),
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
     * Retrieve the connection metadata element with the supplied unique identifier.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param connectionGUID unique identifier of the requested metadata element
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties for query
     *
     * @return matching metadata element or
     *   InvalidParameterException  one of the parameters is invalid
     *   UserNotAuthorizedException the user is not authorized to issue this request
     *   PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public ConnectionResponse getConnectionByGUID(String                        serverName,
                                                  String                        userId,
                                                  String                        connectionGUID,
                                                  boolean                       forLineage,
                                                  boolean                       forDuplicateProcessing,
                                                  EffectiveTimeQueryRequestBody requestBody)
    {
        final String methodName = "getConnectionByGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ConnectionResponse response = new ConnectionResponse();
        AuditLog           auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                ConnectionExchangeHandler handler = instanceHandler.getConnectionExchangeHandler(userId, serverName, methodName);

                response.setElement(handler.getConnectionByGUID(userId,
                                                                requestBody.getAssetManagerGUID(),
                                                                requestBody.getAssetManagerName(),
                                                                connectionGUID,
                                                                forLineage,
                                                                forDuplicateProcessing,
                                                                requestBody.getEffectiveTime(),
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
     * Create a new metadata element to represent a network endpoint.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param assetManagerIsHome ensure that only the asset manager can update this endpoint
     * @param requestBody properties to store
     *
     * @return unique identifier of the new metadata element or
     *   InvalidParameterException  one of the parameters is invalid
     *   UserNotAuthorizedException the user is not authorized to issue this request
     *   PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GUIDResponse createEndpoint(String              serverName,
                                       String              userId,
                                       boolean             assetManagerIsHome,
                                       EndpointRequestBody requestBody)
    {
        final String methodName = "createEndpoint";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                ConnectionExchangeHandler handler = instanceHandler.getConnectionExchangeHandler(userId, serverName, methodName);

                response.setGUID(handler.createEndpoint(userId,
                                                        requestBody.getMetadataCorrelationProperties(),
                                                        assetManagerIsHome,
                                                        requestBody.getElementProperties(),
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
     * Create a new metadata element to represent a network endpoint using an existing metadata element as a template.
     * The template defines additional classifications and relationships that should be added to the new endpoint.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param assetManagerIsHome ensure that only the asset manager can update this endpoint
     * @param templateGUID unique identifier of the metadata element to copy
     * @param requestBody properties to store
     *
     * @return unique identifier of the new metadata element or
     *   InvalidParameterException  one of the parameters is invalid
     *   UserNotAuthorizedException the user is not authorized to issue this request
     *   PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GUIDResponse createEndpointFromTemplate(String              serverName,
                                                   String              userId,
                                                   String              templateGUID,
                                                   boolean             assetManagerIsHome,
                                                   TemplateRequestBody requestBody)
    {
        final String methodName = "createEndpointFromTemplate";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                ConnectionExchangeHandler handler = instanceHandler.getConnectionExchangeHandler(userId, serverName, methodName);

                response.setGUID(handler.createEndpointFromTemplate(userId,
                                                                    requestBody.getMetadataCorrelationProperties(),
                                                                    assetManagerIsHome,
                                                                    templateGUID,
                                                                    requestBody.getElementProperties(),
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
     * Update the metadata element representing an asset.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param endpointGUID unique identifier of the metadata element to update
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties to store
     *
     * @return void or
     *   InvalidParameterException  one of the parameters is invalid
     *   UserNotAuthorizedException the user is not authorized to issue this request
     *   PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse updateEndpoint(String              serverName,
                                       String              userId,
                                       String              endpointGUID,
                                       boolean             isMergeUpdate,
                                       boolean             forLineage,
                                       boolean             forDuplicateProcessing,
                                       EndpointRequestBody requestBody)
    {
        final String methodName = "updateEndpoint";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                ConnectionExchangeHandler handler = instanceHandler.getConnectionExchangeHandler(userId, serverName, methodName);

                handler.updateEndpoint(userId,
                                       requestBody.getMetadataCorrelationProperties(),
                                       endpointGUID,
                                       isMergeUpdate,
                                       requestBody.getElementProperties(),
                                       forLineage,
                                       forDuplicateProcessing,
                                       requestBody.getEffectiveTime(),
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
     * Remove the metadata element representing a network endpoint.  This will delete the endpoint and all anchored
     * elements such as schema and comments.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param endpointGUID unique identifier of the metadata element to remove
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties for query
     *
     * @return void or
     *   InvalidParameterException  one of the parameters is invalid
     *   UserNotAuthorizedException the user is not authorized to issue this request
     *   PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse removeEndpoint(String            serverName,
                                       String            userId,
                                       String            endpointGUID,
                                       boolean           forLineage,
                                       boolean           forDuplicateProcessing,
                                       UpdateRequestBody requestBody)
    {
        final String methodName = "removeEndpoint";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                ConnectionExchangeHandler handler = instanceHandler.getConnectionExchangeHandler(userId, serverName, methodName);

                handler.removeEndpoint(userId,
                                       requestBody.getMetadataCorrelationProperties(),
                                       endpointGUID,
                                       forLineage,
                                       forDuplicateProcessing,
                                       requestBody.getEffectiveTime(),
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
     * Retrieve the list of network endpoint metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties for query
     *
     * @return list of matching metadata elements or
     *   InvalidParameterException  one of the parameters is invalid
     *   UserNotAuthorizedException the user is not authorized to issue this request
     *   PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public EndpointsResponse findEndpoints(String                  serverName,
                                           String                  userId,
                                           int                     startFrom,
                                           int                     pageSize,
                                           boolean                 forLineage,
                                           boolean                 forDuplicateProcessing,
                                           SearchStringRequestBody requestBody)
    {
        final String methodName = "findEndpoints";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        EndpointsResponse response = new EndpointsResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                ConnectionExchangeHandler handler = instanceHandler.getConnectionExchangeHandler(userId, serverName, methodName);

                response.setElementList(handler.findEndpoints(userId,
                                                              requestBody.getAssetManagerGUID(),
                                                              requestBody.getAssetManagerName(),
                                                              requestBody.getSearchString(),
                                                              startFrom,
                                                              pageSize,
                                                              forLineage,
                                                              forDuplicateProcessing,
                                                              requestBody.getEffectiveTime(),
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
     * Retrieve the list of network endpoint metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties for query
     *
     * @return list of matching metadata elements or
     *   InvalidParameterException  one of the parameters is invalid
     *   UserNotAuthorizedException the user is not authorized to issue this request
     *   PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public EndpointsResponse getEndpointsByName(String          serverName,
                                                String          userId,
                                                int             startFrom,
                                                int             pageSize,
                                                boolean         forLineage,
                                                boolean         forDuplicateProcessing,
                                                NameRequestBody requestBody)
    {
        final String methodName        = "getEndpointsByName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        EndpointsResponse response = new EndpointsResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                ConnectionExchangeHandler handler = instanceHandler.getConnectionExchangeHandler(userId, serverName, methodName);

                response.setElementList(handler.getEndpointsByName(userId,
                                                                   requestBody.getAssetManagerGUID(),
                                                                   requestBody.getAssetManagerName(),
                                                                   requestBody.getName(),
                                                                   startFrom,
                                                                   pageSize,
                                                                   forLineage,
                                                                   forDuplicateProcessing,
                                                                   requestBody.getEffectiveTime(),
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
     * Retrieve the list of endpoints created on behalf of the named asset manager.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties to store
     *
     * @return list of matching metadata elements or
     *
     *   InvalidParameterException  one of the parameters is invalid
     *   UserNotAuthorizedException the user is not authorized to issue this request
     *   PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public EndpointsResponse getEndpointsForAssetManager(String                        serverName,
                                                         String                        userId,
                                                         int                           startFrom,
                                                         int                           pageSize,
                                                         boolean                       forLineage,
                                                         boolean                       forDuplicateProcessing,
                                                         EffectiveTimeQueryRequestBody requestBody)
    {
        final String methodName = "getEndpointsForAssetManager";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        EndpointsResponse response = new EndpointsResponse();
        AuditLog          auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                ConnectionExchangeHandler handler = instanceHandler.getConnectionExchangeHandler(userId, serverName, methodName);

                response.setElementList(handler.getEndpointsForAssetManager(userId,
                                                                            requestBody.getAssetManagerGUID(),
                                                                            requestBody.getAssetManagerName(),
                                                                            startFrom,
                                                                            pageSize,
                                                                            forLineage,
                                                                            forDuplicateProcessing,
                                                                            requestBody.getEffectiveTime(),
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
     * Retrieve the network endpoint metadata element with the supplied unique identifier.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param endpointGUID unique identifier of the requested metadata element
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties to store
     *
     * @return matching metadata element or
     *   InvalidParameterException  one of the parameters is invalid
     *   UserNotAuthorizedException the user is not authorized to issue this request
     *   PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public EndpointResponse getEndpointByGUID(String                        serverName,
                                              String                        userId,
                                              String                        endpointGUID,
                                              boolean                       forLineage,
                                              boolean                       forDuplicateProcessing,
                                              EffectiveTimeQueryRequestBody requestBody)
    {
        final String methodName = "getEndpointByGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        EndpointResponse response = new EndpointResponse();
        AuditLog         auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                ConnectionExchangeHandler handler = instanceHandler.getConnectionExchangeHandler(userId, serverName, methodName);

                response.setElement(handler.getEndpointByGUID(userId,
                                                              requestBody.getAssetManagerGUID(),
                                                              requestBody.getAssetManagerName(),
                                                              endpointGUID,
                                                              forLineage,
                                                              forDuplicateProcessing,
                                                              requestBody.getEffectiveTime(),
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
     * Create a new metadata element to represent the root of an asset.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param assetManagerIsHome ensure that only the asset manager can update this asset
     * @param requestBody properties to store
     *
     * @return unique identifier of the new metadata element or
     *   InvalidParameterException  one of the parameters is invalid
     *   UserNotAuthorizedException the user is not authorized to issue this request
     *   PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GUIDResponse createConnectorType(String                   serverName,
                                            String                   userId,
                                            boolean                  assetManagerIsHome,
                                            ConnectorTypeRequestBody requestBody)
    {
        final String methodName = "createConnectorType";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                ConnectionExchangeHandler handler = instanceHandler.getConnectionExchangeHandler(userId, serverName, methodName);

                response.setGUID(handler.createConnectorType(userId,
                                                             requestBody.getMetadataCorrelationProperties(),
                                                             assetManagerIsHome,
                                                             requestBody.getElementProperties(),
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
     * Create a new metadata element to represent an asset using an existing metadata element as a template.
     * The template defines additional classifications and relationships that should be added to the new asset.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param templateGUID unique identifier of the metadata element to copy
     * @param assetManagerIsHome ensure that only the asset manager can update this asset
     * @param requestBody properties to store
     *
     * @return unique identifier of the new metadata element or
     *   InvalidParameterException  one of the parameters is invalid
     *   UserNotAuthorizedException the user is not authorized to issue this request
     *   PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GUIDResponse createConnectorTypeFromTemplate(String              serverName,
                                                        String              userId,
                                                        String              templateGUID,
                                                        boolean             assetManagerIsHome,
                                                        TemplateRequestBody requestBody)
    {
        final String methodName = "createConnectorTypeFromTemplate";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                ConnectionExchangeHandler handler = instanceHandler.getConnectionExchangeHandler(userId, serverName, methodName);

                response.setGUID(handler.createConnectorTypeFromTemplate(userId,
                                                                         requestBody.getMetadataCorrelationProperties(),
                                                                         assetManagerIsHome,
                                                                         templateGUID,
                                                                         requestBody.getElementProperties(),
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
     * Update the metadata element representing an asset.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param connectorTypeGUID unique identifier of the metadata element to update
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties to store
     *
     * @return void or
     *   InvalidParameterException  one of the parameters is invalid
     *   UserNotAuthorizedException the user is not authorized to issue this request
     *   PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse updateConnectorType(String                   serverName,
                                            String                   userId,
                                            String                   connectorTypeGUID,
                                            boolean                  isMergeUpdate,
                                            boolean                  forLineage,
                                            boolean                  forDuplicateProcessing,
                                            ConnectorTypeRequestBody requestBody)
    {
        final String methodName = "updateConnectorType";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                ConnectionExchangeHandler handler = instanceHandler.getConnectionExchangeHandler(userId, serverName, methodName);

                handler.updateConnectorType(userId,
                                       requestBody.getMetadataCorrelationProperties(),
                                       connectorTypeGUID,
                                       isMergeUpdate,
                                       requestBody.getElementProperties(),
                                       forLineage,
                                       forDuplicateProcessing,
                                       requestBody.getEffectiveTime(),
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
     * Remove the metadata element representing an asset.  This will delete the asset and all anchored
     * elements such as schema and comments.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param connectorTypeGUID unique identifier of the metadata element to remove
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties to store
     *
     * @return void or
     *   InvalidParameterException  one of the parameters is invalid
     *   UserNotAuthorizedException the user is not authorized to issue this request
     *   PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse removeConnectorType(String            serverName,
                                            String            userId,
                                            String            connectorTypeGUID,
                                            boolean           forLineage,
                                            boolean           forDuplicateProcessing,
                                            UpdateRequestBody requestBody)
    {
        final String methodName = "removeConnectorType";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                ConnectionExchangeHandler handler = instanceHandler.getConnectionExchangeHandler(userId, serverName, methodName);

                handler.removeConnectorType(userId,
                                            requestBody.getMetadataCorrelationProperties(),
                                            connectorTypeGUID,
                                            forLineage,
                                            forDuplicateProcessing,
                                            requestBody.getEffectiveTime(),
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
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties for query
     *
     * @return list of matching metadata elements or
     *   InvalidParameterException  one of the parameters is invalid
     *   UserNotAuthorizedException the user is not authorized to issue this request
     *   PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public ConnectorTypesResponse findConnectorTypes(String                  serverName,
                                                     String                  userId,
                                                     int                     startFrom,
                                                     int                     pageSize,
                                                     boolean                 forLineage,
                                                     boolean                 forDuplicateProcessing,
                                                     SearchStringRequestBody requestBody)
    {
        final String methodName = "findConnectorTypes";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ConnectorTypesResponse response = new ConnectorTypesResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                ConnectionExchangeHandler handler = instanceHandler.getConnectionExchangeHandler(userId, serverName, methodName);

                response.setElementList(handler.findConnectorTypes(userId,
                                                                   requestBody.getAssetManagerGUID(),
                                                                   requestBody.getAssetManagerName(),
                                                                   requestBody.getSearchString(),
                                                                   startFrom,
                                                                   pageSize,
                                                                   forLineage,
                                                                   forDuplicateProcessing,
                                                                   requestBody.getEffectiveTime(),
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
     * Retrieve the list of connector type metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties to store
     *
     * @return list of matching metadata elements or
     *   InvalidParameterException  one of the parameters is invalid
     *   UserNotAuthorizedException the user is not authorized to issue this request
     *   PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public ConnectorTypesResponse getConnectorTypesByName(String          serverName,
                                                          String          userId,
                                                          int             startFrom,
                                                          int             pageSize,
                                                          boolean         forLineage,
                                                          boolean         forDuplicateProcessing,
                                                          NameRequestBody requestBody)
    {
        final String methodName = "getConnectorTypesByName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ConnectorTypesResponse response = new ConnectorTypesResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                ConnectionExchangeHandler handler = instanceHandler.getConnectionExchangeHandler(userId, serverName, methodName);

                response.setElementList(handler.getConnectorTypesByName(userId,
                                                                        requestBody.getAssetManagerGUID(),
                                                                        requestBody.getAssetManagerName(),
                                                                        requestBody.getName(),
                                                                        startFrom,
                                                                        pageSize,
                                                                        forLineage,
                                                                        forDuplicateProcessing,
                                                                        requestBody.getEffectiveTime(),
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
     * Retrieve the list of assets created on behalf of the named asset manager.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties for query
     *
     * @return list of matching metadata elements or
     *   InvalidParameterException  one of the parameters is invalid
     *   UserNotAuthorizedException the user is not authorized to issue this request
     *   PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public ConnectorTypesResponse getConnectorTypesForAssetManager(String                        serverName,
                                                                   String                        userId,
                                                                   int                           startFrom,
                                                                   int                           pageSize,
                                                                   boolean                       forLineage,
                                                                   boolean                       forDuplicateProcessing,
                                                                   EffectiveTimeQueryRequestBody requestBody)
    {
        final String methodName = "getConnectorTypesForAssetManager";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ConnectorTypesResponse response = new ConnectorTypesResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                ConnectionExchangeHandler handler = instanceHandler.getConnectionExchangeHandler(userId, serverName, methodName);

                response.setElementList(handler.getConnectorTypesForAssetManager(userId,
                                                                                 requestBody.getAssetManagerGUID(),
                                                                                 requestBody.getAssetManagerName(),
                                                                                 startFrom,
                                                                                 pageSize,
                                                                                 forLineage,
                                                                                 forDuplicateProcessing,
                                                                                 requestBody.getEffectiveTime(),
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
     * Retrieve the connector type metadata element with the supplied unique identifier.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param connectorTypeGUID unique identifier of the requested metadata element
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties for query
     *
     * @return matching metadata element or
     *   InvalidParameterException  one of the parameters is invalid
     *   UserNotAuthorizedException the user is not authorized to issue this request
     *   PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public ConnectorTypeResponse getConnectorTypeByGUID(String                        serverName,
                                                        String                        userId,
                                                        String                        connectorTypeGUID,
                                                        boolean                       forLineage,
                                                        boolean                       forDuplicateProcessing,
                                                        EffectiveTimeQueryRequestBody requestBody)
    {
        final String methodName = "getConnectorTypeByGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ConnectorTypeResponse response = new ConnectorTypeResponse();
        AuditLog              auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                ConnectionExchangeHandler handler = instanceHandler.getConnectionExchangeHandler(userId, serverName, methodName);

                response.setElement(handler.getConnectorTypeByGUID(userId,
                                                                   requestBody.getAssetManagerGUID(),
                                                                   requestBody.getAssetManagerName(),
                                                                   connectorTypeGUID,
                                                                   forLineage,
                                                                   forDuplicateProcessing,
                                                                   requestBody.getEffectiveTime(),
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
}
