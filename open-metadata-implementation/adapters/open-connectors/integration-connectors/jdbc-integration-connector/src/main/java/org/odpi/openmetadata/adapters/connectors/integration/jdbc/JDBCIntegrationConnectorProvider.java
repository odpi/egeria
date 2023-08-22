/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.jdbc;

import org.odpi.openmetadata.adapters.connectors.integration.jdbc.transfer.customization.TransferCustomizations;
import org.odpi.openmetadata.frameworks.auditlog.AuditLogReportingComponent;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.frameworks.integration.connectors.IntegrationConnectorProvider;

import java.util.ArrayList;
import java.util.List;

public class JDBCIntegrationConnectorProvider extends IntegrationConnectorProvider
{
    /*
     * Unique identifier of the connector for the audit log.
     */
    private static final int    connectorComponentId   = 661;

    /*
     * Unique identifier for the connector type.
     */
    private static final String connectorTypeGUID      = "49cd6772-1efd-40bb-a1d9-cc9460962ff6";

    /*
     * Descriptive information about the connector for the connector type and audit log.
     */
    private static final String connectorQualifiedName = "Egeria:IntegrationConnector:Database:JDBC";
    private static final String connectorDisplayName   = "JDBC Relational Database Integration Connector";
    private static final String connectorDescription   = "This connector retrieves schema information about a relational database's tables and columns and catalogs them in the open metadata ecosystem.";
    private static final String connectorWikiPage      = "https://egeria-project.org/connectors/integration/jdbc-integration-connector";

    /*
     * Class of the connector.
     */
    private static final Class<?> connectorClass       = JDBCIntegrationConnector.class;

    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * connector implementation.
     */
    public JDBCIntegrationConnectorProvider()
    {
        super();

        /*
         * Set up the class name of the connector that this provider creates.
         */
        super.setConnectorClassName(connectorClass.getName());

        /*
         * Set up the connector type that should be included in a connection used to configure this connector.
         */
        ConnectorType connectorType = new ConnectorType();
        connectorType.setType(ConnectorType.getConnectorTypeType());
        connectorType.setGUID(connectorTypeGUID);
        connectorType.setQualifiedName(connectorQualifiedName);
        connectorType.setDisplayName(connectorDisplayName);
        connectorType.setDescription(connectorDescription);
        connectorType.setConnectorProviderClassName(this.getClass().getName());
        List<String> recognizedConfigurationProperties = new ArrayList<>();
        recognizedConfigurationProperties.add(TransferCustomizations.INCLUDE_SCHEMA_NAMES);
        recognizedConfigurationProperties.add(TransferCustomizations.EXCLUDE_SCHEMA_NAMES);
        recognizedConfigurationProperties.add(TransferCustomizations.INCLUDE_TABLE_NAMES);
        recognizedConfigurationProperties.add(TransferCustomizations.EXCLUDE_TABLE_NAMES);
        recognizedConfigurationProperties.add(TransferCustomizations.INCLUDE_COLUMN_NAMES);
        recognizedConfigurationProperties.add(TransferCustomizations.EXCLUDE_COLUMN_NAMES);
        connectorType.setRecognizedConfigurationProperties(recognizedConfigurationProperties);
        connectorType.setSupportedAssetTypeName(supportedAssetTypeName);

        super.connectorTypeBean = connectorType;

        /*
         * Set up the component description used in the connector's audit log messages.
         */
        AuditLogReportingComponent componentDescription = new AuditLogReportingComponent();

        componentDescription.setComponentId(connectorComponentId);
        componentDescription.setComponentName(connectorQualifiedName);
        componentDescription.setComponentDescription(connectorDescription);
        componentDescription.setComponentWikiURL(connectorWikiPage);

        super.setConnectorComponentDescription(componentDescription);
    }
}
