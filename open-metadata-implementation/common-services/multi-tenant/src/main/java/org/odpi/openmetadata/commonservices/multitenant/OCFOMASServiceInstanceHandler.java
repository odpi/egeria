/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.multitenant;

import org.odpi.openmetadata.commonservices.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.commonservices.ffdc.exceptions.PropertyServerException;
import org.odpi.openmetadata.commonservices.ffdc.exceptions.UserNotAuthorizedException;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.handlers.*;


/**
 * Provide access to the common handlers for OMAS's that use the OCF beans.
 */
public abstract class OCFOMASServiceInstanceHandler extends OMASServiceInstanceHandler
{
    /**
     * Constructor
     *
     * @param serviceName name of the service
     */
    public OCFOMASServiceInstanceHandler(String serviceName)
    {
        super(serviceName);
    }


    /**
     * Retrieve the specific handler for the access service.
     *
     * @param userId calling user
     * @param serverName name of the server tied to the request
     * @param serviceOperationName name of the REST API call (typically the top-level methodName)
     * @return handler for use by the requested instance
     * @throws InvalidParameterException no available instance for the requested server
     * @throws UserNotAuthorizedException user does not have access to the requested server
     * @throws PropertyServerException error in the requested server
     */
    public AssetHandler getAssetHandler(String userId,
                                        String serverName,
                                        String serviceOperationName) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException
    {

        OCFOMASServiceInstance instance = (OCFOMASServiceInstance)super.getServerServiceInstance(userId,
                                                                                                 serverName,
                                                                                                 serviceOperationName);

        if (instance != null)
        {
            return instance.getAssetHandler();
        }

        return null;
    }
}
