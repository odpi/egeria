/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.schemamaker.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.commonservices.ffdc.rest.GetRequestBody;
import org.odpi.openmetadata.viewservices.schemamaker.server.SchemaMakerRESTServices;
import org.springframework.web.bind.annotation.*;


/**
 * The SchemaMakerResource provides part of the server-side implementation of the Schema Maker OMVS.
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
@Tag(name="API: Schema Maker", description="Schemas describe the structure of data. Schema Maker provides APIs for the creation and editing of schemas and the elements within them.",
        externalDocs=@ExternalDocumentation(description="Further Information",
                url="https://egeria-project.org/services/omvs/schema-maker/overview/"))

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
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/schema-types")
    @SecurityRequirement(name = "BearerAuthorization")

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
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/schema-types/from-template")
    @SecurityRequirement(name = "BearerAuthorization")

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
     * @param requestBody     properties for the new element.
     *
     * @return boolean or
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/schema-types/{schemaTypeGUID}/update")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="updateSchemaType",
            description="Update the properties of a schema type.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/schema-type"))

    public BooleanResponse updateSchemaType(@PathVariable
                                            String                                  serverName,
                                            @PathVariable String             urlMarker,
                                            @PathVariable
                                            String                                  schemaTypeGUID,
                                            @RequestBody (required = false)
                                            UpdateElementRequestBody requestBody)
    {
        return restAPI.updateSchemaType(serverName, urlMarker, schemaTypeGUID, requestBody);
    }


    /**
     * Delete a schema type.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
     * @param schemaTypeGUID  unique identifier of the element to delete
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/schema-types/{schemaTypeGUID}/delete")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="deleteSchemaType",
            description="Delete a schema type.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/schema-type"))

    public VoidResponse deleteSchemaType(@PathVariable
                                         String                    serverName,
                                         @PathVariable String             urlMarker,
                                         @PathVariable
                                         String                    schemaTypeGUID,
                                         @RequestBody (required = false)
                                         DeleteElementRequestBody requestBody)
    {
        return restAPI.deleteSchemaType(serverName, urlMarker, schemaTypeGUID, requestBody);
    }


    /**
     * Returns the list of schema types with a particular name.
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
    @PostMapping(path = "/schema-types/by-name")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getSchemaTypesByName",
            description="Returns the list of schema types with a particular name.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/schema-type"))

    public OpenMetadataRootElementsResponse getSchemaTypesByName(@PathVariable
                                                                 String            serverName,
                                                                 @PathVariable String             urlMarker,
                                                                 @RequestBody (required = false)
                                                                 FilterRequestBody requestBody)
    {
        return restAPI.getSchemaTypesByName(serverName, urlMarker, requestBody);
    }


    /**
     * Retrieve the list of schema type metadata elements that contain the search string.
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
    @PostMapping(path = "/schema-types/by-search-string")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="findSchemaTypes",
            description="Retrieve the list of schema type metadata elements that contain the search string.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/schema-type"))

    public OpenMetadataRootElementsResponse findSchemaTypes(@PathVariable
                                                            String                  serverName,
                                                            @PathVariable String             urlMarker,
                                                            @RequestBody (required = false)
                                                            SearchStringRequestBody requestBody)
    {
        return restAPI.findSchemaTypes(serverName, urlMarker,  requestBody);
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
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/schema-types/{schemaTypeGUID}/retrieve")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getSchemaTypeByGUID",
            description="Return the properties of a specific schema type.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/schema-type"))

    public OpenMetadataRootElementResponse getSchemaTypeByGUID(@PathVariable
                                                               String             serverName,
                                                               @PathVariable String             urlMarker,
                                                               @PathVariable
                                                               String             schemaTypeGUID,
                                                               @RequestBody (required = false)
                                                               GetRequestBody requestBody)
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
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = {"/schema-attributes"})
    @SecurityRequirement(name = "BearerAuthorization")

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
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = {"/schema-attributes/from-template"})
    @SecurityRequirement(name = "BearerAuthorization")

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
     * @param requestBody     properties for the new element.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = {"/schema-attributes/{schemaAttributeGUID}/update"})
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="updateSchemaAttribute",
            description="Update the properties of a schema attribute.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/schema-attribute"))

    public BooleanResponse updateSchemaAttribute(@PathVariable String                                  serverName,
                                                 @PathVariable String             urlMarker,
                                                 @PathVariable String                                  schemaAttributeGUID,
                                                 @RequestBody (required = false)
                                                     UpdateElementRequestBody requestBody)
    {
        return restAPI.updateSchemaAttribute(serverName, urlMarker, schemaAttributeGUID, requestBody);
    }


    /**
     * Delete a schema attribute.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
     * @param schemaAttributeGUID  unique identifier of the element to delete
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = {"/schema-attributes/{schemaAttributeGUID}/delete"})
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="deleteSchemaAttribute",
            description="Delete a schema attribute.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/schema-attribute"))

    public VoidResponse deleteSchemaAttribute(@PathVariable
                                              String                    serverName,
                                              @PathVariable String             urlMarker,
                                              @PathVariable
                                              String                    schemaAttributeGUID,
                                              @RequestBody (required = false)
                                              DeleteElementRequestBody requestBody)
    {
        return restAPI.deleteSchemaAttribute(serverName, urlMarker, schemaAttributeGUID, requestBody);
    }


    /**
     * Returns the list of schema attributes with a particular name.
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
    @PostMapping(path = {"/schema-attributes/by-name"})
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getSchemaAttributesByName",
            description="Returns the list of schema attributes with a particular name.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/schema-attribute"))

    public OpenMetadataRootElementsResponse getSchemaAttributesByName(@PathVariable
                                                                      String            serverName,
                                                                      @PathVariable String             urlMarker,
                                                                      @RequestBody (required = false)
                                                                      FilterRequestBody requestBody)
    {
        return restAPI.getSchemaAttributesByName(serverName, urlMarker, requestBody);
    }


    /**
     * Retrieve the list of schema attribute metadata elements that contain the search string.
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
    @PostMapping(path = {"/schema-attributes/by-search-string"})
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="findSchemaAttributes",
            description="Retrieve the list of schema attribute metadata elements that contain the search string.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/schema-attribute"))

    public OpenMetadataRootElementsResponse findSchemaAttributes(@PathVariable
                                                                 String                  serverName,
                                                                 @PathVariable String             urlMarker,
                                                                 @RequestBody (required = false)
                                                                 SearchStringRequestBody requestBody)
    {
        return restAPI.findSchemaAttributes(serverName, urlMarker,  requestBody);
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
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = {"/schema-attributes/{schemaAttributeGUID}/retrieve"})
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getSchemaAttributeByGUID",
            description="Return the properties of a specific schema attribute.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/schema-attribute"))

    public OpenMetadataRootElementResponse getSchemaAttributeByGUID(@PathVariable
                                                                    String             serverName,
                                                                    @PathVariable String             urlMarker,
                                                                    @PathVariable
                                                                    String             schemaAttributeGUID,
                                                                    @RequestBody (required = false)
                                                                    GetRequestBody requestBody)
    {
        return restAPI.getSchemaAttributeByGUID(serverName, urlMarker, schemaAttributeGUID, requestBody);
    }



    /*
     * =====================================================================================================================
     * Schema element relationships
     */

    /**
     * Attach a nested schema attribute to its parent schema attribute.
     *
     * @param serverName name of the server to route the request to
     * @param urlMarker  view service URL marker
     * @param schemaAttributeGUID unique identifier of the parent schema attribute
     * @param nestedSchemaAttributeGUID unique identifier of the nested schema attribute
     * @param requestBody properties for the relationship
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/schema-attributes/{schemaAttributeGUID}/nested-schema-attributes/{nestedSchemaAttributeGUID}/attach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="linkNestedSchemaAttribute",
            description="Attach a nested schema attribute to its parent schema attribute.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/schema-attribute"))

    public VoidResponse linkNestedSchemaAttribute(@PathVariable String serverName,
                                                  @PathVariable String urlMarker,
                                                  @PathVariable String schemaAttributeGUID,
                                                  @PathVariable String nestedSchemaAttributeGUID,
                                                  @RequestBody (required = false)
                                                  NewRelationshipRequestBody requestBody)
    {
        return restAPI.linkNestedSchemaAttribute(serverName, urlMarker, schemaAttributeGUID, nestedSchemaAttributeGUID, requestBody);
    }


    /**
     * Detach a nested schema attribute from its parent schema attribute.
     *
     * @param serverName name of the server to route the request to
     * @param urlMarker  view service URL marker
     * @param schemaAttributeGUID unique identifier of the parent schema attribute
     * @param nestedSchemaAttributeGUID unique identifier of the nested schema attribute
     * @param requestBody delete options
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/schema-attributes/{schemaAttributeGUID}/nested-schema-attributes/{nestedSchemaAttributeGUID}/detach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="detachNestedSchemaAttribute",
            description="Detach a nested schema attribute from its parent schema attribute.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/schema-attribute"))

    public VoidResponse detachNestedSchemaAttribute(@PathVariable String serverName,
                                                    @PathVariable String urlMarker,
                                                    @PathVariable String schemaAttributeGUID,
                                                    @PathVariable String nestedSchemaAttributeGUID,
                                                    @RequestBody (required = false)
                                                    DeleteRelationshipRequestBody requestBody)
    {
        return restAPI.detachNestedSchemaAttribute(serverName, urlMarker, schemaAttributeGUID, nestedSchemaAttributeGUID, requestBody);
    }


    /**
     * Attach a schema attribute to the schema type that it belongs to.
     *
     * @param serverName name of the server to route the request to
     * @param urlMarker  view service URL marker
     * @param schemaTypeGUID unique identifier of the schema type
     * @param nestedSchemaAttributeGUID unique identifier of the schema attribute
     * @param requestBody properties for the relationship
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/schema-types/{schemaTypeGUID}/attribute-for-schema/{nestedSchemaAttributeGUID}/attach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="linkAttributeForSchema",
            description="Attach a schema attribute to the schema type that it belongs to.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/schema-attribute"))

    public VoidResponse linkAttributeForSchema(@PathVariable String serverName,
                                               @PathVariable String urlMarker,
                                               @PathVariable String schemaTypeGUID,
                                               @PathVariable String nestedSchemaAttributeGUID,
                                               @RequestBody (required = false)
                                               NewRelationshipRequestBody requestBody)
    {
        return restAPI.linkAttributeForSchema(serverName, urlMarker, schemaTypeGUID, nestedSchemaAttributeGUID, requestBody);
    }


    /**
     * Detach a schema attribute from the schema type that it belongs to.
     *
     * @param serverName name of the server to route the request to
     * @param urlMarker  view service URL marker
     * @param schemaTypeGUID unique identifier of the schema type
     * @param nestedSchemaAttributeGUID unique identifier of the schema attribute
     * @param requestBody delete options
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/schema-types/{schemaTypeGUID}/attribute-for-schema/{nestedSchemaAttributeGUID}/detach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="detachAttributeForSchema",
            description="Detach a schema attribute from the schema type that it belongs to.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/schema-attribute"))

    public VoidResponse detachAttributeForSchema(@PathVariable String serverName,
                                                 @PathVariable String urlMarker,
                                                 @PathVariable String schemaTypeGUID,
                                                 @PathVariable String nestedSchemaAttributeGUID,
                                                 @RequestBody (required = false)
                                                 DeleteRelationshipRequestBody requestBody)
    {
        return restAPI.detachAttributeForSchema(serverName, urlMarker, schemaTypeGUID, nestedSchemaAttributeGUID, requestBody);
    }


    /**
     * Attach a foreign key column to the primary key column that it refers to.
     *
     * @param serverName name of the server to route the request to
     * @param urlMarker  view service URL marker
     * @param primaryKeyColumnGUID unique identifier of the primary key column
     * @param foreignKeyColumnGUID unique identifier of the foreign key column
     * @param requestBody properties for the relationship
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/schema-attributes/{primaryKeyColumnGUID}/foreign-keys/{foreignKeyColumnGUID}/attach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="linkForeignKey",
            description="Attach a foreign key column to the primary key column that it refers to.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/schema-attribute"))

    public VoidResponse linkForeignKey(@PathVariable String serverName,
                                       @PathVariable String urlMarker,
                                       @PathVariable String primaryKeyColumnGUID,
                                       @PathVariable String foreignKeyColumnGUID,
                                       @RequestBody (required = false)
                                       NewRelationshipRequestBody requestBody)
    {
        return restAPI.linkForeignKey(serverName, urlMarker, primaryKeyColumnGUID, foreignKeyColumnGUID, requestBody);
    }


    /**
     * Detach a foreign key column from the primary key column that it refers to.
     *
     * @param serverName name of the server to route the request to
     * @param urlMarker  view service URL marker
     * @param primaryKeyColumnGUID unique identifier of the primary key column
     * @param foreignKeyColumnGUID unique identifier of the foreign key column
     * @param requestBody delete options
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/schema-attributes/{primaryKeyColumnGUID}/foreign-keys/{foreignKeyColumnGUID}/detach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="detachForeignKey",
            description="Detach a foreign key column from the primary key column that it refers to.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/schema-attribute"))

    public VoidResponse detachForeignKey(@PathVariable String serverName,
                                         @PathVariable String urlMarker,
                                         @PathVariable String primaryKeyColumnGUID,
                                         @PathVariable String foreignKeyColumnGUID,
                                         @RequestBody (required = false)
                                         DeleteRelationshipRequestBody requestBody)
    {
        return restAPI.detachForeignKey(serverName, urlMarker, primaryKeyColumnGUID, foreignKeyColumnGUID, requestBody);
    }


    /**
     * Attach an external schema type to the schema element that uses it.
     *
     * @param serverName name of the server to route the request to
     * @param urlMarker  view service URL marker
     * @param schemaElementGUID unique identifier of the schema element
     * @param schemaTypeGUID unique identifier of the external schema type
     * @param requestBody properties for the relationship
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/schema-elements/{schemaElementGUID}/linked-external-schema-types/{schemaTypeGUID}/attach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="linkExternalSchemaType",
            description="Attach an external schema type to the schema element that uses it.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/schema-type"))

    public VoidResponse linkExternalSchemaType(@PathVariable String serverName,
                                               @PathVariable String urlMarker,
                                               @PathVariable String schemaElementGUID,
                                               @PathVariable String schemaTypeGUID,
                                               @RequestBody (required = false)
                                               NewRelationshipRequestBody requestBody)
    {
        return restAPI.linkExternalSchemaType(serverName, urlMarker, schemaElementGUID, schemaTypeGUID, requestBody);
    }


    /**
     * Detach an external schema type from the schema element that uses it.
     *
     * @param serverName name of the server to route the request to
     * @param urlMarker  view service URL marker
     * @param schemaElementGUID unique identifier of the schema element
     * @param schemaTypeGUID unique identifier of the external schema type
     * @param requestBody delete options
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/schema-elements/{schemaElementGUID}/linked-external-schema-types/{schemaTypeGUID}/detach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="detachExternalSchemaType",
            description="Detach an external schema type from the schema element that uses it.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/schema-type"))

    public VoidResponse detachExternalSchemaType(@PathVariable String serverName,
                                                 @PathVariable String urlMarker,
                                                 @PathVariable String schemaElementGUID,
                                                 @PathVariable String schemaTypeGUID,
                                                 @RequestBody (required = false)
                                                 DeleteRelationshipRequestBody requestBody)
    {
        return restAPI.detachExternalSchemaType(serverName, urlMarker, schemaElementGUID, schemaTypeGUID, requestBody);
    }


    /**
     * Attach the schema type that describes the domain (from) element of a map.
     *
     * @param serverName name of the server to route the request to
     * @param urlMarker  view service URL marker
     * @param schemaElementGUID unique identifier of the map schema element
     * @param schemaTypeGUID unique identifier of the schema type describing the domain of the map
     * @param requestBody properties for the relationship
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/schema-elements/{schemaElementGUID}/map-from-element-types/{schemaTypeGUID}/attach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="linkMapFromSchemaType",
            description="Attach the schema type that describes the domain (from) element of a map.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/schema-type"))

    public VoidResponse linkMapFromSchemaType(@PathVariable String serverName,
                                              @PathVariable String urlMarker,
                                              @PathVariable String schemaElementGUID,
                                              @PathVariable String schemaTypeGUID,
                                              @RequestBody (required = false)
                                              NewRelationshipRequestBody requestBody)
    {
        return restAPI.linkMapFromSchemaType(serverName, urlMarker, schemaElementGUID, schemaTypeGUID, requestBody);
    }


    /**
     * Detach the schema type that describes the domain (from) element of a map.
     *
     * @param serverName name of the server to route the request to
     * @param urlMarker  view service URL marker
     * @param schemaElementGUID unique identifier of the map schema element
     * @param schemaTypeGUID unique identifier of the schema type describing the domain of the map
     * @param requestBody delete options
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/schema-elements/{schemaElementGUID}/map-from-element-types/{schemaTypeGUID}/detach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="detachMapFromSchemaType",
            description="Detach the schema type that describes the domain (from) element of a map.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/schema-type"))

    public VoidResponse detachMapFromSchemaType(@PathVariable String serverName,
                                                @PathVariable String urlMarker,
                                                @PathVariable String schemaElementGUID,
                                                @PathVariable String schemaTypeGUID,
                                                @RequestBody (required = false)
                                                DeleteRelationshipRequestBody requestBody)
    {
        return restAPI.detachMapFromSchemaType(serverName, urlMarker, schemaElementGUID, schemaTypeGUID, requestBody);
    }


    /**
     * Attach the schema type that describes the range (to) element of a map.
     *
     * @param serverName name of the server to route the request to
     * @param urlMarker  view service URL marker
     * @param schemaElementGUID unique identifier of the map schema element
     * @param schemaTypeGUID unique identifier of the schema type describing the range of the map
     * @param requestBody properties for the relationship
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/schema-elements/{schemaElementGUID}/map-to-element-types/{schemaTypeGUID}/attach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="linkMapToSchemaType",
            description="Attach the schema type that describes the range (to) element of a map.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/schema-type"))

    public VoidResponse linkMapToSchemaType(@PathVariable String serverName,
                                            @PathVariable String urlMarker,
                                            @PathVariable String schemaElementGUID,
                                            @PathVariable String schemaTypeGUID,
                                            @RequestBody (required = false)
                                            NewRelationshipRequestBody requestBody)
    {
        return restAPI.linkMapToSchemaType(serverName, urlMarker, schemaElementGUID, schemaTypeGUID, requestBody);
    }


    /**
     * Detach the schema type that describes the range (to) element of a map.
     *
     * @param serverName name of the server to route the request to
     * @param urlMarker  view service URL marker
     * @param schemaElementGUID unique identifier of the map schema element
     * @param schemaTypeGUID unique identifier of the schema type describing the range of the map
     * @param requestBody delete options
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/schema-elements/{schemaElementGUID}/map-to-element-types/{schemaTypeGUID}/detach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="detachMapToSchemaType",
            description="Detach the schema type that describes the range (to) element of a map.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/schema-type"))

    public VoidResponse detachMapToSchemaType(@PathVariable String serverName,
                                              @PathVariable String urlMarker,
                                              @PathVariable String schemaElementGUID,
                                              @PathVariable String schemaTypeGUID,
                                              @RequestBody (required = false)
                                              DeleteRelationshipRequestBody requestBody)
    {
        return restAPI.detachMapToSchemaType(serverName, urlMarker, schemaElementGUID, schemaTypeGUID, requestBody);
    }


    /**
     * Attach a graph edge to one of the graph vertices that it connects.
     *
     * @param serverName name of the server to route the request to
     * @param urlMarker  view service URL marker
     * @param graphEdgeGUID unique identifier of the graph edge
     * @param graphVertexGUID unique identifier of the graph vertex
     * @param requestBody properties for the relationship
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/graph-edges/{graphEdgeGUID}/graph-vertices/{graphVertexGUID}/attach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="linkGraphEdge",
            description="Attach a graph edge to one of the graph vertices that it connects.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/schema-attribute"))

    public VoidResponse linkGraphEdge(@PathVariable String serverName,
                                      @PathVariable String urlMarker,
                                      @PathVariable String graphEdgeGUID,
                                      @PathVariable String graphVertexGUID,
                                      @RequestBody (required = false)
                                      NewRelationshipRequestBody requestBody)
    {
        return restAPI.linkGraphEdge(serverName, urlMarker, graphEdgeGUID, graphVertexGUID, requestBody);
    }


    /**
     * Detach a graph edge from one of the graph vertices that it connects.
     *
     * @param serverName name of the server to route the request to
     * @param urlMarker  view service URL marker
     * @param graphEdgeGUID unique identifier of the graph edge
     * @param graphVertexGUID unique identifier of the graph vertex
     * @param requestBody delete options
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/graph-edges/{graphEdgeGUID}/graph-vertices/{graphVertexGUID}/detach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="detachGraphEdge",
            description="Detach a graph edge from one of the graph vertices that it connects.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/schema-attribute"))

    public VoidResponse detachGraphEdge(@PathVariable String serverName,
                                        @PathVariable String urlMarker,
                                        @PathVariable String graphEdgeGUID,
                                        @PathVariable String graphVertexGUID,
                                        @RequestBody (required = false)
                                        DeleteRelationshipRequestBody requestBody)
    {
        return restAPI.detachGraphEdge(serverName, urlMarker, graphEdgeGUID, graphVertexGUID, requestBody);
    }


    /**
     * Attach a query target to the derived schema element that queries it.
     *
     * @param serverName name of the server to route the request to
     * @param urlMarker  view service URL marker
     * @param schemaElementGUID unique identifier of the derived schema element
     * @param queryTargetSchemaElementGUID unique identifier of the schema element supplying the query target
     * @param requestBody properties for the relationship
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/schema-elements/{schemaElementGUID}/query-targets/{queryTargetSchemaElementGUID}/attach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="linkQueryTarget",
            description="Attach a query target to the derived schema element that queries it.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/schema-type"))

    public VoidResponse linkQueryTarget(@PathVariable String serverName,
                                        @PathVariable String urlMarker,
                                        @PathVariable String schemaElementGUID,
                                        @PathVariable String queryTargetSchemaElementGUID,
                                        @RequestBody (required = false)
                                        NewRelationshipRequestBody requestBody)
    {
        return restAPI.linkQueryTarget(serverName, urlMarker, schemaElementGUID, queryTargetSchemaElementGUID, requestBody);
    }


    /**
     * Detach a query target from the derived schema element that queries it.
     *
     * @param serverName name of the server to route the request to
     * @param urlMarker  view service URL marker
     * @param schemaElementGUID unique identifier of the derived schema element
     * @param queryTargetSchemaElementGUID unique identifier of the schema element supplying the query target
     * @param requestBody delete options
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/schema-elements/{schemaElementGUID}/query-targets/{queryTargetSchemaElementGUID}/detach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="detachQueryTarget",
            description="Detach a query target from the derived schema element that queries it.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/schema-type"))

    public VoidResponse detachQueryTarget(@PathVariable String serverName,
                                          @PathVariable String urlMarker,
                                          @PathVariable String schemaElementGUID,
                                          @PathVariable String queryTargetSchemaElementGUID,
                                          @RequestBody (required = false)
                                          DeleteRelationshipRequestBody requestBody)
    {
        return restAPI.detachQueryTarget(serverName, urlMarker, schemaElementGUID, queryTargetSchemaElementGUID, requestBody);
    }


    /**
     * Attach a schema type to the element that it describes.
     *
     * @param serverName name of the server to route the request to
     * @param urlMarker  view service URL marker
     * @param elementGUID unique identifier of the element that is described by the schema type
     * @param schemaTypeGUID unique identifier of the schema type
     * @param requestBody properties for the relationship
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/elements/{elementGUID}/schema-types/{schemaTypeGUID}/attach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="linkSchema",
            description="Attach a schema type to the element that it describes.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/schema-type"))

    public VoidResponse linkSchema(@PathVariable String serverName,
                                   @PathVariable String urlMarker,
                                   @PathVariable String elementGUID,
                                   @PathVariable String schemaTypeGUID,
                                   @RequestBody (required = false)
                                   NewRelationshipRequestBody requestBody)
    {
        return restAPI.linkSchema(serverName, urlMarker, elementGUID, schemaTypeGUID, requestBody);
    }


    /**
     * Detach a schema type from the element that it describes.
     *
     * @param serverName name of the server to route the request to
     * @param urlMarker  view service URL marker
     * @param elementGUID unique identifier of the element that is described by the schema type
     * @param schemaTypeGUID unique identifier of the schema type
     * @param requestBody delete options
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/elements/{elementGUID}/schema-types/{schemaTypeGUID}/detach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="detachSchema",
            description="Detach a schema type from the element that it describes.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/schema-type"))

    public VoidResponse detachSchema(@PathVariable String serverName,
                                     @PathVariable String urlMarker,
                                     @PathVariable String elementGUID,
                                     @PathVariable String schemaTypeGUID,
                                     @RequestBody (required = false)
                                     DeleteRelationshipRequestBody requestBody)
    {
        return restAPI.detachSchema(serverName, urlMarker, elementGUID, schemaTypeGUID, requestBody);
    }

    /**
     * Attach a relational database schema type to the list that contains it.
     *
     * @param serverName name of the server to route the request to
     * @param urlMarker  view service URL marker
     * @param databaseSchemaTypeListGUID unique identifier of the relational database schema type list
     * @param relationalDBSchemaTypeGUID unique identifier of the relational database schema type
     * @param requestBody properties for the request
     *
     * @return response object
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/relational-db-schema-type-lists/{databaseSchemaTypeListGUID}/relational-db-schemas/{relationalDBSchemaTypeGUID}/attach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="linkRelationalDBSchema",
            description="Attach a relational database schema type to the list that contains it.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/schema-type"))

    public VoidResponse linkRelationalDBSchema(@PathVariable String serverName,
                                               @PathVariable String urlMarker,
                                               @PathVariable String databaseSchemaTypeListGUID,
                                               @PathVariable String relationalDBSchemaTypeGUID,
                                               @RequestBody (required = false)
                                               NewRelationshipRequestBody requestBody)
    {
        return restAPI.linkRelationalDBSchema(serverName, urlMarker, databaseSchemaTypeListGUID, relationalDBSchemaTypeGUID, requestBody);
    }


    /**
     * Detach a relational database schema type from the list that contained it.
     *
     * @param serverName name of the server to route the request to
     * @param urlMarker  view service URL marker
     * @param databaseSchemaTypeListGUID unique identifier of the relational database schema type list
     * @param relationalDBSchemaTypeGUID unique identifier of the relational database schema type
     * @param requestBody properties for the request
     *
     * @return response object
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/relational-db-schema-type-lists/{databaseSchemaTypeListGUID}/relational-db-schemas/{relationalDBSchemaTypeGUID}/detach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="detachRelationalDBSchema",
            description="Detach a relational database schema type from the list that contained it.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/schema-type"))

    public VoidResponse detachRelationalDBSchema(@PathVariable String serverName,
                                                 @PathVariable String urlMarker,
                                                 @PathVariable String databaseSchemaTypeListGUID,
                                                 @PathVariable String relationalDBSchemaTypeGUID,
                                                 @RequestBody (required = false)
                                                 DeleteRelationshipRequestBody requestBody)
    {
        return restAPI.detachRelationalDBSchema(serverName, urlMarker, databaseSchemaTypeListGUID, relationalDBSchemaTypeGUID, requestBody);
    }
}
