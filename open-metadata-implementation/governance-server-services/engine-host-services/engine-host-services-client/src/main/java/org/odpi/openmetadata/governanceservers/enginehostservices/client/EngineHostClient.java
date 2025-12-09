/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.enginehostservices.client;

import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.SecretsStoreConnector;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.governanceservers.enginehostservices.client.rest.EngineHostRESTClient;
import org.odpi.openmetadata.governanceservers.enginehostservices.properties.GovernanceEngineSummary;
import org.odpi.openmetadata.governanceservers.enginehostservices.rest.GovernanceEngineSummariesResponse;
import org.odpi.openmetadata.governanceservers.enginehostservices.rest.GovernanceEngineSummaryResponse;

import java.util.List;
import java.util.Map;


/**
 * GovernanceEngineClient is a client-side library for calling a specific governance engine with a governance server.
 */
public class EngineHostClient
{
    private final String               serverName;               /* Initialized in constructor */
    private final String               serverPlatformRootURL;    /* Initialized in constructor */
    private final EngineHostRESTClient restClient;               /* Initialized in constructor */

    private final InvalidParameterHandler invalidParameterHandler = new InvalidParameterHandler();
    private final RESTExceptionHandler    exceptionHandler        = new RESTExceptionHandler();


    /**
     * Create a client-side object for calling a governance engine.
     *
     * @param serverName name of the OMAG Server to call
     * @param serverPlatformURLRoot URL root of the server platform where the OMAG Server is running.
     * @param secretsStoreProvider secrets store connector for bearer token
     * @param secretsStoreLocation secrets store location for bearer token
     * @param secretsStoreCollection secrets store collection for bearer token
     * @param auditLog destination for log messages.
     * @throws InvalidParameterException one of the parameters is null or invalid.
     */
    public EngineHostClient(String   serverName,
                            String   serverPlatformURLRoot,
                            String   secretsStoreProvider,
                            String   secretsStoreLocation,
                            String   secretsStoreCollection,
                            AuditLog auditLog) throws InvalidParameterException
    {
        this.serverPlatformRootURL = serverPlatformURLRoot;
        this.serverName            = serverName;

        this.restClient = new EngineHostRESTClient(serverName, serverPlatformRootURL, secretsStoreProvider, secretsStoreLocation, secretsStoreCollection, auditLog);
    }


    /**
     * Create a client-side object for calling a governance engine.
     *
     * @param serverName name of the OMAG Server to call
     * @param serverPlatformURLRoot URL root of the server platform where the OMAG Server is running
     * @param secretsStoreConnectorMap connectors to secrets stores
     * @param auditLog destination for log messages.
     * @throws InvalidParameterException one of the parameters is null or invalid.
     */
    public EngineHostClient(String                             serverName,
                            String                             serverPlatformURLRoot,
                            Map<String, SecretsStoreConnector> secretsStoreConnectorMap,
                            AuditLog                           auditLog) throws InvalidParameterException
    {
        this.serverPlatformRootURL = serverPlatformURLRoot;
        this.serverName            = serverName;

        this.restClient = new EngineHostRESTClient(serverName, serverPlatformRootURL, secretsStoreConnectorMap, auditLog);
    }


    /**
     * Retrieve the description and status of the requested governance engine.
     *
     * @param governanceEngineName qualifiedName of the governance engine to target
     *
     * @return governance engine summary
     * @throws InvalidParameterException no available instance for the requested server
     * @throws UserNotAuthorizedException user does not have access to the requested server
     * @throws PropertyServerException the service name is not known - indicating a logic error
     */
    public GovernanceEngineSummary getGovernanceEngineSummary(String governanceEngineName) throws InvalidParameterException,
                                                                                                  UserNotAuthorizedException,
                                                                                                  PropertyServerException
    {
        final String   methodName = "getGovernanceEngineSummary";
        final String   urlTemplate = "/servers/{0}/open-metadata/engine-host/governance-engines/{1}/summary";

        GovernanceEngineSummaryResponse restResult = restClient.callGovernanceEngineSummaryGetRESTCall(methodName,
                                                                                                           serverPlatformRootURL + urlTemplate,
                                                                                                           serverName,
                                                                                                           governanceEngineName);

        exceptionHandler.detectAndThrowInvalidParameterException(restResult);
        exceptionHandler.detectAndThrowPropertyServerException(restResult);
        exceptionHandler.detectAndThrowUserNotAuthorizedException(restResult);

        return restResult.getGovernanceEngineSummary();
    }


    /**
     * Retrieve the description and status of each governance engine assigned to the Engine Host OMAG Server.
     *
     * @return list of governance engine summaries
     * @throws InvalidParameterException no available instance for the requested server
     * @throws UserNotAuthorizedException user does not have access to the requested server
     * @throws PropertyServerException the service name is not known - indicating a logic error
     */
    public List<GovernanceEngineSummary> getGovernanceEngineSummaries() throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        final String   methodName = "getGovernanceEngineSummaries";
        final String   urlTemplate = "/servers/{0}/open-metadata/engine-host/governance-engines/summary";

        GovernanceEngineSummariesResponse restResult = restClient.callGovernanceEngineSummariesGetRESTCall(methodName,
                                                                                                           serverPlatformRootURL + urlTemplate,
                                                                                                           serverName);

        exceptionHandler.detectAndThrowInvalidParameterException(restResult);
        exceptionHandler.detectAndThrowPropertyServerException(restResult);
        exceptionHandler.detectAndThrowUserNotAuthorizedException(restResult);

        return restResult.getGovernanceEngineSummaries();
    }


    /**
     * Request that the governance engine refresh its configuration by calling the metadata server.
     * This request is useful if the metadata server has an outage, particularly while the
     * governance server is initializing.  This request just ensures that the latest configuration
     * is in use.
     *
     * @param governanceEngineName qualifiedName of the governance engine to target
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException there was a problem detected by the governance engine.
     */
    public  void refreshConfig(String governanceEngineName) throws InvalidParameterException,
                                                                   UserNotAuthorizedException,
                                                                   PropertyServerException
    {
        final String   methodName = "refreshConfig";
        final String   governanceEngineParameterName = "governanceEngineName";
        final String   urlTemplate = "/servers/{0}/open-metadata/engine-host/governance-engines/{1}/refresh-config";

        invalidParameterHandler.validateName(governanceEngineName, governanceEngineParameterName, methodName);

        restClient.callVoidGetRESTCall(methodName,
                                       serverPlatformRootURL + urlTemplate,
                                       serverName,
                                       governanceEngineName);
    }


    /**
     * Request that all governance engines refresh their configuration by calling the metadata server.
     * This request is useful if the metadata server has an outage, particularly while the
     * governance server is initializing.  This request just ensures that the latest configuration
     * is in use.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException there was a problem detected by the governance engine.
     */
    public  void refreshConfig() throws InvalidParameterException,
                                        UserNotAuthorizedException,
                                        PropertyServerException
    {
        final String   methodName = "refreshConfig";
        final String   urlTemplate = "/servers/{0}/open-metadata/engine-host/governance-engines/refresh-config";


        restClient.callVoidGetRESTCall(methodName,
                                       serverPlatformRootURL + urlTemplate,
                                       serverName);
    }
}
