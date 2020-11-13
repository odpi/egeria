/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.digitalarchitecture.rest;

import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;

/**
 * Test the overridden methods of DigitalArchitectureOMASAPIResponse
 */
public class OMASAPIResponseTest
{
    @Test public void TestToString()
    {
        MockAPIResponse  testObject = new MockAPIResponse();

        assertTrue(testObject.toString().contains("DigitalArchitectureOMASAPIResponse"));
        assertTrue(new MockAPIResponse(testObject).toString().contains("DigitalArchitectureOMASAPIResponse"));
    }
}
