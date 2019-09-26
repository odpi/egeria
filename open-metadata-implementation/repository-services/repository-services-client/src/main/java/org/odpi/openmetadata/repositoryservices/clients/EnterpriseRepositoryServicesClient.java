/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.clients;


import org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException;

/**
 * EnterpriseRepositoryServicesClient provides a client interface for calling the enterprise repository
 * services in a remote server.
 */
public class EnterpriseRepositoryServicesClient extends MetadataCollectionServicesClient
{
    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName name of the server to connect to
     * @param restURLRoot the network address of the server running the repository services.  This is of the form
     *                    serverURLroot + "/servers/" + serverName.
     *
     * @throws InvalidParameterException bad input parameters
     */
    public EnterpriseRepositoryServicesClient(String serverName,
                                              String restURLRoot) throws InvalidParameterException
    {
        super(serverName,restURLRoot, "/enterprise/");
    }


    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param serverName name of the server to connect to
     * @param restURLRoot the network address of the server running the repository services.  This is of the form
     *                    serverURLroot + "/servers/" + serverName.
     * @param userId caller's userId embedded in all HTTP requests
     * @param password caller's userId embedded in all HTTP requests
     *
     * @throws InvalidParameterException bad input parameters
     */
    public EnterpriseRepositoryServicesClient(String     serverName,
                                              String     restURLRoot,
                                              String     userId,
                                              String     password) throws InvalidParameterException
    {
        super(serverName,restURLRoot, "/enterprise/", userId, password);
    }
}
