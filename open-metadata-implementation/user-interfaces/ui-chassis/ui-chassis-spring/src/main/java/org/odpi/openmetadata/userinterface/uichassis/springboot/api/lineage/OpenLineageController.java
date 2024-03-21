/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.uichassis.springboot.api.lineage;

import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.governanceservers.lineagewarehouse.converters.ScopeEnumConverter;
import org.odpi.openmetadata.governanceservers.lineagewarehouse.ffdc.LineageWarehouseException;
import org.odpi.openmetadata.governanceservers.lineagewarehouse.model.LineageVertex;
import org.odpi.openmetadata.governanceservers.lineagewarehouse.model.Scope;
import org.odpi.openmetadata.governanceservers.lineagewarehouse.requests.ElementHierarchyRequest;
import org.odpi.openmetadata.governanceservers.lineagewarehouse.requests.LineageSearchRequest;
import org.odpi.openmetadata.userinterface.uichassis.springboot.beans.Graph;
import org.odpi.openmetadata.userinterface.uichassis.springboot.service.OpenLineageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * This controller serves all requests for retrieving lineage details, both vertical and horizontal
 */
@RestController
@RequestMapping("/api/lineage")
public class OpenLineageController {

    @Autowired
    private OpenLineageService openLineageService;

    /**
     *
     * @param guid unique identifier of the asset
     * @param includeProcesses if true Process nodes will be included
     * @return graph of nodes and edges describing the ultimate sources of the asset
     * @throws InvalidParameterException from the underlying service
     * @throws PropertyServerException from the underlying service
     * @throws LineageWarehouseException from the underlying service
     */
    @GetMapping( value = "/entities/{guid}/ultimate-source")
    public Graph ultimateSourceGraph(@PathVariable("guid") String guid , @RequestParam boolean includeProcesses)
            throws InvalidParameterException, PropertyServerException, LineageWarehouseException
    {
        Graph exportedGraph;
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        exportedGraph = openLineageService.getUltimateSource(userId, guid, includeProcesses);
        return exportedGraph;
    }

    /**
     *
     * @param guid unique identifier of the asset
     * @param includeProcesses if true Process nodes will be included
     * @return graph of nodes and edges describing the end to end flow
     * @throws InvalidParameterException from the underlying service
     * @throws PropertyServerException from the underlying service
     * @throws LineageWarehouseException from the underlying service
     *
     * TODO: Remove api request mapping /entities/{guid}/end2end in major release (i.e. v4.x.x)
     */
    @GetMapping( value = {"/entities/{guid}/end-to-end", "/entities/{guid}/end2end"})
    @ResponseBody
    public Graph endToEndLineage(@PathVariable("guid") String guid, @RequestParam boolean includeProcesses)
            throws InvalidParameterException, PropertyServerException, LineageWarehouseException
    {
        Graph exportedGraph;
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        exportedGraph = openLineageService.getEndToEndLineage(userId, guid, includeProcesses);
        return exportedGraph;
    }

    /**
     *
     * @param guid unique identifier of the asset
     * @param includeProcesses if true Process nodes will be included
     * @return graph of nodes and edges describing the ultimate destination of the asset
     * @throws InvalidParameterException from the underlying service
     * @throws PropertyServerException from the underlying service
     * @throws LineageWarehouseException from the underlying service
     */
    @GetMapping( value = "/entities/{guid}/ultimate-destination")
    public Graph ultimateDestination(@PathVariable("guid") String guid, @RequestParam boolean includeProcesses)
            throws InvalidParameterException, PropertyServerException, LineageWarehouseException
    {
        Graph exportedGraph;
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        exportedGraph = openLineageService.getUltimateDestination(userId, guid, includeProcesses);
        return exportedGraph;
    }


    /**
     *
     * @param guid unique identifier of the glossary term
     * @param includeProcesses if true Process nodes will be included
     * @return graph of nodes and edges describing the assets linked to the glossary term
     * @throws InvalidParameterException from the underlying service
     * @throws PropertyServerException from the underlying service
     * @throws LineageWarehouseException from the underlying service
     */
    @GetMapping( value = "/entities/{guid}/vertical-lineage")
    public Graph verticalLineage(@PathVariable("guid") String guid, @RequestParam boolean includeProcesses)
            throws InvalidParameterException, PropertyServerException, LineageWarehouseException
    {
        Graph exportedGraph;
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        exportedGraph = openLineageService.getVerticalLineage(userId, guid, includeProcesses);
        return exportedGraph;
    }

    /**
     * @param guid of the Entity to be retrieved
     * @return the entity details
     * @throws InvalidParameterException from the underlying service
     * @throws PropertyServerException from the underlying service
     * @throws LineageWarehouseException from the underlying service
     */
    @GetMapping( value = "entities/{guid}/details")
    public LineageVertex getEntityDetails(@PathVariable("guid") String guid)
            throws InvalidParameterException, PropertyServerException, LineageWarehouseException
    {
        String user = SecurityContextHolder.getContext().getAuthentication().getName();
        return openLineageService.getEntityDetails(user, guid);
    }

    /**
     * Gets available entities types from lineage repository.
     *
     * @return the available entities types
     */
    @GetMapping( value = "types")
    public List<String> getTypes() {
        String user = SecurityContextHolder.getContext().getAuthentication().getName();
        return openLineageService.getTypes(user);
    }

    /**
     * Gets nodes names of certain type with display name containing a certain value.
     * @param type        the type of the nodes name to search for
     * @param searchValue the string to be contained in the qualified name of the node - case insensitive
     * @param limit       the maximum number of node names to retrieve
     *
     * @return the list of node names
     */
    @GetMapping( value = "nodes")
    public List<String> getNodes(@RequestParam("type") String type, @RequestParam("name") String searchValue,
                                 @RequestParam("limit") int limit) {
        String user = SecurityContextHolder.getContext().getAuthentication().getName();
        return openLineageService.getNodes(user, type, searchValue, limit);
    }

    /**
     * @param searchRequest filtering details for the search
     * @return the entity details
     * @throws InvalidParameterException from the underlying service
     * @throws PropertyServerException from the underlying service
     * @throws LineageWarehouseException from the underlying service
     */
    @PostMapping( value = "entities/search")
    public List<LineageVertex> search(@RequestBody LineageSearchRequest searchRequest)
            throws InvalidParameterException, PropertyServerException, LineageWarehouseException
    {
        String user = SecurityContextHolder.getContext().getAuthentication().getName();
        return openLineageService.search(user, searchRequest);
    }


    /**
     * Returns a subraph representing the hierarchy of a certain node, based on the request
     *
     * @param elementHierarchyRequest contains the guid of the queried node and the hierarchyType of the display name of the nodes
     *
     * @return a subgraph containing all relevant paths,
     */
    @PostMapping(value = "elements/hierarchy")
    public Graph elementHierarchy(@RequestBody ElementHierarchyRequest elementHierarchyRequest) {
        String user = SecurityContextHolder.getContext().getAuthentication().getName();
        return openLineageService.getElementHierarchy(user, elementHierarchyRequest);
    }

    /**
     * This method is registering a custom converter for View and Scope enums in order to be able to use in url the text of the enum and not the actual name
     * @param webdataBinder DataBinder for data binding from web request parameters to JavaBean objects
     */
    @InitBinder
    public void initBinder(final WebDataBinder webdataBinder) {
        webdataBinder.registerCustomEditor(Scope.class, new ScopeEnumConverter());
    }

}
