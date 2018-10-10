/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetconsumer.server;

import org.odpi.openmetadata.accessservices.assetconsumer.ffdc.exceptions.*;
import org.odpi.openmetadata.accessservices.assetconsumer.properties.AssetCollection;
import org.odpi.openmetadata.accessservices.assetconsumer.properties.AssetCollectionMember;
import org.odpi.openmetadata.accessservices.assetconsumer.properties.AssetCollectionOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.List;
import java.util.Map;

/**
 * MyAssetsHandler provides the mechanisms to manage collections of favorite assets for an asset consumer.
 */
public class MyAssetsHandler
{
    private String               serviceName;
    private OMRSRepositoryHelper repositoryHelper = null;
    private String               serverName       = null;
    private ErrorHandler         errorHandler     = null;

    /**
     * Construct the connection handler with a link to the property server's connector and this access service's
     * official name.
     *
     * @param serviceName  name of this service
     * @param repositoryConnector  connector to the property server.
     */
    MyAssetsHandler(String                  serviceName,
                    OMRSRepositoryConnector repositoryConnector)
    {
        this.serviceName = serviceName;
        if (repositoryConnector != null)
        {
            this.repositoryHelper = repositoryConnector.getRepositoryHelper();
            this.serverName = repositoryConnector.getServerName();
            errorHandler = new ErrorHandler(repositoryConnector);
        }
    }


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
    public List<AssetCollection>    getMyAssetCollections(String    userId,
                                                          int       startFrom,
                                                          int       pageSize) throws InvalidParameterException,
                                                                                     NoProfileForUserException,
                                                                                     PropertyServerException,
                                                                                     UserNotAuthorizedException
    {
        final String  methodName  = "getMyAssetCollections";

        errorHandler.validateUserId(userId, methodName);

        OMRSMetadataCollection metadataCollection = errorHandler.validateRepositoryConnector(methodName);

        // TODO

        return null;
    }


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
    public AssetCollection     getAssetCollection(String    userId,
                                                  String    assetCollectionGUID) throws InvalidParameterException,
                                                                                        NoProfileForUserException,
                                                                                        PropertyServerException,
                                                                                        UserNotAuthorizedException
    {
        final String  methodName  = "getAssetCollection";

        errorHandler.validateUserId(userId, methodName);

        OMRSMetadataCollection metadataCollection = errorHandler.validateRepositoryConnector(methodName);

        // TODO

        return null;
    }

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
    public AssetCollection     createAssetCollection(String                 userId,
                                                     String                 qualifiedName,
                                                     String                 displayName,
                                                     String                 description,
                                                     String                 collectionUse,
                                                     AssetCollectionOrder   assetCollectionOrder,
                                                     Map<String, Object>    additionalProperties) throws InvalidParameterException,
                                                                                                         NoProfileForUserException,
                                                                                                         PropertyServerException,
                                                                                                         UserNotAuthorizedException
    {
        final String  methodName  = "createAssetCollection";

        errorHandler.validateUserId(userId, methodName);

        OMRSMetadataCollection metadataCollection = errorHandler.validateRepositoryConnector(methodName);

        // TODO

        return null;
    }


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
    public AssetCollection     attachAssetCollection(String    userId,
                                                     String    assetCollectionGUID) throws InvalidParameterException,
                                                                                           NoProfileForUserException,
                                                                                           PropertyServerException,
                                                                                           UserNotAuthorizedException
    {
        final String  methodName  = "attachAssetCollection";

        errorHandler.validateUserId(userId, methodName);

        OMRSMetadataCollection metadataCollection = errorHandler.validateRepositoryConnector(methodName);

        // TODO

        return null;
    }


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
    public void   removeAssetCollection(String    userId,
                                        String    assetCollectionGUID) throws InvalidParameterException,
                                                                              NoProfileForUserException,
                                                                              PropertyServerException,
                                                                              UserNotAuthorizedException
    {
        final String  methodName  = "removeAssetCollection";

        errorHandler.validateUserId(userId, methodName);

        OMRSMetadataCollection metadataCollection = errorHandler.validateRepositoryConnector(methodName);

        // TODO


    }


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
    public List<AssetCollectionMember> getMyAssets(String    userId,
                                                   String    assetCollectionGUID,
                                                   int       startFrom,
                                                   int       pageSize) throws InvalidParameterException,
                                                                              NoProfileForUserException,
                                                                              PropertyServerException,
                                                                              UserNotAuthorizedException
    {
        final String  methodName  = "getMyAssets";

        errorHandler.validateUserId(userId, methodName);

        OMRSMetadataCollection metadataCollection = errorHandler.validateRepositoryConnector(methodName);

        return null;
    }


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
    public void  addToMyAssets(String   userId,
                               String   assetCollectionGUID,
                               String   assetGUID) throws InvalidParameterException,
                                                          NoProfileForUserException,
                                                          NotAnAssetException,
                                                          PropertyServerException,
                                                          UserNotAuthorizedException
    {
        final String  methodName  = "addToMyAssets";

        errorHandler.validateUserId(userId, methodName);

        OMRSMetadataCollection metadataCollection = errorHandler.validateRepositoryConnector(methodName);

        // TODO


    }


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
    public void  removeFromMyAssets(String   userId,
                                    String   assetCollectionGUID,
                                    String   assetGUID) throws InvalidParameterException,
                                                               NoProfileForUserException,
                                                               PropertyServerException,
                                                               UserNotAuthorizedException
    {
        final String  methodName = "removeFromMyAssets";

        errorHandler.validateUserId(userId, methodName);

        OMRSMetadataCollection metadataCollection = errorHandler.validateRepositoryConnector(methodName);

        // TODO

    }
}
