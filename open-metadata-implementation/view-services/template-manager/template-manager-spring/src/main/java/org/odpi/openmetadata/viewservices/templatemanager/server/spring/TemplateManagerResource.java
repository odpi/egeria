/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.templatemanager.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.frameworkservices.gaf.rest.OpenMetadataElementsResponse;
import org.odpi.openmetadata.viewservices.templatemanager.rest.TemplateClassificationRequestBody;
import org.odpi.openmetadata.viewservices.templatemanager.server.TemplateManagerRESTServices;
import org.springframework.web.bind.annotation.*;


/**
 * The TemplateManagerResource provides part of the server-side implementation of the Template Manager OMVS.
 */
@RestController
@RequestMapping("/servers/{serverName}/api/open-metadata/template-manager")

@Tag(name="API: Template Manager OMVS", description="The Template Manager OMVS provides APIs for retrieving, creating and maintaining templates.",
        externalDocs=@ExternalDocumentation(description="Further Information",
                url="https://egeria-project.org/services/omvs/template-manager/overview/"))

public class TemplateManagerResource
{
    private final TemplateManagerRESTServices restAPI = new TemplateManagerRESTServices();

    /**
     * Default constructor
     */
    public TemplateManagerResource()
    {
    }


    /**
     * Retrieve the elements with the template classification.  The request can include the .
     *
     * @param serverName     name of server instance to route request to
     * @param elementTypeName optional type name for the template
     * @param viewServiceURLMarker optional view service URL marker (overrides accessServiceURLMarker)
     * @param accessServiceURLMarker optional access service URL marker used to identify which back end service to call
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody searchString  to retrieve
     *
     * @return list of matching metadata elements (or null if no elements match the name) or
     *  InvalidParameterException the qualified name is null
     *  UserNotAuthorizedException the governance action service is not able to access the element
     *  PropertyServerException there is a problem accessing the metadata store
     */
    @PostMapping(path = "/templates/by-search-string")

    @Operation(summary="findTemplates",
            description="Retrieve the metadata elements that contain the requested string in its template classification.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/services/gaf-metadata-management/"))

    public OpenMetadataElementsResponse findTemplates(@PathVariable String                  serverName,
                                                      @RequestParam (required = false)
                                                                    String                  elementTypeName,
                                                      @RequestParam (required = false)
                                                                    String                  viewServiceURLMarker,
                                                      @RequestParam (required = false, defaultValue = "digital-architecture")
                                                                    String                  accessServiceURLMarker,
                                                      @RequestParam (required = false, defaultValue = "0")
                                                                    int                     startFrom,
                                                      @RequestParam (required = false, defaultValue = "0")
                                                                    int                     pageSize,
                                                      @RequestBody TemplateClassificationRequestBody requestBody)
    {
        return restAPI.findTemplates(serverName, elementTypeName, viewServiceURLMarker, accessServiceURLMarker, startFrom, pageSize, requestBody);
    }
}
