/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.assetconsumer.server;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.odpi.openmetadata.accessservices.assetconsumer.ffdc.AssetConsumerErrorCode;
import org.odpi.openmetadata.accessservices.assetconsumer.ffdc.exceptions.*;
import org.odpi.openmetadata.accessservices.assetconsumer.responses.AssetConsumerOMASAPIResponse;
import org.odpi.openmetadata.accessservices.assetconsumer.responses.ConnectionResponse;
import org.odpi.openmetadata.accessservices.assetconsumer.responses.VoidResponse;
import org.odpi.openmetadata.adminservices.OMAGAccessServiceRegistration;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.CommentType;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceOperationalStatus;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceRegistration;
import org.odpi.openmetadata.accessservices.assetconsumer.admin.AssetConsumerAdmin;
import org.odpi.openmetadata.accessservices.assetconsumer.responses.GUIDResponse;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.StarRating;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;

import java.util.HashMap;
import java.util.Map;


/**
 * The AssetConsumerRESTServices provides the server-side implementation of the AssetConsumer Open Metadata
 * Assess Service (OMAS).  This interface provides connections to assets and APIs for adding feedback
 * on the asset.
 */
public class AssetConsumerRESTServices
{
    static private String                  accessServiceName   = null;
    static private OMRSRepositoryConnector repositoryConnector = null;

    private static final Logger log = LoggerFactory.getLogger(AssetConsumerRESTServices.class);

    /**
     * Provide a connector to the REST Services.
     *
     * @param accessServiceName  name of this access service
     * @param repositoryConnector  OMRS Repository Connector to the property server.
     */
    static public void setRepositoryConnector(String                   accessServiceName,
                                              OMRSRepositoryConnector  repositoryConnector)
    {
        AssetConsumerRESTServices.accessServiceName = accessServiceName;
        AssetConsumerRESTServices.repositoryConnector = repositoryConnector;
    }


    /**
     * Default constructor
     */
    public AssetConsumerRESTServices()
    {
        AccessServiceDescription   myDescription = AccessServiceDescription.ASSET_CONSUMER_OMAS;
        AccessServiceRegistration  myRegistration = new AccessServiceRegistration(myDescription.getAccessServiceCode(),
                                                                                  myDescription.getAccessServiceName(),
                                                                                  myDescription.getAccessServiceDescription(),
                                                                                  myDescription.getAccessServiceWiki(),
                                                                                  AccessServiceOperationalStatus.ENABLED,
                                                                                  AssetConsumerAdmin.class.getName());
        OMAGAccessServiceRegistration.registerAccessService(myRegistration);
    }



    /**
     * Returns the connection object corresponding to the supplied connection name.
     *
     * @param userId  String - userId of user making request.
     * @param name  this may be the qualifiedName or displayName of the connection.
     *
     * @return ConnectionResponse or
     * InvalidParameterException one of the parameters is null or invalid.
     * UnrecognizedConnectionNameException there is no connection defined for this name.
     * AmbiguousConnectionNameException there is more than one connection defined for this name.
     * PropertyServerException there is a problem retrieving information from the property (metadata) server.
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public ConnectionResponse getConnectionByName(String   userId,
                                                  String   name)
    {
        final String        methodName = "getConnectionByName";

        log.debug("Calling method: " + methodName);

        ConnectionResponse  response = new ConnectionResponse();

        try
        {
            this.validateInitialization(methodName);

            ConnectionHandler   connectionHandler = new ConnectionHandler(accessServiceName,
                                                                          repositoryConnector);

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
        catch (UnrecognizedConnectionNameException error)
        {
            captureUnrecognizedConnectionNameException(response, error);
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
     * @param userId  String - userId of user making request.
     * @param guid  the unique id for the connection within the property server.
     *
     * @return ConnectionResponse or
     * InvalidParameterException one of the parameters is null or invalid.
     * UnrecognizedConnectionGUIDException the supplied GUID is not recognized by the metadata repository.
     * PropertyServerException there is a problem retrieving information from the property (metadata) server.
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public ConnectionResponse getConnectionByGUID(String     userId,
                                                  String     guid)
    {
        final String        methodName = "getConnectionByGUID";

        log.debug("Calling method: " + methodName);

        ConnectionResponse  response = new ConnectionResponse();

        try
        {
            this.validateInitialization(methodName);

            ConnectionHandler   connectionHandler = new ConnectionHandler(accessServiceName,
                                                                          repositoryConnector);

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
        catch (UnrecognizedConnectionGUIDException error)
        {
            captureUnrecognizedConnectionGUIDException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            captureUserNotAuthorizedException(response, error);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        return response;
    }


    /**
     * Creates an Audit log record for the asset.  This log record is stored in the Asset's Audit Log.
     *
     * @param userId  String - userId of user making request.
     * @param guid  String - unique id for the asset.
     * @param connectorInstanceId  String - (optional) id of connector in use (if any).
     * @param connectionName  String - (optional) name of the connection (extracted from the connector).
     * @param connectorType  String - (optional) type of connector in use (if any).
     * @param contextId  String - (optional) function name, or processId of the activity that the caller is performing.
     * @param message  log record content.
     *
     * @return VoidResponse or
     * InvalidParameterException one of the parameters is null or invalid.
     * PropertyServerException There is a problem adding the asset properties to
     *                                   the metadata repository.
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse addLogMessageToAsset(String      userId,
                                             String      guid,
                                             String      connectorInstanceId,
                                             String      connectionName,
                                             String      connectorType,
                                             String      contextId,
                                             String      message)
    {
        final String        methodName = "addLogMessageToAsset";

        log.debug("Calling method: " + methodName);

        VoidResponse  response = new VoidResponse();

        try
        {
            this.validateInitialization(methodName);

            AuditLogHandler   auditLogHandler = new AuditLogHandler(accessServiceName,
                                                                    repositoryConnector);

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


    /**
     * Adds a new public tag to the asset's properties.
     *
     * @param userId  String - userId of user making request.
     * @param guid  String - unique id for the asset.
     * @param tagName  String - name of the tag.
     * @param tagDescription  String - (optional) description of the tag.  Setting a description, particularly in
     *                       a public tag makes the tag more valuable to other users and can act as an embryonic
     *                       glossary term.
     *
     * @return GUIDResponse or
     * InvalidParameterException one of the parameters is null or invalid.
     * PropertyServerException There is a problem adding the asset properties to
     *                                   the metadata repository.
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public GUIDResponse addTagToAsset(String      userId,
                                      String      guid,
                                      String      tagName,
                                      String      tagDescription)
    {
        final String        methodName = "addTagToAsset";

        log.debug("Calling method: " + methodName);

        GUIDResponse  response = new GUIDResponse();

        try
        {
            this.validateInitialization(methodName);

            FeedbackHandler   feedbackHandler = new FeedbackHandler(accessServiceName,
                                                                    repositoryConnector);

            feedbackHandler.addTagToAsset(userId, guid, tagName, tagDescription);
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
     * Adds a new private tag to the asset's properties.
     *
     * @param userId  String - userId of user making request.
     * @param guid  String - unique id for the asset.
     * @param tagName  String - name of the tag.
     * @param tagDescription  String - (optional) description of the tag.  Setting a description, particularly in
     *                       a public tag makes the tag more valuable to other users and can act as an embryonic
     *                       glossary term.
     *
     * @return GUIDResponse or
     * InvalidParameterException one of the parameters is null or invalid.
     * PropertyServerException There is a problem adding the asset properties to
     *                                   the metadata repository.
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public GUIDResponse addPrivateTagToAsset(String      userId,
                                             String      guid,
                                             String      tagName,
                                             String      tagDescription)
    {
        final String        methodName = "addPrivateTagToAsset";

        log.debug("Calling method: " + methodName);

        GUIDResponse  response = new GUIDResponse();

        try
        {
            this.validateInitialization(methodName);

            FeedbackHandler   feedbackHandler = new FeedbackHandler(accessServiceName,
                                                                    repositoryConnector);

            feedbackHandler.addPrivateTagToAsset(userId, guid, tagName, tagDescription);
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
     * Adds a rating to the asset.
     *
     * @param userId  String - userId of user making request.
     * @param guid  String - unique id for the asset.
     * @param starRating StarRating  - enumeration for none, one to five stars.
     * @param review  String - user review of asset.
     *
     * @return GUIDResponse or
     * InvalidParameterException one of the parameters is null or invalid.
     * PropertyServerException There is a problem adding the asset properties to
     *                                   the metadata repository.
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public GUIDResponse addRatingToAsset(String     userId,
                                         String     guid,
                                         StarRating starRating,
                                         String     review)
    {
        final String        methodName = "addRatingToAsset";

        log.debug("Calling method: " + methodName);

        GUIDResponse  response = new GUIDResponse();

        try
        {
            this.validateInitialization(methodName);

            FeedbackHandler   feedbackHandler = new FeedbackHandler(accessServiceName,
                                                                    repositoryConnector);

            feedbackHandler.addRatingToAsset(userId, guid, starRating, review);
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
     * @param userId  String - userId of user making request.
     * @param guid  String - unique id for the asset
     *
     * @return GUIDResponse or
     * InvalidParameterException one of the parameters is null or invalid.
     * PropertyServerException There is a problem adding the asset properties to
     *                                   the metadata repository.
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public GUIDResponse addLikeToAsset(String       userId,
                                       String       guid)
    {
        final String        methodName = "addLikeToAsset";

        log.debug("Calling method: " + methodName);

        GUIDResponse  response = new GUIDResponse();

        try
        {
            this.validateInitialization(methodName);

            FeedbackHandler   feedbackHandler = new FeedbackHandler(accessServiceName,
                                                                    repositoryConnector);

            feedbackHandler.addLikeToAsset(userId, guid);
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
     * @param userId  String - userId of user making request.
     * @param guid  String - unique id for the asset.
     * @param commentType  type of comment enum.
     * @param commentText  String - the text of the comment.
     *
     * @return GUIDResponse or
     * InvalidParameterException one of the parameters is null or invalid.
     * PropertyServerException There is a problem adding the asset properties to
     *                                   the metadata repository.
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public GUIDResponse addCommentToAsset(String      userId,
                                          String      guid,
                                          CommentType commentType,
                                          String      commentText)
    {
        final String        methodName = "addCommentToAsset";

        log.debug("Calling method: " + methodName);

        GUIDResponse  response = new GUIDResponse();

        try
        {
            this.validateInitialization(methodName);

            FeedbackHandler   feedbackHandler = new FeedbackHandler(accessServiceName,
                                                                    repositoryConnector);

            feedbackHandler.addCommentToAsset(userId, guid, commentType, commentText);
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
     * @param userId  String - userId of user making request.
     * @param commentGUID  String - unique id for an existing comment.  Used to add a reply to a comment.
     * @param commentType  type of comment enum.
     * @param commentText  String - the text of the comment.
     *
     * @return GUIDResponse or
     * InvalidParameterException one of the parameters is null or invalid.
     * PropertyServerException There is a problem adding the asset properties to
     *                                   the metadata repository.
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public GUIDResponse addCommentReply(String      userId,
                                        String      commentGUID,
                                        CommentType commentType,
                                        String      commentText)
    {
        final String        methodName = "addCommentReply";

        log.debug("Calling method: " + methodName);

        GUIDResponse  response = new GUIDResponse();

        try
        {
            this.validateInitialization(methodName);

            FeedbackHandler   feedbackHandler = new FeedbackHandler(accessServiceName,
                                                                    repositoryConnector);

            feedbackHandler.addCommentReply(userId, commentGUID, commentType, commentText);
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
     * @param userId  String - userId of user making request.
     * @param guid  String - unique id for the tag.
     *
     * @return VoidResponse or
     * InvalidParameterException one of the parameters is null or invalid.
     * PropertyServerException There is a problem updating the asset properties in
     *                                   the metadata repository.
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse   removeTag(String     userId,
                                    String     guid)
    {
        final String        methodName = "removeTag";

        log.debug("Calling method: " + methodName);

        VoidResponse  response = new VoidResponse();

        try
        {
            this.validateInitialization(methodName);

            FeedbackHandler feedbackHandler = new FeedbackHandler(accessServiceName,
                                                                  repositoryConnector);

            feedbackHandler.removeTagFromAsset(userId, guid);
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
     * @param userId  String - userId of user making request.
     * @param guid  String - unique id for the tag.
     *
     * @return VoidResponse or
     * InvalidParameterException one of the parameters is null or invalid.
     * PropertyServerException There is a problem updating the asset properties in
     *                                   the metadata repository.
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse   removePrivateTag(String     userId,
                                           String     guid)
    {
        final String        methodName = "removePrivateTag";

        log.debug("Calling method: " + methodName);

        VoidResponse  response = new VoidResponse();

        try
        {
            this.validateInitialization(methodName);

            FeedbackHandler feedbackHandler = new FeedbackHandler(accessServiceName,
                                                                  repositoryConnector);

            feedbackHandler.removePrivateTagFromAsset(userId, guid);
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
     * Removes of a star rating that was added to the asset by this user.
     *
     * @param userId  String - userId of user making request.
     * @param guid  String - unique id for the rating object
     *
     * @return VoidResponse or
     * InvalidParameterException one of the parameters is null or invalid.
     * PropertyServerException There is a problem updating the asset properties in
     *                                   the metadata repository.
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse   removeRating(String     userId,
                                       String     guid)
    {
        final String        methodName = "removeRating";

        log.debug("Calling method: " + methodName);

        VoidResponse  response = new VoidResponse();

        try
        {
            this.validateInitialization(methodName);

            FeedbackHandler feedbackHandler = new FeedbackHandler(accessServiceName,
                                                                  repositoryConnector);

            feedbackHandler.removeRatingFromAsset(userId, guid);
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
     * @param userId  String - userId of user making request.
     * @param guid  String - unique id for the like object
     * @return VoidResponse or
     * InvalidParameterException one of the parameters is null or invalid.
     * PropertyServerException There is a problem updating the asset properties in
     *                                   the metadata repository.
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse   removeLike(String     userId,
                                     String     guid)
    {
        final String        methodName = "removeLike";

        log.debug("Calling method: " + methodName);

        VoidResponse  response = new VoidResponse();

        try
        {
            this.validateInitialization(methodName);

            FeedbackHandler feedbackHandler = new FeedbackHandler(accessServiceName,
                                                                  repositoryConnector);

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
     * Removes a comment added to the asset by this user.
     *
     * @param userId  String - userId of user making request.
     * @param guid  String - unique id for the comment object
     * @return VoidResponse or
     * InvalidParameterException one of the parameters is null or invalid.
     * PropertyServerException There is a problem updating the asset properties in
     *                                   the metadata repository.
     * UserNotAuthorizedException the user does not have permission to perform this request.
     */
    public VoidResponse   removeComment(String     userId,
                                        String     guid)
    {
        final String        methodName = "removeComment";

        log.debug("Calling method: " + methodName);

        VoidResponse  response = new VoidResponse();

        try
        {
            this.validateInitialization(methodName);

            FeedbackHandler feedbackHandler = new FeedbackHandler(accessServiceName,
                                                                  repositoryConnector);

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
    private void captureUnrecognizedConnectionGUIDException(AssetConsumerOMASAPIResponse        response,
                                                            UnrecognizedConnectionGUIDException error)
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
    private void captureUnrecognizedConnectionNameException(AssetConsumerOMASAPIResponse        response,
                                                            UnrecognizedConnectionNameException error)
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


    /**
     * Validate that this access service has been initialized before attempting to process a request.
     *
     * @param methodName  name of method called.
     * @throws PropertyServerException not initialized
     */
    private void validateInitialization(String  methodName) throws PropertyServerException
    {
        if (repositoryConnector == null)
        {
            AssetConsumerErrorCode errorCode = AssetConsumerErrorCode.SERVICE_NOT_INITIALIZED;
            String        errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName);

            throw new PropertyServerException(errorCode.getHTTPErrorCode(),
                                                          this.getClass().getName(),
                                                          methodName,
                                                          errorMessage,
                                                          errorCode.getSystemAction(),
                                                          errorCode.getUserAction());
        }
    }
}
