/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.classificationmanager.server;


import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.commonservices.multitenant.OMVSServiceInstanceHandler;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.SearchKeywordHandler;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.StewardshipManagementHandler;


/**
 * ClassificationManagerInstanceHandler retrieves information from the instance map for the
 * view service instances.  The instance map is thread-safe.  Instances are added
 * and removed by the ClassificationManagerAdmin class.
 */
public class ClassificationManagerInstanceHandler extends OMVSServiceInstanceHandler
{
    /**
     * Default constructor registers the view service
     */
    public ClassificationManagerInstanceHandler()
    {
        super(ViewServiceDescription.CLASSIFICATION_MANAGER.getViewServiceFullName());

        ClassificationManagerRegistration.registerViewService();
    }


    /**
     * This method returns the object for the tenant to use to work with the open metadata API
     *
     * @param serverName           name of the server that the request is for
     * @param userId               local server userid
     * @param urlMarker  view service URL marker
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
        ClassificationManagerInstance instance = (ClassificationManagerInstance) getServerServiceInstance(userId, serverName, serviceOperationName);

        if (instance != null)
        {
            return instance.getStewardshipManagementHandler(urlMarker, serviceOperationName);
        }

        return null;
    }



    /**
     * This method returns the object for the tenant to use to work with the open metadata API
     *
     * @param serverName           name of the server that the request is for
     * @param userId               local server userid
     * @param urlMarker  view service URL marker
     * @param serviceOperationName service operation - usually the top level rest call
     * @return client
     * @throws InvalidParameterException unknown server/service
     * @throws UserNotAuthorizedException User not authorized to call this service
     * @throws PropertyServerException internal error
     */
    public SearchKeywordHandler getSearchKeywordHandler(String userId,
                                                        String serverName,
                                                        String urlMarker,
                                                        String serviceOperationName) throws InvalidParameterException,
                                                                                            PropertyServerException,
                                                                                            UserNotAuthorizedException
    {
        ClassificationManagerInstance instance = (ClassificationManagerInstance) getServerServiceInstance(userId, serverName, serviceOperationName);

        if (instance != null)
        {
            return instance.getSearchKeywordHandler(urlMarker, serviceOperationName);
        }

        return null;
    }
}
