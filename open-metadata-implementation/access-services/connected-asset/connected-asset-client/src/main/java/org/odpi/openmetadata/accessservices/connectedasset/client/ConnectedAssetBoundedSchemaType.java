/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.connectedasset.client;

import org.odpi.openmetadata.frameworks.connectors.properties.AssetBoundedSchemaType;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.BoundedSchemaType;

public class ConnectedAssetBoundedSchemaType extends AssetBoundedSchemaType
{
    /**
     * Typical constructor creates an AssetSchema object primed with information to retrieve an asset's schema
     * information.
     *
     * @param userId user id to use on server calls.
     * @param parentAsset descriptor of parent asset.
     * @param schemaBean details of the schema object.
     */
    ConnectedAssetBoundedSchemaType(ConnectedAssetUniverse parentAsset,
                                    String                userId,
                                    BoundedSchemaType     schemaBean)
    {
        super(parentAsset);
        super.setBean(schemaBean);
        super.boundedSchemaElementType = parentAsset.getAssetSchemaType(userId, schemaBean.getElementType());
    }
}
