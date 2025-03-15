/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static org.testng.Assert.*;

/**
 * TypeDefPropertiesTest provides test of TypeDefProperties
 */
public class TypeDefPropertiesTest
{
    private Map<String, Object> typeDefProperties = new HashMap<>();


    /**
     * Constructor to set up complex attributes
     */
    public TypeDefPropertiesTest()
    {
        typeDefProperties.put("TestPropertyName", "TestPropertyValue");
    }

    /**
     * Return a filled in test object
     *
     * @return test object
     */
    private TypeDefProperties   getTestObject()
    {
        TypeDefProperties testObject = new TypeDefProperties();

        testObject.setTypeDefProperties(typeDefProperties);

        return testObject;
    }


    private void validateObject(TypeDefProperties   testObject)
    {
        assertEquals(testObject.getTypeDefProperties(), typeDefProperties);
    }

    private void validateNullObject(TypeDefProperties   testObject)
    {
        assertNull(testObject.getTypeDefProperties());
    }



    /**
     * Validate that the constructors set up the attributes correctly.
     */
    @Test public void testConstructors()
    {
        validateNullObject(new TypeDefProperties());
        validateNullObject(new TypeDefProperties(new TypeDefProperties()));

        validateObject(new TypeDefProperties(getTestObject()));

        TypeDefProperties testObject = new TypeDefProperties();
        testObject.setTypeDefProperties(new HashMap<>());
        validateNullObject(testObject);
    }


    /**
     * Validate that an object generated from a JSON String has the same content as the object used to
     * create the JSON String.
     */
    @Test public void testJSON()
    {
        ObjectMapper objectMapper = new ObjectMapper();
        String       jsonString   = null;

        try
        {
            jsonString = objectMapper.writeValueAsString(getTestObject());
        }
        catch (Exception   exc)
        {
            fail("Exception: " + exc.getMessage());
        }

        try
        {
            validateObject(objectMapper.readValue(jsonString, TypeDefProperties.class));
        }
        catch (Exception   exc)
        {
            fail("Exception: " + exc.getMessage());
        }
    }


    /**
     * Test that toString is overridden.
     */
    @Test public void testToString()
    {
        assertTrue(getTestObject().toString().contains("TypeDefProperties"));
    }


    /**
     * Test that equals works
     */
    @Test public void testEquals()
    {
        assertTrue(getTestObject().equals(getTestObject()));

        TypeDefProperties testObject = getTestObject();

        assertTrue(testObject.equals(testObject));

        assertFalse(getTestObject().equals(null));
        assertFalse(getTestObject().equals("AString"));
        assertFalse(getTestObject().equals(new CollectionDef()));
    }


    /**
     * Test that hashcode is consistent
     */
    @Test public void testHash()
    {
        assertEquals(getTestObject().hashCode(), getTestObject().hashCode());

        TypeDefProperties testObject = getTestObject();
        TypeDefProperties anotherObject = getTestObject();

        Map<String, Object>  differentProperties = new HashMap<>();

        differentProperties.put("DifferentPropertyName", "DifferentPropertyValue");
        anotherObject.setTypeDefProperties(differentProperties);

        assertNotEquals(testObject.hashCode(), anotherObject.hashCode());
    }
}
