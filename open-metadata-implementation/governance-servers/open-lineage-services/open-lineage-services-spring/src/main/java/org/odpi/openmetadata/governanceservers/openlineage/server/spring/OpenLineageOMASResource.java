/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.openlineage.server.spring;


import org.odpi.openmetadata.governanceservers.openlineage.responses.OpenLineageOMASAPIResponse;
import org.odpi.openmetadata.governanceservers.openlineage.server.OpenLineageRestServices;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/open-metadata/admin-services/users/{userId}/servers/{serverName}/open-lineage")
public class OpenLineageOMASResource {

    private final OpenLineageRestServices restAPI = new OpenLineageRestServices();

    @GetMapping(path = "/export")
    public OpenLineageOMASAPIResponse exportGraph(@PathVariable("userId") String userId,
                                                   @PathVariable("serverName") String serverName) {
        return restAPI.exportGraph(serverName, userId);
    }
}
