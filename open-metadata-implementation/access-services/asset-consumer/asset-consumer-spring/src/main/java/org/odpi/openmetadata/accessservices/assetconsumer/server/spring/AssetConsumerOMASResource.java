/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetconsumer.server.spring;

import org.odpi.openmetadata.accessservices.assetconsumer.rest.*;
import org.odpi.openmetadata.accessservices.assetconsumer.server.AssetConsumerRESTServices;
import org.springframework.web.bind.annotation.*;


/**
 * The AssetConsumerOMASResource provides the server-side implementation of the Asset Consumer Open Metadata
 * Assess Service (OMAS).  This interface provides connections to assets and APIs for adding feedback
 * on the asset.
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/access-services/asset-consumer/users/{userId}")
public class AssetConsumerOMASResource
{
    private AssetConsumerRESTServices  restAPI = new AssetConsumerRESTServices();

    /**
     * Default constructor
     */
    public AssetConsumerOMASResource()
    {
    }


    /**
     * Adds a reply to a comment.
     *
     * @param serverName    name of the server instances for this request.
     * @param userId        String - userId of user making request.
     * @param commentGUID   String - unique id for an existing comment.  Used to add a reply to a comment.
     * @param requestBody   containing type of comment enum and the text of the comment.
     *
     * @return guid for new comment object or
     * InvalidParameterException one of the parameters is null or invalid or
     * PropertyServerException There is a problem adding the asset properties to
     *                                   the metadata repository or
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @RequestMapping(method = RequestMethod.POST, path = "/comments/{commentGUID}/replies")

    public GUIDResponse addCommentReply(@PathVariable String             serverName,
                                        @PathVariable String             userId,
                                        @PathVariable String             commentGUID,
                                        @RequestBody  CommentRequestBody requestBody)
    {
        return restAPI.addCommentReply(serverName, userId, commentGUID, requestBody);
    }


    /**
     * Creates a comment and attaches it to an asset.
     *
     * @param serverName name of the server instances for this request.
     * @param userId      String - userId of user making request.
     * @param guid        String - unique id for the asset.
     * @param requestBody containing type of comment enum and the text of the comment.
     *
     * @return guid for new comment object or
     * InvalidParameterException one of the parameters is null or invalid or
     * PropertyServerException There is a problem adding the asset properties to
     *                                   the metadata repository or
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @RequestMapping(method = RequestMethod.POST, path = "/assets/{guid}/comments")

    public GUIDResponse addCommentToAsset(@PathVariable String              serverName,
                                          @PathVariable String              userId,
                                          @PathVariable String              guid,
                                          @RequestBody  CommentRequestBody  requestBody)
    {
        return restAPI.addCommentToAsset(serverName, userId, guid, requestBody);
    }


    /**
     * Creates a "like" object and attaches it to an asset.
     *
     * @param serverName  name of the server instances for this request.
     * @param userId      String - userId of user making request.
     * @param guid        String - unique id for the asset.
     * @param requestBody null request body to satisfy HTTP protocol.
     *
     * @return guid for new like object or
     * InvalidParameterException one of the parameters is null or invalid or
     * PropertyServerException There is a problem adding the asset properties to
     *                                   the metadata repository or
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @RequestMapping(method = RequestMethod.POST, path = "/assets/{guid}/likes")

    public GUIDResponse addLikeToAsset(@PathVariable String          serverName,
                                       @PathVariable String          userId,
                                       @PathVariable String          guid,
                                       @RequestBody  NullRequestBody requestBody)
    {
        return restAPI.addLikeToAsset(serverName, userId, guid, requestBody);
    }


    /**
     * Creates an audit log record for the asset.  This log record is stored in the local server's Audit Log.
     *
     * @param serverName name of the server instances for this request.
     * @param userId  String - userId of user making request.
     * @param guid  String - unique id for the asset.
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
    @RequestMapping(method = RequestMethod.POST, path = "/assets/{guid}/log-records")

    public VoidResponse addLogMessageToAsset(@PathVariable String                serverName,
                                             @PathVariable String                userId,
                                             @PathVariable String                guid,
                                             @RequestBody  LogRecordRequestBody  requestBody)
    {
        return restAPI.addLogMessageToAsset(serverName, userId, guid, requestBody);
    }


    /**
     * Adds a star rating and optional review text to the asset.
     *
     * @param serverName  name of the server instances for this request.
     * @param userId      String - userId of user making request.
     * @param guid        String - unique id for the asset.
     * @param requestBody containing the StarRating and user review of asset.
     *
     * @return guid for new review object or
     * InvalidParameterException one of the parameters is null or invalid or
     * PropertyServerException There is a problem adding the asset properties to the metadata repository or
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @RequestMapping(method = RequestMethod.POST, path = "/assets/{guid}/reviews")

    public GUIDResponse addReviewToAsset(@PathVariable String             serverName,
                                         @PathVariable String             userId,
                                         @PathVariable String             guid,
                                         @RequestBody  ReviewRequestBody requestBody)
    {
        return restAPI.addReviewToAsset(serverName, userId, guid, requestBody);
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
    @RequestMapping(method = RequestMethod.POST, path = "/assets/{assetGUID}/tags/{tagGUID}")

    public VoidResponse addTagToAsset(@PathVariable String          serverName,
                                      @PathVariable String          userId,
                                      @PathVariable String          assetGUID,
                                      @PathVariable String          tagGUID,
                                      @RequestBody  NullRequestBody requestBody)
    {
        return restAPI.addTagToAsset(serverName, userId, assetGUID, tagGUID, requestBody);
    }


    /**
     * Creates a new private informal tag and returns the unique identifier for it.
     *
     * @param serverName   name of the server instances for this request.
     * @param userId       userId of user making request.
     * @param requestBody  name of the tag and (optional) description of the tag.
     *
     * @return new guid or
     * InvalidParameterException one of the parameters is null or invalid or
     * PropertyServerException There is a problem adding the asset properties to the metadata repository or
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @RequestMapping(method = RequestMethod.POST, path = "/tags/private")

    public GUIDResponse createPrivateTag(@PathVariable String         serverName,
                                         @PathVariable String         userId,
                                         @RequestBody  TagRequestBody requestBody)
    {
        return restAPI.createPrivateTag(serverName, userId, requestBody);
    }


    /**
     * Creates a new public informal tag and returns the unique identifier for it.
     *
     * @param serverName   name of the server instances for this request.
     * @param userId       userId of user making request.
     * @param requestBody  name of the tag and (optional) description of the tag.
     *                     Setting a description, particularly in a public tag
     *                     makes the tag more valuable to other users and can act as an embryonic glossary term.
     *
     * @return new guid or
     * InvalidParameterException one of the parameters is null or invalid or
     * PropertyServerException There is a problem adding the asset properties to the metadata repository or
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @RequestMapping(method = RequestMethod.POST, path = "/tags/public")

    public GUIDResponse createPublicTag(@PathVariable String         serverName,
                                        @PathVariable String         userId,
                                        @RequestBody  TagRequestBody requestBody)
    {
        return restAPI.createPublicTag(serverName, userId, requestBody);
    }


    /**
     * Removes a tag from the asset that was added by this user.
     *
     * @param serverName   name of the server instances for this request
     * @param userId       String - userId of user making request.
     * @param guid         String - unique id for the tag.
     * @param requestBody  null request body.
     *
     * @return void or
     * InvalidParameterException - one of the parameters is null or invalid or
     * PropertyServerException - there is a problem updating the asset properties in the metadata repository or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    @RequestMapping(method = RequestMethod.POST, path = "/tags/{guid}/delete")

    public VoidResponse   deleteTag(@PathVariable String          serverName,
                                    @PathVariable String          userId,
                                    @PathVariable String          guid,
                                    @RequestBody  NullRequestBody requestBody)
    {
        return restAPI.deleteTag(serverName, userId, guid, requestBody);
    }


    /**
     * Returns the unique identifier for the asset connected to the connection identified by the supplied guid.
     *
     * @param serverName name of the server instances for this request.
     * @param userId the userId of the requesting user.
     * @param connectionGUID  uniqueId for the connection.
     *
     * @return unique identifier of asset or
     * InvalidParameterException one of the parameters is null or invalid or
     * PropertyServerException there is a problem retrieving the connected asset properties from the property server or
     * UnrecognizedConnectionGUIDException the supplied GUID is not recognized by the property server or
     * NoConnectedAssetException there is no asset associated with this connection or
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @RequestMapping(method = RequestMethod.GET, path = "/assets/by-connection/{connectionGUID}")

    public GUIDResponse getAssetForConnectionGUID(@PathVariable String   serverName,
                                                  @PathVariable String   userId,
                                                  @PathVariable String   connectionGUID)
    {
        return restAPI.getAssetForConnection(serverName, userId, connectionGUID);
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
    @RequestMapping(method = RequestMethod.GET, path = "/assets/by-connection-name/{connectionName}")

    public GUIDResponse getAssetForConnectionName(@PathVariable String   serverName,
                                                  @PathVariable String   userId,
                                                  @PathVariable String   connectionName)
    {
        return restAPI.getAssetForConnection(serverName, userId, connectionName);
    }


    /**
     * Returns the connection object corresponding to the supplied connection GUID.
     *
     * @param serverName name of the server instances for this request.
     * @param userId userId of user making request.
     * @param guid  the unique id for the connection within the property server.
     *
     * @return connection object or
     * InvalidParameterException one of the parameters is null or invalid or
     * UnrecognizedConnectionGUIDException the supplied GUID is not recognized by the metadata repository or
     * PropertyServerException there is a problem retrieving information from the property (metadata) server or
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @RequestMapping(method = RequestMethod.GET, path = "/connections/{guid}")

    public ConnectionResponse getConnectionByGUID(@PathVariable String   serverName,
                                                  @PathVariable String     userId,
                                                  @PathVariable String     guid)
    {
        return restAPI.getConnectionByGUID(serverName, userId, guid);
    }


    /**
     * Returns the connection object corresponding to the supplied connection name.
     *
     * @param serverName name of the server instances for this request
     * @param userId userId of user making request.
     * @param name   this may be the qualifiedName or displayName of the connection.
     *
     * @return connection object or
     * InvalidParameterException one of the parameters is null or invalid or
     * UnrecognizedConnectionNameException there is no connection defined for this name or
     * AmbiguousConnectionNameException there is more than one connection defined for this name or
     * PropertyServerException there is a problem retrieving information from the property (metadata) server or
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @RequestMapping(method = RequestMethod.GET, path = "/connections/by-name/{name}")

    public ConnectionResponse getConnectionByName(@PathVariable String   serverName,
                                                  @PathVariable String   userId,
                                                  @PathVariable String   name)
    {
        return restAPI.getConnectionByName(serverName, userId, name);
    }


    /**
     * Return the full definition (meaning) of the terms matching the supplied name.
     *
     * @param serverName name of the server instances for this request.
     * @param userId the name of the calling user.
     * @param term name of term.  This may include wild card characters.
     * @param startFrom  index of the list ot start from (0 for start).
     * @param pageSize   maximum number of elements to return.
     * @return list of meaning objects or
     * InvalidParameterException - one of the parameters is invalid or
     * PropertyServerException - there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    @RequestMapping(method = RequestMethod.GET, path = "/meanings/by-name/{term}")

    public MeaningListResponse getMeaningByName(@PathVariable String  serverName,
                                                @PathVariable String  userId,
                                                @PathVariable String  term,
                                                @RequestParam int     startFrom,
                                                @RequestParam int     pageSize)
    {
        return restAPI.getMeaningByName(serverName, userId, term, startFrom, pageSize);
    }


    /**
     * Return the full definition (meaning) of a term using the unique identifier of the glossary term.
     *
     * @param serverName name of the server instances for this request.
     * @param userId userId of the user making the request.
     * @param guid unique identifier of the meaning.
     *
     * @return meaning object or
     * InvalidParameterException the userId is null or invalid or
     * PropertyServerException there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @RequestMapping(method = RequestMethod.GET, path = "/meanings/{guid}")

    public MeaningResponse getMeaning(@PathVariable String   serverName,
                                      @PathVariable String   userId,
                                      @PathVariable String   guid)
    {
        return restAPI.getMeaning(serverName, userId, guid);
    }


    /**
     * Return the Informal Tag for the supplied unique identifier (guid).
     *
     * @param serverName name of the server instances for this request.
     * @param userId userId of the user making the request.
     * @param guid unique identifier of the meaning.
     *
     * @return tag object or
     * InvalidParameterException the userId is null or invalid or
     * PropertyServerException there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @RequestMapping(method = RequestMethod.GET, path = "/tags/{guid}")

    public TagResponse getTag(@PathVariable String   serverName,
                              @PathVariable String   userId,
                              @PathVariable String   guid)
    {
        return restAPI.getTag(serverName, userId, guid);
    }


    /**
     * Return the tags matching the supplied name.
     *
     * @param serverName name of the server instances for this request.
     * @param userId the name of the calling user.
     * @param tagName name of tag.  This may include wild card characters.
     * @param startFrom  index of the list ot start from (0 for start).
     * @param pageSize   maximum number of elements to return.
     * @return list of tag objects or
     * InvalidParameterException - one of the parameters is invalid or
     * PropertyServerException - there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    @RequestMapping(method = RequestMethod.GET, path = "/tags/by-name/{tagName}")

    public TagListResponse getTagsByName(@PathVariable String  serverName,
                                         @PathVariable String  userId,
                                         @PathVariable String  tagName,
                                         @RequestParam int     startFrom,
                                         @RequestParam int     pageSize)
    {
        return restAPI.getTagsByName(serverName, userId, tagName, startFrom, pageSize);
    }


    /**
     * Removes a comment added to the asset by this user.
     *
     * @param serverName name of the server instances for this request
     * @param userId       String - userId of user making request.
     * @param guid         String - unique id for the comment object
     * @param requestBody  containing type of comment enum and the text of the comment.
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid.
     * PropertyServerException There is a problem updating the asset properties in the metadata repository.
     * UserNotAuthorizedException the user does not have permission to perform this request.
     */
    @RequestMapping(method = RequestMethod.POST, path = "/comments/{guid}/delete")

    public VoidResponse removeCommentFromAsset(@PathVariable String          serverName,
                                               @PathVariable String          userId,
                                               @PathVariable String          guid,
                                               @RequestBody  NullRequestBody requestBody)
    {
        return restAPI.removeCommentFromAsset(serverName, userId, guid, requestBody);
    }


    /**
     * Removes a "Like" added to the asset by this user.
     *
     * @param serverName   name of the server instances for this request.
     * @param userId       String - userId of user making request.
     * @param guid         String - unique id for the like object.
     * @param requestBody  containing type of comment enum and the text of the comment.
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid.
     * PropertyServerException There is a problem updating the asset properties in the metadata repository.
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @RequestMapping(method = RequestMethod.POST, path = "/likes/{guid}/delete")

    public VoidResponse   removeLikeFromAsset(@PathVariable String          serverName,
                                              @PathVariable String          userId,
                                              @PathVariable String          guid,
                                              @RequestBody  NullRequestBody requestBody)
    {
        return restAPI.removeLikeFromAsset(serverName, userId, guid, requestBody);
    }


    /**
     * Removes of a star rating that was added to the asset by this user.
     *
     * @param serverName   name of the server instances for this request.
     * @param userId       String - userId of user making request.
     * @param guid         unique id for the review.
     * @param requestBody  containing type of comment enum and the text of the comment.
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid.
     * PropertyServerException There is a problem updating the asset properties in the metadata repository.
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @RequestMapping(method = RequestMethod.POST, path = "/reviews/{guid}/delete")

    public VoidResponse   removeReviewFromAsset(@PathVariable String          serverName,
                                                @PathVariable String          userId,
                                                @PathVariable String          guid,
                                                @RequestBody  NullRequestBody requestBody)
    {
        return restAPI.removeReviewFromAsset(serverName, userId, guid, requestBody);
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
    @RequestMapping(method = RequestMethod.POST, path = "/assets/{assetGUID}/tags/{tagGUID}/delete")

    public VoidResponse   removeTagFromAsset(@PathVariable String          serverName,
                                             @PathVariable String          userId,
                                             @PathVariable String          assetGUID,
                                             @PathVariable String          tagGUID,
                                             @RequestBody  NullRequestBody requestBody)
    {
        return restAPI.removeTagFromAsset(serverName, userId, assetGUID, tagGUID, requestBody);
    }


    /**
     * Update an existing comment.
     *
     * @param serverName   name of the server instances for this request.
     * @param userId       userId of user making request.
     * @param commentGUID  unique identifier for the comment to change.
     * @param requestBody  containing type of comment enum and the text of the comment.
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid.
     * PropertyServerException There is a problem updating the asset properties in the metadata repository.
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @RequestMapping(method = RequestMethod.POST, path = "/comments/{commentGUID}/update")

    public VoidResponse   updateComment(@PathVariable String              serverName,
                                        @PathVariable String              userId,
                                        @PathVariable String              commentGUID,
                                        @RequestBody  CommentRequestBody  requestBody)
    {
        return restAPI.updateComment(serverName, userId, commentGUID, requestBody);
    }


    /**
     * Updates the rating and optional review text attached to the asset by this user.
     *
     * @param serverName  name of the server instances for this request.
     * @param userId      userId of user making request.
     * @param reviewGUID  unique identifier for the review.
     * @param requestBody provides the StarRating enumeration for none, one to five stars plus
     *                    optional review test.
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid.
     * PropertyServerException There is a problem updating the asset properties in the metadata repository.
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @RequestMapping(method = RequestMethod.POST, path = "/reviews/{reviewGUID}/update")

    public VoidResponse   updateReviewOnAsset(@PathVariable String              serverName,
                                              @PathVariable String              userId,
                                              @PathVariable String              reviewGUID,
                                              @RequestBody  ReviewRequestBody   requestBody)
    {
        return restAPI.updateReviewOnAsset(serverName, userId, reviewGUID, requestBody);
    }


    /**
     * Updates the description of an existing tag (either private of public).
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
    @RequestMapping(method = RequestMethod.POST, path = "/tags/{tagGUID}/update")

    public VoidResponse   updateTagDescription(@PathVariable String         serverName,
                                               @PathVariable String         userId,
                                               @PathVariable String         tagGUID,
                                               @RequestBody  TagRequestBody requestBody)
    {
        return restAPI.updateTagDescription(serverName, userId, tagGUID, requestBody);
    }
}
