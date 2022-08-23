/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.digitalarchitecture.server;

import org.odpi.openmetadata.accessservices.digitalarchitecture.metadataelements.*;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.commonservices.generichandlers.*;
import org.odpi.openmetadata.commonservices.multitenant.OMASServiceInstanceHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;


/**
 * DigitalArchitectureInstanceHandler retrieves information from the instance map for the
 * access service instances.  The instance map is thread-safe.  Instances are added
 * and removed by the DigitalArchitectureAdmin class.
 */
class DigitalArchitectureInstanceHandler extends OMASServiceInstanceHandler
{
    /**
     * Default constructor registers the access service
     */
    DigitalArchitectureInstanceHandler()
    {
        super(AccessServiceDescription.DIGITAL_ARCHITECTURE_OMAS.getAccessServiceFullName());

        DigitalArchitectureRegistration.registerAccessService();
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
    AssetHandler<ReferenceDataAssetElement> getAssetHandler(String userId,
                                                            String serverName,
                                                            String serviceOperationName) throws InvalidParameterException,
                                                                                                UserNotAuthorizedException,
                                                                                                PropertyServerException
    {
        DigitalArchitectureServicesInstance instance = (DigitalArchitectureServicesInstance)super.getServerServiceInstance(userId,
                                                                                                                           serverName,
                                                                                                                           serviceOperationName);

        if (instance != null)
        {
            return instance.getAssetHandler();
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
     * @throws PropertyServerException error in the requested server
     */
    ConnectionHandler<ConnectionElement> getConnectionHandler(String userId,
                                                              String serverName,
                                                              String serviceOperationName) throws InvalidParameterException,
                                                                                                  UserNotAuthorizedException,
                                                                                                  PropertyServerException
    {

        DigitalArchitectureServicesInstance instance = (DigitalArchitectureServicesInstance) super.getServerServiceInstance(userId,
                                                                                                                            serverName,
                                                                                                                            serviceOperationName);

        if (instance != null)
        {
            return instance.getConnectionHandler();
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
     * @throws PropertyServerException error in the requested server
     */
    ConnectorTypeHandler<ConnectorTypeElement> getConnectorTypeHandler(String userId,
                                                                       String serverName,
                                                                       String serviceOperationName) throws InvalidParameterException,
                                                                                                           UserNotAuthorizedException,
                                                                                                           PropertyServerException
    {

        DigitalArchitectureServicesInstance instance = (DigitalArchitectureServicesInstance) super.getServerServiceInstance(userId,
                                                                                                                            serverName,
                                                                                                                            serviceOperationName);

        if (instance != null)
        {
            return instance.getConnectorTypeHandler();
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
     * @throws PropertyServerException error in the requested server
     */
    EndpointHandler<EndpointElement> getEndpointHandler(String userId,
                                                        String serverName,
                                                        String serviceOperationName) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException
    {

        DigitalArchitectureServicesInstance instance = (DigitalArchitectureServicesInstance) super.getServerServiceInstance(userId,
                                                                                                                            serverName,
                                                                                                                            serviceOperationName);

        if (instance != null)
        {
            return instance.getEndpointHandler();
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
     * @throws PropertyServerException error in the requested server
     */
    LocationHandler<LocationElement> getLocationHandler(String userId,
                                                        String serverName,
                                                        String serviceOperationName) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException
    {
        DigitalArchitectureServicesInstance instance = (DigitalArchitectureServicesInstance)super.getServerServiceInstance(userId,
                                                                                                                           serverName,
                                                                                                                           serviceOperationName);

        if (instance != null)
        {
            return instance.getLocationHandler();
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
     * @throws PropertyServerException error in the requested server
     */
    ReferenceDataHandler<ValidValueElement,
                              ValidValueAssignmentConsumerElement,
                              ValidValueAssignmentDefinitionElement,
                              ValidValueImplAssetElement,
                              ValidValueImplDefinitionElement,
                              ValidValueMappingElement,
                              ReferenceValueAssignmentDefinitionElement,
                              ReferenceValueAssignmentItemElement> getReferenceDataHandler(String userId,
                                                                                         String serverName,
                                                                                         String serviceOperationName) throws InvalidParameterException,
                                                                                                                             UserNotAuthorizedException,
                                                                                                                             PropertyServerException
    {
        DigitalArchitectureServicesInstance instance = (DigitalArchitectureServicesInstance)super.getServerServiceInstance(userId,
                                                                                                                           serverName,
                                                                                                                           serviceOperationName);

        if (instance != null)
        {
            return instance.getReferenceDataHandler();
        }

        return null;
    }
}
