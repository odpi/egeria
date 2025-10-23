/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors;

import org.odpi.openmetadata.frameworks.connectors.controls.SupportedTechnologyType;
import org.odpi.openmetadata.frameworks.openmetadata.specificationproperties.TemplateType;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectionCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.openmetadata.specificationproperties.ConfigurationPropertyType;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;

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
     * The list of supported templates that are used by this connector when it creates new
     * open metadata elements.
     */
    protected List<TemplateType> supportedTemplateTypes = null;


    /**
     * The list of technology types that this connector supports.
     */
    protected List<SupportedTechnologyType> supportedTechnologyTypes = null;


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
     * Return the list of supported template types that describe the templates used by the connector
     * when it is creating new elements.
     *
     * @return list of template types
     */
    public List<TemplateType> getSupportedTemplateTypes()
    {
        return supportedTemplateTypes;
    }


    /**
     * Return the list of technology types supported by this connector.
     *
     * @return technology types
     */
    public List<SupportedTechnologyType> getSupportedTechnologyTypes()
    {
        return supportedTechnologyTypes;
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
    public abstract Connector getConnector(Connection connection) throws ConnectionCheckedException, ConnectorCheckedException, UserNotAuthorizedException;
}