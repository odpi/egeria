/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.postgres.tabulardatasource;

import org.odpi.openmetadata.adapters.connectors.postgres.controls.PostgresConfigurationProperty;
import org.odpi.openmetadata.adapters.connectors.postgres.controls.PostgresDeployedImplementationType;
import org.odpi.openmetadata.frameworks.auditlog.AuditLogReportingComponent;
import org.odpi.openmetadata.frameworks.auditlog.ComponentDevelopmentStatus;
import org.odpi.openmetadata.frameworks.connectors.ConnectorProviderBase;
import org.odpi.openmetadata.frameworks.connectors.ReadableTabularDataSource;
import org.odpi.openmetadata.frameworks.connectors.WritableTabularDataSource;
import org.odpi.openmetadata.frameworks.connectors.controls.SupportedTechnologyType;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationType;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationTypeDefinition;

import java.util.ArrayList;
import java.util.List;


/**
 * PostgresTabularDataSourceProvider is the OCF connector provider for the PostgreSQL Tabular Data Source resource connector.
 */
public class PostgresTabularDataSourceProvider extends ConnectorProviderBase
{
    /*
     * Unique identifier of the connector for the audit log.
     */
    private static final int    connectorComponentId   = 703;

    /*
     * Unique identifier for the connector type.
     */
    private static final String  connectorTypeGUID = "0621d861-2485-4ba3-89c5-d6744b648a37";
    private static final String  connectorQualifiedName = "Egeria:ResourceConnector:TabularDataSource:PostgreSQL";
    private static final String  connectorTypeName = "PostgreSQL Tabular Data Source Connector";
    private static final String  connectorTypeDescription = "Connector supports reading/writing of data to a PostgreSQL table.";
    private static final String connectorWikiPage = "https://egeria-project.org/connectors/resource/postgres-tabular-data-source/";


    private static final String  connectorClass = "org.odpi.openmetadata.adapters.connectors.postgres.tabulardatasource.PostgresTabularDataSourceConnector";


    private static final String  expectedDataFormat = "relational";


    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * file store implementation.
     */
    public PostgresTabularDataSourceProvider()
    {
        super();

        super.setConnectorClassName(connectorClass);

        connectorInterfaces.add(ReadableTabularDataSource.class.getName());
        connectorInterfaces.add(WritableTabularDataSource.class.getName());

        ConnectorType connectorType = new ConnectorType();

        connectorType.setGUID(connectorTypeGUID);
        connectorType.setQualifiedName(connectorQualifiedName);
        connectorType.setDisplayName(connectorTypeName);
        connectorType.setDescription(connectorTypeDescription);
        connectorType.setConnectorProviderClassName(this.getClass().getName());
        connectorType.setSupportedAssetTypeName(DeployedImplementationType.CSV_FILE.getAssociatedTypeName());
        connectorType.setSupportedDeployedImplementationType(DeployedImplementationType.CSV_FILE.getDeployedImplementationType());
        connectorType.setExpectedDataFormat(expectedDataFormat);
        connectorType.setConnectorInterfaces(connectorInterfaces);

        connectorType.setRecognizedConfigurationProperties(PostgresConfigurationProperty.getPostgresTabularDataSourceConnectorNames());

        super.connectorTypeBean = connectorType;

        /*
         * Set up the component description used in the connector's audit log messages.
         */
        AuditLogReportingComponent componentDescription = new AuditLogReportingComponent();

        componentDescription.setComponentId(connectorComponentId);
        componentDescription.setComponentDevelopmentStatus(ComponentDevelopmentStatus.STABLE);
        componentDescription.setComponentName(connectorTypeName);
        componentDescription.setComponentDescription(connectorTypeDescription);
        componentDescription.setComponentWikiURL(connectorWikiPage);

        super.setConnectorComponentDescription(componentDescription);

        super.supportedConfigurationProperties = PostgresConfigurationProperty.getPostgresTabularDataSourceConfigurationPropertyTypes();
        super.supportedTechnologyTypes = SupportedTechnologyType.getSupportedTechnologyTypes(new DeployedImplementationTypeDefinition[]{PostgresDeployedImplementationType.POSTGRESQL_DATABASE_TABLE});
    }
}
