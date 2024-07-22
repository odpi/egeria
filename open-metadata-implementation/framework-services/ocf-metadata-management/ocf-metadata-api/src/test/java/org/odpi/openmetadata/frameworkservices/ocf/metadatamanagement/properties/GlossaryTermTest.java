/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworkservices.ocf.metadatamanagement.properties;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementClassification;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * Validate that the GlossaryTerm bean can be cloned, compared, serialized, deserialized and printed as a String.
 */
public class GlossaryTermTest
{
    private String                      guid                     = "TestGUID";
    private String                      typeId                   = "TestTypeId";
    private String                            typeName                 = "TestTypeName";
    private long                              typeVersion              = 7;
    private String                            typeDescription          = "TestTypeDescription";
    private String                            summary                  = "TestSummary";
    private String                      examples                 = "TestExamples";
    private String                      abbreviation             = "TestAbbreviation";
    private String                      usage                    = "TestUsage";
    private String                      qualifiedName            = "TestQualifiedName";
    private String                      displayName              = "TestDisplayName";
    private String                      description              = "TestDescription";
    private Map<String, String>         additionalProperties     = new HashMap<>();
    private List<ElementClassification> classifications          = new ArrayList<>();
    private ElementClassification       classification           = new ElementClassification();
    private Map<String, Object>         classificationProperties = new HashMap<>();



    /**
     * Default constructor
     */
    public GlossaryTermTest()
    {
        additionalProperties.put("TestAdditionalPropertyName", "TestAdditionalPropertyValue");
        
        classification.setClassificationName("TestClassificationName");
        classificationProperties.put("TestClassificationPropertyName", "TestClassificationPropertyValue");
        classification.setClassificationProperties(classificationProperties);
        classifications.add(classification);
    }


    /**
     * Set up an example object to test.
     *
     * @return filled in object
     */
    private GlossaryTerm getTestObject()
    {
        GlossaryTerm testObject = new GlossaryTerm();

        testObject.setGUID(guid);
        testObject.setQualifiedName(qualifiedName);
        testObject.setDisplayName(displayName);
        testObject.setDescription(description);
        testObject.setAbbreviation(abbreviation);
        testObject.setExamples(examples);
        testObject.setUsage(usage);
        testObject.setSummary(summary);
        testObject.setAdditionalProperties(additionalProperties);
        testObject.setClassifications(classifications);

        return testObject;
    }


    /**
     * Validate that the object that comes out of the test has the same content as the original test object.
     *
     * @param resultObject object returned by the test
     */
    private void validateResultObject(GlossaryTerm  resultObject)
    {
        assertTrue(resultObject.getGUID().equals(guid));
        assertTrue(resultObject.getQualifiedName().equals(qualifiedName));
        assertTrue(resultObject.getDisplayName().equals(displayName));
        assertTrue(resultObject.getDescription().equals(description));
        assertTrue(resultObject.getAbbreviation().equals(abbreviation));
        assertTrue(resultObject.getExamples().equals(examples));
        assertTrue(resultObject.getUsage().equals(usage));
        assertTrue(resultObject.getSummary().equals(summary));
        assertTrue(resultObject.getAdditionalProperties().equals(additionalProperties));
        assertTrue(resultObject.getClassifications().equals(classifications));
    }


    /**
     * Validate that the object is initialized properly
     */
    @Test public void testNullObject()
    {
        GlossaryTerm    nullObject = new GlossaryTerm();

        assertTrue(nullObject.getGUID() == null);
        assertTrue(nullObject.getQualifiedName() == null);
        assertTrue(nullObject.getDisplayName() == null);
        assertTrue(nullObject.getDescription() == null);
        assertTrue(nullObject.getSummary() == null);
        assertTrue(nullObject.getUsage() == null);
        assertTrue(nullObject.getExamples() == null);
        assertTrue(nullObject.getAbbreviation() == null);
        assertTrue(nullObject.getAdditionalProperties() == null);
        assertTrue(nullObject.getClassifications() == null);

        nullObject = new GlossaryTerm(null);

        assertTrue(nullObject.getGUID() == null);
        assertTrue(nullObject.getQualifiedName() == null);
        assertTrue(nullObject.getDisplayName() == null);
        assertTrue(nullObject.getDescription() == null);
        assertTrue(nullObject.getSummary() == null);
        assertTrue(nullObject.getUsage() == null);
        assertTrue(nullObject.getExamples() == null);
        assertTrue(nullObject.getAbbreviation() == null);
        assertTrue(nullObject.getAdditionalProperties() == null);
        assertTrue(nullObject.getClassifications() == null);
    }


    /**
     * Validate that additional properties are managed properly
     */
    @Test public void testAdditionalProperties()
    {
        Map<String, String>   propertyMap;
        GlossaryTerm                 testObject = new GlossaryTerm();

        assertTrue(testObject.getAdditionalProperties() == null);

        propertyMap = null;
        testObject = new GlossaryTerm();
        testObject.setAdditionalProperties(propertyMap);

        assertTrue(testObject.getAdditionalProperties() == null);

        propertyMap = new HashMap<>();
        testObject = new GlossaryTerm();
        testObject.setAdditionalProperties(propertyMap);

        assertTrue(testObject.getAdditionalProperties() != null);

        propertyMap.put("propertyName", "propertyValue");
        testObject = new GlossaryTerm();
        testObject.setAdditionalProperties(propertyMap);

        Map<String, String>   retrievedPropertyMap = testObject.getAdditionalProperties();

        assertTrue(retrievedPropertyMap != null);
        assertFalse(retrievedPropertyMap.isEmpty());
        assertTrue("propertyValue".equals(retrievedPropertyMap.get("propertyName")));
    }


    /**
     * Validate that additional properties are managed properly
     */
    @Test public void testClassifications()
    {
        GlossaryTerm                 testObject = new GlossaryTerm();

        assertTrue(testObject.getClassifications() == null);

        testObject = new GlossaryTerm();
        testObject.setClassifications(null);

        assertTrue(testObject.getClassifications() == null);

        List<ElementClassification> emptyClassifications = new ArrayList<>();
        testObject = new GlossaryTerm();
        testObject.setClassifications(emptyClassifications);

        assertTrue(testObject.getClassifications() != null);

        testObject = new GlossaryTerm();
        testObject.setClassifications(classifications);

        List<ElementClassification> retrievedClassifications = testObject.getClassifications();

        assertTrue(retrievedClassifications != null);
        assertFalse(retrievedClassifications.isEmpty());
        assertTrue(retrievedClassifications.equals(classifications));
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

        GlossaryTerm  sameObject = getTestObject();
        assertTrue(sameObject.equals(sameObject));

        GlossaryTerm  differentObject = getTestObject();
        differentObject.setDescription("Different");
        assertFalse(getTestObject().equals(differentObject));

        differentObject = getTestObject();
        differentObject.setQualifiedName("Different");
        assertFalse(getTestObject().equals(differentObject));
    }


    /**
     *  Validate that 2 different objects with the same content have the same hash code.
     */
    @Test public void testHashCode()
    {
        assertTrue(getTestObject().hashCode() == getTestObject().hashCode());

        GlossaryTerm  testObject = getTestObject();

        assertTrue(testObject.hashCode() != 0);
    }


    /**
     *  Validate that an object cloned from another object has the same content as the original
     */
    @Test public void testClone()
    {
        validateResultObject(new GlossaryTerm(getTestObject()));
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
        catch (Exception  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }

        try
        {
            validateResultObject(objectMapper.readValue(jsonString, GlossaryTerm.class));
        }
        catch (Exception  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }
    }


    /**
     * Test that toString is overridden.
     */
    @Test public void testToString()
    {
        assertTrue(getTestObject().toString().contains("GlossaryTerm"));
    }
}
