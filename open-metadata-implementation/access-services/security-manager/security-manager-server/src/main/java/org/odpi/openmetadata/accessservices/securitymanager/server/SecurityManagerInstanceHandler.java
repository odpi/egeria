/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.securitymanager.server;

import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.commonservices.generichandlers.*;
import org.odpi.openmetadata.commonservices.multitenant.OMASServiceInstanceHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.*;

class SecurityManagerInstanceHandler extends OMASServiceInstanceHandler
{
    /**
     * Default constructor registers the access service
     */
    SecurityManagerInstanceHandler()
    {
        super(AccessServiceDescription.SECURITY_MANAGER_OMAS.getAccessServiceFullName());

        SecurityManagerOMASRegistration.registerAccessService();
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
    SoftwareCapabilityHandler<SecurityManagerElement> getSoftwareCapabilityHandler(String userId,
                                                                                   String serverName,
                                                                                   String serviceOperationName) throws InvalidParameterException,
                                                                                                                       UserNotAuthorizedException,
                                                                                                                       PropertyServerException
    {
        SecurityManagerServicesInstance instance = (SecurityManagerServicesInstance)super.getServerServiceInstance(userId,
                                                                                                                   serverName,
                                                                                                                   serviceOperationName);

        if (instance != null)
        {
            return instance.getSoftwareCapabilityHandler();
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
    GovernanceDefinitionHandler<SecurityGroupElement> getSecurityGroupHandler(String userId,
                                                                              String serverName,
                                                                              String serviceOperationName) throws InvalidParameterException,
                                                                                                                  UserNotAuthorizedException,
                                                                                                                  PropertyServerException
    {
        SecurityManagerServicesInstance instance = (SecurityManagerServicesInstance)super.getServerServiceInstance(userId,
                                                                                                                     serverName,
                                                                                                                     serviceOperationName);

        if (instance != null)
        {
            return instance.getSecurityGroupHandler();
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
    UserIdentityHandler<UserIdentityElement> getUserIdentityHandler(String userId,
                                                                    String serverName,
                                                                    String serviceOperationName) throws InvalidParameterException,
                                                                                                        UserNotAuthorizedException,
                                                                                                        PropertyServerException
    {
        SecurityManagerServicesInstance instance = (SecurityManagerServicesInstance)super.getServerServiceInstance(userId,
                                                                                                                     serverName,
                                                                                                                     serviceOperationName);

        if (instance != null)
        {
            return instance.getUserIdentityHandler();
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
    ActorProfileHandler<ActorProfileElement> getActorProfileHandler(String userId,
                                                                    String serverName,
                                                                    String serviceOperationName) throws InvalidParameterException,
                                                                                                        UserNotAuthorizedException,
                                                                                                        PropertyServerException
    {
        SecurityManagerServicesInstance instance = (SecurityManagerServicesInstance)super.getServerServiceInstance(userId,
                                                                                                                   serverName,
                                                                                                                   serviceOperationName);

        if (instance != null)
        {
            return instance.getActorProfileHandler();
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
    PersonRoleHandler<PersonRoleElement> getPersonRoleHandler(String userId,
                                                              String serverName,
                                                              String serviceOperationName) throws InvalidParameterException,
                                                                                                        UserNotAuthorizedException,
                                                                                                        PropertyServerException
    {
        SecurityManagerServicesInstance instance = (SecurityManagerServicesInstance)super.getServerServiceInstance(userId,
                                                                                                                   serverName,
                                                                                                                   serviceOperationName);

        if (instance != null)
        {
            return instance.getPersonRoleHandler();
        }

        return null;
    }

}
