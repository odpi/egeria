/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.auditlogstore.eventtopic;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.auditlogstore.OMRSAuditLogStoreProviderBase;

/**
 * EventTopicAuditLogStoreProvider is the OCF connector provider for the event topic audit log store destination.
 * This log destination is an event topic and each log record is sent as an individual event with a JSON payload.
 */
public class EventTopicAuditLogStoreProvider extends OMRSAuditLogStoreProviderBase
{
    /*
     * Unique identifier for the connector type.
     */
    private static final String connectorTypeGUID      = "e92e8bc3-c3ef-404d-933f-9819083c0386";

    /*
     * Descriptive information about the connector for the connector type and audit log.
     */
    private static final String connectorQualifiedName = "Egeria:AuditLogDestinationConnector:EventTopic";
    private static final String connectorDisplayName   = "Event Topic Audit Log Destination Connector";
    private static final String connectorDescription   = "Connector supports the distribution of audit log record to an event topic.";

    /*
     * Class of the connector.
     */
    private static final Class<?> connectorClass       = EventTopicAuditLogStoreConnector.class;


    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * audit log destination implementation.
     */
    public EventTopicAuditLogStoreProvider()
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
        connectorType.setRecognizedConfigurationProperties(super.getRecognizedConfigurationProperties());

        super.connectorTypeBean = connectorType;
    }
}

