/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.metadatasecurity;

import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.metadatasecurity.properties.Asset;
import org.odpi.openmetadata.metadatasecurity.properties.AssetAuditHeader;

import java.util.List;

/**
 * OpenMetadataAssetSecurity is able to intercede as the zones are set up in an asset and
 * validates what a user is allowed to do with to Assets (often using the zone values).
 * The methods are given access to the whole asset to allow a variety of values to be tested.
 */
public interface OpenMetadataAssetSecurity
{
    /**
     * Determine the appropriate setting for the supported zones depending on the user and the
     * default supported zones set up for the service.  This is called whenever an asset is accessed.
     *
     * @param supportedZones default setting of the supported zones for the service
     * @param serviceName name of the called service
     * @param user name of the user
     *
     * @return list of supported zones for the user
     * @throws InvalidParameterException one of the parameter values is invalid
     * @throws PropertyServerException there is a problem calculating the zones
     */
    List<String> setSupportedZonesForUser(List<String>  supportedZones,
                                          String        serviceName,
                                          String        user) throws InvalidParameterException,
                                                                     PropertyServerException;


    /**
     * Determine the appropriate setting for the asset zones depending on the content of the asset and the
     * default zones.  This is called whenever a new asset is created.
     *
     * @param defaultZones setting of the default zones for the service
     * @param asset initial values for the asset
     *
     * @return list of zones to set in the asset
     * @throws InvalidParameterException one of the asset values is invalid
     * @throws PropertyServerException there is a problem calculating the zones
     */
    List<String> setAssetZonesToDefault(List<String>  defaultZones,
                                        Asset         asset) throws InvalidParameterException,
                                                                    PropertyServerException;


    /**
     * Determine the appropriate setting for the asset zones depending on the content of the asset and the
     * settings of both default zones and supported zones.  This method is called whenever an asset's
     * values are changed.
     *
     * @param defaultZones setting of the default zones for the service
     * @param supportedZones setting of the supported zones for the service
     * @param publishZones setting of the publish zones for the service
     * @param originalAsset original values for the asset
     * @param updatedAsset updated values for the asset
     *
     * @return list of zones to set in the asset
     * @throws InvalidParameterException one of the asset values is invalid
     * @throws PropertyServerException there is a problem calculating the zones
     */
    List<String> verifyAssetZones(List<String>  defaultZones,
                                  List<String>  supportedZones,
                                  List<String>  publishZones,
                                  Asset         originalAsset,
                                  Asset         updatedAsset) throws InvalidParameterException,
                                                                     PropertyServerException;


    /**
     * Tests for whether a specific user should have the right to create an asset.
     *
     * @param userId identifier of user
     * @param asset new asset details
     * @throws UserNotAuthorizedException the user is not authorized to change this asset
     */
    void  validateUserForAssetCreate(String userId,
                                     Asset  asset) throws UserNotAuthorizedException;


    /**
     * Tests for whether a specific user should have read access to a specific asset.
     *
     * @param userId identifier of user
     * @param asset asset details
     * @throws UserNotAuthorizedException the user is not authorized to access this asset
     */
    void  validateUserForAssetRead(String userId,
                                   Asset  asset) throws UserNotAuthorizedException;


    /**
     * Tests for whether a specific user should have the right to update an asset.
     * This is used for a general asset update, which may include changes to the
     * zones and the ownership.
     *
     * @param userId identifier of user
     * @param originalAsset original asset details
     * @param originalAssetAuditHeader details of the asset's audit header
     * @param newAsset new asset details
     * @throws UserNotAuthorizedException the user is not authorized to change this asset
     */
    void  validateUserForAssetDetailUpdate(String           userId,
                                           Asset            originalAsset,
                                           AssetAuditHeader originalAssetAuditHeader,
                                           Asset            newAsset) throws UserNotAuthorizedException;


    /**
     * Tests for whether a specific user should have the right to update elements attached directly
     * to an asset such as schema and connections.
     *
     * @param userId identifier of user
     * @param asset original asset details
     * @throws UserNotAuthorizedException the user is not authorized to change this asset
     */
    void  validateUserForAssetAttachmentUpdate(String userId,
                                               Asset  asset) throws UserNotAuthorizedException;


    /**
     * Tests for whether a specific user should have the right to attach feedback - such as comments,
     * ratings, tags and likes, to the asset.
     *
     * @param userId identifier of user
     * @param asset original asset details
     * @throws UserNotAuthorizedException the user is not authorized to change this asset
     */
    void  validateUserForAssetFeedback(String userId,
                                       Asset  asset) throws UserNotAuthorizedException;


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
