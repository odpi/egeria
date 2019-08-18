/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.accessservices.api;


import org.odpi.openmetadata.governanceservers.openlineage.model.Graphs;
import org.odpi.openmetadata.userinterface.accessservices.service.OpenLineageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


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
    public Map<String, Object> exportGraph(String userId, @RequestParam Graphs graph){
        Map<String, Object> exportedGraph = openLineageService.exportGraph(userId, graph);
        return exportedGraph;
    }


}
