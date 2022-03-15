/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.itinfrastructure.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.accessservices.itinfrastructure.rest.AssetExtensionsRequestBody;
import org.odpi.openmetadata.accessservices.itinfrastructure.rest.AssetListResponse;
import org.odpi.openmetadata.accessservices.itinfrastructure.rest.AssetRelationshipListResponse;
import org.odpi.openmetadata.accessservices.itinfrastructure.rest.AssetRequestBody;
import org.odpi.openmetadata.accessservices.itinfrastructure.rest.AssetResponse;
import org.odpi.openmetadata.accessservices.itinfrastructure.rest.EffectiveTimeMetadataSourceRequestBody;
import org.odpi.openmetadata.accessservices.itinfrastructure.rest.ElementStatusRequestBody;
import org.odpi.openmetadata.accessservices.itinfrastructure.rest.MetadataSourceRequestBody;
import org.odpi.openmetadata.accessservices.itinfrastructure.rest.RelatedAssetListResponse;
import org.odpi.openmetadata.accessservices.itinfrastructure.rest.TemplateRequestBody;
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

@Tag(name="IT Infrastructure OMAS",
     description="The IT Infrastructure OMAS provides APIs for tools and applications managing the IT infrastructure that supports the data assets.\n",
     externalDocs=@ExternalDocumentation(description="IT Infrastructure Open Metadata Access Service (OMAS)",
                                         url="https://egeria-project.org/services/omas/it-infrastructure/overview/"))


public class ITAssetResource
{
    private static ITAssetRESTService restAPI = new ITAssetRESTService();

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
     * Create a new metadata element to represent a asset.
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
     * Create a new metadata element to represent a asset using an existing metadata element as a template.
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
     * Update the metadata element representing a asset.
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
     * Create a relationship between a asset and a asseted asset.
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
     * @param isMergeUpdate   should the supplied properties be merged with existing properties (true) by replacing the just the properties with
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
     * Remove a relationship between a asset and a related asset.
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
     * Update the properties of a classification for a asset.
     *
     * @param serverName name of the server to route the request to.
     * @param userId calling user
     * @param assetTypeName name of type for the asset
     * @param assetGUID unique identifier of the asset
     * @param classificationName name of the classification type
     * @param isMergeUpdate   should the supplied properties be merged with existing properties (true) by replacing the just the properties with
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
     * Update the zones for the asset asset so that it becomes visible to consumers.
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
     * Update the zones for the asset asset so that it is no longer visible to consumers.
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
     * Remove the metadata element representing a asset.
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
        // todo
        return null;    // not sure this is needed
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
}
