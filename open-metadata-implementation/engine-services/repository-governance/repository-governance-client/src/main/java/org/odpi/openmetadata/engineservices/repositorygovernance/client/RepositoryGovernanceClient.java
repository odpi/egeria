/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.engineservices.repositorygovernance.client;


import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.ffdc.properties.ConnectorReport;
import org.odpi.openmetadata.commonservices.ffdc.rest.ConnectorReportResponse;
import org.odpi.openmetadata.engineservices.repositorygovernance.api.RepositoryGovernanceAPI;
import org.odpi.openmetadata.engineservices.repositorygovernance.client.rest.RepositoryGovernanceRESTClient;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.SecretsStoreConnector;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;

import java.util.Map;


/**
 * RepositoryGovernanceClient is a client-side library for calling a specific archive engine with an engine host server.
 */
public class RepositoryGovernanceClient  implements RepositoryGovernanceAPI
{
    private final String                   serverName;                     /* Initialized in constructor */
    private final String                   serverPlatformRootURL;          /* Initialized in constructor */
    private final String                   delegatingUserId;               /* Initialized in constructor */
    private RepositoryGovernanceRESTClient restClient;                     /* Initialized in constructor */

    private InvalidParameterHandler invalidParameterHandler = new InvalidParameterHandler();


    /**
     * Create a client-side object for calling a archive engine.
     *
     * @param serverPlatformRootURL the root url of the platform where the archive engine is running.
     * @param serverName the name of the engine host server where the archive engine is running
     * @param localServerSecretsStoreProvider secrets store connector for bearer token
     * @param localServerSecretsStoreLocation secrets store location for bearer token
     * @param localServerSecretsStoreCollection secrets store collection for bearer token
     * @param delegatingUserId external userId making request
     * @param auditLog destination for log messages
     * @throws InvalidParameterException one of the parameters is null or invalid.
     */
    public RepositoryGovernanceClient(String   serverPlatformRootURL,
                                      String   serverName,
                                      String   localServerSecretsStoreProvider,
                                      String   localServerSecretsStoreLocation,
                                      String   localServerSecretsStoreCollection,
                                      String   delegatingUserId,
                                      AuditLog auditLog) throws InvalidParameterException
    {
        this.serverPlatformRootURL = serverPlatformRootURL;
        this.serverName            = serverName;
        this.delegatingUserId      = delegatingUserId;

        this.restClient = new RepositoryGovernanceRESTClient(serverName,
                                                             serverPlatformRootURL,
                                                             localServerSecretsStoreProvider,
                                                             localServerSecretsStoreLocation,
                                                             localServerSecretsStoreCollection,
                                                             auditLog);
    }


    /**
     * Create a new client with bearer token from supplied secrets store.
     *
     * @param serverPlatformRootURL the root url of the platform where the governance action engine is running.
     * @param serverName the name of the engine host server where the governance action engine is running
     * @param secretsStoreConnectorMap connectors to secrets stores
     * @param delegatingUserId external userId making request
     * @param auditLog destination for log messages.
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public RepositoryGovernanceClient(String                             serverPlatformRootURL,
                                      String                             serverName,
                                      Map<String, SecretsStoreConnector> secretsStoreConnectorMap,
                                      String                             delegatingUserId,
                                      AuditLog                           auditLog) throws InvalidParameterException
    {
        this.serverPlatformRootURL = serverPlatformRootURL;
        this.serverName            = serverName;
        this.delegatingUserId      = delegatingUserId;

        this.restClient = new RepositoryGovernanceRESTClient(serverName,
                                                             serverPlatformRootURL,
                                                             secretsStoreConnectorMap,
                                                             auditLog);
    }


    /**
     * Validate the connector and return its connector type.
     *
     * @param connectorProviderClassName name of a specific connector or null for all connectors
     *
     * @return connector report for this connector
     *
     * @throws InvalidParameterException the connector provider class name is not a valid connector fo this service
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException there was a problem detected by the integration service
     */
    public ConnectorReport validateConnector(String connectorProviderClassName) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        final String methodName = "validateConnector";
        final String nameParameter = "connectorProviderClassName";
        final String urlTemplate = "/servers/{0}/open-metadata/engine-services/repository-governance/validate-connector/{1}?delegatingUserId={2}";

        invalidParameterHandler.validateName(connectorProviderClassName, nameParameter, methodName);

        ConnectorReportResponse restResult = restClient.callOCFConnectorReportGetRESTCall(methodName,
                                                                                          serverPlatformRootURL + urlTemplate,
                                                                                          serverName,
                                                                                          connectorProviderClassName,
                                                                                          delegatingUserId);

        return restResult.getConnectorReport();
    }
}
