/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors;


import org.odpi.openmetadata.frameworks.connectors.properties.ConnectionProperties;
import org.odpi.openmetadata.frameworks.connectors.properties.MockConnectedAssetProperties;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * TestConnectorProviderBase validates that the connector provider base class provides the
 * appropriate services to a connector provider implementation.  It uses the MockConnectorProvider
 * class as part of the tests
 */
public class TestConnectorBase
{
    /**
     * Return a filled out connection
     *
     * @return connectionProperties object
     */
    private ConnectionProperties   getOKConnection()
    {
        ConnectorType testConnType = new ConnectorType();

        testConnType.setQualifiedName("Test.ConnectorType");
        testConnType.setDisplayName("TestCT");
        testConnType.setConnectorProviderClassName(MockConnectorProvider.class.getName());

        Connection      testConnection = new Connection();

        testConnection.setQualifiedName("Test.Connection");
        testConnection.setDisplayName("Test");
        testConnection.setConnectorType(testConnType);

        return new ConnectionProperties(testConnection);
    }


    /**
     * Return a filled out connection
     *
     * @return connectionProperties object
     */
    private ConnectionProperties   getSecuredConnection()
    {
        ConnectorType testConnType = new ConnectorType();

        testConnType.setQualifiedName("Test.ConnectorType");
        testConnType.setDisplayName("TestCT");
        testConnType.setConnectorProviderClassName(MockConnectorProvider.class.getName());

        Connection      testConnection = new Connection();

        testConnection.setQualifiedName("Test.Connection");
        testConnection.setDisplayName("Test");
        testConnection.setConnectorType(testConnType);

        Map<String, String>  securedProperties = new HashMap<>();
        securedProperties.put("testProperty", "testPropertyValue");
        testConnection.setSecuredProperties(securedProperties);

        return new ConnectionProperties(testConnection);
    }


    @Test public void testLifecycle()
    {
        String         connectorInstanceId = UUID.randomUUID().toString();
        ConnectionProperties connectionProperties = getOKConnection();

        ConnectorBase  testObject = new MockConnector();

        assertFalse(testObject.isActive());

        testObject.initialize(connectorInstanceId, connectionProperties);

        assertFalse(testObject.isActive());
        assertTrue(connectorInstanceId.equals(testObject.getConnectorInstanceId()));
        assertTrue(connectionProperties.equals(testObject.getConnection()));

        try
        {
            testObject.start();
            assertTrue(true);
        }
        catch (Throwable   exc)
        {
            assertTrue(false);
        }

        assertTrue(testObject.isActive());

        try
        {
            assertTrue(testObject.getConnectedAssetProperties("TestUserId") == null);
        }
        catch (Throwable   exc)
        {
            assertTrue(false);
        }

        testObject.initializeConnectedAssetProperties(new MockConnectedAssetProperties());

        try
        {
            assertTrue(testObject.getConnectedAssetProperties("TestUserId") != null);
        }
        catch (Throwable   exc)
        {
            assertTrue(false);
        }

        assertTrue(testObject.isActive());

        try
        {
            testObject.disconnect();
            assertTrue(true);
        }
        catch (Throwable   exc)
        {
            assertTrue(false);
        }

        assertFalse(testObject.isActive());
    }


    @Test
    public void testSecureConnection()
    {
        String               connectorInstanceId = UUID.randomUUID().toString();
        ConnectionProperties connectionProperties = getSecuredConnection();

        ConnectorBase  testObject = new MockConnector();

        assertFalse(testObject.isActive());

        testObject.initialize(connectorInstanceId, connectionProperties);

        assertFalse(testObject.isActive());
        assertTrue(connectorInstanceId.equals(testObject.getConnectorInstanceId()));
        assertTrue(connectionProperties.equals(testObject.getConnection()));
    }


    /**
     *  Validate that 2 different objects with the same content have the same hash code.
     */
    @Test public void testHashCode()
    {
        assertTrue(new MockConnector().hashCode() != new MockConnector().hashCode());
    }


    @Test public void testEquals()
    {
        ConnectorBase   testObject = new MockConnector();
        ConnectorBase   anotherTestObject = new MockConnector();

        assertTrue(testObject.equals(testObject));
        assertFalse(testObject.equals(null));
        assertFalse(testObject.equals("String"));
        assertFalse(testObject.equals(anotherTestObject));
    }


    /**
     * Ensure toString works if the method is not overridden in the subclass.
     */
    @Test public void testToString()
    {
        assertTrue(new MockConnector().toString().contains("ConnectorBase"));
    }
}
