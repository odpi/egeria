/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.frameworks.connectors;

import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectionCheckedException;
import org.odpi.openmetadata.frameworks.connectors.properties.Connection;
import org.odpi.openmetadata.frameworks.connectors.properties.ConnectorType;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.assertTrue;

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
            cb.getConnector(null);
        }
        catch (ConnectionCheckedException chk)
        {
            assertTrue(chk.getMessage().contains("OCF-CONNECTION-400-001 Null connection object passed on request for new connector instance"));
        }
        catch (Throwable   exc)
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
        catch (Throwable   exc)
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

        Connection testConnection = new Connection(null,
                                                   null,
                                                   null,
                                                   null,
                                                   "Test.Connection",
                                                   null,
                                                   null,
                                                   "Test",
                                                   null,
                                                   null,
                                                   null,
                                                   null);

        try
        {
            cb.getConnector(testConnection);
        }
        catch (ConnectionCheckedException chk)
        {
            assertTrue(chk.getMessage().contains("OCF-CONNECTION-400-003 Null connectorType property passed in connection Test.Connection"));
        }
        catch (Throwable   exc)
        {
            assertTrue(false);
        }
    }


    /**
     * Validate exception when the connector provider class is unknown.
     */
    @Test public void testUnknownConnectorProvider()
    {
        ConnectorBroker cb = new ConnectorBroker();

        ConnectorType testConnType = new ConnectorType(null,
                                                       null,
                                                       null,
                                                       null,
                                                       "Test.ConnectorType",
                                                       null,
                                                       null,
                                                       "TestCT",
                                                       null,
                                                       "Joke");

        Connection testConnection = new Connection(null,
                                                   null,
                                                   null,
                                                   null,
                                                   "Test.Connection",
                                                   null,
                                                   null,
                                                   "Test",
                                                   null,
                                                   testConnType,
                                                   null,
                                                   null);

        try
        {
            cb.getConnector(testConnection);
        }
        catch (ConnectionCheckedException chk)
        {
            assertTrue(chk.getMessage().contains("OCF-CONNECTION-400-005 Unknown Connector Provider class Joke passed in connection Test.Connection"));
        }
        catch (Throwable   exc)
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
        ConnectorType testConnType;
        Connection testConnection;

        testConnType = new ConnectorType(null,
                                         null,
                                         null,
                                         null,
                                         "Test.String.ConnectorType",
                                         null,
                                         null,
                                         "TestCT",
                                         null,
                                         "java.lang.String");

        testConnection = new Connection(null,
                                        null,
                                        null,
                                        null,
                                        "Test.Connection",
                                        null,
                                        null,
                                        "Test",
                                        null,
                                        testConnType,
                                        null,
                                        null);

        ConnectorBroker cb = new ConnectorBroker();

        try
        {
            cb.getConnector(testConnection);
        }
        catch (ConnectionCheckedException chk)
        {
            assertTrue(chk.getMessage().contains("OCF-CONNECTION-400-006 Class java.lang.String passed in connection Test.Connection is not a Connector Provider"));
        }
        catch (Throwable   exc)
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

        ConnectorType testConnType = new ConnectorType(null,
                                                       null,
                                                       null,
                                                       null,
                                                       "Test.ConnectorType",
                                                       null,
                                                       null,
                                                       "TestCT",
                                                       null,
                                                       MockConnectorProvider.class.getName());

        Connection testConnection = new Connection(null,
                                                   null,
                                                   null,
                                                   null,
                                                   "Test.Connection",
                                                   null,
                                                   null,
                                                   "Test",
                                                   null,
                                                   testConnType,
                                                   null,
                                                   null);

        try
        {
            Connector newConnector = cb.getConnector(testConnection);

            MockConnector mockConnector = (MockConnector)newConnector;
            assertTrue(mockConnector.getMockConnectorData().contains("This is from the mock connector"));
            assertTrue(mockConnector.getConnectedAssetProperties() == null);
        }
        catch (Throwable error)
        {
            assertTrue(false);
        }
    }
}
