/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.integration.openlineage;

import org.odpi.openmetadata.adapters.connectors.integration.openlineage.ffdc.OpenLineageIntegrationConnectorAuditCode;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.properties.ConnectionProperties;
import org.odpi.openmetadata.frameworks.connectors.properties.EndpointProperties;
import org.odpi.openmetadata.integrationservices.lineage.connector.LineageIntegratorConnector;
import org.odpi.openmetadata.integrationservices.lineage.connector.LineageIntegratorContext;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.OpenMetadataTopicConnector;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.OpenMetadataTopicListener;

import java.util.HashMap;
import java.util.Map;


/**
 * OpenLineageEventReceiverIntegrationConnector receives open lineage events from an event broker such as an Apache Kafka topic.
 * It publishes them to other listening lineage integration connectors.
 */
public class OpenLineageEventReceiverIntegrationConnector extends LineageIntegratorConnector implements OpenMetadataTopicListener
{
    private LineageIntegratorContext                myContext       = null;
    private final Map<String, OpenMetadataTopicConnector> topicConnectors = new HashMap<>();


    /**
     * Default constructor
     */
    public OpenLineageEventReceiverIntegrationConnector()
    {
    }


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

        myContext = super.getContext();

        if (myContext != null)
        {
            if (embeddedConnectors != null)
            {
                for (Connector embeddedConnector : embeddedConnectors)
                {
                    if (embeddedConnector instanceof OpenMetadataTopicConnector)
                    {
                        /*
                         * Register this connector as a listener of the event bus connector.
                         */
                        OpenMetadataTopicConnector topicConnector = (OpenMetadataTopicConnector)embeddedConnector;
                        topicConnector.registerListener(this);

                        ConnectionProperties connectionProperties = topicConnector.getConnection();

                        if (connectionProperties != null)
                        {
                            EndpointProperties endpoint = connectionProperties.getEndpoint();

                            if (endpoint != null)
                            {
                                topicConnectors.put(endpoint.getAddress(), topicConnector);
                            }
                        }
                    }
                }
            }

            for (String topicName : topicConnectors.keySet())
            {
                OpenMetadataTopicConnector topicConnector = topicConnectors.get(topicName);
                ConnectionProperties       topicConnection = topicConnector.getConnection();

                /*
                 * Record the configuration
                 */
                if (auditLog != null)
                {
                    auditLog.logMessage(methodName,
                                        OpenLineageIntegrationConnectorAuditCode.KAFKA_RECEIVER_CONFIGURATION.getMessageDefinition(connectorName,
                                                                                                                                   topicName,
                                                                                                                                   topicConnection.getConnectionName()));
                }

                topicConnector.start();
            }
        }
    }


    /**
     * Method to pass an event received on topic to the Lineage Integrator OMIS to be published to listening integration connectors.
     *
     * @param event inbound event
     */
    public void processEvent(String event)
    {
        if (myContext != null)
        {
            myContext.publishOpenLineageRunEvent(event);
        }
    }


    /**
     * No function needed in refresh()
     */
    @Override
    public void refresh()
    {
    }


    /**
     * Shutdown event monitoring
     *
     * @throws ConnectorCheckedException something failed in the super class
     */
    @Override
    public void disconnect() throws ConnectorCheckedException
    {
        final String methodName = "disconnect";

        for (OpenMetadataTopicConnector topicConnector : topicConnectors.values())
        {
            topicConnector.disconnect();
        }

        if (auditLog != null)
        {
            auditLog.logMessage(methodName,
                                OpenLineageIntegrationConnectorAuditCode.CONNECTOR_STOPPING.getMessageDefinition(connectorName));
        }

        super.disconnect();
    }
}
