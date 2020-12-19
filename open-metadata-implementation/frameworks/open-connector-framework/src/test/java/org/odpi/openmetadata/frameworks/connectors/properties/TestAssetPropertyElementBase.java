/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties;


import org.testng.annotations.Test;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * Tests the getters and setters of AssetPropertyElementBase.  This is an abstract class and so
 * it uses the MockAssetPropertyElement object as the concrete class.
 */
public class TestAssetPropertyElementBase
{
    /**
     * Constructor
     */
    public TestAssetPropertyElementBase()
    {
    }


    /**
     * Test that the hash code is populated and remains the same when objects are cloned.
     */
    @Test public void testHashCode()
    {
        AssetPropertyElementBase  testTemplate = new MockAssetPropertyElement();

        assertTrue(testTemplate.hashCode() != 0);

        AssetPropertyElementBase  testObject = new MockAssetPropertyElement(testTemplate);

        assertTrue(testObject.hashCode() != 0);
        assertTrue(testObject.hashCode() == testTemplate.hashCode());
    }


    /**
     * Test paths through the equals method
     */
    @Test public void testEquals()
    {
        AssetPropertyElementBase testObject = new MockAssetPropertyElement();
        AssetPropertyElementBase clonedObject = new MockAssetPropertyElement(testObject);


        assertFalse(testObject.equals("String Value"));
        assertTrue(testObject.equals(testObject));
        assertTrue(testObject.equals(clonedObject));

    }
}
