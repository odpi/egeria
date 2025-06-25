/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.schemamaker.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.frameworkservices.omf.rest.AnyTimeRequestBody;
import org.odpi.openmetadata.viewservices.schemamaker.server.SchemaMakerRESTServices;
import org.springframework.web.bind.annotation.*;


/**
 * The SchemaMakerResource provides part of the server-side implementation of the Schema Maker OMVS.
 = */
@RestController
@RequestMapping("/servers/{serverName}/api/open-metadata/{urlMarker}")

@Tag(name="API: Schema Maker OMVS", description="The Schema Maker OMVS provides APIs for supporting the creation and editing of schema types, schema attributes and user identities.",
        externalDocs=@ExternalDocumentation(description="Further Information",
                url="https://egeria-project.org/services/omvs/schema=maker/overview/"))

public class SchemaMakerResource
{
    private final SchemaMakerRESTServices restAPI = new SchemaMakerRESTServices();

    /**
     * Default constructor
     */
    public SchemaMakerResource()
    {
    }


    /**
     * Create a schema type.
     *
     * @param serverName                 name of called server.
     * @param urlMarker  view service URL marker
     * @param requestBody             properties for the schema type.
     *
     * @return unique identifier of the newly created element
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/schema-types")

    @Operation(summary="createSchemaType",
            description="Create a schema type.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/schema-type"))

    public GUIDResponse createSchemaType(@PathVariable String                               serverName,
                                           @PathVariable String             urlMarker,
                                           @RequestBody (required = false)
                                           NewElementRequestBody requestBody)
    {
        return restAPI.createSchemaType(serverName, urlMarker, requestBody);
    }


    /**
     * Create a new metadata element to represent a schema type using an existing metadata element as a template.
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
    @PostMapping(path = "/schema-types/from-template")
    @Operation(summary="createSchemaTypeFromTemplate",
            description="Create a new metadata element to represent a schema type using an existing metadata element as a template.  The template defines additional classifications and relationships that should be added to the new element.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/schema-type"))

    public GUIDResponse createSchemaTypeFromTemplate(@PathVariable
                                                       String              serverName,
                                                       @PathVariable String             urlMarker,
                                                       @RequestBody (required = false)
                                                       TemplateRequestBody requestBody)
    {
        return restAPI.createSchemaTypeFromTemplate(serverName, urlMarker, requestBody);
    }


    /**
     * Update the properties of a schema type.
     *
     * @param serverName         name of called server.
     * @param urlMarker  view service URL marker
     * @param schemaTypeGUID unique identifier of the schema type (returned from create)
     * @param replaceAllProperties flag to indicate whether to completely replace the existing properties with the new properties, or just update
     *                          the individual properties specified on the request.
     * @param requestBody     properties for the new element.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/schema-types/{schemaTypeGUID}/update")
    @Operation(summary="updateSchemaType",
            description="Update the properties of a schema type.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/schema-type"))

    public VoidResponse updateSchemaType(@PathVariable
                                           String                                  serverName,
                                           @PathVariable String             urlMarker,
                                           @PathVariable
                                           String                                  schemaTypeGUID,
                                           @RequestParam (required = false, defaultValue = "false")
                                           boolean                                 replaceAllProperties,
                                           @RequestBody (required = false)
                                           UpdateElementRequestBody requestBody)
    {
        return restAPI.updateSchemaType(serverName, urlMarker, schemaTypeGUID, replaceAllProperties, requestBody);
    }


    /**
     * Attach a profile to a location.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
     * @param locationGUID           unique identifier of the location
     * @param schemaTypeGUID       unique identifier of the schema type
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/schema-types/{schemaTypeGUID}/profile-locations/{locationGUID}/attach")
    @Operation(summary="linkLocationToProfile",
            description="Attach a profile to a location.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/schema-type"))

    public VoidResponse linkLocationToProfile(@PathVariable
                                              String                     serverName,
                                              @PathVariable String             urlMarker,
                                              @PathVariable
                                              String schemaTypeGUID,
                                              @PathVariable
                                              String locationGUID,
                                              @RequestBody (required = false)
                                              RelationshipRequestBody requestBody)
    {
        return restAPI.linkLocationToProfile(serverName, urlMarker, schemaTypeGUID, locationGUID, requestBody);
    }


    /**
     * Detach a schema type from a location.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
     * @param schemaTypeGUID       unique identifier of the schema type
     * @param locationGUID           unique identifier of the location
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/schema-types/{schemaTypeGUID}/profile-locations/{locationGUID}/detach")
    @Operation(summary="detachLocationFromProfile",
            description="Detach a schema type from a location.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/schema-type"))

    public VoidResponse detachLocationFromProfile(@PathVariable
                                                  String                    serverName,
                                                  @PathVariable String             urlMarker,
                                                  @PathVariable
                                                  String schemaTypeGUID,
                                                  @PathVariable
                                                  String locationGUID,
                                                  @RequestBody (required = false)
                                                  MetadataSourceRequestBody requestBody)
    {
        return restAPI.detachLocationFromProfile(serverName, urlMarker, schemaTypeGUID, locationGUID, requestBody);
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
    @PostMapping(path = "/schema-types/{personOneGUID}/peer-persons/{personTwoGUID}/attach")
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
    @PostMapping(path = "/schema-types/{personOneGUID}/peer-persons/{personTwoGUID}/detach")
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
    @PostMapping(path = "/schema-types/{superTeamGUID}/team-structures/{subteamGUID}/attach")
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
    @PostMapping(path = "/schema-types/{superTeamGUID}/team-structures/{subteamGUID}/detach")
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
    @PostMapping(path = "/schema-types/{teamGUID}/team-membership-roles/{personRoleGUID}/attach")
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
    @PostMapping(path = "/schema-types/{teamGUID}/team-membership-roles/{personRoleGUID}/detach")
    @Operation(summary="detachTeamFromMembershipRole",
            description="Detach a schema type from a supporting schema type.",
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
    @PostMapping(path = "/schema-types/{teamGUID}/team-leadership-roles/{personRoleGUID}/attach")
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
    @PostMapping(path = "/schema-types/{teamGUID}/team-leadership-roles/{personRoleGUID}/detach")
    @Operation(summary="detachTeamFromLeadershipRole",
            description="Detach a schema type from a supporting schema type.",
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
     * Delete a schema type.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
     * @param schemaTypeGUID  unique identifier of the element to delete
     * @param cascadedDelete ca schema types be deleted if data fields are attached?
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/schema-types/{schemaTypeGUID}/delete")
    @Operation(summary="deleteSchemaType",
            description="Delete a schema type.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/schema-type"))

    public VoidResponse deleteSchemaType(@PathVariable
                                           String                    serverName,
                                           @PathVariable String             urlMarker,
                                           @PathVariable
                                           String                    schemaTypeGUID,
                                           @RequestParam(required = false, defaultValue = "false")
                                           boolean                   cascadedDelete,
                                           @RequestBody (required = false)
                                           MetadataSourceRequestBody requestBody)
    {
        return restAPI.deleteSchemaType(serverName, urlMarker, schemaTypeGUID, cascadedDelete, requestBody);
    }


    /**
     * Returns the list of schema types with a particular name.
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
    @PostMapping(path = "/schema-types/by-name")
    @Operation(summary="getSchemaTypesByName",
            description="Returns the list of schema types with a particular name.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/schema-type"))

    public SchemaTypesResponse getSchemaTypesByName(@PathVariable
                                                        String            serverName,
                                                        @PathVariable String             urlMarker,
                                                        @RequestParam (required = false, defaultValue = "0")
                                                        int                     startFrom,
                                                        @RequestParam (required = false, defaultValue = "0")
                                                        int                     pageSize,
                                                        @RequestBody (required = false)
                                                        FilterRequestBody requestBody)
    {
        return restAPI.getSchemaTypesByName(serverName, urlMarker, startFrom, pageSize, requestBody);
    }


    /**
     * Retrieve the list of schema type metadata elements that contain the search string.
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
    @PostMapping(path = "/schema-types/by-search-string")
    @Operation(summary="findSchemaTypes",
            description="Retrieve the list of schema type metadata elements that contain the search string.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/schema-type"))

    public SchemaTypesResponse findSchemaTypes(@PathVariable
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
        return restAPI.findSchemaTypes(serverName, urlMarker, startsWith, endsWith, ignoreCase, startFrom, pageSize, requestBody);
    }


    /**
     * Return the properties of a specific schema type.
     *
     * @param serverName name of the service to route the request to
     * @param urlMarker  view service URL marker
     * @param schemaTypeGUID    unique identifier of the required element
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/schema-types/{schemaTypeGUID}/retrieve")
    @Operation(summary="getSchemaTypeByGUID",
            description="Return the properties of a specific schema type.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/schema-type"))

    public SchemaTypeResponse getSchemaTypeByGUID(@PathVariable
                                                      String             serverName,
                                                      @PathVariable String             urlMarker,
                                                      @PathVariable
                                                      String             schemaTypeGUID,
                                                      @RequestBody (required = false)
                                                      AnyTimeRequestBody requestBody)
    {
        return restAPI.getSchemaTypeByGUID(serverName, urlMarker, schemaTypeGUID, requestBody);
    }


    /**
     * Create a schema attribute.
     *
     * @param serverName                 name of called server.
     * @param urlMarker  view service URL marker
     * @param requestBody             properties for the schema attribute.
     *
     * @return unique identifier of the newly created element
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = {"/schema-attributes","/solution-roles"})

    @Operation(summary="createSchemaAttribute",
            description="Create a schema attribute.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/schema-attribute"))

    public GUIDResponse createSchemaAttribute(@PathVariable String                               serverName,
                                        @PathVariable String             urlMarker,
                                        @RequestBody (required = false)
                                        NewElementRequestBody requestBody)
    {
        return restAPI.createSchemaAttribute(serverName, urlMarker, requestBody);
    }


    /**
     * Create a new metadata element to represent a schema attribute using an existing metadata element as a template.
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
    @PostMapping(path = {"/schema-attributes/from-template","/solution-roles/from-template"})
    @Operation(summary="createSchemaAttributeFromTemplate",
            description="Create a new metadata element to represent a schema attribute using an existing metadata element as a template.  The template defines additional classifications and relationships that should be added to the new element.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/schema-attribute"))

    public GUIDResponse createSchemaAttributeFromTemplate(@PathVariable
                                                    String              serverName,
                                                    @PathVariable String             urlMarker,
                                                    @RequestBody (required = false)
                                                    TemplateRequestBody requestBody)
    {
        return restAPI.createSchemaAttributeFromTemplate(serverName, urlMarker, requestBody);
    }


    /**
     * Update the properties of a schema attribute.
     *
     * @param serverName         name of called server.
     * @param urlMarker  view service URL marker
     * @param schemaAttributeGUID unique identifier of the schema attribute (returned from create)
     * @param replaceAllProperties flag to indicate whether to completely replace the existing properties with the new properties, or just update
     *                          the individual properties specified on the request.
     * @param requestBody     properties for the new element.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = {"/schema-attributes/{schemaAttributeGUID}/update","/solution-roles/{schemaAttributeGUID}/update"})
    @Operation(summary="updateSchemaAttribute",
            description="Update the properties of a schema attribute.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/schema-attribute"))

    public VoidResponse updateSchemaAttribute(@PathVariable
                                        String                                  serverName,
                                        @PathVariable String             urlMarker,
                                        @PathVariable
                                        String                                  schemaAttributeGUID,
                                        @RequestParam (required = false, defaultValue = "false")
                                        boolean                                 replaceAllProperties,
                                        @RequestBody (required = false)
                                        UpdateElementRequestBody requestBody)
    {
        return restAPI.updateSchemaAttribute(serverName, urlMarker, schemaAttributeGUID, replaceAllProperties, requestBody);
    }



    /**
     * Delete a schema attribute.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
     * @param schemaAttributeGUID  unique identifier of the element to delete
     * @param cascadedDelete ca schemaAttributes be deleted if data fields are attached?
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = {"/schema-attributes/{schemaAttributeGUID}/delete","/solution-roles/{schemaAttributeGUID}/delete"})
    @Operation(summary="deleteSchemaAttribute",
            description="Delete a schema attribute.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/schema-attribute"))

    public VoidResponse deleteSchemaAttribute(@PathVariable
                                        String                    serverName,
                                        @PathVariable String             urlMarker,
                                        @PathVariable
                                        String                    schemaAttributeGUID,
                                        @RequestParam(required = false, defaultValue = "false")
                                        boolean                   cascadedDelete,
                                        @RequestBody (required = false)
                                        MetadataSourceRequestBody requestBody)
    {
        return restAPI.deleteSchemaAttribute(serverName, urlMarker, schemaAttributeGUID, cascadedDelete, requestBody);
    }


    /**
     * Returns the list of schema attributes with a particular name.
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
    @PostMapping(path = {"/schema-attributes/by-name","/solution-roles/by-name"})
    @Operation(summary="getSchemaAttributesByName",
            description="Returns the list of schema attributes with a particular name.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/schema-attribute"))

    public SchemaAttributesResponse getSchemaAttributesByName(@PathVariable
                                                  String            serverName,
                                                  @PathVariable String             urlMarker,
                                                  @RequestParam (required = false, defaultValue = "0")
                                                  int                     startFrom,
                                                  @RequestParam (required = false, defaultValue = "0")
                                                  int                     pageSize,
                                                  @RequestBody (required = false)
                                                  FilterRequestBody requestBody)
    {
        return restAPI.getSchemaAttributesByName(serverName, urlMarker, startFrom, pageSize, requestBody);
    }


    /**
     * Retrieve the list of schema attribute metadata elements that contain the search string.
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
    @PostMapping(path = {"/schema-attributes/by-search-string", "/solution-roles/by-search-string"})
    @Operation(summary="findSchemaAttributes",
            description="Retrieve the list of schema attribute metadata elements that contain the search string.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/schema-attribute"))

    public SchemaAttributesResponse findSchemaAttributes(@PathVariable
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
        return restAPI.findSchemaAttributes(serverName, urlMarker, startsWith, endsWith, ignoreCase, startFrom, pageSize, requestBody);
    }


    /**
     * Return the properties of a specific schema attribute.
     *
     * @param serverName name of the service to route the request to
     * @param urlMarker  view service URL marker
     * @param schemaAttributeGUID    unique identifier of the required element
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = {"/schema-attributes/{schemaAttributeGUID}/retrieve","/solution-roles/{schemaAttributeGUID}/retrieve"})
    @Operation(summary="getSchemaAttributeByGUID",
            description="Return the properties of a specific schema attribute.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/schema-attribute"))

    public SchemaAttributeResponse getSchemaAttributeByGUID(@PathVariable
                                                String             serverName,
                                                @PathVariable String             urlMarker,
                                                @PathVariable
                                                String             schemaAttributeGUID,
                                                @RequestBody (required = false)
                                                AnyTimeRequestBody requestBody)
    {
        return restAPI.getSchemaAttributeByGUID(serverName, urlMarker, schemaAttributeGUID, requestBody);
    }
}
