/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.testng.annotations.Test;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * AttributeTypeDefTest provides test of AttributeTypeDef
 */
public class AttributeTypeDefTest
{
    protected long                     version         = 6L;
    protected String                   versionName     = "TestVersion";
    protected AttributeTypeDefCategory category        = AttributeTypeDefCategory.PRIMITIVE;
    protected String                   guid            = "TestGUID";
    protected String                   name            = "TestName";
    protected String                   description     = "TestDescription";
    protected String                   descriptionGUID = "TestDescriptionGUID";


    /**
     * Return a filled in test object
     *
     * @return test object
     */
    private AttributeTypeDef   getTestObject()
    {
        AttributeTypeDef testObject = new AttributeTypeDefMock();

        testObject.setVersion(version);
        testObject.setVersionName(versionName);
        testObject.setCategory(category);
        testObject.setGUID(guid);
        testObject.setName(name);
        testObject.setDescription(description);
        testObject.setDescriptionGUID(descriptionGUID);

        return testObject;
    }


    private void validateObject(AttributeTypeDef   testObject)
    {
        assertTrue(testObject.getVersion() == version);
        assertTrue(testObject.getVersionName().equals(versionName));
        assertTrue(testObject.getCategory().equals(category));
        assertTrue(testObject.getGUID().equals(guid));
        assertTrue(testObject.getName().equals(name));
        assertTrue(testObject.getDescription().equals(description));
        assertTrue(testObject.getDescriptionGUID().equals(descriptionGUID));
    }


    /**
     * Validate that the constructors set up the attributes correctly.
     */
    @Test public void testConstructors()
    {
        AttributeTypeDefMock testObject = new AttributeTypeDefMock();

        assertTrue(testObject.getVersion() == 0L);
        assertTrue(testObject.getVersionName() == null);
        assertTrue(testObject.getCategory() == null);
        assertTrue(testObject.getGUID() == null);
        assertTrue(testObject.getName() == null);
        assertTrue(testObject.getDescription() == null);
        assertTrue(testObject.getDescriptionGUID() == null);

        testObject = new AttributeTypeDefMock(AttributeTypeDefCategory.ENUM_DEF);

        assertTrue(testObject.getVersion() == 0L);
        assertTrue(testObject.getVersionName() == null);
        assertTrue(testObject.getCategory() == AttributeTypeDefCategory.ENUM_DEF);
        assertTrue(testObject.getGUID() == null);
        assertTrue(testObject.getName() == null);
        assertTrue(testObject.getDescription() == null);
        assertTrue(testObject.getDescriptionGUID() == null);

        testObject = new AttributeTypeDefMock(category, guid, name);

        assertTrue(testObject.getVersion() == 0L);
        assertTrue(testObject.getVersionName() == null);
        assertTrue(testObject.getCategory().equals(category));
        assertTrue(testObject.getGUID().equals(guid));
        assertTrue(testObject.getName().equals(name));
        assertTrue(testObject.getDescription() == null);
        assertTrue(testObject.getDescriptionGUID() == null);

        AttributeTypeDef anotherTestObject = getTestObject();

        validateObject(new AttributeTypeDefMock(anotherTestObject));
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
            validateObject(objectMapper.readValue(jsonString, AttributeTypeDefMock.class));
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
        assertTrue(getTestObject().toString().contains("AttributeTypeDef"));
    }


    /**
     * Test that equals works
     */
    @Test public void testEquals()
    {
        assertTrue(getTestObject().equals(getTestObject()));

        AttributeTypeDef testObject = getTestObject();

        assertTrue(testObject.equals(testObject));

        assertFalse(getTestObject().equals(null));
        assertFalse(getTestObject().equals("AString"));
    }


    /**
     * Test that hashcode is consistent
     */
    @Test public void testHash()
    {
        assertTrue(getTestObject().hashCode() == getTestObject().hashCode());

        AttributeTypeDef testObject = getTestObject();
        AttributeTypeDef anotherObject = getTestObject();
        anotherObject.setGUID("DifferentGUID");

        assertFalse(testObject.hashCode() == anotherObject.hashCode());
    }
}
