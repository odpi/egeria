/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors;

import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectionCheckedException;
import org.odpi.openmetadata.frameworks.connectors.properties.ConnectionDetails;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.testng.annotations.Test;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;


/**
 * TestConnectorBroker drives the connector factory capability of the OCF's ConnectorBroker
 */
public class TestConnectorBroker
{
    /**
     * Validate exception for a null connection
     */
    @Test public void testNullConnection()
    {
        ConnectorBroker cb = new ConnectorBroker();

        try
        {
            cb.getConnector((Connection)null);
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
        ConnectorBroker cb = new ConnectorBroker();

        try
        {
            cb.getConnector((ConnectionDetails)null);
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
        ConnectorBroker cb = new ConnectorBroker();

        Connection testConnection = new Connection(null);

        try
        {
            cb.getConnector(testConnection);
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
        ConnectorBroker cb = new ConnectorBroker();

        ConnectionDetails testConnection = new ConnectionDetails((Connection)null);

        try
        {
            cb.getConnector(testConnection);
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
        ConnectorBroker cb = new ConnectorBroker();
        Connection      testConnection = new Connection();

        testConnection.setQualifiedName("Test.Connection");
        testConnection.setDisplayName("Test");

        try
        {
            cb.getConnector(testConnection);
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
        ConnectorBroker cb = new ConnectorBroker();
        Connection      testConnection = new Connection();

        testConnection.setQualifiedName("Test.Connection");
        testConnection.setDisplayName("Test");

        ConnectionDetails testConnectionDetails = new ConnectionDetails(testConnection);

        try
        {
            cb.getConnector(testConnectionDetails);
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
     * Validate exception when the connector provider class is unknown.
     */
    @Test public void testUnknownConnectorProvider()
    {
        ConnectorType testConnType = new ConnectorType();

        testConnType.setQualifiedName("Test.Joke.ConnectorType");
        testConnType.setDisplayName("TestCTJoke");
        testConnType.setConnectorProviderClassName("Joke");

        Connection      testConnection = new Connection();

        testConnection.setQualifiedName("Test.Connection");
        testConnection.setDisplayName("Test");
        testConnection.setConnectorType(testConnType);

        ConnectorBroker cb = new ConnectorBroker();

        try
        {
            cb.getConnector(testConnection);
        }
        catch (ConnectionCheckedException chk)
        {
            assertTrue(chk.getMessage().contains("OCF-CONNECTION-400-005 Unknown Connector Provider class Joke passed in connection Test.Connection"));
        }
        catch (Exception   exc)
        {
            assertTrue(false);
        }
    }



    /**
     * Validate exception when the connector provider class is null.
     */
    @Test public void testNullConnectorProvider()
    {
        ConnectorType testConnType = new ConnectorType();

        testConnType.setQualifiedName("Test.NullConnectorProvider.ConnectorType");
        testConnType.setDisplayName("TestCTNull");

        Connection      testConnection = new Connection();

        testConnection.setQualifiedName("Test.Connection");
        testConnection.setDisplayName("Test");
        testConnection.setConnectorType(testConnType);

        ConnectorBroker cb = new ConnectorBroker();

        try
        {
            cb.getConnector(testConnection);
        }
        catch (ConnectionCheckedException chk)
        {
            assertTrue(chk.getMessage().contains("OCF-CONNECTION-400-004 Null Connector Provider passed in connection Test.Connection"));
        }
        catch (Exception   exc)
        {
            assertTrue(false);
        }
    }


    /**
     * Validate exception when the connector provider class is null.
     */
    @Test public void testUnusableConnectorProvider()
    {
        ConnectorType testConnType = new ConnectorType();

        testConnType.setQualifiedName("Test.UnusableConnectorProvider.ConnectorType");
        testConnType.setDisplayName("TestCTUnusable");
        testConnType.setConnectorProviderClassName(ConnectorProviderBase.class.getName());

        Connection      testConnection = new Connection();

        testConnection.setQualifiedName("Test.Connection");
        testConnection.setDisplayName("Test");
        testConnection.setConnectorType(testConnType);

        ConnectorBroker cb = new ConnectorBroker();

        try
        {
            cb.getConnector(testConnection);
        }
        catch (Exception   exc)
        {
            assertTrue(exc.getMessage().contains("OCF-CONNECTION-400-008 Connector Provider class org.odpi.openmetadata.frameworks.connectors.ConnectorProviderBase passed in connection Test.Connection"));
        }
    }


    /**
     * Validate exception when the connector provider class is null.
     */
    @Test public void testNullConnector()
    {
        ConnectorType testConnType = new ConnectorType();

        testConnType.setQualifiedName("Test.EmptyConnectorProvider.ConnectorType");
        testConnType.setDisplayName("TestCTEmpty");
        testConnType.setConnectorProviderClassName(MockEmptyConnectorProvider.class.getName());

        Connection      testConnection = new Connection();

        testConnection.setQualifiedName("Test.Connection");
        testConnection.setDisplayName("Test");
        testConnection.setConnectorType(testConnType);

        ConnectorBroker cb = new ConnectorBroker();

        try
        {
            cb.getConnector(testConnection);
        }
        catch (Exception   exc)
        {
            assertTrue(exc.getMessage().contains("OCF-CONNECTION-500-011 Connector Provider org.odpi.openmetadata.frameworks.connectors.MockEmptyConnectorProvider returned a null connector instance for connection Test.Connection"));
        }
    }


    /**
     * Validate exception when the connector provider class throws an exception.
     */
    @Test public void testConnectorExceptionConnector()
    {
        ConnectorType testConnType = new ConnectorType();

        testConnType.setQualifiedName("Test.MockConnectorCheckedExceptionConnectorProvider.ConnectorType");
        testConnType.setDisplayName("TestException");
        testConnType.setConnectorProviderClassName(MockConnectorCheckedExceptionConnectorProvider.class.getName());

        Connection      testConnection = new Connection();

        testConnection.setQualifiedName("Test.Connection");
        testConnection.setDisplayName("Test");
        testConnection.setConnectorType(testConnType);

        ConnectorBroker cb = new ConnectorBroker();

        try
        {
            cb.getConnector(testConnection);
        }
        catch (Exception   exc)
        {
            assertTrue(exc.getMessage().contains("OCF-PROPERTIES-400-014"));
        }
    }


    /**
     * Validate exception when the connector provider throws an exception.
     */
    @Test public void testConnectionExceptionConnector()
    {
        ConnectorType testConnType = new ConnectorType();

        testConnType.setQualifiedName("Test.MockConnectionCheckedExceptionConnectorProvider.ConnectorType");
        testConnType.setDisplayName("TestException");
        testConnType.setConnectorProviderClassName(MockConnectionCheckedExceptionConnectorProvider.class.getName());

        Connection      testConnection = new Connection();

        testConnection.setQualifiedName("Test.Connection");
        testConnection.setDisplayName("Test");
        testConnection.setConnectorType(testConnType);

        ConnectorBroker cb = new ConnectorBroker();

        try
        {
            cb.getConnector(testConnection);
        }
        catch (Exception   exc)
        {
            assertTrue(exc.getMessage().contains("OCF-PROPERTIES-400-014"));
        }
    }


    /**
     * Validate exception when the connector provider class throws an exception.
     */
    @Test public void testRuntimeExceptionConnector()
    {
        ConnectorType testConnType = new ConnectorType();

        testConnType.setQualifiedName("Test.MockRuntimeExceptionConnectorProvider.ConnectorType");
        testConnType.setDisplayName("TestException");
        testConnType.setConnectorProviderClassName(MockRuntimeExceptionConnectorProvider.class.getName());

        Connection      testConnection = new Connection();

        testConnection.setQualifiedName("Test.Connection");
        testConnection.setDisplayName("Test");
        testConnection.setConnectorType(testConnType);

        ConnectorBroker cb = new ConnectorBroker();

        try
        {
            cb.getConnector(testConnection);
        }
        catch (Exception   exc)
        {
            assertTrue(exc.getMessage().contains("OCF-CONNECTION-500-001 OCF method detected an unexpected exception"));
        }
    }


    /**
     * Validate exception when the connector provider class is unknown.
     */
    @Test public void testUnknownConnectorProviderProperties()
    {
        ConnectorType testConnType = new ConnectorType();

        testConnType.setQualifiedName("Test.Joke.ConnectorType");
        testConnType.setDisplayName("TestCTJoke");
        testConnType.setConnectorProviderClassName("Joke");

        Connection      testConnection = new Connection();

        testConnection.setQualifiedName("Test.Connection");
        testConnection.setDisplayName("Test");
        testConnection.setConnectorType(testConnType);

        ConnectionDetails testConnectionDetails = new ConnectionDetails(testConnection);

        ConnectorBroker cb = new ConnectorBroker();

        try
        {
            cb.getConnector(testConnectionDetails);
        }
        catch (ConnectionCheckedException chk)
        {
            assertTrue(chk.getMessage().contains("OCF-CONNECTION-400-005 Unknown Connector Provider class Joke passed in connection Test.Connection"));
        }
        catch (Exception   exc)
        {
            assertTrue(false);
        }
    }


    /**
     * Validate exception for an invalid connector provider.  It uses a class name of java.lang.String which is
     * a valid class name by does not implement the ConnectorProvider interface.
     */
    @Test public void testInvalidConnectorProvider()
    {
        ConnectorType testConnType = new ConnectorType();

        testConnType.setQualifiedName("Test.String.ConnectorType");
        testConnType.setDisplayName("TestCTString");
        testConnType.setConnectorProviderClassName("java.lang.String");

        Connection      testConnection = new Connection();

        testConnection.setQualifiedName("Test.Connection");
        testConnection.setDisplayName("Test");
        testConnection.setConnectorType(testConnType);

        ConnectorBroker cb = new ConnectorBroker();

        try
        {
            cb.getConnector(testConnection);
        }
        catch (ConnectionCheckedException chk)
        {
            assertTrue(chk.getMessage().contains("OCF-CONNECTION-400-006 Class java.lang.String passed in connection Test.Connection is not a Connector Provider"));
        }
        catch (Exception   exc)
        {
            assertTrue(false);
        }
    }


    /**
     * Validate exception for an invalid connector provider.  It uses a class name of java.lang.String which is
     * a valid class name by does not implement the ConnectorProvider interface.
     */
    @Test public void testInvalidConnectorProviderProperties()
    {
        ConnectorType testConnType = new ConnectorType();

        testConnType.setQualifiedName("Test.String.ConnectorType");
        testConnType.setDisplayName("TestCTString");
        testConnType.setConnectorProviderClassName("java.lang.String");

        Connection      testConnection = new Connection();

        testConnection.setQualifiedName("Test.Connection");
        testConnection.setDisplayName("Test");
        testConnection.setConnectorType(testConnType);

        ConnectionDetails testConnectionDetails = new ConnectionDetails(testConnection);

        ConnectorBroker cb = new ConnectorBroker();

        try
        {
            cb.getConnector(testConnectionDetails);
        }
        catch (ConnectionCheckedException chk)
        {
            assertTrue(chk.getMessage().contains("OCF-CONNECTION-400-006 Class java.lang.String passed in connection Test.Connection is not a Connector Provider"));
        }
        catch (Exception   exc)
        {
            assertTrue(false);
        }
    }


    /**
     * Validate that a valid connection creates a valid connector
     */
    @Test public void testOKConnection()
    {
        ConnectorBroker cb = new ConnectorBroker();

        ConnectorType testConnType = new ConnectorType();

        testConnType.setQualifiedName("Test.ConnectorType");
        testConnType.setDisplayName("TestCT");
        testConnType.setConnectorProviderClassName(MockConnectorProvider.class.getName());

        Connection      testConnection = new Connection();

        testConnection.setQualifiedName("Test.Connection");
        testConnection.setDisplayName("Test");
        testConnection.setConnectorType(testConnType);

        try
        {
            Connector newConnector = cb.getConnector(testConnection);

            MockConnector mockConnector = (MockConnector)newConnector;
            assertTrue(mockConnector.getMockConnectorData().contains("This is from the mock connector"));
            assertTrue(mockConnector.getConnectedAssetProperties("TestUserId") == null);
        }
        catch (Exception error)
        {
            assertTrue(false);
        }
    }


    /**
     * Validate that a valid connection creates a valid connector
     */
    @Test public void testOKConnectionProperties()
    {
        ConnectorBroker cb = new ConnectorBroker();

        ConnectorType testConnType = new ConnectorType();

        testConnType.setQualifiedName("Test.ConnectorType");
        testConnType.setDisplayName("TestCT");
        testConnType.setConnectorProviderClassName(MockConnectorProvider.class.getName());

        Connection      testConnection = new Connection();

        testConnection.setQualifiedName("Test.Connection");
        testConnection.setDisplayName("Test");
        testConnection.setConnectorType(testConnType);

        ConnectionDetails testConnectionDetails = new ConnectionDetails(testConnection);

        try
        {
            Connector newConnector = cb.getConnector(testConnectionDetails);

            MockConnector mockConnector = (MockConnector)newConnector;
            assertTrue(mockConnector.getMockConnectorData().contains("This is from the mock connector"));
            assertTrue(mockConnector.getConnectedAssetProperties("TestUserId") == null);
        }
        catch (Exception error)
        {
            assertTrue(false);
        }
    }

    /**
     *  Validate that 2 different objects with the same content have the same hash code.
     */
    @Test public void testHashCode()
    {
        assertTrue(new ConnectorBroker().hashCode() != new ConnectorBroker().hashCode());
    }

    /**
     * Validate that 2 different objects with the same content are evaluated as equal.
     * Also that different objects are considered not equal.
     */
    @Test public void testEquals()
    {
        ConnectorBroker    testObject = new ConnectorBroker();
        ConnectorBroker    differentObject = new ConnectorBroker();
        ConnectorBroker    nullObject = null;
        String             wrongClassObject = "pretendConnectorBroker";

        assertTrue(testObject.equals(testObject));

        assertFalse(testObject.equals(null));
        assertFalse(testObject.equals(wrongClassObject));
        assertFalse(testObject.equals(differentObject));
        assertFalse(testObject.equals(nullObject));
    }


    /**
     * Ensure toString works if the method is not overridden in the subclass.
     */
    @Test public void testToString()
    {
        assertTrue(new ConnectorBroker().toString().contains("ConnectorBroker"));
    }
}
