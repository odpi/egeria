/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;

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
     *
     * @param connectorClassName name of class for connector
     */
    public MockConnectorProvider(String     connectorClassName)
    {
        super.setConnectorClassName(connectorClassName);
    }


    /**
     * Pass the supplied ConnectorTypeDetails object to the super class.
     *
     * @param connectorType properties from metadata repository
     */
    public void setTestConnectorTypeProperties(ConnectorType connectorType)
    {
        super.setConnectorTypeProperties(connectorType);
    }
}