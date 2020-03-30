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
 * Validate that the DerivedSchemaAttribute bean can be cloned, compared, serialized,
 * deserialized and printed as a String.
 */
public class TestDerivedSchemaAttribute
{
    private ElementType                       type                 = new ElementType();
    private List<Classification>              classifications      = new ArrayList<>();
    private Map<String, String>               additionalProperties = new HashMap<>();
    private SchemaType                        schemaElement        = new PrimitiveSchemaType();
    private SchemaLink                        schemaLink           = new SchemaLink();
    private List<SchemaAttributeRelationship> relationships        = new ArrayList<>();
    private List<SchemaImplementationQuery>   queries              = new ArrayList<>();


    /**
     * Default constructor
     */
    public TestDerivedSchemaAttribute()
    {
        classifications.add(new Classification());
        relationships.add(new SchemaAttributeRelationship());
        additionalProperties.put("TestKey", "TestValue");
        queries.add(new SchemaImplementationQuery());
    }


    /**
     * Set up an example object to test.
     *
     * @return filled in object
     */
    private DerivedSchemaAttribute getTestObject()
    {
        DerivedSchemaAttribute testObject = new DerivedSchemaAttribute();

        testObject.setType(type);
        testObject.setGUID("TestGUID");
        testObject.setURL("TestURL");
        testObject.setClassifications(classifications);

        testObject.setQualifiedName("TestQualifiedName");
        testObject.setAdditionalProperties(additionalProperties);

        testObject.setQualifiedName("TestQualifiedName");
        testObject.setAdditionalProperties(additionalProperties);
        testObject.setAttributeName("TestAttributeName");
        testObject.setElementPosition(23);
        testObject.setDefaultValueOverride("TestDefault");
        testObject.setAttributeType(schemaElement);
        testObject.setExternalAttributeType(schemaLink);
        testObject.setAttributeRelationships(relationships);

        testObject.setFormula("TestFormula");
        testObject.setQueries(queries);

        return testObject;
    }


    /**
     * Validate that the object that comes out of the test has the same content as the original test object.
     *
     * @param resultObject object returned by the test
     */
    private void validateResultObject(DerivedSchemaAttribute resultObject)
    {
        assertTrue(resultObject.getType().equals(type));
        assertTrue(resultObject.getGUID().equals("TestGUID"));
        assertTrue(resultObject.getURL().equals("TestURL"));
        assertTrue(resultObject.getClassifications().equals(classifications));

        assertTrue(resultObject.getQualifiedName().equals("TestQualifiedName"));
        assertTrue(resultObject.getAdditionalProperties().equals(additionalProperties));

        assertTrue(resultObject.getAttributeName().equals("TestAttributeName"));
        assertTrue(resultObject.getElementPosition() == 23);
        assertTrue(resultObject.getDefaultValueOverride().equals("TestDefault"));

        assertTrue(resultObject.getAttributeType().equals(schemaElement));
        assertTrue(resultObject.getExternalAttributeType().equals(schemaLink));

        assertTrue(resultObject.getFormula().equals("TestFormula"));
        assertTrue(resultObject.getQueries().equals(queries));
    }


    /**
     * Validate that the object is initialized properly
     */
    @Test public void testNullObject()
    {
        DerivedSchemaAttribute nullObject = new DerivedSchemaAttribute();

        assertTrue(nullObject.getType() == null);
        assertTrue(nullObject.getGUID() == null);
        assertTrue(nullObject.getURL() == null);
        assertTrue(nullObject.getClassifications() == null);

        assertTrue(nullObject.getQualifiedName() == null);
        assertTrue(nullObject.getAdditionalProperties() == null);

        assertTrue(nullObject.getAttributeName() == null);
        assertTrue(nullObject.getElementPosition() == 0);
        assertTrue(nullObject.getDefaultValueOverride() == null);
        assertTrue(nullObject.getAttributeType() == null);
        assertTrue(nullObject.getExternalAttributeType() == null);

        assertTrue(nullObject.getFormula() == null);
        assertTrue(nullObject.getQueries() == null);

        nullObject = new DerivedSchemaAttribute(null);

        assertTrue(nullObject.getType() == null);
        assertTrue(nullObject.getGUID() == null);
        assertTrue(nullObject.getURL() == null);
        assertTrue(nullObject.getClassifications() == null);

        assertTrue(nullObject.getQualifiedName() == null);
        assertTrue(nullObject.getAdditionalProperties() == null);

        assertTrue(nullObject.getAttributeName() == null);
        assertTrue(nullObject.getElementPosition() == 0);
        assertTrue(nullObject.getDefaultValueOverride() == null);
        assertTrue(nullObject.getAttributeType() == null);
        assertTrue(nullObject.getExternalAttributeType() == null);

        assertTrue(nullObject.getFormula() == null);
        assertTrue(nullObject.getQueries() == null);

        nullObject = new DerivedSchemaAttribute();

        nullObject.setClassifications(new ArrayList<>());
        nullObject.setAdditionalProperties(new HashMap<>());
        nullObject.setAttributeRelationships(new ArrayList<>());

        assertTrue(nullObject.getClassifications() == null);
        assertTrue(nullObject.getAdditionalProperties() == null);

        nullObject.setQueries(new ArrayList<>());

        assertTrue(nullObject.getQueries() == null);
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

        DerivedSchemaAttribute sameObject = getTestObject();
        assertTrue(sameObject.equals(sameObject));

        DerivedSchemaAttribute differentObject = getTestObject();
        differentObject.setGUID("Different");
        assertFalse(getTestObject().equals(differentObject));
    }


    /**
     *  Validate that 2 different objects with the same content have the same hash code.
     */
    @Test public void testHashCode()
    {
        assertTrue(getTestObject().hashCode() == getTestObject().hashCode());
    }


    /**
     *  Validate that an object cloned from another object has the same content as the original
     */
    @Test public void testClone()
    {
        validateResultObject(new DerivedSchemaAttribute(getTestObject()));
    }


    /**
     * Test that a an object cloned through the superclass cloneSchemaElement has the same content as
     * the original
     */
    @Test public void testAbstractClone()
    {
        SchemaElement schemaElement = getTestObject();

        validateResultObject((DerivedSchemaAttribute) schemaElement.cloneSchemaElement());
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
            validateResultObject(objectMapper.readValue(jsonString, DerivedSchemaAttribute.class));
        }
        catch (Throwable  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }

        /*
         * Through superclass
         */
        SchemaAttribute schemaAttribute = getTestObject();

        try
        {
            jsonString = objectMapper.writeValueAsString(schemaAttribute);
        }
        catch (Throwable  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }

        try
        {
            validateResultObject((DerivedSchemaAttribute) objectMapper.readValue(jsonString, SchemaAttribute.class));
        }
        catch (Throwable  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }

        /*
         * Through superclass
         */
        SchemaElement schemaElement = getTestObject();

        try
        {
            jsonString = objectMapper.writeValueAsString(schemaElement);
        }
        catch (Throwable  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }

        try
        {
            validateResultObject((DerivedSchemaAttribute) objectMapper.readValue(jsonString, SchemaElement.class));
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
            validateResultObject((DerivedSchemaAttribute) objectMapper.readValue(jsonString, Referenceable.class));
        }
        catch (Throwable  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }

        /*
         * Through superclass
         */
        ElementHeader elementHeader = getTestObject();

        try
        {
            jsonString = objectMapper.writeValueAsString(elementHeader);
        }
        catch (Throwable  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }

        try
        {
            validateResultObject((DerivedSchemaAttribute) objectMapper.readValue(jsonString, ElementHeader.class));
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
            validateResultObject((DerivedSchemaAttribute) objectMapper.readValue(jsonString, PropertyBase.class));
        }
        catch (Throwable  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }
    }


    /**
     * Validate that an object generated from a JSON String has the same content as the object used to
     * create the JSON String.
     */
    @Test public void testAbstractJSON()
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
            validateResultObject((DerivedSchemaAttribute)objectMapper.readValue(jsonString, SchemaElement.class));
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
        assertTrue(getTestObject().toString().contains("DerivedSchemaAttribute"));
    }
}
