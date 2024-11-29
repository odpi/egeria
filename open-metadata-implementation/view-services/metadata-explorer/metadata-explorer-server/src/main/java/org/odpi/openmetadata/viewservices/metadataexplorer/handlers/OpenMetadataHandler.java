/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.viewservices.metadataexplorer.handlers;

import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworkservices.gaf.client.OpenMetadataClientBase;
import org.odpi.openmetadata.frameworkservices.gaf.client.rest.GAFRESTClient;

public class OpenMetadataHandler extends OpenMetadataClientBase
{
    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param serverName            name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param maxPageSize           maximum value allowed for page size
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     *                                   REST API calls.
     */
    public OpenMetadataHandler(String serviceURLMarker,
                               String serverName,
                               String serverPlatformURLRoot, int maxPageSize) throws InvalidParameterException
    {
        super(serviceURLMarker, serverName, serverPlatformURLRoot, maxPageSize);
    }

    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param serverName            name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param serverUserId          caller's userId embedded in all HTTP requests
     * @param serverPassword        caller's password embedded in all HTTP requests
     * @param maxPageSize           maximum value allowed for page size
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     *                                   REST API calls.
     */
    public OpenMetadataHandler(String serviceURLMarker, String serverName, String serverPlatformURLRoot, String serverUserId, String serverPassword, int maxPageSize) throws InvalidParameterException
    {
        super(serviceURLMarker, serverName, serverPlatformURLRoot, serverUserId, serverPassword, maxPageSize);
    }

    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param serverName            name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param restClient            pre-initialized REST client
     * @param maxPageSize           pre-initialized parameter limit
     * @throws InvalidParameterException there is a problem with the information about the remote OMAS
     */
    public OpenMetadataHandler(String serviceURLMarker, String serverName, String serverPlatformURLRoot, GAFRESTClient restClient, int maxPageSize) throws InvalidParameterException
    {
        super(serviceURLMarker, serverName, serverPlatformURLRoot, restClient, maxPageSize);
    }
}
