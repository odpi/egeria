/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetconsumer.client;

import org.odpi.openmetadata.accessservices.assetconsumer.api.*;
import org.odpi.openmetadata.accessservices.assetconsumer.client.rest.AssetConsumerRESTClient;
import org.odpi.openmetadata.accessservices.assetconsumer.elements.InformalTagElement;
import org.odpi.openmetadata.accessservices.assetconsumer.elements.MeaningElement;
import org.odpi.openmetadata.accessservices.assetconsumer.properties.*;
import org.odpi.openmetadata.accessservices.assetconsumer.rest.*;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDListResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.NameRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.SearchStringRequestBody;
import org.odpi.openmetadata.frameworkservices.ocf.metadatamanagement.client.ConnectedAssetClientBase;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.*;
import org.odpi.openmetadata.frameworks.connectors.properties.AssetUniverse;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Asset;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.CommentType;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.StarRating;

import java.util.ArrayList;
import java.util.List;

/**
 * The Asset Consumer Open Metadata Access Service (OMAS) is used by applications and tools as a factory for Open
 * Connector Framework (OCF) connectors.  The configuration for the connectors is managed as open metadata in
 * a Connection definition.  The caller to the Asset Consumer OMAS passes either the name, GUID or URL for the
 * connection to the appropriate method to retrieve a connector.  The Asset Consumer OMAS retrieves the connection
 * from the metadata repository and creates an appropriate connector as described the connection and
 * returns it to the caller.
 *
 * The Asset Consumer OMAS supports access to the asset properties either through the connector, or by a direct
 * call to Asset Consumer API.  It is also possible to look up the definitions of terms associated with the assets.
 *
 * It supports the ability to add and remove feedback for an asset.
 * This feedback may be in the form of reviews, likes and comments.
 * Asset Consumer OMAS also supports the maintenance of informal tags and their attachments to assets.
 *
 * Finally, Asset Consumer OMAS supports the ability to add audit log records to the local server's audit log
 * about an asset.
 */
public class AssetConsumer extends ConnectedAssetClientBase implements AssetConsumerAssetInterface,
                                                                       AssetConsumerFeedbackInterface,
                                                                       AssetConsumerGlossaryInterface,
                                                                       AssetConsumerLoggingInterface,
                                                                       AssetConsumerTaggingInterface
{
    private final AssetConsumerRESTClient restClient;               /* Initialized in constructor */

    private static final String  serviceURLName = "asset-consumer";

    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param auditLog logging destination
     *
     * @throws InvalidParameterException null URL or server name
     */
    public AssetConsumer(String   serverName,
                         String   serverPlatformURLRoot,
                         AuditLog auditLog) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, serviceURLName, auditLog);

        this.restClient = new AssetConsumerRESTClient(serverName, serverPlatformURLRoot, auditLog);
    }


    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @throws InvalidParameterException null URL or server name
     */
    public AssetConsumer(String serverName,
                         String serverPlatformURLRoot) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, serviceURLName);

        this.restClient = new AssetConsumerRESTClient(serverName, serverPlatformURLRoot);
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
     * @throws InvalidParameterException null URL or server name
     */
    public AssetConsumer(String     serverName,
                         String     serverPlatformURLRoot,
                         String     userId,
                         String     password,
                         AuditLog   auditLog) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, serviceURLName, auditLog);

        this.restClient = new AssetConsumerRESTClient(serverName, serverPlatformURLRoot, userId, password, auditLog);
    }


    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param userId caller's userId embedded in all HTTP requests
     * @param password caller's userId embedded in all HTTP requests
     * @throws InvalidParameterException null URL or server name
     */
    public AssetConsumer(String     serverName,
                         String     serverPlatformURLRoot,
                         String     userId,
                         String     password) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, serviceURLName, userId, password);

        this.restClient = new AssetConsumerRESTClient(serverName, serverPlatformURLRoot, userId, password);
    }


    /**
     * Create a new client that is going to be used in an OMAG Server (view service or integration service typically).
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param restClient client that issues the REST API calls
     * @param maxPageSize maximum number of results supported by this server
     * @param auditLog logging destination
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public AssetConsumer(String                  serverName,
                         String                  serverPlatformURLRoot,
                         AssetConsumerRESTClient restClient,
                         int                     maxPageSize,
                         AuditLog                auditLog) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, serviceURLName, maxPageSize, auditLog);

        this.restClient = restClient;
    }

    /*
     * ===============================================
     * AssetConsumerAssetInterface
     * ===============================================
     */


    /**
     * Returns the unique identifier for the asset connected to the requested connection.
     *
     * @param userId the userId of the requesting user.
     * @param connectionGUID  unique identifier for the connection.
     *
     * @return unique identifier of asset.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @Override
    public String  getAssetForConnection(String   userId,
                                         String   connectionGUID) throws InvalidParameterException,
                                                                         PropertyServerException,
                                                                         UserNotAuthorizedException
    {
        final String   methodName = "getAssetForConnection";
        final String   guidParameter = "connectionGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(connectionGUID, guidParameter, methodName);

        return super.getAssetForConnection(restClient, serviceURLName, userId, connectionGUID);
    }


    /**
     * Returns the asset corresponding to the supplied connection name.
     *
     * @param userId           userId of user making request.
     * @param connectionName   this may be the qualifiedName or displayName of the connection.
     *
     * @return unique identifier of asset.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @Override
    public String  getAssetForConnectionName(String userId,
                                             String connectionName) throws InvalidParameterException,
                                                                           PropertyServerException,
                                                                           UserNotAuthorizedException
    {
        final String   methodName = "getAssetForConnectionName";
        final String   nameParameter = "connectionName";
        final String   urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/asset-consumer/users/{1}/assets/by-connection-name/{2}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(connectionName, nameParameter, methodName);

        GUIDResponse restResult = restClient.callGUIDGetRESTCall(methodName,
                                                                 urlTemplate,
                                                                 serverName,
                                                                 userId,
                                                                 connectionName);

        return restResult.getGUID();
    }


    /**
     * Return a list of assets with the requested name.  The name must match exactly.
     *
     * @param userId calling user
     * @param name name to search for
     * @param startFrom starting element (used in paging through large result sets)
     * @param pageSize maximum number of results to return
     * @param methodName calling method
     *
     * @return list of unique identifiers of assets with matching name.
     *
     * @throws InvalidParameterException the name is invalid
     * @throws PropertyServerException there is a problem access in the property server
     * @throws UserNotAuthorizedException the user does not have access to the properties
     */
    private List<String> getAssetsByName(String   userId,
                                         String   name,
                                         int      startFrom,
                                         int      pageSize,
                                         String   methodName) throws InvalidParameterException,
                                                                     PropertyServerException,
                                                                     UserNotAuthorizedException
    {
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/asset-consumer/users/{1}/assets/by-name?startFrom={2}&pageSize={3}";

        NameRequestBody requestBody = new NameRequestBody();

        requestBody.setName(name);

        GUIDListResponse restResult = restClient.callGUIDListPostRESTCall(methodName,
                                                                          urlTemplate,
                                                                          requestBody,
                                                                          serverName,
                                                                          userId,
                                                                          startFrom,
                                                                          pageSize);

        return restResult.getGUIDs();
    }


    /**
     * Return a list of assets with the requested search string in their name, qualified name
     * or description.  The search string is interpreted as a regular expression (RegEx).
     *
     * @param userId calling user
     * @param searchString string to search for in text
     * @param startFrom starting element (used in paging through large result sets)
     * @param pageSize maximum number of results to return
     *
     * @return list of assets that match the search string.
     *
     * @throws InvalidParameterException the searchString is invalid
     * @throws PropertyServerException there is a problem access in the property server
     * @throws UserNotAuthorizedException the user does not have access to the properties
     */
    @Override
    public List<String>  findAssets(String   userId,
                                    String   searchString,
                                    int      startFrom,
                                    int      pageSize) throws InvalidParameterException,
                                                              PropertyServerException,
                                                              UserNotAuthorizedException
    {
        final String methodName = "findAssets";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/asset-consumer/users/{1}/assets/by-search-string?startFrom={2}&pageSize={3}";
        final String nameParameter = "searchString";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateSearchString(searchString, nameParameter, methodName);

        SearchStringRequestBody requestBody = new SearchStringRequestBody();

        requestBody.setSearchString(searchString);
        requestBody.setSearchStringParameterName(nameParameter);

        GUIDListResponse restResult = restClient.callGUIDListPostRESTCall(methodName,
                                                                          urlTemplate,
                                                                          requestBody,
                                                                          serverName,
                                                                          userId,
                                                                          startFrom,
                                                                          pageSize);

        return restResult.getGUIDs();
    }




    /**
     * Return a list of assets with the requested name.
     *
     * @param userId calling user
     * @param name name to search for
     * @param startFrom starting element (used in paging through large result sets)
     * @param pageSize maximum number of results to return
     *
     * @return list of unique identifiers of assets with matching name.
     *
     * @throws InvalidParameterException the name is invalid
     * @throws PropertyServerException there is a problem access in the property server
     * @throws UserNotAuthorizedException the user does not have access to the properties
     */
    @Override
    public List<String> getAssetsByName(String   userId,
                                        String   name,
                                        int      startFrom,
                                        int      pageSize) throws InvalidParameterException,
                                                                  PropertyServerException,
                                                                  UserNotAuthorizedException
    {
        final String   methodName = "getAssetsByName";
        final String   nameParameter = "name";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(name, nameParameter, methodName);

        return this.getAssetsByName(userId, name, startFrom, pageSize, methodName);
    }


    /**
     * Returns a list of assets that match the token. The following calls are issued
     * in order for find the asset.
     * - getAssetProperties passing the token as the GUID
     * - getAssetByName passing the token as the name
     *
     * @param userId         userId of user making request.
     * @param assetToken     token used to find the Asset - may be a name or GUID
     * @param startFrom starting element (used in paging through large result sets)
     * @param pageSize maximum number of results to return
     *
     * @return a list of unique identifiers for the matching assets
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem retrieving the asset properties from the property servers).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @Override
    public List<String> getAssetsByToken(String userId,
                                         String assetToken,
                                         int    startFrom,
                                         int    pageSize) throws InvalidParameterException,
                                                                 PropertyServerException,
                                                                 UserNotAuthorizedException
    {
        final String   methodName    = "getAssetsByToken";
        final String   tokenParameter = "assetToken";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(assetToken, tokenParameter, methodName);

        Asset asset;

        try
        {
            asset = this.getAssetSummary(userId, assetToken, methodName);
        }
        catch (Exception error)
        {
            asset = null;
        }

        if (asset != null)
        {
            List<String>  retrievedAssets = new ArrayList<>();
            retrievedAssets.add(asset.getGUID());
            return retrievedAssets;
        }
        else
        {
            return this.getAssetsByName(userId, assetToken, startFrom, pageSize, methodName);
        }
    }


    /**
     * Returns a comprehensive collection of properties about the requested asset.
     *
     * @param userId         userId of user making request.
     * @param assetGUID      unique identifier for asset.
     *
     * @return a comprehensive collection of properties about the asset.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem retrieving the asset properties from the property servers).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @Override
    public AssetUniverse getAssetProperties(String userId,
                                            String assetGUID) throws InvalidParameterException,
                                                                     PropertyServerException,
                                                                     UserNotAuthorizedException
    {
        return super.getAssetProperties(serviceURLName, userId, assetGUID);
    }


    /*
     * ===============================================
     * AssetConsumerFeedbackInterface
     * ===============================================
     */

    /**
     * Adds a star rating and optional review text to the asset.  If the user has already attached
     * a rating then the original one is over-ridden.
     *
     * @param userId      userId of user making request.
     * @param assetGUID   unique identifier for the asset.
     * @param starRating  StarRating enumeration for not recommended, one to five stars.
     * @param review      user review of asset.  This can be null.
     * @param isPublic   indicates whether the feedback should be shared or only be visible to the originating user
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the asset properties to the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @Override
    public void   addRatingToAsset(String     userId,
                                   String     assetGUID,
                                   StarRating starRating,
                                   String     review,
                                   boolean    isPublic) throws InvalidParameterException,
                                                                PropertyServerException,
                                                                UserNotAuthorizedException
    {
        final String   methodName  = "addRatingToAsset";
        final String   guidParameter = "assetGUID";

        final String   urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/asset-consumer/users/{1}/assets/{2}/ratings";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(assetGUID, guidParameter, methodName);

        RatingRequestBody requestBody = new RatingRequestBody();
        requestBody.setStarRating(starRating);
        requestBody.setReview(review);
        requestBody.setIsPublic(isPublic);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        assetGUID);
    }


    /**
     * Removes of a review that was added to the asset by this user.
     *
     * @param userId      userId of user making request.
     * @param assetGUID  unique identifier for the attached asset.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem updating the asset properties in the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @Override
    public void removeRatingFromAsset(String userId,
                                      String assetGUID) throws InvalidParameterException,
                                                               PropertyServerException,
                                                               UserNotAuthorizedException
    {
        final String   methodName = "removeRatingFromAsset";
        final String   guidParameter = "assetGUID";

        final String   urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/asset-consumer/users/{1}/assets/{2}/ratings/delete";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(assetGUID, guidParameter, methodName);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        nullRequestBody,
                                        serverName,
                                        userId,
                                        assetGUID);
    }


    /**
     * Adds a "LikeProperties" to the asset.
     *
     * @param userId      userId of user making request
     * @param assetGUID   unique identifier for the asset
     * @param isPublic   indicates whether the feedback should be shared or only be visible to the originating user
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the asset properties to the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @Override
    public void addLikeToAsset(String         userId,
                                 String       assetGUID,
                                 boolean      isPublic) throws InvalidParameterException,
                                                                PropertyServerException,
                                                                UserNotAuthorizedException
    {
        final String   methodName  = "addLikeToAsset";
        final String   guidParameter = "assetGUID";

        final String   urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/asset-consumer/users/{1}/assets/{2}/likes";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(assetGUID, guidParameter, methodName);

        FeedbackRequestBody requestBody = new FeedbackRequestBody();
        requestBody.setIsPublic(isPublic);
        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        assetGUID);
    }


    /**
     * Removes a "LikeProperties" added to the asset by this user.
     *
     * @param userId   userId of user making request.
     * @param assetGUID unique identifier for the like object.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem updating the asset properties in the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @Override
    public void   removeLikeFromAsset(String userId,
                                      String assetGUID) throws InvalidParameterException,
                                                               PropertyServerException,
                                                               UserNotAuthorizedException
    {
        final String   methodName = "removeLikeFromAsset";
        final String   guidParameter = "assetGUID";

        final String   urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/asset-consumer/users/{1}/assets/{2}/likes/delete";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(assetGUID, guidParameter, methodName);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        nullRequestBody,
                                        serverName,
                                        userId,
                                        assetGUID);
    }


    /**
     * Adds a comment to the asset.
     *
     * @param userId        userId of user making request.
     * @param assetGUID     unique identifier for the asset.
     * @param commentType   type of comment enum.
     * @param commentText   the text of the comment.
     * @param isPublic   indicates whether the feedback should be shared or only be visible to the originating user
     *
     * @return guid of new comment.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the asset properties to the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @Override
    public String addCommentToAsset(String      userId,
                                    String      assetGUID,
                                    CommentType commentType,
                                    String      commentText,
                                    boolean     isPublic) throws InvalidParameterException,
                                                                 PropertyServerException,
                                                                 UserNotAuthorizedException
    {
        final String   methodName  = "addCommentToAsset";
        final String   guidParameter = "assetGUID";

        final String   urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/asset-consumer/users/{1}/assets/{2}/comments";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(assetGUID, guidParameter, methodName);

        CommentRequestBody requestBody = new CommentRequestBody();
        requestBody.setCommentType(commentType);
        requestBody.setCommentText(commentText);
        requestBody.setIsPublic(isPublic);

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  urlTemplate,
                                                                  requestBody,
                                                                  serverName,
                                                                  userId,
                                                                  assetGUID);

        return restResult.getGUID();
    }


    /**
     * Adds a comment to another comment.
     *
     * @param userId        userId of user making request.
     * @param assetGUID     String - unique id of asset that this chain of comments is linked.
     * @param commentGUID   unique identifier for an existing comment.  Used to add a reply to a comment.
     * @param commentType   type of comment enum.
     * @param commentText   the text of the comment.
     * @param isPublic   indicates whether the feedback should be shared or only be visible to the originating user
     *
     * @return guid of new comment.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the asset properties to the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @Override
    public String addCommentReply(String      userId,
                                  String      assetGUID,
                                  String      commentGUID,
                                  CommentType commentType,
                                  String      commentText,
                                  boolean     isPublic) throws InvalidParameterException,
                                                                PropertyServerException,
                                                                UserNotAuthorizedException
    {
        final String   methodName  = "addCommentReply";
        final String   commentGUIDParameter = "commentGUID";
        final String   urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/asset-consumer/users/{1}/comments/{2}/replies";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(commentGUID, commentGUIDParameter, methodName);

        CommentRequestBody requestBody = new CommentRequestBody();
        requestBody.setCommentType(commentType);
        requestBody.setCommentText(commentText);
        requestBody.setIsPublic(isPublic);

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  urlTemplate,
                                                                  requestBody,
                                                                  serverName,
                                                                  userId,
                                                                  commentGUID);

        return restResult.getGUID();
    }


    /**
     * Update an existing comment.
     *
     * @param userId        userId of user making request.
     * @param assetGUID    unique identifier for the asset that the comment is attached to (directly or indirectly).
     * @param commentGUID   unique identifier for the comment to change.
     * @param commentType   type of comment enum.
     * @param commentText   the text of the comment.
     * @param isPublic   indicates whether the feedback should be shared or only be visible to the originating user
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the asset properties to the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @Override
    public void   updateComment(String      userId,
                                String      assetGUID,
                                String      commentGUID,
                                CommentType commentType,
                                String      commentText,
                                boolean     isPublic) throws InvalidParameterException,
                                                             PropertyServerException,
                                                             UserNotAuthorizedException
    {
        final String   methodName  = "updateComment";
        final String   assetGUIDParameter = "assetGUID";
        final String   commentGUIDParameter = "commentGUID";
        final String   urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/asset-consumer/users/{1}/assets/{2}/comments/{3}/update";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(assetGUID, assetGUIDParameter, methodName);
        invalidParameterHandler.validateGUID(commentGUID, commentGUIDParameter, methodName);

        CommentRequestBody requestBody = new CommentRequestBody();
        requestBody.setCommentType(commentType);
        requestBody.setCommentText(commentText);
        requestBody.setIsPublic(isPublic);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        assetGUID,
                                        commentGUID);
    }


    /**
     * Removes a comment added to the asset by this user.
     *
     * @param userId       userId of user making request.
     * @param assetGUID  unique identifier for the asset object.
     * @param commentGUID  unique identifier for the comment object.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem updating the asset properties in the property server.
     * @throws UserNotAuthorizedException the user does not have permission to perform this request.
     */
    @Override
    public void removeComment(String     userId,
                              String     assetGUID,
                              String     commentGUID) throws InvalidParameterException,
                                                             PropertyServerException,
                                                             UserNotAuthorizedException
    {
        final  String  methodName = "removeComment";
        final  String  assetGUIDParameter = "assetGUID";
        final  String  commentGUIDParameter = "commentGUID";

        final String   urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/asset-consumer/users/{1}/assets/{2}/comments/{3}/delete";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(assetGUID, assetGUIDParameter, methodName);
        invalidParameterHandler.validateGUID(commentGUID, commentGUIDParameter, methodName);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        nullRequestBody,
                                        serverName,
                                        userId,
                                        assetGUID,
                                        commentGUID);
    }


    /*
     * ===============================================
     * AssetConsumerGlossaryInterface
     * ===============================================
     */


    /**
     * Return the full definition (meaning) of a term using the unique identifier of the glossary term
     * that contains the definition.
     *
     * @param userId userId of the user making the request.
     * @param guid unique identifier of the glossary term.
     *
     * @return properties that describe the meaning
     * @throws InvalidParameterException the userId is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @Override
    public MeaningElement getMeaning(String userId,
                                     String guid) throws InvalidParameterException,
                                                         PropertyServerException,
                                                         UserNotAuthorizedException
    {
        final String   methodName = "getMeaning";
        final String   urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/asset-consumer/users/{1}/meanings/{2}";
        final String   guidParameter = "guid";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(guid, guidParameter, methodName);

        GlossaryTermResponse restResult = restClient.callGlossaryTermGetRESTCall(methodName,
                                                                                 urlTemplate,
                                                                                 serverName,
                                                                                 userId,
                                                                                 guid);

         return restResult.getMeaning();
    }


    /**
     * Return the full definition (meaning) of the terms exactly matching the supplied name.
     *
     * @param userId the name of the calling user.
     * @param term name of term.
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return list of glossary terms that contain the properties that describe the term name, and it's meaning.
     * @throws InvalidParameterException the userId is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @Override
    public List<MeaningElement> getMeaningByName(String userId,
                                                 String term,
                                                 int    startFrom,
                                                 int    pageSize) throws InvalidParameterException,
                                                                         PropertyServerException,
                                                                         UserNotAuthorizedException
    {
        final String   methodName = "getMeaningByName";
        final String   urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/asset-consumer/users/{1}/meanings/by-name/{2}?startFrom={3}&pageSize={4}";
        final String   nameParameter = "term";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(term, nameParameter, methodName);

        NameRequestBody requestBody = new NameRequestBody();

        requestBody.setName(term);
        requestBody.setNameParameterName(nameParameter);

        GlossaryTermListResponse restResult = restClient.callGlossaryTermListPostRESTCall(methodName,
                                                                                          urlTemplate,
                                                                                          requestBody,
                                                                                          serverName,
                                                                                          userId,
                                                                                          startFrom,
                                                                                          pageSize);

        return restResult.getMeanings();
    }


    /**
     * Return the full definition (meaning) of the terms matching the supplied name.
     *
     * @param userId the name of the calling user.
     * @param term name of term.  This may include wild card characters.
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return meaning list response or
     * @throws InvalidParameterException the userId is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @Override
    public List<MeaningElement> findMeanings(String userId,
                                             String term,
                                             int    startFrom,
                                             int    pageSize) throws InvalidParameterException,
                                                                     PropertyServerException,
                                                                     UserNotAuthorizedException
    {
        final String   methodName = "findMeanings";
        final String   urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/asset-consumer/users/{1}/meanings/by-search-string/{2}?startFrom={3}&pageSize={4}";
        final String   nameParameter = "term";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateSearchString(term, nameParameter, methodName);

        SearchStringRequestBody requestBody = new SearchStringRequestBody();

        requestBody.setSearchString(term);
        requestBody.setSearchStringParameterName(nameParameter);

        GlossaryTermListResponse restResult = restClient.callGlossaryTermListPostRESTCall(methodName,
                                                                                          urlTemplate,
                                                                                          requestBody,
                                                                                          serverName,
                                                                                          userId,
                                                                                          startFrom,
                                                                                          pageSize);

        return restResult.getMeanings();
    }


    /**
     * Return the list of unique identifiers for assets that are linked to a specific (meaning) either directly or via
     * fields in the schema.
     *
     * @param userId the name of the calling user.
     * @param termGUID unique identifier of term.
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return asset guid list
     * @throws InvalidParameterException the userId is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @Override
    public List<String> getAssetsByMeaning(String userId,
                                           String termGUID,
                                           int    startFrom,
                                           int    pageSize) throws InvalidParameterException,
                                                                   PropertyServerException,
                                                                   UserNotAuthorizedException
    {
        final String   methodName = "getAssetsByMeaning";
        final String   urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/asset-consumer/users/{1}/assets/by-meaning/{2}?startFrom={3}&pageSize={4}";
        final String   termGUIDParameterName = "termGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(termGUID, termGUIDParameterName, methodName);

        GUIDListResponse restResult = restClient.callGUIDListGetRESTCall(methodName,
                                                                         urlTemplate,
                                                                         serverName,
                                                                         userId,
                                                                         termGUID,
                                                                         startFrom,
                                                                         pageSize);

        return restResult.getGUIDs();
    }


    /*
     * ===============================================
     * AssetConsumerLoggingInterface
     * ===============================================
     */


    /**
     * Creates an Audit log record about the asset.  This log record is stored in the local server's Audit Log.
     *
     * @param userId               userId of user making request.
     * @param assetGUID            unique id for the asset.
     * @param connectorInstanceId  (optional) id of connector in use (if any).
     * @param connectionName       (optional) name of the connection (extracted from the connector).
     * @param connectorType        (optional) type of connector in use (if any).
     * @param contextId            (optional) function name, or processId of the activity that the caller is performing.
     * @param message              log record content.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException There is a problem adding the asset properties to the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @Override
    public void  addLogMessageToAsset(String userId,
                                      String assetGUID,
                                      String connectorInstanceId,
                                      String connectionName,
                                      String connectorType,
                                      String contextId,
                                      String message) throws InvalidParameterException,
                                                             PropertyServerException,
                                                             UserNotAuthorizedException
    {
        final String   methodName = "addLogMessageToAsset";
        final String   guidParameter = "assetGUID";

        final String   urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/asset-consumer/users/{1}/assets/{2}/log-records";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(assetGUID, guidParameter, methodName);

        LogRecordRequestBody requestBody = new LogRecordRequestBody();
        requestBody.setConnectorInstanceId(connectorInstanceId);
        requestBody.setConnectionName(connectionName);
        requestBody.setConnectorType(connectorType);
        requestBody.setContextId(contextId);
        requestBody.setMessage(message);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        assetGUID);
    }


    /*
     * ===============================================
     * AssetConsumerTaggingInterface
     * ===============================================
     */

    /**
     * Creates a new informal tag and returns the unique identifier for it.
     *
     * @param isPublic         Flg to indicate whether the tag is a public or private tag.
     * @param methodName       name of calling method.
     * @param userId           userId of user making request.
     * @param tagName          name of the tag.
     * @param tagDescription  (optional) description of the tag.  Setting a description, particularly in a public tag
     *                        makes the tag more valuable to other users and can act as an embryonic glossary term.
     *
     * @return GUID for new tag.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the asset properties to the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    private String createTag(boolean isPublic,
                             String  methodName,
                             String  userId,
                             String  tagName,
                             String  tagDescription) throws InvalidParameterException,
                                                            PropertyServerException,
                                                            UserNotAuthorizedException
    {
        final String   urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/asset-consumer/users/{1}/tags";

        invalidParameterHandler.validateUserId(userId, methodName);

        TagRequestBody  tagRequestBody = new TagRequestBody();
        tagRequestBody.setIsPrivateTag(! isPublic);
        tagRequestBody.setName(tagName);
        tagRequestBody.setDescription(tagDescription);

        GUIDResponse restResult = restClient.callGUIDPostRESTCall(methodName,
                                                                  urlTemplate,
                                                                  tagRequestBody,
                                                                  serverName,
                                                                  userId);

        return restResult.getGUID();
    }


    /**
     * Creates a new public informal tag and returns the unique identifier for it.
     *
     * @param userId           userId of user making request.
     * @param tagName          name of the tag.
     * @param tagDescription  (optional) description of the tag.  Setting a description, particularly in a public tag
     *                        makes the tag more valuable to other users and can act as an embryonic glossary term.
     *
     * @return GUID for new tag.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the asset properties to the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @Override
    public String createPublicTag(String userId,
                                  String tagName,
                                  String tagDescription) throws InvalidParameterException,
                                                                PropertyServerException,
                                                                UserNotAuthorizedException
    {
        final String   methodName = "createPublicTag";

        return this.createTag(true, methodName, userId, tagName, tagDescription);
    }


    /**
     * Creates a new private informal tag and returns the unique identifier for it.
     *
     * @param userId           userId of user making request.
     * @param tagName          name of the tag.
     * @param tagDescription  (optional) description of the tag.  Setting a description, particularly in a public tag
     *                        makes the tag more valuable to other users and can act as an embryonic glossary term.
     *
     * @return GUID for new tag.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the asset properties to the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @Override
    public String createPrivateTag(String userId,
                                   String tagName,
                                   String tagDescription) throws InvalidParameterException,
                                                                 PropertyServerException,
                                                                 UserNotAuthorizedException
    {
        final String   methodName = "createPrivateTag";

        return this.createTag(false, methodName, userId, tagName, tagDescription);
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
     * @throws PropertyServerException there is a problem adding the asset properties to the property server.
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

        final String   urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/asset-consumer/users/{1}/tags/{2}/update";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(tagGUID, guidParameter, methodName);

        TagUpdateRequestBody  tagRequestBody = new TagUpdateRequestBody();
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
     * @throws PropertyServerException there is a problem updating the asset properties in the property server.
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

        final String   urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/asset-consumer/users/{1}/tags/{2}/delete";

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
     * @param guid unique identifier of the tag.
     *
     * @return tag
     * @throws InvalidParameterException the userId is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @Override
    public InformalTagElement getTag(String userId,
                                     String guid) throws InvalidParameterException,
                                                         PropertyServerException,
                                                         UserNotAuthorizedException
    {
        final String   methodName = "getTag";
        final String   urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/asset-consumer/users/{1}/tags/{2}";
        final String   guidParameter = "guid";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(guid, guidParameter, methodName);

        TagResponse restResult = restClient.callInformalTagGetRESTCall(methodName,
                                                                       urlTemplate,
                                                                       serverName,
                                                                       userId,
                                                                       guid);

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
        final String   urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/asset-consumer/users/{1}/tags/by-name?startFrom={2}&pageSize={3}";
        final String   nameParameter = "tag";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(tag, nameParameter, methodName);

        NameRequestBody requestBody = new NameRequestBody();

        requestBody.setName(tag);
        requestBody.setNameParameterName(nameParameter);

        TagsResponse restResult = restClient.callInformalTagListPostRESTCall(methodName,
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
        final String   urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/asset-consumer/users/{1}/tags/private/by-name?startFrom={2}&pageSize={3}";
        final String   nameParameter = "tag";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(tag, nameParameter, methodName);

        NameRequestBody requestBody = new NameRequestBody();

        requestBody.setName(tag);
        requestBody.setNameParameterName(nameParameter);

        TagsResponse restResult = restClient.callInformalTagListPostRESTCall(methodName,
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
        final String   urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/asset-consumer/users/{1}/tags/by-search-string?startFrom={2}&pageSize={3}";
        final String   nameParameter = "tag";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(tag, nameParameter, methodName);

        SearchStringRequestBody requestBody = new SearchStringRequestBody();

        requestBody.setSearchString(tag);
        requestBody.setSearchStringParameterName(nameParameter);

        TagsResponse restResult = restClient.callInformalTagListPostRESTCall(methodName,
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
        final String   urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/asset-consumer/users/{1}/tags/private/by-search-string?startFrom={2}&pageSize={3}";
        final String   nameParameter = "tag";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(tag, nameParameter, methodName);

        SearchStringRequestBody requestBody = new SearchStringRequestBody();

        requestBody.setSearchString(tag);
        requestBody.setSearchStringParameterName(nameParameter);

        TagsResponse restResult = restClient.callInformalTagListPostRESTCall(methodName,
                                                                             urlTemplate,
                                                                             requestBody,
                                                                             serverName,
                                                                             userId,
                                                                             startFrom,
                                                                             pageSize);

        return restResult.getTags();
    }


    /**
     * Adds a tag (either private of public) to an asset.
     *
     * @param userId           userId of user making request.
     * @param assetGUID        unique id for the asset.
     * @param tagGUID          unique id of the tag.
     * @param isPublic         flag indicating whether the attachment of the tag is public or not
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the asset properties to the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @Override
    public void   addTagToAsset(String  userId,
                                String  assetGUID,
                                String  tagGUID,
                                boolean isPublic) throws InvalidParameterException,
                                                         PropertyServerException,
                                                         UserNotAuthorizedException
    {
        final String   methodName  = "addTagToAsset";
        final String   assetGUIDParameterName = "assetGUID";
        final String   tagGUIDParameterName = "tagGUID";

        final String   urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/asset-consumer/users/{1}/assets/{2}/tags/{3}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(assetGUID, assetGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(tagGUID, tagGUIDParameterName, methodName);

        FeedbackRequestBody requestBody = new FeedbackRequestBody();
        requestBody.setIsPublic(isPublic);
        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        assetGUID,
                                        tagGUID);
    }


    /**
     * Adds a tag (either private of public) to an element attached to an asset - such as schema element, glossary term, ...
     *
     * @param userId           userId of user making request.
     * @param elementGUID      unique id for the element.
     * @param tagGUID          unique id of the tag.
     * @param isPublic         flag indicating whether the attachment of the tag is public or not
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the asset properties to the property server.
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

        final String   urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/asset-consumer/users/{1}/assets/elements/{2}/tags/{3}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(elementGUID, elementGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(tagGUID, tagGUIDParameterName, methodName);

        FeedbackRequestBody requestBody = new FeedbackRequestBody();
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
     * Removes a tag from the asset that was added by this user.
     *
     * @param userId    userId of user making request.
     * @param assetGUID unique id for the asset.
     * @param tagGUID   unique id for the tag.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem updating the asset properties in the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @Override
    public void   removeTagFromAsset(String userId,
                                     String assetGUID,
                                     String tagGUID) throws InvalidParameterException,
                                                            PropertyServerException,
                                                            UserNotAuthorizedException
    {
        final String   methodName  = "removeTagFromAsset";
        final String   assetGUIDParameterName = "assetGUID";
        final String   tagGUIDParameterName = "tagGUID";

        final String   urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/asset-consumer/users/{1}/assets/{2}/tags/{3}/delete";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(assetGUID, assetGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(tagGUID, tagGUIDParameterName, methodName);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        nullRequestBody,
                                        serverName,
                                        userId,
                                        assetGUID,
                                        tagGUID);
    }


    /**
     * Removes a tag from an element attached to an asset - such as schema element, glossary term, ... that was added by this user.
     *
     * @param userId    userId of user making request.
     * @param elementGUID unique id for the element.
     * @param tagGUID   unique id for the tag.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem updating the asset properties in the property server.
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

        final String   urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/asset-consumer/users/{1}/assets/elements/{2}/tags/{3}/delete";

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
     * Return the list of unique identifiers for assets that are linked to a specific tag either directly, or via one
     * of its schema elements.  An Asset's GUID may appear multiple times in the results if it is tagged multiple times
     * with the requested tag.
     *
     * @param userId the name of the calling user.
     * @param tagGUID unique identifier of tag.
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return asset guid list
     * @throws InvalidParameterException the userId is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @Override
    public List<String> getAssetsByTag(String userId,
                                       String tagGUID,
                                       int    startFrom,
                                       int    pageSize) throws InvalidParameterException,
                                                               PropertyServerException,
                                                               UserNotAuthorizedException
    {
        final String methodName = "getAssetsByTag";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/asset-consumer/users/{1}/assets/by-tag/{2}?startFrom={3}&pageSize={4}";
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
}
