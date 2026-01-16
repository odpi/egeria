/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworkservices.gaf.client;

import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDListResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.NameRequestBody;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.opengovernance.client.GovernanceConfiguration;
import org.odpi.openmetadata.frameworks.opengovernance.properties.GovernanceEngineElement;
import org.odpi.openmetadata.frameworks.opengovernance.properties.IntegrationGroupElement;
import org.odpi.openmetadata.frameworks.opengovernance.properties.RegisteredGovernanceServiceElement;
import org.odpi.openmetadata.frameworks.opengovernance.properties.RegisteredIntegrationConnectorElement;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworkservices.gaf.client.rest.GAFRESTClient;
import org.odpi.openmetadata.frameworkservices.gaf.rest.*;

import java.util.List;


/**
 * GovernanceEngineConfigurationClient supports the configuration of governance engine and governance services.
 */
public class GovernanceConfigurationClientBase extends GovernanceConfiguration
{
    private final GAFRESTClient           restClient;               /* Initialized in constructor */

    private final InvalidParameterHandler invalidParameterHandler = new InvalidParameterHandler();

    private AuditLog auditLog = null;


    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param localServerSecretsStoreProvider secrets store connector for bearer token
     * @param localServerSecretsStoreLocation secrets store location for bearer token
     * @param localServerSecretsStoreCollection secrets store collection for bearer token
     * @param maxPageSize maximum value allowed for page size
     * @param auditLog logging destination
     * @throws InvalidParameterException a problem creating the client-side components to issue any
     * REST API calls.
     */
    public GovernanceConfigurationClientBase(String   serverName,
                                             String   serverPlatformURLRoot,
                                             String   localServerSecretsStoreProvider,
                                             String   localServerSecretsStoreLocation,
                                             String   localServerSecretsStoreCollection,
                                             int      maxPageSize,
                                             AuditLog auditLog) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot);
        
        final String methodName = "Constructor (no security)";

        invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);
        invalidParameterHandler.setMaxPagingSize(maxPageSize);
        
        this.restClient = new GAFRESTClient(serverName, serverPlatformURLRoot, localServerSecretsStoreProvider, localServerSecretsStoreLocation, localServerSecretsStoreCollection, auditLog);
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
        final String   urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/governance-configuration-service/users/{1}/governance-engines/by-name";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(name, nameParameterName, methodName);

        NameRequestBody requestBody = new NameRequestBody();

        requestBody.setName(name);

        GovernanceEngineElementResponse restResult = restClient.callGovernanceEnginePostRESTCall(methodName,
                                                                                                urlTemplate,
                                                                                                requestBody,
                                                                                                serverName,
                                                                                                userId);
        return restResult.getElement();
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
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/governance-configuration-service/users/{1}/governance-engines/{2}/governance-services/{3}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(governanceEngineGUID, governanceEngineGUIDParameter, methodName);
        invalidParameterHandler.validateGUID(governanceServiceGUID, governanceServiceGUIDParameter, methodName);

        RegisteredGovernanceServiceResponse restResult = restClient.callRegisteredGovernanceServiceGetRESTCall(methodName,
                                                                                                               urlTemplate,
                                                                                                               serverName,
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
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/governance-configuration-service/users/{1}/governance-engines/{2}/governance-services?startingFrom={3}&maximumResults={4}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(governanceEngineGUID, governanceEngineGUIDParameter, methodName);
        invalidParameterHandler.validatePaging(startingFrom, maximumResults, methodName);

        RegisteredGovernanceServicesResponse restResult = restClient.callRegisteredGovernanceServicesGetRESTCall(methodName,
                                                                                                                 urlTemplate,
                                                                                                                 serverName,
                                                                                                                 userId,
                                                                                                                 governanceEngineGUID,
                                                                                                                 startingFrom,
                                                                                                                 maximumResults);

        return restResult.getElements();
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
        final String   urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/governance-configuration-service/users/{1}/integration-groups/by-name/{2}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(name, nameParameterName, methodName);

        IntegrationGroupElementResponse restResult = restClient.callIntegrationGroupGetRESTCall(methodName,
                                                                                                urlTemplate,
                                                                                                serverName,
                                                                                                userId,
                                                                                                name);
        return restResult.getElement();
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
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/governance-configuration-service/users/{1}/integration-connectors/{2}/registrations";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(integrationConnectorGUID, guidParameter, methodName);

        GUIDListResponse restResult = restClient.callGUIDListGetRESTCall(methodName,
                                                                         urlTemplate,
                                                                         serverName,
                                                                         userId,
                                                                         integrationConnectorGUID);

        return restResult.getGUIDs();
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
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/governance-configuration-service/users/{1}/integration-groups/{2}/integration-connectors/{3}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(integrationGroupGUID, integrationGroupGUIDParameter, methodName);
        invalidParameterHandler.validateGUID(integrationConnectorGUID, integrationConnectorGUIDParameter, methodName);

        RegisteredIntegrationConnectorResponse restResult = restClient.callRegisteredIntegrationConnectorGetRESTCall(methodName,
                                                                                                                     urlTemplate,
                                                                                                                     serverName,
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
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/governance-configuration-service/users/{1}/integration-groups/{2}/integration-connectors?startingFrom={3}&maximumResults={4}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(integrationGroupGUID, integrationGroupGUIDParameter, methodName);
        invalidParameterHandler.validatePaging(startingFrom, maximumResults, methodName);

        RegisteredIntegrationConnectorsResponse restResult = restClient.callRegisteredIntegrationConnectorsGetRESTCall(methodName,
                                                                                                                       urlTemplate,
                                                                                                                       serverName,
                                                                                                                       userId,
                                                                                                                       integrationGroupGUID,
                                                                                                                       startingFrom,
                                                                                                                       maximumResults);

        return restResult.getElements();
    }
}
