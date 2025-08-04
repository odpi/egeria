/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworkservices.oif.server;

import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.commonservices.generichandlers.SoftwareCapabilityHandler;
import org.odpi.openmetadata.commonservices.multitenant.AccessServerServiceInstanceHandler;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;

/**
 * OIFServicesInstanceHandler retrieves information from the instance map for the
 * framework service instances.  The instance map is thread-safe.  Instances are added
 * and removed by the OIFOperationalServices class.
 */
public class OIFServicesInstanceHandler extends AccessServerServiceInstanceHandler
{
    /**
     * Default constructor registers the framework service
     */
    public OIFServicesInstanceHandler()
    {
        super(AccessServiceDescription.OIF_METADATA_MANAGEMENT.getServiceName());

        OIFServicesRegistration.registerAccessService();
    }


    /**
     * Retrieve the specific handler for the framework service.
     *
     * @param userId calling user
     * @param serverName name of the server tied to the request
     * @param serviceOperationName name of the REST API call (typically the top-level methodName)
     * @return handler for use by the requested instance
     * @throws InvalidParameterException no available instance for the requested server
     * @throws UserNotAuthorizedException user does not have access to the requested server
     * @throws PropertyServerException error in the requested server
     */
    SoftwareCapabilityHandler<Object> getMetadataSourceHandler(String userId,
                                                               String serverName,
                                                               String serviceOperationName) throws InvalidParameterException,
                                                                                                   UserNotAuthorizedException,
                                                                                                   PropertyServerException
    {
        OIFServicesInstance instance = (OIFServicesInstance)super.getServerServiceInstance(userId, serverName, serviceOperationName);

        if (instance != null)
        {
            return instance.getMetadataSourceHandler();
        }

        return null;
    }
}
