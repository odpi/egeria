/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceprogram.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.accessservices.governanceprogram.server.GovernanceRolesRESTServices;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.springframework.web.bind.annotation.*;

/**
 * The GovernanceRolesResource provides a Spring based server-side REST API
 * that supports the GovernanceRolesInterface.   It delegates each request to the
 * GovernanceRolesRESTServices.  This provides the server-side implementation of the Governance Program Open Metadata
 * Assess Service (OMAS) which is used to manage the full lifecycle of a governance program.
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/access-services/governance-program/users/{userId}")

@Tag(name="Metadata Access Server: Governance Program OMAS", description="The Governance Program OMAS provides APIs and events for tools and applications focused on defining a data strategy, planning support for a regulation and/or developing a governance program for the data landscape." +
        "\n",
     externalDocs=@ExternalDocumentation(description="Further Information",
                                         url="https://egeria-project.org/services/omas/governance-program/overview/"))

public class GovernanceRolesResource
{
    private final GovernanceRolesRESTServices restAPI = new GovernanceRolesRESTServices();

    /**
     * Default constructor
     */
    public GovernanceRolesResource()
    {
    }


    /**
     * Create the governance role appointment.
     *
     * @param serverName name of server instance to call
     * @param userId the name of the calling user.
     * @param requestBody  properties of the governance role.
     * @return Unique identifier (guid) of the governance role or
     * InvalidParameterException the governance domain or appointment id is null or
     * PropertyServerException the server is not available or
     * UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    @PostMapping(path = "/governance-roles")

    public GUIDResponse createGovernanceRole(@PathVariable String                    serverName,
                                             @PathVariable String                    userId,
                                             @RequestBody  GovernanceRoleRequestBody requestBody)

    {
        return restAPI.createGovernanceRole(serverName, userId, requestBody);
    }


    /**
     * Update selected fields for the governance role.
     *
     * @param serverName name of server instance to call
     * @param userId the name of the calling user.
     * @param governanceRoleGUID unique identifier (guid) of the governance role.
     * @param isMergeUpdate are unspecified properties unchanged (true) or replaced with null?
     * @param requestBody  properties of the governance role
     * @return void response or
     * UnrecognizedGUIDException the unique identifier of the governance role is either null or invalid or
     * InvalidParameterException the title is null or the governanceDomain/appointmentId does not match
     *                                   the existing values associated with the governanceRoleGUID or
     * PropertyServerException the server is not available or
     * UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    @PostMapping(path = "/governance-roles/{governanceRoleGUID}")

    public VoidResponse   updateGovernanceRole(@PathVariable String                    serverName,
                                               @PathVariable String                    userId,
                                               @PathVariable String                    governanceRoleGUID,
                                               @RequestParam boolean                   isMergeUpdate,
                                               @RequestBody  GovernanceRoleRequestBody requestBody)
    {
        return restAPI.updateGovernanceRole(serverName, userId, governanceRoleGUID, isMergeUpdate, requestBody);
    }



    /**
     * Link a governance role to a governance control that defines a governance responsibility that a person fulfils.
     *
     * @param serverName name of server instance to call
     * @param userId calling user
     * @param governanceRoleGUID unique identifier of the governance role
     * @param responsibilityGUID unique identifier of the governance responsibility control
     * @param requestBody  relationship request body
     *
     * @return void or
     *  InvalidParameterException one of the guids is null or not known
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/governance-roles/{governanceRoleGUID}/governance-responsibility/{responsibilityGUID}/link")

    public VoidResponse linkRoleToResponsibility(@PathVariable String          serverName,
                                                 @PathVariable String          userId,
                                                 @PathVariable String          governanceRoleGUID,
                                                 @PathVariable String          responsibilityGUID,
                                                 @RequestBody(required = false)
                                                               RelationshipRequestBody requestBody)
    {
        return restAPI.linkRoleToResponsibility(serverName, userId, governanceRoleGUID, responsibilityGUID, requestBody);
    }


    /**
     * Remove the link between a governance role and a governance responsibility.
     *
     * @param serverName name of server instance to call
     * @param userId calling user
     * @param governanceRoleGUID unique identifier of the governance role
     * @param responsibilityGUID unique identifier of the governance responsibility control
     * @param requestBody  relationship request body
     *
     * @return void or
     *  InvalidParameterException one of the guids is null or not known
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/governance-roles/{governanceRoleGUID}/governance-responsibility/{responsibilityGUID}/unlink")

    public VoidResponse unlinkRoleFromResponsibility(@PathVariable String          serverName,
                                                     @PathVariable String          userId,
                                                     @PathVariable String          governanceRoleGUID,
                                                     @PathVariable String          responsibilityGUID,
                                                     @RequestBody(required = false)
                                                                   RelationshipRequestBody requestBody)
    {
        return restAPI.unlinkRoleFromResponsibility(serverName, userId, governanceRoleGUID, responsibilityGUID, requestBody);
    }


    /**
     * Link a governance role to the description of a resource that the role is responsible for.
     *
     * @param serverName name of server instance to call
     * @param userId calling user
     * @param governanceRoleGUID unique identifier of the governance role
     * @param resourceGUID unique identifier of the resource description
     * @param requestBody  relationship request body
     *
     * @return void or
     *  InvalidParameterException one of the guids is null or not known
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/governance-roles/{governanceRoleGUID}/resource/{resourceGUID}/link")

    public VoidResponse linkRoleToResource(@PathVariable String          serverName,
                                           @PathVariable String          userId,
                                           @PathVariable String          governanceRoleGUID,
                                           @PathVariable String          resourceGUID,
                                           @RequestBody(required = false)
                                                         RelationshipRequestBody requestBody)
    {
        return restAPI.linkRoleToResource(serverName, userId, governanceRoleGUID, resourceGUID, requestBody);
    }


    /**
     * Remove the link between a governance role and a resource.
     *
     * @param serverName name of server instance to call
     * @param userId calling user
     * @param governanceRoleGUID unique identifier of the governance role
     * @param resourceGUID unique identifier of the resource description
     * @param requestBody  relationship request body
     *
     * @return void or
     *  InvalidParameterException one of the guids is null or not known
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/governance-roles/{governanceRoleGUID}/resource/{resourceGUID}/unlink")

    public VoidResponse unlinkRoleFromResource(@PathVariable String          serverName,
                                               @PathVariable String          userId,
                                               @PathVariable String          governanceRoleGUID,
                                               @PathVariable String          resourceGUID,
                                               @RequestBody(required = false)
                                                             RelationshipRequestBody requestBody)
    {
        return restAPI.unlinkRoleFromResource(serverName, userId, governanceRoleGUID, resourceGUID, requestBody);
    }


    /**
     * Remove the requested governance role.
     *
     * @param serverName name of server instance to call
     * @param userId the name of the calling user
     * @param governanceRoleGUID unique identifier (guid) of the governance role
     * @param requestBody  properties to verify this is the right governance role
     *
     * @return void response or
     * InvalidParameterException the governanceRoleGUID null or invalid or
     * PropertyServerException the server is not available or
     * UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    @PostMapping(path = "/governance-roles/{governanceRoleGUID}/delete")

    public VoidResponse   deleteGovernanceRole(@PathVariable String          serverName,
                                               @PathVariable String          userId,
                                               @PathVariable String          governanceRoleGUID,
                                               @RequestBody(required = false)
                                                             ExternalSourceRequestBody requestBody)
    {
        return restAPI.deleteGovernanceRole(serverName, userId, governanceRoleGUID, requestBody);
    }


    /**
     * Retrieve a governance role description by unique guid.
     *
     * @param serverName name of server instance to call
     * @param userId the name of the calling user
     * @param governanceRoleGUID unique identifier (guid) of the governance role
     *
     * @return governance role object or
     * InvalidParameterException the unique identifier of the governance role is either null or invalid or
     * PropertyServerException the server is not available or
     * UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    @GetMapping(path = "/governance-roles/{governanceRoleGUID}")

    public GovernanceRoleResponse getGovernanceRoleByGUID(@PathVariable String serverName,
                                                          @PathVariable String userId,
                                                          @PathVariable String governanceRoleGUID)
    {
        return restAPI.getGovernanceRoleByGUID(serverName, userId, governanceRoleGUID);
    }


    /**
     * Retrieve a governance role description by unique guid along with the history of who has been appointed
     * to the role.
     *
     * @param serverName name of server instance to call
     * @param userId the name of the calling user
     * @param governanceRoleGUID unique identifier (guid) of the governance role
     *
     * @return governance role object or
     * InvalidParameterException the unique identifier of the governance role is either null or invalid or
     * PropertyServerException the server is not available or
     * UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    @GetMapping(path = "/governance-roles/{governanceRoleGUID}/history")

    public GovernanceRoleHistoryResponse getGovernanceRoleHistoryByGUID(@PathVariable String serverName,
                                                                        @PathVariable String userId,
                                                                        @PathVariable String governanceRoleGUID)
    {
        return restAPI.getGovernanceRoleHistoryByGUID(serverName, userId, governanceRoleGUID);
    }


    /**
     * Retrieve the properties of a governance role using its unique name.  The results are returned as a list
     * since it is possible that two roles have the same identifier due to the distributed nature of the
     * open metadata ecosystem.  By returning all the search results here it is possible to manage the
     * duplicates through this interface.
     *
     * @param serverName name of server instance to call
     * @param userId the name of the calling user
     * @param roleId  the unique identifier (qualifiedName) of the governance role
     *
     * @return governance role object or
     * InvalidParameterException the guid is either null or invalid or
     * PropertyServerException the server is not available or
     * UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    @GetMapping(path = "/governance-roles/by-role-id/{roleId}")

    public GovernanceRolesResponse getGovernanceRoleByRoleId(@PathVariable String serverName,
                                                             @PathVariable String userId,
                                                             @PathVariable String roleId)
    {
        return restAPI.getGovernanceRoleByRoleId(serverName, userId, roleId);
    }


    /**
     * Return all the defined governance roles.
     *
     * @param serverName name of server instance to call
     * @param userId the name of the calling user.
     * @param domainIdentifier domain of interest - 0 is all domains
     * @param startFrom where to start from in the list of definitions
     * @param pageSize max number of results to return in one call
     * @return list of governance role objects or
     * InvalidParameterException the guid is either null or invalid or
     * PropertyServerException the server is not available or
     * UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    @GetMapping(path = "/governance-roles/by-domain/{domainIdentifier}")

    public GovernanceRolesResponse getGovernanceRolesByDomainId(@PathVariable String serverName,
                                                                @PathVariable String userId,
                                                                @PathVariable int    domainIdentifier,
                                                                @RequestParam int    startFrom,
                                                                @RequestParam int    pageSize)
    {
        return restAPI.getGovernanceRolesByDomainId(serverName, userId, domainIdentifier, startFrom, pageSize);
    }


    /**
     * Retrieve all the governance roles for a particular title.  The title can include regEx wildcards.
     *
     * @param serverName name of server instance to call
     * @param userId calling user
     * @param title short description of the role
     * @param startFrom where to start from in the list of definitions
     * @param pageSize max number of results to return in one call
     *
     * @return list of governance role objects or
     * InvalidParameterException the title is either null or invalid or
     * PropertyServerException the server is not available or
     * UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    @GetMapping(path = "/governance-roles/by-title/{title}")

    public GovernanceRolesResponse getGovernanceRolesByTitle(@PathVariable String serverName,
                                                             @PathVariable String userId,
                                                             @PathVariable String title,
                                                             @RequestParam int    startFrom,
                                                             @RequestParam int    pageSize)
    {
        return restAPI.getGovernanceRolesByTitle(serverName, userId, title, startFrom, pageSize);
    }


    /**
     * Return all the governance roles and their incumbents (if any).
     *
     * @param serverName name of server instance to call
     * @param userId the name of the calling user
     * @param domainIdentifier domain of interest - 0 is all domains
     * @param startFrom where to start from in the list of definitions
     * @param pageSize max number of results to return in one call
     *
     * @return list of governance role objects or
     * PropertyServerException the server is not available or
     * UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    @GetMapping(path = "/governance-roles/by-domain/{domainIdentifier}/current-appointments")

    public GovernanceRoleAppointeesResponse getCurrentGovernanceRoleAppointments(@PathVariable String serverName,
                                                                                 @PathVariable String userId,
                                                                                 @PathVariable int    domainIdentifier,
                                                                                 @RequestParam int    startFrom,
                                                                                 @RequestParam int    pageSize)
    {
        return restAPI.getCurrentGovernanceRoleAppointments(serverName, userId, domainIdentifier, startFrom, pageSize);
    }


    /**
     * Link a person to a governance role.  Only one person may be appointed at any one time.
     *
     * @param serverName name of server instance to call
     * @param userId the name of the calling user.
     * @param governanceRoleGUID unique identifier (guid) of the governance role.
     * @param profileGUID unique identifier of the actor profile
     * @param requestBody unique identifier for the profile and start date.
     * @return unique identifier (guid) of the appointment relationship or
     * UnrecognizedGUIDException the unique identifier of the governance role or profile is either null or invalid or
     * PropertyServerException the server is not available or
     * UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    @PostMapping(path = "/governance-roles/{governanceRoleGUID}/appoint/{profileGUID}")

    public GUIDResponse appointGovernanceRole(@PathVariable String                  serverName,
                                              @PathVariable String                  userId,
                                              @PathVariable String                  governanceRoleGUID,
                                              @PathVariable String                  profileGUID,
                                              @RequestBody  AppointmentRequestBody  requestBody)
    {
        return restAPI.appointGovernanceRole(serverName, userId, governanceRoleGUID, profileGUID, requestBody);
    }


    /**
     * Unlink a person from a governance role appointment.
     *
     * @param serverName name of server instance to call
     * @param userId the name of the calling user
     * @param governanceRoleGUID unique identifier (guid) of the governance role
     * @param appointmentGUID unique identifier (guid) of the appointment relationship
     * @param requestBody unique identifier for the profile and end date.
     * @return void response or
     * UnrecognizedGUIDException the unique identifier of the governance role or profile is either null or invalid or
     * InvalidParameterException the profile is not linked to this governance role or
     * PropertyServerException the server is not available or
     * UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    @PostMapping(path = "/governance-roles/{governanceRoleGUID}/relieve/{appointmentGUID}/{profileGUID}")

    public VoidResponse relieveGovernanceRole(@PathVariable String                  serverName,
                                              @PathVariable String                  userId,
                                              @PathVariable String                  governanceRoleGUID,
                                              @PathVariable String                  appointmentGUID,
                                              @PathVariable String                  profileGUID,
                                              @RequestBody  AppointmentRequestBody  requestBody)
    {
        return restAPI.relieveGovernanceRole(serverName, userId, governanceRoleGUID, appointmentGUID, profileGUID, requestBody);
    }
}
