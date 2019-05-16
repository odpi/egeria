/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.multitenant;

import org.odpi.openmetadata.commonservices.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.commonservices.ffdc.exceptions.PropertyServerException;
import org.odpi.openmetadata.commonservices.ffdc.exceptions.UserNotAuthorizedException;

/**
 * OMAGServerServiceInstanceHandler retrieves information from the instance map for
 * an OMAG server service instance.  The instance map is thread-safe.  Instances are added
 * and removed during server initialization and termination.
 */
public abstract class OMAGServerServiceInstanceHandler
{
    private  OMAGServerPlatformInstanceMap platformInstanceMap = new OMAGServerPlatformInstanceMap();

    protected String serviceName;

    /**
     * Constructor passes the service name that is used on all calls to this instance.
     *
     * @param serviceName unique identifier for this service with a human meaningful value
     */
    public OMAGServerServiceInstanceHandler(String       serviceName)
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
     * This is used by the admin services when there being no instance is not an error.
     *
     * @param userId calling user or null if it is an anonymous request
     * @param serverName name of the server
     *
     * @return boolean
     * @throws UserNotAuthorizedException the user is not authorized to issue the request.
     */
    public boolean isServerKnown(String    userId,
                                 String    serverName) throws UserNotAuthorizedException
    {
        return platformInstanceMap.isServerKnown(userId, serverName);
    }


    /**
     * Get the object containing the properties for this server.
     *
     * @param userId calling user
     * @param serverName name of this server
     * @return specific service instance
     * @throws InvalidParameterException the server name is not known
     * @throws UserNotAuthorizedException the user is not authorized to issue the request.
     * @throws PropertyServerException the service name is not known - indicating a logic error
     */
    protected  OMAGServerServiceInstance getServerServiceInstance(String  userId,
                                                                  String  serverName) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException
    {
        return platformInstanceMap.getServiceInstance(userId, serverName, serviceName);
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
