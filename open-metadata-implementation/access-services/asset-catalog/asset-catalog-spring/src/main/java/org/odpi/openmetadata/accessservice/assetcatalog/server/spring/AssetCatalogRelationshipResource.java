/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservice.assetcatalog.server.spring;

import org.odpi.openmetadata.accessservice.assetcatalog.model.SequenceOrderType;
import org.odpi.openmetadata.accessservice.assetcatalog.model.Status;
import org.odpi.openmetadata.accessservice.assetcatalog.responses.RelationshipResponse;
import org.odpi.openmetadata.accessservice.assetcatalog.responses.RelationshipsResponse;
import org.odpi.openmetadata.accessservice.assetcatalog.service.AssetCatalogRelationshipService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * The AssetCatalogRelationshipResource provides the server-side implementation of the Asset Catalog Open Metadata
 * Assess Service (OMAS).
 * This interface facilitates the searching for asset's relationships, fetch the details about a specific relationship.
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/access-services/asset-catalog/users/{userId}/relationships")
public class AssetCatalogRelationshipResource {

    private AssetCatalogRelationshipService relationshipService = new AssetCatalogRelationshipService();

    /**
     * Fetch relationship details based on its unique identifier
     *
     * @param serverName     unique identifier for requested server.
     * @param userId         String unique identifier for the user
     * @param relationshipId String unique identifier for the relationship
     * @return relationship details
     */
    @RequestMapping(method = RequestMethod.GET,
            path = "/{relationshipId}",
            produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
    public RelationshipResponse getRelationship(@PathVariable("serverName") String serverName,
                                                @PathVariable("userId") String userId,
                                                @PathVariable("relationshipId") String relationshipId) {
        return relationshipService.getRelationshipById(serverName, userId, relationshipId);
    }

    /**
     * Fetch relationship details based on property name
     *
     * @param serverName         unique identifier for requested server.
     * @param userId             String unique identifier for the user
     * @param propertyName       String that it is used to identify the relationship label
     * @param propertyValue      list of properties used to narrow the search.
     * @param relationshipTypeId limit the result to include the specific relationship type
     * @param limit              limit the result set to only include the specified number of entries
     * @param offset             start offset of the result set (for pagination)
     * @param orderType          enum defining how the results should be ordered.
     * @param orderProperty      the name of the property that is to be used to sequence the results
     * @param status             By default, relationships in all statuses are returned.
     *                           However, it is possible to specify a single status (eg ACTIVE) to restrict the results to.
     * @return a list of relationships that have the property specified
     */
    @RequestMapping(method = RequestMethod.GET,
            path = "/property-name/{propertyName}",
            produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
    public RelationshipsResponse getRelationshipByLabel(
            @PathVariable("serverName") String serverName,
            @PathVariable("userId") String userId,
            @PathVariable("propertyName") String propertyName,
            @RequestParam(required = false, value = "propertyValue") String propertyValue,
            @RequestParam(required = false, value = "relationshipTypeId") String relationshipTypeId,
            @RequestParam(required = false, value = "limit", defaultValue = "0") Integer limit,
            @RequestParam(required = false, value = "offset", defaultValue = "0") Integer offset,
            @RequestParam(required = false, value = "orderType") SequenceOrderType orderType,
            @RequestParam(required = false, value = "orderProperty") String orderProperty,
            @RequestParam(required = false, value = "status") Status status) {
        return relationshipService.getRelationshipByProperty(serverName, userId, relationshipTypeId, propertyName,
                propertyValue, limit, offset, orderType, orderProperty, status);
    }

    /**
     * Return a list of relationships that match the search criteria.
     *
     * @param serverName         unique identifier for requested server.
     * @param userId             String unique identifier for the user
     * @param relationshipTypeId limit the result set to only include the specified types for relationships
     * @param criteria           String for searching the relationship
     * @param limit              limit the result set to only include the specified number of entries
     * @param offset             start offset of the result set (for pagination)
     * @param orderType          enum defining how the results should be ordered.
     * @param orderProperty      the name of the property that is to be used to sequence the results
     * @param status             By default, relationships in all statuses are returned.
     *                           However, it is possible to specify a single status (eg ACTIVE) to restrict the results to.
     * @return a list of relationships that match the search criteria
     */
    @RequestMapping(method = RequestMethod.GET,
            path = "/type/{relationshipTypeId}/search/{criteria}",
            produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
    public RelationshipsResponse searchForRelationships(@PathVariable("serverName") String serverName,
                                                        @PathVariable("userId") String userId,
                                                        @PathVariable("relationshipTypeId") String relationshipTypeId,
                                                        @PathVariable("criteria") String criteria,
                                                        @RequestParam(required = false, value = "limit", defaultValue = "0") Integer limit,
                                                        @RequestParam(required = false, value = "offset", defaultValue = "0") Integer offset,
                                                        @RequestParam(required = false, value = "orderType") SequenceOrderType orderType,
                                                        @RequestParam(required = false, value = "orderProperty") String orderProperty,
                                                        @RequestParam(required = false, value = "status") Status status) {
        return relationshipService.searchForRelationships(serverName, userId, relationshipTypeId, criteria, limit, offset, orderProperty, orderType, status);
    }
}
