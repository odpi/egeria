/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetmanager.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.accessservices.assetmanager.properties.MetadataCorrelationProperties;
import org.odpi.openmetadata.accessservices.assetmanager.rest.*;
import org.odpi.openmetadata.accessservices.assetmanager.server.SchemaExchangeRESTServices;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.springframework.web.bind.annotation.*;

/**
 * DataAssetExchangeResource is the server-side implementation of the Asset Manager OMAS's
 * support for data asset such as data sets.  It matches the DataAssetExchangeClient.
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/access-services/asset-manager/users/{userId}")

@Tag(name="Asset Manager OMAS",
     description="The Asset Manager OMAS provides APIs and events for managing metadata exchange with third party asset managers, such as data catalogs.",
     externalDocs=@ExternalDocumentation(description="Asset Manager Open Metadata Access Service (OMAS)",
                                         url="https://egeria.odpi.org/open-metadata-implementation/access-services/asset-manager/"))

public class SchemaExchangeResource
{
    private SchemaExchangeRESTServices restAPI = new SchemaExchangeRESTServices();


    /**
     * Default constructor
     */
    public SchemaExchangeResource()
    {
    }



    /* =====================================================================================================================
     * A schemaType describes the structure of a data asset, process or port
     */

    /**
     * Create a new metadata element to represent a schema type.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param assetManagerIsHome ensure that only the asset manager can update this schema element
     * @param requestBody properties about the schema type to store
     *
     * @return unique identifier of the new schema type
     *
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/schema-types")

    public GUIDResponse createSchemaType(@PathVariable String                serverName,
                                         @PathVariable String                userId,
                                         @RequestParam boolean               assetManagerIsHome,
                                         @RequestBody  SchemaTypeRequestBody requestBody)
    {
        return restAPI.createSchemaType(serverName, userId, assetManagerIsHome, requestBody);
    }


    /**
     * Create a new metadata element to represent a schema type using an existing metadata element as a template.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param templateGUID unique identifier of the metadata element to copy
     * @param assetManagerIsHome ensure that only the asset manager can update this schema element
     * @param requestBody properties that override the template
     *
     * @return unique identifier of the new schema type
     *
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/schema-types/from-template/{templateGUID}")

    public GUIDResponse createSchemaTypeFromTemplate(@PathVariable String              serverName,
                                                     @PathVariable String              userId,
                                                     @PathVariable String              templateGUID,
                                                     @RequestParam boolean             assetManagerIsHome,
                                                     @RequestBody  TemplateRequestBody requestBody)
    {
        return restAPI.createSchemaTypeFromTemplate(serverName, userId, templateGUID, assetManagerIsHome, requestBody);
    }


    /**
     * Update the metadata element representing a schema type.
     *
     * @param serverName name of the server to route the request to
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
     * Connect a schema type to a data asset, process or port.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param schemaTypeGUID unique identifier of the schema type to connect
     * @param parentElementGUID unique identifier of the open metadata element that this schema type is to be connected to
     * @param parentElementTypeName unique type name of the open metadata element that this schema type is to be connected to
     * @param assetManagerIsHome ensure that only the asset manager can update this relationship
     * @param requestBody unique identifier/name of software server capability representing the caller
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/parents/{parentElementGUID}/{parentElementTypeName}/schema-types/{schemaTypeGUID}")

    public VoidResponse setupSchemaTypeParent(@PathVariable String                             serverName,
                                              @PathVariable String                             userId,
                                              @PathVariable String                             parentElementGUID,
                                              @PathVariable String                             parentElementTypeName,
                                              @PathVariable String                             schemaTypeGUID,
                                              @RequestParam boolean                            assetManagerIsHome,
                                              @RequestBody  AssetManagerIdentifiersRequestBody requestBody)
    {
        return restAPI.setupSchemaTypeParent(serverName, userId, parentElementGUID, parentElementTypeName, schemaTypeGUID, assetManagerIsHome, requestBody);
    }


    /**
     * Remove the relationship between a schema type and its parent data asset, process or port.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param schemaTypeGUID unique identifier of the schema type to connect
     * @param parentElementGUID unique identifier of the open metadata element that this schema type is to be connected to
     * @param parentElementTypeName unique type name of the open metadata element that this schema type is to be connected to
     * @param requestBody unique identifier/name of software server capability representing the caller
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/parents/{parentElementGUID}/{parentElementTypeName}/schema-types/{schemaTypeGUID}/remove")

    public VoidResponse clearSchemaTypeParent(@PathVariable String                             serverName,
                                              @PathVariable String                             userId,
                                              @PathVariable String                             parentElementGUID,
                                              @PathVariable String                             parentElementTypeName,
                                              @PathVariable String                             schemaTypeGUID,
                                              @RequestBody  AssetManagerIdentifiersRequestBody requestBody)
    {
        return restAPI.clearSchemaTypeParent(serverName, userId, parentElementGUID, parentElementTypeName, schemaTypeGUID, requestBody);
    }


    /**
     * Remove the metadata element representing a schema type.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param schemaTypeGUID unique identifier of the metadata element to remove
     * @param requestBody unique identifier/name of software server capability representing the caller and external identifier of element
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/schema-types/{schemaTypeGUID}/remove")

    public VoidResponse removeSchemaType(@PathVariable String                        serverName,
                                         @PathVariable String                        userId,
                                         @PathVariable String                        schemaTypeGUID,
                                         @RequestBody  MetadataCorrelationProperties requestBody)
    {
        return restAPI.removeSchemaType(serverName, userId, schemaTypeGUID, requestBody);
    }


    /**
     * Retrieve the list of schema type metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody string to find in the properties plus external identifiers
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/schema-types/by-search-string")

    public SchemaTypeElementsResponse findSchemaType(@PathVariable String                  serverName,
                                                     @PathVariable String                  userId,
                                                     @RequestParam int                     startFrom,
                                                     @RequestParam int                     pageSize,
                                                     @RequestBody  SearchStringRequestBody requestBody)
    {
        return restAPI.findSchemaType(serverName, userId, startFrom, pageSize, requestBody);
    }


    /**
     * Return the schema type associated with a specific open metadata element (data asset, process or port).
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param parentElementGUID unique identifier of the open metadata element that this schema type is to be connected to
     * @param parentElementTypeName unique type name of the open metadata element that this schema type is to be connected to
     * @param requestBody unique identifier/name of software server capability representing the caller
     *
     * @return metadata element describing the schema type associated with the requested parent element or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/parents/{parentElementGUID}/schema-types/retrieve")

    public SchemaTypeElementResponse getSchemaTypeForElement(@PathVariable String                             serverName,
                                                             @PathVariable String                             userId,
                                                             @PathVariable String                             parentElementGUID,
                                                             @PathVariable String                             parentElementTypeName,
                                                             @RequestBody  AssetManagerIdentifiersRequestBody requestBody)
    {
        return restAPI.getSchemaTypeForElement(serverName, userId, parentElementGUID, parentElementTypeName, requestBody);
    }


    /**
     * Retrieve the list of schema type metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody name to search for plus identifiers
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/schema-types/by-name")

    public SchemaTypeElementsResponse   getSchemaTypeByName(@PathVariable String          serverName,
                                                            @PathVariable String          userId,
                                                            @RequestParam int             startFrom,
                                                            @RequestParam int             pageSize,
                                                            @RequestBody  NameRequestBody requestBody)
    {
        return restAPI.getSchemaTypeByName(serverName, userId, startFrom, pageSize, requestBody);
    }


    /**
     * Retrieve the schema type metadata element with the supplied unique identifier.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param schemaTypeGUID unique identifier of the requested metadata element
     * @param requestBody unique identifier/name of software server capability representing the caller
     *
     * @return requested metadata element or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/schema-types/{schemaTypeGUID}/retrieve")

    public SchemaTypeElementResponse getSchemaTypeByGUID(@PathVariable String                             serverName,
                                                         @PathVariable String                             userId,
                                                         @PathVariable String                             schemaTypeGUID,
                                                         @RequestBody  AssetManagerIdentifiersRequestBody requestBody)
    {
        return restAPI.getSchemaTypeByGUID(serverName, userId, schemaTypeGUID, requestBody);
    }


    /**
     * Retrieve the header of the metadata element connected to a schema type.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param schemaTypeGUID unique identifier of the requested metadata element
     * @param requestBody unique identifier/name of software server capability representing the caller
     *
     * @return header for parent element (data asset, process, port) or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/parents/schema-types/{schemaTypeGUID}/retrieve")

    public ElementHeaderResponse getSchemaTypeParent(@PathVariable String                             serverName,
                                                     @PathVariable String                             userId,
                                                     @PathVariable String                             schemaTypeGUID,
                                                     @RequestBody  AssetManagerIdentifiersRequestBody requestBody)
    {
        return restAPI.getSchemaTypeParent(serverName, userId, schemaTypeGUID, requestBody);
    }


    /* ===============================================================================
     * A schemaType typically contains many schema attributes, linked with relationships.
     */

    /**
     * Create a new metadata element to represent a schema attribute.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param assetManagerIsHome ensure that only the asset manager can update this schema attribute
     * @param schemaElementGUID unique identifier of the schemaType or Schema Attribute where the schema attribute is connected to
     * @param requestBody properties for the schema attribute
     *
     * @return unique identifier of the new metadata element for the schema attribute or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/schema-elements/{schemaElementGUID}/schema-attribute")

    public GUIDResponse createSchemaAttribute(@PathVariable String                     serverName,
                                              @PathVariable String                     userId,
                                              @PathVariable String                     schemaElementGUID,
                                              @RequestParam boolean                    assetManagerIsHome,
                                              @RequestBody  SchemaAttributeRequestBody requestBody)
    {
        return restAPI.createSchemaAttribute(serverName, userId, schemaElementGUID, assetManagerIsHome, requestBody);
    }


    /**
     * Create a new metadata element to represent a schema attribute using an existing metadata element as a template.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param schemaElementGUID unique identifier of the schemaType or Schema Attribute where the schema attribute is connected to
     * @param templateGUID unique identifier of the metadata element to copy
     * @param assetManagerIsHome ensure that only the asset manager can update this schema element
     * @param requestBody properties that override the template
     *
     * @return unique identifier of the new metadata element for the schema attribute or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/schema-elements/{schemaElementGUID}/schema-attributes/from-template/{templateGUID}")

    public GUIDResponse createSchemaAttributeFromTemplate(@PathVariable String              serverName,
                                                          @PathVariable String              userId,
                                                          @PathVariable String              schemaElementGUID,
                                                          @PathVariable String              templateGUID,
                                                          @RequestParam boolean             assetManagerIsHome,
                                                          @RequestBody  TemplateRequestBody requestBody)
    {
        return restAPI.createSchemaAttributeFromTemplate(serverName, userId, schemaElementGUID, templateGUID, assetManagerIsHome, requestBody);
    }


    /**
     * Update the properties of the metadata element representing a schema attribute.
     *
     * @param serverName name of the server to route the request to
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
     * Classify the schema type (or attribute if type is embedded) to indicate that it is a calculated value.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param schemaElementGUID unique identifier of the metadata element to update
     * @param assetManagerIsHome ensure that only the asset manager can update this classification
     * @param requestBody unique identifier/name of software server capability representing the caller and external identifier of element
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/schema-elements/{schemaElementGUID}/is-calculated-value")

    public VoidResponse setSchemaElementAsCalculatedValue(@PathVariable String                        serverName,
                                                          @PathVariable String                        userId,
                                                          @PathVariable String                        schemaElementGUID,
                                                          @RequestParam boolean                       assetManagerIsHome,
                                                          @RequestBody  MetadataCorrelationProperties requestBody)
    {
        return restAPI.setSchemaElementAsCalculatedValue(serverName, userId, schemaElementGUID, assetManagerIsHome, requestBody);
    }


    /**
     * Remove the calculated value designation from the schema element.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param schemaElementGUID unique identifier of the metadata element to update
     * @param requestBody unique identifier/name of software server capability representing the caller and external identifier of element
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/schema-elements/{schemaElementGUID}/is-calculated-value/remove")

    public VoidResponse clearSchemaElementAsCalculatedValue(@PathVariable String                        serverName,
                                                            @PathVariable String                        userId,
                                                            @PathVariable String                        schemaElementGUID,
                                                            @RequestBody  MetadataCorrelationProperties requestBody)
    {
        return restAPI.clearSchemaElementAsCalculatedValue(serverName, userId, schemaElementGUID, requestBody);
    }


    /**
     * Classify the column schema attribute to indicate that it describes a primary key.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param assetManagerIsHome ensure that only the asset manager can update this classification
     * @param schemaAttributeGUID unique identifier of the metadata element to update
     * @param requestBody details of the primary key plus external identifiers
     *
     * @return null or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/schema-attributes/{schemaAttributeGUID}/is-primary-key")

    public VoidResponse setupColumnAsPrimaryKey(@PathVariable String                              serverName,
                                                @PathVariable String                              userId,
                                                @PathVariable String                              schemaAttributeGUID,
                                                @RequestParam boolean                             assetManagerIsHome,
                                                @RequestBody  PrimaryKeyClassificationRequestBody requestBody)
    {
        return restAPI.setupColumnAsPrimaryKey(serverName, userId, schemaAttributeGUID, assetManagerIsHome, requestBody);
    }


    /**
     * Remove the primary key designation from the schema attribute.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param schemaAttributeGUID unique identifier of the metadata element to update
     * @param requestBody unique identifier/name of software server capability representing the caller and external identifier of element
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/schema-attributes/{schemaAttributeGUID}/is-primary-key/remove")

    public VoidResponse clearColumnAsPrimaryKey(@PathVariable String                        serverName,
                                                @PathVariable String                        userId,
                                                @PathVariable String                        schemaAttributeGUID,
                                                @RequestBody  MetadataCorrelationProperties requestBody)
    {
        return restAPI.clearColumnAsPrimaryKey(serverName, userId, schemaAttributeGUID, requestBody);
    }


    /**
     * Link two schema attributes together to show a foreign key relationship.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param primaryKeyGUID unique identifier of the derived schema element
     * @param foreignKeyGUID unique identifier of the query target schema element
     * @param assetManagerIsHome ensure that only the asset manager can update this relationship
     * @param requestBody properties for the foreign key relationship
     *
     * @return  void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/schema-attributes/{primaryKeyGUID}/relationships/foreign-keys")

    public VoidResponse setupForeignKeyRelationship(@PathVariable String                serverName,
                                                    @PathVariable String                userId,
                                                    @PathVariable String                primaryKeyGUID,
                                                    @PathVariable String                foreignKeyGUID,
                                                    @RequestParam boolean               assetManagerIsHome,
                                                    @RequestBody  ForeignKeyRequestBody requestBody)
    {
        return restAPI.setupForeignKeyRelationship(serverName, userId, primaryKeyGUID, foreignKeyGUID, assetManagerIsHome, requestBody);
    }


    /**
     * Update the relationship properties for the query target.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param primaryKeyGUID unique identifier of the derived schema element
     * @param foreignKeyGUID unique identifier of the query target schema element
     * @param requestBody properties for the foreign key relationship plus external identifiers
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/schema-attributes/{primaryKeyGUID}/relationships/foreign-keys/update")

    public VoidResponse updateForeignKeyRelationship(@PathVariable String                serverName,
                                                     @PathVariable String                userId,
                                                     @PathVariable String                primaryKeyGUID,
                                                     @PathVariable String                foreignKeyGUID,
                                                     @RequestBody  ForeignKeyRequestBody requestBody)
    {
        return restAPI.updateForeignKeyRelationship(serverName, userId, primaryKeyGUID, foreignKeyGUID, requestBody);
    }


    /**
     * Remove the foreign key relationship between two schema elements.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param primaryKeyGUID unique identifier of the derived schema element
     * @param foreignKeyGUID unique identifier of the query target schema element
     * @param requestBody unique identifier/name of software server capability representing the caller
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/schema-attributes/{primaryKeyGUID}/relationships/foreign-keys/remove")

    public VoidResponse clearForeignKeyRelationship(@PathVariable String                             serverName,
                                                    @PathVariable String                             userId,
                                                    @PathVariable String                             primaryKeyGUID,
                                                    @PathVariable String                             foreignKeyGUID,
                                                    @RequestBody  AssetManagerIdentifiersRequestBody requestBody)
    {
        return restAPI.clearForeignKeyRelationship(serverName, userId, primaryKeyGUID, foreignKeyGUID, requestBody);
    }


    /**
     * Remove the metadata element representing a schema attribute.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param schemaAttributeGUID unique identifier of the metadata element to remove
     * @param requestBody unique identifier/name of software server capability representing the caller and external identifier of element
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/schema-attributes/{schemaAttributeGUID}/remove")

    public VoidResponse removeSchemaAttribute(@PathVariable String                        serverName,
                                              @PathVariable String                        userId,
                                              @PathVariable String                        schemaAttributeGUID,
                                              @RequestBody  MetadataCorrelationProperties requestBody)
    {
        return restAPI.removeSchemaAttribute(serverName, userId, schemaAttributeGUID, requestBody);
    }


    /**
     * Retrieve the list of schema attribute metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody string to find in the properties plus external identifiers
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/schema-attributes/by-search-string")

    public SchemaAttributeElementsResponse findSchemaAttributes(@PathVariable String                  serverName,
                                                                @PathVariable String                  userId,
                                                                @RequestParam int                     startFrom,
                                                                @RequestParam int                     pageSize,
                                                                @RequestBody  SearchStringRequestBody requestBody)
    {
        return restAPI.findSchemaAttributes(serverName, userId, startFrom, pageSize, requestBody);
    }


    /**
     * Retrieve the list of schema attributes associated with a schemaType.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param schemaTypeGUID unique identifier of the schemaType of interest
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody unique identifier/name of software server capability representing the caller
     *
     * @return list of associated metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/schema-types/{schemaTypeGUID}/schema-attributes/retrieve")

    public SchemaAttributeElementsResponse getAttributesForSchemaType(@PathVariable String                             serverName,
                                                                      @PathVariable String                             userId,
                                                                      @PathVariable String                             schemaTypeGUID,
                                                                      @RequestParam int                                startFrom,
                                                                      @RequestParam int                                pageSize,
                                                                      @RequestBody  AssetManagerIdentifiersRequestBody requestBody)
    {
        return restAPI.getAttributesForSchemaType(serverName, userId, schemaTypeGUID, startFrom, pageSize, requestBody);
    }


    /**
     * Retrieve the list of schema attribute metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody unique identifier/name of software server capability representing the caller
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/schema-attributes/by-name")

    public SchemaAttributeElementsResponse getSchemaAttributesByName(@PathVariable String          serverName,
                                                                     @PathVariable String          userId,
                                                                     @RequestParam int             startFrom,
                                                                     @RequestParam int             pageSize,
                                                                     @PathVariable NameRequestBody requestBody)
    {
        return restAPI.getSchemaAttributesByName(serverName, userId, startFrom, pageSize, requestBody);
    }


    /**
     * Retrieve the schema attribute metadata element with the supplied unique identifier.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param schemaAttributeGUID unique identifier of the requested metadata element
     * @param requestBody unique identifier/name of software server capability representing the caller
     *
     * @return matching metadata element or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/schema-attributes/{schemaAttributeGUID}/retrieve")

    public SchemaAttributeElementResponse getSchemaAttributeByGUID(@PathVariable String                             serverName,
                                                                   @PathVariable String                             userId,
                                                                   @PathVariable String                             schemaAttributeGUID,
                                                                   @RequestBody  AssetManagerIdentifiersRequestBody requestBody)
    {
        return restAPI.getSchemaAttributeByGUID(serverName, userId, schemaAttributeGUID, requestBody);
    }
}
