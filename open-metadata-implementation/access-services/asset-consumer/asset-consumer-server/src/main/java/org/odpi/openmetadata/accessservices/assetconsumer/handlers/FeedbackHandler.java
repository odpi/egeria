/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetconsumer.handlers;

import org.odpi.openmetadata.accessservices.assetconsumer.AssetConsumerFeedbackInterface;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.CommentType;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.StarRating;
import org.odpi.openmetadata.accessservices.assetconsumer.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.accessservices.assetconsumer.ffdc.exceptions.PropertyServerException;
import org.odpi.openmetadata.accessservices.assetconsumer.ffdc.exceptions.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.Date;

/**
 * FeedbackHandler manages the creation of asset feedback (likes, ratings, comments and tags) in the
 * property server.
 */
public class FeedbackHandler implements AssetConsumerFeedbackInterface
{
    private static final String likeTypeName                         = "Like";
    private static final String likeTypeGUID                         = "deaa5ca0-47a0-483d-b943-d91c76744e01";
    private static final String attachedLikeTypeGUID                 = "e2509715-a606-415d-a995-61d00503dad4";

    private static final String ratingTypeName                       = "Rating";
    private static final String ratingTypeGUID                       = "7299d721-d17f-4562-8286-bcd451814478";
    private static final String starsPropertyName                    = "stars";
    private static final String reviewPropertyName                   = "review";
    private static final String attachedRatingTypeGUID               = "0aaad9e9-9cc5-4ad8-bc2e-c1099bab6344";

    private static final String commentTypeName                      = "Comment";
    private static final String commentTypeGUID                      = "1a226073-9c84-40e4-a422-fbddb9b84278";
    private static final String qualifiedNamePropertyName            = "qualifiedName";
    private static final String commentPropertyName                  = "comment";
    private static final String commentTypePropertyName              = "commentType";
    private static final String attachedCommentTypeGUID              = "0d90501b-bf29-4621-a207-0c8c953bdac9";

    private static final String assetTypeName                        = "Asset";


    private String               serviceName;

    private String               serverName       = null;
    private OMRSRepositoryHelper repositoryHelper = null;
    private ErrorHandler         errorHandler     = null;
    private RepositoryHandler    basicHandler     = null;


    /**
     * Construct the feedback handler with a link to the property server's connector and this access service's
     * official name.
     *
     * @param serviceName  name of this service
     * @param repositoryConnector  connector to the property server.
     */
    public FeedbackHandler(String                  serviceName,
                           OMRSRepositoryConnector repositoryConnector)
    {
        this.serviceName = serviceName;

        if (repositoryConnector != null)
        {
            this.repositoryHelper = repositoryConnector.getRepositoryHelper();
            this.serverName = repositoryConnector.getServerName();
            errorHandler = new ErrorHandler(repositoryConnector);
            basicHandler = new RepositoryHandler(serviceName, repositoryConnector);
        }
    }


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
    public String addReviewToAsset(String     userId,
                                   String     assetGUID,
                                   StarRating starRating,
                                   String     review) throws InvalidParameterException,
                                                             PropertyServerException,
                                                             UserNotAuthorizedException
    {
        final String methodName = "addReviewToAsset";
        final String guidParameter = "assetGUID";
        final String ratingParameter = "starRating";

        errorHandler.validateUserId(userId, methodName);
        errorHandler.validateGUID(assetGUID, guidParameter, methodName);
        errorHandler.validateEnum(starRating, ratingParameter, methodName);

        OMRSMetadataCollection  metadataCollection = errorHandler.validateRepositoryConnector(methodName);

        basicHandler.validateEntityGUID(userId, assetGUID, assetTypeName, metadataCollection, methodName);

        try
        {
            /*
             * Create the Rating Entity
             */
            InstanceProperties properties;

            properties = this.addStarRatingPropertyToInstance(null,
                                                              starRating,
                                                              methodName);
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      reviewPropertyName,
                                                                      review,
                                                                      methodName);
            EntityDetail feedbackEntity = metadataCollection.addEntity(userId,
                                                                       ratingTypeGUID,
                                                                       properties,
                                                                       null,
                                                                       InstanceStatus.ACTIVE);

            String  feedbackGUID = null;

            if (feedbackEntity != null)
            {
                feedbackGUID = feedbackEntity.getGUID();

                /*
                 * Link the Rating to the asset
                 */
                metadataCollection.addRelationship(userId,
                                                   attachedRatingTypeGUID,
                                                   null,
                                                   assetGUID,
                                                   feedbackGUID,
                                                   InstanceStatus.ACTIVE);
            }

            return feedbackGUID;
        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException error)
        {
            errorHandler.handleUnauthorizedUser(userId,
                                                methodName,
                                                serverName,
                                                serviceName);
        }
        catch (Throwable   error)
        {
            errorHandler.handleRepositoryError(error,
                                               methodName,
                                               serverName,
                                               serviceName);
        }

        return null;
    }


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
    public void updateReviewOnAsset(String     userId,
                                    String     reviewGUID,
                                    StarRating starRating,
                                    String     review) throws InvalidParameterException,
                                                                PropertyServerException,
                                                                UserNotAuthorizedException
    {
        return;
    }



    /**
     * Set up a property value for the StarRating enum property.
     *
     * @param properties  current properties
     * @param starRating  enum value
     * @param methodName  calling method
     * @return  InstanceProperties object with the enum value added
     */
    private InstanceProperties addStarRatingPropertyToInstance(InstanceProperties  properties,
                                                               StarRating          starRating,
                                                               String              methodName)
    {
        int                ordinal = 99;
        String             symbolicName = null;
        String             description = null;

        final int    element1Ordinal         = 0;
        final String element1Value           = "NotRecommended";
        final String element1Description     = "This content is not recommended.";

        final int    element2Ordinal         = 1;
        final String element2Value           = "OneStar";
        final String element2Description     = "One star rating.";

        final int    element3Ordinal         = 2;
        final String element3Value           = "TwoStar";
        final String element3Description     = "Two star rating.";

        final int    element4Ordinal         = 3;
        final String element4Value           = "ThreeStar";
        final String element4Description     = "Three star rating.";

        final int    element5Ordinal         = 4;
        final String element5Value           = "FourStar";
        final String element5Description     = "Four star rating.";

        final int    element6Ordinal         = 5;
        final String element6Value           = "FiveStar";
        final String element6Description     = "Five star rating.";

        switch (starRating)
        {
            case NOT_RECOMMENDED:
                ordinal = element1Ordinal;
                symbolicName = element1Value;
                description = element1Description;
                break;

            case ONE_STAR:
                ordinal = element2Ordinal;
                symbolicName = element2Value;
                description = element2Description;
                break;

            case TWO_STARS:
                ordinal = element3Ordinal;
                symbolicName = element3Value;
                description = element3Description;
                break;

            case THREE_STARS:
                ordinal = element4Ordinal;
                symbolicName = element4Value;
                description = element4Description;
                break;

            case FOUR_STARS:
                ordinal = element5Ordinal;
                symbolicName = element5Value;
                description = element5Description;
                break;

            case FIVE_STARS:
                ordinal = element6Ordinal;
                symbolicName = element6Value;
                description = element6Description;
                break;
        }

        return repositoryHelper.addEnumPropertyToInstance(serviceName,
                                                          properties,
                                                          starsPropertyName,
                                                          ordinal,
                                                          symbolicName,
                                                          description,
                                                          methodName);
    }


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
    public void   removeReviewFromAsset(String     userId,
                                        String     reviewGUID) throws InvalidParameterException,
                                                                      PropertyServerException,
                                                                      UserNotAuthorizedException
    {
        final String methodName = "removeRatingFromAsset";
        final String guidParameter = "reviewGUID";

        errorHandler.validateUserId(userId, methodName);
        errorHandler.validateGUID(reviewGUID, guidParameter, methodName);

        OMRSMetadataCollection  metadataCollection = errorHandler.validateRepositoryConnector(methodName);

        try
        {
            metadataCollection.deleteEntity(userId, ratingTypeGUID, ratingTypeName, reviewGUID);
        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException error)
        {
            errorHandler.handleUnauthorizedUser(userId,
                                                methodName,
                                                serverName,
                                                serviceName);
        }
        catch (Throwable   error)
        {
            errorHandler.handleRepositoryError(error,
                                               methodName,
                                               serverName,
                                               serviceName);
        }
    }


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
    public String addLikeToAsset(String       userId,
                                 String       assetGUID) throws InvalidParameterException,
                                                                PropertyServerException,
                                                                UserNotAuthorizedException
    {
        final String methodName = "addLikeToAsset";
        final String guidParameter = "assetGUID";

        errorHandler.validateUserId(userId, methodName);
        errorHandler.validateGUID(assetGUID, guidParameter, methodName);

        OMRSMetadataCollection  metadataCollection = errorHandler.validateRepositoryConnector(methodName);

        basicHandler.validateEntityGUID(userId, assetGUID, assetTypeName, metadataCollection, methodName);

        try
        {
            /*
             * Create the Like Entity
             */
            EntityDetail feedbackEntity = metadataCollection.addEntity(userId,
                                                                       likeTypeGUID,
                                                                       null,
                                                                       null,
                                                                       InstanceStatus.ACTIVE);

            String  feedbackGUID = null;

            if (feedbackEntity != null)
            {
                feedbackGUID = feedbackEntity.getGUID();

                /*
                 * Link the Like to the asset
                 */
                metadataCollection.addRelationship(userId,
                                                   attachedLikeTypeGUID,
                                                   null,
                                                   assetGUID,
                                                   feedbackEntity.getGUID(),
                                                   InstanceStatus.ACTIVE);
            }

            return feedbackGUID;
        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException error)
        {
            errorHandler.handleUnauthorizedUser(userId,
                                                methodName,
                                                serverName,
                                                serviceName);
        }
        catch (Throwable   error)
        {
            errorHandler.handleRepositoryError(error,
                                               methodName,
                                               serverName,
                                               serviceName);
        }

        return null;
    }


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
    public void   removeLikeFromAsset(String     userId,
                                      String     likeGUID) throws InvalidParameterException,
                                                                  PropertyServerException,
                                                                  UserNotAuthorizedException
    {
        final String methodName = "removeLikeFromAsset";
        final String guidParameter = "likeGUID";

        errorHandler.validateUserId(userId, methodName);
        errorHandler.validateGUID(likeGUID, guidParameter, methodName);

        OMRSMetadataCollection  metadataCollection = errorHandler.validateRepositoryConnector(methodName);

        try
        {
            metadataCollection.deleteEntity(userId, likeTypeGUID, likeTypeName, likeGUID);
        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException error)
        {
            errorHandler.handleUnauthorizedUser(userId,
                                                methodName,
                                                serverName,
                                                serviceName);
        }
        catch (Throwable   error)
        {
            errorHandler.handleRepositoryError(error,
                                               methodName,
                                               serverName,
                                               serviceName);
        }
    }


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
    public String addCommentToAsset(String      userId,
                                    String      assetGUID,
                                    CommentType commentType,
                                    String      commentText) throws InvalidParameterException,
                                                                    PropertyServerException,
                                                                    UserNotAuthorizedException
    {
        final String methodName = "addCommentToAsset";
        final String guidParameter = "assetGUID";

        return this.addCommentToEntity(userId, assetGUID, guidParameter, commentType, commentText, methodName);
    }


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
    public String addCommentReply(String      userId,
                                  String      commentGUID,
                                  CommentType commentType,
                                  String      commentText) throws InvalidParameterException,
                                                                  PropertyServerException,
                                                                  UserNotAuthorizedException
    {
        final String methodName = "addCommentReply";
        final String guidParameter = "assetGUID";

        return this.addCommentToEntity(userId, commentGUID, guidParameter, commentType, commentText, methodName);
    }


    /**
     * Set up a property value for the CommentType enum property.
     *
     * @param properties   current properties
     * @param commentType  enum value
     * @param methodName   calling method
     *
     * @return  InstanceProperties object with the enum value added
     */
    private InstanceProperties addCommentTypePropertyToInstance(InstanceProperties  properties,
                                                                CommentType         commentType,
                                                                String              methodName)
    {
        int                ordinal = 99;
        String             symbolicName = null;
        String             description = null;

        final int    element1Ordinal         = 0;
        final String element1Value           = "GeneralComment";
        final String element1Description     = "General comment.";

        final int    element2Ordinal         = 1;
        final String element2Value           = "Question";
        final String element2Description     = "A question.";

        final int    element3Ordinal         = 2;
        final String element3Value           = "Answer";
        final String element3Description     = "An answer to a previously asked question.";

        final int    element4Ordinal         = 3;
        final String element4Value           = "Suggestion";
        final String element4Description     = "A suggestion for improvement.";

        final int    element5Ordinal         = 3;
        final String element5Value           = "Experience";
        final String element5Description     = "An account of an experience.";

        switch (commentType)
        {
            case STANDARD_COMMENT:
                ordinal = element1Ordinal;
                symbolicName = element1Value;
                description = element1Description;
                break;

            case QUESTION:
                ordinal = element2Ordinal;
                symbolicName = element2Value;
                description = element2Description;
                break;

            case ANSWER:
                ordinal = element3Ordinal;
                symbolicName = element3Value;
                description = element3Description;
                break;

            case SUGGESTION:
                ordinal = element4Ordinal;
                symbolicName = element4Value;
                description = element4Description;
                break;

            case USAGE_EXPERIENCE:
                ordinal = element5Ordinal;
                symbolicName = element5Value;
                description = element5Description;
                break;
        }

        return repositoryHelper.addEnumPropertyToInstance(serviceName,
                                                          properties,
                                                          commentTypePropertyName,
                                                          ordinal,
                                                          symbolicName,
                                                          description,
                                                          methodName);
    }




    /**
     * Adds a comment and links it to the supplied entity.
     *
     * @param userId        String - userId of user making request.
     * @param entityGUID    String - unique id for an existing comment.  Used to add a reply to a comment.
     * @param guidParameter name of parameter that supplied the entity'ss unique identifier.
     * @param commentType   type of comment enum.
     * @param commentText   String - the text of the comment.
     *
     * @return guid of new comment.
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException There is a problem adding the asset properties to
     *                                   the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    private String addCommentToEntity(String      userId,
                                      String      entityGUID,
                                      String      guidParameter,
                                      CommentType commentType,
                                      String      commentText,
                                      String      methodName) throws InvalidParameterException,
                                                                     PropertyServerException,
                                                                     UserNotAuthorizedException
    {
        final String typeParameter = "commentType";
        final String textParameter = "commentText";

        errorHandler.validateUserId(userId, methodName);
        errorHandler.validateGUID(entityGUID, guidParameter, methodName);
        errorHandler.validateEnum(commentType, typeParameter, methodName);
        errorHandler.validateText(commentText, textParameter, methodName);

        OMRSMetadataCollection  metadataCollection = errorHandler.validateRepositoryConnector(methodName);

        basicHandler.validateEntityGUID(userId, entityGUID, assetTypeName, metadataCollection, methodName);

        try
        {
            /*
             * Create the Comment Entity
             */
            InstanceProperties properties;

            properties = this.addCommentTypePropertyToInstance(null,
                                                               commentType,
                                                               methodName);
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      qualifiedNamePropertyName,
                                                                      "Comment:" + userId + ":" + new Date().toString(),
                                                                      methodName);
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      commentPropertyName,
                                                                      commentText,
                                                                      methodName);
            EntityDetail feedbackEntity = metadataCollection.addEntity(userId,
                                                                       commentTypeGUID,
                                                                       properties,
                                                                       null,
                                                                       InstanceStatus.ACTIVE);

            String   feedbackGUID = null;

            if (feedbackEntity != null)
            {
                feedbackGUID = feedbackEntity.getGUID();

                /*
                 * Link the comment reply to the supplied entity
                 */
                metadataCollection.addRelationship(userId,
                                                   attachedCommentTypeGUID,
                                                   null,
                                                   entityGUID,
                                                   feedbackEntity.getGUID(),
                                                   InstanceStatus.ACTIVE);
            }

            return feedbackGUID;
        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException error)
        {
            errorHandler.handleUnauthorizedUser(userId,
                                                methodName,
                                                serverName,
                                                serviceName);
        }
        catch (Throwable   error)
        {
            errorHandler.handleRepositoryError(error,
                                               methodName,
                                               serverName,
                                               serviceName);
        }

        return null;
    }


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
    public void   updateComment(String      userId,
                                String      commentGUID,
                                CommentType commentType,
                                String      commentText) throws InvalidParameterException,
                                                                PropertyServerException,
                                                                UserNotAuthorizedException
    {
        // todo
    }



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
    public void   removeCommentFromAsset(String     userId,
                                         String     commentGUID) throws InvalidParameterException,
                                                                        PropertyServerException,
                                                                        UserNotAuthorizedException
    {
        final String methodName = "removeCommentFromAsset";
        final String guidParameter = "commentGUID";

        errorHandler.validateUserId(userId, methodName);
        errorHandler.validateGUID(commentGUID, guidParameter, methodName);

        OMRSMetadataCollection  metadataCollection = errorHandler.validateRepositoryConnector(methodName);

        try
        {
            metadataCollection.deleteEntity(userId, commentTypeGUID, commentTypeName, commentGUID);
        }
        catch (org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException error)
        {
            errorHandler.handleUnauthorizedUser(userId,
                                                methodName,
                                                serverName,
                                                serviceName);        }
        catch (Throwable   error)
        {
            errorHandler.handleRepositoryError(error,
                                               methodName,
                                               serverName,
                                               serviceName);
        }
    }
}
