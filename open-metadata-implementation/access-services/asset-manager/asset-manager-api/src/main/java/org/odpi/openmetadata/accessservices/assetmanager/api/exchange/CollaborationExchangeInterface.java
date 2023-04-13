/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetmanager.api.exchange;

import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.CommentElement;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.InformalTagElement;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.NoteElement;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.NoteLogElement;
import org.odpi.openmetadata.accessservices.assetmanager.properties.ArchiveProperties;
import org.odpi.openmetadata.accessservices.assetmanager.properties.CommentProperties;
import org.odpi.openmetadata.accessservices.assetmanager.properties.ExternalIdentifierProperties;
import org.odpi.openmetadata.accessservices.assetmanager.properties.FeedbackProperties;
import org.odpi.openmetadata.accessservices.assetmanager.properties.InformalTagProperties;
import org.odpi.openmetadata.accessservices.assetmanager.properties.LikeProperties;
import org.odpi.openmetadata.accessservices.assetmanager.properties.NoteLogProperties;
import org.odpi.openmetadata.accessservices.assetmanager.properties.NoteProperties;
import org.odpi.openmetadata.accessservices.assetmanager.properties.RatingProperties;
import org.odpi.openmetadata.accessservices.assetmanager.properties.TemplateProperties;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

import java.util.Date;
import java.util.List;

/**
 * The CollaborationExchangeInterface supports the exchange of comments, likes, reviews/ratings and
 * informal tags.
 */
public interface CollaborationExchangeInterface
{
    /**
     * Adds a star rating and optional review text to the element.  If the user has already attached
     * a rating then the original one is over-ridden.
     *
     * @param userId      userId of user making request.
     * @param elementGUID   unique identifier for the element.
     * @param isPublic is this visible to other people
     * @param properties  properties for the rating
     * @return unique identifier of the rating
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the element properties to the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    String addRatingToElement(String           userId,
                              String           elementGUID,
                              boolean          isPublic,
                              RatingProperties properties) throws InvalidParameterException,
                                                                  PropertyServerException,
                                                                  UserNotAuthorizedException;


    /**
     * Removes of a review that was added to the element by this user.
     *
     * @param userId      userId of user making request.
     * @param elementGUID   unique identifier for the element where the rating is attached.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem updating the element properties in the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    void removeRatingFromElement(String userId,
                                 String elementGUID) throws InvalidParameterException,
                                                            PropertyServerException,
                                                            UserNotAuthorizedException;


    /**
     * Adds a "LikeProperties" to the element.  If the user has already attached a like then the original one
     * is over-ridden.
     *
     * @param userId      userId of user making request.
     * @param elementGUID   unique identifier for the element where the like is to be attached.
     * @param isPublic is this visible to other people
     * @param properties    indicates whether the feedback should be shared or only be visible to the originating user
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the element properties to the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    void   addLikeToElement(String         userId,
                            String         elementGUID,
                            boolean        isPublic,
                            LikeProperties properties) throws InvalidParameterException,
                                                              PropertyServerException,
                                                              UserNotAuthorizedException;


    /**
     * Removes a "LikeProperties" added to the element by this user.
     *
     * @param userId   userId of user making request.
     * @param elementGUID unique identifier for the element where the like is attached.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem updating the element properties in the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    void   removeLikeFromElement(String     userId,
                                 String     elementGUID) throws InvalidParameterException,
                                                                PropertyServerException,
                                                                UserNotAuthorizedException;


    /**
     * Adds a comment to the element.
     *
     * @param userId        userId of user making request.
     * @param assetManagerGUID unique identifier of software capability representing the caller
     * @param assetManagerName unique name of software capability representing the caller
     * @param assetManagerIsHome      ensure that only the asset manager can update this element
     * @param elementGUID     unique identifier for the element.
     * @param externalIdentifierProperties optional properties used to define an external identifier for the comment.
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
    String addCommentToElement(String                       userId,
                               String                       assetManagerGUID,
                               String                       assetManagerName,
                               boolean                      assetManagerIsHome,
                               String                       elementGUID,
                               ExternalIdentifierProperties externalIdentifierProperties,
                               boolean                      isPublic,
                               CommentProperties            properties,
                               Date                         effectiveTime,
                               boolean                      forLineage,
                               boolean                      forDuplicateProcessing) throws InvalidParameterException,
                                                                                           PropertyServerException,
                                                                                           UserNotAuthorizedException;


    /**
     * Adds a comment to another comment.
     *
     * @param userId        userId of user making request.
     * @param assetManagerGUID unique identifier of software capability representing the caller
     * @param assetManagerName unique name of software capability representing the caller
     * @param assetManagerIsHome      ensure that only the asset manager can update this element
     * @param commentGUID   unique identifier for an existing comment.  Used to add a reply to a comment.
     * @param externalIdentifierProperties optional properties used to define an external identifier for the comment.
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
    String addCommentReply(String                       userId,
                           String                       assetManagerGUID,
                           String                       assetManagerName,
                           boolean                      assetManagerIsHome,
                           String                       commentGUID,
                           ExternalIdentifierProperties externalIdentifierProperties,
                           boolean                      isPublic,
                           CommentProperties            properties,
                           Date                         effectiveTime,
                           boolean                      forLineage,
                           boolean                      forDuplicateProcessing) throws InvalidParameterException,
                                                                                       PropertyServerException,
                                                                                       UserNotAuthorizedException;


    /**
     * Update an existing comment.
     *
     * @param userId        userId of user making request.
     * @param assetManagerGUID unique identifier of software capability representing the caller
     * @param assetManagerName unique name of software capability representing the caller
     * @param commentGUID   unique identifier for the comment to change.
     * @param externalIdentifier unique identifier of the comment in the external asset manager
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
    void   updateComment(String            userId,
                         String            assetManagerGUID,
                         String            assetManagerName,
                         String            commentGUID,
                         String            externalIdentifier,
                         boolean           isMergeUpdate,
                         boolean           isPublic,
                         CommentProperties properties,
                         Date              effectiveTime,
                         boolean           forLineage,
                         boolean           forDuplicateProcessing) throws InvalidParameterException,
                                                                          PropertyServerException,
                                                                          UserNotAuthorizedException;


    /**
     * Link a comment that contains the best answer to a question posed in another comment.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software capability representing the caller
     * @param assetManagerName unique name of software capability representing the caller
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
    void setupAcceptedAnswer(String             userId,
                             String             assetManagerGUID,
                             String             assetManagerName,
                             String             questionCommentGUID,
                             String             answerCommentGUID,
                             FeedbackProperties properties,
                             Date               effectiveTime,
                             boolean            forLineage,
                             boolean            forDuplicateProcessing) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException;


    /**
     * Unlink a comment that contains an answer to a question posed in another comment.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software capability representing the caller
     * @param assetManagerName unique name of software capability representing the caller
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
    void clearAcceptedAnswer(String  userId,
                             String  assetManagerGUID,
                             String  assetManagerName,
                             String  questionCommentGUID,
                             String  answerCommentGUID,
                             Date    effectiveTime,
                             boolean forLineage,
                             boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException;


    /**
     * Removes a comment added to the element by this user.
     *
     * @param userId       userId of user making request.
     * @param assetManagerGUID unique identifier of software capability representing the caller
     * @param assetManagerName unique name of software capability representing the caller
     * @param commentGUID  unique identifier for the comment object.
     * @param externalIdentifier unique identifier of the comment in the external asset manager
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem updating the element properties in the property server.
     * @throws UserNotAuthorizedException the user does not have permission to perform this request.
     */
    void removeComment(String            userId,
                       String            assetManagerGUID,
                       String            assetManagerName,
                       String            commentGUID,
                       String            externalIdentifier,
                       Date              effectiveTime,
                       boolean           forLineage,
                       boolean           forDuplicateProcessing) throws InvalidParameterException,
                                                                        PropertyServerException,
                                                                        UserNotAuthorizedException;


    /**
     * Return the requested comment.
     *
     * @param userId       userId of user making request.
     * @param assetManagerGUID unique identifier of software capability representing the caller
     * @param assetManagerName unique name of software capability representing the caller
     * @param commentGUID  unique identifier for the comment object.
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @return comment properties
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem updating the element properties in the property server.
     * @throws UserNotAuthorizedException the user does not have permission to perform this request.
     */
    CommentElement getComment(String  userId,
                              String  assetManagerGUID,
                              String  assetManagerName,
                              String  commentGUID,
                              Date    effectiveTime,
                              boolean forLineage,
                              boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                     PropertyServerException,
                                                                     UserNotAuthorizedException;


    /**
     * Return the comments attached to an element.
     *
     * @param userId       userId of user making request.
     * @param assetManagerGUID unique identifier of software capability representing the caller
     * @param assetManagerName unique name of software capability representing the caller
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
    List<CommentElement>  getAttachedComments(String  userId,
                                              String  assetManagerGUID,
                                              String  assetManagerName,
                                              String  elementGUID,
                                              int     startFrom,
                                              int     pageSize,
                                              Date    effectiveTime,
                                              boolean forLineage,
                                              boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                                     PropertyServerException,
                                                                                     UserNotAuthorizedException;


    /**
     * Retrieve the list of comment metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software capability representing the caller
     * @param assetManagerName unique name of software capability representing the caller
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
    List<CommentElement>   findComments(String  userId,
                                        String  assetManagerGUID,
                                        String  assetManagerName,
                                        String  searchString,
                                        int     startFrom,
                                        int     pageSize,
                                        Date    effectiveTime,
                                        boolean forLineage,
                                        boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException;

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
    String createInformalTag(String                userId,
                             InformalTagProperties properties) throws InvalidParameterException,
                                                                      PropertyServerException,
                                                                      UserNotAuthorizedException;



    /**
     * Updates the description of an existing tag (either private or public).
     *
     * @param userId          userId of user making request.
     * @param tagGUID         unique identifier for the tag.
     * @param tagDescription  description of the tag.  Setting a description, particularly in a public tag
     *                        makes the tag more valuable to other users and can act as an embryonic note.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the element properties to the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    void   updateTagDescription(String userId,
                                String tagGUID,
                                String tagDescription) throws InvalidParameterException,
                                                              PropertyServerException,
                                                              UserNotAuthorizedException;


    /**
     * Removes an informal tag from the repository.  All the tagging relationships to this informal tag are lost.
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
    void   deleteTag(String userId,
                     String tagGUID) throws InvalidParameterException,
                                            PropertyServerException,
                                            UserNotAuthorizedException;


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
    InformalTagElement getTag(String userId,
                              String tagGUID) throws InvalidParameterException,
                                                     PropertyServerException,
                                                     UserNotAuthorizedException;


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
    List<InformalTagElement> getTagsByName(String userId,
                                           String tag,
                                           int    startFrom,
                                           int    pageSize) throws InvalidParameterException,
                                                                   PropertyServerException,
                                                                   UserNotAuthorizedException;


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
    List<InformalTagElement> getMyTagsByName(String userId,
                                             String tag,
                                             int    startFrom,
                                             int    pageSize) throws InvalidParameterException,
                                                                     PropertyServerException,
                                                                     UserNotAuthorizedException;


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
    List<InformalTagElement> findTags(String userId,
                                      String tag,
                                      int    startFrom,
                                      int    pageSize) throws InvalidParameterException,
                                                              PropertyServerException,
                                                              UserNotAuthorizedException;

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
    List<InformalTagElement> findMyTags(String userId,
                                        String tag,
                                        int    startFrom,
                                        int    pageSize) throws InvalidParameterException,
                                                                PropertyServerException,
                                                                UserNotAuthorizedException;

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
    void   addTagToElement(String  userId,
                           String  elementGUID,
                           String  tagGUID,
                           boolean isPublic) throws InvalidParameterException,
                                                    PropertyServerException,
                                                    UserNotAuthorizedException;


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
    void   removeTagFromElement(String userId,
                                String elementGUID,
                                String tagGUID) throws InvalidParameterException,
                                                       PropertyServerException,
                                                       UserNotAuthorizedException;


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
    List<String> getElementsByTag(String userId,
                                  String tagGUID,
                                  int    startFrom,
                                  int    pageSize) throws InvalidParameterException,
                                                          PropertyServerException,
                                                          UserNotAuthorizedException;

    /* =====================================================================================================================
     * A note log maintains an ordered list of notes.  It can be used to support release note, blogs and similar 
     * broadcast information.  Notelogs are typically maintained by the owners/stewards of an element.
     */

    /**
     * Create a new metadata element to represent a note log and attach it to an element (if supplied).
     * Any supplied element becomes the note log's anchor, causing the note log to be deleted if/when the element is deleted.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software capability representing the caller
     * @param assetManagerName unique name of software capability representing the caller
     * @param assetManagerIsHome      ensure that only the asset manager can update this element
     * @param elementGUID unique identifier of the element where the note log is located
     * @param externalIdentifierProperties optional properties used to define an external identifier for the note log
     * @param noteLogProperties properties about the note log to store
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
    String createNoteLog(String                       userId,
                         String                       assetManagerGUID,
                         String                       assetManagerName,
                         boolean                      assetManagerIsHome,
                         String                       elementGUID,
                         ExternalIdentifierProperties externalIdentifierProperties,
                         NoteLogProperties            noteLogProperties,
                         Date                         effectiveTime,
                         boolean                      forLineage,
                         boolean                      forDuplicateProcessing) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException;


    /**
     * Create a new metadata element to represent a note log using an existing metadata element as a template.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software capability representing the caller
     * @param assetManagerName unique name of software capability representing the caller
     * @param assetManagerIsHome      ensure that only the asset manager can update this element
     * @param elementGUID unique identifier of the element where the note log is located
     * @param templateGUID unique identifier of the metadata element to copy
     * @param externalIdentifierProperties optional properties used to define an external identifier
     * @param templateProperties properties that override the template
     *
     * @return unique identifier of the new note log
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    String createNoteLogFromTemplate(String                       userId,
                                     String                       assetManagerGUID,
                                     String                       assetManagerName,
                                     boolean                      assetManagerIsHome,
                                     String                       elementGUID,
                                     String                       templateGUID,
                                     ExternalIdentifierProperties externalIdentifierProperties,
                                     TemplateProperties           templateProperties) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException;


    /**
     * Update the metadata element representing a note log.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software capability representing the caller
     * @param assetManagerName unique name of software capability representing the caller
     * @param noteLogGUID unique identifier of the metadata element to update
     * @param noteLogExternalIdentifier unique identifier of the note log in the external asset manager
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param isPublic      is this visible to other people
     * @param noteLogProperties new properties for the metadata element
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void updateNoteLog(String            userId,
                       String            assetManagerGUID,
                       String            assetManagerName,
                       String            noteLogGUID,
                       String            noteLogExternalIdentifier,
                       boolean           isMergeUpdate,
                       boolean           isPublic,
                       NoteLogProperties noteLogProperties,
                       Date              effectiveTime,
                       boolean           forLineage,
                       boolean           forDuplicateProcessing) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException;


    /**
     * Remove the metadata element representing a note log.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software capability representing the caller
     * @param assetManagerName unique name of software capability representing the caller
     * @param noteLogGUID unique identifier of the metadata element to remove
     * @param noteLogExternalIdentifier unique identifier of the note log in the external asset manager
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void removeNoteLog(String  userId,
                       String  assetManagerGUID,
                       String  assetManagerName,
                       String  noteLogGUID,
                       String  noteLogExternalIdentifier,
                       Date    effectiveTime,
                       boolean forLineage,
                       boolean forDuplicateProcessing) throws InvalidParameterException,
                                                              UserNotAuthorizedException,
                                                              PropertyServerException;


    /**
     * Retrieve the list of note log metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software capability representing the caller
     * @param assetManagerName unique name of software capability representing the caller
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
    List<NoteLogElement>   findNoteLogs(String  userId,
                                        String  assetManagerGUID,
                                        String  assetManagerName,
                                        String  searchString,
                                        int     startFrom,
                                        int     pageSize,
                                        Date    effectiveTime,
                                        boolean forLineage,
                                        boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException;


    /**
     * Retrieve the list of note log metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software capability representing the caller
     * @param assetManagerName unique name of software capability representing the caller
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
    List<NoteLogElement>   getNoteLogsByName(String  userId,
                                             String  assetManagerGUID,
                                             String  assetManagerName,
                                             String  name,
                                             int     startFrom,
                                             int     pageSize,
                                             Date    effectiveTime,
                                             boolean forLineage,
                                             boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException;


    /**
     * Retrieve the note log metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software capability representing the caller
     * @param assetManagerName unique name of software capability representing the caller
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
    NoteLogElement getNoteLogByGUID(String  userId,
                                    String  assetManagerGUID,
                                    String  assetManagerName,
                                    String  noteLogGUID,
                                    Date    effectiveTime,
                                    boolean forLineage,
                                    boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException;


    /* ===============================================================================
     * A element typically contains many notes, linked with relationships.
     */

    /**
     * Create a new metadata element to represent a note.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software capability representing the caller
     * @param assetManagerName unique name of software capability representing the caller
     * @param assetManagerIsHome      ensure that only the asset manager can update this element
     * @param noteLogGUID unique identifier of the element where the note is located
     * @param externalIdentifierProperties optional properties used to define an external identifier for the note
     * @param noteProperties properties for the note
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return unique identifier of the new metadata element for the note
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    String createNote(String                       userId,
                      String                       assetManagerGUID,
                      String                       assetManagerName,
                      boolean                      assetManagerIsHome,
                      String                       noteLogGUID,
                      ExternalIdentifierProperties externalIdentifierProperties,
                      NoteProperties               noteProperties,
                      Date                         effectiveTime,
                      boolean                      forLineage,
                      boolean                      forDuplicateProcessing) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException;



    /**
     * Create a new metadata element to represent a note using an existing metadata element as a template.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software capability representing the caller
     * @param assetManagerName unique name of software capability representing the caller
     * @param assetManagerIsHome      ensure that only the asset manager can update this element
     * @param noteLogGUID unique identifier of the element where the note is located
     * @param templateGUID unique identifier of the metadata element to copy
     * @param externalIdentifierProperties optional properties used to define an external identifier
     * @param templateProperties properties that override the template
     *
     * @return unique identifier of the new metadata element for the note
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    String createNoteFromTemplate(String                       userId,
                                  String                       assetManagerGUID,
                                  String                       assetManagerName,
                                  boolean                      assetManagerIsHome,
                                  String                       noteLogGUID,
                                  String                       templateGUID,
                                  ExternalIdentifierProperties externalIdentifierProperties,
                                  TemplateProperties           templateProperties) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException;


    /**
     * Update the properties of the metadata element representing a note.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software capability representing the caller
     * @param assetManagerName unique name of software capability representing the caller
     * @param noteGUID unique identifier of the note to update
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param noteProperties new properties for the note
     * @param noteExternalIdentifier unique identifier of the note in the external asset manager
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void updateNote(String         userId,
                    String         assetManagerGUID,
                    String         assetManagerName,
                    String         noteGUID,
                    String         noteExternalIdentifier,
                    boolean        isMergeUpdate,
                    NoteProperties noteProperties,
                    Date           effectiveTime,
                    boolean        forLineage,
                    boolean        forDuplicateProcessing) throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException;


    /**
     * Undo the last update to the note.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software capability representing the caller
     * @param assetManagerName unique name of software capability representing the caller
     * @param noteGUID unique identifier of the metadata element to update
     * @param noteExternalIdentifier unique identifier of the note in the external asset manager
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @return recovered note
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    NoteElement undoNoteUpdate(String  userId,
                                               String  assetManagerGUID,
                                               String  assetManagerName,
                                               String  noteGUID,
                                               String  noteExternalIdentifier,
                                               Date    effectiveTime,
                                               boolean forLineage,
                                               boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException;


    /**
     * Archive the metadata element representing a note.  This removes it from normal access.  However, it is still available
     * for lineage requests.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software capability representing the caller
     * @param assetManagerName unique name of software capability representing the caller
     * @param noteGUID unique identifier of the metadata element to archive
     * @param noteExternalIdentifier unique identifier of the note in the external asset manager
     * @param archiveProperties option parameters about the archive process
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void archiveNote(String            userId,
                     String            assetManagerGUID,
                     String            assetManagerName,
                     String            noteGUID,
                     String            noteExternalIdentifier,
                     ArchiveProperties archiveProperties,
                     Date              effectiveTime,
                     boolean           forDuplicateProcessing) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException;


    /**
     * Remove the metadata element representing a note.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software capability representing the caller
     * @param assetManagerName unique name of software capability representing the caller
     * @param noteGUID unique identifier of the metadata element to remove
     * @param noteExternalIdentifier unique identifier of the note in the external asset manager
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void removeNote(String  userId,
                    String  assetManagerGUID,
                    String  assetManagerName,
                    String  noteGUID,
                    String  noteExternalIdentifier,
                    Date    effectiveTime,
                    boolean forLineage,
                    boolean forDuplicateProcessing) throws InvalidParameterException,
                                                           UserNotAuthorizedException,
                                                           PropertyServerException;


    /**
     * Retrieve the list of note metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software capability representing the caller
     * @param assetManagerName unique name of software capability representing the caller
     * @param elementGUID unique identifier of the element to query
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
    List<NoteElement>   findNotes(String                   userId,
                                  String                   assetManagerGUID,
                                  String                   assetManagerName,
                                  String                   elementGUID,
                                  String                   searchString,
                                  int                      startFrom,
                                  int                      pageSize,
                                  Date                     effectiveTime,
                                  boolean                  forLineage,
                                  boolean                  forDuplicateProcessing) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException;


    /**
     * Retrieve the list of notes associated with a note log.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software capability representing the caller
     * @param assetManagerName unique name of software capability representing the caller
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
    List<NoteElement>    getNotesForNoteLog(String  userId,
                                            String  assetManagerGUID,
                                            String  assetManagerName,
                                            String  noteLogGUID,
                                            int     startFrom,
                                            int     pageSize,
                                            Date    effectiveTime,
                                            boolean forLineage,
                                            boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException;


    /**
     * Retrieve the list of note metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software capability representing the caller
     * @param assetManagerName unique name of software capability representing the caller
     * @param elementGUID unique identifier of the element to query
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
    List<NoteElement>   getNotesByName(String                   userId,
                                       String                   assetManagerGUID,
                                       String                   assetManagerName,
                                       String                   elementGUID,
                                       String                   name,
                                       int                      startFrom,
                                       int                      pageSize,
                                       Date                     effectiveTime,
                                       boolean                  forLineage,
                                       boolean                  forDuplicateProcessing) throws InvalidParameterException,
                                                                                               UserNotAuthorizedException,
                                                                                               PropertyServerException;


    /**
     * Retrieve the note metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software capability representing the caller
     * @param assetManagerName unique name of software capability representing the caller
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
    NoteElement getNoteByGUID(String  userId,
                              String  assetManagerGUID,
                              String  assetManagerName,
                              String  noteGUID,
                              Date    effectiveTime,
                              boolean forLineage,
                              boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                     UserNotAuthorizedException,
                                                                     PropertyServerException;
}
