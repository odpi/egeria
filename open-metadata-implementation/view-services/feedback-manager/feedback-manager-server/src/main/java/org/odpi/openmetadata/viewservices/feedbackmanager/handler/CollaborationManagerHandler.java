/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.viewservices.feedbackmanager.handler;

import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementStatus;
import org.odpi.openmetadata.frameworks.governanceaction.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.governanceaction.properties.RelatedMetadataElement;
import org.odpi.openmetadata.frameworks.governanceaction.properties.RelatedMetadataElementStub;
import org.odpi.openmetadata.frameworks.governanceaction.properties.RelatedMetadataElements;
import org.odpi.openmetadata.frameworks.governanceaction.search.ElementProperties;
import org.odpi.openmetadata.frameworks.governanceaction.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.openmetadata.enums.StarRating;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.viewservices.feedbackmanager.converters.*;
import org.odpi.openmetadata.viewservices.feedbackmanager.metadataelements.*;
import org.odpi.openmetadata.viewservices.feedbackmanager.properties.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * CollaborationManagerHandler is the handler for managing comments, ratings, likes and tags.
 */
public class CollaborationManagerHandler
{
    private final OpenMetadataStoreHandler client;
    private final InvalidParameterHandler  invalidParameterHandler = new InvalidParameterHandler();
    private final AuditLog                 auditLog;

    private final PropertyHelper propertyHelper = new PropertyHelper();

    private final CommentConverter<CommentElement>         commentConverter;
    private final InformalTagConverter<InformalTagElement> tagConverter;
    private final LikeConverter<LikeElement>               likeConverter;
    private final NoteLogConverter<NoteLogElement>         noteLogConverter;
    private final NoteConverter<NoteElement>               noteConverter;
    private final RatingConverter<RatingElement>           ratingConverter;


    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param accessServiceURLMarker optional access service URL marker used to identify which back end service to call
     * @param auditLog logging destination
     * @param maxPageSize maximum value allowed for page size
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public CollaborationManagerHandler(String   localServerName,
                                       String   serverName,
                                       String   serverPlatformURLRoot,
                                       AuditLog auditLog,
                                       String   accessServiceURLMarker,
                                       int      maxPageSize) throws InvalidParameterException
    {
        this.client = new OpenMetadataStoreHandler(serverName, serverPlatformURLRoot, accessServiceURLMarker, maxPageSize);
        this.auditLog = auditLog;

        String serviceName = ViewServiceDescription.FEEDBACK_MANAGER.getViewServiceFullName();

        this.commentConverter = new CommentConverter<>(propertyHelper, serviceName, localServerName);
        this.tagConverter     = new InformalTagConverter<>(propertyHelper, serviceName, localServerName);
        this.likeConverter    = new LikeConverter<>(propertyHelper, serviceName, localServerName);
        this.noteLogConverter = new NoteLogConverter<>(propertyHelper, serviceName, localServerName);
        this.noteConverter    = new NoteConverter<>(propertyHelper, serviceName, localServerName);
        this.ratingConverter  = new RatingConverter<>(propertyHelper, serviceName, localServerName);
    }



    /**
     * Adds a star rating and optional review text to the element.  If the user has already attached
     * a rating then the original one is over-ridden.
     *
     * @param userId      userId of user making request
     * @param elementGUID   unique identifier for the element
     * @param elementTypeName type of feedback element
     * @param relationshipTypeName type of attaching relationship
     * @param isPublic is this visible to other people
     * @param elementProperties  properties of the feedback
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the element properties to the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    private void addFeedbackToElement(String            userId,
                                      String            elementGUID,
                                      String            elementTypeName,
                                      String            relationshipTypeName,
                                      boolean           isPublic,
                                      ElementProperties elementProperties) throws InvalidParameterException,
                                                                                  PropertyServerException,
                                                                                  UserNotAuthorizedException
    {
        RelatedMetadataElement existingFeedback = this.getFeedbackForUser(userId, elementGUID, relationshipTypeName);

        ElementProperties relationshipProperties = propertyHelper.addBooleanProperty(null,
                                                                                     OpenMetadataProperty.IS_PUBLIC.name,
                                                                                     isPublic);

        if (existingFeedback == null)
        {
            client.createMetadataElementInStore(userId,
                                                elementTypeName,
                                                ElementStatus.ACTIVE,
                                                null,
                                                elementGUID,
                                                false,
                                                null,
                                                null,
                                                elementProperties,
                                                elementGUID,
                                                relationshipTypeName,
                                                relationshipProperties,
                                                true);
        }
        else
        {
            client.updateMetadataElementInStore(userId,
                                                existingFeedback.getElement().getElementGUID(),
                                                false,
                                                false,
                                                false,
                                                elementProperties,
                                                new Date());

            client.updateRelatedElementsInStore(userId,
                                                existingFeedback.getRelationshipGUID(),
                                                false,
                                                false,
                                                false,
                                                relationshipProperties,
                                                new Date());
        }
    }


    /**
     * Retrieve any rating previously added by this user.
     *
     * @param userId      userId of user making request.
     * @param elementGUID   unique identifier for the element.
     * @param relationshipTypeName name of the relationship type
     * @return relationship and entity
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the element properties to the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    private RelatedMetadataElement getFeedbackForUser(String userId,
                                                      String elementGUID,
                                                      String relationshipTypeName) throws InvalidParameterException,
                                                                                          PropertyServerException,
                                                                                          UserNotAuthorizedException
    {
        int startFrom = 0;
        List<RelatedMetadataElement> attachedFeedbacks = client.getRelatedMetadataElements(userId,
                                                                                           elementGUID,
                                                                                           1,
                                                                                           relationshipTypeName,
                                                                                           false,
                                                                                           false,
                                                                                           new Date(),
                                                                                           startFrom,
                                                                                           invalidParameterHandler.getMaxPagingSize());

        RelatedMetadataElement existingFeedback = null;

        while ((existingFeedback == null) && (attachedFeedbacks != null))
        {
            for (RelatedMetadataElement attachedFeedback : attachedFeedbacks)
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

            startFrom = startFrom + invalidParameterHandler.getMaxPagingSize();
            attachedFeedbacks = client.getRelatedMetadataElements(userId,
                                                                  elementGUID,
                                                                  1,
                                                                  relationshipTypeName,
                                                                  false,
                                                                  false,
                                                                  new Date(),
                                                                  startFrom,
                                                                  invalidParameterHandler.getMaxPagingSize());
        }

        return existingFeedback;
    }


    /**
     * Adds a star rating and optional review text to the element.  If the user has already attached
     * a rating then the original one is over-ridden.
     *
     * @param userId      userId of user making request.
     * @param elementGUID   unique identifier for the element.
     * @param isPublic is this visible to other people
     * @param properties  properties of the rating
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the element properties to the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void addRatingToElement(String           userId,
                                   String           elementGUID,
                                   boolean          isPublic,
                                   RatingProperties properties) throws InvalidParameterException,
                                                                       PropertyServerException,
                                                                       UserNotAuthorizedException
    {
        final String methodName = "addRatingToElement";
        final String propertiesParameterName = "properties";
        final String starRatingParameterName = "properties.starRating";

        invalidParameterHandler.validateObject(properties, propertiesParameterName, methodName);
        invalidParameterHandler.validateObject(properties.getStarRating(), starRatingParameterName, methodName);

        ElementProperties elementProperties = propertyHelper.addStringProperty(null,
                                                                               OpenMetadataProperty.REVIEW.name,
                                                                               properties.getReview());

        if (properties.getStarRating() != null)
        {
            elementProperties = propertyHelper.addEnumProperty(elementProperties,
                                                               OpenMetadataProperty.STARS.name,
                                                               StarRating.getOpenTypeName(),
                                                               properties.getStarRating().getName());
        }

        this.addFeedbackToElement(userId,
                                  elementGUID,
                                  OpenMetadataType.RATING.typeName,
                                  OpenMetadataType.ATTACHED_RATING_RELATIONSHIP.typeName,
                                  isPublic,
                                  elementProperties);
    }


    /**
     * Removes of a review that was added to the element by this user.
     *
     * @param userId      userId of user making request.
     * @param elementGUID  unique identifier for the attached element.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem updating the element properties in the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void removeRatingFromElement(String userId,
                                        String elementGUID) throws InvalidParameterException,
                                                                   PropertyServerException,
                                                                   UserNotAuthorizedException
    {
        RelatedMetadataElement existingRating = this.getFeedbackForUser(userId,
                                                                        elementGUID,
                                                                        OpenMetadataType.ATTACHED_RATING_RELATIONSHIP.typeName);

        if (existingRating != null)
        {
            client.deleteMetadataElementInStore(userId,
                                                existingRating.getElement().getElementGUID(),
                                                false,
                                                false,
                                                new Date());
        }
    }


    /**
     * Adds a "Like" to the element.
     *
     * @param userId      userId of user making request
     * @param elementGUID   unique identifier for the element
     * @param isPublic is this visible to other people
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the element properties to the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void addLikeToElement(String         userId,
                                 String         elementGUID,
                                 boolean        isPublic) throws InvalidParameterException,
                                                                 PropertyServerException,
                                                                 UserNotAuthorizedException
    {
        addFeedbackToElement(userId,
                             elementGUID,
                             OpenMetadataType.LIKE.typeName,
                             OpenMetadataType.ATTACHED_LIKE_RELATIONSHIP.typeName,
                             isPublic,
                             null);
    }


    /**
     * Removes a "Like" added to the element by this user.
     *
     * @param userId   userId of user making request.
     * @param elementGUID unique identifier for the like object.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem updating the element properties in the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void removeLikeFromElement(String userId,
                                      String elementGUID) throws InvalidParameterException,
                                                                 PropertyServerException,
                                                                 UserNotAuthorizedException
    {
        RelatedMetadataElement existingLike = this.getFeedbackForUser(userId,
                                                                      elementGUID,
                                                                      OpenMetadataType.ATTACHED_LIKE_RELATIONSHIP.typeName);
        if (existingLike != null)
        {
            client.deleteMetadataElementInStore(userId,
                                                existingLike.getElement().getElementGUID(),
                                                false,
                                                false,
                                                new Date());
        }
    }


    /**
     * Adds a comment to the element.
     *
     * @param userId        userId of user making request.
     * @param elementGUID     unique identifier for the element.
     * @param isPublic is this comment visible to other people.
     * @param properties   properties of the comment
     *
     * @return guid of new comment.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the element properties to the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String addCommentToElement(String                       userId,
                                      String                       elementGUID,
                                      boolean                      isPublic,
                                      CommentProperties            properties) throws InvalidParameterException,
                                                                                      PropertyServerException,
                                                                                      UserNotAuthorizedException
    {
        final String methodName = "addCommentToElement";

        ElementProperties elementProperties = this.getElementPropertiesForComment(properties, methodName);

        ElementProperties relationshipProperties = propertyHelper.addBooleanProperty(null,
                                                                                     OpenMetadataProperty.IS_PUBLIC.name,
                                                                                     isPublic);
        return client.createMetadataElementInStore(userId,
                                                   OpenMetadataType.COMMENT.typeName,
                                                   ElementStatus.ACTIVE,
                                                   null,
                                                   elementGUID,
                                                   false,
                                                   properties.getEffectiveFrom(),
                                                   properties.getEffectiveTo(),
                                                   elementProperties,
                                                   elementGUID,
                                                   OpenMetadataType.ATTACHED_COMMENT_RELATIONSHIP.typeName,
                                                   relationshipProperties,
                                                   true);
    }


    /**
     * Validate comment properties and construct an element properties object.
     *
     * @param properties comment properties from caller
     * @param methodName calling method
     * @return element properties
     * @throws InvalidParameterException properties from caller are invalid
     */
    private ElementProperties getElementPropertiesForComment(CommentProperties properties,
                                                             String            methodName) throws InvalidParameterException
    {
        final String propertiesParameterName = "properties";
        final String commentTextParameterName = "properties.commentText";

        invalidParameterHandler.validateObject(properties, propertiesParameterName, methodName);
        invalidParameterHandler.validateObject(properties.getCommentText(), commentTextParameterName, methodName);

        ElementProperties elementProperties = propertyHelper.addStringProperty(null,
                                                                               OpenMetadataProperty.TEXT.name,
                                                                               properties.getCommentText());


        if (properties.getCommentType() != null)
        {
            elementProperties = propertyHelper.addEnumProperty(elementProperties,
                                                               OpenMetadataProperty.COMMENT_TYPE.name,
                                                               StarRating.getOpenTypeName(),
                                                               properties.getCommentType().getName());
        }

        return elementProperties;
    }


    /**
     * Adds a comment to the element.
     *
     * @param userId        userId of user making request.
     * @param commentGUID     unique identifier for the comment to attach to.
     * @param elementGUID     unique identifier for the anchor element.
     * @param isPublic is this comment visible to other people.
     * @param properties   properties of the comment
     *
     * @return guid of new comment.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the element properties to the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String addCommentReply(String                       userId,
                                  String                       elementGUID,
                                  String                       commentGUID,
                                  boolean                      isPublic,
                                  CommentProperties            properties) throws InvalidParameterException,
                                                                                  PropertyServerException,
                                                                                  UserNotAuthorizedException
    {
        final String methodName = "addCommentToElement";

        ElementProperties elementProperties = this.getElementPropertiesForComment(properties, methodName);

        ElementProperties relationshipProperties = propertyHelper.addBooleanProperty(null,
                                                                                     OpenMetadataProperty.IS_PUBLIC.name,
                                                                                     isPublic);

        return client.createMetadataElementInStore(userId,
                                                   OpenMetadataType.COMMENT.typeName,
                                                   ElementStatus.ACTIVE,
                                                   null,
                                                   elementGUID,
                                                   false,
                                                   properties.getEffectiveFrom(),
                                                   properties.getEffectiveTo(),
                                                   elementProperties,
                                                   commentGUID,
                                                   OpenMetadataType.ATTACHED_COMMENT_RELATIONSHIP.typeName,
                                                   relationshipProperties,
                                                   true);
    }


    /**
     * Update an existing comment.
     *
     * @param userId        userId of user making request.
     * @param commentGUID   unique identifier for the comment to change.
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param properties   properties of the comment
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the element properties to the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void   updateComment(String            userId,
                                String            commentGUID,
                                boolean           isMergeUpdate,
                                CommentProperties properties) throws InvalidParameterException,
                                                                     PropertyServerException,
                                                                     UserNotAuthorizedException
    {
        final String methodName = "updateComment";

        ElementProperties elementProperties = this.getElementPropertiesForComment(properties, methodName);

        client.updateMetadataElementInStore(userId,
                                            commentGUID,
                                            ! isMergeUpdate,
                                            false,
                                            false,
                                            elementProperties,
                                            new Date());
    }


    /**
     * Update an existing comment's visibility.
     *
     * @param userId        userId of user making request.
     * @param parentGUID   unique identifier for the element that the comment is attached to.
     * @param commentGUID   unique identifier for the comment to change.
     * @param isPublic      is this visible to other people
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the element properties to the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void   updateCommentVisibility(String            userId,
                                          String            parentGUID,
                                          String            commentGUID,
                                          boolean           isPublic) throws InvalidParameterException,
                                                                             PropertyServerException,
                                                                             UserNotAuthorizedException
    {
        ElementProperties relationshipProperties = propertyHelper.addBooleanProperty(null,
                                                                                     OpenMetadataProperty.IS_PUBLIC.name,
                                                                                     isPublic);

        List<RelatedMetadataElements> relationships = client.getMetadataElementRelationships(userId,
                                                                                             parentGUID,
                                                                                             commentGUID,
                                                                                             OpenMetadataType.ATTACHED_COMMENT_RELATIONSHIP.typeName,
                                                                                             false,
                                                                                             false,
                                                                                             null,
                                                                                             0,
                                                                                             0);

        if (relationships != null)
        {
            for (RelatedMetadataElements relationship : relationships)
            {
                if (relationship != null)
                {
                    client.updateRelatedElementsInStore(userId,
                                                        relationship.getRelationshipGUID(),
                                                        false,
                                                        false,
                                                        false,
                                                        relationshipProperties,
                                                        new Date());

                    break;
                }
            }
        }
    }


    /**
     * Link a comment that contains the best answer to a question posed in another comment.
     *
     * @param userId calling user
     * @param questionCommentGUID unique identifier of the comment containing the question
     * @param answerCommentGUID unique identifier of the comment containing the accepted answer
     * @param isPublic      is this visible to other people
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setupAcceptedAnswer(String             userId,
                                    String             questionCommentGUID,
                                    String             answerCommentGUID,
                                    boolean            isPublic) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException
    {
        ElementProperties relationshipProperties = propertyHelper.addBooleanProperty(null,
                                                                                     OpenMetadataProperty.IS_PUBLIC.name,
                                                                                     isPublic);

        client.createRelatedElementsInStore(userId,
                                            OpenMetadataType.ACCEPTED_ANSWER_RELATIONSHIP.typeName,
                                            questionCommentGUID,
                                            answerCommentGUID,
                                            false,
                                            false,
                                            null,
                                            null,
                                            relationshipProperties,
                                            new Date());
    }


    /**
     * Unlink a comment that contains an answer to a question posed in another comment.
     *
     * @param userId calling user
     * @param questionCommentGUID unique identifier of the comment containing the question
     * @param answerCommentGUID unique identifier of the comment containing the accepted answer
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearAcceptedAnswer(String  userId,
                                    String  questionCommentGUID,
                                    String  answerCommentGUID) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException
    {
        List<RelatedMetadataElements> relationships = client.getMetadataElementRelationships(userId,
                                                                                             questionCommentGUID,
                                                                                             answerCommentGUID,
                                                                                             OpenMetadataType.ACCEPTED_ANSWER_RELATIONSHIP.typeName,
                                                                                             false,
                                                                                             false,
                                                                                             null,
                                                                                             0,
                                                                                             0);

        if (relationships != null)
        {
            for (RelatedMetadataElements relationship : relationships)
            {
                if (relationship != null)
                {
                    client.deleteRelatedElementsInStore(userId,
                                                        relationship.getRelationshipGUID(),
                                                        false,
                                                        false,
                                                        new Date());

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
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem updating the element properties in the property server.
     * @throws UserNotAuthorizedException the user does not have permission to perform this request.
     */
    public void removeComment(String            userId,
                              String            commentGUID) throws InvalidParameterException,
                                                                    PropertyServerException,
                                                                    UserNotAuthorizedException
    {
        client.deleteMetadataElementInStore(userId, commentGUID, false, false, new Date());
    }


    /**
     * Return the requested comment.
     *
     * @param userId       userId of user making request.
     * @param commentGUID  unique identifier for the comment object.
     * @return comment properties
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem updating the element properties in the property server.
     * @throws UserNotAuthorizedException the user does not have permission to perform this request.
     */
    public CommentElement getComment(String  userId,
                                     String  commentGUID) throws InvalidParameterException,
                                                                 PropertyServerException,
                                                                 UserNotAuthorizedException
    {
        final String methodName = "getComment";

        OpenMetadataElement commentElement = client.getMetadataElementByGUID(userId,
                                                                             commentGUID,
                                                                             false,
                                                                             false,
                                                                             new Date());

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
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @return list of comments
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem updating the element properties in the property server.
     * @throws UserNotAuthorizedException the user does not have permission to perform this request.
     */
    public List<CommentElement>  getAttachedComments(String  userId,
                                                     String  elementGUID,
                                                     int     startFrom,
                                                     int     pageSize) throws InvalidParameterException,
                                                                              PropertyServerException,
                                                                              UserNotAuthorizedException
    {
        final String methodName = "getAttachedComments";

        List<RelatedMetadataElement> relatedMetadataElements = client.getRelatedMetadataElements(userId,
                                                                                                 elementGUID,
                                                                                                 1,
                                                                                                 OpenMetadataType.ATTACHED_COMMENT_RELATIONSHIP.typeName,
                                                                                                 false,
                                                                                                 false,
                                                                                                 new Date(),
                                                                                                 startFrom,
                                                                                                 pageSize);
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
    private List<CommentElement> getCommentsFromRelatedMetadataElement(List<RelatedMetadataElement> relatedMetadataElements,
                                                                       String                       methodName) throws PropertyServerException
    {
        if (relatedMetadataElements != null)
        {
            List<CommentElement> results = new ArrayList<>();

            for (RelatedMetadataElement relatedMetadataElement : relatedMetadataElements)
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
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<CommentElement> findComments(String  userId,
                                             String  searchString,
                                             int     startFrom,
                                             int     pageSize) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException
    {
        final String methodName = "findComments";

        List<OpenMetadataElement> openMetadataElements = client.findMetadataElementsWithString(userId,
                                                                                               searchString,
                                                                                               OpenMetadataType.COMMENT.typeName,
                                                                                               false,
                                                                                               false,
                                                                                               new Date(),
                                                                                               startFrom,
                                                                                               pageSize);
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
     * @param properties       name of the tag and (optional) description.  Setting a description, particularly in a public tag
     * makes the tag more valuable to other users and can act as an embryonic note.
     *
     * @return GUID for new tag.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the element properties to the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String createInformalTag(String        userId,
                                    TagProperties properties) throws InvalidParameterException,
                                                                     PropertyServerException,
                                                                     UserNotAuthorizedException
    {
        final String methodName = "createInformalTag";
        final String propertiesParameterName = "properties";
        final String nameParameterName = "properties.name";

        invalidParameterHandler.validateObject(properties, propertiesParameterName, methodName);
        invalidParameterHandler.validateName(properties.getName(), nameParameterName, methodName);

        ElementProperties elementProperties = propertyHelper.addStringProperty(null,
                                                                               OpenMetadataProperty.TAG_NAME.name,
                                                                               properties.getName());

        elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                             OpenMetadataProperty.TAG_DESCRIPTION.name,
                                                             properties.getDescription());

        elementProperties = propertyHelper.addBooleanProperty(elementProperties,
                                                              OpenMetadataProperty.IS_PUBLIC.name,
                                                              ! properties.getIsPrivateTag());

        return client.createMetadataElementInStore(userId,
                                                   OpenMetadataType.INFORMAL_TAG.typeName,
                                                   ElementStatus.ACTIVE,
                                                   null,
                                                   null,
                                                   true,
                                                   null,
                                                   null,
                                                   elementProperties,
                                                   null,
                                                   null,
                                                   null,
                                                   true);
    }


    /**
     * Updates the description of an existing tag (either private or public).
     *
     * @param userId          userId of user making request.
     * @param tagGUID         unique identifier for the tag.
     * @param tagDescription  description of the tag.  Setting a description, particularly in a public tag
     *                        makes the tag more valuable to other users and can act as an embryonic glossary term.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the element properties to the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void   updateTagDescription(String userId,
                                       String tagGUID,
                                       String tagDescription) throws InvalidParameterException,
                                                                     PropertyServerException,
                                                                     UserNotAuthorizedException
    {
        client.updateMetadataElementInStore(userId,
                                            tagGUID,
                                            false,
                                            false,
                                            false,
                                            propertyHelper.addStringProperty(null,
                                                                             OpenMetadataProperty.TAG_DESCRIPTION.name,
                                                                             tagDescription),
                                            new Date());
    }


    /**
     * Removes a tag from the repository.
     * A private tag can be deleted by its creator and all the references are lost;
     * a public tag can be deleted by anyone, but only if it is not attached to any referenceable.
     *
     * @param userId    userId of user making request.
     * @param tagGUID   unique id for the tag.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem updating the element properties in the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void   deleteTag(String userId,
                            String tagGUID) throws InvalidParameterException,
                                                   PropertyServerException,
                                                   UserNotAuthorizedException
    {
        client.deleteMetadataElementInStore(userId, tagGUID, false, false, new Date());
    }


    /**
     * Return the tag for the supplied unique identifier (guid).
     *
     * @param userId userId of the user making the request.
     * @param tagGUID unique identifier of the tag.
     *
     * @return tag
     * @throws InvalidParameterException the userId is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public InformalTagElement getTag(String userId,
                                     String tagGUID) throws InvalidParameterException,
                                                            PropertyServerException,
                                                            UserNotAuthorizedException
    {
        final String methodName = "getTag";

        OpenMetadataElement openMetadataElement = client.getMetadataElementByGUID(userId,
                                                                                  tagGUID,
                                                                                  false,
                                                                                  false,
                                                                                  new Date());

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
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return tag list
     * @throws InvalidParameterException the userId is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<InformalTagElement> getTagsByName(String userId,
                                                  String tag,
                                                  int    startFrom,
                                                  int    pageSize) throws InvalidParameterException,
                                                                          PropertyServerException,
                                                                          UserNotAuthorizedException
    {
        return null;
    }


    /**
     * Return the list of the calling user's private tags exactly matching the supplied name.
     *
     * @param userId the name of the calling user.
     * @param tag name of tag.
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return tag list
     * @throws InvalidParameterException the userId is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<InformalTagElement> getMyTagsByName(String userId,
                                                    String tag,
                                                    int    startFrom,
                                                    int    pageSize) throws InvalidParameterException,
                                                                            PropertyServerException,
                                                                            UserNotAuthorizedException
    {
        return null;
    }


    /**
     * Return the list of tags containing the supplied string in either the name or description.
     *
     * @param userId the name of the calling user.
     * @param tag name of tag.  This may include wild card characters.
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return tag list
     * @throws InvalidParameterException the userId is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<InformalTagElement> findTags(String userId,
                                             String tag,
                                             int    startFrom,
                                             int    pageSize) throws InvalidParameterException,
                                                                     PropertyServerException,
                                                                     UserNotAuthorizedException
    {
        return null;
    }


    /**
     * Return the list of the calling user's private tags containing the supplied string in either the name or description.
     *
     * @param userId the name of the calling user.
     * @param tag name of tag.  This may include wild card characters.
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return tag list
     * @throws InvalidParameterException the userId is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<InformalTagElement> findMyTags(String userId,
                                               String tag,
                                               int    startFrom,
                                               int    pageSize) throws InvalidParameterException,
                                                                       PropertyServerException,
                                                                       UserNotAuthorizedException
    {
        return null;
    }


    /**
     * Adds a tag (either private of public) to an element.
     *
     * @param userId           userId of user making request.
     * @param elementGUID        unique id for the element.
     * @param tagGUID          unique id of the tag.
     * @param isPublic         flag indicating whether the attachment of the tag is public or not
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the element properties to the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void   addTagToElement(String  userId,
                                  String  elementGUID,
                                  String  tagGUID,
                                  boolean isPublic) throws InvalidParameterException,
                                                           PropertyServerException,
                                                           UserNotAuthorizedException
    {
    }


    /**
     * Removes a tag from the element that was added by this user.
     *
     * @param userId    userId of user making request.
     * @param elementGUID unique id for the element.
     * @param tagGUID   unique id for the tag.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem updating the element properties in the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void   removeTagFromElement(String userId,
                                       String elementGUID,
                                       String tagGUID) throws InvalidParameterException,
                                                              PropertyServerException,
                                                              UserNotAuthorizedException
    {
    }


    /**
     * Return the list of unique identifiers for elements that are linked to a specific tag either directly, or via one
     * of its schema elements.  An Element's GUID may appear multiple times in the results if it is tagged multiple times
     * with the requested tag.
     *
     * @param userId the name of the calling user.
     * @param tagGUID unique identifier of tag.
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return element guid list
     * @throws InvalidParameterException the userId is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<RelatedMetadataElementStub> getElementsByTag(String userId,
                                                             String tagGUID,
                                                             int    startFrom,
                                                             int    pageSize) throws InvalidParameterException,
                                                                                     PropertyServerException,
                                                                                     UserNotAuthorizedException
    {
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
     * @param noteLogProperties properties about the note log to store
     * @param isPublic                 is this element visible to other people.
     *
     * @return unique identifier of the new note log
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public  String createNoteLog(String            userId,
                                 String            elementGUID,
                                 NoteLogProperties noteLogProperties,
                                 boolean           isPublic) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException
    {
        return null;
    }


    /**
     * Update the metadata element representing a note log.
     *
     * @param userId calling user
     * @param noteLogGUID unique identifier of the metadata element to update
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param isPublic                 is this element visible to other people.
     * @param noteLogProperties new properties for the metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateNoteLog(String            userId,
                              String            noteLogGUID,
                              boolean           isMergeUpdate,
                              boolean           isPublic,
                              NoteLogProperties noteLogProperties) throws InvalidParameterException,
                                                                          UserNotAuthorizedException,
                                                                          PropertyServerException
    {
    }


    /**
     * Remove the metadata element representing a note log.
     *
     * @param userId calling user
     * @param noteLogGUID unique identifier of the metadata element to remove
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeNoteLog(String  userId,
                              String  noteLogGUID) throws InvalidParameterException,
                                                          UserNotAuthorizedException,
                                                          PropertyServerException
    {
        client.deleteMetadataElementInStore(userId, noteLogGUID, false, false, new Date());
    }


    /**
     * Retrieve the list of note log metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param userId calling user
     * @param searchString string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<NoteLogElement>   findNoteLogs(String  userId,
                                               String  searchString,
                                               int     startFrom,
                                               int     pageSize) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException
    {
        return null;
    }


    /**
     * Retrieve the list of note log metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param userId calling user
     * @param name name to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<NoteLogElement>   getNoteLogsByName(String  userId,
                                                    String  name,
                                                    int     startFrom,
                                                    int     pageSize) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException
    {
        return null;
    }


    /**
     * Retrieve the list of note log metadata elements attached to the element.
     *
     * @param userId calling user
     * @param elementGUID element to start from
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<NoteLogElement>   getNoteLogsForElement(String  userId,
                                                        String  elementGUID,
                                                        int     startFrom,
                                                        int     pageSize) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException
    {
        return  null;
    }


    /**
     * Retrieve the note log metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param noteLogGUID unique identifier of the requested metadata element
     *
     * @return requested metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public NoteLogElement getNoteLogByGUID(String  userId,
                                           String  noteLogGUID) throws InvalidParameterException,
                                                                       UserNotAuthorizedException,
                                                                       PropertyServerException
    {
        final String methodName = "getNoteLogByGUID";

        OpenMetadataElement openMetadataElement = client.getMetadataElementByGUID(userId,
                                                                                  noteLogGUID,
                                                                                  false,
                                                                                  false,
                                                                                  new Date());

        if (openMetadataElement != null)
        {
            return noteLogConverter.getNewBean(NoteLogElement.class,
                                               openMetadataElement,
                                               methodName);
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
     * @param noteProperties properties for the note
     *
     * @return unique identifier of the new metadata element for the note
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createNote(String          userId,
                             String          noteLogGUID,
                             NoteProperties  noteProperties) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException
    {
        return null;
    }


    /**
     * Update the properties of the metadata element representing a note.
     *
     * @param userId calling user
     * @param noteGUID unique identifier of the note to update
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param noteProperties new properties for the note
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateNote(String         userId,
                           String         noteGUID,
                           boolean        isMergeUpdate,
                           NoteProperties noteProperties) throws InvalidParameterException,
                                                                 UserNotAuthorizedException,
                                                                 PropertyServerException
    {
    }


    /**
     * Remove the metadata element representing a note.
     *
     * @param userId calling user
     * @param noteGUID unique identifier of the metadata element to remove
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeNote(String  userId,
                           String  noteGUID) throws InvalidParameterException,
                                                    UserNotAuthorizedException,
                                                    PropertyServerException
    {
        client.deleteMetadataElementInStore(userId, noteGUID, false, false, new Date());
    }


    /**
     * Retrieve the list of note metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param userId calling user
     * @param searchString string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<NoteElement>   findNotes(String  userId,
                                         String  searchString,
                                         int     startFrom,
                                         int     pageSize) throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException
    {
        return null;
    }


    /**
     * Retrieve the list of notes associated with a note log.
     *
     * @param userId calling user
     * @param noteLogGUID unique identifier of the note log of interest
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned

     *
     * @return list of associated metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<NoteElement>    getNotesForNoteLog(String  userId,
                                                   String  noteLogGUID,
                                                   int     startFrom,
                                                   int     pageSize) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException
    {
        return null;
    }


    /**
     * Retrieve the note metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param noteGUID unique identifier of the requested metadata element
     *
     * @return matching metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public NoteElement getNoteByGUID(String  userId,
                                     String  noteGUID) throws InvalidParameterException,
                                                              UserNotAuthorizedException,
                                                              PropertyServerException
    {
        final String methodName = "getNoteByGUID";

        OpenMetadataElement openMetadataElement = client.getMetadataElementByGUID(userId,
                                                                                  noteGUID,
                                                                                  false,
                                                                                  false,
                                                                                  new Date());

        if (openMetadataElement != null)
        {
            return noteConverter.getNewBean(NoteElement.class,
                                            openMetadataElement,
                                            methodName);
        }

        return null;
    }
}
