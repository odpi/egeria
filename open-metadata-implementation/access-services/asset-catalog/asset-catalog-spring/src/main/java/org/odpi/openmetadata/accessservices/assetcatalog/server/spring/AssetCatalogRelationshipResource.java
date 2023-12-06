/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetcatalog.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.accessservices.assetcatalog.model.rest.responses.RelationshipResponse;
import org.odpi.openmetadata.accessservices.assetcatalog.service.AssetCatalogRelationshipRESTService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * The AssetCatalogRelationshipResource provides the server-side implementation of the Asset Catalog Open Metadata
 * Assess Service (OMAS). This interface facilitates the searching for asset's relationships, fetch the details about a specific relationship.
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/access-services/asset-catalog/users/{userId}")

@Tag(name="Metadata Access Server: Asset Catalog OMAS", description="The Asset Catalog OMAS provides services to search for data assets including data stores, event feeds, APIs, data sets.",
     externalDocs=@ExternalDocumentation(description="Further Information",
                                         url="https://egeria-project.org/services/omas/asset-catalog/overview/"))
@Deprecated
public class AssetCatalogRelationshipResource {

    private final AssetCatalogRelationshipRESTService relationshipService = new AssetCatalogRelationshipRESTService();

    /**
     * Fetch relationship between entities details based on its unique identifier of the ends
     *
     * @param serverName       unique identifier for requested server.
     * @param userId           String unique identifier for the user
     * @param entity1GUID      Entity guid of the first end of the relationship
     * @param entity2GUID      Entity guid of the second end of the relationship
     * @param relationshipType Type of the relationship
     * @return relationships between entities
     */
    @GetMapping(path = "/relationship-between-entities/{entity1GUID}/{entity2GUID}",
            produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "getRelationshipBetweenEntities", description = "Returns the relationships between the 2 given entities",
            externalDocs = @ExternalDocumentation(description = "Metadata Relationships",
            url = "https://egeria-project.org/patterns/metadata-manager/categories-of-metadata/?h=relationships#metadata-relationships-and-classifications"))
    public RelationshipResponse getRelationshipBetweenEntities(@PathVariable("serverName") String serverName,
                                                               @PathVariable("userId") String userId,
                                                               @PathVariable("entity1GUID") String entity1GUID,
                                                               @PathVariable("entity2GUID") String entity2GUID,
                                                               @RequestParam(name = "relationshipType", required = false) String relationshipType) {
        return relationshipService.getRelationshipBetweenEntities(serverName, userId, entity1GUID, entity2GUID, relationshipType);
    }
}
