/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.classificationexplorer.server;


import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.commonservices.multitenant.OMVSServiceInstanceHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.viewservices.classificationexplorer.handler.OpenMetadataStoreHandler;
import org.odpi.openmetadata.viewservices.classificationexplorer.handler.StewardshipManagementHandler;


/**
 * ClassificationExplorerInstanceHandler retrieves information from the instance map for the
 * view service instances.  The instance map is thread-safe.  Instances are added
 * and removed by the ClassificationExplorerAdmin class.
 */
public class ClassificationExplorerInstanceHandler extends OMVSServiceInstanceHandler
{
    /**
     * Default constructor registers the view service
     */
    public ClassificationExplorerInstanceHandler()
    {
        super(ViewServiceDescription.CLASSIFICATION_EXPLORER.getViewServiceName());

        ClassificationExplorerRegistration.registerViewService();
    }




    /**
     * This method returns the object for the tenant to use to work with the asset manager API
     *
     * @param serverName           name of the server that the request is for
     * @param userId               local server userid
     * @param urlMarker             view service URL marker
     * @param serviceOperationName service operation - usually the top level rest call
     * @return client
     * @throws InvalidParameterException unknown server/service
     * @throws UserNotAuthorizedException User not authorized to call this service
     * @throws PropertyServerException internal error
     */
    public StewardshipManagementHandler getStewardshipManagementHandler(String userId,
                                                                        String serverName,
                                                                        String urlMarker,
                                                                        String serviceOperationName) throws InvalidParameterException,
                                                                                                          PropertyServerException,
                                                                                                          UserNotAuthorizedException
    {
        ClassificationExplorerInstance instance = (ClassificationExplorerInstance) getServerServiceInstance(userId, serverName, serviceOperationName);

        if (instance != null)
        {
            return instance.getStewardshipManagerHandler(urlMarker, serviceOperationName);
        }

        return null;
    }



    /**
     * This method returns the object for the tenant to use to work with the
     * Governance Action Framework Services API
     *
     * @param serverName           name of the server that the request is for
     * @param userId               local server userid
     * @param urlMarker             view service URL marker
     * @param serviceOperationName service operation - usually the top level rest call
     * @return client
     * @throws InvalidParameterException unknown server/service
     * @throws UserNotAuthorizedException User not authorized to call this service
     * @throws PropertyServerException internal error
     */
    public OpenMetadataStoreHandler getOpenMetadataHandler(String userId,
                                                           String serverName,
                                                           String urlMarker,
                                                           String serviceOperationName) throws InvalidParameterException,
                                                                                               PropertyServerException,
                                                                                               UserNotAuthorizedException
    {
        ClassificationExplorerInstance instance = (ClassificationExplorerInstance) getServerServiceInstance(userId, serverName, serviceOperationName);

        if (instance != null)
        {
            return instance.getOpenMetadataStoreHandler(urlMarker, serviceOperationName);
        }

        return null;
    }
}
