/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.datamanager.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.odpi.openmetadata.accessservices.datamanager.rest.*;
import org.odpi.openmetadata.accessservices.datamanager.server.SchemaManagerRESTServices;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.NameRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.SearchStringRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.springframework.web.bind.annotation.*;


/**
 * SchemaManagerResource is the server-side implementation of the Data Manager OMAS's
 * support for relational topics.  It matches the SchemaManagerClient.
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/access-services/data-manager/users/{userId}")

@Tag(name="Metadata Access Server: Data Manager OMAS",
     description="The Data Manager OMAS provides APIs for tools and applications wishing to manage metadata relating to data managers " +
                         "such as database servers, event brokers, content managers and file systems.",
     externalDocs=@ExternalDocumentation(description="Further Information",
                                         url="https://egeria-project.org/services/omas/data-manager/overview/"))

public class SchemaManagerResource
{
    private final SchemaManagerRESTServices restAPI = new SchemaManagerRESTServices();

    /**
     * Default constructor
     */
    public SchemaManagerResource()
    {
    }

    /* =====================================================================================================================
     * A schemaType is used to describe complex structures found in the schema of a data asset
     */

    /**
     * Create a new metadata element to represent a primitive schema type such as a string, integer or character.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param requestBody properties about the schema type to store
     *
     * @return unique identifier of the new schema type or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/schema-types/primitives")

    public GUIDResponse createPrimitiveSchemaType(@PathVariable String                         serverName,
                                                  @PathVariable String                         userId,
                                                  @RequestBody  PrimitiveSchemaTypeRequestBody requestBody)
    {
        return restAPI.createPrimitiveSchemaType(serverName, userId, requestBody);
    }


    /**
     * Create a new metadata element to represent a schema type that has a fixed value.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param requestBody properties about the schema type to store
     *
     * @return unique identifier of the new schema type or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/schema-types/literals")

    public GUIDResponse createLiteralSchemaType(@PathVariable String                       serverName,
                                                @PathVariable String                       userId,
                                                @RequestBody  LiteralSchemaTypeRequestBody requestBody)
    {
        return restAPI.createLiteralSchemaType(serverName, userId, requestBody);
    }


    /**
     * Create a new metadata element to represent a schema type that has a fixed set of values that are described by a valid value set.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param validValuesSetGUID unique identifier of the valid values set to used
     * @param requestBody properties about the schema type to store
     *
     * @return unique identifier of the new schema type or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/schema-types/enums/valid-values/{validValuesSetGUID}")

    public GUIDResponse createEnumSchemaType(@PathVariable String                    serverName,
                                             @PathVariable String                    userId,
                                             @PathVariable String                    validValuesSetGUID,
                                             @RequestBody  EnumSchemaTypeRequestBody requestBody)
    {
        return restAPI.createEnumSchemaType(serverName, userId, validValuesSetGUID, requestBody);
    }


    /**
     * Retrieve the list of valid value set metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param requestBody name to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/valid-value-sets/by-name")

    public ValidValueSetsResponse getValidValueSetByName(@PathVariable String          serverName,
                                                         @PathVariable String          userId,
                                                         @RequestBody  NameRequestBody requestBody,
                                                         @RequestParam int             startFrom,
                                                         @RequestParam int             pageSize)
    {
        return restAPI.getValidValueSetByName(serverName, userId, requestBody, startFrom, pageSize);
    }


    /**
     * Retrieve the list of valid value set metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param requestBody string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/valid-value-sets/by-search-string")

    public ValidValueSetsResponse findValidValueSet(@PathVariable String                  serverName,
                                                    @PathVariable String                  userId,
                                                    @RequestBody  SearchStringRequestBody requestBody,
                                                    @RequestParam int                     startFrom,
                                                    @RequestParam int                     pageSize)
    {
        return restAPI.findValidValueSet(serverName, userId, requestBody, startFrom, pageSize);
    }


    /**
     * Create a new metadata element to represent a schema type.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param requestBody properties about the schema type to store
     *
     * @return unique identifier of the new schema type or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/schema-types/structs")

    public GUIDResponse createStructSchemaType(@PathVariable String                      serverName,
                                               @PathVariable String                      userId,
                                               @RequestBody  StructSchemaTypeRequestBody requestBody)
    {
        return restAPI.createStructSchemaType(serverName, userId, requestBody);
    }


    /**
     * Create a new metadata element to represent a list of possible schema types that can be used for the attached schema attribute.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param requestBody properties about the schema type to store
     *
     * @return unique identifier of the new schema type or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/schema-types/choices")

    public GUIDResponse createSchemaTypeChoice(@PathVariable String                      serverName,
                                               @PathVariable String                      userId,
                                               @RequestBody  SchemaTypeChoiceRequestBody requestBody)
    {
        return restAPI.createSchemaTypeChoice(serverName, userId, requestBody);
    }


    /**
     * Create a new metadata element to represent a schema type.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param mapFromSchemaTypeGUID unique identifier of the domain of the map
     * @param mapToSchemaTypeGUID unique identifier of the range of the map
     * @param requestBody properties about the schema type to store
     *
     * @return unique identifier of the new schema type or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/schema-types/maps/from/{mapFromSchemaTypeGUID}/to/{mapToSchemaTypeGUID}")

    public GUIDResponse createMapSchemaType(@PathVariable String                   serverName,
                                            @PathVariable String                   userId,
                                            @PathVariable String                   mapFromSchemaTypeGUID,
                                            @PathVariable String                   mapToSchemaTypeGUID,
                                            @RequestBody  MapSchemaTypeRequestBody requestBody)
    {
        return restAPI.createMapSchemaType(serverName, userId, mapFromSchemaTypeGUID, mapToSchemaTypeGUID, requestBody);
    }


    /**
     * Create a new metadata element to represent a schema type using an existing metadata element as a template.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param templateGUID unique identifier of the metadata element to copy
     * @param requestBody properties that override the template
     *
     * @return unique identifier of the new schema type or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/schema-types/from-template/{templateGUID}")

    public GUIDResponse createSchemaTypeFromTemplate(@PathVariable String              serverName,
                                                     @PathVariable String              userId,
                                                     @PathVariable String              templateGUID,
                                                     @RequestBody  TemplateRequestBody requestBody)
    {
        return restAPI.createSchemaTypeFromTemplate(serverName, userId, templateGUID, requestBody);
    }


    /**
     * Update the metadata element representing a schema type.  It is possible to use the subtype property classes or
     * set up specialized properties in extended properties.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param schemaTypeGUID unique identifier of the metadata element to update
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param requestBody new properties for the metadata element
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/schema-types/{schemaTypeGUID}")

    public VoidResponse updateSchemaType(@PathVariable String                serverName,
                                         @PathVariable String                userId,
                                         @PathVariable String                schemaTypeGUID,
                                         @RequestParam boolean               isMergeUpdate,
                                         @RequestBody  SchemaTypeRequestBody requestBody)
    {
        return restAPI.updateSchemaType(serverName, userId, schemaTypeGUID, isMergeUpdate, requestBody);
    }


    /**
     * Remove the metadata element representing a schema type.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param schemaTypeGUID unique identifier of the metadata element to remove
     * @param requestBody optional identifiers for the external source
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/schema-types/{schemaTypeGUID}/delete")

    public VoidResponse removeSchemaType(@PathVariable String                    serverName,
                                         @PathVariable String                    userId,
                                         @PathVariable String                    schemaTypeGUID,
                                         @RequestBody  MetadataSourceRequestBody requestBody)
    {
        return restAPI.removeSchemaType(serverName, userId, schemaTypeGUID, requestBody);
    }


    /**
     * Create a relationship between two schema elements.  The name of the desired relationship, and any properties (including effectivity dates)
     * are passed on the API.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param endOneGUID unique identifier of the schema element at end one of the relationship
     * @param endTwoGUID unique identifier of the schema element at end two of the relationship
     * @param relationshipTypeName type of the relationship to delete
     * @param requestBody new properties for the metadata element
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "schema-elements/{endOneGUID}/relationships/{relationshipTypeName}/schema-elements/{endTwoGUID}")

    public VoidResponse setupSchemaElementRelationship(@PathVariable String                    serverName,
                                                       @PathVariable String                    userId,
                                                       @PathVariable String                    endOneGUID,
                                                       @PathVariable String                    relationshipTypeName,
                                                       @PathVariable String                    endTwoGUID,
                                                       @RequestBody(required = false)
                                                                     RelationshipRequestBody   requestBody)
    {
        return restAPI.setupSchemaElementRelationship(serverName, userId, endOneGUID, relationshipTypeName, endTwoGUID, requestBody);
    }


    /**
     * Remove a relationship between two schema elements.  The name of the desired relationship is passed on the API.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param endOneGUID unique identifier of the schema element at end one of the relationship
     * @param endTwoGUID unique identifier of the schema element at end two of the relationship
     * @param relationshipTypeName type of the relationship to delete
     * @param requestBody optional identifiers for the external source
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "schema-elements/{endOneGUID}/relationships/{relationshipTypeName}/schema-elements/{endTwoGUID}/delete")

    public VoidResponse clearSchemaElementRelationship(@PathVariable String                    serverName,
                                                       @PathVariable String                    userId,
                                                       @PathVariable String                    endOneGUID,
                                                       @PathVariable String                    relationshipTypeName,
                                                       @PathVariable String                    endTwoGUID,
                                                       @RequestBody(required = false)
                                                                     MetadataSourceRequestBody requestBody)
    {
        return restAPI.clearSchemaElementRelationship(serverName, userId, endOneGUID, relationshipTypeName, endTwoGUID, requestBody);
    }


    /**
     * Retrieve the list of schema type metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param requestBody string to find in the properties
     * @param typeName optional type name for the schema type - used to restrict the search results
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/schema-types/types/{typeName}/by-search-string")

    public SchemaTypesResponse findSchemaType(@PathVariable String                  serverName,
                                              @PathVariable String                  userId,
                                              @PathVariable String                  typeName,
                                              @RequestBody  SearchStringRequestBody requestBody,
                                              @RequestParam int                     startFrom,
                                              @RequestParam int                     pageSize)
    {
        return restAPI.findSchemaType(serverName, userId, typeName, requestBody, startFrom, pageSize);
    }


    /**
     * Return the schema type associated with a specific open metadata element (data asset, process or port).
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param parentElementGUID unique identifier of the open metadata element that this schema type is connected to
     * @param parentElementTypeName unique type name of the open metadata element that this schema type is connected to
     *
     * @return metadata element describing the schema type associated with the requested parent element or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @GetMapping(path = "/schema-types/types/{parentElementTypeName}/by-parent-element/{parentElementGUID}")

    public SchemaTypeResponse getSchemaTypeForElement(@PathVariable String serverName,
                                                      @PathVariable String userId,
                                                      @PathVariable String parentElementGUID,
                                                      @PathVariable String parentElementTypeName)
    {
        return restAPI.getSchemaTypeForElement(serverName, userId, parentElementGUID, parentElementTypeName);
    }


    /**
     * Retrieve the list of schema type metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param requestBody name to search for
     * @param typeName optional type name for the schema type - used to restrict the search results
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/schema-types/types/{typeName}/by-name")

    public SchemaTypesResponse getSchemaTypeByName(@PathVariable String          serverName,
                                                   @PathVariable String          userId,
                                                   @PathVariable String          typeName,
                                                   @RequestBody  NameRequestBody requestBody,
                                                   @RequestParam int             startFrom,
                                                   @RequestParam int             pageSize)
    {
        return restAPI.getSchemaTypeByName(serverName, userId, typeName, requestBody, startFrom, pageSize);
    }


    /**
     * Retrieve the schema type metadata element with the supplied unique identifier.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param schemaTypeGUID unique identifier of the requested metadata element
     *
     * @return requested metadata element or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @GetMapping(path = "/schema-types/{schemaTypeGUID}")

    public SchemaTypeResponse getSchemaTypeByGUID(@PathVariable String serverName,
                                                  @PathVariable String userId,
                                                  @PathVariable String schemaTypeGUID)
    {
        return restAPI.getSchemaTypeByGUID(serverName, userId, schemaTypeGUID);
    }


    /**
     * Retrieve the header of the metadata element connected to a schema type.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param schemaTypeGUID unique identifier of the requested metadata element
     *
     * @return header for parent element (data asset, process, port) plus qualified name or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @GetMapping(path = "/schema-types/{schemaTypeGUID}/parent")

    public ElementStubResponse getSchemaTypeParent(@PathVariable String serverName,
                                                   @PathVariable String userId,
                                                   @PathVariable String schemaTypeGUID)
    {
        return restAPI.getSchemaTypeParent(serverName, userId, schemaTypeGUID);
    }


    /* ===============================================================================
     * A schemaType typically contains many schema attributes, linked with relationships.
     */

    /**
     * Create a new metadata element to represent a schema attribute.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param schemaElementGUID unique identifier of the schemaType or Schema Attribute where the schema attribute is nested underneath
     * @param requestBody properties for the schema attribute
     *
     * @return unique identifier of the new metadata element for the schema attribute or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/schema-attributes/attached-to/{schemaElementGUID}")

    public GUIDResponse createSchemaAttribute(@PathVariable String                     serverName,
                                              @PathVariable String                     userId,
                                              @PathVariable String                     schemaElementGUID,
                                              @RequestBody  SchemaAttributeRequestBody requestBody)
    {
        return restAPI.createSchemaAttribute(serverName, userId, schemaElementGUID, requestBody);
    }


    /**
     * Create a new metadata element to represent a schema attribute using an existing metadata element as a template.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param schemaElementGUID unique identifier of the schemaType or Schema Attribute where the schema attribute is connected to
     * @param templateGUID unique identifier of the metadata element to copy
     * @param requestBody properties that override the template
     *
     * @return unique identifier of the new metadata element for the schema attribute or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/schema-attributes/from-template/{templateGUID}/attached-to/{schemaElementGUID}")

    public GUIDResponse createSchemaAttributeFromTemplate(@PathVariable String              serverName,
                                                          @PathVariable String              userId,
                                                          @PathVariable String              schemaElementGUID,
                                                          @PathVariable String              templateGUID,
                                                          @RequestBody  TemplateRequestBody requestBody)
    {
        return restAPI.createSchemaAttributeFromTemplate(serverName, userId, schemaElementGUID, templateGUID, requestBody);
    }


    /**
     * Connect a schema type to a schema attribute.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param relationshipTypeName name of relationship to create
     * @param schemaAttributeGUID unique identifier of the schema attribute
     * @param schemaTypeGUID unique identifier of the schema type to connect
     * @param requestBody unique identifier of software server capability representing the caller
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/schema-attributes/{schemaAttributeGUID}/schema-types/{schemaTypeGUID}/relationship-type-name/{relationshipTypeName}")

    public VoidResponse setupSchemaType(@PathVariable String                    serverName,
                                        @PathVariable String                    userId,
                                        @PathVariable String                    relationshipTypeName,
                                        @PathVariable String                    schemaAttributeGUID,
                                        @PathVariable String                    schemaTypeGUID,
                                        @RequestBody  MetadataSourceRequestBody requestBody)
    {
        return restAPI.setupSchemaType(serverName, userId, relationshipTypeName, schemaAttributeGUID, schemaTypeGUID, requestBody);
    }


    /**
     * Remove the linked schema types from a schema attribute.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param schemaAttributeGUID unique identifier of the schema attribute
     * @param requestBody unique identifier of software server capability representing the caller
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/schema-attributes/{schemaAttributeGUID}/schema-types/delete")

    public VoidResponse clearSchemaTypes(@PathVariable String                    serverName,
                                         @PathVariable String                    userId,
                                         @PathVariable String                    schemaAttributeGUID,
                                         @RequestBody  MetadataSourceRequestBody requestBody)
    {
        return restAPI.clearSchemaTypes(serverName, userId, schemaAttributeGUID, requestBody);
    }


    /**
     * Update the properties of the metadata element representing a schema attribute.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param schemaAttributeGUID unique identifier of the schema attribute to update
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param requestBody new properties for the schema attribute
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/schema-attributes/{schemaAttributeGUID}")

    public VoidResponse updateSchemaAttribute(@PathVariable String                     serverName,
                                              @PathVariable String                     userId,
                                              @PathVariable String                     schemaAttributeGUID,
                                              @RequestParam boolean                    isMergeUpdate,
                                              @RequestBody  SchemaAttributeRequestBody requestBody)
    {
        return restAPI.updateSchemaAttribute(serverName, userId, schemaAttributeGUID, isMergeUpdate, requestBody);
    }


    /**
     * Remove the metadata element representing a schema attribute.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param requestBody unique identifier of software server capability representing the caller
     * @param schemaAttributeGUID unique identifier of the metadata element to remove
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/schema-attributes/{schemaAttributeGUID}/delete")

    public VoidResponse removeSchemaAttribute(@PathVariable String                    serverName,
                                              @PathVariable String                    userId,
                                              @PathVariable String                    schemaAttributeGUID,
                                              @RequestBody  MetadataSourceRequestBody requestBody)
    {
        return restAPI.removeSchemaAttribute(serverName, userId, schemaAttributeGUID, requestBody);
    }


    /**
     * Retrieve the list of schema attribute metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param requestBody string to find in the properties
     * @param typeName optional type name for the schema attribute - used to restrict the search results
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/schema-attributes/types/{typeName}/by-search-string")

    public SchemaAttributesResponse findSchemaAttributes(@PathVariable String                  serverName,
                                                         @PathVariable String                  userId,
                                                         @PathVariable String                  typeName,
                                                         @RequestBody  SearchStringRequestBody requestBody,
                                                         @RequestParam int                     startFrom,
                                                         @RequestParam int                     pageSize)
    {
        return restAPI.findSchemaAttributes(serverName, userId, typeName, requestBody, startFrom, pageSize);
    }


    /**
     * Retrieve the list of schema attributes associated with a StructSchemaType or nested underneath a schema attribute.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param parentSchemaElementGUID unique identifier of the schema element of interest
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of associated metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @GetMapping(path = "/schema-elements/{parentSchemaElementGUID}/nested-attributes")

    public SchemaAttributesResponse getNestedAttributes(@PathVariable String serverName,
                                                        @PathVariable String userId,
                                                        @PathVariable String parentSchemaElementGUID,
                                                        @RequestParam int    startFrom,
                                                        @RequestParam int    pageSize)
    {
        return restAPI.getNestedAttributes(serverName, userId, parentSchemaElementGUID, startFrom, pageSize);
    }


    /**
     * Retrieve the list of schema attribute metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param requestBody name to search for
     * @param typeName optional type name for the schema attribute - used to restrict the search results
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/schema-attributes/types/{typeName}/by-name")

    public SchemaAttributesResponse getSchemaAttributesByName(@PathVariable String          serverName,
                                                              @PathVariable String          userId,
                                                              @PathVariable String          typeName,
                                                              @RequestBody  NameRequestBody requestBody,
                                                              @RequestParam int             startFrom,
                                                              @RequestParam int             pageSize)
    {
        return restAPI.getSchemaAttributesByName(serverName, userId, typeName, requestBody, startFrom, pageSize);
    }


    /**
     * Retrieve the schema attribute metadata element with the supplied unique identifier.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param schemaAttributeGUID unique identifier of the requested metadata element
     *
     * @return matching metadata element or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @GetMapping(path = "/schema-attributes/{schemaAttributeGUID}")

    public SchemaAttributeResponse getSchemaAttributeByGUID(@PathVariable String serverName,
                                                            @PathVariable String userId,
                                                            @PathVariable String schemaAttributeGUID)
    {
        return restAPI.getSchemaAttributeByGUID(serverName, userId, schemaAttributeGUID);
    }


    /* =====================================================================================================================
     * Working with derived values
     */


    /**
     * Classify the schema element to indicate that it describes a calculated value.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param schemaElementGUID unique identifier of the metadata element to update
     * @param requestBody requestBody for calculating the value - this may contain placeholders that are identified by the
     *                queryIds used in the queryTarget relationships
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/schema-elements/{schemaElementGUID}/calculated-value")

    public VoidResponse setupCalculatedValue(@PathVariable String             serverName,
                                             @PathVariable String             userId,
                                             @PathVariable String             schemaElementGUID,
                                             @RequestBody  FormulaRequestBody requestBody)
    {
        return restAPI.setupCalculatedValue(serverName, userId, schemaElementGUID, requestBody);
    }


    /**
     * Remove the calculated value designation from the schema element.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param schemaElementGUID unique identifier of the metadata element to update
     * @param requestBody unique identifier of software server capability representing the caller
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/schema-elements/{schemaElementGUID}/calculated-value/delete")

    public VoidResponse clearCalculatedValue(@PathVariable String                    serverName,
                                             @PathVariable String                    userId,
                                             @PathVariable String                    schemaElementGUID,
                                             @RequestBody  MetadataSourceRequestBody requestBody)
    {
        return restAPI.clearCalculatedValue(serverName, userId, schemaElementGUID, requestBody);
    }


    /**
     * Link two schema elements together to show a query target relationship.  The query target provides
     * data values to calculate a derived value.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param derivedElementGUID unique identifier of the derived schema element
     * @param queryTargetGUID unique identifier of the query target schema element
     * @param requestBody properties for the query target relationship
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/schema-elements/{derivedElementGUID}/query-targets/{queryTargetGUID}")

    public VoidResponse setupQueryTargetRelationship(@PathVariable String                                  serverName,
                                                     @PathVariable String                                  userId,
                                                     @PathVariable String                                  derivedElementGUID,
                                                     @PathVariable String                                  queryTargetGUID,
                                                     @RequestBody  DerivedSchemaTypeQueryTargetRequestBody requestBody)

    {
        return restAPI.setupQueryTargetRelationship(serverName, userId, derivedElementGUID, queryTargetGUID, requestBody);
    }


    /**
     * Update the relationship properties for the query target.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param derivedElementGUID unique identifier of the derived schema element
     * @param queryTargetGUID unique identifier of the query target schema element
     * @param requestBody properties for the query target relationship
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/schema-elements/{derivedElementGUID}/query-targets/{queryTargetGUID}/update")

    public VoidResponse updateQueryTargetRelationship(@PathVariable String                                  serverName,
                                                      @PathVariable String                                  userId,
                                                      @PathVariable String                                  derivedElementGUID,
                                                      @PathVariable String                                  queryTargetGUID,
                                                      @RequestBody  DerivedSchemaTypeQueryTargetRequestBody requestBody)
    {
        return restAPI.updateQueryTargetRelationship(serverName, userId, derivedElementGUID, queryTargetGUID, requestBody);
    }


    /**
     * Remove the query target relationship between two schema elements.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param requestBody unique identifier of software server capability representing the caller
     * @param derivedElementGUID unique identifier of the derived schema element
     * @param queryTargetGUID unique identifier of the query target schema element
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/schema-elements/{derivedElementGUID}/query-targets/{queryTargetGUID}/delete")

    public VoidResponse clearQueryTargetRelationship(@PathVariable String                    serverName,
                                                     @PathVariable String                    userId,
                                                     @PathVariable String                    derivedElementGUID,
                                                     @PathVariable String                    queryTargetGUID,
                                                     @RequestBody  MetadataSourceRequestBody requestBody)
    {
        return restAPI.clearQueryTargetRelationship(serverName, userId, derivedElementGUID, queryTargetGUID, requestBody);
    }
}
