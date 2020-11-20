/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.repositoryservices.rest.properties;

import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;

/**
 * Test the overridden methods of OMRSRESTAPIResponse
 */
public class TestOMRSAPIResponse
{
    @Test(enabled=false) public void TestToString()
    {
        assertTrue(new MockAPIResponse().toString().contains("OMRSRESTAPIResponse"));

    }
}
