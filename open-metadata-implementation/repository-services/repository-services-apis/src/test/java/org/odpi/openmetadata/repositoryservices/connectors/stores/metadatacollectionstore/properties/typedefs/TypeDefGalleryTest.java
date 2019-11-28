/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.*;

/**
 * TypeDefGalleryTest provides test of TypeDefGallery
 */
public class TypeDefGalleryTest
{
    private List<AttributeTypeDef> attributeTypeDefs = new ArrayList<>();
    private List<TypeDef>          typeDefs          = new ArrayList<>();


    public TypeDefGalleryTest()
    {
        attributeTypeDefs.add(new PrimitiveDef());
        attributeTypeDefs.add(new CollectionDef());
        attributeTypeDefs.add(new EnumDef());
        typeDefs.add(new EntityDef());
        typeDefs.add(new ClassificationDef());
        typeDefs.add(new RelationshipDef());
    }

    /**
     * Return a filled in test object
     *
     * @return test object
     */
    private TypeDefGallery   getTestObject()
    {
        TypeDefGallery testObject = new TypeDefGallery();

        testObject.setTypeDefs(typeDefs);
        testObject.setAttributeTypeDefs(attributeTypeDefs);

        return testObject;
    }


    /**
     * Validate supplied object.
     *
     * @param testObject object to test
     */
    private void validateObject(TypeDefGallery   testObject)
    {
        assertEquals(testObject.getTypeDefs(), typeDefs);
        assertEquals(testObject.getAttributeTypeDefs(), attributeTypeDefs);
    }


    /**
     * Validate that the constructors set up the correct properties
     */
    @Test public void testConstructors()
    {
        TypeDefGallery testObject = new TypeDefGallery();

        assertNull(testObject.getTypeDefs());
        assertNull(testObject.getAttributeTypeDefs());

        testObject.setAttributeTypeDefs(new ArrayList<>());
        assertNull(testObject.getAttributeTypeDefs());

        testObject.setTypeDefs(new ArrayList<>());
        assertNull(testObject.getTypeDefs());

        validateObject(new TypeDefGallery(getTestObject()));
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
            validateObject(objectMapper.readValue(jsonString, TypeDefGallery.class));
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
        assertTrue(getTestObject().toString().contains("TypeDefGallery"));
    }


    /**
     * Test that equals works
     */
    @Test public void testEquals()
    {
        assertTrue(getTestObject().equals(getTestObject()));

        TypeDefGallery testObject = getTestObject();

        assertTrue(testObject.equals(testObject));
        assertTrue(testObject.equals(getTestObject()));

        assertFalse(getTestObject().equals(null));
        assertFalse(getTestObject().equals("A String"));

        TypeDefGallery  differentObject = getTestObject();
        differentObject.setTypeDefs(new ArrayList<>());

        assertFalse(testObject.equals(differentObject));
    }


    /**
     * Test that hashcode is consistent
     */
    @Test public void testHash()
    {
        assertTrue(getTestObject().hashCode() == getTestObject().hashCode());

        TypeDefGallery testObject = getTestObject();
        TypeDefGallery anotherObject = getTestObject();
        anotherObject.setAttributeTypeDefs(new ArrayList<>());

        assertNotEquals(testObject.hashCode(), anotherObject.hashCode());
    }
}
