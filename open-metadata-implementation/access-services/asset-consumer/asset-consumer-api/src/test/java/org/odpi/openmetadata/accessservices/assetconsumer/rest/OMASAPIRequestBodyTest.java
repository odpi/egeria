/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.assetconsumer.rest;

import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;

/**
 * Test the overridden methods of AssetConsumerOMASAPIRequestBody
 */
public class OMASAPIRequestBodyTest
{
    @Test public void TestToString()
    {
        MockAPIRequestBody  testObject = new MockAPIRequestBody();

        assertTrue(testObject.toString().contains("AssetConsumerOMASAPIRequestBody"));
        assertTrue(new MockAPIRequestBody(testObject).toString().contains("AssetConsumerOMASAPIRequestBody"));
    }
}
