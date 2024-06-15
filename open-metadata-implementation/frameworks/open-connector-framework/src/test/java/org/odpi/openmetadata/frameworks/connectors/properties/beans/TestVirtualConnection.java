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
 * Validate that the VirtualConnection bean can be cloned, compared, serialized, deserialized and printed as a String.
 */
public class TestVirtualConnection
{
    private ElementType                 type                 = new ElementType();
    private List<ElementClassification> classifications      = new ArrayList<>();
    private Map<String, String>         additionalProperties = new HashMap<>();
    private Map<String, String>         securedProperties    = new HashMap<>();
    private ConnectorType               connectorType        = new ConnectorType();
    private Endpoint                    endpoint             = new Endpoint();
    private List<EmbeddedConnection>    embeddedConnections  = new ArrayList<>();


    /**
     * Default constructor
     */
    public TestVirtualConnection()
    {
        embeddedConnections.add(new EmbeddedConnection());
    }


    /**
     * Set up an example object to test.
     *
     * @return filled in object
     */
    private VirtualConnection getTestObject()
    {
        VirtualConnection testObject = new VirtualConnection();

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

        testObject.setEmbeddedConnections(embeddedConnections);

        return testObject;
    }


    /**
     * Validate that the object that comes out of the test has the same content as the original test object.
     *
     * @param resultObject object returned by the test
     */
    private void validateResultObject(VirtualConnection resultObject)
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
        assertTrue(resultObject.getSecuredProperties() == null);

        assertTrue(resultObject.getEmbeddedConnections() != null);
    }


    /**
     * Validate that a virtual connection type object is returned.
     */
    @Test public void  testVirtualConnectionType()
    {
        assertTrue(VirtualConnection.getVirtualConnectionType() != null);
    }


    /**
     * Validate that the object is initialized properly
     */
    @Test public void testNullObject()
    {
        VirtualConnection nullObject = new VirtualConnection();

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

        assertTrue(nullObject.getEmbeddedConnections() == null);

        nullObject = new VirtualConnection(null);

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

        assertTrue(nullObject.getEmbeddedConnections() == null);

        nullObject = new VirtualConnection();

        nullObject.setEmbeddedConnections(new ArrayList<>());

        assertTrue(nullObject.getEmbeddedConnections() == null);
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

        VirtualConnection sameObject = getTestObject();
        assertTrue(sameObject.equals(sameObject));

        VirtualConnection differentObject = getTestObject();
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
        validateResultObject(new VirtualConnection(getTestObject()));
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
            validateResultObject(objectMapper.readValue(jsonString, VirtualConnection.class));
        }
        catch (Throwable  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }

        /*
         * Through superclass
         */
        Connection connection = getTestObject();

        try
        {
            jsonString = objectMapper.writeValueAsString(connection);
        }
        catch (Throwable  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }

        try
        {
            validateResultObject((VirtualConnection) objectMapper.readValue(jsonString, Connection.class));
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
            validateResultObject((VirtualConnection) objectMapper.readValue(jsonString, Referenceable.class));
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
            validateResultObject((VirtualConnection) objectMapper.readValue(jsonString, ElementBase.class));
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
            validateResultObject((VirtualConnection) objectMapper.readValue(jsonString, PropertyBase.class));
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
        assertTrue(getTestObject().toString().contains("VirtualConnection"));
    }
}
