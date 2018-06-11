/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.frameworks.connectors;

/**
 * MockConnector is a very simple data connector that can return a single String.
 */
public class MockConnector extends ConnectorBase
{
    private final String   testConnectorData = new String("This is from the mock connector");

    /**
     * Returns the data string.
     *
     * @return String data
     */
    public  String getMockConnectorData()
    {
        return testConnectorData;
    }


    /**
     * Disconnect all resources (nothing to do).
     */
    public void disconnect()
    {

    }
}