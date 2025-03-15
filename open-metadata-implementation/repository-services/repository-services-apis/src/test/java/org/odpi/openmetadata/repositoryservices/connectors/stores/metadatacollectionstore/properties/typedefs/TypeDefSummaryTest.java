/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

/**
 * TypeDefSummaryTest provides test of TypeDefSummary
 */
public class TypeDefSummaryTest
{
    protected String          guid               = "TestGUID";
    protected String          name               = "TestName";
    protected TypeDefStatus   status             = TypeDefStatus.RENAMED_TYPEDEF;
    protected String          replacedByTypeGUID = "TestReplacedByGUID";
    protected String          replacedByTypeName = "TestReplacedByName";
    protected long            version            = 6;
    protected String          versionName        = "TestVersionName";
    protected TypeDefCategory category           = TypeDefCategory.CLASSIFICATION_DEF;



    public TypeDefSummaryTest()
    {
    }


    /**
     * Return a filled in test object
     *
     * @return test object
     */
    private TypeDefSummary   getTestObject()
    {
        TypeDefSummary testObject = new TypeDefSummary();

        testObject.setGUID(guid);
        testObject.setName(name);
        testObject.setStatus(status);
        testObject.setReplacedByTypeGUID(replacedByTypeGUID);
        testObject.setReplacedByTypeName(replacedByTypeName);
        testObject.setVersion(version);
        testObject.setVersionName(versionName);
        testObject.setCategory(category);

        return testObject;
    }


    /**
     * Validate supplied object.
     *
     * @param testObject object to test
     */
    private void validateObject(TypeDefSummary   testObject)
    {
        assertTrue(testObject.getGUID().equals(guid));
        assertTrue(testObject.getName().equals(name));
        assertTrue(testObject.getStatus().equals(status));
        assertTrue(testObject.getReplacedByTypeGUID().equals(replacedByTypeGUID));
        assertTrue(testObject.getReplacedByTypeName().equals(replacedByTypeName));
        assertTrue(testObject.getVersion() == version);
        assertTrue(testObject.getVersionName().equals(versionName));
        assertTrue(testObject.getCategory().equals(category));
    }


    /**
     * Validate that the constructors set up the correct properties
     */
    @Test public void testConstructors()
    {
        TypeDefSummary testObject = new TypeDefSummary();

        assertNull(testObject.getGUID());
        assertNull(testObject.getName());
        assertNull(testObject.getStatus());
        assertNull(testObject.getReplacedByTypeGUID());
        assertNull(testObject.getReplacedByTypeName());
        assertTrue(testObject.getVersion() == 0);
        assertNull(testObject.getVersionName());
        assertTrue(testObject.getCategory() == TypeDefCategory.UNKNOWN_DEF);

        TypeDefSummary anotherTestObject = getTestObject();

        validateObject(new TypeDefSummary(anotherTestObject));
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
            validateObject(objectMapper.readValue(jsonString, TypeDefSummary.class));
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
        assertTrue(getTestObject().toString().contains("TypeDefSummary"));
    }


    /**
     * Test that equals works
     */
    @Test public void testEquals()
    {
        assertTrue(getTestObject().equals(getTestObject()));

        TypeDefSummary testObject = getTestObject();

        assertTrue(testObject.equals(testObject));

        assertFalse(getTestObject().equals(null));
        assertFalse(getTestObject().equals("A String"));

        TypeDefSummary  differentObject = new TypeDefSummary(TypeDefCategory.CLASSIFICATION_DEF, guid, name, 0 , "1.0");
        
        assertFalse(testObject.equals(differentObject));

        differentObject = getTestObject();

        differentObject.setVersionName("DifferentHomeId");

        assertFalse(testObject.equals(differentObject));
    }


    /**
     * Test that hashcode is consistent
     */
    @Test public void testHash()
    {
        assertTrue(getTestObject().hashCode() == getTestObject().hashCode());

        TypeDefSummary testObject = getTestObject();
        TypeDefSummary anotherObject = getTestObject();
        anotherObject.setGUID("DifferentAuthor");

        assertFalse(testObject.hashCode() == anotherObject.hashCode());
    }
}
