/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.assetsearch.services;

import org.odpi.openmetadata.accessservices.assetcatalog.AssetCatalog;
import org.odpi.openmetadata.accessservices.assetcatalog.model.*;
import org.odpi.openmetadata.accessservices.assetcatalog.model.rest.body.SearchParameters;
import org.odpi.openmetadata.commonservices.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.commonservices.ffdc.exceptions.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.userinterface.common.ffdc.DependantServerNotAvailableException;
import org.odpi.openmetadata.viewservices.assetsearch.admin.handlers.AssetSearchViewInstanceHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

/**
 * The Asset Search OMVS Service provides an interface to search for assets using the Asset Catalog OMAS client
 */

public class AssetSearchViewRESTServices {

    protected static AssetSearchViewInstanceHandler instanceHandler     = new AssetSearchViewInstanceHandler();



    private static final Logger LOG = LoggerFactory.getLogger(AssetSearchViewRESTServices.class);


    public AssetSearchViewRESTServices() {
    }


    /**
     * Fetch asset's header, classification and properties
     *
     * @param serverName         name of the local UI server.
     * @param userId             user identifier
     * @param assetId the unique identifier for the asset
     * @param assetType asset type
     * @return the asset with its header and the list of associated classifications and specific properties
     * @throws PropertyServerException there is a problem retrieving information from the property server
     * @throws InvalidParameterException invalid parameter exception
     */
    public List<AssetDescription> getAssetDetails(String serverName, String userId, String assetId, String assetType) throws PropertyServerException, org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException, UserNotAuthorizedException, DependantServerNotAvailableException {
        try {
            AssetCatalog  assetCatalog = instanceHandler.getAssetCatalog(serverName, userId, "getAssetDetails");
            if (assetCatalog.getAssetDetails(userId, assetId, assetType) != null) {
                return assetCatalog.getAssetDetails(userId, assetId, assetType).getAssetDescriptionList();
            }
        } catch (org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException | PropertyServerException e) {
            LOG.error(String.format("Error retrieving asset details for %s", assetId));
            throw e;
        }
        return Collections.emptyList();
    }

    /**
     * Fetch asset's header, classification, properties and relationships
     *
     * @param serverName         name of the local UI server.
     * @param userId             user identifier
     * @param assetId the unique identifier for the asset
     * @return the asset with its header and the list of associated classifications
     * @throws PropertyServerException   there is a problem retrieving information from the property server
     * @throws InvalidParameterException one of the parameters is null or invalid
     */
    public List<AssetDescription> getAssetUniverse(String serverName, String userId, String assetId, String assetType)
            throws PropertyServerException, org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException, UserNotAuthorizedException, DependantServerNotAvailableException {
        try {
            AssetCatalog assetCatalog = instanceHandler.getAssetCatalog(serverName, userId, "getAssetUniverse");
            return assetCatalog.getAssetUniverse(userId, assetId, assetType).getAssetDescriptionList();
        } catch (PropertyServerException | org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException | UserNotAuthorizedException | DependantServerNotAvailableException e) {
            LOG.error(String.format("Error retrieving asset universe for %s", assetId));
            throw e;
        }
    }

    /**
     * Fetch the relationships for a specific asset
     *
     * @param serverName         name of the local UI server.
     * @param userId             user identifier
     * @param assetId the unique identifier for the asset
     * @param assetType asset type
     * @param relationshipTypeGUID relationship type guid
     * @param from from index
     * @param pageSize page size
     * @return list of relationships for the given asset
     * @throws PropertyServerException   there is a problem retrieving information from the property server
     * @throws InvalidParameterException one of the parameters is null or invalid
     */
    public List<Relationship> getAssetRelationships(String serverName, String userId, String assetId,
                                                    String assetType, String relationshipTypeGUID, Integer from, Integer pageSize)
            throws PropertyServerException, org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException, UserNotAuthorizedException, DependantServerNotAvailableException {
        try {
            AssetCatalog assetCatalog = instanceHandler.getAssetCatalog(serverName, userId, "getAssetRelationships");
            return assetCatalog.getAssetRelationships(userId, assetId, assetType, relationshipTypeGUID, from, pageSize).getRelationships();
        } catch (PropertyServerException | org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException | UserNotAuthorizedException | DependantServerNotAvailableException e) {
            LOG.error(String.format("Error retrieving asset relationships for %s", assetId));
            throw e;
        }
    }

    /**
     * Fetch the classification for a specific asset
     *
     * @param serverName         name of the local UI server.
     * @param userId             user identifier
     * @param assetId the unique identifier for the asset
     * @param assetType type of asset
     * @param classificationName classification name to retrieve
     * @return the classification for the asset
     * @throws PropertyServerException   there is a problem retrieving information from the property server
     * @throws InvalidParameterException one of the parameters is null or invalid
     */
    public List<Classification> getClassificationsForAsset(String serverName, String userId, String assetId, String assetType, String classificationName)
            throws PropertyServerException, org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException, UserNotAuthorizedException, DependantServerNotAvailableException {
        try {
            AssetCatalog assetCatalog = instanceHandler.getAssetCatalog(serverName, userId, "getClassificationsForAsset");
            return assetCatalog.getClassificationsForAsset(userId, assetId, assetType, classificationName).getClassifications();
        } catch (PropertyServerException | org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException | UserNotAuthorizedException | DependantServerNotAvailableException e) {
            LOG.error(String.format("Error retrieving asset classifications for %s", assetId));
            throw e;
        }
    }

    /**
     * Fetch asset's header
     *
     * @param serverName     local UI server name
     * @param userId           userId of the user triggering the request
     * @param searchCriteria the searchCriteria
     * @return the assets for the search criteria
     * @throws PropertyServerException   there is a problem retrieving information from the property server
     * @throws InvalidParameterException there is a problem with the parameters
     */
    public List<AssetElements>  searchAssets(String serverName, String userId, String searchCriteria, SearchParameters searchParameters)
            throws org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException, PropertyServerException, UserNotAuthorizedException, DependantServerNotAvailableException {
        try {
            AssetCatalog assetCatalog = instanceHandler.getAssetCatalog(serverName, userId, "getAssetDetails");
            return assetCatalog.searchByType(userId, searchCriteria, searchParameters).getAssets();
        } catch (PropertyServerException | org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException | UserNotAuthorizedException | DependantServerNotAvailableException e) {
            LOG.error(String.format("Error searching for assets with criteria  '%s'", searchCriteria));
            throw e;
        }
    }

    public List<AssetElements>  getAssetContext(String serverName, String userId, String assetId, String assetType) throws PropertyServerException, org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException, UserNotAuthorizedException, DependantServerNotAvailableException {
        try {
            AssetCatalog assetCatalog = instanceHandler.getAssetCatalog(serverName, userId, "getAssetContext");
            return assetCatalog.getAssetContext(userId, assetId, assetType).getAssets();
        } catch (PropertyServerException | org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException | UserNotAuthorizedException | DependantServerNotAvailableException e) {
            LOG.error(String.format("Error retrieving asset context for '%s'", assetId));
            throw e;
        }
    }

    public List<Type> getSupportedTypes(String serverName, String userId) throws UserNotAuthorizedException, PropertyServerException, org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException, DependantServerNotAvailableException {
        AssetCatalog assetCatalog = instanceHandler.getAssetCatalog(serverName, userId, "getSupportedTypes");
        try {
        return assetCatalog.getSupportedTypes(userId, null).getTypes();
        } catch (PropertyServerException | org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException e) {
            LOG.error(String.format("Error retrieving types"));
            throw e;
        }
    }
}