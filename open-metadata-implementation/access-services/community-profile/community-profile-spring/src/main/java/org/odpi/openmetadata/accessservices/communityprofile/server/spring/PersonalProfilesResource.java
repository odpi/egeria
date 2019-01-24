/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.server.spring;

import org.odpi.openmetadata.accessservices.communityprofile.rest.*;
import org.odpi.openmetadata.accessservices.communityprofile.server.PersonalProfilesRESTServices;
import org.springframework.web.bind.annotation.*;

/**
 * The PersonalProfilesResource provides a Spring based server-side REST API
 * that supports the GovernanceLeadershipInterface.   It delegates each request to the
 * GovernanceProgramRESTServices.  This provides the server-side implementation of the Governance Program Open Metadata
 * Assess Service (OMAS) which is used to manage the full lifecycle of a governance program.
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/access-services/community-profile/users/{userId}")

public class PersonalProfilesResource
{
    private PersonalProfilesRESTServices restAPI = new PersonalProfilesRESTServices();

    /**
     * Default constructor
     */
    public PersonalProfilesResource()
    {
    }


    /**
     *
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
    @RequestMapping(method = RequestMethod.POST, path = "/personal-profiles")

    public GUIDResponse createPersonalProfile(@PathVariable String                     serverName,
                                              @PathVariable String                     userId,
                                              @RequestBody PersonalProfileRequestBody requestBody)
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
    @RequestMapping(method = RequestMethod.POST, path = "/personal-profiles/{profileGUID}")

    public VoidResponse   updatePersonalProfile(@PathVariable String                     serverName,
                                                @PathVariable String                     userId,
                                                @PathVariable String                     profileGUID,
                                                @RequestBody PersonalProfileRequestBody requestBody)
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
    @RequestMapping(method = RequestMethod.POST, path = "/personal-profiles/{profileGUID}/delete")

    public VoidResponse deletePersonalProfile(@PathVariable String                              serverName,
                                              @PathVariable String                              userId,
                                              @PathVariable String                              profileGUID,
                                              @RequestBody PersonalProfileValidatorRequestBody requestBody)
    {
        return restAPI.deletePersonalProfile(serverName, userId, profileGUID, requestBody);
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
    @RequestMapping(method = RequestMethod.GET, path = "/personal-profiles/{profileGUID}")

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
     * QualifiedNameNotUniqueException more than one personal profile was found or
     * PropertyServerException the server is not available or
     * UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    @RequestMapping(method = RequestMethod.GET, path = "/personal-profiles/by-employee-number/{employeeNumber}")

    public PersonalProfileResponse getPersonalProfileByEmployeeNumber(@PathVariable String         serverName,
                                                                      @PathVariable String         userId,
                                                                      @PathVariable String         employeeNumber)
    {
        return restAPI.getPersonalProfileByEmployeeNumber(serverName, userId, employeeNumber);
    }


    /**
     * Return a list of candidate personal profiles for an individual.  It matches on full name and known name.
     * The name may include wild card parameters.
     *
     * @param serverName name of server instance to call
     * @param userId the name of the calling user.
     * @param name name of individual.
     * @return list of personal profile objects or
     * InvalidParameterException the name is null or
     * PropertyServerException the server is not available or
     * UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    @RequestMapping(method = RequestMethod.GET, path = "/personal-profiles/by-name/{name}")

    public PersonalProfileListResponse getPersonalProfilesByName(@PathVariable String        serverName,
                                                                 @PathVariable String        userId,
                                                                 @PathVariable String        name)
    {
        return restAPI.getPersonalProfilesByName(serverName, userId, name);
    }
}
