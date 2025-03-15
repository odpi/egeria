/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors;


import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectionCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.properties.ConnectionProperties;
import org.odpi.openmetadata.frameworks.connectors.properties.ConnectorTypeProperties;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

/**
 * TestConnectorProviderBase validates that the connector provider base class provides the
 * appropriate services to a connector provider implementation.  It uses the MockConnectorProvider
 * class as part of the tests
 */
public class TestConnectorProviderBase
{

    /**
     * Test that the connectorTypeProperties are returned to the caller
     */
    @Test public void testConnectorTypeProperties()
    {
        ConnectorType            connectorTypeBean       = new ConnectorType();
        ConnectorTypeProperties  connectorTypeProperties = new ConnectorTypeProperties(connectorTypeBean);
        MockConnectorProvider    mockConnectorProvider   = new MockConnectorProvider();

        mockConnectorProvider.setTestConnectorTypeProperties(connectorTypeBean);

        assertTrue(mockConnectorProvider.getConnectorTypeProperties().equals(connectorTypeProperties));
    }


    @Test public void testConnectorClassName()
    {
        MockConnectorProvider    mockConnectorProvider   = new MockConnectorProvider();

        assertTrue(MockConnector.class.getName().equals(mockConnectorProvider.getConnectorClassName()));
    }


    /**
     * Validate exception for a null connection
     */
    @Test public void testNullConnection()
    {
        MockConnectorProvider testObject = new MockConnectorProvider();

        try
        {
            testObject.getConnector((Connection)null);
        }
        catch (ConnectionCheckedException chk)
        {
            assertTrue(chk.getMessage().contains("OCF-CONNECTION-400-001 Null connection object passed on request for new connector instance"));
        }
        catch (Exception   exc)
        {
            assertTrue(false);
        }
    }


    /**
     * Validate exception for a null connection
     */
    @Test public void testNullConnectionProperties()
    {
        MockConnectorProvider testObject = new MockConnectorProvider();

        try
        {
            testObject.getConnector((ConnectionProperties)null);
        }
        catch (ConnectionCheckedException chk)
        {
            assertTrue(chk.getMessage().contains("OCF-CONNECTION-400-001 Null connection object passed on request for new connector instance"));
        }
        catch (Exception   exc)
        {
            assertTrue(false);
        }
    }


    /**
     * Validate exception if connection properties are all null.
     */
    @Test public void testEmptyConnection()
    {
        MockConnectorProvider testObject = new MockConnectorProvider();

        Connection testConnection = new Connection(null);

        try
        {
            testObject.getConnector(testConnection);
        }
        catch (ConnectionCheckedException chk)
        {
            assertTrue(chk.getMessage().contains("OCF-CONNECTION-400-003 Null connectorType property passed in connection <Unknown>"));
        }
        catch (Exception   exc)
        {
            assertTrue(false);
        }
    }


    /**
     * Validate exception if connection properties are all null.
     */
    @Test public void testEmptyConnectionProperties()
    {
        MockConnectorProvider testObject = new MockConnectorProvider();

        ConnectionProperties testConnection = new ConnectionProperties((Connection)null);

        try
        {
            testObject.getConnector(testConnection);
        }
        catch (ConnectionCheckedException chk)
        {
            assertTrue(chk.getMessage().contains("OCF-CONNECTION-400-003 Null connectorType property passed in connection <Unknown>"));
        }
        catch (Exception   exc)
        {
            assertTrue(false);
        }
    }


    /**
     * Validate exception for a connection that only has its name set up.
     */
    @Test public void testEmptyNamedConnection()
    {
        MockConnectorProvider testObject = new MockConnectorProvider();
        Connection      testConnection = new Connection();

        testConnection.setQualifiedName("Test.Connection");
        testConnection.setDisplayName("Test");

        try
        {
            testObject.getConnector(testConnection);
        }
        catch (ConnectionCheckedException chk)
        {
            assertTrue(chk.getMessage().contains("OCF-CONNECTION-400-003 Null connectorType property passed in connection Test.Connection"));
        }
        catch (Exception   exc)
        {
            assertTrue(false);
        }
    }


    /**
     * Validate exception for a connection that only has its name set up.
     */
    @Test public void testEmptyNamedConnectionProperties()
    {
        MockConnectorProvider testObject = new MockConnectorProvider();
        Connection      testConnection = new Connection();

        testConnection.setQualifiedName("Test.Connection");
        testConnection.setDisplayName("Test");

        ConnectionProperties testConnectionProperties = new ConnectionProperties(testConnection);

        try
        {
            testObject.getConnector(testConnectionProperties);
        }
        catch (ConnectionCheckedException chk)
        {
            assertTrue(chk.getMessage().contains("OCF-CONNECTION-400-003 Null connectorType property passed in connection Test.Connection"));
        }
        catch (Exception   exc)
        {
            assertTrue(false);
        }
    }


    /**
     * Return a filled out connection
     *
     * @param connectorProviderClass name of connector provider to use
     * @return connection object
     */
    private Connection   getOKConnection(String    connectorProviderClass)
    {
        ConnectorType testConnType = new ConnectorType();

        testConnType.setQualifiedName("Test.ConnectorType");
        testConnType.setDisplayName("TestCT");
        testConnType.setConnectorProviderClassName(connectorProviderClass);

        Connection      testConnection = new Connection();

        testConnection.setQualifiedName("Test.Connection");
        testConnection.setDisplayName("Test");
        testConnection.setConnectorType(testConnType);

        return testConnection;
    }



    /**
     * Validate that a valid connection creates a valid connector
     */
    @Test public void testOKConnection()
    {
        MockConnectorProvider testObject = new MockConnectorProvider();

        try
        {
            Connector newConnector = testObject.getConnector(getOKConnection(MockConnectorProvider.class.getName()));

            MockConnector mockConnector = (MockConnector)newConnector;
            assertTrue(mockConnector.getMockConnectorData().contains("This is from the mock connector"));
            assertTrue(mockConnector.getConnectedAssetProperties("TestUserId") == null);
        }
        catch (Throwable error)
        {
            assertTrue(false);
        }
    }



    /**
     * Validate that a valid connection creates a valid connector
     */
    @Test public void testOKConnectionProperties()
    {
        MockConnectorProvider testObject = new MockConnectorProvider();

        ConnectionProperties testConnectionProperties = new ConnectionProperties(getOKConnection(MockConnectorProvider.class.getName()));

        try
        {
            Connector newConnector = testObject.getConnector(testConnectionProperties);

            MockConnector mockConnector = (MockConnector)newConnector;
            assertTrue(mockConnector.getMockConnectorData().contains("This is from the mock connector"));
            assertTrue(mockConnector.getConnectedAssetProperties("TestUserId") == null);
        }
        catch (Throwable error)
        {
            assertTrue(false);
        }
    }


    /**
     * Verify that an exception is thrown if the connector class name is null.
     */
    @Test public void testNullConnectorClassName()
    {
        ConnectorProviderBase  testObject = new MockConnectorProvider();

        testObject.setConnectorClassName(null);

        try
        {
            testObject.getConnector(getOKConnection(MockConnectorProvider.class.getName()));
            assertTrue(false);
        }
        catch (Throwable error)
        {
            assertTrue(error.getMessage().contains("OCF-CONNECTOR-500-006 The class name for the connector is not set up"));
        }
    }


    /**
     * Verify that an exception is thrown if the connector class name is not a class name.
     */
    @Test public void testJokeConnectorClassName()
    {
        ConnectorProviderBase  testObject = new MockConnectorProvider();

        testObject.setConnectorClassName("Joke");

        try
        {
            testObject.getConnector(getOKConnection(MockConnectorProvider.class.getName()));
            assertTrue(false);

        }
        catch (Throwable error)
        {
            assertTrue(error.getMessage().contains("OCF-CONNECTOR-500-007 Unknown Connector Java class Joke"));
        }
    }


    /**
     * Verify that an exception is thrown if the connector class name is not a connector class name.
     */
    @Test public void testStringConnectorClassName()
    {
        ConnectorProviderBase  testObject = new MockConnectorProvider();

        testObject.setConnectorClassName("java.lang.String");

        try
        {
            testObject.getConnector(getOKConnection(MockConnectorProvider.class.getName()));
            assertTrue(false);

        }
        catch (Throwable error)
        {
            assertTrue(error.getMessage().contains("OCF-CONNECTOR-500-008 Java class java.lang.String for connector named Test.Connection does not implement the Connector interface"));
        }
    }


    /**
     * Verify that an exception is thrown if the connector throws a runtime exception in initialize().
     */
    @Test public void testRuntimeExceptionConnector()
    {
        ConnectorProviderBase  testObject = new MockConnectorProvider();

        testObject.setConnectorClassName(MockRuntimeExceptionConnector.class.getName());

        try
        {
            testObject.getConnector(getOKConnection(MockConnectorProvider.class.getName()));
            assertTrue(false);

        }
        catch (Throwable error)
        {
            assertTrue(error.getMessage().contains("OCF-CONNECTION-500-010 Invalid Connector class org.odpi.openmetadata.frameworks.connectors.MockRuntimeExceptionConnector"));
        }
    }


    /**
     *  Validate that 2 different objects with the same content have the same hash code.
     */
    @Test public void testHashCode()
    {
        assertTrue(new MockConnectorProvider().hashCode() != new MockConnectorProvider().hashCode());
    }


    @Test public void testEquals()
    {
        ConnectorProviderBase   testObject = new MockConnectorProvider();
        ConnectorProviderBase   anotherTestObject = new MockConnectorProvider();

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
        assertTrue(new MockConnectorProvider().toString().contains("ConnectorProviderBase"));
    }
}
