/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.integration.openlineage;

import org.odpi.openmetadata.adapters.connectors.integration.openlineage.ffdc.OpenLineageIntegrationConnectorAuditCode;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.properties.ConnectionProperties;
import org.odpi.openmetadata.frameworks.connectors.properties.EndpointProperties;
import org.odpi.openmetadata.frameworks.governanceaction.properties.CatalogTarget;
import org.odpi.openmetadata.frameworks.governanceaction.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.governanceaction.properties.RelatedMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.integrationservices.lineage.connector.LineageIntegratorConnector;
import org.odpi.openmetadata.integrationservices.lineage.connector.LineageIntegratorContext;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.OpenMetadataTopicConnector;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.OpenMetadataTopicListener;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
                    if (embeddedConnector instanceof OpenMetadataTopicConnector topicConnector)
                    {
                        registerTopicConnector(topicConnector);
                    }
                }
            }
        }
    }


    /**
     * Add the topic connector to the list of topics this connector is listening on.
     *
     * @param topicConnector connector
     * @throws ConnectorCheckedException unable to start connector
     */
    private void registerTopicConnector(OpenMetadataTopicConnector topicConnector) throws ConnectorCheckedException
    {
        final String methodName = "registerTopicConnector";
        /*
         * Register this connector as a listener of the event bus connector.
         */
        topicConnector.registerListener(this);

        ConnectionProperties connectionProperties = topicConnector.getConnection();

        if (connectionProperties != null)
        {
            EndpointProperties endpoint = connectionProperties.getEndpoint();

            if ((endpoint != null) && (endpoint.getAddress() != null) && (topicConnectors.get(endpoint.getAddress()) == null))
            {
                topicConnectors.put(endpoint.getAddress(), topicConnector);
                topicConnector.start();

                ConnectionProperties topicConnection = topicConnector.getConnection();

                auditLog.logMessage(methodName,
                                    OpenLineageIntegrationConnectorAuditCode.KAFKA_RECEIVER_CONFIGURATION.getMessageDefinition(connectorName,
                                                                                                                               endpoint.getAddress(),
                                                                                                                               topicConnection.getConnectionName()));
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
     * Maintains the list of catalog targets.
     *
     * @throws ConnectorCheckedException there is a problem with the connector.  It is not able to refresh the catalog targets.
     */
    @Override
    public synchronized void refresh() throws ConnectorCheckedException
    {
        final String methodName = "refresh";

        try
        {
            int                 startFrom      = 0;
            List<CatalogTarget> catalogTargets = myContext.getCatalogTargets(startFrom, myContext.getMaxPageSize());

            while (catalogTargets != null)
            {
                for (CatalogTarget catalogTarget : catalogTargets)
                {
                    if (catalogTarget != null)
                    {
                        if (propertyHelper.isTypeOf(catalogTarget.getCatalogTargetElement(), OpenMetadataType.TOPIC.typeName))
                        {
                            Connector connector = myContext.getConnectedAssetContext().getConnectorToAsset(catalogTarget.getCatalogTargetElement().getGUID());

                            if (connector instanceof OpenMetadataTopicConnector topicConnector)
                            {
                                registerTopicConnector(topicConnector);
                            }
                        }
                    }
                }

                startFrom = startFrom + myContext.getMaxPageSize();

                catalogTargets = myContext.getCatalogTargets(startFrom, myContext.getMaxPageSize());
            }
        }
        catch (Exception error)
        {
            auditLog.logException(methodName,
                                  OpenLineageIntegrationConnectorAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                                     error.getClass().getName(),
                                                                                                                     methodName,
                                                                                                                     error.getMessage()),
                                  error);
        }
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
