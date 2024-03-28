/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetcatalog;

import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworkservices.oif.client.OpenIntegrationServiceBase;

/**
 * OpenIntegrationServiceClient provides an interface to the open integration service. This is part of the Open Integration Framework (OIF)
 * and provides an interface for understanding the work of the integration connectors that are synchronizing metadata.
 */
public class OpenIntegrationServiceClient extends OpenIntegrationServiceBase
{
    private final static String serviceURLMarker = "asset-catalog";

    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName            name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param maxPageSize maximum value allowed for page size
     *
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     *                                   REST API calls.
     */
    public OpenIntegrationServiceClient(String serverName,
                                        String serverPlatformURLRoot,
                                        int    maxPageSize) throws InvalidParameterException
    {
        super(serviceURLMarker, serverName, serverPlatformURLRoot, maxPageSize);
    }


    /**
     * Create a new client that passes userID and password in each HTTP request. This is the
     * userID/password of the calling server. The end user's userID is sent on each request.
     *
     * @param serverName            name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param serverUserId          caller's userID embedded in all HTTP requests
     * @param serverPassword        caller's password embedded in all HTTP requests
     * @param maxPageSize maximum value allowed for page size
     *
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     *                                   REST API calls.
     */
    public OpenIntegrationServiceClient(String serverName,
                                        String serverPlatformURLRoot,
                                        String serverUserId,
                                        String serverPassword,
                                        int    maxPageSize) throws InvalidParameterException
    {
        super(serviceURLMarker, serverName, serverPlatformURLRoot, serverUserId, serverPassword, maxPageSize);
    }
}
