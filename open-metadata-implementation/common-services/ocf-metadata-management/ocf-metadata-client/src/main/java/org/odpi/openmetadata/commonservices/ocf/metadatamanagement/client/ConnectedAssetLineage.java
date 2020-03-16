/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ocf.metadatamanagement.client;

import org.odpi.openmetadata.frameworks.connectors.properties.AssetLineage;

/**
 * ConnectedAssetLineage is the open metadata implementation of the Open Connector Framework (OCF)'s AssetLineage
 * object.  It returns information about an asset's lineage.
 */
public class ConnectedAssetLineage extends AssetLineage
{
    private static final long    serialVersionUID = 1L;

    private String                 serviceName;
    private String                 serverName;
    private String                 userId;
    private String                 omasServerURL;
    private String                 assetGUID;
    private ConnectedAssetUniverse connectedAsset;
    private int                    maxCacheSize;
    private OCFRESTClient          restClient;


    /**
     * Typical constructor creates an AssetLineage object primed with information to retrieve an asset's lineage
     * information.
     *
     * @param serviceName calling service
     * @param serverName name of server to use on server calls.
     * @param userId user id to use on server calls.
     * @param omasServerURL url root of the server to use.
     * @param assetGUID unique identifier of the asset.
     * @param parentAsset descriptor of parent asset.
     * @param maxCacheSize maximum number of elements that should be retrieved from the property server and
     *                     cached in the element list at any one time.  If a number less than one is supplied, 1 is used.
     * @param restClient client to call REST API
     */
    ConnectedAssetLineage(String                 serviceName,
                          String                 serverName,
                          String                 userId,
                          String                 omasServerURL,
                          String                 assetGUID,
                          ConnectedAssetUniverse parentAsset,
                          int                    maxCacheSize,
                          OCFRESTClient          restClient)
    {
        super(parentAsset);

        this.serviceName    = serviceName;
        this.serverName     = serverName;
        this.userId         = userId;
        this.omasServerURL  = omasServerURL;
        this.assetGUID      = assetGUID;
        this.connectedAsset = parentAsset;
        this.maxCacheSize   = maxCacheSize;
        this.restClient     = restClient;
    }


    /**
     * Copy/clone constructor.  Used to reset iterator element pointer to 0;
     *
     * @param parentAsset descriptor of parent asset
     * @param template type-specific iterator to copy; null to create an empty iterator
     */
    private ConnectedAssetLineage(ConnectedAssetUniverse parentAsset, ConnectedAssetLineage template)
    {
        super(parentAsset, template);

        if (template != null)
        {
            this.serviceName    = template.serviceName;
            this.serverName     = template.serverName;
            this.userId         = template.userId;
            this.omasServerURL  = template.omasServerURL;
            this.assetGUID      = template.assetGUID;
            this.connectedAsset = parentAsset;
            this.restClient     = template.restClient;
        }
    }
}
