/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.*;
import org.testng.annotations.Test;

import java.util.*;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * Validate that the ConnectorTypeProperties can function as a facade for its bean.
 */
public class TestConnectorTypeProperties
{
    private ElementType                 type                              = new ElementType();
    private List<ElementClassification> classifications                   = new ArrayList<>();
    private Map<String, String>         additionalProperties              = new HashMap<>();
    private List<String>                recognizedAdditionalProperties    = new ArrayList<>();
    private List<String>                recognizedSecuredProperties       = new ArrayList<>();
    private List<String>                recognizedConfigurationProperties = new ArrayList<>();




    /**
     * Default constructor
     */
    public TestConnectorTypeProperties()
    {
        type.setElementTypeName("TestType");

        recognizedAdditionalProperties.add("TestValue");
        recognizedSecuredProperties.add("TestValue");
        recognizedConfigurationProperties.add("TestValue");
    }


    /**
     * Set up an example object to test.
     *
     * @return filled in object
     */
    private ConnectorTypeProperties getTestObject()
    {
        ConnectorType testObject = new ConnectorType();

        testObject.setType(type);
        testObject.setGUID("TestGUID");
        testObject.setURL("TestURL");
        testObject.setClassifications(classifications);

        testObject.setQualifiedName("TestQualifiedName");
        testObject.setAdditionalProperties(additionalProperties);

        testObject.setDisplayName("TestDisplayName");
        testObject.setDescription("TestDescription");
        testObject.setConnectorProviderClassName("TestClassName");
        testObject.setRecognizedAdditionalProperties(recognizedAdditionalProperties);
        testObject.setRecognizedSecuredProperties(recognizedSecuredProperties);
        testObject.setRecognizedConfigurationProperties(recognizedConfigurationProperties);

        return new ConnectorTypeProperties(testObject);
    }


    /**
     * Set up an example object to test. A property from the super class is different.
     *
     * @return filled in object
     */
    private ConnectorTypeProperties getDifferentObject()
    {
        ConnectorType testObject = new ConnectorType();

        testObject.setType(type);
        testObject.setGUID("TestDifferentGUID");
        testObject.setURL("TestURL");
        testObject.setClassifications(classifications);

        testObject.setQualifiedName("TestQualifiedName");
        testObject.setAdditionalProperties(additionalProperties);

        testObject.setDisplayName("TestDisplayName");
        testObject.setDescription("TestDescription");
        testObject.setConnectorProviderClassName("TestClassName");
        testObject.setRecognizedAdditionalProperties(recognizedAdditionalProperties);
        testObject.setRecognizedSecuredProperties(recognizedSecuredProperties);
        testObject.setRecognizedConfigurationProperties(recognizedConfigurationProperties);

        return new ConnectorTypeProperties(testObject);
    }


    /**
     * Set up an example object to test.  A property from the subclass is different.
     *
     * @return filled in object
     */
    private ConnectorTypeProperties getAnotherDifferentObject()
    {
        ConnectorType testObject = new ConnectorType();

        testObject.setType(type);
        testObject.setGUID("TestGUID");
        testObject.setURL("TestURL");
        testObject.setClassifications(classifications);

        testObject.setQualifiedName("TestQualifiedName");
        testObject.setAdditionalProperties(additionalProperties);

        testObject.setDisplayName("TestDisplayName");
        testObject.setDescription("TestDifferentDescription");
        testObject.setConnectorProviderClassName("TestDifferentClassName");
        testObject.setRecognizedAdditionalProperties(new ArrayList<>());
        testObject.setRecognizedSecuredProperties(new ArrayList<>());
        testObject.setRecognizedConfigurationProperties(new ArrayList<>());

        return new ConnectorTypeProperties(testObject);
    }


    /**
     * Validate that the object that comes out of the test has the same content as the original test object.
     *
     * @param resultObject object returned by the test
     */
    private void validateResultObject(ConnectorTypeProperties  resultObject)
    {
        assertTrue(resultObject.getType().getElementTypeBean().equals(type));
        assertTrue(resultObject.getGUID().equals("TestGUID"));
        assertTrue(resultObject.getURL().equals("TestURL"));
        assertTrue(resultObject.getAssetClassifications() == null);

        assertTrue(resultObject.getQualifiedName().equals("TestQualifiedName"));
        assertTrue(resultObject.getAdditionalProperties() == null);

        assertTrue(resultObject.getDisplayName().equals("TestDisplayName"));
        assertTrue(resultObject.getDescription().equals("TestDescription"));
        assertTrue(resultObject.getConnectorProviderClassName().equals("TestClassName"));
        assertTrue(resultObject.getRecognizedAdditionalProperties().equals(recognizedAdditionalProperties));
        assertTrue(resultObject.getRecognizedSecuredProperties().equals(recognizedSecuredProperties));
        assertTrue(resultObject.getRecognizedConfigurationProperties().equals(recognizedConfigurationProperties));
    }


    /**
     * Validate that the object that comes out of the test has the same content as the original test object.
     *
     * @param nullObject object to test
     */
    private void validateNullObject(ConnectorTypeProperties  nullObject)
    {
        assertTrue(nullObject.getType() == null);
        assertTrue(nullObject.getGUID() == null);
        assertTrue(nullObject.getURL() == null);
        assertTrue(nullObject.getAssetClassifications() == null);

        assertTrue(nullObject.getQualifiedName() == null);
        assertTrue(nullObject.getAdditionalProperties() == null);

        assertTrue(nullObject.getDisplayName() == null);
        assertTrue(nullObject.getDescription() == null);
        assertTrue(nullObject.getConnectorProviderClassName() == null);
        assertTrue(nullObject.getRecognizedAdditionalProperties() == null);
        assertTrue(nullObject.getRecognizedSecuredProperties() == null);
        assertTrue(nullObject.getRecognizedConfigurationProperties() == null);
    }


    /**
     * Validate that the object is initialized properly
     */
    @Test public void testNullObject()
    {
        ConnectorType           nullBean;
        ConnectorTypeProperties nullObject;
        ConnectorTypeProperties nullTemplate;
        AssetDescriptor         parentAsset;

        nullBean = null;
        nullObject = new ConnectorTypeProperties(nullBean);
        validateNullObject(nullObject);

        nullBean = new ConnectorType();
        nullObject = new ConnectorTypeProperties(nullBean);
        validateNullObject(nullObject);

        nullBean = new ConnectorType(null);
        nullObject = new ConnectorTypeProperties(nullBean);
        validateNullObject(nullObject);

        parentAsset = null;
        nullBean = null;
        nullObject = new ConnectorTypeProperties(parentAsset, nullBean);
        validateNullObject(nullObject);

        nullBean = new ConnectorType();
        nullObject = new ConnectorTypeProperties(parentAsset, nullBean);
        validateNullObject(nullObject);

        nullBean = new ConnectorType(null);
        nullObject = new ConnectorTypeProperties(parentAsset, nullBean);
        validateNullObject(nullObject);

        nullTemplate = null;
        nullObject = new ConnectorTypeProperties(parentAsset, nullTemplate);
        validateNullObject(nullObject);

        nullTemplate = null;
        nullObject = new ConnectorTypeProperties(nullTemplate);
        validateNullObject(nullObject);
    }


    /**
     * Validate that an empty array is returned as null.
     */
    @Test public void testRecognizedList()
    {
        ConnectorTypeProperties connectorTypeProperties = getAnotherDifferentObject();

        assertTrue(connectorTypeProperties.getRecognizedAdditionalProperties() == null);
        assertTrue(connectorTypeProperties.getRecognizedSecuredProperties() == null);
        assertTrue(connectorTypeProperties.getRecognizedConfigurationProperties() == null);

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

        ConnectorTypeProperties  sameObject = getTestObject();
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
        validateResultObject(new ConnectorTypeProperties(null, getTestObject()));
    }


    /**
     * Test that toString is overridden.
     */
    @Test public void testToString()
    {
        assertTrue(getTestObject().toString().contains("ConnectorType"));
    }
}
