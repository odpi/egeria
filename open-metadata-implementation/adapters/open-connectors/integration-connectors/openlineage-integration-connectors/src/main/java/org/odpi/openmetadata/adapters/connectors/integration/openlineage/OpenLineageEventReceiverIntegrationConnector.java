/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.integration.openlineage;

import org.odpi.openmetadata.adapters.connectors.integration.openlineage.ffdc.OpenLineageIntegrationConnectorAuditCode;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.governanceaction.properties.CatalogTarget;
import org.odpi.openmetadata.frameworks.integration.connectors.CatalogTargetIntegrator;
import org.odpi.openmetadata.frameworks.integration.properties.RequestedCatalogTarget;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.integrationservices.lineage.connector.LineageIntegratorConnector;
import org.odpi.openmetadata.integrationservices.lineage.connector.LineageIntegratorContext;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.OpenMetadataTopicListener;


/**
 * OpenLineageEventReceiverIntegrationConnector receives open lineage events from an event broker such as an Apache Kafka topic.
 * It publishes them to other listening lineage integration connectors.
 */
public class OpenLineageEventReceiverIntegrationConnector extends LineageIntegratorConnector implements OpenMetadataTopicListener,
                                                                                                        CatalogTargetIntegrator
{
    /**
     * Default constructor
     */
    public OpenLineageEventReceiverIntegrationConnector()
    {
    }


    /**
     * Method to pass an event received on topic to the Lineage Integrator OMIS to be published to listening integration connectors.
     *
     * @param event inbound event
     */
    public void processEvent(String event)
    {
        final String methodName = "processEvent";

        try
        {
            LineageIntegratorContext myContext = super.getContext();

            if (myContext != null)
            {
                myContext.publishOpenLineageRunEvent(event);
            }
        }
        catch (Exception error)
        {
            auditLog.logMessage(methodName,
                                OpenLineageIntegrationConnectorAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                                   error.getClass().getName(),
                                                                                                                   methodName,
                                                                                                                   error.getMessage()));
        }
    }


    /**
     * Requests that the connector does a comparison of the metadata in the third party technology and open metadata repositories.
     * Refresh is called when the integration connector first starts and then at intervals defined in the connector's configuration
     * as well as any external REST API calls to explicitly refresh the connector.
     *
     * @throws ConnectorCheckedException there is a problem with the connector.  It is not able to refresh the metadata.
     */
    @Override
    public void refresh() throws ConnectorCheckedException
    {
        super.refreshCatalogTargets(this);
    }


    /**
     * Perform the required integration logic for the assigned catalog target.
     *
     * @param requestedCatalogTarget the catalog target
     * @throws ConnectorCheckedException there is an unrecoverable error and the connector should stop processing.
     */
    @Override
    public void integrateCatalogTarget(RequestedCatalogTarget requestedCatalogTarget) throws ConnectorCheckedException
    {
        // Nothing to do
    }


    /**
     * Create a new catalog target processor (typically inherits from CatalogTargetProcessorBase).
     *
     * @param retrievedCatalogTarget details of the open metadata elements describing the catalog target
     * @param connectorToTarget connector to access the target resource
     * @return new processor based on the catalog target information
     */
    @Override
    public RequestedCatalogTarget getNewRequestedCatalogTargetSkeleton(CatalogTarget retrievedCatalogTarget,
                                                                       Connector     connectorToTarget)
    {
        if (propertyHelper.isTypeOf(retrievedCatalogTarget.getCatalogTargetElement(), OpenMetadataType.KAFKA_TOPIC.typeName))
        {
            return new OpenLineageEventReceiverCatalogTargetProcessor(retrievedCatalogTarget,
                                                                      connectorToTarget,
                                                                      connectorName,
                                                                      auditLog,
                                                                      this);
        }

        return new RequestedCatalogTarget(retrievedCatalogTarget, connectorToTarget);
    }
}
