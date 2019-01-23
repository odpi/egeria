/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetconsumer.properties;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * Validate that the Tag bean can be cloned, compared, serialized, deserialized and printed as a String.
 */
public class TagTest
{
    private String                            guid                     = "TestGUID";
    private String                            name                     = "TestName";
    private String                            description              = "TestDescription";
    

    /**
     * Default constructor
     */
    public TagTest()
    {
    }


    /**
     * Set up an example object to test.
     *
     * @return filled in object
     */
    private Tag getTestObject()
    {
        Tag testObject = new Tag();

        testObject.setGUID(guid);
        testObject.setName(name);
        testObject.setDescription(description);

        return testObject;
    }


    /**
     * Validate that the object that comes out of the test has the same content as the original test object.
     *
     * @param resultObject object returned by the test
     */
    private void validateResultObject(Tag  resultObject)
    {
        assertTrue(resultObject.getGUID().equals(guid));
        assertTrue(resultObject.getName().equals(name));
        assertTrue(resultObject.getDescription().equals(description));
    }


    /**
     * Validate that the object is initialized properly
     */
    @Test public void testNullObject()
    {
        Tag    nullObject = new Tag();

        assertTrue(nullObject.getGUID() == null);
        assertTrue(nullObject.getName() == null);
        assertTrue(nullObject.getDescription() == null);

        nullObject = new Tag(null);

        assertTrue(nullObject.getGUID() == null);
        assertTrue(nullObject.getName() == null);
        assertTrue(nullObject.getDescription() == null);
    }


    /**
     * Validate that 2 different objects with the same content are evaluated as equal.
     * Also that different objects are considered not equal.
     */
    @Test public void testEquals()
    {
        assertFalse(getTestObject().equals(null));
        assertFalse(getTestObject().equals("DummyString"));
        assertTrue(getTestObject().equals(getTestObject()));

        Tag  sameObject = getTestObject();
        assertTrue(sameObject.equals(sameObject));

        Tag  differentObject = getTestObject();
        differentObject.setDescription("Different");
        assertFalse(getTestObject().equals(differentObject));

        differentObject = getTestObject();
        differentObject.setName("Different");
        assertFalse(getTestObject().equals(differentObject));
    }


    /**
     *  Validate that 2 different objects with the same content have the same hash code.
     */
    @Test public void testHashCode()
    {
        assertTrue(getTestObject().hashCode() == getTestObject().hashCode());

        Tag  testObject = getTestObject();

        assertTrue(testObject.hashCode() != 0);

        Tag  differentObject = getTestObject();

        differentObject.setDescription(null);

        assertFalse(testObject.hashCode() == differentObject.hashCode());

        differentObject = getTestObject();

        differentObject.setName(null);

        assertFalse(testObject.hashCode() == differentObject.hashCode());

        differentObject = getTestObject();

        differentObject.setGUID(null);

        assertFalse(testObject.hashCode() == differentObject.hashCode());
    }


    /**
     *  Validate that an object cloned from another object has the same content as the original
     */
    @Test public void testClone()
    {
        validateResultObject(new Tag(getTestObject()));
    }


    /**
     * Validate that an object generated from a JSON String has the same content as the object used to
     * create the JSON String.
     */
    @Test public void testJSON()
    {
        ObjectMapper objectMapper = new ObjectMapper();
        String       jsonString   = null;

        /*
         * This class
         */
        try
        {
            jsonString = objectMapper.writeValueAsString(getTestObject());
        }
        catch (Throwable  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }

        try
        {
            validateResultObject(objectMapper.readValue(jsonString, Tag.class));
        }
        catch (Throwable  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }

        /*
         * Through superclass
         */
        AssetConsumerElementHeader superObject = getTestObject();

        try
        {
            jsonString = objectMapper.writeValueAsString(superObject);
        }
        catch (Throwable  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }

        try
        {
            validateResultObject((Tag) objectMapper.readValue(jsonString, AssetConsumerElementHeader.class));
        }
        catch (Throwable  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }
    }


    /**
     * Test that toString is overridden.
     */
    @Test public void testToString()
    {
        assertTrue(getTestObject().toString().contains("Tag"));
    }
}
