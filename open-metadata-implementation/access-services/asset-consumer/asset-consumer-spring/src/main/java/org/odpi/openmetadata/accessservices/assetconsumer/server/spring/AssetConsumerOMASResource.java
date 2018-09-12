/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the Egeria project. */
package org.odpi.openmetadata.accessservices.assetconsumer.server.spring;

import org.odpi.openmetadata.accessservices.assetconsumer.rest.*;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.CommentType;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.StarRating;
import org.odpi.openmetadata.accessservices.assetconsumer.server.AssetConsumerRESTServices;
import org.springframework.web.bind.annotation.*;


/**
 * The AssetConsumerRESTServices provides the server-side implementation of the AssetConsumer Open Metadata
 * Assess Service (OMAS).  This interface provides connections to assets and APIs for adding feedback
 * on the asset.
 */
@RestController
@RequestMapping("/open-metadata/access-services/asset-consumer/users/{userId}")
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

    public ConnectionResponse getConnectionByName(@PathVariable String   userId,
                                                  @PathVariable String   name)
    {
        return restAPI.getConnectionByName(userId, name);
    }


    /**
     * Returns the connection object corresponding to the supplied connection GUID.
     *
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

    public ConnectionResponse getConnectionByGUID(@PathVariable String     userId,
                                                  @PathVariable String     guid)
    {
        return restAPI.getConnectionByGUID(userId, guid);
    }


    /**
     * Creates an Audit log record for the asset.  This log record is stored in the Asset's Audit Log.
     *
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

    public VoidResponse addLogMessageToAsset(@PathVariable String                userId,
                                             @PathVariable String                guid,
                                             @RequestBody  LogRecordRequestBody  requestBody)
    {
        return restAPI.addLogMessageToAsset(userId, guid, requestBody);
    }


    /**
     * Adds a new public tag to the asset's properties.
     *
     * @param userId       String - userId of user making request.
     * @param guid         String - unique id for the asset.
     * @param requestBody  contains the name of the tag and (optional) description of the tag.
     *
     * @return GUIDResponse or
     * InvalidParameterException one of the parameters is null or invalid or
     * PropertyServerException There is a problem adding the asset properties to
     *                                   the metadata repository or
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @RequestMapping(method = RequestMethod.POST, path = "/assets/{guid}/tags")

    public GUIDResponse addTagToAsset(@PathVariable String          userId,
                                      @PathVariable String          guid,
                                      @RequestBody  TagRequestBody  requestBody)
    {
        return restAPI.addTagToAsset(userId, guid, requestBody);
    }


    /**
     * Adds a new private tag to the asset's properties.
     *
     * @param userId       String - userId of user making request.
     * @param guid         String - unique id for the asset.
     * @param requestBody  contains the name of the tag and (optional) description of the tag.
     *
     * @return GUIDResponse or
     * InvalidParameterException one of the parameters is null or invalid or
     * PropertyServerException There is a problem adding the asset properties to
     *                                   the metadata repository or
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @RequestMapping(method = RequestMethod.POST, path = "/assets/{guid}/tags/private")

    public GUIDResponse addPrivateTagToAsset(@PathVariable String          userId,
                                             @PathVariable String          guid,
                                             @RequestBody  TagRequestBody  requestBody)
    {
        return restAPI.addPrivateTagToAsset(userId, guid, requestBody);
    }


    /**
     * Adds a rating to the asset.
     *
     * @param userId      String - userId of user making request.
     * @param guid        String - unique id for the asset.
     * @param requestBody containing the StarRating and user review of asset.
     *
     * @return GUIDResponse or
     * InvalidParameterException one of the parameters is null or invalid or
     * PropertyServerException There is a problem adding the asset properties to
     *                                   the metadata repository or
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @RequestMapping(method = RequestMethod.POST, path = "/assets/{guid}/ratings")

    public GUIDResponse addRatingToAsset(@PathVariable String             userId,
                                         @PathVariable String             guid,
                                         @RequestBody  RatingRequestBody  requestBody)
    {
        return restAPI.addRatingToAsset(userId, guid, requestBody);
    }


    /**
     * Adds a "Like" to the asset.
     *
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

    public GUIDResponse addLikeToAsset(@PathVariable String          userId,
                                       @PathVariable String          guid,
                                       @RequestBody  NullRequestBody requestBody)
    {
        return restAPI.addLikeToAsset(userId, guid, requestBody);
    }


    /**
     * Adds a comment to the asset.
     *
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

    public GUIDResponse addCommentToAsset(@PathVariable String              userId,
                                          @PathVariable String              guid,
                                          @RequestBody  CommentRequestBody  requestBody)
    {
        return restAPI.addCommentToAsset(userId, guid, requestBody);
    }


    /**
     * Adds a reply to a comment.
     *
     * @param userId        String - userId of user making request.
     * @param commentGUID   String - unique id for an existing comment.  Used to add a reply to a comment.
     * @param requestBody containing type of comment enum and the text of the comment.
     *
     * @return GUIDResponse or
     * InvalidParameterException one of the parameters is null or invalid or
     * PropertyServerException There is a problem adding the asset properties to
     *                                   the metadata repository or
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @RequestMapping(method = RequestMethod.POST, path = "/comments/{commentGUID}/reply")

    public GUIDResponse addCommentReply(@PathVariable String             userId,
                                        @PathVariable String             commentGUID,
                                        @RequestBody  CommentRequestBody requestBody)
    {
        return restAPI.addCommentReply(userId, commentGUID, requestBody);
    }


    /**
     * Removes a tag from the asset that was added by this user.
     *
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

    public VoidResponse   removeTag(@PathVariable String          userId,
                                    @PathVariable String          guid,
                                    @RequestBody  NullRequestBody requestBody)
    {
        return restAPI.removeTag(userId, guid, requestBody);
    }


    /**
     * Removes a tag from the asset that was added by this user.
     *
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

    public VoidResponse   removePrivateTag(@PathVariable String          userId,
                                           @PathVariable String          guid,
                                           @RequestBody  NullRequestBody requestBody)
    {
        return restAPI.removePrivateTag(userId, guid, requestBody);
    }


    /**
     * Removes of a star rating that was added to the asset by this user.
     *
     * @param userId       String - userId of user making request.
     * @param guid         String - unique id for the rating object
     * @param requestBody  containing type of comment enum and the text of the comment.
     *
     * @return VoidResponse or
     * InvalidParameterException one of the parameters is null or invalid.
     * PropertyServerException There is a problem updating the asset properties in the metadata repository.
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @RequestMapping(method = RequestMethod.POST, path = "/ratings/{guid}/delete")

    public VoidResponse   removeRating(@PathVariable String          userId,
                                       @PathVariable String          guid,
                                       @RequestBody  NullRequestBody requestBody)
    {
        return restAPI.removeRating(userId, guid, requestBody);
    }


    /**
     * Removes a "Like" added to the asset by this user.
     *
     * @param userId       String - userId of user making request.
     * @param guid         String - unique id for the like object
     * @param requestBody  containing type of comment enum and the text of the comment.
     *
     * @return VoidResponse or
     * InvalidParameterException one of the parameters is null or invalid.
     * PropertyServerException There is a problem updating the asset properties in the metadata repository.
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @RequestMapping(method = RequestMethod.POST, path = "/likes/{guid}/delete")

    public VoidResponse   removeLike(@PathVariable String          userId,
                                     @PathVariable String          guid,
                                     @RequestBody  NullRequestBody requestBody)
    {
        return restAPI.removeLike(userId, guid, requestBody);
    }


    /**
     * Removes a comment added to the asset by this user.
     *
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

    public VoidResponse   removeComment(@PathVariable String          userId,
                                        @PathVariable String          guid,
                                        @RequestBody  NullRequestBody requestBody)
    {
        return restAPI.removeComment(userId, guid, requestBody);
    }
}
