/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.*;

/**
 * EnumDefTest provides test of EnumDef
 */
public class EnumDefTest
{
    private long                     version         = 6L;
    private String                   versionName     = "TestVersion";
    private AttributeTypeDefCategory category        = AttributeTypeDefCategory.PRIMITIVE;
    private String                   guid            = "TestGUID";
    private String                   name            = "TestName";
    private String                   description     = "TestDescription";
    private String                   descriptionGUID = "TestDescriptionGUID";
    private List<EnumElementDef>     elementDefs;
    private EnumElementDef           defaultValue;

    /**
     * Constructor sets up complex objects
     */
    public EnumDefTest()
    {
        elementDefs     = new ArrayList<>();

        EnumElementDef enum1 = new EnumElementDef();

        enum1.setDescription("enum1TestDescription");
        enum1.setDescriptionGUID("enum1TestDescriptionGUID");
        enum1.setOrdinal(1);
        enum1.setValue("TestEnum1");

        elementDefs.add(enum1);
        defaultValue = enum1;

        EnumElementDef enum2 = new EnumElementDef();

        enum2.setDescription("enum2TestDescription");
        enum2.setDescriptionGUID("enum2TestDescriptionGUID");
        enum2.setOrdinal(2);
        enum2.setValue("TestEnum2");

        elementDefs.add(enum2);
    }


    /**
     * Return a filled in test object
     *
     * @return test object
     */
    private EnumDef   getTestObject()
    {
        EnumDef testObject = new EnumDef();

        testObject.setVersion(version);
        testObject.setVersionName(versionName);
        testObject.setCategory(category);
        testObject.setGUID(guid);
        testObject.setName(name);
        testObject.setDescription(description);
        testObject.setDescriptionGUID(descriptionGUID);
        testObject.setDefaultValue(defaultValue);
        testObject.setElementDefs(elementDefs);

        return testObject;
    }


    private void validateObject(EnumDef   testObject)
    {
        assertEquals(testObject.getVersion(), version);
        assertTrue(testObject.getVersionName().equals(versionName));
        assertTrue(testObject.getCategory().equals(category));
        assertTrue(testObject.getGUID().equals(guid));
        assertTrue(testObject.getName().equals(name));
        assertTrue(testObject.getDescription().equals(description));
        assertTrue(testObject.getDescriptionGUID().equals(descriptionGUID));
        assertTrue(testObject.getDefaultValue().equals(defaultValue));
        assertTrue(testObject.getElementDefs().equals(elementDefs));
    }

    private void validateNullObject(EnumDef   testObject)
    {
        assertEquals(testObject.getVersion(), 0L);
        assertNull(testObject.getVersionName());
        assertSame(testObject.getCategory(), AttributeTypeDefCategory.ENUM_DEF);
        assertNull(testObject.getGUID());
        assertNull(testObject.getName());
        assertNull(testObject.getDescription());
        assertNull(testObject.getDescriptionGUID());
        assertNull(testObject.getDescriptionGUID());
        assertNull(testObject.getDefaultValue());
        assertNull(testObject.getElementDefs());
    }


    /**
     * Validate that the cloning process sets up the correct properties
     */
    @Test public void testCloneFromSubclass()
    {
        EnumDef testObject = this.getTestObject();

        validateObject((EnumDef) testObject.cloneFromSubclass());
    }


    /**
     * Validate that the constructors set up the attributes correctly.
     */
    @Test public void testConstructors()
    {
        validateNullObject(new EnumDef());
        validateNullObject(new EnumDef(new EnumDef()));

        validateObject(new EnumDef(getTestObject()));

        EnumDef testObject = new EnumDef();
        testObject.setElementDefs(new ArrayList<>());
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
        catch (Throwable  exc)
        {
            fail("Exception: " + exc.getMessage());
        }

        try
        {
            validateObject(objectMapper.readValue(jsonString, EnumDef.class));
        }
        catch (Throwable  exc)
        {
            fail("Exception: " + exc.getMessage());
        }
    }


    /**
     * Test that toString is overridden.
     */
    @Test public void testToString()
    {
        assertTrue(getTestObject().toString().contains("EnumDef"));
    }


    /**
     * Test that equals works
     */
    @Test public void testEquals()
    {
        assertTrue(getTestObject().equals(getTestObject()));

        AttributeTypeDef testObject = getTestObject();

        assertTrue(testObject.equals(testObject));

        testObject.setName("Freddy");
        assertFalse(getTestObject().equals(testObject));

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

        AttributeTypeDef testObject = getTestObject();
        AttributeTypeDef anotherObject = getTestObject();
        anotherObject.setGUID("DifferentGUID");

        assertNotEquals(testObject.hashCode(), anotherObject.hashCode());
    }
}
