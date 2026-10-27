/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.pepys;

import org.odpi.openmetadata.adapters.connectors.pepys.ffdc.PepysAuditCode;
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
 * PepysTopicCatalogTargetProcessor looks after one of the audit log topics that Pepys is listening to.  It
 * registers the connector as a listener with the topic's connector and starts it.  The records themselves are
 * processed by the connector, since the counts for an anchor element are gathered from every topic.
 */
public class PepysTopicCatalogTargetProcessor extends CatalogTargetProcessorBase
{
    /**
     * Principle constructor.
     *
     * @param template object to copy
     * @param catalogTargetContext specialized context for this catalog target
     * @param connectorToTarget connector to access the target resource
     * @param connectorName name of this integration connector
     * @param auditLog logging destination
     * @param listener listener for the audit log records
     * @throws ConnectorCheckedException the connector detected a problem.
     * @throws UserNotAuthorizedException the connector was disconnected before/during start
     */
    public PepysTopicCatalogTargetProcessor(CatalogTarget             template,
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
     * Register the listener with the topic connector and start it if it is not already running.
     *
     * @param topicConnector connector to the audit log topic
     * @param listener event listener
     * @throws ConnectorCheckedException the connector detected a problem.
     * @throws UserNotAuthorizedException the connector was disconnected before/during start
     */
    private void registerTopicConnector(OpenMetadataTopicConnector topicConnector,
                                        OpenMetadataTopicListener  listener) throws ConnectorCheckedException,
                                                                                    UserNotAuthorizedException
    {
        final String methodName = "registerTopicConnector";

        topicConnector.registerListener(listener);

        Connection connection = topicConnector.getConnection();

        if (connection != null)
        {
            Endpoint endpoint = connection.getEndpoint();

            if (endpoint != null)
            {
                logRecord(methodName,
                          PepysAuditCode.LISTENING_TO_TOPIC.getMessageDefinition(connectorName,
                                                                                 endpoint.getNetworkAddress(),
                                                                                 connection.getDisplayName()));
            }
        }

        if (! topicConnector.isActive())
        {
            topicConnector.start();
        }
    }


    /**
     * There is nothing to refresh for a topic - the records arrive as events.
     */
    @Override
    public void refresh()
    {
        // nothing to do
    }
}
