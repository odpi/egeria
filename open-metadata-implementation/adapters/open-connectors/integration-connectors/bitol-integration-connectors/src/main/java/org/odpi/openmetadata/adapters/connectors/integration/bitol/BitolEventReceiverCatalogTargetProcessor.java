/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.bitol;

import org.odpi.openmetadata.adapters.connectors.integration.bitol.ffdc.BitolIntegrationConnectorAuditCode;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Endpoint;
import org.odpi.openmetadata.frameworks.integration.connectors.CatalogTargetProcessorBase;
import org.odpi.openmetadata.frameworks.integration.context.CatalogTargetContext;
import org.odpi.openmetadata.frameworks.opengovernance.properties.CatalogTarget;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.OpenMetadataTopicConnector;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.OpenMetadataTopicListener;

/**
 * BitolEventReceiverCatalogTargetProcessor registers the integration connector as a listener on the topic connector
 * created for a Topic catalog target, and starts the topic connector.
 */
public class BitolEventReceiverCatalogTargetProcessor extends CatalogTargetProcessorBase
{
    /**
     * Constructor.
     *
     * @param template catalog target information
     * @param catalogTargetContext context for the catalog target
     * @param connectorToTarget connector to the topic
     * @param connectorName name of the integration connector
     * @param auditLog logging destination
     * @param listener listener to receive the events
     * @throws ConnectorCheckedException problem starting the topic connector
     * @throws UserNotAuthorizedException the connector was disconnected before/during start
     */
    public BitolEventReceiverCatalogTargetProcessor(CatalogTarget             template,
                                                    CatalogTargetContext      catalogTargetContext,
                                                    Connector                 connectorToTarget,
                                                    String                    connectorName,
                                                    AuditLog                  auditLog,
                                                    OpenMetadataTopicListener listener) throws ConnectorCheckedException,
                                                                                               UserNotAuthorizedException
    {
        super(template, catalogTargetContext, connectorToTarget, connectorName, auditLog);

        if (super.getConnectorToTarget() instanceof OpenMetadataTopicConnector topicConnector)
        {
            this.registerTopicConnector(topicConnector, listener);
        }
    }


    /**
     * Register the listener with the topic connector and start it.
     *
     * @param topicConnector connector to the topic
     * @param listener listener to receive the events
     * @throws ConnectorCheckedException problem starting the topic connector
     * @throws UserNotAuthorizedException the connector was disconnected before/during start
     */
    private void registerTopicConnector(OpenMetadataTopicConnector topicConnector,
                                        OpenMetadataTopicListener  listener) throws ConnectorCheckedException,
                                                                                    UserNotAuthorizedException
    {
        final String methodName = "registerTopicConnector";

        topicConnector.registerListener(listener);

        Connection connectionDetails = topicConnector.getConnection();

        if (connectionDetails != null)
        {
            Endpoint endpoint = connectionDetails.getEndpoint();

            if (endpoint != null)
            {
                auditLog.logMessage(methodName,
                                    BitolIntegrationConnectorAuditCode.TOPIC_RECEIVER_CONFIGURATION.getMessageDefinition(connectorName,
                                                                                                                         endpoint.getNetworkAddress(),
                                                                                                                         connectionDetails.getDisplayName()));
            }

            if (! topicConnector.isActive())
            {
                topicConnector.start();
            }
        }
    }


    /**
     * The events arrive through the listener so there is nothing to do on refresh.
     */
    @Override
    public void refresh()
    {
        // nothing to do
    }
}
