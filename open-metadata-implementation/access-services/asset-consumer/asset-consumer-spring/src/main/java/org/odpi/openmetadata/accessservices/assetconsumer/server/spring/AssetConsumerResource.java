/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetconsumer.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.accessservices.assetconsumer.properties.CommentProperties;
import org.odpi.openmetadata.accessservices.assetconsumer.properties.InformalTagProperties;
import org.odpi.openmetadata.accessservices.assetconsumer.properties.RatingProperties;
import org.odpi.openmetadata.accessservices.assetconsumer.rest.*;
import org.odpi.openmetadata.accessservices.assetconsumer.server.AssetConsumerRESTServices;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.springframework.web.bind.annotation.*;


/**
 * The AssetConsumerResource provides the server-side implementation of the Asset Consumer Open Metadata
 * Assess Service (OMAS).  This interface provides connections to assets and APIs for adding feedback
 * on the asset.
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/access-services/asset-consumer/users/{userId}")

@Tag(name="Asset Consumer OMAS", description="The Asset Consumer OMAS provides services to an individual who wants to work with assets such as: data stores, data sets and data feeds, reports, APIs, functions such as analytical services", externalDocs=@ExternalDocumentation(description="Asset Consumer Open Metadata Access Service (OMAS)",url="https://egeria.odpi.org/open-metadata-implementation/access-services/asset-consumer/"))

public class AssetConsumerResource
{
    private AssetConsumerRESTServices  restAPI = new AssetConsumerRESTServices();

    /**
     * Default constructor
     */
    public AssetConsumerResource()
    {
    }


    /**
     * Return the connection object for the Asset Consumer's OMAS's out topic.
     *
     * @param serverName name of the server to route the request to
     * @param userId identifier of calling user
     * @param callerId unique identifier of the caller
     *
     * @return connection object for the out topic or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem retrieving the discovery engine definition.
     */
    @GetMapping(path = "/topics/out-topic-connection/{callerId}")

    public ConnectionResponse getOutTopicConnection(@PathVariable String serverName,
                                                    @PathVariable String userId,
                                                    @PathVariable String callerId)
    {
        return restAPI.getOutTopicConnection(serverName, userId, callerId);
    }


    /**
     * Adds a reply to a comment.
     *
     * @param serverName    name of the server instances for this request.
     * @param userId        String - userId of user making request.
     * @param assetGUID     String - unique id of asset that this chain of comments is linked.
     * @param commentGUID   String - unique id for an existing comment.  Used to add a reply to a comment.
     * @param requestBody   containing type of comment enum and the text of the comment.
     *
     * @return assetGUID for new comment object or
     * InvalidParameterException one of the parameters is null or invalid or
     * PropertyServerException There is a problem adding the asset properties to
     *                                   the metadata repository or
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/assets/{assetGUID}/comments/{commentGUID}/replies")

    public GUIDResponse addCommentReply(@PathVariable String            serverName,
                                        @PathVariable String            userId,
                                        @PathVariable String            assetGUID,
                                        @PathVariable String            commentGUID,
                                        @RequestBody  CommentProperties requestBody)
    {
        return restAPI.addCommentReply(serverName, userId, assetGUID, commentGUID, requestBody);
    }


    /**
     * Creates a comment and attaches it to an asset.
     *
     * @param serverName name of the server instances for this request.
     * @param userId      String - userId of user making request.
     * @param assetGUID        String - unique id for the asset.
     * @param requestBody containing type of comment enum and the text of the comment.
     *
     * @return assetGUID for new comment object or
     * InvalidParameterException one of the parameters is null or invalid or
     * PropertyServerException There is a problem adding the asset properties to
     *                                   the metadata repository or
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/assets/{assetGUID}/comments")

    public GUIDResponse addCommentToAsset(@PathVariable String            serverName,
                                          @PathVariable String            userId,
                                          @PathVariable String            assetGUID,
                                          @RequestBody  CommentProperties requestBody)
    {
        return restAPI.addCommentToAsset(serverName, userId, assetGUID, requestBody);
    }


    /**
     * Creates a "like" object and attaches it to an asset.
     *
     * @param serverName  name of the server instances for this request.
     * @param userId      String - userId of user making request.
     * @param assetGUID   String - unique id for the asset.
     * @param requestBody feedback request body.
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid or
     * PropertyServerException There is a problem adding the asset properties to
     *                                   the metadata repository or
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/assets/{assetGUID}/likes")

    public VoidResponse addLikeToAsset(@PathVariable String              serverName,
                                       @PathVariable String              userId,
                                       @PathVariable String              assetGUID,
                                       @RequestBody  FeedbackRequestBody requestBody)
    {
        return restAPI.addLikeToAsset(serverName, userId, assetGUID, requestBody);
    }


    /**
     * Creates an audit log record for the asset.  This log record is stored in the local server's Audit Log.
     *
     * @param serverName name of the server instances for this request.
     * @param userId  String - userId of user making request.
     * @param assetGUID  String - unique id for the asset.
     * @param requestBody containing:
     * connectorInstanceId  (String - (optional) id of connector in use (if any)),
     * connectionName  (String - (optional) name of the connection (extracted from the connector)),
     * connectorType  (String - (optional) type of connector in use (if any)),
     * contextId  (String - (optional) function name, or processId of the activity that the caller is performing),
     * message  (log record content).
     *
     * @return void or
     * InvalidParameterException - one of the parameters is null or invalid or
     * PropertyServerException - there is a problem adding the log message to the audit log for this asset or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/assets/{assetGUID}/log-records")

    public VoidResponse addLogMessageToAsset(@PathVariable String                serverName,
                                             @PathVariable String                userId,
                                             @PathVariable String                assetGUID,
                                             @RequestBody  LogRecordRequestBody requestBody)
    {
        return restAPI.addLogMessageToAsset(serverName, userId, assetGUID, requestBody);
    }


    /**
     * Adds a star rating and optional review text to the asset.
     *
     * @param serverName  name of the server instances for this request.
     * @param userId      String - userId of user making request.
     * @param assetGUID        String - unique id for the asset.
     * @param requestBody containing the StarRating and user review of asset.
     *
     * @return assetGUID for new review object or
     * InvalidParameterException one of the parameters is null or invalid or
     * PropertyServerException There is a problem adding the asset properties to the metadata repository or
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/assets/{assetGUID}/ratings")

    public VoidResponse addRatingToAsset(@PathVariable String           serverName,
                                         @PathVariable String           userId,
                                         @PathVariable String           assetGUID,
                                         @RequestBody  RatingProperties requestBody)
    {
        return restAPI.addRatingToAsset(serverName, userId, assetGUID, requestBody);
    }


    /**
     * Adds a tag (either private of public) to an asset.
     *
     * @param serverName       name of the server instances for this request.
     * @param userId           userId of user making request.
     * @param assetGUID        unique id for the asset.
     * @param tagGUID          unique id of the tag.
     * @param requestBody      null request body needed for correct protocol exchange.
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid or
     * PropertyServerException There is a problem adding the asset properties to the metadata repository or
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/assets/{assetGUID}/tags/{tagGUID}")

    public VoidResponse addTagToAsset(@PathVariable String              serverName,
                                      @PathVariable String              userId,
                                      @PathVariable String              assetGUID,
                                      @PathVariable String              tagGUID,
                                      @RequestBody  FeedbackRequestBody requestBody)
    {
        return restAPI.addTagToAsset(serverName, userId, assetGUID, tagGUID, requestBody);
    }


    /**
     * Adds a tag (either private of public) to an element attached to an asset - such as schema element, glossary term, ...
     *
     * @param serverName       name of the server instances for this request.
     * @param userId           userId of user making request.
     * @param elementGUID        unique id for the element.
     * @param tagGUID          unique id of the tag.
     * @param requestBody      null request body needed for correct protocol exchange.
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid or
     * PropertyServerException There is a problem adding the asset properties to the metadata repository or
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/assets/elements/{elementGUID}/tags/{tagGUID}")

    public VoidResponse addTagToElement(@PathVariable String              serverName,
                                        @PathVariable String              userId,
                                        @PathVariable String              elementGUID,
                                        @PathVariable String              tagGUID,
                                        @RequestBody  FeedbackRequestBody requestBody)
    {
        return restAPI.addTagToElement(serverName, userId, elementGUID, tagGUID, requestBody);
    }


    /**
     * Creates a new informal tag and returns the unique identifier for it.
     *
     * @param serverName   name of the server instances for this request.
     * @param userId       userId of user making request.
     * @param requestBody  public/private flag, name of the tag and (optional) description of the tag.
     *
     * @return new assetGUID or
     * InvalidParameterException one of the parameters is null or invalid or
     * PropertyServerException There is a problem adding the asset properties to the metadata repository or
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/tags")

    public GUIDResponse createTag(@PathVariable String                serverName,
                                  @PathVariable String                userId,
                                  @RequestBody  InformalTagProperties requestBody)
    {
        return restAPI.createTag(serverName, userId, requestBody);
    }


    /**
     * Removes a tag from the repository.  All of the relationships to referenceables are lost.
     *
     * @param serverName   name of the server instances for this request
     * @param userId       String - userId of user making request.
     * @param tagGUID      String - unique id for the tag.
     * @param requestBody  null request body.
     *
     * @return void or
     * InvalidParameterException - one of the parameters is null or invalid or
     * PropertyServerException - there is a problem updating the asset properties in the metadata repository or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/tags/{tagGUID}/delete")

    public VoidResponse   deleteTag(@PathVariable String          serverName,
                                    @PathVariable String          userId,
                                    @PathVariable String          tagGUID,
                                    @RequestBody  NullRequestBody requestBody)
    {
        return restAPI.deleteTag(serverName, userId, tagGUID, requestBody);
    }


    /**
     * Returns the unique identifier for the asset connected to the connection identified by the supplied name.
     *
     * @param serverName name of the server instances for this request.
     * @param userId the userId of the requesting user.
     * @param connectionName  uniqueId for the connection.
     *
     * @return unique identifier of asset or
     * InvalidParameterException one of the parameters is null or invalid or
     * PropertyServerException there is a problem retrieving the connected asset properties from the property server or
     * UnrecognizedConnectionGUIDException the supplied GUID is not recognized by the property server or
     * NoConnectedAssetException there is no asset associated with this connection or
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @GetMapping(path = "/assets/by-connection-name/{connectionName}")

    public GUIDResponse getAssetForConnectionName(@PathVariable String   serverName,
                                                  @PathVariable String   userId,
                                                  @PathVariable String   connectionName)
    {
        return restAPI.getAssetForConnectionName(serverName, userId, connectionName);
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
     * @return list of unique identifiers for assets that match the search string or
     * InvalidParameterException the searchString is invalid or
     * PropertyServerException there is a problem access in the property server or
     * UserNotAuthorizedException the user does not have access to the properties
     */
    @PostMapping(path = "/assets/by-search-string")

    public GUIDListResponse findAssets(@PathVariable String   serverName,
                                       @PathVariable String   userId,
                                       @RequestParam int      startFrom,
                                       @RequestParam int      pageSize,
                                       @RequestBody  String   searchString)
    {
        return restAPI.findAssets(serverName, userId, searchString, startFrom, pageSize);
    }


    /**
     * Return the list of unique identifiers for assets that are linked to a specific (meaning) either directly or via
     * fields in the schema.
     *
     * @param serverName name of the server instances for this request
     * @param userId the name of the calling user.
     * @param termGUID unique identifier of term.
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return asset guid list or
     * InvalidParameterException the userId is null or invalid or
     * PropertyServerException there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @GetMapping(path = "/assets/by-meaning/{termGUID}")

    public GUIDListResponse getAssetsByMeaning(@PathVariable String serverName,
                                               @PathVariable String userId,
                                               @PathVariable String termGUID,
                                               @RequestParam int    startFrom,
                                               @RequestParam int    pageSize)
    {
        return restAPI.getAssetsByMeaning(serverName, userId, termGUID, startFrom, pageSize);
    }


    /**
     * Return a list of assets with the requested name.
     *
     * @param serverName name of the server instances for this request
     * @param userId calling user
     * @param name name to search for
     * @param startFrom starting element (used in paging through large result sets)
     * @param pageSize maximum number of results to return
     *
     * @return list of unique identifiers for matching assets or
     * InvalidParameterException the name is invalid or
     * PropertyServerException there is a problem access in the property server or
     * UserNotAuthorizedException the user does not have access to the properties
     */
    @PostMapping(path = "/assets/by-name")

    public GUIDListResponse getAssetsByName(@PathVariable String   serverName,
                                            @PathVariable String   userId,
                                            @RequestParam int      startFrom,
                                            @RequestParam int      pageSize,
                                            @RequestBody  String   name)
    {
        return restAPI.getAssetsByName(serverName, userId, name, startFrom, pageSize);
    }


    /**
     * Return the list of unique identifiers for assets that are linked to a specific tag either directly, or via one
     * of its schema elements.
     *
     * @param serverName name of the server instances for this request
     * @param userId the name of the calling user.
     * @param tagGUID unique identifier of tag.
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return asset guid list or
     * InvalidParameterException the userId is null or invalid or
     * PropertyServerException there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @GetMapping(path = "/assets/by-tag/{tagGUID}")

    public GUIDListResponse getAssetsByTag(@PathVariable String serverName,
                                           @PathVariable String userId,
                                           @PathVariable String tagGUID,
                                           @RequestParam int    startFrom,
                                           @RequestParam int    pageSize)

    {
        return restAPI.getAssetsByTag(serverName, userId, tagGUID, startFrom, pageSize);
    }


    /**
     * Return the full definition (meaning) of the terms exactly matching the supplied name.
     *
     * @param serverName name of the server instances for this request.
     * @param userId the name of the calling user.
     * @param name name of term.
     * @param startFrom  index of the list to start from (0 for start).
     * @param pageSize   maximum number of elements to return.
     * @return list of meaning objects or
     * InvalidParameterException - one of the parameters is invalid or
     * PropertyServerException - there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/meanings/by-name")

    public GlossaryTermListResponse getMeaningByName(@PathVariable String   serverName,
                                                     @PathVariable String   userId,
                                                     @RequestParam int      startFrom,
                                                     @RequestParam int      pageSize,
                                                     @RequestBody  String   name)
    {
        return restAPI.getMeaningByName(serverName, userId, name, startFrom, pageSize);
    }


    /**
     * Return the full definition (meaning) of the terms matching the supplied name.
     *
     * @param serverName name of the server instances for this request.
     * @param userId the name of the calling user.
     * @param name name of term.  This may include wild card characters.
     * @param startFrom  index of the list to start from (0 for start).
     * @param pageSize   maximum number of elements to return.
     * @return list of meaning objects or
     * InvalidParameterException - one of the parameters is invalid or
     * PropertyServerException - there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/meanings/by-search-string")

    public GlossaryTermListResponse findMeanings(@PathVariable String   serverName,
                                                 @PathVariable String   userId,
                                                 @RequestParam int      startFrom,
                                                 @RequestParam int      pageSize,
                                                 @RequestBody  String   name)
    {
        return restAPI.findMeanings(serverName, userId, name, startFrom, pageSize);
    }


    /**
     * Return the full definition (meaning) of a term using the unique identifier of the glossary term.
     *
     * @param serverName name of the server instances for this request.
     * @param userId userId of the user making the request.
     * @param termGUID unique identifier of the glossary term that contains the meaning.
     *
     * @return meaning object or
     * InvalidParameterException the userId is null or invalid or
     * PropertyServerException there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @GetMapping(path = "/meanings/{termGUID}")

    public GlossaryTermResponse getMeaning(@PathVariable String   serverName,
                                           @PathVariable String   userId,
                                           @PathVariable String   termGUID)
    {
        return restAPI.getMeaning(serverName, userId, termGUID);
    }


    /**
     * Return the Informal Tag for the supplied unique identifier (assetGUID).
     *
     * @param serverName name of the server instances for this request.
     * @param userId userId of the user making the request.
     * @param tagGUID unique identifier of the meaning.
     *
     * @return tag object or
     * InvalidParameterException the userId is null or invalid or
     * PropertyServerException there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @GetMapping(path = "/tags/{tagGUID}")

    public TagResponse getTag(@PathVariable String   serverName,
                              @PathVariable String   userId,
                              @PathVariable String   tagGUID)
    {
        return restAPI.getTag(serverName, userId, tagGUID);
    }


    /**
     * Return the tags exactly matching the supplied name.
     *
     * @param serverName name of the server instances for this request.
     * @param userId the name of the calling user.
     * @param tagName name of tag.
     * @param startFrom  index of the list to start from (0 for start).
     * @param pageSize   maximum number of elements to return.
     * @return list of tag objects or
     * InvalidParameterException - one of the parameters is invalid or
     * PropertyServerException - there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/tags/by-name")

    public TagsResponse getTagsByName(@PathVariable String  serverName,
                                      @PathVariable String  userId,
                                      @RequestParam int     startFrom,
                                      @RequestParam int     pageSize,
                                      @RequestBody  String  tagName)
    {
        return restAPI.getTagsByName(serverName, userId, tagName, startFrom, pageSize);
    }


    /**
     * Return the list of the calling user's private tags exactly matching the supplied name.
     *
     * @param serverName name of the server instances for this request.
     * @param userId the name of the calling user.
     * @param tagName name of tag.
     * @param startFrom  index of the list to start from (0 for start).
     * @param pageSize   maximum number of elements to return.
     * @return list of tag objects or
     * InvalidParameterException - one of the parameters is invalid or
     * PropertyServerException - there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/tags/private/by-name")

    public TagsResponse getMyTagsByName(@PathVariable String  serverName,
                                        @PathVariable String  userId,
                                        @RequestParam int     startFrom,
                                        @RequestParam int     pageSize,
                                        @RequestBody  String  tagName)
    {
        return restAPI.getMyTagsByName(serverName, userId, tagName, startFrom, pageSize);
    }


    /**
     * Return the list of tags containing the supplied string in either the name or description.
     *
     * @param serverName name of the server instances for this request.
     * @param userId the name of the calling user.
     * @param tagName name of tag.  This may include wild card characters.
     * @param startFrom  index of the list to start from (0 for start).
     * @param pageSize   maximum number of elements to return.
     * @return list of tag objects or
     * InvalidParameterException - one of the parameters is invalid or
     * PropertyServerException - there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/tags/by-search-string")

    public TagsResponse findTags(@PathVariable String  serverName,
                                 @PathVariable String  userId,
                                 @RequestParam int     startFrom,
                                 @RequestParam int     pageSize,
                                 @RequestBody  String  tagName)
    {
        return restAPI.findTags(serverName, userId, tagName, startFrom, pageSize);
    }


    /**
     * Return the list of the calling user's private tags containing the supplied string in either the name or description.
     *
     * @param serverName name of the server instances for this request.
     * @param userId the name of the calling user.
     * @param tagName name of tag.  This may include wild card characters.
     * @param startFrom  index of the list to start from (0 for start).
     * @param pageSize   maximum number of elements to return.
     * @return list of tag objects or
     * InvalidParameterException - one of the parameters is invalid or
     * PropertyServerException - there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/tags/private/by-search-string")

    public TagsResponse findMyTags(@PathVariable String  serverName,
                                   @PathVariable String  userId,
                                   @RequestParam int     startFrom,
                                   @RequestParam int     pageSize,
                                   @RequestBody  String  tagName)
    {
        return restAPI.findMyTags(serverName, userId, tagName, startFrom, pageSize);
    }


    /**
     * Removes a comment added to the asset by this user.
     *
     * @param serverName name of the server instances for this request
     * @param userId       String - userId of user making request.
     * @param assetGUID  String - unique id for the asset object
     * @param commentGUID  String - unique id for the comment object
     * @param requestBody  containing type of comment enum and the text of the comment.
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid.
     * PropertyServerException There is a problem updating the asset properties in the metadata repository.
     * UserNotAuthorizedException the user does not have permission to perform this request.
     */
    @PostMapping(path = "/assets/{assetGUID}/comments/{commentGUID}/delete")

    public VoidResponse removeCommentFromAsset(@PathVariable                  String          serverName,
                                               @PathVariable                  String          userId,
                                               @PathVariable                  String          assetGUID,
                                               @PathVariable                  String          commentGUID,
                                               @RequestBody(required = false) NullRequestBody requestBody)
    {
        return restAPI.removeCommentFromAsset(serverName, userId, assetGUID, commentGUID, requestBody);
    }


    /**
     * Removes a "LikeProperties" added to the asset by this user.
     *
     * @param serverName   name of the server instances for this request.
     * @param userId       String - userId of user making request.
     * @param assetGUID    unique identifier for the asset where the like is attached.
     * @param requestBody  containing type of comment enum and the text of the comment.
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid.
     * PropertyServerException There is a problem updating the asset properties in the metadata repository.
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/assets/{assetGUID}/likes/delete")

    public VoidResponse   removeLikeFromAsset(@PathVariable                  String          serverName,
                                              @PathVariable                  String          userId,
                                              @PathVariable                  String          assetGUID,
                                              @RequestBody(required = false) NullRequestBody requestBody)
    {
        return restAPI.removeLikeFromAsset(serverName, userId, assetGUID, requestBody);
    }


    /**
     * Removes of a star rating that was added to the asset by this user.
     *
     * @param serverName   name of the server instances for this request.
     * @param userId       String - userId of user making request.
     * @param assetGUID         unique identifier for the asset where the rating is attached.
     * @param requestBody  containing type of comment enum and the text of the comment.
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid.
     * PropertyServerException There is a problem updating the asset properties in the metadata repository.
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/assets/{assetGUID}/ratings/delete")

    public VoidResponse   removeRatingFromAsset(@PathVariable                  String          serverName,
                                                @PathVariable                  String          userId,
                                                @PathVariable                  String          assetGUID,
                                                @RequestBody(required = false) NullRequestBody requestBody)
    {
        return restAPI.removeRatingFromAsset(serverName, userId, assetGUID, requestBody);
    }


    /**
     * Removes a link between a tag and an asset that was added by this user.
     *
     * @param serverName   name of the server instances for this request.
     * @param userId       String - userId of user making request.
     * @param assetGUID    unique id for the asset.
     * @param tagGUID      unique id of the tag.
     * @param requestBody  null request body needed for correct protocol exchange.
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid.
     * PropertyServerException There is a problem updating the asset properties in the metadata repository.
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/assets/{assetGUID}/tags/{tagGUID}/delete")

    public VoidResponse   removeTagFromAsset(@PathVariable                  String          serverName,
                                             @PathVariable                  String          userId,
                                             @PathVariable                  String          assetGUID,
                                             @PathVariable                  String          tagGUID,
                                             @RequestBody(required = false) NullRequestBody requestBody)
    {
        return restAPI.removeTagFromAsset(serverName, userId, assetGUID, tagGUID, requestBody);
    }


    /**
     * Removes a tag from an element attached to an asset - such as schema element, glossary term, ... that was added by this user.
     *
     * @param serverName   name of the server instances for this request.
     * @param userId       String - userId of user making request.
     * @param elementGUID    unique id for the element.
     * @param tagGUID      unique id of the tag.
     * @param requestBody  null request body needed for correct protocol exchange.
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid.
     * PropertyServerException There is a problem updating the asset properties in the metadata repository.
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/assets/elements/{elementGUID}/tags/{tagGUID}/delete")

    public VoidResponse   removeTagFromElement(@PathVariable                  String          serverName,
                                               @PathVariable                  String          userId,
                                               @PathVariable                  String          elementGUID,
                                               @PathVariable                  String          tagGUID,
                                               @RequestBody(required = false) NullRequestBody requestBody)
    {
        return restAPI.removeTagFromElement(serverName, userId, elementGUID, tagGUID, requestBody);
    }


    /**
     * Update an existing comment.
     *
     * @param serverName   name of the server instances for this request.
     * @param userId       userId of user making request.
     * @param assetGUID    unique identifier for the asset that the comment is attached to (directly or indirectly).
     * @param commentGUID  unique identifier for the comment to change.
     * @param requestBody  containing type of comment enum and the text of the comment.
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid.
     * PropertyServerException There is a problem updating the asset properties in the metadata repository.
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "assets/{assetGUID}/comments/{commentGUID}/update")

    public VoidResponse   updateComment(@PathVariable String            serverName,
                                        @PathVariable String            userId,
                                        @PathVariable String            assetGUID,
                                        @PathVariable String            commentGUID,
                                        @RequestBody  CommentProperties requestBody)
    {
        return restAPI.updateComment(serverName, userId, assetGUID, commentGUID, requestBody);
    }


    /**
     * Updates the description of an existing tag (either private or public).
     *
     * @param serverName   name of the server instances for this request
     * @param userId       userId of user making request.
     * @param tagGUID      unique id for the tag.
     * @param requestBody  contains the name of the tag and (optional) description of the tag.
     *
     * @return void or
     * InvalidParameterException - one of the parameters is invalid or
     * PropertyServerException - there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/tags/{tagGUID}/update")

    public VoidResponse   updateTagDescription(@PathVariable String                serverName,
                                               @PathVariable String                userId,
                                               @PathVariable String                tagGUID,
                                               @RequestBody  InformalTagProperties requestBody)
    {
        return restAPI.updateTagDescription(serverName, userId, tagGUID, requestBody);
    }
}
