/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetcatalog.server.spring;

import org.odpi.openmetadata.accessservices.assetcatalog.model.rest.responses.RelationshipResponse;
import org.odpi.openmetadata.accessservices.assetcatalog.service.AssetCatalogRelationshipService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * The AssetCatalogRelationshipResource provides the server-side implementation of the Asset Catalog Open Metadata
 * Assess Service (OMAS). This interface facilitates the searching for asset's relationships, fetch the details about a specific relationship.
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/access-services/asset-catalog/users/{userId}")
public class AssetCatalogRelationshipResource {

    private AssetCatalogRelationshipService relationshipService = new AssetCatalogRelationshipService();

    /**
     * Fetch relationship between entities details based on its unique identifier of the ends
     *
     * @param serverName           unique identifier for requested server.
     * @param userId               String unique identifier for the user
     * @param entity1GUID          Entity guid of the first end of the relationship
     * @param entity2GUID          Entity guid of the second end of the relationship
     * @param relationshipTypeGUID Type of the relationship
     * @return relationships between entities
     */
    @RequestMapping(method = RequestMethod.GET,
            path = "/relationship-between-entities/{entity1GUID}/{entity2GUID}",
            produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
    public RelationshipResponse getRelationshipBetweenEntities(@PathVariable("serverName") String serverName,
                                                               @PathVariable("userId") String userId,
                                                               @PathVariable("entity1GUID") String entity1GUID,
                                                               @PathVariable("entity2GUID") String entity2GUID,
                                                               @RequestParam(name = "relationshipTypeGUID") String relationshipTypeGUID) {
        return relationshipService.getRelationshipBetweenEntities(serverName, userId, entity1GUID, entity2GUID, relationshipTypeGUID);
    }
}
