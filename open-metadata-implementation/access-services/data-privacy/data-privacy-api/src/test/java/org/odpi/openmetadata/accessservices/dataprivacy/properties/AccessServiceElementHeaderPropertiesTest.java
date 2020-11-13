/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataprivacy.properties;

import org.testng.annotations.Test;

/**
 * AccessServiceElementHeaderPropertiesTest provides a test of the access service's element header
 */
public class AccessServiceElementHeaderPropertiesTest
{
    @Test public void testConstructors()
    {
        MockAccessServiceElementHeader  testObject = new MockAccessServiceElementHeader();

        new MockAccessServiceElementHeader(testObject);
        new MockAccessServiceElementHeader(null);
    }
}
