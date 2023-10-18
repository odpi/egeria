/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.digitalservice.client;

import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.governanceaction.search.PropertyHelper;

/**
 * DigitalServiceBaseClient supports the common properties and functions for the Digital Service OMAS.
 */
public class DigitalServiceBaseClient
{
    final protected InvalidParameterHandler invalidParameterHandler = new InvalidParameterHandler();
    final protected PropertyHelper          propertyHelper          = new PropertyHelper();
    final protected OpenMetadataStoreClient openMetadataStoreClient;   /* Initialized in constructor */


    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName            name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param maxPageSize           number of elements that can be returned on a call
     *
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any REST API calls.
     */
    public DigitalServiceBaseClient(String serverName,
                                    String serverPlatformURLRoot,
                                    int    maxPageSize) throws InvalidParameterException
    {
        this.openMetadataStoreClient = new OpenMetadataStoreClient(serverName, serverPlatformURLRoot);
        this.invalidParameterHandler.setMaxPagingSize(maxPageSize);
    }


    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param serverName            name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param userId                caller's userId embedded in all HTTP requests
     * @param password              caller's userId embedded in all HTTP requests
     * @param maxPageSize           number of elements that can be returned on a call
     *
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any REST API calls.
     */
    public DigitalServiceBaseClient(String serverName,
                                    String serverPlatformURLRoot,
                                    String userId,
                                    String password,
                                    int    maxPageSize) throws InvalidParameterException
    {
        this.openMetadataStoreClient = new OpenMetadataStoreClient(serverName, serverPlatformURLRoot, userId, password);
        this.invalidParameterHandler.setMaxPagingSize(maxPageSize);
    }
}
