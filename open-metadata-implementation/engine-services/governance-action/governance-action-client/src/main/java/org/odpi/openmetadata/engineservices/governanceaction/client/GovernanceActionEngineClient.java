/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.engineservices.governanceaction.client;


import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.engineservices.governanceaction.client.rest.GovernanceActionRESTClient;
import org.odpi.openmetadata.engineservices.governanceaction.properties.EngineSummary;
import org.odpi.openmetadata.engineservices.governanceaction.rest.EngineSummaryListResponse;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;



import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * GovernanceActionEngineClient is a client-side library for calling a specific governance action engine within an engine host server.
 */
public class GovernanceActionEngineClient
{
    private String                     serverName;               /* Initialized in constructor */
    private String                     serverPlatformRootURL;    /* Initialized in constructor */
    private GovernanceActionRESTClient restClient;               /* Initialized in constructor */

    private InvalidParameterHandler invalidParameterHandler = new InvalidParameterHandler();
    private RESTExceptionHandler    exceptionHandler        = new RESTExceptionHandler();
    /**
     * Create a client-side object for calling a governance action engine.
     *
     * @param serverPlatformRootURL the root url of the platform where the governance action engine is running.
     * @param serverName the name of the engine host server where the governance action engine is running
     * @throws InvalidParameterException one of the parameters is null or invalid.
     */
    public GovernanceActionEngineClient(String serverPlatformRootURL,
                                        String serverName) throws InvalidParameterException
    {
        this.serverPlatformRootURL = serverPlatformRootURL;
        this.serverName            = serverName;

        this.restClient = new GovernanceActionRESTClient(serverName, serverPlatformRootURL);
    }


    /**
     * Create a client-side object for calling a governance action engine.
     *
     * @param serverPlatformRootURL the root url of the platform where the governance action engine is running.
     * @param serverName the name of the engine host server where the governance action engine is running
     * @param userId user id for the HTTP request
     * @param password password for the HTTP request
     * @throws InvalidParameterException one of the parameters is null or invalid.
     */
    public GovernanceActionEngineClient(String serverPlatformRootURL,
                                        String serverName,
                                        String userId,
                                        String password) throws InvalidParameterException
    {
        this.serverPlatformRootURL = serverPlatformRootURL;
        this.serverName            = serverName;

        this.restClient = new GovernanceActionRESTClient(serverName, serverPlatformRootURL, userId, password);

    }


    /**
     * Return the names of the the governance engines running in this Governance Action OMES.
     *
     * @return list of engine summaries or
     *
     * @throws InvalidParameterException one of the parameters is null or invalid or
     * @throws UserNotAuthorizedException user not authorized to issue this request or
     * @throws PropertyServerException there was a problem detected by the governance action engine.
     */
    public List<EngineSummary> getLocalEngines(String userId) throws InvalidParameterException,
                                                                     UserNotAuthorizedException,
                                                                     PropertyServerException
    {
        final String methodName = "getLocalEngines";
        final String urlTemplate = "/servers/{0}/open-metadata/engine-services/governance-action/users/{1}/governance-action-engines";

        invalidParameterHandler.validateUserId(userId, methodName);

        EngineSummaryListResponse restResult = restClient.callGovernanceEngineSummariesGetRESTCall(methodName,
                                                                                                   serverPlatformRootURL + urlTemplate,
                                                                                                   serverName,
                                                                                                   userId);

        exceptionHandler.detectAndThrowInvalidParameterException(restResult);
        exceptionHandler.detectAndThrowPropertyServerException(restResult);
        exceptionHandler.detectAndThrowUserNotAuthorizedException(restResult);

        return restResult.getEngineSummaries();
    }
}
