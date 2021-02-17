/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.multitenant;

import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.multitenant.ffdc.OMAGServerInstanceErrorCode;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ConnectorProvider;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectionCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;

/**
 * GovernanceServerServiceInstanceHandler provides the base class for a governance
 * server's instance handler.
 */
public class GovernanceServerServiceInstanceHandler extends AuditableServerServiceInstanceHandler
{
    protected final InvalidParameterHandler invalidParameterHandler = new InvalidParameterHandler();

    /**
     * Constructor passes the service name that is used on all calls to this instance.
     *
     * @param serviceName unique identifier for this service with a human meaningful value
     */
    public GovernanceServerServiceInstanceHandler(String       serviceName)
    {
        super(serviceName);
    }


    /**
     * Return the connector type for the requested connector provider after validating that the resulting
     * connector implements the correct interface.  This method is for tools that are configuring
     * connector into an Egeria governance server.  Each integration service/engine service has a specific
     * REST API endpoint for their service.  The configuration tool calls it and this method is called.
     * Because the method is in the instance handler, a result can be returned for all registered services
     * (the service does not need to be configured and running).
     *
     * @param connectorProviderClassName name of the connector provider class
     * @param requiredConnectorInterface  connector interface class
     * @param serviceName service name
     * @return ConnectorType bean
     * @throws InvalidParameterException one of the parameters is null
     * @throws ConnectionCheckedException the connection passed to the connector provider is not valid
     * @throws ConnectorCheckedException the connector is not valid
     * @throws ClassNotFoundException when the provided class cannot be found
     * @throws InstantiationException when the provided class cannot be instantiated
     * @throws IllegalAccessException when there is insufficient access to instantiate the provided class
     */
    public ConnectorType validateConnector(String   connectorProviderClassName,
                                           Class<?> requiredConnectorInterface,
                                           String   serviceName) throws InvalidParameterException,
                                                                        ConnectionCheckedException,
                                                                        ConnectorCheckedException,
                                                                        ClassNotFoundException,
                                                                        InstantiationException,
                                                                        IllegalAccessException
    {
        final String providerClassNameParameterName = "connectorProviderClassName";
        final String requiredConnectorInterfaceParameterName = "requiredConnectorInterface";
        final String testConnectionQualifiedName = "testConnection";
        final String methodName = "validateConnector";

        invalidParameterHandler.validateName(connectorProviderClassName, providerClassNameParameterName, methodName);
        invalidParameterHandler.validateObject(requiredConnectorInterface, requiredConnectorInterfaceParameterName, methodName);

        ConnectorType  connectorType;

        Class<?>   connectorProviderClass = Class.forName(connectorProviderClassName);
        Object     potentialConnectorProvider = connectorProviderClass.newInstance();

        ConnectorProvider connectorProvider = (ConnectorProvider)potentialConnectorProvider;

        connectorType = connectorProvider.getConnectorType();

        if (connectorType == null)
        {
            connectorType = new ConnectorType();

            connectorType.setConnectorProviderClassName(connectorProviderClassName);
        }

        Connection testConnection = new Connection();

        testConnection.setQualifiedName(testConnectionQualifiedName);
        testConnection.setConnectorType(connectorType);

        Connector connector = connectorProvider.getConnector(testConnection);

        if (! requiredConnectorInterface.isInstance(connector))
        {
            throw new InvalidParameterException(
                    OMAGServerInstanceErrorCode.NOT_CORRECT_CONNECTOR_PROVIDER.getMessageDefinition(connectorProviderClassName,
                                                                                                    requiredConnectorInterface.getCanonicalName(),
                                                                                                    serviceName),
                    this.getClass().getName(),
                    methodName,
                    connectorProviderClassName);
        }

        return connectorType;
    }
}
