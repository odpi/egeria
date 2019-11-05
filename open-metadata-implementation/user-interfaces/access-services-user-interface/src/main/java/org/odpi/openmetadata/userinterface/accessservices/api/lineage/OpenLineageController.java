/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.accessservices.api.lineage;


import org.odpi.openmetadata.governanceservers.openlineage.converters.ScopeEnumConverter;
import org.odpi.openmetadata.governanceservers.openlineage.converters.ViewEnumConverter;
import org.odpi.openmetadata.governanceservers.openlineage.model.GraphName;
import org.odpi.openmetadata.governanceservers.openlineage.model.Scope;
import org.odpi.openmetadata.governanceservers.openlineage.model.View;
import org.odpi.openmetadata.userinterface.accessservices.service.OpenLineageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


import java.util.Map;

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
     * @param graphName name of the graph source to use
     * @return map of nodes and edges describing the graph
     */
    @RequestMapping(method = RequestMethod.GET, value = "/export")
    public Map<String, Object> exportGraph(@RequestParam GraphName graphName){
        Map<String, Object> exportedGraph;
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        exportedGraph = openLineageService.exportGraph(userId);
        return exportedGraph;
    }

    /**
     *
     * @param guid unique identifier of the asset
     * @param view level of granularity, eg down to column or table level
     * @return map of nodes and edges describing the ultimate sources of the asset
     */
    @RequestMapping(method = RequestMethod.GET, value = "/entities/{guid}/ultimate-source")
    public Map<String, Object> ultimateSourceGraph(@PathVariable("guid") String guid, @RequestParam View view){
        Map<String, Object> exportedGraph;
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        exportedGraph = openLineageService.getUltimateSource(userId, view, guid);

        return exportedGraph;
    }

    /**
     *
     * @param guid unique identifier of the asset
     * @param view level of granularity, eg down to column or table level
     * @return map of nodes and edges describing the end to end flow
     */
    @RequestMapping(method = RequestMethod.GET, value = "/entities/{guid}/end2end")
    @ResponseBody
    public Map<String, Object> endToEndLineage(@PathVariable("guid") String guid, @RequestParam View view){
        Map<String, Object> exportedGraph;
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        exportedGraph = openLineageService.getEndToEndLineage(userId, view, guid);

        return exportedGraph;
    }

    /**
     *
     * @param guid unique identifier of the asset
     * @param view level of granularity, eg down to column or table level
     * @return map of nodes and edges describing the ultimate destination of the asset
     */
    @RequestMapping(method = RequestMethod.GET, value = "/entities/{guid}/ultimate-destination")
    public Map<String, Object> ultimateDestination(@PathVariable("guid") String guid, @RequestParam View view){
        Map<String, Object> exportedGraph;
            String userId = SecurityContextHolder.getContext().getAuthentication().getName();
            exportedGraph = openLineageService.getUltimateDestination(userId, view, guid);

        return exportedGraph;
    }


    /**
     *
     * @param guid unique identifier of the glossary term
     * @param view level of granularity, eg down to column or table level
     * @return map of nodes and edges describing the assets linked to the glossary term
     */
    @RequestMapping(method = RequestMethod.GET, value = "/entities/{guid}/glossary-lineage")
    public Map<String, Object> glossaryLineage(@PathVariable("guid") String guid, @RequestParam View view){
        Map<String, Object> exportedGraph;
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        exportedGraph = openLineageService.getGlossaryLineage(userId, view, guid);

        return exportedGraph;
    }


    /**
     *
     * @param guid unique identifier of the asset
     * @param view level of granularity, eg down to column or table level
     * @return map of nodes and edges describing the ultimate source and destination of the asset
     */
    @RequestMapping(method = RequestMethod.GET, value = "/entities/{guid}/source-and-destination")
    public Map<String, Object> sourceAndDestinationLineage(@PathVariable("guid") String guid, @RequestParam View view){
        Map<String, Object> exportedGraph;
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        exportedGraph = openLineageService.getSourceAndDestination(userId, view, guid);
        return exportedGraph;
    }


    /**
     * This method is registering a custom converter for View and Scope enums in order to be able to use in url the text of the enum and not the actual name
     * @param webdataBinder DataBinder for data binding from web request parameters to JavaBean objects
     */
    @InitBinder
    public void initBinder(final WebDataBinder webdataBinder) {
        webdataBinder.registerCustomEditor(View.class, new ViewEnumConverter());
        webdataBinder.registerCustomEditor(Scope.class, new ScopeEnumConverter());
    }

}
