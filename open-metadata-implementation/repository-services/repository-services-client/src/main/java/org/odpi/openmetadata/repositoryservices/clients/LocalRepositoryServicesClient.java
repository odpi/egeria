/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.clients;


import org.odpi.openmetadata.frameworks.connectors.SecretsStoreConnector;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;

import java.util.Map;

/**
 * LocalRepositoryServicesClient provides a client interface for calling the local repository
 * services in a remote server.
 */
public class LocalRepositoryServicesClient extends MetadataCollectionServicesClient
{

    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param repositoryName name of the repository to connect to (used in error messages)
     * @param restURLRoot the network address of the server running the repository services.  This is of the form
     *                    serverURLroot + "/servers/" + serverName.
     * @param secretsStoreConnectorMap secrets to create bearer token
     *
     * @throws InvalidParameterException bad input parameters
     */
    public LocalRepositoryServicesClient(String                             repositoryName,
                                         String                             restURLRoot,
                                         Map<String, SecretsStoreConnector> secretsStoreConnectorMap) throws InvalidParameterException
    {
        super(repositoryName, restURLRoot, "/", secretsStoreConnectorMap);
    }
}
