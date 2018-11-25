/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.connectedasset.client;

import org.odpi.openmetadata.frameworks.connectors.properties.AssetDescriptor;
import org.odpi.openmetadata.frameworks.connectors.properties.AssetSchemaType;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.SchemaType;

public abstract class ConnectedAssetSchemaType extends AssetSchemaType
{
    protected String         serverName;
    protected String         userId;
    protected String         omasServerURL;
    protected String         assetGUID;
    protected ConnectedAsset connectedAsset;
    protected int            maxCacheSize;


    /**
     * Typical constructor creates an AssetSchemaType object primed with information to retrieve an asset's schema
     * information.
     *
     * @param serverName  name of the server.
     * @param userId user id to use on server calls.
     * @param omasServerURL url root of the server to use.
     * @param assetGUID unique identifier of the asset.
     * @param parentAsset descriptor of parent asset.
     * @param maxCacheSize maximum number of elements that should be retrieved from the property server and
     *                     cached in the element list at any one time.  If a number less than one is supplied, 1 is used.
     */
    ConnectedAssetSchemaType(String                serverName,
                             String                userId,
                             String                omasServerURL,
                             String                assetGUID,
                             ConnectedAsset        parentAsset,
                             int                   maxCacheSize)
    {
        super(parentAsset);

        this.userId = userId;
        this.omasServerURL = omasServerURL;
        this.assetGUID = assetGUID;
        this.connectedAsset = parentAsset;
        this.maxCacheSize = maxCacheSize;
    }


    /**
     * Return a clone of this schema element.  This method is needed because AssetSchemaType
     * is abstract.
     *
     * @param parentAsset description of the asset that this schema element is attached to.
     * @return An instance of the this object's subclass
     */
    protected abstract AssetSchemaType cloneAssetSchemaType(AssetDescriptor parentAsset);


    /**
     * Return this schema element bean.  This method is needed because SchemaType
     * is abstract.
     *
     * @return An instance of the appropriate subclass of SchemaElement bean
     */
    protected abstract SchemaType getSchemaTypeBean();
}
