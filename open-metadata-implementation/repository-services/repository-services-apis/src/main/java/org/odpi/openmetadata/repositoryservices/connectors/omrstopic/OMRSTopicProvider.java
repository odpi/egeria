/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.connectors.omrstopic;

import org.odpi.openmetadata.frameworks.connectors.ConnectorProviderBase;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditingComponent;


/**
 * OMRSTopicProvider provides implementation of the connector provider for the OMRSTopicConnector.
 */
public class OMRSTopicProvider extends ConnectorProviderBase
{
    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * OMRS Connector implementation.
     */
    public OMRSTopicProvider()
    {
        Class<OMRSTopicConnector>    connectorClass = OMRSTopicConnector.class;

        super.setConnectorClassName(connectorClass.getName());
        super.setConnectorComponentDescription(OMRSAuditingComponent.OMRS_TOPIC_CONNECTOR);
    }
}
