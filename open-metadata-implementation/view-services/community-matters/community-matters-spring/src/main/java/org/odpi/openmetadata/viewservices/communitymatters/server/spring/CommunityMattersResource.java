/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.communitymatters.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.viewservices.communitymatters.server.CommunityMattersRESTServices;
import org.springframework.web.bind.annotation.*;


/**
 * The CommunityMattersResource provides part of the server-side implementation of the Community Matters OMVS.
 = */
@RestController
@RequestMapping("/servers/{serverName}/api/open-metadata/community-matters")
@SecurityScheme(
        name = "BearerAuthorization",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer",
        in = SecuritySchemeIn.HEADER
)
@Tag(name="API: Community Matters", description="Communities bring people together from across the organization around a common theme, activity or skill. They are particularly useful for governance programs and cross-organization initiatives. The Community Matters OMVS provides APIs for managing the definition and members of the communities.",
        externalDocs=@ExternalDocumentation(description="Further Information",
                url="https://egeria-project.org/services/omvs/community-matters/overview/"))

public class CommunityMattersResource
{
    private final CommunityMattersRESTServices restAPI = new CommunityMattersRESTServices();

    /**
     * Default constructor
     */
    public CommunityMattersResource()
    {
    }


    /**
     * Create a community.
     *
     * @param serverName                 name of called server.
     * @param requestBody             properties for the community.
     *
     * @return unique identifier of the newly created element
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/communities")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="createCommunity",
            description="Create a community.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/community"))

    public GUIDResponse createCommunity(@PathVariable String                               serverName,
                                        @RequestBody (required = false)
                                        NewElementRequestBody requestBody)
    {
        return restAPI.createCommunity(serverName, requestBody);
    }


    /**
     * Create a new metadata element to represent a community using an existing metadata element as a template.
     * The template defines additional classifications and relationships that should be added to the new element.
     *
     * @param serverName             calling user
     * @param requestBody properties that override the template
     *
     * @return unique identifier of the new metadata element
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/communities/from-template")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="createCommunityFromTemplate",
            description="Create a new metadata element to represent a community using an existing metadata element as a template.  The template defines additional classifications and relationships that should be added to the new element.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/community"))

    public GUIDResponse createCommunityFromTemplate(@PathVariable
                                                    String              serverName,
                                                    @RequestBody (required = false)
                                                    TemplateRequestBody requestBody)
    {
        return restAPI.createCommunityFromTemplate(serverName, requestBody);
    }


    /**
     * Update the properties of a community.
     *
     * @param serverName         name of called server.
     * @param communityGUID unique identifier of the community (returned from create)
     * @param requestBody     properties for the new element.
     *
     * @return boolean or
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/communities/{communityGUID}/update")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="updateCommunity",
            description="Update the properties of a community.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/community"))

    public BooleanResponse updateCommunity(@PathVariable
                                        String                                  serverName,
                                        @PathVariable
                                        String                                  communityGUID,
                                        @RequestBody (required = false)
                                        UpdateElementRequestBody requestBody)
    {
        return restAPI.updateCommunity(serverName, communityGUID, requestBody);
    }


    /**
     * Delete a community.
     *
     * @param serverName         name of called server
     * @param communityGUID  unique identifier of the element to delete
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/communities/{communityGUID}/delete")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="deleteCommunity",
            description="Delete a community.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/community"))

    public VoidResponse deleteCommunity(@PathVariable
                                        String                    serverName,
                                        @PathVariable
                                        String                    communityGUID,
                                        @RequestBody (required = false)
                                        DeleteElementRequestBody requestBody)
    {
        return restAPI.deleteCommunity(serverName, communityGUID, requestBody);
    }


    /**
     * Returns the list of communities with a particular name.
     *
     * @param serverName name of the service to route the request to
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/communities/by-name")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getCommunitiesByName",
            description="Returns the list of communities with a particular name.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/community"))

    public OpenMetadataRootElementsResponse getCommunitiesByName(@PathVariable
                                                                 String            serverName,
                                                                 @RequestBody (required = false)
                                                                 FilterRequestBody requestBody)
    {
        return restAPI.getCommunitiesByName(serverName, requestBody);
    }


    /**
     * Retrieve the list of community metadata elements that contain the search string.
     *
     * @param serverName name of the service to route the request to
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/communities/by-search-string")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="findCommunities",
            description="Retrieve the list of community metadata elements that contain the search string.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/community"))

    public OpenMetadataRootElementsResponse findCommunities(@PathVariable
                                                            String                  serverName,
                                                            @RequestBody (required = false)
                                                            SearchStringRequestBody requestBody)
    {
        return restAPI.findCommunities(serverName, requestBody);
    }


    /**
     * Return the properties of a specific community.
     *
     * @param serverName name of the service to route the request to
     * @param communityGUID    unique identifier of the required element
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/communities/{communityGUID}/retrieve")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getCommunityByGUID",
            description="Return the properties of a specific community.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/community"))

    public OpenMetadataRootElementResponse getCommunityByGUID(@PathVariable
                                                              String             serverName,
                                                              @PathVariable
                                                              String             communityGUID,
                                                              @RequestBody (required = false)
                                                              GetRequestBody requestBody)
    {
        return restAPI.getCommunityByGUID(serverName, communityGUID, requestBody);
    }

}
