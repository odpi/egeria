/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project */
/* Copyright Contributors to the ODPi Egeria category. */
package org.odpi.openmetadata.viewservices.tex.server.spring;


import org.odpi.openmetadata.viewservices.tex.api.rest.TexResourceEndpointListResponse;
import org.odpi.openmetadata.viewservices.tex.api.rest.TexTypesRequestBody;
import org.odpi.openmetadata.viewservices.tex.api.rest.TypeExplorerResponse;
import org.odpi.openmetadata.viewservices.tex.server.TexViewRESTServices;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;



/**
 * The TexViewRESTResource provides the Spring API endpoints of the Type Explorer Open Metadata View Service (OMVS).
 * This interface provides a service for enterprise architects.
 */

@RestController
@RequestMapping("/servers/{viewServerName}/open-metadata/view-services/tex/users/{userId}")

@Tag(name="View Server: Tex OMVS", description="Explore type information in a repository or cohort for visualization of graphs of related types.",
     externalDocs=@ExternalDocumentation(description="Further Information",
                                         url="https://egeria-project.org/services/omvs/tex/overview/"))

public class TexViewRESTResource {

    private final TexViewRESTServices restAPI = new TexViewRESTServices();


    /**
     * Default constructor
     */
    public TexViewRESTResource() {
    }

    /**
     * Get the configured resource endpoints
     *
     * @param viewServerName   name of the server running the view-service.
     * @param userId       user account under which to conduct operation.
     * @return response object containing the list of resource endpoints or exception information
     */

    @GetMapping("/resource-endpoints")
    public TexResourceEndpointListResponse getResourceEndpoints(@PathVariable String        viewServerName,
                                                                @PathVariable String        userId) {
        return restAPI.getResourceEndpointList(viewServerName, userId);

    }

    /**
     * Load type information
     * <p>
     * Load type information from the repository server. This is used to populate filters.
     *
     * @param viewServerName   name of the server running the view-service.
     * @param userId       user account under which to conduct operation.
     * @param body         request body containing parameters to formulate repository request
     * @return response object containing the repository's type information or exception information
     */
    @PostMapping("/types")
    public TypeExplorerResponse getTypeExplorer(@PathVariable String              viewServerName,
                                                @PathVariable String              userId,
                                                @RequestBody  TexTypesRequestBody body) {
        return restAPI.getTypeExplorer(viewServerName, userId, body);

    }

}
