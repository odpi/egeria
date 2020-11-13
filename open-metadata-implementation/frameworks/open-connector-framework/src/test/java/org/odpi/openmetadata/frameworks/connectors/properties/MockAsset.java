/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.Asset;

/**
 * Mock asset provides a concrete class to test AssetDescriptor
 */
public class MockAsset extends AssetDescriptor
{
    private static final long     serialVersionUID = 1L;

    /**
     * Default constructor
     */
    public MockAsset()
    {
        super();
    }


    /**
     * Bean constructor
     *
     * @param assetBean - asset bean with all the properties
     */
    public MockAsset(Asset assetBean)
    {
        super(assetBean);
    }


    /**
     * Copy/clone constructor
     *
     * @param templateAssetDescriptor template to copy
     */
    public MockAsset(AssetDescriptor templateAssetDescriptor)
    {
        super(templateAssetDescriptor);
    }
}
