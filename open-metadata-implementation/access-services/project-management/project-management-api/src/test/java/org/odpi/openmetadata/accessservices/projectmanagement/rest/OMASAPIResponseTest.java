/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.projectmanagement.rest;

import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;

/**
 * Test the overridden methods of ProjectManagementOMASAPIResponse
 */
public class OMASAPIResponseTest
{
    @Test public void TestToString()
    {
        assertTrue(new MockAPIResponse().toString().contains("ProjectManagementOMASAPIResponse"));
        MockAPIResponse  testObject = new MockAPIResponse();

        assertTrue(testObject.toString().contains("ProjectManagementOMASAPIResponse"));
        assertTrue(new MockAPIResponse(testObject).toString().contains("ProjectManagementOMASAPIResponse"));

    }
}
