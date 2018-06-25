/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.connectedasset;

import org.odpi.openmetadata.accessservices.connectedasset.ffdc.exceptions.*;
import org.odpi.openmetadata.frameworks.connectors.properties.AssetUniverse;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Comment;

/**
 * ConnectedAssetInterface is the OMAS client interface of the Connected Asset OMAS.
 *
 */
public interface ConnectedAssetInterface
{
    /**
     * Returns a comprehensive collection of properties about the requested asset.
     *
     * @param userId   String   userId of user making request.
     * @param assetGUID   String   unique id for asset.
     *
     * @return AssetUniverse   a comprehensive collection of properties about the asset.

     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException There is a problem retrieving the asset properties from
     *                                   the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    AssetUniverse getAssetProperties(String   userId,
                                     String   assetGUID) throws UnrecognizedAssetGUIDException,
                                                                PropertyServerException,
                                                                UserNotAuthorizedException;


    /**
     * Returns a comprehensive collection of properties about the asset linked to the supplied connection.
     *
     * @param userId   identifier for calling user
     * @param connectionGUID   uniqueId for the connection.
     * @return AssetUniverse   a comprehensive collection of properties about the connected asset.
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException There is a problem retrieving the connected asset properties from
     *                                   the property server.
     * @throws UnrecognizedConnectionGUIDException the supplied GUID is not recognized by the property server.
     */
    AssetUniverse  getAssetPropertiesByConnection(String   userId,
                                                  String   connectionGUID) throws InvalidParameterException,
                                                                                  UnrecognizedConnectionGUIDException,
                                                                                  PropertyServerException;


    /*
     * Feedback


    Comment getCommentsForAsset(String    userId,
                                String    assetGUID,
                                int       startComment,
                                int       pageSize);
                                */
}
