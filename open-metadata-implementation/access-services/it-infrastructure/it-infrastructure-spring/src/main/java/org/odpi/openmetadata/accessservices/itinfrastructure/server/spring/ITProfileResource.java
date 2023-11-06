/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.itinfrastructure.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.accessservices.itinfrastructure.rest.ITProfileListResponse;
import org.odpi.openmetadata.accessservices.itinfrastructure.rest.ITProfileRequestBody;
import org.odpi.openmetadata.accessservices.itinfrastructure.rest.ITProfileResponse;
import org.odpi.openmetadata.accessservices.itinfrastructure.rest.ContactMethodRequestBody;
import org.odpi.openmetadata.accessservices.itinfrastructure.rest.MetadataSourceRequestBody;
import org.odpi.openmetadata.accessservices.itinfrastructure.rest.UserIdentityListResponse;
import org.odpi.openmetadata.accessservices.itinfrastructure.rest.UserIdentityRequestBody;
import org.odpi.openmetadata.accessservices.itinfrastructure.rest.UserIdentityResponse;
import org.odpi.openmetadata.accessservices.itinfrastructure.server.ITProfileRESTServices;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.NameRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.SearchStringRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * The ITProfileResource provides a Spring based server-side REST API
 * that supports the ITProfileManagerInterface.   It delegates each request to the
 * OrganizationRESTServices.  This provides the server-side implementation of the IT Infrastructure Open Metadata
 * Assess Service (OMAS) which is used to manage information about IT profiles and their userIds.
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/access-services/it-infrastructure/users/{userId}")

@Tag(name="Metadata Access Server: IT Infrastructure OMAS",
     description="The IT Infrastructure OMAS provides APIs for tools and applications managing the IT infrastructure that supports the data assets.\n",
     externalDocs=@ExternalDocumentation(description="Further Information",
                                         url="https://egeria-project.org/services/omas/it-infrastructure/overview/"))

public class ITProfileResource
{
    private final ITProfileRESTServices restAPI = new ITProfileRESTServices();

    /**
     * Default constructor
     */
    public ITProfileResource()
    {
    }


    /**
     * Create a definition of an IT profile.
     *
     * @param serverName called server
     * @param userId calling user
     * @param requestBody properties for an IT profile
     *
     * @return unique identifier of IT profile
     *
     *   InvalidParameterException qualifiedName or userId is null; qualifiedName is not unique
     *   PropertyServerException problem accessing property server
     *   UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/profiles")

    public GUIDResponse createITProfile(@PathVariable String                  serverName,
                                        @PathVariable String                  userId,
                                        @RequestBody  ITProfileRequestBody requestBody)
    {
        return restAPI.createITProfile(serverName, userId, requestBody);
    }


    /**
     * Update the definition of an IT profile.
     *
     * @param serverName called server
     * @param userId calling user
     * @param itProfileGUID unique identifier of IT profile
     * @param isMergeUpdate are unspecified properties unchanged (true) or replaced with null?
     * @param requestBody properties to change
     *
     * @return void or
     *
     *   InvalidParameterException guid, qualifiedName or userId is null; qualifiedName is not unique; guid is not known
     *   PropertyServerException problem accessing property server
     *   UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/profiles/{itProfileGUID}")

    public VoidResponse updateITProfile(@PathVariable String               serverName,
                                        @PathVariable String               userId,
                                        @PathVariable String               itProfileGUID,
                                        @RequestParam boolean              isMergeUpdate,
                                        @RequestBody  ITProfileRequestBody requestBody)
    {
        return restAPI.updateITProfile(serverName, userId, itProfileGUID, isMergeUpdate, requestBody);
    }


    /**
     * Remove the definition of an IT profile.
     *
     * @param serverName called server
     * @param userId calling user
     * @param itProfileGUID unique identifier of IT profile
     * @param requestBody   identifiers of the software server capability entity that represented the external source - null for local
     *
     * @return void or
     *
     *   InvalidParameterException guid or userId is null; guid is not known
     *   PropertyServerException problem accessing property server
     *   UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/profiles/{itProfileGUID}/delete")

    public VoidResponse deleteITProfile(@PathVariable String                    serverName,
                                        @PathVariable String                    userId,
                                        @PathVariable String                    itProfileGUID,
                                        @RequestBody  MetadataSourceRequestBody requestBody)
    {
        return restAPI.deleteITProfile(serverName, userId, itProfileGUID, requestBody);
    }


    /**
     * Add a new contact method to the profile.
     *
     * @param serverName called server
     * @param userId the name of the calling user.
     * @param itProfileGUID identifier of the profile to update.
     * @param requestBody properties of contact method.
     *
     * @return unique identifier (guid) for the new contact method.
     *
     *   InvalidParameterException the userId is null or invalid.  Another property is invalid.
     *   PropertyServerException there is a problem retrieving information from the property server(s).
     *   UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/profiles/{itProfileGUID}/contact-methods")

    public GUIDResponse addContactMethod(@PathVariable String                   serverName,
                                         @PathVariable String                   userId,
                                         @PathVariable String                   itProfileGUID,
                                         @RequestBody  ContactMethodRequestBody requestBody)
    {
        return restAPI.addContactMethod(serverName, userId, itProfileGUID, requestBody);
    }


    /**
     * Remove an obsolete contact method from the profile.
     *
     * @param serverName called server
     * @param userId the name of the calling user.
     * @param contactMethodGUID unique identifier (guid) for the obsolete contact method.
     * @param requestBody   identifiers of the software server capability entity that represented the external source - null for local
     *
     * @return void or
     *
     *   InvalidParameterException the userId is null or invalid.  Another property is invalid.
     *   PropertyServerException there is a problem retrieving information from the property server(s).
     *   UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/profiles/contact-methods/{contactMethodGUID}/delete")

    public VoidResponse deleteContactMethod(@PathVariable String                    serverName,
                                            @PathVariable String                    userId,
                                            @PathVariable String                    contactMethodGUID,
                                            @RequestBody  MetadataSourceRequestBody requestBody)
    {
        return restAPI.deleteContactMethod(serverName, userId, contactMethodGUID, requestBody);
    }


    /**
     * Return information about a specific IT profile.
     *
     * @param serverName called server
     * @param userId calling user
     * @param itProfileGUID unique identifier for the IT profile
     *
     * @return properties of the IT profile
     *
     *   InvalidParameterException itProfileGUID or userId is null
     *   PropertyServerException problem accessing property server
     *   UserNotAuthorizedException security access problem
     */
    @GetMapping(path = "/profiles/{itProfileGUID}")

    public ITProfileResponse getITProfileByGUID(@PathVariable String serverName,
                                                @PathVariable String userId,
                                                @PathVariable String itProfileGUID)
    {
        return restAPI.getITProfileByGUID(serverName, userId, itProfileGUID);
    }


    /**
     * Return information about a specific IT profile.
     *
     * @param serverName called server
     * @param userId calling user
     * @param itProfileUserId unique identifier for the IT profile
     *
     * @return properties of the IT profile
     *
     *   InvalidParameterException itProfileUserId or userId is null
     *   PropertyServerException problem accessing property server
     *   UserNotAuthorizedException security access problem
     */
    @GetMapping(path = "/profiles/user-ids/{itProfileUserId}")

    public ITProfileResponse getITProfileByUserId(@PathVariable String serverName,
                                                  @PathVariable String userId,
                                                  @PathVariable String itProfileUserId)
    {
        return restAPI.getITProfileByUserId(serverName, userId, itProfileUserId);
    }


    /**
     * Return information about a named IT profile.
     *
     * @param serverName called server
     * @param userId calling user
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param requestBody unique name for the IT profile
     *
     * @return list of matching IT profiles (hopefully only one)
     *
     *   InvalidParameterException name or userId is null
     *   PropertyServerException problem accessing property server
     *   UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/profiles/by-name")

    public ITProfileListResponse getITProfileByName(@PathVariable String          serverName,
                                                    @PathVariable String          userId,
                                                    @RequestParam int             startFrom,
                                                    @RequestParam int             pageSize,
                                                    @RequestBody  NameRequestBody requestBody)
    {
        return restAPI.getITProfileByName(serverName, userId, startFrom, pageSize, requestBody);
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
     * @return list of matching IT profiles
     *
     *   InvalidParameterException guid invalid or the external references are not correctly specified, or are null.
     *   PropertyServerException the server is not available.
     *   UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    @PostMapping(path = "/profiles/by-search-string")

    public ITProfileListResponse findITProfile(@PathVariable String                  serverName,
                                               @PathVariable String                  userId,
                                               @RequestParam int                     startFrom,
                                               @RequestParam int                     pageSize,
                                               @RequestBody  SearchStringRequestBody requestBody)
    {
        return restAPI.findITProfile(serverName, userId, startFrom, pageSize, requestBody);
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
                                           @RequestBody  MetadataSourceRequestBody requestBody)
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
                                              @RequestBody  MetadataSourceRequestBody requestBody)
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
                                                  @RequestBody  MetadataSourceRequestBody requestBody)
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

    public UserIdentityListResponse findUserIdentities(@PathVariable String                  serverName,
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

    public UserIdentityListResponse getUserIdentitiesByName(@PathVariable String          serverName,
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
}
