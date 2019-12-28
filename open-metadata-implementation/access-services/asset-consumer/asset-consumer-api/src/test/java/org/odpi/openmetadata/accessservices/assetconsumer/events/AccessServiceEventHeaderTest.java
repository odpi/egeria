/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.assetconsumer.events;


import org.testng.annotations.Test;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * AccessServiceEventHeaderTest provides tests for the access service's event header
 */
public class AccessServiceEventHeaderTest
{
    /**
     * Test that the constructors run without error.
     */
    @Test
    public void testConstructors()
    {
        MockAccessServiceEventHeader  testObject = new MockAccessServiceEventHeader();

        new MockAccessServiceEventHeader(testObject);
        new MockAccessServiceEventHeader(null);
    }


    /**
     * Test that the event version id is not null.
     */
    @Test public void testEventVersionId()
    {
        MockAccessServiceEventHeader  testObject = new MockAccessServiceEventHeader();

        assertTrue(testObject.getEventVersionId() != 0);
    }


    /**
     * Test that toString is implemented.
     */
    @Test public void testToString()
    {
        MockAccessServiceEventHeader  testObject = new MockAccessServiceEventHeader();

        assertTrue(testObject.toString().contains("AssetConsumerEvent"));
    }


    /**
     * Test that hashcode ans equals are working.
     */
    @Test public void testHashCode()
    {
        MockAccessServiceEventHeader  testObject = new MockAccessServiceEventHeader();
        MockAccessServiceEventHeader  testObjectClone = new MockAccessServiceEventHeader(testObject);
        MockAccessServiceEventHeader  testObjectDiff = new MockAccessServiceEventHeader();

        testObjectDiff.setEventVersionId(8L);

        assertTrue(testObject.hashCode() == testObjectClone.hashCode());
        assertFalse(testObject.hashCode() == testObjectDiff.hashCode());

        assertTrue(testObject.equals(testObject));
        assertTrue(testObject.equals(testObjectClone));
        assertFalse(testObject.equals(testObjectDiff));
        assertFalse(testObject.equals("TestString"));
    }
}
