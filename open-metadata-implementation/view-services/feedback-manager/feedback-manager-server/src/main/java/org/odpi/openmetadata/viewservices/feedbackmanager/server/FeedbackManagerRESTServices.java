/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project */
/* Copyright Contributors to the ODPi Egeria category. */
package org.odpi.openmetadata.viewservices.feedbackmanager.server;

import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.NameRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.SearchStringRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.tokencontroller.TokenController;
import org.odpi.openmetadata.viewservices.feedbackmanager.handler.CollaborationManagerHandler;
import org.odpi.openmetadata.viewservices.feedbackmanager.properties.*;
import org.odpi.openmetadata.viewservices.feedbackmanager.rest.*;
import org.odpi.openmetadata.viewservices.feedbackmanager.rest.RelationshipRequestBody;
import org.slf4j.LoggerFactory;


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

                handler.addRatingToElement(userId, guid, isPublic, requestBody);
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
                                                String          guid,
                                                String          viewServiceURLMarker,
                                                String          accessServiceURLMarker,
                                                NullRequestBody requestBody)
    {
        final String methodName = "removeRatingFromElement";
        final String guidParameterName = "guid";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse  response = new VoidResponse();
        AuditLog      auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            CollaborationManagerHandler handler = instanceHandler.getCollaborationManagerHandler(userId, serverName, viewServiceURLMarker, accessServiceURLMarker, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            handler.removeRatingFromElement(userId, guid);
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
     * @param requestBody feedback request body .
     *
     * @return void or
     * InvalidParameterException - one of the parameters is null or invalid or
     * PropertyServerException - there is a problem adding the element properties to
     *                                   the metadata repository or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse addLikeToElement(String          serverName,
                                         String          guid,
                                         boolean         isPublic,
                                         String          viewServiceURLMarker,
                                         String          accessServiceURLMarker,
                                         NullRequestBody requestBody)
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

            handler.addLikeToElement(userId, guid, isPublic);
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
                                              String          guid,
                                              String          viewServiceURLMarker,
                                              String          accessServiceURLMarker,
                                              NullRequestBody requestBody)
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

            handler.removeLikeFromElement(userId, guid);
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
                if (requestBody.getElementProperties() instanceof  CommentProperties commentProperties)
                {
                    CollaborationManagerHandler handler = instanceHandler.getCollaborationManagerHandler(userId, serverName, viewServiceURLMarker, accessServiceURLMarker, methodName);

                    handler.updateComment(userId,
                                          commentGUID,
                                          isMergeUpdate,
                                          commentProperties);
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
     * @param requestBody  containing type of comment enum and the text of the comment.
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid.
     * PropertyServerException There is a problem updating the element properties in the metadata repository.
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse   updateCommentVisibility(String          serverName,
                                                  String          parentGUID,
                                                  String          commentGUID,
                                                  boolean         isPublic,
                                                  String          viewServiceURLMarker,
                                                  String          accessServiceURLMarker,
                                                  NullRequestBody requestBody)
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

            handler.updateCommentVisibility(userId, parentGUID, commentGUID, isPublic);
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
     * @param viewServiceURLMarker optional view service URL marker (overrides accessServiceURLMarker)
     * @param accessServiceURLMarker optional access service URL marker used to identify which back end service to call
     * @param requestBody properties to help with the mapping of the elements in the external asset manager and open metadata
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse setupAcceptedAnswer(String                  serverName,
                                            String                  questionCommentGUID,
                                            String                  answerCommentGUID,
                                            String                  viewServiceURLMarker,
                                            String                  accessServiceURLMarker,
                                            RelationshipRequestBody requestBody)
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
                if (requestBody.getProperties() instanceof FeedbackProperties feedbackProperties)
                {
                    handler.setupAcceptedAnswer(userId,
                                                questionCommentGUID,
                                                answerCommentGUID,
                                                feedbackProperties.getIsPublic());
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(CommentProperties.class.getName(), methodName);
                }
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName, RelationshipRequestBody.class.getName());
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
     * @param requestBody properties to help with the mapping of the elements in the external asset manager and open metadata
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse clearAcceptedAnswer(String                        serverName,
                                            String                        questionCommentGUID,
                                            String                        answerCommentGUID,
                                            String                        viewServiceURLMarker,
                                            String                        accessServiceURLMarker,
                                            NullRequestBody               requestBody)
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

            handler.clearAcceptedAnswer(userId, questionCommentGUID, answerCommentGUID);
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
     * @param elementGUID  String - unique id for the element object
     * @param commentGUID  String - unique id for the comment object
     * @param viewServiceURLMarker optional view service URL marker (overrides accessServiceURLMarker)
     * @param accessServiceURLMarker optional access service URL marker used to identify which back end service to call
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
                                                 String                         elementGUID,
                                                 String                         commentGUID,
                                                 String                         viewServiceURLMarker,
                                                 String                         accessServiceURLMarker,
                                                 ReferenceableUpdateRequestBody requestBody)
    {
        final String guidParameterName = "commentGUID";
        final String methodName        = "removeElementComment";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse  response = new VoidResponse();
        AuditLog      auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            CollaborationManagerHandler handler = instanceHandler.getCollaborationManagerHandler(userId, serverName, viewServiceURLMarker, accessServiceURLMarker, methodName);

            handler.removeComment(userId, commentGUID);
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
     * @return comment properties or
     *  InvalidParameterException one of the parameters is null or invalid.
     *  PropertyServerException there is a problem updating the element properties in the property server.
     *  UserNotAuthorizedException the user does not have permission to perform this request.
     */
    public CommentResponse getCommentByGUID(String                        serverName,
                                            String                        commentGUID,
                                            String                        viewServiceURLMarker,
                                            String                        accessServiceURLMarker)
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

            response.setElement(handler.getComment(userId, commentGUID));
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
                                                       String                        accessServiceURLMarker)
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

            response.setElementList(handler.getAttachedComments(userId, elementGUID, startFrom, pageSize));
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
                                                SearchStringRequestBody requestBody)
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
                                                             instanceHandler.getSearchString(requestBody.getSearchString(), startsWith, endsWith, ignoreCase),
                                                             startFrom,
                                                             pageSize));
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName, SearchStringRequestBody.class.getName());
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
                                             requestBody.getDescription());
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
     * @param requestBody  null request body.
     *
     * @return void or
     * InvalidParameterException - one of the parameters is invalid or
     * PropertyServerException - there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse   deleteTag(String          serverName,
                                    String          tagGUID,
                                    String          viewServiceURLMarker,
                                    String          accessServiceURLMarker,
                                    NullRequestBody requestBody)
    {
        final String methodName           = "deleteTag";
        final String tagGUIDParameterName = "tagGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse  response = new VoidResponse();
        AuditLog      auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            CollaborationManagerHandler handler = instanceHandler.getCollaborationManagerHandler(userId, serverName, viewServiceURLMarker, accessServiceURLMarker, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            handler.deleteTag(userId, tagGUID);
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
     *
     * @return Tag object or
     * InvalidParameterException - one of the parameters is invalid or
     * PropertyServerException - there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public InformalTagResponse getTag(String serverName,
                                      String viewServiceURLMarker,
                                      String accessServiceURLMarker,
                                      String guid)
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
            response.setTag(handler.getTag(userId, guid));
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
     * @param requestBody name of tag.
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param viewServiceURLMarker optional view service URL marker (overrides accessServiceURLMarker)
     * @param accessServiceURLMarker optional access service URL marker used to identify which back end service to call
     *
     * @return tag list or
     * InvalidParameterException - one of the parameters is invalid or
     * PropertyServerException - there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public InformalTagsResponse getTagsByName(String          serverName,
                                              NameRequestBody requestBody,
                                              int             startFrom,
                                              int             pageSize,
                                              String          viewServiceURLMarker,
                                              String          accessServiceURLMarker)
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
                                                       requestBody.getName(),
                                                       startFrom,
                                                       pageSize));
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
     * @param requestBody name of tag.
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param viewServiceURLMarker optional view service URL marker (overrides accessServiceURLMarker)
     * @param accessServiceURLMarker optional access service URL marker used to identify which back end service to call
     *
     * @return tag list or
     * InvalidParameterException - one of the parameters is invalid or
     * PropertyServerException - there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public InformalTagsResponse getMyTagsByName(String          serverName,
                                                NameRequestBody requestBody,
                                                int             startFrom,
                                                int             pageSize,
                                                String          viewServiceURLMarker,
                                                String          accessServiceURLMarker)
    {
        final String methodName = "getMyTagsByName";

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
                response.setTags(handler.getMyTagsByName(userId,
                                                         requestBody.getName(),
                                                         startFrom,
                                                         pageSize));
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName, NameRequestBody.class.getName());
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
                                         SearchStringRequestBody requestBody)
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
                                                  instanceHandler.getSearchString(requestBody.getSearchString(), startsWith, endsWith, ignoreCase),
                                                  startFrom,
                                                  pageSize));
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName, SearchStringRequestBody.class.getName());
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
                                           SearchStringRequestBody requestBody)
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
                                                    instanceHandler.getSearchString(requestBody.getSearchString(), startsWith, endsWith, ignoreCase),
                                                    startFrom,
                                                    pageSize));
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName, SearchStringRequestBody.class.getName());
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
     * @param viewServiceURLMarker optional view service URL marker (overrides accessServiceURLMarker)
     * @param accessServiceURLMarker optional access service URL marker used to identify which back end service to call
     * @param requestBody  feedback request body.
     *
     * @return void or
     * InvalidParameterException - one of the parameters is invalid or
     * PropertyServerException - there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public VoidResponse   addTagToElement(String             serverName,
                                          String             elementGUID,
                                          String             tagGUID,
                                          String             viewServiceURLMarker,
                                          String             accessServiceURLMarker,
                                          FeedbackProperties requestBody)
    {
        final String methodName = "addTagToElement";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        boolean  isPublic = false;

        if (requestBody != null)
        {
            isPublic = requestBody.getIsPublic();
        }

        VoidResponse  response = new VoidResponse();
        AuditLog      auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            CollaborationManagerHandler handler = instanceHandler.getCollaborationManagerHandler(userId, serverName, viewServiceURLMarker, accessServiceURLMarker, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            handler.addTagToElement(userId, elementGUID, tagGUID, isPublic);
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
     * @param requestBody  null request body needed for correct protocol exchange.
     *
     * @return void or
     * InvalidParameterException - one of the parameters is invalid or
     * PropertyServerException - there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse   removeTagFromElement(String          serverName,
                                               String          elementGUID,
                                               String          tagGUID,
                                               String          viewServiceURLMarker,
                                               String          accessServiceURLMarker,
                                               NullRequestBody requestBody)
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

            handler.removeTagFromElement(userId, elementGUID, tagGUID);
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
     *
     * @return element stubs list or
     * InvalidParameterException the userId is null or invalid or
     * PropertyServerException there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public RelatedElementsResponse getElementsByTag(String serverName,
                                                    String tagGUID,
                                                    int    startFrom,
                                                    int    pageSize,
                                                    String viewServiceURLMarker,
                                                    String accessServiceURLMarker)
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
            response.setElementList(handler.getElementsByTag(userId, tagGUID, startFrom, pageSize));
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
                                         SearchStringRequestBody requestBody)
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

            if (requestBody != null)
            {
                CollaborationManagerHandler handler = instanceHandler.getCollaborationManagerHandler(userId, serverName, viewServiceURLMarker, accessServiceURLMarker, methodName);

                response.setElementList(handler.findNoteLogs(userId,
                                                             instanceHandler.getSearchString(requestBody.getSearchString(), startsWith, endsWith, ignoreCase),
                                                             startFrom,
                                                             pageSize));
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName, SearchStringRequestBody.class.getName());
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
    public NoteLogsResponse getNoteLogsByName(String          serverName,
                                              int             startFrom,
                                              int             pageSize,
                                              String          viewServiceURLMarker,
                                              String          accessServiceURLMarker,
                                              NameRequestBody requestBody)
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

           response.setElementList(handler.getNoteLogsByName(userId, requestBody.getName(), startFrom, pageSize));
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
                                                  String                        accessServiceURLMarker)
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

            response.setElementList(handler.getNoteLogsForElement(userId, elementGUID, startFrom, pageSize));
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
     *
     * @return requested metadata element or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public NoteLogResponse getNoteLogByGUID(String                        serverName,
                                            String                        noteLogGUID,
                                            String                        viewServiceURLMarker,
                                            String                        accessServiceURLMarker)
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

            response.setElement(handler.getNoteLogByGUID(userId, noteLogGUID));
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
                                   SearchStringRequestBody requestBody)
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

            if (requestBody != null)
            {
                CollaborationManagerHandler handler = instanceHandler.getCollaborationManagerHandler(userId, serverName, viewServiceURLMarker, accessServiceURLMarker, methodName);

                response.setElementList(handler.findNotes(userId,
                                                          instanceHandler.getSearchString(requestBody.getSearchString(), startsWith, endsWith, ignoreCase),
                                                          startFrom,
                                                          pageSize));
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName, SearchStringRequestBody.class.getName());
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
                                            String                        accessServiceURLMarker)
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

            response.setElementList(handler.getNotesForNoteLog(userId, noteLogGUID, startFrom, pageSize));
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
     *
     * @return matching metadata element or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public NoteResponse getNoteByGUID(String                        serverName,
                                      String                        noteGUID,
                                      String                        viewServiceURLMarker,
                                      String                        accessServiceURLMarker)
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

            response.setElement(handler.getNoteByGUID(userId, noteGUID));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }
}
