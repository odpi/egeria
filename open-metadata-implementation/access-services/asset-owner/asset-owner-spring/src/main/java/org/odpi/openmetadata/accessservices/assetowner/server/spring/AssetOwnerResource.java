/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.assetowner.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.accessservices.assetowner.properties.*;
import org.odpi.openmetadata.accessservices.assetowner.rest.TemplateRequestBody;
import org.odpi.openmetadata.accessservices.assetowner.server.AssetOwnerRESTServices;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.AssetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.SchemaTypeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.SchemaAttributeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.SemanticAssignmentProperties;

import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * AssetOwnerResource provides the generic server-side interface for the Asset Owner Open Metadata Access Service (OMAS).
 * There are other resources that provide specialized methods for specific types of Asset.
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/access-services/asset-owner/users/{userId}")

@Tag(name="Metadata Access Server: Asset Owner OMAS",
     description="The Asset Owner OMAS provides APIs and notifications for tools and applications supporting the work of " +
                                                  "Asset Owners in protecting and enhancing their assets.",
     externalDocs=@ExternalDocumentation(description="Further Information",
                                         url="https://egeria-project.org/services/omas/asset-owner/overview/"))

public class AssetOwnerResource
{
    private final AssetOwnerRESTServices restAPI = new AssetOwnerRESTServices();


    /**
     * Default constructor
     */
    public AssetOwnerResource()
    {
    }



    /*
     * ==============================================
     * AssetKnowledgeInterface
     * ==============================================
     */



    /**
     * Return the asset subtype names.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @return list of type names that are subtypes of asset or
     * throws InvalidParameterException full path or userId is null or
     * throws PropertyServerException problem accessing property server or
     * throws UserNotAuthorizedException security access problem.
     */
    @GetMapping(path = "/assets/sub-types")

    public NameListResponse getTypesOfAsset(@PathVariable String           serverName,
                                            @PathVariable String           userId)
    {
        return restAPI.getTypesOfAsset(serverName, userId);
    }


    /**
     * Return the asset subtype names mapped to their descriptions.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @return list of type names that are subtypes of asset or
     * throws InvalidParameterException full path or userId is null or
     * throws PropertyServerException problem accessing property server or
     * throws UserNotAuthorizedException security access problem.
     */
    @GetMapping(path = "/assets/sub-types/descriptions")

    public StringMapResponse getTypesOfAssetDescriptions(@PathVariable String           serverName,
                                                         @PathVariable String           userId)
    {
        return restAPI.getTypesOfAssetDescriptions(serverName, userId);
    }


    /*
     * ==============================================
     * AssetOnboardingInterface
     * ==============================================
     */


    /**
     * Add a simple asset description to the catalog.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user (assumed to be the owner)
     * @param typeName specific type of the asset - this must match a defined subtype
     * @param requestBody other properties for asset
     *
     * @return unique identifier (guid) of the asset or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/assets/{typeName}")

    public GUIDResponse addAssetToCatalog(@PathVariable String          serverName,
                                          @PathVariable String          userId,
                                          @PathVariable String          typeName,
                                          @RequestBody  AssetProperties requestBody)
    {
        return restAPI.addAssetToCatalog(serverName, userId, typeName, requestBody);
    }


    /**
     * Create a new metadata element to represent an asset using an existing asset as a template.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param templateGUID unique identifier of the metadata element to copy
     * @param requestBody properties that override the template
     *
     * @return unique identifier (guid) of the asset or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/assets/from-template/{templateGUID}")

    public GUIDResponse  addAssetToCatalogUsingTemplate(@PathVariable String             serverName,
                                                        @PathVariable String             userId,
                                                        @PathVariable String             templateGUID,
                                                        @RequestBody  TemplateProperties requestBody)
    {
        return restAPI.addAssetToCatalogUsingTemplate(serverName, userId, templateGUID, requestBody);
    }


    /**
     * Update a simple asset description to the catalog.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user (assumed to be the owner)
     * @param assetGUID unique identifier of the asset
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param requestBody other properties for asset
     *
     * @return unique identifier (guid) of the asset or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/assets/{assetGUID}/update")

    public VoidResponse  updateAsset(@PathVariable String           serverName,
                                     @PathVariable String           userId,
                                     @PathVariable String           assetGUID,
                                     @RequestParam boolean          isMergeUpdate,
                                     @RequestBody  AssetProperties  requestBody)
    {
        return restAPI.updateAsset(serverName, userId, assetGUID, isMergeUpdate, requestBody);
    }




    /**
     * Link two asset together.
     * Use information from the relationship type definition to ensure the fromAssetGUID and toAssetGUID are the right way around.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param relationshipTypeName type name of relationship to create
     * @param fromAssetGUID unique identifier of the asset at end 1 of the relationship
     * @param toAssetGUID unique identifier of the asset at end 2 of the relationship
     * @param requestBody unique identifier for this relationship
     *
     * @return unique identifier of the relationship or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/assets/relationships/{relationshipTypeName}/from-asset/{fromAssetGUID}/to-asset/{toAssetGUID}")

    public GUIDResponse setupRelatedAsset(@PathVariable String                  serverName,
                                          @PathVariable String                  userId,
                                          @PathVariable String                  relationshipTypeName,
                                          @PathVariable String                  fromAssetGUID,
                                          @PathVariable String                  toAssetGUID,
                                          @RequestBody(required = false)
                                                        RelationshipRequestBody requestBody)
    {
        return restAPI.setupRelatedAsset(serverName, userId, relationshipTypeName, fromAssetGUID, toAssetGUID, requestBody);
    }


    /**
     * Retrieve the relationship between two elements.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param relationshipTypeName type name of relationship to create
     * @param fromAssetGUID unique identifier of the asset at end 1 of the relationship
     * @param toAssetGUID unique identifier of the asset at end 2 of the relationship
     * @param requestBody optional date for effective time of the query.  Null means any effective time
     *
     * @return unique identifier and properties of the relationship or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/assets/relationships/{relationshipTypeName}/from-asset/{fromAssetGUID}/to-asset/{toAssetGUID}/retrieve")

    public RelationshipElementResponse getAssetRelationship(@PathVariable String                        serverName,
                                                            @PathVariable String                        userId,
                                                            @PathVariable String                        relationshipTypeName,
                                                            @PathVariable String                        fromAssetGUID,
                                                            @PathVariable String                        toAssetGUID,
                                                            @RequestBody(required = false)  EffectiveTimeQueryRequestBody requestBody)
    {
        return restAPI.getAssetRelationship(serverName, userId, relationshipTypeName, fromAssetGUID, toAssetGUID, requestBody);
    }


    /**
     * Update relationship between two elements.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param relationshipTypeName type name of relationship to update
     * @param relationshipGUID unique identifier of the relationship
     * @param isMergeUpdate should the new properties be merged with the existing properties, or replace them entirely
     * @param requestBody description and/or purpose of the relationship
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/assets/relationships/{relationshipTypeName}/{relationshipGUID}/update")

    public VoidResponse updateAssetRelationship(@PathVariable String                  serverName,
                                                @PathVariable String                  userId,
                                                @PathVariable String                  relationshipTypeName,
                                                @PathVariable String                  relationshipGUID,
                                                @RequestParam boolean                 isMergeUpdate,
                                                @RequestBody(required = false)
                                                              RelationshipRequestBody requestBody)
    {
        return restAPI.updateAssetRelationship(serverName, userId, relationshipTypeName, relationshipGUID, isMergeUpdate, requestBody);
    }


    /**
     * Remove the relationship between two elements.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param relationshipTypeName type name of relationship to delete
     * @param relationshipGUID unique identifier of the relationship
     * @param requestBody external source ids
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/assets/relationships/{relationshipTypeName}/{relationshipGUID}/remove")

    public VoidResponse clearAssetRelationship(@PathVariable String                        serverName,
                                               @PathVariable String                        userId,
                                               @PathVariable String                        relationshipTypeName,
                                               @PathVariable String                        relationshipGUID,
                                               @RequestBody(required = false)
                                                             EffectiveTimeQueryRequestBody requestBody)
    {
        return restAPI.clearAssetRelationship(serverName, userId, relationshipTypeName, relationshipGUID, requestBody);
    }


    /**
     * Retrieve the requested relationships linked from a specific element at end 2.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param relationshipTypeName type name of relationship to delete
     * @param fromAssetGUID unique identifier of the asset at end 1 of the relationship
     * @param startFrom start position for results
     * @param pageSize     maximum number of results
     * @param requestBody effective time
     *
     * @return unique identifier and properties of the relationships or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/assets/relationships/{relationshipTypeName}/from-asset/{fromAssetGUID}/retrieve/end2")

    public RelationshipElementsResponse getRelatedAssetsAtEnd2(@PathVariable String                        serverName,
                                                               @PathVariable String                        userId,
                                                               @PathVariable String                        relationshipTypeName,
                                                               @PathVariable String                        fromAssetGUID,
                                                               @RequestParam int                           startFrom,
                                                               @RequestParam int                           pageSize,
                                                               @RequestBody(required = false)  EffectiveTimeQueryRequestBody requestBody)
    {
        return restAPI.getRelatedAssetsAtEnd2(serverName, userId, relationshipTypeName, fromAssetGUID, startFrom, pageSize, requestBody);
    }


    /**
     * Retrieve the relationships linked from a specific element at end 2 of the relationship.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param relationshipTypeName type name of relationship to delete
     * @param toAssetGUID unique identifier of the asset at end 2 of the relationship
     * @param startFrom start position for results
     * @param pageSize     maximum number of results
     * @param requestBody the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @return unique identifier and properties of the relationships or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/assets/relationships/{relationshipTypeName}/to-asset/{toAssetGUID}/retrieve/end1")

    public RelationshipElementsResponse getRelatedAssetsAtEnd1(@PathVariable String                        serverName,
                                                               @PathVariable String                        userId,
                                                               @PathVariable String                        relationshipTypeName,
                                                               @PathVariable String                        toAssetGUID,
                                                               @RequestParam int                           startFrom,
                                                               @RequestParam int                           pageSize,
                                                               @RequestBody  EffectiveTimeQueryRequestBody requestBody)
    {
        return restAPI.getRelatedAssetsAtEnd1(serverName, userId, relationshipTypeName, toAssetGUID, startFrom, pageSize, requestBody);
    }



    /**
     * Stores the supplied schema details in the catalog and attaches it to the asset.  If another schema is currently
     * attached to the asset, it is unlinked and deleted.  If more attributes need to be added in addition to the
     * ones supplied then this can be done with addSchemaAttributesToSchemaType().
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param assetGUID unique identifier of the asset that the schema is to be attached to
     * @param schemaInformation schema type to create and attach directly to the asset.
     *
     * @return guid of the schema type or
     * InvalidParameterException full path or userId is null, or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/assets/{assetGUID}/schemas/top-level-schema-type-with-attributes")
    @Deprecated
    public GUIDResponse   addCombinedSchemaToAsset(@PathVariable String                    serverName,
                                                   @PathVariable String                    userId,
                                                   @PathVariable String                    assetGUID,
                                                   @RequestBody  CombinedSchemaRequestBody schemaInformation)
    {
        return restAPI.addCombinedSchemaToAsset(serverName, userId, assetGUID, schemaInformation);
    }


    /**
     * Stores the supplied schema details in the catalog and attaches it to the asset.  If another schema is currently
     * attached to the asset, it is unlinked and deleted.  If more attributes need to be added in addition to the
     * ones supplied then this can be done with addSchemaAttributesToSchemaType().
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param assetGUID unique identifier of the asset that the schema is to be attached to
     * @param schemaInformation schema type to create and attach directly to the asset.
     *
     * @return guid of the schema type or
     * InvalidParameterException full path or userId is null, or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/assets/{assetGUID}/schemas/with-attributes")

    public GUIDResponse   addComplexSchemaToAsset(@PathVariable String                    serverName,
                                                  @PathVariable String                    userId,
                                                  @PathVariable String                    assetGUID,
                                                  @RequestBody  CombinedSchemaRequestBody schemaInformation)
    {
        return restAPI.addCombinedSchemaToAsset(serverName, userId, assetGUID, schemaInformation);
    }



    /**
     * Stores the supplied schema type in the catalog and attaches it to the asset.  If another schema is currently
     * attached to the asset, it is unlinked and deleted.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param assetGUID unique identifier of the asset that the schema is to be attached to
     * @param schemaType schema type to create and attach directly to the asset.
     *
     * @return guid of the new schema type or
     * InvalidParameterException full path or userId is null, or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/assets/{assetGUID}/schemas")

    public GUIDResponse   addSchemaTypeToAsset(@PathVariable String                serverName,
                                               @PathVariable String                userId,
                                               @PathVariable String                assetGUID,
                                               @RequestBody  SchemaTypeProperties schemaType)
    {
        return restAPI.addSchemaTypeToAsset(serverName, userId, assetGUID, schemaType);
    }


    /**
     * Links the supplied schema type directly to the asset.  If this schema is either not found, or
     * already attached to an asset, then an error occurs.  If another schema is currently
     * attached to the asset, it is unlinked and deleted.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param assetGUID unique identifier of the asset that the schema is to be attached to
     * @param schemaTypeGUID unique identifier of the schema type to attach
     * @param requestBody null
     *
     * @return void or
     * InvalidParameterException full path or userId or one of the GUIDs is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/assets/{assetGUID}/schemas/{schemaTypeGUID}/attach")

    public VoidResponse   attachSchemaTypeToAsset(@PathVariable                   String            serverName,
                                                  @PathVariable                   String            userId,
                                                  @PathVariable                   String            assetGUID,
                                                  @PathVariable                   String            schemaTypeGUID,
                                                  @RequestBody(required = false)  NullRequestBody   requestBody)
    {
        return restAPI.attachSchemaTypeToAsset(serverName, userId, assetGUID, schemaTypeGUID, requestBody);
    }


    /**
     * Unlinks the schema from the asset but does not delete it.  This means it can be reattached to a different asset.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param assetGUID unique identifier of the asset that the schema is to be attached to
     * @param requestBody null
     *
     * @return guid of the schema type or
     * InvalidParameterException full path or userId or one of the GUIDs is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/assets/{assetGUID}/schemas/detach")

    public GUIDResponse   detachSchemaTypeFromAsset(@PathVariable                  String          serverName,
                                                    @PathVariable                  String          userId,
                                                    @PathVariable                  String          assetGUID,
                                                    @RequestBody(required = false) NullRequestBody requestBody)
    {
        return restAPI.detachSchemaTypeFromAsset(serverName, userId, assetGUID, requestBody);
    }


    /**
     * Detaches and deletes an asset's schema.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param assetGUID unique identifier of the asset that the schema is to be attached to
     * @param requestBody null
     *
     * @return void or
     * InvalidParameterException full path or userId is null, or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/assets/{assetGUID}/schemas/delete")

    public VoidResponse  deleteAssetSchemaType(@PathVariable                  String          serverName,
                                               @PathVariable                  String          userId,
                                               @PathVariable                  String          assetGUID,
                                               @RequestBody(required = false) NullRequestBody requestBody)
    {
        return restAPI.deleteAssetSchemaType(serverName, userId, assetGUID, requestBody);
    }


    /* =====================================================================================================================
     * A schemaType describes the structure of a data asset, process or port
     */

    /**
     * Create a new metadata element to represent a schema type.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param anchorGUID unique identifier of the intended anchor of the schema type
     * @param requestBody properties about the schema type to store
     *
     * @return unique identifier of the new schema type
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/schema-types")

    public GUIDResponse createSchemaType(@PathVariable String               serverName,
                                         @PathVariable String               userId,
                                         @RequestParam (required = false, defaultValue = "null")
                                                       String               anchorGUID,
                                         @RequestBody  SchemaTypeProperties requestBody)
    {
        return restAPI.createSchemaType(serverName, userId, anchorGUID, requestBody);
    }


    /**
     * Create a new metadata element to represent a schema type using an existing metadata element as a template.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param templateGUID unique identifier of the metadata element to copy
     * @param requestBody properties that override the template
     *
     * @return unique identifier of the new schema type
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
                                         @RequestBody  SchemaTypeProperties  requestBody)
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
     * @param requestBody unique identifier/name of software server capability representing the caller
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/parents/{parentElementGUID}/{parentElementTypeName}/schema-types/{schemaTypeGUID}")

    public VoidResponse setupSchemaTypeParent(@PathVariable String                        serverName,
                                              @PathVariable String                        userId,
                                              @PathVariable String                        parentElementGUID,
                                              @PathVariable String                        parentElementTypeName,
                                              @PathVariable String                        schemaTypeGUID,
                                              @RequestBody  (required=false)
                                                            RelationshipRequestBody       requestBody)
    {
        return restAPI.setupSchemaTypeParent(serverName, userId, parentElementGUID, parentElementTypeName, schemaTypeGUID, requestBody);
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

    public VoidResponse clearSchemaTypeParent(@PathVariable String                        serverName,
                                              @PathVariable String                        userId,
                                              @PathVariable String                        parentElementGUID,
                                              @PathVariable String                        parentElementTypeName,
                                              @PathVariable String                        schemaTypeGUID,
                                              @RequestBody  (required=false)
                                                            EffectiveTimeQueryRequestBody requestBody)
    {
        return restAPI.clearSchemaTypeParent(serverName, userId, parentElementGUID, parentElementTypeName, schemaTypeGUID, requestBody);
    }




    /**
     * Create a relationship between two schema elements.  The name of the desired relationship, and any properties (including effectivity dates)
     * are passed on the API.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param endOneGUID unique identifier of the schema element at end one of the relationship
     * @param endTwoGUID unique identifier of the schema element at end two of the relationship
     * @param relationshipTypeName type of the relationship to create
     * @param requestBody relationship properties
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/schema-elements/{endOneGUID}/relationships/{relationshipTypeName}/schema-elements/{endTwoGUID}")

    public VoidResponse setupSchemaElementRelationship(@PathVariable String                         serverName,
                                                       @PathVariable String                         userId,
                                                       @PathVariable String                         endOneGUID,
                                                       @PathVariable String                         relationshipTypeName,
                                                       @PathVariable String                         endTwoGUID,
                                                       @RequestBody  (required=false)
                                                                     RelationshipRequestBody        requestBody)
    {
        return restAPI.setupSchemaElementRelationship(serverName, userId, endOneGUID, relationshipTypeName, endTwoGUID, requestBody);
    }


    /**
     * Remove a relationship between two schema elements.  The name of the desired relationship is passed on the API.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param endOneGUID unique identifier of the schema element at end one of the relationship
     * @param endTwoGUID unique identifier of the schema element at end two of the relationship
     * @param relationshipTypeName type of the relationship to delete
     * @param requestBody unique identifier/name of software server capability representing the caller
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/schema-elements/{endOneGUID}/relationships/{relationshipTypeName}/schema-elements/{endTwoGUID}/remove")

    public VoidResponse clearSchemaElementRelationship(@PathVariable String                        serverName,
                                                       @PathVariable String                        userId,
                                                       @PathVariable String                        endOneGUID,
                                                       @PathVariable String                        relationshipTypeName,
                                                       @PathVariable String                        endTwoGUID,
                                                       @RequestBody  (required=false)
                                                                     EffectiveTimeQueryRequestBody requestBody)
    {
        return restAPI.clearSchemaElementRelationship(serverName, userId, endOneGUID, relationshipTypeName, endTwoGUID, requestBody);
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

    public VoidResponse removeSchemaType(@PathVariable String            serverName,
                                         @PathVariable String            userId,
                                         @PathVariable String            schemaTypeGUID,
                                         @RequestBody  (required=false)
                                                       UpdateRequestBody requestBody)
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
    @PostMapping(path = "/parents/{parentElementTypeName}/{parentElementGUID}/schema-types/retrieve")

    public SchemaTypeElementResponse getSchemaTypeForElement(@PathVariable String                        serverName,
                                                             @PathVariable String                        userId,
                                                             @PathVariable String                        parentElementGUID,
                                                             @PathVariable String                        parentElementTypeName,
                                                             @RequestBody(required = false)
                                                                           EffectiveTimeQueryRequestBody requestBody)
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

    public SchemaTypeElementResponse getSchemaTypeByGUID(@PathVariable String                        serverName,
                                                         @PathVariable String                        userId,
                                                         @PathVariable String                        schemaTypeGUID,
                                                         @RequestBody(required = false)
                                                                       EffectiveTimeQueryRequestBody requestBody)
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

    public ElementHeaderResponse getSchemaTypeParent(@PathVariable String                        serverName,
                                                     @PathVariable String                        userId,
                                                     @PathVariable String                        schemaTypeGUID,
                                                     @RequestBody(required = false)
                                                                   EffectiveTimeQueryRequestBody requestBody)
    {
        return restAPI.getSchemaTypeParent(serverName, userId, schemaTypeGUID, requestBody);
    }


    /* ===============================================================================
     * A schemaType typically contains many schema attributes, linked with relationships.
     */


    /**
     * Adds attributes to a complex schema type like a relational table, avro schema or a structured document.
     * This method can be called repeatedly to add many attributes to a schema.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param assetGUID unique identifier of the asset that the schema is to be attached to
     * @param parentGUID unique identifier of the schema element to anchor these attributes to.
     * @param schemaAttributes list of schema attribute objects.
     *
     * @return list of unique identifiers for the new schema attributes returned in the same order as the supplied attribute or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/assets/{assetGUID}/schemas/{parentGUID}/schema-attributes/list")

    public VoidResponse addSchemaAttributes(@PathVariable String                          serverName,
                                            @PathVariable String                          userId,
                                            @PathVariable String                          assetGUID,
                                            @PathVariable String                          parentGUID,
                                            @RequestBody  List<SchemaAttributeProperties> schemaAttributes)
    {
        return restAPI.addSchemaAttributes(serverName, userId, assetGUID, parentGUID, schemaAttributes);
    }


    /**
     * Adds the attribute to a complex schema type like a relational table, avro schema or a structured document.
     * This method can be called repeatedly to add many attributes to a schema.  The GUID returned can be used to add
     * nested attributes.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param assetGUID unique identifier of the asset that the schema is to be attached to
     * @param parentGUID unique identifier of the schema element to anchor these attributes to.
     * @param schemaAttribute schema attribute object.
     *
     * @return list of unique identifiers for the new schema attributes returned in the same order as the supplied attribute or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/assets/{assetGUID}/schemas/{parentGUID}/schema-attributes")

    public GUIDResponse addSchemaAttribute(@PathVariable String                   serverName,
                                           @PathVariable String                   userId,
                                           @PathVariable String                   assetGUID,
                                           @PathVariable String                   parentGUID,
                                           @RequestBody  SchemaAttributeProperties schemaAttribute)
    {
        return restAPI.addSchemaAttribute(serverName, userId, assetGUID, parentGUID, schemaAttribute);
    }


    /**
     * Create a new metadata element to represent a schema attribute using an existing metadata element as a template.
     *
     * @param serverName name of the server to route the request to
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
    @PostMapping(path = "/schema-elements/{schemaElementGUID}/schema-attributes/from-template/{templateGUID}")

    public GUIDResponse createSchemaAttributeFromTemplate(@PathVariable String              serverName,
                                                          @PathVariable String              userId,
                                                          @PathVariable String              schemaElementGUID,
                                                          @PathVariable String              templateGUID,
                                                          @RequestBody  TemplateRequestBody requestBody)
    {
        return restAPI.createSchemaAttributeFromTemplate(serverName, userId, schemaElementGUID, templateGUID, requestBody);
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
                                              @RequestBody  SchemaAttributeProperties  requestBody)
    {
        return restAPI.updateSchemaAttribute(serverName, userId, schemaAttributeGUID, isMergeUpdate, requestBody);
    }


    /**
     * Classify the schema type (or attribute if type is embedded) to indicate that it is a calculated value.
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
    @PostMapping(path = "/schema-elements/{schemaElementGUID}/is-calculated-value")

    public VoidResponse setSchemaElementAsCalculatedValue(@PathVariable String                                   serverName,
                                                          @PathVariable String                                   userId,
                                                          @PathVariable String                                   schemaElementGUID,
                                                          @RequestBody  CalculatedValueClassificationRequestBody requestBody)
    {
        return restAPI.setSchemaElementAsCalculatedValue(serverName, userId, schemaElementGUID, requestBody);
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

    public VoidResponse clearSchemaElementAsCalculatedValue(@PathVariable String            serverName,
                                                            @PathVariable String            userId,
                                                            @PathVariable String            schemaElementGUID,
                                                            @RequestBody  UpdateRequestBody requestBody)
    {
        return restAPI.clearSchemaElementAsCalculatedValue(serverName, userId, schemaElementGUID, requestBody);
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

    public VoidResponse removeSchemaAttribute(@PathVariable String            serverName,
                                              @PathVariable String            userId,
                                              @PathVariable String            schemaAttributeGUID,
                                              @RequestBody  UpdateRequestBody requestBody)
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

    public SchemaAttributesResponse findSchemaAttributes(@PathVariable String                  serverName,
                                                         @PathVariable String                  userId,
                                                         @RequestParam int                     startFrom,
                                                         @RequestParam int                     pageSize,
                                                         @RequestBody  SearchStringRequestBody requestBody)
    {
        return restAPI.findSchemaAttributes(serverName, userId, startFrom, pageSize, requestBody);
    }


    /**
     * Retrieve the list of schema attributes associated with a schema element.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param parentSchemaElementGUID unique identifier of the schema element of interest
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody unique identifier/name of software server capability representing the caller
     *
     * @return list of associated metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/schema-elements/{parentSchemaElementGUID}/schema-attributes/retrieve")

    public SchemaAttributesResponse getNestedAttributes(@PathVariable String                        serverName,
                                                        @PathVariable String                        userId,
                                                        @PathVariable String                        parentSchemaElementGUID,
                                                        @RequestParam int                           startFrom,
                                                        @RequestParam int                           pageSize,
                                                        @RequestBody  EffectiveTimeQueryRequestBody requestBody)
    {
        return restAPI.getNestedAttributes(serverName, userId, parentSchemaElementGUID, startFrom, pageSize, requestBody);
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

    public SchemaAttributesResponse getSchemaAttributesByName(@PathVariable String          serverName,
                                                              @PathVariable String          userId,
                                                              @RequestParam int             startFrom,
                                                              @RequestParam int             pageSize,
                                                              @RequestBody  NameRequestBody requestBody)
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

    public SchemaAttributeResponse getSchemaAttributeByGUID(@PathVariable String                        serverName,
                                                            @PathVariable String                        userId,
                                                            @PathVariable String                        schemaAttributeGUID,
                                                            @RequestBody  EffectiveTimeQueryRequestBody requestBody)
    {
        return restAPI.getSchemaAttributeByGUID(serverName, userId, schemaAttributeGUID, requestBody);
    }

    /**
     * Adds a connection to an asset.  Assets can have multiple connections attached.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param assetGUID unique identifier of the asset to attach the connection to
     * @param requestBody request body including a summary and connection object.
     *                   If the connection is already stored (matching guid)
     *                   then the existing connection is used.
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/assets/{assetGUID}/connection")

    public VoidResponse addConnectionToAsset(@PathVariable String                serverName,
                                             @PathVariable String                userId,
                                             @PathVariable String                assetGUID,
                                             @RequestBody OCFConnectionRequestBody requestBody)
    {
        return restAPI.addConnectionToAsset(serverName, userId, assetGUID, requestBody);
    }



    /*
     * ==============================================
     * AssetClassificationInterface
     * ==============================================
     */


    /**
     * Create a simple relationship between a glossary term and an Asset description.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param assetGUID unique identifier of the asset that is being described
     * @param glossaryTermGUID unique identifier of the glossary term
     * @param requestBody null request body to satisfy POST request.
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/assets/{assetGUID}/meanings/{glossaryTermGUID}")

    public VoidResponse  addSemanticAssignment(@PathVariable                  String                       serverName,
                                               @PathVariable                  String                       userId,
                                               @PathVariable                  String                       assetGUID,
                                               @PathVariable                  String                       glossaryTermGUID,
                                               @RequestBody(required = false) SemanticAssignmentProperties requestBody)
    {
        return restAPI.addSemanticAssignment(serverName,
                                             userId,
                                             assetGUID,
                                             glossaryTermGUID,
                                             requestBody);
    }


    /**
     * Create a simple relationship between a glossary term and an element in an Asset description (typically
     * a field in the schema).
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param assetGUID unique identifier of the asset that is being described
     * @param glossaryTermGUID unique identifier of the glossary term
     * @param assetElementGUID element to link it to - its type must inherit from Referenceable.
     * @param requestBody null request body to satisfy POST request.
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/assets/{assetGUID}/attachments/{assetElementGUID}/meanings/{glossaryTermGUID}")

    public VoidResponse  addSemanticAssignment(@PathVariable                  String          serverName,
                                               @PathVariable                  String          userId,
                                               @PathVariable                  String          assetGUID,
                                               @PathVariable                  String          glossaryTermGUID,
                                               @PathVariable                  String          assetElementGUID,
                                               @RequestBody(required = false) NullRequestBody requestBody)
    {
        return restAPI.addSemanticAssignment(serverName,
                                             userId,
                                             assetGUID,
                                             glossaryTermGUID,
                                             assetElementGUID,
                                             requestBody);
    }


    /**
     * Remove the relationship between a glossary term and an element in an Asset description (typically
     * a field in the schema).
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param assetGUID unique identifier of asset
     * @param glossaryTermGUID unique identifier of the glossary term
     * @param requestBody null request body
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/assets/{assetGUID}/meanings/{glossaryTermGUID}/delete")

    public VoidResponse  removeSemanticAssignment(@PathVariable                  String          serverName,
                                                  @PathVariable                  String          userId,
                                                  @PathVariable                  String          assetGUID,
                                                  @PathVariable                  String          glossaryTermGUID,
                                                  @RequestBody(required = false) NullRequestBody requestBody)
    {
        return restAPI.removeSemanticAssignment(serverName, userId, assetGUID, glossaryTermGUID, requestBody);
    }


    /**
     * Remove the relationship between a glossary term and an element in an Asset description (typically
     * a field in the schema).
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param assetGUID unique identifier of asset
     * @param glossaryTermGUID unique identifier of the glossary term
     * @param assetElementGUID element to link it to - its type must inherit from Referenceable.
     * @param requestBody null request body
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/assets/{assetGUID}/attachments/{assetElementGUID}/meanings/{glossaryTermGUID}/delete")

    public VoidResponse  removeSemanticAssignment(@PathVariable                   String          serverName,
                                                  @PathVariable                   String          userId,
                                                  @PathVariable                   String          assetGUID,
                                                  @PathVariable                   String          glossaryTermGUID,
                                                  @PathVariable                   String          assetElementGUID,
                                                  @RequestBody(required = false)  NullRequestBody requestBody)
    {
        return restAPI.removeSemanticAssignment(serverName, userId, assetGUID, glossaryTermGUID, assetElementGUID, requestBody);
    }


    /**
     * Set up the labels that classify an asset's origin.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param assetGUID unique identifier of asset
     * @param requestBody Descriptive labels describing origin of the asset
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/assets/{assetGUID}/origin")

    public VoidResponse  addAssetOrigin(@PathVariable String            serverName,
                                        @PathVariable String            userId,
                                        @PathVariable String            assetGUID,
                                        @RequestBody  OriginRequestBody requestBody)
    {
        return restAPI.addAssetOrigin(serverName, userId, assetGUID, requestBody);
    }


    /**
     * Remove the asset origin classification to an asset.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param assetGUID unique identifier of asset
     * @param requestBody null request body
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/assets/{assetGUID}/origin/delete")

    public VoidResponse  removeAssetOrigin(@PathVariable                   String                serverName,
                                           @PathVariable                   String                userId,
                                           @PathVariable                   String                assetGUID,
                                           @RequestBody(required = false)  NullRequestBody       requestBody)
    {
        return restAPI.removeAssetOrigin(serverName, userId, assetGUID, requestBody);
    }


    /**
     * Update the zones for a specific asset to the zone list specified in the publishZones.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param assetGUID unique identifier for the asset to update
     * @param requestBody null request body
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/assets/{assetGUID}/publish")

    public VoidResponse publishAsset(@PathVariable                   String                serverName,
                                     @PathVariable                   String                userId,
                                     @PathVariable                   String                assetGUID,
                                     @RequestBody(required = false)  NullRequestBody       requestBody)
    {
        return restAPI.publishAsset(serverName, userId, assetGUID, requestBody);
    }


    /**
     * Update the zones for a specific asset to the zone list specified in the defaultZones.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param assetGUID unique identifier for the asset to update
     * @param requestBody null request body
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/assets/{assetGUID}/withdraw")

    public VoidResponse withdrawAsset(@PathVariable                   String                serverName,
                                      @PathVariable                   String                userId,
                                      @PathVariable                   String                assetGUID,
                                      @RequestBody(required = false)  NullRequestBody       requestBody)
    {
        return restAPI.withdrawAsset(serverName, userId, assetGUID, requestBody);
    }


    /**
     * Update the zones for a specific asset.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param assetGUID unique identifier for the asset to update
     * @param assetZones list of zones for the asset - these values override the current values - null means belongs
     *                   to no zones.
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/assets/{assetGUID}/governance-zones")

    public VoidResponse updateAssetZones(@PathVariable String        serverName,
                                         @PathVariable String        userId,
                                         @PathVariable String        assetGUID,
                                         @RequestBody  List<String>  assetZones)
    {
        return restAPI.updateAssetZones(serverName, userId, assetGUID, assetZones);
    }



    /**
     * Update the owner information for a specific asset.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param assetGUID unique identifier for the asset to update
     * @param requestBody values describing the new owner
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/assets/{assetGUID}/owner")

    public VoidResponse  updateAssetOwner(@PathVariable String           serverName,
                                          @PathVariable String           userId,
                                          @PathVariable String           assetGUID,
                                          @RequestBody  OwnerRequestBody requestBody)
    {
        return restAPI.updateAssetOwner(serverName, userId, assetGUID, requestBody);
    }


    /**
     * Add or replace the security tags for an asset or one of its elements.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param assetGUID unique identifier of asset
     * @param requestBody list of security labels and properties
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/assets/{assetGUID}/security-tags")

    public VoidResponse  addSecurityTags(@PathVariable String                  serverName,
                                         @PathVariable String                  userId,
                                         @PathVariable String                  assetGUID,
                                         @RequestBody  SecurityTagsRequestBody requestBody)
    {
        return restAPI.addSecurityTags(serverName, userId, assetGUID, requestBody);
    }


    /**
     * Add or replace the security tags for an asset or one of its elements.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param assetGUID unique identifier of asset
     * @param assetElementGUID element to link it to - its type must inherit from Referenceable.
     * @param requestBody list of security labels and properties
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/assets/{assetGUID}/attachments/{assetElementGUID}/security-tags")

    public VoidResponse  addSecurityTags(@PathVariable String                  serverName,
                                         @PathVariable String                  userId,
                                         @PathVariable String                  assetGUID,
                                         @PathVariable String                  assetElementGUID,
                                         @RequestBody  SecurityTagsRequestBody requestBody)
    {
        return restAPI.addSecurityTags(serverName, userId, assetGUID, assetElementGUID, requestBody);
    }


    /**
     * Remove the security tags classification from an asset.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param assetGUID unique identifier of asset
     * @param requestBody null request body
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/assets/{assetGUID}/security-tags/delete")

    public VoidResponse  removeSecurityTags(@PathVariable                   String                serverName,
                                            @PathVariable                   String                userId,
                                            @PathVariable                   String                assetGUID,
                                            @RequestBody (required = false) NullRequestBody       requestBody)
    {
        return restAPI.removeSecurityTags(serverName, userId, assetGUID, requestBody);
    }


    /**
     * Remove the security tags classification to one of an asset's elements.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param assetGUID unique identifier of asset
     * @param assetElementGUID element where the security tags need to be removed.
     * @param requestBody null request body
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/assets/{assetGUID}/attachments/{assetElementGUID}/security-tags/delete")

    public VoidResponse  removeSecurityTags(@PathVariable                  String                serverName,
                                            @PathVariable                  String                userId,
                                            @PathVariable                  String                assetGUID,
                                            @PathVariable                  String                assetElementGUID,
                                            @RequestBody(required = false) NullRequestBody       requestBody)
    {
        return restAPI.removeSecurityTags(serverName, userId, assetGUID, assetElementGUID, requestBody);
    }


    /**
     * Classify an asset as suitable to be used as a template for cataloguing assets of a similar types.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param assetGUID unique identifier of the asset to classify
     * @param requestBody  properties of the template
     *
     * @return void or
     *  InvalidParameterException asset or element not known, null userId or guid or
     *  PropertyServerException problem accessing property server or
     *  UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/assets/{assetGUID}/template-classification")

    public VoidResponse addTemplateClassification(@PathVariable                   String                            serverName,
                                                  @PathVariable                   String                            userId,
                                                  @PathVariable                   String                            assetGUID,
                                                  @RequestBody (required = false) TemplateClassificationRequestBody requestBody)
    {
        return restAPI.addTemplateClassification(serverName, userId, assetGUID, requestBody);
    }


    /**
     * Remove the classification that indicates that this asset can be used as a template.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param assetGUID unique identifier of the asset to declassify
     * @param requestBody null request body
     *
     * @return void or
     *  InvalidParameterException asset or element not known, null userId or guid or
     *  PropertyServerException problem accessing property server or
     *  UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/assets/{assetGUID}/template-classification/delete")

    public VoidResponse removeTemplateClassification(@PathVariable                   String          serverName,
                                                     @PathVariable                   String          userId,
                                                     @PathVariable                   String          assetGUID,
                                                     @RequestBody (required = false) NullRequestBody requestBody)
    {
        return restAPI.removeTemplateClassification(serverName, userId, assetGUID, requestBody);
    }


    /*
     * ==============================================
     * AssetReviewInterface
     * ==============================================
     */


    /**
     * Return a list of assets with the requested name.
     *
     * @param serverName name of the server instances for this request
     * @param userId calling user
     * @param requestBody name to search for
     * @param startFrom starting element (used in paging through large result sets)
     * @param pageSize maximum number of results to return
     *
     * @return list of Asset summaries or
     * InvalidParameterException the name is invalid or
     * PropertyServerException there is a problem access in the property server or
     * UserNotAuthorizedException the user does not have access to the properties
     */
    @PostMapping(path = "/assets/by-name")

    public AssetElementsResponse getAssetsByName(@PathVariable String          serverName,
                                                 @PathVariable String          userId,
                                                 @RequestParam int             startFrom,
                                                 @RequestParam int             pageSize,
                                                 @RequestBody  NameRequestBody requestBody)
    {
        return restAPI.getAssetsByName(serverName, userId, requestBody, startFrom, pageSize);
    }


    /**
     * Return a list of assets with the requested search string in their name, qualified name
     * or description.
     *
     * @param serverName name of the server instances for this request
     * @param userId calling user
     * @param requestBody string to search for in text
     * @param startFrom starting element (used in paging through large result sets)
     * @param pageSize maximum number of results to return
     *
     * @return list of assets that match the search string or
     * InvalidParameterException the searchString is invalid or
     * PropertyServerException there is a problem access in the property server or
     * UserNotAuthorizedException the user does not have access to the properties
     */
    @PostMapping(path = "/assets/by-search-string")

    public AssetElementsResponse findAssets(@PathVariable String                  serverName,
                                            @PathVariable String                  userId,
                                            @RequestParam int                     startFrom,
                                            @RequestParam int                     pageSize,
                                            @RequestBody  SearchStringRequestBody requestBody)
    {
        return restAPI.findAssets(serverName, userId, requestBody, startFrom, pageSize);
    }


    /**
     * Return the basic attributes of an asset.
     *
     * @param serverName server called
     * @param userId calling user
     * @param assetGUID unique identifier of the asset
     * @return basic asset properties
     * InvalidParameterException one of the parameters is null or invalid.
     * UserNotAuthorizedException user not authorized to issue this request.
     * PropertyServerException there was a problem that occurred within the property server.
     */
    @GetMapping(path = "/assets/{assetGUID}")

    public AssetElementResponse getAssetSummary(@PathVariable String  serverName,
                                                @PathVariable String  userId,
                                                @PathVariable String  assetGUID)
    {
        return restAPI.getAssetSummary(serverName, userId, assetGUID);
    }


    /**
     * Return the discovery analysis reports about the asset.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param assetGUID unique identifier of the asset
     * @param startingFrom position in the list (used when there are so many reports that paging is needed
     * @param maximumResults maximum number of elements to return on this call
     *
     * @return list of discovery analysis reports or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @GetMapping(path = "/assets/{assetGUID}/discovery-analysis-reports")

    public SurveyReportsResponse getDiscoveryAnalysisReports(@PathVariable String  serverName,
                                                             @PathVariable String  userId,
                                                             @PathVariable String  assetGUID,
                                                             @RequestParam int     startingFrom,
                                                             @RequestParam int     maximumResults)
    {
        return restAPI.getSurveyReports(serverName,
                                        userId,
                                        assetGUID,
                                        startingFrom,
                                        maximumResults);
    }



    /**
     * Return the annotations linked directly to the report.
     *
     * @param serverName name of the server instance to connect to
     * @param userId identifier of calling user
     * @param discoveryReportGUID identifier of the discovery request.
     * @param startingFrom initial position in the stored list.
     * @param maximumResults maximum number of definitions to return on this call.
     * @param requestBody status of the desired annotations - null means all statuses.
     *
     * @return list of annotations or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @GetMapping(path = "/discovery-analysis-reports/{discoveryReportGUID}/annotations")

    public AnnotationsResponse getDiscoveryReportAnnotations(@PathVariable String            serverName,
                                                             @PathVariable String            userId,
                                                             @PathVariable String            discoveryReportGUID,
                                                             @RequestParam int               startingFrom,
                                                             @RequestParam int               maximumResults,
                                                             @RequestBody AnnotationStatusRequestBody requestBody)
    {
        return restAPI.getSurveyReportAnnotations(serverName,
                                                  userId,
                                                  discoveryReportGUID,
                                                  startingFrom,
                                                  maximumResults,
                                                  requestBody);
    }



    /**
     * Return any annotations attached to this annotation.
     *
     * @param serverName name of the server instance to connect to
     * @param userId identifier of calling user
     * @param annotationGUID anchor annotation
     * @param startingFrom starting position in the list
     * @param maximumResults maximum number of annotations that can be returned.
     * @param requestBody status of the desired annotations - null means all statuses.
     *
     * @return list of Annotation objects or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @GetMapping(path = "/annotations/{annotationGUID}/annotations")

    public AnnotationsResponse getExtendedAnnotations(@PathVariable String            serverName,
                                                      @PathVariable String            userId,
                                                      @PathVariable String            annotationGUID,
                                                      @RequestParam int               startingFrom,
                                                      @RequestParam int               maximumResults,
                                                      @RequestBody AnnotationStatusRequestBody requestBody)
    {
        return restAPI.getExtendedAnnotations(serverName,
                                              userId,
                                              annotationGUID,
                                              startingFrom,
                                              maximumResults,
                                              requestBody);
    }



    /*
     * ==============================================
     * AssetDecommissioningInterface
     * ==============================================
     */


    /**
     * Deletes an asset and all of its associated elements such as schema, connections (unless they are linked to
     * another asset), discovery reports and associated feedback.
     * Given the depth of the elements deleted by this call, it should be used with care.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param assetGUID unique identifier of the asset to attach the connection to
     * @param requestBody dummy request body to satisfy POST protocol.
     *
     * @return void or
     *  InvalidParameterException full path or userId is null or
     *  PropertyServerException problem accessing property server or
     *  UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/assets/{assetGUID}/delete")

    public VoidResponse deleteAsset(@PathVariable                   String          serverName,
                                    @PathVariable                   String          userId,
                                    @PathVariable                   String          assetGUID,
                                    @RequestBody(required = false)  NullRequestBody requestBody)
    {
        return restAPI.deleteAsset(serverName, userId, assetGUID, requestBody);
    }



    /*
     * ==============================================
     * AssetDuplicateManagementInterface
     * ==============================================
     */


    /**
     * Create a simple relationship between two elements in an Asset description (typically the asset itself or
     * attributes in their schema).
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param element1GUID unique identifier of first element
     * @param element2GUID unique identifier of second element
     * @param requestBody dummy request body to satisfy POST protocol.
     *
     * @return void or
     *  InvalidParameterException one of the parameters is null or invalid or
     *  PropertyServerException problem accessing property server or
     *  UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/elements/{element1GUID}/duplicate-of/{element2GUID}")

    public VoidResponse linkElementsAsPeerDuplicates(@PathVariable String          serverName,
                                                     @PathVariable String          userId,
                                                     @PathVariable String          element1GUID,
                                                     @PathVariable String          element2GUID,
                                                     @RequestBody (required = false)
                                                                   NullRequestBody requestBody)
    {
        return restAPI.linkElementsAsPeerDuplicates(serverName, userId, element1GUID, element2GUID, requestBody);
    }


    /**
     * Remove the relationship between two elements that marks them as duplicates.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param element1GUID unique identifier of first element
     * @param element2GUID unique identifier of second element
     * @param requestBody dummy request body to satisfy POST protocol.
     *
     * @return void or
     *  InvalidParameterException one of the parameters is null or invalid or
     *  PropertyServerException problem accessing property server or
     *  UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/elements/{element1GUID}/duplicate-of/{element2GUID}/delete")

    public VoidResponse unlinkElementsAsPeerDuplicates(@PathVariable String          serverName,
                                                       @PathVariable String          userId,
                                                       @PathVariable String          element1GUID,
                                                       @PathVariable String          element2GUID,
                                                       @RequestBody (required = false)
                                                                     NullRequestBody requestBody)
    {
        return restAPI.unlinkElementsAsPeerDuplicates(serverName, userId, element1GUID, element2GUID, requestBody);
    }
}
