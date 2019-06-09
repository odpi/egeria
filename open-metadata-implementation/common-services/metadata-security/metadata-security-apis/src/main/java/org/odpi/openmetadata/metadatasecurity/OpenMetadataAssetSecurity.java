/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.metadatasecurity;

import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Asset;

/**
 * OpenMetadataAssetSecurity validates what a user is allowed to do with to Assets.
 * The methods are given access to the whole asset to allow a variety of values to be tested.
 */
public interface OpenMetadataAssetSecurity extends OpenMetadataServerSecurity
{
    /**
     * Tests for whether a specific user should have the right to create an asset.
     *
     * @param userId identifier of user
     * @param asset new asset details
     * @throws UserNotAuthorizedException the user is not authorized to change this asset
     */
    void  validateUserForAssetCreate(String userId,
                                     Asset asset) throws UserNotAuthorizedException;


    /**
     * Tests for whether a specific user should have read access to a specific asset.
     *
     * @param userId identifier of user
     * @param asset asset details
     * @throws UserNotAuthorizedException the user is not authorized to access this asset
     */
    void  validateUserForAssetRead(String userId,
                                   Asset asset) throws UserNotAuthorizedException;


    /**
     * Tests for whether a specific user should have the right to update an asset.
     * This is used for a general asset update, which may include changes to the
     * zones and the ownership.
     *
     * @param userId identifier of user
     * @param originalAsset original asset details
     * @param newAsset new asset details
     * @throws UserNotAuthorizedException the user is not authorized to change this asset
     */
    void  validateUserForAssetDetailUpdate(String userId,
                                           Asset originalAsset,
                                           Asset newAsset) throws UserNotAuthorizedException;


    /**
     * Tests for whether a specific user should have the right to update elements attached directly
     * to an asset such as schema and connections.
     *
     * @param userId identifier of user
     * @param asset original asset details
     * @throws UserNotAuthorizedException the user is not authorized to change this asset
     */
    void  validateUserForAssetAttachmentUpdate(String userId,
                                               Asset asset) throws UserNotAuthorizedException;


    /**
     * Tests for whether a specific user should have the right to attach feedback - such as comments,
     * ratings, tags and likes, to the asset.
     *
     * @param userId identifier of user
     * @param asset original asset details
     * @throws UserNotAuthorizedException the user is not authorized to change this asset
     */
    void  validateUserForAssetFeedback(String userId,
                                       Asset asset) throws UserNotAuthorizedException;


    /**
     * Tests for whether a specific user should have the right to delete an asset.
     *
     * @param userId identifier of user
     * @param asset original asset details
     * @throws UserNotAuthorizedException the user is not authorized to change this asset
     */
    void  validateUserForAssetDelete(String userId,
                                     Asset asset) throws UserNotAuthorizedException;
}
