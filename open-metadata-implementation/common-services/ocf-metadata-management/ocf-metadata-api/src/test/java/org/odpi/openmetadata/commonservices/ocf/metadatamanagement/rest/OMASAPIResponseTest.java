/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ocf.metadatamanagement.rest;

import org.testng.annotations.Test;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * Test the overridden methods of OCFOMASAPIResponse
 */
public class OMASAPIResponseTest
{
    private OCFOMASAPIResponse getTestObject()
    {
        return new MockAPIResponse();
    }


    @Test public void TestToString()
    {
        OCFOMASAPIResponse testObject = this.getTestObject();

        assertTrue(testObject.toString().contains("OCFOMASAPIResponse"));
        assertTrue(new MockAPIResponse(testObject).toString().contains("OCFOMASAPIResponse"));
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

        OCFOMASAPIResponse sameObject = getTestObject();
        assertTrue(sameObject.equals(sameObject));

        OCFOMASAPIResponse differentObject = getTestObject();
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
