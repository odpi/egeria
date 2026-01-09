/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.resource.jdbc;

import org.odpi.openmetadata.adapters.connectors.EgeriaOpenConnectorDefinition;
import org.odpi.openmetadata.adapters.connectors.resource.jdbc.controls.JDBCConfigurationProperty;
import org.odpi.openmetadata.frameworks.connectors.OpenConnectorProviderBase;
import org.odpi.openmetadata.frameworks.connectors.controls.SupportedTechnologyType;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationType;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationTypeDefinition;

/**
 * JDBCResourceConnectorProvider is the OCF connector provider for the jdbc resource connector.
 */
public class JDBCResourceConnectorProvider extends OpenConnectorProviderBase
{
    /*
     * Class of the connector.
     */
    private static final String connectorClassName = "org.odpi.openmetadata.adapters.connectors.resource.jdbc.JDBCResourceConnector";

    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific connector implementation.
     * Most of the work of this connector provider is handled by the base class.
     */
    public JDBCResourceConnectorProvider()
    {
        super(EgeriaOpenConnectorDefinition.JDBC_RESOURCE_CONNECTOR,
              connectorClassName,
              JDBCConfigurationProperty.getRecognizedConfigurationProperties());

        super.supportedTechnologyTypes = SupportedTechnologyType.getSupportedTechnologyTypes(new DeployedImplementationTypeDefinition[]{
                DeployedImplementationType.JDBC_RELATIONAL_DATABASE,
                DeployedImplementationType.JDBC_RELATIONAL_DATABASE_SCHEMA});

        super.supportedConfigurationProperties = JDBCConfigurationProperty.getConfigurationPropertyTypes();
    }
}
