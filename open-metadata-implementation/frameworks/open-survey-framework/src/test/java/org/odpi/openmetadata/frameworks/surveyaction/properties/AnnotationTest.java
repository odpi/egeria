/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.surveyaction.properties;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementClassification;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementType;
import org.odpi.openmetadata.frameworks.openmetadata.properties.surveyreports.AnnotationProperties;
import org.testng.annotations.Test;

import java.util.*;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * Validate that the AnnotationProperties bean can be cloned, compared, serialized, deserialized and printed as a String.
 */
public class AnnotationTest
{
    private ElementType                 type                 = new ElementType();
    private Date                        creationDate         = new Date(27);
    private Date                        reviewDate           = new Date(1234);
    private List<ElementClassification> classifications      = new ArrayList<>();
    private Map<String, Object>         analysisParameters   = new HashMap<>();
    private Map<String, String>         additionalProperties = new HashMap<>();


    /**
     * Default constructor
     */
    public AnnotationTest()
    {
    }


    /**
     * Set up an example object to test.
     *
     * @return filled in object
     */
    private AnnotationProperties getTestObject()
    {
        AnnotationProperties testObject = new AnnotationProperties();

        testObject.setAnnotationType("TestAnnotationType");
        testObject.setSummary("TestSummary");
        testObject.setConfidenceLevel(5);
        testObject.setExpression("TestExpression");
        testObject.setExplanation("TestExplanation");
        testObject.setAnalysisStep("TestAnalysisStep");
        testObject.setJsonProperties("TestJsonProperties");
        testObject.setAdditionalProperties(additionalProperties);

        return testObject;
    }


    /**
     * Validate that the object that comes out of the test has the same content as the original test object.
     *
     * @param resultObject object returned by the test
     */
    private void validateResultObject(AnnotationProperties resultObject)
    {
        assertTrue(resultObject.getAnnotationType().equals("TestAnnotationType"));
        assertTrue(resultObject.getSummary().equals("TestSummary"));
        assertTrue(resultObject.getConfidenceLevel() == 5);
        assertTrue(resultObject.getExpression().equals("TestExpression"));
        assertTrue(resultObject.getExplanation().equals("TestExplanation"));
        assertTrue(resultObject.getAnalysisStep().equals("TestAnalysisStep"));
        assertTrue(resultObject.getJsonProperties().equals("TestJsonProperties"));
        assertTrue(resultObject.getAdditionalProperties().equals(additionalProperties));
    }


    /**
     * Validate that the object is initialized properly
     */
    @Test public void testNullObject()
    {
        AnnotationProperties nullObject = new AnnotationProperties();

        assertTrue(nullObject.getAnnotationType() == null);
        assertTrue(nullObject.getSummary() == null);
        assertTrue(nullObject.getConfidenceLevel() == 0);
        assertTrue(nullObject.getExpression() == null);
        assertTrue(nullObject.getExplanation() == null);
        assertTrue(nullObject.getAnalysisStep() == null);
        assertTrue(nullObject.getJsonProperties() == null);
        assertTrue(nullObject.getAdditionalProperties() == null);

        nullObject = new AnnotationProperties(null);

        assertTrue(nullObject.getAnnotationType() == null);
        assertTrue(nullObject.getSummary() == null);
        assertTrue(nullObject.getConfidenceLevel() == 0);
        assertTrue(nullObject.getExpression() == null);
        assertTrue(nullObject.getExplanation() == null);
        assertTrue(nullObject.getAnalysisStep() == null);
        assertTrue(nullObject.getJsonProperties() == null);
        assertTrue(nullObject.getAdditionalProperties() == null);
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

        AnnotationProperties sameObject = getTestObject();
        assertTrue(sameObject.equals(sameObject));

        AnnotationProperties differentObject = getTestObject();
        differentObject.setExplanation("Different");
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
        validateResultObject(new AnnotationProperties(getTestObject()));
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
        catch (Exception   exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }

        try
        {
            validateResultObject(objectMapper.readValue(jsonString, AnnotationProperties.class));
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
        assertTrue(getTestObject().toString().contains("AnnotationProperties"));
    }
}
