/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties;


import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementHeader;

/**
 * MockAssetElement provides the
 */
public class MockAssetElement extends AssetElementHeader
{
    private static final long     serialVersionUID = 1L;

    /**
     * Bean constructor
     *
     * @param elementHeaderBean bean with all the properties
     */
    public MockAssetElement(ElementHeader elementHeaderBean)
    {
        super(elementHeaderBean);
    }


    /**
     * Bean constructor with parent asset
     *
     * @param parentAsset       descriptor for parent asset
     * @param elementHeaderBean bean containing properties
     */
    protected MockAssetElement(AssetDescriptor parentAsset,
                               ElementHeader   elementHeaderBean)
    {
        super(parentAsset, elementHeaderBean);
    }


    /**
     * Copy/clone constructor.
     *
     * @param parentAsset        descriptor for parent asset
     * @param assetElementHeader element to copy
     */
    protected MockAssetElement(AssetDescriptor  parentAsset,
                               MockAssetElement assetElementHeader)
    {
        super(parentAsset, assetElementHeader);

    }
}
