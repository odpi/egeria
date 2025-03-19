/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors;


import org.odpi.openmetadata.frameworks.connectors.properties.ConnectionDetails;
import org.odpi.openmetadata.frameworks.connectors.properties.MockConnectedAssetDetails;
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
     * @return connectionDetails object
     */
    private ConnectionDetails getOKConnection()
    {
        ConnectorType testConnType = new ConnectorType();

        testConnType.setQualifiedName("Test.ConnectorType");
        testConnType.setDisplayName("TestCT");
        testConnType.setConnectorProviderClassName(MockConnectorProvider.class.getName());

        Connection      testConnection = new Connection();

        testConnection.setQualifiedName("Test.Connection");
        testConnection.setDisplayName("Test");
        testConnection.setConnectorType(testConnType);

        return new ConnectionDetails(testConnection);
    }


    /**
     * Return a filled out connection
     *
     * @return connectionDetails object
     */
    private ConnectionDetails getSecuredConnection()
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

        return new ConnectionDetails(testConnection);
    }


    @Test public void testLifecycle()
    {
        String            connectorInstanceId = UUID.randomUUID().toString();
        ConnectionDetails connectionDetails   = getOKConnection();

        ConnectorBase  testObject = new MockConnector();

        assertFalse(testObject.isActive());

        testObject.initialize(connectorInstanceId, connectionDetails);

        assertFalse(testObject.isActive());
        assertTrue(connectorInstanceId.equals(testObject.getConnectorInstanceId()));
        assertTrue(connectionDetails.equals(testObject.getConnection()));

        try
        {
            testObject.start();
            assertTrue(true);
        }
        catch (Exception   exc)
        {
            assertTrue(false);
        }

        assertTrue(testObject.isActive());

        try
        {
            assertTrue(testObject.getConnectedAssetProperties("TestUserId") == null);
        }
        catch (Exception   exc)
        {
            assertTrue(false);
        }

        testObject.initializeConnectedAssetProperties(new MockConnectedAssetDetails());

        try
        {
            assertTrue(testObject.getConnectedAssetProperties("TestUserId") != null);
        }
        catch (Exception   exc)
        {
            assertTrue(false);
        }

        assertTrue(testObject.isActive());

        try
        {
            testObject.disconnect();
            assertTrue(true);
        }
        catch (Exception   exc)
        {
            assertTrue(false);
        }

        assertFalse(testObject.isActive());
    }


    @Test
    public void testSecureConnection()
    {
        String            connectorInstanceId = UUID.randomUUID().toString();
        ConnectionDetails connectionDetails   = getSecuredConnection();

        ConnectorBase  testObject = new MockConnector();

        assertFalse(testObject.isActive());

        testObject.initialize(connectorInstanceId, connectionDetails);

        assertFalse(testObject.isActive());
        assertTrue(connectorInstanceId.equals(testObject.getConnectorInstanceId()));
        assertTrue(connectionDetails.equals(testObject.getConnection()));
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
