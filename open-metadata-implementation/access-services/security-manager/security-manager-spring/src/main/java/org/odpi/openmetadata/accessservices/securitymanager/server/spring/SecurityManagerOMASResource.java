/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.securitymanager.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.accessservices.securitymanager.server.SecurityManagerRESTServices;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.security.SecurityGroupProperties;
import org.springframework.web.bind.annotation.*;

/**
 * Server-side REST API support for security manager independent REST endpoints
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/access-services/security-manager/users/{userId}")

@Tag(name="Metadata Access Server: Security Manager OMAS",
        description="The Security Manager OMAS provides APIs for tools and applications wishing to manage metadata relating to security managers.",
        externalDocs=@ExternalDocumentation(description="Further Information",
                url="https://egeria-project.org/services/omas/security-manager/overview/"))

public class SecurityManagerOMASResource
{
    private final SecurityManagerRESTServices restAPI = new SecurityManagerRESTServices();


    /**
     * Instantiates a new Security Manager OMAS resource.
     */
    public SecurityManagerOMASResource()
    {
    }


    /**
     * Return the connection object for the Security Manager OMAS's out topic.
     *
     * @param serverName name of the server to route the request to
     * @param userId identifier of calling user
     * @param callerId unique identifier of the caller
     *
     * @return connection object for the out topic or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem retrieving the discovery engine definition.
     */
    @GetMapping(path = "/topics/out-topic-connection/{callerId}")

    public OCFConnectionResponse getOutTopicConnection(@PathVariable String serverName,
                                                       @PathVariable String userId,
                                                       @PathVariable String callerId)
    {
        return restAPI.getOutTopicConnection(serverName, userId, callerId);
    }



    /**
     * Create the security manager software server capability.
     *
     * @param serverName name of calling server
     * @param userId calling user
     * @param requestBody properties of the file system
     *
     * @return unique identifier for the file system or
     * InvalidParameterException one of the parameters is null or invalid or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/metadata-sources/security-managers")

    public GUIDResponse   createSecurityManagerInCatalog(@PathVariable String                     serverName,
                                                         @PathVariable String                     userId,
                                                         @RequestBody  SecurityManagerRequestBody requestBody)
    {
        return restAPI.createSecurityManagerInCatalog(serverName, userId, requestBody);
    }


    /**
     * Retrieve the unique identifier of the software server capability representing a metadata source.
     *
     * @param serverName name of the server to route the request to.
     * @param userId calling user
     * @param qualifiedName unique name of the integration daemon
     *
     * @return unique identifier of the integration daemon's software server capability or
     * InvalidParameterException  the bean properties are invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException    problem accessing the property server
     */
    @GetMapping(path = "metadata-sources/by-name/{qualifiedName}")

    public GUIDResponse  getMetadataSourceGUID(@PathVariable String serverName,
                                               @PathVariable String userId,
                                               @PathVariable String qualifiedName)
    {
        return restAPI.getMetadataSourceGUID(serverName, userId, qualifiedName);
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
                                            @RequestBody  SecurityGroupProperties requestBody)
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

    public VoidResponse updateSecurityGroup(@PathVariable String                  serverName,
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
                                                     @RequestBody SearchStringRequestBody requestBody)
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


    /**
     * Create a UserIdentity.  This is not connected to a profile.
     *
     * @param serverName name of target server
     * @param userId the name of the calling user.
     * @param requestBody userId for the new userIdentity.
     *
     * @return guid or
     * InvalidParameterException - one of the parameters is invalid or
     * PropertyServerException  - there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/user-identities")

    public GUIDResponse createUserIdentity(@PathVariable String                  serverName,
                                           @PathVariable String                  userId,
                                           @RequestBody  UserIdentityRequestBody requestBody)
    {
        return restAPI.createUserIdentity(serverName, userId, requestBody);
    }


    /**
     * Update a UserIdentity.
     *
     * @param serverName name of target server
     * @param userId the name of the calling user
     * @param userIdentityGUID unique identifier of the UserIdentity
     * @param isMergeUpdate should the supplied properties be overlaid on the existing properties (true) or replace them (false
     * @param requestBody updated properties for the new userIdentity
     *
     * @return void or
     *  InvalidParameterException one of the parameters is invalid.
     *  PropertyServerException  there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/user-identities/{userIdentityGUID}")

    public VoidResponse updateUserIdentity(@PathVariable String                  serverName,
                                           @PathVariable String                  userId,
                                           @PathVariable String                  userIdentityGUID,
                                           @RequestParam boolean                 isMergeUpdate,
                                           @RequestBody  UserIdentityRequestBody requestBody)
    {
        return restAPI.updateUserIdentity(serverName, userId, userIdentityGUID, isMergeUpdate, requestBody);
    }


    /**
     * Remove a user identity object.  This will fail if a profile would be left without an
     * associated user identity.
     *
     * @param serverName name of target server
     * @param userId the name of the calling user.
     * @param userIdentityGUID unique identifier of the UserIdentity
     * @param requestBody external source identifiers
     *
     * @return void or
     * InvalidParameterException - one of the parameters is invalid or
     * PropertyServerException  - there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/user-identities/{userIdentityGUID}/delete")

    public VoidResponse deleteUserIdentity(@PathVariable String                    serverName,
                                           @PathVariable String                    userId,
                                           @PathVariable String                    userIdentityGUID,
                                           @RequestBody  ExternalSourceRequestBody requestBody)
    {
        return restAPI.deleteUserIdentity(serverName, userId, userIdentityGUID, requestBody);
    }


    /**
     * Link a user identity to a profile.  This will fail if the user identity is already connected to
     * a profile.
     *
     * @param serverName name of target server
     * @param userId the name of the calling user.
     * @param userIdentityGUID unique identifier of the UserIdentity
     * @param profileGUID the profile to add the identity to.
     * @param requestBody external source identifiers
     *
     * @return void or
     * InvalidParameterException - one of the parameters is invalid or
     * PropertyServerException  - there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/user-identities/{userIdentityGUID}/profiles/{profileGUID}/link")

    public VoidResponse  addIdentityToProfile(@PathVariable String                    serverName,
                                              @PathVariable String                    userId,
                                              @PathVariable String                    userIdentityGUID,
                                              @PathVariable String                    profileGUID,
                                              @RequestBody  ExternalSourceRequestBody requestBody)
    {
        return restAPI.addIdentityToProfile(serverName, userId, userIdentityGUID, profileGUID, requestBody);
    }


    /**
     * Remove a user identity object.  This will fail if the profile would be left without an
     * associated user identity.
     *
     * @param serverName name of target server
     * @param userId the name of the calling user.
     * @param userIdentityGUID unique identifier of the UserIdentity
     * @param profileGUID the profile to add the identity to.
     * @param requestBody external source identifiers
     *
     * @return void or
     * InvalidParameterException - one of the parameters is invalid or
     * PropertyServerException  - there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/user-identities/{userIdentityGUID}/profiles/{profileGUID}/unlink")

    public VoidResponse removeIdentityFromProfile(@PathVariable String                    serverName,
                                                  @PathVariable String                    userId,
                                                  @PathVariable String                    userIdentityGUID,
                                                  @PathVariable String                    profileGUID,
                                                  @RequestBody  ExternalSourceRequestBody requestBody)
    {
        return restAPI.removeIdentityFromProfile(serverName, userId, userIdentityGUID, profileGUID, requestBody);
    }


    /**
     * Retrieve the list of user identity metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param serverName name of target server
     * @param userId calling user
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/user-identities/by-search-string")

    public UserIdentitiesResponse findUserIdentities(@PathVariable String                  serverName,
                                                     @PathVariable String                  userId,
                                                     @RequestParam int                     startFrom,
                                                     @RequestParam int                     pageSize,
                                                     @RequestBody  SearchStringRequestBody requestBody)
    {
        return restAPI.findUserIdentities(serverName, userId, startFrom, pageSize, requestBody);
    }


    /**
     * Retrieve the list of user identity metadata elements with a matching qualified name.
     * There are no wildcards supported on this request.
     *
     * @param serverName name of target server
     * @param userId calling user
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody name to search for
     *
     * @return list of matching metadata elements
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/user-identities/by-name")

    public UserIdentitiesResponse getUserIdentitiesByName(@PathVariable String          serverName,
                                                          @PathVariable String          userId,
                                                          @RequestParam int             startFrom,
                                                          @RequestParam int             pageSize,
                                                          @RequestBody  NameRequestBody requestBody)
    {
        return restAPI.getUserIdentitiesByName(serverName, userId, startFrom, pageSize, requestBody);
    }


    /**
     * Retrieve the userIdentity metadata element with the supplied unique identifier.
     *
     * @param serverName name of target server
     * @param userId calling user
     * @param userIdentityGUID unique identifier of the requested metadata element
     *
     * @return matching metadata element or
     *
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @GetMapping(path = "/user-identities/{userIdentityGUID}")

    public UserIdentityResponse getUserIdentityByGUID(@PathVariable String serverName,
                                                      @PathVariable String userId,
                                                      @PathVariable String userIdentityGUID)
    {
        return restAPI.getUserIdentityByGUID(serverName, userId, userIdentityGUID);
    }



    /**
     * Return information about a specific actor profile.
     *
     * @param serverName called server
     * @param userId calling user
     * @param actorProfileGUID unique identifier for the actor profile
     *
     * @return properties of the actor profile
     *
     *   InvalidParameterException actorProfileGUID or userId is null
     *   PropertyServerException problem accessing property server
     *   UserNotAuthorizedException security access problem
     */
    @GetMapping(path = "/profiles/{actorProfileGUID}")

    public ActorProfileResponse getActorProfileByGUID(@PathVariable String serverName,
                                                      @PathVariable String userId,
                                                      @PathVariable String actorProfileGUID)
    {
        return restAPI.getActorProfileByGUID(serverName, userId, actorProfileGUID);
    }


    /**
     * Return information about a specific actor profile.
     *
     * @param serverName called server
     * @param userId calling user
     * @param actorProfileUserId unique identifier for the actor profile
     *
     * @return properties of the actor profile
     *
     *   InvalidParameterException actorProfileUserId or userId is null
     *   PropertyServerException problem accessing property server
     *   UserNotAuthorizedException security access problem
     */
    @GetMapping(path = "/profiles/user-ids/{actorProfileUserId}")

    public ActorProfileResponse getActorProfileByUserId(@PathVariable String serverName,
                                                        @PathVariable String userId,
                                                        @PathVariable String actorProfileUserId)
    {
        return restAPI.getActorProfileByUserId(serverName, userId, actorProfileUserId);
    }


    /**
     * Return information about a named actor profile.
     *
     * @param serverName called server
     * @param userId calling user
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param requestBody unique name for the actor profile
     *
     * @return list of matching actor profiles (hopefully only one)
     *
     *   InvalidParameterException name or userId is null
     *   PropertyServerException problem accessing property server
     *   UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/profiles/by-name")

    public ActorProfilesResponse getActorProfileByName(@PathVariable String          serverName,
                                                       @PathVariable String          userId,
                                                       @RequestParam int             startFrom,
                                                       @RequestParam int             pageSize,
                                                       @RequestBody  NameRequestBody requestBody)
    {
        return restAPI.getActorProfileByName(serverName, userId, startFrom, pageSize, requestBody);
    }


    /**
     * Retrieve the list of matching profiles for the search string.
     *
     * @param serverName called server
     * @param userId the name of the calling user.
     * @param requestBody RegEx string to search for
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return list of matching actor profiles
     *
     *   InvalidParameterException guid invalid or the external references are not correctly specified, or are null.
     *   PropertyServerException the server is not available.
     *   UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    @PostMapping(path = "/profiles/by-search-string")

    public ActorProfilesResponse findActorProfile(@PathVariable String                  serverName,
                                                  @PathVariable String                  userId,
                                                  @RequestParam int                     startFrom,
                                                  @RequestParam int                     pageSize,
                                                  @RequestBody  SearchStringRequestBody requestBody)
    {
        return restAPI.findActorProfile(serverName, userId, startFrom, pageSize, requestBody);
    }



    /**
     * Return the list of people appointed to a particular role.
     *
     * @param serverName           called server
     * @param userId               calling user
     * @param personRoleGUID       unique identifier of the person role
     * @param startFrom            index of the list to start from (0 for start)
     * @param pageSize             maximum number of elements to return
     * @param requestBody          time for appointments, null for full appointment history
     *
     * @return list of appointees or
     *   InvalidParameterException one of the guids is null or not known
     *   PropertyServerException problem accessing property server
     *   UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/person-roles/{personRoleGUID}/appointees")

    public AppointeesResponse getAppointees(@PathVariable String                   serverName,
                                            @PathVariable String                   userId,
                                            @PathVariable String                   personRoleGUID,
                                            @RequestParam int                      startFrom,
                                            @RequestParam int                      pageSize,
                                            @RequestBody ResultsRequestBody requestBody)
    {
        return restAPI.getAppointees(serverName, userId, personRoleGUID, startFrom, pageSize, requestBody);
    }


    /**
     * Return information about a specific person role.
     *
     * @param serverName called server
     * @param userId calling user
     * @param personRoleGUID unique identifier for the person role
     *
     * @return properties of the person role
     *
     *   InvalidParameterException personRoleGUID or userId is null
     *   PropertyServerException problem accessing property server
     *   UserNotAuthorizedException security access problem
     */
    @GetMapping(path = "/person-roles/{personRoleGUID}")

    public PersonRoleResponse getPersonRoleByGUID(@PathVariable String serverName,
                                                  @PathVariable String userId,
                                                  @PathVariable String personRoleGUID)
    {
        return restAPI.getPersonRoleByGUID(serverName, userId, personRoleGUID);
    }


    /**
     * Return information about a named person role.
     *
     * @param serverName called server
     * @param userId calling user
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param requestBody unique name for the actor profile
     *
     * @return list of matching actor profiles (hopefully only one)
     *
     *   InvalidParameterException name or userId is null
     *   PropertyServerException problem accessing property server
     *   UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/person-roles/by-name")

    public PersonRolesResponse getPersonRoleByName(@PathVariable String          serverName,
                                                   @PathVariable String          userId,
                                                   @RequestParam int             startFrom,
                                                   @RequestParam int             pageSize,
                                                   @RequestBody  NameRequestBody requestBody)
    {
        return restAPI.getPersonRoleByName(serverName, userId, startFrom, pageSize, requestBody);
    }


    /**
     * Retrieve the list of matching roles for the search string.
     *
     * @param serverName called server
     * @param userId the name of the calling user.
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param requestBody RegEx string to search for
     *
     * @return list of matching actor profiles
     *
     *   InvalidParameterException guid invalid or the external references are not correctly specified, or are null.
     *   PropertyServerException the server is not available.
     *   UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    @PostMapping(path = "/person-roles/by-search-string")

    public PersonRolesResponse findPersonRole(@PathVariable String                  serverName,
                                              @PathVariable String                  userId,
                                              @RequestParam int                     startFrom,
                                              @RequestParam int                     pageSize,
                                              @RequestBody  SearchStringRequestBody requestBody)
    {
        return restAPI.findPersonRole(serverName, userId, startFrom, pageSize, requestBody);
    }
}
