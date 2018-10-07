/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.assetconsumer;

import org.odpi.openmetadata.accessservices.assetconsumer.ffdc.exceptions.*;
import org.odpi.openmetadata.accessservices.assetconsumer.properties.AssetCollection;
import org.odpi.openmetadata.accessservices.assetconsumer.properties.AssetCollectionMember;
import org.odpi.openmetadata.accessservices.assetconsumer.properties.AssetCollectionOrder;
import org.odpi.openmetadata.accessservices.assetconsumer.properties.MyProfile;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectionCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.properties.AssetUniverse;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.CommentType;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.StarRating;

import java.util.List;
import java.util.Map;

/**
 * The Asset Consumer Open Metadata Access Service (OMAS) is used by applications and tools as a factory for Open
 * Connector Framework (OCF) connectors.  The configuration for the connectors is managed as open metadata in
 * a Connection definition.  The caller to the Asset Consumer OMAS passes either the name, GUID or URL for the
 * connection to the appropriate method to retrieve a connector.  The Asset Consumer OMAS retrieves the connection
 * from the metadata repository and creates an appropriate connector as described the connection and
 * returns it to the caller.
 *
 * Each connection has a unique guid and a name.  An application can request a connector instance
 * from the OCF's Connector Broker using the guid, name or URL of a connection, or by passing a fully
 * populated connection object.  If the connection guid, name or URL is used, AssetConsumer OMAS
 * looks up the connection properties in the metadata repository before calling the OCF ConnectorBroker to create the
 * connector.
 *
 * The Asset Consumer OMAS supports access to the asset properties either through the connector, or by a direct
 * call to Asset Consumer API.  It also supports the management of Asset Collections.
 *
 * In addition it is possible to maintain feedback for the asset through the Asset Consumer OMAS.
 * This is in terms of tags, star ratings, likes and comments. Finally, there is the ability to add audit log records
 * related to the use of the asset through the Asset Consumer OMAS.
 */
public interface AssetConsumerInterface
{
    /**
     * Returns the connector corresponding to the supplied connection name.
     *
     * @param userId           userId of user making request.
     * @param connectionName   this may be the qualifiedName or displayName of the connection.
     *
     * @return Connector   connector instance.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UnrecognizedConnectionNameException there is no connection defined for this name.
     * @throws AmbiguousConnectionNameException there is more than one connection defined for this name.
     * @throws ConnectionCheckedException there are errors in the configuration of the connection which is preventing
     *                                      the creation of a connector.
     * @throws ConnectorCheckedException there are errors in the initialization of the connector.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    Connector getConnectorByName(String   userId,
                                 String   connectionName) throws InvalidParameterException,
                                                                 UnrecognizedConnectionNameException,
                                                                 AmbiguousConnectionNameException,
                                                                 ConnectionCheckedException,
                                                                 ConnectorCheckedException,
                                                                 PropertyServerException,
                                                                 UserNotAuthorizedException;


    /**
     * Returns the connector corresponding to the supplied connection GUID.
     *
     * @param userId           userId of user making request.
     * @param connectionGUID   the unique id for the connection within the metadata repository.
     *
     * @return Connector   connector instance.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UnrecognizedConnectionGUIDException the supplied GUID is not recognized by the property server.
     * @throws ConnectionCheckedException there are errors in the configuration of the connection which is preventing
     *                                      the creation of a connector.
     * @throws ConnectorCheckedException there are errors in the initialization of the connector.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    Connector getConnectorByGUID(String     userId,
                                 String     connectionGUID) throws InvalidParameterException,
                                                                   UnrecognizedConnectionGUIDException,
                                                                   ConnectionCheckedException,
                                                                   ConnectorCheckedException,
                                                                   PropertyServerException,
                                                                   UserNotAuthorizedException;


    /**
     * Returns the connector corresponding to the supplied connection.
     *
     * @param userId       userId of user making request.
     * @param connection   the connection object that contains the properties needed to create the connection.
     *
     * @return Connector   connector instance
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws ConnectionCheckedException there are errors in the configuration of the connection which is preventing
     *                                      the creation of a connector.
     * @throws ConnectorCheckedException there are errors in the initialization of the connector.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     */
    Connector  getConnectorByConnection(String        userId,
                                        Connection    connection) throws InvalidParameterException,
                                                                         ConnectionCheckedException,
                                                                         ConnectorCheckedException,
                                                                         PropertyServerException;


    /**
     * Return the profile for this user.
     *
     * @param userId userId of the user making the request.
     *
     * @return profile object
     *
     * @throws InvalidParameterException the userId is null or invalid.
     * @throws NoProfileForUserException the user does not have a profile.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    MyProfile   getMyProfile(String userId) throws InvalidParameterException,
                                                   NoProfileForUserException,
                                                   PropertyServerException,
                                                   UserNotAuthorizedException;


    /**
     * Create or update the profile for the requesting user.
     *
     * @param userId the name of the calling user.
     * @param employeeNumber personnel/serial/unique employee number of the individual.
     * @param fullName full name of the person.
     * @param knownName known name or nickname of the individual.
     * @param jobTitle job title of the individual.
     * @param jobRoleDescription job description of the individual.
     * @param additionalProperties  additional properties about the individual.
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws PropertyServerException  there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    void      updateMyProfile(String              userId,
                              String              employeeNumber,
                              String              fullName,
                              String              knownName,
                              String              jobTitle,
                              String              jobRoleDescription,
                              Map<String, Object> additionalProperties) throws InvalidParameterException,
                                                                               PropertyServerException,
                                                                               UserNotAuthorizedException;


    /**
     * Returns the list of asset collections that this user has created or linked to their profile.
     *
     * @param userId     userId of user making request
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return
     *
     * @return a list of asset collections
     *
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws NoProfileForUserException  the user does not have a profile so can not have any asset collections.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    List<AssetCollection>    getMyAssetCollections(String    userId,
                                                   int       startFrom,
                                                   int       pageSize) throws InvalidParameterException,
                                                                              NoProfileForUserException,
                                                                              PropertyServerException,
                                                                              UserNotAuthorizedException;


    /**
     * Return the properties of a specific asset collection,
     *
     * @param userId              userId of user making request.
     * @param assetCollectionGUID unique identifier of the required connection.
     *
     * @return asset collection properties
     *
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws NoProfileForUserException  the user does not have a profile so can not have any asset collections.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    AssetCollection     getAssetCollection(String    userId,
                                           String    assetCollectionGUID) throws InvalidParameterException,
                                                                                 NoProfileForUserException,
                                                                                 PropertyServerException,
                                                                                 UserNotAuthorizedException;


    /**
     * Create a new asset collection.
     *
     * @param userId                 userId of user making request.
     * @param qualifiedName          unique name of the asset collection.
     * @param displayName            short displayable name for the asset collection.
     * @param description            description of the asset collection.
     * @param collectionUse          description of how the asset consumer intends to use the collection.
     * @param assetCollectionOrder   description of how the assets in the collection should be organized (null for no organization).
     * @param additionalProperties   additional arbitrary properties.
     *
     * @return the newly created AssetCollection
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws NoProfileForUserException  the user does not have a profile so can not have any asset collections.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    AssetCollection     createAssetCollection(String                 userId,
                                              String                 qualifiedName,
                                              String                 displayName,
                                              String                 description,
                                              String                 collectionUse,
                                              AssetCollectionOrder   assetCollectionOrder,
                                              Map<String, Object>    additionalProperties) throws InvalidParameterException,
                                                                                                  NoProfileForUserException,
                                                                                                  PropertyServerException,
                                                                                                  UserNotAuthorizedException;


    /**
     * Connect an existing asset collection to this user's profile.
     *
     * @param userId               userId of user making request.
     * @param assetCollectionGUID  unique identifier of the asset collection.
     * @return details of the identified asset collection
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws NoProfileForUserException  the user does not have a profile so can not have any asset collections.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    AssetCollection     attachAssetCollection(String    userId,
                                              String    assetCollectionGUID) throws InvalidParameterException,
                                                                                    NoProfileForUserException,
                                                                                    PropertyServerException,
                                                                                    UserNotAuthorizedException;


    /**
     * Delete an asset collection (the assets are not affected).
     *
     * @param userId               userId of user making request.
     * @param assetCollectionGUID  unique identifier of the asset collection.
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws NoProfileForUserException  the user does not have a profile so can not have any asset collections.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    void   removeAssetCollection(String    userId,
                                 String    assetCollectionGUID) throws InvalidParameterException,
                                                                       PropertyServerException,
                                                                       NoProfileForUserException,
                                                                       UserNotAuthorizedException;


    /**
     * Return a list of assets that the specified user has added to their favorites list.
     *
     * @param userId     userId of user making request.
     * @param assetCollectionGUID  unique identifier of the asset collection to use.
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return list of asset details
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws NoProfileForUserException  the user does not have a profile so can not have any asset collections.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    List<AssetCollectionMember> getMyAssets(String    userId,
                                            String    assetCollectionGUID,
                                            int       startFrom,
                                            int       pageSize) throws InvalidParameterException,
                                                                       NoProfileForUserException,
                                                                       PropertyServerException,
                                                                       UserNotAuthorizedException;

    /**
     * Add an asset to the identified user's list of favorite assets.
     *
     * @param userId     userId of user making request.
     * @param assetCollectionGUID  unique identifier of the asset collection to use.
     * @param assetGUID  unique identifier of the asset.
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws NoProfileForUserException  the user does not have a profile so can not have any asset collections.
     * @throws NotAnAssetException the guid is not recognized as an identifier for an asset.
     * @throws PropertyServerException there is a problem updating information in the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    void  addToMyAssets(String   userId,
                        String   assetCollectionGUID,
                        String   assetGUID) throws InvalidParameterException,
                                                   NoProfileForUserException,
                                                   NotAnAssetException,
                                                   PropertyServerException,
                                                   UserNotAuthorizedException;


    /**
     * Remove an asset from identified user's list of favorite assets.
     *
     * @param userId     userId of user making request.
     * @param assetCollectionGUID  unique identifier of the asset collection to use.
     * @param assetGUID  unique identifier of the asset.
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws NoProfileForUserException  the user does not have a profile so can not have any asset collections.
     * @throws PropertyServerException there is a problem updating information in the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    void  removeFromMyAssets(String   userId,
                             String   assetCollectionGUID,
                             String   assetGUID) throws InvalidParameterException,
                                                        NoProfileForUserException,
                                                        PropertyServerException,
                                                        UserNotAuthorizedException;


    /**
     * Returns the unique identifier for the asset connected to the connection.
     *
     * @param userId the userId of the requesting user.
     * @param connectionGUID  uniqueId for the connection.
     *
     * @return unique identifier of asset
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem retrieving the connected asset properties from the property server.
     * @throws UnrecognizedConnectionGUIDException the supplied GUID is not recognized by the property server.
     * @throws NoConnectedAssetException there is no asset associated with this connection.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    String getAssetForConnection(String   userId,
                                 String   connectionGUID) throws InvalidParameterException,
                                                                 PropertyServerException,
                                                                 UnrecognizedConnectionGUIDException,
                                                                 NoConnectedAssetException,
                                                                 UserNotAuthorizedException;

    /**
     * Returns a comprehensive collection of properties about the requested asset.
     *
     * @param userId         userId of user making request.
     * @param assetGUID      unique id for asset.
     *
     * @return a comprehensive collection of properties about the asset.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException There is a problem retrieving the asset properties from the property servers).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    AssetUniverse getAssetProperties(String   userId,
                                     String   assetGUID) throws InvalidParameterException,
                                                                PropertyServerException,
                                                                UserNotAuthorizedException;


    /**
     * Creates an Audit log record for the asset.  This log record is stored in the Asset's Audit Log.
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
    void  addLogMessageToAsset(String      userId,
                               String      assetGUID,
                               String      connectorInstanceId,
                               String      connectionName,
                               String      connectorType,
                               String      contextId,
                               String      message) throws InvalidParameterException,
                                                           PropertyServerException,
                                                           UserNotAuthorizedException;



    /**
     * Adds a new public tag to the asset's properties.
     *
     * @param userId           userId of user making request.
     * @param assetGUID        unique id for the asset.
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
    String addTagToAsset(String userId,
                         String assetGUID,
                         String tagName,
                         String tagDescription) throws InvalidParameterException,
                                                       PropertyServerException,
                                                       UserNotAuthorizedException;


    /**
     * Adds a new private tag to the asset's properties.
     *
     * @param userId              userId of user making request.
     * @param assetGUID           unique id for the asset.
     * @param tagName             name of the tag.
     * @param tagDescription      (optional) description of the tag.
     *
     * @return GUID for new tag.
     *
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem adding the asset properties to the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    String addPrivateTagToAsset(String userId,
                                String assetGUID,
                                String tagName,
                                String tagDescription) throws InvalidParameterException,
                                                              PropertyServerException,
                                                              UserNotAuthorizedException;



    /**
     * Adds a rating to the asset.
     *
     * @param userId      userId of user making request.
     * @param assetGUID   unique id for the asset.
     * @param starRating  StarRating enumeration for none, one to five stars.
     * @param review      user review of asset.
     *
     * @return guid of new rating object.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the asset properties to the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    String addRatingToAsset(String     userId,
                            String     assetGUID,
                            StarRating starRating,
                            String     review) throws InvalidParameterException,
                                                      PropertyServerException,
                                                      UserNotAuthorizedException;

    /**
     * Adds a "Like" to the asset.
     *
     * @param userId      userId of user making request.
     * @param assetGUID   unique id for the asset
     *
     * @return guid of new like object.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the asset properties to the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    String addLikeToAsset(String       userId,
                          String       assetGUID) throws InvalidParameterException,
                                                         PropertyServerException,
                                                         UserNotAuthorizedException;


    /**
     * Adds a comment to the asset.
     *
     * @param userId        userId of user making request.
     * @param assetGUID     unique id for the asset.
     * @param commentType   type of comment enum.
     * @param commentText   the text of the comment.
     *
     * @return guid of new comment.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the asset properties to the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    String addCommentToAsset(String      userId,
                             String      assetGUID,
                             CommentType commentType,
                             String      commentText) throws InvalidParameterException,
                                                             PropertyServerException,
                                                             UserNotAuthorizedException;


    /**
     * Adds a comment to the asset.
     *
     * @param userId        userId of user making request.
     * @param commentGUID   unique id for an existing comment.  Used to add a reply to a comment.
     * @param commentType   type of comment enum.
     * @param commentText   the text of the comment.
     *
     * @return guid of new comment.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the asset properties to the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    String addCommentReply(String      userId,
                           String      commentGUID,
                           CommentType commentType,
                           String      commentText) throws InvalidParameterException,
                                                           PropertyServerException,
                                                           UserNotAuthorizedException;


    /**
     * Removes a tag from the asset that was added by this user.
     *
     * @param userId    userId of user making request.
     * @param tagGUID   unique id for the tag.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem updating the asset properties in the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    void   removeTag(String     userId,
                     String     tagGUID) throws InvalidParameterException,
                                                PropertyServerException,
                                                UserNotAuthorizedException;


    /**
     * Removes a private tag from the asset that was added by this user.
     *
     * @param userId    userId of user making request.
     * @param tagGUID   unique id for the tag.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem updating the asset properties in the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    void   removePrivateTag(String     userId,
                            String     tagGUID) throws InvalidParameterException,
                                                       PropertyServerException,
                                                       UserNotAuthorizedException;


    /**
     * Removes of a star rating that was added to the asset by this user.
     *
     * @param userId      userId of user making request.
     * @param ratingGUID  unique id for the rating object.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem updating the asset properties in the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    void   removeRating(String     userId,
                        String     ratingGUID) throws InvalidParameterException,
                                                      PropertyServerException,
                                                      UserNotAuthorizedException;


    /**
     * Removes a "Like" added to the asset by this user.
     *
     * @param userId   userId of user making request.
     * @param likeGUID unique id for the like object.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem updating the asset properties in the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    void   removeLike(String     userId,
                      String     likeGUID) throws InvalidParameterException,
                                                  PropertyServerException,
                                                  UserNotAuthorizedException;


    /**
     * Removes a comment added to the asset by this user.
     *
     * @param userId       userId of user making request.
     * @param commentGUID  unique id for the comment object.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem updating the asset properties in the property server.
     * @throws UserNotAuthorizedException the user does not have permission to perform this request.
     */
    void   removeComment(String     userId,
                         String     commentGUID) throws InvalidParameterException,
                                                        PropertyServerException,
                                                        UserNotAuthorizedException;
}
