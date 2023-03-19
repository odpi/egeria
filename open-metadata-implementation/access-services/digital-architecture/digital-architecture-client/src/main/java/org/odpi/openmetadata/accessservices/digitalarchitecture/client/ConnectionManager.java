/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.digitalarchitecture.client;

import org.odpi.openmetadata.accessservices.digitalarchitecture.api.ManageConnections;
import org.odpi.openmetadata.accessservices.digitalarchitecture.client.rest.DigitalArchitectureRESTClient;
import org.odpi.openmetadata.accessservices.digitalarchitecture.metadataelements.ConnectionElement;
import org.odpi.openmetadata.accessservices.digitalarchitecture.metadataelements.EndpointElement;
import org.odpi.openmetadata.accessservices.digitalarchitecture.metadataelements.ConnectorTypeElement;
import org.odpi.openmetadata.accessservices.digitalarchitecture.properties.ConnectionProperties;
import org.odpi.openmetadata.accessservices.digitalarchitecture.properties.EndpointProperties;
import org.odpi.openmetadata.accessservices.digitalarchitecture.properties.ConnectorTypeProperties;
import org.odpi.openmetadata.accessservices.digitalarchitecture.properties.TemplateProperties;
import org.odpi.openmetadata.accessservices.digitalarchitecture.rest.*;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.NameRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.SearchStringRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.StringRequestBody;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

import java.util.List;
import java.util.Map;

/**
 * ConnectionManager provides the API operations to create and maintain connection definitions and their related objects.
 * The client supports the following types of objects
 *
 * <ul>
 *     <li>Connections - the connections used to create connector instances that can access the connection.</li>
 *     <li>ConnectorTypes - description of a specific that can be used to access the connection.</li>
 *     <li>ConnectorCategories - the network information needed to access the connection.</li>
 *     <li>Endpoints - the network information needed to access the connection.</li>
 * </ul>*/
public class ConnectionManager extends DigitalArchitectureClientBase implements ManageConnections
{
    /**
     * Create a new client with no authentication embedded in the HTTP request and an audit log.
     *
     * @param serverName            name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param auditLog              logging destination
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     *                                   REST API calls.
     */
    public ConnectionManager(String   serverName,
                             String   serverPlatformURLRoot,
                             AuditLog auditLog) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, auditLog);
    }


    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName            name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     *                                   REST API calls.
     */
    public ConnectionManager(String serverName,
                             String serverPlatformURLRoot) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot);
    }


    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     * There is also an audit log destination.
     *
     * @param serverName            name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param userId                caller's userId embedded in all HTTP requests
     * @param password              caller's userId embedded in all HTTP requests
     * @param auditLog              logging destination
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     *                                   REST API calls.
     */
    public ConnectionManager(String   serverName,
                             String   serverPlatformURLRoot,
                             String   userId,
                             String   password,
                             AuditLog auditLog) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, userId, password, auditLog);
    }


    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param serverName            name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param userId                caller's userId embedded in all HTTP requests
     * @param password              caller's userId embedded in all HTTP requests
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     *                                   REST API calls.
     */
    public ConnectionManager(String serverName,
                             String serverPlatformURLRoot,
                             String userId,
                             String password) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, userId, password);
    }


    /**
     * Create a new client that is going to be used in an OMAG Server (view service or integration service typically).
     *
     * @param serverName            name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param restClient            client that issues the REST API calls
     * @param maxPageSize           maximum number of results supported by this server
     * @param auditLog              logging destination
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     *                                   REST API calls.
     */
    public ConnectionManager(String                        serverName,
                             String                        serverPlatformURLRoot,
                             DigitalArchitectureRESTClient restClient,
                             int                           maxPageSize,
                             AuditLog                      auditLog) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, restClient, maxPageSize, auditLog);
    }


    /*
     * ==============================================
     * ManageConnections
     * ==============================================
     */


    /**
     * Create a new metadata element to represent a connection. Classifications can be added later to define the
     * type of connection.
     *
     * @param userId             calling user
     * @param connectionProperties properties to store
     * @return unique identifier of the new metadata element
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public String createConnection(String               userId,
                                   ConnectionProperties connectionProperties) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        final String methodName = "createConnection";
        final String nameParameter = "qualifiedName";
        final String propertiesParameter = "connectionProperties";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateObject(connectionProperties, propertiesParameter, methodName);
        invalidParameterHandler.validateName(connectionProperties.getQualifiedName(), nameParameter, methodName);

        final String   urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/digital-architecture/users/{1}/connections";

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  urlTemplate,
                                                                  connectionProperties,
                                                                  serverName,
                                                                  userId);

        return restResult.getGUID();
    }


    /**
     * Create a new metadata element to represent a connection using an existing metadata element as a template.
     * The template defines additional classifications and relationships that should be added to the new connection.
     *
     * @param userId             calling user
     * @param templateGUID       unique identifier of the metadata element to copy
     * @param templateProperties properties that override the template
     * @return unique identifier of the new metadata element
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public String createConnectionFromTemplate(String             userId,
                                               String             templateGUID,
                                               TemplateProperties templateProperties) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException
    {
        final String methodName = "createConnectionFromTemplate";
        final String nameParameter = "qualifiedName";
        final String propertiesParameter = "templateProperties";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateObject(templateProperties, propertiesParameter, methodName);
        invalidParameterHandler.validateName(templateProperties.getQualifiedName(), nameParameter, methodName);

        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/digital-architecture/users/{1}/connections/from-template/{2}";

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  urlTemplate,
                                                                  templateProperties,
                                                                  serverName,
                                                                  userId,
                                                                  templateGUID);

        return restResult.getGUID();
    }


    /**
     * Update the metadata element representing a connection.
     *
     * @param userId             calling user
     * @param connectionGUID       unique identifier of the metadata element to update
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param connectionProperties new properties for this element
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void updateConnection(String               userId,
                                 String               connectionGUID,
                                 boolean              isMergeUpdate,
                                 ConnectionProperties connectionProperties) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException
    {
        final String methodName = "updateConnection";
        final String guidParameter = "connectionGUID";
        final String propertiesParameter = "connectionProperties";
        final String qualifiedNameParameter = "connectionProperties.qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(connectionGUID, guidParameter, methodName);
        invalidParameterHandler.validateObject(connectionProperties, propertiesParameter, methodName);

        if (!isMergeUpdate)
        {
            invalidParameterHandler.validateName(connectionProperties.getQualifiedName(), qualifiedNameParameter, methodName);
        }

        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/digital-architecture/users/{1}/connections/{2}/update?isMergeUpdate={3}";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        connectionProperties,
                                        serverName,
                                        userId,
                                        connectionGUID,
                                        isMergeUpdate);
    }


    /**
     * Create a relationship between a connection and a connector type.
     *
     * @param userId calling user
     * @param connectionGUID unique identifier of the connection in the external asset manager
     * @param connectorTypeGUID unique identifier of the connector type in the external asset manager
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void setupConnectorType(String userId,
                                   String connectionGUID,
                                   String connectorTypeGUID) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException
    {
        final String methodName = "setupConnectorType";
        final String connectionGUIDParameter = "connectionGUID";
        final String connectorTypeGUIDParameter = "connectorTypeGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(connectionGUID, connectionGUIDParameter, methodName);
        invalidParameterHandler.validateGUID(connectorTypeGUID, connectorTypeGUIDParameter, methodName);

        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/digital-architecture/users/{1}/connections/{2}/connector-types/{3}";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        nullRequestBody,
                                        serverName,
                                        userId,
                                        connectionGUID,
                                        connectorTypeGUID);
    }


    /**
     * Remove a relationship between a connection and a connector type.
     *
     * @param userId calling user
     * @param connectionGUID unique identifier of the connection in the external asset manager
     * @param connectorTypeGUID unique identifier of the connector type in the external asset manager
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void clearConnectorType(String userId,
                                   String connectionGUID,
                                   String connectorTypeGUID) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException
    {
        final String methodName = "clearConnectorType";
        final String connectionGUIDParameter = "connectionGUID";
        final String connectorTypeGUIDParameter = "connectorTypeGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(connectionGUID, connectionGUIDParameter, methodName);
        invalidParameterHandler.validateGUID(connectorTypeGUID, connectorTypeGUIDParameter, methodName);

        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/digital-architecture/users/{1}/connections/{2}/connector-types/{3}/delete";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        nullRequestBody,
                                        serverName,
                                        userId,
                                        connectionGUID,
                                        connectorTypeGUID);
    }


    /**
     * Create a relationship between a connection and an endpoint.
     *
     * @param userId calling user
     * @param connectionGUID unique identifier of the connection in the external asset manager
     * @param endpointGUID unique identifier of the endpoint in the external asset manager
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void setupEndpoint(String userId,
                              String connectionGUID,
                              String endpointGUID) throws InvalidParameterException,
                                                          UserNotAuthorizedException,
                                                          PropertyServerException
    {
        final String methodName = "setupEndpoint";
        final String connectionGUIDParameter = "connectionGUID";
        final String endpointGUIDParameter = "endpointGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(connectionGUID, connectionGUIDParameter, methodName);
        invalidParameterHandler.validateGUID(endpointGUID, endpointGUIDParameter, methodName);

        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/digital-architecture/users/{1}/connections/{2}/endpoints/{3}";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        nullRequestBody,
                                        serverName,
                                        userId,
                                        connectionGUID,
                                        endpointGUID);
    }


    /**
     * Remove a relationship between a connection and an endpoint.
     *
     * @param userId calling user
     * @param connectionGUID unique identifier of the connection in the external asset manager
     * @param endpointGUID unique identifier of the endpoint in the external asset manager
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void clearEndpoint(String userId,
                              String connectionGUID,
                              String endpointGUID) throws InvalidParameterException,
                                                          UserNotAuthorizedException,
                                                          PropertyServerException
    {
        final String methodName = "clearEndpoint";
        final String connectionGUIDParameter = "connectionGUID";
        final String endpointGUIDParameter = "endpointGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(connectionGUID, connectionGUIDParameter, methodName);
        invalidParameterHandler.validateGUID(endpointGUID, endpointGUIDParameter, methodName);

        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/digital-architecture/users/{1}/connections/{2}/endpoints/{3}/delete";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        nullRequestBody,
                                        serverName,
                                        userId,
                                        connectionGUID,
                                        endpointGUID);
    }


    /**
     * Create a relationship between a virtual connection and an embedded connection.
     *
     * @param userId calling user
     * @param connectionGUID unique identifier of the virtual connection in the external asset manager
     * @param position which order should this connection be processed
     * @param arguments What additional properties should be passed to the embedded connector via the configuration properties
     * @param displayName what does this connector signify?
     * @param embeddedConnectionGUID unique identifier of the embedded connection in the external asset manager
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void setupEmbeddedConnection(String              userId,
                                        String              connectionGUID,
                                        int                 position,
                                        String              displayName,
                                        Map<String, Object> arguments,
                                        String              embeddedConnectionGUID) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException
    {
        final String methodName = "setupEmbeddedConnection";
        final String connectionGUIDParameter = "connectionGUID";
        final String embeddedConnectionGUIDParameter = "embeddedConnectionGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(connectionGUID, connectionGUIDParameter, methodName);
        invalidParameterHandler.validateGUID(embeddedConnectionGUID, embeddedConnectionGUIDParameter, methodName);

        EmbeddedConnectionRequestBody requestBody = new EmbeddedConnectionRequestBody();

        requestBody.setPosition(position);
        requestBody.setDisplayName(displayName);
        requestBody.setArguments(arguments);

        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/digital-architecture/users/{1}/connections/{2}/embedded-connections/{3}";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        connectionGUID,
                                        embeddedConnectionGUID);
    }


    /**
     * Remove a relationship between a virtual connection and an embedded connection.
     *
     * @param userId calling user
     * @param connectionGUID unique identifier of the virtual connection in the external asset manager
     * @param embeddedConnectionGUID unique identifier of the embedded connection in the external asset manager
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void clearEmbeddedConnection(String userId,
                                        String connectionGUID,
                                        String embeddedConnectionGUID) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException
    {
        final String methodName = "clearEmbeddedConnection";
        final String connectionGUIDParameter = "connectionGUID";
        final String embeddedConnectionGUIDParameter = "embeddedConnectionGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(connectionGUID, connectionGUIDParameter, methodName);
        invalidParameterHandler.validateGUID(embeddedConnectionGUID, embeddedConnectionGUIDParameter, methodName);

        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/digital-architecture/users/{1}/connections/{2}/embedded-connections/{3}/delete";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        nullRequestBody,
                                        serverName,
                                        userId,
                                        connectionGUID,
                                        embeddedConnectionGUID);
    }


    /**
     * Create a relationship between an asset and its connection.
     *
     * @param userId calling user
     * @param assetGUID unique identifier of the asset
     * @param assetSummary summary of the asset that is stored in the relationship between the asset and the connection.
     * @param connectionGUID unique identifier of the connection
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void setupAssetConnection(String userId,
                                     String assetGUID,
                                     String assetSummary,
                                     String connectionGUID) throws InvalidParameterException,
                                                                   UserNotAuthorizedException,
                                                                   PropertyServerException
    {
        final String methodName = "setupAssetConnection";
        final String assetGUIDParameter = "assetGUID";
        final String connectionGUIDParameter = "connectionGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(assetGUID, assetGUIDParameter, methodName);
        invalidParameterHandler.validateGUID(connectionGUID, connectionGUIDParameter, methodName);

        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/digital-architecture/users/{1}/assets/{2}/connections/{3}";

        StringRequestBody requestBody = new StringRequestBody();

        requestBody.setString(assetSummary);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        assetGUID,
                                        connectionGUID);
    }


    /**
     * Remove a relationship between an asset and its connection.
     *
     * @param userId calling user
     * @param assetGUID unique identifier of the asset
     * @param connectionGUID unique identifier of the connection
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void clearAssetConnection(String userId,
                                     String assetGUID,
                                     String connectionGUID) throws InvalidParameterException,
                                                                   UserNotAuthorizedException,
                                                                   PropertyServerException
    {
        final String methodName = "clearAssetConnection";
        final String assetGUIDParameter = "assetGUID";
        final String connectionGUIDParameter = "connectionGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(assetGUID, assetGUIDParameter, methodName);
        invalidParameterHandler.validateGUID(connectionGUID, connectionGUIDParameter, methodName);

        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/digital-architecture/users/{1}/assets/{2}/connections/{3}/delete";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        nullRequestBody,
                                        serverName,
                                        userId,
                                        assetGUID,
                                        connectionGUID);
    }


    /**
     * Remove the metadata element representing a connection.
     *
     * @param userId calling user
     * @param connectionGUID unique identifier of the metadata element to remove
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void removeConnection(String userId,
                                 String connectionGUID) throws InvalidParameterException,
                                                               UserNotAuthorizedException,
                                                               PropertyServerException
    {
        final String   methodName = "removeConnection";
        final String   guidParameter = "connectionGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(connectionGUID, guidParameter, methodName);

        final String   urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/digital-architecture/users/{1}/connections/{2}/delete";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        nullRequestBody,
                                        serverName,
                                        userId,
                                        connectionGUID);
    }



    /**
     * Retrieve the list of connection metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param userId calling user
     * @param searchString string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<ConnectionElement> findConnections(String userId,
                                                   String searchString,
                                                   int    startFrom,
                                                   int    pageSize) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        final String methodName = "findConnections";
        final String parameterName = "searchString";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateSearchString(searchString, parameterName, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        final String   urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/digital-architecture/users/{1}/connections/by-search-string" +
                                             "?startFrom={2}&pageSize={3}";

        SearchStringRequestBody requestBody = new SearchStringRequestBody();

        requestBody.setSearchString(searchString);
        requestBody.setSearchStringParameterName(parameterName);

        ConnectionsResponse restResult = restClient.callConnectionsPostRESTCall(methodName,
                                                                                urlTemplate,
                                                                                requestBody,
                                                                                serverName,
                                                                                userId,
                                                                                startFrom,
                                                                                validatedPageSize);

        return restResult.getElementList();
    }


    /**
     * Retrieve the list of connection metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param userId calling user
     * @param name name to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<ConnectionElement> getConnectionsByName(String userId,
                                                        String name,
                                                        int    startFrom,
                                                        int    pageSize) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        final String methodName = "getConnectionsByName";
        final String nameParameter = "name";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(name, nameParameter, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        final String   urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/digital-architecture/users/{1}/connections/by-name?startFrom={2}&pageSize={3}";

        NameRequestBody requestBody = new NameRequestBody();

        requestBody.setName(name);
        requestBody.setNamePropertyName(nameParameter);

        ConnectionsResponse restResult = restClient.callConnectionsPostRESTCall(methodName,
                                                                                urlTemplate,
                                                                                requestBody,
                                                                                serverName,
                                                                                userId,
                                                                                startFrom,
                                                                                validatedPageSize);

        return restResult.getElementList();
    }


    /**
     * Retrieve the connection metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param connectionGUID unique identifier of the requested metadata element
     *
     * @return matching metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public ConnectionElement getConnectionByGUID(String userId,
                                                 String connectionGUID) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        final String   methodName = "getConnectionByGUID";
        final String   connectionGUIDParameter = "connectionGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(connectionGUID, connectionGUIDParameter, methodName);

        final String   urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/digital-architecture/users/{1}/connections/{2}";

        ConnectionResponse restResult = restClient.callConnectionGetRESTCall(methodName,
                                                                             urlTemplate,
                                                                             serverName,
                                                                             userId,
                                                                             connectionGUID);
        return restResult.getElement();
    }




    /**
     * Create a new metadata element to represent a endpoint. Classifications can be added later to define the
     * type of endpoint.
     *
     * @param userId             calling user
     * @param endpointProperties properties to store
     * @return unique identifier of the new metadata element
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public String createEndpoint(String             userId,
                                 EndpointProperties endpointProperties) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        final String methodName = "createEndpoint";
        final String nameParameter = "qualifiedName";
        final String propertiesParameter = "endpointProperties";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateObject(endpointProperties, propertiesParameter, methodName);
        invalidParameterHandler.validateName(endpointProperties.getQualifiedName(), nameParameter, methodName);

        final String   urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/digital-architecture/users/{1}/endpoints";

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  urlTemplate,
                                                                  endpointProperties,
                                                                  serverName,
                                                                  userId);

        return restResult.getGUID();
    }


    /**
     * Create a new metadata element to represent a endpoint using an existing metadata element as a template.
     * The template defines additional classifications and relationships that should be added to the new endpoint.
     *
     * @param userId             calling user
     * @param networkAddress     location of the endpoint
     * @param templateGUID       unique identifier of the metadata element to copy
     * @param templateProperties properties that override the template
     * @return unique identifier of the new metadata element
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public String createEndpointFromTemplate(String             userId,
                                             String             networkAddress,
                                             String             templateGUID,
                                             TemplateProperties templateProperties) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException
    {
        final String methodName = "createEndpointFromTemplate";
        final String nameParameter = "qualifiedName";
        final String propertiesParameter = "templateProperties";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateObject(templateProperties, propertiesParameter, methodName);
        invalidParameterHandler.validateName(templateProperties.getQualifiedName(), nameParameter, methodName);

        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/digital-architecture/users/{1}/endpoints/network-address/{2}/from-template/{3}";

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  urlTemplate,
                                                                  templateProperties,
                                                                  serverName,
                                                                  userId,
                                                                  networkAddress,
                                                                  templateGUID);

        return restResult.getGUID();
    }


    /**
     * Update the metadata element representing a endpoint.
     *
     * @param userId             calling user
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param endpointGUID       unique identifier of the metadata element to update
     * @param endpointProperties new properties for this element
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void updateEndpoint(String             userId,
                               boolean            isMergeUpdate,
                               String             endpointGUID,
                               EndpointProperties endpointProperties) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException
    {
        final String methodName = "updateEndpoint";
        final String guidParameter = "endpointGUID";
        final String propertiesParameter = "endpointProperties";
        final String qualifiedNameParameter = "endpointProperties.qualifiedName";
        
        if (isMergeUpdate)
        {
            invalidParameterHandler.validateName(endpointProperties.getQualifiedName(), qualifiedNameParameter, methodName);
        }

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(endpointGUID, guidParameter, methodName);
        invalidParameterHandler.validateObject(endpointProperties, propertiesParameter, methodName);

        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/digital-architecture/users/{1}/endpoints/{2}/update?isMergeUpdate={3}";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        endpointProperties,
                                        serverName,
                                        userId,
                                        endpointGUID,
                                        isMergeUpdate);
    }



    /**
     * Remove the metadata element representing a endpoint.
     *
     * @param userId calling user
     * @param endpointGUID unique identifier of the metadata element to remove
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void removeEndpoint(String userId,
                               String endpointGUID) throws InvalidParameterException,
                                                           UserNotAuthorizedException,
                                                           PropertyServerException
    {
        final String   methodName = "removeEndpoint";
        final String   guidParameter = "endpointGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(endpointGUID, guidParameter, methodName);

        final String   urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/digital-architecture/users/{1}/endpoints/{2}/delete";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        nullRequestBody,
                                        serverName,
                                        userId,
                                        endpointGUID);
    }


    /**
     * Retrieve the list of endpoint metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param userId calling user
     * @param searchString string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<EndpointElement> findEndpoints(String userId,
                                               String searchString,
                                               int    startFrom,
                                               int    pageSize) throws InvalidParameterException,
                                                                       UserNotAuthorizedException,
                                                                       PropertyServerException
    {
        final String methodName = "findEndpoints";
        final String parameterName = "searchString";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateSearchString(searchString, parameterName, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        final String   urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/digital-architecture/users/{1}/endpoints/by-search-string" +
                                             "?startFrom={2}&pageSize={3}";

        SearchStringRequestBody requestBody = new SearchStringRequestBody();

        requestBody.setSearchString(searchString);
        requestBody.setSearchStringParameterName(parameterName);

        EndpointsResponse restResult = restClient.callEndpointsPostRESTCall(methodName,
                                                                            urlTemplate,
                                                                            requestBody,
                                                                            serverName,
                                                                            userId,
                                                                            startFrom,
                                                                            validatedPageSize);

        return restResult.getElementList();
    }


    /**
     * Retrieve the list of endpoint metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param userId calling user
     * @param name name to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<EndpointElement> getEndpointsByName(String userId,
                                                    String name,
                                                    int    startFrom,
                                                    int    pageSize) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException
    {
        final String methodName = "getEndpointsByName";
        final String nameParameter = "name";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(name, nameParameter, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        final String   urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/digital-architecture/users/{1}/endpoints/by-name?startFrom={2}&pageSize={3}";

        NameRequestBody requestBody = new NameRequestBody();

        requestBody.setName(name);
        requestBody.setNamePropertyName(nameParameter);

        EndpointsResponse restResult = restClient.callEndpointsPostRESTCall(methodName,
                                                                            urlTemplate,
                                                                            requestBody,
                                                                            serverName,
                                                                            userId,
                                                                            startFrom,
                                                                            validatedPageSize);

        return restResult.getElementList();
    }


    /**
     * Retrieve the endpoint metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param endpointGUID unique identifier of the requested metadata element
     *
     * @return matching metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public EndpointElement getEndpointByGUID(String userId,
                                             String endpointGUID) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        final String   methodName = "getEndpointByGUID";
        final String   endpointGUIDParameter = "endpointGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(endpointGUID, endpointGUIDParameter, methodName);

        final String   urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/digital-architecture/users/{1}/endpoints/{2}";

        EndpointResponse restResult = restClient.callEndpointGetRESTCall(methodName,
                                                                         urlTemplate,
                                                                         serverName,
                                                                         userId,
                                                                         endpointGUID);
        return restResult.getElement();
    }


    /**
     * Create a new metadata element to represent a connectorType. Classifications can be added later to define the
     * type of connectorType.
     *
     * @param userId             calling user
     * @param connectorTypeProperties properties to store
     * @return unique identifier of the new metadata element
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public String createConnectorType(String                  userId,
                                      ConnectorTypeProperties connectorTypeProperties) throws InvalidParameterException,
                                                                                              UserNotAuthorizedException,
                                                                                              PropertyServerException
    {
        final String methodName = "createConnectorType";
        final String nameParameter = "qualifiedName";
        final String propertiesParameter = "connectorTypeProperties";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateObject(connectorTypeProperties, propertiesParameter, methodName);
        invalidParameterHandler.validateName(connectorTypeProperties.getQualifiedName(), nameParameter, methodName);

        final String   urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/digital-architecture/users/{1}/connector-types";

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  urlTemplate,
                                                                  connectorTypeProperties,
                                                                  serverName,
                                                                  userId);

        return restResult.getGUID();
    }


    /**
     * Create a new metadata element to represent a connectorType using an existing metadata element as a template.
     * The template defines additional classifications and relationships that should be added to the new connectorType.
     *
     * @param userId             calling user
     * @param templateGUID       unique identifier of the metadata element to copy
     * @param templateProperties properties that override the template
     * @return unique identifier of the new metadata element
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public String createConnectorTypeFromTemplate(String             userId,
                                                  String             templateGUID,
                                                  TemplateProperties templateProperties) throws InvalidParameterException,
                                                                                                UserNotAuthorizedException,
                                                                                                PropertyServerException
    {
        final String methodName = "createConnectorTypeFromTemplate";
        final String nameParameter = "qualifiedName";
        final String propertiesParameter = "templateProperties";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateObject(templateProperties, propertiesParameter, methodName);
        invalidParameterHandler.validateName(templateProperties.getQualifiedName(), nameParameter, methodName);

        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/digital-architecture/users/{1}/connector-types/from-template/{2}";

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  urlTemplate,
                                                                  templateProperties,
                                                                  serverName,
                                                                  userId,
                                                                  templateGUID);

        return restResult.getGUID();
    }


    /**
     * Update the metadata element representing a connectorType.
     *
     * @param userId             calling user
     * @param isMergeUpdate        should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param connectorTypeGUID       unique identifier of the metadata element to update
     * @param connectorTypeProperties new properties for this element
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void updateConnectorType(String                  userId,
                                    boolean                 isMergeUpdate,
                                    String                  connectorTypeGUID,
                                    ConnectorTypeProperties connectorTypeProperties) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException
    {
        final String methodName = "updateConnectorType";
        final String guidParameter = "connectorTypeGUID";
        final String propertiesParameter = "connectorTypeProperties";
        final String qualifiedNameParameter = "connectorTypeProperties.qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(connectorTypeGUID, guidParameter, methodName);
        invalidParameterHandler.validateObject(connectorTypeProperties, propertiesParameter, methodName);
        
        if (! isMergeUpdate)
        {
            invalidParameterHandler.validateName(connectorTypeProperties.getQualifiedName(), qualifiedNameParameter, methodName);
        }

        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/digital-architecture/users/{1}/connector-types/{2}/update?isMergeUpdate={3}";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        connectorTypeProperties,
                                        serverName,
                                        userId,
                                        connectorTypeGUID,
                                        isMergeUpdate);
    }


    /**
     * Remove the metadata element representing a connectorType.
     *
     * @param userId calling user
     * @param connectorTypeGUID unique identifier of the metadata element to remove
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void removeConnectorType(String userId,
                                    String connectorTypeGUID) throws InvalidParameterException,
                                                                     UserNotAuthorizedException,
                                                                     PropertyServerException
    {
        final String   methodName = "removeConnectorType";
        final String   guidParameter = "connectorTypeGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(connectorTypeGUID, guidParameter, methodName);

        final String   urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/digital-architecture/users/{1}/connector-types/{2}/delete";

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        nullRequestBody,
                                        serverName,
                                        userId,
                                        connectorTypeGUID);
    }


    /**
     * Retrieve the list of connectorType metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param userId calling user
     * @param searchString string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<ConnectorTypeElement> findConnectorTypes(String userId,
                                                         String searchString,
                                                         int    startFrom,
                                                         int    pageSize) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException
    {
        final String methodName = "findConnectorTypes";
        final String parameterName = "searchString";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateSearchString(searchString, parameterName, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        final String   urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/digital-architecture/users/{1}/connector-types/by-search-string" +
                                             "?startFrom={2}&pageSize={3}";

        SearchStringRequestBody requestBody = new SearchStringRequestBody();

        requestBody.setSearchString(searchString);
        requestBody.setSearchStringParameterName(parameterName);

        ConnectorTypesResponse restResult = restClient.callConnectorTypesPostRESTCall(methodName,
                                                                                      urlTemplate,
                                                                                      requestBody,
                                                                                      serverName,
                                                                                      userId,
                                                                                      startFrom,
                                                                                      validatedPageSize);

        return restResult.getElementList();
    }


    /**
     * Retrieve the list of connectorType metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param userId calling user
     * @param name name to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<ConnectorTypeElement> getConnectorTypesByName(String userId,
                                                              String name,
                                                              int    startFrom,
                                                              int    pageSize) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException
    {
        final String methodName = "getConnectorTypesByName";
        final String nameParameter = "name";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(name, nameParameter, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        final String   urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/digital-architecture/users/{1}/connector-types/by-name?startFrom={2}&pageSize={3}";

        NameRequestBody requestBody = new NameRequestBody();

        requestBody.setName(name);
        requestBody.setNamePropertyName(nameParameter);

        ConnectorTypesResponse restResult = restClient.callConnectorTypesPostRESTCall(methodName,
                                                                                      urlTemplate,
                                                                                      requestBody,
                                                                                      serverName,
                                                                                      userId,
                                                                                      startFrom,
                                                                                      validatedPageSize);

        return restResult.getElementList();
    }


    /**
     * Retrieve the connectorType metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param connectorTypeGUID unique identifier of the requested metadata element
     *
     * @return matching metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public ConnectorTypeElement getConnectorTypeByGUID(String userId,
                                                       String connectorTypeGUID) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException
    {
        final String   methodName = "getConnectorTypeByGUID";
        final String   connectorTypeGUIDParameter = "connectorTypeGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(connectorTypeGUID, connectorTypeGUIDParameter, methodName);

        final String   urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/digital-architecture/users/{1}/connector-types/{2}";

        ConnectorTypeResponse restResult = restClient.callMyConnectorTypeGetRESTCall(methodName,
                                                                                     urlTemplate,
                                                                                     serverName,
                                                                                     userId,
                                                                                     connectorTypeGUID);
        return restResult.getElement();
    }
}
