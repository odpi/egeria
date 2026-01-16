/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.actormanager.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.commonservices.ffdc.rest.GetRequestBody;
import org.odpi.openmetadata.viewservices.actormanager.server.ActorManagerRESTServices;
import org.springframework.web.bind.annotation.*;


/**
 * The ActorManagerResource provides part of the server-side implementation of the Actor Manager OMVS.
 = */
@RestController
@RequestMapping("/servers/{serverName}/api/open-metadata/{urlMarker}")
@SecurityScheme(
        name = "BearerAuthorization",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer",
        in = SecuritySchemeIn.HEADER
)
@Tag(name="API: Actor Manager", description="Create and edit information about the people, teams and automated processes running in your organization. These are collectively called actors and their definition includes an actor profile, the roles they perform, and their user identities.",
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
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/actor-profiles")
    @SecurityRequirement(name = "BearerAuthorization")

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
     * Create a new metadata element to represent an actor profile using an existing metadata element as a template.
     * The template defines additional classifications and relationships that should be added to the new element.
     *
     * @param serverName             calling user
     * @param urlMarker  view service URL marker
     * @param requestBody properties that override the template
     *
     * @return unique identifier of the new metadata element
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/actor-profiles/from-template")
    @SecurityRequirement(name = "BearerAuthorization")

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
     * Update the properties of an actor profile.
     *
     * @param serverName         name of called server.
     * @param urlMarker  view service URL marker
     * @param actorProfileGUID unique identifier of the actor profile (returned from create)
     * @param requestBody     properties for the new element.
     *
     * @return boolean or
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/actor-profiles/{actorProfileGUID}/update")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="updateActorProfile",
            description="Update the properties of a actor profile.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/actor-profile"))

    public BooleanResponse updateActorProfile(@PathVariable
                                              String                                  serverName,
                                              @PathVariable String             urlMarker,
                                              @PathVariable
                                              String                                  actorProfileGUID,
                                              @RequestBody (required = false)
                                              UpdateElementRequestBody requestBody)
    {
        return restAPI.updateActorProfile(serverName, urlMarker, actorProfileGUID, requestBody);
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
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/assets/{assetGUID}/it-profiles/{itProfileGUID}/attach")
    @SecurityRequirement(name = "BearerAuthorization")

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
                                           NewRelationshipRequestBody requestBody)
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
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/assets/{assetGUID}/it-profiles/{itProfileGUID}/detach")
    @SecurityRequirement(name = "BearerAuthorization")

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
                                               DeleteRelationshipRequestBody requestBody)
    {
        return restAPI.detachAssetFromProfile(serverName, urlMarker, assetGUID, itProfileGUID, requestBody);
    }


    /**
     * Delete a actor profile.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
     * @param actorProfileGUID  unique identifier of the element to delete
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/actor-profiles/{actorProfileGUID}/delete")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="deleteActorProfile",
            description="Delete a actor profile.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/actor-profile"))

    public VoidResponse deleteActorProfile(@PathVariable
                                           String                    serverName,
                                           @PathVariable String             urlMarker,
                                           @PathVariable
                                           String                    actorProfileGUID,
                                           @RequestBody (required = false)
                                           DeleteElementRequestBody requestBody)
    {
        return restAPI.deleteActorProfile(serverName, urlMarker, actorProfileGUID, requestBody);
    }


    /**
     * Returns the list of actor profiles with a particular name.
     *
     * @param serverName name of the service to route the request to
     * @param urlMarker  view service URL marker
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/actor-profiles/by-name")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getActorProfilesByName",
            description="Returns the list of actor profiles with a particular name.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/actor-profile"))

    public OpenMetadataRootElementsResponse getActorProfilesByName(@PathVariable
                                                                   String            serverName,
                                                                   @PathVariable String             urlMarker,
                                                                   @RequestBody (required = false)
                                                                   FilterRequestBody requestBody)
    {
        return restAPI.getActorProfilesByName(serverName, urlMarker, requestBody);
    }


    /**
     * Retrieve the list of actor profile metadata elements that contain the search string.
     *
     * @param serverName name of the service to route the request to
     * @param urlMarker  view service URL marker
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/actor-profiles/by-search-string")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="findActorProfiles",
            description="Retrieve the list of actor profile metadata elements that contain the search string.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/actor-profile"))

    public OpenMetadataRootElementsResponse findActorProfiles(@PathVariable
                                                              String                  serverName,
                                                              @PathVariable String             urlMarker,
                                                              @RequestBody (required = false)
                                                              SearchStringRequestBody requestBody)
    {
        return restAPI.findActorProfiles(serverName, urlMarker, requestBody);
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
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/actor-profiles/{actorProfileGUID}/retrieve")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getActorProfileByGUID",
            description="Return the properties of a specific actor profile.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/actor-profile"))

    public OpenMetadataRootElementResponse getActorProfileByGUID(@PathVariable
                                                                 String             serverName,
                                                                 @PathVariable String             urlMarker,
                                                                 @PathVariable
                                                                 String             actorProfileGUID,
                                                                 @RequestBody (required = false)
                                                                 GetRequestBody requestBody)
    {
        return restAPI.getActorProfileByGUID(serverName, urlMarker, actorProfileGUID, requestBody);
    }

    /* =====================================================================================================================
     * A contribution record describes the contribution of the linked profile.
     */


    /**
     * Creates a new contributionRecord for an element and returns the unique identifier for it.
     *
     * @param serverName   name of the server instances for this request
     * @param urlMarker  view service URL marker
     * @param actorProfileGUID unique identifier of the profile to link the contribution record to
     * @param requestBody  contains the name of the tag and (optional) description of the tag
     *
     * @return guid for new tag or
     * InvalidParameterException - one of the parameters is invalid or
     * PropertyServerException - a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/actor-profiles/{actorProfileGUID}/contribution-records")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="createContributionRecord",
            description="Creates a new contributionRecord for an element and returns the unique identifier for it.",
            externalDocs=@ExternalDocumentation(description="Contribution Records",
                    url="https://egeria-project.org/concepts/contribution-record/"))

    public GUIDResponse createContributionRecord(@PathVariable String            serverName,
                                                 @PathVariable String                        urlMarker,
                                                 @PathVariable String            actorProfileGUID,
                                                 @RequestBody NewAttachmentRequestBody requestBody)
    {
        return restAPI.createContributionRecord(serverName, urlMarker, actorProfileGUID, requestBody);
    }


    /**
     * Update an existing contribution record.
     *
     * @param serverName   name of the server instances for this request.
     * @param urlMarker  view service URL marker
     * @param contributionRecordGUID  unique identifier for the contribution record to change.
     * @param requestBody  containing type of comment enum and the text of the comment.
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid.
     * PropertyServerException There is a problem updating the element properties in the metadata repository.
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/contribution-records/{contributionRecordGUID}")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="updateContributionRecord",
            description="Update an existing contribution record.",
            externalDocs=@ExternalDocumentation(description="Contribution Records",
                    url="https://egeria-project.org/concepts/contribution-record/"))


    public BooleanResponse updateContributionRecord(@PathVariable String                         serverName,
                                                    @PathVariable String                        urlMarker,
                                                    @PathVariable String                         contributionRecordGUID,
                                                    @RequestBody UpdateElementRequestBody requestBody)
    {
        return restAPI.updateContributionRecord(serverName, urlMarker, contributionRecordGUID, requestBody);
    }


    /**
     * Removes a contribution record from the repository.  All the relationships to referenceables are lost.
     *
     * @param serverName   name of the server instances for this request
     * @param urlMarker  view service URL marker
     * @param contributionRecordGUID   unique id for the contribution record.
     * @param requestBody  delete request body.
     *
     * @return void or
     * InvalidParameterException - one of the parameters is invalid or
     * PropertyServerException - a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */

    @PostMapping(path = "/contribution-records/{contributionRecordGUID}/delete")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="deleteContributionRecord",
            description="Removes a contribution record from the repository.",
            externalDocs=@ExternalDocumentation(description="Contribution Records",
                    url="https://egeria-project.org/concepts/contribution-record/"))

    public VoidResponse   deleteContributionRecord(@PathVariable String          serverName,
                                                   @PathVariable String                        urlMarker,
                                                   @PathVariable String          contributionRecordGUID,
                                                   @RequestBody (required = false)
                                                   DeleteElementRequestBody requestBody)
    {
        return restAPI.deleteContributionRecord(serverName, urlMarker, contributionRecordGUID, requestBody);
    }


    /**
     * Retrieve the list of contribution record metadata elements that contain the search string.
     *
     * @param serverName name of the server instances for this request.
     * @param urlMarker  view service URL marker
     * @param requestBody search string and effective time.
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping("/contribution-records/by-search-string")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="findContributionRecords",
            description="Retrieve the list of contribution record metadata elements that contain the search string.",
            externalDocs=@ExternalDocumentation(description="Contribution Records",
                    url="https://egeria-project.org/concepts/contribution-record/"))

    public OpenMetadataRootElementsResponse findContributionRecords(@PathVariable String                  serverName,
                                                                    @PathVariable String                        urlMarker,
                                                                    @RequestBody  (required = false)
                                                                    SearchStringRequestBody              requestBody)
    {
        return restAPI.findContributionRecords(serverName, urlMarker, requestBody);
    }


    /**
     * Retrieve the list of contribution record metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param serverName   name of the server instances for this request
     * @param urlMarker  view service URL marker
     * @param requestBody name to search for and correlators
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping("/contribution-records/by-name")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getContributionRecordsByName",
            description="Retrieve the list of contribution record metadata elements with a matching qualified or display name. There are no wildcards supported on this request.",
            externalDocs=@ExternalDocumentation(description="Contribution Records",
                    url="https://egeria-project.org/concepts/contribution-record/"))

    public OpenMetadataRootElementsResponse getContributionRecordsByName(@PathVariable String          serverName,
                                                                         @PathVariable String            urlMarker,
                                                                         @RequestBody(required = false)  FilterRequestBody requestBody)
    {
        return restAPI.getContributionRecordsByName(serverName, urlMarker, requestBody);
    }


    /**
     * Retrieve the contribution record metadata element with the supplied unique identifier.
     *
     * @param serverName   name of the server instances for this request
     * @param urlMarker  view service URL marker
     * @param contributionRecordGUID unique identifier of the requested metadata element
     * @param requestBody optional effective time
     *
     * @return requested metadata element or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping("/contribution-records/{contributionRecordGUID}/retrieve")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getContributionRecordByGUID",
            description="Retrieve the contribution record metadata element with the supplied unique identifier.",
            externalDocs=@ExternalDocumentation(description="Contribution Records",
                    url="https://egeria-project.org/concepts/contribution-record/"))

    public OpenMetadataRootElementResponse getContributionRecordByGUID(@PathVariable String                        serverName,
                                                                       @PathVariable String                        urlMarker,
                                                                       @PathVariable String                        contributionRecordGUID,
                                                                       @RequestBody(required = false) GetRequestBody requestBody)
    {
        return restAPI.getContributionRecordByGUID(serverName, urlMarker, contributionRecordGUID, requestBody);
    }


    /* ===============================================================================
     * A role describes a set of relationships
     */

    /**
     * Create a actor role.
     *
     * @param serverName                 name of called server.
     * @param urlMarker  view service URL marker
     * @param requestBody             properties for the actor role.
     *
     * @return unique identifier of the newly created element
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = {"/actor-roles","/solution-roles"})
    @SecurityRequirement(name = "BearerAuthorization")

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
     * Create a new metadata element to represent an actor role using an existing metadata element as a template.
     * The template defines additional classifications and relationships that should be added to the new element.
     *
     * @param serverName             calling user
     * @param urlMarker  view service URL marker
     * @param requestBody properties that override the template
     *
     * @return unique identifier of the new metadata element
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = {"/actor-roles/from-template","/solution-roles/from-template"})
    @SecurityRequirement(name = "BearerAuthorization")

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
     * Update the properties of an actor role.
     *
     * @param serverName         name of called server.
     * @param urlMarker  view service URL marker
     * @param actorRoleGUID unique identifier of the actor role (returned from create)
     * @param requestBody     properties for the new element.
     *
     * @return boolean or
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = {"/actor-roles/{actorRoleGUID}/update","/solution-roles/{actorRoleGUID}/update"})
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="updateActorRole",
            description="Update the properties of a actor role.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/actor-role"))

    public BooleanResponse updateActorRole(@PathVariable
                                           String                                  serverName,
                                           @PathVariable String             urlMarker,
                                           @PathVariable
                                           String                                  actorRoleGUID,
                                           @RequestBody (required = false)
                                           UpdateElementRequestBody requestBody)
    {
        return restAPI.updateActorRole(serverName, urlMarker, actorRoleGUID, requestBody);
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
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/actor-roles/{personRoleGUID}/person-role-appointments/{personProfileGUID}/attach")
    @SecurityRequirement(name = "BearerAuthorization")

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
                                                NewRelationshipRequestBody requestBody)
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
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/actor-roles/{personRoleGUID}/person-role-appointments/{personProfileGUID}/detach")
    @SecurityRequirement(name = "BearerAuthorization")

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
                                                    DeleteRelationshipRequestBody requestBody)
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
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/actor-roles/{teamRoleGUID}/team-role-appointments/{teamProfileGUID}/attach")
    @SecurityRequirement(name = "BearerAuthorization")

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
                                              NewRelationshipRequestBody requestBody)
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
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/actor-roles/{teamRoleGUID}/team-role-appointments/{teamProfileGUID}/detach")
    @SecurityRequirement(name = "BearerAuthorization")

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
                                                  DeleteRelationshipRequestBody requestBody)
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
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/actor-roles/{itProfileRoleGUID}/it-profile-role-appointments/{itProfileGUID}/attach")
    @SecurityRequirement(name = "BearerAuthorization")

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
                                                   NewRelationshipRequestBody requestBody)
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
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/actor-roles/{itProfileRoleGUID}/it-profile-role-appointments/{itProfileGUID}/detach")
    @SecurityRequirement(name = "BearerAuthorization")

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
                                                       DeleteRelationshipRequestBody requestBody)
    {
        return restAPI.detachITProfileRoleFromProfile(serverName, urlMarker, itProfileRoleGUID, itProfileGUID, requestBody);
    }


    /**
     * Delete a actor role.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
     * @param actorRoleGUID  unique identifier of the element to delete
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = {"/actor-roles/{actorRoleGUID}/delete","/solution-roles/{actorRoleGUID}/delete"})
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="deleteActorRole",
            description="Delete a actor role.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/actor-role"))

    public VoidResponse deleteActorRole(@PathVariable
                                        String                    serverName,
                                        @PathVariable String             urlMarker,
                                        @PathVariable
                                        String                    actorRoleGUID,
                                        @RequestBody (required = false)
                                        DeleteElementRequestBody requestBody)
    {
        return restAPI.deleteActorRole(serverName, urlMarker, actorRoleGUID, requestBody);
    }


    /**
     * Returns the list of actor roles with a particular name.
     *
     * @param serverName name of the service to route the request to
     * @param urlMarker  view service URL marker
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = {"/actor-roles/by-name","/solution-roles/by-name"})
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getActorRolesByName",
            description="Returns the list of actor roles with a particular name.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/actor-role"))

    public OpenMetadataRootElementsResponse getActorRolesByName(@PathVariable
                                                                String            serverName,
                                                                @PathVariable String             urlMarker,
                                                                @RequestBody (required = false)
                                                                FilterRequestBody requestBody)
    {
        return restAPI.getActorRolesByName(serverName, urlMarker, requestBody);
    }


    /**
     * Retrieve the list of actor role metadata elements that contain the search string.
     *
     * @param serverName name of the service to route the request to
     * @param urlMarker  view service URL marker
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = {"/actor-roles/by-search-string", "/solution-roles/by-search-string"})
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="findActorRoles",
            description="Retrieve the list of actor role metadata elements that contain the search string.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/actor-role"))

    public OpenMetadataRootElementsResponse findActorRoles(@PathVariable
                                                           String                  serverName,
                                                           @PathVariable String             urlMarker,
                                                           @RequestBody (required = false)
                                                           SearchStringRequestBody requestBody)
    {
        return restAPI.findActorRoles(serverName, urlMarker, requestBody);
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
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = {"/actor-roles/{actorRoleGUID}/retrieve","/solution-roles/{actorRoleGUID}/retrieve"})
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getActorRoleByGUID",
            description="Return the properties of a specific actor role.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/actor-role"))

    public OpenMetadataRootElementResponse getActorRoleByGUID(@PathVariable
                                                              String             serverName,
                                                              @PathVariable String             urlMarker,
                                                              @PathVariable
                                                              String             actorRoleGUID,
                                                              @RequestBody (required = false)
                                                              GetRequestBody requestBody)
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
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/user-identities")
    @SecurityRequirement(name = "BearerAuthorization")

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
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/user-identities/from-template")
    @SecurityRequirement(name = "BearerAuthorization")

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
     * @param requestBody     properties for the new element.
     *
     * @return boolean or
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/user-identities/{userIdentityGUID}/update")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="updateUserIdentity",
            description="Update the properties of a user identity.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/user-identity"))

    public BooleanResponse updateUserIdentity(@PathVariable
                                              String                                  serverName,
                                              @PathVariable String             urlMarker,
                                              @PathVariable
                                              String                                  userIdentityGUID,
                                              @RequestBody (required = false)
                                              UpdateElementRequestBody requestBody)
    {
        return restAPI.updateUserIdentity(serverName, urlMarker, userIdentityGUID, requestBody);
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
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/user-identities/{userIdentityGUID}/profile-identity/{profileGUID}/attach")
    @SecurityRequirement(name = "BearerAuthorization")

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
                                              NewRelationshipRequestBody requestBody)
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
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/user-identities/{userIdentityGUID}/profile-identity/{profileGUID}/detach")
    @SecurityRequirement(name = "BearerAuthorization")

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
                                              DeleteRelationshipRequestBody requestBody)
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
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/user-identities/{userIdentityGUID}/security-group-memberships/classify")
    @SecurityRequirement(name = "BearerAuthorization")

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
                                                   NewClassificationRequestBody requestBody)
    {
        return restAPI.addSecurityGroupMembership(serverName, urlMarker, userIdentityGUID, requestBody);
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
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/user-identities/{userIdentityGUID}/security-group-memberships/reclassify")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="updateSecurityGroupMembership",
            description="Update the SecurityGroupMembership classification to the user identity.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/user-identity"))

    public VoidResponse updateSecurityGroupMembership(@PathVariable
                                                      String                     serverName,
                                                      @PathVariable String             urlMarker,
                                                      @PathVariable
                                                      String                     userIdentityGUID,
                                                      @RequestBody (required = false)
                                                      UpdateClassificationRequestBody requestBody)
    {
        return restAPI.updateSecurityGroupMembership(serverName, urlMarker, userIdentityGUID, requestBody);
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
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/user-identities/{userIdentityGUID}/security-group-memberships/declassify")
    @SecurityRequirement(name = "BearerAuthorization")

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
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/user-identities/{userIdentityGUID}/delete")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="deleteUserIdentity",
            description="Delete a user identity.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/user-identity"))

    public VoidResponse deleteUserIdentity(@PathVariable
                                           String                    serverName,
                                           @PathVariable String             urlMarker,
                                           @PathVariable
                                           String                    userIdentityGUID,
                                           @RequestBody (required = false)
                                           DeleteElementRequestBody requestBody)
    {
        return restAPI.deleteUserIdentity(serverName, urlMarker, userIdentityGUID, requestBody);
    }


    /**
     * Returns the list of user identities with a particular name.
     *
     * @param serverName name of the service to route the request to
     * @param urlMarker  view service URL marker
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/user-identities/by-name")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getUserIdentitiesByName",
            description="Returns the list of user identities with a particular name.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/user-identity"))

    public OpenMetadataRootElementsResponse getUserIdentitiesByName(@PathVariable
                                                                    String            serverName,
                                                                    @PathVariable String             urlMarker,
                                                                    @RequestBody (required = false)
                                                                    FilterRequestBody requestBody)
    {
        return restAPI.getUserIdentitiesByName(serverName, urlMarker, requestBody);
    }


    /**
     * Retrieve the list of user identity metadata elements that contain the search string.
     *
     * @param serverName name of the service to route the request to
     * @param urlMarker  view service URL marker
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/user-identities/by-search-string")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="findUserIdentities",
            description="Retrieve the list of user identity metadata elements that contain the search string.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/user-identity"))

    public OpenMetadataRootElementsResponse findUserIdentities(@PathVariable
                                                               String                  serverName,
                                                               @PathVariable String             urlMarker,

                                                               @RequestBody (required = false)
                                                               SearchStringRequestBody requestBody)
    {
        return restAPI.findUserIdentities(serverName, urlMarker, requestBody);
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
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/user-identities/{userIdentityGUID}/retrieve")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getUserIdentityByGUID",
            description="Return the properties of a specific user identity.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/user-identity"))

    public OpenMetadataRootElementResponse getUserIdentityByGUID(@PathVariable
                                                                 String             serverName,
                                                                 @PathVariable String             urlMarker,
                                                                 @PathVariable
                                                                 String             userIdentityGUID,
                                                                 @RequestBody (required = false)
                                                                 GetRequestBody requestBody)
    {
        return restAPI.getUserIdentityByGUID(serverName, urlMarker, userIdentityGUID, requestBody);
    }


    /**
     * Create a contact details.
     *
     * @param serverName                 name of called server.
     * @param urlMarker  view service URL marker
     * @param requestBody             properties for the contact details.
     *
     * @return unique identifier of the newly created element
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/contact-details")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="createContactDetails",
            description="Create a contact details.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/contact-method"))

    public GUIDResponse createContactDetails(@PathVariable
                                             String                               serverName,
                                             @PathVariable String             urlMarker,
                                             @RequestBody (required = false)
                                             NewElementRequestBody requestBody)
    {
        return restAPI.createContactDetails(serverName, urlMarker, requestBody);
    }


    /**
     * Create a new metadata element to represent a contact details using an existing metadata element as a template.
     * The template defines additional classifications and relationships that should be added to the new element.
     *
     * @param serverName             calling user
     * @param urlMarker  view service URL marker
     * @param requestBody properties that override the template
     *
     * @return unique identifier of the new metadata element
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/contact-details/from-template")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="createContactDetailsFromTemplate",
            description="Create a new metadata element to represent a contact details using an existing metadata element as a template.  The template defines additional classifications and relationships that should be added to the new element.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/contact-method"))

    public GUIDResponse createContactDetailsFromTemplate(@PathVariable
                                                         String              serverName,
                                                         @PathVariable String             urlMarker,
                                                         @RequestBody (required = false)
                                                         TemplateRequestBody requestBody)
    {
        return restAPI.createContactDetailsFromTemplate(serverName, urlMarker, requestBody);
    }


    /**
     * Update the properties of a contact details.
     *
     * @param serverName         name of called server.
     * @param urlMarker  view service URL marker
     * @param contactDetailsGUID unique identifier of the contact details (returned from create)
     * @param requestBody     properties for the new element.
     *
     * @return boolean or
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/contact-details/{contactDetailsGUID}/update")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="updateContactDetails",
            description="Update the properties of a contact details.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/contact-method"))

    public BooleanResponse updateContactDetails(@PathVariable
                                                String                                  serverName,
                                                @PathVariable String             urlMarker,
                                                @PathVariable
                                                String                                  contactDetailsGUID,
                                                @RequestBody (required = false)
                                                UpdateElementRequestBody requestBody)
    {
        return restAPI.updateContactDetails(serverName, urlMarker, contactDetailsGUID, requestBody);
    }


    /**
     * Attach an element to its contact details.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
     * @param elementGUID unique identifier of the parent element
     * @param contactDetailsGUID      unique identifier of the contact details
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/elements/{elementGUID}/contact-details/{contactDetailsGUID}/attach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="linkContactDetails",
            description="Attach an element to its contact details.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/contact-method"))

    public VoidResponse linkContactDetails(@PathVariable String                  serverName,
                                           @PathVariable String             urlMarker,
                                           @PathVariable String                  elementGUID,
                                           @PathVariable String                  contactDetailsGUID,
                                           @RequestBody (required = false)
                                           NewRelationshipRequestBody requestBody)
    {
        return restAPI.linkContactDetails(serverName, urlMarker, elementGUID, contactDetailsGUID, requestBody);
    }


    /**
     * Detach an element from its contact details.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
     * @param elementGUID unique identifier of the parent element
     * @param contactDetailsGUID      unique identifier of the contact details
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/elements/{elementGUID}/contact-details/{contactDetailsGUID}/detach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="detachContactDetails",
            description="Detach an element from its contact details.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/contact-method"))

    public VoidResponse detachContactDetails(@PathVariable
                                             String                    serverName,
                                             @PathVariable String             urlMarker,
                                             @PathVariable String                  elementGUID,
                                             @PathVariable String                  contactDetailsGUID,
                                             @RequestBody (required = false)
                                             DeleteRelationshipRequestBody requestBody)
    {
        return restAPI.detachContactDetails(serverName, urlMarker, elementGUID, contactDetailsGUID, requestBody);
    }


    /**
     * Delete a contact details.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
     * @param contactDetailsGUID  unique identifier of the element to delete
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/contact-details/{contactDetailsGUID}/delete")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="deleteContactDetails",
            description="Delete a contact details.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/contact-method"))

    public VoidResponse deleteContactDetails(@PathVariable
                                             String                    serverName,
                                             @PathVariable String             urlMarker,
                                             @PathVariable
                                             String                    contactDetailsGUID,
                                             @RequestBody (required = false)
                                             DeleteElementRequestBody requestBody)
    {
        return restAPI.deleteContactDetails(serverName, urlMarker, contactDetailsGUID, requestBody);
    }


    /**
     * Returns the list of contact details with a particular name.
     *
     * @param serverName name of the service to route the request to
     * @param urlMarker  view service URL marker
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/contact-details/by-name")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getContactDetailsByName",
            description="Returns the list of contact details with a particular name.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/contact-method"))

    public OpenMetadataRootElementsResponse getContactDetailsByName(@PathVariable
                                                                    String            serverName,
                                                                    @PathVariable String             urlMarker,
                                                                    @RequestBody (required = false)
                                                                    FilterRequestBody requestBody)
    {
        return restAPI.getContactDetailsByName(serverName, urlMarker, requestBody);
    }


    /**
     * Retrieve the list of contact details metadata elements that contain the search string.
     *
     * @param serverName name of the service to route the request to
     * @param urlMarker  view service URL marker
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/contact-details/by-search-string")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="findContactDetails",
            description="Retrieve the list of contact details metadata elements that contain the search string.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/contact-method"))

    public OpenMetadataRootElementsResponse findContactDetails(@PathVariable
                                                               String                  serverName,
                                                               @PathVariable String             urlMarker,
                                                               @RequestBody (required = false)
                                                               SearchStringRequestBody requestBody)
    {
        return restAPI.findContactDetails(serverName, urlMarker, requestBody);
    }


    /**
     * Return the properties of a specific contact details.
     *
     * @param serverName name of the service to route the request to
     * @param urlMarker  view service URL marker
     * @param contactDetailsGUID    unique identifier of the required element
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/contact-details/{contactDetailsGUID}/retrieve")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getContactDetailsByGUID",
            description="Return the properties of a specific contact details.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/contact-method"))

    public OpenMetadataRootElementResponse getContactDetailsByGUID(@PathVariable String             serverName,
                                                                   @PathVariable String             urlMarker,
                                                                   @PathVariable
                                                                   String             contactDetailsGUID,
                                                                   @RequestBody (required = false)
                                                                   GetRequestBody requestBody)
    {
        return restAPI.getContactDetailsByGUID(serverName, urlMarker, contactDetailsGUID, requestBody);
    }


    /**
     * Attach an actor to an element that describes its scope.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
     * @param scopeElementGUID            unique identifier of the element
     * @param actorGUID unique identifier of the actor
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/elements/{scopeElementGUID}/actors/{actorGUID}/attach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="linkAssignmentScope",
            description="Attach an actor to an element that describes its scope.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/actor"))

    public VoidResponse linkAssignmentScope(@PathVariable
                                            String                                serverName,
                                            @PathVariable String             urlMarker,
                                            @PathVariable
                                            String scopeElementGUID,
                                            @PathVariable
                                            String actorGUID,
                                            @RequestBody (required = false)
                                            NewRelationshipRequestBody requestBody)
    {
        return restAPI.linkAssignmentScope(serverName, urlMarker, scopeElementGUID, actorGUID, requestBody);
    }


    /**
     * Detach an actor from the element that describes its scope.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
     * @param scopeElementGUID            unique identifier of the element
     * @param actorGUID unique identifier of the actor
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/elements/{scopeElementGUID}/actors/{actorGUID}/detach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="detachAssignmentScope",
            description="Detach an actor from the element that describes its scope.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/actor"))

    public VoidResponse detachAssignmentScope(@PathVariable
                                              String                    serverName,
                                              @PathVariable String             urlMarker,
                                              @PathVariable
                                              String scopeElementGUID,
                                              @PathVariable
                                              String actorGUID,
                                              @RequestBody (required = false)
                                              DeleteRelationshipRequestBody requestBody)
    {
        return restAPI.detachAssignmentScope(serverName, urlMarker, scopeElementGUID, actorGUID, requestBody);
    }
}
