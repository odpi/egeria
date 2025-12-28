/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.multitenant;

import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.metadatasecurity.server.OpenMetadataServerSecurityVerifier;

/**
 * OMAGServerServiceInstanceHandler retrieves information from the instance map for
 * an OMAG server service instance.  The instance map is thread-safe.  Instances are added
 * and removed during server initialization and termination.
 */
public abstract class OMAGServerServiceInstanceHandler
{
    protected  OMAGServerPlatformInstanceMap platformInstanceMap = new OMAGServerPlatformInstanceMap();

    protected String serviceName;


    /**
     * Constructor passes the service name that is used on all calls to this instance.
     *
     * @param serviceName unique identifier for this service with a human meaningful value
     */
    public OMAGServerServiceInstanceHandler(String serviceName)
    {
        this.serviceName = serviceName;
    }


    /**
     * Return the service's official name
     *
     * @return String name
     */
    public String  getServiceName()
    {
        return serviceName;
    }


    /**
     * Return whether a particular server is registered with the platform.
     * This is used by the admin services when finding no instance is not an error.
     *
     * @param userId calling user or null if it is an anonymous request
     * @param delegatingUserId external userId making request
     * @param serverName name of the server
     *
     * @return boolean
     * @throws UserNotAuthorizedException the user is not authorized to issue the request.
     */
    public boolean isServerActive(String    userId,
                                  String    delegatingUserId,
                                  String    serverName) throws UserNotAuthorizedException
    {
        return platformInstanceMap.isServerActive(userId, delegatingUserId, serverName);
    }


    /**
     * Return the security verifier for the server.
     *
     * @param userId calling user or null if it is an anonymous request
     * @param serverName name of the server
     *
     * @return OpenMetadataServerSecurityVerifier object - never null
     * @throws InvalidParameterException the server name is not known
     */
    public OpenMetadataServerSecurityVerifier getServerSecurityVerifier(String    userId,
                                                                        String    serverName) throws InvalidParameterException

    {
        return platformInstanceMap.getServerSecurityVerifier(userId, serverName);
    }


    /**
     * Get the object containing the properties for this server.
     *
     * @param userId calling user
     * @param delegatingUserId external userId making request
     * @param serverName name of this server
     * @param serviceOperationName name of the REST API call (typically the top-level methodName)
     * @return specific service instance
     * @throws InvalidParameterException the server name is not known
     * @throws UserNotAuthorizedException the user is not authorized to issue the request.
     * @throws PropertyServerException the service name is not known - indicating a logic error
     */
    protected  OMAGServerServiceInstance getServerServiceInstance(String  userId,
                                                                  String  delegatingUserId,
                                                                  String  serverName,
                                                                  String  serviceOperationName) throws InvalidParameterException,
                                                                                                       UserNotAuthorizedException,
                                                                                                       PropertyServerException
    {
        return platformInstanceMap.getServiceInstance(userId, delegatingUserId, serverName, serviceName, serviceOperationName);
    }


    /**
     * Get the object containing the properties for this server.
     *
     * @param userId calling user
     * @param serverName name of this server
     * @param serviceOperationName name of the REST API call (typically the top-level methodName)
     * @return specific service instance
     * @throws InvalidParameterException the server name is not known
     * @throws UserNotAuthorizedException the user is not authorized to issue the request.
     * @throws PropertyServerException the service name is not known - indicating a logic error
     */
    protected  OMAGServerServiceInstance getServerServiceInstance(String  userId,
                                                                  String  serverName,
                                                                  String  serviceOperationName) throws InvalidParameterException,
                                                                                                       UserNotAuthorizedException,
                                                                                                       PropertyServerException
    {
        return platformInstanceMap.getServiceInstance(userId, serverName, serviceName, serviceOperationName);
    }


    /**
     * Remove the instance for this server.
     *
     * @param serverName name of the server
     */
    public void removeServerServiceInstance(String  serverName)
    {
        platformInstanceMap.removeServiceInstanceFromPlatform(serverName, serviceName);
    }
}
