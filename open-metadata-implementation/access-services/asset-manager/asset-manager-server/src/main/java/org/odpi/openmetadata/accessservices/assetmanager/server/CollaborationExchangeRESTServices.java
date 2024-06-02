/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetmanager.server;

import org.odpi.openmetadata.accessservices.assetmanager.handlers.CommentExchangeHandler;
import org.odpi.openmetadata.accessservices.assetmanager.handlers.NoteLogExchangeHandler;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.InformalTagElement;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.LikeElement;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.RatingElement;
import org.odpi.openmetadata.accessservices.assetmanager.properties.CommentProperties;
import org.odpi.openmetadata.accessservices.assetmanager.properties.FeedbackProperties;
import org.odpi.openmetadata.accessservices.assetmanager.properties.NoteLogProperties;
import org.odpi.openmetadata.accessservices.assetmanager.properties.NoteProperties;
import org.odpi.openmetadata.accessservices.assetmanager.properties.RatingProperties;
import org.odpi.openmetadata.accessservices.assetmanager.properties.TagProperties;
import org.odpi.openmetadata.accessservices.assetmanager.rest.CommentElementResponse;
import org.odpi.openmetadata.accessservices.assetmanager.rest.CommentElementsResponse;
import org.odpi.openmetadata.accessservices.assetmanager.rest.EffectiveTimeQueryRequestBody;
import org.odpi.openmetadata.accessservices.assetmanager.rest.InformalTagResponse;
import org.odpi.openmetadata.accessservices.assetmanager.rest.InformalTagUpdateRequestBody;
import org.odpi.openmetadata.accessservices.assetmanager.rest.InformalTagsResponse;
import org.odpi.openmetadata.accessservices.assetmanager.rest.NameRequestBody;
import org.odpi.openmetadata.accessservices.assetmanager.rest.NoteElementResponse;
import org.odpi.openmetadata.accessservices.assetmanager.rest.NoteElementsResponse;
import org.odpi.openmetadata.accessservices.assetmanager.rest.NoteLogElementResponse;
import org.odpi.openmetadata.accessservices.assetmanager.rest.NoteLogElementsResponse;
import org.odpi.openmetadata.accessservices.assetmanager.rest.ReferenceableUpdateRequestBody;
import org.odpi.openmetadata.accessservices.assetmanager.rest.RelationshipRequestBody;
import org.odpi.openmetadata.accessservices.assetmanager.rest.SearchStringRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDListResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.NullRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.commonservices.generichandlers.InformalTagHandler;
import org.odpi.openmetadata.commonservices.generichandlers.LikeHandler;
import org.odpi.openmetadata.frameworks.openmetadata.enums.StarRating;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.commonservices.generichandlers.RatingHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.slf4j.LoggerFactory;

import java.util.Date;


/**
 * The AssetManagerRESTServices provides the server-side implementation of the Asset Manager Open Metadata
 * Assess Service (OMAS).  This interface provides connections to elements and APIs for adding feedback
 * on the element.
 */
public class CollaborationExchangeRESTServices
{
    private static final AssetManagerInstanceHandler   instanceHandler      = new AssetManagerInstanceHandler();
    private        final RESTExceptionHandler           restExceptionHandler = new RESTExceptionHandler();

    private static final RESTCallLogger                 restCallLogger       = new RESTCallLogger(LoggerFactory.getLogger(AssetManagerRESTServices.class),
                                                                                                  instanceHandler.getServiceName());

    /**
     * Default constructor
     */
    public CollaborationExchangeRESTServices()
    {
    }


    /**
     * Adds a star rating and optional review text to the element.  If the user has already attached
     * a rating then the original one is over-ridden.
     *
     * @param serverName name of the server instances for this request
     * @param userId      String - userId of user making request.
     * @param guid        String - unique id for the element.
     * @param isPublic is this visible to other people
     * @param requestBody containing the StarRating and user review of referenceable (probably element).
     *
     * @return void or
     * InvalidParameterException - one of the parameters is null or invalid or
     * PropertyServerException - there is a problem adding the element properties to
     *                                   the metadata repository or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public VoidResponse addRatingToElement(String           serverName,
                                           String           userId,
                                           String           guid,
                                           boolean          isPublic,
                                           RatingProperties requestBody)
    {
        final String methodName = "addRatingToElement";
        final String guidParameterName = "guid";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse  response = new VoidResponse();
        AuditLog      auditLog = null;

        try
        {
            if (requestBody != null)
            {
                RatingHandler<RatingElement> handler = instanceHandler.getRatingHandler(userId, serverName, methodName);

                int starRating = StarRating.NOT_RECOMMENDED.getOrdinal();

                if (requestBody.getStarRating() != null)
                {
                    starRating = requestBody.getStarRating().getOrdinal();
                }

                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
                handler.saveRating(userId,
                                   null,
                                   null,
                                   guid,
                                   guidParameterName,
                                   starRating,
                                   requestBody.getReview(),
                                   isPublic,
                                   false,
                                   false,
                                   new Date(),
                                   methodName);

            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Removes a star rating that was added to the element by this user.
     *
     * @param serverName name of the server instances for this request
     * @param userId      String - userId of user making request.
     * @param guid        String - unique id for the rating object
     * @param requestBody null request body needed to satisfy the HTTP Post request
     *
     * @return void or
     * InvalidParameterException - one of the parameters is null or invalid or
     * PropertyServerException - there is a problem updating the element properties in
     *                                   the metadata repository or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse removeRatingFromElement(String          serverName,
                                                String          userId,
                                                String          guid,
                                                NullRequestBody requestBody)
    {
        final String methodName = "removeRatingFromElement";
        final String guidParameterName = "guid";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse  response = new VoidResponse();
        AuditLog      auditLog = null;

        try
        {
            RatingHandler<RatingElement> handler = instanceHandler.getRatingHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            handler.removeRating(userId,
                                 null,
                                 null,
                                 guid,
                                 guidParameterName,
                                 false,
                                 false,
                                 new Date(),
                                 methodName);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Adds a "LikeProperties" to the element.
     *
     * @param serverName name of the server instances for this request
     * @param userId      String - userId of user making request.
     * @param guid        String - unique id for the element.
     * @param isPublic is this visible to other people
     * @param requestBody feedback request body .
     *
     * @return void or
     * InvalidParameterException - one of the parameters is null or invalid or
     * PropertyServerException - there is a problem adding the element properties to
     *                                   the metadata repository or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse addLikeToElement(String         serverName,
                                         String         userId,
                                         String         guid,
                                         boolean        isPublic,
                                         NullRequestBody requestBody)
    {
        final String methodName        = "addLikeToElement";
        final String guidParameterName = "guid";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse  response = new VoidResponse();
        AuditLog      auditLog = null;

        try
        {
            if (requestBody != null)
            {
                LikeHandler<LikeElement> handler = instanceHandler.getLikeHandler(userId, serverName, methodName);

                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
                handler.saveLike(userId,
                                 null,
                                 null,
                                 guid,
                                 guidParameterName,
                                 isPublic,
                                 false,
                                 false,
                                 new Date(),
                                 methodName);
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Removes a "LikeProperties" added to the element by this user.
     *
     * @param serverName name of the server instances for this request
     * @param userId  String - userId of user making request.
     * @param guid  String - unique id for the like object
     * @param requestBody null request body needed to satisfy the HTTP Post request
     *
     * @return void or
     * InvalidParameterException - one of the parameters is null or invalid or
     * PropertyServerException - there is a problem updating the element properties in
     *                                   the metadata repository or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse removeLikeFromElement(String          serverName,
                                              String          userId,
                                              String          guid,
                                              NullRequestBody requestBody)
    {
        final String methodName        = "removeLikeFromElement";
        final String guidParameterName = "guid";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse  response = new VoidResponse();
        AuditLog      auditLog = null;

        try
        {
            LikeHandler<LikeElement> handler = instanceHandler.getLikeHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            handler.removeLike(userId,
                               null,
                               null,
                               guid,
                               guidParameterName,
                               false,
                               false,
                               new Date(),
                               methodName);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Adds a comment to the element.
     *
     * @param serverName name of the server instances for this request
     * @param userId  String - userId of user making request.
     * @param guid  String - unique id for the element.
     * @param isPublic is this visible to other people
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody containing type of comment enum and the text of the comment.
     *
     * @return guid for new comment object or
     * InvalidParameterException - one of the parameters is null or invalid or
     * PropertyServerException - there is a problem adding the element properties to
     *                                   the metadata repository or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public GUIDResponse addCommentToElement(String                         serverName,
                                            String                         userId,
                                            String                         guid,
                                            boolean                        isPublic,
                                            boolean                        forLineage,
                                            boolean                        forDuplicateProcessing,
                                            ReferenceableUpdateRequestBody requestBody)
    {
        final String methodName = "addCommentToElement";
        final String guidParameterName = "elementGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse  response = new GUIDResponse();
        AuditLog      auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getElementProperties() instanceof CommentProperties commentProperties)
                {
                    CommentExchangeHandler handler = instanceHandler.getCommentHandler(userId, serverName, methodName);

                    response.setGUID(handler.createComment(userId,
                                                           guid,
                                                           guidParameterName,
                                                           requestBody.getMetadataCorrelationProperties(),
                                                           isPublic,
                                                           commentProperties,
                                                           forLineage,
                                                           forDuplicateProcessing,
                                                           requestBody.getEffectiveTime(),
                                                           methodName));
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(CommentProperties.class.getName(), methodName);
                }
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Adds a reply to a comment.
     *
     * @param serverName name of the server instances for this request
     * @param userId       String - userId of user making request.
     * @param commentGUID  String - unique id for an existing comment.  Used to add a reply to a comment.
     * @param isPublic is this visible to other people
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody  containing type of comment enum and the text of the comment.
     *
     * @return guid for new comment object or
     * InvalidParameterException - one of the parameters is null or invalid or
     * PropertyServerException - there is a problem adding the element properties to
     *                                   the metadata repository or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public GUIDResponse addCommentReply(String                         serverName,
                                        String                         userId,
                                        String                         commentGUID,
                                        boolean                        isPublic,
                                        boolean                        forLineage,
                                        boolean                        forDuplicateProcessing,
                                        ReferenceableUpdateRequestBody requestBody)
    {
        final String guidParameterName = "commentGUID";
        final String methodName        = "addCommentReply";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse  response = new GUIDResponse();
        AuditLog      auditLog = null;

        try
        {
            if (requestBody != null)
            {
                if (requestBody.getElementProperties() instanceof CommentProperties commentProperties)
                {
                    CommentExchangeHandler handler = instanceHandler.getCommentHandler(userId, serverName, methodName);

                    response.setGUID(handler.createComment(userId,
                                                           commentGUID,
                                                           guidParameterName,
                                                           requestBody.getMetadataCorrelationProperties(),
                                                           isPublic,
                                                           commentProperties,
                                                           forLineage,
                                                           forDuplicateProcessing,
                                                           requestBody.getEffectiveTime(),
                                                           methodName));
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(CommentProperties.class.getName(), methodName);
                }
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Update an existing comment.
     *
     * @param serverName   name of the server instances for this request.
     * @param userId       userId of user making request.
     * @param commentGUID  unique identifier for the comment to change.
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param isPublic is this visible to other people
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody  containing type of comment enum and the text of the comment.
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid.
     * PropertyServerException There is a problem updating the element properties in the metadata repository.
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse   updateComment(String                         serverName,
                                        String                         userId,
                                        String                         commentGUID,
                                        boolean                        isMergeUpdate,
                                        boolean                        isPublic,
                                        boolean                        forLineage,
                                        boolean                        forDuplicateProcessing,
                                        ReferenceableUpdateRequestBody requestBody)
    {
        final String methodName        = "updateComment";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse  response = new VoidResponse();
        AuditLog      auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getElementProperties() instanceof  CommentProperties commentProperties)
                {
                    CommentExchangeHandler handler = instanceHandler.getCommentHandler(userId, serverName, methodName);

                    handler.updateComment(userId,
                                          requestBody.getMetadataCorrelationProperties(),
                                          commentGUID,
                                          commentProperties,
                                          isMergeUpdate,
                                          isPublic,
                                          forLineage,
                                          forDuplicateProcessing,
                                          requestBody.getEffectiveTime(),
                                          methodName);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(CommentProperties.class.getName(), methodName);
                }
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Link a comment that contains the best answer to a question posed in another comment.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param questionCommentGUID unique identifier of the comment containing the question
     * @param answerCommentGUID unique identifier of the comment containing the accepted answer
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties to help with the mapping of the elements in the external asset manager and open metadata
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse setupAcceptedAnswer(String                  serverName,
                                            String                  userId,
                                            String                  questionCommentGUID,
                                            String                  answerCommentGUID,
                                            boolean                 forLineage,
                                            boolean                 forDuplicateProcessing,
                                            RelationshipRequestBody requestBody)
    {
        final String methodName = "setupAcceptedAnswer";

        RESTCallToken token      = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            CommentExchangeHandler handler = instanceHandler.getCommentHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof FeedbackProperties feedbackProperties)
                {
                    handler.setupAcceptedAnswer(userId,
                                                requestBody.getAssetManagerGUID(),
                                                requestBody.getAssetManagerName(),
                                                questionCommentGUID,
                                                answerCommentGUID,
                                                feedbackProperties.getIsPublic(),
                                                requestBody.getProperties().getEffectiveFrom(),
                                                requestBody.getProperties().getEffectiveTo(),
                                                forLineage,
                                                forDuplicateProcessing,
                                                requestBody.getEffectiveTime(),
                                                methodName);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(CommentProperties.class.getName(), methodName);
                }
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Unlink a comment that contains an answer to a question posed in another comment.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param questionCommentGUID unique identifier of the comment containing the question
     * @param answerCommentGUID unique identifier of the comment containing the accepted answer
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties to help with the mapping of the elements in the external asset manager and open metadata
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse clearAcceptedAnswer(String                        serverName,
                                            String                        userId,
                                            String                        questionCommentGUID,
                                            String                        answerCommentGUID,
                                            boolean                       forLineage,
                                            boolean                       forDuplicateProcessing,
                                            EffectiveTimeQueryRequestBody requestBody)
    {
        final String methodName = "clearAcceptedAnswer";

        RESTCallToken token      = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            CommentExchangeHandler handler = instanceHandler.getCommentHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.clearAcceptedAnswer(userId,
                                            requestBody.getAssetManagerGUID(),
                                            requestBody.getAssetManagerName(),
                                            questionCommentGUID,
                                            answerCommentGUID,
                                            forLineage,
                                            forDuplicateProcessing,
                                            requestBody.getEffectiveTime(),
                                            methodName);
            }
            else
            {
                handler.clearAcceptedAnswer(userId,
                                            null,
                                            null,
                                            questionCommentGUID,
                                            answerCommentGUID,
                                            forLineage,
                                            forDuplicateProcessing,
                                            null,
                                            methodName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Removes a comment added to the element by this user.
     *
     * @param serverName name of the server instances for this request
     * @param userId  String - userId of user making request.
     * @param elementGUID  String - unique id for the element object
     * @param commentGUID  String - unique id for the comment object
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody null request body needed to satisfy the HTTP Post request
     *
     * @return void or
     * InvalidParameterException - one of the parameters is null or invalid or
     * PropertyServerException - there is a problem updating the element properties in
     *                                   the metadata repository or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse removeCommentFromElement(String                         serverName,
                                                 String                         userId,
                                                 String                         elementGUID,
                                                 String                         commentGUID,
                                                 boolean                        forLineage,
                                                 boolean                        forDuplicateProcessing,
                                                 ReferenceableUpdateRequestBody requestBody)
    {
        final String guidParameterName = "commentGUID";
        final String methodName        = "removeElementComment";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse  response = new VoidResponse();
        AuditLog      auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            CommentExchangeHandler handler = instanceHandler.getCommentHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.removeComment(userId,
                                      requestBody.getMetadataCorrelationProperties(),
                                      commentGUID,
                                      forLineage,
                                      forDuplicateProcessing,
                                      requestBody.getEffectiveTime(),
                                      methodName);
            }
            else
            {
                handler.removeComment(userId,
                                      null,
                                      commentGUID,
                                      forLineage,
                                      forDuplicateProcessing,
                                      null,
                                      methodName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Return the requested comment.
     *
     * @param serverName name of the server instances for this request
     * @param userId       userId of user making request.
     * @param commentGUID  unique identifier for the comment object.
     * @param forLineage               return elements marked with the Memento classification?
     * @param forDuplicateProcessing   do not merge elements marked as duplicates?
     * @param requestBody effectiveTime and asset manager identifiers
     * @return comment properties or
     *  InvalidParameterException one of the parameters is null or invalid.
     *  PropertyServerException there is a problem updating the element properties in the property server.
     *  UserNotAuthorizedException the user does not have permission to perform this request.
     */
    public CommentElementResponse getCommentByGUID(String                        serverName,
                                                   String                        userId,
                                                   String                        commentGUID,
                                                   boolean                       forLineage,
                                                   boolean                       forDuplicateProcessing,
                                                   EffectiveTimeQueryRequestBody requestBody)
    {
        final String methodName = "getComment";
        final String guidParameterName = "commentGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        CommentElementResponse response = new CommentElementResponse();
        AuditLog            auditLog = null;

        try
        {
            CommentExchangeHandler handler = instanceHandler.getCommentHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElement(handler.getCommentByGUID(userId,
                                                             commentGUID,
                                                             guidParameterName,
                                                             OpenMetadataType.COMMENT.typeName,
                                                             forLineage,
                                                             forDuplicateProcessing,
                                                             requestBody.getEffectiveTime(),
                                                             methodName));
            }
            else
            {
                response.setElement(handler.getCommentByGUID(userId,
                                                             commentGUID,
                                                             guidParameterName,
                                                             OpenMetadataType.COMMENT.typeName,
                                                             forLineage,
                                                             forDuplicateProcessing,
                                                             null,
                                                             methodName));
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Return the comments attached to an element.
     *
     * @param serverName name of the server instances for this request
     * @param userId       userId of user making request.
     * @param elementGUID    unique identifier for the element that the comments are connected to (maybe a comment too).
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param forLineage               return elements marked with the Memento classification?
     * @param forDuplicateProcessing   do not merge elements marked as duplicates?
     * @param requestBody effectiveTime and asset manager identifiers
     * @return list of comments or
     *  InvalidParameterException one of the parameters is null or invalid.
     *  PropertyServerException there is a problem updating the element properties in the property server.
     *  UserNotAuthorizedException the user does not have permission to perform this request.
     */
    public CommentElementsResponse getAttachedComments(String                        serverName,
                                                       String                        userId,
                                                       String                        elementGUID,
                                                       int                           startFrom,
                                                       int                           pageSize,
                                                       boolean                       forLineage,
                                                       boolean                       forDuplicateProcessing,
                                                       EffectiveTimeQueryRequestBody requestBody)
    {
        final String methodName = "getAttachedComments";
        final String guidParameterName = "elementGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        CommentElementsResponse response = new CommentElementsResponse();
        AuditLog                auditLog = null;

        try
        {
            CommentExchangeHandler handler = instanceHandler.getCommentHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElementList(handler.getAttachedComments(userId,
                                                                    requestBody.getAssetManagerGUID(),
                                                                    requestBody.getAssetManagerName(),
                                                                    elementGUID,
                                                                    guidParameterName,
                                                                    startFrom,
                                                                    pageSize,
                                                                    forLineage,
                                                                    forDuplicateProcessing,
                                                                    requestBody.getEffectiveTime(),
                                                                    methodName));
            }
            else
            {
                response.setElementList(handler.getAttachedComments(userId,
                                                                    null,
                                                                    null,
                                                                    elementGUID,
                                                                    guidParameterName,
                                                                    startFrom,
                                                                    pageSize,
                                                                    forLineage,
                                                                    forDuplicateProcessing,
                                                                    null,
                                                                    methodName));
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Creates a new informal tag and returns the unique identifier for it.
     *
     * @param serverName   name of the server instances for this request
     * @param userId       userId of user making request
     * @param requestBody  contains the name of the tag and (optional) description of the tag
     *
     * @return guid for new tag or
     * InvalidParameterException - one of the parameters is invalid or
     * PropertyServerException - there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public GUIDResponse createInformalTag(String        serverName,
                                          String        userId,
                                          TagProperties requestBody)
    {
        final String   methodName = "createTag";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse  response = new GUIDResponse();
        AuditLog      auditLog = null;

        try
        {
            if (requestBody != null)
            {
                InformalTagHandler<InformalTagElement> handler = instanceHandler.getInformalTagHandler(userId, serverName, methodName);

                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
                response.setGUID(handler.createTag(userId,
                                                   null,
                                                   null,
                                                   requestBody.getName(),
                                                   requestBody.getDescription(),
                                                   (!requestBody.getIsPrivateTag()),
                                                   null,
                                                   null,
                                                   new Date(),
                                                   methodName));
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Updates the description of an existing tag (either private or public).
     *
     * @param serverName   name of the server instances for this request
     * @param userId       userId of user making request.
     * @param tagGUID      unique id for the tag.
     * @param requestBody  contains the name of the tag and (optional) description of the tag.
     *
     * @return void or
     * InvalidParameterException - one of the parameters is invalid or
     * PropertyServerException - there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public VoidResponse   updateTagDescription(String                serverName,
                                               String                userId,
                                               String                tagGUID,
                                               InformalTagUpdateRequestBody requestBody)
    {
        final String methodName           = "updateTagDescription";
        final String tagGUIDParameterName = "tagGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse  response = new VoidResponse();
        AuditLog      auditLog = null;

        try
        {
            if (requestBody != null)
            {
                InformalTagHandler<InformalTagElement> handler = instanceHandler.getInformalTagHandler(userId, serverName, methodName);

                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
                handler.updateTagDescription(userId,
                                             null,
                                             null,
                                             tagGUID,
                                             tagGUIDParameterName,
                                             requestBody.getDescription(),
                                             null,
                                             null,
                                             false,
                                             false,
                                             new Date(),
                                             methodName);
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Removes a tag from the repository.  All the relationships to referenceables are lost.
     *
     * @param serverName   name of the server instances for this request
     * @param userId    userId of user making request.
     * @param tagGUID   unique id for the tag.
     * @param requestBody  null request body.
     *
     * @return void or
     * InvalidParameterException - one of the parameters is invalid or
     * PropertyServerException - there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse   deleteTag(String          serverName,
                                    String          userId,
                                    String          tagGUID,
                                    NullRequestBody requestBody)
    {
        final String methodName           = "deleteTag";
        final String tagGUIDParameterName = "tagGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse  response = new VoidResponse();
        AuditLog      auditLog = null;

        try
        {
            InformalTagHandler<InformalTagElement> handler = instanceHandler.getInformalTagHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            handler.deleteTag(userId,
                              null,
                              null,
                              tagGUID,
                              tagGUIDParameterName,
                              false,
                              false,
                              new Date(),
                              methodName);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Return the tag for the supplied unique identifier (guid).
     *
     * @param serverName   name of the server instances for this request
     * @param userId userId of the user making the request.
     * @param guid unique identifier of the tag.
     *
     * @return Tag object or
     * InvalidParameterException - one of the parameters is invalid or
     * PropertyServerException - there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public InformalTagResponse getTag(String serverName,
                                      String userId,
                                      String guid)
    {
        final String methodName = "getTag";
        final String guidParameterName = "guid";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        InformalTagResponse response = new InformalTagResponse();
        AuditLog            auditLog = null;

        try
        {
            InformalTagHandler<InformalTagElement>  handler = instanceHandler.getInformalTagHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            response.setTag(handler.getTag(userId,
                                           guid,
                                           guidParameterName,
                                           false,
                                           false,
                                           new Date(),
                                           methodName));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Return the list of tags exactly matching the supplied name.
     *
     * @param serverName name of the server instances for this request
     * @param userId the name of the calling user.
     * @param requestBody name of tag.
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return tag list or
     * InvalidParameterException - one of the parameters is invalid or
     * PropertyServerException - there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public InformalTagsResponse getTagsByName(String          serverName,
                                              String          userId,
                                              NameRequestBody requestBody,
                                              int             startFrom,
                                              int             pageSize)
    {
        final String methodName = "getTagsByName";
        final String nameParameterName = "tagName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        InformalTagsResponse response = new InformalTagsResponse();
        AuditLog             auditLog = null;

        try
        {
            InformalTagHandler<InformalTagElement> handler = instanceHandler.getInformalTagHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setTags(handler.getTagsByName(userId,
                                                       requestBody.getName(),
                                                       nameParameterName,
                                                       startFrom,
                                                       pageSize,
                                                       false,
                                                       false,
                                                       new Date(),
                                                       methodName));
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Return the list of the calling user's private tags exactly matching the supplied name.
     *
     * @param serverName name of the server instances for this request
     * @param userId the name of the calling user.
     * @param requestBody name of tag.
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return tag list or
     * InvalidParameterException - one of the parameters is invalid or
     * PropertyServerException - there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public InformalTagsResponse getMyTagsByName(String          serverName,
                                                String          userId,
                                                NameRequestBody requestBody,
                                                int             startFrom,
                                                int             pageSize)
    {
        final String methodName = "getMyTagsByName";
        final String nameParameterName = "tagName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        InformalTagsResponse response = new InformalTagsResponse();
        AuditLog             auditLog = null;

        try
        {
            InformalTagHandler<InformalTagElement> handler = instanceHandler.getInformalTagHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setTags(handler.getMyTagsByName(userId,
                                                         requestBody.getName(),
                                                         nameParameterName,
                                                         startFrom,
                                                         pageSize,
                                                         false,
                                                         false,
                                                         new Date(),
                                                         methodName));
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Return the list of tags containing the supplied string in either the name or description.
     *
     * @param serverName name of the server instances for this request
     * @param userId the name of the calling user.
     * @param requestBody name of tag.  This may include wild card characters.
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return tag list or
     * InvalidParameterException - one of the parameters is invalid or
     * PropertyServerException - there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public InformalTagsResponse findTags(String                  serverName,
                                         String                  userId,
                                         SearchStringRequestBody requestBody,
                                         int                     startFrom,
                                         int                     pageSize)
    {
        final String methodName = "findTags";
        final String nameParameterName = "tagName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        InformalTagsResponse response = new InformalTagsResponse();
        AuditLog             auditLog = null;

        try
        {
            InformalTagHandler<InformalTagElement> handler = instanceHandler.getInformalTagHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setTags(handler.findTags(userId,
                                                  requestBody.getSearchString(),
                                                  nameParameterName,
                                                  startFrom,
                                                  pageSize,
                                                  false,
                                                  false,
                                                  new Date(),
                                                  methodName));
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Return the list of the calling user's private tags containing the supplied string in either the name or description.
     *
     * @param serverName name of the server instances for this request
     * @param userId the name of the calling user.
     * @param requestBody name of tag.  This may include wild card characters.
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return tag list or
     * InvalidParameterException - one of the parameters is invalid or
     * PropertyServerException - there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public InformalTagsResponse findMyTags(String                  serverName,
                                           String                  userId,
                                           SearchStringRequestBody requestBody,
                                           int                     startFrom,
                                           int                     pageSize)
    {
        final String methodName = "findMyTags";
        final String nameParameterName = "tagName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        InformalTagsResponse response = new InformalTagsResponse();
        AuditLog             auditLog = null;

        try
        {
            InformalTagHandler<InformalTagElement> handler = instanceHandler.getInformalTagHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setTags(handler.findMyTags(userId,
                                                    requestBody.getSearchString(),
                                                    nameParameterName,
                                                    startFrom,
                                                    pageSize,
                                                    false,
                                                    false,
                                                    new Date(),
                                                    methodName));
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Adds a tag (either private of public) to an element.
     *
     * @param serverName   name of the server instances for this request
     * @param userId       userId of user making request.
     * @param elementGUID    unique id for the element.
     * @param tagGUID      unique id of the tag.
     * @param requestBody  feedback request body.
     *
     * @return void or
     * InvalidParameterException - one of the parameters is invalid or
     * PropertyServerException - there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public VoidResponse   addTagToElement(String              serverName,
                                          String              userId,
                                          String              elementGUID,
                                          String              tagGUID,
                                          FeedbackProperties requestBody)
    {
        final String methodName             = "addTagToElement";
        final String elementGUIDParameterName = "elementGUID";
        final String tagGUIDParameterName   = "tagGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        boolean  isPublic = false;

        if (requestBody != null)
        {
            isPublic = requestBody.getIsPublic();
        }

        VoidResponse  response = new VoidResponse();
        AuditLog      auditLog = null;

        try
        {
            InformalTagHandler<InformalTagElement> handler = instanceHandler.getInformalTagHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            handler.addTagToElement(userId,
                                    null,
                                    null,
                                    elementGUID,
                                    elementGUIDParameterName,
                                    OpenMetadataType.ASSET.typeName,
                                    tagGUID,
                                    tagGUIDParameterName,
                                    instanceHandler.getSupportedZones(userId, serverName, methodName),
                                    isPublic,
                                    null,
                                    null,
                                    false,
                                    false,
                                    new Date(),
                                    methodName);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Removes a tag from the element that was added by this user.
     *
     * @param serverName   name of the server instances for this request
     * @param userId    userId of user making request.
     * @param elementGUID unique id for the element.
     * @param tagGUID   unique id for the tag.
     * @param requestBody  null request body needed for correct protocol exchange.
     *
     * @return void or
     * InvalidParameterException - one of the parameters is invalid or
     * PropertyServerException - there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse   removeTagFromElement(String          serverName,
                                               String          userId,
                                               String          elementGUID,
                                               String          tagGUID,
                                               NullRequestBody requestBody)
    {
        final String   methodName  = "removeTagFromElement";
        final String   elementGUIDParameterName  = "elementGUID";
        final String   tagGUIDParameterName  = "tagGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse  response = new VoidResponse();
        AuditLog      auditLog = null;

        try
        {
            InformalTagHandler<InformalTagElement> handler = instanceHandler.getInformalTagHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            handler.removeTagFromElement(userId,
                                         null,
                                         null,
                                         elementGUID,
                                         elementGUIDParameterName,
                                         OpenMetadataType.ASSET.typeName,
                                         tagGUID,
                                         tagGUIDParameterName,
                                         instanceHandler.getSupportedZones(userId, serverName, methodName),
                                         false,
                                         false,
                                         new Date(),
                                         methodName);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Return the list of unique identifiers for elements that are linked to a specific tag either directly, or via one
     * of its schema elements.
     *
     * @param serverName   name of the server instances for this request
     * @param userId the name of the calling user.
     * @param tagGUID unique identifier of tag.
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return element guid list or
     * InvalidParameterException the userId is null or invalid or
     * PropertyServerException there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public GUIDListResponse getElementsByTag(String serverName,
                                             String userId,
                                             String tagGUID,
                                             int    startFrom,
                                             int    pageSize)
    {
        final String methodName           = "getElementsByTag";
        final String tagGUIDParameterName = "tagGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDListResponse  response = new GUIDListResponse();
        AuditLog          auditLog = null;

        try
        {
            InformalTagHandler<InformalTagElement> handler = instanceHandler.getInformalTagHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            response.setGUIDs(handler.getAttachedElementGUIDs(userId,
                                                              tagGUID,
                                                              tagGUIDParameterName,
                                                              OpenMetadataType.INFORMAL_TAG_TYPE_NAME,
                                                              null,
                                                              null,
                                                              OpenMetadataType.REFERENCEABLE.typeName,
                                                              false,
                                                              false,
                                                              startFrom,
                                                              pageSize,
                                                              new Date(),
                                                              methodName));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /* =====================================================================================================================
     * A note log maintains an ordered list of notes.  It can be used to support release note, blogs and similar
     * broadcast information.  Notelogs are typically maintained by the owners/stewards of an element.
     */

    /**
     * Create a new metadata element to represent a note log and attach it to an element (if supplied).
     * Any supplied element becomes the note log's anchor, causing the note log to be deleted if/when the element is deleted.
     *
     * @param serverName   name of the server instances for this request
     * @param userId calling user
     * @param assetManagerIsHome      ensure that only the asset manager can update this element
     * @param elementGUID unique identifier of the element where the note log is located
     * @param isPublic                 is this element visible to other people.
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties to control the type of the request
     *
     * @return unique identifier of the new note log or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public  GUIDResponse createNoteLog(String                         serverName,
                                       String                         userId,
                                       String                         elementGUID,
                                       boolean                        assetManagerIsHome,
                                       boolean                        isPublic,
                                       boolean                        forLineage,
                                       boolean                        forDuplicateProcessing,
                                       ReferenceableUpdateRequestBody requestBody)
    {
        final String methodName = "createNoteLog";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getElementProperties() instanceof NoteLogProperties properties)
                {
                    NoteLogExchangeHandler handler = instanceHandler.getNoteLogHandler(userId, serverName, methodName);

                    response.setGUID(handler.createNoteLog(userId,
                                                           elementGUID,
                                                           requestBody.getMetadataCorrelationProperties(),
                                                           assetManagerIsHome,
                                                           properties,
                                                           isPublic,
                                                           forLineage,
                                                           forDuplicateProcessing,
                                                           requestBody.getEffectiveTime(),
                                                           methodName));
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(NoteLogProperties.class.getName(), methodName);
                }
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Update the metadata element representing a note log.
     *
     * @param serverName   name of the server instances for this request
     * @param userId calling user
     * @param noteLogGUID unique identifier of the metadata element to update
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param isPublic                 is this element visible to other people.
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody new properties for the metadata element
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse updateNoteLog(String                         serverName,
                                      String                         userId,
                                      String                         noteLogGUID,
                                      boolean                        isMergeUpdate,
                                      boolean                        isPublic,
                                      boolean                        forLineage,
                                      boolean                        forDuplicateProcessing,
                                      ReferenceableUpdateRequestBody requestBody)
    {
        final String methodName = "updateNoteLog";

        RESTCallToken token      = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getElementProperties() instanceof NoteLogProperties properties)
                {
                    NoteLogExchangeHandler handler = instanceHandler.getNoteLogHandler(userId, serverName, methodName);

                    handler.updateNoteLog(userId,
                                          requestBody.getMetadataCorrelationProperties(),
                                          noteLogGUID,
                                          properties,
                                          isMergeUpdate,
                                          isPublic,
                                          forLineage,
                                          forDuplicateProcessing,
                                          requestBody.getEffectiveTime(),
                                          methodName);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(NoteLogProperties.class.getName(), methodName);
                }
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Remove the metadata element representing a note log.
     *
     * @param serverName   name of the server instances for this request
     * @param userId calling user
     * @param noteLogGUID unique identifier of the metadata element to remove
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties to help with the mapping of the elements in the external asset manager and open metadata
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse removeNoteLog(String                         serverName,
                                      String                         userId,
                                      String                         noteLogGUID,
                                      boolean                        forLineage,
                                      boolean                        forDuplicateProcessing,
                                      ReferenceableUpdateRequestBody requestBody)
    {
        final String methodName = "removeNoteLog";

        RESTCallToken token      = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            NoteLogExchangeHandler handler = instanceHandler.getNoteLogHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.removeNoteLog(userId,
                                      requestBody.getMetadataCorrelationProperties(),
                                      noteLogGUID,
                                      forLineage,
                                      forDuplicateProcessing,
                                      requestBody.getEffectiveTime(),
                                      methodName);
            }
            else
            {
                handler.removeNoteLog(userId,
                                      null,
                                      noteLogGUID,
                                      forLineage,
                                      forDuplicateProcessing,
                                      null,
                                      methodName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Retrieve the list of note log metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param serverName   name of the server instances for this request
     * @param userId calling user
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody string to find in the properties and correlators
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public NoteLogElementsResponse findNoteLogs(String                  serverName,
                                                String                  userId,
                                                int                     startFrom,
                                                int                     pageSize,
                                                boolean                 forLineage,
                                                boolean                 forDuplicateProcessing,
                                                SearchStringRequestBody requestBody)
    {
        final String methodName = "findNoteLogs";
        final String searchStringParameterName = "searchString";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        NoteLogElementsResponse response = new NoteLogElementsResponse();
        AuditLog                auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                NoteLogExchangeHandler handler = instanceHandler.getNoteLogHandler(userId, serverName, methodName);

                response.setElementList(handler.findNoteLogs(userId,
                                                             requestBody.getAssetManagerGUID(),
                                                             requestBody.getAssetManagerName(),
                                                             requestBody.getSearchString(),
                                                             searchStringParameterName,
                                                             startFrom,
                                                             pageSize,
                                                             requestBody.getEffectiveTime(),
                                                             forLineage,
                                                             forDuplicateProcessing,
                                                             methodName));
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Retrieve the list of note log metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param serverName   name of the server instances for this request
     * @param userId calling user
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody name to search for and correlators
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public NoteLogElementsResponse getNoteLogsByName(String          serverName,
                                                     String          userId,
                                                     int             startFrom,
                                                     int             pageSize,
                                                     boolean         forLineage,
                                                     boolean         forDuplicateProcessing,
                                                     NameRequestBody requestBody)
    {
        final String methodName        = "getNoteLogsByName";
        final String nameParameterName = "name";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        NoteLogElementsResponse response = new NoteLogElementsResponse();
        AuditLog                auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                NoteLogExchangeHandler handler = instanceHandler.getNoteLogHandler(userId, serverName, methodName);

                response.setElementList(handler.getNoteLogsByName(userId,
                                                                  requestBody.getAssetManagerGUID(),
                                                                  requestBody.getAssetManagerName(),
                                                                  requestBody.getName(),
                                                                  nameParameterName,
                                                                  startFrom,
                                                                  pageSize,
                                                                  forLineage,
                                                                  forDuplicateProcessing,
                                                                  requestBody.getEffectiveTime(),
                                                                  methodName));
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }



    /**
     * Retrieve the list of note log metadata elements attached to the element.
     *
     * @param serverName   name of the server instances for this request
     * @param userId calling user
     * @param elementGUID unique identifier of the note log of interest
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody asset manager identifiers
     *
     * @return list of associated metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public NoteLogElementsResponse getNoteLogsForElement(String                        serverName,
                                                         String                        userId,
                                                         String                        elementGUID,
                                                         int                           startFrom,
                                                         int                           pageSize,
                                                         boolean                       forLineage,
                                                         boolean                       forDuplicateProcessing,
                                                         EffectiveTimeQueryRequestBody requestBody)
    {
        final String methodName = "getNotesForNoteLog";
        final String elementGUIDParameter = "elementGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        NoteLogElementsResponse response = new NoteLogElementsResponse();
        AuditLog                auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            NoteLogExchangeHandler handler = instanceHandler.getNoteLogHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElementList(handler.getNoteLogsForElement(userId,
                                                                      requestBody.getAssetManagerGUID(),
                                                                      requestBody.getAssetManagerName(),
                                                                      elementGUID,
                                                                      elementGUIDParameter,
                                                                      startFrom,
                                                                      pageSize,
                                                                      forLineage,
                                                                      forDuplicateProcessing,
                                                                      requestBody.getEffectiveTime(),
                                                                      methodName));
            }
            else
            {
                response.setElementList(handler.getNoteLogsForElement(userId,
                                                                      null,
                                                                      null,
                                                                      elementGUID,
                                                                      elementGUIDParameter,
                                                                      startFrom,
                                                                      pageSize,
                                                                      forLineage,
                                                                      forDuplicateProcessing,
                                                                      null,
                                                                      methodName));
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }



    /**
     * Retrieve the note log metadata element with the supplied unique identifier.
     *
     * @param serverName   name of the server instances for this request
     * @param userId calling user
     * @param noteLogGUID unique identifier of the requested metadata element
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody correlators
     *
     * @return requested metadata element or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public NoteLogElementResponse getNoteLogByGUID(String                        serverName,
                                                   String                        userId,
                                                   String                        noteLogGUID,
                                                   boolean                       forLineage,
                                                   boolean                       forDuplicateProcessing,
                                                   EffectiveTimeQueryRequestBody requestBody)
    {
        final String methodName = "getNoteLogByGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        NoteLogElementResponse response = new NoteLogElementResponse();
        AuditLog                auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                NoteLogExchangeHandler handler = instanceHandler.getNoteLogHandler(userId, serverName, methodName);

                response.setElement(handler.getNoteLogByGUID(userId,
                                                             requestBody.getAssetManagerGUID(),
                                                             requestBody.getAssetManagerName(),
                                                             noteLogGUID,
                                                             forLineage,
                                                             forDuplicateProcessing,
                                                             requestBody.getEffectiveTime(),
                                                             methodName));
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /* ===============================================================================
     * A element typically contains many notes, linked with relationships.
     */

    /**
     * Create a new metadata element to represent a note.
     *
     * @param serverName   name of the server instances for this request
     * @param userId calling user
     * @param assetManagerIsHome      ensure that only the asset manager can update this element
     * @param noteLogGUID unique identifier of the element where the note is located
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties to help with the mapping of the elements in the external asset manager and open metadata
     *
     * @return unique identifier of the new metadata element for the note or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GUIDResponse createNote(String                         serverName,
                                   String                         userId,
                                   boolean                        assetManagerIsHome,
                                   String                         noteLogGUID,
                                   boolean                        forLineage,
                                   boolean                        forDuplicateProcessing,
                                   ReferenceableUpdateRequestBody requestBody)
    {
        final String methodName = "createNote";


        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getElementProperties() instanceof NoteProperties properties)
                {
                    NoteLogExchangeHandler handler = instanceHandler.getNoteLogHandler(userId, serverName, methodName);

                    response.setGUID(handler.createNote(userId,
                                                        noteLogGUID,
                                                        requestBody.getMetadataCorrelationProperties(),
                                                        assetManagerIsHome,
                                                        properties,
                                                        requestBody.getEffectiveTime(),
                                                        forLineage,
                                                        forDuplicateProcessing,
                                                        methodName));
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(NoteProperties.class.getName(), methodName);
                }
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Update the properties of the metadata element representing a note.
     *
     * @param userId calling user
     * @param serverName   name of the server instances for this request
     * @param noteGUID unique identifier of the note to update
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties to help with the mapping of the elements in the external asset manager and open metadata
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse updateNote(String                         serverName,
                                   String                         userId,
                                   String                         noteGUID,
                                   boolean                        isMergeUpdate,
                                   boolean                        forLineage,
                                   boolean                        forDuplicateProcessing,
                                   ReferenceableUpdateRequestBody requestBody)
    {
        final String methodName = "updateNote";

        RESTCallToken token      = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getElementProperties() instanceof NoteProperties properties)
                {
                    NoteLogExchangeHandler handler = instanceHandler.getNoteLogHandler(userId, serverName, methodName);

                    handler.updateNote(userId,
                                       requestBody.getMetadataCorrelationProperties(),
                                       noteGUID,
                                       properties,
                                       isMergeUpdate,
                                       forLineage,
                                       forDuplicateProcessing,
                                       requestBody.getEffectiveTime(),
                                       methodName);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(NoteProperties.class.getName(), methodName);
                }
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Remove the metadata element representing a note.
     *
     * @param serverName   name of the server instances for this request
     * @param userId calling user
     * @param noteGUID unique identifier of the metadata element to remove
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties to help with the mapping of the elements in the external asset manager and open metadata
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse removeNote(String                         serverName,
                                   String                         userId,
                                   String                         noteGUID,
                                   boolean                        forLineage,
                                   boolean                        forDuplicateProcessing,
                                   ReferenceableUpdateRequestBody requestBody)
    {
        final String methodName = "removeNote";

        RESTCallToken token      = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            NoteLogExchangeHandler handler = instanceHandler.getNoteLogHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.removeNote(userId,
                                   requestBody.getMetadataCorrelationProperties(),
                                   noteGUID,
                                   forLineage,
                                   forDuplicateProcessing,
                                   requestBody.getEffectiveTime(), methodName);
            }
            else
            {
                handler.removeNote(userId,
                                   null,
                                   noteGUID,
                                   forLineage,
                                   forDuplicateProcessing,
                                   null,
                                   methodName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Retrieve the list of note metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param serverName   name of the server instances for this request
     * @param userId calling user
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody string to find in the properties and correlators
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public NoteElementsResponse findNotes(String                  serverName,
                                          String                  userId,
                                          int                     startFrom,
                                          int                     pageSize,
                                          boolean                 forLineage,
                                          boolean                 forDuplicateProcessing,
                                          SearchStringRequestBody requestBody)
    {
        final String methodName                = "findNotes";
        final String searchStringParameterName = "searchString";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        NoteElementsResponse response = new NoteElementsResponse();
        AuditLog                auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                NoteLogExchangeHandler handler = instanceHandler.getNoteLogHandler(userId, serverName, methodName);

                response.setElementList(handler.findNotes(userId,
                                                          requestBody.getAssetManagerGUID(),
                                                          requestBody.getAssetManagerName(),
                                                          requestBody.getSearchString(),
                                                          searchStringParameterName,
                                                          startFrom,
                                                          pageSize,
                                                          forLineage,
                                                          forDuplicateProcessing,
                                                          requestBody.getEffectiveTime(),
                                                          methodName));
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Retrieve the list of notes associated with a note log.
     *
     * @param serverName   name of the server instances for this request
     * @param userId calling user
     * @param noteLogGUID unique identifier of the note log of interest
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody asset manager identifiers
     *
     * @return list of associated metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public NoteElementsResponse getNotesForNoteLog(String                           serverName,
                                                   String                        userId,
                                                   String                        noteLogGUID,
                                                   int                           startFrom,
                                                   int                           pageSize,
                                                   boolean                       forLineage,
                                                   boolean                       forDuplicateProcessing,
                                                   EffectiveTimeQueryRequestBody requestBody)
    {
        final String methodName = "getNotesForNoteLog";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        NoteElementsResponse response = new NoteElementsResponse();
        AuditLog                auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            NoteLogExchangeHandler handler = instanceHandler.getNoteLogHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElementList(handler.getNotesForNoteLog(userId,
                                                                   requestBody.getAssetManagerGUID(),
                                                                   requestBody.getAssetManagerName(),
                                                                   noteLogGUID,
                                                                   startFrom,
                                                                   pageSize,
                                                                   forLineage,
                                                                   forDuplicateProcessing,
                                                                   requestBody.getEffectiveTime(),
                                                                   methodName));
            }
            else
            {
                response.setElementList(handler.getNotesForNoteLog(userId,
                                                                   null,
                                                                   null,
                                                                   noteLogGUID,
                                                                   startFrom,
                                                                   pageSize,
                                                                   forLineage,
                                                                   forDuplicateProcessing,
                                                                   null,
                                                                   methodName));
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Retrieve the note metadata element with the supplied unique identifier.
     *
     * @param serverName   name of the server instances for this request
     * @param userId calling user
     * @param noteGUID unique identifier of the requested metadata element
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody asset manager identifiers
     *
     * @return matching metadata element or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public NoteElementResponse getNoteByGUID(String                        serverName,
                                             String                        userId,
                                             String                        noteGUID,
                                             boolean                       forLineage,
                                             boolean                       forDuplicateProcessing,
                                             EffectiveTimeQueryRequestBody requestBody)
    {
        final String methodName = "getNoteByGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        NoteElementResponse response = new NoteElementResponse();
        AuditLog            auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                NoteLogExchangeHandler handler = instanceHandler.getNoteLogHandler(userId, serverName, methodName);

                response.setElement(handler.getNoteByGUID(userId,
                                                          requestBody.getAssetManagerGUID(),
                                                          requestBody.getAssetManagerName(),
                                                          noteGUID,
                                                          forLineage,
                                                          forDuplicateProcessing,
                                                          requestBody.getEffectiveTime(),
                                                          methodName));
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }
}