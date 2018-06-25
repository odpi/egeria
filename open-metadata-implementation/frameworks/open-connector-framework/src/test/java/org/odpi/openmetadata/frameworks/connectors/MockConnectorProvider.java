/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.frameworks.connectors;

import org.odpi.openmetadata.frameworks.connectors.properties.ConnectorTypeProperties;

/**
 * MockConnectorProvider is the simplest connector provider implementation possible that will
 * create a valid connector of type MockConnector.  It is used in the testing of the
 * ConnectorProviderBase.
 */
public class MockConnectorProvider extends ConnectorProviderBase
{
    /**
     * The constructor sets up the name of the connector class.
     */
    public MockConnectorProvider()
    {
        super.setConnectorClassName(MockConnector.class.getName());
    }


    /**
     * The constructor sets up the name of the connector class.
     */
    public MockConnectorProvider(String     connectorClassName)
    {
        super.setConnectorClassName(connectorClassName);
    }


    /**
     * Pass the supplied ConnectorTypeProperties object to the super class.
     *
     * @param connectorTypeProperties
     */
    public void setTestConnectorTypeProperties(ConnectorTypeProperties connectorTypeProperties)
    {
        super.setConnectorTypeProperties(connectorTypeProperties);
    }
}