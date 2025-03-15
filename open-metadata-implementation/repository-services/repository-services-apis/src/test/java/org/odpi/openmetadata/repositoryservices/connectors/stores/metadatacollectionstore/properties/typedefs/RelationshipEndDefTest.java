/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

/**
 * RelationshipEndDefTest provides test of RelationshipEndDef
 */
public class RelationshipEndDefTest
{
    private TypeDefLink                entityType               = new TypeDefLink();
    private String                     attributeName            = "TestAttributeName";
    private String                     attributeDescription     = "TestAttributeDescription";
    private String                     attributeDescriptionGUID = "TestAttributeDescriptionGUID";
    private RelationshipEndCardinality attributeCardinality     = RelationshipEndCardinality.AT_MOST_ONE;


    public RelationshipEndDefTest()
    {
        entityType.setGUID("TestEntityGUID");
        entityType.setName("TestEntityName");
        entityType.setStatus(TypeDefStatus.ACTIVE_TYPEDEF);
        entityType.setReplacedByTypeGUID("TestNewEntityGUID");
        entityType.setReplacedByTypeName("TestNewEntityName");
    }

    /**
     * Return a filled in test object
     *
     * @return test object
     */
    private RelationshipEndDef   getTestObject()
    {
        RelationshipEndDef testObject = new RelationshipEndDef();

        testObject.setAttributeName(attributeName);
        testObject.setAttributeDescription(attributeDescription);
        testObject.setAttributeDescriptionGUID(attributeDescriptionGUID);
        testObject.setEntityType(entityType);
        testObject.setAttributeCardinality(attributeCardinality);

        return testObject;
    }


    /**
     * Validate supplied object.
     *
     * @param testObject object to test
     */
    private void validateObject(RelationshipEndDef   testObject)
    {
        assertEquals(testObject.getAttributeName(), attributeName);
        assertEquals(testObject.getAttributeDescription(), attributeDescription);
        assertEquals(testObject.getAttributeDescriptionGUID(), attributeDescriptionGUID);
        assertEquals(testObject.getEntityType(), entityType);
        assertEquals(testObject.getAttributeCardinality(), attributeCardinality);
    }


    /**
     * Validate that the constructors set up the correct properties
     */
    @Test public void testConstructors()
    {
        RelationshipEndDef testObject = new RelationshipEndDef();

        assertNull(testObject.getAttributeName());
        assertNull(testObject.getAttributeDescription());
        assertNull(testObject.getAttributeDescriptionGUID());
        assertNull(testObject.getEntityType());
        assertEquals(testObject.getAttributeCardinality(), RelationshipEndCardinality.UNKNOWN);

        validateObject(new RelationshipEndDef(getTestObject()));
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
            validateObject(objectMapper.readValue(jsonString, RelationshipEndDef.class));
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
        assertTrue(getTestObject().toString().contains("RelationshipEndDef"));
    }


    /**
     * Test that equals works
     */
    @Test public void testEquals()
    {
        assertTrue(getTestObject().equals(getTestObject()));

        RelationshipEndDef testObject = getTestObject();

        assertTrue(testObject.equals(testObject));
        assertTrue(testObject.equals(getTestObject()));

        assertFalse(getTestObject().equals(null));
        assertFalse(getTestObject().equals("A String"));

        RelationshipEndDef  differentObject = getTestObject();
        differentObject.setAttributeName("AnotherValue");

        assertFalse(testObject.equals(differentObject));
    }


    /**
     * Test that hashcode is consistent
     */
    @Test public void testHash()
    {
        assertTrue(getTestObject().hashCode() == getTestObject().hashCode());

        RelationshipEndDef testObject = getTestObject();
        RelationshipEndDef anotherObject = getTestObject();
        anotherObject.setAttributeName("88");

        assertNotEquals(testObject.hashCode(), anotherObject.hashCode());
    }
}
