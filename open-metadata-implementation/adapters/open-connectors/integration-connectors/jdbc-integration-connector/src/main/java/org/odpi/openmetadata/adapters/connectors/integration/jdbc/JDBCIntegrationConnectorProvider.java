/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.jdbc;

import org.odpi.openmetadata.adapters.connectors.integration.jdbc.transfer.customization.TransferCustomizations;
import org.odpi.openmetadata.frameworks.connectors.ConnectorProviderBase;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;

import java.util.ArrayList;
import java.util.List;

public class JDBCIntegrationConnectorProvider extends ConnectorProviderBase {

    static final String CONNECTOR_TYPE_GUID = "49cd6772-1efd-40bb-a1d9-cc9460962ff6";
    static final String CONNECTOR_TYPE_NAME = "JDBC Database Connector";
    static final String CONNECTOR_TYPE_DESCRIPTION = "Connector supports JDBC Database instance";

    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * store implementation.
     */
    public JDBCIntegrationConnectorProvider(){
        super.setConnectorClassName(JDBCIntegrationConnector.class.getName());

        ConnectorType connectorType = new ConnectorType();
        connectorType.setType(ConnectorType.getConnectorTypeType());
        connectorType.setGUID(CONNECTOR_TYPE_GUID);
        connectorType.setQualifiedName(CONNECTOR_TYPE_NAME);
        connectorType.setDisplayName(CONNECTOR_TYPE_NAME);
        connectorType.setDescription(CONNECTOR_TYPE_DESCRIPTION);
        connectorType.setConnectorProviderClassName(this.getClass().getName());

        List<String> recognizedConfigurationProperties = new ArrayList<>();
        recognizedConfigurationProperties.add(TransferCustomizations.INCLUDE_SCHEMA_NAMES);
        recognizedConfigurationProperties.add(TransferCustomizations.EXCLUDE_SCHEMA_NAMES);
        recognizedConfigurationProperties.add(TransferCustomizations.INCLUDE_TABLE_NAMES);
        recognizedConfigurationProperties.add(TransferCustomizations.EXCLUDE_TABLE_NAMES);
        recognizedConfigurationProperties.add(TransferCustomizations.INCLUDE_COLUMN_NAMES);
        recognizedConfigurationProperties.add(TransferCustomizations.EXCLUDE_COLUMN_NAMES);
        connectorType.setRecognizedConfigurationProperties(recognizedConfigurationProperties);

        super.connectorTypeBean = connectorType;
    }
}
