/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.connectedasset.client;

import org.odpi.openmetadata.frameworks.connectors.properties.AssetBoundedSchemaType;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.BoundedSchemaType;

public class ConnectedAssetBoundedSchemaType extends AssetBoundedSchemaType
{
    /**
     * Typical constructor creates an AssetComplexSchemaType object primed with information to retrieve an asset's schema
     * information.
     *
     * @param serverName  name of the server.
     * @param omasServerURL url root of the server to use.
     * @param userId user id to use on server calls.
     * @param parentAsset descriptor of parent asset.
     * @param schemaBean details of the schema object.
     * @param restClient client to call REST API
     */
    ConnectedAssetBoundedSchemaType(String                 serverName,
                                    String                 omasServerURL,
                                    String                 userId,
                                    ConnectedAssetUniverse parentAsset,
                                    BoundedSchemaType      schemaBean,
                                    RESTClient             restClient)
    {
        super(parentAsset);
        super.setBean(schemaBean);
        super.boundedSchemaElementType = parentAsset.getAssetSchemaType(serverName, omasServerURL, userId, schemaBean.getElementType(), restClient);
    }
}
