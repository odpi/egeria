/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.kafkaaudit;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.odpi.openmetadata.adapters.connectors.integration.kafkaaudit.ffdc.DistributeKafkaAuditCode;
import org.odpi.openmetadata.adapters.connectors.integration.kafkaaudit.ffdc.DistributeKafkaErrorCode;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.integrationservices.catalog.connector.CatalogIntegratorConnector;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.OpenMetadataTopicConnector;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.OpenMetadataTopicListener;
import org.odpi.openmetadata.repositoryservices.connectors.stores.auditlogstore.OMRSAuditLogRecord;
import org.odpi.openmetadata.repositoryservices.connectors.stores.auditlogstore.OMRSAuditLogStore;
import org.odpi.openmetadata.repositoryservices.connectors.stores.auditlogstore.OMRSAuditLogStoreConnectorBase;

import java.util.ArrayList;
import java.util.List;

/**
 * Distributes audit log events from one or more embedded topic connectors to one or more embedded audit log destinations.
 */
public class DistributeAuditEventsFromKafkaConnector extends CatalogIntegratorConnector implements OpenMetadataTopicListener
{
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final List<OpenMetadataTopicConnector>      auditLogEventTopics = new ArrayList<>();
    private final List<OMRSAuditLogStoreConnectorBase>  auditLogDestinations = new ArrayList<>();

    /**
     * Indicates that the connector is completely configured and can begin processing.
     * This call can be used to register with non-blocking services.
     *
     * @throws ConnectorCheckedException there is a problem within the connector.
     */
    @Override
    public void start() throws ConnectorCheckedException
    {
        super.start();

        final String methodName = "start";

        if (embeddedConnectors == null)
        {
            throw new ConnectorCheckedException(DistributeKafkaErrorCode.NULL_TOPIC.getMessageDefinition(connectorName),
                                                this.getClass().getName(),
                                                methodName);
        }

        for (Connector embeddedConnector : embeddedConnectors)
        {
            if (embeddedConnector instanceof OpenMetadataTopicConnector openMetadataTopicConnector)
            {
                auditLogEventTopics.add(openMetadataTopicConnector);
            }
            else if (embeddedConnector instanceof OMRSAuditLogStoreConnectorBase auditLogDestination)
            {
                auditLogDestinations.add(auditLogDestination);
            }
        }

        if (auditLogEventTopics.isEmpty())
        {
            throw new ConnectorCheckedException(DistributeKafkaErrorCode.NULL_TOPIC.getMessageDefinition(connectorName),
                                                this.getClass().getName(),
                                                methodName);
        }

        if (auditLogDestinations.isEmpty())
        {
            throw new ConnectorCheckedException(DistributeKafkaErrorCode.NULL_DESTINATION.getMessageDefinition(connectorName),
                                                this.getClass().getName(),
                                                methodName);
        }

        for (OMRSAuditLogStoreConnectorBase auditLogDestination : auditLogDestinations)
        {
            auditLogDestination.start();
        }

        for (OpenMetadataTopicConnector openMetadataTopicConnector : auditLogEventTopics)
        {
            openMetadataTopicConnector.registerListener(this);
            openMetadataTopicConnector.start();
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


    /**
     * Method to pass an event received on topic.
     *
     * @param event inbound event
     */
    @Override
    public void processEvent(String event)
    {
        final String methodName = "processEvent";

        if (event != null)
        {
            try
            {
                OMRSAuditLogRecord eventObject = OBJECT_MAPPER.readValue(event, OMRSAuditLogRecord.class);

                for (OMRSAuditLogStore auditLogDestination : auditLogDestinations)
                {
                    auditLogDestination.storeLogRecord(eventObject);
                }
            }
            catch (Exception error)
            {
                auditLog.logException(methodName,
                                      DistributeKafkaAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                         error.getClass().getName(),

                                                                                                         error.getMessage()),
                                      error);
            }
        }
    }
}
