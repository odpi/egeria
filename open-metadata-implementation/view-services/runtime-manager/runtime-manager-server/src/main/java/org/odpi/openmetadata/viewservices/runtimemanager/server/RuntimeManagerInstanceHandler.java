/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.runtimemanager.server;


import org.odpi.openmetadata.accessservices.itinfrastructure.client.ConnectedAssetClient;
import org.odpi.openmetadata.accessservices.itinfrastructure.client.OpenMetadataStoreClient;
import org.odpi.openmetadata.accessservices.itinfrastructure.client.PlatformManagerClient;
import org.odpi.openmetadata.accessservices.itinfrastructure.client.ServerManagerClient;
import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.commonservices.multitenant.OMVSServiceInstanceHandler;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;


/**
 * RuntimeManagerInstanceHandler retrieves information from the instance map for the
 * view service instances.  The instance map is thread-safe.  Instances are added
 * and removed by the RuntimeManagerAdmin class.
 */
public class RuntimeManagerInstanceHandler extends OMVSServiceInstanceHandler
{
    /**
     * Default constructor registers the view service
     */
    public RuntimeManagerInstanceHandler()
    {
        super(ViewServiceDescription.RUNTIME_MANAGER.getViewServiceName());

        RuntimeManagerRegistration.registerViewService();
    }


    /**
     * This method returns the object for the tenant to use to work with the IT Infrastructure API.
     *
     * @param serverName           name of the server that the request is for
     * @param userId               local server userid
     * @param serviceOperationName service operation - usually the top level rest call
     * @return client
     * @throws InvalidParameterException unknown server/service
     * @throws UserNotAuthorizedException User not authorized to call this service
     * @throws PropertyServerException internal error
     */
    public PlatformManagerClient getPlatformManagerClient(String userId,
                                                          String serverName,
                                                          String serviceOperationName) throws InvalidParameterException,
                                                                                              PropertyServerException,
                                                                                              UserNotAuthorizedException
    {
        RuntimeManagerInstance instance = (RuntimeManagerInstance) getServerServiceInstance(userId, serverName, serviceOperationName);

        if (instance != null)
        {
            return instance.getPlatformManagerClient();
        }

        return null;
    }




    /**
     * This method returns the object for the tenant to use to work with the IT Infrastructure API.
     *
     * @param serverName           name of the server that the request is for
     * @param userId               local server userid
     * @param serviceOperationName service operation - usually the top level rest call
     * @return client
     * @throws InvalidParameterException unknown server/service
     * @throws UserNotAuthorizedException User not authorized to call this service
     * @throws PropertyServerException internal error
     */
    public ServerManagerClient getServerManagerClient(String userId,
                                                      String serverName,
                                                      String serviceOperationName) throws InvalidParameterException,
                                                                                          PropertyServerException,
                                                                                          UserNotAuthorizedException
    {
        RuntimeManagerInstance instance = (RuntimeManagerInstance) getServerServiceInstance(userId, serverName, serviceOperationName);

        if (instance != null)
        {
            return instance.getServerManagerClient();
        }

        return null;
    }


    /**
     * This method returns the object for the tenant to use to work with the IT Infrastructure API.
     *
     * @param serverName           name of the server that the request is for
     * @param userId               local server userid
     * @param serviceOperationName service operation - usually the top level rest call
     * @return client
     * @throws InvalidParameterException unknown server/service
     * @throws UserNotAuthorizedException User not authorized to call this service
     * @throws PropertyServerException internal error
     */
    public ConnectedAssetClient getConnectedAssetClient(String userId,
                                                        String serverName,
                                                        String serviceOperationName) throws InvalidParameterException,
                                                                                            PropertyServerException,
                                                                                            UserNotAuthorizedException
    {
        RuntimeManagerInstance instance = (RuntimeManagerInstance) getServerServiceInstance(userId, serverName, serviceOperationName);

        if (instance != null)
        {
            return instance.getConnectedAssetClient();
        }

        return null;
    }



    /**
     * This method returns the object for the tenant to use to work with the IT Infrastructure API.
     *
     * @param serverName           name of the server that the request is for
     * @param userId               local server userid
     * @param serviceOperationName service operation - usually the top level rest call
     * @return client
     * @throws InvalidParameterException unknown server/service
     * @throws UserNotAuthorizedException User not authorized to call this service
     * @throws PropertyServerException internal error
     */
    public OpenMetadataStoreClient getOpenMetadataStoreClient(String userId,
                                                              String serverName,
                                                              String serviceOperationName) throws InvalidParameterException,
                                                                                                  PropertyServerException,
                                                                                                  UserNotAuthorizedException
    {
        RuntimeManagerInstance instance = (RuntimeManagerInstance) getServerServiceInstance(userId, serverName, serviceOperationName);

        if (instance != null)
        {
            return instance.getOpenMetadataStoreClient();
        }

        return null;
    }
}
