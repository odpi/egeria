/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetconsumer.api;

import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.AssetUniverse;

import java.util.List;

/**
 * AssetConsumerAssetInterface supports queries to retrieve information about an asset.
 */
public interface AssetConsumerAssetInterface
{
    /**
     * Return a list of assets with the requested search string in their name, qualified name
     * or description.  The search string is interpreted as a regular expression (RegEx).
     *
     * @param userId calling user
     * @param searchString string to search for in text
     * @param startFrom starting element (used in paging through large result sets)
     * @param pageSize maximum number of results to return
     *
     * @return list of unique identifiers (GUIDs) for the matching assets
     *
     * @throws InvalidParameterException the searchString is invalid
     * @throws PropertyServerException there is a problem access in the property server
     * @throws UserNotAuthorizedException the user does not have access to the properties
     */
    List<String> findAssets(String   userId,
                            String   searchString,
                            int      startFrom,
                            int      pageSize) throws InvalidParameterException,
                                                      PropertyServerException,
                                                      UserNotAuthorizedException;


    /**
     * Return a list of assets with the requested name.
     *
     * @param userId calling user
     * @param name name to search for
     * @param startFrom starting element (used in paging through large result sets)
     * @param pageSize maximum number of results to return
     *
     * @return list of unique identifiers (GUIDs) of assets with matching name.
     *
     * @throws InvalidParameterException the name is invalid
     * @throws PropertyServerException there is a problem access in the property server
     * @throws UserNotAuthorizedException the user does not have access to the properties
     */
    List<String>  getAssetsByName(String   userId,
                                  String   name,
                                  int      startFrom,
                                  int      pageSize) throws InvalidParameterException,
                                                            PropertyServerException,
                                                            UserNotAuthorizedException;


    /**
     * Returns a list of assets that match the token which may be the GUID or the qualified name.
     *
     * @param userId         userId of user making request.
     * @param assetToken     token used to find the Asset - may be a name or GUID
     * @param startFrom starting element (used in paging through large result sets)
     * @param pageSize maximum number of results to return
     *
     * @return a list of unique identifiers (GUIDs) for the matching assets
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem retrieving the asset properties from the property servers).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    List<String> getAssetsByToken(String userId,
                                  String assetToken,
                                  int    startFrom,
                                  int    pageSize) throws InvalidParameterException,
                                                          PropertyServerException,
                                                          UserNotAuthorizedException;


    /**
     * Returns the unique identifier for the asset connected to the requested connection.
     *
     * @param userId the userId of the requesting user.
     * @param connectionGUID  unique identifier for the connection.
     *
     * @return unique identifier (GUID) of asset.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    String  getAssetForConnection(String   userId,
                                  String   connectionGUID) throws InvalidParameterException,
                                                                  PropertyServerException,
                                                                  UserNotAuthorizedException;


    /**
     * Returns the asset corresponding to the supplied connection name.
     *
     * @param userId           userId of user making request.
     * @param connectionName   this may be the qualifiedName or displayName of the connection.
     *
     * @return unique identifier (GUID) of asset.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    String  getAssetForConnectionName(String userId,
                                      String connectionName) throws InvalidParameterException,
                                                                    PropertyServerException,
                                                                    UserNotAuthorizedException;


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
    AssetUniverse getAssetProperties(String userId,
                                     String assetGUID) throws InvalidParameterException,
                                                              PropertyServerException,
                                                              UserNotAuthorizedException;
}
