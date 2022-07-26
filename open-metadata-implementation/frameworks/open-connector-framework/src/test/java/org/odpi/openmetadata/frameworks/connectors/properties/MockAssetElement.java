/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties;


import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementBase;

/**
 * MockAssetElement provides the
 */
public class MockAssetElement extends AssetElementHeader
{
    private static final long     serialVersionUID = 1L;

    /**
     * Bean constructor
     *
     * @param elementBaseBean bean with all the properties
     */
    public MockAssetElement(ElementBase elementBaseBean)
    {
        super(elementBaseBean);
    }


    /**
     * Bean constructor with parent asset
     *
     * @param parentAsset       descriptor for parent asset
     * @param elementBaseBean bean containing properties
     */
    protected MockAssetElement(AssetDescriptor parentAsset,
                               ElementBase elementBaseBean)
    {
        super(parentAsset, elementBaseBean);
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
