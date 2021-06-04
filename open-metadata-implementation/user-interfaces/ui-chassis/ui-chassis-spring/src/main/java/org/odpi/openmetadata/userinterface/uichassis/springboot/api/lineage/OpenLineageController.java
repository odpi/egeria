/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.uichassis.springboot.api.lineage;


import org.odpi.openmetadata.governanceservers.openlineage.converters.ScopeEnumConverter;
import org.odpi.openmetadata.governanceservers.openlineage.model.LineageVertex;
import org.odpi.openmetadata.governanceservers.openlineage.model.Scope;
import org.odpi.openmetadata.userinterface.uichassis.springboot.beans.Graph;
import org.odpi.openmetadata.userinterface.uichassis.springboot.service.OpenLineageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

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
     */
    @GetMapping( value = "/entities/{guid}/ultimate-source")
    public Graph ultimateSourceGraph(@PathVariable("guid") String guid , @RequestParam boolean includeProcesses){
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
     */
    @GetMapping( value = "/entities/{guid}/end2end")
    @ResponseBody
    public Graph endToEndLineage(@PathVariable("guid") String guid, @RequestParam boolean includeProcesses){
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
     */
    @GetMapping( value = "/entities/{guid}/ultimate-destination")
    public Graph ultimateDestination(@PathVariable("guid") String guid, @RequestParam boolean includeProcesses){
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
     */
    @GetMapping( value = "/entities/{guid}/vertical-lineage")
    public Graph verticalLineage(@PathVariable("guid") String guid, @RequestParam boolean includeProcesses){
        Graph exportedGraph;
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        exportedGraph = openLineageService.getVerticalLineage(userId, guid, includeProcesses);
        return exportedGraph;
    }

    /**
     *
     * @param guid unique identifier of the asset
     * @param includeProcesses if true Process nodes will be included
     * @return graph of nodes and edges describing the ultimate source and destination of the asset
     */
    @GetMapping( value = "/entities/{guid}/source-and-destination")
    public Graph sourceAndDestinationLineage(@PathVariable("guid") String guid, @RequestParam boolean includeProcesses){
        Graph exportedGraph;
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        exportedGraph = openLineageService.getSourceAndDestination(userId, guid, includeProcesses);
        return exportedGraph;
    }

    /**
     * @param guid of the Entity to be retrieved
     * @return the entity details
     */
    @GetMapping( value = "entities/{guid}/details")
    public LineageVertex getEntityDetails(@PathVariable("guid") String guid) {
        String user = SecurityContextHolder.getContext().getAuthentication().getName();
        return openLineageService.getEntityDetails(user, guid);
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
