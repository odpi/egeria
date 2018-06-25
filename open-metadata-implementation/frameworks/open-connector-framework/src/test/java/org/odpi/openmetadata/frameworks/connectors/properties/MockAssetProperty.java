/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.frameworks.connectors.properties;

/**
 * MockAssetProperty is a concrete class for testing AssetPropertyBase
 */
public class MockAssetProperty extends AssetPropertyBase
{
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
