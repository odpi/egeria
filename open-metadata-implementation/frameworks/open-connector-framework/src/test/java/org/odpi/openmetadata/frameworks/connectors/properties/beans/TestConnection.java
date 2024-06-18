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
 * Validate that the Connection bean can be cloned, compared, serialized, deserialized and printed as a String.
 */
public class TestConnection
{
    private ElementType                 type                    = new ElementType();
    private List<ElementClassification> classifications         = new ArrayList<>();
    private Map<String, String>         additionalProperties    = new HashMap<>();
    private Map<String, Object>         configurationProperties = new HashMap<>();
    private Map<String, String>         securedProperties       = new HashMap<>();
    private ConnectorType               connectorType           = new ConnectorType();
    private Endpoint                    endpoint                = new Endpoint();


    /**
     * Default constructor
     */
    public TestConnection()
    {

    }


    /**
     * Set up an example object to test.
     *
     * @return filled in object
     */
    private Connection getTestObject()
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
        testObject.setConfigurationProperties(configurationProperties);
        testObject.setSecuredProperties(securedProperties);

        return testObject;
    }


    /**
     * Validate that the object that comes out of the test has the same content as the original test object.
     *
     * @param resultObject object returned by the test
     */
    private void validateResultObject(Connection resultObject)
    {
        assertTrue(resultObject.getType().equals(type));
        assertTrue(resultObject.getGUID().equals("TestGUID"));
        assertTrue(resultObject.getClassifications() == null);

        assertTrue(resultObject.getQualifiedName().equals("TestQualifiedName"));
        assertTrue(resultObject.getAdditionalProperties() == null);

        assertTrue(resultObject.getDisplayName().equals("TestDisplayName"));
        assertTrue(resultObject.getDescription().equals("TestDescription"));
        assertTrue(resultObject.getConnectorType().equals(connectorType));
        assertTrue(resultObject.getEndpoint().equals(endpoint));
        assertTrue(resultObject.getConfigurationProperties() == null);
        assertTrue(resultObject.getSecuredProperties() == null);
    }


    /**
     * Validate that a connection type object is returned.
     */
    @Test public void  testConnectionType()
    {
       assertTrue(Connection.getConnectionType() != null);
    }


    /**
     * Validate that the object is initialized properly
     */
    @Test public void testNullObject()
    {
        Connection nullObject = new Connection();

        assertTrue(nullObject.getType() == null);
        assertTrue(nullObject.getGUID() == null);
        assertTrue(nullObject.getClassifications() == null);

        assertTrue(nullObject.getQualifiedName() == null);
        assertTrue(nullObject.getAdditionalProperties() == null);

        assertTrue(nullObject.getDisplayName() == null);
        assertTrue(nullObject.getDescription() == null);
        assertTrue(nullObject.getConnectorType() == null);
        assertTrue(nullObject.getEndpoint() == null);
        assertTrue(nullObject.getConfigurationProperties() == null);
        assertTrue(nullObject.getSecuredProperties() == null);

        nullObject = new Connection(null);

        assertTrue(nullObject.getType() == null);
        assertTrue(nullObject.getGUID() == null);
        assertTrue(nullObject.getClassifications() == null);

        assertTrue(nullObject.getQualifiedName() == null);
        assertTrue(nullObject.getAdditionalProperties() == null);

        assertTrue(nullObject.getDisplayName() == null);
        assertTrue(nullObject.getDescription() == null);
        assertTrue(nullObject.getConnectorType() == null);
        assertTrue(nullObject.getEndpoint() == null);
        assertTrue(nullObject.getConfigurationProperties() == null);
        assertTrue(nullObject.getSecuredProperties() == null);
    }


    /**
     * Validate that configuration properties are managed properly
     */
    @Test public void testConfigurationProperties()
    {
        Map<String, Object> propertyMap;
        Connection          testObject = new Connection();

        assertTrue(testObject.getConfigurationProperties() == null);

        propertyMap = null;
        testObject = new Connection();
        testObject.setConfigurationProperties(propertyMap);

        assertTrue(testObject.getConfigurationProperties() == null);

        propertyMap = new HashMap<>();
        testObject = new Connection();
        testObject.setConfigurationProperties(propertyMap);

        assertTrue(testObject.getConfigurationProperties() == null);

        propertyMap.put("propertyName", "propertyValue");
        testObject = new Connection();
        testObject.setConfigurationProperties(propertyMap);

        Map<String, Object>   retrievedPropertyMap = testObject.getConfigurationProperties();

        assertTrue(retrievedPropertyMap != null);
        assertFalse(retrievedPropertyMap.isEmpty());
        assertTrue("propertyValue".equals(retrievedPropertyMap.get("propertyName")));
    }


    /**
     * Validate that secure properties are managed properly
     */
    @Test public void testSecuredProperties()
    {
        Map<String, String> propertyMap;
        Connection          testObject = new Connection();

        assertTrue(testObject.getSecuredProperties() == null);

        propertyMap = null;
        testObject = new Connection();
        testObject.setSecuredProperties(propertyMap);

        assertTrue(testObject.getSecuredProperties() == null);

        propertyMap = new HashMap<>();
        testObject = new Connection();
        testObject.setSecuredProperties(propertyMap);

        assertTrue(testObject.getSecuredProperties() == null);

        propertyMap.put("propertyName", "propertyValue");
        testObject = new Connection();
        testObject.setSecuredProperties(propertyMap);

        Map<String, String>   retrievedPropertyMap = testObject.getSecuredProperties();

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

        Connection sameObject = getTestObject();
        assertTrue(sameObject.equals(sameObject));

        Connection differentObject = getTestObject();
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
        validateResultObject(new Connection(getTestObject()));
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
            validateResultObject(objectMapper.readValue(jsonString, Connection.class));
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
            validateResultObject((Connection) objectMapper.readValue(jsonString, Referenceable.class));
        }
        catch (Throwable  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }

        /*
         * Through superclass
         */
        ElementBase elementBase = getTestObject();

        try
        {
            jsonString = objectMapper.writeValueAsString(elementBase);
        }
        catch (Throwable  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }

        try
        {
            validateResultObject((Connection) objectMapper.readValue(jsonString, ElementBase.class));
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
            validateResultObject((Connection) objectMapper.readValue(jsonString, PropertyBase.class));
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
        assertTrue(getTestObject().toString().contains("Connection"));
    }
}
