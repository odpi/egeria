/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetconsumer.server;

import org.odpi.openmetadata.accessservices.assetconsumer.handlers.*;
import org.odpi.openmetadata.accessservices.assetconsumer.rest.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.odpi.openmetadata.accessservices.assetconsumer.ffdc.exceptions.*;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.CommentType;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.StarRating;

import java.util.HashMap;
import java.util.Map;


/**
 * The AssetConsumerRESTServices provides the server-side implementation of the Asset Consumer Open Metadata
 * Assess Service (OMAS).  This interface provides connections to assets and APIs for adding feedback
 * on the asset.
 */
public class AssetConsumerRESTServices
{
    private static AssetConsumerInstanceHandler   instanceHandler     = new AssetConsumerInstanceHandler();

    private static final Logger log = LoggerFactory.getLogger(AssetConsumerRESTServices.class);


    /**
     * Default constructor
     */
    public AssetConsumerRESTServices()
    {
    }


    /*
     * ===========================================
     * AssetConsumerAssetInterface
     * ===========================================
     */

    /**
     * Returns the unique identifier for the asset connected to the connection.
     *
     * @param serverName name of the server instances for this request
     * @param userId the userId of the requesting user.
     * @param connectionGUID  uniqueId for the connection.
     *
     * @return unique identifier of asset or
     * InvalidParameterException - one of the parameters is null or invalid or
     * PropertyServerException - there is a problem retrieving the connected asset properties from the property server or
     * UnrecognizedConnectionGUIDException - the supplied GUID is not recognized by the property server or
     * NoConnectedAssetException - there is no asset associated with this connection or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public GUIDResponse getAssetForConnection(String   serverName,
                                              String   userId,
                                              String   connectionGUID)
    {
        final String        methodName = "getAssetForConnection";

        log.debug("Calling method: " + methodName);

        GUIDResponse  response = new GUIDResponse();

        try
        {
            AssetHandler assetHandler = new AssetHandler(instanceHandler.getAccessServiceName(),
                                                         instanceHandler.getRepositoryConnector(serverName));

            response.setGUID(assetHandler.getAssetForConnection(userId, connectionGUID));
        }
        catch (InvalidParameterException  error)
        {
            captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException  error)
        {
            capturePropertyServerException(response, error);
        }
        catch (NoConnectedAssetException error)
        {
            captureNoConnectedAssetException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            captureUserNotAuthorizedException(response, error);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        return response;
    }


    /*
     * ===========================================
     * AssetConsumerConnectorFactoryInterface
     * ===========================================
     */


    /**
     * Returns the connection object corresponding to the supplied connection name.
     *
     * @param serverName name of the server instances for this request
     * @param userId userId of user making request.
     * @param name   this may be the qualifiedName or displayName of the connection.
     *
     * @return connection object or
     * InvalidParameterException - one of the parameters is null or invalid or
     * UnrecognizedConnectionNameException - there is no connection defined for this name or
     * AmbiguousConnectionNameException - there is more than one connection defined for this name or
     * PropertyServerException - there is a problem retrieving information from the property (metadata) server or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public ConnectionResponse getConnectionByName(String   serverName,
                                                  String   userId,
                                                  String   name)
    {
        final String        methodName = "getConnectionByName";

        log.debug("Calling method: " + methodName);

        ConnectionResponse  response = new ConnectionResponse();

        try
        {
            ConnectionHandler   connectionHandler = new ConnectionHandler(instanceHandler.getAccessServiceName(),
                                                                          instanceHandler.getRepositoryConnector(serverName));

            response.setConnection(connectionHandler.getConnectionByName(userId, name));
        }
        catch (InvalidParameterException error)
        {
            captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException error)
        {
            capturePropertyServerException(response, error);
        }
        catch (AmbiguousConnectionNameException  error)
        {
            captureAmbiguousConnectionNameException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            captureUserNotAuthorizedException(response, error);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        return response;
    }


    /**
     * Returns the connection object corresponding to the supplied connection GUID.
     *
     * @param serverName name of the server instances for this request
     * @param userId userId of user making request.
     * @param guid  the unique id for the connection within the property server.
     *
     * @return connection object or
     * InvalidParameterException - one of the parameters is null or invalid or
     * UnrecognizedConnectionGUIDException - the supplied GUID is not recognized by the metadata repository or
     * PropertyServerException - there is a problem retrieving information from the property (metadata) server or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public ConnectionResponse getConnectionByGUID(String     serverName,
                                                  String     userId,
                                                  String     guid)
    {
        final String        methodName = "getConnectionByGUID";

        log.debug("Calling method: " + methodName);

        ConnectionResponse  response = new ConnectionResponse();

        try
        {
            ConnectionHandler   connectionHandler = new ConnectionHandler(instanceHandler.getAccessServiceName(),
                                                                          instanceHandler.getRepositoryConnector(serverName));

            response.setConnection(connectionHandler.getConnectionByGUID(userId, guid));
        }
        catch (InvalidParameterException  error)
        {
            captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException  error)
        {
            capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            captureUserNotAuthorizedException(response, error);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        return response;
    }


    /*
     * ===========================================
     * AssetConsumerFeedbackInterface
     * ===========================================
     */


    /**
     * Adds a star rating and optional review text to the asset.
     *
     * @param serverName name of the server instances for this request
     * @param userId      String - userId of user making request.
     * @param guid        String - unique id for the asset.
     * @param requestBody containing the StarRating and user review of asset.
     *
     * @return guid of new review object or
     * InvalidParameterException - one of the parameters is null or invalid or
     * PropertyServerException - there is a problem adding the asset properties to
     *                                   the metadata repository or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public GUIDResponse addReviewToAsset(String            serverName,
                                         String            userId,
                                         String            guid,
                                         ReviewRequestBody requestBody)
    {
        final String        methodName = "addReviewToAsset";

        log.debug("Calling method: " + methodName);

        GUIDResponse  response = new GUIDResponse();

        try
        {
            StarRating starRating = null;
            String     review = null;

            if (requestBody != null)
            {
                starRating = requestBody.getStarRating();
                review = requestBody.getReview();
            }

            FeedbackHandler feedbackHandler = new FeedbackHandler(instanceHandler.getAccessServiceName(),
                                                                  instanceHandler.getRepositoryConnector(serverName));

            response.setGUID(feedbackHandler.addReviewToAsset(userId, guid, starRating, review));
        }
        catch (InvalidParameterException  error)
        {
            captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException  error)
        {
            capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            captureUserNotAuthorizedException(response, error);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        return response;
    }


    /**
     * Updates the rating and optional review text attached to the asset by this user.
     *
     * @param userId      userId of user making request.
     * @param guid        unique identifier for the review.
     * @param requestBody provides the StarRating enumeration for none, one to five stars plus
     *                    optional review test.
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid.
     * PropertyServerException There is a problem updating the asset properties in the metadata repository.
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse   updateReviewOnAsset(String              serverName,
                                              String              userId,
                                              String              guid,
                                              ReviewRequestBody   requestBody)
    {
        final String        methodName = "updateReviewOnAsset";

        log.debug("Calling method: " + methodName);

        VoidResponse  response = new VoidResponse();

        try
        {
            StarRating starRating = null;
            String     review = null;

            if (requestBody != null)
            {
                starRating = requestBody.getStarRating();
                review = requestBody.getReview();
            }

            FeedbackHandler feedbackHandler = new FeedbackHandler(instanceHandler.getAccessServiceName(),
                                                                  instanceHandler.getRepositoryConnector(serverName));

            feedbackHandler.updateReviewOnAsset(userId, guid, starRating, review);
        }
        catch (InvalidParameterException  error)
        {
            captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException  error)
        {
            capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            captureUserNotAuthorizedException(response, error);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        return response;
    }


    /**
     * Removes a star rating that was added to the asset by this user.
     *
     * @param serverName name of the server instances for this request
     * @param userId      String - userId of user making request.
     * @param guid        String - unique id for the rating object
     * @param requestBody null request body needed to satisfy the HTTP Post request
     *
     * @return void or
     * InvalidParameterException - one of the parameters is null or invalid or
     * PropertyServerException - there is a problem updating the asset properties in
     *                                   the metadata repository or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public VoidResponse removeReviewFromAsset(String          serverName,
                                              String          userId,
                                              String          guid,
                                              NullRequestBody requestBody)
    {
        final String        methodName = "removeRating";

        log.debug("Calling method: " + methodName);

        VoidResponse  response = new VoidResponse();

        try
        {
            FeedbackHandler feedbackHandler = new FeedbackHandler(instanceHandler.getAccessServiceName(),
                                                                  instanceHandler.getRepositoryConnector(serverName));

            feedbackHandler.removeReviewFromAsset(userId, guid);
        }
        catch (InvalidParameterException  error)
        {
            captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException  error)
        {
            capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            captureUserNotAuthorizedException(response, error);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        return response;
    }


    /**
     * Adds a "Like" to the asset.
     *
     * @param serverName name of the server instances for this request
     * @param userId      String - userId of user making request.
     * @param guid        String - unique id for the asset.
     * @param requestBody null request body to satisfy HTTP protocol.
     *
     * @return guid of new like or
     * InvalidParameterException - one of the parameters is null or invalid or
     * PropertyServerException - there is a problem adding the asset properties to
     *                                   the metadata repository or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public GUIDResponse addLikeToAsset(String          serverName,
                                       String          userId,
                                       String          guid,
                                       NullRequestBody requestBody)
    {
        final String        methodName = "addLikeToAsset";

        log.debug("Calling method: " + methodName);

        GUIDResponse  response = new GUIDResponse();

        try
        {
            FeedbackHandler   feedbackHandler = new FeedbackHandler(instanceHandler.getAccessServiceName(),
                                                                    instanceHandler.getRepositoryConnector(serverName));

            response.setGUID(feedbackHandler.addLikeToAsset(userId, guid));
        }
        catch (InvalidParameterException  error)
        {
            captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException  error)
        {
            capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            captureUserNotAuthorizedException(response, error);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        return response;
    }


    /**
     * Removes a "Like" added to the asset by this user.
     *
     * @param serverName name of the server instances for this request
     * @param userId  String - userId of user making request.
     * @param guid  String - unique id for the like object
     * @param requestBody null request body needed to satisfy the HTTP Post request
     *
     * @return void or
     * InvalidParameterException - one of the parameters is null or invalid or
     * PropertyServerException - there is a problem updating the asset properties in
     *                                   the metadata repository or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public VoidResponse removeLikeFromAsset(String          serverName,
                                            String          userId,
                                            String          guid,
                                            NullRequestBody requestBody)
    {
        final String        methodName = "removeLikeFromAsset";

        log.debug("Calling method: " + methodName);

        VoidResponse  response = new VoidResponse();

        try
        {
            FeedbackHandler feedbackHandler = new FeedbackHandler(instanceHandler.getAccessServiceName(),
                                                                  instanceHandler.getRepositoryConnector(serverName));

            feedbackHandler.removeLikeFromAsset(userId, guid);
        }
        catch (InvalidParameterException  error)
        {
            captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException  error)
        {
            capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            captureUserNotAuthorizedException(response, error);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        return response;
    }


    /**
     * Adds a comment to the asset.
     *
     * @param serverName name of the server instances for this request
     * @param userId  String - userId of user making request.
     * @param guid  String - unique id for the asset.
     * @param requestBody containing type of comment enum and the text of the comment.
     *
     * @return guid for new comment object or
     * InvalidParameterException - one of the parameters is null or invalid or
     * PropertyServerException - there is a problem adding the asset properties to
     *                                   the metadata repository or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public GUIDResponse addCommentToAsset(String             serverName,
                                          String             userId,
                                          String             guid,
                                          CommentRequestBody requestBody)
    {
        final String        methodName = "addCommentToAsset";

        log.debug("Calling method: " + methodName);

        GUIDResponse  response = new GUIDResponse();

        try
        {
            CommentType commentType = null;
            String      commentText = null;

            if (requestBody != null)
            {
                commentType = requestBody.getCommentType();
                commentText = requestBody.getCommentText();
            }

            FeedbackHandler   feedbackHandler = new FeedbackHandler(instanceHandler.getAccessServiceName(),
                                                                    instanceHandler.getRepositoryConnector(serverName));

            response.setGUID(feedbackHandler.addCommentToAsset(userId, guid, commentType, commentText));
        }
        catch (InvalidParameterException  error)
        {
            captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException  error)
        {
            capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            captureUserNotAuthorizedException(response, error);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        return response;
    }


    /**
     * Adds a reply to a comment.
     *
     * @param serverName name of the server instances for this request
     * @param userId       String - userId of user making request.
     * @param commentGUID  String - unique id for an existing comment.  Used to add a reply to a comment.
     * @param requestBody  containing type of comment enum and the text of the comment.
     *
     * @return guid for new comment object or
     * InvalidParameterException - one of the parameters is null or invalid or
     * PropertyServerException - there is a problem adding the asset properties to
     *                                   the metadata repository or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public GUIDResponse addCommentReply(String             serverName,
                                        String             userId,
                                        String             commentGUID,
                                        CommentRequestBody requestBody)
    {
        final String        methodName = "addCommentReply";

        log.debug("Calling method: " + methodName);

        GUIDResponse  response = new GUIDResponse();

        try
        {
            CommentType commentType = null;
            String      commentText = null;

            if (requestBody != null)
            {
                commentType = requestBody.getCommentType();
                commentText = requestBody.getCommentText();
            }

            FeedbackHandler   feedbackHandler = new FeedbackHandler(instanceHandler.getAccessServiceName(),
                                                                    instanceHandler.getRepositoryConnector(serverName));

            response.setGUID(feedbackHandler.addCommentReply(userId, commentGUID, commentType, commentText));
        }
        catch (InvalidParameterException  error)
        {
            captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException  error)
        {
            capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            captureUserNotAuthorizedException(response, error);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        return response;
    }


    /**
     * Update an existing comment.
     *
     * @param serverName   name of the server instances for this request.
     * @param userId       userId of user making request.
     * @param guid         unique identifier for the comment to change.
     * @param requestBody  containing type of comment enum and the text of the comment.
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid.
     * PropertyServerException There is a problem updating the asset properties in the metadata repository.
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse   updateComment(String              serverName,
                                        String              userId,
                                        String              guid,
                                        CommentRequestBody  requestBody)
    {
        final String        methodName = "updateComment";

        log.debug("Calling method: " + methodName);


        VoidResponse  response = new VoidResponse();

        try
        {
            CommentType commentType = null;
            String      commentText = null;

            if (requestBody != null)
            {
                commentType = requestBody.getCommentType();
                commentText = requestBody.getCommentText();
            }

            FeedbackHandler feedbackHandler = new FeedbackHandler(instanceHandler.getAccessServiceName(),
                                                                  instanceHandler.getRepositoryConnector(serverName));

            feedbackHandler.updateComment(userId, guid, commentType, commentText);
        }
        catch (InvalidParameterException  error)
        {
            captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException  error)
        {
            capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            captureUserNotAuthorizedException(response, error);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        return response;
    }


    /**
     * Removes a comment added to the asset by this user.
     *
     * @param serverName name of the server instances for this request
     * @param userId  String - userId of user making request.
     * @param guid  String - unique id for the comment object
     * @param requestBody null request body needed to satisfy the HTTP Post request
     *
     * @return void or
     * InvalidParameterException - one of the parameters is null or invalid or
     * PropertyServerException - there is a problem updating the asset properties in
     *                                   the metadata repository or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public VoidResponse removeCommentFromAsset(String          serverName,
                                               String          userId,
                                               String          guid,
                                               NullRequestBody requestBody)
    {
        final String        methodName = "removeCommentFromAsset";

        log.debug("Calling method: " + methodName);

        VoidResponse  response = new VoidResponse();

        try
        {
            FeedbackHandler feedbackHandler = new FeedbackHandler(instanceHandler.getAccessServiceName(),
                                                                  instanceHandler.getRepositoryConnector(serverName));

            feedbackHandler.removeCommentFromAsset(userId, guid);
        }
        catch (InvalidParameterException  error)
        {
            captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException  error)
        {
            capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            captureUserNotAuthorizedException(response, error);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        return response;
    }


    /*
     * ===========================================
     * AssetConsumerGlossaryInterface
     * ===========================================
     */


    /**
     * Return the full definition (meaning) of a term using the unique identifier of the glossary term.
     *
     * @param serverName name of the server instances for this request
     * @param userId userId of the user making the request.
     * @param guid unique identifier of the meaning.
     *
     * @return glossary term object or
     * InvalidParameterException the userId is null or invalid or
     * NoProfileForUserException the user does not have a profile or
     * PropertyServerException there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public MeaningResponse getMeaning(String   serverName,
                                      String   userId,
                                      String   guid)
    {
        final String        methodName = "getMeaning";

        log.debug("Calling method: " + methodName);

        MeaningResponse  response = new MeaningResponse();

        try
        {
            MeaningHandler meaningHandler = new MeaningHandler(instanceHandler.getAccessServiceName(),
                                                               instanceHandler.getRepositoryConnector(serverName));

            response.setGlossaryTerm(meaningHandler.getMeaning(userId, guid));
        }
        catch (InvalidParameterException  error)
        {
            captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException  error)
        {
            capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            captureUserNotAuthorizedException(response, error);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        return response;
    }


    /**
     * Return the full definition (meaning) of the terms matching the supplied name.
     *
     * @param serverName name of the server instances for this request
     * @param userId the name of the calling user.
     * @param term name of term.  This may include wild card characters.
     * @param startFrom  index of the list ot start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return list of glossary terms or
     * InvalidParameterException - one of the parameters is invalid or
     * PropertyServerException - there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public MeaningListResponse getMeaningByName(String  serverName,
                                                String  userId,
                                                String  term,
                                                int     startFrom,
                                                int     pageSize)
    {
        final String        methodName = "getMeaningByName";

        log.debug("Calling method: " + methodName);

        MeaningListResponse  response = new MeaningListResponse();

        try
        {
            MeaningHandler   meaningHandler = new MeaningHandler(instanceHandler.getAccessServiceName(),
                                                                 instanceHandler.getRepositoryConnector(serverName));

            response.setMeanings(meaningHandler.getMeaningByName(userId, term, startFrom, pageSize));
        }
        catch (InvalidParameterException  error)
        {
            captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException  error)
        {
            capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            captureUserNotAuthorizedException(response, error);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        return response;
    }


    /*
     * ===========================================
     * AssetConsumerLoggingInterface
     * ===========================================
     */


    /**
     * Creates an Audit log record for the asset.  This log record is stored in the Asset's Audit Log.
     *
     * @param serverName name of the server instances for this request
     * @param userId  String - userId of user making request.
     * @param guid  String - unique id for the asset.
     * @param requestBody containing:
     * connectorInstanceId  (String - (optional) id of connector in use (if any)),
     * connectionName  (String - (optional) name of the connection (extracted from the connector)),
     * connectorType  (String - (optional) type of connector in use (if any)),
     * contextId  (String - (optional) function name, or processId of the activity that the caller is performing),
     * message  (log record content).
     *
     * @return void or
     * InvalidParameterException - one of the parameters is null or invalid or
     * PropertyServerException - there is a problem adding the log message to the audit log for this asset or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public VoidResponse addLogMessageToAsset(String                serverName,
                                             String                userId,
                                             String                guid,
                                             LogRecordRequestBody  requestBody)
    {
        final String        methodName = "addLogMessageToAsset";

        log.debug("Calling method: " + methodName);

        VoidResponse  response = new VoidResponse();


        try
        {
            String      connectorInstanceId = null;
            String      connectionName = null;
            String      connectorType = null;
            String      contextId = null;
            String      message = null;

            if (requestBody != null)
            {
                connectorInstanceId = requestBody.getConnectorInstanceId();
                connectionName = requestBody.getConnectionName();
                connectorType = requestBody.getConnectorType();
                contextId = requestBody.getContextId();
                message = requestBody.getMessage();
            }

            AuditLogHandler auditLogHandler = new AuditLogHandler(instanceHandler.getAccessServiceName(),
                                                                  instanceHandler.getAuditLog(serverName));

            auditLogHandler.addLogMessageToAsset(userId,
                                                 guid,
                                                 connectorInstanceId,
                                                 connectionName,
                                                 connectorType,
                                                 contextId,
                                                 message);
        }
        catch (InvalidParameterException  error)
        {
            captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException  error)
        {
            capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            captureUserNotAuthorizedException(response, error);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        return response;
    }


    /*
     * ===========================================
     * AssetConsumerTaggingInterface
     * ===========================================
     */


    /**
     * Creates a new public informal tag and returns the unique identifier for it.
     *
     * @param serverName   name of the server instances for this request
     * @param userId           userId of user making request.
     * @param requestBody  contains the name of the tag and (optional) description of the tag.
     *
     * @return guid for new tag or
     * InvalidParameterException - one of the parameters is invalid or
     * PropertyServerException - there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public GUIDResponse createPublicTag(String         serverName,
                                        String         userId,
                                        TagRequestBody requestBody)
    {
        final String   methodName = "createPublicTag";

        log.debug("Calling method: " + methodName);

        GUIDResponse  response = new GUIDResponse();

        try
        {
            String      tagName = null;
            String      tagDescription = null;

            if (requestBody != null)
            {
                tagName = requestBody.getTagName();
                tagDescription = requestBody.getTagDescription();
            }

            TaggingHandler taggingHandler = new TaggingHandler(instanceHandler.getAccessServiceName(),
                                                               instanceHandler.getRepositoryConnector(serverName));

            response.setGUID(taggingHandler.createPublicTag(userId, tagName, tagDescription));
        }
        catch (InvalidParameterException  error)
        {
            captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException  error)
        {
            capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            captureUserNotAuthorizedException(response, error);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        return response;
    }


    /**
     * Creates a new private informal tag and returns the unique identifier for it.
     *
     * @param serverName   name of the server instances for this request
     * @param userId           userId of user making request.
     * @param requestBody  contains the name of the tag and (optional) description of the tag.
     *
     * @return guid for new tag or
     * InvalidParameterException - one of the parameters is invalid or
     * PropertyServerException - there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public GUIDResponse createPrivateTag(String         serverName,
                                         String         userId,
                                         TagRequestBody requestBody)
    {
        final String   methodName = "createPrivateTag";

        log.debug("Calling method: " + methodName);

        GUIDResponse  response = new GUIDResponse();

        try
        {
            String      tagName = null;
            String      tagDescription = null;

            if (requestBody != null)
            {
                tagName = requestBody.getTagName();
                tagDescription = requestBody.getTagDescription();
            }

            TaggingHandler   taggingHandler = new TaggingHandler(instanceHandler.getAccessServiceName(),
                                                                 instanceHandler.getRepositoryConnector(serverName));

            response.setGUID(taggingHandler.createPrivateTag(userId, tagName, tagDescription));
        }
        catch (InvalidParameterException  error)
        {
            captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException  error)
        {
            capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            captureUserNotAuthorizedException(response, error);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        return response;
    }


    /**
     * Updates the description of an existing tag (either private of public).
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
    public VoidResponse   updateTagDescription(String         serverName,
                                               String         userId,
                                               String         tagGUID,
                                               TagRequestBody requestBody)
    {
        final String   methodName = "updateTagDescription";

        log.debug("Calling method: " + methodName);

        VoidResponse  response = new VoidResponse();

        try
        {
            String      tagDescription = null;

            if (requestBody != null)
            {
                tagDescription = requestBody.getTagDescription();
            }

            TaggingHandler   taggingHandler = new TaggingHandler(instanceHandler.getAccessServiceName(),
                                                                 instanceHandler.getRepositoryConnector(serverName));

            taggingHandler.updateTagDescription(userId, tagGUID, tagDescription);
        }
        catch (InvalidParameterException  error)
        {
            captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException  error)
        {
            capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            captureUserNotAuthorizedException(response, error);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        return response;
    }


    /**
     * Removes a tag from the repository.  All of the relationships to assets are lost.
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
    public VoidResponse   deleteTag(String          serverName,
                                    String          userId,
                                    String          tagGUID,
                                    NullRequestBody requestBody)
    {
        final String   methodName = "removeTag";

        log.debug("Calling method: " + methodName);

        VoidResponse  response = new VoidResponse();

        try
        {
            TaggingHandler   taggingHandler = new TaggingHandler(instanceHandler.getAccessServiceName(),
                                                                 instanceHandler.getRepositoryConnector(serverName));

            taggingHandler.deleteTag(userId, tagGUID);
        }
        catch (InvalidParameterException  error)
        {
            captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException  error)
        {
            capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            captureUserNotAuthorizedException(response, error);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

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
    public TagResponse getTag(String serverName,
                              String userId,
                              String guid)
    {
        final String   methodName = "getTag";

        log.debug("Calling method: " + methodName);

        TagResponse  response = new TagResponse();

        try
        {
            TaggingHandler   taggingHandler = new TaggingHandler(instanceHandler.getAccessServiceName(),
                                                                 instanceHandler.getRepositoryConnector(serverName));

            response.setTag(taggingHandler.getTag(userId, guid));
        }
        catch (InvalidParameterException  error)
        {
            captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException  error)
        {
            capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            captureUserNotAuthorizedException(response, error);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        return response;
    }


    /**
     * Return the list of tags matching the supplied name.
     *
     * @param serverName name of the server instances for this request
     * @param userId the name of the calling user.
     * @param tagName name of tag.  This may include wild card characters.
     * @param startFrom  index of the list ot start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return tag list or
     * InvalidParameterException - one of the parameters is invalid or
     * PropertyServerException - there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public TagListResponse getTagsByName(String serverName,
                                         String userId,
                                         String tagName,
                                         int    startFrom,
                                         int    pageSize)
    {
        final String   methodName = "getTagsByName";

        log.debug("Calling method: " + methodName);

        TagListResponse  response = new TagListResponse();

        try
        {
            TaggingHandler   taggingHandler = new TaggingHandler(instanceHandler.getAccessServiceName(),
                                                                 instanceHandler.getRepositoryConnector(serverName));

            response.setTags(taggingHandler.getTagsByName(userId, tagName, startFrom, pageSize));
            response.setStartingFromElement(startFrom);
        }
        catch (InvalidParameterException  error)
        {
            captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException  error)
        {
            capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            captureUserNotAuthorizedException(response, error);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        return response;
    }


    /**
     * Adds a tag (either private of public) to an asset.
     *
     * @param serverName   name of the server instances for this request
     * @param userId       userId of user making request.
     * @param assetGUID    unique id for the asset.
     * @param tagGUID      unique id of the tag.
     * @param requestBody  null request body needed for correct protocol exchange.
     *
     * @return void or
     * InvalidParameterException - one of the parameters is invalid or
     * PropertyServerException - there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public VoidResponse   addTagToAsset(String          serverName,
                                        String          userId,
                                        String          assetGUID,
                                        String          tagGUID,
                                        NullRequestBody requestBody)
    {
        final String   methodName  = "addTagToAsset";

        log.debug("Calling method: " + methodName);

        VoidResponse  response = new VoidResponse();

        try
        {
            TaggingHandler   taggingHandler = new TaggingHandler(instanceHandler.getAccessServiceName(),
                                                                 instanceHandler.getRepositoryConnector(serverName));

            taggingHandler.addTagToAsset(userId, assetGUID, tagGUID);
        }
        catch (InvalidParameterException  error)
        {
            captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException  error)
        {
            capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            captureUserNotAuthorizedException(response, error);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        return response;
    }


    /**
     * Removes a tag from the asset that was added by this user.
     *
     * @param serverName   name of the server instances for this request
     * @param userId    userId of user making request.
     * @param assetGUID unique id for the asset.
     * @param tagGUID   unique id for the tag.
     * @param requestBody  null request body needed for correct protocol exchange.
     *
     * @return void or
     * InvalidParameterException - one of the parameters is invalid or
     * PropertyServerException - there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public VoidResponse   removeTagFromAsset(String          serverName,
                                             String          userId,
                                             String          assetGUID,
                                             String          tagGUID,
                                             NullRequestBody requestBody)
    {
        final String   methodName  = "removeTagFromAsset";

        log.debug("Calling method: " + methodName);

        VoidResponse  response = new VoidResponse();

        try
        {
            TaggingHandler   taggingHandler = new TaggingHandler(instanceHandler.getAccessServiceName(),
                                                                 instanceHandler.getRepositoryConnector(serverName));

            taggingHandler.addTagToAsset(userId, assetGUID, tagGUID);
        }
        catch (InvalidParameterException  error)
        {
            captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException  error)
        {
            capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            captureUserNotAuthorizedException(response, error);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        return response;
    }


    /* ==========================
     * Support methods
     * ==========================
     */


    /**
     * Set the exception information into the response.
     *
     * @param response  REST Response
     * @param error returned response.
     * @param exceptionClassName  class name of the exception to recreate
     */
    private void captureCheckedException(AssetConsumerOMASAPIResponse      response,
                                         AssetConsumerCheckedExceptionBase error,
                                         String                            exceptionClassName)
    {
        response.setRelatedHTTPCode(error.getReportedHTTPCode());
        response.setExceptionClassName(exceptionClassName);
        response.setExceptionErrorMessage(error.getErrorMessage());
        response.setExceptionSystemAction(error.getReportedSystemAction());
        response.setExceptionUserAction(error.getReportedUserAction());
    }


    /**
     * Set the exception information into the response.
     *
     * @param response  REST Response
     * @param error returned response.
     * @param exceptionClassName  class name of the exception to recreate
     * @param exceptionProperties map of properties stored in the exception to help with diagnostics
     */
    private void captureCheckedException(AssetConsumerOMASAPIResponse      response,
                                         AssetConsumerCheckedExceptionBase error,
                                         String                            exceptionClassName,
                                         Map<String, Object>               exceptionProperties)
    {
        response.setRelatedHTTPCode(error.getReportedHTTPCode());
        response.setExceptionClassName(exceptionClassName);
        response.setExceptionErrorMessage(error.getErrorMessage());
        response.setExceptionSystemAction(error.getReportedSystemAction());
        response.setExceptionUserAction(error.getReportedUserAction());
        response.setExceptionProperties(exceptionProperties);
    }


    /**
     * Set the exception information into the response.
     *
     * @param response  REST Response
     * @param error returned response.
     */
    private void captureAmbiguousConnectionNameException(AssetConsumerOMASAPIResponse     response,
                                                         AmbiguousConnectionNameException error)
    {
        String  connectionName = error.getConnectionName();

        if (connectionName != null)
        {
            Map<String, Object>  exceptionProperties = new HashMap<>();

            exceptionProperties.put("connectionName", connectionName);
            captureCheckedException(response, error, error.getClass().getName(), exceptionProperties);
        }
        else
        {
            captureCheckedException(response, error, error.getClass().getName());
        }
    }


    /**
     * Set the exception information into the response.
     *
     * @param response  REST Response
     * @param error returned response.
     */
    private void captureInvalidParameterException(AssetConsumerOMASAPIResponse response,
                                                  InvalidParameterException    error)
    {
        String  parameterName = error.getParameterName();

        if (parameterName != null)
        {
            Map<String, Object>  exceptionProperties = new HashMap<>();

            exceptionProperties.put("parameterName", parameterName);
            captureCheckedException(response, error, error.getClass().getName(), exceptionProperties);
        }
        else
        {
            captureCheckedException(response, error, error.getClass().getName());
        }
    }


    /**
     * Set the exception information into the response.
     *
     * @param response  REST Response
     * @param error returned response.
     */
    private void capturePropertyServerException(AssetConsumerOMASAPIResponse     response,
                                                PropertyServerException          error)
    {
        captureCheckedException(response, error, error.getClass().getName());
    }


    /**
     * Set the exception information into the response.
     *
     * @param response  REST Response
     * @param error returned response.
     */
    private void captureNoConnectedAssetException(AssetConsumerOMASAPIResponse     response,
                                                  NoConnectedAssetException        error)
    {
        String  connectionGUID = error.getConnectionGUID();

        if (connectionGUID != null)
        {
            Map<String, Object>  exceptionProperties = new HashMap<>();

            exceptionProperties.put("connectionGUID", connectionGUID);
            captureCheckedException(response, error, error.getClass().getName(), exceptionProperties);
        }
        else
        {
            captureCheckedException(response, error, error.getClass().getName());
        }
    }


    /**
     * Set the exception information into the response.
     *
     * @param response  REST Response
     * @param error returned response.
     */
    private void captureUserNotAuthorizedException(AssetConsumerOMASAPIResponse response,
                                                   UserNotAuthorizedException   error)
    {
        String  userId = error.getUserId();

        if (userId != null)
        {
            Map<String, Object>  exceptionProperties = new HashMap<>();

            exceptionProperties.put("userId", userId);
            captureCheckedException(response, error, error.getClass().getName(), exceptionProperties);
        }
        else
        {
            captureCheckedException(response, error, error.getClass().getName());
        }
    }
}
