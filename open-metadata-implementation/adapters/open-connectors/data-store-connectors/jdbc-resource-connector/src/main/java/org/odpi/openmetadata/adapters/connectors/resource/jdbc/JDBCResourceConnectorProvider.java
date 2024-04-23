/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.resource.jdbc;

import org.odpi.openmetadata.frameworks.auditlog.AuditLogReportingComponent;
import org.odpi.openmetadata.frameworks.auditlog.ComponentDevelopmentStatus;
import org.odpi.openmetadata.frameworks.connectors.ConnectorProviderBase;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationType;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

/**
 * JDBCResourceConnectorProvider is the OCF connector provider for the jdbc resource connector.
 */
public class JDBCResourceConnectorProvider extends ConnectorProviderBase
{
    /*
     * Unique identifier of the connector for the audit log.
     */
    private static final int    connectorComponentId   = 93;

    /*
     * Unique identifier for the connector type.
     */
    private static final String connectorTypeGUID = "64463b01-92f6-4d7b-9737-f1d20b2654f4";
    private static final String connectorQualifiedName = "Egeria:ResourceConnector:RelationalDatabase:JDBC";
    private static final String connectorDisplayName = "Relational Database JDBC Connector";
    private static final String connectorTypeDescription = "Connector supports access to relational databases using exclusively the JDBC API.  This includes both data and metadata.";
    private static final String connectorWikiPage = "https://egeria-project.org/connectors/resource/jdbc-resource-connector/";


    /**
     * An optional configuration property that causes the named class to be loaded and registered as a driver.
     * This property only needs to be defined if the connector is experiencing exceptions related to a missing DriverManager class for
     * the database URL.
     */
    public static final String JDBC_DRIVER_MANAGER_CLASS_NAME = "jdbcDriverManagerClassName";

    /**
     * Sets the maximum time in seconds that this data source will wait while attempting to connect to a database.
     * The default value is 0 which means use the system default timeout, if any; otherwise it means no timeout.
     */
    public static final String JDBC_CONNECTION_TIMEOUT = "jdbcConnectionTimeout";

    /**
     * Provides a name to use in messages about the database.  If it is not set then the connection URL string is used.
     */
    public static final String JDBC_DATABASE_NAME = "jdbcDatabaseName";


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
        super();

        /*
         * Set up the class name of the connector that this provider creates.
         */
        super.setConnectorClassName(connectorClassName);

        /*
         * Set up the connector type that should be included in a connection used to configure this connector.
         */
        ConnectorType connectorType = new ConnectorType();
        connectorType.setType(ConnectorType.getConnectorTypeType());
        connectorType.setGUID(connectorTypeGUID);
        connectorType.setQualifiedName(connectorQualifiedName);
        connectorType.setDisplayName(connectorDisplayName);
        connectorType.setDescription(connectorTypeDescription);
        connectorType.setConnectorProviderClassName(this.getClass().getName());
        List<String> recognizedConfigurationProperties = new ArrayList<>();
        recognizedConfigurationProperties.add(JDBC_DRIVER_MANAGER_CLASS_NAME);
        recognizedConfigurationProperties.add(JDBC_CONNECTION_TIMEOUT);
        recognizedConfigurationProperties.add(JDBC_DATABASE_NAME);
        connectorType.setRecognizedConfigurationProperties(recognizedConfigurationProperties);

        /*
         * Information about the type of assets this type of connector works with and the interface it supports.
         */
        connectorType.setSupportedAssetTypeName(DeployedImplementationType.JDBC_RELATIONAL_DATABASE.getAssociatedTypeName());
        connectorType.setDeployedImplementationType(DeployedImplementationType.JDBC_RELATIONAL_DATABASE.getDeployedImplementationType());
        connectorInterfaces.add(DataSource.class.getName());
        connectorType.setConnectorInterfaces(connectorInterfaces);

        super.connectorTypeBean = connectorType;

        /*
         * Set up the component description used in the connector's audit log messages.
         */
        AuditLogReportingComponent componentDescription = new AuditLogReportingComponent();

        componentDescription.setComponentId(connectorComponentId);
        componentDescription.setComponentDevelopmentStatus(ComponentDevelopmentStatus.STABLE);
        componentDescription.setComponentName(connectorDisplayName);
        componentDescription.setComponentDescription(connectorTypeDescription);
        componentDescription.setComponentWikiURL(connectorWikiPage);

        super.setConnectorComponentDescription(componentDescription);
    }
}
