/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.openlineage.server.spring;


import org.odpi.openmetadata.governanceservers.openlineage.responses.OpenLineageAPIResponse;
import org.odpi.openmetadata.governanceservers.openlineage.server.OpenLineageRestServices;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/open-metadata/admin-services/users/{userId}/servers/{serverName}/open-lineage")
public class OpenLineageOMASResource {

    private final OpenLineageRestServices restAPI = new OpenLineageRestServices();

    @GetMapping(path = "/dump/{graph}")
    public OpenLineageAPIResponse dumpGraph(@PathVariable("userId") String userId,
                                              @PathVariable("serverName") String serverName,
                                              @PathVariable("graph") String graph) {
        return restAPI.dumpGraph(serverName, userId, graph);
    }
    
    @GetMapping(path = "/export/{graph}")
    public String exportGraph(@PathVariable("userId") String userId,
                                              @PathVariable("serverName") String serverName,
                                              @PathVariable("graph") String graph) {
        return restAPI.exportGraph(serverName, userId, graph);
    }

    @GetMapping(path = "/generate-mock-graph")
    public OpenLineageAPIResponse generateGraph(@PathVariable("userId") String userId,
                                                @PathVariable("serverName") String serverName) {
        return restAPI.generateGraph(serverName, userId);
    }

    @GetMapping(path = "/initial-graph/{lineageType}/{guid}")
    public String initialGraph(@PathVariable("userId") String userId,
                                               @PathVariable("serverName") String serverName,
                                               @PathVariable("lineageType") String lineageType,
                                               @PathVariable("guid") String guid) {
        return restAPI.initialGraph(serverName, userId, lineageType, guid);
    }

}
