/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.client;

import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworkservices.ocf.metadatamanagement.client.ConnectedAssetClientBase;

/**
 * ConnectedAssetClient manages the retrieval of connections, and the creation of resource connectors used to access the
 * content of data sources and services.
 */
public class ConnectedAssetClient extends ConnectedAssetClientBase
{
    private final static String serviceURLMarker = "community-profile";

    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName            name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     *
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     *                                   REST API calls.
     */
    public ConnectedAssetClient(String serverName,
                                String serverPlatformURLRoot) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, serviceURLMarker);
    }


    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param serverName            name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param serverUserId          caller's userId embedded in all HTTP requests
     * @param serverPassword        caller's userId embedded in all HTTP requests
     *
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     *                                   REST API calls.
     */
    public ConnectedAssetClient(String serverName,
                                String serverPlatformURLRoot,
                                String serverUserId,
                                String serverPassword) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, serviceURLMarker, serverUserId, serverPassword);
    }
}
