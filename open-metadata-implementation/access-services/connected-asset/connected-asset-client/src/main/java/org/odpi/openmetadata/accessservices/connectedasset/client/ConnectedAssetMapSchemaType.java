/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.connectedasset.client;

import org.odpi.openmetadata.frameworks.connectors.properties.AssetMapSchemaType;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.MapSchemaType;

public class ConnectedAssetMapSchemaType extends AssetMapSchemaType
{
    /**
     * Typical constructor creates an AssetSchemaType object primed with information to retrieve an asset's schema
     * information.
     *
     * @param userId user id to use on server calls.
     * @param connectedAsset descriptor of parent asset.
     * @param mapSchemaTypeBean bean describing the map.
     */
    ConnectedAssetMapSchemaType(ConnectedAsset        connectedAsset,
                                String                userId,
                                MapSchemaType         mapSchemaTypeBean)
    {
        super(connectedAsset);

        super.setBean(mapSchemaTypeBean);
        super.mapFromElement = connectedAsset.getAssetSchemaType(userId, mapSchemaTypeBean.getMapFromElement());
        super.mapToElement = connectedAsset.getAssetSchemaType(userId, mapSchemaTypeBean.getMapToElement());
    }
}
