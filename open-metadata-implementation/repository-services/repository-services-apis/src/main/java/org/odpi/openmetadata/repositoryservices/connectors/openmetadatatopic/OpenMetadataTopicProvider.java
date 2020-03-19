/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic;

import org.odpi.openmetadata.frameworks.connectors.ConnectorProviderBase;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditingComponent;


/**
 * OMRSTopicProvider provides implementation of the connector provider for the OMRSTopicConnector.
 */
public abstract class OpenMetadataTopicProvider extends ConnectorProviderBase
{
    protected static final String  sleepTimeProperty = "sleepTime";

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