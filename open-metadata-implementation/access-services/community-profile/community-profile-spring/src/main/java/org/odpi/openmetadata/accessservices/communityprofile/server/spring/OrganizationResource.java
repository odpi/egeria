/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;

import org.odpi.openmetadata.accessservices.communityprofile.server.OrganizationRESTServices;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * The OrganizationResource provides a Spring based server-side REST API
 * that supports the OrganizationManagementInterface.   It delegates each request to the
 * OrganizationRESTServices.  This provides the server-side implementation of the Community Profile Open Metadata
 * Assess Service (OMAS) which is used to manage information about people, roles and organizations.
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/access-services/community-profile/users/{userId}")

@Tag(name="Metadata Access Server: Community Profile OMAS", description="The Community Profile OMAS provides APIs and events for tools and applications that are managing information about people and the way they work together.",
     externalDocs=@ExternalDocumentation(description="Further Information",
                                         url="https://egeria-project.org/services/omas/community-profile/overview/"))

public class OrganizationResource
{
    private final OrganizationRESTServices restAPI = new OrganizationRESTServices();

    /**
     * Default constructor
     */
    public OrganizationResource()
    {
    }


    /**
     * Create a definition of an actor profile.  This could be for the whole organization, a team, a person or a system.
     *
     * @param serverName called server
     * @param userId calling user
     * @param requestBody properties for a actor profile
     *
     * @return unique identifier of actor profile
     *
     *   InvalidParameterException qualifiedName or userId is null; qualifiedName is not unique
     *   PropertyServerException problem accessing property server
     *   UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/profiles")

    public GUIDResponse createActorProfile(@PathVariable String                  serverName,
                                           @PathVariable String                  userId,
                                           @RequestBody  ActorProfileRequestBody requestBody)
    {
        return restAPI.createActorProfile(serverName, userId, requestBody);
    }


    /**
     * Update the definition of an actor profile.
     *
     * @param serverName called server
     * @param userId calling user
     * @param actorProfileGUID unique identifier of profile to update
     * @param isMergeUpdate are unspecified properties unchanged (true) or replaced with null?
     * @param requestBody properties to change
     *
     * @return void or
     *
     *   InvalidParameterException guid, qualifiedName or userId is null; qualifiedName is not unique; guid is not known
     *   PropertyServerException problem accessing property server
     *   UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/profiles/{actorProfileGUID}")

    public VoidResponse updateActorProfile(@PathVariable String                  serverName,
                                           @PathVariable String                  userId,
                                           @PathVariable String                  actorProfileGUID,
                                           @RequestParam boolean                 isMergeUpdate,
                                           @RequestBody  ActorProfileRequestBody requestBody)
    {
        return restAPI.updateActorProfile(serverName, userId, actorProfileGUID, isMergeUpdate, requestBody);
    }


    /**
     * Remove the definition of an actor profile.
     *
     * @param serverName called server
     * @param userId calling user
     * @param actorProfileGUID unique identifier of actor profile
     * @param requestBody   identifiers of the software server capability entity that represented the external source - null for local
     *
     * @return void or
     *
     *   InvalidParameterException guid or userId is null; guid is not known
     *   PropertyServerException problem accessing property server
     *   UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/profiles/{actorProfileGUID}/delete")

    public VoidResponse deleteActorProfile(@PathVariable String                    serverName,
                                           @PathVariable String                    userId,
                                           @PathVariable String                    actorProfileGUID,
                                           @RequestBody ExternalSourceRequestBody requestBody)
    {
        return restAPI.deleteActorProfile(serverName, userId, actorProfileGUID, requestBody);
    }


    /**
     * Add a new contact method to the profile.
     *
     * @param serverName called server
     * @param userId the name of the calling user.
     * @param actorProfileGUID identifier of the profile to update.
     * @param requestBody properties of contact method.
     *
     * @return unique identifier (guid) for the new contact method.
     *
     *   InvalidParameterException the userId is null or invalid.  Another property is invalid.
     *   PropertyServerException there is a problem retrieving information from the property server(s).
     *   UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/profiles/{actorProfileGUID}/contact-methods")

    public GUIDResponse addContactMethod(@PathVariable String                   serverName,
                                         @PathVariable String                   userId,
                                         @PathVariable String                   actorProfileGUID,
                                         @RequestBody  ContactMethodRequestBody requestBody)
    {
        return restAPI.addContactMethod(serverName, userId, actorProfileGUID, requestBody);
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
                                            @RequestBody ExternalSourceRequestBody requestBody)
    {
        return restAPI.deleteContactMethod(serverName, userId, contactMethodGUID, requestBody);
    }


    /**
     * Link two related team/organization actor profiles together as part of a hierarchy.
     * A team/organization actor profile can only have one parent but many child actor profiles.
     *
     * @param serverName called server
     * @param userId calling user
     * @param superTeamProfileGUID unique identifier of the parent team profile
     * @param subTeamProfileGUID unique identifier of the child team profile
     * @param delegationEscalationAuthority can workflows delegate/escalate through this link?
     * @param requestBody   identifiers of the software server capability entity that represented the external source - null for local
     *
     * @return void or
     *
     *   InvalidParameterException one of the guids is null or not known
     *   PropertyServerException problem accessing property server
     *   UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/profiles/{superTeamProfileGUID}/sub-team-profiles/{subTeamProfileGUID}/link")

    public VoidResponse linkTeamsInHierarchy(@PathVariable String                    serverName,
                                             @PathVariable String                    userId,
                                             @PathVariable String                    superTeamProfileGUID,
                                             @PathVariable String                    subTeamProfileGUID,
                                             @RequestParam boolean                   delegationEscalationAuthority,
                                             @RequestBody  EffectiveDatesRequestBody requestBody)
    {
        return restAPI.linkTeamsInHierarchy(serverName, userId, superTeamProfileGUID, subTeamProfileGUID, delegationEscalationAuthority, requestBody);
    }


    /**
     * Remove the link between two actor profiles in the actor profile hierarchy.
     *
     * @param serverName called server
     * @param userId calling user
     * @param superTeamProfileGUID unique identifier of the parent actor profile
     * @param subTeamProfileGUID unique identifier of the child actor profile
     * @param requestBody   identifiers of the software server capability entity that represented the external source - null for local
     *
     * @return void or
     *
     *   InvalidParameterException one of the guids is null or not known
     *   PropertyServerException problem accessing property server
     *   UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/profiles/{superTeamProfileGUID}/sub-team-profiles/{subTeamProfileGUID}/unlink")

    public VoidResponse unlinkTeamsInHierarchy(@PathVariable String                    serverName,
                                               @PathVariable String                    userId,
                                               @PathVariable String                    superTeamProfileGUID,
                                               @PathVariable String                    subTeamProfileGUID,
                                               @RequestBody ExternalSourceRequestBody requestBody)
    {
        return restAPI.unlinkTeamsInHierarchy(serverName, userId, superTeamProfileGUID, subTeamProfileGUID, requestBody);
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
     * @param actorProfileUserId unique identifier for the userId
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
     * Return all actor profiles.
     *
     * @param serverName called server
     * @param userId calling user
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return list of the actor profiles
     *
     *   InvalidParameterException actorProfileUserId or userId is null
     *   PropertyServerException problem accessing property server
     *   UserNotAuthorizedException security access problem
     */
    @GetMapping(path = "/profiles")

    public ActorProfilesResponse getActorProfiles(@PathVariable String serverName,
                                                  @PathVariable String userId,
                                                  @RequestParam int    startFrom,
                                                  @RequestParam int    pageSize)
    {
        return restAPI.getActorProfiles(serverName, userId, startFrom, pageSize);
    }


    /**
     * Return information about a specific actor profile.
     *
     * @param serverName called server
     * @param userId calling user
     * @param locationGUID unique identifier for the location
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return properties of the actor profile
     *
     *   InvalidParameterException actorProfileUserId or userId is null
     *   PropertyServerException problem accessing property server
     *   UserNotAuthorizedException security access problem
     */
    @GetMapping(path = "/profiles/locations/{locationGUID}")

    public ActorProfilesResponse getActorProfilesByLocation(@PathVariable String serverName,
                                                            @PathVariable String userId,
                                                            @PathVariable String locationGUID,
                                                            @RequestParam int    startFrom,
                                                            @RequestParam int    pageSize)
    {
        return restAPI.getActorProfilesByLocation(serverName, userId, locationGUID, startFrom, pageSize);
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

    public ActorProfilesResponse getActorProfilesByName(@PathVariable String          serverName,
                                                        @PathVariable String          userId,
                                                        @RequestParam int             startFrom,
                                                        @RequestParam int             pageSize,
                                                        @RequestBody  NameRequestBody requestBody)
    {
        return restAPI.getActorProfilesByName(serverName, userId, startFrom, pageSize, requestBody);
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
        return restAPI.findActorProfiles(serverName, userId, startFrom, pageSize, requestBody);
    }


    /**
     * Create a definition of a person role.
     *
     * @param serverName called server
     * @param userId calling user
     * @param requestBody properties for a person role
     *
     * @return unique identifier of person role
     *
     *   InvalidParameterException qualifiedName or userId is null; qualifiedName is not unique
     *   PropertyServerException problem accessing property server
     *   UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/person-roles")

    public GUIDResponse createPersonRole(@PathVariable String                serverName,
                                         @PathVariable String                userId,
                                         @RequestBody  PersonRoleRequestBody requestBody)
    {
        return restAPI.createPersonRole(serverName, userId, requestBody);
    }


    /**
     * Update the definition of a person role.
     *
     * @param serverName called server
     * @param userId calling user
     * @param personRoleGUID unique identifier of person role
     * @param isMergeUpdate are unspecified properties unchanged (true) or replaced with null?
     * @param requestBody properties to change
     *
     * @return void or
     *
     *   InvalidParameterException guid, qualifiedName or userId is null; qualifiedName is not unique; guid is not known
     *   PropertyServerException problem accessing property server
     *   UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/person-roles/{personRoleGUID}")

    public VoidResponse updatePersonRole(@PathVariable String                serverName,
                                         @PathVariable String                userId,
                                         @PathVariable String                personRoleGUID,
                                         @RequestParam boolean               isMergeUpdate,
                                         @RequestBody  PersonRoleRequestBody requestBody)
    {
        return restAPI.updatePersonRole(serverName, userId, personRoleGUID, isMergeUpdate, requestBody);
    }


    /**
     * Remove the definition of a person role.
     *
     * @param serverName called server
     * @param userId calling user
     * @param personRoleGUID unique identifier of person role
     * @param requestBody   identifiers of the software server capability entity that represented the external source - null for local
     *
     * @return void or
     *
     *   InvalidParameterException guid or userId is null; guid is not known
     *   PropertyServerException problem accessing property server
     *   UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/person-roles/{personRoleGUID}/delete")

    public VoidResponse deletePersonRole(@PathVariable String                    serverName,
                                         @PathVariable String                    userId,
                                         @PathVariable String                    personRoleGUID,
                                         @RequestBody ExternalSourceRequestBody requestBody)
    {
        return restAPI.deletePersonRole(serverName, userId, personRoleGUID, requestBody);
    }


    /**
     * Link a person role to a person profile to show that that person is performing the role.
     *
     * @param serverName called server
     * @param userId calling user
     * @param personRoleGUID unique identifier of the person role
     * @param personProfileGUID unique identifier of the person profile
     * @param requestBody   identifiers of the software server capability entity that represented the external source - null for local
     *
     * @return appointment unique identifier or
     *
     *   InvalidParameterException one of the guids is null or not known
     *   PropertyServerException problem accessing property server
     *   UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/person-roles/{personRoleGUID}/profiles/{personProfileGUID}/link")

    public GUIDResponse linkPersonRoleToProfile(@PathVariable String                 serverName,
                                                @PathVariable String                 userId,
                                                @PathVariable String                 personRoleGUID,
                                                @PathVariable String                 personProfileGUID,
                                                @RequestBody  AppointmentRequestBody requestBody)
    {
        return restAPI.linkPersonRoleToProfile(serverName, userId, personRoleGUID, personProfileGUID, requestBody);
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
     * Update the properties for the appointment of a person to a role.
     *
     * @param serverName called server
     * @param userId calling user
     * @param appointmentGUID unique identifier of the appointment relationship
     * @param isMergeUpdate are unspecified properties unchanged (true) or replaced with null?
     * @param requestBody properties to change
     *
     * @return void or
     *  InvalidParameterException one of the guids is null or not known
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/person-roles/appointees/{appointmentGUID}")

    public VoidResponse updateAppointment(@PathVariable String                 serverName,
                                          @PathVariable String                 userId,
                                          @PathVariable String                 appointmentGUID,
                                          @RequestParam boolean                isMergeUpdate,
                                          @RequestBody  AppointmentRequestBody requestBody)
    {
        return restAPI.updateAppointment(serverName, userId, appointmentGUID, isMergeUpdate, requestBody);
    }


    /**
     * Remove the link between a person role and a person profile.
     *
     * @param serverName called server
     * @param userId calling user
     * @param appointmentGUID unique identifier of the appointment relationship
     * @param requestBody   identifiers of the software server capability entity that represented the external source - null for local
     *
     * @return void or
     *
     *   InvalidParameterException one of the guids is null or not known
     *   PropertyServerException problem accessing property server
     *   UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/person-roles/appointees/{appointmentGUID}/unlink")

    public VoidResponse unlinkPersonRoleFromProfile(@PathVariable String                    serverName,
                                                    @PathVariable String                    userId,
                                                    @PathVariable String                    appointmentGUID,
                                                    @RequestBody ExternalSourceRequestBody requestBody)
    {
        return restAPI.unlinkPersonRoleFromProfile(serverName, userId, appointmentGUID, requestBody);
    }


    /**
     * Link a team leader person role or team member person role to a team profile.
     *
     * @param serverName called server
     * @param userId calling user
     * @param teamRoleGUID unique identifier of the person role
     * @param teamProfileGUID unique identifier of the team profile
     * @param requestBody   identifiers of the software server capability entity that represented the external source - null for local
     *
     * @return void or
     *
     *   InvalidParameterException one of the guids is null or not known; the person role is not a team member or team leader
     *   PropertyServerException problem accessing property server
     *   UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/team-profiles/{teamProfileGUID}/team-roles/{teamRoleGUID}/link")

    public VoidResponse linkTeamPlayer(@PathVariable String               serverName,
                                       @PathVariable String               userId,
                                       @PathVariable String               teamRoleGUID,
                                       @PathVariable String               teamProfileGUID,
                                       @RequestBody TeamPlayerRequestBody requestBody)
    {
        return restAPI.linkTeamPlayer(serverName, userId, teamRoleGUID, teamProfileGUID, requestBody);
    }


    /**
     * Remove the link between a person role and a team profile.
     *
     * @param serverName called server
     * @param userId calling user
     * @param teamRoleGUID unique identifier of the person role
     * @param teamProfileGUID unique identifier of the team profile
     * @param requestBody   identifiers of the software server capability entity that represented the external source - null for local
     *
     * @return void or
     *
     *   InvalidParameterException one of the guids is null or not known; the person role is not a team member or team leader
     *   PropertyServerException problem accessing property server
     *   UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/team-profiles/{teamProfileGUID}/team-roles/{teamRoleGUID}/unlink")

    public VoidResponse unlinkTeamPlayer(@PathVariable String                serverName,
                                         @PathVariable String                userId,
                                         @PathVariable String                teamRoleGUID,
                                         @PathVariable String                teamProfileGUID,
                                         @RequestBody  TeamPlayerRequestBody requestBody)
    {
        return restAPI.unlinkTeamPlayer(serverName, userId, teamRoleGUID, teamProfileGUID, requestBody);
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
     * @return list of matching person roles
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
     * Return information about a person role connected to the named team via the TeamLeadership relationship.
     *
     * @param serverName called server
     * @param userId calling user
     * @param teamGUID unique identifier for the Team actor profile
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return list of matching person roles
     *
     *   InvalidParameterException name or userId is null
     *   PropertyServerException problem accessing property server
     *   UserNotAuthorizedException security access problem
     */
    @GetMapping(path = "/person-roles/by-team/{teamGUID}/leadership")

    public PersonRolesResponse getLeadershipRolesForTeam(@PathVariable String serverName,
                                                         @PathVariable String userId,
                                                         @PathVariable String teamGUID,
                                                         @RequestParam int    startFrom,
                                                         @RequestParam int    pageSize)
    {
        return restAPI.getLeadershipRolesForTeam(serverName, userId, teamGUID, startFrom, pageSize);
    }


    /**
     * Return information about a person role connected to the named team via the TeamMembership relationship.
     *
     * @param serverName called server
     * @param userId calling user
     * @param teamGUID unique identifier for the Team actor profile
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return list of matching person roles
     *
     *   InvalidParameterException name or userId is null
     *   PropertyServerException problem accessing property server
     *   UserNotAuthorizedException security access problem
     */
    @GetMapping(path = "/person-roles/by-team/{teamGUID}/membership")

    public PersonRolesResponse getMembershipRolesForTeam(@PathVariable String serverName,
                                                         @PathVariable String userId,
                                                         @PathVariable String teamGUID,
                                                         @RequestParam int    startFrom,
                                                         @RequestParam int    pageSize)
    {
        return restAPI.getMembershipRolesForTeam(serverName, userId, teamGUID, startFrom, pageSize);
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
     * @return list of matching person roles
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
        return restAPI.findPersonRoles(serverName, userId, startFrom, pageSize, requestBody);
    }
}
