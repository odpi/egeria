/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.viewservices.feedbackmanager.handler;

import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.NoteLog;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Comment;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.InformalTag;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Note;
import org.odpi.openmetadata.frameworks.governanceaction.properties.RelatedMetadataElement;
import org.odpi.openmetadata.frameworks.governanceaction.properties.RelatedMetadataElementStub;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.viewservices.feedbackmanager.properties.*;

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
    public CollaborationManagerHandler(String   serverName,
                                       String   serverPlatformURLRoot,
                                       AuditLog auditLog,
                                       String   accessServiceURLMarker,
                                       int      maxPageSize) throws InvalidParameterException
    {
        this.client = new OpenMetadataStoreHandler(serverName, serverPlatformURLRoot, accessServiceURLMarker, maxPageSize);
        this.auditLog = auditLog;
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

        int startFrom = 0;
        List<RelatedMetadataElement> attachedRatings = client.getRelatedMetadataElements(userId,
                                                                                        elementGUID,
                                                                                        1,
                                                                                        OpenMetadataType.ATTACHED_RATING_RELATIONSHIP.typeName,
                                                                                        false,
                                                                                        false,
                                                                                        new Date(),
                                                                                        startFrom,
                                                                                        invalidParameterHandler.getMaxPagingSize());

        RelatedMetadataElement existingRating = null;

        while ((existingRating == null) && (attachedRatings != null))
        {
            for (RelatedMetadataElement attachedRating : attachedRatings)
            {
                if (attachedRating != null)
                {
                    if (userId.equals(attachedRating.getVersions().getCreatedBy()))
                    {
                        existingRating = attachedRating;
                        break;
                    }
                }
            }

            startFrom = startFrom + invalidParameterHandler.getMaxPagingSize();
            attachedRatings = client.getRelatedMetadataElements(userId,
                                                               elementGUID,
                                                               1,
                                                               OpenMetadataType.ATTACHED_RATING_RELATIONSHIP.typeName,
                                                               false,
                                                               false,
                                                               new Date(),
                                                               startFrom,
                                                               invalidParameterHandler.getMaxPagingSize());
        }

        if (existingRating == null)
        {
            // create
        }
        else
        {
            // update
        }
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
    }


    /**
     * Adds a "LikeProperties" to the element.
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
    }


    /**
     * Removes a "LikeProperties" added to the element by this user.
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
    }


    /**
     * Adds a comment to the element.
     *
     * @param userId        userId of user making request.
     * @param elementGUID     unique identifier for the element.
     * @param isPublic is this comment visible to other people.
     * @param properties   properties of the comment
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
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
                                      CommentProperties            properties,
                                      Date                         effectiveTime,
                                      boolean                      forLineage,
                                      boolean                      forDuplicateProcessing) throws InvalidParameterException,
                                                                                                  PropertyServerException,
                                                                                                  UserNotAuthorizedException
    {
        return null;
    }


    /**
     * Adds a comment to another comment.
     *
     * @param userId        userId of user making request.
     * @param commentGUID   unique identifier for an existing comment.  Used to add a reply to a comment.
     * @param properties   properties of the comment
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return guid of new comment.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the element properties to the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String addCommentReply(String                       userId,
                                  String                       commentGUID,
                                  boolean                      isPublic,
                                  CommentProperties            properties,
                                  Date                         effectiveTime,
                                  boolean                      forLineage,
                                  boolean                      forDuplicateProcessing) throws InvalidParameterException,
                                                                                              PropertyServerException,
                                                                                              UserNotAuthorizedException
    {
        return null;
    }


    /**
     * Update an existing comment.
     *
     * @param userId        userId of user making request.
     * @param commentGUID   unique identifier for the comment to change.
     * @param isPublic      is this visible to other people
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param properties   properties of the comment
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the element properties to the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void   updateComment(String            userId,
                                String            commentGUID,
                                boolean           isMergeUpdate,
                                boolean           isPublic,
                                CommentProperties properties,
                                Date              effectiveTime,
                                boolean           forLineage,
                                boolean           forDuplicateProcessing) throws InvalidParameterException,
                                                                                 PropertyServerException,
                                                                                 UserNotAuthorizedException
    {

    }


    /**
     * Link a comment that contains the best answer to a question posed in another comment.
     *
     * @param userId calling user
     * @param questionCommentGUID unique identifier of the comment containing the question
     * @param answerCommentGUID unique identifier of the comment containing the accepted answer
     * @param properties      is this visible to other people
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setupAcceptedAnswer(String             userId,
                                    String             questionCommentGUID,
                                    String             answerCommentGUID,
                                    FeedbackProperties properties,
                                    Date               effectiveTime,
                                    boolean            forLineage,
                                    boolean            forDuplicateProcessing) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException
    {
    }


    /**
     * Unlink a comment that contains an answer to a question posed in another comment.
     *
     * @param userId calling user
     * @param questionCommentGUID unique identifier of the comment containing the question
     * @param answerCommentGUID unique identifier of the comment containing the accepted answer
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearAcceptedAnswer(String  userId,
                                    String  questionCommentGUID,
                                    String  answerCommentGUID,
                                    Date    effectiveTime,
                                    boolean forLineage,
                                    boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
    }


    /**
     * Removes a comment added to the element by this user.
     *
     * @param userId       userId of user making request.
     * @param commentGUID  unique identifier for the comment object.
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem updating the element properties in the property server.
     * @throws UserNotAuthorizedException the user does not have permission to perform this request.
     */
    public void removeComment(String            userId,
                              String            commentGUID,
                              Date              effectiveTime,
                              boolean           forLineage,
                              boolean           forDuplicateProcessing) throws InvalidParameterException,
                                                                               PropertyServerException,
                                                                               UserNotAuthorizedException
    {
    }


    /**
     * Return the requested comment.
     *
     * @param userId       userId of user making request.
     * @param commentGUID  unique identifier for the comment object.
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @return comment properties
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem updating the element properties in the property server.
     * @throws UserNotAuthorizedException the user does not have permission to perform this request.
     */
    public Comment getComment(String  userId,
                              String  commentGUID,
                              Date    effectiveTime,
                              boolean forLineage,
                              boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                     PropertyServerException,
                                                                     UserNotAuthorizedException
    {
        return null;
    }


    /**
     * Return the comments attached to an element.
     *
     * @param userId       userId of user making request.
     * @param elementGUID    unique identifier for the element that the comments are connected to (maybe a comment too).
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @return list of comments
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem updating the element properties in the property server.
     * @throws UserNotAuthorizedException the user does not have permission to perform this request.
     */
    public List<Comment>  getAttachedComments(String  userId,
                                              String  elementGUID,
                                              int     startFrom,
                                              int     pageSize,
                                              Date    effectiveTime,
                                              boolean forLineage,
                                              boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                                     PropertyServerException,
                                                                                     UserNotAuthorizedException
    {
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
     * @param effectiveTime           the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<Comment> findComments(String  userId,
                                      String  searchString,
                                      int     startFrom,
                                      int     pageSize,
                                      Date    effectiveTime,
                                      boolean forLineage,
                                      boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException
    {
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
        return null;
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
    public InformalTag getTag(String userId,
                              String tagGUID) throws InvalidParameterException,
                                                     PropertyServerException,
                                                     UserNotAuthorizedException
    {
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
    public List<InformalTag> getTagsByName(String userId,
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
    public List<InformalTag> getMyTagsByName(String userId,
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
    public List<InformalTag> findTags(String userId,
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
    public List<InformalTag> findMyTags(String userId,
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
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
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
                                 boolean           isPublic,
                                 Date              effectiveTime,
                                 boolean           forLineage,
                                 boolean           forDuplicateProcessing) throws InvalidParameterException,
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
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateNoteLog(String            userId,
                              String            noteLogGUID,
                              boolean           isMergeUpdate,
                              boolean           isPublic,
                              NoteLogProperties noteLogProperties,
                              Date              effectiveTime,
                              boolean           forLineage,
                              boolean           forDuplicateProcessing) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
    }


    /**
     * Remove the metadata element representing a note log.
     *
     * @param userId calling user
     * @param noteLogGUID unique identifier of the metadata element to remove
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeNoteLog(String  userId,
                              String  noteLogGUID,
                              Date    effectiveTime,
                              boolean forLineage,
                              boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                     UserNotAuthorizedException,
                                                                     PropertyServerException
    {
    }


    /**
     * Retrieve the list of note log metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param userId calling user
     * @param searchString string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<NoteLog>   findNoteLogs(String  userId,
                                        String  searchString,
                                        int     startFrom,
                                        int     pageSize,
                                        Date    effectiveTime,
                                        boolean forLineage,
                                        boolean forDuplicateProcessing) throws InvalidParameterException,
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
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<NoteLog>   getNoteLogsByName(String  userId,
                                             String  name,
                                             int     startFrom,
                                             int     pageSize,
                                             Date    effectiveTime,
                                             boolean forLineage,
                                             boolean forDuplicateProcessing) throws InvalidParameterException,
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
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<NoteLog>   getNoteLogsForElement(String  userId,
                                                 String  elementGUID,
                                                 int     startFrom,
                                                 int     pageSize,
                                                 Date    effectiveTime,
                                                 boolean forLineage,
                                                 boolean forDuplicateProcessing) throws InvalidParameterException,
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
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return requested metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public NoteLog getNoteLogByGUID(String  userId,
                                    String  noteLogGUID,
                                    Date    effectiveTime,
                                    boolean forLineage,
                                    boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
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
                             NoteProperties  noteProperties,
                             Date            effectiveTime,
                             boolean         forLineage,
                             boolean         forDuplicateProcessing) throws InvalidParameterException,
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
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateNote(String         userId,
                           String         noteGUID,
                           boolean        isMergeUpdate,
                           NoteProperties noteProperties,
                           Date           effectiveTime,
                           boolean        forLineage,
                           boolean        forDuplicateProcessing) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
    }


    /**
     * Remove the metadata element representing a note.
     *
     * @param userId calling user
     * @param noteGUID unique identifier of the metadata element to remove
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeNote(String  userId,
                           String  noteGUID,
                           Date    effectiveTime,
                           boolean forLineage,
                           boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException
    {
    }


    /**
     * Retrieve the list of note metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param userId calling user
     * @param searchString string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<Note>   findNotes(String  userId,
                                  String  searchString,
                                  int     startFrom,
                                  int     pageSize,
                                  Date    effectiveTime,
                                  boolean forLineage,
                                  boolean forDuplicateProcessing) throws InvalidParameterException,
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
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return list of associated metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<Note>    getNotesForNoteLog(String  userId,
                                            String  noteLogGUID,
                                            int     startFrom,
                                            int     pageSize,
                                            Date    effectiveTime,
                                            boolean forLineage,
                                            boolean forDuplicateProcessing) throws InvalidParameterException,
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
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return matching metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public Note getNoteByGUID(String  userId,
                              String  noteGUID,
                              Date    effectiveTime,
                              boolean forLineage,
                              boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                     UserNotAuthorizedException,
                                                                     PropertyServerException
    {
        return null;
    }
}
