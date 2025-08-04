/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworkservices.ocf.metadatamanagement.server;

import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.commonservices.generichandlers.*;
import org.odpi.openmetadata.commonservices.multitenant.AccessServerServiceInstanceHandler;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.*;


/**
 * ConnectedAssetInstanceHandler retrieves information from the instance map for the
 * access service instances.  The instance map is thread-safe.  Instances are added
 * and removed by the ConnectedAssetAdmin class.
 */
public class OCFServicesInstanceHandler extends AccessServerServiceInstanceHandler
{
    /**
     * Default constructor registers the access service
     */
    public OCFServicesInstanceHandler()
    {
        super(AccessServiceDescription.OCF_METADATA_MANAGEMENT.getServiceName());

        OCFServicesRegistration.registerAccessService();
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
    ConnectionHandler<Connection> getConnectionHandler(String userId,
                                                       String serverName,
                                                       String serviceOperationName) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException
    {
        OCFServicesInstance instance = (OCFServicesInstance)super.getServerServiceInstance(userId,
                                                                                           serverName,
                                                                                           serviceOperationName);

        if (instance != null)
        {
            return instance.getConnectionHandler();
        }

        return null;
    }
}
