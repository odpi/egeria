/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties;

/**
 * MockAssetPropertyElement is a concrete class for testing AssetPropertyElementBase
 */
public class MockAssetPropertyElement extends AssetPropertyElementBase
{
    /**
     * Default constructor
     */
    public MockAssetPropertyElement()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template - mock asset property to clone
     */
    public MockAssetPropertyElement(AssetPropertyElementBase template)
    {
        super(template);
    }
}
