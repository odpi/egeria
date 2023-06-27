/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic;

import org.odpi.openmetadata.frameworks.connectors.ConnectorProviderBase;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditingComponent;


/**
 * OpenMetadataTopicProvider provides implementation of the connector provider for the OpenMetadataTopicConnector.  This connector provides
 * a generic interface for sending and receiving string-based events.
 */
public abstract class OpenMetadataTopicProvider extends ConnectorProviderBase
{
    public static final String EVENT_DIRECTION_PROPERTY_NAME = "eventDirection";
    public static final String EVENT_DIRECTION_INOUT         = "inOut";
    public static final String EVENT_DIRECTION_OUT_ONLY      = "outOnly";
    public static final String  EVENT_DIRECTION_IN_ONLY  = "inOnly";


    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * OMRS Connector implementation.
     */
    protected OpenMetadataTopicProvider()
    {
        Class<OpenMetadataTopicConnector> connectorClass = OpenMetadataTopicConnector.class;

        super.setConnectorClassName(connectorClass.getName());
        super.setConnectorComponentDescription(OMRSAuditingComponent.OPEN_METADATA_TOPIC_CONNECTOR);

    }
}