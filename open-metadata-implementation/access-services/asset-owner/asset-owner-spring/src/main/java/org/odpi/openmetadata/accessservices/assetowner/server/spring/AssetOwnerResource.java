/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.assetowner.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.accessservices.assetowner.rest.*;
import org.odpi.openmetadata.accessservices.assetowner.server.AssetOwnerRESTServices;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.commonservices.gaf.metadatamanagement.rest.SecurityTagsRequestBody;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.rest.*;
import org.odpi.openmetadata.commonservices.odf.metadatamanagement.rest.AnnotationListResponse;
import org.odpi.openmetadata.commonservices.odf.metadatamanagement.rest.DiscoveryAnalysisReportListResponse;
import org.odpi.openmetadata.commonservices.odf.metadatamanagement.rest.StatusRequestBody;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.SchemaAttribute;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * AssetOwnerResource provides the generic server-side interface for the Asset Owner Open Metadata Access Service (OMAS).
 * There are other resources that provide specialized methods for specific types of Asset.
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/access-services/asset-owner/users/{userId}")

@Tag(name="Asset Owner OMAS", description="The Asset Owner OMAS provides APIs and notifications for tools and applications supporting the work of Asset Owners in protecting and enhancing their assets.\n" +
        "\n", externalDocs=@ExternalDocumentation(description="Asset Owner Open Metadata Access Service (OMAS)",url="https://egeria.odpi.org/open-metadata-implementation/access-services/asset-owner/"))

public class AssetOwnerResource
{
    private AssetOwnerRESTServices restAPI = new AssetOwnerRESTServices();


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

    public GUIDResponse addAssetToCatalog(@PathVariable String           serverName,
                                          @PathVariable String           userId,
                                          @PathVariable String           typeName,
                                          @RequestBody  AssetRequestBody requestBody)
    {
        return restAPI.addAssetToCatalog(serverName, userId, typeName, requestBody);
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

    public GUIDResponse   addCombinedSchemaToAsset(@PathVariable String                    serverName,
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
    @PostMapping(path = "/assets/{assetGUID}/schemas/top-level-schema-type")

    public GUIDResponse   addSchemaTypeToAsset(@PathVariable String                serverName,
                                               @PathVariable String                userId,
                                               @PathVariable String                assetGUID,
                                               @RequestBody  SchemaTypeRequestBody schemaType)
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
    @PostMapping(path = "/assets/{assetGUID}/schemas/top-level-schema-type/{schemaTypeGUID}/attach")

    public VoidResponse   attachSchemaTypeToAsset(@PathVariable                   String            serverName,
                                                  @PathVariable                   String            userId,
                                                  @PathVariable                   String            assetGUID,
                                                  @PathVariable                   String            schemaTypeGUID,
                                                  @RequestBody(required = false)  NullRequestBody   requestBody)
    {
        return restAPI.attachSchemaTypeToAsset(serverName, userId, assetGUID, schemaTypeGUID, requestBody);
    }


    /**
     * Unlinks the schema from the asset but does not delete it.  This means it can be be reattached to a different asset.
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
    @PostMapping(path = "/assets/{assetGUID}/schemas/top-level-schema-type/detach")

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
    @PostMapping(path = "/assets/{assetGUID}/schemas/top-level-schema-type/delete")

    public VoidResponse  deleteAssetSchemaType(@PathVariable                  String          serverName,
                                               @PathVariable                  String          userId,
                                               @PathVariable                  String          assetGUID,
                                               @RequestBody(required = false) NullRequestBody requestBody)
    {
        return restAPI.deleteAssetSchemaType(serverName, userId, assetGUID, requestBody);
    }


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

    public VoidResponse addSchemaAttributes(@PathVariable String                      serverName,
                                            @PathVariable String                      userId,
                                            @PathVariable String                      assetGUID,
                                            @PathVariable String                      parentGUID,
                                            @RequestBody  SchemaAttributesRequestBody schemaAttributes)
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

    public GUIDResponse addSchemaAttribute(@PathVariable String                      serverName,
                                           @PathVariable String                      userId,
                                           @PathVariable String                      assetGUID,
                                           @PathVariable String                      parentGUID,
                                           @RequestBody  SchemaAttributeRequestBody schemaAttribute)
    {
        return restAPI.addSchemaAttribute(serverName, userId, assetGUID, parentGUID, schemaAttribute);
    }


    /**
     * Links the supplied schema to the asset.  If the schema has the GUID set, it is assumed to refer to
     * an existing schema defined in the metadata repository.  If this schema is either not found, or
     * already attached to an asset, then an error occurs.  If the GUID is null then a new schemaType
     * is added to the metadata repository and attached to the asset.  If another schema is currently
     * attached to the asset, it is unlinked and deleted.  This method is deprecated in favour of the use of
     * Asset Owner specific request body objects that do not have the element header.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param assetGUID unique identifier of the asset that the schema is to be attached to
     * @param requestBody schema to attach
     *
     * @return guid of the schema type or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/assets/{assetGUID}/schema-type")
    @Deprecated
    public GUIDResponse addSchemaToAsset(@PathVariable String            serverName,
                                         @PathVariable String            userId,
                                         @PathVariable String            assetGUID,
                                         @RequestBody  SchemaRequestBody requestBody)
    {
        return restAPI.addSchemaToAsset(serverName, userId, assetGUID, requestBody);
    }


    /**
     * Adds attributes to a complex schema type like a relational table or a structured document.
     * This method can be called repeatedly to add many attributes to a schema.
     * It is deprecated because it does not supply the assetGUID which is needed to ensure that
     * the caller has a right to update the attachments of the asset.  With this original version of the
     * API, the asset has to be located to validate the authorization of the user.  This extra processing
     * is unnecessary in the Asset Owner's case because the assetGUID is known.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param schemaTypeGUID unique identifier if the schema to anchor these attributes to.
     * @param schemaAttributes list of schema attribute objects.
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/schemas/{schemaTypeGUID}/schema-attributes")
    @Deprecated
    public VoidResponse addSchemaAttributesToSchema(@PathVariable String                 serverName,
                                                    @PathVariable String                 userId,
                                                    @PathVariable String                 schemaTypeGUID,
                                                    @RequestBody  List<SchemaAttribute>  schemaAttributes)
    {
        return restAPI.addSchemaAttributesToSchema(serverName, userId, schemaTypeGUID, schemaAttributes);
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
                                             @RequestBody  ConnectionRequestBody requestBody)
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

    public VoidResponse  addSemanticAssignment(@PathVariable                  String          serverName,
                                               @PathVariable                  String          userId,
                                               @PathVariable                  String          assetGUID,
                                               @PathVariable                  String          glossaryTermGUID,
                                               @RequestBody(required = false) NullRequestBody requestBody)
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
                                                  @PathVariable(required = false) NullRequestBody requestBody)
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
     * @param name name to search for
     * @param startFrom starting element (used in paging through large result sets)
     * @param pageSize maximum number of results to return
     *
     * @return list of Asset summaries or
     * InvalidParameterException the name is invalid or
     * PropertyServerException there is a problem access in the property server or
     * UserNotAuthorizedException the user does not have access to the properties
     */
    @PostMapping(path = "/assets/by-name")

    public AssetsResponse getAssetsByName(@PathVariable String   serverName,
                                          @PathVariable String   userId,
                                          @RequestParam int      startFrom,
                                          @RequestParam int      pageSize,
                                          @RequestBody  String   name)
    {
        return restAPI.getAssetsByName(serverName, userId, name, startFrom, pageSize);
    }


    /**
     * Return a list of assets with the requested search string in their name, qualified name
     * or description.
     *
     * @param serverName name of the server instances for this request
     * @param userId calling user
     * @param searchString string to search for in text
     * @param startFrom starting element (used in paging through large result sets)
     * @param pageSize maximum number of results to return
     *
     * @return list of assets that match the search string or
     * InvalidParameterException the searchString is invalid or
     * PropertyServerException there is a problem access in the property server or
     * UserNotAuthorizedException the user does not have access to the properties
     */
    @PostMapping(path = "/assets/by-search-string")

    public AssetsResponse  findAssets(@PathVariable String   serverName,
                                      @PathVariable String   userId,
                                      @RequestParam int      startFrom,
                                      @RequestParam int      pageSize,
                                      @RequestBody  String   searchString)
    {
        return restAPI.findAssets(serverName, userId, searchString, startFrom, pageSize);
    }


    /**
     * Return the discovery analysis reports about the asset.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param assetGUID unique identifier of the asset
     * @param startingFrom position in the list (used when there are so many reports that paging is needed
     * @param maximumResults maximum number of elements to return an this call
     *
     * @return list of discovery analysis reports or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @GetMapping(path = "/assets/{assetGUID}/discovery-analysis-reports")

    public DiscoveryAnalysisReportListResponse getDiscoveryAnalysisReports(@PathVariable String  serverName,
                                                                           @PathVariable String  userId,
                                                                           @PathVariable String  assetGUID,
                                                                           @RequestParam int     startingFrom,
                                                                           @RequestParam int     maximumResults)
    {
        return restAPI.getDiscoveryAnalysisReports(serverName,
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

    public AnnotationListResponse getDiscoveryReportAnnotations(@PathVariable String            serverName,
                                                                @PathVariable String            userId,
                                                                @PathVariable String            discoveryReportGUID,
                                                                @RequestParam int               startingFrom,
                                                                @RequestParam int               maximumResults,
                                                                @RequestBody  StatusRequestBody requestBody)
    {
        return restAPI.getDiscoveryReportAnnotations(serverName,
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

    public AnnotationListResponse  getExtendedAnnotations(@PathVariable String            serverName,
                                                          @PathVariable String            userId,
                                                          @PathVariable String            annotationGUID,
                                                          @RequestParam int               startingFrom,
                                                          @RequestParam int               maximumResults,
                                                          @RequestBody  StatusRequestBody requestBody)
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
     *
     * Given the depth of the delete performed by this call, it should be used with care.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param assetGUID unique identifier of the attest to attach the connection to
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
}
