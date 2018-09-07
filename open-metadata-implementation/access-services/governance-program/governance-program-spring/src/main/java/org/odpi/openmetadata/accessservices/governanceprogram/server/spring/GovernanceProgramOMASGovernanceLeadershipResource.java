/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.governanceprogram.server.spring;

import org.odpi.openmetadata.accessservices.governanceprogram.rest.*;
import org.odpi.openmetadata.accessservices.governanceprogram.server.GovernanceProgramRESTServices;
import org.springframework.web.bind.annotation.*;

/**
 * The GovernanceProgramOMASGovernanceLeadershipResource provides a Spring based server-side REST API
 * that supports the GovernanceLeadershipInterface.   It delegates each request to the
 * GovernanceProgramRESTServices.  This provides the server-side implementation of the Governance Program Open Metadata
 * Assess Service (OMAS) which is used to manage the full lifecycle of a governance program.
 */
@RestController
@RequestMapping("/open-metadata/access-services/governance-program/users/{userId}/leadership")

public class GovernanceProgramOMASGovernanceLeadershipResource
{
    private GovernanceProgramRESTServices  restAPI = new GovernanceProgramRESTServices();

    /**
     * Default constructor
     */
    public GovernanceProgramOMASGovernanceLeadershipResource()
    {
    }


    /**
     * Create a personal profile for an individual who is to be appointed to a governance role but does not
     * have a profile in open metadata.
     *
     * @param userId the name of the calling user.
     * @param requestBody properties about the individual.
     * @return Unique identifier for the personal profile or
     * InvalidParameterException the employee number or full name is null or
     * PropertyServerException the server is not available or
     * UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    @RequestMapping(method = RequestMethod.POST, path = "/personal-profiles")

    public GUIDResponse createPersonalProfile(@PathVariable String                     userId,
                                              @RequestBody  PersonalDetailsRequestBody requestBody)
    {
        return restAPI.createPersonalProfile(userId, requestBody);
    }


    /**
     * Update properties for the personal properties.  Null values result in empty fields in the profile.
     *
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

    public VoidResponse   updatePersonalProfile(@PathVariable String                     userId,
                                                @PathVariable String                     profileGUID,
                                                @RequestBody  PersonalDetailsRequestBody requestBody)
    {
        return restAPI.updatePersonalProfile(userId, profileGUID, requestBody);
    }


    /**
     * Delete the personal profile.
     *
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

    public VoidResponse   deletePersonalProfile(@PathVariable String                              userId,
                                                @PathVariable String                              profileGUID,
                                                @RequestBody  PersonalProfileValidatorRequestBody requestBody)
    {
        return restAPI.deletePersonalProfile(userId, profileGUID, requestBody);
    }


    /**
     * Retrieve a personal profile by guid.
     *
     * @param userId the name of the calling user.
     * @param profileGUID unique identifier for the profile.
     * @return personal profile object or
     * UnrecognizedGUIDException the unique identifier of the personal profile is either null or invalid or
     * PropertyServerException the server is not available or
     * UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    @RequestMapping(method = RequestMethod.GET, path = "/personal-profiles/{profileGUID}")

    public PersonalProfileResponse getPersonalProfileByGUID(@PathVariable String        userId,
                                                            @PathVariable String        profileGUID)
    {
        return restAPI.getPersonalProfileByGUID(userId, profileGUID);
    }


    /**
     * Retrieve a personal profile by personnel/serial/unique employee number of the individual.
     *
     * @param userId the name of the calling user.
     * @param employeeNumber personnel/serial/unique employee number of the individual.
     * @return personal profile object or
     * InvalidParameterException the employee number is null or
     * EmployeeNumberNotUniqueException more than one personal profile was found or
     * PropertyServerException the server is not available or
     * UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    @RequestMapping(method = RequestMethod.GET, path = "/personal-profiles/by-employee-number/{employeeNumber}")

    public PersonalProfileResponse getPersonalProfileByEmployeeNumber(@PathVariable String         userId,
                                                                      @PathVariable String         employeeNumber)
    {
        return restAPI.getPersonalProfileByEmployeeNumber(userId, employeeNumber);
    }


    /**
     * Return a list of candidate personal profiles for an individual.  It matches on full name and known name.
     * The name may include wild card parameters.
     *
     * @param userId the name of the calling user.
     * @param name name of individual.
     * @return list of personal profile objects or
     * InvalidParameterException the name is null or
     * PropertyServerException the server is not available or
     * UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    @RequestMapping(method = RequestMethod.GET, path = "/personal-profiles/by-name/{name}")

    public PersonalProfileListResponse getPersonalProfilesByName(@PathVariable String        userId,
                                                                 @PathVariable String        name)
    {
        return restAPI.getPersonalProfilesByName(userId, name);
    }


    /**
     * Create the governance officer appointment.
     *
     * @param userId the name of the calling user.
     * @param requestBody  properties of the governance officer.
     * @return Unique identifier (guid) of the governance officer or
     * InvalidParameterException the governance domain or appointment id is null or
     * PropertyServerException the server is not available or
     * UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    @RequestMapping(method = RequestMethod.POST, path = "/governance-officers")

    public GUIDResponse createGovernanceOfficer(@PathVariable String                               userId,
                                                @RequestBody  GovernanceOfficerDetailsRequestBody  requestBody)

    {
        return restAPI.createGovernanceOfficer(userId, requestBody);
    }


    /**
     * Update selected fields for the governance officer.
     *
     * @param userId the name of the calling user.
     * @param governanceOfficerGUID unique identifier (guid) of the governance officer.
     * @param requestBody  properties of the governance officer
     * @return void response or
     * UnrecognizedGUIDException the unique identifier of the governance officer is either null or invalid or
     * InvalidParameterException the title is null or the governanceDomain/appointmentId does not match the
     *                                   the existing values associated with the governanceOfficerGUID or
     * PropertyServerException the server is not available or
     * UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    @RequestMapping(method = RequestMethod.POST, path = "/governance-officers/{governanceOfficerGUID}")

    public VoidResponse   updateGovernanceOfficer(@PathVariable String                               userId,
                                                  @PathVariable String                               governanceOfficerGUID,
                                                  @RequestBody  GovernanceOfficerDetailsRequestBody  requestBody)
    {
        return restAPI.updateGovernanceOfficer(userId, governanceOfficerGUID, requestBody);
    }


    /**
     * Remove the requested governance officer.
     *
     * @param userId the name of the calling user.
     * @param governanceOfficerGUID unique identifier (guid) of the governance officer.
     * @param requestBody  properties to verify this is the right governance officer
     * @return void response or
     * UnrecognizedGUIDException the unique identifier of the governance officer is either null or invalid or
     * InvalidParameterException the appointmentId or governance domain is either null or invalid or
     * PropertyServerException the server is not available or
     * UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    @RequestMapping(method = RequestMethod.POST, path = "/governance-officers/{governanceOfficerGUID}/delete")

    public VoidResponse   deleteGovernanceOfficer(@PathVariable String                                 userId,
                                                  @PathVariable String                                 governanceOfficerGUID,
                                                  @RequestBody  GovernanceOfficerValidatorRequestBody  requestBody)
    {
        return restAPI.deleteGovernanceOfficer(userId, governanceOfficerGUID, requestBody);
    }


    /**
     * Retrieve a governance officer description by unique guid.
     *
     * @param userId the name of the calling user.
     * @param governanceOfficerGUID unique identifier (guid) of the governance officer.
     * @return governance officer object or
     * UnrecognizedGUIDException the unique identifier of the governance officer is either null or invalid or
     * PropertyServerException the server is not available or
     * UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    @RequestMapping(method = RequestMethod.GET, path = "/governance-officers/{governanceOfficerGUID}")

    public GovernanceOfficerResponse getGovernanceOfficerByGUID(@PathVariable String     userId,
                                                                @PathVariable String     governanceOfficerGUID)
    {
        return restAPI.getGovernanceOfficerByGUID(userId, governanceOfficerGUID);
    }


    /**
     * Retrieve a governance officer by unique appointment id.
     *
     * @param userId the name of the calling user.
     * @param appointmentId  the unique appointment identifier of the governance officer.
     * @return governance officer object or
     * InvalidParameterException the appointmentId or governance domain is either null or invalid or
     * AppointmentIdNotUniqueException more than one governance officer entity was retrieved for this appointmentId
     * PropertyServerException the server is not available or
     * UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    @RequestMapping(method = RequestMethod.GET, path = "/governance-officers/by-appointment-id/{appointmentId}")

    public GovernanceOfficerResponse   getGovernanceOfficerByAppointmentId(@PathVariable String     userId,
                                                                           @PathVariable String     appointmentId)
    {
        return restAPI.getGovernanceOfficerByAppointmentId(userId, appointmentId);
    }


    /**
     * Return all of the defined governance officers.
     *
     * @param userId the name of the calling user.
     * @return list of governance officer objects or
     * PropertyServerException the server is not available or
     * UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    @RequestMapping(method = RequestMethod.GET, path = "/governance-officers")

    public GovernanceOfficerListResponse  getGovernanceOfficers(@PathVariable String     userId)
    {
        return restAPI.getGovernanceOfficers(userId);
    }


    /**
     * Return all of the active governance officers.
     *
     * @param userId the name of the calling user.
     * @return list of governance officer objects or
     * PropertyServerException the server is not available or
     * UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    @RequestMapping(method = RequestMethod.GET, path = "/governance-officers/active")

    public GovernanceOfficerListResponse  getActiveGovernanceOfficers(@PathVariable String     userId)
    {
        return restAPI.getActiveGovernanceOfficers(userId);
    }



    /**
     * Return all of the defined governance officers for a specific governance domain.  In a small organization
     * there is typically only one governance officer.   However a large organization may have multiple governance
     * officers, each with a different scope.  The governance officer with a null scope is the overall leader.
     *
     * @param userId the name of the calling user.
     * @param requestBody domain of interest
     * @return list of governance officer objects or
     * InvalidParameterException the governance domain is either null or invalid or
     * PropertyServerException the server is not available or
     * UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    @RequestMapping(method = RequestMethod.POST, path = "/governance-officers/by-domain")

    public GovernanceOfficerListResponse  getGovernanceOfficersByDomain(@PathVariable String                        userId,
                                                                        @RequestBody  GovernanceDomainRequestBody   requestBody)
    {
        return restAPI.getGovernanceOfficersByDomain(userId, requestBody);
    }


    /**
     * Link a person to a governance officer.  Only one person may be appointed at any one time.
     *
     * @param userId the name of the calling user.
     * @param governanceOfficerGUID unique identifier (guid) of the governance officer.
     * @param requestBody unique identifier for the profile and start date.
     * @return void response or
     * UnrecognizedGUIDException the unique identifier of the governance officer or profile is either null or invalid or
     * PropertyServerException the server is not available or
     * UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    @RequestMapping(method = RequestMethod.POST, path = "/governance-officers/{governanceOfficerGUID}/appoint")

    public VoidResponse appointGovernanceOfficer(@PathVariable String                  userId,
                                                 @PathVariable String                  governanceOfficerGUID,
                                                 @RequestBody  AppointmentRequestBody  requestBody)
    {
        return restAPI.appointGovernanceOfficer(userId, governanceOfficerGUID, requestBody);
    }


    /**
     * Unlink a person from a governance officer appointment.
     *
     * @param userId the name of the calling user.
     * @param governanceOfficerGUID unique identifier (guid) of the governance officer.
     * @param requestBody unique identifier for the profile and end date.
     * @return void response or
     * UnrecognizedGUIDException the unique identifier of the governance officer or profile is either null or invalid or
     * InvalidParameterException the profile is not linked to this governance officer or
     * PropertyServerException the server is not available or
     * UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    @RequestMapping(method = RequestMethod.POST, path = "/governance-officers/{governanceOfficerGUID}/relieve")

    public VoidResponse relieveGovernanceOfficer(@PathVariable String                  userId,
                                                 @PathVariable String                  governanceOfficerGUID,
                                                 @RequestBody  AppointmentRequestBody  requestBody)
    {
        return restAPI.relieveGovernanceOfficer(userId, governanceOfficerGUID, requestBody);
    }
}
