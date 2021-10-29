/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.integration.openlineage;

import org.odpi.openmetadata.adapters.connectors.integration.openlineage.ffdc.OpenLineageIntegrationConnectorAuditCode;
import org.odpi.openmetadata.adapters.connectors.integration.openlineage.ffdc.OpenLineageIntegrationConnectorErrorCode;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBroker;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.properties.ConnectionProperties;
import org.odpi.openmetadata.frameworks.connectors.properties.EmbeddedConnectionProperties;
import org.odpi.openmetadata.frameworks.connectors.properties.EndpointProperties;
import org.odpi.openmetadata.frameworks.connectors.properties.VirtualConnectionProperties;
import org.odpi.openmetadata.integrationservices.lineage.connector.LineageIntegratorConnector;
import org.odpi.openmetadata.integrationservices.lineage.connector.LineageIntegratorContext;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.OpenMetadataTopicConnector;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.OpenMetadataTopicListener;
import java.util.List;


/**
 * KafkaOpenLineageReceiverIntegrationConnector receives open lineage events from an Apache Kafka topic and publishes them to other listening
 * lineage integration connectors.
 */
public class KafkaOpenLineageReceiverIntegrationConnector extends LineageIntegratorConnector implements OpenMetadataTopicListener
{
    private String               targetTopic = null;
    private ConnectionProperties topicConnection = null;

    private LineageIntegratorContext   myContext = null;
    private OpenMetadataTopicConnector topicConnector = null;


    /**
     * Initialize the connector.
     *
     * @param connectorInstanceId - unique id for the connector instance - useful for messages etc
     * @param connectionProperties - POJO for the configuration used to create the connector.
     */
    @Override
    public void initialize(String connectorInstanceId, ConnectionProperties connectionProperties)
    {
        super.initialize(connectorInstanceId, connectionProperties);

        if (connectionProperties instanceof VirtualConnectionProperties)
        {
            VirtualConnectionProperties virtualConnectionProperties = (VirtualConnectionProperties)connectionProperties;

            List<EmbeddedConnectionProperties> embeddedConnections = virtualConnectionProperties.getEmbeddedConnections();

            if ((embeddedConnections != null) && (! embeddedConnections.isEmpty()) && (embeddedConnections.get(0) != null))
            {
                topicConnection = embeddedConnections.get(0).getConnectionProperties();

                if (topicConnection != null)
                {
                    EndpointProperties endpoint = topicConnection.getEndpoint();

                    if (endpoint != null)
                    {
                        targetTopic = endpoint.getAddress();
                    }
                }
            }
        }
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

        /*
         * Record the configuration
         */
        if (auditLog != null)
        {
            auditLog.logMessage(methodName,
                                OpenLineageIntegrationConnectorAuditCode.KAFKA_RECEIVER_CONFIGURATION.getMessageDefinition(connectorName,
                                                                                                                           targetTopic,
                                                                                                                           topicConnection.getConnectionName()));
        }

        /*
         * Set up the topic connector
         */
        startTopicConnector(methodName);
    }


    /**
     * Start up the topic connector if not already running.
     *
     * @param methodName calling method
     * @throws ConnectorCheckedException an error setting up the connector
     */
    private void startTopicConnector(String methodName) throws ConnectorCheckedException
    {
        if ((topicConnector == null) && (myContext != null))
        {
            /*
             * Set up the topic connector
             */
            if ((topicConnection != null) && (targetTopic != null))
            {
                try
                {
                    ConnectorBroker connectorBroker = new ConnectorBroker();

                    Connector newConnector = connectorBroker.getConnector(topicConnection);

                    if (newConnector instanceof OpenMetadataTopicConnector)
                    {
                        this.topicConnector = (OpenMetadataTopicConnector) newConnector;
                    }
                }
                catch (ConnectorCheckedException error)
                {
                    if (auditLog != null)
                    {
                        auditLog.logException(methodName,
                                              OpenLineageIntegrationConnectorAuditCode.BAD_KAFKA_RECEIVER_CONFIGURATION.getMessageDefinition(connectorName,
                                                                                                                                             error.getClass().getName(),
                                                                                                                                             targetTopic,
                                                                                                                                             methodName,
                                                                                                                                             error.getMessage(),
                                                                                                                                             topicConnection.toString()),
                                              error);
                    }

                    throw error;
                }
                catch (Exception error)
                {
                    if (auditLog != null)
                    {
                        auditLog.logException(methodName,
                                              OpenLineageIntegrationConnectorAuditCode.BAD_KAFKA_RECEIVER_CONFIGURATION.getMessageDefinition(connectorName,
                                                                                                                                             error.getClass().getName(),
                                                                                                                                             targetTopic,
                                                                                                                                             methodName,
                                                                                                                                             error.getMessage(),
                                                                                                                                             topicConnection.toString()),
                                              error);


                    }

                    throw new ConnectorCheckedException(
                            OpenLineageIntegrationConnectorErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                               error.getClass().getName(),
                                                                                                               error.getMessage()),
                            this.getClass().getName(),
                            methodName,
                            error);

                }
            }
            else if (auditLog != null)
            {
                if (topicConnection == null)
                {
                    auditLog.logMessage(methodName, OpenLineageIntegrationConnectorAuditCode.NO_KAFKA_CONNECTION.getMessageDefinition(connectorName, connectionProperties.toString()));
                }
                else /* (targetTopic == null) */
                {
                    auditLog.logMessage(methodName, OpenLineageIntegrationConnectorAuditCode.NO_KAFKA_TOPIC_NAME.getMessageDefinition(connectorName, connectionProperties.toString()));
                }
            }

            if (topicConnector != null)
            {
                topicConnector.registerListener(this);

                if (auditLog != null)
                {
                    auditLog.logMessage(methodName, OpenLineageIntegrationConnectorAuditCode.LISTENING_ON_TOPIC.getMessageDefinition(connectorName, targetTopic));
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
     * Requests that the connector does a comparison of the metadata in the third party technology and open metadata repositories.
     * Refresh is called when the integration connector first starts and then at intervals defined in the connector's configuration
     * as well as any external REST API calls to explicitly refresh the connector.
     *
     * This method performs two sweeps.  It first retrieves the files in the directory and validates that are in the
     * catalog - adding or updating them if necessary.  The second sweep is to ensure that all of the assets catalogued
     * in this directory actually exist on the file system.
     *
     * @throws ConnectorCheckedException there is a problem with the connector.  It is not able to refresh the metadata.
     */
    @Override
    public void refresh() throws ConnectorCheckedException
    {
        final String methodName = "refresh";

        startTopicConnector(methodName);
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

        if (topicConnector != null)
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
