/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.clients;


import org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException;

/**
 * LocalRepositoryServicesClient provides a client interface for calling the local repository
 * services in a remote server.
 */
public class LocalRepositoryServicesClient extends MetadataCollectionServicesClient
{
    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param repositoryName name of the repository to connect to (used in error messages)
     * @param restURLRoot the network address of the server running the repository services.  This is of the form
     *                    serverURLroot + "/servers/" + serverName.
     *
     * @throws InvalidParameterException bad input parameters
     */
    public LocalRepositoryServicesClient(String repositoryName,
                                         String restURLRoot) throws InvalidParameterException
    {
        super(repositoryName, restURLRoot, "/");
    }


    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param repositoryName name of the repository to connect to (used in error messages)
     * @param restURLRoot the network address of the server running the repository services.  This is of the form
     *                    serverURLroot + "/servers/" + serverName.
     * @param userId caller's userId embedded in all HTTP requests
     * @param password caller's userId embedded in all HTTP requests
     *
     * @throws InvalidParameterException bad input parameters
     */
    public LocalRepositoryServicesClient(String     repositoryName,
                                         String     restURLRoot,
                                         String     userId,
                                         String     password) throws InvalidParameterException
    {
        super(repositoryName, restURLRoot, "/", userId, password);
    }
}
