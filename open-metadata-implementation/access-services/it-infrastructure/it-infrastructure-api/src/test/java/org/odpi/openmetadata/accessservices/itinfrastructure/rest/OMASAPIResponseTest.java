/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.itinfrastructure.rest;

import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;

/**
 * Test the overridden methods of ITInfrastructureOMASAPIResponse
 */
public class OMASAPIResponseTest
{
    @Test public void TestToString()
    {
        MockAPIResponse  testObject = new MockAPIResponse();

        assertTrue(testObject.toString().contains("ITInfrastructureOMASAPIResponse"));
        assertTrue(new MockAPIResponse(testObject).toString().contains("ITInfrastructureOMASAPIResponse"));
    }
}
