/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceprogram.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.accessservices.governanceprogram.rest.*;
import org.odpi.openmetadata.accessservices.governanceprogram.server.GovernanceProgramRESTServices;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.springframework.web.bind.annotation.*;

/**
 * The GovernanceLeadershipResource provides a Spring based server-side REST API
 * that supports the GovernanceLeadershipInterface.   It delegates each request to the
 * GovernanceProgramRESTServices.  This provides the server-side implementation of the Governance Program Open Metadata
 * Assess Service (OMAS) which is used to manage the full lifecycle of a governance program.
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/access-services/governance-program/users/{userId}/leadership")

@Tag(name="Governance Program OMAS", description="The Governance Program OMAS provides APIs and events for tools and applications focused on defining a data strategy, planning support for a regulation and/or developing a governance program for the data landscape." +
        "\n", externalDocs=@ExternalDocumentation(description="Governance Program Open Metadata Access Service (OMAS)",url="https://egeria.odpi.org/open-metadata-implementation/access-services/governance-program/"))

public class GovernanceLeadershipResource
{
    private GovernanceProgramRESTServices  restAPI = new GovernanceProgramRESTServices();

    /**
     * Default constructor
     */
    public GovernanceLeadershipResource()
    {
    }


    /**
     * Create the governance officer appointment.
     *
     * @param serverName name of server instance to call
     * @param userId the name of the calling user.
     * @param requestBody  properties of the governance officer.
     * @return Unique identifier (guid) of the governance officer or
     * InvalidParameterException the governance domain or appointment id is null or
     * PropertyServerException the server is not available or
     * UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    @PostMapping(path = "/governance-officers")

    public GUIDResponse createGovernanceOfficer(@PathVariable String                               serverName,
                                                @PathVariable String                               userId,
                                                @RequestBody  GovernanceOfficerDetailsRequestBody  requestBody)

    {
        return restAPI.createGovernanceOfficer(serverName, userId, requestBody);
    }


    /**
     * Update selected fields for the governance officer.
     *
     * @param serverName name of server instance to call
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
    @PostMapping(path = "/governance-officers/{governanceOfficerGUID}")

    public VoidResponse   updateGovernanceOfficer(@PathVariable String                               serverName,
                                                  @PathVariable String                               userId,
                                                  @PathVariable String                               governanceOfficerGUID,
                                                  @RequestBody  GovernanceOfficerDetailsRequestBody  requestBody)
    {
        return restAPI.updateGovernanceOfficer(serverName, userId, governanceOfficerGUID, requestBody);
    }


    /**
     * Remove the requested governance officer.
     *
     * @param serverName name of server instance to call
     * @param userId the name of the calling user.
     * @param governanceOfficerGUID unique identifier (guid) of the governance officer.
     * @param requestBody  properties to verify this is the right governance officer
     * @return void response or
     * UnrecognizedGUIDException the unique identifier of the governance officer is either null or invalid or
     * InvalidParameterException the appointmentId or governance domain is either null or invalid or
     * PropertyServerException the server is not available or
     * UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    @PostMapping(path = "/governance-officers/{governanceOfficerGUID}/delete")

    public VoidResponse   deleteGovernanceOfficer(@PathVariable String                                 serverName,
                                                  @PathVariable String                                 userId,
                                                  @PathVariable String                                 governanceOfficerGUID,
                                                  @RequestBody  GovernanceOfficerValidatorRequestBody  requestBody)
    {
        return restAPI.deleteGovernanceOfficer(serverName, userId, governanceOfficerGUID, requestBody);
    }


    /**
     * Retrieve a governance officer description by unique guid.
     *
     * @param serverName name of server instance to call
     * @param userId the name of the calling user.
     * @param governanceOfficerGUID unique identifier (guid) of the governance officer.
     * @return governance officer object or
     * UnrecognizedGUIDException the unique identifier of the governance officer is either null or invalid or
     * PropertyServerException the server is not available or
     * UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    @GetMapping(path = "/governance-officers/{governanceOfficerGUID}")

    public GovernanceOfficerResponse getGovernanceOfficerByGUID(@PathVariable String     serverName,
                                                                @PathVariable String     userId,
                                                                @PathVariable String     governanceOfficerGUID)
    {
        return restAPI.getGovernanceOfficerByGUID(serverName, userId, governanceOfficerGUID);
    }


    /**
     * Retrieve a governance officer by unique appointment id.
     *
     * @param serverName name of server instance to call
     * @param userId the name of the calling user.
     * @param appointmentId  the unique appointment identifier of the governance officer.
     * @return governance officer object or
     * InvalidParameterException the appointmentId or governance domain is either null or invalid or
     * AppointmentIdNotUniqueException more than one governance officer entity was retrieved for this appointmentId
     * PropertyServerException the server is not available or
     * UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    @GetMapping(path = "/governance-officers/by-appointment-id/{appointmentId}")

    public GovernanceOfficerResponse   getGovernanceOfficerByAppointmentId(@PathVariable String     serverName,
                                                                           @PathVariable String     userId,
                                                                           @PathVariable String     appointmentId)
    {
        return restAPI.getGovernanceOfficerByAppointmentId(serverName, userId, appointmentId);
    }


    /**
     * Return all of the defined governance officers.
     *
     * @param serverName name of server instance to call
     * @param userId the name of the calling user.
     * @return list of governance officer objects or
     * PropertyServerException the server is not available or
     * UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    @GetMapping(path = "/governance-officers")

    public GovernanceOfficerListResponse  getGovernanceOfficers(@PathVariable String     serverName,
                                                                @PathVariable String     userId)
    {
        return restAPI.getGovernanceOfficers(serverName, userId);
    }


    /**
     * Return all of the active governance officers.
     *
     * @param serverName name of server instance to call
     * @param userId the name of the calling user.
     * @return list of governance officer objects or
     * PropertyServerException the server is not available or
     * UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    @GetMapping(path = "/governance-officers/active")

    public GovernanceOfficerListResponse  getActiveGovernanceOfficers(@PathVariable String     serverName,
                                                                      @PathVariable String     userId)
    {
        return restAPI.getActiveGovernanceOfficers(serverName, userId);
    }



    /**
     * Return all of the defined governance officers for a specific governance domain.  In a small organization
     * there is typically only one governance officer.   However a large organization may have multiple governance
     * officers, each with a different scope.  The governance officer with a null scope is the overall leader.
     *
     * @param serverName name of server instance to call
     * @param userId the name of the calling user.
     * @param requestBody domain of interest
     * @return list of governance officer objects or
     * InvalidParameterException the governance domain is either null or invalid or
     * PropertyServerException the server is not available or
     * UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    @PostMapping(path = "/governance-officers/by-domain")

    public GovernanceOfficerListResponse  getGovernanceOfficersByDomain(@PathVariable String                        serverName,
                                                                        @PathVariable String                        userId,
                                                                        @RequestBody  GovernanceDomainRequestBody   requestBody)
    {
        return restAPI.getGovernanceOfficersByDomain(serverName, userId, requestBody);
    }


    /**
     * Link a person to a governance officer.  Only one person may be appointed at any one time.
     *
     * @param serverName name of server instance to call
     * @param userId the name of the calling user.
     * @param governanceOfficerGUID unique identifier (guid) of the governance officer.
     * @param requestBody unique identifier for the profile and start date.
     * @return void response or
     * UnrecognizedGUIDException the unique identifier of the governance officer or profile is either null or invalid or
     * PropertyServerException the server is not available or
     * UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    @PostMapping(path = "/governance-officers/{governanceOfficerGUID}/appoint")

    public VoidResponse appointGovernanceOfficer(@PathVariable String                  serverName,
                                                 @PathVariable String                  userId,
                                                 @PathVariable String                  governanceOfficerGUID,
                                                 @RequestBody  AppointmentRequestBody  requestBody)
    {
        return restAPI.appointGovernanceOfficer(serverName, userId, governanceOfficerGUID, requestBody);
    }


    /**
     * Unlink a person from a governance officer appointment.
     *
     * @param serverName name of server instance to call
     * @param userId the name of the calling user.
     * @param governanceOfficerGUID unique identifier (guid) of the governance officer.
     * @param requestBody unique identifier for the profile and end date.
     * @return void response or
     * UnrecognizedGUIDException the unique identifier of the governance officer or profile is either null or invalid or
     * InvalidParameterException the profile is not linked to this governance officer or
     * PropertyServerException the server is not available or
     * UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    @PostMapping(path = "/governance-officers/{governanceOfficerGUID}/relieve")

    public VoidResponse relieveGovernanceOfficer(@PathVariable String                  serverName,
                                                 @PathVariable String                  userId,
                                                 @PathVariable String                  governanceOfficerGUID,
                                                 @RequestBody  AppointmentRequestBody  requestBody)
    {
        return restAPI.relieveGovernanceOfficer(serverName, userId, governanceOfficerGUID, requestBody);
    }
}
