/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.securityofficer.server.services;

import org.odpi.openmetadata.accessservices.securityofficer.server.handler.GovernedAssetHandler;
import org.odpi.openmetadata.accessservices.securityofficer.server.handler.SecurityOfficerRequestHandler;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.commonservices.multitenant.OMASServiceInstanceHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

/**
 * GovernanceEngineServiceInstanceHandler retrieves information from the instance map for the
 * access service instances.  The instance map is thread-safe.  Instances are added
 * and removed by the GovernanceEngineAdmin class.
 */
public class SecurityOfficerInstanceHandler extends OMASServiceInstanceHandler
{
    /**
     * Default constructor registers the access service
     */
    public SecurityOfficerInstanceHandler() {
        super(AccessServiceDescription.SECURITY_OFFICER_OMAS.getAccessServiceFullName());

        SecurityOfficerRegistration.registerAccessService();
    }


    /**
     * Retrieve the governance assets handler for the access service
     *
     * @param userId               calling user
     * @param serverName           name of the server tied to the request
     * @param serviceOperationName name of the REST API call (typically the top-level methodName)
     * @return handler for use by the requested instance
     * @throws InvalidParameterException  no available instance for the requested server
     * @throws UserNotAuthorizedException user does not have access to the requested server
     * @throws PropertyServerException    the service name is not known - indicating a logic error
     */
    public SecurityOfficerRequestHandler getSecurityOfficerRequestHandler(String userId, String serverName, String serviceOperationName)
            throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException
    {
        SecurityOfficerInstance instance = (SecurityOfficerInstance) super.getServerServiceInstance(userId, serverName, serviceOperationName);

        return instance.getSecurityOfficerRequestHandler();
    }


    /**
     * Retrieve the governance assets handler for the access service
     *
     * @param userId               calling user
     * @param serverName           name of the server tied to the request
     * @param serviceOperationName name of the REST API call (typically the top-level methodName)
     * @return handler for use by the requested instance
     * @throws InvalidParameterException  no available instance for the requested server
     * @throws UserNotAuthorizedException user does not have access to the requested server
     * @throws PropertyServerException    the service name is not known - indicating a logic error
     */
    public GovernedAssetHandler getGovernedAssetHandler(String userId, String serverName, String serviceOperationName)
            throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException
    {
        SecurityOfficerInstance instance = (SecurityOfficerInstance) super.getServerServiceInstance(userId, serverName, serviceOperationName);

        return instance.getGovernedAssetHandler();
    }
}
