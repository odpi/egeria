/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetowner.client;

import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworkservices.gaf.client.OpenGovernanceClientBase;
import org.odpi.openmetadata.frameworkservices.gaf.client.OpenMetadataClientBase;

/**
 * OpenGovernanceClient provides an interface to the services that build, monitor and trigger governance actions.
 * This is part of the Governance Action Framework (GAF).
 */
public class OpenGovernanceClient extends OpenGovernanceClientBase
{
    private final static String serviceURLMarker = AccessServiceDescription.ASSET_OWNER_OMAS.getAccessServiceURLMarker();

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
    public OpenGovernanceClient(String serverName,
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
    public OpenGovernanceClient(String serverName,
                                String serverPlatformURLRoot,
                                String serverUserId,
                                String serverPassword,
                                int    maxPageSize) throws InvalidParameterException
    {
        super(serviceURLMarker, serverName, serverPlatformURLRoot, serverUserId, serverPassword, maxPageSize);
    }
}
