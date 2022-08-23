/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.Referenceable;

/**
 * Mock asset provides a concrete class to test AssetDescriptor
 */
public class MockAssetReferenceable extends AssetReferenceable
{
    private static final long     serialVersionUID = 1L;

    /**
     * Bean constructor
     *
     * @param referenceableBean - asset bean with all the properties
     */
    public MockAssetReferenceable(Referenceable referenceableBean)
    {
        super(referenceableBean);
    }


    /**
     * Copy/clone constructor
     *
     * @param template template to copy
     */
    public MockAssetReferenceable(MockAssetReferenceable template)
    {
        super(template);
    }
}
