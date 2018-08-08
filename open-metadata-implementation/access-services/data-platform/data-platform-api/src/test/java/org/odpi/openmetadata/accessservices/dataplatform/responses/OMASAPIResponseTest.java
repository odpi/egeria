/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.dataplatform.responses;

import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;

/**
 * Test the overridden methods of DataPlatformOMASAPIResponse
 */
public class OMASAPIResponseTest
{
    @Test public void TestToString()
    {
        MockAPIResponse  testObject = new MockAPIResponse();

        assertTrue(testObject.toString().contains("DataPlatformOMASAPIResponse"));
        assertTrue(new MockAPIResponse(testObject).toString().contains("DataPlatformOMASAPIResponse"));

    }
}
