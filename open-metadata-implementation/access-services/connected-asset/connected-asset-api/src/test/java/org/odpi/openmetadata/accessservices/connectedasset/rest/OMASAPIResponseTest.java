/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.connectedasset.rest;

import org.testng.annotations.Test;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * Test the overridden methods of ConnectedAssetOMASAPIResponse
 */
public class OMASAPIResponseTest
{
    private ConnectedAssetOMASAPIResponse getTestObject()
    {
        return new MockAPIResponse();
    }


    @Test public void TestToString()
    {
        ConnectedAssetOMASAPIResponse  testObject = this.getTestObject();

        assertTrue(testObject.toString().contains("ConnectedAssetOMASAPIResponse"));
        assertTrue(new MockAPIResponse(testObject).toString().contains("ConnectedAssetOMASAPIResponse"));
    }

    /**
     * Validate that 2 different objects with the same content are evaluated as equal.
     * Also that different objects are considered not equal.
     */
    @Test public void testEquals()
    {
        assertFalse(getTestObject().equals(null));
        assertFalse(getTestObject().equals("DummyString"));
        assertTrue(getTestObject().equals(getTestObject()));

        ConnectedAssetOMASAPIResponse  sameObject = getTestObject();
        assertTrue(sameObject.equals(sameObject));

        ConnectedAssetOMASAPIResponse  differentObject = getTestObject();
        differentObject.setExceptionErrorMessage("Different");
        assertFalse(getTestObject().equals(differentObject));
    }


    /**
     *  Validate that 2 different objects with the same content have the same hash code.
     */
    @Test public void testHashCode()
    {
        assertTrue(getTestObject().hashCode() == getTestObject().hashCode());
    }


}
