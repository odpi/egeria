/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.validmetadata.server;


import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.commonservices.multitenant.OMVSServiceInstanceHandler;
import org.odpi.openmetadata.frameworks.openmetadata.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.SpecificationPropertyHandler;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.ValidMetadataValueHandler;


/**
 * ValidMetadataInstanceHandler retrieves information from the instance map for the
 * view service instances.  The instance map is thread-safe.  Instances are added
 * and removed by the ValidMetadataAdmin class.
 */
public class ValidMetadataInstanceHandler extends OMVSServiceInstanceHandler
{
    /**
     * Default constructor registers the view service
     */
    public ValidMetadataInstanceHandler()
    {
        super(ViewServiceDescription.VALID_METADATA.getViewServiceFullName());

        ValidMetadataRegistration.registerViewService();
    }



    /**
     * This method returns the handler.
     *
     * @param serverName           name of the server that the request is for
     * @param userId               local server userid
     * @param urlMarker  view service URL marker
     * @param serviceOperationName service operation - usually the top level rest call
     * @return  client
     * @throws InvalidParameterException unknown server/service
     * @throws UserNotAuthorizedException User not authorized to call this service
     * @throws PropertyServerException internal error
     */
    public ValidMetadataValueHandler getValidMetadataValueHandler(String userId,
                                                                  String serverName,
                                                                  String urlMarker,
                                                                  String serviceOperationName) throws InvalidParameterException,
                                                                                                      PropertyServerException,
                                                                                                      UserNotAuthorizedException
    {
        ValidMetadataInstance instance = (ValidMetadataInstance) getServerServiceInstance(userId, serverName, serviceOperationName);

        if (instance != null)
        {
            return instance.getValidMetadataValueHandler(urlMarker, serviceOperationName);
        }

        return null;
    }


    /**
     * This method returns the handler.
     *
     * @param serverName           name of the server that the request is for
     * @param userId               local server userid
     * @param urlMarker  view service URL marker
     * @param serviceOperationName service operation - usually the top level rest call
     * @return  client
     * @throws InvalidParameterException unknown server/service
     * @throws UserNotAuthorizedException User not authorized to call this service
     * @throws PropertyServerException internal error
     */
    public SpecificationPropertyHandler getSpecificationPropertyHandler(String userId,
                                                                        String serverName,
                                                                        String urlMarker,
                                                                        String serviceOperationName) throws InvalidParameterException,
                                                                                                            PropertyServerException,
                                                                                                            UserNotAuthorizedException
    {
        ValidMetadataInstance instance = (ValidMetadataInstance) getServerServiceInstance(userId, serverName, serviceOperationName);

        if (instance != null)
        {
            return instance.getSpecificationPropertyHandler(urlMarker, serviceOperationName);
        }

        return null;
    }



    /**
     * This method returns the object for the tenant to use to work with the Asset Owner API.
     *
     * @param serverName           name of the server that the request is for
     * @param urlMarker  view service URL marker
     * @param userId               local server userid
     * @param serviceOperationName service operation - usually the top level rest call
     * @return client
     * @throws InvalidParameterException unknown server/service
     * @throws UserNotAuthorizedException User not authorized to call this service
     * @throws PropertyServerException internal error
     */
    public OpenMetadataClient getOpenMetadataStoreClient(String userId,
                                                         String serverName,
                                                         String urlMarker,
                                                         String serviceOperationName) throws InvalidParameterException,
                                                                                             PropertyServerException,
                                                                                             UserNotAuthorizedException
    {
        ValidMetadataInstance instance = (ValidMetadataInstance) getServerServiceInstance(userId, serverName, serviceOperationName);

        if (instance != null)
        {
            return instance.getOpenMetadataStoreClient(urlMarker, serviceOperationName);
        }

        return null;
    }
}
