/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors;

/**
 * MockConnector is a very simple data connector that can return a single String.
 */
public class MockConnector extends ConnectorBase
{
    private final String   testConnectorData = new String("This is from the mock connector");

    /**
     * Default constructor
     */
    public MockConnector()
    {
        super();
    }


    /**
     * Returns the data string.
     *
     * @return String data
     */
    public  String getMockConnectorData()
    {
        return testConnectorData;
    }
}