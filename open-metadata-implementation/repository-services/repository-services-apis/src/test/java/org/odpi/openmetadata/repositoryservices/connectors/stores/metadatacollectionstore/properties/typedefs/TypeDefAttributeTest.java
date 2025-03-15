/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.*;

/**
 * TypeDefAttributeTest provides test of TypeDefAttribute
 */
public class TypeDefAttributeTest
{
    protected String                        attributeName            = "TestAttributeName";
    protected AttributeTypeDef              attributeType            = new PrimitiveDef(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_BOOLEAN);
    protected TypeDefAttributeStatus        attributeStatus          = TypeDefAttributeStatus.ACTIVE_ATTRIBUTE;
    protected String                        replacedByAttribute      = "TestReplacementAttribute";
    protected String                        attributeDescription     = "TestAttributeDescription";
    protected String                        attributeDescriptionGUID = "TestAttributeDescriptionGUID";
    protected AttributeCardinality          cardinality              = AttributeCardinality.ONE_ONLY;
    protected int                           valuesMinCount           = 4;
    protected int                           valuesMaxCount           = 9;
    protected boolean                       isIndexable              = false;
    protected boolean                       isUnique                 = true;
    protected String                        defaultValue             = "TestDefaultValue";
    protected List<ExternalStandardMapping> externalStandardMappings = new ArrayList<>();


    /**
     * Constructor to set up complex attributes
     */
    public TypeDefAttributeTest()
    {
        ExternalStandardMapping  externalStandardMapping = new ExternalStandardMapping();

        externalStandardMapping.setStandardName("TestStandardName");
        externalStandardMapping.setStandardTypeName("TestStandardTypeName");
        externalStandardMapping.setStandardOrganization("TestOrganizationName");

        externalStandardMappings.add(externalStandardMapping);
    }

    /**
     * Return a filled in test object
     *
     * @return test object
     */
    private TypeDefAttribute   getTestObject()
    {
        TypeDefAttribute testObject = new TypeDefAttribute();

        testObject.setAttributeName(attributeName);
        testObject.setAttributeType(attributeType);
        testObject.setAttributeStatus(attributeStatus);
        testObject.setReplacedByAttribute(replacedByAttribute);
        testObject.setAttributeDescription(attributeDescription);
        testObject.setAttributeDescriptionGUID(attributeDescriptionGUID);
        testObject.setAttributeCardinality(cardinality);
        testObject.setValuesMinCount(valuesMinCount);
        testObject.setValuesMaxCount(valuesMaxCount);
        testObject.setIndexable(isIndexable);
        testObject.setUnique(isUnique);
        testObject.setDefaultValue(defaultValue);
        testObject.setExternalStandardMappings(externalStandardMappings);

        return testObject;
    }


    private void validateObject(TypeDefAttribute   testObject)
    {
        assertEquals(testObject.getAttributeName(), attributeName);
        assertEquals(testObject.getAttributeType(), attributeType);
        assertEquals(testObject.getAttributeStatus(), attributeStatus);
        assertEquals(testObject.getReplacedByAttribute(), replacedByAttribute);
        assertEquals(testObject.getAttributeDescription(), attributeDescription);
        assertEquals(testObject.getAttributeDescriptionGUID(), attributeDescriptionGUID);
        assertEquals(testObject.getAttributeCardinality(), AttributeCardinality.ONE_ONLY);
        assertEquals(testObject.getValuesMinCount(), 4);
        assertEquals(testObject.getValuesMaxCount(), 9);
        assertEquals(testObject.isIndexable(), false);
        assertEquals(testObject.isUnique(), true);
        assertEquals(testObject.getDefaultValue(), defaultValue);
        assertEquals(testObject.getExternalStandardMappings(), externalStandardMappings);
    }

    private void validateNullObject(TypeDefAttribute   testObject)
    {
        assertNull(testObject.getAttributeName());
        assertNull(testObject.getAttributeType());
        assertNull(testObject.getAttributeStatus());
        assertNull(testObject.getReplacedByAttribute());
        assertNull(testObject.getAttributeDescription());
        assertNull(testObject.getAttributeDescriptionGUID());
        assertEquals(testObject.getAttributeCardinality(), AttributeCardinality.UNKNOWN);
        assertEquals(testObject.getValuesMinCount(), 0);
        assertEquals(testObject.getValuesMaxCount(), 1);
        assertEquals(testObject.isIndexable(), true);
        assertEquals(testObject.isUnique(), false);
        assertNull(testObject.getDefaultValue());
        assertNull(testObject.getExternalStandardMappings());
    }



    /**
     * Validate that the constructors set up the attributes correctly.
     */
    @Test public void testConstructors()
    {
        validateNullObject(new TypeDefAttribute());
        validateNullObject(new TypeDefAttribute(new TypeDefAttribute()));

        validateObject(new TypeDefAttribute(getTestObject()));

        TypeDefAttribute testObject = new TypeDefAttribute();
        testObject.setExternalStandardMappings(new ArrayList<>());
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
            validateObject(objectMapper.readValue(jsonString, TypeDefAttribute.class));
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
        assertTrue(getTestObject().toString().contains("TypeDefAttribute"));
    }


    /**
     * Test that equals works
     */
    @Test public void testEquals()
    {
        assertTrue(getTestObject().equals(getTestObject()));

        TypeDefAttribute testObject = getTestObject();

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

        TypeDefAttribute testObject = getTestObject();
        TypeDefAttribute anotherObject = getTestObject();
        anotherObject.setAttributeDescriptionGUID("DifferentGUID");

        assertNotEquals(testObject.hashCode(), anotherObject.hashCode());
    }
}
