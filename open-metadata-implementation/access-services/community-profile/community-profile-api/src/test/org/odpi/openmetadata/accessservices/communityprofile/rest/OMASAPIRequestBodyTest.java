/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.rest;

import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;

/**
 * Test the overridden methods of CommunityProfileOMASAPIRequestBody
 */
public class OMASAPIRequestBodyTest
{
    @Test public void TestToString()
    {
        MockAPIRequestBody  testObject = new MockAPIRequestBody();

        assertTrue(testObject.toString().contains("CommunityProfileOMASAPIRequestBody"));
        assertTrue(new MockAPIRequestBody(testObject).toString().contains("CommunityProfileOMASAPIRequestBody"));
    }
}
