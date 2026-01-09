/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.jdbc;

import org.odpi.openmetadata.adapters.connectors.EgeriaOpenConnectorDefinition;
import org.odpi.openmetadata.adapters.connectors.integration.jdbc.controls.JDBCConfigurationProperty;
import org.odpi.openmetadata.adapters.connectors.integration.jdbc.controls.JDBCDatabaseCatalogTarget;
import org.odpi.openmetadata.frameworks.auditlog.AuditLogReportingComponent;
import org.odpi.openmetadata.frameworks.auditlog.ComponentDevelopmentStatus;
import org.odpi.openmetadata.frameworks.connectors.controls.SupportedTechnologyType;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.frameworks.integration.connectors.IntegrationConnectorProvider;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationType;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationTypeDefinition;

    public class JDBCIntegrationConnectorProvider extends IntegrationConnectorProvider
{
    /**
     * Class of the connector.
     */
    private static final String connectorClassName     = "org.odpi.openmetadata.adapters.connectors.integration.jdbc.JDBCIntegrationConnector";


    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * connector implementation.
     */
    public JDBCIntegrationConnectorProvider()
    {
        super(EgeriaOpenConnectorDefinition.JDBC_INTEGRATION_CONNECTOR,
              connectorClassName,
              JDBCConfigurationProperty.getRecognizedConfigurationProperties());

        super.supportedConfigurationProperties = JDBCConfigurationProperty.getConfigurationPropertyTypes();
        super.catalogTargets = JDBCDatabaseCatalogTarget.getCatalogTargetTypes();

        super.supportedTechnologyTypes = SupportedTechnologyType.getSupportedTechnologyTypes(new DeployedImplementationTypeDefinition[]{DeployedImplementationType.JDBC_RELATIONAL_DATABASE, DeployedImplementationType.JDBC_RELATIONAL_DATABASE_SCHEMA});
    }
}
