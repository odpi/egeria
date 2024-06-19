/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.itinfrastructure.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.accessservices.itinfrastructure.rest.*;
import org.odpi.openmetadata.accessservices.itinfrastructure.server.ITAssetRESTService;
import org.odpi.openmetadata.commonservices.ffdc.rest.EffectiveTimeRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.NameRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.NullRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.SearchStringRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * ITAssetResource is the server-side of AssetManagerClientBase.  It is called from the specific clients that manage the
 * specializations of IT Infrastructure assets.
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/access-services/it-infrastructure/users/{userId}")

@Tag(name="Metadata Access Server: IT Infrastructure OMAS",
     description="The IT Infrastructure OMAS provides APIs for tools and applications managing the IT infrastructure that supports the data assets.\n",
     externalDocs=@ExternalDocumentation(description="Further Information",
                                         url="https://egeria-project.org/services/omas/it-infrastructure/overview/"))


public class ITAssetResource
{
    private static final ITAssetRESTService restAPI = new ITAssetRESTService();

    /**
     * Default constructor
     */
    public ITAssetResource()
    {
    }



    /* =====================================================================================================================
     * The asset describes the computer or container that provides the operating system for the platforms.
     */

    /**
     * Create a new metadata element to represent an asset.
     *
     * @param serverName name of the server to route the request to.
     * @param userId calling user
     * @param infrastructureManagerIsHome should the asset be marked as owned by the infrastructure manager so others can not update?
     * @param requestBody properties to store
     *
     * @return unique identifier of the new metadata element or
     *
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/assets")

    public GUIDResponse createAsset(@PathVariable String           serverName,
                                    @PathVariable String           userId,
                                    @RequestParam boolean          infrastructureManagerIsHome,
                                    @RequestBody  AssetRequestBody requestBody)
    {
        return restAPI.createAsset(serverName, userId, infrastructureManagerIsHome, requestBody);
    }


    /**
     * Create a new metadata element to represent an asset using an existing metadata element as a template.
     *
     * @param serverName name of the server to route the request to.
     * @param userId calling user
     * @param infrastructureManagerIsHome should the asset be marked as owned by the infrastructure manager so others can not update?
     * @param templateGUID unique identifier of the metadata element to copy
     * @param requestBody properties that override the template
     *
     * @return unique identifier of the new metadata element or
     *
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/assets/from-template/{templateGUID}")

    public GUIDResponse createAssetFromTemplate(@PathVariable String              serverName,
                                                @PathVariable String              userId,
                                                @PathVariable String              templateGUID,
                                                @RequestParam boolean             infrastructureManagerIsHome,
                                                @RequestBody  TemplateRequestBody requestBody)
    {
        return restAPI.createAssetFromTemplate(serverName, userId, templateGUID, infrastructureManagerIsHome, requestBody);
    }


    /**
     * Update the metadata element representing an asset.
     *
     * @param serverName name of the server to route the request to.
     * @param userId calling user
     * @param assetGUID unique identifier of the metadata element to update
     * @param isMergeUpdate are unspecified properties unchanged (true) or removed?
     * @param requestBody new properties for this element
     *
     * @return void or
     *
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/assets/{assetGUID}")

    public VoidResponse updateAsset(@PathVariable String           serverName,
                                    @PathVariable String           userId,
                                    @PathVariable String           assetGUID,
                                    @RequestParam boolean          isMergeUpdate,
                                    @RequestBody  AssetRequestBody requestBody)
    {
        return restAPI.updateAsset(serverName, userId, assetGUID, isMergeUpdate, requestBody);
    }


    /**
     * Update the status of the metadata element representing an asset.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param assetTypeName name of type for the asset
     * @param assetGUID unique identifier of the asset to update
     * @param requestBody new status for the process
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/assets/{assetTypeName}/{assetGUID}/status")

    public VoidResponse updateAssetStatus(@PathVariable String                   serverName,
                                          @PathVariable String                   userId,
                                          @PathVariable String                   assetTypeName,
                                          @PathVariable String                   assetGUID,
                                          @RequestBody  ElementStatusRequestBody requestBody)
    {
        return  restAPI.updateAssetStatus(serverName, userId, assetTypeName, assetGUID, requestBody);
    }


    /**
     * Create a relationship between an asset and a related asset.
     *
     * @param serverName name of the server to route the request to.
     * @param userId calling user
     * @param assetTypeName name of type for the asset
     * @param assetGUID unique identifier of the asset
     * @param relationshipTypeName name of the relationship type
     * @param relatedAssetTypeName name of type for the asset
     * @param relatedAssetGUID unique identifier of the related asset
     * @param infrastructureManagerIsHome ensure that only the infrastructure manager can update this asset
     * @param requestBody properties
     *
     * @return void or
     *
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/assets/{assetTypeName}/{assetGUID}/{relationshipTypeName}/{relatedAssetTypeName}/{relatedAssetGUID}")

    public VoidResponse setupRelatedAsset(@PathVariable String                     serverName,
                                          @PathVariable String                     userId,
                                          @PathVariable String                     assetTypeName,
                                          @PathVariable String                     assetGUID,
                                          @PathVariable String                     relationshipTypeName,
                                          @PathVariable String                     relatedAssetTypeName,
                                          @PathVariable String                     relatedAssetGUID,
                                          @RequestParam boolean                    infrastructureManagerIsHome,
                                          @RequestBody  AssetExtensionsRequestBody requestBody)
    {
        return restAPI.setupRelatedAsset(serverName, userId, assetTypeName, assetGUID, relationshipTypeName, relatedAssetTypeName, relatedAssetGUID, infrastructureManagerIsHome, requestBody);
    }


    /**
     * Update a relationship between an asset and a related asset.
     *
     * @param serverName name of the server to route the request to.
     * @param userId calling user
     * @param relationshipTypeName name of the relationship type
     * @param relationshipGUID unique identifier of the asset
     * @param isMergeUpdate   should the supplied properties be merged with existing properties (true) by replacing just the properties with
     *                                  matching names, or should the entire properties of the instance be replaced?
     * @param requestBody new properties
     *
     * @return void or
     *
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/assets/relationships/{relationshipTypeName}/{relationshipGUID}/update")

    public VoidResponse updateAssetRelationship(@PathVariable String                     serverName,
                                                @PathVariable String                     userId,
                                                @PathVariable String                     relationshipTypeName,
                                                @PathVariable String                     relationshipGUID,
                                                @RequestParam boolean                    isMergeUpdate,
                                                @RequestBody  AssetExtensionsRequestBody requestBody)
    {
        return restAPI.updateAssetRelationship(serverName, userId, relationshipTypeName, relationshipGUID, isMergeUpdate, requestBody);

    }

    /**
     * Remove a relationship between an asset and a related asset.
     *
     * @param serverName name of the server to route the request to.
     * @param userId calling user
     * @param assetTypeName name of type for the asset
     * @param assetGUID unique identifier of the asset
     * @param relationshipTypeName name of the relationship type
     * @param relatedAssetTypeName name of type for the asset
     * @param relatedAssetGUID unique identifier of the related asset
     * @param requestBody unique identifier/name of software server capability representing the infrastructure manager
     *
     * @return void or
     *
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/assets/{assetTypeName}/{assetGUID}/{relationshipTypeName}/{relatedAssetTypeName}/{relatedAssetGUID}/delete")

    public VoidResponse clearRelatedAsset(@PathVariable String                                 serverName,
                                          @PathVariable String                                 userId,
                                          @PathVariable String                                 assetTypeName,
                                          @PathVariable String                                 assetGUID,
                                          @PathVariable String                                 relationshipTypeName,
                                          @PathVariable String                                 relatedAssetTypeName,
                                          @PathVariable String                                 relatedAssetGUID,
                                          @RequestBody  EffectiveTimeMetadataSourceRequestBody requestBody)
    {
        return restAPI.clearRelatedAsset(serverName, userId, assetTypeName, assetGUID, relationshipTypeName, relatedAssetTypeName, relatedAssetGUID, requestBody);
    }


    /**
     * Add a classification to an asset.
     *
     * @param serverName name of the server to route the request to.
     * @param userId calling user
     * @param assetTypeName name of type for the asset
     * @param assetGUID unique identifier of the asset
     * @param classificationName name of the classification type
     * @param infrastructureManagerIsHome ensure that only the infrastructure manager can update this asset
     * @param requestBody properties
     *
     * @return void or
     *
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/assets/{assetTypeName}/{assetGUID}/classify/{classificationName}")

    public VoidResponse addClassification(@PathVariable String                     serverName,
                                          @PathVariable String                     userId,
                                          @PathVariable String                     assetTypeName,
                                          @PathVariable String                     assetGUID,
                                          @PathVariable String                     classificationName,
                                          @RequestParam boolean                    infrastructureManagerIsHome,
                                          @RequestBody  AssetExtensionsRequestBody requestBody)
    {
        return restAPI.addClassification(serverName, userId, assetTypeName, assetGUID, classificationName, infrastructureManagerIsHome, requestBody);
    }


    /**
     * Update the properties of a classification for an asset.
     *
     * @param serverName name of the server to route the request to.
     * @param userId calling user
     * @param assetTypeName name of type for the asset
     * @param assetGUID unique identifier of the asset
     * @param classificationName name of the classification type
     * @param isMergeUpdate   should the supplied properties be merged with existing properties (true) by replacing just the properties with
     *                                  matching names, or should the entire properties of the instance be replaced?
     * @param requestBody properties
     *
     * @return void or
     *
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/assets/{assetTypeName}/{assetGUID}/reclassify/{classificationName}")

    public VoidResponse updateClassification(@PathVariable String                     serverName,
                                             @PathVariable String                     userId,
                                             @PathVariable String                     assetTypeName,
                                             @PathVariable String                     assetGUID,
                                             @PathVariable String                     classificationName,
                                             @RequestParam boolean                    isMergeUpdate,
                                             @RequestBody  AssetExtensionsRequestBody requestBody)
    {
        return restAPI.updateClassification(serverName, userId, assetTypeName, assetGUID, classificationName, isMergeUpdate, requestBody);
    }


    /**
     * Remove a classification from an asset.
     *
     * @param serverName name of the server to route the request to.
     * @param userId calling user
     * @param assetTypeName name of type for the asset
     * @param assetGUID unique identifier of the asset
     * @param classificationName name of the classification type
     * @param requestBody properties
     *
     * @return void or
     *
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/assets/{assetTypeName}/{assetGUID}/declassify/{classificationName}")

    public VoidResponse clearClassification(@PathVariable String                                 serverName,
                                            @PathVariable String                                 userId,
                                            @PathVariable String                                 assetTypeName,
                                            @PathVariable String                                 assetGUID,
                                            @PathVariable String                                 classificationName,
                                            @RequestBody  EffectiveTimeMetadataSourceRequestBody requestBody)
    {
        return restAPI.clearClassification(serverName, userId, assetTypeName, assetGUID, classificationName, requestBody);
    }


    /**
     * Update the zones for the asset so that it becomes visible to consumers.
     * (The zones are set to the list of zones in the publishedZones option configured for each
     * instance of the IT Infrastructure OMAS).
     *
     * @param serverName name of the server to route the request to.
     * @param userId calling user
     * @param assetGUID unique identifier of the metadata element to publish
     * @param requestBody null request body
     *
     * @return void or
     *
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/assets/{assetGUID}/publish")

    public VoidResponse publishAsset(@PathVariable String          serverName,
                                     @PathVariable String          userId,
                                     @PathVariable String          assetGUID,
                                     @RequestBody (required = false)
                                                   NullRequestBody requestBody)
    {
        return restAPI.publishAsset(serverName, userId, assetGUID, requestBody);
    }


    /**
     * Update the zones for the asset so that it is no longer visible to consumers.
     * (The zones are set to the list of zones in the defaultZones option configured for each
     * instance of the IT Infrastructure OMAS.  This is the setting when the asset is first created).
     *
     * @param serverName name of the server to route the request to.
     * @param userId calling user
     * @param assetGUID unique identifier of the metadata element to withdraw
     * @param requestBody null request body
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/assets/{assetGUID}/withdraw")

    public VoidResponse withdrawAsset(@PathVariable String          serverName,
                                      @PathVariable String          userId,
                                      @PathVariable String          assetGUID,
                                      @RequestBody (required = false)
                                                    NullRequestBody requestBody)
    {
        return restAPI.withdrawAsset(serverName, userId, assetGUID, requestBody);
    }


    /**
     * Remove the metadata element representing an asset.
     *
     * @param serverName name of the server to route the request to.
     * @param userId calling user
     * @param assetGUID unique identifier of the metadata element to remove
     * @param requestBody unique identifier/name of software server capability representing the infrastructure manager
     *
     * @return void or
     *
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/assets/{assetGUID}/delete")

    public VoidResponse removeAsset(@PathVariable String                    serverName,
                                    @PathVariable String                    userId,
                                    @PathVariable String                    assetGUID,
                                    @RequestBody  MetadataSourceRequestBody requestBody)
    {
        return restAPI.removeAsset(serverName, userId, assetGUID, requestBody);
    }



    /**
     * Retrieve the list of asset metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param serverName name of the server to route the request to.
     * @param userId calling user
     * @param assetTypeName name of type for the asset
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/assets/{assetTypeName}/by-search-string")

    public AssetListResponse findAssets(@PathVariable String                  serverName,
                                        @PathVariable String                  userId,
                                        @PathVariable String                  assetTypeName,
                                        @RequestParam int                     startFrom,
                                        @RequestParam int                     pageSize,
                                        @RequestBody  SearchStringRequestBody requestBody)
    {
        return restAPI.findAssets(serverName, userId, assetTypeName, startFrom, pageSize, requestBody);
    }


    /**
     * Retrieve the list of asset metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param serverName name of the server to route the request to.
     * @param userId calling user
     * @param assetTypeName name of type for the asset
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody name to search for
     *
     * @return list of matching metadata elements or
     *
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/assets/{assetTypeName}/by-name")

    public AssetListResponse getAssetsByName(@PathVariable String          serverName,
                                             @PathVariable String          userId,
                                             @PathVariable String          assetTypeName,
                                             @RequestParam int             startFrom,
                                             @RequestParam int             pageSize,
                                             @RequestBody  NameRequestBody requestBody)
    {
        return restAPI.getAssetsByName(serverName, userId, assetTypeName, startFrom, pageSize, requestBody);
    }


    /**
     * Retrieve the list of asset metadata elements with a matching deployed implementation type.
     * There are no wildcards supported on this request.
     *
     * @param serverName name of the server to route the request to.
     * @param userId calling user
     * @param assetTypeName name of type for the asset
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody name to search for
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/assets/{assetTypeName}/by-deployed-implementation-type")

    public AssetListResponse getAssetsByDeployedImplementationType(@PathVariable String          serverName,
                                                                   @PathVariable String          userId,
                                                                   @PathVariable String          assetTypeName,
                                                                   @RequestParam int             startFrom,
                                                                   @RequestParam int             pageSize,
                                                                   @RequestBody(required = false)
                                                                       NameRequestBody requestBody)
    {
        return restAPI.getAssetsByDeployedImplementationType(serverName, userId, assetTypeName, startFrom, pageSize, requestBody);
    }


    /**
     * Retrieve the list of assets created by this caller.
     *
     * @param serverName name of the server to route the request to.
     * @param userId calling user
     * @param infrastructureManagerGUID unique identifier of software server capability representing the infrastructure manager
     * @param infrastructureManagerName unique name of software server capability representing the infrastructure manager
     * @param assetTypeName name of type for the asset
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody effective time for the query
     *
     * @return list of matching metadata elements or
     *
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/infrastructure-managers/{infrastructureManagerGUID}/{infrastructureManagerName}/assets/{assetTypeName}")

    public AssetListResponse getAssetsForInfrastructureManager(@PathVariable String                   serverName,
                                                               @PathVariable String                   userId,
                                                               @PathVariable String                   infrastructureManagerGUID,
                                                               @PathVariable String                   infrastructureManagerName,
                                                               @PathVariable String                   assetTypeName,
                                                               @RequestParam int                      startFrom,
                                                               @RequestParam int                      pageSize,
                                                               @RequestBody  EffectiveTimeRequestBody requestBody)
    {
        return restAPI.getAssetsForInfrastructureManager(serverName, userId, infrastructureManagerGUID, infrastructureManagerName, assetTypeName, startFrom, pageSize, requestBody);
    }


    /**
     * Retrieve the asset metadata element with the supplied unique identifier.
     *
     * @param serverName name of the server to route the request to.
     * @param userId calling user
     * @param assetTypeName name of type for the asset
     * @param assetGUID unique identifier of the requested metadata element
     * @param requestBody effective time for the query
     *
     * @return matching metadata element or
     *
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/assets/{assetTypeName}/{assetGUID}")

    public AssetResponse getAssetByGUID(@PathVariable String                   serverName,
                                        @PathVariable String                   userId,
                                        @PathVariable String                   assetTypeName,
                                        @PathVariable String                   assetGUID,
                                        @RequestBody  EffectiveTimeRequestBody requestBody)
    {
        return restAPI.getAssetByGUID(serverName, userId, assetTypeName, assetGUID, requestBody);
    }


    /**
     * Return the list of relationships between assets.
     *
     * @param serverName name of the server to route the request to.
     * @param userId calling user
     * @param assetTypeName name of type for the asset
     * @param assetGUID unique identifier of the asset to start with
     * @param startingEnd which end of the relationship to start at 0=either end; 1=end1 and 2=end 2
     * @param relationshipTypeName name of type for the relationship
     * @param relatedAssetTypeName name of type of retrieved assets
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody effective time for the query
     *
     * @return list of matching metadata elements or
     *
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/assets/{assetTypeName}/{assetGUID}/{relationshipTypeName}/{relatedAssetTypeName}/relationships")

    public AssetRelationshipListResponse getAssetRelationships(@PathVariable String                   serverName,
                                                               @PathVariable String                   userId,
                                                               @PathVariable String                   assetTypeName,
                                                               @PathVariable String                   assetGUID,
                                                               @PathVariable String                   relationshipTypeName,
                                                               @PathVariable String                   relatedAssetTypeName,
                                                               @RequestParam int                      startingEnd,
                                                               @RequestParam int                      startFrom,
                                                               @RequestParam int                      pageSize,
                                                               @RequestBody  EffectiveTimeRequestBody requestBody)
    {
        return restAPI.getAssetRelationships(serverName, userId, assetTypeName, assetGUID, relationshipTypeName, relatedAssetTypeName, startingEnd, startFrom, pageSize, requestBody);
    }


    /**
     * Return the list of assets linked by another asset.
     *
     * @param serverName name of the server to route the request to.
     * @param userId calling user
     * @param assetTypeName name of type for the asset
     * @param assetGUID unique identifier of the asset to start with
     * @param startingEnd which end of the relationship to start at 0=either end; 1=end1 and 2=end 2
     * @param relationshipTypeName name of type for the relationship
     * @param relatedAssetTypeName name of type of retrieved assets
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody effective time for the query
     *
     * @return list of matching metadata elements or
     *
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/assets/{assetTypeName}/{assetGUID}/{relationshipTypeName}/{relatedAssetTypeName}")

    public RelatedAssetListResponse getRelatedAssets(@PathVariable String                   serverName,
                                                     @PathVariable String                   userId,
                                                     @PathVariable String                   assetTypeName,
                                                     @PathVariable String                   assetGUID,
                                                     @PathVariable String                   relationshipTypeName,
                                                     @PathVariable String                   relatedAssetTypeName,
                                                     @RequestParam int                      startingEnd,
                                                     @RequestParam int                      startFrom,
                                                     @RequestParam int                      pageSize,
                                                     @RequestBody  EffectiveTimeRequestBody requestBody)
    {
        return restAPI.getRelatedAssets(serverName, userId, assetTypeName, assetGUID, relationshipTypeName, relatedAssetTypeName, startingEnd, startFrom, pageSize, requestBody);
    }


    /* ===============================================================================
     * General linkage and classifications
     */


    /**
     * Link two elements together to show that data flows from one to the other.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param dataSupplierGUID unique identifier of the data supplier
     * @param dataConsumerGUID unique identifier of the data consumer
     * @param infrastructureManagerIsHome ensure that only the process manager can update this process
     * @param requestBody properties of the relationship
     *
     * @return unique identifier of the relationship or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/data-flows/suppliers/{dataSupplierGUID}/consumers/{dataConsumerGUID}")

    public GUIDResponse setupDataFlow(@PathVariable String              serverName,
                                      @PathVariable String              userId,
                                      @PathVariable String              dataSupplierGUID,
                                      @PathVariable String              dataConsumerGUID,
                                      @RequestParam boolean             infrastructureManagerIsHome,
                                      @RequestBody  DataFlowRequestBody requestBody)
    {
        return restAPI.setupDataFlow(serverName, userId, dataSupplierGUID, dataConsumerGUID, infrastructureManagerIsHome, requestBody);
    }


    /**
     * Retrieve the data flow relationship between two elements.  The qualifiedName is optional unless there
     * is more than one data flow relationships between these two elements since it is used to disambiguate
     * the request.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param dataSupplierGUID unique identifier of the data supplier
     * @param dataConsumerGUID unique identifier of the data consumer
     * @param requestBody optional name to search for
     *
     * @return unique identifier and properties of the relationship or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/data-flows/suppliers/{dataSupplierGUID}/consumers/{dataConsumerGUID}/retrieve")

    public DataFlowElementResponse getDataFlow(@PathVariable String          serverName,
                                               @PathVariable String          userId,
                                               @PathVariable String          dataSupplierGUID,
                                               @PathVariable String          dataConsumerGUID,
                                               @RequestBody  NameRequestBody requestBody)
    {
        return restAPI.getDataFlow(serverName, userId, dataSupplierGUID, dataConsumerGUID, requestBody);
    }


    /**
     * Update relationship between two elements that shows that data flows from one to the other.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param dataFlowGUID unique identifier of the data flow relationship
     * @param requestBody properties of the relationship
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/data-flows/{dataFlowGUID}/update")

    public VoidResponse updateDataFlow(@PathVariable String              serverName,
                                       @PathVariable String              userId,
                                       @PathVariable String              dataFlowGUID,
                                       @RequestBody  DataFlowRequestBody requestBody)
    {
        return restAPI.updateDataFlow(serverName, userId, dataFlowGUID, requestBody);
    }


    /**
     * Remove the data flow relationship between two elements.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param dataFlowGUID unique identifier of the data flow relationship
     * @param requestBody unique identifiers of software server capability representing the caller (optional)
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/data-flows/{dataFlowGUID}/remove")

    public VoidResponse clearDataFlow(@PathVariable String                                 serverName,
                                      @PathVariable String                                 userId,
                                      @PathVariable String                                 dataFlowGUID,
                                      @RequestBody  EffectiveTimeMetadataSourceRequestBody requestBody)
    {
        return restAPI.clearDataFlow(serverName, userId, dataFlowGUID, requestBody);
    }


    /**
     * Retrieve the data flow relationships linked from a specific element to the downstream consumers.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param dataSupplierGUID unique identifier of the data supplier
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody unique identifiers of software server capability representing the caller (optional)
     *
     * @return unique identifier and properties of the relationship or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/data-flows/suppliers/{dataSupplierGUID}/consumers/retrieve")

    public DataFlowElementsResponse getDataFlowConsumers(@PathVariable String                   serverName,
                                                         @PathVariable String                   userId,
                                                         @PathVariable String                   dataSupplierGUID,
                                                         @RequestParam int                      startFrom,
                                                         @RequestParam int                      pageSize,
                                                         @RequestBody  EffectiveTimeRequestBody requestBody)
    {
        return restAPI.getDataFlowConsumers(serverName, userId, dataSupplierGUID, startFrom, pageSize, requestBody);
    }


    /**
     * Retrieve the data flow relationships linked from a specific element to the upstream suppliers.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param dataConsumerGUID unique identifier of the data consumer
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody unique identifiers of software server capability representing the caller (optional)
     *
     * @return unique identifier and properties of the relationship or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/data-flows/consumers/{dataConsumerGUID}/suppliers/retrieve")

    public DataFlowElementsResponse getDataFlowSuppliers(@PathVariable String                   serverName,
                                                         @PathVariable String                   userId,
                                                         @PathVariable String                   dataConsumerGUID,
                                                         @RequestParam int                      startFrom,
                                                         @RequestParam int                      pageSize,
                                                         @RequestBody  EffectiveTimeRequestBody requestBody)
    {
        return restAPI.getDataFlowSuppliers(serverName, userId, dataConsumerGUID, startFrom, pageSize, requestBody);
    }


    /**
     * Link two elements to show that when one completes the next is started.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param currentStepGUID unique identifier of the previous step
     * @param nextStepGUID unique identifier of the next step
     * @param infrastructureManagerIsHome ensure that only the process manager can update this process
     * @param requestBody properties of the relationship
     *
     * @return unique identifier for the control flow relationship or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/control-flows/current-steps/{currentStepGUID}/next-steps/{nextStepGUID}")

    public GUIDResponse setupControlFlow(@PathVariable String                 serverName,
                                         @PathVariable String                 userId,
                                         @PathVariable String                 currentStepGUID,
                                         @PathVariable String                 nextStepGUID,
                                         @RequestParam boolean                infrastructureManagerIsHome,
                                         @RequestBody  ControlFlowRequestBody requestBody)
    {
        return restAPI.setupControlFlow(serverName, userId, currentStepGUID, nextStepGUID, infrastructureManagerIsHome, requestBody);
    }


    /**
     * Retrieve the control flow relationship between two elements.  The qualifiedName is optional unless there
     * is more than one control flow relationships between these two elements since it is used to disambiguate
     * the request.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param currentStepGUID unique identifier of the previous step
     * @param nextStepGUID unique identifier of the next step
     * @param requestBody unique identifier for this relationship
     *
     * @return unique identifier and properties of the relationship or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/control-flows/current-steps/{currentStepGUID}/next-steps/{nextStepGUID}/retrieve")

    public ControlFlowElementResponse getControlFlow(@PathVariable String          serverName,
                                                     @PathVariable String          userId,
                                                     @PathVariable String          currentStepGUID,
                                                     @PathVariable String          nextStepGUID,
                                                     @RequestBody  NameRequestBody requestBody)
    {
        return restAPI.getControlFlow(serverName, userId, currentStepGUID, nextStepGUID, requestBody);
    }


    /**
     * Update the relationship between two elements that shows that when one completes the next is started.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param controlFlowGUID unique identifier of the  control flow relationship
     * @param requestBody properties of the relationship
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/control-flows/{controlFlowGUID}/update")

    public VoidResponse updateControlFlow(@PathVariable String                 serverName,
                                          @PathVariable String                 userId,
                                          @PathVariable String                 controlFlowGUID,
                                          @RequestBody  ControlFlowRequestBody requestBody)
    {
        return restAPI.updateControlFlow(serverName, userId, controlFlowGUID, requestBody);
    }


    /**
     * Remove the control flow relationship between two elements.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param controlFlowGUID unique identifier of the  control flow relationship
     * @param requestBody effective time and external identifiers
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/control-flows/{controlFlowGUID}/remove")

    public VoidResponse clearControlFlow(@PathVariable String                                 serverName,
                                         @PathVariable String                                 userId,
                                         @PathVariable String                                 controlFlowGUID,
                                         @RequestBody  EffectiveTimeMetadataSourceRequestBody requestBody)
    {
        return restAPI.clearControlFlow(serverName, userId, controlFlowGUID, requestBody);
    }


    /**
     * Retrieve the control relationships linked from a specific element to the possible next elements in the process.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param currentStepGUID unique identifier of the current step
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody null request body
     *
     * @return unique identifier and properties of the relationship or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/control-flows/current-steps/{currentStepGUID}/next-steps/retrieve")

    public ControlFlowElementsResponse getControlFlowNextSteps(@PathVariable String                   serverName,
                                                               @PathVariable String                   userId,
                                                               @PathVariable String                   currentStepGUID,
                                                               @RequestParam int                      startFrom,
                                                               @RequestParam int                      pageSize,
                                                               @RequestBody  EffectiveTimeRequestBody requestBody)
    {
        return restAPI.getControlFlowNextSteps(serverName, userId, currentStepGUID, startFrom, pageSize, requestBody);
    }


    /**
     * Retrieve the control relationships linked from a specific element to the possible previous elements in the process.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param currentStepGUID unique identifier of the previous step
     * @param requestBody unique identifiers of software server capability representing the caller (optional)
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return unique identifier and properties of the relationship or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/control-flows/current-steps/{currentStepGUID}/previous-steps/retrieve")

    public ControlFlowElementsResponse getControlFlowPreviousSteps(@PathVariable String                   serverName,
                                                                   @PathVariable String                   userId,
                                                                   @PathVariable String                   currentStepGUID,
                                                                   @RequestParam int                      startFrom,
                                                                   @RequestParam int                      pageSize,
                                                                   @RequestBody  EffectiveTimeRequestBody requestBody)
    {
        return restAPI.getControlFlowPreviousSteps(serverName, userId, currentStepGUID, startFrom, pageSize, requestBody);
    }


    /**
     * Link two elements together to show a request-response call between them.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param callerGUID unique identifier of the element that is making the call
     * @param calledGUID unique identifier of the element that is processing the call
     * @param infrastructureManagerIsHome ensure that only the process manager can update this process
     * @param requestBody properties of the relationship
     *
     * @return unique identifier of the new relationship or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/process-calls/callers/{callerGUID}/called/{calledGUID}")

    public GUIDResponse setupProcessCall(@PathVariable String                 serverName,
                                         @PathVariable String                 userId,
                                         @PathVariable String                 callerGUID,
                                         @PathVariable String                 calledGUID,
                                         @RequestParam boolean                infrastructureManagerIsHome,
                                         @RequestBody  ProcessCallRequestBody requestBody)
    {
        return restAPI.setupProcessCall(serverName, userId, callerGUID, calledGUID, infrastructureManagerIsHome, requestBody);
    }


    /**
     * Retrieve the process call relationship between two elements.  The qualifiedName is optional unless there
     * is more than one process call relationships between these two elements since it is used to disambiguate
     * the request.  This is often used in conjunction with update.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param callerGUID unique identifier of the element that is making the call
     * @param calledGUID unique identifier of the element that is processing the call
     * @param requestBody qualified name to disambiguate request
     *
     * @return unique identifier and properties of the relationship or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/process-calls/callers/{callerGUID}/called/{calledGUID}/retrieve")

    public ProcessCallElementResponse getProcessCall(@PathVariable String          serverName,
                                                     @PathVariable String          userId,
                                                     @PathVariable String          callerGUID,
                                                     @PathVariable String          calledGUID,
                                                     @RequestBody  NameRequestBody requestBody)
    {
        return restAPI.getProcessCall(serverName, userId, callerGUID, calledGUID, requestBody);
    }


    /**
     * Update the relationship between two elements that shows a request-response call between them.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param processCallGUID unique identifier of the process call relationship
     * @param requestBody properties of the relationship
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/process-calls/{processCallGUID}/update")

    public VoidResponse updateProcessCall(@PathVariable String                 serverName,
                                          @PathVariable String                 userId,
                                          @PathVariable String                 processCallGUID,
                                          @RequestBody  ProcessCallRequestBody requestBody)
    {
        return restAPI.updateProcessCall(serverName, userId, processCallGUID, requestBody);
    }


    /**
     * Remove the process call relationship.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param processCallGUID unique identifier of the process call relationship
     * @param requestBody unique identifiers of software server capability representing the caller (optional)
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/process-calls/{processCallGUID}/remove")

    public VoidResponse clearProcessCall(@PathVariable String                                 serverName,
                                         @PathVariable String                                 userId,
                                         @PathVariable String                                 processCallGUID,
                                         @RequestBody  EffectiveTimeMetadataSourceRequestBody requestBody)
    {
        return restAPI.clearProcessCall(serverName, userId, processCallGUID, requestBody);
    }


    /**
     * Retrieve the process call relationships linked from a specific element to the elements it calls.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param callerGUID unique identifier of the element that is making the call
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody unique identifiers of software server capability representing the caller (optional)
     *
     * @return unique identifier and properties of the relationship or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/process-calls/callers/{callerGUID}/called/retrieve")

    public ProcessCallElementsResponse getProcessCalled(@PathVariable String                   serverName,
                                                        @PathVariable String                   userId,
                                                        @PathVariable String                   callerGUID,
                                                        @RequestParam int                      startFrom,
                                                        @RequestParam int                      pageSize,
                                                        @RequestBody  EffectiveTimeRequestBody requestBody)
    {
        return restAPI.getProcessCalled(serverName, userId, callerGUID, startFrom, pageSize, requestBody);
    }


    /**
     * Retrieve the process call relationships linked from a specific element to its callers.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param calledGUID unique identifier of the element that is processing the call
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody unique identifiers of software server capability representing the caller (optional)
     *
     * @return unique identifier and properties of the relationship or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/process-calls/called/{calledGUID}/callers/retrieve")

    public ProcessCallElementsResponse getProcessCallers(@PathVariable String                   serverName,
                                                         @PathVariable String                   userId,
                                                         @PathVariable String                   calledGUID,
                                                         @RequestParam int                      startFrom,
                                                         @RequestParam int                      pageSize,
                                                         @RequestBody  EffectiveTimeRequestBody requestBody)
    {
        return restAPI.getProcessCallers(serverName, userId, calledGUID, startFrom, pageSize, requestBody);
    }


    /**
     * Link to elements together to show that they are part of the lineage of the data that is moving
     * between the processes.  Typically, the lineage relationships stitch together processes and data assets
     * supported by different technologies.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param sourceElementGUID unique identifier of the source
     * @param destinationElementGUID unique identifier of the destination
     * @param requestBody unique identifiers of software server capability representing the caller (optional)
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/lineage-mappings/sources/{sourceElementGUID}/destinations/{destinationElementGUID}")

    public VoidResponse setupLineageMapping(@PathVariable String                    serverName,
                                            @PathVariable String                    userId,
                                            @PathVariable String                    sourceElementGUID,
                                            @PathVariable String                    destinationElementGUID,
                                            @RequestBody  LineageMappingRequestBody requestBody)
    {
        return restAPI.setupLineageMapping(serverName, userId, sourceElementGUID, destinationElementGUID, requestBody);
    }


    /**
     * Retrieve the process call relationship between two elements.  The qualifiedName is optional unless there
     * is more than one process call relationships between these two elements since it is used to disambiguate
     * the request.  This is often used in conjunction with update.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param sourceElementGUID unique identifier of the source
     * @param destinationElementGUID unique identifier of the destination
     * @param requestBody unique identifier for this relationship
     *
     * @return unique identifier and properties of the relationship or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/lineage-mappings/sources/{sourceElementGUID}/destinations/{destinationElementGUID}/retrieve")

    public LineageMappingElementResponse getLineageMapping(@PathVariable String          serverName,
                                                           @PathVariable String          userId,
                                                           @PathVariable String          sourceElementGUID,
                                                           @PathVariable String          destinationElementGUID,
                                                           @RequestBody  NameRequestBody requestBody)
    {
        return restAPI.getLineageMapping(serverName, userId, sourceElementGUID, destinationElementGUID, requestBody);
    }


    /**
     * Remove the lineage mapping between two elements.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param lineageMappingGUID unique identifier of the relationship
     * @param requestBody unique identifiers of software server capability representing the caller (optional)
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/lineage-mappings/{lineageMappingGUID}/remove")

    public VoidResponse clearLineageMapping(@PathVariable String                                 serverName,
                                            @PathVariable String                                 userId,
                                            @PathVariable String                                 lineageMappingGUID,
                                            @RequestBody  EffectiveTimeMetadataSourceRequestBody requestBody)
    {
        return restAPI.clearLineageMapping(serverName, userId, lineageMappingGUID, requestBody);
    }


    /**
     * Retrieve the lineage mapping relationships linked from a specific source element to its destinations.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param sourceElementGUID unique identifier of the source
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody unique identifiers of software server capability representing the caller (optional)
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/lineage-mappings/sources/{sourceElementGUID}/destinations/retrieve")

    public LineageMappingElementsResponse getDestinationLineageMappings(@PathVariable String                   serverName,
                                                                        @PathVariable String                   userId,
                                                                        @PathVariable String                   sourceElementGUID,
                                                                        @RequestParam int                      startFrom,
                                                                        @RequestParam int                      pageSize,
                                                                        @RequestBody  EffectiveTimeRequestBody requestBody)
    {
        return restAPI.getDestinationLineageMappings(serverName, userId, sourceElementGUID, startFrom, pageSize, requestBody);
    }


    /**
     * Retrieve the lineage mapping relationships linked from a specific destination element to its sources.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param destinationElementGUID unique identifier of the destination
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody unique identifiers of software server capability representing the caller (optional)
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/lineage-mappings/destinations/{destinationElementGUID}/sources/retrieve")

    public LineageMappingElementsResponse getSourceLineageMappings(@PathVariable String                   serverName,
                                                                   @PathVariable String                   userId,
                                                                   @PathVariable String                   destinationElementGUID,
                                                                   @RequestParam int                      startFrom,
                                                                   @RequestParam int                      pageSize,
                                                                   @RequestBody  EffectiveTimeRequestBody requestBody)
    {
        return restAPI.getSourceLineageMappings(serverName, userId, destinationElementGUID, startFrom, pageSize, requestBody);
    }
}
