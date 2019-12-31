/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

/**
 * ExternalStandardMappingTest provides test of ExternalStandardMapping
 */
public class ExternalStandardMappingTest
{
    private   String standardName = "TestStandardName";
    private   String standardOrganization = "TestStandardOrg";
    private   String standardTypeName = "TestStandTypeName";


    /**
     * Return a filled in test object
     *
     * @return test object
     */
    private ExternalStandardMapping   getTestObject()
    {
        ExternalStandardMapping testObject = new ExternalStandardMapping();

        testObject.setStandardName(standardName);
        testObject.setStandardOrganization(standardOrganization);
        testObject.setStandardTypeName(standardTypeName);

        return testObject;
    }


    /**
     * Validate supplied object.
     *
     * @param testObject object to test
     */
    private void validateObject(ExternalStandardMapping   testObject)
    {
        assertEquals(testObject.getStandardName(), standardName);
        assertEquals(testObject.getStandardOrganization(), standardOrganization);
        assertEquals(testObject.getStandardTypeName(), standardTypeName);
    }


    /**
     * Validate that the constructors set up the correct properties
     */
    @Test public void testConstructors()
    {
        ExternalStandardMapping testObject = new ExternalStandardMapping();

        assertNull(testObject.getStandardName());
        assertNull(testObject.getStandardOrganization());
        assertNull(testObject.getStandardTypeName());

        validateObject(new ExternalStandardMapping(getTestObject()));
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
        catch (Throwable  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }

        try
        {
            validateObject(objectMapper.readValue(jsonString, ExternalStandardMapping.class));
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
        assertTrue(getTestObject().toString().contains("ExternalStandardMapping"));
    }


    /**
     * Test that equals works
     */
    @Test public void testEquals()
    {
        assertTrue(getTestObject().equals(getTestObject()));

        ExternalStandardMapping testObject = getTestObject();

        assertTrue(testObject.equals(testObject));
        assertTrue(testObject.equals(getTestObject()));

        assertFalse(getTestObject().equals(null));
        assertFalse(getTestObject().equals("A String"));

        ExternalStandardMapping  differentObject = getTestObject();
        differentObject.setStandardTypeName("AnotherValue");

        assertFalse(testObject.equals(differentObject));
    }


    /**
     * Test that hashcode is consistent
     */
    @Test public void testHash()
    {
        assertTrue(getTestObject().hashCode() == getTestObject().hashCode());

        ExternalStandardMapping testObject = getTestObject();
        ExternalStandardMapping anotherObject = getTestObject();
        anotherObject.setStandardName("Blah");

        assertNotEquals(testObject.hashCode(), anotherObject.hashCode());
    }
}
