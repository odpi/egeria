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

@Tag(name="API: Actor Manager OMVS", description="The Actor Manager OMVS provides APIs for supporting the creation and editing of a new governance domain.",
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
