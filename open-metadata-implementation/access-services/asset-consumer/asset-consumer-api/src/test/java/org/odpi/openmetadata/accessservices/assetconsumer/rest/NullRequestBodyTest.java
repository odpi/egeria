/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetconsumer.rest;

import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;

/**
 * Test the overridden methods of NullRequestBody
 */
public class NullRequestBodyTest
{
    @Test public void TestToString()
    {
        NullRequestBody  testObject = new NullRequestBody();

        assertTrue(testObject.toString().contains("NullRequestBody"));
        assertTrue(new NullRequestBody(testObject).toString().contains("NullRequestBody"));
    }
}
