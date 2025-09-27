/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.client;


import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;


/**
 * OpenIntegrationClient defines the interface of the client that implements calls to the metadata server.
 */
public abstract class OpenIntegrationClient
{
    protected final String   serverName;               /* Initialized in constructor */
    protected final String   serverPlatformURLRoot;    /* Initialized in constructor */
    protected final String   serviceURLMarker;         /* Initialized in constructor */


    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param serverName            name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     */
    public OpenIntegrationClient(String serviceURLMarker,
                                 String serverName,
                                 String serverPlatformURLRoot)
    {
        this.serviceURLMarker = serviceURLMarker;
        this.serverName = serverName;
        this.serverPlatformURLRoot = serverPlatformURLRoot;
    }


    /**
     * Retrieve the unique identifier of the metadata element that represents the metadata source.
     *
     * @param userId calling user
     * @param qualifiedName unique name of the metadata source
     *
     * @return unique identifier of the metadata source
     *
     * @throws InvalidParameterException  the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public abstract String getMetadataSourceGUID(String  userId,
                                                 String  qualifiedName) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException;


    /**
     * Create a new metadata element to represent a software capability.  This describes the metadata source.
     *
     * @param userId calling user
     * @param softwareCapabilityTypeName name of software capability type to describe the metadata source
     * @param classificationName optional classification name that refines the type of the software capability.
     * @param qualifiedName unique name for the external source
     * @param deployedImplementationType type of technology
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public abstract String createMetadataOrigin(String userId,
                                                String softwareCapabilityTypeName,
                                                String classificationName,
                                                String qualifiedName,
                                                String deployedImplementationType) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException;
}
