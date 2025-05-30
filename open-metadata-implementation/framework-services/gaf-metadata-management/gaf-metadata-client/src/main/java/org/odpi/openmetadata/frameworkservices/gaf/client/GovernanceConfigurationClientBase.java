/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworkservices.gaf.client;

import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.governanceaction.client.GovernanceConfiguration;
import org.odpi.openmetadata.frameworks.governanceaction.properties.*;
import org.odpi.openmetadata.frameworkservices.gaf.rest.CatalogTargetResponse;
import org.odpi.openmetadata.frameworkservices.gaf.rest.CatalogTargetsResponse;
import org.odpi.openmetadata.frameworkservices.gaf.client.rest.GAFRESTClient;
import org.odpi.openmetadata.frameworkservices.gaf.rest.*;

import java.util.List;
import java.util.Map;


/**
 * GovernanceEngineConfigurationClient supports the configuration of governance engine and governance services.
 */
public class GovernanceConfigurationClientBase extends GovernanceConfiguration
{
    private final GAFRESTClient           restClient;               /* Initialized in constructor */

    private final InvalidParameterHandler invalidParameterHandler = new InvalidParameterHandler();
    private final RESTExceptionHandler    exceptionHandler = new RESTExceptionHandler();
    private final NullRequestBody         nullRequestBody  = new NullRequestBody();

    private AuditLog auditLog = null;


    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param maxPageSize           pre-initialized parameter limit
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public GovernanceConfigurationClientBase(String serverName,
                                             String serverPlatformURLRoot,
                                             String serviceURLMarker,
                                             int    maxPageSize) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, serviceURLMarker);
        
        final String methodName = "Constructor (no security)";

        invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);
        invalidParameterHandler.setMaxPagingSize(maxPageSize);
        
        this.restClient = new GAFRESTClient(serverName, serverPlatformURLRoot);
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
     * @param maxPageSize           pre-initialized parameter limit
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public GovernanceConfigurationClientBase(String serverName,
                                             String serverPlatformURLRoot,
                                             String serviceURLMarker,
                                             String userId,
                                             String password,
                                             int    maxPageSize) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, serviceURLMarker);

        final String methodName = "Constructor (with security)";

        invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);
        invalidParameterHandler.setMaxPagingSize(maxPageSize);

        this.restClient = new GAFRESTClient(serverName, serverPlatformURLRoot, userId, password);
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
    public GovernanceConfigurationClientBase(String         serverName,
                                             String         serverPlatformURLRoot,
                                             String         serviceURLMarker,
                                             GAFRESTClient  restClient,
                                             int            maxPageSize,
                                             AuditLog       auditLog) throws InvalidParameterException
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
     * Create a new governance engine definition.
     *
     * @param userId identifier of calling user
     * @param governanceEngineType type of governance engine to create
     * @param qualifiedName unique name for the governance engine.
     * @param displayName display name for messages and user interfaces.
     * @param description description of the types of governance services that will be associated with
     *                    this governance engine.
     *
     * @return unique identifier (guid) of the governance engine definition.  This is for use on other requests.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem storing the governance engine definition.
     */
    @Override
    public String createGovernanceEngine(String userId,
                                         String governanceEngineType,
                                         String qualifiedName,
                                         String displayName,
                                         String description) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException
    {
        final String methodName = "createGovernanceEngine";
        final String engineTypeParameterName = "governanceEngineType";
        final String nameParameterName = "qualifiedName";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/governance-configuration-service/users/{2}/governance-engines/{3}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(governanceEngineType, engineTypeParameterName, methodName);
        invalidParameterHandler.validateName(qualifiedName, nameParameterName, methodName);

        NewGovernanceEngineRequestBody requestBody = new NewGovernanceEngineRequestBody();
        requestBody.setQualifiedName(qualifiedName);
        requestBody.setDisplayName(displayName);
        requestBody.setDescription(description);

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  urlTemplate,
                                                                  requestBody,
                                                                  serverName,
                                                                  serviceURLMarker,
                                                                  userId,
                                                                  governanceEngineType);

        return restResult.getGUID();
    }


    /**
     * Return the properties from a governance engine definition.
     *
     * @param userId identifier of calling user
     * @param guid unique identifier (guid) of the governance engine definition.
     * @return properties from the governance engine definition.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the governance engine definition.
     */
    @Override
    public GovernanceEngineElement getGovernanceEngineByGUID(String    userId,
                                                             String    guid) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException
    {
        final String   methodName = "getGovernanceEngineByGUID";
        final String   guidParameterName = "guid";
        final String   urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/governance-configuration-service/users/{2}/governance-engines/{3}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(guid, guidParameterName, methodName);

        GovernanceEngineElementResponse restResult = restClient.callGovernanceEngineGetRESTCall(methodName,
                                                                                                urlTemplate,
                                                                                                serverName,
                                                                                                serviceURLMarker,
                                                                                                userId,
                                                                                                guid);

        return restResult.getElement();
    }


    /**
     * Return the properties from a governance engine definition.
     *
     * @param userId identifier of calling user
     * @param name qualified name or display name (if unique).
     * @return properties from the governance engine definition.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the governance engine definition.
     */
    @Override
    public  GovernanceEngineElement getGovernanceEngineByName(String    userId,
                                                              String    name) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        final String   methodName = "getGovernanceEngineByName";
        final String   nameParameterName = "name";
        final String   urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/governance-configuration-service/users/{2}/governance-engines/by-name";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(name, nameParameterName, methodName);

        NameRequestBody requestBody = new NameRequestBody();

        requestBody.setName(name);

        GovernanceEngineElementResponse restResult = restClient.callGovernanceEnginePostRESTCall(methodName,
                                                                                                urlTemplate,
                                                                                                requestBody,
                                                                                                serverName,
                                                                                                serviceURLMarker,
                                                                                                userId);
        return restResult.getElement();
    }


    /**
     * Return the list of governance engine definitions that are stored.
     *
     * @param userId identifier of calling user
     * @param governanceEngineType type of governance engine to create
     * @param startingFrom initial position in the stored list.
     * @param maximumResults maximum number of definitions to return on this call.
     * @return list of governance engine definitions.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the governance engine definitions.
     */
    @Override
    public  List<GovernanceEngineElement> getAllGovernanceEngines(String userId,
                                                                  String governanceEngineType,
                                                                  int    startingFrom,
                                                                  int    maximumResults) throws InvalidParameterException,
                                                                                                UserNotAuthorizedException,
                                                                                                PropertyServerException
    {
        final String methodName = "getAllGovernanceEngines";
        final String engineTypeParameterName = "governanceEngineType";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/governance-configuration-service/users/{2}/governance-engines/{3}?startingFrom={4}&maximumResults={5}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(governanceEngineType, engineTypeParameterName, methodName);
        invalidParameterHandler.validatePaging(startingFrom, maximumResults, methodName);

        GovernanceEngineElementsResponse restResult = restClient.callGovernanceEnginesGetRESTCall(methodName,
                                                                                                  urlTemplate,
                                                                                                  serverName,
                                                                                                  serviceURLMarker,
                                                                                                  userId,
                                                                                                  governanceEngineType,
                                                                                                  Integer.toString(startingFrom),
                                                                                                  Integer.toString(maximumResults));

        return restResult.getElements();
    }


    /**
     * Update the properties of an existing governance engine definition.  Use the current value to
     * keep a property value the same, or use the new value.  Null means remove the property from
     * the definition.
     *
     * @param userId identifier of calling user
     * @param guid unique identifier of the governance engine - used to locate the definition.
     * @param qualifiedName new value for unique name of governance engine.
     * @param displayName new value for the display name.
     * @param description new description for the governance engine.
     * @param typeDescription new description of the type ofg governance engine.
     * @param version new version number for the governance engine implementation.
     * @param patchLevel new patch level for the governance engine implementation.
     * @param source new source description for the implementation of the governance engine.
     * @param additionalProperties additional properties for the governance engine.
     * @param extendedProperties properties to populate the subtype of the governance engine.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem storing the governance engine definition.
     */
    @Override
    public  void    updateGovernanceEngine(String                userId,
                                           String                guid,
                                           String                qualifiedName,
                                           String                displayName,
                                           String                description,
                                           String                typeDescription,
                                           String                version,
                                           String                patchLevel,
                                           String                source,
                                           Map<String, String>   additionalProperties,
                                           Map<String, Object>   extendedProperties) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException
    {
        final String   methodName = "updateGovernanceEngine";
        final String   guidParameterName = "guid";
        final String   nameParameterName = "qualifiedName";
        final String   urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/governance-configuration-service/users/{2}/governance-engines/{3}/update";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(guid, guidParameterName, methodName);
        invalidParameterHandler.validateName(qualifiedName, nameParameterName, methodName);

        UpdateGovernanceEngineRequestBody requestBody = new UpdateGovernanceEngineRequestBody();
        requestBody.setQualifiedName(qualifiedName);
        requestBody.setDisplayName(displayName);
        requestBody.setDescription(description);
        requestBody.setTypeDescription(typeDescription);
        requestBody.setVersion(version);
        requestBody.setPatchLevel(patchLevel);
        requestBody.setSource(source);
        requestBody.setAdditionalProperties(additionalProperties);
        requestBody.setExtendedProperties(extendedProperties);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        serviceURLMarker,
                                        userId,
                                        guid);
    }


    /**
     * Remove the properties of the governance engine.  Both the guid and the qualified name is supplied
     * to validate that the correct governance engine is being deleted.
     *
     * @param userId identifier of calling user
     * @param guid unique identifier of the governance engine - used to locate the definition.
     * @param qualifiedName unique name for the governance engine.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the governance engine definition.
     */
    @Override
    public  void   deleteGovernanceEngine(String  userId,
                                          String  guid,
                                          String  qualifiedName) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException
    {
        final String   methodName = "deleteGovernanceEngine";
        final String   guidParameterName = "guid";
        final String   nameParameterName = "qualifiedName";
        final String   urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/governance-configuration-service/users/{2}/governance-engines/{3}/delete";

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
     * Create a governance service definition.  The same governance service can be associated with multiple
     * governance engines.
     *
     * @param userId identifier of calling user
     * @param governanceServiceType type of the governance service to create
     * @param qualifiedName  unique name for the governance service.
     * @param displayName   display name for the governance service.
     * @param description  description of the analysis provided by the governance service.
     * @param connection   connection to instantiate the governance service implementation.
     *
     * @return unique identifier of the governance service.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem storing the governance service definition.
     */
    @Override
    public String  createGovernanceService(String     userId,
                                           String     governanceServiceType,
                                           String     qualifiedName,
                                           String     displayName,
                                           String     description,
                                           Connection connection) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        final String methodName = "createGovernanceService";
        final String serviceTypeParameterName = "governanceServiceType";
        final String nameParameterName = "qualifiedName";
        final String connectionParameterName = "connection";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/governance-configuration-service/users/{2}/governance-services/types/{3}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(governanceServiceType, serviceTypeParameterName, methodName);
        invalidParameterHandler.validateName(qualifiedName, nameParameterName, methodName);
        invalidParameterHandler.validateConnection(connection, connectionParameterName, methodName);

        NewGovernanceServiceRequestBody requestBody = new NewGovernanceServiceRequestBody();
        requestBody.setQualifiedName(qualifiedName);
        requestBody.setName(displayName);
        requestBody.setDescription(description);
        requestBody.setConnection(connection);

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  urlTemplate,
                                                                  requestBody,
                                                                  serverName,
                                                                  serviceURLMarker,
                                                                  userId,
                                                                  governanceServiceType);

        return restResult.getGUID();
    }


    /**
     * Return the properties from a governance service definition.
     *
     * @param userId identifier of calling user
     * @param guid unique identifier (guid) of the governance service definition.
     *
     * @return properties of the governance service.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the governance service definition.
     */
    @Override
    public GovernanceServiceElement getGovernanceServiceByGUID(String userId,
                                                               String guid) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException
    {
        final String   methodName = "getGovernanceServiceByGUID";
        final String   guidParameterName = "guid";
        final String   urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/governance-configuration-service/users/{2}/governance-services/{3}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(guid, guidParameterName, methodName);

        GovernanceServiceElementResponse restResult = restClient.callGovernanceServiceGetRESTCall(methodName,
                                                                                                  urlTemplate,
                                                                                                  serverName,
                                                                                                  serviceURLMarker,
                                                                                                  userId,
                                                                                                  guid);

        return restResult.getElement();
    }


    /**
     * Return the properties from a governance service definition.
     *
     * @param userId identifier of calling user
     * @param name qualified name or display name (if unique).
     *
     * @return properties from the governance engine definition.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the governance engine definition.
     */
    @Override
    public  GovernanceServiceElement getGovernanceServiceByName(String    userId,
                                                                String    name) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        final String   methodName = "getGovernanceServiceByName";
        final String   nameParameterName = "name";
        final String   urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/governance-configuration-service/users/{2}/governance-services/by-name";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(name, nameParameterName, methodName);

        NameRequestBody requestBody = new NameRequestBody();

        requestBody.setName(name);

        GovernanceServiceElementResponse restResult = restClient.callGovernanceServicePostRESTCall(methodName,
                                                                                                   urlTemplate,
                                                                                                   requestBody,
                                                                                                   serverName,
                                                                                                   serviceURLMarker,
                                                                                                   userId);

        return restResult.getElement();
    }


    /**
     * Return the list of governance services definitions that are stored.
     *
     * @param userId identifier of calling user
     * @param startingFrom initial position in the stored list.
     * @param maximumResults maximum number of definitions to return on this call.
     *
     * @return list of governance service definitions.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the governance service definitions.
     */
    @Override
    public  List<GovernanceServiceElement> getAllGovernanceServices(String  userId,
                                                                    int     startingFrom,
                                                                    int     maximumResults) throws InvalidParameterException,
                                                                                                   UserNotAuthorizedException,
                                                                                                   PropertyServerException
    {
        final String methodName = "getAllGovernanceServices";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/governance-configuration-service/users/{2}/governance-services?startingFrom={3}&maximumResults={4}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validatePaging(startingFrom, maximumResults, methodName);

        GovernanceServiceElementsResponse restResult = restClient.callGovernanceServicesGetRESTCall(methodName,
                                                                                                    urlTemplate,
                                                                                                    serverName,
                                                                                                    serviceURLMarker,
                                                                                                    userId,
                                                                                                    Integer.toString(startingFrom),
                                                                                                    Integer.toString(maximumResults));

        return restResult.getElements();
    }


    /**
     * Return the list of governance engines that a specific governance service is registered with.
     *
     * @param userId identifier of calling user
     * @param governanceServiceGUID governance service to search for.
     *
     * @return list of governance engine unique identifiers (guids)
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the governance service and/or governance engine definitions.
     */
    @Override
    public  List<String>  getGovernanceServiceRegistrations(String   userId,
                                                            String   governanceServiceGUID) throws InvalidParameterException,
                                                                                                   UserNotAuthorizedException,
                                                                                                   PropertyServerException
    {
        final String methodName = "getGovernanceServiceRegistrations";
        final String guidParameter = "governanceServiceGUID";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/governance-configuration-service/users/{2}/governance-services/{3}/registrations";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(governanceServiceGUID, guidParameter, methodName);

        GUIDListResponse restResult = restClient.callGUIDListGetRESTCall(methodName,
                                                                         urlTemplate,
                                                                         serverName,
                                                                         serviceURLMarker,
                                                                         userId,
                                                                         governanceServiceGUID);

        return restResult.getGUIDs();
    }


    /**
     * Update the properties of an existing governance service definition.  Use the current value to
     * keep a property value the same, or use the new value.  Null means remove the property from
     * the definition.
     *
     * @param userId identifier of calling user
     * @param guid unique identifier of the governance service - used to locate the definition.
     * @param qualifiedName new value for unique name of governance service.
     * @param displayName new value for the display name.
     * @param description new value for the description.
     * @param connection connection used to create an instance of this governance service.
     * @param additionalProperties additional properties for the governance engine.
     * @param extendedProperties properties to populate the subtype of the governance service.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem storing the governance service definition.
     */
    @Override
    public void updateGovernanceService(String              userId,
                                        String              guid,
                                        String              qualifiedName,
                                        String              displayName,
                                        String              description,
                                        Connection          connection,
                                        Map<String, String> additionalProperties,
                                        Map<String, Object> extendedProperties) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        final String   methodName = "updateGovernanceService";
        final String   guidParameterName = "guid";
        final String   nameParameterName = "qualifiedName";
        final String   connectionParameterName = "connection";
        final String   urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/governance-configuration-service/users/{2}/governance-services/{3}/update";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(guid, guidParameterName, methodName);
        invalidParameterHandler.validateName(qualifiedName, nameParameterName, methodName);
        invalidParameterHandler.validateConnection(connection, connectionParameterName, methodName);

        UpdateGovernanceServiceRequestBody requestBody = new UpdateGovernanceServiceRequestBody();
        requestBody.setQualifiedName(qualifiedName);
        requestBody.setName(displayName);
        requestBody.setDescription(description);
        requestBody.setConnection(connection);
        requestBody.setAdditionalProperties(additionalProperties);
        requestBody.setExtendedProperties(extendedProperties);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        serviceURLMarker,
                                        userId,
                                        guid);
    }


    /**
     * Remove the properties of the governance service.  Both the guid and the qualified name is supplied
     * to validate that the correct governance service is being deleted.  The governance service is also
     * unregistered from its governance engines.
     *
     * @param userId identifier of calling user
     * @param guid unique identifier of the governance service - used to locate the definition.
     * @param qualifiedName unique name for the governance service.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the governance service definition.
     */
    @Override
    public void deleteGovernanceService(String  userId,
                                        String  guid,
                                        String  qualifiedName) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException
    {
        final String methodName = "deleteGovernanceService";
        final String guidParameterName = "guid";
        final String nameParameterName = "qualifiedName";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/governance-configuration-service/users/{2}/governance-services/{3}/delete";

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
     * Register a governance service with a specific governance engine.
     *
     * @param userId identifier of calling user
     * @param governanceEngineGUID unique identifier of the governance engine.
     * @param governanceServiceGUID unique identifier of the governance service.
     * @param governanceRequestType governance request type used by caller and supported by the governance server.
     * @param requestParameters list of parameters that are passed to the governance service (via
     *                                  the governance context).  These values can be overridden on the actual governance request.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the governance service and/or governance engine definitions.
     */
    @Deprecated
    @Override
    public void registerGovernanceServiceWithEngine(String               userId,
                                                    String               governanceEngineGUID,
                                                    String               governanceServiceGUID,
                                                    String               governanceRequestType,
                                                    Map<String, String>  requestParameters) throws InvalidParameterException,
                                                                                                   UserNotAuthorizedException,
                                                                                                   PropertyServerException
    {
        registerGovernanceServiceWithEngine(userId, governanceEngineGUID, governanceServiceGUID, governanceRequestType, null, requestParameters);
    }


    /**
     * Register a governance service with a specific governance engine.
     *
     * @param userId identifier of calling user
     * @param governanceEngineGUID unique identifier of the governance engine.
     * @param governanceServiceGUID unique identifier of the governance service.
     * @param governanceRequestType governance request type used by caller.
     * @param serviceRequestType mapped governance request type that this governance service is able to process.
     * @param requestParameters list of parameters that are passed to the governance service (via
     *                                  the governance context).  These values can be overridden on the actual governance request.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the governance service and/or governance engine definitions.
     */
    @Override
    public void registerGovernanceServiceWithEngine(String               userId,
                                                    String               governanceEngineGUID,
                                                    String               governanceServiceGUID,
                                                    String               governanceRequestType,
                                                    String               serviceRequestType,
                                                    Map<String, String>  requestParameters) throws InvalidParameterException,
                                                                                                   UserNotAuthorizedException,
                                                                                                   PropertyServerException
    {
        final String methodName = "registerGovernanceServiceWithEngine";
        final String governanceEngineGUIDParameter = "governanceEngineGUID";
        final String governanceServiceGUIDParameter = "governanceServiceGUID";
        final String governanceRequestTypesParameter = "governanceRequestType";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/governance-configuration-service/users/{2}/governance-engines/{3}/governance-services";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(governanceEngineGUID, governanceEngineGUIDParameter, methodName);
        invalidParameterHandler.validateGUID(governanceServiceGUID, governanceServiceGUIDParameter, methodName);
        invalidParameterHandler.validateName(governanceRequestType, governanceRequestTypesParameter, methodName);

        GovernanceServiceRegistrationRequestBody requestBody = new GovernanceServiceRegistrationRequestBody();
        requestBody.setGovernanceServiceGUID(governanceServiceGUID);
        requestBody.setRequestType(governanceRequestType);
        requestBody.setServiceRequestType(serviceRequestType);
        requestBody.setRequestParameters(requestParameters);

        VoidResponse restResult = restClient.callVoidPostRESTCall(methodName,
                                                                  urlTemplate,
                                                                  requestBody,
                                                                  serverName,
                                                                  serviceURLMarker,
                                                                  userId,
                                                                  governanceEngineGUID);

        exceptionHandler.detectAndThrowInvalidParameterException(restResult);
        exceptionHandler.detectAndThrowUserNotAuthorizedException(restResult);
        exceptionHandler.detectAndThrowPropertyServerException(restResult);
    }


    /**
     * Retrieve a specific governance service registered with a governance engine.
     *
     * @param userId identifier of calling user
     * @param governanceEngineGUID unique identifier of the governance engine.
     * @param governanceServiceGUID unique identifier of the governance service.
     *
     * @return details of the governance service and the asset types it is registered for.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the governance service and/or governance engine definitions.
     */
    @Override
    public RegisteredGovernanceServiceElement getRegisteredGovernanceService(String  userId,
                                                                             String  governanceEngineGUID,
                                                                             String  governanceServiceGUID) throws InvalidParameterException,
                                                                                                                   UserNotAuthorizedException,
                                                                                                                   PropertyServerException
    {
        final String methodName = "getRegisteredGovernanceService";
        final String governanceEngineGUIDParameter = "governanceEngineGUID";
        final String governanceServiceGUIDParameter = "governanceServiceGUID";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/governance-configuration-service/users/{2}/governance-engines/{3}/governance-services/{4}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(governanceEngineGUID, governanceEngineGUIDParameter, methodName);
        invalidParameterHandler.validateGUID(governanceServiceGUID, governanceServiceGUIDParameter, methodName);

        RegisteredGovernanceServiceResponse restResult = restClient.callRegisteredGovernanceServiceGetRESTCall(methodName,
                                                                                                               urlTemplate,
                                                                                                               serverName,
                                                                                                               serviceURLMarker,
                                                                                                               userId,
                                                                                                               governanceEngineGUID,
                                                                                                               governanceServiceGUID);

        return restResult.getRegisteredGovernanceService();
    }


    /**
     * Retrieve the identifiers of the governance services registered with a governance engine.
     *
     * @param userId identifier of calling user
     * @param governanceEngineGUID unique identifier of the governance engine.
     * @param startingFrom initial position in the stored list.
     * @param maximumResults maximum number of definitions to return on this call.
     *
     * @return list of registered services
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the governance service and/or governance engine definitions.
     */
    @Override
    public List<RegisteredGovernanceServiceElement> getRegisteredGovernanceServices(String  userId,
                                                                                    String  governanceEngineGUID,
                                                                                    int     startingFrom,
                                                                                    int     maximumResults) throws InvalidParameterException,
                                                                                                                   UserNotAuthorizedException,
                                                                                                                   PropertyServerException
    {
        final String methodName = "getRegisteredGovernanceServices";
        final String governanceEngineGUIDParameter = "governanceEngineGUID";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/governance-configuration-service/users/{2}/governance-engines/{3}/governance-services?startingFrom={4}&maximumResults={5}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(governanceEngineGUID, governanceEngineGUIDParameter, methodName);
        invalidParameterHandler.validatePaging(startingFrom, maximumResults, methodName);

        RegisteredGovernanceServicesResponse restResult = restClient.callRegisteredGovernanceServicesGetRESTCall(methodName,
                                                                                                                 urlTemplate,
                                                                                                                 serverName,
                                                                                                                 serviceURLMarker,
                                                                                                                 userId,
                                                                                                                 governanceEngineGUID,
                                                                                                                 startingFrom,
                                                                                                                 maximumResults);

        return restResult.getElements();
    }


    /**
     * Unregister a governance service from the governance engine.
     *
     * @param userId identifier of calling user
     * @param governanceEngineGUID unique identifier of the governance engine.
     * @param governanceServiceGUID unique identifier of the governance service.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the governance service and/or governance engine definitions.
     */
    @Override
    public void unregisterGovernanceServiceFromEngine(String userId,
                                                      String governanceEngineGUID,
                                                      String governanceServiceGUID) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException
    {
        final String methodName = "unregisterGovernanceServiceFromEngine";
        final String governanceEngineGUIDParameter = "governanceEngineGUID";
        final String governanceServiceGUIDParameter = "governanceServiceGUID";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/governance-configuration-service/users/{2}/governance-engines/{3}/governance-services/{4}/delete";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(governanceEngineGUID, governanceEngineGUIDParameter, methodName);
        invalidParameterHandler.validateGUID(governanceServiceGUID, governanceServiceGUIDParameter, methodName);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        nullRequestBody,
                                        serverName,
                                        serviceURLMarker,
                                        userId,
                                        governanceEngineGUID,
                                        governanceServiceGUID);

    }


    /*
     * Integration connectors
     */


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
     * @return catalog target GUID
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem storing the catalog target definition.
     */
    @Override
    public String addCatalogTarget(String                  userId,
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

        GUIDResponse response = restClient.callGUIDPostRESTCall(methodName,
                                                                urlTemplate,
                                                                properties,
                                                                serverName,
                                                                serviceURLMarker,
                                                                userId,
                                                                integrationConnectorGUID,
                                                                metadataElementGUID);

        return response.getGUID();
    }


    /**
     * Update a catalog target relationship for an integration connector.
     *
     * @param userId identifier of calling user.
     * @param relationshipGUID unique identifier of the relationship.
     * @param properties properties for the relationship.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem storing the catalog target definition.
     */
    @Override
    public void updateCatalogTarget(String                  userId,
                                    String                  relationshipGUID,
                                    CatalogTargetProperties properties) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        final String methodName = "addCatalogTarget";
        final String propertiesParameterName = "properties";
        final String integrationConnectorGUIDParameter = "relationshipGUID";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/governance-configuration-service/users/{2}/catalog-targets/{3}/update";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(relationshipGUID, integrationConnectorGUIDParameter, methodName);
        invalidParameterHandler.validateObject(properties, propertiesParameterName, methodName);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        properties,
                                        serverName,
                                        serviceURLMarker,
                                        userId,
                                        relationshipGUID);
    }


    /**
     * Retrieve a specific catalog target associated with an integration connector.
     *
     * @param userId identifier of calling user.
     * @param relationshipGUID unique identifier of the relationship.
     *
     * @return details of the integration connector and the elements it is to catalog
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the integration connector definition.
     */
    @Override
    public CatalogTarget getCatalogTarget(String userId,
                                          String relationshipGUID) throws InvalidParameterException,
                                                                          UserNotAuthorizedException,
                                                                          PropertyServerException
    {
        final String methodName = "getCatalogTarget";
        final String integrationConnectorGUIDParameter = "relationshipGUID";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/governance-configuration-service/users/{2}/catalog-targets/{3}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(relationshipGUID, integrationConnectorGUIDParameter, methodName);

        CatalogTargetResponse restResult = restClient.callCatalogTargetGetRESTCall(methodName,
                                                                                   urlTemplate,
                                                                                   serverName,
                                                                                   serviceURLMarker,
                                                                                   userId,
                                                                                   relationshipGUID);

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
     * @return list of named elements
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
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/governance-configuration-service/users/{2}/integration-connectors/{2}/catalog-targets?startingFrom={3}&maximumResults={4}";

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
     * @param relationshipGUID unique identifier of the integration connector.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem accessing/updating the integration connector definition.
     */
    @Override
    public void removeCatalogTarget(String userId,
                                    String relationshipGUID) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException
    {
        final String methodName = "removeCatalogTarget";
        final String integrationConnectorGUIDParameter = "relationshipGUID";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/framework-services/{1}/governance-configuration-service/users/{2}/catalog-targets/{3}/delete";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(relationshipGUID, integrationConnectorGUIDParameter, methodName);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        nullRequestBody,
                                        serverName,
                                        serviceURLMarker,
                                        userId,
                                        relationshipGUID);
    }

}
