/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.assetmanager.client.exchange;

import org.odpi.openmetadata.accessservices.assetmanager.api.exchange.ConnectionExchangeInterface;
import org.odpi.openmetadata.accessservices.assetmanager.client.rest.AssetManagerRESTClient;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.ConnectionElement;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.ConnectorTypeElement;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.EndpointElement;
import org.odpi.openmetadata.accessservices.assetmanager.properties.*;
import org.odpi.openmetadata.accessservices.assetmanager.rest.*;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

import java.util.Date;
import java.util.List;


/**
 * ConnectionExchangeClient is the client for managing Connections, Endpoints and ConnectorTypes.
 */
public class ConnectionExchangeClient extends ExchangeClientBase implements ConnectionExchangeInterface
{
    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param auditLog logging destination
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public ConnectionExchangeClient(String   serverName,
                                    String   serverPlatformURLRoot,
                                    AuditLog auditLog) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, auditLog);
    }


    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public ConnectionExchangeClient(String serverName,
                                    String serverPlatformURLRoot) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot);
    }


    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param userId caller's userId embedded in all HTTP requests
     * @param password caller's userId embedded in all HTTP requests
     * @param auditLog logging destination
     *
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public ConnectionExchangeClient(String   serverName,
                                    String   serverPlatformURLRoot,
                                    String   userId,
                                    String   password,
                                    AuditLog auditLog) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, userId, password, auditLog);
    }


    /**
     * Create a new client that is going to be used in an OMAG Server.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param restClient client that issues the REST API calls
     * @param maxPageSize maximum number of results supported by this server
     * @param auditLog logging destination
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public ConnectionExchangeClient(String                 serverName,
                                    String                 serverPlatformURLRoot,
                                    AssetManagerRESTClient restClient,
                                    int                    maxPageSize,
                                    AuditLog               auditLog) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, restClient, maxPageSize, auditLog);
    }


    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param userId caller's userId embedded in all HTTP requests
     * @param password caller's userId embedded in all HTTP requests
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public ConnectionExchangeClient(String serverName,
                                    String serverPlatformURLRoot,
                                    String userId,
                                    String password) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, userId, password);
    }


    /* ======================================================================================
     * The Connection entity is the top level element to describe a connection.
     */

    /**
     * Create a new metadata element to represent the root of a connection.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param assetManagerIsHome ensure that only the asset manager can update this connection
     * @param externalIdentifierProperties optional properties used to define an external identifier
     * @param connectionProperties properties to store
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public String createConnection(String                       userId,
                                   String                       assetManagerGUID,
                                   String                       assetManagerName,
                                   boolean                      assetManagerIsHome,
                                   ExternalIdentifierProperties externalIdentifierProperties,
                                   ConnectionProperties         connectionProperties) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException
    {
        final String methodName                  = "createConnection";
        final String propertiesParameterName     = "connectionProperties";
        final String qualifiedNameParameterName  = "connectionProperties.qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateObject(connectionProperties, propertiesParameterName, methodName);
        invalidParameterHandler.validateName(connectionProperties.getQualifiedName(), qualifiedNameParameterName, methodName);

        ConnectionRequestBody requestBody = new ConnectionRequestBody();
        requestBody.setElementProperties(connectionProperties);
        requestBody.setMetadataCorrelationProperties(this.getCorrelationProperties(assetManagerGUID,
                                                                                   assetManagerName,
                                                                                   externalIdentifierProperties,
                                                                                   methodName));

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/connections?assetManagerIsHome={2}";

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  urlTemplate,
                                                                  requestBody,
                                                                  serverName,
                                                                  userId,
                                                                  assetManagerIsHome);

        return restResult.getGUID();
    }


    /**
     * Create a new metadata element to represent a connection using an existing metadata element as a template.
     * The template defines additional classifications and relationships that should be added to the new connection.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param assetManagerIsHome ensure that only the asset manager can update this connection
     * @param externalIdentifierProperties optional properties used to define an external identifier
     * @param templateGUID unique identifier of the metadata element to copy
     * @param templateProperties properties that override the template
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public String createConnectionFromTemplate(String                       userId,
                                               String                       assetManagerGUID,
                                               String                       assetManagerName,
                                               boolean                      assetManagerIsHome,
                                               String                       templateGUID,
                                               ExternalIdentifierProperties externalIdentifierProperties,
                                               TemplateProperties           templateProperties) throws InvalidParameterException,
                                                                                                       UserNotAuthorizedException,
                                                                                                       PropertyServerException
    {
        final String methodName                  = "createConnectionFromTemplate";
        final String templateGUIDParameterName   = "templateGUID";
        final String propertiesParameterName     = "templateProperties";
        final String qualifiedNameParameterName  = "templateProperties.qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(templateGUID, templateGUIDParameterName, methodName);
        invalidParameterHandler.validateObject(templateProperties, propertiesParameterName, methodName);
        invalidParameterHandler.validateName(templateProperties.getQualifiedName(), qualifiedNameParameterName, methodName);

        TemplateRequestBody requestBody = new TemplateRequestBody();
        requestBody.setElementProperties(templateProperties);
        requestBody.setMetadataCorrelationProperties(this.getCorrelationProperties(assetManagerGUID,
                                                                                   assetManagerName,
                                                                                   externalIdentifierProperties,
                                                                                   methodName));

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/connections/from-template/{2}?assetManagerIsHome={3}";

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  urlTemplate,
                                                                  requestBody,
                                                                  serverName,
                                                                  userId,
                                                                  templateGUID,
                                                                  assetManagerIsHome);

        return restResult.getGUID();
    }


    /**
     * Update the metadata element representing an asset.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param connectionGUID unique identifier of the metadata element to update
     * @param connectionExternalIdentifier unique identifier of the connection in the external asset manager
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param connectionProperties new properties for this element
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void updateConnection(String               userId,
                                 String               assetManagerGUID,
                                 String               assetManagerName,
                                 String               connectionGUID,
                                 String               connectionExternalIdentifier,
                                 boolean              isMergeUpdate,
                                 ConnectionProperties connectionProperties,
                                 Date                 effectiveTime,
                                 boolean              forLineage,
                                 boolean              forDuplicateProcessing) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        final String methodName                  = "updateConnection";
        final String connectionGUIDParameterName = "connectionGUID";
        final String propertiesParameterName     = "connectionProperties";
        final String qualifiedNameParameterName  = "connectionProperties.qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(connectionGUID, connectionGUIDParameterName, methodName);
        invalidParameterHandler.validateObject(connectionProperties, propertiesParameterName, methodName);
        invalidParameterHandler.validateName(connectionProperties.getQualifiedName(), qualifiedNameParameterName, methodName);

        ConnectionRequestBody requestBody = new ConnectionRequestBody();
        requestBody.setElementProperties(connectionProperties);
        requestBody.setMetadataCorrelationProperties(this.getCorrelationProperties(assetManagerGUID,
                                                                                   assetManagerName,
                                                                                   connectionExternalIdentifier,
                                                                                   methodName));
        requestBody.setEffectiveTime(effectiveTime);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/connections/{2}?isMergeUpdate={3}&forLineage={4}&forDuplicateProcessing={5}";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        connectionGUID,
                                        isMergeUpdate,
                                        forLineage,
                                        forDuplicateProcessing);
    }


    /**
     * Create a relationship between a connection and a connector type.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param assetManagerIsHome ensure that only the asset manager can update this relationship
     * @param connectionGUID unique identifier of the connection in the external asset manager
     * @param connectorTypeGUID unique identifier of the connector type in the external asset manager
     * @param effectiveFrom the date when this element is active - null for active now
     * @param effectiveTo the date when this element becomes inactive - null for active until deleted
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void setupConnectorType(String  userId,
                                   String  assetManagerGUID,
                                   String  assetManagerName,
                                   boolean assetManagerIsHome,
                                   String  connectionGUID,
                                   String  connectorTypeGUID,
                                   Date    effectiveFrom,
                                   Date    effectiveTo,
                                   Date    effectiveTime,
                                   boolean forLineage,
                                   boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                          UserNotAuthorizedException,
                                                                          PropertyServerException
    {
        final String methodName                     = "setupConnectorType";
        final String connectionGUIDParameterName    = "connectionGUID";
        final String connectorTypeGUIDParameterName = "connectorTypeGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(connectionGUID, connectionGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(connectorTypeGUID, connectorTypeGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/connections/{2}/connector-types/{3}?assetManagerIsHome={4}&forLineage={5}&forDuplicateProcessing={6}";

        RelationshipProperties properties = new RelationshipProperties();
        properties.setEffectiveFrom(effectiveFrom);
        properties.setEffectiveTo(effectiveTo);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        getRelationshipRequestBody(assetManagerGUID, assetManagerName, effectiveTime, properties),
                                        serverName,
                                        userId,
                                        connectionGUID,
                                        connectorTypeGUID,
                                        assetManagerIsHome,
                                        forLineage,
                                        forDuplicateProcessing);
    }


    /**
     * Remove a relationship between a connection and a connector type.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param connectionGUID unique identifier of the connection in the external asset manager
     * @param connectorTypeGUID unique identifier of the connector type in the external asset manager
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void clearConnectorType(String  userId,
                                   String  assetManagerGUID,
                                   String  assetManagerName,
                                   String  connectionGUID,
                                   String  connectorTypeGUID,
                                   Date    effectiveTime,
                                   boolean forLineage,
                                   boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                          UserNotAuthorizedException,
                                                                          PropertyServerException
    {
        final String methodName                     = "clearConnectorType";
        final String connectionGUIDParameterName    = "connectionGUID";
        final String connectorTypeGUIDParameterName = "connectorTypeGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(connectionGUID, connectionGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(connectorTypeGUID, connectorTypeGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/connections/{2}/connector-types/{3}/remove?forLineage={4}&forDuplicateProcessing={5}";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        getEffectiveTimeQueryRequestBody(assetManagerGUID, assetManagerName, effectiveTime),
                                        serverName,
                                        userId,
                                        connectionGUID,
                                        connectorTypeGUID,
                                        forLineage,
                                        forDuplicateProcessing);
    }


    /**
     * Create a relationship between a connection and an endpoint.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param assetManagerIsHome ensure that only the asset manager can update this relationship
     * @param connectionGUID unique identifier of the connection in the external asset manager
     * @param endpointGUID unique identifier of the endpoint in the external asset manager
     * @param effectiveFrom the date when this element is active - null for active now
     * @param effectiveTo the date when this element becomes inactive - null for active until deleted
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void setupEndpoint(String  userId,
                              String  assetManagerGUID,
                              String  assetManagerName,
                              boolean assetManagerIsHome,
                              String  connectionGUID,
                              String  endpointGUID,
                              Date    effectiveFrom,
                              Date    effectiveTo,
                              Date    effectiveTime,
                              boolean forLineage,
                              boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                     UserNotAuthorizedException,
                                                                     PropertyServerException
    {
        final String methodName                  = "setupEndpoint";
        final String connectionGUIDParameterName = "connectionGUID";
        final String endpointGUIDParameterName   = "endpointGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(connectionGUID, connectionGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(endpointGUID, endpointGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/connections/{2}/endpoints/{3}?assetManagerIsHome={4}&forLineage={5}&forDuplicateProcessing={6}";

        RelationshipProperties properties = new RelationshipProperties();
        properties.setEffectiveFrom(effectiveFrom);
        properties.setEffectiveTo(effectiveTo);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        getRelationshipRequestBody(assetManagerGUID, assetManagerName, effectiveTime, properties),
                                        serverName,
                                        userId,
                                        connectionGUID,
                                        endpointGUID,
                                        assetManagerIsHome,
                                        forLineage,
                                        forDuplicateProcessing);
    }


    /**
     * Remove a relationship between a connection and an endpoint.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param connectionGUID unique identifier of the connection in the external asset manager
     * @param endpointGUID unique identifier of the endpoint in the external asset manager
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void clearEndpoint(String  userId,
                              String  assetManagerGUID,
                              String  assetManagerName,
                              String  connectionGUID,
                              String  endpointGUID,
                              Date    effectiveTime,
                              boolean forLineage,
                              boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                     UserNotAuthorizedException,
                                                                     PropertyServerException
    {
        final String methodName                  = "clearEndpoint";
        final String connectionGUIDParameterName = "connectionGUID";
        final String endpointGUIDParameterName   = "endpointGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(connectionGUID, connectionGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(endpointGUID, endpointGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/connections/{2}/endpoints/{3}/remove?forLineage={4}&forDuplicateProcessing={5}";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        getEffectiveTimeQueryRequestBody(assetManagerGUID, assetManagerName, effectiveTime),
                                        serverName,
                                        userId,
                                        connectionGUID,
                                        endpointGUID,
                                        forLineage,
                                        forDuplicateProcessing);
    }


    /**
     * Create a relationship between a virtual connection and an embedded connection.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param assetManagerIsHome ensure that only the asset manager can update this relationship
     * @param connectionGUID unique identifier of the virtual connection in the external asset manager
     * @param embeddedConnectionGUID unique identifier of the embedded connection in the external asset manager
     * @param properties properties describing how to use the embedded connection such as: Which order should this connection be processed;
     * What additional properties should be passed to the embedded connector via the configuration properties;
     * What does this connector signify?
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void setupEmbeddedConnection(String                       userId,
                                        String                       assetManagerGUID,
                                        String                       assetManagerName,
                                        boolean                      assetManagerIsHome,
                                        String                       connectionGUID,
                                        String                       embeddedConnectionGUID,
                                        EmbeddedConnectionProperties properties,
                                        Date                         effectiveTime,
                                        boolean                      forLineage,
                                        boolean                      forDuplicateProcessing) throws InvalidParameterException,
                                                                                                    UserNotAuthorizedException,
                                                                                                    PropertyServerException
    {
        final String methodName                          = "setupEmbeddedConnection";
        final String connectionGUIDParameterName         = "connectionGUID";
        final String embeddedConnectionGUIDParameterName = "embeddedConnectionGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(connectionGUID, connectionGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(embeddedConnectionGUID, embeddedConnectionGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/connections/{2}/embedded-connections/{3}?assetManagerIsHome={4}&forLineage={5}&forDuplicateProcessing={6}";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        getRelationshipRequestBody(assetManagerGUID, assetManagerName, effectiveTime, properties),
                                        serverName,
                                        userId,
                                        connectionGUID,
                                        embeddedConnectionGUID,
                                        assetManagerIsHome,
                                        forLineage,
                                        forDuplicateProcessing);
    }


    /**
     * Remove a relationship between a virtual connection and an embedded connection.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param connectionGUID unique identifier of the virtual connection in the external asset manager
     * @param embeddedConnectionGUID unique identifier of the embedded connection in the external asset manager
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void clearEmbeddedConnection(String  userId,
                                        String  assetManagerGUID,
                                        String  assetManagerName,
                                        String  connectionGUID,
                                        String  embeddedConnectionGUID,
                                        Date    effectiveTime,
                                        boolean forLineage,
                                        boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        final String methodName                          = "clearEmbeddedConnection";
        final String connectionGUIDParameterName         = "connectionGUID";
        final String embeddedConnectionGUIDParameterName = "embeddedConnectionGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(connectionGUID, connectionGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(embeddedConnectionGUID, embeddedConnectionGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/connections/{2}/embedded-connections/{3}/remove?forLineage={4}&forDuplicateProcessing={5}";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        getEffectiveTimeQueryRequestBody(assetManagerGUID, assetManagerName, effectiveTime),
                                        serverName,
                                        userId,
                                        connectionGUID,
                                        embeddedConnectionGUID,
                                        forLineage,
                                        forDuplicateProcessing);
    }


    /**
     * Create a relationship between an asset and its connection.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param assetManagerIsHome ensure that only the asset manager can update this relationship
     * @param assetGUID unique identifier of the asset
     * @param properties summary of the asset that is stored in the relationship between the asset and the connection.
     * @param connectionGUID unique identifier of the  connection
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void setupAssetConnection(String                    userId,
                                     String                    assetManagerGUID,
                                     String                    assetManagerName,
                                     boolean                   assetManagerIsHome,
                                     String                    assetGUID,
                                     String                    connectionGUID,
                                     AssetConnectionProperties properties,
                                     Date                      effectiveTime,
                                     boolean                   forLineage,
                                     boolean                   forDuplicateProcessing) throws InvalidParameterException,
                                                                                              UserNotAuthorizedException,
                                                                                              PropertyServerException
    {
        final String methodName                  = "setupAssetConnection";
        final String assetGUIDParameterName      = "assetGUID";
        final String connectionGUIDParameterName = "connectionGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(assetGUID, assetGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(connectionGUID, connectionGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/data-assets/{2}/connections/{3}?assetManagerIsHome={4}&forLineage={5}&forDuplicateProcessing={6}";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        getRelationshipRequestBody(assetManagerGUID, assetManagerName, effectiveTime, properties),
                                        serverName,
                                        userId,
                                        assetGUID,
                                        connectionGUID,
                                        assetManagerIsHome,
                                        forLineage,
                                        forDuplicateProcessing);
    }


    /**
     * Remove a relationship between an asset and its connection.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param assetGUID unique identifier of the asset
     * @param connectionGUID unique identifier of the connection
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void clearAssetConnection(String  userId,
                                     String  assetManagerGUID,
                                     String  assetManagerName,
                                     String  assetGUID,
                                     String  connectionGUID,
                                     Date    effectiveTime,
                                     boolean forLineage,
                                     boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException
    {
        final String methodName                  = "clearAssetConnection";
        final String assetGUIDParameterName      = "assetGUID";
        final String connectionGUIDParameterName = "connectionGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(assetGUID, assetGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(connectionGUID, connectionGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/data-assets/{2}/connections/{3}/remove?forLineage={4}&forDuplicateProcessing={5}";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        getEffectiveTimeQueryRequestBody(assetManagerGUID, assetManagerName, effectiveTime),
                                        serverName,
                                        userId,
                                        assetGUID,
                                        connectionGUID,
                                        forLineage,
                                        forDuplicateProcessing);
    }


    /**
     * Remove the metadata element representing a connection.  This will delete the connection and all anchored
     * elements such as schema and comments.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param connectionGUID unique identifier of the metadata element to remove
     * @param connectionExternalIdentifier unique identifier of the connection in the external asset manager
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void removeConnection(String  userId,
                                 String  assetManagerGUID,
                                 String  assetManagerName,
                                 String  connectionGUID,
                                 String  connectionExternalIdentifier,
                                 Date    effectiveTime,
                                 boolean forLineage,
                                 boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException
    {
        final String methodName                  = "removeConnection";
        final String connectionGUIDParameterName = "connectionGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(connectionGUID, connectionGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/connections/{2}/remove?forLineage={3}&forDuplicateProcessing={4}";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        getUpdateRequestBody(assetManagerGUID, assetManagerName, connectionExternalIdentifier, effectiveTime, methodName),
                                        serverName,
                                        userId,
                                        connectionGUID,
                                        forLineage,
                                        forDuplicateProcessing);
    }



    /**
     * Retrieve the list of connection metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param searchString string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<ConnectionElement> findConnections(String  userId,
                                                   String  assetManagerGUID,
                                                   String  assetManagerName,
                                                   String  searchString,
                                                   int     startFrom,
                                                   int     pageSize,
                                                   Date    effectiveTime,
                                                   boolean forLineage,
                                                   boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException
    {
        final String methodName = "findConnections";

        invalidParameterHandler.validateUserId(userId, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/connections/by-search-string?startFrom={2}&pageSize={3}&forLineage={4}&forDuplicateProcessing={5}";

        ConnectionsResponse restResult = restClient.callConnectionsPostRESTCall(methodName,
                                                                                urlTemplate,
                                                                                getSearchStringRequestBody(assetManagerGUID, assetManagerName, searchString, effectiveTime, methodName),
                                                                                serverName,
                                                                                userId,
                                                                                startFrom,
                                                                                validatedPageSize,
                                                                                forLineage,
                                                                                forDuplicateProcessing);

        return restResult.getElementList();
    }


    /**
     * Retrieve the list of connection metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param name name to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<ConnectionElement> getConnectionsByName(String  userId,
                                                        String  assetManagerGUID,
                                                        String  assetManagerName,
                                                        String  name,
                                                        int     startFrom,
                                                        int     pageSize,
                                                        Date    effectiveTime,
                                                        boolean forLineage,
                                                        boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                                               UserNotAuthorizedException,
                                                                                               PropertyServerException
    {
        final String methodName = "getConnectionsByName";

        invalidParameterHandler.validateUserId(userId, methodName);

        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);


        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/connections/by-name?startFrom={2}&pageSize={3}&forLineage={4}&forDuplicateProcessing={5}";

        ConnectionsResponse restResult = restClient.callConnectionsPostRESTCall(methodName,
                                                                                urlTemplate,
                                                                                getNameRequestBody(assetManagerGUID, assetManagerName, name, effectiveTime, methodName),
                                                                                serverName,
                                                                                userId,
                                                                                startFrom,
                                                                                validatedPageSize,
                                                                                forLineage,
                                                                                forDuplicateProcessing);

        return restResult.getElementList();
    }


    /**
     * Retrieve the list of connections created on behalf of the named asset manager.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<ConnectionElement> getConnectionsForAssetManager(String  userId,
                                                                 String  assetManagerGUID,
                                                                 String  assetManagerName,
                                                                 int     startFrom,
                                                                 int     pageSize,
                                                                 Date    effectiveTime,
                                                                 boolean forLineage,
                                                                 boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                                                        UserNotAuthorizedException,
                                                                                                        PropertyServerException
    {
        final String methodName = "getConnectionsForAssetManager";
        final String assetManagerGUIDParameterName = "assetManagerGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(assetManagerGUID, assetManagerGUIDParameterName, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/connections/by-asset-manager?startFrom={2}&pageSize={3}&forLineage={4}&forDuplicateProcessing={5}";

        ConnectionsResponse restResult = restClient.callConnectionsPostRESTCall(methodName,
                                                                                urlTemplate,
                                                                                getEffectiveTimeQueryRequestBody(assetManagerGUID, assetManagerName, effectiveTime),
                                                                                serverName,
                                                                                userId,
                                                                                startFrom,
                                                                                validatedPageSize,
                                                                                forLineage,
                                                                                forDuplicateProcessing);

        return restResult.getElementList();
    }


    /**
     * Retrieve the connection metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param connectionGUID unique identifier of the requested metadata element
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return matching metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public ConnectionElement getConnectionByGUID(String  userId,
                                                 String  assetManagerGUID,
                                                 String  assetManagerName,
                                                 String  connectionGUID,
                                                 Date    effectiveTime,
                                                 boolean forLineage,
                                                 boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException
    {
        final String methodName = "getConnectionByGUID";
        final String guidParameterName = "connectionGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(connectionGUID, guidParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/connections/{2}/retrieve?forLineage={3}&forDuplicateProcessing={4}";

        ConnectionResponse restResult = restClient.callConnectionPostRESTCall(methodName,
                                                                              urlTemplate,
                                                                              getEffectiveTimeQueryRequestBody(assetManagerGUID, assetManagerName, effectiveTime),
                                                                              serverName,
                                                                              userId,
                                                                              connectionGUID,
                                                                              forLineage,
                                                                              forDuplicateProcessing);

        return restResult.getElement();
    }


    /**
     * Create a new metadata element to represent a network endpoint.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param assetManagerIsHome ensure that only the asset manager can update this endpoint
     * @param externalIdentifierProperties optional properties used to define an external identifier
     * @param endpointProperties properties to store
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public String createEndpoint(String                       userId,
                                 String                       assetManagerGUID,
                                 String                       assetManagerName,
                                 boolean                      assetManagerIsHome,
                                 ExternalIdentifierProperties externalIdentifierProperties,
                                 EndpointProperties           endpointProperties) throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException
    {
        final String methodName                  = "createEndpoint";
        final String propertiesParameterName     = "endpointProperties";
        final String qualifiedNameParameterName  = "endpointProperties.qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateObject(endpointProperties, propertiesParameterName, methodName);
        invalidParameterHandler.validateName(endpointProperties.getQualifiedName(), qualifiedNameParameterName, methodName);

        EndpointRequestBody requestBody = new EndpointRequestBody();
        requestBody.setElementProperties(endpointProperties);
        requestBody.setMetadataCorrelationProperties(this.getCorrelationProperties(assetManagerGUID,
                                                                                   assetManagerName,
                                                                                   externalIdentifierProperties,
                                                                                   methodName));

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/endpoints?assetManagerIsHome={2}";

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  urlTemplate,
                                                                  requestBody,
                                                                  serverName,
                                                                  userId,
                                                                  assetManagerIsHome);

        return restResult.getGUID();
    }


    /**
     * Create a new metadata element to represent a network endpoint using an existing metadata element as a template.
     * The template defines additional classifications and relationships that should be added to the new endpoint.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param assetManagerIsHome ensure that only the asset manager can update this endpoint
     * @param externalIdentifierProperties optional properties used to define an external identifier
     * @param templateGUID unique identifier of the metadata element to copy
     * @param templateProperties properties that override the template
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public String createEndpointFromTemplate(String                       userId,
                                             String                       assetManagerGUID,
                                             String                       assetManagerName,
                                             boolean                      assetManagerIsHome,
                                             String                       templateGUID,
                                             ExternalIdentifierProperties externalIdentifierProperties,
                                             TemplateProperties           templateProperties) throws InvalidParameterException,
                                                                                                     UserNotAuthorizedException,
                                                                                                     PropertyServerException
    {
        final String methodName                  = "createEndpointFromTemplate";
        final String templateGUIDParameterName   = "templateGUID";
        final String propertiesParameterName     = "templateProperties";
        final String qualifiedNameParameterName  = "templateProperties.qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(templateGUID, templateGUIDParameterName, methodName);
        invalidParameterHandler.validateObject(templateProperties, propertiesParameterName, methodName);
        invalidParameterHandler.validateName(templateProperties.getQualifiedName(), qualifiedNameParameterName, methodName);

        TemplateRequestBody requestBody = new TemplateRequestBody();
        requestBody.setElementProperties(templateProperties);
        requestBody.setMetadataCorrelationProperties(this.getCorrelationProperties(assetManagerGUID,
                                                                                   assetManagerName,
                                                                                   externalIdentifierProperties,
                                                                                   methodName));

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/endpoints/from-template/{2}?assetManagerIsHome={3}";

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  urlTemplate,
                                                                  requestBody,
                                                                  serverName,
                                                                  userId,
                                                                  templateGUID,
                                                                  assetManagerIsHome);

        return restResult.getGUID();
    }


    /**
     * Update the metadata element representing an asset.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param endpointGUID unique identifier of the metadata element to update
     * @param endpointExternalIdentifier unique identifier of the endpoint in the external asset manager
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param endpointProperties new properties for this element
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void updateEndpoint(String             userId,
                               String             assetManagerGUID,
                               String             assetManagerName,
                               String             endpointGUID,
                               String             endpointExternalIdentifier,
                               boolean            isMergeUpdate,
                               EndpointProperties endpointProperties,
                               Date               effectiveTime,
                               boolean            forLineage,
                               boolean            forDuplicateProcessing) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException
    {
        final String methodName                  = "updateEndpoint";
        final String endpointGUIDParameterName   = "endpointGUID";
        final String propertiesParameterName     = "endpointProperties";
        final String qualifiedNameParameterName  = "endpointProperties.qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(endpointGUID, endpointGUIDParameterName, methodName);
        invalidParameterHandler.validateObject(endpointProperties, propertiesParameterName, methodName);
        invalidParameterHandler.validateName(endpointProperties.getQualifiedName(), qualifiedNameParameterName, methodName);

        EndpointRequestBody requestBody = new EndpointRequestBody();
        requestBody.setElementProperties(endpointProperties);
        requestBody.setMetadataCorrelationProperties(this.getCorrelationProperties(assetManagerGUID,
                                                                                   assetManagerName,
                                                                                   endpointExternalIdentifier,
                                                                                   methodName));
        requestBody.setEffectiveTime(effectiveTime);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/endpoints/{2}?isMergeUpdate={3}&forLineage={4}&forDuplicateProcessing={5}";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        endpointGUID,
                                        isMergeUpdate,
                                        forLineage,
                                        forDuplicateProcessing);
    }


    /**
     * Remove the metadata element representing a network endpoint.  This will delete the endpoint and all anchored
     * elements such as schema and comments.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param endpointGUID unique identifier of the metadata element to remove
     * @param endpointExternalIdentifier unique identifier of the endpoint in the external asset manager
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void removeEndpoint(String  userId,
                               String  assetManagerGUID,
                               String  assetManagerName,
                               String  endpointGUID,
                               String  endpointExternalIdentifier,
                               Date    effectiveTime,
                               boolean forLineage,
                               boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException
    {
        final String methodName                     = "removeEndpoint";
        final String connectorTypeGUIDParameterName = "endpointGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(endpointGUID, connectorTypeGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/endpoints/{2}/remove?forLineage={3}&forDuplicateProcessing={4}";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        getUpdateRequestBody(assetManagerGUID, assetManagerName, endpointExternalIdentifier, effectiveTime, methodName),
                                        serverName,
                                        userId,
                                        endpointGUID,
                                        forLineage,
                                        forDuplicateProcessing);
    }


    /**
     * Retrieve the list of network endpoint metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param searchString string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<EndpointElement> findEndpoints(String  userId,
                                               String  assetManagerGUID,
                                               String  assetManagerName,
                                               String  searchString,
                                               int     startFrom,
                                               int     pageSize,
                                               Date    effectiveTime,
                                               boolean forLineage,
                                               boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException
    {
        final String methodName = "findEndpoints";

        invalidParameterHandler.validateUserId(userId, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/endpoints/by-search-string?startFrom={2}&pageSize={3}&forLineage={4}&forDuplicateProcessing={5}";

        EndpointsResponse restResult = restClient.callEndpointsPostRESTCall(methodName,
                                                                            urlTemplate,
                                                                            getSearchStringRequestBody(assetManagerGUID, assetManagerName, searchString, effectiveTime, methodName),
                                                                            serverName,
                                                                            userId,
                                                                            startFrom,
                                                                            validatedPageSize,
                                                                            forLineage,
                                                                            forDuplicateProcessing);

        return restResult.getElementList();
    }


    /**
     * Retrieve the list of network endpoint metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param name name to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<EndpointElement> getEndpointsByName(String  userId,
                                                    String  assetManagerGUID,
                                                    String  assetManagerName,
                                                    String  name,
                                                    int     startFrom,
                                                    int     pageSize,
                                                    Date    effectiveTime,
                                                    boolean forLineage,
                                                    boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException
    {
        final String methodName        = "getEndpointsByName";

        invalidParameterHandler.validateUserId(userId, methodName);

        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/endpoints/by-name?startFrom={2}&pageSize={3}&forLineage={4}&forDuplicateProcessing={5}";

        EndpointsResponse restResult = restClient.callEndpointsPostRESTCall(methodName,
                                                                            urlTemplate,
                                                                            getNameRequestBody(assetManagerGUID, assetManagerName, name, effectiveTime, methodName),
                                                                            serverName,
                                                                            userId,
                                                                            startFrom,
                                                                            validatedPageSize,
                                                                            forLineage,
                                                                            forDuplicateProcessing);

        return restResult.getElementList();
    }


    /**
     * Retrieve the list of endpoints created on behalf of the named asset manager.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<EndpointElement> getEndpointsForAssetManager(String  userId,
                                                             String  assetManagerGUID,
                                                             String  assetManagerName,
                                                             int     startFrom,
                                                             int     pageSize,
                                                             Date    effectiveTime,
                                                             boolean forLineage,
                                                             boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                                                    UserNotAuthorizedException,
                                                                                                    PropertyServerException
    {
        final String methodName = "getEndpointsForAssetManager";
        final String assetManagerGUIDParameterName = "assetManagerGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(assetManagerGUID, assetManagerGUIDParameterName, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/endpoints/by-asset-manager?startFrom={2}&pageSize={3}&forLineage={4}&forDuplicateProcessing={5}";

        EndpointsResponse restResult = restClient.callEndpointsPostRESTCall(methodName,
                                                                            urlTemplate,
                                                                            getEffectiveTimeQueryRequestBody(assetManagerGUID, assetManagerName, effectiveTime),
                                                                            serverName,
                                                                            userId,
                                                                            startFrom,
                                                                            validatedPageSize,
                                                                            forLineage,
                                                                            forDuplicateProcessing);

        return restResult.getElementList();
    }


    /**
     * Retrieve the network endpoint metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param endpointGUID unique identifier of the requested metadata element
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return matching metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public EndpointElement getEndpointByGUID(String  userId,
                                             String  assetManagerGUID,
                                             String  assetManagerName,
                                             String  endpointGUID,
                                             Date    effectiveTime,
                                             boolean forLineage,
                                             boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException
    {
        final String methodName = "getEndpointByGUID";
        final String guidParameterName = "endpointGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(endpointGUID, guidParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/endpoints/{2}/retrieve?forLineage={3}&forDuplicateProcessing={4}";

        EndpointResponse restResult = restClient.callEndpointPostRESTCall(methodName,
                                                                          urlTemplate,
                                                                          getEffectiveTimeQueryRequestBody(assetManagerGUID, assetManagerName, effectiveTime),
                                                                          serverName,
                                                                          userId,
                                                                          endpointGUID,
                                                                          forLineage,
                                                                          forDuplicateProcessing);

        return restResult.getElement();
    }


    /**
     * Create a new metadata element to represent the root of an asset.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param assetManagerIsHome ensure that only the asset manager can update this asset
     * @param externalIdentifierProperties optional properties used to define an external identifier
     * @param connectorTypeProperties properties to store
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public String createConnectorType(String                       userId,
                                      String                       assetManagerGUID,
                                      String                       assetManagerName,
                                      boolean                      assetManagerIsHome,
                                      ExternalIdentifierProperties externalIdentifierProperties,
                                      ConnectorTypeProperties      connectorTypeProperties) throws InvalidParameterException,
                                                                                                   UserNotAuthorizedException,
                                                                                                   PropertyServerException
    {
        final String methodName                  = "createConnectorType";
        final String propertiesParameterName     = "connectorTypeProperties";
        final String qualifiedNameParameterName  = "connectorTypeProperties.qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateObject(connectorTypeProperties, propertiesParameterName, methodName);
        invalidParameterHandler.validateName(connectorTypeProperties.getQualifiedName(), qualifiedNameParameterName, methodName);

        ConnectorTypeRequestBody requestBody = new ConnectorTypeRequestBody();
        requestBody.setElementProperties(connectorTypeProperties);
        requestBody.setMetadataCorrelationProperties(this.getCorrelationProperties(assetManagerGUID,
                                                                                   assetManagerName,
                                                                                   externalIdentifierProperties,
                                                                                   methodName));

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/connector-types?assetManagerIsHome={2}";

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  urlTemplate,
                                                                  requestBody,
                                                                  serverName,
                                                                  userId,
                                                                  assetManagerIsHome);

        return restResult.getGUID();
    }


    /**
     * Create a new metadata element to represent an asset using an existing metadata element as a template.
     * The template defines additional classifications and relationships that should be added to the new asset.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param assetManagerIsHome ensure that only the asset manager can update this asset
     * @param externalIdentifierProperties optional properties used to define an external identifier
     * @param templateGUID unique identifier of the metadata element to copy
     * @param templateProperties properties that override the template
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public String createConnectorTypeFromTemplate(String                       userId,
                                                  String                       assetManagerGUID,
                                                  String                       assetManagerName,
                                                  boolean                      assetManagerIsHome,
                                                  String                       templateGUID,
                                                  ExternalIdentifierProperties externalIdentifierProperties,
                                                  TemplateProperties           templateProperties) throws InvalidParameterException,
                                                                                                          UserNotAuthorizedException,
                                                                                                          PropertyServerException
    {
        final String methodName                  = "createConnectorTypeFromTemplate";
        final String templateGUIDParameterName   = "templateGUID";
        final String propertiesParameterName     = "templateProperties";
        final String qualifiedNameParameterName  = "templateProperties.qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(templateGUID, templateGUIDParameterName, methodName);
        invalidParameterHandler.validateObject(templateProperties, propertiesParameterName, methodName);
        invalidParameterHandler.validateName(templateProperties.getQualifiedName(), qualifiedNameParameterName, methodName);

        TemplateRequestBody requestBody = new TemplateRequestBody();
        requestBody.setElementProperties(templateProperties);
        requestBody.setMetadataCorrelationProperties(this.getCorrelationProperties(assetManagerGUID,
                                                                                   assetManagerName,
                                                                                   externalIdentifierProperties,
                                                                                   methodName));

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/connector-types/from-template/{2}?assetManagerIsHome={3}";

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  urlTemplate,
                                                                  requestBody,
                                                                  serverName,
                                                                  userId,
                                                                  templateGUID,
                                                                  assetManagerIsHome);

        return restResult.getGUID();
    }


    /**
     * Update the metadata element representing an asset.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param connectorTypeGUID unique identifier of the metadata element to update
     * @param connectorTypeExternalIdentifier unique identifier of the asset in the external asset manager
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param connectorTypeProperties new properties for this element
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void updateConnectorType(String                  userId,
                                    String                  assetManagerGUID,
                                    String                  assetManagerName,
                                    String                  connectorTypeGUID,
                                    String                  connectorTypeExternalIdentifier,
                                    boolean                 isMergeUpdate,
                                    ConnectorTypeProperties connectorTypeProperties,
                                    Date                    effectiveTime,
                                    boolean                 forLineage,
                                    boolean                 forDuplicateProcessing) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException
    {
        final String methodName                     = "updateConnectorType";
        final String connectorTypeGUIDParameterName = "connectorTypeGUID";
        final String propertiesParameterName        = "connectorTypeProperties";
        final String qualifiedNameParameterName     = "connectorTypeProperties.qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(connectorTypeGUID, connectorTypeGUIDParameterName, methodName);
        invalidParameterHandler.validateObject(connectorTypeProperties, propertiesParameterName, methodName);
        if (! isMergeUpdate)
        {
            invalidParameterHandler.validateName(connectorTypeProperties.getQualifiedName(), qualifiedNameParameterName, methodName);
        }

        ConnectorTypeRequestBody requestBody = new ConnectorTypeRequestBody();
        requestBody.setElementProperties(connectorTypeProperties);
        requestBody.setMetadataCorrelationProperties(this.getCorrelationProperties(assetManagerGUID,
                                                                                   assetManagerName,
                                                                                   connectorTypeExternalIdentifier,
                                                                                   methodName));
        requestBody.setEffectiveTime(effectiveTime);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/connector-types/{2}?isMergeUpdate={3}&forLineage={4}&forDuplicateProcessing={5}";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        connectorTypeGUID,
                                        isMergeUpdate,
                                        forLineage,
                                        forDuplicateProcessing);
    }


    /**
     * Remove the metadata element representing an asset.  This will delete the asset and all anchored
     * elements such as schema and comments.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param connectorTypeGUID unique identifier of the metadata element to remove
     * @param connectorTypeExternalIdentifier unique identifier of the asset in the external asset manager
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void removeConnectorType(String userId,
                                    String   assetManagerGUID,
                                    String  assetManagerName,
                                    String  connectorTypeGUID,
                                    String  connectorTypeExternalIdentifier,
                                    Date    effectiveTime,
                                    boolean forLineage,
                                    boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        final String methodName                       = "removeConnectorType";
        final String connectorTypeGUIDParameterName   = "connectorTypeGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(connectorTypeGUID, connectorTypeGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/connector-types/{2}/remove?forLineage={3}&forDuplicateProcessing={4}";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        getUpdateRequestBody(assetManagerGUID, assetManagerName, connectorTypeExternalIdentifier, effectiveTime, methodName),
                                        serverName,
                                        userId,
                                        connectorTypeGUID,
                                        forLineage,
                                        forDuplicateProcessing);
    }


    /**
     * Retrieve the list of connector type metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param searchString string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<ConnectorTypeElement> findConnectorTypes(String  userId,
                                                         String  assetManagerGUID,
                                                         String  assetManagerName,
                                                         String  searchString,
                                                         int     startFrom,
                                                         int     pageSize,
                                                         Date    effectiveTime,
                                                         boolean forLineage,
                                                         boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                                                UserNotAuthorizedException,
                                                                                                PropertyServerException
    {
        final String methodName = "findConnectorTypes";

        invalidParameterHandler.validateUserId(userId, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/connector-types/by-search-string?startFrom={2}&pageSize={3}&forLineage={4}&forDuplicateProcessing={5}";

        ConnectorTypesResponse restResult = restClient.callConnectorTypesPostRESTCall(methodName,
                                                                                      urlTemplate,
                                                                                      getSearchStringRequestBody(assetManagerGUID, assetManagerName, searchString, effectiveTime, methodName),
                                                                                      serverName,
                                                                                      userId,
                                                                                      startFrom,
                                                                                      validatedPageSize,
                                                                                      forLineage,
                                                                                      forDuplicateProcessing);

        return restResult.getElementList();
    }


    /**
     * Retrieve the list of connector type metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param name name to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<ConnectorTypeElement> getConnectorTypesByName(String  userId,
                                                              String  assetManagerGUID,
                                                              String  assetManagerName,
                                                              String  name,
                                                              int     startFrom,
                                                              int     pageSize,
                                                              Date    effectiveTime,
                                                              boolean forLineage,
                                                              boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                                                     UserNotAuthorizedException,
                                                                                                     PropertyServerException
    {
        final String methodName = "getConnectorTypesByName";

        invalidParameterHandler.validateUserId(userId, methodName);

        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/connector-types/by-name?startFrom={2}&pageSize={3}&forLineage={4}&forDuplicateProcessing={5}";

        ConnectorTypesResponse restResult = restClient.callConnectorTypesPostRESTCall(methodName,
                                                                                      urlTemplate,
                                                                                      getNameRequestBody(assetManagerGUID, assetManagerName, name,  effectiveTime, methodName),
                                                                                      serverName,
                                                                                      userId,
                                                                                      startFrom,
                                                                                      validatedPageSize,
                                                                                      forLineage,
                                                                                      forDuplicateProcessing);

        return restResult.getElementList();
    }


    /**
     * Retrieve the list of assets created on behalf of the named asset manager.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<ConnectorTypeElement> getConnectorTypesForAssetManager(String  userId,
                                                                       String  assetManagerGUID,
                                                                       String  assetManagerName,
                                                                       int     startFrom,
                                                                       int     pageSize,
                                                                       Date    effectiveTime,
                                                                       boolean forLineage,
                                                                       boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                                                              UserNotAuthorizedException,
                                                                                                              PropertyServerException
    {
        final String methodName = "getConnectorTypesForAssetManager";
        final String assetManagerGUIDParameterName = "assetManagerGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(assetManagerGUID, assetManagerGUIDParameterName, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/connector-types/by-asset-manager?startFrom={2}&pageSize={3}&forLineage={4}&forDuplicateProcessing={5}";

        ConnectorTypesResponse restResult = restClient.callConnectorTypesPostRESTCall(methodName,
                                                                                      urlTemplate,
                                                                                      getEffectiveTimeQueryRequestBody(assetManagerGUID, assetManagerName, effectiveTime),
                                                                                      serverName,
                                                                                      userId,
                                                                                      startFrom,
                                                                                      validatedPageSize,
                                                                                      forLineage,
                                                                                      forDuplicateProcessing);

        return restResult.getElementList();
    }


    /**
     * Retrieve the connector type metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param openMetadataGUID unique identifier of the requested metadata element
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return matching metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public ConnectorTypeElement getConnectorTypeByGUID(String  userId,
                                                       String  assetManagerGUID,
                                                       String  assetManagerName,
                                                       String  openMetadataGUID,
                                                       Date    effectiveTime,
                                                       boolean forLineage,
                                                       boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                                              UserNotAuthorizedException,
                                                                                              PropertyServerException
    {
        final String methodName = "getConnectorTypeByGUID";
        final String guidParameterName = "openMetadataGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(openMetadataGUID, guidParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/connector-types/{2}/retrieve?forLineage={3}&forDuplicateProcessing={4}";

        ConnectorTypeResponse restResult = restClient.callConnectorTypePostRESTCall(methodName,
                                                                                    urlTemplate,
                                                                                    getEffectiveTimeQueryRequestBody(assetManagerGUID, assetManagerName, effectiveTime),
                                                                                    serverName,
                                                                                    userId,
                                                                                    openMetadataGUID,
                                                                                    forLineage,
                                                                                    forDuplicateProcessing);

        return restResult.getElement();
    }
}
