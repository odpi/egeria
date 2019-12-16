/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.openlineage.server.spring;


import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.governanceservers.openlineage.converters.GraphNameEnumConverter;
import org.odpi.openmetadata.governanceservers.openlineage.converters.ScopeEnumConverter;
import org.odpi.openmetadata.governanceservers.openlineage.converters.ViewEnumConverter;
import org.odpi.openmetadata.governanceservers.openlineage.model.GraphName;
import org.odpi.openmetadata.governanceservers.openlineage.model.Scope;
import org.odpi.openmetadata.governanceservers.openlineage.model.View;
import org.odpi.openmetadata.governanceservers.openlineage.responses.LineageResponse;
import org.odpi.openmetadata.governanceservers.openlineage.server.OpenLineageRestServices;
import org.springframework.http.MediaType;
import org.springframework.web.bind.WebDataBinder;
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
     * @param view       The view queried by the user: hostview, tableview, columnview.
     * @param scope      ultimate-source, ultimate-destination, glossary.
     * @param graph      main, buffer, mock, history.
     * @param guid       The guid of the node of which the lineage is queried of.
     * @return A subgraph containing all relevant paths, in graphSON format.
     */
    @GetMapping(path = "/lineage/sources/{graph}/scopes/{scope}/views/{view}/entities/{guid}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public LineageResponse lineage(
            @PathVariable("serverName") String serverName,
            @PathVariable("userId") String userId,
            @PathVariable("graph") GraphName graph,
            @PathVariable("scope") Scope scope,
            @PathVariable("view") View view,
            @PathVariable("guid") String guid,
            @RequestParam (value = "displayname-contains", required = false, defaultValue = "") String displayNameMustContain,
            @RequestParam (value = "include-processes", required=false, defaultValue = "true") boolean includeProcesses){
        return restAPI.lineage(serverName, userId, graph, scope, view, guid, displayNameMustContain, includeProcesses);
    }


    /**
     * Write an entire graph to disc in the Egeria root folder, in the .GraphMl format.
     *
     * @param userId     calling user.
     * @param serverName name of the server instance to connect to.
     * @param graph      MAIN, BUFFER, MOCK, HISTORY.
     * @return Voidresponse
     */
    @GetMapping(path = "/dump/sources/{graph}")
    public VoidResponse dumpGraph(@PathVariable("userId") String userId,
                                  @PathVariable("serverName") String serverName,
                                  @PathVariable("graph") GraphName graph) {
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
    @GetMapping(path = "/export/sources/{graph}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public String exportGraph(@PathVariable("userId") String userId,
                              @PathVariable("serverName") String serverName,
                              @PathVariable("graph") GraphName graph) {
        return restAPI.exportGraph(serverName, userId, graph);
    }

    @InitBinder
    public void initBinder(final WebDataBinder webdataBinder) {
        webdataBinder.registerCustomEditor(View.class, new ViewEnumConverter());
        webdataBinder.registerCustomEditor(GraphName.class, new GraphNameEnumConverter());
        webdataBinder.registerCustomEditor(Scope.class, new ScopeEnumConverter());
    }

}
