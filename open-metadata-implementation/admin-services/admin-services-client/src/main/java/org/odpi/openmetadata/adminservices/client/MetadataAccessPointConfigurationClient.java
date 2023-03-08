/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adminservices.client;


import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGInvalidParameterException;


/**
 * MetadataAccessPointConfigurationClient configures a MetadataAccessPoint OMAG Server.  This server
 * can become a cohort member and, through the access services, offers a wide range of specialist APIs
 * and event streams to access and store metadata.
 */
public class MetadataAccessPointConfigurationClient extends MetadataAccessServerConfigurationClient
{
    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param adminUserId           administrator's (end user's) userId to associate with calls.
     * @param serverName            name of the server to connect to
     * @param serverPlatformRootURL the network address of the server running the admin services
     * @throws OMAGInvalidParameterException there is a problem creating the client-side components to issue any
     *                                       REST API calls.
     */
    public MetadataAccessPointConfigurationClient(String adminUserId,
                                                  String serverName,
                                                  String serverPlatformRootURL) throws OMAGInvalidParameterException
    {
        super(adminUserId, serverName, serverPlatformRootURL);
    }


    /**
     * Create a new client that passes a connection userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is passed as the admin userId.
     *
     * @param adminUserId           administrator's (end user's) userId to associate with calls.
     * @param serverName            name of the server to connect to
     * @param serverPlatformRootURL the network address of the server running the admin services
     * @param connectionUserId      caller's system userId embedded in all HTTP requests
     * @param connectionPassword    caller's system password embedded in all HTTP requests
     * @throws OMAGInvalidParameterException there is a problem creating the client-side components to issue any
     *                                       REST API calls.
     */
    public MetadataAccessPointConfigurationClient(String adminUserId,
                                                  String serverName,
                                                  String serverPlatformRootURL,
                                                  String connectionUserId,
                                                  String connectionPassword) throws OMAGInvalidParameterException
    {
        super(adminUserId, serverName, serverPlatformRootURL, connectionUserId, connectionPassword);
    }
}
