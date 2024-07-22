/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties.beans;

import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * Validate that the SchemaElement bean can be cloned, compared, serialized, deserialized and printed as a String.
 */
public class TestSchemaElement
{
    private ElementType                 type                 = new ElementType();
    private List<ElementClassification> classifications      = new ArrayList<>();
    private Map<String, String>         additionalProperties = new HashMap<>();


    /**
     * Default constructor
     */
    public TestSchemaElement()
    {

    }


    /**
     * Set up an example object to test.
     *
     * @return filled in object
     */
    private SchemaElement getTestObject()
    {
        SchemaElement testObject = new MockSchemaElement();

        testObject.setType(type);
        testObject.setGUID("TestGUID");
        testObject.setClassifications(classifications);

        testObject.setQualifiedName("TestQualifiedName");
        testObject.setAdditionalProperties(additionalProperties);

        return testObject;
    }

    /**
     * Validate that the object that comes out of the test has the same content as the original test object.
     *
     * @param resultObject object returned by the test
     */
    private void validateResultObject(SchemaElement resultObject)
    {
        assertTrue(resultObject.getType().equals(type));
        assertTrue(resultObject.getGUID().equals("TestGUID"));
        assertTrue(resultObject.getClassifications().equals(classifications));

        assertTrue(resultObject.getQualifiedName().equals("TestQualifiedName"));
        assertTrue(resultObject.getAdditionalProperties() == null);
    }


    /**
     * Validate that the object is initialized properly
     */
    @Test public void testNullObject()
    {
        SchemaElement nullObject = new MockSchemaElement();

        assertTrue(nullObject.getType() == null);
        assertTrue(nullObject.getGUID() == null);
        assertTrue(nullObject.getClassifications() == null);

        assertTrue(nullObject.getQualifiedName() == null);
        assertTrue(nullObject.getAdditionalProperties() == null);

        nullObject = new MockSchemaElement(null);

        assertTrue(nullObject.getType() == null);
        assertTrue(nullObject.getGUID() == null);
        assertTrue(nullObject.getClassifications() == null);

        assertTrue(nullObject.getQualifiedName() == null);
        assertTrue(nullObject.getAdditionalProperties() == null);
    }


    /**
     * Validate that additional properties are managed properly
     */
    @Test public void testAdditionalProperties()
    {
        Map<String, String> propertyMap;
        SchemaElement       testObject = new MockSchemaElement();

        assertTrue(testObject.getQualifiedName() == null);

        propertyMap = null;
        testObject = new MockSchemaElement();
        testObject.setAdditionalProperties(propertyMap);

        assertTrue(testObject.getAdditionalProperties() == null);

        propertyMap = new HashMap<>();
        testObject = new MockSchemaElement();
        testObject.setAdditionalProperties(propertyMap);

        assertTrue(testObject.getAdditionalProperties() != null);

        propertyMap.put("propertyName", "propertyValue");
        testObject = new MockSchemaElement();
        testObject.setAdditionalProperties(propertyMap);

        Map<String, String>   retrievedPropertyMap = testObject.getAdditionalProperties();

        assertTrue(retrievedPropertyMap != null);
        assertFalse(retrievedPropertyMap.isEmpty());
        assertTrue("propertyValue".equals(retrievedPropertyMap.get("propertyName")));
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

        SchemaElement sameObject = getTestObject();
        assertTrue(sameObject.equals(sameObject));

        SchemaElement differentObject = getTestObject();
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
     * Test that toString is overridden.
     */
    @Test public void testToString()
    {
        assertTrue(getTestObject().toString().contains("SchemaElement"));
    }
}
