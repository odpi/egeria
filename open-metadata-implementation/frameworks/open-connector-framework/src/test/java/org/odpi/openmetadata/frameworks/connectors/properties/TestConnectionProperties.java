/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.*;
import org.testng.annotations.Test;

import java.util.*;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * Validate that the ConnectionProperties can function as a facade for its bean.
 */
public class TestConnectionProperties
{
    private ElementType                 type                 = new ElementType();
    private List<ElementClassification> classifications      = new ArrayList<>();
    private Map<String, String>         additionalProperties = new HashMap<>();
    private Map<String, String>         securedProperties    = new HashMap<>();
    private ConnectorType               connectorType        = new ConnectorType();
    private Endpoint                    endpoint             = new Endpoint();


    /**
     * Default constructor
     */
    public TestConnectionProperties()
    {
        type.setTypeName("TestType");
    }


    /**
     * Set up an example object to test.
     *
     * @return filled in object
     */
    private ConnectionProperties getTestObject()
    {
        Connection testObject = new Connection();

        testObject.setType(type);
        testObject.setGUID("TestGUID");
        testObject.setClassifications(classifications);

        testObject.setQualifiedName("TestQualifiedName");
        testObject.setAdditionalProperties(additionalProperties);

        testObject.setDisplayName("TestDisplayName");
        testObject.setDescription("TestDescription");
        testObject.setConnectorType(connectorType);
        testObject.setEndpoint(endpoint);
        testObject.setSecuredProperties(securedProperties);

        return new ConnectionProperties(testObject);
    }


    /**
     * Set up an example object to test. A property from the super class is different.
     *
     * @return filled in object
     */
    private ConnectionProperties getDifferentObject()
    {
        Connection testObject = new Connection();

        testObject.setType(type);
        testObject.setGUID("TestDifferentGUID");
        testObject.setClassifications(classifications);

        testObject.setQualifiedName("TestQualifiedName");
        testObject.setAdditionalProperties(additionalProperties);

        testObject.setDisplayName("TestDisplayName");
        testObject.setDescription("TestDescription");
        testObject.setConnectorType(connectorType);
        testObject.setEndpoint(endpoint);
        testObject.setSecuredProperties(securedProperties);

        return new ConnectionProperties(testObject);
    }


    /**
     * Set up an example object to test. A property from the subclass is different.
     *
     * @return filled in object
     */
    private ConnectionProperties getAnotherDifferentObject()
    {
        Connection testObject = new Connection();

        testObject.setType(type);
        testObject.setGUID("TestGUID");
        testObject.setClassifications(classifications);

        testObject.setQualifiedName("TestQualifiedName");
        testObject.setAdditionalProperties(additionalProperties);

        testObject.setDisplayName("TestDisplayName");
        testObject.setDescription("TestDifferentDescription");
        testObject.setConnectorType(connectorType);
        testObject.setEndpoint(endpoint);
        testObject.setSecuredProperties(securedProperties);

        return new ConnectionProperties(testObject);
    }


    /**
     * Validate that the object that comes out of the test has the same content as the original test object.
     *
     * @param resultObject object returned by the test
     */
    private void validateResultObject(ConnectionProperties  resultObject)
    {
        assertTrue(resultObject.getType().equals(type));
        assertTrue(resultObject.getGUID().equals("TestGUID"));
        assertTrue(resultObject.getClassifications() == null);

        assertTrue(resultObject.getQualifiedName().equals("TestQualifiedName"));
        assertTrue(resultObject.getAdditionalProperties() != null);

        assertTrue(resultObject.getDisplayName().equals("TestDisplayName"));
        assertTrue(resultObject.getDescription().equals("TestDescription"));
        assertTrue(resultObject.getConnectorType().getConnectorTypeBean().equals(connectorType));
        assertTrue(resultObject.getEndpoint().getEndpointBean().equals(endpoint));
        assertTrue(resultObject.getSecuredProperties() != null);
    }


    /**
     * Validate that the object that comes out of the test has the same content as the original test object.
     *
     * @param nullObject object to test
     */
    private void validateNullObject(ConnectionProperties  nullObject)
    {
        assertTrue(nullObject.getType() == null);
        assertTrue(nullObject.getGUID() == null);
        assertTrue(nullObject.getClassifications() == null);

        assertTrue(nullObject.getQualifiedName() == null);
        assertTrue(nullObject.getAdditionalProperties() == null);

        assertTrue(nullObject.getDisplayName() == null);
        assertTrue(nullObject.getDescription() == null);
        assertTrue(nullObject.getConnectorType() == null);
        assertTrue(nullObject.getEndpoint() == null);
        assertTrue(nullObject.getSecuredProperties() == null);
    }


    /**
     * Validate that the object is initialized properly
     */
    @Test public void testNullObject()
    {
        Connection           nullBean;
        ConnectionProperties nullObject;
        ConnectionProperties nullTemplate;
        AssetDescriptor      parentAsset;

        nullBean = null;
        nullObject = new ConnectionProperties(nullBean);
        validateNullObject(nullObject);

        nullBean = new Connection();
        nullObject = new ConnectionProperties(nullBean);
        validateNullObject(nullObject);

        nullBean = new Connection(null);
        nullObject = new ConnectionProperties(nullBean);
        validateNullObject(nullObject);

    }


    /**
     * Validate that secured properties are handled properly.
     */
    @Test public void testSecuredProperties()
    {
        Map<String, String>  propertyMap = new HashMap<>();

        propertyMap.put("property1", "TestString");
        propertyMap.put("property2", "2");

        Connection connectionBean = new Connection();
        connectionBean.setSecuredProperties(propertyMap);

        ConnectionProperties testObject = new ConnectionProperties(connectionBean);

        Map<String, String> securedProperties = testObject.getSecuredProperties();

        assertTrue(securedProperties.keySet() != null);

        Iterator<String> iterator = securedProperties.keySet().iterator();

        String propertyName;

        propertyName = iterator.next();
        assertTrue(propertyName.equals("property2"));
        assertTrue(securedProperties.get(propertyName).equals("2"));

        propertyName = iterator.next();
        assertTrue(propertyName.equals("property1"));
        assertTrue(securedProperties.get(propertyName).equals("TestString"));

        try
        {
            iterator.next();
            assertTrue(false);
        }
        catch (Throwable   exc)
        {
            assertTrue(true);
        }

        connectionBean = new Connection();
        testObject = new ConnectionProperties(connectionBean);

        securedProperties = testObject.getSecuredProperties();

        assertTrue(securedProperties == null);

        propertyMap = new HashMap<>();
        connectionBean = new Connection();
        connectionBean.setSecuredProperties(propertyMap);
        testObject = new ConnectionProperties(connectionBean);

        securedProperties = testObject.getSecuredProperties();

        assertTrue(securedProperties != null);
    }


    @Test public void testConnectionName()
    {
        Connection connectionBean = new Connection();

        ConnectionProperties  testObject = new ConnectionProperties(connectionBean);

        assertTrue(testObject.getConnectionName().equals("<Unknown>"));

        connectionBean = new Connection();
        connectionBean.setQualifiedName("TestQualifiedName");
        testObject = new ConnectionProperties(connectionBean);

        assertTrue(testObject.getConnectionName().equals("TestQualifiedName"));


        connectionBean = new Connection();
        connectionBean.setDisplayName("TestDisplayName");
        testObject = new ConnectionProperties(connectionBean);

        assertTrue(testObject.getConnectionName().equals("TestDisplayName"));


        connectionBean = new Connection();
        connectionBean.setQualifiedName("TestQualifiedName");
        connectionBean.setDisplayName("TestDisplayName");

        testObject = new ConnectionProperties(connectionBean);

        assertTrue(testObject.getConnectionName().equals("TestQualifiedName"));
    }



    /**
     * Test that the endpoint is properly managed
     */
    @Test public void testEndpointProperties()
    {
        Endpoint   endpointBean = new Endpoint();

        endpointBean.setAddress("TestAddress");

        Connection connectionBean = new Connection();
        connectionBean.setEndpoint(endpointBean);

        ConnectionProperties testObject = new ConnectionProperties(connectionBean);
        EndpointProperties   endpointProperties = testObject.getEndpoint();

        assertTrue(endpointProperties.getAddress().equals("TestAddress"));
        assertTrue(endpointProperties.getEndpointBean().getAddress().equals("TestAddress"));
    }


    /**
     * Test that the connectorType is properly managed
     */
    @Test public void testConnectorTypeProperties()
    {
        ConnectorType   connectorTypeBean = new ConnectorType();

        connectorTypeBean.setConnectorProviderClassName("TestClass");

        Connection connectionBean = new Connection();
        connectionBean.setConnectorType(connectorTypeBean);

        ConnectionProperties testObject = new ConnectionProperties(connectionBean);
        ConnectorTypeProperties   connectorTypeProperties = testObject.getConnectorType();

        assertTrue(connectorTypeProperties.getConnectorProviderClassName().equals("TestClass"));
        assertTrue(connectorTypeProperties.getConnectorTypeBean().getConnectorProviderClassName().equals("TestClass"));
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

        ConnectionProperties  sameObject = getTestObject();
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
        validateResultObject(new ConnectionProperties( getTestObject()));
    }

    /**
     * Test that toString is overridden.
     */
    @Test public void testToString()
    {
        assertTrue(getTestObject().toString().contains("Connection"));
    }
}
