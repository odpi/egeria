/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.engineservices.repositorygovernance.client.rest;

import org.odpi.openmetadata.commonservices.ffdc.rest.FFDCRESTClient;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.SecretsStoreConnector;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;

import java.util.Map;

/**
 * RepositoryGovernanceRESTClient is responsible for issuing the REST API calls
 */
public class RepositoryGovernanceRESTClient extends FFDCRESTClient
{
    /**
     * Constructor for no authentication with audit log.
     *
     * @param serverName name of the OMAG Server to call
     * @param serverPlatformURLRoot URL root of the server platform where the OMAG Server is running.
     * @param localServerSecretsStoreProvider secrets store connector for bearer token
     * @param localServerSecretsStoreLocation secrets store location for bearer token
     * @param localServerSecretsStoreCollection secrets store collection for bearer token
     * @param auditLog destination for log messages.
     *
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public RepositoryGovernanceRESTClient(String   serverName,
                                          String   serverPlatformURLRoot,
                                          String   localServerSecretsStoreProvider,
                                          String   localServerSecretsStoreLocation,
                                          String   localServerSecretsStoreCollection,
                                          AuditLog auditLog) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, localServerSecretsStoreProvider, localServerSecretsStoreLocation, localServerSecretsStoreCollection, auditLog);
    }


    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName name of the OMAG Server to call
     * @param serverPlatformURLRoot URL root of the server platform where the OMAG Server is running
     * @param secretsStoreConnectorMap connectors to secrets stores
     * @param auditLog destination for log messages
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     *                                       REST API calls.
     */
    public RepositoryGovernanceRESTClient(String                             serverName,
                                          String                             serverPlatformURLRoot,
                                          Map<String, SecretsStoreConnector> secretsStoreConnectorMap,
                                          AuditLog                           auditLog) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, secretsStoreConnectorMap, auditLog);
    }
}
