/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.*;

/**
 * PrimitiveDefTest provides test of PrimitiveDef
 */
public class PrimitiveDefTest
{
    private  PrimitiveDefCategory   primitiveDefCategory = PrimitiveDefCategory.OM_PRIMITIVE_TYPE_BIGDECIMAL;

    /**
     * Return a filled in test object
     *
     * @return test object
     */
    private PrimitiveDef   getTestObject()
    {
        PrimitiveDef testObject = new PrimitiveDef();

        testObject.setPrimitiveDefCategory(primitiveDefCategory);

        return testObject;
    }


    private void validateObject(PrimitiveDef   testObject)
    {
        assertEquals(testObject.getPrimitiveDefCategory(), primitiveDefCategory);
    }

    private void validateNullObject(PrimitiveDef   testObject)
    {
        assertNull(testObject.getPrimitiveDefCategory());
    }


    /**
     * Validate that the cloning process sets up the correct properties
     */
    @Test public void testCloneFromSubclass()
    {
        PrimitiveDef testObject = this.getTestObject();

        validateObject((PrimitiveDef) testObject.cloneFromSubclass());
    }


    /**
     * Validate that the constructors set up the attributes correctly.
     */
    @Test public void testConstructors()
    {
        validateNullObject(new PrimitiveDef());
        validateNullObject(new PrimitiveDef(new PrimitiveDef()));

        validateObject(new PrimitiveDef(getTestObject()));

        validateObject(new PrimitiveDef(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_BIGDECIMAL));
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
            validateObject(objectMapper.readValue(jsonString, PrimitiveDef.class));
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
        assertTrue(getTestObject().toString().contains("PrimitiveDef"));
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
