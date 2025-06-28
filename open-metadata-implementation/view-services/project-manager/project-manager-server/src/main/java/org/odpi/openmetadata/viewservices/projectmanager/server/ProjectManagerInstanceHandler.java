/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.projectmanager.server;


import org.odpi.openmetadata.accessservices.projectmanagement.client.ProjectManagement;
import org.odpi.openmetadata.accessservices.projectmanagement.client.ConnectedAssetClient;
import org.odpi.openmetadata.accessservices.projectmanagement.client.OpenMetadataStoreClient;
import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.commonservices.multitenant.OMVSServiceInstanceHandler;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;



/**
 * ProjectManagerInstanceHandler retrieves information from the instance map for the
 * view service instances.  The instance map is thread-safe.  Instances are added
 * and removed by the ProjectManagerAdmin class.
 */
public class ProjectManagerInstanceHandler extends OMVSServiceInstanceHandler
{
    /**
     * Default constructor registers the view service
     */
    public ProjectManagerInstanceHandler()
    {
        super(ViewServiceDescription.PROJECT_MANAGER.getViewServiceFullName());

        ProjectManagerRegistration.registerViewService();
    }


    /**
     * This method returns the object for the tenant to use to work with the Project Management API.
     *
     * @param serverName           name of the server that the request is for
     * @param userId               local server userid
     * @param serviceOperationName service operation - usually the top level rest call
     * @return client
     * @throws InvalidParameterException unknown server/service
     * @throws UserNotAuthorizedException User not authorized to call this service
     * @throws PropertyServerException internal error
     */
    public ProjectManagement getProjectManagement(String userId,
                                                  String serverName,
                                                  String serviceOperationName) throws InvalidParameterException,
                                                                                      PropertyServerException,
                                                                                      UserNotAuthorizedException
    {
        ProjectManagerInstance instance = (ProjectManagerInstance) getServerServiceInstance(userId, serverName, serviceOperationName);

        if (instance != null)
        {
            return instance.getProjectManagement();
        }

        return null;
    }


    /**
     * This method returns the object for the tenant to use to work with the Project Management API.
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
        ProjectManagerInstance instance = (ProjectManagerInstance) getServerServiceInstance(userId, serverName, serviceOperationName);

        if (instance != null)
        {
            return instance.getConnectedAssetClient();
        }

        return null;
    }



    /**
     * This method returns the object for the tenant to use to work with the Project Management API.
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
        ProjectManagerInstance instance = (ProjectManagerInstance) getServerServiceInstance(userId, serverName, serviceOperationName);

        if (instance != null)
        {
            return instance.getOpenMetadataStoreClient();
        }

        return null;
    }
}
