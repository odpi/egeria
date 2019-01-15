/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetconsumer.properties;

import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * Validate that the ReferenceableHeader bean can be cloned, compared, serialized, deserialized and printed as a String.
 */
public class ReferenceableHeaderTest
{
    private String                            guid                     = "TestGUID";
    private String                            typeName                 = "TestTypeName";
    private String                            typeDescription          = "TestTypeDescription";
    private String                            qualifiedName            = "TestQualifiedName";
    private String                            originId                 = "TestOriginId";
    private String                            originName               = "TestOriginName";
    private String                            originType               = "TestOriginType";
    private String                            originLicense            = "TestOriginLicense";
    private Map<String, Object>               additionalProperties     = new HashMap<>();
    private List<ReferenceableClassification> classifications          = new ArrayList<>();
    private ReferenceableClassification       classification           = new ReferenceableClassification();
    private Map<String, Object>               classificationProperties = new HashMap<>();



    /**
     * Default constructor
     */
    public ReferenceableHeaderTest()
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
    private ReferenceableHeader getTestObject()
    {
        ReferenceableHeader testObject = new MockReferenceableHeader();

        testObject.setGUID(guid);
        testObject.setTypeName(typeName);
        testObject.setTypeDescription(typeDescription);
        testObject.setOriginId(originId);
        testObject.setOriginType(originType);
        testObject.setOriginName(originName);
        testObject.setOriginLicense(originLicense);
        testObject.setQualifiedName(qualifiedName);
        testObject.setAdditionalProperties(additionalProperties);
        testObject.setClassifications(classifications);

        return testObject;
    }


    /**
     * Validate that the object that comes out of the test has the same content as the original test object.
     *
     * @param resultObject object returned by the test
     */
    private void validateResultObject(ReferenceableHeader  resultObject)
    {
        assertTrue(resultObject.getGUID().equals(guid));
        assertTrue(resultObject.getTypeName().equals(typeName));
        assertTrue(resultObject.getTypeDescription().equals(typeDescription));
        assertTrue(resultObject.getOriginId().equals(originId));
        assertTrue(resultObject.getOriginType().equals(originType));
        assertTrue(resultObject.getOriginName().equals(originName));
        assertTrue(resultObject.getOriginLicense().equals(originLicense));
        assertTrue(resultObject.getQualifiedName().equals(qualifiedName));
        assertTrue(resultObject.getAdditionalProperties().equals(additionalProperties));
        assertTrue(resultObject.getClassifications().equals(classifications));
    }


    /**
     * Validate that the object is initialized properly
     */
    @Test public void testNullObject()
    {
        ReferenceableHeader    nullObject = new MockReferenceableHeader();

        assertTrue(nullObject.getGUID() == null);
        assertTrue(nullObject.getTypeName() == null);
        assertTrue(nullObject.getTypeDescription() == null);
        assertTrue(nullObject.getQualifiedName() == null);

        assertTrue(nullObject.getOriginId() == null);
        assertTrue(nullObject.getOriginType() == null);
        assertTrue(nullObject.getOriginName() == null);
        assertTrue(nullObject.getOriginLicense() == null);

        assertTrue(nullObject.getAdditionalProperties() == null);
        assertTrue(nullObject.getClassifications() == null);

        nullObject = new MockReferenceableHeader(null);

        assertTrue(nullObject.getGUID() == null);
        assertTrue(nullObject.getTypeName() == null);
        assertTrue(nullObject.getTypeDescription() == null);
        assertTrue(nullObject.getQualifiedName() == null);
        assertTrue(nullObject.getAdditionalProperties() == null);
        assertTrue(nullObject.getClassifications() == null);

        assertTrue(nullObject.getOriginId() == null);
        assertTrue(nullObject.getOriginType() == null);
        assertTrue(nullObject.getOriginName() == null);
        assertTrue(nullObject.getOriginLicense() == null);
    }


    /**
     * Validate that additional properties are managed properly
     */
    @Test public void testAdditionalProperties()
    {
        Map<String, Object>   propertyMap;
        ReferenceableHeader                 testObject = new MockReferenceableHeader();

        assertTrue(testObject.getAdditionalProperties() == null);

        propertyMap = null;
        testObject = new MockReferenceableHeader();
        testObject.setAdditionalProperties(propertyMap);

        assertTrue(testObject.getAdditionalProperties() == null);

        propertyMap = new HashMap<>();
        testObject = new MockReferenceableHeader();
        testObject.setAdditionalProperties(propertyMap);

        assertTrue(testObject.getAdditionalProperties() == null);

        propertyMap.put("propertyName", "propertyValue");
        testObject = new MockReferenceableHeader();
        testObject.setAdditionalProperties(propertyMap);

        Map<String, Object>   retrievedPropertyMap = testObject.getAdditionalProperties();

        assertTrue(retrievedPropertyMap != null);
        assertFalse(retrievedPropertyMap.isEmpty());
        assertTrue("propertyValue".equals(retrievedPropertyMap.get("propertyName")));
    }


    /**
     * Validate that additional properties are managed properly
     */
    @Test public void testClassifications()
    {
        ReferenceableHeader                 testObject = new MockReferenceableHeader();

        assertTrue(testObject.getClassifications() == null);

        testObject = new MockReferenceableHeader();
        testObject.setClassifications(null);

        assertTrue(testObject.getClassifications() == null);

        List<ReferenceableClassification> emptyClassifications = new ArrayList<>();
        testObject = new MockReferenceableHeader();
        testObject.setClassifications(emptyClassifications);

        assertTrue(testObject.getClassifications() == null);

        testObject = new MockReferenceableHeader();
        testObject.setClassifications(classifications);

        List<ReferenceableClassification> retrievedClassifications = testObject.getClassifications();

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

        ReferenceableHeader  sameObject = getTestObject();
        assertTrue(sameObject.equals(sameObject));

        ReferenceableHeader  differentObject = getTestObject();
        differentObject.setQualifiedName("Different");
        assertFalse(getTestObject().equals(differentObject));
    }


    /**
     *  Validate that 2 different objects with the same content have the same hash code.
     */
    @Test public void testHashCode()
    {
        assertTrue(getTestObject().hashCode() == getTestObject().hashCode());

        ReferenceableHeader  testObject = getTestObject();

        assertTrue(testObject.hashCode() != 0);

        ReferenceableHeader  differentObject = getTestObject();

        differentObject.setClassifications(null);

        assertFalse(testObject.hashCode() == differentObject.hashCode());
    }


    /**
     *  Validate that an object cloned from another object has the same content as the original
     */
    @Test public void testClone()
    {
        validateResultObject(new MockReferenceableHeader(getTestObject()));
    }





    /**
     * Test that toString is overridden.
     */
    @Test public void testToString()
    {
        assertTrue(getTestObject().toString().contains("ReferenceableHeader"));
    }
}
