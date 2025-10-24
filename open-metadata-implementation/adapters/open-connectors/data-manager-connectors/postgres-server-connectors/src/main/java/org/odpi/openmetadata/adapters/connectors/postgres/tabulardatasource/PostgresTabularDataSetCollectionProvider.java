/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.postgres.tabulardatasource;

import org.odpi.openmetadata.adapters.connectors.postgres.controls.PostgresConfigurationProperty;
import org.odpi.openmetadata.adapters.connectors.postgres.controls.PostgresDeployedImplementationType;
import org.odpi.openmetadata.frameworks.auditlog.AuditLogReportingComponent;
import org.odpi.openmetadata.frameworks.auditlog.ComponentDevelopmentStatus;
import org.odpi.openmetadata.frameworks.connectors.ConnectorProviderBase;
import org.odpi.openmetadata.frameworks.connectors.controls.SupportedTechnologyType;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.frameworks.connectors.tabulardatasets.ReadableTabularDataSource;
import org.odpi.openmetadata.frameworks.connectors.tabulardatasets.WritableTabularDataSource;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationTypeDefinition;


/**
 * PostgresTabularDataSourceProvider is the OCF connector provider for the PostgreSQL Tabular Data Source resource connector.
 */
public class PostgresTabularDataSetCollectionProvider extends ConnectorProviderBase
{
    /*
     * Unique identifier of the connector for the audit log.
     */
    private static final int    connectorComponentId   = 711;

    /*
     * Unique identifier for the connector type.
     */
    private static final String  connectorTypeGUID = "25a44f44-fee6-4334-acab-282a09bbc924";
    private static final String  connectorQualifiedName = "Egeria:ResourceConnector:TabularDataSetCollection:PostgreSQL";
    private static final String  connectorTypeName = "PostgreSQL Tabular Data Set Collection Connector";
    private static final String  connectorTypeDescription = "Connector supports a collection of PostgreSQL tabular data sets stored in the same PostgreSQL schema.";
    private static final String connectorWikiPage = "https://egeria-project.org/connectors/resource/postgres-tabular-data-set-collection/";


    private static final String  connectorClass = PostgresTabularDataSetCollectionConnector.class.getName();


    private static final String  expectedDataFormat = "relational";


    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * file store implementation.
     */
    public PostgresTabularDataSetCollectionProvider()
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
        connectorType.setSupportedAssetTypeName(PostgresDeployedImplementationType.POSTGRESQL_TABULAR_DATA_SET_COLLECTION.getAssociatedTypeName());
        connectorType.setSupportedDeployedImplementationType(PostgresDeployedImplementationType.POSTGRESQL_TABULAR_DATA_SET_COLLECTION.getDeployedImplementationType());
        connectorType.setExpectedDataFormat(expectedDataFormat);
        connectorType.setConnectorInterfaces(connectorInterfaces);

        connectorType.setRecognizedConfigurationProperties(PostgresConfigurationProperty.getPostgresTabularDataSourceConfigPropertyNames());

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
        super.supportedTechnologyTypes = SupportedTechnologyType.getSupportedTechnologyTypes(new DeployedImplementationTypeDefinition[]{PostgresDeployedImplementationType.POSTGRESQL_TABULAR_DATA_SET_COLLECTION});
    }
}
