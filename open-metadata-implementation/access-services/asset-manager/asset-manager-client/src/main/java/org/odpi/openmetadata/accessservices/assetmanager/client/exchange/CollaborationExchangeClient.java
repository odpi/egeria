/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.assetmanager.client.exchange;

import org.odpi.openmetadata.accessservices.assetmanager.api.exchange.CollaborationExchangeInterface;
import org.odpi.openmetadata.accessservices.assetmanager.client.AssetManagerBaseClient;
import org.odpi.openmetadata.accessservices.assetmanager.client.rest.AssetManagerRESTClient;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.CommentElement;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.GlossaryElement;
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
import org.odpi.openmetadata.accessservices.assetmanager.rest.ArchiveRequestBody;
import org.odpi.openmetadata.accessservices.assetmanager.rest.CommentElementResponse;
import org.odpi.openmetadata.accessservices.assetmanager.rest.CommentElementsResponse;
import org.odpi.openmetadata.accessservices.assetmanager.rest.GlossaryElementsResponse;
import org.odpi.openmetadata.accessservices.assetmanager.rest.GlossarySearchStringRequestBody;
import org.odpi.openmetadata.accessservices.assetmanager.rest.InformalTagResponse;
import org.odpi.openmetadata.accessservices.assetmanager.rest.InformalTagUpdateRequestBody;
import org.odpi.openmetadata.accessservices.assetmanager.rest.InformalTagsResponse;
import org.odpi.openmetadata.accessservices.assetmanager.rest.NameRequestBody;
import org.odpi.openmetadata.accessservices.assetmanager.rest.NoteElementResponse;
import org.odpi.openmetadata.accessservices.assetmanager.rest.NoteElementsResponse;
import org.odpi.openmetadata.accessservices.assetmanager.rest.NoteLogElementResponse;
import org.odpi.openmetadata.accessservices.assetmanager.rest.NoteLogElementsResponse;
import org.odpi.openmetadata.accessservices.assetmanager.rest.SearchStringRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDListResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

import java.util.Date;
import java.util.List;


/**
 * CollaborationExchangeClient is the client for managing comments, ratings, likes and tags.
 */
public class CollaborationExchangeClient extends AssetManagerBaseClient implements CollaborationExchangeInterface
{
    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param auditLog logging destination
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public CollaborationExchangeClient(String   serverName,
                                       String   serverPlatformURLRoot,
                                       AuditLog auditLog) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, auditLog);
    }


    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public CollaborationExchangeClient(String serverName,
                                       String serverPlatformURLRoot) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot);
    }


    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param userId caller's userId embedded in all HTTP requests
     * @param password caller's userId embedded in all HTTP requests
     * @param auditLog logging destination
     *
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public CollaborationExchangeClient(String   serverName,
                                       String   serverPlatformURLRoot,
                                       String   userId,
                                       String   password,
                                       AuditLog auditLog) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, userId, password, auditLog);
    }


    /**
     * Create a new client that is going to be used in an OMAG Server.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param restClient client that issues the REST API calls
     * @param maxPageSize maximum number of results supported by this server
     * @param auditLog logging destination
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public CollaborationExchangeClient(String                 serverName,
                                       String                 serverPlatformURLRoot,
                                       AssetManagerRESTClient restClient,
                                       int                    maxPageSize,
                                       AuditLog               auditLog) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, restClient, maxPageSize, auditLog);
    }


    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param userId caller's userId embedded in all HTTP requests
     * @param password caller's userId embedded in all HTTP requests
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public CollaborationExchangeClient(String serverName,
                                       String serverPlatformURLRoot,
                                       String userId,
                                       String password) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, userId, password);
    }


    /**
     * Adds a star rating and optional review text to the element.  If the user has already attached
     * a rating then the original one is over-ridden.
     *
     * @param userId      userId of user making request.
     * @param elementGUID   unique identifier for the element.
     * @param isPublic is this visible to other people
     * @param properties  properties of the rating
     * @return unique identifier of the rating
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the element properties to the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @Override
    public String addRatingToElement(String           userId,
                                     String           elementGUID,
                                     boolean          isPublic,
                                     RatingProperties properties) throws InvalidParameterException,
                                                                         PropertyServerException,
                                                                         UserNotAuthorizedException
    {
        final String methodName  = "addRatingToElement";
        final String guidParameter = "elementGUID";

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix  + "/elements/{2}/ratings?isPublic={3}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(elementGUID, guidParameter, methodName);

        GUIDResponse response = restClient.callGUIDPostRESTCall(methodName,
                                                                urlTemplate,
                                                                properties,
                                                                serverName,
                                                                userId,
                                                                elementGUID,
                                                                isPublic);

        return response.getGUID();
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
    @Override
    public void removeRatingFromElement(String userId,
                                        String elementGUID) throws InvalidParameterException,
                                                                   PropertyServerException,
                                                                   UserNotAuthorizedException
    {
        final String   methodName = "removeRatingFromElement";
        final String   guidParameter = "elementGUID";

        final String   urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/elements/{2}/ratings/remove";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(elementGUID, guidParameter, methodName);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        nullRequestBody,
                                        serverName,
                                        userId,
                                        elementGUID);
    }


    /**
     * Adds a "LikeProperties" to the element.
     *
     * @param userId      userId of user making request
     * @param elementGUID   unique identifier for the element
     * @param isPublic is this visible to other people
     * @param properties   indicates whether the feedback should be shared or only be visible to the originating user
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the element properties to the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @Override
    public void addLikeToElement(String         userId,
                                 String         elementGUID,
                                 boolean        isPublic,
                                 LikeProperties properties) throws InvalidParameterException,
                                                                   PropertyServerException,
                                                                   UserNotAuthorizedException
    {
        final String   methodName  = "addLikeToElement";
        final String   guidParameter = "elementGUID";

        final String   urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/elements/{2}/likes?isPublic={3}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(elementGUID, guidParameter, methodName);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        properties,
                                        serverName,
                                        userId,
                                        elementGUID,
                                        isPublic);
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
    @Override
    public void removeLikeFromElement(String userId,
                                      String elementGUID) throws InvalidParameterException,
                                                                 PropertyServerException,
                                                                 UserNotAuthorizedException
    {
        final String   methodName = "removeLikeFromElement";
        final String   guidParameter = "elementGUID";

        final String   urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/elements/{2}/likes/remove";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(elementGUID, guidParameter, methodName);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        nullRequestBody,
                                        serverName,
                                        userId,
                                        elementGUID);
    }


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
    @Override
    public String addCommentToElement(String                       userId,
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
                                                                                                  UserNotAuthorizedException
    {
        final String methodName  = "addCommentToElement";
        final String guidParameterName = "elementGUID";
        final String propertiesParameterName  = "properties";

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/elements/{2}/comments";

        return super.createFeedbackWithParent(userId,
                                              assetManagerGUID,
                                              assetManagerName,
                                              assetManagerIsHome,
                                              elementGUID,
                                              guidParameterName,
                                              isPublic,
                                              properties,
                                              propertiesParameterName,
                                              externalIdentifierProperties,
                                              urlTemplate,
                                              effectiveTime,
                                              forLineage,
                                              forDuplicateProcessing,
                                              methodName);
    }


    /**
     * Adds a comment to another comment.
     *
     * @param userId        userId of user making request.
     * @param assetManagerGUID unique identifier of software capability representing the caller
     * @param assetManagerName unique name of software capability representing the caller
     * @param assetManagerIsHome      ensure that only the asset manager can update this element
     * @param commentGUID   unique identifier for an existing comment.  Used to add a reply to a comment.
     * @param externalIdentifierProperties optional properties used to define an external identifier for the comment.
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
    @Override
    public String addCommentReply(String                       userId,
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
                                                                                              UserNotAuthorizedException
    {
        final String methodName  = "addCommentReply";
        final String commentGUIDParameter = "commentGUID";
        final String propertiesParameterName  = "properties";
        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/comments/{2}/replies";

        return super.createFeedbackWithParent(userId,
                                              assetManagerGUID,
                                              assetManagerName,
                                              assetManagerIsHome,
                                              commentGUID,
                                              commentGUIDParameter,
                                              isPublic,
                                              properties,
                                              propertiesParameterName,
                                              externalIdentifierProperties,
                                              urlTemplate,
                                              effectiveTime,
                                              forLineage,
                                              forDuplicateProcessing,
                                              methodName);
    }


    /**
     * Update an existing comment.
     *
     * @param userId        userId of user making request.
     * @param assetManagerGUID unique identifier of software capability representing the caller
     * @param assetManagerName unique name of software capability representing the caller
     * @param commentGUID   unique identifier for the comment to change.
     * @param externalIdentifier unique identifier of the comment in the external asset manager
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param isPublic      is this visible to other people
     * @param properties   properties of the comment
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the element properties to the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @Override
    public void   updateComment(String            userId,
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
                                                                                 UserNotAuthorizedException
    {
        final String methodName  = "updateComment";
        final String commentGUIDParameter = "commentGUID";
        final String propertiesParameterName  = "properties";
        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/comments/{2}/update";

        super.updateFeedback(userId,
                             assetManagerGUID,
                             assetManagerName,
                             commentGUID,
                             commentGUIDParameter,
                             externalIdentifier,
                             isMergeUpdate,
                             isPublic,
                             properties,
                             propertiesParameterName,
                             urlTemplate,
                             effectiveTime,
                             forLineage,
                             forDuplicateProcessing,
                             methodName);
    }


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
    @Override
    public void setupAcceptedAnswer(String             userId,
                                    String             assetManagerGUID,
                                    String             assetManagerName,
                                    String             questionCommentGUID,
                                    String             answerCommentGUID,
                                    FeedbackProperties properties,
                                    Date               effectiveTime,
                                    boolean            forLineage,
                                    boolean            forDuplicateProcessing) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException
    {
        final String methodName                       = "setupAcceptedAnswer";
        final String questionCommentGUIDParameterName = "questionCommentGUID";
        final String answerCommentGUIDParameterName   = "answerCommentGUID";

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/comments/questions/{2}/answers/{3}";

        super.setupRelationship(userId,
                                assetManagerGUID,
                                assetManagerName,
                                questionCommentGUID,
                                questionCommentGUIDParameterName,
                                properties,
                                answerCommentGUID,
                                answerCommentGUIDParameterName,
                                urlTemplate,
                                effectiveTime,
                                forLineage,
                                forDuplicateProcessing,
                                methodName);
    }


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
    @Override
    public void clearAcceptedAnswer(String  userId,
                                    String  assetManagerGUID,
                                    String  assetManagerName,
                                    String  questionCommentGUID,
                                    String  answerCommentGUID,
                                    Date    effectiveTime,
                                    boolean forLineage,
                                    boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        final String methodName                       = "clearAcceptedAnswer";
        final String questionCommentGUIDParameterName = "questionCommentGUID";
        final String answerCommentGUIDParameterName   = "answerCommentGUID";

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/comments/questions/{2}/answers/{3}/remove";

        super.clearRelationship(userId,
                                assetManagerGUID,
                                assetManagerName,
                                questionCommentGUID,
                                questionCommentGUIDParameterName,
                                answerCommentGUID,
                                answerCommentGUIDParameterName,
                                urlTemplate,
                                effectiveTime,
                                forLineage,
                                forDuplicateProcessing,
                                methodName);
    }


    /**
     * Removes a comment added to the element.
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
    @Override
    public void removeComment(String            userId,
                              String            assetManagerGUID,
                              String            assetManagerName,
                              String            commentGUID,
                              String            externalIdentifier,
                              Date              effectiveTime,
                              boolean           forLineage,
                              boolean           forDuplicateProcessing) throws InvalidParameterException,
                                                                               PropertyServerException,
                                                                               UserNotAuthorizedException
    {
        final  String  methodName = "removeComment";
        final  String  commentGUIDParameter = "commentGUID";

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/comments/{2}/remove";

        super.removeReferenceable(userId,
                                  assetManagerGUID,
                                  assetManagerName,
                                  commentGUID,
                                  commentGUIDParameter,
                                  externalIdentifier,
                                  urlTemplate,
                                  effectiveTime,
                                  forLineage,
                                  forDuplicateProcessing,
                                  methodName);
    }


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
    @Override
    public CommentElement getComment(String  userId,
                                     String  assetManagerGUID,
                                     String  assetManagerName,
                                     String  commentGUID,
                                     Date    effectiveTime,
                                     boolean forLineage,
                                     boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                            PropertyServerException,
                                                                            UserNotAuthorizedException
    {
        final String   methodName = "getComment";
        final String   urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/comments/{2}?forLineage={3}&forDuplicateProcessing={4}";
        final String   guidParameter = "commentGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(commentGUID, guidParameter, methodName);

        CommentElementResponse restResult = restClient.callCommentElementPostRESTCall(methodName,
                                                                                      urlTemplate,
                                                                                      getEffectiveTimeQueryRequestBody(assetManagerGUID,
                                                                                                                       assetManagerName,
                                                                                                                       effectiveTime),
                                                                                      serverName,
                                                                                      userId,
                                                                                      commentGUID,
                                                                                      forLineage,
                                                                                      forDuplicateProcessing);

        return restResult.getElement();
    }


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
    @Override
    public List<CommentElement>  getAttachedComments(String  userId,
                                                     String  assetManagerGUID,
                                                     String  assetManagerName,
                                                     String  elementGUID,
                                                     int     startFrom,
                                                     int     pageSize,
                                                     Date    effectiveTime,
                                                     boolean forLineage,
                                                     boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                                            PropertyServerException,
                                                                                            UserNotAuthorizedException
    {
        final String methodName = "getAttachedComments";
        final String guidParameter = "elementGUID";

        final String   urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/elements/{2}/comments/retrieve?startFrom={3}&pageSize={4}&forLineage={}&forLineage={5}&forDuplicateProcessing={6}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(elementGUID, guidParameter, methodName);

        CommentElementsResponse restResult = restClient.callCommentElementsPostRESTCall(methodName,
                                                                                        urlTemplate,
                                                                                        getEffectiveTimeQueryRequestBody(assetManagerGUID, assetManagerName, effectiveTime),
                                                                                        serverName,
                                                                                        userId,
                                                                                        elementGUID,
                                                                                        startFrom,
                                                                                        pageSize,
                                                                                        forLineage,
                                                                                        forDuplicateProcessing);

        return restResult.getElementList();
    }


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
    @Override
    public List<CommentElement>   findComments(String  userId,
                                                String  assetManagerGUID,
                                                String  assetManagerName,
                                                String  searchString,
                                                int     startFrom,
                                                int     pageSize,
                                                Date    effectiveTime,
                                                boolean forLineage,
                                                boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        final String methodName                = "findGlossaries";
        final String searchStringParameterName = "searchString";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateSearchString(searchString, searchStringParameterName, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        SearchStringRequestBody requestBody = new SearchStringRequestBody();
        requestBody.setAssetManagerGUID(assetManagerGUID);
        requestBody.setAssetManagerName(assetManagerName);requestBody.setSearchString(searchString);
        requestBody.setSearchStringParameterName(searchStringParameterName);
        requestBody.setEffectiveTime(effectiveTime);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/comments/by-search-string?startFrom={2}&pageSize={3}&forLineage={4}&forDuplicateProcessing={5}";

        CommentElementsResponse restResult = restClient.callCommentElementsPostRESTCall(methodName,
                                                                                        urlTemplate,
                                                                                        requestBody,
                                                                                        serverName,
                                                                                        userId,
                                                                                        startFrom,
                                                                                        validatedPageSize,
                                                                                        forLineage,
                                                                                        forDuplicateProcessing);

        return restResult.getElementList();
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
    @Override
    public String createInformalTag(String                userId,
                                    InformalTagProperties properties) throws InvalidParameterException,
                                                                             PropertyServerException,
                                                                             UserNotAuthorizedException
    {
        final String methodName = "createInformalTag";
        final String propertiesParameterName  = "properties";
        final String nameParameterName  = "properties.tagName";

        final String   urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/tags";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateObject(properties, propertiesParameterName, methodName);
        invalidParameterHandler.validateName(properties.getName(), nameParameterName, methodName);

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  urlTemplate,
                                                                  properties,
                                                                  serverName,
                                                                  userId);

        return restResult.getGUID();
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
    @Override
    public void   updateTagDescription(String userId,
                                       String tagGUID,
                                       String tagDescription) throws InvalidParameterException,
                                                                     PropertyServerException,
                                                                     UserNotAuthorizedException
    {
        final String   methodName = "updateTagDescription";
        final String   guidParameter = "tagGUID";

        final String   urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/tags/{2}/update";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(tagGUID, guidParameter, methodName);

        InformalTagUpdateRequestBody tagRequestBody = new InformalTagUpdateRequestBody();
        tagRequestBody.setDescription(tagDescription);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        tagRequestBody,
                                        serverName,
                                        userId,
                                        tagGUID);
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
    @Override
    public void   deleteTag(String userId,
                            String tagGUID) throws InvalidParameterException,
                                                   PropertyServerException,
                                                   UserNotAuthorizedException
    {
        final String   methodName = "deleteTag";
        final String   guidParameter = "tagGUID";

        final String   urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/tags/{2}/remove";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(tagGUID, guidParameter, methodName);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        nullRequestBody,
                                        serverName,
                                        userId,
                                        tagGUID);
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
    @Override
    public InformalTagElement getTag(String userId,
                                     String tagGUID) throws InvalidParameterException,
                                                            PropertyServerException,
                                                            UserNotAuthorizedException
    {
        final String   methodName = "getTag";
        final String   urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/tags/{2}";
        final String   guidParameter = "guid";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(tagGUID, guidParameter, methodName);

        InformalTagResponse restResult = restClient.callInformalTagGetRESTCall(methodName,
                                                                               urlTemplate,
                                                                               serverName,
                                                                               userId,
                                                                               tagGUID);

        return restResult.getTag();
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
    @Override
    public List<InformalTagElement> getTagsByName(String userId,
                                                  String tag,
                                                  int    startFrom,
                                                  int    pageSize) throws InvalidParameterException,
                                                                          PropertyServerException,
                                                                          UserNotAuthorizedException
    {
        final String   methodName = "getTagsByName";
        final String   urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/tags/by-name?startFrom={2}&pageSize={3}";
        final String   nameParameter = "tag";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(tag, nameParameter, methodName);

        NameRequestBody requestBody = new NameRequestBody();

        requestBody.setName(tag);
        requestBody.setNameParameterName(nameParameter);

        InformalTagsResponse restResult = restClient.callInformalTagListPostRESTCall(methodName,
                                                                                     urlTemplate,
                                                                                     requestBody,
                                                                                     serverName,
                                                                                     userId,
                                                                                     startFrom,
                                                                                     pageSize);

        return restResult.getTags();
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
    @Override
    public List<InformalTagElement> getMyTagsByName(String userId,
                                                    String tag,
                                                    int    startFrom,
                                                    int    pageSize) throws InvalidParameterException,
                                                                            PropertyServerException,
                                                                            UserNotAuthorizedException
    {
        final String   methodName = "getTagsByName";
        final String   urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/tags/private/by-name?startFrom={2}&pageSize={3}";
        final String   nameParameter = "tag";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(tag, nameParameter, methodName);

        NameRequestBody requestBody = new NameRequestBody();

        requestBody.setName(tag);
        requestBody.setNameParameterName(nameParameter);

        InformalTagsResponse restResult = restClient.callInformalTagListPostRESTCall(methodName,
                                                                                     urlTemplate,
                                                                                     requestBody,
                                                                                     serverName,
                                                                                     userId,
                                                                                     startFrom,
                                                                                     pageSize);

        return restResult.getTags();
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
    @Override
    public List<InformalTagElement> findTags(String userId,
                                             String tag,
                                             int    startFrom,
                                             int    pageSize) throws InvalidParameterException,
                                                                     PropertyServerException,
                                                                     UserNotAuthorizedException
    {
        final String   methodName = "findTags";
        final String   urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/tags/by-search-string?startFrom={2}&pageSize={3}";
        final String   nameParameter = "tag";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(tag, nameParameter, methodName);

        SearchStringRequestBody requestBody = new SearchStringRequestBody();

        requestBody.setSearchString(tag);
        requestBody.setSearchStringParameterName(nameParameter);

        InformalTagsResponse restResult = restClient.callInformalTagListPostRESTCall(methodName,
                                                                                     urlTemplate,
                                                                                     requestBody,
                                                                                     serverName,
                                                                                     userId,
                                                                                     startFrom,
                                                                                     pageSize);

        return restResult.getTags();
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
    @Override
    public List<InformalTagElement> findMyTags(String userId,
                                               String tag,
                                               int    startFrom,
                                               int    pageSize) throws InvalidParameterException,
                                                                       PropertyServerException,
                                                                       UserNotAuthorizedException
    {
        final String   methodName = "findTags";
        final String   urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/tags/private/by-search-string?startFrom={2}&pageSize={3}";
        final String   nameParameter = "tag";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(tag, nameParameter, methodName);

        SearchStringRequestBody requestBody = new SearchStringRequestBody();

        requestBody.setSearchString(tag);
        requestBody.setSearchStringParameterName(nameParameter);

        InformalTagsResponse restResult = restClient.callInformalTagListPostRESTCall(methodName,
                                                                                     urlTemplate,
                                                                                     requestBody,
                                                                                     serverName,
                                                                                     userId,
                                                                                     startFrom,
                                                                                     pageSize);

        return restResult.getTags();
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
    @Override
    public void   addTagToElement(String  userId,
                                  String  elementGUID,
                                  String  tagGUID,
                                  boolean isPublic) throws InvalidParameterException,
                                                           PropertyServerException,
                                                           UserNotAuthorizedException
    {
        final String   methodName  = "addTagToElement";
        final String   elementGUIDParameterName = "elementGUID";
        final String   tagGUIDParameterName = "tagGUID";

        final String   urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/elements/{2}/tags/{3}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(elementGUID, elementGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(tagGUID, tagGUIDParameterName, methodName);

        FeedbackProperties requestBody = new FeedbackProperties();
        requestBody.setIsPublic(isPublic);
        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        elementGUID,
                                        tagGUID);
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
    @Override
    public void   removeTagFromElement(String userId,
                                       String elementGUID,
                                       String tagGUID) throws InvalidParameterException,
                                                              PropertyServerException,
                                                              UserNotAuthorizedException
    {
        final String   methodName  = "removeTagFromElement";
        final String   elementGUIDParameterName = "elementGUID";
        final String   tagGUIDParameterName = "tagGUID";

        final String   urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/elements/{2}/tags/{3}/remove";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(elementGUID, elementGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(tagGUID, tagGUIDParameterName, methodName);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        nullRequestBody,
                                        serverName,
                                        userId,
                                        elementGUID,
                                        tagGUID);
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
    @Override
    public List<String> getElementsByTag(String userId,
                                         String tagGUID,
                                         int    startFrom,
                                         int    pageSize) throws InvalidParameterException,
                                                                 PropertyServerException,
                                                                 UserNotAuthorizedException
    {
        final String methodName = "getElementsByTag";
        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/elements/by-tag/{2}?startFrom={3}&pageSize={4}";
        final String tagGUIDParameterName = "tagGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(tagGUID, tagGUIDParameterName, methodName);

        GUIDListResponse restResult = restClient.callGUIDListGetRESTCall(methodName,
                                                                         urlTemplate,
                                                                         serverName,
                                                                         userId,
                                                                         tagGUID,
                                                                         startFrom,
                                                                         pageSize);

        return restResult.getGUIDs();
    }


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
    @Override
    public  String createNoteLog(String                       userId,
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
                                                                                             PropertyServerException
    {
        final String methodName                  = "createNoteLog";
        final String guidParameterName           = "elementGUID";
        final String propertiesParameterName     = "noteLogProperties";

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/elements/{2}/note-logs";

        return super.createReferenceableWithParent(userId,
                                                   assetManagerGUID,
                                                   assetManagerName,
                                                   assetManagerIsHome,
                                                   elementGUID,
                                                   guidParameterName,
                                                   noteLogProperties,
                                                   propertiesParameterName,
                                                   externalIdentifierProperties,
                                                   urlTemplate,
                                                   effectiveTime,
                                                   forLineage,
                                                   forDuplicateProcessing,
                                                   methodName);
    }


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
    @Override
    public String createNoteLogFromTemplate(String                       userId,
                                            String                       assetManagerGUID,
                                            String                       assetManagerName,
                                            boolean                      assetManagerIsHome,
                                            String                       elementGUID,
                                            String                       templateGUID,
                                            ExternalIdentifierProperties externalIdentifierProperties,
                                            TemplateProperties           templateProperties) throws InvalidParameterException,
                                                                                                    UserNotAuthorizedException,
                                                                                                    PropertyServerException
    {
        final String methodName = "createNoteLogFromTemplate";
        final String guidParameterName = "elementGUID";

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/elements/{2}/note-logs/from-template/{3}";

        return super.createReferenceableFromTemplateWithParent(userId,
                                                               assetManagerGUID,
                                                               assetManagerName,
                                                               assetManagerIsHome,
                                                               elementGUID,
                                                               guidParameterName,
                                                               templateGUID,
                                                               templateProperties,
                                                               externalIdentifierProperties,
                                                               urlTemplate,
                                                               methodName);
    }


    /**
     * Update the metadata element representing a note log.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software capability representing the caller
     * @param assetManagerName unique name of software capability representing the caller
     * @param noteLogGUID unique identifier of the metadata element to update
     * @param noteLogExternalIdentifier unique identifier of the note log in the external asset manager
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
    @Override
    public void updateNoteLog(String            userId,
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
                                                                               PropertyServerException
    {
        final String methodName                 = "updateNoteLog";
        final String noteLogGUIDParameterName   = "noteLogGUID";
        final String propertiesParameterName    = "noteLogProperties";

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/note-logs/{2}/update";

        super.updateFeedback(userId,
                             assetManagerGUID,
                             assetManagerName,
                             noteLogGUID,
                             noteLogGUIDParameterName,
                             noteLogExternalIdentifier,
                             isMergeUpdate,
                             isPublic,
                             noteLogProperties,
                             propertiesParameterName,
                             urlTemplate,
                             effectiveTime,
                             forLineage,
                             forDuplicateProcessing,
                             methodName);
    }


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
    @Override
    public void removeNoteLog(String  userId,
                              String  assetManagerGUID,
                              String  assetManagerName,
                              String  noteLogGUID,
                              String  noteLogExternalIdentifier,
                              Date    effectiveTime,
                              boolean forLineage,
                              boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                     UserNotAuthorizedException,
                                                                     PropertyServerException
    {
        final String methodName                  = "removeNoteLog";
        final String noteLogGUIDParameterName   = "noteLogGUID";

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/note-logs/{2}/remove";

        super.removeReferenceable(userId,
                                  assetManagerGUID,
                                  assetManagerName,
                                  noteLogGUID,
                                  noteLogGUIDParameterName,
                                  noteLogExternalIdentifier,
                                  urlTemplate,
                                  effectiveTime,
                                  forLineage,
                                  forDuplicateProcessing,
                                  methodName);
    }


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
    @Override
    public List<NoteLogElement>   findNoteLogs(String  userId,
                                               String  assetManagerGUID,
                                               String  assetManagerName,
                                               String  searchString,
                                               int     startFrom,
                                               int     pageSize,
                                               Date    effectiveTime,
                                               boolean forLineage,
                                               boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException
    {
        final String methodName                = "findNoteLogs";
        final String searchStringParameterName = "searchString";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateSearchString(searchString, searchStringParameterName, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        SearchStringRequestBody requestBody = new SearchStringRequestBody();
        requestBody.setAssetManagerGUID(assetManagerGUID);
        requestBody.setAssetManagerName(assetManagerName);
        requestBody.setSearchString(searchString);
        requestBody.setSearchStringParameterName(searchStringParameterName);
        requestBody.setEffectiveTime(effectiveTime);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/note-logs/by-search-string?startFrom={2}&pageSize={3}&forLineage={4}&forDuplicateProcessing={5}";

        NoteLogElementsResponse restResult = restClient.callNoteLogElementsPostRESTCall(methodName,
                                                                                        urlTemplate,
                                                                                        requestBody,
                                                                                        serverName,
                                                                                        userId,
                                                                                        startFrom,
                                                                                        validatedPageSize,
                                                                                        forLineage,
                                                                                        forDuplicateProcessing);

        return restResult.getElementList();
    }


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
    @Override
    public List<NoteLogElement>   getNoteLogsByName(String  userId,
                                                    String  assetManagerGUID,
                                                    String  assetManagerName,
                                                    String  name,
                                                    int     startFrom,
                                                    int     pageSize,
                                                    Date    effectiveTime,
                                                    boolean forLineage,
                                                    boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException
    {
        final String methodName        = "getNoteLogsByName";
        final String nameParameterName = "name";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(name, nameParameterName, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        NameRequestBody requestBody = new NameRequestBody();
        requestBody.setAssetManagerGUID(assetManagerGUID);
        requestBody.setAssetManagerName(assetManagerName);
        requestBody.setName(name);
        requestBody.setNameParameterName(nameParameterName);
        requestBody.setEffectiveTime(effectiveTime);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/note-logs/by-name?startFrom={2}&pageSize={3}&forLineage={4}&forDuplicateProcessing={5}";

        NoteLogElementsResponse restResult = restClient.callNoteLogElementsPostRESTCall(methodName,
                                                                                        urlTemplate,
                                                                                        requestBody,
                                                                                        serverName,
                                                                                        userId,
                                                                                        startFrom,
                                                                                        validatedPageSize,
                                                                                        forLineage,
                                                                                        forDuplicateProcessing);

        return restResult.getElementList();
    }


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
    @Override
    public NoteLogElement getNoteLogByGUID(String  userId,
                                           String  assetManagerGUID,
                                           String  assetManagerName,
                                           String  noteLogGUID,
                                           Date    effectiveTime,
                                           boolean forLineage,
                                           boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException
    {
        final String methodName = "getNoteLogByGUID";
        final String guidParameterName = "noteLogGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(noteLogGUID, guidParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/note-logs/{2}/retrieve?forLineage={3}&forDuplicateProcessing={4}";

        NoteLogElementResponse restResult = restClient.callNoteLogElementPostRESTCall(methodName,
                                                                                    urlTemplate,
                                                                                    getEffectiveTimeQueryRequestBody(assetManagerGUID,
                                                                                                                          assetManagerName,
                                                                                                                          effectiveTime),
                                                                                    serverName,
                                                                                    userId,
                                                                                    noteLogGUID,
                                                                                    forLineage,
                                                                                    forDuplicateProcessing);

        return restResult.getElement();
    }


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
    @Override
    public String createNote(String                       userId,
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
                                                                                         PropertyServerException
    {
        final String methodName                  = "createNote";
        final String guidParameterName           = "noteLogGUID";
        final String propertiesParameterName     = "noteProperties";

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/note-logs/{2}/notes";

        return super.createReferenceableWithParent(userId,
                                                   assetManagerGUID,
                                                   assetManagerName,
                                                   assetManagerIsHome,
                                                   noteLogGUID,
                                                   guidParameterName,
                                                   noteProperties,
                                                   propertiesParameterName,
                                                   externalIdentifierProperties,
                                                   urlTemplate,
                                                   effectiveTime,
                                                   forLineage,
                                                   forDuplicateProcessing,
                                                   methodName);
    }



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
    @Override
    public String createNoteFromTemplate(String                       userId,
                                         String                       assetManagerGUID,
                                         String                       assetManagerName,
                                         boolean                      assetManagerIsHome,
                                         String                       noteLogGUID,
                                         String                       templateGUID,
                                         ExternalIdentifierProperties externalIdentifierProperties,
                                         TemplateProperties           templateProperties) throws InvalidParameterException,
                                                                                                 UserNotAuthorizedException,
                                                                                                 PropertyServerException
    {
        final String methodName = "createNoteFromTemplate";
        final String guidParameterName = "noteLogGUID";

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/note-logs/{2}/notes/from-template/{3}";

        return super.createReferenceableFromTemplateWithParent(userId,
                                                               assetManagerGUID,
                                                               assetManagerName,
                                                               assetManagerIsHome,
                                                               noteLogGUID,
                                                               guidParameterName,
                                                               templateGUID,
                                                               templateProperties,
                                                               externalIdentifierProperties,
                                                               urlTemplate,
                                                               methodName);
    }


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
    @Override
    public void updateNote(String         userId,
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
                                                                         PropertyServerException
    {
        final String methodName              = "updateNote";
        final String noteGUIDParameterName   = "noteGUID";
        final String propertiesParameterName = "noteProperties";

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/note-logs/notes/{2}/update";

        super.updateReferenceable(userId,
                                  assetManagerGUID,
                                  assetManagerName,
                                  noteGUID,
                                  noteGUIDParameterName,
                                  noteExternalIdentifier,
                                  isMergeUpdate,
                                  noteProperties,
                                  propertiesParameterName,
                                  urlTemplate,
                                  effectiveTime,
                                  forLineage,
                                  forDuplicateProcessing,
                                  methodName);
    }


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
    @Override
    public NoteElement undoNoteUpdate(String  userId,
                                      String  assetManagerGUID,
                                      String  assetManagerName,
                                      String  noteGUID,
                                      String  noteExternalIdentifier,
                                      Date    effectiveTime,
                                      boolean forLineage,
                                      boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException
    {
        final String methodName = "undoNoteUpdate";
        final String noteGUIDParameterName = "noteGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(noteGUID, noteGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/note-logs/notes/{2}/undo&forLineage={3}&forDuplicateProcessing={4}";

        NoteElementResponse response = restClient.callNoteElementPostRESTCall(methodName,
                                                                              urlTemplate,
                                                                              getUpdateRequestBody(assetManagerGUID, assetManagerName, noteExternalIdentifier, effectiveTime, methodName),
                                                                              serverName,
                                                                              userId,
                                                                              noteGUID,
                                                                              forLineage,
                                                                              forDuplicateProcessing);

        return response.getElement();
    }


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
    @Override
    public void archiveNote(String            userId,
                            String            assetManagerGUID,
                            String            assetManagerName,
                            String            noteGUID,
                            String            noteExternalIdentifier,
                            ArchiveProperties archiveProperties,
                            Date              effectiveTime,
                            boolean           forDuplicateProcessing) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException
    {
        final String methodName = "archiveNote";
        final String noteGUIDParameterName = "noteGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(noteGUID, noteGUIDParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/note-logs/notes/{2}/archive&forLineage={3}&forDuplicateProcessing={4}";

        ArchiveRequestBody requestBody = new ArchiveRequestBody();
        requestBody.setElementProperties(archiveProperties);
        requestBody.setMetadataCorrelationProperties(this.getCorrelationProperties(assetManagerGUID,
                                                                                   assetManagerName,
                                                                                   noteExternalIdentifier,
                                                                                   methodName));

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        noteGUID,
                                        forDuplicateProcessing);
    }


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
    @Override
    public void removeNote(String  userId,
                           String  assetManagerGUID,
                           String  assetManagerName,
                           String  noteGUID,
                           String  noteExternalIdentifier,
                           Date    effectiveTime,
                           boolean forLineage,
                           boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException
    {
        final String methodName              = "removeNote";
        final String noteGUIDParameterName   = "noteGUID";

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/note-logs/notes/{2}/remove";

        super.removeReferenceable(userId,
                                  assetManagerGUID,
                                  assetManagerName,
                                  noteGUID,
                                  noteGUIDParameterName,
                                  noteExternalIdentifier,
                                  urlTemplate,
                                  effectiveTime,
                                  forLineage,
                                  forDuplicateProcessing,
                                  methodName);
    }


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
    @Override
    public List<NoteElement>   findNotes(String  userId,
                                         String  assetManagerGUID,
                                         String  assetManagerName,
                                         String  elementGUID,
                                         String  searchString,
                                         int     startFrom,
                                         int     pageSize,
                                         Date    effectiveTime,
                                         boolean forLineage,
                                         boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        final String methodName                = "findNotes";
        final String searchStringParameterName = "searchString";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateSearchString(searchString, searchStringParameterName, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        SearchStringRequestBody requestBody = new SearchStringRequestBody();
        requestBody.setAssetManagerGUID(assetManagerGUID);
        requestBody.setAssetManagerName(assetManagerName);
        requestBody.setSearchString(searchString);
        requestBody.setSearchStringParameterName(searchStringParameterName);
        requestBody.setEffectiveTime(effectiveTime);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/note-logs/notes/by-search-string?startFrom={2}&pageSize={3}&forLineage={4}&forDuplicateProcessing={5}";

        NoteElementsResponse restResult = restClient.callNoteElementsPostRESTCall(methodName,
                                                                                  urlTemplate,
                                                                                  requestBody,
                                                                                  serverName,
                                                                                  userId,
                                                                                  startFrom,
                                                                                  validatedPageSize,
                                                                                  forLineage,
                                                                                  forDuplicateProcessing);

        return restResult.getElementList();
    }


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
    @Override
    public List<NoteElement>    getNotesForNoteLog(String  userId,
                                                   String  assetManagerGUID,
                                                   String  assetManagerName,
                                                   String  noteLogGUID,
                                                   int     startFrom,
                                                   int     pageSize,
                                                   Date    effectiveTime,
                                                   boolean forLineage,
                                                   boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException
    {
        final String methodName        = "getNotesForNoteLog";
        final String guidParameterName = "noteLogGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(noteLogGUID, guidParameterName, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/note-logs/{2}/notes/retrieve?startFrom={3}&pageSize={4}&forLineage={5}&forDuplicateProcessing={6}";

        NoteElementsResponse restResult = restClient.callNoteElementsPostRESTCall(methodName,
                                                                                  urlTemplate,
                                                                                  getEffectiveTimeQueryRequestBody(assetManagerGUID, assetManagerName, effectiveTime),
                                                                                  serverName,
                                                                                  userId,
                                                                                  noteLogGUID,
                                                                                  startFrom,
                                                                                  validatedPageSize,
                                                                                  forLineage,
                                                                                  forDuplicateProcessing);

        return restResult.getElementList();
    }


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
    @Override
    public List<NoteElement>   getNotesByName(String  userId,
                                              String  assetManagerGUID,
                                              String  assetManagerName,
                                              String  elementGUID,
                                              String  name,
                                              int     startFrom,
                                              int     pageSize,
                                              Date    effectiveTime,
                                              boolean forLineage,
                                              boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        final String methodName        = "getNotesByName";
        final String nameParameterName = "name";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(name, nameParameterName, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        NameRequestBody requestBody = new NameRequestBody();
        requestBody.setAssetManagerGUID(assetManagerGUID);
        requestBody.setAssetManagerName(assetManagerName);
        requestBody.setName(name);
        requestBody.setNameParameterName(nameParameterName);
        requestBody.setEffectiveTime(effectiveTime);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/note-logs/notes/by-name?startFrom={2}&pageSize={3}&forLineage={4}&forDuplicateProcessing={5}";

        NoteElementsResponse restResult = restClient.callNoteElementsPostRESTCall(methodName,
                                                                                  urlTemplate,
                                                                                  requestBody,
                                                                                  serverName,
                                                                                  userId,
                                                                                  startFrom,
                                                                                  validatedPageSize,
                                                                                  forLineage,
                                                                                  forDuplicateProcessing);

        return restResult.getElementList();
    }


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
    @Override
    public NoteElement getNoteByGUID(String  userId,
                                     String  assetManagerGUID,
                                     String  assetManagerName,
                                     String  noteGUID,
                                     Date    effectiveTime,
                                     boolean forLineage,
                                     boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException
    {
        final String methodName = "getNoteByGUID";
        final String guidParameterName = "noteGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(noteGUID, guidParameterName, methodName);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/note-logs/notes/{2}/retrieve?forLineage={3}&forDuplicateProcessing={4}";

        NoteElementResponse restResult = restClient.callNoteElementPostRESTCall(methodName,
                                                                                urlTemplate,
                                                                                getEffectiveTimeQueryRequestBody(assetManagerGUID,
                                                                                                                 assetManagerName,
                                                                                                                 effectiveTime),
                                                                                serverName,
                                                                                userId,
                                                                                noteGUID,
                                                                                forLineage,
                                                                                forDuplicateProcessing);

        return restResult.getElement();
    }
}
