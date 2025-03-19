/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors;

import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectionCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.properties.ConnectionDetails;

/**
 * MockEmptyConnectorProvider is a badly behaved ConnectorProvider to force error paths in the
 * ConnectorBroker and ConnectorProviderBase.
 */
public class MockEmptyConnectorProvider extends ConnectorProviderBase
{
    private ConnectorCheckedException  connectorCheckedException  = null;
    private ConnectionCheckedException connectionCheckedException = null;
    private RuntimeException           runtimeException           = null;

    /**
     * The constructor sets up the name of the connector class.
     */
    public MockEmptyConnectorProvider()
    {
        super();
    }


    /**
     * Provide control on the connector class name used.
     *
     * @param className class name
     */
    public void  setTestConnectorClassName(String  className)
    {
        super.setConnectorClassName(className);
    }


    /**
     * Provide an exception for getConnector to throw.
     *
     * @param exception exception
     */
    public void  setTestConnectionCheckedException(ConnectionCheckedException  exception)
    {
        this.connectionCheckedException = exception;
    }


    /**
     * Provide an exception for getConnector to throw.
     *
     * @param exception exception
     */
    public void  setTestConnectorCheckedException(ConnectorCheckedException  exception)
    {
        this.connectorCheckedException = exception;
    }


    /**
     * Provide an exception for getConnector to throw.
     *
     * @param exception exception
     */
    public void  setTestRuntimeException(RuntimeException  exception)
    {
        this.runtimeException = exception;
    }


    /**
     * Creates a new instance of a connector based on the information in the supplied connection.
     *
     * @param connection   connection that should have all the properties needed by the Connector Provider
     *                   to create a connector instance.
     * @return Connector   instance of the connector.
     * @throws ConnectionCheckedException if there are missing or invalid properties in the connection
     * @throws ConnectorCheckedException if there are issues instantiating or initializing the connector
     */
    @Override
    public Connector getConnector(ConnectionDetails connection) throws ConnectionCheckedException, ConnectorCheckedException
    {
        if (connectionCheckedException != null)
        {
            throw connectionCheckedException;
        }

        if (connectorCheckedException != null)
        {
            throw connectorCheckedException;
        }

        if (runtimeException != null)
        {
            throw runtimeException;
        }

        return null;
    }
}