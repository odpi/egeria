/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetconsumer.server;


import org.odpi.openmetadata.accessservices.assetconsumer.rest.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.odpi.openmetadata.accessservices.assetconsumer.ffdc.AssetConsumerErrorCode;
import org.odpi.openmetadata.accessservices.assetconsumer.ffdc.exceptions.*;
import org.odpi.openmetadata.adminservices.OMAGAccessServiceRegistration;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.CommentType;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceOperationalStatus;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceRegistration;
import org.odpi.openmetadata.accessservices.assetconsumer.admin.AssetConsumerAdmin;
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
     * @param userId userId of user making request.
     * @param name   this may be the qualifiedName or displayName of the connection.
     *
     * @return ConnectionResponse or
     * InvalidParameterException - one of the parameters is null or invalid or
     * UnrecognizedConnectionNameException - there is no connection defined for this name or
     * AmbiguousConnectionNameException - there is more than one connection defined for this name or
     * PropertyServerException - there is a problem retrieving information from the property (metadata) server or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
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
     * @param userId userId of user making request.
     * @param guid  the unique id for the connection within the property server.
     *
     * @return ConnectionResponse or
     * InvalidParameterException - one of the parameters is null or invalid or
     * UnrecognizedConnectionGUIDException - the supplied GUID is not recognized by the metadata repository or
     * PropertyServerException - there is a problem retrieving information from the property (metadata) server or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
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
     * Return the profile for this user.
     *
     * @param userId userId of the user making the request.
     *
     * @return profile response object or
     * InvalidParameterException the userId is null or invalid or
     * NoProfileForUserException the user does not have a profile or
     * PropertyServerException there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public MyProfileResponse getMyProfile(String userId)
    {
        final String   methodName = "getMyProfile";

        log.debug("Calling method: " + methodName);

        MyProfileResponse  response = new MyProfileResponse();

        try
        {
            this.validateInitialization(methodName);

            MyProfileHandler   handler = new MyProfileHandler(accessServiceName, repositoryConnector);

            response.setPersonalProfile(handler.getMyProfile(userId));
        }
        catch (InvalidParameterException  error)
        {
            captureInvalidParameterException(response, error);
        }
        catch (NoProfileForUserException  error)
        {
            captureNoProfileForUserException(response, error);
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
     * Create or update the profile for the requesting user.
     *
     * @param userId the name of the calling user.
     * @param requestBody properties for the new profile.
     * @return void response or
     * InvalidParameterException - one of the parameters is invalid or
     * PropertyServerException - there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public VoidResponse updateMyProfile(String               userId,
                                        MyProfileRequestBody requestBody)
    {
        final String   methodName = "updateMyProfile";

        log.debug("Calling method: " + methodName);

        VoidResponse  response = new VoidResponse();

        try
        {
            this.validateInitialization(methodName);

            String              employeeNumber       = null;
            String              fullName             = null;
            String              knownName            = null;
            String              jobTitle             = null;
            String              jobRoleDescription   = null;
            Map<String, Object> additionalProperties = null;

            if (requestBody != null)
            {
                employeeNumber = requestBody.getEmployeeNumber();
                fullName = requestBody.getFullName();
                knownName = requestBody.getKnownName();
                jobTitle = requestBody.getJobTitle();
                jobRoleDescription = requestBody.getJobRoleDescription();
                additionalProperties = requestBody.getAdditionalProperties();
            }

            MyProfileHandler   handler = new MyProfileHandler(accessServiceName, repositoryConnector);

            handler.updateMyProfile(userId, employeeNumber, fullName, knownName, jobTitle, jobRoleDescription, additionalProperties);
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
     * Return a list of assets that the specified user has added to their favorites list.
     *
     * @param userId     userId of user making request.
     * @param startFrom  index of the list ot start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return list of asset details or
     * InvalidParameterException one of the parameters is invalid or
     * PropertyServerException there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public AssetListResponse getMyAssets(String    userId,
                                         int       startFrom,
                                         int       pageSize)
    {
        // todo
        return null;
    }


    /**
     * Add an asset to the identified user's list of favorite assets.
     *
     * @param userId          userId of user making request.
     * @param assetGUID       unique identifier of the asset.
     * @param nullRequestBody null request body
     *
     * @return void response or
     * InvalidParameterException one of the parameters is invalid or
     * PropertyServerException there is a problem updating information in the property server(s) or
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse  addToMyAssets(String           userId,
                                       String           assetGUID,
                                       NullRequestBody  nullRequestBody)
    {
        // todo
        return null;
    }


    /**
     * Remove an asset from identified user's list of favorite assets.
     *
     * @param userId          userId of user making request.
     * @param assetGUID       unique identifier of the asset.
     * @param nullRequestBody null request body
     *
     * @return void response or
     * InvalidParameterException one of the parameters is invalid or
     * PropertyServerException there is a problem updating information in the property server(s) or
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse  removeFromMyAssets(String           userId,
                                            String           assetGUID,
                                            NullRequestBody  nullRequestBody)
    {
        // todo
        return null;

    }


    /**
     * Returns the unique identifier for the asset connected to the connection.
     *
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
    public GUIDResponse getAssetForConnection(String   userId,
                                              String   connectionGUID)
    {
        final String        methodName = "getAssetForConnection";

        log.debug("Calling method: " + methodName);

        GUIDResponse  response = new GUIDResponse();

        try
        {
            this.validateInitialization(methodName);

            ConnectionHandler   connectionHandler = new ConnectionHandler(accessServiceName,
                                                                          repositoryConnector);

            response.setGUID(connectionHandler.getAssetForConnection(userId, connectionGUID));
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


    /**
     * Creates an Audit log record for the asset.  This log record is stored in the Asset's Audit Log.
     *
     * @param userId  String - userId of user making request.
     * @param guid  String - unique id for the asset.
     * @param requestBody containing:
     * connectorInstanceId  (String - (optional) id of connector in use (if any)),
     * connectionName  (String - (optional) name of the connection (extracted from the connector)),
     * connectorType  (String - (optional) type of connector in use (if any)),
     * contextId  (String - (optional) function name, or processId of the activity that the caller is performing),
     * message  (log record content).
     *
     * @return VoidResponse or
     * InvalidParameterException - one of the parameters is null or invalid or
     * PropertyServerException - there is a problem adding the log message to the audit log for this asset or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public VoidResponse addLogMessageToAsset(String                userId,
                                             String                guid,
                                             LogRecordRequestBody  requestBody)
    {
        final String        methodName = "addLogMessageToAsset";

        log.debug("Calling method: " + methodName);

        VoidResponse  response = new VoidResponse();


        try
        {
            this.validateInitialization(methodName);

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
     * @param requestBody  contains the name of the tag and (optional) description of the tag.
     *
     * @return GUIDResponse or
     * InvalidParameterException - one of the parameters is null or invalid or
     * PropertyServerException - there is a problem adding the asset properties to
     *                                   the metadata repository or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public GUIDResponse addTagToAsset(String          userId,
                                      String          guid,
                                      TagRequestBody  requestBody)
    {
        final String        methodName = "addTagToAsset";

        log.debug("Calling method: " + methodName);

        GUIDResponse  response = new GUIDResponse();

        try
        {
            this.validateInitialization(methodName);

            String      tagName = null;
            String      tagDescription = null;

            if (requestBody != null)
            {
                tagName = requestBody.getTagName();
                tagDescription = requestBody.getTagDescription();
            }

            FeedbackHandler   feedbackHandler = new FeedbackHandler(accessServiceName,
                                                                    repositoryConnector);

            response.setGUID(feedbackHandler.addTagToAsset(userId, guid, tagName, tagDescription));
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
     * @param userId       String - userId of user making request.
     * @param guid         String - unique id for the asset.
     * @param requestBody  contains the name of the tag and (optional) description of the tag.
     *
     * @return GUIDResponse or
     * InvalidParameterException - one of the parameters is null or invalid or
     * PropertyServerException - there is a problem adding the asset properties to
     *                                   the metadata repository or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public GUIDResponse addPrivateTagToAsset(String          userId,
                                             String          guid,
                                             TagRequestBody  requestBody)
    {
        final String        methodName = "addPrivateTagToAsset";

        log.debug("Calling method: " + methodName);

        GUIDResponse  response = new GUIDResponse();

        try
        {
            this.validateInitialization(methodName);

            String      tagName = null;
            String      tagDescription = null;

            if (requestBody != null)
            {
                tagName = requestBody.getTagName();
                tagDescription = requestBody.getTagDescription();
            }

            FeedbackHandler   feedbackHandler = new FeedbackHandler(accessServiceName,
                                                                    repositoryConnector);

            response.setGUID(feedbackHandler.addPrivateTagToAsset(userId, guid, tagName, tagDescription));
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
     * @param userId      String - userId of user making request.
     * @param guid        String - unique id for the asset.
     * @param requestBody containing the StarRating and user review of asset.
     *
     * @return GUIDResponse or
     * InvalidParameterException - one of the parameters is null or invalid or
     * PropertyServerException - there is a problem adding the asset properties to
     *                                   the metadata repository or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public GUIDResponse addRatingToAsset(String            userId,
                                         String            guid,
                                         RatingRequestBody requestBody)
    {
        final String        methodName = "addRatingToAsset";

        log.debug("Calling method: " + methodName);

        GUIDResponse  response = new GUIDResponse();

        try
        {
            this.validateInitialization(methodName);

            StarRating starRating = null;
            String     review = null;

            if (requestBody != null)
            {
                starRating = requestBody.getStarRating();
                review = requestBody.getReview();
            }

            FeedbackHandler   feedbackHandler = new FeedbackHandler(accessServiceName,
                                                                    repositoryConnector);

            response.setGUID(feedbackHandler.addRatingToAsset(userId, guid, starRating, review));
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
     * @param userId      String - userId of user making request.
     * @param guid        String - unique id for the asset.
     * @param requestBody null request body to satisfy HTTP protocol.
     *
     * @return GUIDResponse or
     * InvalidParameterException - one of the parameters is null or invalid or
     * PropertyServerException - there is a problem adding the asset properties to
     *                                   the metadata repository or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public GUIDResponse addLikeToAsset(String          userId,
                                       String          guid,
                                       NullRequestBody requestBody)
    {
        final String        methodName = "addLikeToAsset";

        log.debug("Calling method: " + methodName);

        GUIDResponse  response = new GUIDResponse();

        try
        {
            this.validateInitialization(methodName);

            FeedbackHandler   feedbackHandler = new FeedbackHandler(accessServiceName,
                                                                    repositoryConnector);

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
     * Adds a comment to the asset.
     *
     * @param userId  String - userId of user making request.
     * @param guid  String - unique id for the asset.
     * @param requestBody containing type of comment enum and the text of the comment.
     *
     * @return GUIDResponse or
     * InvalidParameterException - one of the parameters is null or invalid or
     * PropertyServerException - there is a problem adding the asset properties to
     *                                   the metadata repository or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public GUIDResponse addCommentToAsset(String             userId,
                                          String             guid,
                                          CommentRequestBody requestBody)
    {
        final String        methodName = "addCommentToAsset";

        log.debug("Calling method: " + methodName);

        GUIDResponse  response = new GUIDResponse();

        try
        {
            this.validateInitialization(methodName);

            CommentType commentType = null;
            String      commentText = null;

            if (requestBody != null)
            {
                commentType = requestBody.getCommentType();
                commentText = requestBody.getCommentText();
            }

            FeedbackHandler   feedbackHandler = new FeedbackHandler(accessServiceName,
                                                                    repositoryConnector);

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
     * @param userId       String - userId of user making request.
     * @param commentGUID  String - unique id for an existing comment.  Used to add a reply to a comment.
     * @param requestBody  containing type of comment enum and the text of the comment.
     *
     * @return GUIDResponse or
     * InvalidParameterException - one of the parameters is null or invalid or
     * PropertyServerException - there is a problem adding the asset properties to
     *                                   the metadata repository or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public GUIDResponse addCommentReply(String             userId,
                                        String             commentGUID,
                                        CommentRequestBody requestBody)
    {
        final String        methodName = "addCommentReply";

        log.debug("Calling method: " + methodName);

        GUIDResponse  response = new GUIDResponse();

        try
        {
            this.validateInitialization(methodName);

            CommentType commentType = null;
            String      commentText = null;

            if (requestBody != null)
            {
                commentType = requestBody.getCommentType();
                commentText = requestBody.getCommentText();
            }

            FeedbackHandler   feedbackHandler = new FeedbackHandler(accessServiceName,
                                                                    repositoryConnector);

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
     * Removes a tag from the asset that was added by this user.
     *
     * @param userId       String - userId of user making request.
     * @param guid         String - unique id for the tag.
     * @param requestBody  containing type of comment enum and the text of the comment.
     *
     * @return VoidResponse or
     * InvalidParameterException - one of the parameters is null or invalid or
     * PropertyServerException - there is a problem updating the asset properties in
     *                                   the metadata repository or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public VoidResponse   removeTag(String          userId,
                                    String          guid,
                                    NullRequestBody requestBody)
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
     * @param userId      String - userId of user making request.
     * @param guid        String - unique id for the tag.
     * @param requestBody null request body needed to satisfy the HTTP Post request
     *
     * @return VoidResponse or
     * InvalidParameterException - one of the parameters is null or invalid or
     * PropertyServerException - there is a problem updating the asset properties in
     *                                   the metadata repository or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public VoidResponse   removePrivateTag(String          userId,
                                           String          guid,
                                           NullRequestBody requestBody)
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
     * Removes a star rating that was added to the asset by this user.
     *
     * @param userId      String - userId of user making request.
     * @param guid        String - unique id for the rating object
     * @param requestBody null request body needed to satisfy the HTTP Post request
     *
     * @return VoidResponse or
     * InvalidParameterException - one of the parameters is null or invalid or
     * PropertyServerException - there is a problem updating the asset properties in
     *                                   the metadata repository or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public VoidResponse   removeRating(String          userId,
                                       String          guid,
                                       NullRequestBody requestBody)
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
     * @param requestBody null request body needed to satisfy the HTTP Post request
     *
     * @return VoidResponse or
     * InvalidParameterException - one of the parameters is null or invalid or
     * PropertyServerException - there is a problem updating the asset properties in
     *                                   the metadata repository or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public VoidResponse   removeLike(String          userId,
                                     String          guid,
                                     NullRequestBody requestBody)
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
     * @param requestBody null request body needed to satisfy the HTTP Post request
     *
     * @return VoidResponse or
     * InvalidParameterException - one of the parameters is null or invalid or
     * PropertyServerException - there is a problem updating the asset properties in
     *                                   the metadata repository or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public VoidResponse   removeComment(String          userId,
                                        String          guid,
                                        NullRequestBody requestBody)
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
     * Set the exception information into the response.
     *
     * @param response  REST Response
     * @param error returned response.
     */
    private void captureNoProfileForUserException(AssetConsumerOMASAPIResponse response,
                                                  NoProfileForUserException    error)
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
            String                 errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName);

            throw new PropertyServerException(errorCode.getHTTPErrorCode(),
                                                          this.getClass().getName(),
                                                          methodName,
                                                          errorMessage,
                                                          errorCode.getSystemAction(),
                                                          errorCode.getUserAction());
        }
    }
}
