/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.stewardshipaction.server;

import org.odpi.openmetadata.accessservices.stewardshipaction.converters.ElementStubConverter;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.commonservices.generichandlers.ReferenceableHandler;
import org.odpi.openmetadata.commonservices.multitenant.OMASServiceInstanceHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementStub;


/**
 * StewardshipActionInstanceHandler retrieves information from the instance map for the
 * access service instances.  The instance map is thread-safe.  Instances are added
 * and removed by the StewardshipActionAdmin class.
 */
class StewardshipActionInstanceHandler extends OMASServiceInstanceHandler
{
    /**
     * Default constructor registers the access service
     */
    StewardshipActionInstanceHandler()
    {
        super(AccessServiceDescription.STEWARDSHIP_ACTION_OMAS.getAccessServiceFullName());

        StewardshipActionRegistration.registerAccessService();
    }



    /**
     * Retrieve the specific converter for the access service.
     *
     * @param userId calling user
     * @param serverName name of the server tied to the request
     * @param serviceOperationName name of the REST API call (typically the top-level methodName)
     * @return converter for use by the requested instance
     * @throws InvalidParameterException no available instance for the requested server
     * @throws UserNotAuthorizedException user does not have access to the requested server
     * @throws PropertyServerException the service name is not known - indicating a logic error
     */
    ElementStubConverter<ElementStub> getElementStubConverter(String userId,
                                                              String serverName,
                                                              String serviceOperationName) throws InvalidParameterException,
                                                                                                  UserNotAuthorizedException,
                                                                                                  PropertyServerException
    {
        StewardshipActionServicesInstance instance = (StewardshipActionServicesInstance)super.getServerServiceInstance(userId,
                                                                                                                       serverName,
                                                                                                                       serviceOperationName);

        if (instance != null)
        {
            return instance.getElementStubConverter();
        }

        return null;
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
     * @throws PropertyServerException the service name is not known - indicating a logic error
     */
    ReferenceableHandler<ElementStub> getReferenceableHandler(String userId,
                                                              String serverName,
                                                              String serviceOperationName) throws InvalidParameterException,
                                                                                                 UserNotAuthorizedException,
                                                                                                 PropertyServerException
    {
        StewardshipActionServicesInstance instance = (StewardshipActionServicesInstance)super.getServerServiceInstance(userId,
                                                                                                                       serverName,
                                                                                                                       serviceOperationName);

        if (instance != null)
        {
            return instance.getReferenceableHandler();
        }

        return null;
    }

}
