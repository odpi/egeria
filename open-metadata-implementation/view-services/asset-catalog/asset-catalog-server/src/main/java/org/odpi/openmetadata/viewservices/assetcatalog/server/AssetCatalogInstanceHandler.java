/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.assetcatalog.server;


import org.odpi.openmetadata.accessservices.assetconsumer.client.AssetConsumer;
import org.odpi.openmetadata.accessservices.assetconsumer.client.OpenIntegrationServiceClient;
import org.odpi.openmetadata.accessservices.assetconsumer.client.OpenMetadataStoreClient;
import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.commonservices.ffdc.exceptions.PropertyServerException;
import org.odpi.openmetadata.commonservices.multitenant.OMVSServiceInstanceHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

import java.util.regex.Pattern;


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
        super(ViewServiceDescription.ASSET_CATALOG.getViewServiceName());

        AssetCatalogRegistration.registerViewService();
    }


    /**
     * This method returns the object for the tenant to use to work with the
     * asset consumer API
     *
     * @param serverName           name of the server that the request is for
     * @param userId               local server userid
     * @param serviceOperationName service operation - usually the top level rest call
     * @return SubjectAreaNodeClients subject area nodes API objects
     * @throws InvalidParameterException unknown server/service
     * @throws UserNotAuthorizedException User not authorized to call this service
     * @throws PropertyServerException internal error
     */
    public AssetConsumer getAssetConsumerClient(String userId,
                                                String serverName,
                                                String serviceOperationName) throws InvalidParameterException,
                                                                                    PropertyServerException,
                                                                                    UserNotAuthorizedException
    {
        AssetCatalogInstance instance = (AssetCatalogInstance) getServerServiceInstance(userId, serverName, serviceOperationName);

        if (instance != null)
        {
            return instance.getAssetConsumerClient();
        }

        return null;
    }


    /**
     * This method returns the object for the tenant to use to work with the
     * asset consumer API
     *
     * @param serverName           name of the server that the request is for
     * @param userId               local server userid
     * @param serviceOperationName service operation - usually the top level rest call
     * @return SubjectAreaNodeClients subject area nodes API objects
     * @throws InvalidParameterException unknown server/service
     * @throws UserNotAuthorizedException User not authorized to call this service
     * @throws PropertyServerException internal error
     */
    public OpenIntegrationServiceClient getOpenIntegrationServiceClient(String userId,
                                                                        String serverName,
                                                                        String serviceOperationName) throws InvalidParameterException,
                                                                                                            PropertyServerException,
                                                                                                            UserNotAuthorizedException
    {
        AssetCatalogInstance instance = (AssetCatalogInstance) getServerServiceInstance(userId, serverName, serviceOperationName);

        if (instance != null)
        {
            return instance.getOpenIntegrationServiceClient();
        }

        return null;
    }


    /**
     * This method returns the object for the tenant to use to work with the asset consumer API
     *
     * @param serverName           name of the server that the request is for
     * @param userId               local server userid
     * @param serviceOperationName service operation - usually the top level rest call
     * @return SubjectAreaNodeClients subject area nodes API objects
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
        AssetCatalogInstance instance = (AssetCatalogInstance) getServerServiceInstance(userId, serverName, serviceOperationName);

        if (instance != null)
        {
            return instance.getOpenMetadataStoreClient();
        }

        return null;
    }
}
