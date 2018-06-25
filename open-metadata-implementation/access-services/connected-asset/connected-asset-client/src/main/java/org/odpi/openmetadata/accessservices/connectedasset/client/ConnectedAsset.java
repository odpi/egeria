/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.connectedasset.client;

import org.odpi.openmetadata.accessservices.connectedasset.ffdc.exceptions.*;
import org.odpi.openmetadata.accessservices.connectedasset.ConnectedAssetInterface;
import org.odpi.openmetadata.frameworks.connectors.properties.AssetUniverse;


/**
 * ConnectedAsset is the OMAS client library implementation of the ConnectedAsset OMAS.
 * ConnectedAsset provides the metadata for the ConnectedAssetProperties API that is
 * supported by all Open Connector Framework (OCF)
 * connectors.   It provides access to the metadata about the Asset that the connector is linked to.
 */
public class ConnectedAsset implements ConnectedAssetInterface
{
    /*
     * The URL of the server where OMAS is active
     */
    private String                    omasServerURL = null;


    /**
     * Default Constructor used once a connector is created.
     *
     * @param omasServerURL - unique id for the connector instance
     */
    public ConnectedAsset(String   omasServerURL)
    {
        /*
         * Save OMAS Server URL
         */
        this.omasServerURL = omasServerURL;
    }



    /**
     * Returns a comprehensive collection of properties about the requested asset.
     *
     * @param userId - String - userId of user making request.
     * @param assetGUID - String - unique id for asset.
     *
     * @return AssetUniverse - a comprehensive collection of properties about the asset.

     * @throws UnrecognizedAssetGUIDException the GUID is null or invalid.
     * @throws PropertyServerException There is a problem retrieving the asset properties from
     *                                   the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public AssetUniverse getAssetProperties(String   userId,
                                            String   assetGUID) throws UnrecognizedAssetGUIDException,
                                                                       PropertyServerException,
                                                                       UserNotAuthorizedException
    {
        return null;
    }


    /**
     * Returns a comprehensive collection of properties about the asset linked to the supplied connection.
     *
     * @param connectionGUID - unique identifier for the connection
     * @return AssetUniverse - a comprehensive collection of properties about the connected asset
     * @throws PropertyServerException There is a problem retrieving the connected asset properties from
     *                                   the metadata repository.
     */
    public AssetUniverse  getAssetPropertiesByConnection(String   connectionGUID) throws PropertyServerException
    {
        AssetUniverse   extractedAssetProperties = null;

        /*
         * Set up the OMAS URL in the asset universe
         */

        return extractedAssetProperties;
    }


    /**
     * Returns a comprehensive collection of properties about the asset linked to the supplied connection.
     *
     * @param connectionGUID - uniqueId for the connection.
     * @return AssetUniverse - a comprehensive collection of properties about the connected asset.
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException There is a problem retrieving the connected asset properties from
     *                                   the property server.
     * @throws UnrecognizedConnectionGUIDException the supplied GUID is not recognized by the property server.
     */
    public AssetUniverse  getAssetPropertiesByConnection(String   userId,
                                                         String   connectionGUID) throws InvalidParameterException,
                                                                                         UnrecognizedConnectionGUIDException,
                                                                                         PropertyServerException
    {
        return null;
    }
}
