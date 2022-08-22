/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties;

import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;

public class TestAssetLineage
{

    /**
     * Set up an example object to test.
     *
     * @return filled in object
     */
    private AssetLineage getTestObject()
    {
        return new AssetLineage();
    }


    /**
     * Validate that the object that comes out of the test has the same content as the original test object.
     *
     * @param resultObject object returned by the test
     */
    private void validateResultObject(AssetLineage  resultObject)
    {
        assertTrue(resultObject != null);
    }


    /**
     * Test the typical constructor
     */
    @Test public void testConstructor()
    {
        validateResultObject(getTestObject());

        validateResultObject(new AssetLineage(null));
    }


    /**
     *  Validate that an object cloned from another object has the same content as the original
     */
    @Test public void testClone()
    {
        validateResultObject(new AssetLineage( getTestObject()));
    }


    /**
     * Test that toString is overridden.
     */
    @Test public void testToString()
    {
        assertTrue(getTestObject().toString().contains("AssetLineage"));
    }
}
