/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties.beans;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * Validate that the SchemaAttribute bean can be cloned, compared, serialized, deserialized and printed as a String.
 */
public class TestSchemaAttribute
{
    private ElementType                       type                 = new ElementType();
    private List<ElementClassification>       classifications      = new ArrayList<>();
    private Map<String, String>               additionalProperties = new HashMap<>();
    private SchemaType                        schemaElement        = new PrimitiveSchemaType();
    private List<SchemaAttributeRelationship> relationships        = new ArrayList<>();



    /**
     * Default constructor
     */
    public TestSchemaAttribute()
    {
        classifications.add(new ElementClassification());
        relationships.add(new SchemaAttributeRelationship());
        additionalProperties.put("TestKey", "TestValue");
    }


    /**
     * Set up an example object to test.
     *
     * @return filled in object
     */
    private SchemaAttribute getTestObject()
    {
        SchemaAttribute testObject = new SchemaAttribute();

        testObject.setType(type);
        testObject.setGUID("TestGUID");
        testObject.setClassifications(classifications);

        testObject.setQualifiedName("TestQualifiedName");
        testObject.setAdditionalProperties(additionalProperties);

        testObject.setDisplayName("TestAttributeName");
        testObject.setDescription("TestDescription");
        testObject.setElementPosition(23);
        testObject.setMinCardinality(-1);
        testObject.setMaxCardinality(-1);
        testObject.setDefaultValueOverride("TestDefault");

        testObject.setAttributeType(schemaElement);
        testObject.setAttributeRelationships(relationships);

        return testObject;
    }


    /**
     * Validate that the object that comes out of the test has the same content as the original test object.
     *
     * @param resultObject object returned by the test
     */
    private void validateResultObject(SchemaAttribute resultObject)
    {
        assertTrue(resultObject.getType().equals(type));
        assertTrue(resultObject.getGUID().equals("TestGUID"));
        assertTrue(resultObject.getClassifications().equals(classifications));

        assertTrue(resultObject.getQualifiedName().equals("TestQualifiedName"));
        assertTrue(resultObject.getAdditionalProperties().equals(additionalProperties));

        assertTrue(resultObject.getDisplayName().equals("TestAttributeName"));
        assertTrue(resultObject.getDescription().equals("TestDescription"));

        assertTrue(resultObject.getElementPosition() == 23);
        assertTrue(resultObject.getMaxCardinality() == -1);
        assertTrue(resultObject.getMinCardinality() == -1);
        assertTrue(resultObject.getCardinality().equals("*"));
        assertTrue(resultObject.getDefaultValueOverride().equals("TestDefault"));

        assertTrue(resultObject.getAttributeType().equals(schemaElement));
    }


    /**
     * Validate that the object is initialized properly
     */
    @Test public void testNullObject()
    {
        SchemaAttribute nullObject = new SchemaAttribute();

        assertTrue(nullObject.getType() == null);
        assertTrue(nullObject.getGUID() == null);
        assertTrue(nullObject.getClassifications() == null);

        assertTrue(nullObject.getQualifiedName() == null);
        assertTrue(nullObject.getAdditionalProperties() == null);

        assertTrue(nullObject.getDisplayName() == null);
        assertTrue(nullObject.getElementPosition() == 0);
        assertTrue(nullObject.getMaxCardinality() == 0);
        assertTrue(nullObject.getMinCardinality() == 0);
        assertTrue(nullObject.getDefaultValueOverride() == null);
        assertTrue(nullObject.getAttributeType() == null);

        nullObject = new SchemaAttribute(null);

        assertTrue(nullObject.getType() == null);
        assertTrue(nullObject.getGUID() == null);
        assertTrue(nullObject.getClassifications() == null);

        assertTrue(nullObject.getQualifiedName() == null);
        assertTrue(nullObject.getAdditionalProperties() == null);

        assertTrue(nullObject.getDisplayName() == null);
        assertTrue(nullObject.getElementPosition() == 0);
        assertTrue(nullObject.getMaxCardinality() == 0);
        assertTrue(nullObject.getMinCardinality() == 0);
        assertTrue(nullObject.getDefaultValueOverride() == null);

        assertTrue(nullObject.getAttributeType() == null);

        nullObject.setClassifications(new ArrayList<>());
        nullObject.setAdditionalProperties(new HashMap<>());
        nullObject.setAttributeRelationships(new ArrayList<>());

        assertTrue(nullObject.getClassifications() != null);
        assertTrue(nullObject.getAdditionalProperties() != null);

    }


    /**
     * Validate that 2 different objects with the same content are evaluated as equal.
     * Also that different objects are considered not equal.
     */
    @Test public void testEquals()
    {
        assertFalse(getTestObject().equals(null));
        assertFalse(getTestObject().equals("DummyString"));
        assertTrue(getTestObject().equals(getTestObject()));

        SchemaAttribute sameObject = getTestObject();
        assertTrue(sameObject.equals(sameObject));

        SchemaAttribute differentObject = getTestObject();
        differentObject.setGUID("Different");
        assertFalse(getTestObject().equals(differentObject));

        differentObject = getTestObject();
        differentObject.setDisplayName("Different");
        assertFalse(getTestObject().equals(differentObject));
    }


    /**
     *  Validate that an object cloned from another object has the same content as the original
     */
    @Test public void testClone()
    {
        validateResultObject(new SchemaAttribute(getTestObject()));
    }


    /**
     * Validate that cloneSchemaElement works
     */
    @Test public void testCloneSchemaElement()
    {
        validateResultObject((SchemaAttribute) getTestObject().cloneSchemaElement());
    }


    /**
     * Validate that an object generated from a JSON String has the same content as the object used to
     * create the JSON String.
     */
    @Test public void testJSON()
    {
        ObjectMapper objectMapper = new ObjectMapper();
        String       jsonString   = null;

        /*
         * This class
         */
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
            validateResultObject(objectMapper.readValue(jsonString, SchemaAttribute.class));
        }
        catch (Throwable  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }

        /*
         * Through superclass
         */
        Referenceable referenceable = getTestObject();

        try
        {
            jsonString = objectMapper.writeValueAsString(referenceable);
        }
        catch (Throwable  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }

        try
        {
            validateResultObject((SchemaAttribute) objectMapper.readValue(jsonString, Referenceable.class));
        }
        catch (Throwable  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }

        /*
         * Through superclass
         */
        ElementBase elementBase = getTestObject();

        try
        {
            jsonString = objectMapper.writeValueAsString(elementBase);
        }
        catch (Throwable  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }

        try
        {
            validateResultObject((SchemaAttribute) objectMapper.readValue(jsonString, ElementBase.class));
        }
        catch (Throwable  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }

        /*
         * Through superclass
         */
        PropertyBase propertyBase = getTestObject();

        try
        {
            jsonString = objectMapper.writeValueAsString(propertyBase);
        }
        catch (Throwable  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }

        try
        {
            validateResultObject((SchemaAttribute) objectMapper.readValue(jsonString, PropertyBase.class));
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
        assertTrue(getTestObject().toString().contains("SchemaAttribute"));
    }
}
