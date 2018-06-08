/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.connectedasset.server.spring;

import org.odpi.openmetadata.accessservices.connectedasset.server.properties.AssetUniverseResponse;
import org.odpi.openmetadata.accessservices.connectedasset.server.ConnectedAssetRESTServices;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * The ConnectedAssetRESTServices is the server-side implementation of the Connected Asset OMAS REST interface.
 */
@RestController
@RequestMapping("/access-services/connected-asset")
public class ConnectedAssetOMASResource
{
    private ConnectedAssetRESTServices restAPI = new ConnectedAssetRESTServices();

    /**
     * Default constructor
     */
    public ConnectedAssetOMASResource()
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
    @RequestMapping(method = RequestMethod.GET, path = "/users/{userId}/assets/{guid}")

    public AssetUniverseResponse getAssetProperties(@PathVariable String   userId,
                                                    @PathVariable String   guid)
    {
        return restAPI.getAssetProperties(userId, guid);
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
    @RequestMapping(method = RequestMethod.GET, path = "/users/{userId}/assets/by-connection/{guid}")

    public AssetUniverseResponse  getAssetPropertiesByConnection(@PathVariable String   userId,
                                                                 @PathVariable String   guid)
    {
        return restAPI.getAssetPropertiesByConnection(userId, guid);
    }
}
