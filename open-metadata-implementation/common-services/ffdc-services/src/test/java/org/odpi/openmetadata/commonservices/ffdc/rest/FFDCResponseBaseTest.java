/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ffdc.rest;

import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;

/**
 * Test the overridden methods of FFDCResponseBase
 */
public class FFDCResponseBaseTest
{
    @Test public void TestToString()
    {
        MockAPIResponse  testObject = new MockAPIResponse();

        assertTrue(testObject.toString().contains("FFDCResponseBase"));
        assertTrue(new MockAPIResponse(testObject).toString().contains("FFDCResponseBase"));

    }
}
