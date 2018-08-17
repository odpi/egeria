/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.assetowner.properties;

import org.testng.annotations.Test;

/**
 * AccessServiceElementHeaderTest provides a test of the access service's element header
 */
public class AccessServiceElementHeaderTest
{
    @Test public void testConstructors()
    {
        MockAccessServiceElementHeader  testObject = new MockAccessServiceElementHeader();

        new MockAccessServiceElementHeader(testObject);
        new MockAccessServiceElementHeader(null);
    }
}
