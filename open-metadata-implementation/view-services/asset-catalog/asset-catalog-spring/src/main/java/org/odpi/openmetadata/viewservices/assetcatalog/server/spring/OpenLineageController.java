/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.assetcatalog.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.viewservices.assetcatalog.beans.Graph;
import org.odpi.openmetadata.viewservices.assetcatalog.beans.LineageVertex;
import org.odpi.openmetadata.viewservices.assetcatalog.rest.ElementHierarchyRequest;
import org.odpi.openmetadata.viewservices.assetcatalog.rest.LineageNodeNamesResponse;
import org.odpi.openmetadata.viewservices.assetcatalog.rest.LineageResponse;
import org.odpi.openmetadata.viewservices.assetcatalog.rest.LineageSearchRequest;
import org.odpi.openmetadata.viewservices.assetcatalog.rest.LineageSearchResponse;
import org.odpi.openmetadata.viewservices.assetcatalog.rest.LineageTypesResponse;
import org.odpi.openmetadata.viewservices.assetcatalog.rest.LineageVertexResponse;
import org.odpi.openmetadata.viewservices.assetcatalog.server.AssetCatalogRESTServices;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * This controller serves all requests for retrieving lineage details, both vertical and horizontal.  It originated in the Egeria UI application.
 * It is deprecated because it does not follow the exception handling rules for Egeria code.  The replacement services are in AssetCatalogResource.
 */
@RestController
@RequestMapping("/servers/{serverName}/api/open-metadata/asset-catalog/old/lineage")

@Tag(name="API: Asset Catalog OMVS",
     description="Search for assets, retrieve their properties, lineage and related glossary information.",
     externalDocs=@ExternalDocumentation(description="Further Information", url="https://egeria-project.org/services/omvs/asset-catalog/overview/"))

@Deprecated
public class OpenLineageController
{
    private final RESTExceptionHandler exceptionHandler = new RESTExceptionHandler();

    private final AssetCatalogRESTServices restAPI = new AssetCatalogRESTServices();

    /**
     * Return the nodes and relationships that describe the know ultimate sources of the starting element.
     *
     * @param serverName name of the server to route the request to
     * @param guid unique identifier of the starting element
     * @param includeProcesses if true Process nodes will be included
     * @return graph of nodes and edges describing the ultimate sources of the asset
     * @throws InvalidParameterException from the underlying service
     * @throws PropertyServerException from the underlying service
     * @throws UserNotAuthorizedException from the underlying service
     */
    @GetMapping( value = "/entities/{guid}/ultimate-source")
    public Graph getUltimateSource(@PathVariable String serverName,
                                   @PathVariable("guid") String guid,
                                   @RequestParam boolean includeProcesses) throws InvalidParameterException,
                                                                                  PropertyServerException,
                                                                                  UserNotAuthorizedException
    {
        final String methodName = "getUltimateSource";

        LineageResponse restResult = restAPI.getUltimateSource(serverName, guid, includeProcesses);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult.getGraph();
    }

    
    /**
     * Return the graph of nodes that describe the end-to-end lineage for the starting element.
     *
     * @param serverName name of the server to route the request to
     * @param guid unique identifier of the starting element
     * @param includeProcesses if true Process nodes will be included
     * @return graph of nodes and edges describing the end to end flow
     * @throws InvalidParameterException from the underlying service
     * @throws PropertyServerException from the underlying service
     * @throws UserNotAuthorizedException from the underlying service
     */
    @GetMapping( value = {"/entities/{guid}/end-to-end", "/entities/{guid}/end2end"})

    public Graph getEndToEndLineage(@PathVariable String serverName,
                                    @PathVariable("guid") String guid,
                                    @RequestParam boolean includeProcesses) throws InvalidParameterException,
                                                                                PropertyServerException,
                                                                                UserNotAuthorizedException
    {
        final String methodName = "getEndToEndLineage";

        LineageResponse restResult = restAPI.getEndToEndLineage(serverName, guid, includeProcesses);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult.getGraph();
    }


    /**
     * Return the nodes and relationships that describe the know ultimate destinations of the starting element.
     *
     * @param serverName name of the server to route the request to
     * @param guid unique identifier of the starting element
     * @param includeProcesses if true Process nodes will be included
     * @return graph of nodes and edges describing the ultimate destination of the asset
     * @throws InvalidParameterException from the underlying service
     * @throws PropertyServerException from the underlying service
     * @throws UserNotAuthorizedException from the underlying service
     */
    @GetMapping( value = "/entities/{guid}/ultimate-destination")
    public Graph getUltimateDestination(@PathVariable String serverName,
                                        @PathVariable("guid") String guid,
                                        @RequestParam boolean includeProcesses) throws InvalidParameterException,
                                                                                       PropertyServerException,
                                                                                       UserNotAuthorizedException
    {
        final String methodName = "getUltimateDestination";

        LineageResponse restResult = restAPI.getUltimateDestination(serverName, guid, includeProcesses);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult.getGraph();
    }


    /**
     * Retrieve details of the asset related elements linked to a glossary term via the semantic assignment relationship.
     *
     * @param serverName name of the server to route the request to
     * @param guid unique identifier of the starting glossary term element
     * @param includeProcesses if true Process nodes will be included
     * @return graph of nodes and edges describing the assets linked to the glossary term
     * @throws InvalidParameterException from the underlying service
     * @throws PropertyServerException from the underlying service
     * @throws UserNotAuthorizedException from the underlying service
     */
    @GetMapping( value = {"/entities/{guid}/vertical-lineage", "/entities/{guid}/semantic-lineage"})

    public Graph getSemanticLineage(@PathVariable String serverName,
                                    @PathVariable("guid") String guid,
                                    @RequestParam boolean includeProcesses) throws InvalidParameterException,
                                                                                PropertyServerException,
                                                                                UserNotAuthorizedException
    {
        final String methodName = "getSemanticLineage";

        LineageResponse restResult = restAPI.getSemanticLineage(serverName, guid, includeProcesses);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult.getGraph();
    }


    /**
     * Gets entity details.
     *
     * @param serverName name of the server to route the request to
     * @param guid of the Entity to be retrieved
     * @return the entity details
     * @throws InvalidParameterException from the underlying service
     * @throws PropertyServerException from the underlying service
     * @throws UserNotAuthorizedException from the underlying service
     */
    @GetMapping( value = "entities/{guid}/details")

    public LineageVertex getLineageVertex(@PathVariable String serverName,
                                          @PathVariable("guid") String guid) throws InvalidParameterException,
                                                                                    PropertyServerException,
                                                                                    UserNotAuthorizedException
    {
        final String methodName = "getLineageVertex";

        LineageVertexResponse restResult = restAPI.getLineageVertex(serverName, guid);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult.getLineageVertex();
    }


    /**
     * Retrieves available entity types from lineage repository.
     *
     * @return the available entities types
     * @throws InvalidParameterException from the underlying service
     * @throws PropertyServerException from the underlying service
     * @throws UserNotAuthorizedException from the underlying service
     */
    @GetMapping( value = "types")
    public List<String> getTypes(@PathVariable String serverName) throws InvalidParameterException,
                                                                         PropertyServerException,
                                                                         UserNotAuthorizedException
    {
        final String methodName = "getTypes";

        LineageTypesResponse restResult = restAPI.getTypes(serverName);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult.getTypes();
    }


    /**
     * Gets nodes names of certain type with display name containing a certain value.
     *
     * @param serverName name of the server to route the request to
     * @param type        the type of the nodes name to search for
     * @param searchValue the string to be contained in the qualified name of the node - case-insensitive
     * @param limit       the maximum number of node names to retrieve
     *
     * @return the list of node names
     * @throws InvalidParameterException from the underlying service
     * @throws PropertyServerException from the underlying service
     * @throws UserNotAuthorizedException from the underlying service
     */
    @GetMapping( value = "nodes")
    public List<String> getNodes(@PathVariable String serverName,
                                 @RequestParam("type") String type,
                                 @RequestParam("name") String searchValue,
                                 @RequestParam("limit") int limit) throws InvalidParameterException,
                                                                          PropertyServerException,
                                                                          UserNotAuthorizedException
    {
        final String methodName = "getNodes";

        LineageNodeNamesResponse restResult = restAPI.getNodes(serverName, type, searchValue, limit);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult.getNames();
    }


    /**
     * Returns the nodes that the user will initially see when querying lineage. In the future, this method will be
     * extended to condense large paths to prevent cluttering of the users screen. The user will be able to extend
     * the condensed path by querying a different method.
     *
     * @param serverName name of the server to route the request to
     * @param searchRequest filtering details for the search
     * @return the entity details
     * @throws InvalidParameterException from the underlying service
     * @throws PropertyServerException from the underlying service
     * @throws UserNotAuthorizedException from the underlying service
     */
    @PostMapping( value = "entities/search")
    public List<LineageVertex> searchForVertices(@PathVariable String               serverName,
                                                 @RequestBody  LineageSearchRequest searchRequest) throws InvalidParameterException,
                                                                                                          PropertyServerException,
                                                                                                          UserNotAuthorizedException
    {
        final String methodName = "searchForVertices";

        LineageSearchResponse restResult = restAPI.searchForVertices(serverName, searchRequest);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult.getVertices();
    }


    /**
     * Returns a subgraph representing the hierarchy of a certain node, based on the request.
     *
     * @param serverName name of the server to route the request to
     * @param elementHierarchyRequest contains the guid of the queried node and the hierarchyType of the display name of the nodes
     *
     * @return a subgraph containing all relevant paths,
     * @throws InvalidParameterException from the underlying service
     * @throws PropertyServerException from the underlying service
     * @throws UserNotAuthorizedException from the underlying service
     */
    @PostMapping(value = "elements/hierarchy")
    public Graph getElementHierarchy(@PathVariable String                  serverName,
                                     @RequestBody  ElementHierarchyRequest elementHierarchyRequest) throws InvalidParameterException,
                                                                                                           PropertyServerException,
                                                                                                           UserNotAuthorizedException
    {
        final String methodName = "getElementHierarchy";

        LineageResponse restResult = restAPI.getElementHierarchy(serverName, elementHierarchyRequest);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult.getGraph();
    }
}
