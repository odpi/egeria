/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project */
package org.odpi.openmetadata.viewservices.feedbackmanager.server;

import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.feedback.*;
import org.odpi.openmetadata.commonservices.ffdc.rest.GetRequestBody;
import org.odpi.openmetadata.tokencontroller.TokenController;
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
     * @param urlMarker  view service URL marker
     * @param requestBody provides a structure for the additional options when updating an element.
     *
     * @return void or
     * InvalidParameterException - one of the parameters is null or invalid or
     * PropertyServerException - there is a problem adding the element properties to
     *                                   the metadata repository or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public VoidResponse addRatingToElement(String                   serverName,
                                           String                   urlMarker,
                                           String                   guid,
                                           UpdateElementRequestBody requestBody)
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
                RatingHandler handler = instanceHandler.getRatingHandler(userId, serverName, urlMarker, methodName);

                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

                if (requestBody.getProperties() instanceof RatingProperties ratingProperties)
                {
                    handler.addRatingToElement(userId, guid, requestBody, ratingProperties);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(RatingProperties.class.getName(), methodName);
                }
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName, RatingProperties.class.getName());
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Removes a star rating that was added to the element by this user.
     *
     * @param serverName name of the server instances for this request
     * @param guid        String - unique id for the rating object
     * @param urlMarker  view service URL marker
     * @param requestBody optional effective time
     *
     * @return void or
     * InvalidParameterException - one of the parameters is null or invalid or
     * PropertyServerException - there is a problem updating the element properties in
     *                                   the metadata repository or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public VoidResponse removeRatingFromElement(String            serverName,
                                                String            urlMarker,
                                                String            guid,
                                                DeleteElementRequestBody requestBody)
    {
        final String methodName = "removeRatingFromElement";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse  response = new VoidResponse();
        AuditLog      auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            RatingHandler handler = instanceHandler.getRatingHandler(userId, serverName, urlMarker, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            handler.removeRatingFromElement(userId, guid, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Return the comments attached to an element.
     *
     * @param serverName name of the server instances for this request
     * @param elementGUID    unique identifier for the element that the comments are connected to (maybe a comment too).
     * @param urlMarker  view service URL marker
     * @param requestBody optional effective time
     * @return list of ratings or
     *  InvalidParameterException one of the parameters is null or invalid.
     *  PropertyServerException there is a problem updating the element properties in the property server.
     *  UserNotAuthorizedException the user does not have permission to perform this request.
     */
    public OpenMetadataRootElementsResponse getAttachedRatings(String             serverName,
                                                               String             urlMarker,
                                                               String             elementGUID,
                                                               ResultsRequestBody requestBody)
    {
        final String methodName = "getAttachedRatings";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog                         auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            RatingHandler handler = instanceHandler.getRatingHandler(userId, serverName, urlMarker, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            response.setElements(handler.getAttachedRatings(userId, elementGUID, requestBody));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Adds a "LikeProperties" to the element.
     *
     * @param serverName name of the server instances for this request
     * @param guid        String - unique id for the element.
     * @param urlMarker  view service URL marker
     * @param requestBody optional effective time
     *
     * @return void or
     * InvalidParameterException - one of the parameters is null or invalid or
     * PropertyServerException - there is a problem adding the element properties to
     *                                   the metadata repository or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public VoidResponse addLikeToElement(String                   serverName,
                                         String                   urlMarker,
                                         String                   guid,
                                         UpdateElementRequestBody requestBody)
    {
        final String methodName  = "addLikeToElement";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse  response = new VoidResponse();
        AuditLog      auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            LikeHandler handler = instanceHandler.getLikeHandler(userId, serverName, urlMarker, methodName);

            if (requestBody == null)
            {
                handler.addLikeToElement(userId, guid, null, null);

            }
            else
            {
                if (requestBody.getProperties() instanceof LikeProperties likeProperties)
                {
                    handler.addLikeToElement(userId, guid, requestBody, likeProperties);
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.addLikeToElement(userId, guid, requestBody, null);
                }
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Removes a "LikeProperties" added to the element by this user.
     *
     * @param serverName name of the server instances for this request
     * @param guid  String - unique id for the like object
     * @param urlMarker  view service URL marker
     * @param requestBody optional effective time
     *
     * @return void or
     * InvalidParameterException - one of the parameters is null or invalid or
     * PropertyServerException - there is a problem updating the element properties in
     *                                   the metadata repository or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public VoidResponse removeLikeFromElement(String            serverName,
                                              String            urlMarker,
                                              String            guid,
                                              DeleteElementRequestBody requestBody)
    {
        final String methodName = "removeLikeFromElement";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse  response = new VoidResponse();
        AuditLog      auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            LikeHandler handler = instanceHandler.getLikeHandler(userId, serverName, urlMarker, methodName);

            handler.removeLikeFromElement(userId, guid, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }



    /**
     * Return the likes attached to an element.
     *
     * @param serverName name of the server instances for this request
     * @param elementGUID    unique identifier for the element that the comments are connected to (maybe a comment too).
     * @param urlMarker  view service URL marker
     * @param requestBody optional effective time
     * @return list of likes or
     *  InvalidParameterException one of the parameters is null or invalid.
     *  PropertyServerException there is a problem updating the element properties in the property server.
     *  UserNotAuthorizedException the user does not have permission to perform this request.
     */
    public OpenMetadataRootElementsResponse getAttachedLikes(String             serverName,
                                                             String             urlMarker,
                                                             String             elementGUID,
                                                             ResultsRequestBody requestBody)
    {
        final String methodName = "getAttachedLikes";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog                         auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            LikeHandler handler = instanceHandler.getLikeHandler(userId, serverName, urlMarker, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            response.setElements(handler.getAttachedLikes(userId, elementGUID, requestBody));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Adds a comment to the element.
     *
     * @param serverName name of the server instances for this request
     * @param elementGUID  String - unique id for the element.
     * @param urlMarker  view service URL marker
     * @param requestBody containing type of comment enum and the text of the comment.
     *
     * @return elementGUID for new comment object or
     * InvalidParameterException - one of the parameters is null or invalid or
     * PropertyServerException - there is a problem adding the element properties to
     *                                   the metadata repository or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public GUIDResponse addCommentToElement(String                 serverName,
                                            String                 urlMarker,
                                            String                 elementGUID,
                                            NewAttachmentRequestBody requestBody)
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

            CommentHandler handler = instanceHandler.getCommentHandler(userId, serverName, urlMarker, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof CommentProperties commentProperties)
                {

                    response.setGUID(handler.addCommentToElement(userId, elementGUID, requestBody, requestBody.getInitialClassifications(), commentProperties));
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(CommentProperties.class.getName(), methodName);
                }
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName, NewAttachmentRequestBody.class.getName());
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Update an existing comment.
     *
     * @param serverName   name of the server instances for this request.
     * @param commentGUID  unique identifier for the comment to change.
     * @param urlMarker  view service URL marker
     * @param requestBody  containing type of comment enum and the text of the comment.
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid.
     * PropertyServerException There is a problem updating the element properties in the metadata repository.
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse   updateComment(String                   serverName,
                                        String                   urlMarker,
                                        String                   commentGUID,
                                        UpdateElementRequestBody requestBody)
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
                if (requestBody.getProperties() instanceof CommentProperties commentProperties)
                {
                    CommentHandler handler = instanceHandler.getCommentHandler(userId, serverName, urlMarker, methodName);

                    handler.updateComment(userId, commentGUID, requestBody, commentProperties);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(CommentProperties.class.getName(), methodName);
                }
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName, UpdateElementRequestBody.class.getName());
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
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
     * @param urlMarker  view service URL marker
     * @param requestBody optional effective time
     *
     * @return  void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse setupAcceptedAnswer(String                     serverName,
                                            String                     urlMarker,
                                            String                     questionCommentGUID,
                                            String                     answerCommentGUID,
                                            NewRelationshipRequestBody requestBody)
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

            CommentHandler handler = instanceHandler.getCommentHandler(userId, serverName, urlMarker, methodName);

            handler.setupAcceptedAnswer(userId, questionCommentGUID, answerCommentGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
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
     * @param urlMarker  view service URL marker
     * @param requestBody optional effective time
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse clearAcceptedAnswer(String            serverName,
                                            String            urlMarker,
                                            String            questionCommentGUID,
                                            String            answerCommentGUID,
                                            DeleteRelationshipRequestBody requestBody)
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

            CommentHandler handler = instanceHandler.getCommentHandler(userId, serverName, urlMarker, methodName);

            handler.clearAcceptedAnswer(userId, questionCommentGUID, answerCommentGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Removes a comment added to the element by this user.
     *
     * @param serverName name of the server instances for this request
     * @param commentGUID  String - unique id for the comment object
     * @param urlMarker  view service URL marker
     * @param requestBody optional effective time
     *
     * @return void or
     * InvalidParameterException - one of the parameters is null or invalid or
     * PropertyServerException - there is a problem updating the element properties in
     *                                   the metadata repository or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public VoidResponse removeCommentFromElement(String            serverName,
                                                 String            urlMarker,
                                                 String            commentGUID,
                                                 DeleteElementRequestBody requestBody)
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

            CommentHandler handler = instanceHandler.getCommentHandler(userId, serverName, urlMarker, methodName);

            handler.deleteComment(userId, commentGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Return the requested comment.
     *
     * @param serverName name of the server instances for this request
     * @param commentGUID  unique identifier for the comment object.
     * @param urlMarker  view service URL marker
     * @param requestBody optional effective time
     * @return comment properties or
     *  InvalidParameterException one of the parameters is null or invalid.
     *  PropertyServerException there is a problem updating the element properties in the property server.
     *  UserNotAuthorizedException the user does not have permission to perform this request.
     */
    public OpenMetadataRootElementResponse getCommentByGUID(String         serverName,
                                                            String         urlMarker,
                                                            String         commentGUID,
                                                            GetRequestBody requestBody)
    {
        final String methodName = "getComment";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        OpenMetadataRootElementResponse response = new OpenMetadataRootElementResponse();
        AuditLog        auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            CommentHandler handler = instanceHandler.getCommentHandler(userId, serverName, urlMarker, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            response.setElement(handler.getCommentByGUID(userId, commentGUID, requestBody));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Return the comments attached to an element.
     *
     * @param serverName name of the server instances for this request
     * @param elementGUID    unique identifier for the element that the comments are connected to (maybe a comment too).
     * @param urlMarker  view service URL marker
     * @param requestBody optional effective time
     * @return list of comments or
     *  InvalidParameterException one of the parameters is null or invalid.
     *  PropertyServerException there is a problem updating the element properties in the property server.
     *  UserNotAuthorizedException the user does not have permission to perform this request.
     */
    public OpenMetadataRootElementsResponse getAttachedComments(String             serverName,
                                                                String             urlMarker,
                                                                String             elementGUID,
                                                                ResultsRequestBody requestBody)
    {
        final String methodName = "getAttachedComments";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog                         auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            CommentHandler handler = instanceHandler.getCommentHandler(userId, serverName, urlMarker, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            response.setElements(handler.getAttachedComments(userId, elementGUID, requestBody));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Retrieve the list of comment metadata elements that contain the search string.
     *
     * @param serverName name of the server to route the request to
     * @param urlMarker  view service URL marker
     * @param requestBody string to find in the properties
    *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public OpenMetadataRootElementsResponse findComments(String                  serverName,
                                                         String                  urlMarker,
                                                         SearchStringRequestBody requestBody)
    {
        final String methodName = "findComments";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog                auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            CommentHandler handler = instanceHandler.getCommentHandler(userId, serverName, urlMarker, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.findComments(userId,
                                                          requestBody.getSearchString(),
                                                          requestBody));
            }
            else
            {
                response.setElements(handler.findComments(userId, null, null));
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Creates a new informal tag and returns the unique identifier for it.
     *
     * @param serverName   name of the server instances for this request
     * @param urlMarker  view service URL marker
     * @param requestBody  contains the name of the tag and (optional) description of the tag
     *
     * @return guid for new tag or
     * InvalidParameterException - one of the parameters is invalid or
     * PropertyServerException - there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public GUIDResponse createInformalTag(String                serverName,
                                          String                urlMarker,
                                          NewElementRequestBody requestBody)
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
                InformalTagHandler handler = instanceHandler.getInformalTagHandler(userId, serverName, urlMarker, methodName);

                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

                if (requestBody.getProperties() instanceof InformalTagProperties informalTagProperties)
                {
                    response.setGUID(handler.createInformalTag(userId, requestBody, requestBody.getInitialClassifications(), informalTagProperties));
                }
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName, InformalTagProperties.class.getName());
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Updates the description of an existing tag (either private or public).
     *
     * @param serverName   name of the server instances for this request
     * @param tagGUID      unique id for the tag.
     * @param urlMarker  view service URL marker
     * @param requestBody  contains the name of the tag and (optional) description of the tag.
     *
     * @return boolean or
     * InvalidParameterException - one of the parameters is invalid or
     * PropertyServerException - there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public BooleanResponse   updateTagDescription(String                   serverName,
                                                  String                   urlMarker,
                                                  String                   tagGUID,
                                                  UpdateElementRequestBody requestBody)
    {
        final String methodName = "updateTagDescription";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        BooleanResponse  response = new BooleanResponse();
        AuditLog      auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            if (requestBody != null)
            {
                InformalTagHandler handler = instanceHandler.getInformalTagHandler(userId, serverName, urlMarker, methodName);

                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

                if (requestBody.getProperties() instanceof InformalTagProperties informalTagProperties)
                {
                    handler.updateTagDescription(userId, tagGUID, requestBody, informalTagProperties);
                }
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName, UpdateElementRequestBody.class.getName());
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Removes a tag from the repository.  All the relationships to referenceables are lost.
     *
     * @param serverName   name of the server instances for this request
     * @param tagGUID   unique id for the tag.
     * @param urlMarker  view service URL marker
     * @param requestBody  optional effective time.
     *
     * @return void or
     * InvalidParameterException - one of the parameters is invalid or
     * PropertyServerException - there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public VoidResponse   deleteTag(String            serverName,
                                    String            urlMarker,
                                    String            tagGUID,
                                    DeleteElementRequestBody requestBody)
    {
        final String methodName = "deleteTag";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse  response = new VoidResponse();
        AuditLog      auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            InformalTagHandler handler = instanceHandler.getInformalTagHandler(userId, serverName, urlMarker, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            handler.deleteTag(userId, tagGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Return the tag for the supplied unique identifier (guid).
     *
     * @param serverName   name of the server instances for this request
     * @param guid unique identifier of the tag.
     * @param urlMarker  view service URL marker
     * @param requestBody optional effective time
     *
     * @return Tag object or
     * InvalidParameterException - one of the parameters is invalid or
     * PropertyServerException - there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public OpenMetadataRootElementResponse getTag(String         serverName,
                                                  String         urlMarker,
                                                  String         guid, 
                                                  GetRequestBody requestBody)
    {
        final String methodName = "getTag";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        OpenMetadataRootElementResponse response = new OpenMetadataRootElementResponse();
        AuditLog                        auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            InformalTagHandler handler = instanceHandler.getInformalTagHandler(userId, serverName, urlMarker, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            response.setElement(handler.getTag(userId, guid, requestBody));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Return the list of tags exactly matching the supplied name.
     *
     * @param serverName name of the server instances for this request
     * @param urlMarker  view service URL marker
     * @param requestBody name of tag.
     *
     * @return tag list or
     * InvalidParameterException - one of the parameters is invalid or
     * PropertyServerException - there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public OpenMetadataRootElementsResponse getTagsByName(String            serverName, 
                                                          String            urlMarker, 
                                                          FilterRequestBody requestBody)
    {
        final String methodName = "getTagsByName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog                         auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            InformalTagHandler handler = instanceHandler.getInformalTagHandler(userId, serverName, urlMarker, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.getTagsByName(userId,
                                                           requestBody.getFilter(),
                                                           requestBody));
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Return the list of tags containing the supplied string in either the name or description.
     *
     * @param serverName name of the server to route the request to
     * @param urlMarker  view service URL marker
     * @param requestBody string to find in the properties
     *
     * @return tag list or
     * InvalidParameterException - one of the parameters is invalid or
     * PropertyServerException - there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public OpenMetadataRootElementsResponse findTags(String                  serverName, 
                                                     String                  urlMarker, 
                                                     SearchStringRequestBody requestBody)
    {
        final String methodName = "findTags";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog             auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            InformalTagHandler handler = instanceHandler.getInformalTagHandler(userId, serverName, urlMarker, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.findTags(userId,
                                                      requestBody.getSearchString(),
                                                      requestBody));
            }
            else
            {
                response.setElements(handler.findTags(userId, null, null));
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Return the list of the calling user's private tags containing the supplied string in either the name or description.
     *
     * @param serverName name of the server to route the request to
     * @param urlMarker  view service URL marker
     * @param requestBody string to find in the properties
     *
     * @return tag list or
     * InvalidParameterException - one of the parameters is invalid or
     * PropertyServerException - there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public OpenMetadataRootElementsResponse findMyTags(String                  serverName, 
                                                       String                  urlMarker, 
                                                       SearchStringRequestBody requestBody)
    {
        final String methodName = "findMyTags";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog                         auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            InformalTagHandler handler = instanceHandler.getInformalTagHandler(userId, serverName, urlMarker, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.findMyTags(userId,
                                                        requestBody.getSearchString(),
                                                        requestBody));
            }
            else
            {
                response.setElements(handler.findMyTags(userId, null, null));
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
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
     * @param urlMarker  view service URL marker
     * @param requestBody  feedback request body.
     *
     * @return void or
     * InvalidParameterException - one of the parameters is invalid or
     * PropertyServerException - there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public VoidResponse   addTagToElement(String                    serverName,
                                          String                    urlMarker,
                                          String                    elementGUID,
                                          String                    tagGUID,
                                          MetadataSourceRequestBody requestBody)
    {
        final String methodName = "addTagToElement";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse  response = new VoidResponse();
        AuditLog      auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            InformalTagHandler handler = instanceHandler.getInformalTagHandler(userId, serverName, urlMarker, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            handler.addTagToElement(userId, elementGUID, tagGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
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
     * @param urlMarker  view service URL marker
     * @param requestBody optional effective time
     *
     * @return void or
     * InvalidParameterException - one of the parameters is invalid or
     * PropertyServerException - there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public VoidResponse   removeTagFromElement(String            serverName,
                                               String            urlMarker,
                                               String            elementGUID,
                                               String            tagGUID,
                                               DeleteRelationshipRequestBody requestBody)
    {
        final String   methodName  = "removeTagFromElement";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse  response = new VoidResponse();
        AuditLog      auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            InformalTagHandler handler = instanceHandler.getInformalTagHandler(userId, serverName, urlMarker, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            handler.removeTagFromElement(userId, elementGUID, tagGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
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
     * @param urlMarker  view service URL marker
     * @param requestBody optional effective time
     *
     * @return element stubs list or
     * InvalidParameterException the userId is null or invalid or
     * PropertyServerException there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public OpenMetadataRootElementsResponse getElementsByTag(String             serverName,
                                                             String             urlMarker,
                                                             String             tagGUID,
                                                             ResultsRequestBody requestBody)
    {
        final String methodName = "getElementsByTag";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog                            auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            InformalTagHandler handler = instanceHandler.getInformalTagHandler(userId, serverName, urlMarker, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            response.setElements(handler.getElementsByTag(userId, tagGUID, requestBody));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Return the informal tags attached to an element.
     *
     * @param serverName name of the server instances for this request
     * @param urlMarker  view service URL marker
     * @param elementGUID    unique identifier for the element that the comments are connected to (maybe a comment too).
     * @param requestBody optional effective time
     * @return list of tags or
     *  InvalidParameterException one of the parameters is null or invalid.
     *  PropertyServerException there is a problem updating the element properties in the property server.
     *  UserNotAuthorizedException the user does not have permission to perform this request.
     */
    public OpenMetadataRootElementsResponse getAttachedTags(String             serverName, 
                                                            String             urlMarker,
                                                            String             elementGUID,
                                                            ResultsRequestBody requestBody)
    {
        final String methodName = "getAttachedTags";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog                         auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            InformalTagHandler handler = instanceHandler.getInformalTagHandler(userId, serverName, urlMarker, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            response.setElements(handler.getAttachedTags(userId, elementGUID, requestBody));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
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
     * @param urlMarker  view service URL marker
     * @param elementGUID unique identifier of the element where the note log is located
     * @param requestBody  contains the name of the tag and (optional) description of the tag
     *
     * @return guid for new tag or
     * InvalidParameterException - one of the parameters is invalid or
     * PropertyServerException - there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public GUIDResponse createNoteLog(String                 serverName,
                                      String                 urlMarker,
                                      String                 elementGUID,
                                      NewAttachmentRequestBody requestBody)
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
                NoteLogHandler handler = instanceHandler.getNoteLogHandler(userId, serverName, urlMarker, methodName);

                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

                if (requestBody.getProperties() instanceof NoteLogProperties noteLogProperties)
                {
                    response.setGUID(handler.createNoteLog(userId, elementGUID, requestBody, requestBody.getInitialClassifications(), noteLogProperties));
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(NoteLogProperties.class.getName(), methodName);
                }
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName, NoteLogProperties.class.getName());
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Creates a new noteLog and returns the unique identifier for it.
     *
     * @param serverName   name of the server instances for this request
     * @param urlMarker  view service URL marker
     * @param requestBody  contains the name of the tag and (optional) description of the tag
     *
     * @return guid for new tag or
     * InvalidParameterException - one of the parameters is invalid or
     * PropertyServerException - there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public GUIDResponse createNoteLog(String                serverName,
                                      String                urlMarker,
                                      NewElementRequestBody requestBody)
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
                NoteLogHandler handler = instanceHandler.getNoteLogHandler(userId, serverName, urlMarker, methodName);

                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

                if (requestBody.getProperties() instanceof NoteLogProperties noteLogProperties)
                {
                    response.setGUID(handler.createNoteLog(userId,
                                                           requestBody,
                                                           requestBody.getInitialClassifications(),
                                                           noteLogProperties,
                                                           requestBody.getParentRelationshipProperties()));                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(NoteLogProperties.class.getName(), methodName);
                }
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName, NoteLogProperties.class.getName());
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Update an existing note log.
     *
     * @param serverName   name of the server instances for this request.
     * @param noteLogGUID  unique identifier for the note log to change.
     * @param urlMarker  view service URL marker
     * @param requestBody  containing type of comment enum and the text of the comment.
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid.
     * PropertyServerException There is a problem updating the element properties in the metadata repository.
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse   updateNoteLog(String                   serverName,
                                        String                   urlMarker,
                                        String                   noteLogGUID,
                                        UpdateElementRequestBody requestBody)
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
                if (requestBody.getProperties() instanceof  NoteLogProperties noteLogProperties)
                {
                    NoteLogHandler handler = instanceHandler.getNoteLogHandler(userId, serverName, urlMarker, methodName);

                    handler.updateNoteLog(userId, noteLogGUID, requestBody, noteLogProperties);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(NoteLogProperties.class.getName(), methodName);
                }
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName, UpdateElementRequestBody.class.getName());
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Removes a note log from the repository.  All the relationships to referenceables are lost.
     *
     * @param serverName   name of the server instances for this request
     * @param noteLogGUID   unique id for the note log.
     * @param urlMarker  view service URL marker
     * @param requestBody optional effective time
     *
     * @return void or
     * InvalidParameterException - one of the parameters is invalid or
     * PropertyServerException - there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public VoidResponse   deleteNoteLog(String            serverName,
                                        String            urlMarker,
                                        String            noteLogGUID,
                                        DeleteElementRequestBody requestBody)
    {
        final String methodName = "deleteNoteLog";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse  response = new VoidResponse();
        AuditLog      auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            NoteLogHandler handler = instanceHandler.getNoteLogHandler(userId, serverName, urlMarker, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            handler.removeNoteLog(userId, noteLogGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Retrieve the list of note log metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param serverName name of the server to route the request to
     * @param urlMarker  view service URL marker
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public OpenMetadataRootElementsResponse findNoteLogs(String                  serverName, 
                                                         String                  urlMarker, 
                                                         SearchStringRequestBody requestBody)
    {
        final String methodName = "findNoteLogs";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog                         auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            NoteLogHandler handler = instanceHandler.getNoteLogHandler(userId, serverName, urlMarker, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.findNoteLogs(userId, requestBody.getSearchString(), requestBody));
            }
            else
            {
                response.setElements(handler.findNoteLogs(userId, null, null));
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Retrieve the list of note log metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param serverName   name of the server instances for this request
     * @param urlMarker  view service URL marker
     * @param requestBody name to search for and correlators
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public OpenMetadataRootElementsResponse getNoteLogsByName(String            serverName, 
                                                              String            urlMarker, 
                                                              FilterRequestBody requestBody)
    {
        final String methodName = "getNoteLogsByName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog                         auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            NoteLogHandler handler = instanceHandler.getNoteLogHandler(userId, serverName, urlMarker, methodName);

           if (requestBody != null)
           {
               response.setElements(handler.getNoteLogsByName(userId, requestBody.getFilter(), requestBody));
           }
           else
           {
               restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
           }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }



    /**
     * Retrieve the list of note log metadata elements attached to the element.
     *
     * @param serverName   name of the server instances for this request
     * @param urlMarker  view service URL marker
     * @param elementGUID unique identifier of the note log of interest
     * @param requestBody optional effective time
     *
     * @return list of associated metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public OpenMetadataRootElementsResponse getNoteLogsForElement(String             serverName, 
                                                                  String             urlMarker, 
                                                                  String             elementGUID, 
                                                                  ResultsRequestBody requestBody)
    {
        final String methodName = "getNotesForNoteLog";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog                         auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            NoteLogHandler handler = instanceHandler.getNoteLogHandler(userId, serverName, urlMarker, methodName);

            response.setElements(handler.getNoteLogsForElement(userId, elementGUID, requestBody));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }



    /**
     * Retrieve the note log metadata element with the supplied unique identifier.
     *
     * @param serverName   name of the server instances for this request
     * @param noteLogGUID unique identifier of the requested metadata element
     * @param urlMarker  view service URL marker
     * @param requestBody optional effective time
     *
     * @return requested metadata element or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public OpenMetadataRootElementResponse getNoteLogByGUID(String         serverName, 
                                                            String         urlMarker, 
                                                            String         noteLogGUID, 
                                                            GetRequestBody requestBody)
    {
        final String methodName = "getNoteLogByGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        OpenMetadataRootElementResponse response = new OpenMetadataRootElementResponse();
        AuditLog                        auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            NoteLogHandler handler = instanceHandler.getNoteLogHandler(userId, serverName, urlMarker, methodName);

            response.setElement(handler.getNoteLogByGUID(userId, noteLogGUID, requestBody));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /* ===============================================================================
     * A note log typically contains many notes, linked with relationships.
     */


    /**
     * Retrieve the list of notes associated with a note log.
     *
     * @param serverName   name of the server instances for this request
     * @param noteLogGUID unique identifier of the note log of interest
     * @param urlMarker  view service URL marker
     * @param requestBody optional effective time
     *
     * @return list of associated metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public OpenMetadataRootElementsResponse getNotesForNoteLog(String             serverName,
                                                               String             urlMarker,
                                                               String             noteLogGUID, 
                                                               ResultsRequestBody requestBody)
    {
        final String methodName = "getNotesForNoteLog";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog                         auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            NoteLogHandler handler = instanceHandler.getNoteLogHandler(userId, serverName, urlMarker, methodName);

            response.setElements(handler.getNotesForNoteLog(userId, noteLogGUID, requestBody));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }

}
