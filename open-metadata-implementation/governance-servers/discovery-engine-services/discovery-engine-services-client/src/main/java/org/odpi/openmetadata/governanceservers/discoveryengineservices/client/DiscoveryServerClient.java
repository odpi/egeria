/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.discoveryengineservices.client;

import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.discovery.ffdc.DiscoveryEngineException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.governanceservers.discoveryengineservices.properties.DiscoveryEngineSummary;
import org.odpi.openmetadata.governanceservers.discoveryengineservices.rest.DiscoveryEngineStatusResponse;

import java.util.List;

/**
 * DiscoveryServerClient is a client-side library for calling a specific discovery server - generally it
 * is to manage the status of the discovery engines assigned to the discovery server.
 */
public class DiscoveryServerClient
{
    private String                            serverName;               /* Initialized in constructor */
    private String                            serverPlatformRootURL;    /* Initialized in constructor */
    private DiscoveryEngineServicesRESTClient restClient;               /* Initialized in constructor */

    private InvalidParameterHandler invalidParameterHandler = new InvalidParameterHandler();
    private RESTExceptionHandler    exceptionHandler        = new RESTExceptionHandler();


    /**
     * Create a client-side object for calling a discovery engine.
     *
     * @param serverPlatformRootURL the root url of the platform where the discovery engine is running.
     * @param serverName the name of the discovery server where the discovery engine is running
     * @throws InvalidParameterException one of the parameters is null or invalid.
     */
    public DiscoveryServerClient(String serverPlatformRootURL,
                                 String serverName) throws InvalidParameterException
    {
        this.serverPlatformRootURL = serverPlatformRootURL;
        this.serverName            = serverName;

        this.restClient = new DiscoveryEngineServicesRESTClient(serverName, serverPlatformRootURL);
    }


    /**
     * Create a client-side object for calling a discovery engine.
     *
     * @param serverPlatformRootURL the root url of the platform where the discovery engine is running.
     * @param serverName the name of the discovery server where the discovery engine is running
     * @param userId user id for the HTTP request
     * @param password password for the HTTP request
     * @throws InvalidParameterException one of the parameters is null or invalid.
     */
    public DiscoveryServerClient(String serverPlatformRootURL,
                                 String serverName,
                                 String userId,
                                 String password) throws InvalidParameterException
    {
        this.serverPlatformRootURL = serverPlatformRootURL;
        this.serverName            = serverName;

        this.restClient = new DiscoveryEngineServicesRESTClient(serverName, serverPlatformRootURL, userId, password);
    }


    /**
     * Retrieve the status of each assigned discovery engines.
     *
     * @param userId calling user
     * @return list of discovery engine statuses
     * @throws InvalidParameterException no available instance for the requested server
     * @throws UserNotAuthorizedException user does not have access to the requested server
     * @throws PropertyServerException the service name is not known - indicating a logic error
     */
    List<DiscoveryEngineSummary> getDiscoveryEngineStatuses(String userId) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException
    {
        final String   methodName = "getDiscoveryEngineStatuses";
        final String   urlTemplate = "/servers/{0}/open-metadata/discovery-server/users/{1}/discovery-engines/status";

        invalidParameterHandler.validateUserId(userId, methodName);

        DiscoveryEngineStatusResponse restResult = restClient.callDiscoveryEngineStatusGetRESTCall(methodName,
                                                                                                   serverPlatformRootURL + urlTemplate,
                                                                                                   serverName,
                                                                                                   userId);

        exceptionHandler.detectAndThrowInvalidParameterException(restResult);
        exceptionHandler.detectAndThrowPropertyServerException(restResult);
        exceptionHandler.detectAndThrowUserNotAuthorizedException(restResult);

        return restResult.getDiscoveryEngineSummaries();
    }


    /**
     * Request that the discovery engine refresh its configuration by calling the metadata server.
     * This request is useful if the metadata server has an outage, particularly while the
     * discovery server is initializing.  This request just ensures that the latest configuration
     * is in use.
     *
     * @param userId identifier of calling user
     * @param discoveryEngineName name of the discovery engine to target
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws DiscoveryEngineException there was a problem detected by the discovery engine.
     */
    public  void refreshConfig(String                       userId,
                               String                       discoveryEngineName) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        DiscoveryEngineException
    {
        final String   methodName = "refreshConfig";
        final String   discoveryEngineParameterName = "discoveryEngineName";
        final String   urlTemplate = "/servers/{0}/open-metadata/discovery-server/users/{1}/discovery-engines/{2}/refresh-config";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(discoveryEngineName, discoveryEngineParameterName, methodName);

        try
        {
            VoidResponse restResult = restClient.callVoidGetRESTCall(methodName,
                                                                     serverPlatformRootURL + urlTemplate,
                                                                     serverName,
                                                                     userId,
                                                                     discoveryEngineName);

            exceptionHandler.detectAndThrowInvalidParameterException(restResult);
            exceptionHandler.detectAndThrowUserNotAuthorizedException(restResult);
            exceptionHandler.detectAndThrowPropertyServerException(restResult);
        }
        catch (PropertyServerException exception)
        {
            throw new DiscoveryEngineException(exception.getReportedErrorMessage(), exception);
        }
    }
}
