/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.openlineage;

import org.odpi.openmetadata.adapters.connectors.integration.openlineage.ffdc.OpenLineageIntegrationConnectorAuditCode;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Endpoint;
import org.odpi.openmetadata.frameworks.opengovernance.properties.CatalogTarget;
import org.odpi.openmetadata.frameworks.integration.connectors.CatalogTargetProcessorBase;
import org.odpi.openmetadata.frameworks.integration.context.CatalogTargetContext;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.OpenMetadataTopicConnector;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.OpenMetadataTopicListener;

/**
 * Listens for events from Apache Kafka that contain open lineage events.
 */
public class OpenLineageEventReceiverCatalogTargetProcessor extends CatalogTargetProcessorBase
{
    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     * @param catalogTargetContext specialized context for this catalog target
     * @param connectorToTarget connector to access the target resource
     * @param connectorName name of this integration connector
     * @param auditLog logging destination
     * @param listener listener
     * @throws ConnectorCheckedException the connector detected a problem.
     * @throws UserNotAuthorizedException the connector was disconnected before/during start
     */
    public OpenLineageEventReceiverCatalogTargetProcessor(CatalogTarget             template,
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
     * Add the topic connector to the list of topics this connector is listening on.
     *
     * @param topicConnector connector
     * @param listener event listener
     * @throws ConnectorCheckedException the connector detected a problem.
     * @throws UserNotAuthorizedException the connector was disconnected before/during start
     */
    private void registerTopicConnector(OpenMetadataTopicConnector topicConnector,
                                        OpenMetadataTopicListener  listener) throws ConnectorCheckedException,
                                                                                    UserNotAuthorizedException
    {
        final String methodName = "registerTopicConnector";

        /*
         * Register this connector as a listener of the event bus connector.
         */
        topicConnector.registerListener(listener);

        Connection connectionDetails = topicConnector.getConnection();

        if (connectionDetails != null)
        {
            Endpoint endpoint = connectionDetails.getEndpoint();

            if (endpoint != null)
            {
                auditLog.logMessage(methodName,
                                    OpenLineageIntegrationConnectorAuditCode.KAFKA_RECEIVER_CONFIGURATION.getMessageDefinition(connectorName,
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
     * Requests that the connector does a comparison of the metadata in the third party technology and open metadata repositories.
     * Refresh is called when the integration connector first starts and then at intervals defined in the connector's configuration
     * as well as any external REST API calls to explicitly refresh the connector.
     */
    @Override
    public void refresh()
    {
        // nothing to do
    }
}
