/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.testng.annotations.Test;

import java.util.*;

import static org.testng.Assert.*;

/**
 * EnumElementDefTest provides test of EnumElementDef
 */
public class EnumElementDefTest
{
    private int    ordinal         = 99;
    private String value           = "TestValue";
    private String description     = "TestDescription";
    private String descriptionGUID = "TestDescriptionGUID";


    /**
     * Return a filled in test object
     *
     * @return test object
     */
    private EnumElementDef   getTestObject()
    {
        EnumElementDef testObject = new EnumElementDef();

        testObject.setOrdinal(ordinal);
        testObject.setValue(value);
        testObject.setDescription(description);
        testObject.setDescriptionGUID(descriptionGUID);

        return testObject;
    }


    /**
     * Validate supplied object.
     *
     * @param testObject object to test
     */
    private void validateObject(EnumElementDef   testObject)
    {
        assertEquals(testObject.getOrdinal(),ordinal);
        assertEquals(testObject.getValue(), value);
        assertEquals(testObject.getDescription(), description);
        assertEquals(testObject.getDescriptionGUID(), descriptionGUID);
    }


    /**
     * Validate that the constructors set up the correct properties
     */
    @Test public void testConstructors()
    {
        EnumElementDef testObject = new EnumElementDef();

        assertEquals(testObject.getOrdinal(), 99);
        assertNull(testObject.getValue());
        assertNull(testObject.getDescription());
        assertNull(testObject.getDescriptionGUID());

        validateObject(new EnumElementDef(getTestObject()));
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
            assertTrue(false, "Exception: " + exc.getMessage());
        }

        try
        {
            validateObject(objectMapper.readValue(jsonString, EnumElementDef.class));
        }
        catch (Exception   exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }
    }


    /**
     * Test that toString is overridden.
     */
    @Test public void testToString()
    {
        assertTrue(getTestObject().toString().contains("EnumElementDef"));
    }


    /**
     * Test that equals works
     */
    @Test public void testEquals()
    {
        assertTrue(getTestObject().equals(getTestObject()));

        EnumElementDef testObject = getTestObject();

        assertTrue(testObject.equals(testObject));
        assertTrue(testObject.equals(getTestObject()));

        assertFalse(getTestObject().equals(null));
        assertFalse(getTestObject().equals("A String"));

        EnumElementDef  differentObject = getTestObject();
        differentObject.setValue("AnotherValue");

        assertFalse(testObject.equals(differentObject));
    }


    /**
     * Test that hashcode is consistent
     */
    @Test public void testHash()
    {
        assertTrue(getTestObject().hashCode() == getTestObject().hashCode());

        EnumElementDef testObject = getTestObject();
        EnumElementDef anotherObject = getTestObject();
        anotherObject.setOrdinal(88);

        assertNotEquals(testObject.hashCode(), anotherObject.hashCode());
    }
}
