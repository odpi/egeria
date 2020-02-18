/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.openlineage.server.spring;


import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.governanceservers.openlineage.model.LineageQueryParameters;
import org.odpi.openmetadata.governanceservers.openlineage.responses.LineageResponse;
import org.odpi.openmetadata.governanceservers.openlineage.server.OpenLineageRestServices;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * * The OpenLineageResource provides the server-side interface of the Open Lineage Services governance server.
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/open-lineage/users/{userId}")
public class OpenLineageResource {

    private final OpenLineageRestServices restAPI = new OpenLineageRestServices();

    /**
     * Returns the graph that the user will initially see when querying lineage. In the future, this method will be
     * extended to condense large paths to prevent cluttering of the users screen. The user will be able to extended
     * the condensed path by querying a different method.
     *
     * @param userId     calling user.
     * @param serverName name of the server instance to connect to.
     * @param params
     * @return A subgraph containing all relevant paths, in graphSON format.
     */
    @PostMapping(path = "/lineage/entities/{guid}", produces = MediaType.APPLICATION_JSON_VALUE)
    public LineageResponse lineage(
            @PathVariable("serverName") String serverName,
            @PathVariable("userId") String userId,
            @PathVariable("guid") String guid,
            @RequestBody LineageQueryParameters params) {
        return restAPI.lineage(serverName, userId, params.getScope(), guid, params.getDisplayNameMustContain(), params.getIncludeProcesses());
    }

    /**
     * Write an entire graph to disc in the Egeria root folder, in the .GraphMl format.
     *
     * @param userId     calling user.
     * @param serverName name of the server instance to connect to.
     * @return Voidresponse
     */
    @GetMapping(path = "/dump")
    public VoidResponse dumpGraph(@PathVariable("userId") String userId,
                                  @PathVariable("serverName") String serverName) {
        return restAPI.dumpGraph(serverName, userId);
    }

    /**
     * Return an entire graph, in GraphSON format.
     *
     * @param userId     calling user.
     * @param serverName name of the server instance to connect to.
     * @return The queried graph, in graphSON format.
     */
    @GetMapping(path = "/export", produces = {MediaType.APPLICATION_JSON_VALUE})
    public String exportGraph(@PathVariable("userId") String userId,
                              @PathVariable("serverName") String serverName) {
        return restAPI.exportMainGraph(serverName, userId);
    }

}
