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
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
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
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
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
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
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
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
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
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
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
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
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
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
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
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
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
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
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
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
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
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
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
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
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
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
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
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
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
}
