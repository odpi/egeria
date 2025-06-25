/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.digitalservice.client;

import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.frameworks.openmetadata.enums.CollectionMemberStatus;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.properties.collections.CollectionMembershipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.collections.CollectionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.digitalbusiness.AgreementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.digitalbusiness.DigitalProductProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.ElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

/**
 * OpenMetadataBaseClient supports the common properties and functions for the open metadata based clients.
 */
public abstract class OpenMetadataBaseClient
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
    public OpenMetadataBaseClient(String serverName,
                                  String serverPlatformURLRoot,
                                  int    maxPageSize) throws InvalidParameterException
    {
        this.openMetadataStoreClient = new OpenMetadataStoreClient(serverName, serverPlatformURLRoot, maxPageSize);
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
    public OpenMetadataBaseClient(String serverName,
                                  String serverPlatformURLRoot,
                                  String userId,
                                  String password,
                                  int    maxPageSize) throws InvalidParameterException
    {
        this.openMetadataStoreClient = new OpenMetadataStoreClient(serverName, serverPlatformURLRoot, userId, password, maxPageSize);
        this.invalidParameterHandler.setMaxPagingSize(maxPageSize);
    }
}
