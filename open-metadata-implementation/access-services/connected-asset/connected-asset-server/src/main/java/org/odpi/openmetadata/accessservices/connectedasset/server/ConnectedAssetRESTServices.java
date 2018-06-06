/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.connectedasset.server;

import org.odpi.openmetadata.accessservices.connectedasset.server.properties.AssetUniverseResponse;
import org.odpi.openmetadata.accessservices.connectedasset.properties.AssetUniverse;


/**
 * The ConnectedAssetRESTServices is the server-side implementation of the Connected Asset OMAS REST interface.
 */
public class ConnectedAssetRESTServices
{

    /**
     * Default constructor
     */
    public ConnectedAssetRESTServices()
    {

    }


    /**
     * Returns a comprehensive collection of properties about the requested asset.
     *
     * @param userId - String - userId of user making request.
     * @param guid - String - unique id for asset.
     *
     * @return AssetUniverseResponse - a comprehensive collection of properties about the asset or
     * InvalidParameterException one of the parameters is null or invalid.
     * PropertyServerException There is a problem retrieving the asset properties from
     *                                   the metadata repository.
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public AssetUniverseResponse getAssetProperties(String   userId,
                                                    String   guid)
    {
        return null;
    }



    /**
     * Returns a comprehensive collection of properties about the asset linked to the supplied connection.
     *
     * @param userId - String - userId of user making request.
     * @param guid - String - unique id for connection.
     * @return AssetUniverse - a comprehensive collection of properties about the connected asset
     * InvalidParameterException one of the parameters is null or invalid.
     * PropertyServerException There is a problem retrieving the asset properties from
     *                                   the metadata repository.
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public AssetUniverseResponse  getAssetPropertiesByConnection(String   userId,
                                                                 String   guid)
    {
        AssetUniverse   extractedAssetProperties = null;

        return null;
    }
}
