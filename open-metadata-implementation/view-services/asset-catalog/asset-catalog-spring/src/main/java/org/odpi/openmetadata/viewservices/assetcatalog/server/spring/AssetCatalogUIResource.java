/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project */
package org.odpi.openmetadata.viewservices.assetcatalog.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.frameworks.governanceaction.search.SequencingOrder;
import org.odpi.openmetadata.viewservices.assetcatalog.rest.AssetCatalogResponse;
import org.odpi.openmetadata.viewservices.assetcatalog.rest.AssetCatalogSupportedTypes;
import org.odpi.openmetadata.viewservices.assetcatalog.rest.AssetListResponse;
import org.odpi.openmetadata.viewservices.assetcatalog.rest.AssetResponse;
import org.odpi.openmetadata.viewservices.assetcatalog.rest.ElementHierarchyRequest;
import org.odpi.openmetadata.viewservices.assetcatalog.rest.LineageNodeNamesResponse;
import org.odpi.openmetadata.viewservices.assetcatalog.rest.LineageResponse;
import org.odpi.openmetadata.viewservices.assetcatalog.rest.LineageSearchRequest;
import org.odpi.openmetadata.viewservices.assetcatalog.rest.LineageSearchResponse;
import org.odpi.openmetadata.viewservices.assetcatalog.rest.LineageTypesResponse;
import org.odpi.openmetadata.viewservices.assetcatalog.rest.LineageVertexResponse;
import org.odpi.openmetadata.viewservices.assetcatalog.server.AssetCatalogUIRESTServices;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * The AssetCatalogUIResource provides the Spring API endpoints of the Asset Catalog Open Metadata View Service (OMVS).
 * This interface provides the interfaces for Egeria UIs.  The set with "old" in their URL are from the Egeria UI Application.
 * They are deprecated because they do not conform the Egeria's error handling standards.
 */

@RestController
@RequestMapping("/servers/{serverName}/api/open-metadata/asset-catalog/ui")

@Tag(name="API: Asset Catalog OMVS",
     description="Search for assets, retrieve their properties, lineage and related glossary information.",
     externalDocs=@ExternalDocumentation(description="Further Information",url="https://egeria-project.org/services/omvs/asset-catalog/overview/"))

public class AssetCatalogUIResource
{

    private final AssetCatalogUIRESTServices restAPI = new AssetCatalogUIRESTServices();


    /**
     * Default constructor
     */
    public AssetCatalogUIResource()
    {
    }



    /**
     * Return a list of assets matching the search criteria without the full context
     *
     * @param serverName name of the server to route the request to
     * @param searchCriteria the query parameter with the search phrase
     * @param types OM types list to search for
     * @param sequencingProperty name of the property based on which to sort the result
     * @param sequencingOrder PROPERTY_ASCENDING or PROPERTY_DESCENDING
     * @param caseSensitive set case sensitive flag
     * @param exactMatch set exact match flag
     * @param startFrom the offset for the results
     * @param pageSize the number of results per page
     * @return list of assets or
     *  PropertyServerException if a configuration on the backend
     *  UserNotAuthorizedException security access problem
     *  InvalidParameterException if parameter validation fails
     */
    @GetMapping( path = "/assets/search")
    public AssetListResponse searchAssets(@PathVariable String serverName,
                                          @RequestParam("q") String searchCriteria,
                                          @RequestParam("types") List<String> types,
                                          @RequestParam(name = "sequencingProperty",
                                                        defaultValue = "displayName") String sequencingProperty,
                                          @RequestParam(name = "sequencingOrder",
                                                        defaultValue = "PROPERTY_ASCENDING") SequencingOrder sequencingOrder,
                                          @RequestParam(defaultValue="false")  boolean caseSensitive,
                                          @RequestParam(defaultValue="false") boolean exactMatch,
                                          @RequestParam(name = "from", defaultValue="0") Integer startFrom,
                                          @RequestParam(defaultValue="10") Integer pageSize)
    {
        return restAPI.searchAssets(serverName,
                                    searchCriteria,
                                    types,
                                    sequencingProperty,
                                    sequencingOrder,
                                    caseSensitive,
                                    exactMatch,
                                    startFrom,
                                    pageSize);
    }


    /**
     * Return a list of assets matching the type name without the full context
     * The list includes also subtypes
     *
     * @param serverName name of the server to route the request to
     * @param typeName the assets type name to search for
     * @return list of assets by type name or
     *  PropertyServerException if a configuration on the backend
     *  UserNotAuthorizedException security access problem
     *  InvalidParameterException if parameter validation fails
     */
    @GetMapping( path = "/assets/search-by-type-name/{typeName}")
    public AssetListResponse searchAssetsByTypeName(@PathVariable             String serverName,
                                                    @PathVariable("typeName") String typeName)
    {
        return restAPI.searchAssetsByTypeName(serverName, typeName);
    }


    /**
     * Return a list of assets matching the type GUID without the full context.
     * The list includes also subtypes
     *
     * @param serverName name of the server to route the request to
     * @param typeGUID the assets type GUID to search for
     * @return list of assets by type GUID or
     *  PropertyServerException if a configuration on the backend
     *  UserNotAuthorizedException security access problem
     *  InvalidParameterException if parameter validation fails
     */
    @GetMapping( path = "/assets/search-by-type-guid/{typeGUID}")
    public AssetListResponse searchAssetsByTypeGUID(@PathVariable             String serverName,
                                                    @PathVariable("typeGUID") String typeGUID)
    {
        return restAPI.searchAssetsByTypeGUID(serverName, typeGUID);
    }


    /**
     * Returns the list with supported types for search, including the subtypes supported
     *
     * @param serverName name of the server to route the request to
     * @return the supported types from Asset Consumer OMAS or
     *  PropertyServerException if a configuration on the backend
     *  InvalidParameterException if parameter validation fails
     *  UserNotAuthorizedException security access problem
     */
    @GetMapping( path = "/assets/types")
    public AssetCatalogSupportedTypes getSupportedTypes(@PathVariable String serverName)
    {
        return restAPI.getSupportedTypes(serverName);
    }


    /**
     * Fetch asset's header, classification and properties.
     *
     * @param serverName name of the server to route the request to
     * @param guid of the Entity to be retrieved
     * @return the asset details or
     *  PropertyServerException if a configuration on the backend
     *  UserNotAuthorizedException security access problem
     *  InvalidParameterException if parameter validation fails
     */
    @GetMapping( value = "/assets/{guid}")
    public AssetCatalogResponse getAsset(@PathVariable String serverName,
                                         @PathVariable("guid") String guid)
    {
        return restAPI.getAsset(serverName, guid);
    }


    /**
     * Return the full context of an asset/glossary term based on its identifier.
     * The response contains the list of the connections assigned to the asset.
     *
     * @param serverName name of the server to route the request to
     * @param guid of the Entity to be retrieved
     * @return the entity context or
     * PropertyServerException if a configuration on the backend
     * UserNotAuthorizedException security access problem
     * InvalidParameterException if parameter validation fails
     */
    @GetMapping( value = "/assets/{guid}/context")
    public AssetResponse getAssetContext(@PathVariable String serverName,
                                         @PathVariable String guid)
    {
        return restAPI.getAssetContext(serverName, guid);
    }


    /**
     * Return the nodes and relationships that describe the know ultimate sources of the starting element.
     *
     * @param serverName name of the server to route the request to
     * @param guid unique identifier of the starting element
     * @param includeProcesses if true Process nodes will be included
     * @return graph of nodes and edges describing the ultimate sources of the asset or
     *  InvalidParameterException from the underlying service,
     *  PropertyServerException from the underlying service or
     *  UserNotAuthorizedException from the underlying service
     */
    @GetMapping( value = "/lineage/entities/{guid}/ultimate-source")
    public LineageResponse getUltimateSource(@PathVariable String serverName,
                                             @PathVariable("guid") String guid,
                                             @RequestParam boolean includeProcesses)
    {
        return restAPI.getUltimateSource(serverName, guid, includeProcesses);
    }


    /**
     * Return the graph of nodes that describe the end-to-end lineage for the starting element.
     *
     * @param serverName name of the server to route the request to
     * @param guid unique identifier of the starting element
     * @param includeProcesses if true Process nodes will be included
     * @return graph of nodes and edges describing the end to end flow or
     *  InvalidParameterException from the underlying service,
     *  PropertyServerException from the underlying service or
     *  UserNotAuthorizedException from the underlying service
    */
    @GetMapping( value = {"/lineage/entities/{guid}/end-to-end"})

    public LineageResponse getEndToEndLineage(@PathVariable String serverName,
                                              @PathVariable("guid") String guid,
                                              @RequestParam boolean includeProcesses)
    {
        return restAPI.getEndToEndLineage(serverName, guid, includeProcesses);
    }


    /**
     * Return the nodes and relationships that describe the know ultimate destinations of the starting element.
     *
     * @param serverName name of the server to route the request to
     * @param guid unique identifier of the starting element
     * @param includeProcesses if true Process nodes will be included
     * @return graph of nodes and edges describing the ultimate destination of the asset or
     *  InvalidParameterException from the underlying service,
     *  PropertyServerException from the underlying service or
     *  UserNotAuthorizedException from the underlying service
     */
    @GetMapping( value = "/lineage/entities/{guid}/ultimate-destination")
    public LineageResponse getUltimateDestination(@PathVariable String serverName,
                                                  @PathVariable("guid") String guid,
                                                  @RequestParam boolean includeProcesses)
    {
        return restAPI.getUltimateDestination(serverName, guid, includeProcesses);
    }


    /**
     * Retrieve details of the asset related elements linked to a glossary term via the semantic assignment relationship.
     *
     * @param serverName name of the server to route the request to
     * @param guid unique identifier of the starting glossary term element
     * @param includeProcesses if true Process nodes will be included
     * @return graph of nodes and edges describing the assets linked to the glossary term or
     *  InvalidParameterException from the underlying service,
     *  PropertyServerException from the underlying service or
     *  UserNotAuthorizedException from the underlying service
    */
    @GetMapping( value = "/lineage/entities/{guid}/semantic-lineage")
    public LineageResponse getSemanticLineage(@PathVariable String serverName,
                                              @PathVariable("guid") String guid,
                                              @RequestParam boolean includeProcesses)
    {
        return restAPI.getSemanticLineage(serverName, guid, includeProcesses);
    }


    /**
     * Retrieves the details of a specific node in the lineage graph.
     *
     * @param serverName name of the server to route the request to
     * @param guid of the Entity to be retrieved
     * @return the entity details or
     *  InvalidParameterException from the underlying service,
     *  PropertyServerException from the underlying service or
     *  UserNotAuthorizedException from the underlying service
     */
    @GetMapping( value = "/lineage/entities/{guid}/details")
    public LineageVertexResponse getLineageVertex(@PathVariable String serverName,
                                                  @PathVariable("guid") String guid)
    {
        return restAPI.getLineageVertex(serverName, guid);
    }


    /**
     * Gets available entities types from lineage repository.
     *
     * @param serverName name of the server to route the request to
     * @return the available entities types or
     *  InvalidParameterException from the underlying service,
     *  PropertyServerException from the underlying service or
     *  UserNotAuthorizedException from the underlying service
     */
    @GetMapping( value = "/lineage/types")
    public LineageTypesResponse getTypes(@PathVariable String serverName)
    {
        return restAPI.getTypes(serverName);
    }


    /**
     * Gets nodes names of certain type with display name containing a certain value.
     *
     * @param serverName name of the server to route the request to
     * @param type        the type of the nodes name to search for
     * @param searchValue the string to be contained in the qualified name of the node - case-insensitive
     * @param limit       the maximum number of node names to retrieve
     *
     * @return the list of node names or
     *  InvalidParameterException from the underlying service,
     *  PropertyServerException from the underlying service or
     *  UserNotAuthorizedException from the underlying service
     */
    @GetMapping( value = "/lineage/nodes")
    public LineageNodeNamesResponse getNodes(@PathVariable String serverName,
                                             @RequestParam("type") String type,
                                             @RequestParam("name") String searchValue,
                                             @RequestParam("limit") int limit)
    {
        return restAPI.getNodes(serverName, type, searchValue, limit);
    }


    /**
     * Returns the list of vertices that the user will initially see when querying lineage. In the future, this method will be
     * extended to condense large paths to prevent cluttering of the users screen. The user will be able to extend
     * the condensed path by querying a different method.
     *
     * @param serverName name of the server to route the request to
     * @param searchRequest filtering details for the search
     * @return list of graph nodes or
     *  InvalidParameterException from the underlying service,
     *  PropertyServerException from the underlying service or
     *  UserNotAuthorizedException from the underlying service
     */
    @PostMapping( value = "/lineage/nodes/search")
    public LineageSearchResponse searchForVertices(@PathVariable String               serverName,
                                                   @RequestBody  LineageSearchRequest searchRequest)
    {
        return restAPI.searchForVertices(serverName, searchRequest);
    }


    /**
     * Returns a subgraph representing the hierarchy of a certain node, based on the request.
     *
     * @param serverName name of the server to route the request to
     * @param elementHierarchyRequest contains the guid of the queried node and the hierarchyType of the display name of the nodes
     *
     * @return a subgraph containing all relevant paths or
     *  InvalidParameterException from the underlying service,
     *  PropertyServerException from the underlying service or
     *  UserNotAuthorizedException from the underlying service
     */
    @PostMapping(value = "/lineage/elements/hierarchy")
    public LineageResponse elementHierarchy(@PathVariable String                  serverName,
                                            @RequestBody  ElementHierarchyRequest elementHierarchyRequest)
    {
        return restAPI.getElementHierarchy(serverName, elementHierarchyRequest);
    }
}

