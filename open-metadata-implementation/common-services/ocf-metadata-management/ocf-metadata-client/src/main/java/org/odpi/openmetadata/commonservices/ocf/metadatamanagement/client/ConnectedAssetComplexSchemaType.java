/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ocf.metadatamanagement.client;

import org.odpi.openmetadata.frameworks.connectors.properties.AssetComplexSchemaType;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ComplexSchemaType;

class ConnectedAssetComplexSchemaType extends AssetComplexSchemaType
{
    private static final long    serialVersionUID = 1L;

    /**
     * Typical constructor creates an AssetComplexSchemaType object primed with information to retrieve an asset's schema
     * information.
     *
     * @param serviceName calling service
     * @param serverName  name of the server.
     * @param omasServerURL url root of the server to use.
     * @param userId user id to use on server calls.
     * @param parentAsset descriptor of parent asset.
     * @param maxCacheSize maximum number of elements that should be retrieved from the property server and
     *                     cached in the element list at any one time.  If a number less than one is supplied, 1 is used.
     * @param schemaBean details of the schema object.
     * @param restClient client to call REST API
     */
    ConnectedAssetComplexSchemaType(String                 serviceName,
                                    String                 serverName,
                                    String                 omasServerURL,
                                    String                 userId,
                                    ConnectedAssetUniverse parentAsset,
                                    int                    maxCacheSize,
                                    ComplexSchemaType      schemaBean,
                                    OCFRESTClient          restClient)
    {
        super(parentAsset);

        super.setBean(schemaBean);

        if (schemaBean != null)
        {
            int  schemaAttributeCount = schemaBean.getAttributeCount();

            if (schemaAttributeCount > 0)
            {
                super.schemaAttributes = new ConnectedAssetSchemaAttributes(serviceName,
                                                                            serverName,
                                                                            userId,
                                                                            omasServerURL,
                                                                            schemaBean.getGUID(),
                                                                            parentAsset,
                                                                            maxCacheSize,
                                                                            schemaAttributeCount,
                                                                            restClient);

            }
        }
    }
}
