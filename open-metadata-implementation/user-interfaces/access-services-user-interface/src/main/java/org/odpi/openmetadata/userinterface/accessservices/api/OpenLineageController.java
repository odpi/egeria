/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.accessservices.api;


import org.odpi.openmetadata.accessservices.assetcatalog.exception.InvalidParameterException;
import org.odpi.openmetadata.governanceservers.openlineage.model.GraphName;
import org.odpi.openmetadata.governanceservers.openlineage.model.View;
import org.odpi.openmetadata.userinterface.accessservices.service.OpenLineageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/lineage")
public class OpenLineageController {

    @Autowired
    private OpenLineageService openLineageService;

    @RequestMapping(method = RequestMethod.GET)
    public String getMockGraph(String userId){
        return openLineageService.generateMockGraph(userId);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/export")
    public Map<String, Object> exportGraph(@RequestParam GraphName graphName){
        Map<String, Object> exportedGraph;
        try {
            String userId = SecurityContextHolder.getContext().getAuthentication().getName();
            exportedGraph = openLineageService.exportGraph(userId);
        } catch (IOException e) {
            handleException(e);
            return null;
        }
        return exportedGraph;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/entities/{guid}/ultimate-source")
    public Map<String, Object> ultimateSourceGraph(@PathVariable("guid") String guid, @RequestParam String view){
        Map<String, Object> exportedGraph;
        try {
            String userId = SecurityContextHolder.getContext().getAuthentication().getName();
            exportedGraph = openLineageService.getUltimateSource(userId, View.fromString(view), guid);
        } catch (IOException e) {
            handleException(e);
            return null;
        }
        return exportedGraph;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/entities/{guid}/end2end")
    public Map<String, Object> endToEndLineage(@PathVariable("guid") String guid, @RequestParam String view){
        Map<String, Object> exportedGraph;
        try {
            String userId = SecurityContextHolder.getContext().getAuthentication().getName();
            exportedGraph = openLineageService.getEndToEndLineage(userId, View.fromString(view), guid);
        } catch (IOException e) {
            handleException(e);
            return null;
        }
        return exportedGraph;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/entities/{guid}/ultimate-destination")
    public Map<String, Object> ultimateDestination(@PathVariable("guid") String guid, @RequestParam String view){
        Map<String, Object> exportedGraph;
        try {
            String userId = SecurityContextHolder.getContext().getAuthentication().getName();
            exportedGraph = openLineageService.getUltimateDestination(userId, View.fromString(view), guid);
        } catch (IOException e) {
            handleException(e);
            return null;
        }
        return exportedGraph;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/entities/{guid}/glossary-lineage")
    public Map<String, Object> glossaryLineage(@PathVariable("guid") String guid, @RequestParam String view){
        Map<String, Object> exportedGraph;
        try {
            String userId = SecurityContextHolder.getContext().getAuthentication().getName();
            exportedGraph = openLineageService.getGlossaryLineage(userId, View.fromString(view), guid);
        } catch (IOException e) {
            handleException(e);
            return null;
        }
        return exportedGraph;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/entities/{guid}/source-and-destination")
    public Map<String, Object> sourceAndDestinationLineage(@PathVariable("guid") String guid, @RequestParam String view){
        Map<String, Object> exportedGraph;
        try {
            String userId = SecurityContextHolder.getContext().getAuthentication().getName();
            exportedGraph = openLineageService.getSourceAndDestination(userId, View.fromString(view), guid);
        } catch (IOException e) {
            handleException(e);
            return null;
        }
        return exportedGraph;
    }

    //TODO use global exception handler
    private void handleException(Exception e){
        if(e instanceof InvalidParameterException){
            throw new IllegalArgumentException(e.getMessage());
        }
        throw new RuntimeException("Unknown exception! " + e.getMessage());
    }

}
