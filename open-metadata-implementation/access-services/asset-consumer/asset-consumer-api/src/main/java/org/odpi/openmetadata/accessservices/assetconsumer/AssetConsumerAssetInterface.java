/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetconsumer;

import org.odpi.openmetadata.accessservices.assetconsumer.ffdc.exceptions.*;
import org.odpi.openmetadata.frameworks.connectors.properties.AssetUniverse;

/**
 * AssetConsumerAssetInterface supports queries to retrieve information about an asset.
 */
public interface AssetConsumerAssetInterface
{
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
     * @throws NoConnectedAssetException there is no asset associated with this connection.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    String  getAssetForConnection(String   userId,
                                  String   connectionGUID) throws InvalidParameterException,
                                                                  NoConnectedAssetException,
                                                                  PropertyServerException,
                                                                  UserNotAuthorizedException;


    /**
     * Returns the asset corresponding to the supplied connection name.
     *
     * @param userId           userId of user making request.
     * @param connectionName   this may be the qualifiedName or displayName of the connection.
     *
     * @return unique identifier of asset.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws AmbiguousConnectionNameException there is more than one connection defined for this name.
     * @throws NoConnectedAssetException there is no asset associated with this connection.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    String  getAssetForConnectionName(String userId,
                                      String connectionName) throws InvalidParameterException,
                                                                    AmbiguousConnectionNameException,
                                                                    NoConnectedAssetException,
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
