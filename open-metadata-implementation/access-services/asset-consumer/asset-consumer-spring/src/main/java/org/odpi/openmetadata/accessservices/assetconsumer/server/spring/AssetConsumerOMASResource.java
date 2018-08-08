/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.assetconsumer.server.spring;

import org.odpi.openmetadata.accessservices.assetconsumer.responses.ConnectionResponse;
import org.odpi.openmetadata.accessservices.assetconsumer.responses.VoidResponse;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.CommentType;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.StarRating;
import org.odpi.openmetadata.accessservices.assetconsumer.server.AssetConsumerRESTServices;
import org.odpi.openmetadata.accessservices.assetconsumer.responses.GUIDResponse;
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
     * @param userId - String - userId of user making request.
     * @param name - this may be the qualifiedName or displayName of the connection.
     *
     * @return ConnectionResponse or
     * InvalidParameterException one of the parameters is null or invalid.
     * UnrecognizedConnectionNameException there is no connection defined for this name.
     * AmbiguousConnectionNameException there is more than one connection defined for this name.
     * PropertyServerException there is a problem retrieving information from the property (metadata) server.
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
     * @param userId - String - userId of user making request.
     * @param guid - the unique id for the connection within the property server.
     *
     * @return ConnectionResponse or
     * InvalidParameterException one of the parameters is null or invalid.
     * UnrecognizedConnectionGUIDException the supplied GUID is not recognized by the metadata repository.
     * PropertyServerException there is a problem retrieving information from the property (metadata) server.
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
     * @param userId - String - userId of user making request.
     * @param guid - String - unique id for the asset.
     * @param connectorInstanceId - String - (optional) id of connector in use (if any).
     * @param connectionName - String - (optional) name of the connection (extracted from the connector).
     * @param connectorType - String - (optional) type of connector in use (if any).
     * @param contextId - String - (optional) function name, or processId of the activity that the caller is performing.
     * @param message - log record content.
     *
     * @return VoidResponse or
     * InvalidParameterException one of the parameters is null or invalid.
     * PropertyServerException There is a problem adding the asset properties to
     *                                   the metadata repository.
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @RequestMapping(method = RequestMethod.POST, path = "/assets/{guid}/log-record")

    public VoidResponse addLogMessageToAsset(@PathVariable                   String      userId,
                                             @PathVariable                   String      guid,
                                             @RequestParam(required = false) String      connectorInstanceId,
                                             @RequestParam(required = false) String      connectionName,
                                             @RequestParam(required = false) String      connectorType,
                                             @RequestParam(required = false) String      contextId,
                                             @RequestParam(required = false) String      message)
    {
        return restAPI.addLogMessageToAsset(userId,
                                            guid,
                                            connectorInstanceId,
                                            connectionName,
                                            connectorType,
                                            contextId,
                                            message);
    }


    /**
     * Adds a new public tag to the asset's properties.
     *
     * @param userId - String - userId of user making request.
     * @param guid - String - unique id for the asset.
     * @param tagName - String - name of the tag.
     * @param tagDescription - String - (optional) description of the tag.  Setting a description, particularly in
     *                       a public tag makes the tag more valuable to other users and can act as an embryonic
     *                       glossary term.
     *
     * @return GUIDResponse or
     * InvalidParameterException one of the parameters is null or invalid.
     * PropertyServerException There is a problem adding the asset properties to
     *                                   the metadata repository.
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @RequestMapping(method = RequestMethod.POST, path = "/assets/{guid}/tags")

    public GUIDResponse addTagToAsset(@PathVariable                   String      userId,
                                      @PathVariable                   String      guid,
                                      @RequestParam                   String      tagName,
                                      @RequestParam(required = false) String      tagDescription)
    {
        return restAPI.addTagToAsset(userId, guid, tagName, tagDescription);
    }


    /**
     * Adds a new private tag to the asset's properties.
     *
     * @param userId - String - userId of user making request.
     * @param guid - String - unique id for the asset.
     * @param tagName - String - name of the tag.
     * @param tagDescription - String - (optional) description of the tag.  Setting a description, particularly in
     *                       a public tag makes the tag more valuable to other users and can act as an embryonic
     *                       glossary term.
     *
     * @return GUIDResponse or
     * InvalidParameterException one of the parameters is null or invalid.
     * PropertyServerException There is a problem adding the asset properties to
     *                                   the metadata repository.
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @RequestMapping(method = RequestMethod.POST, path = "/assets/{guid}/tags/private")

    public GUIDResponse addPrivateTagToAsset(@PathVariable                   String      userId,
                                             @PathVariable                   String      guid,
                                             @RequestParam                   String      tagName,
                                             @RequestParam(required = false) String      tagDescription)
    {
        return restAPI.addPrivateTagToAsset(userId, guid, tagName, tagDescription);
    }


    /**
     * Adds a rating to the asset.
     *
     * @param userId - String - userId of user making request.
     * @param guid - String - unique id for the asset.
     * @param starRating - StarRating  - enumeration for none, one to five stars.
     * @param review - String - user review of asset.
     *
     * @return GUIDResponse or
     * InvalidParameterException one of the parameters is null or invalid.
     * PropertyServerException There is a problem adding the asset properties to
     *                                   the metadata repository.
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @RequestMapping(method = RequestMethod.POST, path = "/assets/{guid}/ratings/")

    public GUIDResponse addRatingToAsset(@PathVariable String     userId,
                                         @PathVariable String     guid,
                                         @RequestParam StarRating starRating,
                                         @RequestParam String     review)
    {
        return restAPI.addRatingToAsset(userId, guid, starRating, review);
    }


    /**
     * Adds a "Like" to the asset.
     *
     * @param userId - String - userId of user making request.
     * @param guid - String - unique id for the asset
     *
     * @return GUIDResponse or
     * InvalidParameterException one of the parameters is null or invalid.
     * PropertyServerException There is a problem adding the asset properties to
     *                                   the metadata repository.
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @RequestMapping(method = RequestMethod.POST, path = "/assets/{guid}/likes/")

    public GUIDResponse addLikeToAsset(@PathVariable String       userId,
                                       @PathVariable String       guid)
    {
        return restAPI.addLikeToAsset(userId, guid);
    }


    /**
     * Adds a comment to the asset.
     *
     * @param userId - String - userId of user making request.
     * @param guid - String - unique id for the asset.
     * @param commentType - type of comment enum.
     * @param commentText - String - the text of the comment.
     *
     * @return GUIDResponse or
     * InvalidParameterException one of the parameters is null or invalid.
     * PropertyServerException There is a problem adding the asset properties to
     *                                   the metadata repository.
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @RequestMapping(method = RequestMethod.POST, path = "/assets/{guid}/comments/")

    public GUIDResponse addCommentToAsset(@PathVariable String      userId,
                                          @PathVariable String      guid,
                                          @RequestParam CommentType commentType,
                                          @RequestParam String      commentText)
    {
        return restAPI.addCommentToAsset(userId, guid, commentType, commentText);
    }


    /**
     * Adds a comment to the asset.
     *
     * @param userId - String - userId of user making request.
     * @param commentGUID - String - unique id for an existing comment.  Used to add a reply to a comment.
     * @param commentType - type of comment enum.
     * @param commentText - String - the text of the comment.
     *
     * @return GUIDResponse or
     * InvalidParameterException one of the parameters is null or invalid.
     * PropertyServerException There is a problem adding the asset properties to
     *                                   the metadata repository.
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @RequestMapping(method = RequestMethod.POST, path = "/comments/{commentGUID}/reply")

    public GUIDResponse addCommentReply(@PathVariable String      userId,
                                        @PathVariable String      commentGUID,
                                        @RequestParam CommentType commentType,
                                        @RequestParam String      commentText)
    {
        return restAPI.addCommentReply(userId, commentGUID, commentType, commentText);
    }


    /**
     * Removes a tag from the asset that was added by this user.
     *
     * @param userId - String - userId of user making request.
     * @param guid - String - unique id for the tag.
     *
     * @return VoidResponse or
     * InvalidParameterException one of the parameters is null or invalid.
     * PropertyServerException There is a problem updating the asset properties in
     *                                   the metadata repository.
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @RequestMapping(method = RequestMethod.PATCH, path = "/tags/{guid}/delete")

    public VoidResponse   removeTag(@PathVariable String     userId,
                                    @PathVariable String     guid)
    {
        return restAPI.removeTag(userId, guid);
    }


    /**
     * Removes a tag from the asset that was added by this user.
     *
     * @param userId - String - userId of user making request.
     * @param guid - String - unique id for the tag.
     *
     * @return VoidResponse or
     * InvalidParameterException one of the parameters is null or invalid.
     * PropertyServerException There is a problem updating the asset properties in
     *                                   the metadata repository.
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @RequestMapping(method = RequestMethod.PATCH, path = "/tags/private/{guid}/delete")

    public VoidResponse   removePrivateTag(@PathVariable String     userId,
                                           @PathVariable String     guid)
    {
        return restAPI.removePrivateTag(userId, guid);
    }


    /**
     * Removes of a star rating that was added to the asset by this user.
     *
     * @param userId - String - userId of user making request.
     * @param guid - String - unique id for the rating object
     *
     * @return VoidResponse or
     * InvalidParameterException one of the parameters is null or invalid.
     * PropertyServerException There is a problem updating the asset properties in
     *                                   the metadata repository.
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @RequestMapping(method = RequestMethod.PATCH, path = "/ratings/{guid}/delete")

    public VoidResponse   removeRating(@PathVariable String     userId,
                                       @PathVariable String     guid)
    {
        return restAPI.removeRating(userId, guid);
    }


    /**
     * Removes a "Like" added to the asset by this user.
     *
     * @param userId - String - userId of user making request.
     * @param guid - String - unique id for the like object
     * @return VoidResponse or
     * InvalidParameterException one of the parameters is null or invalid.
     * PropertyServerException There is a problem updating the asset properties in
     *                                   the metadata repository.
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @RequestMapping(method = RequestMethod.PATCH, path = "/likes/{guid}/delete")

    public VoidResponse   removeLike(@PathVariable String     userId,
                                     @PathVariable String     guid)
    {
        return restAPI.removeLike(userId, guid);
    }


    /**
     * Removes a comment added to the asset by this user.
     *
     * @param userId - String - userId of user making request.
     * @param guid - String - unique id for the comment object
     * @return VoidResponse or
     * InvalidParameterException one of the parameters is null or invalid.
     * PropertyServerException There is a problem updating the asset properties in
     *                                   the metadata repository.
     * UserNotAuthorizedException the user does not have permission to perform this request.
     */
    @RequestMapping(method = RequestMethod.PATCH, path = "/comments/{guid}/delete")

    public VoidResponse   removeComment(@PathVariable String     userId,
                                        @PathVariable String     guid)
    {
        return restAPI.removeComment(userId, guid);
    }
}
