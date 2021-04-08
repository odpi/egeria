/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetlineage.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.accessservices.assetlineage.model.FindEntitiesParameters;
import org.odpi.openmetadata.accessservices.assetlineage.server.AssetLineageRestServices;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDListResponse;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.SearchClassifications;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;


@RestController
@RequestMapping("/servers/{serverName}/open-metadata/access-services/asset-lineage/users/{userId}/")

@Tag(name = "Asset Lineage OMAS",
        description = "The Asset Lineage OMAS provides services to query the lineage of business terms and data assets.",
        externalDocs = @ExternalDocumentation(description = "Asset Lineage Open Metadata Access Service (OMAS)",
                url = "https://egeria.odpi.org/open-metadata-implementation/access-services/asset-lineage/"))
public class AssetLineageResource {

    private final AssetLineageRestServices restAPI = new AssetLineageRestServices();

    /**
     * Scan the cohort based on the given entity type and publish the contexts for the found entities to the out topic
     *
     * @param serverName name of server instance to call
     * @param userId     the name of the calling user
     * @param entityType the name of the relationship type
     * @param updatedAfterDate      match entities updated starting from this date forward. The date must be provided in the ISO format
     * @param entitySubtypeGUIDs    optional list of the unique identifiers (guids) for subtypes of the entityTypeGUID to
     *                              include in the search results. Null means all subtypes.
     * @param limitResultsByStatus  By default, entities in all statuses are returned.  However, it is possible
     *                              to specify a list of statuses (eg ACTIVE) to restrict the results to.  Null means all
     *                              status values.
     * @param searchClassifications Optional list of entity classifications to match.
     * @param sequencingProperty    String name of the entity property that is to be used to sequence the results.
     *                              Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder       Enum defining how the results should be ordered.
     * @return a list of unique identifiers (guids) of the available entities with the given type provided as a response
     */
    @GetMapping(path = "/publish-entities/{entityType}")
    public GUIDListResponse publishEntities(@PathVariable String serverName,
                                            @PathVariable String userId,
                                            @PathVariable String entityType,
                                            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime updatedAfterDate,
                                            @RequestParam(required = false) List<String> entitySubtypeGUIDs,
                                            @RequestParam(required = false) List<InstanceStatus> limitResultsByStatus,
                                            @RequestParam(required = false) SearchClassifications searchClassifications,
                                            @RequestParam(required = false) String sequencingProperty,
                                            @RequestParam(required = false) SequencingOrder sequencingOrder) {

        FindEntitiesParameters findEntitiesParameters = new FindEntitiesParameters.Builder()
                .withUpdatedAfter(updatedAfterDate)
                .withEntitySubtypeGUIDs(entitySubtypeGUIDs)
                .withLimitResultsByStatus(limitResultsByStatus)
                .withSearchClassifications(searchClassifications)
                .withSequencingProperty(sequencingProperty)
                .withSequencingOrder(sequencingOrder)
                .build();
        return restAPI.publishEntities(serverName, userId, entityType, findEntitiesParameters);
    }

    /**
     * Find the entity by guid and publish the context for it
     *
     * @param serverName name of server instance to call
     * @param userId     the name of the calling user
     * @param guid       the guid of the entity to build context
     * @param entityType the name of the relationship type
     * @return a list of unique identifiers (guids) of the available entity with the given type provided as a response
     */
    @GetMapping(path = "/publish-entity/{entityType}/{guid}")
    public GUIDListResponse publishEntity(@PathVariable String serverName,
                                          @PathVariable String userId,
                                          @PathVariable String guid,
                                          @PathVariable String entityType) {
        return restAPI.publishEntity(serverName, userId, entityType, guid);
    }

    /**
     * Find the entity by guid and publish the asset context for it. It applies for data tables and files.
     *
     * @param serverName name of server instance to call
     * @param userId     the name of the calling user
     * @param guid       the guid of the entity to build context
     * @param entityType the name of the relationship type
     * @return a list of unique identifiers (guids) of the available entities that exist in the published context
     */
    @GetMapping(path = "/publish-context/{entityType}/{guid}")
    public GUIDListResponse publishAssetContext(@PathVariable String serverName,
                                            @PathVariable String userId,
                                            @PathVariable String guid,
                                            @PathVariable String entityType) {
        return restAPI.publishAssetContext(serverName, userId, entityType, guid);
    }
}
