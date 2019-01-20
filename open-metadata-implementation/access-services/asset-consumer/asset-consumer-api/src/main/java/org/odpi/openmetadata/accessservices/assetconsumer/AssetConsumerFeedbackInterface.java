/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetconsumer;

import org.odpi.openmetadata.accessservices.assetconsumer.ffdc.exceptions.*;
import org.odpi.openmetadata.accessservices.assetconsumer.properties.GlossaryTerm;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectionCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.properties.AssetUniverse;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.CommentType;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.StarRating;

import java.util.List;

/**
 * AssetConsumerFeedbackInterface supports the ability to add and remove feedback for an asset.
 * This feedback may be in the form of reviews, likes and comments.
 */
public interface AssetConsumerFeedbackInterface
{
    /**
     * Adds a star rating and optional review text to the asset.
     *
     * @param userId      userId of user making request.
     * @param assetGUID   unique identifier for the asset.
     * @param starRating  StarRating enumeration for not recommended, one to five stars.
     * @param review      user review of asset.  This can be null.
     *
     * @return guid of new review object.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the asset properties to the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    String addReviewToAsset(String     userId,
                            String     assetGUID,
                            StarRating starRating,
                            String     review) throws InvalidParameterException,
                                                      PropertyServerException,
                                                      UserNotAuthorizedException;


    /**
     * Updates the rating and optional review text attached to the asset by this user.
     *
     * @param userId      userId of user making request.
     * @param reviewGUID  unique identifier for the review.
     * @param starRating  StarRating enumeration for none, one to five stars.
     * @param review      user review of asset.  This can be null.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the asset properties to the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    String updateReviewOnAsset(String     userId,
                               String     reviewGUID,
                               StarRating starRating,
                               String     review) throws InvalidParameterException,
                                                         PropertyServerException,
                                                         UserNotAuthorizedException;



    /**
     * Removes of a review that was added to the asset by this user.
     *
     * @param userId      userId of user making request.
     * @param reviewGUID  unique identifier for the review object.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem updating the asset properties in the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    void   removeReviewFromAsset(String     userId,
                                 String     reviewGUID) throws InvalidParameterException,
                                                               PropertyServerException,
                                                               UserNotAuthorizedException;


    /**
     * Adds a "Like" to the asset.
     *
     * @param userId      userId of user making request.
     * @param assetGUID   unique identifier for the asset
     *
     * @return guid of new like object.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the asset properties to the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    String addLikeToAsset(String       userId,
                          String       assetGUID) throws InvalidParameterException,
                                                         PropertyServerException,
                                                         UserNotAuthorizedException;


    /**
     * Removes a "Like" added to the asset by this user.
     *
     * @param userId   userId of user making request.
     * @param likeGUID unique identifier for the like object.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem updating the asset properties in the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    void   removeLikeFromAsset(String     userId,
                               String     likeGUID) throws InvalidParameterException,
                                                           PropertyServerException,
                                                           UserNotAuthorizedException;


    /**
     * Adds a comment to the asset.
     *
     * @param userId        userId of user making request.
     * @param assetGUID     unique identifier for the asset.
     * @param commentType   type of comment enum.
     * @param commentText   the text of the comment.
     *
     * @return guid of new comment.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the asset properties to the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    String addCommentToAsset(String      userId,
                             String      assetGUID,
                             CommentType commentType,
                             String      commentText) throws InvalidParameterException,
                                                             PropertyServerException,
                                                             UserNotAuthorizedException;


    /**
     * Adds a comment to another comment.
     *
     * @param userId        userId of user making request.
     * @param commentGUID   unique identifier for an existing comment.  Used to add a reply to a comment.
     * @param commentType   type of comment enum.
     * @param commentText   the text of the comment.
     *
     * @return guid of new comment.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the asset properties to the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    String addCommentReply(String      userId,
                           String      commentGUID,
                           CommentType commentType,
                           String      commentText) throws InvalidParameterException,
                                                           PropertyServerException,
                                                           UserNotAuthorizedException;


    /**
     * Update an existing comment.
     *
     * @param userId        userId of user making request.
     * @param commentGUID   unique identifier for the comment to change.
     * @param commentType   type of comment enum.
     * @param commentText   the text of the comment.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the asset properties to the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    void   updateComment(String      userId,
                         String      commentGUID,
                         CommentType commentType,
                         String      commentText) throws InvalidParameterException,
                                                         PropertyServerException,
                                                         UserNotAuthorizedException;


    /**
     * Removes a comment added to the asset by this user.
     *
     * @param userId       userId of user making request.
     * @param commentGUID  unique identifier for the comment object.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem updating the asset properties in the property server.
     * @throws UserNotAuthorizedException the user does not have permission to perform this request.
     */
    void   removeCommentFromAsset(String     userId,
                                  String     commentGUID) throws InvalidParameterException,
                                                                 PropertyServerException,
                                                                 UserNotAuthorizedException;
}
