/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.handlers;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.openmetadata.converters.*;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.feedback.*;
import org.odpi.openmetadata.frameworks.openmetadata.search.*;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ElementStatus;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.*;


/**
 * FeedbackHandler is the handler for managing comments, ratings, likes and tags.
 */
public class FeedbackHandler extends OpenMetadataHandlerBase
{
    private final CommentConverter<CommentElement>         commentConverter;
    private final LikeConverter<LikeElement>               likeConverter;
    private final RatingConverter<RatingElement>           ratingConverter;
    private final InformalTagConverter<InformalTagElement> tagConverter;
    private final NoteLogConverter<NoteLogElement>         noteLogConverter;
    private final NoteConverter<NoteElement>               noteConverter;



    /**
     * Create a new handler.
     *
     * @param localServerName        name of this server (view server)
     * @param auditLog               logging destination
     * @param serviceName            local service name
     * @param openMetadataClient     access to open metadata
     */
    public FeedbackHandler(String             localServerName,
                           AuditLog           auditLog,
                           String             serviceName,
                           OpenMetadataClient openMetadataClient)
    {
        super(localServerName, auditLog, serviceName, openMetadataClient, null);

        this.commentConverter = new CommentConverter<>(propertyHelper, serviceName, localServerName);
        this.likeConverter    = new LikeConverter<>(propertyHelper, serviceName, localServerName);
        this.ratingConverter  = new RatingConverter<>(propertyHelper, serviceName, localServerName);
        this.tagConverter     = new InformalTagConverter<>(propertyHelper, serviceName, localServerName);
        this.noteLogConverter = new NoteLogConverter<>(propertyHelper, serviceName, localServerName);
        this.noteConverter    = new NoteConverter<>(propertyHelper, serviceName, localServerName);
    }


    /**
     * Adds a star rating and optional review text to the element.  If the user has already attached
     * a rating then the original one is over-ridden.
     *
     * @param userId      userId of user making request
     * @param elementGUID   unique identifier for the element
     * @param feedbackTypeName type of feedback element
     * @param relationshipTypeName type of attaching relationship
     * @param metadataSourceOptions  options to control access to open metadata
     * @param feedbackProperties  properties of the feedback
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the element properties to the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    private void addFeedbackToElement(String                            userId,
                                      String                            elementGUID,
                                      String                            feedbackTypeName,
                                      String                            relationshipTypeName,
                                      MetadataSourceOptions             metadataSourceOptions,
                                      NewElementProperties              feedbackProperties) throws InvalidParameterException,
                                                                                                   PropertyServerException,
                                                                                                   UserNotAuthorizedException
    {
        RelatedMetadataElement existingFeedback = this.getFeedbackForUser(userId, elementGUID, relationshipTypeName, new QueryOptions(metadataSourceOptions));

        if (existingFeedback == null)
        {
            NewElementOptions newElementOptions = new NewElementOptions(metadataSourceOptions);

            newElementOptions.setInitialStatus(ElementStatus.ACTIVE);
            newElementOptions.setAnchorGUID(elementGUID);
            newElementOptions.setIsOwnAnchor(false);
            newElementOptions.setParentGUID(elementGUID);
            newElementOptions.setParentRelationshipTypeName(relationshipTypeName);

            openMetadataClient.createMetadataElementInStore(userId,
                                                            feedbackTypeName,
                                                            newElementOptions,
                                                            null,
                                                            feedbackProperties,
                                                            null);
        }
        else
        {
            openMetadataClient.updateMetadataElementInStore(userId,
                                                            existingFeedback.getElement().getElementGUID(),
                                                            new UpdateOptions(metadataSourceOptions),
                                                            feedbackProperties);
        }
    }


    /**
     * Retrieve any feedback previously added by this user.
     *
     * @param userId      userId of user making request.
     * @param elementGUID   unique identifier for the element.
     * @param relationshipTypeName name of the relationship type
     * @param queryOptions  options to control access to open metadata
     * @return relationship and entity
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the element properties to the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    private RelatedMetadataElement getFeedbackForUser(String                userId,
                                                      String                elementGUID,
                                                      String                relationshipTypeName,
                                                      QueryOptions queryOptions) throws InvalidParameterException,
                                                                                                          PropertyServerException,
                                                                                                          UserNotAuthorizedException
    {
        QueryOptions workingQueryOptions = new QueryOptions(queryOptions);

        workingQueryOptions.setStartFrom(0);
        workingQueryOptions.setPageSize(openMetadataClient.getMaxPagingSize());

        RelatedMetadataElementList attachedFeedbacks = openMetadataClient.getRelatedMetadataElements(userId,
                                                                                                     elementGUID,
                                                                                                     1,
                                                                                                     relationshipTypeName,
                                                                                                     workingQueryOptions);

        RelatedMetadataElement existingFeedback = null;

        while ((existingFeedback == null) && (attachedFeedbacks != null) && (attachedFeedbacks.getElementList() != null))
        {
            for (RelatedMetadataElement attachedFeedback : attachedFeedbacks.getElementList())
            {
                if (attachedFeedback != null)
                {
                    if (userId.equals(attachedFeedback.getVersions().getCreatedBy()))
                    {
                        existingFeedback = attachedFeedback;
                        break;
                    }
                }
            }

            workingQueryOptions.setStartFrom(workingQueryOptions.getStartFrom() + openMetadataClient.getMaxPagingSize());
            attachedFeedbacks = openMetadataClient.getRelatedMetadataElements(userId,
                                                                              elementGUID,
                                                                              1,
                                                                              relationshipTypeName,
                                                                              workingQueryOptions);
        }

        return existingFeedback;
    }


    /**
     * Adds a star rating and optional review text to the element.  If the user has already attached
     * a rating then the original one is over-ridden.
     *
     * @param userId      userId of user making request.
     * @param elementGUID   unique identifier for the element.
     * @param updateOptions provides a structure for the additional options when updating an element.
     * @param properties  properties of the rating
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the element properties to the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void addRatingToElement(String                            userId,
                                   String                            elementGUID,
                                   UpdateOptions                     updateOptions,
                                   RatingProperties                  properties) throws InvalidParameterException,
                                                                                        PropertyServerException,
                                                                                        UserNotAuthorizedException
    {
        final String methodName = "addRatingToElement";
        final String propertiesParameterName = "properties";
        final String starRatingParameterName = "properties.starRating";

        propertyHelper.validateObject(properties, propertiesParameterName, methodName);
        propertyHelper.validateObject(properties.getStarRating(), starRatingParameterName, methodName);

        this.addFeedbackToElement(userId,
                                  elementGUID,
                                  OpenMetadataType.RATING.typeName,
                                  OpenMetadataType.ATTACHED_RATING_RELATIONSHIP.typeName,
                                  updateOptions,
                                  elementBuilder.getNewElementProperties(properties));
    }


    /**
     * Removes of a review that was added to the element by this user.
     *
     * @param userId      userId of user making request.
     * @param elementGUID  unique identifier for the attached element.
     * @param metadataSourceOptions  options to control access to open metadata
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem updating the element properties in the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void removeRatingFromElement(String                userId,
                                        String                elementGUID,
                                        MetadataSourceOptions metadataSourceOptions) throws InvalidParameterException,
                                                                                            PropertyServerException,
                                                                                            UserNotAuthorizedException
    {
        RelatedMetadataElement existingRating = this.getFeedbackForUser(userId,
                                                                        elementGUID,
                                                                        OpenMetadataType.ATTACHED_RATING_RELATIONSHIP.typeName,
                                                                        new QueryOptions(metadataSourceOptions));

        if (existingRating != null)
        {
            DeleteOptions deleteOptions = new DeleteOptions(metadataSourceOptions);

            openMetadataClient.deleteMetadataElementInStore(userId,
                                                            existingRating.getElement().getElementGUID(),
                                                            deleteOptions);
        }
    }



    /**
     * Return the ratings attached to an element.
     *
     * @param userId       userId of user making request.
     * @param elementGUID    unique identifier for the element that the comments are connected to (maybe a comment too).
     * @param queryOptions multiple options to control the query
     * @return list of ratings
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem updating the element properties in the property server.
     * @throws UserNotAuthorizedException the user does not have permission to perform this request.
     */
    public List<RatingElement>  getAttachedRatings(String              userId,
                                                   String              elementGUID,
                                                   QueryOptions        queryOptions) throws InvalidParameterException,
                                                                                            PropertyServerException,
                                                                                            UserNotAuthorizedException
    {
        final String methodName = "getAttachedRatings";

        RelatedMetadataElementList relatedMetadataElements = openMetadataClient.getRelatedMetadataElements(userId,
                                                                                                           elementGUID,
                                                                                                           1,
                                                                                                           OpenMetadataType.ATTACHED_RATING_RELATIONSHIP.typeName,
                                                                                                           queryOptions);
        return this.getRatingsFromRelatedMetadataElement(relatedMetadataElements, methodName);
    }


    /**
     * Convert the GAF beans into feedback beans.
     *
     * @param relatedMetadataElements elements retrieved from the repository
     * @param methodName calling method
     * @return feedback beans
     * @throws PropertyServerException error formatting bean
     */
    private List<RatingElement> getRatingsFromRelatedMetadataElement(RelatedMetadataElementList relatedMetadataElements,
                                                                     String                     methodName) throws PropertyServerException
    {
        if ((relatedMetadataElements != null) && (relatedMetadataElements.getElementList() != null))
        {
            List<RatingElement> results = new ArrayList<>();

            for (RelatedMetadataElement relatedMetadataElement : relatedMetadataElements.getElementList())
            {
                if (relatedMetadataElement != null)
                {
                    results.add(ratingConverter.getNewBean(RatingElement.class,
                                                           relatedMetadataElement.getElement(),
                                                           methodName));
                }
            }

            return results;
        }

        return null;
    }


    /**
     * Adds a "Like" to the element.
     *
     * @param userId      userId of user making request
     * @param elementGUID   unique identifier for the element
     * @param updateOptions provides a structure for the additional options when updating an element.
     * @param properties properties
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the element properties to the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void addLikeToElement(String                            userId,
                                 String                            elementGUID,
                                 UpdateOptions                     updateOptions,
                                 LikeProperties                    properties) throws InvalidParameterException,
                                                                                      PropertyServerException,
                                                                                      UserNotAuthorizedException
    {
        addFeedbackToElement(userId,
                             elementGUID,
                             OpenMetadataType.LIKE.typeName,
                             OpenMetadataType.ATTACHED_LIKE_RELATIONSHIP.typeName,
                             updateOptions,
                             elementBuilder.getNewElementProperties(properties));
    }


    /**
     * Removes a "Like" added to the element by this user.
     *
     * @param userId   userId of user making request.
     * @param elementGUID unique identifier for the like object.
     * @param metadataSourceOptions  options to control access to open metadata
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem updating the element properties in the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void removeLikeFromElement(String                userId,
                                      String                elementGUID,
                                      MetadataSourceOptions metadataSourceOptions) throws InvalidParameterException,
                                                                                          PropertyServerException,
                                                                                          UserNotAuthorizedException
    {
        RelatedMetadataElement existingLike = this.getFeedbackForUser(userId,
                                                                      elementGUID,
                                                                      OpenMetadataType.ATTACHED_LIKE_RELATIONSHIP.typeName,
                                                                      new QueryOptions(metadataSourceOptions));
        if (existingLike != null)
        {
            DeleteOptions deleteOptions = new DeleteOptions(metadataSourceOptions);

            openMetadataClient.deleteMetadataElementInStore(userId,
                                                            existingLike.getElement().getElementGUID(),
                                                            deleteOptions);
        }
    }


    /**
     * Return the likes attached to an element.
     *
     * @param userId       userId of user making request.
     * @param elementGUID    unique identifier for the element that the comments are connected to (maybe a comment too).
     * @param queryOptions multiple options to control the query
     *
     * @return list of likes
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem updating the element properties in the property server.
     * @throws UserNotAuthorizedException the user does not have permission to perform this request.
     */
    public List<LikeElement>  getAttachedLikes(String              userId,
                                               String              elementGUID,
                                               QueryOptions        queryOptions) throws InvalidParameterException,
                                                                                         PropertyServerException,
                                                                                         UserNotAuthorizedException
    {
        final String methodName = "getAttachedLikes";

        RelatedMetadataElementList relatedMetadataElements = openMetadataClient.getRelatedMetadataElements(userId,
                                                                                                           elementGUID,
                                                                                                           1,
                                                                                                           OpenMetadataType.ATTACHED_LIKE_RELATIONSHIP.typeName,
                                                                                                           queryOptions);
        return this.getLikesFromRelatedMetadataElement(relatedMetadataElements, methodName);
    }


    /**
     * Convert the GAF beans into feedback beans.
     *
     * @param relatedMetadataElements elements retrieved from the repository
     * @param methodName calling method
     * @return feedback beans
     * @throws PropertyServerException error formatting bean
     */
    private List<LikeElement> getLikesFromRelatedMetadataElement(RelatedMetadataElementList relatedMetadataElements,
                                                                 String                     methodName) throws PropertyServerException
    {
        if ((relatedMetadataElements != null) && (relatedMetadataElements.getElementList() != null))
        {
            List<LikeElement> results = new ArrayList<>();

            for (RelatedMetadataElement relatedMetadataElement : relatedMetadataElements.getElementList())
            {
                if (relatedMetadataElement != null)
                {
                    results.add(likeConverter.getNewBean(LikeElement.class,
                                                         relatedMetadataElement.getElement(),
                                                         methodName));
                }
            }

            return results;
        }

        return null;
    }


    /**
     * Adds a comment to the element.
     *
     * @param userId        userId of user making request.
     * @param elementGUID     unique identifier for the element.
     * @param initialClassifications map of classification names to classification properties to include in the entity creation request
     * @param properties   properties of the comment
     *
     * @return guid of new comment.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the element properties to the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String addCommentToElement(String                                userId,
                                      String                                elementGUID,
                                      MetadataSourceOptions                 metadataSourceOptions,
                                      Map<String, ClassificationProperties> initialClassifications,
                                      CommentProperties                     properties) throws InvalidParameterException,
                                                                                               PropertyServerException,
                                                                                               UserNotAuthorizedException
    {
        final String methodName = "addCommentToElement";

        NewElementProperties elementProperties = this.getElementPropertiesForComment(properties, methodName);

        NewElementOptions newElementOptions = new NewElementOptions(metadataSourceOptions);

        newElementOptions.setInitialStatus(ElementStatus.ACTIVE);
        newElementOptions.setAnchorGUID(elementGUID);
        newElementOptions.setIsOwnAnchor(false);
        newElementOptions.setParentGUID(elementGUID);
        newElementOptions.setParentRelationshipTypeName(OpenMetadataType.ATTACHED_COMMENT_RELATIONSHIP.typeName);
        newElementOptions.setParentAtEnd1(true);

        return openMetadataClient.createMetadataElementInStore(userId,
                                                               OpenMetadataType.COMMENT.typeName,
                                                               newElementOptions,
                                                               classificationBuilder.getInitialClassifications(initialClassifications),
                                                               elementProperties,
                                                               null);
    }


    /**
     * Validate comment properties and construct an element properties object.
     *
     * @param properties comment properties from caller
     * @param methodName calling method
     * @return element properties
     * @throws InvalidParameterException properties from caller are invalid
     */
    private NewElementProperties getElementPropertiesForComment(CommentProperties properties,
                                                                String            methodName) throws InvalidParameterException
    {
        final String propertiesParameterName   = "properties";
        final String commentQNameParameterName = "properties.qualifiedName";
        final String commentTextParameterName  = "properties.commentText";

        propertyHelper.validateObject(properties, propertiesParameterName, methodName);
        propertyHelper.validateMandatoryName(properties.getQualifiedName(), commentQNameParameterName, methodName);
        propertyHelper.validateText(properties.getDescription(), commentTextParameterName, methodName);

        return elementBuilder.getNewElementProperties(properties);
    }


    /**
     * Adds a comment to the element.
     *
     * @param userId        userId of user making request.
     * @param commentGUID     unique identifier for the comment to attach to.
     * @param elementGUID     unique identifier for the anchor element.
     * @param metadataSourceOptions  options to control access to open metadata
     * @param initialClassifications map of classification names to classification properties to include in the entity creation request
     * @param properties   properties of the comment
     *
     * @return guid of new comment.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the element properties to the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String addCommentReply(String                                userId,
                                  String                                elementGUID,
                                  String                                commentGUID,
                                  MetadataSourceOptions                 metadataSourceOptions,
                                  Map<String, ClassificationProperties> initialClassifications,
                                  CommentProperties                     properties) throws InvalidParameterException,
                                                                                       PropertyServerException,
                                                                                       UserNotAuthorizedException
    {
        final String methodName = "addCommentToElement";

        NewElementProperties elementProperties = this.getElementPropertiesForComment(properties, methodName);

        NewElementOptions newElementOptions = new NewElementOptions(metadataSourceOptions);

        newElementOptions.setInitialStatus(ElementStatus.ACTIVE);
        newElementOptions.setAnchorGUID(elementGUID);
        newElementOptions.setIsOwnAnchor(false);
        newElementOptions.setParentGUID(commentGUID);
        newElementOptions.setParentRelationshipTypeName(OpenMetadataType.ATTACHED_COMMENT_RELATIONSHIP.typeName);
        newElementOptions.setParentAtEnd1(true);

        return openMetadataClient.createMetadataElementInStore(userId,
                                                               OpenMetadataType.COMMENT.typeName,
                                                               newElementOptions,
                                                               classificationBuilder.getInitialClassifications(initialClassifications),
                                                               elementProperties,
                                                               null);
    }


    /**
     * Update an existing comment.
     *
     * @param userId        userId of user making request.
     * @param commentGUID   unique identifier for the comment to change.
     * @param updateOptions provides a structure for the additional options when updating an element.
     * @param properties   properties of the comment
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the element properties to the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void   updateComment(String            userId,
                                String            commentGUID,
                                UpdateOptions     updateOptions,
                                CommentProperties properties) throws InvalidParameterException,
                                                                     PropertyServerException,
                                                                     UserNotAuthorizedException
    {
        final String methodName = "updateComment";

        NewElementProperties elementProperties = this.getElementPropertiesForComment(properties, methodName);

        openMetadataClient.updateMetadataElementInStore(userId, commentGUID, updateOptions, elementProperties);
    }


    /**
     * Link a comment that contains the best answer to a question posed in another comment.
     *
     * @param userId calling user
     * @param questionCommentGUID unique identifier of the comment containing the question
     * @param answerCommentGUID unique identifier of the comment containing the accepted answer
     * @param metadataSourceOptions  options to control access to open metadata
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setupAcceptedAnswer(String  userId,
                                    String  questionCommentGUID,
                                    String  answerCommentGUID,
                                    MetadataSourceOptions metadataSourceOptions) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException
    {
        openMetadataClient.createRelatedElementsInStore(userId,
                                                        OpenMetadataType.ACCEPTED_ANSWER_RELATIONSHIP.typeName,
                                                        questionCommentGUID,
                                                        answerCommentGUID,
                                                        metadataSourceOptions,
                                                        null);
    }


    /**
     * Unlink a comment that contains an answer to a question posed in another comment.
     *
     * @param userId calling user
     * @param questionCommentGUID unique identifier of the comment containing the question
     * @param answerCommentGUID unique identifier of the comment containing the accepted answer
     * @param deleteOptions  options to control access to open metadata
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearAcceptedAnswer(String        userId,
                                    String        questionCommentGUID,
                                    String        answerCommentGUID,
                                    DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException
    {
        QueryOptions queryOptions = new QueryOptions(deleteOptions);

        OpenMetadataRelationshipList relationships = openMetadataClient.getMetadataElementRelationships(userId,
                                                                                                        questionCommentGUID,
                                                                                                        answerCommentGUID,
                                                                                                        OpenMetadataType.ACCEPTED_ANSWER_RELATIONSHIP.typeName,
                                                                                                        queryOptions);

        if ((relationships != null) && (relationships.getElementList() != null))
        {
            for (OpenMetadataRelationship relationship : relationships.getElementList())
            {
                if (relationship != null)
                {
                    openMetadataClient.deleteRelationshipInStore(userId,
                                                                 relationship.getRelationshipGUID(),
                                                                 deleteOptions);

                    break;
                }
            }
        }
    }


    /**
     * Removes a comment added to the element by this user.
     *
     * @param userId       userId of user making request.
     * @param commentGUID  unique identifier for the comment object.
     * @param deleteOptions options for a delete request
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem updating the element properties in the property server.
     * @throws UserNotAuthorizedException the user does not have permission to perform this request.
     */
    public void removeComment(String        userId,
                              String        commentGUID,
                              DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                  PropertyServerException,
                                                                  UserNotAuthorizedException
    {
        openMetadataClient.deleteMetadataElementInStore(userId, commentGUID, deleteOptions);
    }


    /**
     * Return the requested comment.
     *
     * @param userId       userId of user making request.
     * @param commentGUID  unique identifier for the comment object.
     * @param getOptions multiple options to control the query
     * @return comment properties
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem updating the element properties in the property server.
     * @throws UserNotAuthorizedException the user does not have permission to perform this request.
     */
    public CommentElement getComment(String     userId,
                                     String     commentGUID,
                                     GetOptions getOptions) throws InvalidParameterException,
                                                                   PropertyServerException,
                                                                   UserNotAuthorizedException
    {
        final String methodName = "getComment";

        OpenMetadataElement commentElement = openMetadataClient.getMetadataElementByGUID(userId, commentGUID, getOptions);

        if (commentElement != null)
        {
            return commentConverter.getNewBean(CommentElement.class,
                                               commentElement,
                                               methodName);
        }

        return null;
    }


    /**
     * Return the comments attached to an element.
     *
     * @param userId       userId of user making request.
     * @param elementGUID    unique identifier for the element that the comments are connected to (maybe a comment too).
     * @param queryOptions multiple options to control the query
     *
     * @return list of comments
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem updating the element properties in the property server.
     * @throws UserNotAuthorizedException the user does not have permission to perform this request.
     */
    public List<CommentElement>  getAttachedComments(String              userId,
                                                     String              elementGUID,
                                                     QueryOptions        queryOptions) throws InvalidParameterException,
                                                                                              PropertyServerException,
                                                                                              UserNotAuthorizedException
    {
        final String methodName = "getAttachedComments";

        RelatedMetadataElementList relatedMetadataElements = openMetadataClient.getRelatedMetadataElements(userId,
                                                                                                           elementGUID,
                                                                                                           1,
                                                                                                           OpenMetadataType.ATTACHED_COMMENT_RELATIONSHIP.typeName,
                                                                                                           queryOptions);
        return this.getCommentsFromRelatedMetadataElement(relatedMetadataElements, methodName);
    }


    /**
     * Convert the GAF beans into feedback beans.
     *
     * @param relatedMetadataElements elements retrieved from the repository
     * @param methodName calling method
     * @return feedback beans
     * @throws PropertyServerException error formatting bean
     */
    private List<CommentElement> getCommentsFromRelatedMetadataElement(RelatedMetadataElementList relatedMetadataElements,
                                                                       String                     methodName) throws PropertyServerException
    {
        if ((relatedMetadataElements != null) && (relatedMetadataElements.getElementList() != null))
        {
            List<CommentElement> results = new ArrayList<>();

            for (RelatedMetadataElement relatedMetadataElement : relatedMetadataElements.getElementList())
            {
                if (relatedMetadataElement != null)
                {
                    results.add(commentConverter.getNewBean(CommentElement.class,
                                                            relatedMetadataElement.getElement(),
                                                            methodName));
                }
            }

            return results;
        }

        return null;
    }


    /**
     * Retrieve the list of comment metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param userId calling user
     * @param searchString string to find in the properties
     * @param suppliedSearchOptions multiple options to control the query
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<CommentElement> findComments(String        userId,
                                             String        searchString,
                                             SearchOptions suppliedSearchOptions) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException
    {
        final String methodName = "findComments";

        SearchOptions searchOptions = new SearchOptions(suppliedSearchOptions);

        if (searchOptions.getMetadataElementTypeName() == null)
        {
            searchOptions.setMetadataElementTypeName(OpenMetadataType.COMMENT.typeName);
        }

        List<OpenMetadataElement> openMetadataElements = openMetadataClient.findMetadataElementsWithString(userId,
                                                                                                           searchString,
                                                                                                           searchOptions);
        return this.getCommentsFromOpenMetadataElement(openMetadataElements, methodName);
    }


    /**
     * Convert the GAF beans into feedback beans.
     *
     * @param openMetadataElements elements retrieved from the repository
     * @param methodName calling method
     * @return feedback beans
     * @throws PropertyServerException error formatting bean
     */
    private List<CommentElement> getCommentsFromOpenMetadataElement(List<OpenMetadataElement> openMetadataElements,
                                                                    String                    methodName) throws PropertyServerException
    {
        if (openMetadataElements != null)
        {
            List<CommentElement> results = new ArrayList<>();

            for (OpenMetadataElement openMetadataElement : openMetadataElements)
            {
                if (openMetadataElement != null)
                {
                    results.add(commentConverter.getNewBean(CommentElement.class, openMetadataElement, methodName));
                }
            }

            return results;
        }

        return null;
    }


    /**
     * Creates a new informal tag and returns the unique identifier for it.
     *
     * @param userId           userId of user making request.
     * @param requestedNewElementOptions details of the element to create
     * @param initialClassifications map of classification names to classification properties to include in the entity creation request
     * @param properties       name of the tag and (optional) description.  Setting a description, particularly in a public tag
     * makes the tag more valuable to other users and can act as an embryonic note.
     *
     * @return GUID for new tag.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the element properties to the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String createInformalTag(String                                userId,
                                    NewElementOptions                     requestedNewElementOptions,
                                    Map<String, ClassificationProperties> initialClassifications,
                                    InformalTagProperties                 properties) throws InvalidParameterException,
                                                                                             PropertyServerException,
                                                                                             UserNotAuthorizedException
    {
        final String methodName = "createInformalTag";
        final String propertiesParameterName = "properties";
        final String nameParameterName = "properties.name";

        propertyHelper.validateObject(properties, propertiesParameterName, methodName);
        propertyHelper.validateMandatoryName(properties.getDisplayName(), nameParameterName, methodName);

        NewElementOptions newElementOptions = new NewElementOptions(requestedNewElementOptions);

        newElementOptions.setInitialStatus(ElementStatus.ACTIVE);

        return openMetadataClient.createMetadataElementInStore(userId,
                                                               OpenMetadataType.INFORMAL_TAG.typeName,
                                                               requestedNewElementOptions,
                                                               classificationBuilder.getInitialClassifications(initialClassifications),
                                                               elementBuilder.getNewElementProperties(properties),
                                                               null);
    }


    /**
     * Updates the description of an existing tag (either private or public).
     *
     * @param userId          userId of user making request.
     * @param tagGUID         unique identifier for the tag.
     * @param properties  description of the tag.  Setting a description, particularly in a public tag
     *                        makes the tag more valuable to other users and can act as an embryonic glossary term.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the element properties to the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void   updateTagDescription(String                userId,
                                       String                tagGUID,
                                       UpdateOptions         updateOptions,
                                       InformalTagProperties properties) throws InvalidParameterException,
                                                                                PropertyServerException,
                                                                                UserNotAuthorizedException
    {
        openMetadataClient.updateMetadataElementInStore(userId,
                                                        tagGUID,
                                                        updateOptions,
                                                        elementBuilder.getNewElementProperties(properties));
    }


    /**
     * Removes a tag from the repository.
     * A private tag can be deleted by its creator and all the references are lost;
     * a public tag can be deleted by anyone, but only if it is not attached to any referenceable.
     *
     * @param userId    userId of user making request.
     * @param tagGUID   unique id for the tag.
     * @param deleteOptions options for a delete request
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem updating the element properties in the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void   deleteTag(String        userId,
                            String        tagGUID,
                            DeleteOptions deleteOptions) throws InvalidParameterException,
                                                          PropertyServerException,
                                                          UserNotAuthorizedException
    {
        openMetadataClient.deleteMetadataElementInStore(userId, tagGUID, deleteOptions);
    }


    /**
     * Return the tag for the supplied unique identifier (guid).
     *
     * @param userId userId of the user making the request.
     * @param tagGUID unique identifier of the tag.
     * @param getOptions multiple options to control the query
     *
     * @return tag
     * @throws InvalidParameterException the userId is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public InformalTagElement getTag(String     userId,
                                     String     tagGUID,
                                     GetOptions getOptions) throws InvalidParameterException,
                                                                   PropertyServerException,
                                                                   UserNotAuthorizedException
    {
        final String methodName = "getTag";

        OpenMetadataElement openMetadataElement = openMetadataClient.getMetadataElementByGUID(userId, tagGUID, getOptions);

        if (openMetadataElement != null)
        {
            return tagConverter.getNewBean(InformalTagElement.class,
                                           openMetadataElement,
                                           methodName);
        }

        return null;
    }


    /**
     * Return the list of tags exactly matching the supplied name.
     *
     * @param userId the name of the calling user.
     * @param tag name of tag.
     * @param queryOptions multiple options to control the query
     *
     * @return tag list
     * @throws InvalidParameterException the userId is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<InformalTagElement> getTagsByName(String              userId,
                                                  String              tag,
                                                  QueryOptions        queryOptions) throws InvalidParameterException,
                                                                                       PropertyServerException,
                                                                                       UserNotAuthorizedException
    {
        final String methodName = "getTagsByName";
        final String nameParameterName = "tag";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateMandatoryName(tag, nameParameterName, methodName);
        propertyHelper.validatePaging(queryOptions, openMetadataClient.getMaxPagingSize(), methodName);

        List<String> propertyNames = Collections.singletonList(OpenMetadataProperty.DISPLAY_NAME.name);

        List<OpenMetadataElement> openMetadataElements = openMetadataClient.findMetadataElements(userId,
                                                                                                 propertyHelper.getSearchPropertiesByName(propertyNames, tag, PropertyComparisonOperator.EQ),
                                                                                                 null,
                                                                                                 queryOptions);

        if (openMetadataElements != null)
        {
            return this.getTagsFromOpenMetadataElement(openMetadataElements, methodName);
        }

        return null;
    }


    /**
     * Convert the GAF beans into feedback beans.
     *
     * @param openMetadataElements elements retrieved from the repository
     * @param methodName calling method
     * @return feedback beans
     * @throws PropertyServerException error formatting bean
     */
    private List<InformalTagElement> getTagsFromOpenMetadataElement(List<OpenMetadataElement> openMetadataElements,
                                                                    String                    methodName) throws PropertyServerException
    {
        if (openMetadataElements != null)
        {
            List<InformalTagElement> results = new ArrayList<>();

            for (OpenMetadataElement openMetadataElement : openMetadataElements)
            {
                if (openMetadataElement != null)
                {
                    results.add(tagConverter.getNewBean(InformalTagElement.class, openMetadataElement, methodName));
                }
            }

            return results;
        }

        return null;
    }


    /**
     * Return the list of tags containing the supplied string in either the name or description.
     *
     * @param userId the name of the calling user.
     * @param tag name of tag.  This may include wild card characters.
     * @param suppliedSearchOptions multiple options to control the query
     *
     * @return tag list
     * @throws InvalidParameterException the userId is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<InformalTagElement> findTags(String        userId,
                                             String        tag,
                                             SearchOptions suppliedSearchOptions) throws InvalidParameterException,
                                                                                 PropertyServerException,
                                                                                 UserNotAuthorizedException
    {
        final String methodName = "findTags";

        SearchOptions searchOptions = new SearchOptions(suppliedSearchOptions);

        if (searchOptions.getMetadataElementTypeName() == null)
        {
            searchOptions.setMetadataElementTypeName(OpenMetadataType.INFORMAL_TAG.typeName);
        }

        List<OpenMetadataElement> openMetadataElements = openMetadataClient.findMetadataElementsWithString(userId,
                                                                                                           tag,
                                                                                                           searchOptions);
        return this.getTagsFromOpenMetadataElement(openMetadataElements, methodName);
    }


    /**
     * Return the list of the calling user's private tags containing the supplied string in either the name or description.
     *
     * @param userId the name of the calling user.
     * @param tag name of tag.  This may include wild card characters.
     * @param searchOptions multiple options to control the query
     *
     * @return tag list
     * @throws InvalidParameterException the userId is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<InformalTagElement> findMyTags(String        userId,
                                               String        tag,
                                               SearchOptions searchOptions) throws InvalidParameterException,
                                                                                   PropertyServerException,
                                                                                   UserNotAuthorizedException
    {
        List<InformalTagElement> allMatchingTags = this.findTags(userId,
                                                                 tag,
                                                                 searchOptions);

        if (allMatchingTags != null)
        {
            List<InformalTagElement> myTags = new ArrayList<>();

            for (InformalTagElement tagElement : allMatchingTags)
            {
                if ((tagElement == null) || (userId.equals(tagElement.getElementHeader().getVersions().getCreatedBy())))
                {
                    myTags.add(tagElement);
                }
            }

            return myTags;
        }

        return null;
    }


    /**
     * Adds a tag (either private of public) to an element.
     *
     * @param userId           userId of user making request.
     * @param elementGUID        unique id for the element.
     * @param tagGUID          unique id of the tag.
     * @param metadataSourceOptions  options to control access to open metadata
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the element properties to the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void   addTagToElement(String                userId,
                                  String                elementGUID,
                                  String                tagGUID,
                                  MetadataSourceOptions metadataSourceOptions) throws InvalidParameterException,
                                                                                      PropertyServerException,
                                                                                      UserNotAuthorizedException
    {
        openMetadataClient.createRelatedElementsInStore(userId,
                                                        OpenMetadataType.ATTACHED_TAG_RELATIONSHIP.typeName,
                                                        elementGUID,
                                                        tagGUID,
                                                        metadataSourceOptions,
                                                        null);
    }


    /**
     * Removes a tag from the element that was added by this user.
     *
     * @param userId    userId of user making request.
     * @param elementGUID unique id for the element.
     * @param tagGUID   unique id for the tag.
     * @param deleteOptions  options to control access to open metadata
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem updating the element properties in the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void   removeTagFromElement(String        userId,
                                       String        elementGUID,
                                       String        tagGUID,
                                       DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                           PropertyServerException,
                                                                           UserNotAuthorizedException
    {
        OpenMetadataRelationshipList relationships = openMetadataClient.getMetadataElementRelationships(userId,
                                                                                                        elementGUID,
                                                                                                        tagGUID,
                                                                                                        OpenMetadataType.ATTACHED_TAG_RELATIONSHIP.typeName,
                                                                                                        new QueryOptions(deleteOptions));

        if (relationships != null)
        {
            for (OpenMetadataRelationship relationship : relationships.getElementList())
            {
                if (relationship != null)
                {
                    openMetadataClient.deleteRelationshipInStore(userId, relationship.getRelationshipGUID(), deleteOptions);
                }
            }
        }
    }


    /**
     * Return the list of unique identifiers for elements that are linked to a specific tag either directly, or via one
     * of its schema elements.  An Element's GUID may appear multiple times in the results if it is tagged multiple times
     * with the requested tag.
     *
     * @param userId the name of the calling user.
     * @param tagGUID unique identifier of tag.
     * @param queryOptions multiple options to control the query
     *
     * @return element guid list
     * @throws InvalidParameterException the userId is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<RelatedMetadataElementStub> getElementsByTag(String       userId,
                                                             String       tagGUID,
                                                             QueryOptions queryOptions) throws InvalidParameterException,
                                                                                               PropertyServerException,
                                                                                               UserNotAuthorizedException
    {
        RelatedMetadataElementList relatedMetadataElements = openMetadataClient.getRelatedMetadataElements(userId,
                                                                                                           tagGUID,
                                                                                                           2,
                                                                                                           OpenMetadataType.ATTACHED_TAG_RELATIONSHIP.typeName,
                                                                                                           queryOptions);

        if ((relatedMetadataElements != null) && (relatedMetadataElements.getElementList() != null))
        {
            List<RelatedMetadataElementStub> stubs = new ArrayList<>();

            for (RelatedMetadataElement relatedMetadataElement : relatedMetadataElements.getElementList())
            {
                if (relatedMetadataElement != null)
                {
                    RelatedMetadataElementStub stub = new RelatedMetadataElementStub(relatedMetadataElement);

                    stubs.add(stub);
                }
            }

            return  stubs;
        }

        return null;
    }



    /**
     * Return the informal tags attached to an element.
     *
     * @param userId       userId of user making request.
     * @param elementGUID    unique identifier for the element that the comments are connected to (maybe a comment too).
     * @param queryOptions multiple options to control the query
     * @return list of tags
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem updating the element properties in the property server.
     * @throws UserNotAuthorizedException the user does not have permission to perform this request.
     */
    public List<InformalTagElement>  getAttachedTags(String       userId,
                                                     String       elementGUID,
                                                     QueryOptions queryOptions) throws InvalidParameterException,
                                                                                       PropertyServerException,
                                                                                       UserNotAuthorizedException
    {
        final String methodName = "getAttachedTags";

        RelatedMetadataElementList relatedMetadataElements = openMetadataClient.getRelatedMetadataElements(userId,
                                                                                                           elementGUID,
                                                                                                           1,
                                                                                                           OpenMetadataType.ATTACHED_TAG_RELATIONSHIP.typeName,
                                                                                                           queryOptions);
        return this.getTagsFromRelatedMetadataElement(relatedMetadataElements, methodName);
    }


    /**
     * Convert the GAF beans into feedback beans.
     *
     * @param relatedMetadataElements elements retrieved from the repository
     * @param methodName calling method
     * @return feedback beans
     * @throws PropertyServerException error formatting bean
     */
    private List<InformalTagElement> getTagsFromRelatedMetadataElement(RelatedMetadataElementList relatedMetadataElements,
                                                                       String                     methodName) throws PropertyServerException
    {
        if ((relatedMetadataElements != null) && (relatedMetadataElements.getElementList() != null))
        {
            List<InformalTagElement> results = new ArrayList<>();

            for (RelatedMetadataElement relatedMetadataElement : relatedMetadataElements.getElementList())
            {
                if (relatedMetadataElement != null)
                {
                    results.add(tagConverter.getNewBean(InformalTagElement.class,
                                                        relatedMetadataElement.getElement(),
                                                        methodName));
                }
            }

            return results;
        }

        return null;
    }


    /* =====================================================================================================================
     * A note log maintains an ordered list of notes.  It can be used to support release note, blogs and similar
     * broadcast information.  Note logs are typically maintained by the owners/stewards of an element.
     */

    /**
     * Create a new metadata element to represent a note log and attach it to an element (if supplied).
     * Any supplied element becomes the note log's anchor, causing the note log to be deleted if/when the element is deleted.
     *
     * @param userId calling user
     * @param elementGUID unique identifier of the element where the note log is located
     * @param metadataSourceOptions  options to control access to open metadata
     * @param initialClassifications map of classification names to classification properties to include in the entity creation request
     * @param noteLogProperties properties about the note log to store
     *
     * @return unique identifier of the new note log
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public  String createNoteLog(String                                userId,
                                 String                                elementGUID,
                                 MetadataSourceOptions                 metadataSourceOptions,
                                 Map<String, ClassificationProperties> initialClassifications,
                                 NoteLogProperties                     noteLogProperties) throws InvalidParameterException,
                                                                                                 UserNotAuthorizedException,
                                                                                                 PropertyServerException
    {
        final String methodName = "createNoteLog";
        final String guidParameterName = "elementGUID";

        propertyHelper.validateGUID(elementGUID, guidParameterName, methodName);

        NewElementOptions newElementOptions = new NewElementOptions(metadataSourceOptions);

        newElementOptions.setInitialStatus(ElementStatus.ACTIVE);

        if (elementGUID != null)
        {
            newElementOptions.setIsOwnAnchor(false);
            newElementOptions.setAnchorGUID(elementGUID);
            newElementOptions.setParentGUID(elementGUID);
            newElementOptions.setParentAtEnd1(true);
            newElementOptions.setParentRelationshipTypeName(OpenMetadataType.ATTACHED_NOTE_LOG_RELATIONSHIP.typeName);
        }
        else
        {
            newElementOptions.setIsOwnAnchor(true);
        }

        return openMetadataClient.createMetadataElementInStore(userId,
                                                               OpenMetadataType.NOTE_LOG.typeName,
                                                               newElementOptions,
                                                               classificationBuilder.getInitialClassifications(initialClassifications),
                                                               elementBuilder.getNewElementProperties(noteLogProperties),
                                                               null);
    }


    /**
     * Update the metadata element representing a note log.
     *
     * @param userId calling user
     * @param noteLogGUID unique identifier of the metadata element to update
     * @param updateOptions provides a structure for the additional options when updating an element.
     * @param noteLogProperties new properties for the metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateNoteLog(String            userId,
                              String            noteLogGUID,
                              UpdateOptions     updateOptions,
                              NoteLogProperties noteLogProperties) throws InvalidParameterException,
                                                                          UserNotAuthorizedException,
                                                                          PropertyServerException
    {
        openMetadataClient.updateMetadataElementInStore(userId,
                                                        noteLogGUID,
                                                        updateOptions,
                                                        elementBuilder.getElementProperties(noteLogProperties));
    }


    /**
     * Remove the metadata element representing a note log.
     *
     * @param userId calling user
     * @param noteLogGUID unique identifier of the metadata element to remove
     * @param deleteOptions options for a delete request
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeNoteLog(String        userId,
                              String        noteLogGUID,
                              DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                  UserNotAuthorizedException, PropertyServerException
    {
        openMetadataClient.deleteMetadataElementInStore(userId, noteLogGUID, deleteOptions);
    }


    /**
     * Retrieve the list of note log metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param userId calling user
     * @param searchString string to find in the properties
     * @param suppliedSearchOptions multiple options to control the query
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<NoteLogElement>   findNoteLogs(String        userId,
                                               String        searchString,
                                               SearchOptions suppliedSearchOptions) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException
    {
        final String methodName = "findNoteLogs";

        SearchOptions searchOptions = new SearchOptions(suppliedSearchOptions);

        if (searchOptions.getMetadataElementTypeName() == null)
        {
            searchOptions.setMetadataElementTypeName(OpenMetadataType.NOTE_LOG.typeName);
        }

        List<OpenMetadataElement> openMetadataElements = openMetadataClient.findMetadataElementsWithString(userId,
                                                                                                           searchString,
                                                                                                           searchOptions);
        return this.getNoteLogsFromOpenMetadataElement(openMetadataElements, methodName);
    }


    /**
     * Convert the GAF beans into feedback beans.
     *
     * @param openMetadataElements elements retrieved from the repository
     * @param methodName calling method
     * @return feedback beans
     * @throws PropertyServerException error formatting bean
     */
    private List<NoteLogElement> getNoteLogsFromOpenMetadataElement(List<OpenMetadataElement> openMetadataElements,
                                                                    String                    methodName) throws PropertyServerException
    {
        if (openMetadataElements != null)
        {
            List<NoteLogElement> results = new ArrayList<>();

            for (OpenMetadataElement openMetadataElement : openMetadataElements)
            {
                if (openMetadataElement != null)
                {
                    results.add(noteLogConverter.getNewBean(NoteLogElement.class, openMetadataElement, methodName));
                }
            }

            return results;
        }

        return null;
    }


    /**
     * Convert the GAF beans into feedback beans.
     *
     * @param relatedMetadataElements elements retrieved from the repository
     * @param methodName calling method
     * @return feedback beans
     * @throws PropertyServerException error formatting bean
     */
    private List<NoteLogElement> getNoteLogsFromRelatedMetadataElements(RelatedMetadataElementList   relatedMetadataElements,
                                                                        String                       methodName) throws PropertyServerException
    {
        if ((relatedMetadataElements != null) && (relatedMetadataElements.getElementList() != null))
        {
            List<NoteLogElement> results = new ArrayList<>();

            for (RelatedMetadataElement relatedMetadataElement : relatedMetadataElements.getElementList())
            {
                if (relatedMetadataElement != null)
                {
                    results.add(noteLogConverter.getNewBean(NoteLogElement.class, relatedMetadataElement.getElement(), methodName));
                }
            }

            return results;
        }

        return null;
    }


    /**
     * Retrieve the list of note log metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param userId calling user
     * @param name name to search for
     * @param queryOptions multiple options to control the query

     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<NoteLogElement>   getNoteLogsByName(String       userId,
                                                    String       name,
                                                    QueryOptions queryOptions) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException
    {
        final String methodName = "getNoteLogsByName";
        final String nameParameterName = "name";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateMandatoryName(name, nameParameterName, methodName);
        propertyHelper.validatePaging(queryOptions, openMetadataClient.getMaxPagingSize(), methodName);

        List<String> propertyNames = Arrays.asList(OpenMetadataProperty.QUALIFIED_NAME.name,
                                                   OpenMetadataProperty.DISPLAY_NAME.name);

        List<OpenMetadataElement> openMetadataElements = openMetadataClient.findMetadataElements(userId,
                                                                                                 propertyHelper.getSearchPropertiesByName(propertyNames, name, PropertyComparisonOperator.EQ),
                                                                                                 null,
                                                                                                 queryOptions);

        if (openMetadataElements != null)
        {
            return this.getNoteLogsFromOpenMetadataElement(openMetadataElements, methodName);
        }

        return null;
    }


    /**
     * Retrieve the list of note log metadata elements attached to the element.
     *
     * @param userId calling user
     * @param elementGUID element to start from
     * @param queryOptions multiple options to control the query
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<NoteLogElement>   getNoteLogsForElement(String       userId,
                                                        String       elementGUID,
                                                        QueryOptions queryOptions) throws InvalidParameterException,
                                                                                                  UserNotAuthorizedException,
                                                                                                  PropertyServerException
    {
        final String methodName = "getNoteLogsForElement";

        RelatedMetadataElementList relatedMetadataElements = openMetadataClient.getRelatedMetadataElements(userId,
                                                                                                           elementGUID,
                                                                                                           1,
                                                                                                           OpenMetadataType.ATTACHED_NOTE_LOG_RELATIONSHIP.typeName,
                                                                                                           queryOptions);
        return this.getNoteLogsFromRelatedMetadataElements(relatedMetadataElements, methodName);
    }


    /**
     * Retrieve the note log metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param noteLogGUID unique identifier of the requested metadata element
     * @param getOptions multiple options to control the query
     *
     * @return requested metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public NoteLogElement getNoteLogByGUID(String     userId,
                                           String     noteLogGUID,
                                           GetOptions getOptions) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        final String methodName = "getNoteLogByGUID";

        OpenMetadataElement openMetadataElement = openMetadataClient.getMetadataElementByGUID(userId, noteLogGUID, getOptions);

        if (openMetadataElement != null)
        {
            return noteLogConverter.getNewBean(NoteLogElement.class, openMetadataElement, methodName);
        }

        return null;
    }


    /* ===============================================================================
     * A element typically contains many notes, linked with relationships.
     */

    /**
     * Create a new metadata element to represent a note.
     *
     * @param userId calling user
     * @param noteLogGUID unique identifier of the element where the note is located
     * @param metadataSourceOptions  options to control access to open metadata
     * @param initialClassifications map of classification names to classification properties to include in the entity creation request
     * @param noteProperties properties for the note
     *
     * @return unique identifier of the new metadata element for the note
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createNote(String                                userId,
                             String                                noteLogGUID,
                             MetadataSourceOptions                 metadataSourceOptions,
                             Map<String, ClassificationProperties> initialClassifications,
                             NoteProperties                        noteProperties) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException
    {
        final String methodName = "createNote";
        final String guidParameterName = "noteLogGUID";

        propertyHelper.validateGUID(noteLogGUID, guidParameterName, methodName);

        NewElementOptions newElementOptions = new NewElementOptions(metadataSourceOptions);

        newElementOptions.setInitialStatus(ElementStatus.ACTIVE);
        newElementOptions.setIsOwnAnchor(false);
        newElementOptions.setAnchorGUID(noteLogGUID);
        newElementOptions.setParentGUID(noteLogGUID);
        newElementOptions.setParentAtEnd1(true);
        newElementOptions.setParentRelationshipTypeName(OpenMetadataType.ATTACHED_NOTE_LOG_ENTRY_RELATIONSHIP.typeName);

        return openMetadataClient.createMetadataElementInStore(userId,
                                                               OpenMetadataType.NOTE_ENTRY.typeName,
                                                               newElementOptions,
                                                               classificationBuilder.getInitialClassifications(initialClassifications),
                                                               elementBuilder.getNewElementProperties(noteProperties),
                                                               null);

    }


    /**
     * Update the properties of the metadata element representing a note.
     *
     * @param userId calling user
     * @param noteGUID unique identifier of the note to update
     * @param updateOptions provides a structure for the additional options when updating an element.
     * @param noteProperties new properties for the note
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateNote(String         userId,
                           String         noteGUID,
                           UpdateOptions  updateOptions,
                           NoteProperties noteProperties) throws InvalidParameterException,
                                                                UserNotAuthorizedException,
                                                                PropertyServerException
    {
        openMetadataClient.updateMetadataElementInStore(userId,
                                                        noteGUID,
                                                        updateOptions,
                                                        elementBuilder.getNewElementProperties(noteProperties));
    }


    /**
     * Remove the metadata element representing a note.
     *
     * @param userId calling user
     * @param noteGUID unique identifier of the metadata element to remove
     * @param deleteOptions options for a delete request
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeNote(String        userId,
                           String        noteGUID,
                           DeleteOptions deleteOptions) throws InvalidParameterException,
                                                               UserNotAuthorizedException,
                                                               PropertyServerException
    {
        openMetadataClient.deleteMetadataElementInStore(userId, noteGUID, deleteOptions);
    }


    /**
     * Retrieve the list of note metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param userId calling user
     * @param searchString string to find in the properties
     * @param suppliedSearchOptions multiple options to control the query
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<NoteElement>   findNotes(String        userId,
                                         String        searchString,
                                         SearchOptions suppliedSearchOptions) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException
    {
        final String methodName = "findNotes";

        SearchOptions searchOptions = new SearchOptions(suppliedSearchOptions);

        if (searchOptions.getMetadataElementTypeName() == null)
        {
            searchOptions.setMetadataElementTypeName(OpenMetadataType.NOTE_ENTRY.typeName);
        }

        List<OpenMetadataElement> openMetadataElements = openMetadataClient.findMetadataElementsWithString(userId, searchString,
                                                                                                           searchOptions);
        return this.getNotesFromOpenMetadataElement(openMetadataElements, methodName);
    }


    /**
     * Convert the GAF beans into feedback beans.
     *
     * @param openMetadataElements elements retrieved from the repository
     * @param methodName calling method
     * @return feedback beans
     * @throws PropertyServerException error formatting bean
     */
    private List<NoteElement> getNotesFromOpenMetadataElement(List<OpenMetadataElement> openMetadataElements,
                                                              String                    methodName) throws PropertyServerException
    {
        if (openMetadataElements != null)
        {
            List<NoteElement> results = new ArrayList<>();

            for (OpenMetadataElement openMetadataElement : openMetadataElements)
            {
                if (openMetadataElement != null)
                {
                    results.add(noteConverter.getNewBean(NoteElement.class, openMetadataElement, methodName));
                }
            }

            return results;
        }

        return null;
    }


    /**
     * Convert the GAF beans into feedback beans.
     *
     * @param relatedMetadataElements elements retrieved from the repository
     * @param methodName calling method
     * @return feedback beans
     * @throws PropertyServerException error formatting bean
     */
    private List<NoteElement> getNotesFromRelatedMetadataElements(RelatedMetadataElementList relatedMetadataElements,
                                                                  String                     methodName) throws PropertyServerException
    {
        if ((relatedMetadataElements != null) && (relatedMetadataElements.getElementList() != null))
        {
            List<NoteElement> results = new ArrayList<>();

            for (RelatedMetadataElement relatedMetadataElement : relatedMetadataElements.getElementList())
            {
                if (relatedMetadataElement != null)
                {
                    results.add(noteConverter.getNewBean(NoteElement.class, relatedMetadataElement.getElement(), methodName));
                }
            }

            return results;
        }

        return null;
    }


    /**
     * Retrieve the list of notes associated with a note log.
     *
     * @param userId calling user
     * @param noteLogGUID unique identifier of the note log of interest
     * @param queryOptions multiple options to control the query
     *
     * @return list of associated metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<NoteElement>    getNotesForNoteLog(String       userId,
                                                   String       noteLogGUID,
                                                   QueryOptions queryOptions) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        final String methodName = "getNotesForNoteLog";

        RelatedMetadataElementList relatedMetadataElements = openMetadataClient.getRelatedMetadataElements(userId,
                                                                                                           noteLogGUID,
                                                                                                           1,
                                                                                                           OpenMetadataType.ATTACHED_NOTE_LOG_ENTRY_RELATIONSHIP.typeName,
                                                                                                           queryOptions);
        return this.getNotesFromRelatedMetadataElements(relatedMetadataElements, methodName);
    }


    /**
     * Retrieve the note metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param noteGUID unique identifier of the requested metadata element
     * @param getOptions multiple options to control the query
     *
     * @return matching metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public NoteElement getNoteByGUID(String     userId,
                                     String     noteGUID,
                                     GetOptions getOptions) throws InvalidParameterException,
                                                                   UserNotAuthorizedException,
                                                                   PropertyServerException
    {
        final String methodName = "getNoteByGUID";

        OpenMetadataElement openMetadataElement = openMetadataClient.getMetadataElementByGUID(userId, noteGUID, getOptions);

        if (openMetadataElement != null)
        {
            return noteConverter.getNewBean(NoteElement.class,
                                            openMetadataElement,
                                            methodName);
        }

        return null;
    }
}
