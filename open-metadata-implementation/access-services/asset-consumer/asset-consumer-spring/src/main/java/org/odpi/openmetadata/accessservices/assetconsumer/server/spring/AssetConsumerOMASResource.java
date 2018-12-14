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
     * Returns the connection object corresponding to the supplied connection name.
     *
     * @param serverName name of the server instances for this request
     * @param userId userId of user making request.
     * @param name   this may be the qualifiedName or displayName of the connection.
     *
     * @return ConnectionResponse or
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
     * Returns the connection object corresponding to the supplied connection GUID.
     *
     * @param serverName name of the server instances for this request.
     * @param userId userId of user making request.
     * @param guid  the unique id for the connection within the property server.
     *
     * @return ConnectionResponse or
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
     * Returns the unique identifier for the asset connected to the connection.
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

    public GUIDResponse getAssetForConnection(@PathVariable String   serverName,
                                              @PathVariable String   userId,
                                              @PathVariable String   connectionGUID)
    {
        return restAPI.getAssetForConnection(serverName, userId, connectionGUID);
    }


    /**
     * Return the full definition (meaning) of a term using the unique identifier of the glossary term.
     *
     * @param serverName name of the server instances for this request.
     * @param userId userId of the user making the request.
     * @param guid unique identifier of the meaning.
     *
     * @return meaning response object or
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
     * Return the full definition (meaning) of the terms matching the supplied name.
     *
     * @param serverName name of the server instances for this request.
     * @param userId the name of the calling user.
     * @param term name of term.  This may include wild card characters.
     * @param startFrom  index of the list ot start from (0 for start).
     * @param pageSize   maximum number of elements to return.
     * @return meaning list response or
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
     * Creates an Audit log record for the asset.  This log record is stored in the Asset's Audit Log.
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
     * @return VoidResponse or
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
     * Adds a new public tag to the asset's properties.
     *
     * @param serverName   name of the server instances for this request.
     * @param userId       String - userId of user making request.
     * @param guid         String - unique id for the asset.
     * @param requestBody  contains the name of the tag and (optional) description of the tag.
     *
     * @return GUIDResponse or
     * InvalidParameterException one of the parameters is null or invalid or
     * PropertyServerException There is a problem adding the asset properties to the metadata repository or
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @RequestMapping(method = RequestMethod.POST, path = "/assets/{guid}/tags")

    public GUIDResponse addTagToAsset(@PathVariable String          serverName,
                                      @PathVariable String          userId,
                                      @PathVariable String          guid,
                                      @RequestBody  TagRequestBody  requestBody)
    {
        return restAPI.addTagToAsset(serverName, userId, guid, requestBody);
    }


    /**
     * Adds a new private tag to the asset's properties.
     *
     * @param serverName   name of the server instances for this request.
     * @param userId       String - userId of user making request.
     * @param guid         String - unique id for the asset.
     * @param requestBody  contains the name of the tag and (optional) description of the tag.
     *
     * @return GUIDResponse or
     * InvalidParameterException one of the parameters is null or invalid or
     * PropertyServerException There is a problem adding the asset properties to the metadata repository or
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @RequestMapping(method = RequestMethod.POST, path = "/assets/{guid}/tags/private")

    public GUIDResponse addPrivateTagToAsset(@PathVariable String          serverName,
                                             @PathVariable String          userId,
                                             @PathVariable String          guid,
                                             @RequestBody  TagRequestBody  requestBody)
    {
        return restAPI.addPrivateTagToAsset(serverName, userId, guid, requestBody);
    }


    /**
     * Adds a rating to the asset.
     *
     * @param serverName  name of the server instances for this request.
     * @param userId      String - userId of user making request.
     * @param guid        String - unique id for the asset.
     * @param requestBody containing the StarRating and user review of asset.
     *
     * @return GUIDResponse or
     * InvalidParameterException one of the parameters is null or invalid or
     * PropertyServerException There is a problem adding the asset properties to the metadata repository or
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @RequestMapping(method = RequestMethod.POST, path = "/assets/{guid}/ratings")

    public GUIDResponse addRatingToAsset(@PathVariable String             serverName,
                                         @PathVariable String             userId,
                                         @PathVariable String             guid,
                                         @RequestBody  RatingRequestBody  requestBody)
    {
        return restAPI.addRatingToAsset(serverName, userId, guid, requestBody);
    }


    /**
     * Adds a "Like" to the asset.
     *
     * @param serverName  name of the server instances for this request.
     * @param userId      String - userId of user making request.
     * @param guid        String - unique id for the asset.
     * @param requestBody null request body to satisfy HTTP protocol.
     *
     * @return GUIDResponse or
     * InvalidParameterException one of the parameters is null or invalid or
     * PropertyServerException There is a problem adding the asset properties to
     *                                   the metadata repository or
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @RequestMapping(method = RequestMethod.POST, path = "/assets/{guid}/likes/")

    public GUIDResponse addLikeToAsset(@PathVariable String          serverName,
                                       @PathVariable String          userId,
                                       @PathVariable String          guid,
                                       @RequestBody  NullRequestBody requestBody)
    {
        return restAPI.addLikeToAsset(serverName, userId, guid, requestBody);
    }


    /**
     * Adds a comment to the asset.
     *
     * @param serverName name of the server instances for this request.
     * @param userId      String - userId of user making request.
     * @param guid        String - unique id for the asset.
     * @param requestBody containing type of comment enum and the text of the comment.
     *
     * @return GUIDResponse or
     * InvalidParameterException one of the parameters is null or invalid or
     * PropertyServerException There is a problem adding the asset properties to
     *                                   the metadata repository or
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @RequestMapping(method = RequestMethod.POST, path = "/assets/{guid}/comments/")

    public GUIDResponse addCommentToAsset(@PathVariable String              serverName,
                                          @PathVariable String              userId,
                                          @PathVariable String              guid,
                                          @RequestBody  CommentRequestBody  requestBody)
    {
        return restAPI.addCommentToAsset(serverName, userId, guid, requestBody);
    }


    /**
     * Adds a reply to a comment.
     *
     * @param serverName    name of the server instances for this request.
     * @param userId        String - userId of user making request.
     * @param commentGUID   String - unique id for an existing comment.  Used to add a reply to a comment.
     * @param requestBody   containing type of comment enum and the text of the comment.
     *
     * @return GUIDResponse or
     * InvalidParameterException one of the parameters is null or invalid or
     * PropertyServerException There is a problem adding the asset properties to
     *                                   the metadata repository or
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @RequestMapping(method = RequestMethod.POST, path = "/comments/{commentGUID}/reply")

    public GUIDResponse addCommentReply(@PathVariable String             serverName,
                                        @PathVariable String             userId,
                                        @PathVariable String             commentGUID,
                                        @RequestBody  CommentRequestBody requestBody)
    {
        return restAPI.addCommentReply(serverName, userId, commentGUID, requestBody);
    }


    /**
     * Removes a tag from the asset that was added by this user.
     *
     * @param serverName   name of the server instances for this request
     * @param userId       String - userId of user making request.
     * @param guid         String - unique id for the tag.
     * @param requestBody  containing type of comment enum and the text of the comment.
     *
     * @return VoidResponse or
     * InvalidParameterException - one of the parameters is null or invalid or
     * PropertyServerException - there is a problem updating the asset properties in the metadata repository or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    @RequestMapping(method = RequestMethod.POST, path = "/tags/{guid}/delete")

    public VoidResponse   removeTag(@PathVariable String          serverName,
                                    @PathVariable String          userId,
                                    @PathVariable String          guid,
                                    @RequestBody  NullRequestBody requestBody)
    {
        return restAPI.removeTag(serverName, userId, guid, requestBody);
    }


    /**
     * Removes a tag from the asset that was added by this user.
     *
     * @param serverName   name of the server instances for this request.
     * @param userId       String - userId of user making request.
     * @param guid         String - unique id for the tag.
     * @param requestBody  containing type of comment enum and the text of the comment.
     *
     * @return VoidResponse or
     * InvalidParameterException one of the parameters is null or invalid.
     * PropertyServerException There is a problem updating the asset properties in the metadata repository.
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @RequestMapping(method = RequestMethod.POST, path = "/tags/private/{guid}/delete")

    public VoidResponse   removePrivateTag(@PathVariable String          serverName,
                                           @PathVariable String          userId,
                                           @PathVariable String          guid,
                                           @RequestBody  NullRequestBody requestBody)
    {
        return restAPI.removePrivateTag(serverName, userId, guid, requestBody);
    }


    /**
     * Removes of a star rating that was added to the asset by this user.
     *
     * @param serverName   name of the server instances for this request.
     * @param userId       String - userId of user making request.
     * @param guid         String - unique id for the rating object.
     * @param requestBody  containing type of comment enum and the text of the comment.
     *
     * @return VoidResponse or
     * InvalidParameterException one of the parameters is null or invalid.
     * PropertyServerException There is a problem updating the asset properties in the metadata repository.
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @RequestMapping(method = RequestMethod.POST, path = "/ratings/{guid}/delete")

    public VoidResponse   removeRating(@PathVariable String          serverName,
                                       @PathVariable String          userId,
                                       @PathVariable String          guid,
                                       @RequestBody  NullRequestBody requestBody)
    {
        return restAPI.removeRating(serverName, userId, guid, requestBody);
    }


    /**
     * Removes a "Like" added to the asset by this user.
     *
     * @param serverName   name of the server instances for this request.
     * @param userId       String - userId of user making request.
     * @param guid         String - unique id for the like object.
     * @param requestBody  containing type of comment enum and the text of the comment.
     *
     * @return VoidResponse or
     * InvalidParameterException one of the parameters is null or invalid.
     * PropertyServerException There is a problem updating the asset properties in the metadata repository.
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @RequestMapping(method = RequestMethod.POST, path = "/likes/{guid}/delete")

    public VoidResponse   removeLike(@PathVariable String          serverName,
                                     @PathVariable String          userId,
                                     @PathVariable String          guid,
                                     @RequestBody  NullRequestBody requestBody)
    {
        return restAPI.removeLike(serverName, userId, guid, requestBody);
    }


    /**
     * Removes a comment added to the asset by this user.
     *
     * @param serverName name of the server instances for this request
     * @param userId       String - userId of user making request.
     * @param guid         String - unique id for the comment object
     * @param requestBody  containing type of comment enum and the text of the comment.
     *
     * @return VoidResponse or
     * InvalidParameterException one of the parameters is null or invalid.
     * PropertyServerException There is a problem updating the asset properties in the metadata repository.
     * UserNotAuthorizedException the user does not have permission to perform this request.
     */
    @RequestMapping(method = RequestMethod.POST, path = "/comments/{guid}/delete")

    public VoidResponse   removeComment(@PathVariable String          serverName,
                                        @PathVariable String          userId,
                                        @PathVariable String          guid,
                                        @RequestBody  NullRequestBody requestBody)
    {
        return restAPI.removeComment(serverName, userId, guid, requestBody);
    }
}
