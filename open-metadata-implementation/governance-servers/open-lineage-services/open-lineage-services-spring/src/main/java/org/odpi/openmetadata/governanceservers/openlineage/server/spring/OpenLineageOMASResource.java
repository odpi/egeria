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

    @GetMapping(path = "/export")
    public OpenLineageAPIResponse exportGraph(@PathVariable("userId") String userId,
                                              @PathVariable("serverName") String serverName) {
        return restAPI.exportGraph(serverName, userId);
    }

    @GetMapping(path = "/generate")
    public OpenLineageAPIResponse generateGraph(@PathVariable("userId") String userId,
                                                @PathVariable("serverName") String serverName) {
        return restAPI.generateGraph(serverName, userId);
    }

    @GetMapping(path = "/initial-graph/{lineageType}/{guid}")
    public OpenLineageAPIResponse ultimateSource(@PathVariable("userId") String userId,
                                                 @PathVariable("serverName") String serverName,
                                                 @PathVariable("lineageType") String lineageType,
                                                 @PathVariable("guid") String guid) {
        return restAPI.initialGraph(serverName, userId, lineageType, guid);
    }
}
