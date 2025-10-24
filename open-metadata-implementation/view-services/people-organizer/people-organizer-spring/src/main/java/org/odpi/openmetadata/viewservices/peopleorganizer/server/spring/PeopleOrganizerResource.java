/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.peopleorganizer.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.commonservices.ffdc.rest.DeleteRelationshipRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.NewRelationshipRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.viewservices.peopleorganizer.server.PeopleOrganizerRESTServices;
import org.springframework.web.bind.annotation.*;


/**
 * The PeopleOrganizerResource provides part of the server-side implementation of the People Organizer OMVS.
= */
@RestController
@RequestMapping("/servers/{serverName}/api/open-metadata/people-organizer")
@SecurityScheme(
        name = "BearerAuthorization",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer",
        in = SecuritySchemeIn.HEADER
)
@Tag(name="API: People Organizer OMVS", description="The People Organizer OMVS provides APIs to maintain information about an organization.  This includes the definitions of teams, roles and organization structures.",
        externalDocs=@ExternalDocumentation(description="Further Information",
                url="https://egeria-project.org/services/omvs/people-organizer/overview/"))

public class PeopleOrganizerResource
{
    private final PeopleOrganizerRESTServices restAPI = new PeopleOrganizerRESTServices();

    /**
     * Default constructor
     */
    public PeopleOrganizerResource()
    {
    }


    /**
     * Attach a person profile to one of its peers.
     *
     * @param serverName         name of called server
     * @param personOneGUID          unique identifier of the first person profile
     * @param personTwoGUID          unique identifier of the second person profile
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/actor-profiles/{personOneGUID}/peer-persons/{personTwoGUID}/attach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="linkPeerPerson",
            description="Attach a person profile to one of its peers.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/personal-profile/"))

    public VoidResponse linkPeerPerson(@PathVariable
                                       String                     serverName,
                                       @PathVariable
                                       String                     personOneGUID,
                                       @PathVariable
                                       String                     personTwoGUID,
                                       @RequestBody (required = false)
                                       NewRelationshipRequestBody requestBody)
    {
        return restAPI.linkPeerPerson(serverName, personOneGUID, personTwoGUID, requestBody);
    }


    /**
     * Detach a person profile from one of its peers.
     *
     * @param serverName         name of called server
     * @param personOneGUID          unique identifier of the first person profile
     * @param personTwoGUID          unique identifier of the second person profile
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/actor-profiles/{personOneGUID}/peer-persons/{personTwoGUID}/detach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="detachPeerPerson",
            description="Detach a person profile from one of its peers.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/personal-profile/"))

    public VoidResponse detachPeerPerson(@PathVariable
                                         String                    serverName,
                                         @PathVariable
                                         String                     personOneGUID,
                                         @PathVariable
                                         String                     personTwoGUID,
                                         @RequestBody (required = false)
                                             DeleteRelationshipRequestBody requestBody)
    {
        return restAPI.detachPeerPerson(serverName, personOneGUID, personTwoGUID, requestBody);
    }


    /**
     * Attach a super team to a subteam.
     *
     * @param serverName         name of called server
     * @param superTeamGUID          unique identifier of the super team
     * @param subteamGUID            unique identifier of the subteam
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/actor-profiles/{superTeamGUID}/team-structures/{subteamGUID}/attach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="linkTeamStructure",
            description="Attach a super team to a subteam.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/team"))

    public VoidResponse linkTeamStructure(@PathVariable
                                          String                     serverName,
                                          @PathVariable
                                          String superTeamGUID,
                                          @PathVariable
                                          String subteamGUID,
                                          @RequestBody (required = false)
                                          NewRelationshipRequestBody requestBody)
    {
        return restAPI.linkTeamStructure(serverName, superTeamGUID, subteamGUID, requestBody);
    }


    /**
     * Detach a super team from a subteam.
     *
     * @param serverName         name of called server
     * @param superTeamGUID          unique identifier of the super team
     * @param subteamGUID            unique identifier of the subteam
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/actor-profiles/{superTeamGUID}/team-structures/{subteamGUID}/detach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="detachTeamStructure",
            description="Detach a super team from a subteam.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/team"))

    public VoidResponse detachTeamStructure(@PathVariable
                                            String                    serverName,
                                            @PathVariable
                                            String superTeamGUID,
                                            @PathVariable
                                            String subteamGUID,
                                            @RequestBody (required = false)
                                                DeleteRelationshipRequestBody requestBody)
    {
        return restAPI.detachTeamStructure(serverName, superTeamGUID, subteamGUID, requestBody);
    }
}
