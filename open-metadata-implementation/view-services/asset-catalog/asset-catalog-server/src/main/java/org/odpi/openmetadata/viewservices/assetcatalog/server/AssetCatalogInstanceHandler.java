/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.assetcatalog.server;


import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.commonservices.multitenant.OMVSServiceInstanceHandler;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.AssetHandler;
import org.odpi.openmetadata.viewservices.assetcatalog.rest.Type;

import java.util.List;


/**
 * AssetCatalogInstanceHandler retrieves information from the instance map for the
 * view service instances.  The instance map is thread-safe.  Instances are added
 * and removed by the AssetCatalogAdmin class.
 */
public class AssetCatalogInstanceHandler extends OMVSServiceInstanceHandler
{
    /**
     * Default constructor registers the view service
     */
    public AssetCatalogInstanceHandler()
    {
        super(ViewServiceDescription.ASSET_CATALOG.getViewServiceFullName());

        AssetCatalogRegistration.registerViewService();
    }


    /**
     * Return the values from the SupportedTypesForSearch view service option.
     * @param serverName           name of the server that the request is for
     * @param userId               local server userid
     * @param serviceOperationName service operation - usually the top level rest call
     * @return list of types.
     * @throws InvalidParameterException unknown server/service
     * @throws UserNotAuthorizedException User not authorized to call this service
     * @throws PropertyServerException internal error
     */
    public List<Type> getSupportedAssetTypes(String userId,
                                             String serverName,
                                             String serviceOperationName) throws InvalidParameterException,
                                                                                 PropertyServerException,
                                                                                 UserNotAuthorizedException
    {
        AssetCatalogInstance instance = (AssetCatalogInstance) getServerServiceInstance(userId, serverName, serviceOperationName);

        if (instance != null)
        {
            return instance.getSupportedAssetTypes();
        }

        return null;
    }


    /**
     * This method returns the object for the tenant to use to work with the
     * Asset Consumer API
     *
     * @param serverName           name of the server that the request is for
     * @param userId               local server userid
     * @param serviceOperationName service operation - usually the top level rest call
     * @return  requested client
     * @throws InvalidParameterException unknown server/service
     * @throws UserNotAuthorizedException User not authorized to call this service
     * @throws PropertyServerException internal error
     */
    public AssetHandler getAssetHandler(String userId,
                                        String serverName,
                                        String serviceOperationName) throws InvalidParameterException,
                                                                            PropertyServerException,
                                                                            UserNotAuthorizedException
    {
        AssetCatalogInstance instance = (AssetCatalogInstance) getServerServiceInstance(userId, serverName, serviceOperationName);

        if (instance != null)
        {
            return instance.getAssetHandler();
        }

        return null;
    }

}
