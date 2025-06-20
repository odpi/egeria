/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;

import org.odpi.openmetadata.accessservices.communityprofile.server.OrganizationRESTServices;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
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

    public ActorProfileGraphResponse getActorProfileByUserId(@PathVariable String serverName,
                                                             @PathVariable String userId,
                                                             @PathVariable String actorProfileUserId)
    {
        return restAPI.getActorProfileByUserId(serverName, userId, actorProfileUserId);
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

    public ActorRoleResponse getPersonRoleByGUID(@PathVariable String serverName,
                                                 @PathVariable String userId,
                                                 @PathVariable String personRoleGUID)
    {
        return restAPI.getPersonRoleByGUID(serverName, userId, personRoleGUID);
    }
}
