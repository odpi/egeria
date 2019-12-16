/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.openlineage.server;


import org.odpi.openmetadata.governanceservers.openlineage.converters.ScopeEnumConverter;
import org.odpi.openmetadata.governanceservers.openlineage.converters.ViewEnumConverter;
import org.odpi.openmetadata.governanceservers.openlineage.model.Scope;
import org.odpi.openmetadata.governanceservers.openlineage.model.View;
import org.odpi.openmetadata.userinterface.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.userinterface.security.springboot.securitycontrollers.SecureController;
import org.odpi.openmetadata.viewservices.openlineage.services.OpenLineageViewRESTServices;
import org.springframework.context.annotation.DependsOn;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * This controller serves all requests for retrieving lineage details, both vertical and horizontal
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/view-services/open-lineage/")
@DependsOn("securityConfig")
public class OpenLineageViewRESTResource extends SecureController {

    private OpenLineageViewRESTServices restAPI = new OpenLineageViewRESTServices();
    private static String serviceName = ViewServiceDescription.OPEN_LINEAGE.getViewServiceName();
    

    /**
     * @param serverName local UI server name
     * @param guid unique identifier of the asset
     * @param view level of granularity, eg down to column or table level
     * @return map of nodes and edges describing the ultimate sources of the asset
     */
    @GetMapping( path = "/entities/{guid}/ultimate-source")
    public Map<String, List> ultimateSourceGraph(@PathVariable("serverName") String serverName,
                                                 @PathVariable("guid") String guid,
                                                 @RequestParam View view,
                                                 HttpServletRequest request){
        Map<String, List> exportedGraph =null;
        String userId = getUser(request);
        if (userId == null) {
            //TODO sort out how to do the error processing properly. Git issue #2015 raised
            throw new org.odpi.openmetadata.userinterface.security.springboot.exceptions.UserNotAuthorizedException("User not authorised");
        } else {
            exportedGraph = restAPI.getUltimateSource(serverName, userId, view, guid);
        }
        return exportedGraph;
        
    }

    /**
     * Get the end to end lineage of an identified asset.
     * @param serverName local UI server name
     * @param guid unique identifier of the asset
     * @param view level of granularity, eg down to column or table level
     * @return map of nodes and edges describing the end to end flow
     */
    @GetMapping( path = "/entities/{guid}/end2end")
    @ResponseBody
    public Map<String, List> endToEndLineage(@PathVariable("serverName") String serverName,
                                               @PathVariable("guid") String guid, @RequestParam View view, HttpServletRequest request){
        Map<String, List> exportedGraph= null;
        String userId = getUser(request);
        if (userId == null) {
            //TODO
        }else {
            exportedGraph = restAPI.getEndToEndLineage(serverName, userId, view, guid);
        }
        return exportedGraph;
    }

    /**
     * Get the ultimate lineage destination of an identified asset.
     * @param serverName local UI server name
     * @param guid unique identifier of the asset
     * @param view level of granularity, eg down to column or table level
     * @return map of nodes and edges describing the ultimate destination of the asset
     */
    @GetMapping( path = "/entities/{guid}/ultimate-destination")
    public Map<String, List> ultimateDestination(@PathVariable("serverName") String serverName,
                                                   @PathVariable("guid") String guid, @RequestParam View view, HttpServletRequest request){
        Map<String, List> exportedGraph= null;
        String userId = getUser(request);
        if (userId == null) {
            //TODO
        }else {
            exportedGraph = restAPI.getUltimateDestination(serverName, userId, view, guid);
        }
        return exportedGraph;
    }


    /**
     * Get the lineage of an identified glossary term.
     * @param serverName local UI server name
     * @param guid unique identifier of the glossary term
     * @param view level of granularity, eg down to column or table level
     * @return map of nodes and edges describing the assets linked to the glossary term
     */
    @GetMapping( path = "/entities/{guid}/glossary-lineage")
    public Map<String, List> glossaryLineage(@PathVariable("serverName") String serverName,
                                               @PathVariable("guid") String guid, @RequestParam View view, HttpServletRequest request){
        Map<String, List> exportedGraph= null;
        String userId = getUser(request);
        if (userId == null) {
            //TODO
        }else {
            exportedGraph = restAPI.getGlossaryLineage(serverName, userId, view, guid);
        }
        return exportedGraph;
    }

    /**
     * Get the ultimate source and ultimate lineage destination of an identified asset.
     * @param serverName local UI server name
     * @param guid unique identifier of the asset
     * @param view level of granularity, eg down to column or table level
     * @return map of nodes and edges describing the ultimate source and destination of the asset
     */
    @GetMapping( path = "/entities/{guid}/source-and-destination")
    public Map<String, List> sourceAndDestinationLineage(@PathVariable("serverName") String serverName,
                                                           @PathVariable("guid") String guid, @RequestParam View view, HttpServletRequest request){
        Map<String, List> exportedGraph= null;
        String userId = getUser(request);
        if (userId == null) {
            //TODO
        }else {
            exportedGraph = restAPI.getSourceAndDestination(serverName, userId, view, guid);
        }
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
