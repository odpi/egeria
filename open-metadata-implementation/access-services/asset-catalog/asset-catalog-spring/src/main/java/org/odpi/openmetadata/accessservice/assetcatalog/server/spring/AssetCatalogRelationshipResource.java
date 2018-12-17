/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservice.assetcatalog.server.spring;

import org.odpi.openmetadata.accessservice.assetcatalog.model.rest.body.SearchParameters;
import org.odpi.openmetadata.accessservice.assetcatalog.model.rest.responses.RelationshipResponse;
import org.odpi.openmetadata.accessservice.assetcatalog.model.rest.responses.RelationshipsResponse;
import org.odpi.openmetadata.accessservice.assetcatalog.service.AssetCatalogRelationshipService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * The AssetCatalogRelationshipResource provides the server-side implementation of the Asset Catalog Open Metadata
 * Assess Service (OMAS). This interface facilitates the searching for asset's relationships, fetch the details about a specific relationship.
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
     * @param userId           String unique identifier for the user
     * @param propertyName     String that it is used to identify the relationship label
     * @param searchParameters constrains to make the assets's search results more precise
     * @return a list of relationships that have the property specified
     */
    @RequestMapping(method = RequestMethod.POST,
            path = "/property-name/{propertyName}",
            produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
    public RelationshipsResponse getRelationshipByLabel(
            @PathVariable("serverName") String serverName,
            @PathVariable("userId") String userId,
            @PathVariable("propertyName") String propertyName,
            @RequestBody SearchParameters searchParameters) {
        return relationshipService.getRelationshipByProperty(serverName, userId, propertyName,
                searchParameters);
    }

    /**
     * Return a list of relationships that match the search criteria.
     *
     * @param serverName         unique identifier for requested server.
     * @param userId             Unique identifier for the user
     * @param relationshipTypeId Limit the result set to only include the specified types for relationships
     * @param criteria           String for searching the relationship
     * @param searchParameters constrains to make the assets's search results more precise
     * @return RelationshipsResponse    List of relationships that match the search criteria
     */
    @RequestMapping(method = RequestMethod.POST,
            path = "/type/{relationshipTypeId}/search/{criteria}",
            produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
    public RelationshipsResponse searchForRelationships(@PathVariable("serverName") String serverName,
                                                        @PathVariable("userId") String userId,
                                                        @PathVariable("relationshipTypeId") String relationshipTypeId,
                                                        @PathVariable("criteria") String criteria,
                                                        @RequestBody SearchParameters searchParameters) {
        return relationshipService.searchForRelationships(serverName, userId, relationshipTypeId, criteria, searchParameters);
    }
}
