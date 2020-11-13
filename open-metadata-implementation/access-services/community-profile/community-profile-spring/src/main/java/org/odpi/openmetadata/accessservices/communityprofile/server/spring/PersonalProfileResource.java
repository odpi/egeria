/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.accessservices.communityprofile.rest.PersonalProfileListResponse;
import org.odpi.openmetadata.accessservices.communityprofile.rest.PersonalProfileRequestBody;
import org.odpi.openmetadata.accessservices.communityprofile.rest.PersonalProfileResponse;
import org.odpi.openmetadata.accessservices.communityprofile.rest.PersonalProfileValidatorRequestBody;
import org.odpi.openmetadata.accessservices.communityprofile.server.PersonalProfileRESTServices;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.springframework.web.bind.annotation.*;

/**
 * The GovernanceProgramOMASGovernanceLeadershipResource provides a Spring based server-side REST API
 * that supports the GovernanceLeadershipInterface.   It delegates each request to the
 * GovernanceProgramRESTServices.  This provides the server-side implementation of the Governance Program Open Metadata
 * Assess Service (OMAS) which is used to manage the full lifecycle of a governance program.
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/access-services/community-profile/users/{userId}")

@Tag(name="Community Profile OMAS", description="The Community Profile OMAS provides APIs and events for tools and applications that are managing information about people and the way they work together.", externalDocs=@ExternalDocumentation(description="Community Profile Open Metadata Access Service (OMAS)",url="https://egeria.odpi.org/open-metadata-implementation/access-services/community-profile/"))

public class PersonalProfileResource
{
    private PersonalProfileRESTServices restAPI = new PersonalProfileRESTServices();

    /**
     * Default constructor
     */
    public PersonalProfileResource()
    {
    }


    /**
     * Create a personal profile for an individual who is to be appointed to a governance role but does not
     * have a profile in open metadata.
     *
     * @param serverName name of server instance to call
     * @param userId the name of the calling user.
     * @param requestBody properties about the individual.
     * @return Unique identifier for the personal profile or
     * InvalidParameterException the employee number or full name is null or
     * PropertyServerException the server is not available or
     * UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    @PostMapping(path = "/personal-profiles")

    public GUIDResponse createPersonalProfile(@PathVariable String                     serverName,
                                              @PathVariable String                     userId,
                                              @RequestBody  PersonalProfileRequestBody requestBody)
    {
        return restAPI.createPersonalProfile(serverName, userId, requestBody);
    }


    /**
     * Update properties for the personal properties.  Null values result in empty fields in the profile.
     *
     * @param serverName name of server instance to call
     * @param userId the name of the calling user.
     * @param profileGUID unique identifier for the profile.
     * @param requestBody properties about the individual.
     * @return void response or
     * UnrecognizedGUIDException the unique identifier of the personal profile is either null or invalid or
     * InvalidParameterException the full name is null or the employeeNumber does not match the profileGUID or
     * PropertyServerException the server is not available or
     * UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    @PostMapping(path = "/personal-profiles/{profileGUID}")

    public VoidResponse updatePersonalProfile(@PathVariable String                     serverName,
                                              @PathVariable String                     userId,
                                              @PathVariable String                     profileGUID,
                                              @RequestBody  PersonalProfileRequestBody requestBody)
    {
        return restAPI.updatePersonalProfile(serverName, userId, profileGUID, requestBody);
    }


    /**
     * Delete the personal profile.
     *
     * @param serverName name of server instance to call
     * @param userId the name of the calling user.
     * @param profileGUID unique identifier for the profile.
     * @param requestBody personnel/serial/unique employee number of the individual.
     * @return void response or
     * UnrecognizedGUIDException the unique identifier of the personal profile is either null or invalid or
     * InvalidParameterException the employee number or full name is null or
     * PropertyServerException the server is not available or
     * UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    @PostMapping(path = "/personal-profiles/{profileGUID}/delete")

    public VoidResponse   deletePersonalProfile(@PathVariable String                              serverName,
                                                @PathVariable String                              userId,
                                                @PathVariable String                              profileGUID,
                                                @RequestBody  PersonalProfileValidatorRequestBody requestBody)
    {
        return restAPI.deletePersonalProfile(serverName, userId, profileGUID, requestBody);
    }


    /**
     * Return the profile for this user.
     *
     * @param serverName name of the server instances for this request
     * @param userId userId of the user making the request.
     * @param profileUserId userId for person that profile belongs to
     *
     * @return profile response object or null or
     * InvalidParameterException the userId is null or invalid or
     * PropertyServerException there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @GetMapping(path = "/personal-profiles/user/{profileUserId}")

    public PersonalProfileResponse getPersonalProfileForUser(@PathVariable String serverName,
                                                             @PathVariable String userId,
                                                             @PathVariable String profileUserId)
    {
        return restAPI.getPersonalProfileForUser(serverName, userId, profileUserId);
    }


    /**
     * Retrieve a personal profile by guid.
     *
     * @param serverName name of server instance to call
     * @param userId the name of the calling user.
     * @param profileGUID unique identifier for the profile.
     * @return personal profile object or
     * UnrecognizedGUIDException the unique identifier of the personal profile is either null or invalid or
     * PropertyServerException the server is not available or
     * UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    @GetMapping(path = "/personal-profiles/{profileGUID}")

    public PersonalProfileResponse getPersonalProfileByGUID(@PathVariable String        serverName,
                                                            @PathVariable String        userId,
                                                            @PathVariable String        profileGUID)
    {
        return restAPI.getPersonalProfileByGUID(serverName, userId, profileGUID);
    }


    /**
     * Retrieve a personal profile by personnel/serial/unique employee number of the individual.
     *
     * @param serverName name of server instance to call
     * @param userId the name of the calling user.
     * @param employeeNumber personnel/serial/unique employee number of the individual.
     * @return personal profile object or
     * InvalidParameterException the employee number is null or
     * EmployeeNumberNotUniqueException more than one personal profile was found or
     * PropertyServerException the server is not available or
     * UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    @GetMapping(path = "/personal-profiles/by-employee-number/{employeeNumber}")

    public PersonalProfileResponse getPersonalProfileByEmployeeNumber(@PathVariable String         serverName,
                                                                      @PathVariable String         userId,
                                                                      @PathVariable String         employeeNumber)
    {
        return restAPI.getPersonalProfileByQualifiedName(serverName, userId, employeeNumber);
    }


    /**
     * Return a list of candidate personal profiles for an individual.  It matches on full name and known name.
     * The name may include wild card parameters.
     *
     * @param serverName name of server instance to call
     * @param userId the name of the calling user.
     * @param name name of individual.
     * @param startFrom scan pointer
     * @param pageSize maximum number of results
     * @return list of personal profile objects or
     * InvalidParameterException the name is null or
     * PropertyServerException the server is not available or
     * UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    @GetMapping(path = "/personal-profiles/by-name/{name}")

    public PersonalProfileListResponse getPersonalProfilesByName(@PathVariable String        serverName,
                                                                 @PathVariable String        userId,
                                                                 @PathVariable String        name,
                                                                 @RequestParam int           startFrom,
                                                                 @RequestParam int           pageSize)
    {
        return restAPI.getPersonalProfilesByName(serverName, userId, name, startFrom, pageSize);
    }
}
