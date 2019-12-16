/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.tex.spring;


import org.odpi.openmetadata.userinterface.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.userinterface.security.springboot.securitycontrollers.SecureController;
import org.odpi.openmetadata.viewservices.tex.responses.TypeExplorerOMVSAPIResponse;
import org.odpi.openmetadata.viewservices.tex.responses.TypeExplorerResponse;
import org.odpi.openmetadata.viewservices.typeexplorer.services.TypeExplorerRESTServices;
import org.springframework.context.annotation.DependsOn;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;


/**
 * The TypeExplorerRESTServicesInstance provides the server-side implementation
 * of the TypeExplorer UI-component
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/view-services/type-explorer/")

@DependsOn("securityConfig")
public class TypeExplorerRESTResource extends SecureController
{

    private TypeExplorerRESTServices restAPI = new TypeExplorerRESTServices();
    private static String serviceName = ViewServiceDescription.TYPE_EXPLORER.getViewServiceName();


    /**
     * Default constructor
     *
     */
    public TypeExplorerRESTResource() {
    }


    /*
     * This method retrieves all the types from the server in a TypeExplorer object.
     * In the RequestBody:
     *   serverName is the name of the repository server to be interrogated.
     *   serverURLRoot is the root of the URL to use to connect to the server.
     *   enterpriseOption is a string "true" or "false" indicating whether to include results from the cohorts to which the server belongs
     */

    @PostMapping(path = "types")
    public TypeExplorerOMVSAPIResponse typeExplorer(@PathVariable String serverName, @RequestBody Map<String,String> body, HttpServletRequest request)
    {
        String userId = getUser(request);
        TypeExplorerOMVSAPIResponse texResp;
        if (userId == null) {
            //TODO sort out how to do the error processing properly. Git issue #2015 raised
            texResp = new TypeExplorerResponse(400,"The user identifier (user id) passed on the typeExplorer operation is null");
        } else {
            texResp = restAPI.typeExplorer(serverName, userId, body);
        }

        return texResp;
    }

    @GetMapping( path = "metadata-server-names")
    public TypeExplorerOMVSAPIResponse getMetadataServerNames(@PathVariable String serverName,  HttpServletRequest request)
    {
        String userId = getUser(request);
        TypeExplorerOMVSAPIResponse texResp;
        if (userId == null) {
            //TODO sort out how to do the error processing properly. Git issue #2015 raised
            texResp = new TypeExplorerOMVSAPIResponse(400,"The user identifier (user id) passed on the typeExplorer operation is null");
        } else {
            texResp = restAPI.getMetadataServerNames(serverName, userId);
        }

        return texResp;
    }
}
