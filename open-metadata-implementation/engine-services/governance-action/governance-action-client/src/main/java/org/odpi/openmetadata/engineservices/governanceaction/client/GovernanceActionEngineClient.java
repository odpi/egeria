/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.engineservices.governanceaction.client;


import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.ConnectorTypeResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.FFDCRESTClient;
import org.odpi.openmetadata.engineservices.governanceaction.api.GovernanceActionAPI;
import org.odpi.openmetadata.engineservices.governanceaction.client.rest.GovernanceActionRESTClient;
import org.odpi.openmetadata.engineservices.governanceaction.properties.ProviderReport;
import org.odpi.openmetadata.engineservices.governanceaction.rest.ProviderReportResponse;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;


/**
 * GovernanceActionEngineClient is a client-side library for calling a specific governance action OMES within an engine host server.
 */
public class GovernanceActionEngineClient implements GovernanceActionAPI
{
    private String                     serverName;               /* Initialized in constructor */
    private String                     serverPlatformRootURL;    /* Initialized in constructor */
    private GovernanceActionRESTClient restClient;               /* Initialized in constructor */

    private InvalidParameterHandler invalidParameterHandler = new InvalidParameterHandler();
    
    
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
     * Validate the connector and return its connector type.
     *
     * @param userId calling user
     * @param connectorProviderClassName name of a specific connector or null for all connectors
     *
     * @return connector type and other capabilities for this connector
     *
     * @throws InvalidParameterException the connector provider class name is not a valid connector fo this service
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException there was a problem detected by the integration service
     */
    public ProviderReport validateConnector(String userId,
                                            String connectorProviderClassName) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException
    {
        final String   methodName = "validateConnector";
        final String   nameParameter = "connectorProviderClassName";
        final String   urlTemplate = "/servers/{0}/open-metadata/engine-services/governance-action/users/{1}/validate-connector";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(connectorProviderClassName, nameParameter, methodName);

        ProviderReportResponse restResult = restClient.callProviderReportGetRESTCall(methodName,
                                                                                    serverPlatformRootURL + urlTemplate,
                                                                                    serverName,
                                                                                    userId,
                                                                                    connectorProviderClassName);

        return restResult.getProviderReport();
    }
}
