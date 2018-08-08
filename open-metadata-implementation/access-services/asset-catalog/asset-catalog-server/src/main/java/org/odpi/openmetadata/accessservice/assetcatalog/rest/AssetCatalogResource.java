/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservice.assetcatalog.rest;


import org.odpi.openmetadata.accessservice.assetcatalog.admin.AssetCatalogAdmin;
import org.odpi.openmetadata.accessservice.assetcatalog.model.Status;
import org.odpi.openmetadata.accessservice.assetcatalog.model.responses.AssetDescriptionResponse;
import org.odpi.openmetadata.accessservice.assetcatalog.service.OMASCatalogRESTServices;
import org.odpi.openmetadata.adminservices.OMAGAccessServiceRegistration;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceOperationalStatus;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceRegistration;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * The AssetCatalogRelationshipResource provides the server-side implementation of the Asset Catalog Open Metadata
 * Assess Service (OMAS).
 * This interface facilitates the searching for asset's details, provides the connection to a specific asset.
 */
@RestController
@RequestMapping("/open-metadata/access-services/asset-catalog/users/{userId}")
public class AssetCatalogResource {

    private static OMRSRepositoryConnector repositoryConnector;
    private final OMASCatalogRESTServices restAPI;

    /**
     * Default constructor
     */
    public AssetCatalogResource(OMASCatalogRESTServices restAPI) {
        AccessServiceDescription myDescription = AccessServiceDescription.ASSET_CATALOG_OMAS;

        AccessServiceRegistration myRegistration = new AccessServiceRegistration(myDescription.getAccessServiceCode(),
                myDescription.getAccessServiceName(),
                myDescription.getAccessServiceDescription(),
                myDescription.getAccessServiceWiki(),
                AccessServiceOperationalStatus.ENABLED,
                AssetCatalogAdmin.class.getName());
        OMAGAccessServiceRegistration.registerAccessService(myRegistration);

        this.restAPI = restAPI;
    }

    /**
     * Return a list of assets (details and connections) matching the search criteria
     *
     * @param userId         the unique identifier for the user
     * @param searchCriteria a string expression of the characteristics of the required assets
     * @param limit          limit the result set to only include the specified number of entries
     * @param offset         start offset of the result set (for pagination)
     * @param orderType      enum defining how the results should be ordered.
     * @param orderProperty  the name of the property that is to be used to sequence the results
     * @param status         By default, relationships in all statuses are returned.
     *                       However, it is possible to specify a single status (eg ACTIVE) to restrict the results to.
     * @param excludeDeleted exclude deleted entities from result
     * @return list of properties used to narrow the search
     */
    @RequestMapping(method = RequestMethod.GET, path = "/search-asset/{searchCriteria}")
    public AssetDescriptionResponse searchAssets(@PathVariable("userId") String userId,
                                                 @PathVariable("searchCriteria") String searchCriteria,
                                                 @RequestParam(required = false, value = "limit", defaultValue = "0") Integer limit,
                                                 @RequestParam(required = false, value = "offset", defaultValue = "0") Integer offset,
                                                 @RequestParam(required = false, value = "orderType") SequencingOrder orderType,
                                                 @RequestParam(required = false, value = "orderProperty") String orderProperty,
                                                 @RequestParam(required = false, value = "status") Status status,
                                                 @RequestParam(required = false, value = "excludeDeleted") Boolean excludeDeleted) {
        return restAPI.searchAssets(userId, searchCriteria);
    }
}
