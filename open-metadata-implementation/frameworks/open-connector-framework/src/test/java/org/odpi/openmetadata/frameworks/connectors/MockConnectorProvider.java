/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.frameworks.connectors;

/**
 * MockConnectorProvider is the simplest connector provider implementation possible that will
 * create a valid connector of type MockConnector.
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
}