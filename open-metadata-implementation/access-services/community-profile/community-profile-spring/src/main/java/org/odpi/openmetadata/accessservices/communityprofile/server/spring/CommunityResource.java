/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.odpi.openmetadata.accessservices.communityprofile.rest.TemplateRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.accessservices.communityprofile.server.CommunityRESTServices;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * The CommunityResource provides a Spring based server-side REST API
 * that supports the CommunityManagementInterface.   It delegates each request to the
 * CommunityRESTServices.  This provides the server-side implementation of the Community Profile Open Metadata
 * Assess Service (OMAS) which is used to manage information about people, roles and organizations.
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/access-services/community-profile/users/{userId}")

@Tag(name="Metadata Access Server: Community Profile OMAS", description="The Community Profile OMAS provides APIs and events for tools and applications that are managing information about people and the way they work together.",
     externalDocs=@ExternalDocumentation(description="Further Information",
                                         url="https://egeria-project.org/services/omas/community-profile/overview/"))

public class CommunityResource
{
    private final CommunityRESTServices restAPI = new CommunityRESTServices();

    /**
     * Default constructor
     */
    public CommunityResource()
    {
    }



    /**
     * Create a new metadata element to represent a community.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param requestBody properties to store
     *
     * @return unique identifier of the new metadata element or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/communities")

    public GUIDResponse createCommunity(@PathVariable String                   serverName,
                                        @PathVariable String                   userId,
                                        @RequestBody  ReferenceableRequestBody requestBody)
    {
        return restAPI.createCommunity(serverName, userId, requestBody);
    }


    /**
     * Create a new metadata element to represent a community using an existing metadata element as a template.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param templateGUID unique identifier of the metadata element to copy
     * @param requestBody properties that override the template
     *
     * @return unique identifier of the new metadata element or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/communities/from-template/{templateGUID}")

    public GUIDResponse createCommunityFromTemplate(@PathVariable String              serverName,
                                                    @PathVariable String              userId,
                                                    @PathVariable String              templateGUID,
                                                    @RequestBody  TemplateRequestBody requestBody)
    {
        return restAPI.createCommunityFromTemplate(serverName, userId, templateGUID, requestBody);
    }


    /**
     * Update the metadata element representing a community.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param communityGUID unique identifier of the metadata element to update
     * @param isMergeUpdate should the new properties be merged with the existing properties of overlay them?
     * @param requestBody new properties for this element
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/communities/{communityGUID}")

    public VoidResponse updateCommunity(@PathVariable String                  serverName,
                                        @PathVariable String                  userId,
                                        @PathVariable String                  communityGUID,
                                        @RequestParam boolean                 isMergeUpdate,
                                        @RequestBody ReferenceableRequestBody requestBody)
    {
        return restAPI.updateCommunity(serverName, userId, communityGUID, isMergeUpdate, requestBody);
    }



    /**
     * Create a relationship between a community and a person role.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param communityGUID unique identifier of the community in the external data manager
     * @param communityRoleGUID unique identifier of the person role in the external data manager
     * @param requestBody data manager identifiers
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/communities/{communityGUID}/community-roles/{communityRoleGUID}")

    public VoidResponse setupCommunityRole(@PathVariable String                  serverName,
                                           @PathVariable String                  userId,
                                           @PathVariable String                  communityGUID,
                                           @PathVariable String                  communityRoleGUID,
                                           @RequestBody  RelationshipRequestBody requestBody)
    {
        return restAPI.setupCommunityRole(serverName, userId, communityGUID, communityRoleGUID, requestBody);
    }


    /**
     * Remove a relationship between a community and a person role.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param communityGUID unique identifier of the community in the external data manager
     * @param communityRoleGUID unique identifier of the person role in the external data manager
     * @param requestBody data manager identifiers
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/communities/{communityGUID}/community-roles/{communityRoleGUID}/delete")

    public VoidResponse clearCommunityRole(@PathVariable String                    serverName,
                                           @PathVariable String                    userId,
                                           @PathVariable String                    communityGUID,
                                           @PathVariable String                    communityRoleGUID,
                                           @RequestBody ExternalSourceRequestBody requestBody)
    {
        return restAPI.clearCommunityRole(serverName, userId, communityGUID, communityRoleGUID, requestBody);
    }


    /**
     * Remove the metadata element representing a community.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param communityGUID unique identifier of the metadata element to remove
     * @param requestBody external source identifiers
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/communities/{communityGUID}/delete")

    public VoidResponse removeCommunity(@PathVariable String                    serverName,
                                        @PathVariable String                    userId,
                                        @PathVariable String                    communityGUID,
                                        @RequestBody ExternalSourceRequestBody requestBody)
    {
        return restAPI.removeCommunity(serverName, userId, communityGUID, requestBody);
    }


    /**
     * Retrieve the list of community metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param requestBody string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/communities/by-search-string")

    public CommunitiesResponse findCommunities(@PathVariable String                  serverName,
                                               @PathVariable String                  userId,
                                               @RequestBody  SearchStringRequestBody requestBody,
                                               @RequestParam int                     startFrom,
                                               @RequestParam int                     pageSize)
    {
        return restAPI.findCommunities(serverName, userId, requestBody, startFrom, pageSize);
    }


    /**
     * Retrieve the list of community metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param requestBody name to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/communities/by-name")

    public CommunitiesResponse getCommunitiesByName(@PathVariable String          serverName,
                                                    @PathVariable String          userId,
                                                    @RequestBody  NameRequestBody requestBody,
                                                    @RequestParam int             startFrom,
                                                    @RequestParam int             pageSize)
    {
        return restAPI.getCommunitiesByName(serverName, userId, requestBody, startFrom, pageSize);
    }

    /**
     * Return information about a person role connected to the named community.
     *
     * @param serverName called server
     * @param userId calling user
     * @param communityGUID unique identifier for the community
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return list of matching person roles
     *
     *   InvalidParameterException name or userId is null
     *   PropertyServerException problem accessing property server
     *   UserNotAuthorizedException security access problem
     */
    @GetMapping(path = "/person-roles/by-community/{communityGUID}")

    public PersonRolesResponse getRolesForCommunity(@PathVariable String          serverName,
                                                    @PathVariable String          userId,
                                                    @PathVariable String          communityGUID,
                                                    @RequestParam int             startFrom,
                                                    @RequestParam int             pageSize)
    {
        return restAPI.getRolesForCommunity(serverName, userId, communityGUID, startFrom, pageSize);
    }


    /**
     * Retrieve the list of community metadata elements.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @GetMapping(path = "/communities")

    public CommunitiesResponse getCommunitiesByName(@PathVariable String          serverName,
                                                    @PathVariable String          userId,
                                                    @RequestParam int             startFrom,
                                                    @RequestParam int             pageSize)
    {
        return restAPI.getCommunities(serverName, userId, startFrom, pageSize);
    }


    /**
     * Retrieve the community metadata element with the supplied unique identifier.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param guid unique identifier of the requested metadata element
     *
     * @return matching metadata element or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @GetMapping(path = "/communities/{guid}")

    public CommunityResponse getCommunityByGUID(@PathVariable String serverName,
                                                @PathVariable String userId,
                                                @PathVariable String guid)
    {
        return restAPI.getCommunityByGUID(serverName, userId, guid);
    }
}
