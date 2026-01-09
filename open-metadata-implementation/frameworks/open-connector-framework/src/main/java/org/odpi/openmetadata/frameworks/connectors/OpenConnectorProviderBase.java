/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.connectors;

import org.odpi.openmetadata.frameworks.auditlog.AuditLogReportingComponent;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;

import java.util.List;

/**
 * OpenConnectorsProviderBase provides the connector provider base class that performs the setup for a connector provider.
 * Each connector implementation should extend this class and provide the connector definition and connector class name
 * in their constructor.
 */
public class OpenConnectorProviderBase extends ConnectorProviderBase
{
    /**
     * Constructor where subclass sets up the connector provider.
     */
    public OpenConnectorProviderBase()
    {
        super();
    }


    /**
     * Constructor for an open connector provider.
     *
     * @param openConnectorDescription             connector definition
     * @param connectorClassName                   connector class name
     * @param recognizedConfigurationPropertyNames list of property names that the connector supports
     *                                             in the configuration properties.
     */
    public OpenConnectorProviderBase(OpenConnectorDefinition openConnectorDescription,
                                     String                  connectorClassName,
                                     List<String>            recognizedConfigurationPropertyNames)
    {
        this(openConnectorDescription, connectorClassName, recognizedConfigurationPropertyNames, null, null);
    }


    /**
     * Constructor for an open connector provider.
     *
     * @param openConnectorDescription             connector definition
     * @param connectorClassName                   connector class name
     * @param recognizedConfigurationPropertyNames list of property names that the connector supports
     *                                             in the configuration properties.
     */
    public OpenConnectorProviderBase(OpenConnectorDefinition openConnectorDescription,
                                     String                  connectorClassName,
                                     List<String>            recognizedConfigurationPropertyNames,
                                     List<String>            connectorInterfaces,
                                     String                  expectedDataFormat)
    {
        super();

        /*
         * Set up the class name of the connector that this provider creates.
         */
        super.connectorClassName = connectorClassName;

        /*
         * Check that the connector definition has the correct connector provider class name.
         */
        assert (this.getClass().getName().equals(openConnectorDescription.getConnectorProviderClassName()));

        /*
         * Set up the connector type that should be included in a connection used to configure this connector.
         */
        ConnectorType connectorType = new ConnectorType();

        connectorType.setGUID(openConnectorDescription.getConnectorTypeGUID());
        connectorType.setQualifiedName(openConnectorDescription.getConnectorQualifiedName());
        connectorType.setDisplayName(openConnectorDescription.getConnectorDisplayName());
        connectorType.setDescription(openConnectorDescription.getConnectorDescription());
        connectorType.setConnectorProviderClassName(this.getClass().getName());
        connectorType.setRecognizedConfigurationProperties(recognizedConfigurationPropertyNames);
        connectorType.setConnectorInterfaces(connectorInterfaces);
        connectorType.setExpectedDataFormat(expectedDataFormat);

        super.connectorTypeBean = connectorType;

        /*
         * Set up the component description used in the connector's audit log messages.
         */
        AuditLogReportingComponent componentDescription = new AuditLogReportingComponent();

        componentDescription.setComponentId(openConnectorDescription.getConnectorComponentId());
        componentDescription.setComponentDevelopmentStatus(openConnectorDescription.getConnectorDevelopmentStatus());
        componentDescription.setComponentName(openConnectorDescription.getConnectorQualifiedName());
        componentDescription.setComponentDescription(openConnectorDescription.getConnectorDescription());
        componentDescription.setComponentWikiURL(openConnectorDescription.getConnectorWikiPage());

        super.connectorComponentDescription = componentDescription;
    }
}
