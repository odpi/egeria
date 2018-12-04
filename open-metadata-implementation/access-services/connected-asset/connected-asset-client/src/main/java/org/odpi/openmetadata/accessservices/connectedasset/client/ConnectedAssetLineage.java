/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.connectedasset.client;

import org.odpi.openmetadata.frameworks.connectors.properties.AssetLineage;

/**
 * ConnectedAssetLineage is the open metadata implementation of the Open Connector Framework (OCF)'s AssetLineage
 * object.  It returns information about an asset's lineage.
 */
public class ConnectedAssetLineage extends AssetLineage
{
    private String              serverName;
    private String              userId;
    private String              omasServerURL;
    private String              assetGUID;
    private ConnectedAsset      connectedAsset;
    private int                 maxCacheSize;


    /**
     * Typical constructor creates an AssetLineage object primed with information to retrieve an asset's lineage
     * information.
     *
     * @param serverName name of server to use on server calls.
     * @param userId user id to use on server calls.
     * @param omasServerURL url root of the server to use.
     * @param assetGUID unique identifier of the asset.
     * @param parentAsset descriptor of parent asset.
     * @param maxCacheSize maximum number of elements that should be retrieved from the property server and
     *                     cached in the element list at any one time.  If a number less than one is supplied, 1 is used.
     */
    ConnectedAssetLineage(String              serverName,
                          String              userId,
                          String              omasServerURL,
                          String              assetGUID,
                          ConnectedAsset      parentAsset,
                          int                 maxCacheSize)
    {
        super(parentAsset);

        this.serverName = serverName;
        this.userId = userId;
        this.omasServerURL = omasServerURL;
        this.assetGUID = assetGUID;
        this.connectedAsset = parentAsset;
        this.maxCacheSize = maxCacheSize;
    }
}
