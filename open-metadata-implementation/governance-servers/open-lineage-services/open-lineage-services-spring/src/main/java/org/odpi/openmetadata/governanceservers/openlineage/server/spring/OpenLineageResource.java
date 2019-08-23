/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.openlineage.server.spring;


import org.odpi.openmetadata.governanceservers.openlineage.responses.VoidResponse;
import org.odpi.openmetadata.governanceservers.openlineage.server.OpenLineageRestServices;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * * The OpenLineageResource provides the server-side interface of the Open Lineage Services governance server.
 */
@RestController
@RequestMapping("/open-metadata/open-lineage/users/{userId}/servers/{serverName}/")
public class OpenLineageResource {

    private final OpenLineageRestServices restAPI = new OpenLineageRestServices();

    /**
     * Returns the graph that the user will initially see when querying lineage. In the future, this method will be
     * extended to condense large paths to prevent cluttering of the users screen. The user will be able to extended
     * the condensed path by querying a different method.
     *
     * @param userId       calling user.
     * @param serverName   name of the server instance to connect to.
     * @param scope        The scope queried by the user: hostview, tableview, columnview.
     * @param lineageQuery ultimate-source, ultimate-destination, glossary.
     * @param graph        main, buffer, mock, history.
     * @param guid         The guid of the node of which the lineage is queried of.
     * @return A subgraph containing all relevant paths, in graphSON format.
     */
    @GetMapping(path = "/query-lineage/{scope}/{lineageQuery}/{graph}/{guid}")
    public String queryLineage(
            @PathVariable("userId") String userId,
            @PathVariable("serverName") String serverName,
            @PathVariable("scope") String scope,
            @PathVariable("lineageQuery") String lineageQuery,
            @PathVariable("graph") String graph,
            @PathVariable("guid") String guid) {
        return restAPI.queryLineage(serverName, userId, scope, lineageQuery, graph, guid);
    }

    /**
     * Generate the MOCK graph, which can be used for performance testing, or demoing lineage with large amounts of
     * data.
     *
     * @param userId     calling user.
     * @param serverName name of the server instance to connect to.
     * @return Voidresponse.
     */
    @GetMapping(path = "/generate-mock-graph")
    public VoidResponse generateGraph(@PathVariable("userId") String userId,
                                      @PathVariable("serverName") String serverName) {
        return restAPI.generateGraph(serverName, userId);
    }


    /**
     * Write an entire graph to disc in the Egeria root folder, in the .GraphMl format.
     *
     * @param userId     calling user.
     * @param serverName name of the server instance to connect to.
     * @param graph      MAIN, BUFFER, MOCK, HISTORY.
     * @return Voidresponse
     */
    @GetMapping(path = "/dump/{graph}")
    public VoidResponse dumpGraph(@PathVariable("userId") String userId,
                                  @PathVariable("serverName") String serverName,
                                  @PathVariable("graph") String graph) {
        return restAPI.dumpGraph(serverName, userId, graph);
    }

    /**
     * Return an entire graph, in GraphSON format.
     *
     * @param userId     calling user.
     * @param serverName name of the server instance to connect to.
     * @param graph      MAIN, BUFFER, MOCK, HISTORY.
     * @return The queried graph, in graphSON format.
     */
    @GetMapping(path = "/export/{graph}")
    public String exportGraph(@PathVariable("userId") String userId,
                              @PathVariable("serverName") String serverName,
                              @PathVariable("graph") String graph) {
        return restAPI.exportGraph(serverName, userId, graph);
    }
}
