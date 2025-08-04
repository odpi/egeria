/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.auditlogstore.console.eventdisplay;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.auditlogstore.OMRSAuditLogStoreProviderBase;


/**
 * EventDisplayAuditLogStoreProvider is the OCF connector provider for the console audit log store.
 */
public class EventDisplayAuditLogStoreProvider extends OMRSAuditLogStoreProviderBase
{
    /*
     * Unique identifier for the connector type.
     */
    private static final String connectorTypeGUID      = "f986afd0-15d5-4e99-a5fd-35e4a3013da4";

    /*
     * Descriptive information about the connector for the connector type and audit log.
     */
    private static final String connectorQualifiedName = "Egeria:AuditLogDestinationConnector:EventDisplay";
    private static final String connectorDisplayName   = "Event Display Audit Log Destination Connector";
    private static final String connectorDescription   = "Connector supports logging of EVENT audit log messages to stdout along with the event payload from the log record's additional information.";

    /*
     * Class of the connector.
     */
    private static final String connectorClassName     = "org.odpi.openmetadata.adapters.repositoryservices.auditlogstore.console.eventdisplay.EventDisplayAuditLogStoreConnector";


    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * audit log store implementation.
     */
    public EventDisplayAuditLogStoreProvider()
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
        connectorType.setGUID(connectorTypeGUID);
        connectorType.setQualifiedName(connectorQualifiedName);
        connectorType.setDisplayName(connectorDisplayName);
        connectorType.setDescription(connectorDescription);
        connectorType.setConnectorProviderClassName(this.getClass().getName());
        connectorType.setRecognizedConfigurationProperties(super.getRecognizedConfigurationProperties());

        super.connectorTypeBean = connectorType;
    }
}

