/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.frameworks.connectors.properties;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.*;
import org.testng.annotations.Test;

import java.util.*;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * Validate that the ConnectionProperties can function as a facade for its bean.
 */
public class TestAssetClassification
{
    private Map<String, Object>  classificationProperties = new HashMap<>();
    private AssetDescriptor      parentAsset     = new AssetSummary(new Asset());



    /**
     * Default constructor
     */
    public TestAssetClassification()
    {

    }


    /**
     * Set up an example object to test.
     *
     * @return filled in object
     */
    private AssetClassification getTestObject()
    {
        Classification testObject = new Classification();

        testObject.setClassificationName("TestClassificationName");
        testObject.setClassificationProperties(classificationProperties);

        return new AssetClassification(testObject);
    }


    /**
     * Set up an example object to test. A property from the super class is different.
     *
     * @return filled in object
     */
    private AssetClassification getDifferentObject()
    {
        Classification testObject = new Classification();

        testObject.setClassificationName("TestClassificationName");
        testObject.setClassificationProperties(classificationProperties);

        return new AssetClassification(parentAsset, testObject);

    }


    /**
     * Set up an example object to test. A property from the subclass is different.
     *
     * @return filled in object
     */
    private AssetClassification getAnotherDifferentObject()
    {
        Classification testObject = new Classification();

        testObject.setClassificationName("TestDifferentClassificationName");
        testObject.setClassificationProperties(classificationProperties);

        return new AssetClassification(testObject);
    }


    /**
     * Validate that the object that comes out of the test has the same content as the original test object.
     *
     * @param resultObject object returned by the test
     */
    private void validateResultObject(AssetClassification  resultObject)
    {
        assertTrue(resultObject.getName().equals("TestClassificationName"));
        assertTrue(resultObject.getProperties() == null);
    }


    /**
     * Validate that the object that comes out of the test has the same content as the original test object.
     *
     * @param nullObject object to test
     */
    private void validateNullObject(AssetClassification  nullObject)
    {
        assertTrue(nullObject.getProperties() == null);
        assertFalse(nullObject.getName() == null); // Null not allowed
    }


    /**
     * Validate that the object is initialized properly
     */
    @Test public void testNullObject()
    {
        Classification           nullBean = new Classification();
        AssetClassification      nullTemplate = null;
        AssetDescriptor          parentAsset = null;

        try
        {
            new AssetClassification(null);
            assertTrue(false);
        }
        catch (Throwable exc)
        {
            assertTrue(true);
        }

        try
        {
            new AssetClassification(nullBean);
            assertTrue(false);
        }
        catch (Throwable exc)
        {
            assertTrue(true);
        }


        try
        {
            new AssetClassification(parentAsset, (Classification)null);
            assertTrue(false);
        }
        catch (Throwable exc)
        {
            assertTrue(true);
        }


        try
        {
            new AssetClassification(parentAsset, nullBean);
            assertTrue(false);
        }
        catch (Throwable exc)
        {
            assertTrue(true);
        }


        try
        {
            new AssetClassification(parentAsset, nullTemplate);
            assertTrue(false);
        }
        catch (Throwable exc)
        {
            assertTrue(true);
        }
    }


    /**
     * Validate that secured properties are handled properly.
     */
    @Test public void testClassificationProperties()
    {
        Map<String, Object>  propertyMap = new HashMap<>();

        propertyMap.put("property1", "TestString");
        propertyMap.put("property2", new Integer(2));

        Classification classificationBean = new Classification();
        classificationBean.setClassificationName("TestClassificationName");
        classificationBean.setClassificationProperties(propertyMap);

        AssetClassification testObject = new AssetClassification(classificationBean);

        AdditionalProperties classificationProperties = testObject.getProperties();

        assertTrue(classificationProperties.getPropertyNames() != null);

        Iterator<String> iterator = classificationProperties.getPropertyNames();

        String propertyName;

        propertyName = iterator.next();
        assertTrue(propertyName.equals("property2"));
        assertTrue(classificationProperties.getProperty(propertyName).equals(new Integer(2)));

        propertyName = iterator.next();
        assertTrue(propertyName.equals("property1"));
        assertTrue(classificationProperties.getProperty(propertyName).equals("TestString"));

        try
        {
            iterator.next();
            assertTrue(false);
        }
        catch (Throwable   exc)
        {
            assertTrue(true);
        }

        classificationBean = new Classification();
        classificationBean.setClassificationName("TestClassificationName");

        testObject = new AssetClassification(classificationBean);

        classificationProperties = testObject.getProperties();

        assertTrue(classificationProperties == null);

        propertyMap = new HashMap<>();
        classificationBean = new Classification();
        classificationBean.setClassificationName("TestClassificationName");

        classificationBean.setClassificationProperties(propertyMap);
        testObject = new AssetClassification(classificationBean);

        classificationProperties = testObject.getProperties();

        assertTrue(classificationProperties == null);
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

        AssetClassification  sameObject = getTestObject();
        assertTrue(sameObject.equals(sameObject));

        assertFalse(getTestObject().equals(getDifferentObject()));
        assertFalse(getTestObject().equals(getAnotherDifferentObject()));
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
        validateResultObject(new AssetClassification(null, getTestObject()));
    }

    /**
     * Test that toString is overridden.
     */
    @Test public void testToString()
    {
        assertTrue(getTestObject().toString().contains("Classification"));
    }
}
