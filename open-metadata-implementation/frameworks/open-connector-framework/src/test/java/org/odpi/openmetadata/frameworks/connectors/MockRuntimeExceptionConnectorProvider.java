/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors;

import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectionCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.OCFErrorCode;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.OMFRuntimeException;
import org.odpi.openmetadata.frameworks.connectors.properties.ConnectionDetails;

/**
 * MockEmptyConnectorProvider is a badly behaved ConnectorProvider to force error paths in the
 * ConnectorBroker and ConnectorProviderBase.
 */
public class MockRuntimeExceptionConnectorProvider extends ConnectorProviderBase
{
    /**
     * The constructor sets up the name of the connector class.
     */
    public MockRuntimeExceptionConnectorProvider()
    {
        super();
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
        throw new OMFRuntimeException(OCFErrorCode.NO_MORE_ELEMENTS.getMessageDefinition("IteratorName"),
                                      this.getClass().getName(),
                                      "getCachedList");
    }
}