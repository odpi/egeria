/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.kafkaaudit;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
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
     * @param connectorName name of this integration connector
     * @param auditLog logging destination
     * @param listener listener
     */
    public KafkaTopicSourceCatalogTargetProcessor(CatalogTarget             template,
                                                  String                    connectorName,
                                                  AuditLog                  auditLog,
                                                  OpenMetadataTopicListener listener)
    {
        super(template, connectorName, auditLog);

        if (super.getCatalogTargetConnector() instanceof OpenMetadataTopicConnector topicConnector)
        {
            topicConnector.registerListener(listener);
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
