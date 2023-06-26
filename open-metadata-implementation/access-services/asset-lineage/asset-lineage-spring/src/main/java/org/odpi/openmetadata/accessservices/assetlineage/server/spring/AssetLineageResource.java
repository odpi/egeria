/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetlineage.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.accessservices.assetlineage.model.FindEntitiesParameters;
import org.odpi.openmetadata.accessservices.assetlineage.server.AssetLineageRestServices;
import org.odpi.openmetadata.commonservices.ffdc.rest.ConnectionResponse;
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

import java.util.Date;
import java.util.List;

/**
 * The AssetLineageResource class is a Spring REST controller that provides endpoints for querying and publishing asset lineage information.
 * It is part of the Asset Lineage OMAS (Open Metadata Access Service) and allows users to interact with the asset lineage.
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/access-services/asset-lineage/users/{userId}/")

@Tag(name = "Asset Lineage OMAS",
        description = "The Asset Lineage OMAS provides services to query the lineage of business terms and data assets.",
        externalDocs = @ExternalDocumentation(description = "Asset Lineage Open Metadata Access Service (OMAS)",
                url = "https://egeria-project.org/services/omas/asset-lineage/overview/"))
public class AssetLineageResource {

    private final AssetLineageRestServices restAPI = new AssetLineageRestServices();

    /**
     * Scan the cohort based on the given entity type and publish the contexts for the found entities to the out topic.
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
    @Operation(summary = "scanEntitiesByTypeAndPublish",
               description = "Scan the cohort based on the given entity type and publish the contexts for the found entities to the out topic.",
               externalDocs = @ExternalDocumentation(description = "Asset Lineage - Publish Entities",
                                                     url = "https://egeria-project.org/services/omas/asset-lineage/overview/#publish-entities"))
    public GUIDListResponse publishEntities(@PathVariable String serverName,
                                            @PathVariable String userId,
                                            @PathVariable String entityType,
                                            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date updatedAfterDate,
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
    @Operation(summary = "findEntityByGuidAndPublishContext",
               description = "Find the entity by guid and publish the context for it.",
               externalDocs = @ExternalDocumentation(description = "Asset Lineage - Publish Entity",
                                                  url = "https://egeria-project.org/services/omas/asset-lineage/overview/#publish-entity"))
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
    @Operation(summary = "findEntityByGuidAndPublishAssetContext",
               description = "Find the entity by guid and publish the asset context for it. It applies for data tables and files.",
               externalDocs = @ExternalDocumentation(description = "Asset Lineage - Publish Asset Context",
                                                     url = "https://egeria-project.org/services/omas/asset-lineage/overview/#publish-context"))
    public GUIDListResponse publishAssetContext(@PathVariable String serverName,
                                            @PathVariable String userId,
                                            @PathVariable String guid,
                                            @PathVariable String entityType) {
        return restAPI.publishAssetContext(serverName, userId, entityType, guid);
    }

    /**
     * Return the connection object for the Asset Lineage's OMAS's out topic.
     *
     * @param serverName name of the server to route the request to
     * @param userId identifier of calling user
     * @param callerId unique identifier of the caller
     *
     * @return connection object for the out topic or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem retrieving the discovery engine definition.
     */
    @GetMapping(path = "/topics/out-topic-connection/{callerId}")
    @Operation(summary = "getOutTopicConnection",
               description = "Return the connection object for the Asset Lineage's OMAS's out topic.",
               externalDocs = @ExternalDocumentation(description = "Asset Lineage - Out Topic Connection",
                                                     url = "https://egeria-project.org/services/omas/asset-lineage/overview/#out-topic-connection"))
    public ConnectionResponse getOutTopicConnection(@PathVariable String serverName,
                                                    @PathVariable String userId,
                                                    @PathVariable String callerId)
    {
        return restAPI.getOutTopicConnection(serverName, userId, callerId);
    }

}
