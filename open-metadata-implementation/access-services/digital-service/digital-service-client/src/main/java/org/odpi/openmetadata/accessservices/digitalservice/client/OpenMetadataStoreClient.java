/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.digitalservice.client;

import org.odpi.openmetadata.commonservices.gaf.client.OpenMetadataStoreClientBase;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;

/**
 * OpenMetadataStoreClientBase sits in the governance context of a governance action service when it is running in the engine host OMAG server.
 * It is however shared by all the governance action services running in an engine service so that we only need one connector to the topic
 * listener for the watchdog governance services.
 */
public class OpenMetadataStoreClient extends OpenMetadataStoreClientBase
{
    private final static String serviceURLMarker = "digital-service";

    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName            name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST servers
     *
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     *                                   REST API calls.
     */
    public OpenMetadataStoreClient(String serverName,
                                   String serverPlatformURLRoot) throws InvalidParameterException
    {
        super(serviceURLMarker, serverName, serverPlatformURLRoot);
    }


    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param serverName            name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST servers
     * @param serverUserId          caller's userId embedded in all HTTP requests
     * @param serverPassword        caller's userId embedded in all HTTP requests
     *
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     *                                   REST API calls.
     */
    public OpenMetadataStoreClient(String serverName,
                                   String serverPlatformURLRoot,
                                   String serverUserId,
                                   String serverPassword) throws InvalidParameterException
    {
        super(serviceURLMarker, serverName, serverPlatformURLRoot, serverUserId, serverPassword);
    }
}
