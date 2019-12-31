/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.auditlogstore.eventtopic;

import org.odpi.openmetadata.adapters.repositoryservices.auditlogstore.eventtopic.EventTopicAuditLogStoreConnector;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.auditlogstore.OMRSAuditLogStoreProviderBase;

/**
 * EventTopicAuditLogStoreProvider is the OCF connector provider for the event topic audit log store destination.
 * This log destination is an event topic and each log record is sent as an individual event with a JSON payload.
 */
public class EventTopicAuditLogStoreProvider extends OMRSAuditLogStoreProviderBase
{
    private static final String  connectorTypeGUID = "e92e8bc3-c3ef-404d-933f-9819083c0386";
    private static final String  connectorTypeName = "Event Topic Audit Log Store Connector";
    private static final String  connectorTypeDescription = "Connector supports the distribution of audit log record to an event topic.";


    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * audit log destination implementation.
     */
    public EventTopicAuditLogStoreProvider()
    {
        Class<?>    connectorClass = EventTopicAuditLogStoreConnector.class;

        super.setConnectorClassName(connectorClass.getName());

        ConnectorType connectorType = new ConnectorType();
        connectorType.setType(ConnectorType.getConnectorTypeType());
        connectorType.setGUID(connectorTypeGUID);
        connectorType.setQualifiedName(connectorTypeName);
        connectorType.setDisplayName(connectorTypeName);
        connectorType.setDescription(connectorTypeDescription);
        connectorType.setConnectorProviderClassName(this.getClass().getName());

        super.connectorTypeBean = connectorType;
    }
}

