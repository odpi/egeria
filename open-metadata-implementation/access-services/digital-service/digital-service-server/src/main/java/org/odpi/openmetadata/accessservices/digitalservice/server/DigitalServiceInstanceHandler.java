/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.digitalservice.server;

import org.odpi.openmetadata.accessservices.digitalservice.handlers.DigitalServiceEntityHandler;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.commonservices.multitenant.OMASServiceInstanceHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;


/**
 * DigitalServiceInstanceHandler retrieves information from the instance map for the
 * access service instances.  The instance map is thread-safe.  Instances are added
 * and removed by the DigitalServiceAdmin class.
 */
class DigitalServiceInstanceHandler extends OMASServiceInstanceHandler
{
    /**
     * Default constructor registers the access service
     */
    DigitalServiceInstanceHandler()
    {
        super(AccessServiceDescription.DIGITAL_SERVICE_OMAS.getAccessServiceFullName());

        DigitalServiceRegistration.registerAccessService();
    }

    /**
     * Retrieve the DigitalServiceEntityHandler for the access service
     *
     * @param userId     calling user
     * @param serverName name of the server tied to the request
     * @return handler for use by the requested instance
     * @throws InvalidParameterException  no available instance for the requested server
     * @throws UserNotAuthorizedException user does not have access to the requested server
     * @throws PropertyServerException    the service name is not known - indicating a logic error
     */
    public DigitalServiceEntityHandler getDigitalServiceEntityHandler(String userId, String serverName, String serviceOperationName) throws
            InvalidParameterException,
            UserNotAuthorizedException,
            PropertyServerException {

        DigitalServiceServicesInstance instance = (DigitalServiceServicesInstance) super.getServerServiceInstance(userId,
                serverName, serviceOperationName);

        if (instance != null) { return instance.getDigitalServiceEntityHandler(); }

        return null;
    }

}
