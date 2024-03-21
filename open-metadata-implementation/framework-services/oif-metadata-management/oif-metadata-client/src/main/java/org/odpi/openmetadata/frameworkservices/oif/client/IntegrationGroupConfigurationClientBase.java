/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworkservices.oif.client;

import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.DeleteRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDListResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.NullRequestBody;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.integration.client.IntegrationGroupConfiguration;
import org.odpi.openmetadata.frameworks.integration.properties.*;
import org.odpi.openmetadata.frameworkservices.oif.client.rest.OpenIntegrationRESTClient;
import org.odpi.openmetadata.frameworkservices.oif.rest.*;

import java.util.List;


/**
 * IntegrationGroupConfigurationClient supports the configuration of integration group and integration connectors.
 */
public abstract class IntegrationGroupConfigurationClientBase extends IntegrationGroupConfiguration
{
    private final OpenIntegrationRESTClient restClient;               /* Initialized in constructor */

    private final InvalidParameterHandler invalidParameterHandler = new InvalidParameterHandler();
    private final NullRequestBody         nullRequestBody  = new NullRequestBody();

    private AuditLog auditLog = null;


    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public IntegrationGroupConfigurationClientBase(String serverName,
                                                   String serverPlatformURLRoot,
                                                   String serviceURLMarker) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, serviceURLMarker);
        
        final String methodName = "Constructor (no security)";

        invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);
        
        this.restClient = new OpenIntegrationRESTClient(serverName, serverPlatformURLRoot);
    }


    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId caller's userId embedded in all HTTP requests
     * @param password caller's userId embedded in all HTTP requests
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public IntegrationGroupConfigurationClientBase(String serverName,
                                                   String serverPlatformURLRoot,
                                                   String serviceURLMarker,
                                                   String userId,
                                                   String password) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, serviceURLMarker);

        final String methodName = "Constructor (with security)";

        invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);
        
        this.restClient = new OpenIntegrationRESTClient(serverName, serverPlatformURLRoot, userId, password);
    }


    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param restClient pre-initialized REST client
     * @param maxPageSize pre-initialized parameter limit
     * @param auditLog logging destination
     * @throws InvalidParameterException there is a problem with the information about the remote OMAS
     */
    public IntegrationGroupConfigurationClientBase(String                      serverName,
                                                   String                      serverPlatformURLRoot,
                                                   String                      serviceURLMarker,
                                                   OpenIntegrationRESTClient   restClient,
                                                   int                         maxPageSize,
                                                   AuditLog                    auditLog) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, serviceURLMarker);

        final String methodName = "Constructor (with security)";

        invalidParameterHandler.setMaxPagingSize(maxPageSize);
        invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);
        
        this.restClient = restClient;
        this.auditLog = auditLog;
    }


    /**
     * Return the name of the server where configuration is supposed to be stored.
     *
     * @return server name
     */
    public String getConfigurationServerName()
    {
        return serverName;
    }


    /**
     * Create a new integration group definition.
     *
     * @param userId identifier of calling user
     * @param properties values that will be associated with this integration group.
     *
     * @return unique identifier (guid) of the integration group definition.  This is for use on other requests.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem storing the integration group definition.
     */
    @Override
    public String createIntegrationGroup(String                     userId,
                                         IntegrationGroupProperties properties) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        final String methodName = "createIntegrationGroup";
        final String propertiesParameterName = "properties";
        final String nameParameterName = "qualifiedName";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/governance-configuration-service/users/{2}/integration-groups/new";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateObject(properties, propertiesParameterName, methodName);
        invalidParameterHandler.validateName(properties.getQualifiedName(), nameParameterName, methodName);

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  urlTemplate,
                                                                  properties,
                                                                  serverName,
                                                                  serviceURLMarker,
                                                                  userId);

        return restResult.getGUID();
    }


    /**
     * Return the properties from an integration group definition.
     *
     * @param userId identifier of calling user
     * @param guid unique identifier (guid) of the integration group definition.
     * @return properties from the integration group definition.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the integration group definition.
     */
    @Override
    public IntegrationGroupElement getIntegrationGroupByGUID(String    userId,
                                                             String    guid) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException
    {
        final String   methodName = "getIntegrationGroupByGUID";
        final String   guidParameterName = "guid";
        final String   urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/governance-configuration-service/users/{2}/integration-groups/{3}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(guid, guidParameterName, methodName);

        IntegrationGroupElementResponse restResult = restClient.callIntegrationGroupGetRESTCall(methodName,
                                                                                                urlTemplate,
                                                                                                serverName,
                                                                                                serviceURLMarker,
                                                                                                userId,
                                                                                                guid);

        return restResult.getElement();
    }


    /**
     * Return the properties from an integration group definition.
     *
     * @param userId identifier of calling user
     * @param name qualified name or display name (if unique).
     * @return properties from the integration group definition.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the integration group definition.
     */
    @Override
    public  IntegrationGroupElement getIntegrationGroupByName(String    userId,
                                                              String    name) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        final String   methodName = "getIntegrationGroupByName";
        final String   nameParameterName = "name";
        final String   urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/governance-configuration-service/users/{2}/integration-groups/by-name/{3}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(name, nameParameterName, methodName);

        IntegrationGroupElementResponse restResult = restClient.callIntegrationGroupGetRESTCall(methodName,
                                                                                                urlTemplate,
                                                                                                serverName,
                                                                                                serviceURLMarker,
                                                                                                userId,
                                                                                                name);
        return restResult.getElement();
    }


    /**
     * Return the list of integration group definitions that are stored.
     *
     * @param userId identifier of calling user
     * @param startingFrom initial position in the stored list.
     * @param maximumResults maximum number of definitions to return on this call.
     * @return list of integration group definitions.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the integration group definitions.
     */
    @Override
    public  List<IntegrationGroupElement> getAllIntegrationGroups(String userId,
                                                                  int    startingFrom,
                                                                  int    maximumResults) throws InvalidParameterException,
                                                                                                UserNotAuthorizedException,
                                                                                                PropertyServerException
    {
        final String methodName = "getAllIntegrationGroups";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/governance-configuration-service/users/{2}/integration-groups?startingFrom={3}&maximumResults={4}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validatePaging(startingFrom, maximumResults, methodName);

        IntegrationGroupElementsResponse restResult = restClient.callIntegrationGroupsGetRESTCall(methodName,
                                                                                                  urlTemplate,
                                                                                                  serverName,
                                                                                                  serviceURLMarker,
                                                                                                  userId,
                                                                                                  startingFrom,
                                                                                                  maximumResults);

        return restResult.getElements();
    }


    /**
     * Update the properties of an existing integration group definition.  Use the current value to
     * keep a property value the same, or use the new value.  Null means remove the property from
     * the definition.
     *
     * @param userId identifier of calling user
     * @param guid unique identifier of the integration group - used to locate the definition.
     * @param isMergeUpdate should the supplied properties be merged with existing properties (true) only replacing the properties with
     *                      matching names, or should the entire properties of the instance be replaced?
     * @param properties new values for integration group.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem storing the integration group definition.
     */
    @Override
    public  void    updateIntegrationGroup(String                     userId,
                                           String                     guid,
                                           boolean                    isMergeUpdate,
                                           IntegrationGroupProperties properties) throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException
    {
        final String   methodName = "updateIntegrationGroup";
        final String   guidParameterName = "guid";
        final String   nameParameterName = "qualifiedName";
        final String   propertiesParameterName = "properties";
        final String   urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/governance-configuration-service/users/{2}/integration-groups/{3}/update?isMergeUpdate={4}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(guid, guidParameterName, methodName);
        invalidParameterHandler.validateObject(properties, propertiesParameterName, methodName);
        if (! isMergeUpdate)
        {
            invalidParameterHandler.validateName(properties.getQualifiedName(), nameParameterName, methodName);
        }

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        properties,
                                        serverName,
                                        serviceURLMarker,
                                        userId,
                                        guid,
                                        isMergeUpdate);
    }


    /**
     * Remove the properties of the integration group.  Both the guid and the qualified name is supplied
     * to validate that the correct integration group is being deleted.
     *
     * @param userId identifier of calling user
     * @param guid unique identifier of the integration group - used to locate the definition.
     * @param qualifiedName unique name for the integration group.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the integration group definition.
     */
    @Override
    public  void   deleteIntegrationGroup(String  userId,
                                          String  guid,
                                          String  qualifiedName) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException
    {
        final String   methodName = "deleteIntegrationGroup";
        final String   guidParameterName = "guid";
        final String   nameParameterName = "qualifiedName";
        final String   urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/governance-configuration-service/users/{2}/integration-groups/{3}/delete";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(guid, guidParameterName, methodName);
        invalidParameterHandler.validateName(qualifiedName, nameParameterName, methodName);

        DeleteRequestBody requestBody = new DeleteRequestBody();
        requestBody.setQualifiedName(qualifiedName);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        serviceURLMarker,
                                        userId,
                                        guid);
    }


    /**
     * Create an integration connector definition.  The same integration connector can be associated with multiple
     * integration groups.
     *
     * @param userId identifier of calling user
     * @param properties values that will be associated with this integration connector - including the connection.
     *
     * @return unique identifier of the integration connector.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem storing the integration connector definition.
     */
    @Override
    public String  createIntegrationConnector(String                        userId,
                                              IntegrationConnectorProperties properties) throws InvalidParameterException,
                                                                                                UserNotAuthorizedException,
                                                                                                PropertyServerException
    {
        final String methodName = "createIntegrationConnector";
        final String nameParameterName = "qualifiedName";
        final String connectionParameterName = "connection";
        final String propertiesParameterName = "properties";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/governance-configuration-service/users/{2}/integration-connectors/new";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateObject(properties, propertiesParameterName, methodName);
        invalidParameterHandler.validateName(properties.getQualifiedName(), nameParameterName, methodName);
        invalidParameterHandler.validateConnection(properties.getConnection(), connectionParameterName, methodName);

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  urlTemplate,
                                                                  properties,
                                                                  serverName,
                                                                  serviceURLMarker,
                                                                  userId);

        return restResult.getGUID();
    }


    /**
     * Return the properties from an integration connector definition.
     *
     * @param userId identifier of calling user
     * @param guid unique identifier (guid) of the integration connector definition.
     *
     * @return properties of the integration connector.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the integration connector definition.
     */
    @Override
    public IntegrationConnectorElement getIntegrationConnectorByGUID(String userId,
                                                                     String guid) throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException
    {
        final String   methodName = "getIntegrationConnectorByGUID";
        final String   guidParameterName = "guid";
        final String   urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/governance-configuration-service/users/{2}/integration-connectors/{3}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(guid, guidParameterName, methodName);

        IntegrationConnectorElementResponse restResult = restClient.callIntegrationConnectorGetRESTCall(methodName,
                                                                                                        urlTemplate,
                                                                                                        serverName,
                                                                                                        serviceURLMarker,
                                                                                                        userId,
                                                                                                        guid);

        return restResult.getElement();
    }


    /**
     * Return the properties from an integration connector definition.
     *
     * @param userId identifier of calling user
     * @param name qualified name or display name (if unique).
     *
     * @return properties from the integration group definition.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the integration group definition.
     */
    @Override
    public  IntegrationConnectorElement getIntegrationConnectorByName(String    userId,
                                                                      String    name) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException
    {
        final String   methodName = "getIntegrationConnectorByName";
        final String   nameParameterName = "name";
        final String   urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/governance-configuration-service/users/{2}/integration-connectors/by-name/{3}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(name, nameParameterName, methodName);

        IntegrationConnectorElementResponse restResult = restClient.callIntegrationConnectorGetRESTCall(methodName,
                                                                                                        urlTemplate,
                                                                                                        serverName,
                                                                                                        serviceURLMarker,
                                                                                                        userId,
                                                                                                        name);

        return restResult.getElement();
    }


    /**
     * Return the list of integration connectors definitions that are stored.
     *
     * @param userId identifier of calling user
     * @param startingFrom initial position in the stored list.
     * @param maximumResults maximum number of definitions to return on this call.
     *
     * @return list of integration connector definitions.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the integration connector definitions.
     */
    @Override
    public  List<IntegrationConnectorElement> getAllIntegrationConnectors(String  userId,
                                                                          int     startingFrom,
                                                                          int     maximumResults) throws InvalidParameterException,
                                                                                                         UserNotAuthorizedException,
                                                                                                         PropertyServerException
    {
        final String methodName = "getAllIntegrationConnectors";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/governance-configuration-service/users/{2}/integration-connectors?startingFrom={3}&maximumResults={4}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validatePaging(startingFrom, maximumResults, methodName);

        IntegrationConnectorElementsResponse restResult = restClient.callIntegrationConnectorsGetRESTCall(methodName,
                                                                                                          urlTemplate,
                                                                                                          serverName,
                                                                                                          serviceURLMarker,
                                                                                                          userId,
                                                                                                          startingFrom,
                                                                                                          maximumResults);

        return restResult.getElements();
    }


    /**
     * Return the list of integration groups that a specific integration connector is registered with.
     *
     * @param userId identifier of calling user
     * @param integrationConnectorGUID integration connector to search for.
     *
     * @return list of integration group unique identifiers (guids)
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the integration connector and/or integration group definitions.
     */
    @Override
    public  List<String>  getIntegrationConnectorRegistrations(String   userId,
                                                               String   integrationConnectorGUID) throws InvalidParameterException,
                                                                                                         UserNotAuthorizedException,
                                                                                                         PropertyServerException
    {
        final String methodName = "getIntegrationConnectorRegistrations";
        final String guidParameter = "integrationConnectorGUID";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/governance-configuration-service/users/{2}/integration-connectors/{3}/registrations";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(integrationConnectorGUID, guidParameter, methodName);

        GUIDListResponse restResult = restClient.callGUIDListGetRESTCall(methodName,
                                                                         urlTemplate,
                                                                         serverName,
                                                                         serviceURLMarker,
                                                                         userId,
                                                                         integrationConnectorGUID);

        return restResult.getGUIDs();
    }


    /**
     * Update the properties of an existing integration connector definition.  Use the current value to
     * keep a property value the same, or use the new value.  Null means remove the property from
     * the definition.
     *
     * @param userId identifier of calling user
     * @param guid unique identifier of the integration connector - used to locate the definition.
     * @param isMergeUpdate should the supplied properties be merged with existing properties (true) only replacing the properties with
     *                      matching names, or should the entire properties of the instance be replaced?
     * @param properties values that will be associated with this integration connector - including the connection.
     */
    @Override
    public void updateIntegrationConnector(String                         userId,
                                           String                         guid,
                                           boolean                        isMergeUpdate,
                                           IntegrationConnectorProperties properties) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException
    {
        final String   methodName = "updateIntegrationConnector";
        final String   guidParameterName = "guid";
        final String   propertiesParameterName = "properties";
        final String   nameParameterName = "qualifiedName";
        final String   connectionParameterName = "connection";
        final String   urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/governance-configuration-service/users/{2}/integration-connectors/{3}/update?isMergeUpdate={4}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(guid, guidParameterName, methodName);
        invalidParameterHandler.validateObject(properties, propertiesParameterName, methodName);

        if (! isMergeUpdate)
        {
            invalidParameterHandler.validateName(properties.getQualifiedName(), nameParameterName, methodName);
            invalidParameterHandler.validateConnection(properties.getConnection(), connectionParameterName, methodName);
        }

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        properties,
                                        serverName,
                                        serviceURLMarker,
                                        userId,
                                        guid,
                                        isMergeUpdate);
    }


    /**
     * Remove the properties of the integration connector.  Both the guid and the qualified name is supplied
     * to validate that the correct integration connector is being deleted.  The integration connector is also
     * unregistered from its integration groups.
     *
     * @param userId identifier of calling user
     * @param guid unique identifier of the integration connector - used to locate the definition.
     * @param qualifiedName unique name for the integration connector.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the integration connector definition.
     */
    @Override
    public void deleteIntegrationConnector(String  userId,
                                           String  guid,
                                           String  qualifiedName) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        final String methodName = "deleteIntegrationConnector";
        final String guidParameterName = "guid";
        final String nameParameterName = "qualifiedName";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/governance-configuration-service/users/{2}/integration-connectors/{3}/delete";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(guid, guidParameterName, methodName);
        invalidParameterHandler.validateName(qualifiedName, nameParameterName, methodName);

        DeleteRequestBody requestBody = new DeleteRequestBody();
        requestBody.setQualifiedName(qualifiedName);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        serviceURLMarker,
                                        userId,
                                        guid);
    }


    /**
     * Register an integration connector with a specific integration group.
     *
     * @param userId identifier of calling user
     * @param integrationGroupGUID unique identifier of the integration group.
     * @param integrationConnectorGUID unique identifier of the integration connector.
     * @param properties list of parameters that are used to control to the integration connector.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the integration connector and/or integration group definitions.
     */
    @Override
    public void registerIntegrationConnectorWithGroup(String                                   userId,
                                                      String                                   integrationGroupGUID,
                                                      String                                   integrationConnectorGUID,
                                                      RegisteredIntegrationConnectorProperties properties) throws InvalidParameterException,
                                                                                                                  UserNotAuthorizedException,
                                                                                                                  PropertyServerException
    {
        final String methodName = "registerIntegrationConnectorWithGroup";
        final String integrationGroupGUIDParameter = "integrationGroupGUID";
        final String integrationConnectorGUIDParameter = "integrationConnectorGUID";
        final String propertiesParameterName = "properties";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/governance-configuration-service/users/{2}/integration-groups/{3}/integration-connectors{4}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(integrationGroupGUID, integrationGroupGUIDParameter, methodName);
        invalidParameterHandler.validateGUID(integrationConnectorGUID, integrationConnectorGUIDParameter, methodName);
        invalidParameterHandler.validateObject(properties, propertiesParameterName, methodName);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        properties,
                                        serverName,
                                        serviceURLMarker,
                                        userId,
                                        integrationGroupGUID,
                                        integrationConnectorGUID);
    }


    /**
     * Retrieve a specific integration connector registered with an integration group.
     *
     * @param userId identifier of calling user
     * @param integrationGroupGUID unique identifier of the integration group.
     * @param integrationConnectorGUID unique identifier of the integration connector.
     *
     * @return details of the integration connector and the asset types it is registered for.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the integration connector and/or integration group definitions.
     */
    @Override
    public RegisteredIntegrationConnectorElement getRegisteredIntegrationConnector(String  userId,
                                                                                   String  integrationGroupGUID,
                                                                                   String  integrationConnectorGUID) throws InvalidParameterException,
                                                                                                                            UserNotAuthorizedException,
                                                                                                                            PropertyServerException
    {
        final String methodName = "getRegisteredIntegrationConnector";
        final String integrationGroupGUIDParameter = "integrationGroupGUID";
        final String integrationConnectorGUIDParameter = "integrationConnectorGUID";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/governance-configuration-service/users/{2}/integration-groups/{3}/integration-connectors/{4}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(integrationGroupGUID, integrationGroupGUIDParameter, methodName);
        invalidParameterHandler.validateGUID(integrationConnectorGUID, integrationConnectorGUIDParameter, methodName);

        RegisteredIntegrationConnectorResponse restResult = restClient.callRegisteredIntegrationConnectorGetRESTCall(methodName,
                                                                                                                     urlTemplate,
                                                                                                                     serverName,
                                                                                                                     serviceURLMarker,
                                                                                                                     userId,
                                                                                                                     integrationGroupGUID,
                                                                                                                     integrationConnectorGUID);

        return restResult.getRegisteredIntegrationConnector();
    }


    /**
     * Retrieve the identifiers of the integration connectors registered with an integration group.
     *
     * @param userId identifier of calling user
     * @param integrationGroupGUID unique identifier of the integration group.
     * @param startingFrom initial position in the stored list.
     * @param maximumResults maximum number of definitions to return on this call.
     *
     * @return list of registered services
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the integration connector and/or integration group definitions.
     */
    @Override
    public List<RegisteredIntegrationConnectorElement> getRegisteredIntegrationConnectors(String  userId,
                                                                                          String  integrationGroupGUID,
                                                                                          int     startingFrom,
                                                                                          int     maximumResults) throws InvalidParameterException,
                                                                                                                         UserNotAuthorizedException,
                                                                                                                         PropertyServerException
    {
        final String methodName = "getRegisteredIntegrationConnectors";
        final String integrationGroupGUIDParameter = "integrationGroupGUID";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/governance-configuration-service/users/{2}/integration-groups/{3}/integration-connectors?startingFrom={4}&maximumResults={5}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(integrationGroupGUID, integrationGroupGUIDParameter, methodName);
        invalidParameterHandler.validatePaging(startingFrom, maximumResults, methodName);

        RegisteredIntegrationConnectorsResponse restResult = restClient.callRegisteredIntegrationConnectorsGetRESTCall(methodName,
                                                                                                                       urlTemplate,
                                                                                                                       serverName,
                                                                                                                       serviceURLMarker,
                                                                                                                       userId,
                                                                                                                       integrationGroupGUID,
                                                                                                                       startingFrom,
                                                                                                                       maximumResults);

        return restResult.getElements();
    }


    /**
     * Unregister an integration connector from the integration group.
     *
     * @param userId identifier of calling user
     * @param integrationGroupGUID unique identifier of the integration group.
     * @param integrationConnectorGUID unique identifier of the integration connector.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the integration connector and/or integration group definitions.
     */
    @Override
    public void unregisterIntegrationConnectorFromGroup(String userId,
                                                        String integrationGroupGUID,
                                                        String integrationConnectorGUID) throws InvalidParameterException,
                                                                                                UserNotAuthorizedException,
                                                                                                PropertyServerException
    {
        final String methodName = "unregisterIntegrationConnectorFromGroup";
        final String integrationGroupGUIDParameter = "integrationGroupGUID";
        final String integrationConnectorGUIDParameter = "integrationConnectorGUID";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/governance-configuration-service/users/{2}/integration-groups/{3}/integration-connectors/{4}/delete";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(integrationGroupGUID, integrationGroupGUIDParameter, methodName);
        invalidParameterHandler.validateGUID(integrationConnectorGUID, integrationConnectorGUIDParameter, methodName);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        nullRequestBody,
                                        serverName,
                                        serviceURLMarker,
                                        userId,
                                        integrationGroupGUID,
                                        integrationConnectorGUID);
    }


    /**
     * Add a catalog target to an integration connector.
     *
     * @param userId identifier of calling user.
     * @param integrationConnectorGUID unique identifier of the integration service.
     * @param metadataElementGUID unique identifier of the metadata element that is a catalog target.
     * @param properties properties for the relationship.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem storing the catalog target definition.
     */
    @Override
    public void addCatalogTarget(String                  userId,
                                 String                  integrationConnectorGUID,
                                 String                  metadataElementGUID,
                                 CatalogTargetProperties properties) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException
    {
        final String methodName = "addCatalogTarget";
        final String propertiesParameterName = "properties";
        final String integrationConnectorGUIDParameter = "integrationConnectorGUID";
        final String metadataElementGUIDParameter = "metadataElementGUID";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/governance-configuration-service/users/{2}/integration-connectors/{3}/catalog-targets/{4}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(integrationConnectorGUID, integrationConnectorGUIDParameter, methodName);
        invalidParameterHandler.validateGUID(metadataElementGUID, metadataElementGUIDParameter, methodName);
        invalidParameterHandler.validateObject(properties, propertiesParameterName, methodName);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        properties,
                                        serverName,
                                        serviceURLMarker,
                                        userId,
                                        integrationConnectorGUID,
                                        metadataElementGUID);
    }



    /**
     * Retrieve a specific catalog target associated with an integration connector.
     *
     * @param userId identifier of calling user.
     * @param integrationConnectorGUID unique identifier of the integration service.
     * @param metadataElementGUID unique identifier of the metadata element that is a catalog target.
     *
     * @return details of the integration connector and the elements it is to catalog
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the integration connector definition.
     */
    @Override
    public CatalogTarget getCatalogTarget(String userId,
                                          String integrationConnectorGUID,
                                          String metadataElementGUID) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException
    {
        final String methodName = "getCatalogTarget";
        final String integrationConnectorGUIDParameter = "integrationConnectorGUID";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/governance-configuration-service/users/{2}/integration-connectors/{3}/catalog-targets/{4}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(integrationConnectorGUID, integrationConnectorGUIDParameter, methodName);

        CatalogTargetResponse restResult = restClient.callCatalogTargetGetRESTCall(methodName,
                                                                                   urlTemplate,
                                                                                   serverName,
                                                                                   serviceURLMarker,
                                                                                   userId,
                                                                                   integrationConnectorGUID,
                                                                                   metadataElementGUID);

        return restResult.getElement();
    }



    /**
     * Retrieve the identifiers of the metadata elements identified as catalog targets with an integration connector.
     *
     * @param userId identifier of calling user.
     * @param integrationConnectorGUID unique identifier of the integration connector.
     * @param startingFrom initial position in the stored list.
     * @param maximumResults maximum number of definitions to return on this call.
     *
     * @return list of unique identifiers
     * @throws InvalidParameterException one of the parameters is null or invalid,
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the integration connector definition.
     */
    @Override
    public List<CatalogTarget> getCatalogTargets(String  userId,
                                                 String  integrationConnectorGUID,
                                                 int     startingFrom,
                                                 int     maximumResults) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        final String methodName = "getCatalogTargets";
        final String integrationConnectorGUIDParameter = "integrationConnectorGUID";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/governance-configuration-service/users/{2}/integration-connectors/{3}/catalog-targets?startingFrom={4}&maximumResults={5}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(integrationConnectorGUID, integrationConnectorGUIDParameter, methodName);
        invalidParameterHandler.validatePaging(startingFrom, maximumResults, methodName);

        CatalogTargetsResponse restResult = restClient.callCatalogTargetsGetRESTCall(methodName,
                                                                                     urlTemplate,
                                                                                     serverName,
                                                                                     serviceURLMarker,
                                                                                     userId,
                                                                                     integrationConnectorGUID,
                                                                                     startingFrom,
                                                                                     maximumResults);

        return restResult.getElements();
    }


    /**
     * Unregister a catalog target from the integration connector.
     *
     * @param userId identifier of calling user.
     * @param integrationConnectorGUID unique identifier of the integration connector.
     * @param metadataElementGUID unique identifier of the metadata element.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem accessing/updating the integration connector definition.
     */
    @Override
    public void removeCatalogTarget(String userId,
                                    String integrationConnectorGUID,
                                    String metadataElementGUID) throws InvalidParameterException,
                                                                       UserNotAuthorizedException,
                                                                       PropertyServerException
    {
        final String methodName = "removeCatalogTarget";
        final String integrationConnectorGUIDParameter = "integrationConnectorGUID";
        final String metadataElementGUIDParameter = "metadataElementGUID";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/governance-configuration-service/users/{2}/integration-connectors/{3}/catalog-targets/{4}/delete";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(integrationConnectorGUID, integrationConnectorGUIDParameter, methodName);
        invalidParameterHandler.validateGUID(metadataElementGUID, metadataElementGUIDParameter, methodName);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        nullRequestBody,
                                        serverName,
                                        serviceURLMarker,
                                        userId,
                                        integrationConnectorGUID,
                                        metadataElementGUID);
    }
}
