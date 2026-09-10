/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.bitol;

import org.odpi.openmetadata.adapters.connectors.integration.bitol.ffdc.BitolIntegrationConnectorAuditCode;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.integration.connectors.DynamicIntegrationConnectorBase;
import org.odpi.openmetadata.frameworks.integration.context.CatalogTargetContext;
import org.odpi.openmetadata.frameworks.integration.context.IntegrationContext;
import org.odpi.openmetadata.frameworks.integration.properties.RequestedCatalogTarget;
import org.odpi.openmetadata.frameworks.opengovernance.properties.CatalogTarget;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.OpenMetadataTopicListener;

/**
 * BitolEventReceiverIntegrationConnector receives Bitol documents (Open Data Contract Standard data contracts and
 * Open Data Product Standard data products) from event topics and publishes them to the Bitol listeners registered
 * in the same integration daemon.  Each Topic catalog target is monitored through the topic connector created from its
 * connection.  Each event is expected to carry one complete document in YAML or JSON.
 */
public class BitolEventReceiverIntegrationConnector extends DynamicIntegrationConnectorBase implements OpenMetadataTopicListener
{
    /**
     * Default constructor
     */
    public BitolEventReceiverIntegrationConnector()
    {
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

        try
        {
            IntegrationContext myContext = integrationContext;

            if (myContext != null)
            {
                myContext.publishBitolDocument(event);
            }
        }
        catch (Exception error)
        {
            auditLog.logMessage(methodName,
                                BitolIntegrationConnectorAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                             error.getClass().getName(),
                                                                                                             methodName,
                                                                                                             error.getMessage()));
        }
    }


    /**
     * Create a new catalog target processor (typically inherits from CatalogTargetProcessorBase).
     *
     * @param retrievedCatalogTarget the catalog target information retrieved from the open metadata repository
     * @param catalogTargetContext specialized integration context for this catalog target
     * @param connectorToTarget connector to access the target resource
     * @return new processor based on the catalog target information
     * @throws ConnectorCheckedException problem setting up the processor
     * @throws UserNotAuthorizedException the connector was disconnected before/during start
     */
    @Override
    public RequestedCatalogTarget getNewRequestedCatalogTargetSkeleton(CatalogTarget        retrievedCatalogTarget,
                                                                       CatalogTargetContext catalogTargetContext,
                                                                       Connector            connectorToTarget) throws ConnectorCheckedException,
                                                                                                                      UserNotAuthorizedException
    {
        if (propertyHelper.isTypeOf(retrievedCatalogTarget.getCatalogTargetElement().getElementHeader(), OpenMetadataType.TOPIC.typeName))
        {
            return new BitolEventReceiverCatalogTargetProcessor(retrievedCatalogTarget,
                                                                catalogTargetContext,
                                                                connectorToTarget,
                                                                connectorName,
                                                                auditLog,
                                                                this);
        }

        return new RequestedCatalogTarget(retrievedCatalogTarget, catalogTargetContext, connectorToTarget);
    }
}
