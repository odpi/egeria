/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.itinfrastructure.server;

import org.odpi.openmetadata.accessservices.itinfrastructure.metadataelements.ConnectionElement;
import org.odpi.openmetadata.accessservices.itinfrastructure.metadataelements.ConnectorTypeElement;
import org.odpi.openmetadata.accessservices.itinfrastructure.metadataelements.EndpointElement;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.commonservices.generichandlers.ConnectionHandler;
import org.odpi.openmetadata.commonservices.generichandlers.ConnectorTypeHandler;
import org.odpi.openmetadata.commonservices.generichandlers.EndpointHandler;
import org.odpi.openmetadata.commonservices.multitenant.OMASServiceInstanceHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;


/**
 * ITInfrastructureInstanceHandler retrieves information from the instance map for the
 * access service instances.  The instance map is thread-safe.  Instances are added
 * and removed by the ITInfrastructureAdmin class.
 */
class ITInfrastructureInstanceHandler extends OMASServiceInstanceHandler
{
    /**
     * Default constructor registers the access service
     */
    ITInfrastructureInstanceHandler()
    {
        super(AccessServiceDescription.IT_INFRASTRUCTURE_OMAS.getAccessServiceFullName());

        ITInfrastructureRegistration.registerAccessService();
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

        ITInfrastructureServicesInstance instance = (ITInfrastructureServicesInstance) super.getServerServiceInstance(userId,
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

        ITInfrastructureServicesInstance instance = (ITInfrastructureServicesInstance) super.getServerServiceInstance(userId,
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

        ITInfrastructureServicesInstance instance = (ITInfrastructureServicesInstance) super.getServerServiceInstance(userId,
                                                                                                                      serverName,
                                                                                                                      serviceOperationName);

        if (instance != null)
        {
            return instance.getEndpointHandler();
        }

        return null;
    }

}
