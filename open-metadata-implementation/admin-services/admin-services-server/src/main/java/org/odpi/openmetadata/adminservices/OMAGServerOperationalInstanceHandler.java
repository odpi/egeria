/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices;

import org.odpi.openmetadata.commonservices.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.commonservices.ffdc.exceptions.PropertyServerException;
import org.odpi.openmetadata.commonservices.ffdc.exceptions.UserNotAuthorizedException;
import org.odpi.openmetadata.commonservices.multitenant.OMAGServerServiceInstanceHandler;

/**
 * OMAGServerServiceInstanceHandler retrieves information from the instance map for
 * an OMAG server service instance.  The instance map is thread-safe.  Instances are added
 * and removed during server initialization and termination.
 */
public class OMAGServerOperationalInstanceHandler extends OMAGServerServiceInstanceHandler
{
    /**
     * Constructor passes the service name that is used on all calls to this instance.
     *
     * @param serviceName unique identifier for this service with a human meaningful value
     */

    public OMAGServerOperationalInstanceHandler(String serviceName)
    {
        super(serviceName);
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
    @Override
    public OMAGOperationalServicesInstance getServerServiceInstance(String userId,
                                                                    String serverName,
                                                                    String serviceOperationName) throws InvalidParameterException,
                                                                                                        UserNotAuthorizedException,
                                                                                                        PropertyServerException
    {
        return (OMAGOperationalServicesInstance)super.getServerServiceInstance(userId, serverName, serviceOperationName);
    }
}
