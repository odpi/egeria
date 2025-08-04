/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworkservices.gaf.server;

import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.commonservices.generichandlers.AssetHandler;
import org.odpi.openmetadata.commonservices.generichandlers.EngineActionHandler;
import org.odpi.openmetadata.commonservices.generichandlers.GovernanceActionProcessStepHandler;
import org.odpi.openmetadata.commonservices.generichandlers.GovernanceActionTypeHandler;
import org.odpi.openmetadata.commonservices.multitenant.AccessServerServiceInstanceHandler;
import org.odpi.openmetadata.frameworks.governanceaction.properties.EngineActionElement;
import org.odpi.openmetadata.frameworks.governanceaction.properties.GovernanceActionProcessElement;
import org.odpi.openmetadata.frameworks.governanceaction.properties.GovernanceActionProcessStepElement;
import org.odpi.openmetadata.frameworks.governanceaction.properties.GovernanceActionTypeElement;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworkservices.gaf.handlers.GovernanceEngineConfigurationHandler;
import org.odpi.openmetadata.frameworkservices.gaf.handlers.IntegrationGroupConfigurationHandler;
import org.odpi.openmetadata.frameworkservices.omf.handlers.MetadataElementHandler;

/**
 * GAFServicesInstanceHandler retrieves information from the instance map for the
 * access service instances.  The instance map is thread-safe.  Instances are added
 * and removed by the GovernanceEngineAdmin class.
 */
public class GAFServicesInstanceHandler extends AccessServerServiceInstanceHandler
{
    /**
     * Default constructor registers the access service
     */
    public GAFServicesInstanceHandler()
    {
        super(AccessServiceDescription.GAF_METADATA_MANAGEMENT.getServiceName());

        GAFServicesRegistration.registerAccessService();
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
    MetadataElementHandler<OpenMetadataElement> getMetadataElementHandler(String userId,
                                                                          String serverName,
                                                                          String serviceOperationName) throws InvalidParameterException,
                                                                                                              UserNotAuthorizedException,
                                                                                                              PropertyServerException
    {
        GAFServicesInstance instance = (GAFServicesInstance)super.getServerServiceInstance(userId, serverName, serviceOperationName);

        if (instance != null)
        {
            return instance.getMetadataElementHandler();
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
    AssetHandler<GovernanceActionProcessElement> getGovernanceActionProcessHandler(String userId,
                                                                                   String serverName,
                                                                                   String serviceOperationName) throws InvalidParameterException,
                                                                                                                       UserNotAuthorizedException,
                                                                                                                       PropertyServerException
    {
        GAFServicesInstance instance = (GAFServicesInstance)super.getServerServiceInstance(userId, serverName, serviceOperationName);

        if (instance != null)
        {
            return instance.getGovernanceActionProcessHandler();
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
    GovernanceActionProcessStepHandler<GovernanceActionProcessStepElement> getGovernanceActionProcessStepHandler(String userId,
                                                                                                                 String serverName,
                                                                                                                 String serviceOperationName) throws InvalidParameterException,
                                                                                                                                                     UserNotAuthorizedException,
                                                                                                                                                     PropertyServerException
    {
        GAFServicesInstance instance = (GAFServicesInstance)super.getServerServiceInstance(userId, serverName, serviceOperationName);

        if (instance != null)
        {
            return instance.getGovernanceActionProcessStepHandler();
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
    GovernanceActionTypeHandler<GovernanceActionTypeElement> getGovernanceActionTypeHandler(String userId,
                                                                                            String serverName,
                                                                                            String serviceOperationName) throws InvalidParameterException,
                                                                                                                                UserNotAuthorizedException,
                                                                                                                                PropertyServerException
    {
        GAFServicesInstance instance = (GAFServicesInstance)super.getServerServiceInstance(userId, serverName, serviceOperationName);

        if (instance != null)
        {
            return instance.getGovernanceActionTypeHandler();
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
    EngineActionHandler<EngineActionElement> getEngineActionHandler(String userId,
                                                                    String serverName,
                                                                    String serviceOperationName) throws InvalidParameterException,
                                                                                                        UserNotAuthorizedException,
                                                                                                        PropertyServerException
    {
        GAFServicesInstance instance = (GAFServicesInstance)super.getServerServiceInstance(userId, serverName, serviceOperationName);

        if (instance != null)
        {
            return instance.getEngineActionHandler();
        }

        return null;
    }


    /**
     * Retrieve the specific handler for the service.
     *
     * @param userId calling user
     * @param serverName name of the server tied to the request
     * @param serviceOperationName name of the REST API call (typically the top-level methodName)
     * @return handler for use by the requested instance
     * @throws InvalidParameterException no available instance for the requested server
     * @throws UserNotAuthorizedException user does not have access to the requested server
     * @throws PropertyServerException the service name is not known - indicating a logic error
     */
    GovernanceEngineConfigurationHandler getGovernanceEngineConfigurationHandler(String userId,
                                                                                 String serverName,
                                                                                 String serviceOperationName) throws InvalidParameterException,
                                                                                                                     UserNotAuthorizedException,
                                                                                                                     PropertyServerException
    {
        GAFServicesInstance instance = (GAFServicesInstance)super.getServerServiceInstance(userId, serverName, serviceOperationName);

        if (instance != null)
        {
            return instance.getGovernanceConfigurationHandler();
        }

        return null;
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
     * @throws PropertyServerException the service name is not known - indicating a logic error
     */
    IntegrationGroupConfigurationHandler getIntegrationGroupConfigurationHandler(String userId,
                                                                                 String serverName,
                                                                                 String serviceOperationName) throws InvalidParameterException,
                                                                                                                     UserNotAuthorizedException,
                                                                                                                     PropertyServerException
    {
        GAFServicesInstance instance = (GAFServicesInstance)super.getServerServiceInstance(userId, serverName, serviceOperationName);

        if (instance != null)
        {
            return instance.getIntegrationGroupConfigurationHandler();
        }

        return null;
    }
}
