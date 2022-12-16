/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.test.unittest.utilities;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;

import static org.testng.Assert.*;

/**
 * FFDCResponseBasedTest is
 */
public class BeanTestBase
{
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final ObjectReader OBJECT_READER = OBJECT_MAPPER.reader();
    private static final ObjectWriter OBJECT_WRITER = OBJECT_MAPPER.writer();

    /**
     * Validate that 2 different objects with the same content are evaluated as equal.
     * Also that different objects are considered not equal.
     *
     * @param testObject filled out bean
     * @param clonedObject bean cloned from the test object
     * @param differentObject bean filled out differently
     * @param className simple name
     * @param <T> class marker
     */
    private <T> void testStringEqualsHashcode(T      testObject,
                                              T      clonedObject,
                                              T      differentObject,
                                              String className)
    {
        assertNotEquals(testObject, null);
        assertNotEquals(clonedObject, null);
        assertNotEquals(differentObject, null);

        assertNotEquals(testObject, "DummyString");
        assertEquals(clonedObject, testObject);

        assertEquals(clonedObject, clonedObject);
        assertNotEquals(testObject, differentObject);

        assertEquals(testObject.hashCode(), testObject.hashCode());
        assertEquals(testObject.hashCode(), clonedObject.hashCode());
        assertNotEquals(testObject.hashCode(), differentObject.hashCode());

        assertTrue(testObject.toString().contains(className));
    }



    /**
     * Validate that an object generated from a JSON String has the same content as the object used to
     * create the JSON String.
     *
     * @param testObject object to test
     * @param testObjectClass object's class
     * @param <T> class type
     * @return resulting object.
     */
    protected <T>T testJSON(T   testObject, Class<T> testObjectClass)
    {
        String       jsonString   = null;

        /*
         * This class
         */
        try
        {
            jsonString = OBJECT_WRITER.writeValueAsString(testObject);
        }
        catch (Throwable  exc)
        {
            fail("Exception: " + exc.getMessage());
        }

        try
        {
            return OBJECT_READER.readValue(jsonString, testObjectClass);
        }
        catch (Throwable  exc)
        {
            fail("Exception: " + exc.getMessage());
        }

        return null;
    }


    /**
     * Test a Bean
     *
     * @param testObject filled out bean
     * @param clonedObject bean cloned from the test object
     * @param differentObject bean filled out differently
     * @param testObjectClass class of the bean
     * @param <T> class marker
     * @return the test object after it has been pushed in and out through JSON.
     */
    public <T> T testResponseObject(T         testObject,
                                    T         clonedObject,
                                    T         differentObject,
                                    Class<T>  testObjectClass)
    {
        testStringEqualsHashcode(testObject, clonedObject, differentObject, testObjectClass.getSimpleName());

        return testJSON(testObject, testObjectClass);
    }
}
