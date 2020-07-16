/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.uichassis.springboot.service;

import org.odpi.openmetadata.accessservices.assetcatalog.AssetCatalog;
import org.odpi.openmetadata.accessservices.assetcatalog.model.AssetDescription;
import org.odpi.openmetadata.accessservices.assetcatalog.model.AssetElements;
import org.odpi.openmetadata.accessservices.assetcatalog.model.Classification;
import org.odpi.openmetadata.accessservices.assetcatalog.model.Relationship;
import org.odpi.openmetadata.accessservices.assetcatalog.model.Type;
import org.odpi.openmetadata.accessservices.assetcatalog.model.rest.body.SearchParameters;
import org.odpi.openmetadata.commonservices.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.userinterface.uichassis.springboot.beans.Edge;
import org.odpi.openmetadata.userinterface.uichassis.springboot.beans.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
     * @param assetType the open metadata type
     * @return the asset with its header and the list of associated classifications and specific properties
     * @throws PropertyServerException there is a problem retrieving information from the property server
     * @throws InvalidParameterException one of the parameters is null or invalid
     */
    public AssetDescription getAssetDetails(String user,
                                            String assetId,
                                            String assetType)
            throws PropertyServerException, org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException
    {
        try {
            return assetCatalog.getAssetDetails(user, assetId, assetType).getAssetDescription();
        } catch (org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException | PropertyServerException e) {
            LOG.error(String.format("Error retrieving asset details for %s", assetId));
            throw e;
        }
    }

    /**
     * @param response string returned from Open Lineage Services to be processed
     * @return map of nodes and edges describing the end to end flow
     */
    private Map<String, List> processAssetDescription(AssetDescription response) {
        Map<String, List> graphData = new HashMap<>();
        List<Edge> listEdges = new ArrayList<>();
        List<Node> listNodes = new ArrayList<>();

        return  graphData;

    }

    /**
     * Fetch asset's header, classification, properties and relationships
     *
     * @param user    userId of the user triggering the request
     * @param assetId the unique identifier for the asset
     * @param assetType the open metadata type
     * @return the asset with its header and the list of associated classifications
     * @throws PropertyServerException   there is a problem retrieving information from the property server
     * @throws InvalidParameterException one of the parameters is null or invalid
     */
    public AssetDescription getAssetUniverse(String user,
                                             String assetId,
                                             String assetType)
            throws PropertyServerException, org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException {
        try {
            return assetCatalog.getAssetUniverse(user, assetId, assetType).getAssetDescription();
        } catch (PropertyServerException | org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException e) {
            LOG.error(String.format("Error retrieving asset universe for %s", assetId));
            throw e;
        }
    }

    /**
     * Fetch the relationships for a specific asset
     *
     * @param user    userId of the user triggering the request
     * @param assetId the unique identifier for the asset
     * @param assetType the open metadata type
     * @param relationshipTypeGUID the relationships type
     * @param from starting index
     * @param pageSize number of relationships to be returned after starting index
     * @return list of relationships for the given asset
     * @throws PropertyServerException   there is a problem retrieving information from the property server
     * @throws InvalidParameterException one of the parameters is null or invalid
     */
    public List<Relationship> getAssetRelationships(String user, String assetId,
                                                    String assetType,
                                                    String relationshipTypeGUID,
                                                    Integer from,
                                                    Integer pageSize)
            throws PropertyServerException, org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException {
        try {
            return assetCatalog.getAssetRelationships(user, assetId, assetType, relationshipTypeGUID, from, pageSize).getRelationships();
        } catch (PropertyServerException | org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException e) {
            LOG.error(String.format("Error retrieving asset relationships for %s", assetId));
            throw e;
        }
    }

    /**
     * Fetch the classification for a specific asset
     *
     * @param user    userId of the user triggering the request
     * @param assetId the unique identifier for the asset
     * @param assetType the open metadata type
     * @param classificationName the cname of the classification
     * @return the classifications for the asset
     * @throws PropertyServerException   there is a problem retrieving information from the property server
     * @throws InvalidParameterException one of the parameters is null or invalid
     */
    public List<Classification> getClassificationsForAsset(String user,
                                                           String assetId,
                                                           String assetType,
                                                           String classificationName)
            throws PropertyServerException, org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException {
        try {
            return assetCatalog.getClassificationsForAsset(user, assetId, assetType, classificationName).getClassifications();
        } catch (PropertyServerException | org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException e) {
            LOG.error(String.format("Error retrieving asset classifications for %s", assetId));
            throw e;
        }
    }

    /**
     * Fetch asset's header
     *
     * @param user           userId of the user triggering the request
     * @param searchCriteria the searchCriteria
     * @param searchParameters the search parameters
     * @return the assets for the search criteria
     * @throws PropertyServerException   there is a problem retrieving information from the property server
     * @throws InvalidParameterException there is a problem with the parameters
     */
    public List<AssetElements> searchAssets(String user,
                                            String searchCriteria,
                                            SearchParameters searchParameters)
            throws org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException, PropertyServerException {
        try {
            return assetCatalog.searchByType(user, searchCriteria, searchParameters).getAssetElementsList();
        } catch (PropertyServerException | org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException e) {
            LOG.error(String.format("Error searching the assets by criteria %s", searchCriteria));
            throw e;
        }
    }

    /**
     * Fetch asset's context
     *
     * @param userId userId of the user triggering the request
     * @param assetId the id of the asset  (usually String representation of a GUID )
     * @param assetType the open metadata type
     * @return the asset context
     * @throws PropertyServerException   there is a problem retrieving information from the property server
     * @throws InvalidParameterException there is a problem with the parameters
     */
    public AssetElements getAssetContext(String userId,
                                         String assetId,
                                         String assetType)
            throws PropertyServerException, org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException {
        try {
            return assetCatalog.getAssetContext(userId, assetId, assetType).getAsset();
        } catch (PropertyServerException | org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException e) {
            LOG.error(String.format("Error retrieving asset context for '%s'", assetId));
            throw e;
        }
    }

    /**
     * Asset catalog supported types - a list of the types that are returned by the catalog
     *
     * @param userId userId of the user triggering the request
     * @return the list of supported types
     * @throws PropertyServerException   there is a problem retrieving information from the property server
     * @throws InvalidParameterException there is a problem with the parameters
     */
    public List<Type> getSupportedTypes(String userId) throws PropertyServerException,
            org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException {
        try {
            return assetCatalog.getSupportedTypes(userId, null).getTypes();
        } catch (PropertyServerException | org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException e) {
            LOG.error("Error retrieving supported types'");
            throw e;
        }
    }
}