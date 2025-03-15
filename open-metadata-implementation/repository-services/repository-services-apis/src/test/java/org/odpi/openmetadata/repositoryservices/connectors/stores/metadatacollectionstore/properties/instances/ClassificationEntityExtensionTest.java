/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * ClassificationEntityExtensionTest provides test of ClassificationEntityExtension
 */
public class ClassificationEntityExtensionTest
{
    private Classification         classification         = new Classification();
    private EntityProxy            entityProxy            = new EntityProxy();


    public ClassificationEntityExtensionTest()
    {
        classification.setName("TestClassification");
        entityProxy.setGUID(UUID.randomUUID().toString());
    }


    /**
     * Return a filled in test object
     *
     * @return test object
     */
    private ClassificationEntityExtension   getTestObject()
    {
        ClassificationEntityExtension testObject = new ClassificationEntityExtension();

        testObject.setHeaderVersion(ClassificationEntityExtension.CURRENT_CLASSIFICATION_EXT_HEADER_VERSION);
        testObject.setClassification(classification);
        testObject.setEntityToClassify(entityProxy);

        return testObject;
    }


    /**
     * Validate supplied object.
     *
     * @param testObject object to test
     */
    private void validateObject(ClassificationEntityExtension   testObject)
    {
        assertTrue(testObject.getClassification().equals(classification));
        assertTrue(testObject.getEntityToClassify().equals(entityProxy));
    }


    /**
     * Validate that the constructors set up the correct properties
     */
    @Test public void testConstructors()
    {
        ClassificationEntityExtension testObject = new ClassificationEntityExtension();

        assertTrue(testObject.getClassification() == null);
        assertTrue(testObject.getEntityToClassify() == null);

        ClassificationEntityExtension anotherTestObject = getTestObject();

        validateObject(new ClassificationEntityExtension(anotherTestObject));
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
            validateObject(objectMapper.readValue(jsonString, ClassificationEntityExtension.class));
        }
        catch (Exception   exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }


        /*
         * Through super class
         */
        InstanceElementHeader testObject = getTestObject();

        try
        {
            jsonString = objectMapper.writeValueAsString(testObject);
        }
        catch (Exception   exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }

        try
        {
            validateObject((ClassificationEntityExtension)objectMapper.readValue(jsonString, InstanceElementHeader.class));
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
        assertTrue(getTestObject().toString().contains("ClassificationEntityExtension"));
    }


    /**
     * Test that equals works
     */
    @Test public void testEquals()
    {
        assertTrue(getTestObject().equals(getTestObject()));

        ClassificationEntityExtension testObject = getTestObject();

        assertTrue(testObject.equals(testObject));

        assertFalse(getTestObject().equals(null));
        assertFalse(getTestObject().equals("A String"));

        ClassificationEntityExtension  differentObject = getTestObject();

        differentObject.setClassification(new Classification());

        assertFalse(testObject.equals(differentObject));

        differentObject = getTestObject();

        differentObject.setEntityToClassify(new EntityProxy());

        assertFalse(testObject.equals(differentObject));

        differentObject = getTestObject();

        differentObject.setClassification(null);

        assertFalse(testObject.equals(differentObject));

        differentObject = getTestObject();

        differentObject.setEntityToClassify(null);

        assertFalse(testObject.equals(differentObject));
    }


    /**
     * Test that hashcode is consistent
     */
    @Test public void testHash()
    {
        assertTrue(getTestObject().hashCode() == getTestObject().hashCode());
        assertFalse(getTestObject().hashCode() == new ClassificationEntityExtension().hashCode());
    }
}
