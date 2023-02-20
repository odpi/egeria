/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.engineservices.repositorygovernance.client;


import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.properties.ConnectorReport;
import org.odpi.openmetadata.commonservices.ffdc.rest.ConnectorReportResponse;
import org.odpi.openmetadata.engineservices.repositorygovernance.api.RepositoryGovernanceAPI;
import org.odpi.openmetadata.engineservices.repositorygovernance.client.rest.RepositoryGovernanceRESTClient;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;


/**
 * RepositoryGovernanceClient is a client-side library for calling a specific archive engine with an engine host server.
 */
public class RepositoryGovernanceClient  implements RepositoryGovernanceAPI
{
    private String                         serverName;                     /* Initialized in constructor */
    private String                         serverPlatformRootURL;          /* Initialized in constructor */
    private String                         repositoryGovernanceEngineName; /* Initialized in constructor */
    private RepositoryGovernanceRESTClient restClient;                     /* Initialized in constructor */

    private InvalidParameterHandler invalidParameterHandler = new InvalidParameterHandler();
    private RESTExceptionHandler    exceptionHandler        = new RESTExceptionHandler();


    /**
     * Create a client-side object for calling a archive engine.
     *
     * @param serverPlatformRootURL the root url of the platform where the archive engine is running.
     * @param serverName the name of the engine host server where the archive engine is running
     * @param repositoryGovernanceEngineName the unique name of the archive engine.
     * @throws InvalidParameterException one of the parameters is null or invalid.
     */
    public RepositoryGovernanceClient(String serverPlatformRootURL,
                                      String serverName,
                                      String repositoryGovernanceEngineName) throws InvalidParameterException
    {
        this.serverPlatformRootURL = serverPlatformRootURL;
        this.serverName            = serverName;
        this.repositoryGovernanceEngineName = repositoryGovernanceEngineName;

        this.restClient = new RepositoryGovernanceRESTClient(serverName, serverPlatformRootURL);
    }


    /**
     * Create a client-side object for calling a archive engine.
     *
     * @param serverPlatformRootURL the root url of the platform where the archive engine is running.
     * @param serverName the name of the engine host server where the archive engine is running
     * @param repositoryGovernanceEngineName the unique name of the archive engine.
     * @param userId user id for the HTTP request
     * @param password password for the HTTP request
     * @throws InvalidParameterException one of the parameters is null or invalid.
     */
    public RepositoryGovernanceClient(String serverPlatformRootURL,
                                      String serverName,
                                      String repositoryGovernanceEngineName,
                                      String userId,
                                      String password) throws InvalidParameterException
    {
        this.serverPlatformRootURL          = serverPlatformRootURL;
        this.serverName                     = serverName;
        this.repositoryGovernanceEngineName = repositoryGovernanceEngineName;

        this.restClient = new RepositoryGovernanceRESTClient(serverName, serverPlatformRootURL, userId, password);
    }


    /**
     * Validate the connector and return its connector type.
     *
     * @param userId calling user
     * @param connectorProviderClassName name of a specific connector or null for all connectors
     *
     * @return connector report for this connector
     *
     * @throws InvalidParameterException the connector provider class name is not a valid connector fo this service
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException there was a problem detected by the integration service
     */
    public ConnectorReport validateConnector(String userId,
                                             String connectorProviderClassName) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        final String methodName = "validateConnector";
        final String nameParameter = "connectorProviderClassName";
        final String urlTemplate = "/servers/{0}/open-metadata/engine-services/repository-governance/users/{1}/validate-connector";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(connectorProviderClassName, nameParameter, methodName);

        ConnectorReportResponse restResult = restClient.callOCFConnectorReportGetRESTCall(methodName,
                                                                                          serverPlatformRootURL + urlTemplate,
                                                                                          serverName,
                                                                                          userId,
                                                                                          connectorProviderClassName);

        return restResult.getConnectorReport();
    }


}
