/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.actormanager.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.frameworkservices.omf.rest.AnyTimeRequestBody;
import org.odpi.openmetadata.viewservices.actormanager.server.ActorManagerRESTServices;
import org.springframework.web.bind.annotation.*;


/**
 * The ActorManagerResource provides part of the server-side implementation of the Actor Manager OMVS.
 = */
@RestController
@RequestMapping("/servers/{serverName}/api/open-metadata/{urlMarker}")

@Tag(name="API: Actor Manager OMVS", description="The Actor Manager OMVS provides APIs for supporting the creation and editing of actor profiles, actor roles and user identities.",
        externalDocs=@ExternalDocumentation(description="Further Information",
                url="https://egeria-project.org/services/omvs/actor-manager/overview/"))

public class ActorManagerResource
{
    private final ActorManagerRESTServices restAPI = new ActorManagerRESTServices();

    /**
     * Default constructor
     */
    public ActorManagerResource()
    {
    }


    /**
     * Create a actor profile.
     *
     * @param serverName                 name of called server.
     * @param urlMarker  view service URL marker
     * @param requestBody             properties for the actor profile.
     *
     * @return unique identifier of the newly created element
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/actor-profiles")

    @Operation(summary="createActorProfile",
            description="Create a actor profile.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/actor-profile"))

    public GUIDResponse createActorProfile(@PathVariable String                               serverName,
                                           @PathVariable String             urlMarker,
                                           @RequestBody (required = false)
                                           NewElementRequestBody requestBody)
    {
        return restAPI.createActorProfile(serverName, urlMarker, requestBody);
    }


    /**
     * Create a new metadata element to represent a actor profile using an existing metadata element as a template.
     * The template defines additional classifications and relationships that should be added to the new element.
     *
     * @param serverName             calling user
     * @param urlMarker  view service URL marker
     * @param requestBody properties that override the template
     *
     * @return unique identifier of the new metadata element
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/actor-profiles/from-template")
    @Operation(summary="createActorProfileFromTemplate",
            description="Create a new metadata element to represent a actor profile using an existing metadata element as a template.  The template defines additional classifications and relationships that should be added to the new element.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/actor-profile"))

    public GUIDResponse createActorProfileFromTemplate(@PathVariable
                                                       String              serverName,
                                                       @PathVariable String             urlMarker,
                                                       @RequestBody (required = false)
                                                       TemplateRequestBody requestBody)
    {
        return restAPI.createActorProfileFromTemplate(serverName, urlMarker, requestBody);
    }


    /**
     * Update the properties of a actor profile.
     *
     * @param serverName         name of called server.
     * @param urlMarker  view service URL marker
     * @param actorProfileGUID unique identifier of the actor profile (returned from create)
     * @param replaceAllProperties flag to indicate whether to completely replace the existing properties with the new properties, or just update
     *                          the individual properties specified on the request.
     * @param requestBody     properties for the new element.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/actor-profiles/{actorProfileGUID}/update")
    @Operation(summary="updateActorProfile",
            description="Update the properties of a actor profile.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/actor-profile"))

    public VoidResponse updateActorProfile(@PathVariable
                                           String                                  serverName,
                                           @PathVariable String             urlMarker,
                                           @PathVariable
                                           String                                  actorProfileGUID,
                                           @RequestParam (required = false, defaultValue = "false")
                                           boolean                                 replaceAllProperties,
                                           @RequestBody (required = false)
                                           UpdateElementRequestBody requestBody)
    {
        return restAPI.updateActorProfile(serverName, urlMarker, actorProfileGUID, replaceAllProperties, requestBody);
    }


    /**
     * Attach a profile to a location.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
     * @param locationGUID           unique identifier of the location
     * @param actorProfileGUID       unique identifier of the actor profile
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/actor-profiles/{actorProfileGUID}/profile-locations/{locationGUID}/attach")
    @Operation(summary="linkLocationToProfile",
            description="Attach a profile to a location.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/actor-profile"))

    public VoidResponse linkLocationToProfile(@PathVariable
                                              String                     serverName,
                                              @PathVariable String             urlMarker,
                                              @PathVariable
                                              String actorProfileGUID,
                                              @PathVariable
                                              String locationGUID,
                                              @RequestBody (required = false)
                                              RelationshipRequestBody requestBody)
    {
        return restAPI.linkLocationToProfile(serverName, urlMarker, actorProfileGUID, locationGUID, requestBody);
    }


    /**
     * Detach an actor profile from a location.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
     * @param actorProfileGUID       unique identifier of the actor profile
     * @param locationGUID           unique identifier of the location
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/actor-profiles/{actorProfileGUID}/profile-locations/{locationGUID}/detach")
    @Operation(summary="detachLocationFromProfile",
            description="Detach an actor profile from a location.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/actor-profile"))

    public VoidResponse detachLocationFromProfile(@PathVariable
                                                  String                    serverName,
                                                  @PathVariable String             urlMarker,
                                                  @PathVariable
                                                  String actorProfileGUID,
                                                  @PathVariable
                                                  String locationGUID,
                                                  @RequestBody (required = false)
                                                  MetadataSourceRequestBody requestBody)
    {
        return restAPI.detachLocationFromProfile(serverName, urlMarker, actorProfileGUID, locationGUID, requestBody);
    }


    /**
     * Attach a person profile to one of its peers.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
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
    @Operation(summary="linkPeerPerson",
            description="Attach a person profile to one of its peers.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/personal-profile/"))

    public VoidResponse linkPeerPerson(@PathVariable
                                       String                     serverName,
                                       @PathVariable String             urlMarker,
                                       @PathVariable
                                       String                     personOneGUID,
                                       @PathVariable
                                       String                     personTwoGUID,
                                       @RequestBody (required = false)
                                       RelationshipRequestBody requestBody)
    {
        return restAPI.linkPeerPerson(serverName, urlMarker, personOneGUID, personTwoGUID, requestBody);
    }


    /**
     * Detach a person profile from one of its peers.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
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
    @Operation(summary="detachSupportingDefinition",
            description="Detach a person profile from one of its peers.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/personal-profile/"))

    public VoidResponse detachPeerPerson(@PathVariable
                                         String                    serverName,
                                         @PathVariable String             urlMarker,
                                         @PathVariable
                                         String                     personOneGUID,
                                         @PathVariable
                                         String                     personTwoGUID,
                                         @RequestBody (required = false)
                                         MetadataSourceRequestBody requestBody)
    {
        return restAPI.detachPeerPerson(serverName, urlMarker, personOneGUID, personTwoGUID, requestBody);
    }


    /**
     * Attach a super team to a subteam.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
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
    @Operation(summary="linkTeamStructure",
            description="Attach a super team to a subteam.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/team"))

    public VoidResponse linkTeamStructure(@PathVariable
                                          String                     serverName,
                                          @PathVariable String             urlMarker,
                                          @PathVariable
                                          String superTeamGUID,
                                          @PathVariable
                                          String subteamGUID,
                                          @RequestBody (required = false)
                                          RelationshipRequestBody requestBody)
    {
        return restAPI.linkTeamStructure(serverName, urlMarker, superTeamGUID, subteamGUID, requestBody);
    }


    /**
     * Detach a super team from a subteam.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
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
    @Operation(summary="detachTeamStructure",
            description="Detach a super team from a subteam.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/team"))

    public VoidResponse detachTeamStructure(@PathVariable
                                            String                    serverName,
                                            @PathVariable String             urlMarker,
                                            @PathVariable
                                            String superTeamGUID,
                                            @PathVariable
                                            String subteamGUID,
                                            @RequestBody (required = false)
                                            MetadataSourceRequestBody requestBody)
    {
        return restAPI.detachTeamStructure(serverName, urlMarker, superTeamGUID, subteamGUID, requestBody);
    }


    /**
     * Attach an asset to an IT profile.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
     * @param assetGUID       unique identifier of the asset
     * @param itProfileGUID            unique identifier of the IT profile
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/assets/{assetGUID}/it-profiles/{itProfileGUID}/attach")
    @Operation(summary="linkAssetToProfile",
            description="Attach an asset to an IT profile.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/actor-profile"))

    public VoidResponse linkAssetToProfile(@PathVariable
                                           String                     serverName,
                                           @PathVariable String             urlMarker,
                                           @PathVariable
                                           String assetGUID,
                                           @PathVariable
                                           String itProfileGUID,
                                           @RequestBody (required = false)
                                           RelationshipRequestBody requestBody)
    {
        return restAPI.linkAssetToProfile(serverName, urlMarker, assetGUID, itProfileGUID, requestBody);
    }


    /**
     * Detach an asset from an IT profile.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
     * @param assetGUID       unique identifier of the asset
     * @param itProfileGUID            unique identifier of the IT profile
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/assets/{assetGUID}/it-profiles/{itProfileGUID}/detach")
    @Operation(summary="detachAssetFromProfile",
            description="Detach an asset from an IT profile.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/actor-profile"))

    public VoidResponse detachAssetFromProfile(@PathVariable
                                               String                    serverName,
                                               @PathVariable String             urlMarker,
                                               @PathVariable
                                               String assetGUID,
                                               @PathVariable
                                               String itProfileGUID,
                                               @RequestBody (required = false)
                                               MetadataSourceRequestBody requestBody)
    {
        return restAPI.detachAssetFromProfile(serverName, urlMarker, assetGUID, itProfileGUID, requestBody);
    }


    /**
     * Attach a team to its membership role.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
     * @param teamGUID               unique identifier of the team
     * @param personRoleGUID         unique identifier of the associated person role
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/actor-profiles/{teamGUID}/team-membership-roles/{personRoleGUID}/attach")
    @Operation(summary="linkTeamToMembershipRole",
            description="Attach a team to its membership role.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/team"))

    public VoidResponse linkTeamToMembershipRole(@PathVariable
                                                 String                     serverName,
                                                 @PathVariable String             urlMarker,
                                                 @PathVariable
                                                 String teamGUID,
                                                 @PathVariable
                                                 String personRoleGUID,
                                                 @RequestBody (required = false)
                                                 RelationshipRequestBody requestBody)
    {
        return restAPI.linkTeamToMembershipRole(serverName, urlMarker, teamGUID, personRoleGUID, requestBody);
    }


    /**
     * Detach a team profile from its membership role.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
     * @param teamGUID               unique identifier of the team
     * @param personRoleGUID         unique identifier of the associated person role
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/actor-profiles/{teamGUID}/team-membership-roles/{personRoleGUID}/detach")
    @Operation(summary="detachTeamFromMembershipRole",
            description="Detach a actor profile from a supporting actor profile.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/team"))

    public VoidResponse detachTeamFromMembershipRole(@PathVariable
                                                     String                    serverName,
                                                     @PathVariable String             urlMarker,
                                                     @PathVariable
                                                     String teamGUID,
                                                     @PathVariable
                                                     String personRoleGUID,
                                                     @RequestBody (required = false)
                                                     MetadataSourceRequestBody requestBody)
    {
        return restAPI.detachTeamFromMembershipRole(serverName, urlMarker, teamGUID, personRoleGUID, requestBody);
    }

    /**
     * Attach a team to its leadership role.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
     * @param teamGUID               unique identifier of the team
     * @param personRoleGUID         unique identifier of the associated person role
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/actor-profiles/{teamGUID}/team-leadership-roles/{personRoleGUID}/attach")
    @Operation(summary="linkTeamToLeadershipRole",
            description="Attach a team to its leadership role.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/team"))

    public VoidResponse linkTeamToLeadershipRole(@PathVariable
                                                 String                     serverName,
                                                 @PathVariable String             urlMarker,
                                                 @PathVariable
                                                 String teamGUID,
                                                 @PathVariable
                                                 String personRoleGUID,
                                                 @RequestBody (required = false)
                                                 RelationshipRequestBody requestBody)
    {
        return restAPI.linkTeamToLeadershipRole(serverName, urlMarker, teamGUID, personRoleGUID, requestBody);
    }


    /**
     * Detach a team profile from its leadership role.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
     * @param teamGUID               unique identifier of the team
     * @param personRoleGUID         unique identifier of the associated person role
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/actor-profiles/{teamGUID}/team-leadership-roles/{personRoleGUID}/detach")
    @Operation(summary="detachTeamFromLeadershipRole",
            description="Detach a actor profile from a supporting actor profile.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/team"))

    public VoidResponse detachTeamFromLeadershipRole(@PathVariable
                                                     String                    serverName,
                                                     @PathVariable String             urlMarker,
                                                     @PathVariable
                                                     String teamGUID,
                                                     @PathVariable
                                                     String personRoleGUID,
                                                     @RequestBody (required = false)
                                                     MetadataSourceRequestBody requestBody)
    {
        return restAPI.detachTeamFromLeadershipRole(serverName, urlMarker, teamGUID, personRoleGUID, requestBody);
    }


    /**
     * Delete a actor profile.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
     * @param actorProfileGUID  unique identifier of the element to delete
     * @param cascadedDelete can actor profiles be deleted if data fields are attached?
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/actor-profiles/{actorProfileGUID}/delete")
    @Operation(summary="deleteActorProfile",
            description="Delete a actor profile.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/actor-profile"))

    public VoidResponse deleteActorProfile(@PathVariable
                                           String                    serverName,
                                           @PathVariable String             urlMarker,
                                           @PathVariable
                                           String                    actorProfileGUID,
                                           @RequestParam(required = false, defaultValue = "false")
                                           boolean                   cascadedDelete,
                                           @RequestBody (required = false)
                                           MetadataSourceRequestBody requestBody)
    {
        return restAPI.deleteActorProfile(serverName, urlMarker, actorProfileGUID, cascadedDelete, requestBody);
    }


    /**
     * Returns the list of actor profiles with a particular name.
     *
     * @param serverName name of the service to route the request to
     * @param urlMarker  view service URL marker
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/actor-profiles/by-name")
    @Operation(summary="getActorProfilesByName",
            description="Returns the list of actor profiles with a particular name.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/actor-profile"))

    public ActorProfilesResponse getActorProfilesByName(@PathVariable
                                                        String            serverName,
                                                        @PathVariable String             urlMarker,
                                                        @RequestParam (required = false, defaultValue = "0")
                                                        int                     startFrom,
                                                        @RequestParam (required = false, defaultValue = "0")
                                                        int                     pageSize,
                                                        @RequestBody (required = false)
                                                        FilterRequestBody requestBody)
    {
        return restAPI.getActorProfilesByName(serverName, urlMarker, startFrom, pageSize, requestBody);
    }


    /**
     * Retrieve the list of actor profile metadata elements that contain the search string.
     *
     * @param serverName name of the service to route the request to
     * @param urlMarker  view service URL marker
     * @param startsWith does the value start with the supplied string?
     * @param endsWith does the value end with the supplied string?
     * @param ignoreCase should the search ignore case?
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/actor-profiles/by-search-string")
    @Operation(summary="findActorProfiles",
            description="Retrieve the list of actor profile metadata elements that contain the search string.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/actor-profile"))

    public ActorProfilesResponse findActorProfiles(@PathVariable
                                                   String                  serverName,
                                                   @PathVariable String             urlMarker,
                                                   @RequestParam (required = false, defaultValue = "0")
                                                   int                     startFrom,
                                                   @RequestParam (required = false, defaultValue = "0")
                                                   int                     pageSize,
                                                   @RequestParam (required = false, defaultValue = "false")
                                                   boolean                 startsWith,
                                                   @RequestParam (required = false, defaultValue = "false")
                                                   boolean                 endsWith,
                                                   @RequestParam (required = false, defaultValue = "false")
                                                   boolean                 ignoreCase,
                                                   @RequestBody (required = false)
                                                   FilterRequestBody requestBody)
    {
        return restAPI.findActorProfiles(serverName, urlMarker, startsWith, endsWith, ignoreCase, startFrom, pageSize, requestBody);
    }


    /**
     * Return the properties of a specific actor profile.
     *
     * @param serverName name of the service to route the request to
     * @param urlMarker  view service URL marker
     * @param actorProfileGUID    unique identifier of the required element
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/actor-profiles/{actorProfileGUID}/retrieve")
    @Operation(summary="getActorProfileByGUID",
            description="Return the properties of a specific actor profile.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/actor-profile"))

    public ActorProfileResponse getActorProfileByGUID(@PathVariable
                                                      String             serverName,
                                                      @PathVariable String             urlMarker,
                                                      @PathVariable
                                                      String             actorProfileGUID,
                                                      @RequestBody (required = false)
                                                      AnyTimeRequestBody requestBody)
    {
        return restAPI.getActorProfileByGUID(serverName, urlMarker, actorProfileGUID, requestBody);
    }


    /**
     * Create a actor role.
     *
     * @param serverName                 name of called server.
     * @param urlMarker  view service URL marker
     * @param requestBody             properties for the actor role.
     *
     * @return unique identifier of the newly created element
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = {"/actor-roles","/solution-roles"})

    @Operation(summary="createActorRole",
            description="Create a actor role.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/actor-role"))

    public GUIDResponse createActorRole(@PathVariable String                               serverName,
                                        @PathVariable String             urlMarker,
                                        @RequestBody (required = false)
                                        NewElementRequestBody requestBody)
    {
        return restAPI.createActorRole(serverName, urlMarker, requestBody);
    }


    /**
     * Create a new metadata element to represent a actor role using an existing metadata element as a template.
     * The template defines additional classifications and relationships that should be added to the new element.
     *
     * @param serverName             calling user
     * @param urlMarker  view service URL marker
     * @param requestBody properties that override the template
     *
     * @return unique identifier of the new metadata element
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = {"/actor-roles/from-template","/solution-roles/from-template"})
    @Operation(summary="createActorRoleFromTemplate",
            description="Create a new metadata element to represent a actor role using an existing metadata element as a template.  The template defines additional classifications and relationships that should be added to the new element.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/actor-role"))

    public GUIDResponse createActorRoleFromTemplate(@PathVariable
                                                    String              serverName,
                                                    @PathVariable String             urlMarker,
                                                    @RequestBody (required = false)
                                                    TemplateRequestBody requestBody)
    {
        return restAPI.createActorRoleFromTemplate(serverName, urlMarker, requestBody);
    }


    /**
     * Update the properties of a actor role.
     *
     * @param serverName         name of called server.
     * @param urlMarker  view service URL marker
     * @param actorRoleGUID unique identifier of the actor role (returned from create)
     * @param replaceAllProperties flag to indicate whether to completely replace the existing properties with the new properties, or just update
     *                          the individual properties specified on the request.
     * @param requestBody     properties for the new element.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = {"/actor-roles/{actorRoleGUID}/update","/solution-roles/{actorRoleGUID}/update"})
    @Operation(summary="updateActorRole",
            description="Update the properties of a actor role.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/actor-role"))

    public VoidResponse updateActorRole(@PathVariable
                                        String                                  serverName,
                                        @PathVariable String             urlMarker,
                                        @PathVariable
                                        String                                  actorRoleGUID,
                                        @RequestParam (required = false, defaultValue = "false")
                                        boolean                                 replaceAllProperties,
                                        @RequestBody (required = false)
                                        UpdateElementRequestBody requestBody)
    {
        return restAPI.updateActorRole(serverName, urlMarker, actorRoleGUID, replaceAllProperties, requestBody);
    }


    /**
     * Attach a person role to a person profile.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
     * @param personRoleGUID       unique identifier of the person role
     * @param personProfileGUID            unique identifier of the person profile
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/actor-roles/{personRoleGUID}/person-role-appointments/{personProfileGUID}/attach")
    @Operation(summary="linkPersonRoleToProfile",
            description="Attach a person role to a person profile.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/actor-role"))

    public VoidResponse linkPersonRoleToProfile(@PathVariable
                                            String                     serverName,
                                            @PathVariable String             urlMarker,
                                            @PathVariable
                                            String personRoleGUID,
                                            @PathVariable
                                            String personProfileGUID,
                                            @RequestBody (required = false)
                                            RelationshipRequestBody requestBody)
    {
        return restAPI.linkPersonRoleToProfile(serverName, urlMarker, personRoleGUID, personProfileGUID, requestBody);
    }


    /**
     * Detach a person role from a profile.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
     * @param personRoleGUID       unique identifier of the person role
     * @param personProfileGUID            unique identifier of the person profile
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/actor-roles/{personRoleGUID}/person-role-appointments/{personProfileGUID}/detach")
    @Operation(summary="detachPersonRoleFromProfile",
            description="Detach a person role from a profile.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/actor-role"))

    public VoidResponse detachPersonRoleFromProfile(@PathVariable
                                              String                    serverName,
                                              @PathVariable String             urlMarker,
                                              @PathVariable
                                              String personRoleGUID,
                                              @PathVariable
                                              String personProfileGUID,
                                              @RequestBody (required = false)
                                              MetadataSourceRequestBody requestBody)
    {
        return restAPI.detachPersonRoleFromProfile(serverName, urlMarker, personRoleGUID, personProfileGUID, requestBody);
    }



    /**
     * Attach a team role to a team profile.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
     * @param teamRoleGUID           unique identifier of the team role
     * @param teamProfileGUID        unique identifier of the team profile
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/actor-roles/{teamRoleGUID}/team-role-appointments/{teamProfileGUID}/attach")
    @Operation(summary="linkTeamRoleToProfile",
            description="Attach a team role to a team profile.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/actor-role"))

    public VoidResponse linkTeamRoleToProfile(@PathVariable
                                              String                     serverName,
                                              @PathVariable String             urlMarker,
                                              @PathVariable
                                              String teamRoleGUID,
                                              @PathVariable
                                              String teamProfileGUID,
                                              @RequestBody (required = false)
                                              RelationshipRequestBody requestBody)
    {
        return restAPI.linkTeamRoleToProfile(serverName, urlMarker, teamRoleGUID, teamProfileGUID, requestBody);
    }


    /**
     * Detach a team role from a team profile.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
     * @param teamRoleGUID           unique identifier of the team role
     * @param teamProfileGUID        unique identifier of the team profile
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/actor-roles/{teamRoleGUID}/team-role-appointments/{teamProfileGUID}/detach")
    @Operation(summary="Detach a team role from a team profile.",
            description="Detach a actor role from one of its peers.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/actor-role"))

    public VoidResponse detachTeamRoleFromProfile(@PathVariable
                                                  String                    serverName,
                                                  @PathVariable String             urlMarker,
                                                  @PathVariable
                                                  String teamRoleGUID,
                                                  @PathVariable
                                                  String teamProfileGUID,
                                                  @RequestBody (required = false)
                                                  MetadataSourceRequestBody requestBody)
    {
        return restAPI.detachTeamRoleFromProfile(serverName, urlMarker, teamRoleGUID, teamProfileGUID, requestBody);
    }


    /**
     * Attach an IT profile role to an IT profile.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
     * @param itProfileRoleGUID      unique identifier of the IT profile role
     * @param itProfileGUID          unique identifier of the IT profile
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/actor-roles/{itProfileRoleGUID}/it-profile-role-appointments/{itProfileGUID}/attach")
    @Operation(summary="linkITProfileRoleToProfile",
            description="Attach an IT profile role to an IT profile.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/actor-role"))

    public VoidResponse linkITProfileRoleToProfile(@PathVariable
                                                   String                     serverName,
                                                   @PathVariable String             urlMarker,
                                                   @PathVariable
                                                   String itProfileRoleGUID,
                                                   @PathVariable
                                                   String itProfileGUID,
                                                   @RequestBody (required = false)
                                                   RelationshipRequestBody requestBody)
    {
        return restAPI.linkITProfileRoleToProfile(serverName, urlMarker, itProfileRoleGUID, itProfileGUID, requestBody);
    }


    /**
     * Detach an IT profile role from an IT profile.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
     * @param itProfileRoleGUID      unique identifier of the IT profile role
     * @param itProfileGUID          unique identifier of the IT profile
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/actor-roles/{itProfileRoleGUID}/it-profile-role-appointments/{itProfileGUID}/detach")
    @Operation(summary="detachITProfileRoleFromProfile",
            description="Detach an IT profile role from an IT profile.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/actor-role"))

    public VoidResponse detachITProfileRoleFromProfile(@PathVariable
                                                   String                    serverName,
                                                   @PathVariable String             urlMarker,
                                                   @PathVariable
                                                   String itProfileRoleGUID,
                                                   @PathVariable
                                                   String itProfileGUID,
                                                   @RequestBody (required = false)
                                                   MetadataSourceRequestBody requestBody)
    {
        return restAPI.detachITProfileRoleFromProfile(serverName, urlMarker, itProfileRoleGUID, itProfileGUID, requestBody);
    }


    /**
     * Delete a actor role.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
     * @param actorRoleGUID  unique identifier of the element to delete
     * @param cascadedDelete can actor roles be deleted if data fields are attached?
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = {"/actor-roles/{actorRoleGUID}/delete","/solution-roles/{actorRoleGUID}/delete"})
    @Operation(summary="deleteActorRole",
            description="Delete a actor role.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/actor-role"))

    public VoidResponse deleteActorRole(@PathVariable
                                        String                    serverName,
                                        @PathVariable String             urlMarker,
                                        @PathVariable
                                        String                    actorRoleGUID,
                                        @RequestParam(required = false, defaultValue = "false")
                                        boolean                   cascadedDelete,
                                        @RequestBody (required = false)
                                        MetadataSourceRequestBody requestBody)
    {
        return restAPI.deleteActorRole(serverName, urlMarker, actorRoleGUID, cascadedDelete, requestBody);
    }


    /**
     * Returns the list of actor roles with a particular name.
     *
     * @param serverName name of the service to route the request to
     * @param urlMarker  view service URL marker
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = {"/actor-roles/by-name","/solution-roles/by-name"})
    @Operation(summary="getActorRolesByName",
            description="Returns the list of actor roles with a particular name.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/actor-role"))

    public ActorRolesResponse getActorRolesByName(@PathVariable
                                                  String            serverName,
                                                  @PathVariable String             urlMarker,
                                                  @RequestParam (required = false, defaultValue = "0")
                                                  int                     startFrom,
                                                  @RequestParam (required = false, defaultValue = "0")
                                                  int                     pageSize,
                                                  @RequestBody (required = false)
                                                  FilterRequestBody requestBody)
    {
        return restAPI.getActorRolesByName(serverName, urlMarker, startFrom, pageSize, requestBody);
    }


    /**
     * Retrieve the list of actor role metadata elements that contain the search string.
     *
     * @param serverName name of the service to route the request to
     * @param urlMarker  view service URL marker
     * @param startsWith does the value start with the supplied string?
     * @param endsWith does the value end with the supplied string?
     * @param ignoreCase should the search ignore case?
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = {"/actor-roles/by-search-string", "/solution-roles/by-search-string"})
    @Operation(summary="findActorRoles",
            description="Retrieve the list of actor role metadata elements that contain the search string.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/actor-role"))

    public ActorRolesResponse findActorRoles(@PathVariable
                                             String                  serverName,
                                             @PathVariable String             urlMarker,
                                             @RequestParam (required = false, defaultValue = "0")
                                             int                     startFrom,
                                             @RequestParam (required = false, defaultValue = "0")
                                             int                     pageSize,
                                             @RequestParam (required = false, defaultValue = "false")
                                             boolean                 startsWith,
                                             @RequestParam (required = false, defaultValue = "false")
                                             boolean                 endsWith,
                                             @RequestParam (required = false, defaultValue = "false")
                                             boolean                 ignoreCase,
                                             @RequestBody (required = false)
                                             FilterRequestBody requestBody)
    {
        return restAPI.findActorRoles(serverName, urlMarker, startsWith, endsWith, ignoreCase, startFrom, pageSize, requestBody);
    }


    /**
     * Return the properties of a specific actor role.
     *
     * @param serverName name of the service to route the request to
     * @param urlMarker  view service URL marker
     * @param actorRoleGUID    unique identifier of the required element
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = {"/actor-roles/{actorRoleGUID}/retrieve","/solution-roles/{actorRoleGUID}/retrieve"})
    @Operation(summary="getActorRoleByGUID",
            description="Return the properties of a specific actor role.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/actor-role"))

    public ActorRoleResponse getActorRoleByGUID(@PathVariable
                                                String             serverName,
                                                @PathVariable String             urlMarker,
                                                @PathVariable
                                                String             actorRoleGUID,
                                                @RequestBody (required = false)
                                                AnyTimeRequestBody requestBody)
    {
        return restAPI.getActorRoleByGUID(serverName, urlMarker, actorRoleGUID, requestBody);
    }



    /**
     * Create a user identity.
     *
     * @param serverName                 name of called server.
     * @param urlMarker  view service URL marker
     * @param requestBody             properties for the user identity.
     *
     * @return unique identifier of the newly created element
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/user-identities")

    @Operation(summary="createUserIdentity",
            description="Create a user identity.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/user-identity"))

    public GUIDResponse createUserIdentity(@PathVariable String                               serverName,
                                           @PathVariable String             urlMarker,
                                           @RequestBody (required = false)
                                           NewElementRequestBody requestBody)
    {
        return restAPI.createUserIdentity(serverName, urlMarker, requestBody);
    }


    /**
     * Create a new metadata element to represent a user identity using an existing metadata element as a template.
     * The template defines additional classifications and relationships that should be added to the new element.
     *
     * @param serverName             calling user
     * @param urlMarker  view service URL marker
     * @param requestBody properties that override the template
     *
     * @return unique identifier of the new metadata element
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/user-identities/from-template")
    @Operation(summary="createUserIdentityFromTemplate",
            description="Create a new metadata element to represent a user identity using an existing metadata element as a template.  The template defines additional classifications and relationships that should be added to the new element.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/user-identity"))

    public GUIDResponse createUserIdentityFromTemplate(@PathVariable
                                                       String              serverName,
                                                       @PathVariable String             urlMarker,
                                                       @RequestBody (required = false)
                                                       TemplateRequestBody requestBody)
    {
        return restAPI.createUserIdentityFromTemplate(serverName, urlMarker, requestBody);
    }


    /**
     * Update the properties of a user identity.
     *
     * @param serverName         name of called server.
     * @param urlMarker  view service URL marker
     * @param userIdentityGUID unique identifier of the user identity (returned from create)
     * @param replaceAllProperties flag to indicate whether to completely replace the existing properties with the new properties, or just update
     *                          the individual properties specified on the request.
     * @param requestBody     properties for the new element.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/user-identities/{userIdentityGUID}/update")
    @Operation(summary="updateUserIdentity",
            description="Update the properties of a user identity.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/user-identity"))

    public VoidResponse updateUserIdentity(@PathVariable
                                           String                                  serverName,
                                           @PathVariable String             urlMarker,
                                           @PathVariable
                                           String                                  userIdentityGUID,
                                           @RequestParam (required = false, defaultValue = "false")
                                           boolean                                 replaceAllProperties,
                                           @RequestBody (required = false)
                                           UpdateElementRequestBody requestBody)
    {
        return restAPI.updateUserIdentity(serverName, urlMarker, userIdentityGUID, replaceAllProperties, requestBody);
    }


    /**
     * Attach a profile to a user identity.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
     * @param userIdentityGUID unique identifier of the user identity
     * @param profileGUID unique identifier of the actor profile
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/user-identities/{userIdentityGUID}/profile-identity/{profileGUID}/attach")
    @Operation(summary="linkIdentityToProfile",
            description="Attach a profile to a user identity.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/user-identity"))

    public VoidResponse linkIdentityToProfile(@PathVariable
                                              String                     serverName,
                                              @PathVariable String             urlMarker,
                                              @PathVariable
                                              String                     userIdentityGUID,
                                              @PathVariable
                                              String profileGUID,
                                              @RequestBody (required = false)
                                              RelationshipRequestBody requestBody)
    {
        return restAPI.linkIdentityToProfile(serverName, urlMarker, userIdentityGUID, profileGUID, requestBody);
    }


    /**
     * Detach an actor profile from a user identity.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
     * @param userIdentityGUID unique identifier of the user identity
     * @param profileGUID unique identifier of the actor profile
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/user-identities/{userIdentityGUID}/profile-identity/{profileGUID}/detach")
    @Operation(summary="detachProfileIdentity",
            description="Detach an actor profile from a user identity.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/user-identity"))

    public VoidResponse detachProfileIdentity(@PathVariable
                                              String                    serverName,
                                              @PathVariable String             urlMarker,
                                              @PathVariable
                                              String                     userIdentityGUID,
                                              @PathVariable
                                              String                     profileGUID,
                                              @RequestBody (required = false)
                                              MetadataSourceRequestBody requestBody)
    {
        return restAPI.detachProfileIdentity(serverName, urlMarker, userIdentityGUID, profileGUID, requestBody);
    }


    /**
     * Add the SecurityGroupMembership classification to the user identity.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
     * @param userIdentityGUID unique identifier of the user identity
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/user-identities/{userIdentityGUID}/security-group-memberships/classify")
    @Operation(summary="addSecurityGroupMembership",
            description="Add the SecurityGroupMembership classification to the user identity.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/user-identity"))

    public VoidResponse addSecurityGroupMembership(@PathVariable
                                                   String                     serverName,
                                                   @PathVariable String             urlMarker,
                                                   @PathVariable
                                                   String                     userIdentityGUID,
                                                   @RequestBody (required = false)
                                                   ClassificationRequestBody requestBody)
    {
        return restAPI.addSecurityGroupMembership(serverName, urlMarker, userIdentityGUID, requestBody);
    }


    /**
     * Remove the SecurityGroupMembership classification from the user identity.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
     * @param userIdentityGUID unique identifier of the user identity
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/user-identities/{userIdentityGUID}/security-group-memberships/declassify")
    @Operation(summary="removeAllSecurityGroupMembership",
            description="Remove the SecurityGroupMembership classification from the user identity.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/user-identity"))

    public VoidResponse removeAllSecurityGroupMembership(@PathVariable String                     serverName,
                                                         @PathVariable String             urlMarker,
                                                         @PathVariable
                                                         String                     userIdentityGUID,
                                                         @RequestBody (required = false)
                                                         MetadataSourceRequestBody requestBody)
    {
        return restAPI.removeAllSecurityGroupMembership(serverName, urlMarker, userIdentityGUID, requestBody);
    }


    /**
     * Delete a user identity.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
     * @param userIdentityGUID  unique identifier of the element to delete
     * @param cascadedDelete can user identities be deleted if data fields are attached?
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/user-identities/{userIdentityGUID}/delete")
    @Operation(summary="deleteUserIdentity",
            description="Delete a user identity.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/user-identity"))

    public VoidResponse deleteUserIdentity(@PathVariable
                                           String                    serverName,
                                           @PathVariable String             urlMarker,
                                           @PathVariable
                                           String                    userIdentityGUID,
                                           @RequestParam(required = false, defaultValue = "false")
                                           boolean                   cascadedDelete,
                                           @RequestBody (required = false)
                                           MetadataSourceRequestBody requestBody)
    {
        return restAPI.deleteUserIdentity(serverName, urlMarker, userIdentityGUID, cascadedDelete, requestBody);
    }


    /**
     * Returns the list of user identities with a particular name.
     *
     * @param serverName name of the service to route the request to
     * @param urlMarker  view service URL marker
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/user-identities/by-name")
    @Operation(summary="getUserIdentitiesByName",
            description="Returns the list of user identities with a particular name.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/user-identity"))

    public UserIdentitiesResponse getUserIdentitiesByName(@PathVariable
                                                          String            serverName,
                                                          @PathVariable String             urlMarker,
                                                          @RequestParam (required = false, defaultValue = "0")
                                                          int                     startFrom,
                                                          @RequestParam (required = false, defaultValue = "0")
                                                          int                     pageSize,
                                                          @RequestBody (required = false)
                                                          FilterRequestBody requestBody)
    {
        return restAPI.getUserIdentitiesByName(serverName, urlMarker, startFrom, pageSize, requestBody);
    }


    /**
     * Retrieve the list of user identity metadata elements that contain the search string.
     *
     * @param serverName name of the service to route the request to
     * @param urlMarker  view service URL marker
     * @param startsWith does the value start with the supplied string?
     * @param endsWith does the value end with the supplied string?
     * @param ignoreCase should the search ignore case?
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
    @Operation(summary="findUserIdentities",
            description="Retrieve the list of user identity metadata elements that contain the search string.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/user-identity"))

    public UserIdentitiesResponse findUserIdentities(@PathVariable
                                                     String                  serverName,
                                                     @PathVariable String             urlMarker,
                                                     @RequestParam (required = false, defaultValue = "0")
                                                     int                     startFrom,
                                                     @RequestParam (required = false, defaultValue = "0")
                                                     int                     pageSize,
                                                     @RequestParam (required = false, defaultValue = "false")
                                                     boolean                 startsWith,
                                                     @RequestParam (required = false, defaultValue = "false")
                                                     boolean                 endsWith,
                                                     @RequestParam (required = false, defaultValue = "false")
                                                     boolean                 ignoreCase,
                                                     @RequestBody (required = false)
                                                     FilterRequestBody requestBody)
    {
        return restAPI.findUserIdentities(serverName, urlMarker, startsWith, endsWith, ignoreCase, startFrom, pageSize, requestBody);
    }


    /**
     * Return the properties of a specific user identity.
     *
     * @param serverName name of the service to route the request to
     * @param urlMarker  view service URL marker
     * @param userIdentityGUID    unique identifier of the required element
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/user-identities/{userIdentityGUID}/retrieve")
    @Operation(summary="getUserIdentityByGUID",
            description="Return the properties of a specific user identity.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/user-identity"))

    public UserIdentityResponse getUserIdentityByGUID(@PathVariable
                                                      String             serverName,
                                                      @PathVariable String             urlMarker,
                                                      @PathVariable
                                                      String             userIdentityGUID,
                                                      @RequestBody (required = false)
                                                      AnyTimeRequestBody requestBody)
    {
        return restAPI.getUserIdentityByGUID(serverName, urlMarker, userIdentityGUID, requestBody);
    }
}
