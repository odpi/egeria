/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors;

import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectionCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.properties.ConnectionProperties;
import org.odpi.openmetadata.frameworks.connectors.properties.ConnectorTypeProperties;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConfigurationPropertyType;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;

import java.util.List;

/**
 * The ConnectorProvider is a formal plug-in interface for the Open Connector Framework (OCF).  It provides a factory
 * class for a specific type of connector.  Therefore, is it typical to find the ConnectorProvider and Connector
 * implementation written as a pair.
 * The ConnectorProvider uses the properties stored in a Connection object to initialize itself and its Connector instances.
 * the Connection object has the endpoint properties for the server that the connector must communicate with
 * as well as optional additional properties that may be needed for a particular type of connector.
 * It is suggested that the ConnectorProvider validates the contents of the connection and throws
 * ConnectionErrorExceptions if the connection has missing or invalid properties.  If there are errors detected in the
 * instantiations or initialization of the connector, then these should be thrown as ConnectorErrorExceptions.
 */
public abstract class ConnectorProvider
{
    /**
     * The list of supported configuration property types that describe how the connector's
     * behaviour can be modified.
     */
    protected List<ConfigurationPropertyType> supportedConfigurationProperties = null;


    /**
     * Returns the properties about the type of connector that this ConnectorTypeManager supports.
     *
     * @return properties including the name of the connector type, the connector provider class
     * and any specific connection properties that are recognized by this connector.
     */
    public abstract ConnectorTypeProperties getConnectorTypeProperties();


    /**
     * Returns the properties about the type of connector that this ConnectorTypeManager supports.
     *
     * @return properties including the name of the connector type, the connector provider class
     * and any specific connection properties that are recognized by this connector.
     */
    public abstract ConnectorType getConnectorType();


    /**
     * Return the list of supported configuration property types that describe how the connector's
     * behaviour can be modified.
     *
     * @return list of configuration property types
     */
    public List<ConfigurationPropertyType> getSupportedConfigurationProperties()
    {
        return supportedConfigurationProperties;
    }


    /**
     * Creates a new instance of a connector using the information from the connection and the properties
     * from the subclass(es).
     *
     * @param connection   connection that should have all the properties needed by the Connector Provider
     *                   to create a connector instance.
     * @return new connector instance.
     * @throws ConnectionCheckedException an error with the connection.
     * @throws ConnectorCheckedException an error initializing the connector.
     */
    public abstract Connector getConnector(Connection connection) throws ConnectionCheckedException, ConnectorCheckedException;


    /**
     * Creates a new instance of a connector using the information from the connection and the properties
     * from the subclass(es).
     *
     * @param connection   connection that should have all the properties needed by the Connector Provider
     *                   to create a connector instance.
     * @return Connector   instance of the connector.
     * @throws ConnectionCheckedException if there are missing or invalid properties in the connection
     * @throws ConnectorCheckedException if there are issues instantiating or initializing the connector
     */
    public abstract Connector getConnector(ConnectionProperties connection) throws ConnectionCheckedException, ConnectorCheckedException;
}