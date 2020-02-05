/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.designmodel.responses;

import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;

/**
 * Test the overridden methods of DesignModelOMASAPIResponse
 */
public class OMASAPIResponseTest
{
    @Test public void TestToString()
    {
        MockAPIResponse  testObject = new MockAPIResponse();

        assertTrue(testObject.toString().contains("DesignModelOMASAPIResponse"));
        assertTrue(new MockAPIResponse(testObject).toString().contains("DesignModelOMASAPIResponse"));
    }
}
