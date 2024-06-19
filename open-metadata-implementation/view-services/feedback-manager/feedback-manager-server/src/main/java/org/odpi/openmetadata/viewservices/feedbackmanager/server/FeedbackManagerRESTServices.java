/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project */
/* Copyright Contributors to the ODPi Egeria category. */
package org.odpi.openmetadata.viewservices.feedbackmanager.server;

import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.tokencontroller.TokenController;
import org.odpi.openmetadata.viewservices.feedbackmanager.handler.CollaborationManagerHandler;
import org.odpi.openmetadata.viewservices.feedbackmanager.properties.*;
import org.odpi.openmetadata.viewservices.feedbackmanager.rest.*;
import org.odpi.openmetadata.viewservices.feedbackmanager.rest.RelationshipRequestBody;
import org.slf4j.LoggerFactory;

import java.util.Date;


/**
 * The FeedbackManagerRESTServices provides the implementation of the Feedback Manager Open Metadata View Service (OMVS).
 * This interface provides view interfaces for glossary UIs.
 */

public class FeedbackManagerRESTServices extends TokenController
{
    private static final FeedbackManagerInstanceHandler instanceHandler = new FeedbackManagerInstanceHandler();

    private static final RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();

    private static final RESTCallLogger restCallLogger = new RESTCallLogger(LoggerFactory.getLogger(FeedbackManagerRESTServices.class),
                                                                            instanceHandler.getServiceName());


    /**
     * Default constructor
     */
    public FeedbackManagerRESTServices()
    {
    }
    

    /**
     * Adds a star rating and optional review text to the element.  If the user has already attached
     * a rating then the original one is over-ridden.
     *
     * @param serverName name of the server instances for this request
     * @param guid        String - unique id for the element.
     * @param isPublic is this visible to other people
     * @param viewServiceURLMarker optional view service URL marker (overrides accessServiceURLMarker)
     * @param accessServiceURLMarker optional access service URL marker used to identify which back end service to call
     * @param requestBody containing the StarRating and user review of referenceable (probably element).
     *
     * @return void or
     * InvalidParameterException - one of the parameters is null or invalid or
     * PropertyServerException - there is a problem adding the element properties to
     *                                   the metadata repository or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public VoidResponse addRatingToElement(String           serverName,
                                           String           guid,
                                           boolean          isPublic,
                                           String           viewServiceURLMarker,
                                           String           accessServiceURLMarker,
                                           RatingProperties requestBody)
    {
        final String methodName = "addRatingToElement";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse  response = new VoidResponse();
        AuditLog      auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            if (requestBody != null)
            {
                CollaborationManagerHandler handler = instanceHandler.getCollaborationManagerHandler(userId, serverName, viewServiceURLMarker, accessServiceURLMarker, methodName);

                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

                handler.addRatingToElement(userId, guid, isPublic, requestBody, new Date());
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName, RatingProperties.class.getName());
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
     * @param guid        String - unique id for the rating object
     * @param viewServiceURLMarker optional view service URL marker (overrides accessServiceURLMarker)
     * @param accessServiceURLMarker optional access service URL marker used to identify which back end service to call
     * @param requestBody optional effective time
     *
     * @return void or
     * InvalidParameterException - one of the parameters is null or invalid or
     * PropertyServerException - there is a problem updating the element properties in
     *                                   the metadata repository or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public VoidResponse removeRatingFromElement(String                        serverName,
                                                String                        guid,
                                                String                        viewServiceURLMarker,
                                                String                        accessServiceURLMarker,
                                                EffectiveTimeQueryRequestBody requestBody)
    {
        final String methodName = "removeRatingFromElement";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse  response = new VoidResponse();
        AuditLog      auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            CollaborationManagerHandler handler = instanceHandler.getCollaborationManagerHandler(userId, serverName, viewServiceURLMarker, accessServiceURLMarker, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.removeRatingFromElement(userId, guid, requestBody.getEffectiveTime());
            }
            else
            {
                handler.removeRatingFromElement(userId, guid, new Date());
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
     * Adds a "LikeProperties" to the element.
     *
     * @param serverName name of the server instances for this request
     * @param guid        String - unique id for the element.
     * @param isPublic is this visible to other people
     * @param viewServiceURLMarker optional view service URL marker (overrides accessServiceURLMarker)
     * @param accessServiceURLMarker optional access service URL marker used to identify which back end service to call
     * @param requestBody optional effective time
     *
     * @return void or
     * InvalidParameterException - one of the parameters is null or invalid or
     * PropertyServerException - there is a problem adding the element properties to
     *                                   the metadata repository or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public VoidResponse addLikeToElement(String                        serverName,
                                         String                        guid,
                                         boolean                       isPublic,
                                         String                        viewServiceURLMarker,
                                         String                        accessServiceURLMarker,
                                         EffectiveTimeQueryRequestBody requestBody)
    {
        final String methodName        = "addLikeToElement";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse  response = new VoidResponse();
        AuditLog      auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            CollaborationManagerHandler handler = instanceHandler.getCollaborationManagerHandler(userId, serverName, viewServiceURLMarker, accessServiceURLMarker, methodName);

            if (requestBody != null)
            {
                handler.addLikeToElement(userId, guid, isPublic, requestBody.getEffectiveTime());
            }
            else
            {
                handler.addLikeToElement(userId, guid, isPublic, new Date());
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
     * @param guid  String - unique id for the like object
     * @param viewServiceURLMarker optional view service URL marker (overrides accessServiceURLMarker)
     * @param accessServiceURLMarker optional access service URL marker used to identify which back end service to call
     * @param requestBody optional effective time
     *
     * @return void or
     * InvalidParameterException - one of the parameters is null or invalid or
     * PropertyServerException - there is a problem updating the element properties in
     *                                   the metadata repository or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse removeLikeFromElement(String                        serverName,
                                              String                        guid,
                                              String                        viewServiceURLMarker,
                                              String                        accessServiceURLMarker,
                                              EffectiveTimeQueryRequestBody requestBody)
    {
        final String methodName        = "removeLikeFromElement";
        final String guidParameterName = "guid";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse  response = new VoidResponse();
        AuditLog      auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            CollaborationManagerHandler handler = instanceHandler.getCollaborationManagerHandler(userId, serverName, viewServiceURLMarker, accessServiceURLMarker, methodName);

            if (requestBody != null)
            {
                handler.removeLikeFromElement(userId, guid, requestBody.getEffectiveTime());
            }
            else
            {
                handler.removeLikeFromElement(userId, guid, null);
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
     * Adds a comment to the element.
     *
     * @param serverName name of the server instances for this request
     * @param elementGUID  String - unique id for the element.
     * @param isPublic is this visible to other people
     * @param viewServiceURLMarker optional view service URL marker (overrides accessServiceURLMarker)
     * @param accessServiceURLMarker optional access service URL marker used to identify which back end service to call
     * @param requestBody containing type of comment enum and the text of the comment.
     *
     * @return elementGUID for new comment object or
     * InvalidParameterException - one of the parameters is null or invalid or
     * PropertyServerException - there is a problem adding the element properties to
     *                                   the metadata repository or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public GUIDResponse addCommentToElement(String                         serverName,
                                            String                         elementGUID,
                                            boolean                        isPublic,
                                            String                         viewServiceURLMarker,
                                            String                         accessServiceURLMarker,
                                            ReferenceableUpdateRequestBody requestBody)
    {
        final String methodName = "addCommentToElement";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        GUIDResponse  response = new GUIDResponse();
        AuditLog      auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            CollaborationManagerHandler handler = instanceHandler.getCollaborationManagerHandler(userId, serverName, viewServiceURLMarker, accessServiceURLMarker, methodName);

            if (requestBody != null)
            {
                if (requestBody.getElementProperties() instanceof CommentProperties commentProperties)
                {

                    response.setGUID(handler.addCommentToElement(userId, elementGUID, isPublic, commentProperties));
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(CommentProperties.class.getName(), methodName);
                }
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName, ReferenceableUpdateRequestBody.class.getName());
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
     * @param elementGUID  String - unique id for the anchor element.
     * @param commentGUID  String - unique id for an existing comment.  Used to add a reply to a comment.
     * @param isPublic is this visible to other people
     * @param viewServiceURLMarker optional view service URL marker (overrides accessServiceURLMarker)
     * @param accessServiceURLMarker optional access service URL marker used to identify which back end service to call
     * @param requestBody  containing type of comment enum and the text of the comment.
     *
     * @return guid for new comment object or
     * InvalidParameterException - one of the parameters is null or invalid or
     * PropertyServerException - there is a problem adding the element properties to
     *                                   the metadata repository or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public GUIDResponse addCommentReply(String                         serverName,
                                        String                         elementGUID,
                                        String                         commentGUID,
                                        boolean                        isPublic,
                                        String                         viewServiceURLMarker,
                                        String                         accessServiceURLMarker,
                                        ReferenceableUpdateRequestBody requestBody)
    {
        final String methodName = "addCommentReply";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        GUIDResponse  response = new GUIDResponse();
        AuditLog      auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getElementProperties() instanceof CommentProperties commentProperties)
                {
                    CollaborationManagerHandler handler = instanceHandler.getCollaborationManagerHandler(userId, serverName, viewServiceURLMarker, accessServiceURLMarker, methodName);

                    response.setGUID(handler.addCommentReply(userId,
                                                             elementGUID,
                                                             commentGUID,
                                                             isPublic,
                                                             commentProperties));
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(CommentProperties.class.getName(), methodName);
                }
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName, ReferenceableUpdateRequestBody.class.getName());
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
     * @param commentGUID  unique identifier for the comment to change.
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param viewServiceURLMarker optional view service URL marker (overrides accessServiceURLMarker)
     * @param accessServiceURLMarker optional access service URL marker used to identify which back end service to call
     * @param requestBody  containing type of comment enum and the text of the comment.
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid.
     * PropertyServerException There is a problem updating the element properties in the metadata repository.
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse   updateComment(String                         serverName,
                                        String                         commentGUID,
                                        boolean                        isMergeUpdate,
                                        String                         viewServiceURLMarker,
                                        String                         accessServiceURLMarker,
                                        ReferenceableUpdateRequestBody requestBody)
    {
        final String methodName = "updateComment";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse  response = new VoidResponse();
        AuditLog      auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getElementProperties() instanceof CommentProperties commentProperties)
                {
                    CollaborationManagerHandler handler = instanceHandler.getCollaborationManagerHandler(userId, serverName, viewServiceURLMarker, accessServiceURLMarker, methodName);

                    handler.updateComment(userId,
                                          commentGUID,
                                          isMergeUpdate,
                                          commentProperties,
                                          requestBody.getEffectiveTime());
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(CommentProperties.class.getName(), methodName);
                }
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName, ReferenceableUpdateRequestBody.class.getName());
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
     * Update an existing comment's visibility.
     *
     * @param serverName   name of the server instances for this request.
     * @param parentGUID  String - unique id for the attached element.
     * @param commentGUID  unique identifier for the comment to change.
     * @param isPublic is this visible to other people
     * @param viewServiceURLMarker optional view service URL marker (overrides accessServiceURLMarker)
     * @param accessServiceURLMarker optional access service URL marker used to identify which back end service to call
     * @param requestBody optional effective time
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid.
     * PropertyServerException There is a problem updating the element properties in the metadata repository.
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse   updateCommentVisibility(String                        serverName,
                                                  String                        parentGUID,
                                                  String                        commentGUID,
                                                  boolean                       isPublic,
                                                  String                        viewServiceURLMarker,
                                                  String                        accessServiceURLMarker,
                                                  EffectiveTimeQueryRequestBody requestBody)
    {
        final String methodName = "updateCommentVisibility";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse  response = new VoidResponse();
        AuditLog      auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            CollaborationManagerHandler handler = instanceHandler.getCollaborationManagerHandler(userId, serverName, viewServiceURLMarker, accessServiceURLMarker, methodName);

            if (requestBody != null)
            {
                handler.updateCommentVisibility(userId, parentGUID, commentGUID, isPublic, requestBody.getEffectiveTime());
            }
            else
            {
                handler.updateCommentVisibility(userId, parentGUID, commentGUID, isPublic, new Date());
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
     * @param questionCommentGUID unique identifier of the comment containing the question
     * @param answerCommentGUID unique identifier of the comment containing the accepted answer
     * @param isPublic is this visible to other people
     * @param viewServiceURLMarker optional view service URL marker (overrides accessServiceURLMarker)
     * @param accessServiceURLMarker optional access service URL marker used to identify which back end service to call
     * @param requestBody optional effective time
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse setupAcceptedAnswer(String                        serverName,
                                            String                        questionCommentGUID,
                                            String                        answerCommentGUID,
                                            boolean                       isPublic,
                                            String                        viewServiceURLMarker,
                                            String                        accessServiceURLMarker,
                                            EffectiveTimeQueryRequestBody requestBody)
    {
        final String methodName = "setupAcceptedAnswer";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            CollaborationManagerHandler handler = instanceHandler.getCollaborationManagerHandler(userId, serverName, viewServiceURLMarker, accessServiceURLMarker, methodName);

            if (requestBody != null)
            {
                handler.setupAcceptedAnswer(userId,
                                                questionCommentGUID,
                                                answerCommentGUID,
                                                isPublic,
                                                requestBody.getEffectiveTime());
            }
            else
            {
                handler.setupAcceptedAnswer(userId,
                                            questionCommentGUID,
                                            answerCommentGUID,
                                            isPublic,
                                            new Date());
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
     * @param questionCommentGUID unique identifier of the comment containing the question
     * @param answerCommentGUID unique identifier of the comment containing the accepted answer
     * @param viewServiceURLMarker optional view service URL marker (overrides accessServiceURLMarker)
     * @param accessServiceURLMarker optional access service URL marker used to identify which back end service to call
     * @param requestBody optional effective time
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse clearAcceptedAnswer(String                        serverName,
                                            String                        questionCommentGUID,
                                            String                        answerCommentGUID,
                                            String                        viewServiceURLMarker,
                                            String                        accessServiceURLMarker,
                                            EffectiveTimeQueryRequestBody requestBody)
    {
        final String methodName = "clearAcceptedAnswer";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            CollaborationManagerHandler handler = instanceHandler.getCollaborationManagerHandler(userId, serverName, viewServiceURLMarker, accessServiceURLMarker, methodName);

            if (requestBody != null)
            {
                handler.clearAcceptedAnswer(userId, questionCommentGUID, answerCommentGUID, requestBody.getEffectiveTime());
            }
            else
            {
                handler.clearAcceptedAnswer(userId, questionCommentGUID, answerCommentGUID, new Date());
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
     * @param commentGUID  String - unique id for the comment object
     * @param viewServiceURLMarker optional view service URL marker (overrides accessServiceURLMarker)
     * @param accessServiceURLMarker optional access service URL marker used to identify which back end service to call
     * @param requestBody optional effective time
     *
     * @return void or
     * InvalidParameterException - one of the parameters is null or invalid or
     * PropertyServerException - there is a problem updating the element properties in
     *                                   the metadata repository or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public VoidResponse removeCommentFromElement(String                         serverName,
                                                 String                         commentGUID,
                                                 String                         viewServiceURLMarker,
                                                 String                         accessServiceURLMarker,
                                                 EffectiveTimeQueryRequestBody  requestBody)
    {
        final String methodName = "removeElementComment";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse  response = new VoidResponse();
        AuditLog      auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            CollaborationManagerHandler handler = instanceHandler.getCollaborationManagerHandler(userId, serverName, viewServiceURLMarker, accessServiceURLMarker, methodName);

            if (requestBody != null)
            {
                handler.removeComment(userId, commentGUID, requestBody.getEffectiveTime());
            }
            else
            {
                handler.removeComment(userId, commentGUID, new Date());
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
     * @param commentGUID  unique identifier for the comment object.
     * @param viewServiceURLMarker optional view service URL marker (overrides accessServiceURLMarker)
     * @param accessServiceURLMarker optional access service URL marker used to identify which back end service to call
     * @param requestBody optional effective time
     * @return comment properties or
     *  InvalidParameterException one of the parameters is null or invalid.
     *  PropertyServerException there is a problem updating the element properties in the property server.
     *  UserNotAuthorizedException the user does not have permission to perform this request.
     */
    public CommentResponse getCommentByGUID(String                        serverName,
                                            String                        commentGUID,
                                            String                        viewServiceURLMarker,
                                            String                        accessServiceURLMarker,
                                            EffectiveTimeQueryRequestBody requestBody)
    {
        final String methodName = "getComment";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        CommentResponse response = new CommentResponse();
        AuditLog        auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            CollaborationManagerHandler handler = instanceHandler.getCollaborationManagerHandler(userId, serverName, viewServiceURLMarker, accessServiceURLMarker, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElement(handler.getComment(userId, commentGUID, requestBody.getEffectiveTime()));
            }
            else
            {
                response.setElement(handler.getComment(userId, commentGUID, new Date()));
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
     * @param elementGUID    unique identifier for the element that the comments are connected to (maybe a comment too).
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param viewServiceURLMarker optional view service URL marker (overrides accessServiceURLMarker)
     * @param accessServiceURLMarker optional access service URL marker used to identify which back end service to call
     * @param requestBody optional effective time
     * @return list of comments or
     *  InvalidParameterException one of the parameters is null or invalid.
     *  PropertyServerException there is a problem updating the element properties in the property server.
     *  UserNotAuthorizedException the user does not have permission to perform this request.
     */
    public CommentElementsResponse getAttachedComments(String                        serverName,
                                                       String                        elementGUID,
                                                       int                           startFrom,
                                                       int                           pageSize,
                                                       String                        viewServiceURLMarker,
                                                       String                        accessServiceURLMarker,
                                                       EffectiveTimeQueryRequestBody requestBody)
    {
        final String methodName = "getAttachedComments";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        CommentElementsResponse response = new CommentElementsResponse();
        AuditLog                auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            CollaborationManagerHandler handler = instanceHandler.getCollaborationManagerHandler(userId, serverName, viewServiceURLMarker, accessServiceURLMarker, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElementList(handler.getAttachedComments(userId, elementGUID, startFrom, pageSize, requestBody.getEffectiveTime()));
            }
            else
            {
                response.setElementList(handler.getAttachedComments(userId, elementGUID, startFrom, pageSize, new Date()));
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
     * Retrieve the list of comment metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param serverName name of the server to route the request to
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param startsWith does the value start with the supplied string?
     * @param endsWith does the value end with the supplied string?
     * @param ignoreCase should the search ignore case?
     * @param viewServiceURLMarker optional view service URL marker (overrides accessServiceURLMarker)
     * @param accessServiceURLMarker optional access service URL marker used to identify which back end service to call
     * @param requestBody string to find in the properties
    *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public CommentElementsResponse findComments(String                  serverName,
                                                int                     startFrom,
                                                int                     pageSize,
                                                boolean                 startsWith,
                                                boolean                 endsWith,
                                                boolean                 ignoreCase,
                                                String                  viewServiceURLMarker,
                                                String                  accessServiceURLMarker,
                                                FilterRequestBody       requestBody)
    {
        final String methodName = "findComments";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        CommentElementsResponse response = new CommentElementsResponse();
        AuditLog                auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            CollaborationManagerHandler handler = instanceHandler.getCollaborationManagerHandler(userId, serverName, viewServiceURLMarker, accessServiceURLMarker, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElementList(handler.findComments(userId,
                                                             instanceHandler.getSearchString(requestBody.getFilter(), startsWith, endsWith, ignoreCase),
                                                             startFrom,
                                                             pageSize,
                                                             requestBody.getEffectiveTime()));
            }
            else
            {
                response.setElementList(handler.findComments(userId,
                                                             instanceHandler.getSearchString(null, startsWith, endsWith, ignoreCase),
                                                             startFrom,
                                                             pageSize,
                                                             new Date()));
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
     * @param viewServiceURLMarker optional view service URL marker (overrides accessServiceURLMarker)
     * @param accessServiceURLMarker optional access service URL marker used to identify which back end service to call
     * @param requestBody  contains the name of the tag and (optional) description of the tag
     *
     * @return guid for new tag or
     * InvalidParameterException - one of the parameters is invalid or
     * PropertyServerException - there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public GUIDResponse createInformalTag(String        serverName,
                                          String        viewServiceURLMarker,
                                          String        accessServiceURLMarker,
                                          TagProperties requestBody)
    {
        final String   methodName = "createTag";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        GUIDResponse  response = new GUIDResponse();
        AuditLog      auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            if (requestBody != null)
            {
                CollaborationManagerHandler handler = instanceHandler.getCollaborationManagerHandler(userId, serverName, viewServiceURLMarker, accessServiceURLMarker, methodName);

                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
                response.setGUID(handler.createInformalTag(userId, requestBody));
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName, TagProperties.class.getName());
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
     * @param tagGUID      unique id for the tag.
     * @param viewServiceURLMarker optional view service URL marker (overrides accessServiceURLMarker)
     * @param accessServiceURLMarker optional access service URL marker used to identify which back end service to call
     * @param requestBody  contains the name of the tag and (optional) description of the tag.
     *
     * @return void or
     * InvalidParameterException - one of the parameters is invalid or
     * PropertyServerException - there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public VoidResponse   updateTagDescription(String                       serverName,
                                               String                       tagGUID,
                                               String                       viewServiceURLMarker,
                                               String                       accessServiceURLMarker,
                                               InformalTagUpdateRequestBody requestBody)
    {
        final String methodName = "updateTagDescription";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse  response = new VoidResponse();
        AuditLog      auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            if (requestBody != null)
            {
                CollaborationManagerHandler handler = instanceHandler.getCollaborationManagerHandler(userId, serverName, viewServiceURLMarker, accessServiceURLMarker, methodName);

                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
                handler.updateTagDescription(userId,
                                             tagGUID,
                                             requestBody.getDescription(),
                                             requestBody.getEffectiveTime());
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName, InformalTagUpdateRequestBody.class.getName());
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
     * @param tagGUID   unique id for the tag.
     * @param viewServiceURLMarker optional view service URL marker (overrides accessServiceURLMarker)
     * @param accessServiceURLMarker optional access service URL marker used to identify which back end service to call
     * @param requestBody  optional effective time.
     *
     * @return void or
     * InvalidParameterException - one of the parameters is invalid or
     * PropertyServerException - there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public VoidResponse   deleteTag(String                        serverName,
                                    String                        tagGUID,
                                    String                        viewServiceURLMarker,
                                    String                        accessServiceURLMarker,
                                    EffectiveTimeQueryRequestBody requestBody)
    {
        final String methodName           = "deleteTag";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse  response = new VoidResponse();
        AuditLog      auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            CollaborationManagerHandler handler = instanceHandler.getCollaborationManagerHandler(userId, serverName, viewServiceURLMarker, accessServiceURLMarker, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.deleteTag(userId, tagGUID, requestBody.getEffectiveTime());
            }
            else
            {
                handler.deleteTag(userId, tagGUID, new Date());
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
     * Return the tag for the supplied unique identifier (guid).
     *
     * @param serverName   name of the server instances for this request
     * @param guid unique identifier of the tag.
     * @param viewServiceURLMarker optional view service URL marker (overrides accessServiceURLMarker)
     * @param accessServiceURLMarker optional access service URL marker used to identify which back end service to call
     * @param requestBody optional effective time
     *
     * @return Tag object or
     * InvalidParameterException - one of the parameters is invalid or
     * PropertyServerException - there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public InformalTagResponse getTag(String                        serverName,
                                      String                        viewServiceURLMarker,
                                      String                        accessServiceURLMarker,
                                      String                        guid,
                                      EffectiveTimeQueryRequestBody requestBody)
    {
        final String methodName = "getTag";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        InformalTagResponse response = new InformalTagResponse();
        AuditLog            auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            CollaborationManagerHandler handler = instanceHandler.getCollaborationManagerHandler(userId, serverName, viewServiceURLMarker, accessServiceURLMarker, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setTag(handler.getTag(userId, guid, requestBody.getEffectiveTime()));
            }
            else
            {
                response.setTag(handler.getTag(userId, guid, new Date()));
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
     * Return the list of tags exactly matching the supplied name.
     *
     * @param serverName name of the server instances for this request
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param viewServiceURLMarker optional view service URL marker (overrides accessServiceURLMarker)
     * @param accessServiceURLMarker optional access service URL marker used to identify which back end service to call
     * @param requestBody name of tag.
     *
     * @return tag list or
     * InvalidParameterException - one of the parameters is invalid or
     * PropertyServerException - there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public InformalTagsResponse getTagsByName(String            serverName,
                                              int               startFrom,
                                              int               pageSize,
                                              String            viewServiceURLMarker,
                                              String            accessServiceURLMarker,
                                              FilterRequestBody requestBody)
    {
        final String methodName = "getTagsByName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        InformalTagsResponse response = new InformalTagsResponse();
        AuditLog             auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            CollaborationManagerHandler handler = instanceHandler.getCollaborationManagerHandler(userId, serverName, viewServiceURLMarker, accessServiceURLMarker, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setTags(handler.getTagsByName(userId,
                                                       requestBody.getFilter(),
                                                       startFrom,
                                                       pageSize,
                                                       requestBody.getEffectiveTime()));
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
     * @param serverName name of the server to route the request to
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param startsWith does the value start with the supplied string?
     * @param endsWith does the value end with the supplied string?
     * @param ignoreCase should the search ignore case?
     * @param viewServiceURLMarker optional view service URL marker (overrides accessServiceURLMarker)
     * @param accessServiceURLMarker optional access service URL marker used to identify which back end service to call
     * @param requestBody string to find in the properties
     *
     * @return tag list or
     * InvalidParameterException - one of the parameters is invalid or
     * PropertyServerException - there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public InformalTagsResponse findTags(String                  serverName,
                                         int                     startFrom,
                                         int                     pageSize,
                                         boolean                 startsWith,
                                         boolean                 endsWith,
                                         boolean                 ignoreCase,
                                         String                  viewServiceURLMarker,
                                         String                  accessServiceURLMarker,
                                         FilterRequestBody       requestBody)
    {
        final String methodName = "findTags";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        InformalTagsResponse response = new InformalTagsResponse();
        AuditLog             auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            CollaborationManagerHandler handler = instanceHandler.getCollaborationManagerHandler(userId, serverName, viewServiceURLMarker, accessServiceURLMarker, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setTags(handler.findTags(userId,
                                                  instanceHandler.getSearchString(requestBody.getFilter(), startsWith, endsWith, ignoreCase),
                                                  startFrom,
                                                  pageSize,
                                                  requestBody.getEffectiveTime()));
            }
            else
            {
                response.setTags(handler.findTags(userId,
                                                  instanceHandler.getSearchString(null, startsWith, endsWith, ignoreCase),
                                                  startFrom,
                                                  pageSize,
                                                  new Date()));
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
     * @param serverName name of the server to route the request to
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param startsWith does the value start with the supplied string?
     * @param endsWith does the value end with the supplied string?
     * @param ignoreCase should the search ignore case?
     * @param viewServiceURLMarker optional view service URL marker (overrides accessServiceURLMarker)
     * @param accessServiceURLMarker optional access service URL marker used to identify which back end service to call
     * @param requestBody string to find in the properties
     *
     * @return tag list or
     * InvalidParameterException - one of the parameters is invalid or
     * PropertyServerException - there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public InformalTagsResponse findMyTags(String                  serverName,
                                           int                     startFrom,
                                           int                     pageSize,
                                           boolean                 startsWith,
                                           boolean                 endsWith,
                                           boolean                 ignoreCase,
                                           String                  viewServiceURLMarker,
                                           String                  accessServiceURLMarker,
                                           FilterRequestBody requestBody)
    {
        final String methodName = "findMyTags";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        InformalTagsResponse response = new InformalTagsResponse();
        AuditLog             auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            CollaborationManagerHandler handler = instanceHandler.getCollaborationManagerHandler(userId, serverName, viewServiceURLMarker, accessServiceURLMarker, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setTags(handler.findMyTags(userId,
                                                    instanceHandler.getSearchString(requestBody.getFilter(), startsWith, endsWith, ignoreCase),
                                                    startFrom,
                                                    pageSize,
                                                    requestBody.getEffectiveTime()));
            }
            else
            {
                response.setTags(handler.findMyTags(userId,
                                                    instanceHandler.getSearchString(null, startsWith, endsWith, ignoreCase),
                                                    startFrom,
                                                    pageSize,
                                                    new Date()));
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
     * @param elementGUID    unique id for the element.
     * @param tagGUID      unique id of the tag.
     * @param isPublic visibility of link
     * @param viewServiceURLMarker optional view service URL marker (overrides accessServiceURLMarker)
     * @param accessServiceURLMarker optional access service URL marker used to identify which back end service to call
     * @param requestBody  feedback request body.
     *
     * @return void or
     * InvalidParameterException - one of the parameters is invalid or
     * PropertyServerException - there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public VoidResponse   addTagToElement(String                        serverName,
                                          String                        elementGUID,
                                          String                        tagGUID,
                                          boolean                       isPublic,
                                          String                        viewServiceURLMarker,
                                          String                        accessServiceURLMarker,
                                          EffectiveTimeQueryRequestBody requestBody)
    {
        final String methodName = "addTagToElement";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse  response = new VoidResponse();
        AuditLog      auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            CollaborationManagerHandler handler = instanceHandler.getCollaborationManagerHandler(userId, serverName, viewServiceURLMarker, accessServiceURLMarker, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.addTagToElement(userId, elementGUID, tagGUID, isPublic, requestBody.getEffectiveTime());
            }
            else
            {
                handler.addTagToElement(userId, elementGUID, tagGUID, isPublic, new Date());
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
     * Removes a tag from the element that was added by this user.
     *
     * @param serverName   name of the server instances for this request
     * @param elementGUID unique id for the element.
     * @param tagGUID   unique id for the tag.
     * @param viewServiceURLMarker optional view service URL marker (overrides accessServiceURLMarker)
     * @param accessServiceURLMarker optional access service URL marker used to identify which back end service to call
     * @param requestBody optional effective time
     *
     * @return void or
     * InvalidParameterException - one of the parameters is invalid or
     * PropertyServerException - there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public VoidResponse   removeTagFromElement(String                        serverName,
                                               String                        elementGUID,
                                               String                        tagGUID,
                                               String                        viewServiceURLMarker,
                                               String                        accessServiceURLMarker,
                                               EffectiveTimeQueryRequestBody requestBody)
    {
        final String   methodName  = "removeTagFromElement";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse  response = new VoidResponse();
        AuditLog      auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            CollaborationManagerHandler handler = instanceHandler.getCollaborationManagerHandler(userId, serverName, viewServiceURLMarker, accessServiceURLMarker, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.removeTagFromElement(userId, elementGUID, tagGUID, requestBody.getEffectiveTime());
            }
            else
            {
                handler.removeTagFromElement(userId, elementGUID, tagGUID, new Date());
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
     * Return the list of unique identifiers for elements that are linked to a specific tag either directly, or via one
     * of its schema elements.
     *
     * @param serverName   name of the server instances for this request
     * @param tagGUID unique identifier of tag.
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param viewServiceURLMarker optional view service URL marker (overrides accessServiceURLMarker)
     * @param accessServiceURLMarker optional access service URL marker used to identify which back end service to call
     * @param requestBody optional effective time
     *
     * @return element stubs list or
     * InvalidParameterException the userId is null or invalid or
     * PropertyServerException there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public RelatedElementsResponse getElementsByTag(String                        serverName,
                                                    String                        tagGUID,
                                                    int                           startFrom,
                                                    int                           pageSize,
                                                    String                        viewServiceURLMarker,
                                                    String                        accessServiceURLMarker,
                                                    EffectiveTimeQueryRequestBody requestBody)
    {
        final String methodName = "getElementsByTag";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        RelatedElementsResponse  response = new RelatedElementsResponse();
        AuditLog          auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            CollaborationManagerHandler handler = instanceHandler.getCollaborationManagerHandler(userId, serverName, viewServiceURLMarker, accessServiceURLMarker, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElementList(handler.getElementsByTag(userId, tagGUID, startFrom, pageSize, requestBody.getEffectiveTime()));
            }
            else
            {
                response.setElementList(handler.getElementsByTag(userId, tagGUID, startFrom, pageSize, new Date()));
            }
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
     * Creates a new noteLog and returns the unique identifier for it.
     *
     * @param serverName   name of the server instances for this request
     * @param elementGUID unique identifier of the element where the note log is located
     * @param isPublic                 is this element visible to other people.
     * @param viewServiceURLMarker optional view service URL marker (overrides accessServiceURLMarker)
     * @param accessServiceURLMarker optional access service URL marker used to identify which back end service to call
     * @param requestBody  contains the name of the tag and (optional) description of the tag
     *
     * @return guid for new tag or
     * InvalidParameterException - one of the parameters is invalid or
     * PropertyServerException - there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public GUIDResponse createNoteLog(String            serverName,
                                      String            elementGUID,
                                      boolean           isPublic,
                                      String            viewServiceURLMarker,
                                      String            accessServiceURLMarker,
                                      NoteLogProperties requestBody)
    {
        final String   methodName = "createNoteLog";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        GUIDResponse  response = new GUIDResponse();
        AuditLog      auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            if (requestBody != null)
            {
                CollaborationManagerHandler handler = instanceHandler.getCollaborationManagerHandler(userId, serverName, viewServiceURLMarker, accessServiceURLMarker, methodName);

                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
                response.setGUID(handler.createNoteLog(userId, elementGUID, requestBody, isPublic));
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName, NoteLogProperties.class.getName());
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
     * Update an existing note log.
     *
     * @param serverName   name of the server instances for this request.
     * @param noteLogGUID  unique identifier for the note log to change.
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param viewServiceURLMarker optional view service URL marker (overrides accessServiceURLMarker)
     * @param accessServiceURLMarker optional access service URL marker used to identify which back end service to call
     * @param requestBody  containing type of comment enum and the text of the comment.
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid.
     * PropertyServerException There is a problem updating the element properties in the metadata repository.
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse   updateNoteLog(String                         serverName,
                                        String                         noteLogGUID,
                                        boolean                        isMergeUpdate,
                                        String                         viewServiceURLMarker,
                                        String                         accessServiceURLMarker,
                                        ReferenceableUpdateRequestBody requestBody)
    {
        final String methodName = "updateNoteLog";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse  response = new VoidResponse();
        AuditLog      auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getElementProperties() instanceof  NoteLogProperties noteLogProperties)
                {
                    CollaborationManagerHandler handler = instanceHandler.getCollaborationManagerHandler(userId, serverName, viewServiceURLMarker, accessServiceURLMarker, methodName);

                    handler.updateNoteLog(userId,
                                          noteLogGUID,
                                          isMergeUpdate,
                                          noteLogProperties,
                                          requestBody.getEffectiveTime());
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(NoteLogProperties.class.getName(), methodName);
                }
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName, ReferenceableUpdateRequestBody.class.getName());
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
     * Removes a note log from the repository.  All the relationships to referenceables are lost.
     *
     * @param serverName   name of the server instances for this request
     * @param noteLogGUID   unique id for the note log.
     * @param viewServiceURLMarker optional view service URL marker (overrides accessServiceURLMarker)
     * @param accessServiceURLMarker optional access service URL marker used to identify which back end service to call
     * @param requestBody optional effective time
     *
     * @return void or
     * InvalidParameterException - one of the parameters is invalid or
     * PropertyServerException - there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public VoidResponse   deleteNoteLog(String                        serverName,
                                        String                        noteLogGUID,
                                        String                        viewServiceURLMarker,
                                        String                        accessServiceURLMarker,
                                        EffectiveTimeQueryRequestBody requestBody)
    {
        final String methodName = "deleteNoteLog";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse  response = new VoidResponse();
        AuditLog      auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            CollaborationManagerHandler handler = instanceHandler.getCollaborationManagerHandler(userId, serverName, viewServiceURLMarker, accessServiceURLMarker, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            if (requestBody != null)
            {
                handler.removeNoteLog(userId, noteLogGUID, requestBody.getEffectiveTime());
            }
            else
            {
                handler.removeNoteLog(userId, noteLogGUID, new Date());
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
     * @param serverName name of the server to route the request to
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param startsWith does the value start with the supplied string?
     * @param endsWith does the value end with the supplied string?
     * @param ignoreCase should the search ignore case?
     * @param viewServiceURLMarker optional view service URL marker (overrides accessServiceURLMarker)
     * @param accessServiceURLMarker optional access service URL marker used to identify which back end service to call
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public NoteLogsResponse findNoteLogs(String                  serverName,
                                         int                     startFrom,
                                         int                     pageSize,
                                         boolean                 startsWith,
                                         boolean                 endsWith,
                                         boolean                 ignoreCase,
                                         String                  viewServiceURLMarker,
                                         String                  accessServiceURLMarker,
                                         FilterRequestBody       requestBody)
    {
        final String methodName = "findNoteLogs";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        NoteLogsResponse response = new NoteLogsResponse();
        AuditLog         auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            CollaborationManagerHandler handler = instanceHandler.getCollaborationManagerHandler(userId, serverName, viewServiceURLMarker, accessServiceURLMarker, methodName);

            if (requestBody != null)
            {
                response.setElementList(handler.findNoteLogs(userId,
                                                             instanceHandler.getSearchString(requestBody.getFilter(), startsWith, endsWith, ignoreCase),
                                                             startFrom,
                                                             pageSize,
                                                             requestBody.getEffectiveTime()));
            }
            else
            {
                response.setElementList(handler.findNoteLogs(userId,
                                                             instanceHandler.getSearchString(null, startsWith, endsWith, ignoreCase),
                                                             startFrom,
                                                             pageSize,
                                                             new Date()));
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
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param viewServiceURLMarker optional view service URL marker (overrides accessServiceURLMarker)
     * @param accessServiceURLMarker optional access service URL marker used to identify which back end service to call
     * @param requestBody name to search for and correlators
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public NoteLogsResponse getNoteLogsByName(String            serverName,
                                              int               startFrom,
                                              int               pageSize,
                                              String            viewServiceURLMarker,
                                              String            accessServiceURLMarker,
                                              FilterRequestBody requestBody)
    {
        final String methodName = "getNoteLogsByName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        NoteLogsResponse response = new NoteLogsResponse();
        AuditLog         auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

           CollaborationManagerHandler handler = instanceHandler.getCollaborationManagerHandler(userId, serverName, viewServiceURLMarker, accessServiceURLMarker, methodName);

           if (requestBody != null)
           {
               response.setElementList(handler.getNoteLogsByName(userId, requestBody.getFilter(), startFrom, pageSize, requestBody.getEffectiveTime()));
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
     * @param elementGUID unique identifier of the note log of interest
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param viewServiceURLMarker optional view service URL marker (overrides accessServiceURLMarker)
     * @param accessServiceURLMarker optional access service URL marker used to identify which back end service to call
     * @param requestBody optional effective time
     *
     * @return list of associated metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public NoteLogsResponse getNoteLogsForElement(String                        serverName,
                                                  String                        elementGUID,
                                                  int                           startFrom,
                                                  int                           pageSize,
                                                  String                        viewServiceURLMarker,
                                                  String                        accessServiceURLMarker,
                                                  EffectiveTimeQueryRequestBody requestBody)
    {
        final String methodName = "getNotesForNoteLog";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        NoteLogsResponse response = new NoteLogsResponse();
        AuditLog         auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            CollaborationManagerHandler handler = instanceHandler.getCollaborationManagerHandler(userId, serverName, viewServiceURLMarker, accessServiceURLMarker, methodName);

            if (requestBody != null)
            {
                response.setElementList(handler.getNoteLogsForElement(userId, elementGUID, startFrom, pageSize, requestBody.getEffectiveTime()));
            }
            else
            {
                response.setElementList(handler.getNoteLogsForElement(userId, elementGUID, startFrom, pageSize, new Date()));
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
     * @param noteLogGUID unique identifier of the requested metadata element
     * @param viewServiceURLMarker optional view service URL marker (overrides accessServiceURLMarker)
     * @param accessServiceURLMarker optional access service URL marker used to identify which back end service to call
     * @param requestBody optional effective time
     *
     * @return requested metadata element or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public NoteLogResponse getNoteLogByGUID(String                        serverName,
                                            String                        noteLogGUID,
                                            String                        viewServiceURLMarker,
                                            String                        accessServiceURLMarker,
                                            EffectiveTimeQueryRequestBody requestBody)
    {
        final String methodName = "getNoteLogByGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        NoteLogResponse response = new NoteLogResponse();
        AuditLog        auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            CollaborationManagerHandler handler = instanceHandler.getCollaborationManagerHandler(userId, serverName, viewServiceURLMarker, accessServiceURLMarker, methodName);

            if (requestBody != null)
            {
                response.setElement(handler.getNoteLogByGUID(userId, noteLogGUID, requestBody.getEffectiveTime()));
            }
            else
            {
                response.setElement(handler.getNoteLogByGUID(userId, noteLogGUID, new Date()));
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
     * A note log typically contains many notes, linked with relationships.
     */


    /**
     * Creates a new note for a note log and returns the unique identifier for it.
     *
     * @param serverName   name of the server instances for this request
     * @param noteLogGUID unique identifier of the  note log
     * @param viewServiceURLMarker optional view service URL marker (overrides accessServiceURLMarker)
     * @param accessServiceURLMarker optional access service URL marker used to identify which back end service to call
     * @param requestBody  contains the name of the tag and (optional) description of the tag
     *
     * @return guid for new tag or
     * InvalidParameterException - one of the parameters is invalid or
     * PropertyServerException - there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public GUIDResponse createNote(String         serverName,
                                   String         noteLogGUID,
                                   String         viewServiceURLMarker,
                                   String         accessServiceURLMarker,
                                   NoteProperties requestBody)
    {
        final String   methodName = "createNote";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        GUIDResponse  response = new GUIDResponse();
        AuditLog      auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            if (requestBody != null)
            {
                CollaborationManagerHandler handler = instanceHandler.getCollaborationManagerHandler(userId, serverName, viewServiceURLMarker, accessServiceURLMarker, methodName);

                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
                response.setGUID(handler.createNote(userId, noteLogGUID, requestBody));
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName, NoteLogProperties.class.getName());
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
     * Update an existing note.
     *
     * @param serverName   name of the server instances for this request.
     * @param noteGUID  unique identifier for the note to change.
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param viewServiceURLMarker optional view service URL marker (overrides accessServiceURLMarker)
     * @param accessServiceURLMarker optional access service URL marker used to identify which back end service to call
     * @param requestBody  containing type of comment enum and the text of the comment.
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid.
     * PropertyServerException There is a problem updating the element properties in the metadata repository.
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse   updateNote(String                         serverName,
                                     String                         noteGUID,
                                     boolean                        isMergeUpdate,
                                     String                         viewServiceURLMarker,
                                     String                         accessServiceURLMarker,
                                     ReferenceableUpdateRequestBody requestBody)
    {
        final String methodName = "updateNote";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse  response = new VoidResponse();
        AuditLog      auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getElementProperties() instanceof  NoteProperties noteProperties)
                {
                    CollaborationManagerHandler handler = instanceHandler.getCollaborationManagerHandler(userId, serverName, viewServiceURLMarker, accessServiceURLMarker, methodName);

                    handler.updateNote(userId,
                                       noteGUID,
                                       isMergeUpdate,
                                       noteProperties,
                                       requestBody.getEffectiveTime());
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(NoteProperties.class.getName(), methodName);
                }
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName, ReferenceableUpdateRequestBody.class.getName());
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
     * Removes a note from the repository.  All the relationships to referenceables are lost.
     *
     * @param serverName   name of the server instances for this request
     * @param noteGUID   unique id for the note .
     * @param viewServiceURLMarker optional view service URL marker (overrides accessServiceURLMarker)
     * @param accessServiceURLMarker optional access service URL marker used to identify which back end service to call
     * @param requestBody optional effective time
     *
     * @return void or
     * InvalidParameterException - one of the parameters is invalid or
     * PropertyServerException - there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public VoidResponse   deleteNote(String                        serverName,
                                     String                        noteGUID,
                                     String                        viewServiceURLMarker,
                                     String                        accessServiceURLMarker,
                                     EffectiveTimeQueryRequestBody requestBody)
    {
        final String methodName        = "deleteNote";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse  response = new VoidResponse();
        AuditLog      auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            CollaborationManagerHandler handler = instanceHandler.getCollaborationManagerHandler(userId, serverName, viewServiceURLMarker, accessServiceURLMarker, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.removeNote(userId, noteGUID, requestBody.getEffectiveTime());
            }
            else
            {
                handler.removeNote(userId, noteGUID, new Date());
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
     * @param serverName name of the server to route the request to
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param viewServiceURLMarker optional view service URL marker (overrides accessServiceURLMarker)
     * @param accessServiceURLMarker optional access service URL marker used to identify which back end service to call
     * @param startsWith does the value start with the supplied string?
     * @param endsWith does the value end with the supplied string?
     * @param ignoreCase should the search ignore case?
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public NotesResponse findNotes(String                  serverName,
                                   int                     startFrom,
                                   int                     pageSize,
                                   boolean                 startsWith,
                                   boolean                 endsWith,
                                   boolean                 ignoreCase,
                                   String                  viewServiceURLMarker,
                                   String                  accessServiceURLMarker,
                                   FilterRequestBody       requestBody)
    {
        final String methodName = "findNotes";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        NotesResponse response = new NotesResponse();
        AuditLog      auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            CollaborationManagerHandler handler = instanceHandler.getCollaborationManagerHandler(userId, serverName, viewServiceURLMarker, accessServiceURLMarker, methodName);

            if (requestBody != null)
            {
                response.setElementList(handler.findNotes(userId,
                                                          instanceHandler.getSearchString(requestBody.getFilter(), startsWith, endsWith, ignoreCase),
                                                          startFrom,
                                                          pageSize,
                                                          requestBody.getEffectiveTime()));
            }
            else
            {
                response.setElementList(handler.findNotes(userId,
                                                          instanceHandler.getSearchString(null, startsWith, endsWith, ignoreCase),
                                                          startFrom,
                                                          pageSize,
                                                          new Date()));
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
     * @param noteLogGUID unique identifier of the note log of interest
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param viewServiceURLMarker optional view service URL marker (overrides accessServiceURLMarker)
     * @param accessServiceURLMarker optional access service URL marker used to identify which back end service to call
     * @param requestBody optional effective time
     *
     * @return list of associated metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public NotesResponse getNotesForNoteLog(String                        serverName,
                                            String                        noteLogGUID,
                                            int                           startFrom,
                                            int                           pageSize,
                                            String                        viewServiceURLMarker,
                                            String                        accessServiceURLMarker,
                                            EffectiveTimeQueryRequestBody requestBody)
    {
        final String methodName = "getNotesForNoteLog";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        NotesResponse response = new NotesResponse();
        AuditLog      auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            CollaborationManagerHandler handler = instanceHandler.getCollaborationManagerHandler(userId, serverName, viewServiceURLMarker, accessServiceURLMarker, methodName);

            if (requestBody != null)
            {
                response.setElementList(handler.getNotesForNoteLog(userId, noteLogGUID, startFrom, pageSize, requestBody.getEffectiveTime()));
            }
            else
            {
                response.setElementList(handler.getNotesForNoteLog(userId, noteLogGUID, startFrom, pageSize, new Date()));
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
     * @param noteGUID unique identifier of the requested metadata element
     * @param viewServiceURLMarker optional view service URL marker (overrides accessServiceURLMarker)
     * @param accessServiceURLMarker optional access service URL marker used to identify which back end service to call
     * @param requestBody optional effective time
     *
     * @return matching metadata element or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public NoteResponse getNoteByGUID(String                        serverName,
                                      String                        noteGUID,
                                      String                        viewServiceURLMarker,
                                      String                        accessServiceURLMarker,
                                      EffectiveTimeQueryRequestBody requestBody)
    {
        final String methodName = "getNoteByGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        NoteResponse response = new NoteResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            CollaborationManagerHandler handler = instanceHandler.getCollaborationManagerHandler(userId, serverName, viewServiceURLMarker, accessServiceURLMarker, methodName);

            if (requestBody != null)
            {
                response.setElement(handler.getNoteByGUID(userId, noteGUID, requestBody.getEffectiveTime()));
            }
            else
            {
                response.setElement(handler.getNoteByGUID(userId, noteGUID, new Date()));
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
