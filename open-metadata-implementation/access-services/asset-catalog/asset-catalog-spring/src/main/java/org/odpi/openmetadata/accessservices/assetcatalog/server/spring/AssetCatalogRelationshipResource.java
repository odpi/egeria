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
@RequestMapping("/servers/{serverName}/open-metadata/access-services/asset-catalog/users/{userId}/relationships")
public class AssetCatalogRelationshipResource {

    private AssetCatalogRelationshipService relationshipService = new AssetCatalogRelationshipService();

    /**
     * Fetch relationship details based on its unique identifier
     *
     * @param serverName     unique identifier for requested server.
     * @param userId         String unique identifier for the user
     * @return relationship details
     */
    @RequestMapping(method = RequestMethod.GET,
            path = "/{entity1GUID}/{entity2GUID}",
            produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
    public RelationshipResponse getRelationship(@PathVariable("serverName") String serverName,
                                                @PathVariable("userId") String userId,
                                                @PathVariable("entity1GUID") String entity1GUID,
                                                @PathVariable("entity2GUID") String entity2GUID,
                                                @RequestParam("entity1TypeName") String entity1TypeName,
                                                @RequestParam("relationshipTypeGUID") String relationshipTypeGUID,
                                                @RequestParam("relationshipTypeName") String relationshipTypeName) {
        return relationshipService.getRelationshipBetweenEntities(serverName, userId, entity1GUID, entity2GUID, entity1TypeName, relationshipTypeGUID, relationshipTypeName);
    }
}
