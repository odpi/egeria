/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.kafkaaudit;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.governanceaction.properties.CatalogTarget;
import org.odpi.openmetadata.frameworks.integration.connectors.CatalogTargetProcessorBase;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.OpenMetadataTopicConnector;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.OpenMetadataTopicListener;

/**
 * Listens for events from Apache Kafka that contain audit log events.
 */
public class KafkaTopicSourceCatalogTargetProcessor extends CatalogTargetProcessorBase
{
    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     * @param connectorToTarget connector to access the target resource
     * @param connectorName name of this integration connector
     * @param auditLog logging destination
     * @param listener listener
     * @throws ConnectorCheckedException problem connecting to topic
     */
    public KafkaTopicSourceCatalogTargetProcessor(CatalogTarget             template,
                                                  Connector                 connectorToTarget,
                                                  String                    connectorName,
                                                  AuditLog                  auditLog,
                                                  OpenMetadataTopicListener listener) throws ConnectorCheckedException
    {
        super(template, connectorToTarget, connectorName, auditLog);

        if (super.getCatalogTargetConnector() instanceof OpenMetadataTopicConnector topicConnector)
        {
            topicConnector.registerListener(listener);

            if (!topicConnector.isActive())
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
