/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.enginehostservices.client;

import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.governanceservers.enginehostservices.client.rest.EngineHostRESTClient;
import org.odpi.openmetadata.governanceservers.enginehostservices.properties.GovernanceEngineSummary;
import org.odpi.openmetadata.governanceservers.enginehostservices.rest.GovernanceEngineSummariesResponse;
import org.odpi.openmetadata.governanceservers.enginehostservices.rest.GovernanceEngineSummaryResponse;

import java.util.List;


/**
 * GovernanceEngineClient is a client-side library for calling a specific governance engine with a governance server.
 */
public class EngineHostClient
{
    private String               serverName;               /* Initialized in constructor */
    private String               serverPlatformRootURL;    /* Initialized in constructor */
    private EngineHostRESTClient restClient;               /* Initialized in constructor */

    private InvalidParameterHandler invalidParameterHandler = new InvalidParameterHandler();
    private RESTExceptionHandler    exceptionHandler        = new RESTExceptionHandler();


    /**
     * Create a client-side object for calling a governance engine.
     *
     * @param serverPlatformRootURL the root url of the platform where the governance engine is running.
     * @param serverName the name of the governance server where the governance engine is running
     * @throws InvalidParameterException one of the parameters is null or invalid.
     */
    public EngineHostClient(String serverPlatformRootURL,
                            String serverName) throws InvalidParameterException
    {
        this.serverPlatformRootURL = serverPlatformRootURL;
        this.serverName            = serverName;

        this.restClient = new EngineHostRESTClient(serverName, serverPlatformRootURL);
    }


    /**
     * Create a client-side object for calling a governance engine.
     *
     * @param serverPlatformRootURL the root url of the platform where the governance engine is running.
     * @param serverName the name of the governance server where the governance engine is running
     * @param userId user id for the HTTP request
     * @param password password for the HTTP request
     * @throws InvalidParameterException one of the parameters is null or invalid.
     */
    public EngineHostClient(String serverPlatformRootURL,
                            String serverName,
                            String userId,
                            String password) throws InvalidParameterException
    {
        this.serverPlatformRootURL = serverPlatformRootURL;
        this.serverName            = serverName;

        this.restClient = new EngineHostRESTClient(serverName, serverPlatformRootURL, userId, password);
    }


    /**
     * Retrieve the description and status of the requested governance engine.
     *
     * @param userId calling user
     * @param governanceEngineName qualifiedName of the governance engine to target
     *
     * @return governance engine summary
     * @throws InvalidParameterException no available instance for the requested server
     * @throws UserNotAuthorizedException user does not have access to the requested server
     * @throws PropertyServerException the service name is not known - indicating a logic error
     */
    GovernanceEngineSummary getGovernanceEngineSummary(String userId,
                                                       String governanceEngineName) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException
    {
        final String   methodName = "getGovernanceEngineSummaries";
        final String   urlTemplate = "/servers/{0}/open-metadata/engine-host-services/users/{1}/governance-engines/{2}/summary";

        invalidParameterHandler.validateUserId(userId, methodName);

        GovernanceEngineSummaryResponse restResult = restClient.callGovernanceEngineSummaryGetRESTCall(methodName,
                                                                                                           serverPlatformRootURL + urlTemplate,
                                                                                                           serverName,
                                                                                                           userId,
                                                                                                           governanceEngineName);

        exceptionHandler.detectAndThrowInvalidParameterException(restResult);
        exceptionHandler.detectAndThrowPropertyServerException(restResult);
        exceptionHandler.detectAndThrowUserNotAuthorizedException(restResult);

        return restResult.getGovernanceEngineSummary();
    }


    /**
     * Retrieve the description and status of each governance engine assigned to a specific Open Metadata Engine Service (OMES).
     *
     * @param userId calling user
     * @param serviceURLMarker engine service url unique identifier (eg asset-analysis for Asset Analysis OMES)
     *
     * @return list of governance engine summaries
     * @throws InvalidParameterException no available instance for the requested server
     * @throws UserNotAuthorizedException user does not have access to the requested server
     * @throws PropertyServerException the service name is not known - indicating a logic error
     */
    List<GovernanceEngineSummary> getGovernanceEngineSummaries(String userId,
                                                               String serviceURLMarker) throws InvalidParameterException,
                                                                                               UserNotAuthorizedException,
                                                                                               PropertyServerException
    {
        final String   methodName = "getGovernanceEngineSummaries";
        final String   urlTemplate = "/servers/{0}/open-metadata/engine-host-services/users/{1}/{2}/governance-engines/summary";

        invalidParameterHandler.validateUserId(userId, methodName);

        GovernanceEngineSummariesResponse restResult = restClient.callGovernanceEngineSummariesGetRESTCall(methodName,
                                                                                                           serverPlatformRootURL + urlTemplate,
                                                                                                           serverName,
                                                                                                           userId,
                                                                                                           serviceURLMarker);

        exceptionHandler.detectAndThrowInvalidParameterException(restResult);
        exceptionHandler.detectAndThrowPropertyServerException(restResult);
        exceptionHandler.detectAndThrowUserNotAuthorizedException(restResult);

        return restResult.getGovernanceEngineSummaries();
    }


    /**
     * Retrieve the description and status of each governance engine assigned to the Engine Host OMAG Server.
     *
     * @param userId calling user
     * @return list of governance engine summaries
     * @throws InvalidParameterException no available instance for the requested server
     * @throws UserNotAuthorizedException user does not have access to the requested server
     * @throws PropertyServerException the service name is not known - indicating a logic error
     */
    List<GovernanceEngineSummary> getGovernanceEngineSummaries(String userId) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        final String   methodName = "getGovernanceEngineSummaries";
        final String   urlTemplate = "/servers/{0}/open-metadata/engine-host-services/users/{1}/governance-engines/summary";

        invalidParameterHandler.validateUserId(userId, methodName);

        GovernanceEngineSummariesResponse restResult = restClient.callGovernanceEngineSummariesGetRESTCall(methodName,
                                                                                                           serverPlatformRootURL + urlTemplate,
                                                                                                           serverName,
                                                                                                           userId);

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
     * @param userId identifier of calling user
     * @param governanceEngineName qualifiedName of the governance engine to target
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException there was a problem detected by the governance engine.
     */
    public  void refreshConfig(String userId,
                               String governanceEngineName) throws InvalidParameterException,
                                                                   UserNotAuthorizedException,
                                                                   PropertyServerException
    {
        final String   methodName = "refreshConfig";
        final String   governanceEngineParameterName = "governanceEngineName";
        final String   urlTemplate = "/servers/{0}/open-metadata/engine-host-services/users/{1}/governance-engines/{2}/refresh-config";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(governanceEngineName, governanceEngineParameterName, methodName);

        restClient.callVoidGetRESTCall(methodName,
                                       serverPlatformRootURL + urlTemplate,
                                       serverName,
                                       userId,
                                       governanceEngineName);
    }
}
