/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.digitalarchitecture.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.odpi.openmetadata.accessservices.digitalarchitecture.metadataelements.ElementClassification;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * Validate that the DigitalArchitectureEvent bean can be cloned, compared, serialized, deserialized and printed as a String.
 */
public class DigitalArchitectureEventTest
{
    private DigitalArchitectureEventType eventType                = DigitalArchitectureEventType.UNKNOWN_DIGITAL_ARCHITECTURE_EVENT;
    private String                       url                    = "TestURL";
    private String                       guid                   = "TestGUID";
    private String                       typeId                 = "TestTypeId";
    private String                       typeName               = "TestTypeName";
    private long                         typeVersion              = 7;
    private String                       typeDescription          = "TestTypeDescription";
    private String                       qualifiedName            = "TestQualifiedName";
    private String                       displayName              = "TestDisplayName";
    private String                       description              = "TestDescription";
    private Map<String, Object>          additionalProperties     = new HashMap<>();
    private List<ElementClassification>  classifications          = new ArrayList<>();
    private ElementClassification        classification           = new ElementClassification();
    private Map<String, Object>          classificationProperties = new HashMap<>();


    /**
     * Default constructor
     */
    public DigitalArchitectureEventTest()
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
    private DigitalArchitectureEvent getTestObject()
    {
        DigitalArchitectureEvent testObject = new DigitalArchitectureEvent();

        testObject.setEventType(eventType);

        return testObject;
    }


    /**
     * Validate that the object that comes out of the test has the same content as the original test object.
     *
     * @param resultObject object returned by the test
     */
    private void validateResultObject(DigitalArchitectureEvent resultObject)
    {
        assertTrue(resultObject.getEventType().equals(eventType));
    }


    /**
     * Validate that the object is initialized properly
     */
    @Test public void testNullObject()
    {
        DigitalArchitectureEvent nullObject = new DigitalArchitectureEvent();

        assertTrue(nullObject.getEventType() == null);


        nullObject = new DigitalArchitectureEvent(null);

        assertTrue(nullObject.getEventType() == null);

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

        DigitalArchitectureEvent sameObject = getTestObject();
        assertTrue(sameObject.equals(sameObject));

        DigitalArchitectureEvent anotherObject = getTestObject();
        anotherObject.setEventVersionId(3773L);
        assertFalse(getTestObject().equals(anotherObject));
    }


    /**
     *  Validate that 2 different objects with the same content have the same hash code.
     */
    @Test public void testHashCode()
    {
        assertTrue(getTestObject().hashCode() == getTestObject().hashCode());

        DigitalArchitectureEvent testObject = getTestObject();

        assertTrue(testObject.hashCode() != 0);

        DigitalArchitectureEvent differentObject = getTestObject();

        differentObject.setEventType(null);

        assertFalse(testObject.hashCode() == differentObject.hashCode());
    }


    /**
     *  Validate that an object cloned from another object has the same content as the original
     */
    @Test public void testClone()
    {
        validateResultObject(new DigitalArchitectureEvent(getTestObject()));
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
            validateResultObject(objectMapper.readValue(jsonString, DigitalArchitectureEvent.class));
        }
        catch (Exception  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }

        /*
         * Through superclass
         */
        DigitalArchitectureEventHeader superObject = getTestObject();

        try
        {
            jsonString = objectMapper.writeValueAsString(superObject);
        }
        catch (Exception  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }

        try
        {
            validateResultObject((DigitalArchitectureEvent) objectMapper.readValue(jsonString, DigitalArchitectureEventHeader.class));
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
        assertTrue(getTestObject().toString().contains("DigitalArchitectureEvent"));
    }
}
