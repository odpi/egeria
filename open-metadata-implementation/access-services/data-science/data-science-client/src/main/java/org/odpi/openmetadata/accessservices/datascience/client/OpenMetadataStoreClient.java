/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.datascience.client;

import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworkservices.omf.client.OpenMetadataClientBase;

/**
 * OpenMetadataStoreClient provides an interface to the open metadata store.  This is part of the Governance Action Framework (GAF)
 * and provides a comprehensive interface for working with all types of metadata, subject to the user's (and this OMAS's) security permissions.
 * The interface supports search, maintenance of metadata elements, classifications and relationships plus the ability to raise incident reports
 * and todos along with the ability to work with metadata valid values and translations.
 */
public class OpenMetadataStoreClient extends OpenMetadataClientBase
{
    private final static String serviceURLMarker = AccessServiceDescription.DATA_SCIENCE_OMAS.getAccessServiceURLMarker();

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
    public OpenMetadataStoreClient(String serverName,
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
     * @param maxPageSize maximum value allowed for page size
     *
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     *                                   REST API calls.
     */
    public OpenMetadataStoreClient(String serverName,
                                   String serverPlatformURLRoot,
                                   String serverUserId,
                                   String serverPassword,
                                   int    maxPageSize) throws InvalidParameterException
    {
        super(serviceURLMarker, serverName, serverPlatformURLRoot, serverUserId, serverPassword, maxPageSize);
    }
}
