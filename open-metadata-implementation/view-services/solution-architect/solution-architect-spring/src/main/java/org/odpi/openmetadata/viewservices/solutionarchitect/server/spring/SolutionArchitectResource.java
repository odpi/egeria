/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.solutionarchitect.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.commonservices.ffdc.rest.FilterRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.ResultsRequestBody;
import org.odpi.openmetadata.frameworkservices.gaf.rest.*;
import org.odpi.openmetadata.viewservices.solutionarchitect.server.SolutionArchitectRESTServices;
import org.springframework.web.bind.annotation.*;


/**
 * The SolutionArchitectResource provides part of the server-side implementation of the Solution Architect OMVS.
= */
@RestController
@RequestMapping("/servers/{serverName}/api/open-metadata/solution-architect")

@Tag(name="API: Solution Architect OMVS", description="The Solution Architect OMVS is a REST API designed to support user interfaces (UIs) relating to the definition and display of solution blueprints and their supporting solution components along with the relevant information supply chains.",
        externalDocs=@ExternalDocumentation(description="Further Information",
                url="https://egeria-project.org/services/omvs/solution-architect/overview/"))

public class SolutionArchitectResource
{
    private final SolutionArchitectRESTServices restAPI = new SolutionArchitectRESTServices();

    /**
     * Default constructor
     */
    public SolutionArchitectResource()
    {
    }

    /**
     * Retrieve the list of information supply chain metadata elements that contain the search string.
     *
     * @param serverName name of the service to route the request to
     * @param startsWith does the value start with the supplied string?
     * @param endsWith does the value end with the supplied string?
     * @param ignoreCase should the search ignore case?
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/information-supply-chains/by-search-string")
    @Operation(summary="findInformationSupplyChains",
            description="Retrieve the list of information supply chain metadata elements that contain the search string.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/information-supply-chain"))

    public InformationSupplyChainsResponse findInformationSupplyChains(@PathVariable String                  serverName,
                                                                       @RequestParam (required = false, defaultValue = "0")
                                                                       int                     startFrom,
                                                                       @RequestParam (required = false, defaultValue = "0")
                                                                           int                     pageSize,
                                                                       @RequestParam (required = false, defaultValue = "false")
                                                                       boolean                 startsWith,
                                                                       @RequestParam (required = false, defaultValue = "false")
                                                                       boolean                 endsWith,
                                                                       @RequestParam (required = false, defaultValue = "false")
                                                                       boolean                 ignoreCase,
                                                                       @RequestBody FilterRequestBody requestBody)
    {
        return restAPI.findInformationSupplyChains(serverName, startsWith, endsWith, ignoreCase, startFrom, pageSize, requestBody);
    }


    /**
     * Retrieve the list of solution blueprint metadata elements that contain the search string.  The returned blueprints include a list of the components that are associated with it.
     *
     * @param serverName name of the service to route the request to
     * @param startsWith does the value start with the supplied string?
     * @param endsWith does the value end with the supplied string?
     * @param ignoreCase should the search ignore case?
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/solution-blueprints/by-search-string")
    @Operation(summary="findSolutionBlueprints",
            description="Retrieve the list of solution blueprint metadata elements that contain the search string.  The returned blueprints include a list of the components that are associated with it.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/solution-blueprint"))

    public SolutionBlueprintsResponse findSolutionBlueprints(@PathVariable String                  serverName,
                                                             @RequestParam (required = false, defaultValue = "0")
                                                                       int                     startFrom,
                                                             @RequestParam (required = false, defaultValue = "0")
                                                                       int                     pageSize,
                                                             @RequestParam (required = false, defaultValue = "false")
                                                                       boolean                 startsWith,
                                                             @RequestParam (required = false, defaultValue = "false")
                                                                       boolean                 endsWith,
                                                             @RequestParam (required = false, defaultValue = "false")
                                                                       boolean                 ignoreCase,
                                                             @RequestBody FilterRequestBody requestBody)
    {
        return restAPI.findSolutionBlueprints(serverName, startsWith, endsWith, ignoreCase, startFrom, pageSize, requestBody);
    }


    /**
     * Retrieve the list of actor roles metadata elements that contain the search string and show which solution components (if any) are attached to it.
     *
     * @param serverName name of the service to route the request to
     * @param startsWith does the value start with the supplied string?
     * @param endsWith does the value end with the supplied string?
     * @param ignoreCase should the search ignore case?
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/solution-roles/by-search-string")
    @Operation(summary="findSolutionRoles",
            description="Retrieve the list of actor roles metadata elements that contain the search string and show which solution components (if any) are attached to it.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/actor"))

    public SolutionRolesResponse findSolutionRoles(@PathVariable String                  serverName,
                                                   @RequestParam (required = false, defaultValue = "0")
                                                             int                     startFrom,
                                                   @RequestParam (required = false, defaultValue = "0")
                                                             int                     pageSize,
                                                   @RequestParam (required = false, defaultValue = "false")
                                                             boolean                 startsWith,
                                                   @RequestParam (required = false, defaultValue = "false")
                                                             boolean                 endsWith,
                                                   @RequestParam (required = false, defaultValue = "false")
                                                             boolean                 ignoreCase,
                                                   @RequestBody FilterRequestBody requestBody)
    {
        return restAPI.findSolutionRoles(serverName, startsWith, endsWith, ignoreCase, startFrom, pageSize, requestBody);
    }


    /**
     * Retrieve the list of solution component metadata elements that contain the search string.  The solutions components returned include information about the consumers, actors and other solution components that are associated with them.
     *
     * @param serverName name of the service to route the request to
     * @param startsWith does the value start with the supplied string?
     * @param endsWith does the value end with the supplied string?
     * @param ignoreCase should the search ignore case?
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/solution-components/by-search-string")
    @Operation(summary="findSolutionComponents",
            description="Retrieve the list of solution metadata elements that contain the search string.  The solutions components returned include information about the consumers, actors and other solution components that are associated with them.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/solution-component"))

    public SolutionComponentsResponse findSolutionComponents(@PathVariable String                  serverName,
                                                             @RequestParam (required = false, defaultValue = "0")
                                                             int                     startFrom,
                                                             @RequestParam (required = false, defaultValue = "0")
                                                                 int                     pageSize,
                                                             @RequestParam (required = false, defaultValue = "false")
                                                             boolean                 startsWith,
                                                             @RequestParam (required = false, defaultValue = "false")
                                                             boolean                 endsWith,
                                                             @RequestParam (required = false, defaultValue = "false")
                                                             boolean                 ignoreCase,
                                                             @RequestBody FilterRequestBody requestBody)
    {
        return restAPI.findSolutionComponents(serverName, startsWith, endsWith, ignoreCase, startFrom, pageSize, requestBody);
    }


    /**
     * Retrieve the list of  metadata elements that are associated with the solution component via the ImplementedBy relationship.
     *
     * @param serverName name of the service to route the request to
     * @param solutionComponentGUID unique identifier of the solution component to query
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/solution-components/{solutionComponentGUID}/implementations")
    @Operation(summary="getSolutionComponentImplementations",
            description="Retrieve the list of  metadata elements that are associated with the solution component via the ImplementedBy relationship.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/solution-component"))

    public RelatedMetadataElementsResponse getSolutionComponentImplementations(@PathVariable String                  serverName,
                                                                               @PathVariable String                  solutionComponentGUID,
                                                                               @RequestParam (required = false, defaultValue = "0")
                                                                                   int                     startFrom,
                                                                               @RequestParam (required = false, defaultValue = "0")
                                                                                   int                     pageSize,
                                                                               @RequestBody(required = false)
                                                                                   ResultsRequestBody requestBody)
    {
        return restAPI.getSolutionComponentImplementations(serverName, solutionComponentGUID, startFrom, pageSize, requestBody);
    }
}
