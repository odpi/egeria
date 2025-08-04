/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworkservices.gaf.client;

import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.adminservices.configuration.registration.CommonServicesDescription;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworkservices.gaf.client.rest.GAFRESTClient;

/**
 * GovernanceContextClient sits in the governance context of a governance action service when it is running in the engine host OMAG server.
 * It is however shared by all the governance action services running in an engine service so that we only need one connector to the topic
 * listener for the watchdog governance services.
 */
public class GovernanceContextClient extends GovernanceContextClientBase
{
    private final static String serviceURLMarker = AccessServiceDescription.GAF_METADATA_MANAGEMENT.getServiceURLMarker();

    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName            name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param maxPageSize           pre-initialized parameter limit
     *
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     *                                   REST API calls.
     */
    public GovernanceContextClient(String serverName,
                                   String serverPlatformURLRoot,
                                   int    maxPageSize) throws InvalidParameterException
    {
        super(serviceURLMarker, serverName, serverPlatformURLRoot, maxPageSize);
    }


    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param serverName            name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param serverUserId          caller's userId embedded in all HTTP requests
     * @param serverPassword        caller's password embedded in all HTTP requests
     * @param maxPageSize           pre-initialized parameter limit
     *
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     *                                   REST API calls.
     */
    public GovernanceContextClient(String serverName,
                                   String serverPlatformURLRoot,
                                   String serverUserId,
                                   String serverPassword,
                                   int    maxPageSize) throws InvalidParameterException
    {
        super(serviceURLMarker, serverName, serverPlatformURLRoot, serverUserId, serverPassword, maxPageSize);
    }


    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param serverName            name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param restClient            pre-initialized REST client
     * @param maxPageSize           pre-initialized parameter limit
     *
     * @throws InvalidParameterException there is a problem with the information about the remote OMAS
     */
    public GovernanceContextClient(String        serverName,
                                   String        serverPlatformURLRoot,
                                   GAFRESTClient restClient,
                                   int           maxPageSize) throws InvalidParameterException
    {
        super(serviceURLMarker, serverName, serverPlatformURLRoot, restClient, maxPageSize);
    }
}
