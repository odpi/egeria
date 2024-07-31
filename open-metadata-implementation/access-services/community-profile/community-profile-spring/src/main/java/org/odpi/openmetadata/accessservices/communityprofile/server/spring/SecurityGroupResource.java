/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.accessservices.communityprofile.server.SecurityGroupRESTServices;
import org.odpi.openmetadata.frameworks.openmetadata.properties.security.SecurityGroupProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * The SecurityGroupResource provides a Spring based server-side REST API
 * that supports the SecurityGroupManagementInterface.   It delegates each request to the
 * SecurityGroupRESTServices.  This provides the server-side implementation of the Community Profile Open Metadata
 * Access Service (OMAS) which is used to manage information about people, roles and organizations.
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/access-services/community-profile/users/{userId}")

@Tag(name="Metadata Access Server: Community Profile OMAS", description="The Community Profile OMAS provides APIs and events for tools and applications that are managing information about people and the way they work together.",
     externalDocs=@ExternalDocumentation(description="Further Information",
                                         url="https://egeria-project.org/services/omas/community-profile/overview/"))

public class SecurityGroupResource
{
    private final SecurityGroupRESTServices restAPI = new SecurityGroupRESTServices();

    /**
     * Default constructor
     */
    public SecurityGroupResource()
    {
    }


    /**
     * Create a new security group.  The type of the definition is located in the requestBody.
     *
     * @param serverName called server
     * @param userId calling user
     * @param requestBody requestBody of the definition
     *
     * @return unique identifier of the definition or
     *  InvalidParameterException typeName, documentIdentifier or userId is null; documentIdentifier is not unique; typeName is not valid
     *  PropertyServerException problem accessing the metadata service
     *  UserNotAuthorizedException security access problem
     */
    @PostMapping(value = "/security-groups")

    public GUIDResponse createSecurityGroup(@PathVariable String                  serverName,
                                            @PathVariable String                  userId,
                                            @RequestBody SecurityGroupProperties requestBody)
    {
        return restAPI.createSecurityGroup(serverName, userId, requestBody);
    }


    /**
     * Update an existing security group.
     *
     * @param serverName called server
     * @param userId calling user
     * @param securityGroupGUID unique identifier of the definition to update
     * @param isMergeUpdate are unspecified properties unchanged (true) or removed?
     * @param requestBody properties to update
     *
     * @return void or
     *  InvalidParameterException guid, documentIdentifier or userId is null; documentIdentifier is not unique; guid is not known
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    @PostMapping(value = "/security-groups/{securityGroupGUID}/update")

    public VoidResponse  updateSecurityGroup(@PathVariable String                  serverName,
                                             @PathVariable String                  userId,
                                             @PathVariable String                  securityGroupGUID,
                                             @RequestParam boolean                 isMergeUpdate,
                                             @RequestBody  SecurityGroupProperties requestBody)
    {
        return restAPI.updateSecurityGroup(serverName, userId, securityGroupGUID, isMergeUpdate, requestBody);
    }


    /**
     * Delete a specific security group.
     *
     * @param serverName called server
     * @param userId calling user
     * @param securityGroupGUID unique identifier of the definition to remove
     * @param requestBody null request body
     *
     * @return void or
     *  InvalidParameterException guid is null or not known
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    @PostMapping(value = "/security-groups/{securityGroupGUID}/delete")

    public VoidResponse  deleteSecurityGroup(@PathVariable String          serverName,
                                             @PathVariable String          userId,
                                             @PathVariable String          securityGroupGUID,
                                             @RequestBody(required = false)
                                                           NullRequestBody requestBody)
    {
        return restAPI.deleteSecurityGroup(serverName, userId, securityGroupGUID, requestBody);
    }


    /**
     * Return the list of security groups associated with a unique distinguishedName.  In an ideal world, the should be only one.
     *
     * @param serverName called server
     * @param userId calling user
     * @param distinguishedName unique name of the security group
     * @param startFrom where to start from in the list of definitions
     * @param pageSize max number of results to return in one call
     *
     * @return list of security groups or
     *  InvalidParameterException one of the parameters is invalid
     *  UserNotAuthorizedException the caller is not authorized to issue the request
     *  PropertyServerException the metadata service has problems
     */
    @GetMapping(value = "/security-groups/for-distinguished-name/{distinguishedName}")

    public SecurityGroupsResponse getSecurityGroupsForDistinguishedName(@PathVariable String serverName,
                                                                        @PathVariable String userId,
                                                                        @PathVariable String distinguishedName,
                                                                        @RequestParam int    startFrom,
                                                                        @RequestParam int    pageSize)
    {
        return restAPI.getSecurityGroupsForDistinguishedName(serverName, userId, distinguishedName, startFrom, pageSize);
    }


    /**
     * Return the elements that are governed by the supplied security group.
     *
     * @param serverName called server
     * @param userId calling user
     * @param securityGroupGUID unique name of the security group
     * @param startFrom where to start from in the list of definitions
     * @param pageSize max number of results to return in one call
     *
     * @return list of headers for the associated elements or
     *  InvalidParameterException one of the parameters is invalid
     *  UserNotAuthorizedException the caller is not authorized to issue the request
     *  PropertyServerException the metadata service has problems
     */
    @GetMapping(value = "/security-groups/{securityGroupGUID}/governed-by/elements")

    public ElementStubsResponse getElementsGovernedBySecurityGroup(@PathVariable String serverName,
                                                                   @PathVariable String userId,
                                                                   @PathVariable String securityGroupGUID,
                                                                   @RequestParam int    startFrom,
                                                                   @RequestParam int    pageSize)
    {
        return restAPI.getElementsGovernedBySecurityGroup(serverName, userId, securityGroupGUID, startFrom, pageSize);
    }


    /**
     * Return the list of security groups that match the search string - this can be a regular expression.
     *
     * @param serverName called server
     * @param userId calling user
     * @param requestBody value to search for
     * @param startFrom where to start from in the list of definition results
     * @param pageSize max number of results to return in one call
     *
     * @return list of security groups or
     *  InvalidParameterException one of the parameters is invalid
     *  UserNotAuthorizedException the caller is not authorized to issue the request
     *  PropertyServerException the metadata service has problems
     */
    @PostMapping(value = "/security-groups/by-search-string")

    public SecurityGroupsResponse findSecurityGroups(@PathVariable String                  serverName,
                                                     @PathVariable String                  userId,
                                                     @RequestParam int                     startFrom,
                                                     @RequestParam int                     pageSize,
                                                     @RequestBody  SearchStringRequestBody requestBody)
    {
        return restAPI.findSecurityGroups(serverName, userId, startFrom, pageSize, requestBody);
    }


    /**
     * Return information about a specific actor profile.
     *
     * @param serverName called server
     * @param userId calling user
     * @param securityGroupGUID unique identifier for the actor profile
     *
     * @return properties of the actor profile
     *
     *   InvalidParameterException securityGroupGUID or userId is null
     *   PropertyServerException problem accessing property server
     *   UserNotAuthorizedException security access problem
     */
    @GetMapping(value = "/security-groups/{securityGroupGUID}")

    public SecurityGroupResponse getSecurityGroupByGUID(@PathVariable String serverName,
                                                        @PathVariable String userId,
                                                        @PathVariable String securityGroupGUID)
    {
        return restAPI.getSecurityGroupByGUID(serverName, userId, securityGroupGUID);
    }
}
