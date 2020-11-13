/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties;

/**
 * MockAssetProperty is a concrete class for testing AssetPropertyBase
 */
public class MockAssetProperty extends AssetPropertyBase
{
    private static final long     serialVersionUID = 1L;

    /**
     * Typical constructor
     *
     * @param parentAsset - parent asset to provide asset name and type
     */
    public MockAssetProperty(AssetDescriptor parentAsset)
    {
        super(parentAsset);
    }


    /**
     * Copy/clone constructor
     *
     * @param parentAsset - parent asset to provide asset name and type
     * @param template - mock asst property to clone
     */
    public MockAssetProperty(AssetDescriptor parentAsset,
                             AssetPropertyBase template)
    {
        super(parentAsset, template);
    }
}
