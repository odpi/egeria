/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.stewardshipengineservices.client;

import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.governanceaction.ffdc.StewardshipEngineException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.governanceservers.stewardshipengineservices.properties.StewardshipEngineSummary;
import org.odpi.openmetadata.governanceservers.stewardshipengineservices.rest.StewardshipEngineStatusResponse;

import java.util.List;

/**
 * StewardshipServerClient is a client-side library for calling a specific stewardship server - generally it
 * is to manage the status of the stewardship engines assigned to the stewardship server.
 */
public class StewardshipServerClient
{
    private String                              serverName;               /* Initialized in constructor */
    private String                              serverPlatformRootURL;    /* Initialized in constructor */
    private StewardshipEngineServicesRESTClient restClient;               /* Initialized in constructor */

    private InvalidParameterHandler invalidParameterHandler = new InvalidParameterHandler();
    private RESTExceptionHandler    exceptionHandler        = new RESTExceptionHandler();


    /**
     * Create a client-side object for calling a stewardship engine.
     *
     * @param serverPlatformRootURL the root url of the platform where the stewardship engine is running.
     * @param serverName the name of the stewardship server where the stewardship engine is running
     * @throws InvalidParameterException one of the parameters is null or invalid.
     */
    public StewardshipServerClient(String serverPlatformRootURL,
                                   String serverName) throws InvalidParameterException
    {
        this.serverPlatformRootURL = serverPlatformRootURL;
        this.serverName            = serverName;

        this.restClient = new StewardshipEngineServicesRESTClient(serverName, serverPlatformRootURL);
    }


    /**
     * Create a client-side object for calling a stewardship engine.
     *
     * @param serverPlatformRootURL the root url of the platform where the stewardship engine is running.
     * @param serverName the name of the stewardship server where the stewardship engine is running
     * @param userId user id for the HTTP request
     * @param password password for the HTTP request
     * @throws InvalidParameterException one of the parameters is null or invalid.
     */
    public StewardshipServerClient(String serverPlatformRootURL,
                                   String serverName,
                                   String userId,
                                   String password) throws InvalidParameterException
    {
        this.serverPlatformRootURL = serverPlatformRootURL;
        this.serverName            = serverName;

        this.restClient = new StewardshipEngineServicesRESTClient(serverName, serverPlatformRootURL, userId, password);
    }


    /**
     * Retrieve the status of each assigned stewardship engines.
     *
     * @param userId calling user
     * @return list of stewardship engine statuses
     * @throws InvalidParameterException no available instance for the requested server
     * @throws UserNotAuthorizedException user does not have access to the requested server
     * @throws PropertyServerException the service name is not known - indicating a logic error
     */
    List<StewardshipEngineSummary> getStewardshipEngineStatuses(String userId) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException
    {
        final String   methodName = "getStewardshipEngineStatuses";
        final String   urlTemplate = "/servers/{0}/open-metadata/stewardship-server/users/{1}/stewardship-engines/status";

        invalidParameterHandler.validateUserId(userId, methodName);

        StewardshipEngineStatusResponse restResult = restClient.callStewardshipEngineStatusGetRESTCall(methodName,
                                                                                                       serverPlatformRootURL + urlTemplate,
                                                                                                       serverName,
                                                                                                       userId);

        exceptionHandler.detectAndThrowInvalidParameterException(restResult);
        exceptionHandler.detectAndThrowPropertyServerException(restResult);
        exceptionHandler.detectAndThrowUserNotAuthorizedException(restResult);

        return restResult.getStewardshipEngineSummaries();
    }


    /**
     * Request that the stewardship engine refresh its configuration by calling the metadata server.
     * This request is useful if the metadata server has an outage, particularly while the
     * stewardship server is initializing.  This request just ensures that the latest configuration
     * is in use.
     *
     * @param userId identifier of calling user
     * @param stewardshipEngineName name of the stewardship engine to target
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws StewardshipEngineException there was a problem detected by the stewardship engine.
     */
    public  void refreshConfig(String                       userId,
                               String                       stewardshipEngineName) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          StewardshipEngineException
    {
        final String   methodName = "refreshConfig";
        final String   stewardshipEngineParameterName = "stewardshipEngineName";
        final String   urlTemplate = "/servers/{0}/open-metadata/stewardship-server/users/{1}/stewardship-engines/{2}/refresh-config";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(stewardshipEngineName, stewardshipEngineParameterName, methodName);

        try
        {
            VoidResponse restResult = restClient.callVoidGetRESTCall(methodName,
                                                                     serverPlatformRootURL + urlTemplate,
                                                                     serverName,
                                                                     userId,
                                                                     stewardshipEngineName);

            exceptionHandler.detectAndThrowInvalidParameterException(restResult);
            exceptionHandler.detectAndThrowUserNotAuthorizedException(restResult);
            exceptionHandler.detectAndThrowPropertyServerException(restResult);
        }
        catch (PropertyServerException exception)
        {
            throw new StewardshipEngineException(exception.getReportedErrorMessage(), exception);
        }
    }
}
