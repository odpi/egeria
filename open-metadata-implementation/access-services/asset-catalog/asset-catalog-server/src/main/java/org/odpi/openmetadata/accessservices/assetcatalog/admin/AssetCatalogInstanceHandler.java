/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetcatalog.admin;

import org.odpi.openmetadata.accessservices.assetcatalog.handlers.AssetCatalogHandler;
import org.odpi.openmetadata.accessservices.assetcatalog.handlers.RelationshipHandler;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.commonservices.multitenant.OMASServiceInstanceHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

/**
 * AssetCatalogInstanceHandler retrieves information from the instance map for the access service instances.
 * The instance map is thread-safe. Instances are added and removed by the AssetCatalogAdmin class.
 */
public class AssetCatalogInstanceHandler extends OMASServiceInstanceHandler {

    /**
     * Default constructor registers the access service
     */
    public AssetCatalogInstanceHandler() {
        super(AccessServiceDescription.ASSET_CATALOG_OMAS.getAccessServiceFullName());

        AssetCatalogRegistration.registerAccessService();
    }

    /**
     * Retrieve the process handler for the access service
     *
     * @param userId               calling user
     * @param serverName           name of the server tied to the request
     * @param serviceOperationName name of the REST API call (typically the top-level methodName)
     * @return handler for use by the requested instance
     * @throws InvalidParameterException  no available instance for the requested server
     * @throws UserNotAuthorizedException user does not have access to the requested server
     * @throws PropertyServerException    the service name is not known - indicating a logic error
     */
    public AssetCatalogHandler getAssetCatalogHandler(String userId, String serverName, String serviceOperationName) throws
            InvalidParameterException,
            UserNotAuthorizedException,
            PropertyServerException {
        AssetCatalogServicesInstance instance = (AssetCatalogServicesInstance) super.getServerServiceInstance(userId,
                serverName, serviceOperationName);

        return instance.getAssetCatalogHandler();
    }

    /**
     * Retrieve the registration handler for the access service
     *
     * @param userId               calling user
     * @param serverName           name of the server tied to the request
     * @param serviceOperationName name of the REST API call (typically the top-level methodName)
     * @return handler for use by the requested instance
     * @throws InvalidParameterException  no available instance for the requested server
     * @throws UserNotAuthorizedException user does not have access to the requested server
     * @throws PropertyServerException    the service name is not known - indicating a logic error
     */
    public RelationshipHandler getRelationshipHandler(String userId, String serverName,
                                                      String serviceOperationName) throws
            InvalidParameterException,
            UserNotAuthorizedException,
            PropertyServerException {
        AssetCatalogServicesInstance instance = (AssetCatalogServicesInstance) super.getServerServiceInstance(userId,
                serverName, serviceOperationName);

        return instance.getRelationshipHandler();
    }
}


