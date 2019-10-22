/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.accessservices.service;

import org.odpi.openmetadata.accessservices.assetcatalog.client.AssetCatalog;
import org.odpi.openmetadata.accessservices.assetcatalog.model.AssetDescription;
import org.odpi.openmetadata.accessservices.assetcatalog.model.Classification;
import org.odpi.openmetadata.accessservices.assetcatalog.model.Relationship;
import org.odpi.openmetadata.accessservices.assetcatalog.model.Term;
import org.odpi.openmetadata.accessservices.assetcatalog.model.rest.body.SearchParameters;
import org.odpi.openmetadata.commonservices.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * The Asset Catalog OMAS Service provides an interface to search for assets using the Asset Catalog OMAS client
 */
@Service
public class AssetCatalogOMASService {

    private final AssetCatalog assetCatalog;
    private static final Logger LOG = LoggerFactory.getLogger(AssetCatalogOMASService.class);

    @Autowired
    public AssetCatalogOMASService(AssetCatalog assetCatalog) {
        this.assetCatalog = assetCatalog;
    }


    /**
     * Fetch asset's header, classification and properties
     *
     * @param user    userId of the user triggering the request
     * @param assetId the unique identifier for the asset
     * @return the asset with its header and the list of associated classifications and specific properties
     * @throws PropertyServerException there is a problem retrieving information from the property server
     */
    public List<AssetDescription> getAssetDetails(String user, String assetId, String assetType) throws PropertyServerException, InvalidParameterException {
        try {
            if (assetCatalog.getAssetDetails(user, assetId, assetType) != null) {
                return assetCatalog.getAssetDetails(user, assetId, assetType).getAssetDescriptionList();
            }
        } catch (InvalidParameterException | PropertyServerException e) {
            LOG.error(String.format("Error retrieving asset details for %s", assetId));
            throw e;
        }
        return Collections.emptyList();
    }

    /**
     * Fetch asset's header, classification, properties and relationships
     *
     * @param user    userId of the user triggering the request
     * @param assetId the unique identifier for the asset
     * @return the asset with its header and the list of associated classifications
     * @throws PropertyServerException   there is a problem retrieving information from the property server
     * @throws InvalidParameterException one of the parameters is null or invalid
     */
    public List<AssetDescription> getAssetUniverse(String user, String assetId, String assetType)
            throws PropertyServerException, InvalidParameterException {
        try {
            return assetCatalog.getAssetUniverse(user, assetId, assetType).getAssetDescriptionList();
        } catch (InvalidParameterException | PropertyServerException e) {
            LOG.error(String.format("Error retrieving asset universe for %s", assetId));
            throw e;
        }
    }

    /**
     * Fetch the relationships for a specific asset
     *
     * @param user    userId of the user triggering the request
     * @param assetId the unique identifier for the asset
     * @return list of relationships for the given asset
     * @throws PropertyServerException   there is a problem retrieving information from the property server
     * @throws InvalidParameterException one of the parameters is null or invalid
     */
    public List<Relationship> getAssetRelationships(String user, String assetId,
                                                    String assetType, String relationshipTypeGUID, Integer from, Integer pageSize)
            throws PropertyServerException, InvalidParameterException {
        try {
            return assetCatalog.getAssetRelationships(user, assetId, assetType, relationshipTypeGUID, from, pageSize).getRelationships();
        } catch (InvalidParameterException | PropertyServerException e) {
            LOG.error(String.format("Error retrieving asset relationships for %s", assetId));
            throw e;
        }
    }

    /**
     * Fetch the classification for a specific asset
     *
     * @param user    userId of the user triggering the request
     * @param assetId the unique identifier for the asset
     * @return the classification for the asset
     * @throws PropertyServerException   there is a problem retrieving information from the property server
     * @throws InvalidParameterException one of the parameters is null or invalid
     */
    public List<Classification> getClassificationsForAsset(String user, String assetId, String assetType, String classificationName)
            throws PropertyServerException, InvalidParameterException {
        try {
            return assetCatalog.getClassificationsForAsset(user, assetId, assetType, classificationName).getClassifications();
        } catch (InvalidParameterException | PropertyServerException e) {
            LOG.error(String.format("Error retrieving asset classifications for %s", assetId));
            throw e;
        }
    }

    /**
     * Fetch asset's header
     *
     * @param user           userId of the user triggering the request
     * @param searchCriteria the searchCriteria
     * @return the assets for the search criteria
     * @throws PropertyServerException   there is a problem retrieving information from the property server
     * @throws InvalidParameterException there is a problem with the parameters
     */
    public List<Term> searchAssets(String user, String searchCriteria, SearchParameters searchParameters)
            throws InvalidParameterException, PropertyServerException {
        try {
            return assetCatalog.searchAssetsAndGlossaryTerms(user, searchCriteria, searchParameters).getAssets();
        } catch (PropertyServerException | InvalidParameterException e) {
            LOG.error(String.format("Error searching the assets by criteria %s", searchCriteria));
            throw e;
        }
    }

    public List<Term> getAssetContext(String userId, String assetId, String assetType) throws PropertyServerException, InvalidParameterException {
        try {
            return assetCatalog.getAssetContext(userId, assetId, assetType).getAssets();
        } catch (PropertyServerException | InvalidParameterException e) {
            LOG.error(String.format("Error retrieving asset context for '%s'", assetId));
            throw e;
        }
    }

}