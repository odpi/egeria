/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.connectedasset;

import org.odpi.openmetadata.accessservices.connectedasset.ffdc.exceptions.*;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.AssetUniverse;

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
     * @throws UnrecognizedAssetGUIDException the GUID is null or invalid.
     * @throws PropertyServerException There is a problem retrieving the asset properties from the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    AssetUniverse getAssetProperties(String   userId,
                                     String   assetGUID) throws UnrecognizedAssetGUIDException,
                                                                PropertyServerException,
                                                                UserNotAuthorizedException;
}
