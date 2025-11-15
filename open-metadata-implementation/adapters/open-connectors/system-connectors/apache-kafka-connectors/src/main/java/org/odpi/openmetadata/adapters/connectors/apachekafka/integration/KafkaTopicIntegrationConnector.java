/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.apachekafka.integration;


import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.integration.connectors.DynamicIntegrationConnectorBase;
import org.odpi.openmetadata.frameworks.integration.context.CatalogTargetContext;
import org.odpi.openmetadata.frameworks.integration.properties.RequestedCatalogTarget;
import org.odpi.openmetadata.frameworks.opengovernance.properties.CatalogTarget;


/**
 * KafkaTopicIntegrationConnector catalogues active topics in an Apache Kafka event broker.  It supports
 * catalog targets of either an event broker or software server.  If it is passed a software server,
 * this server must have an event broker software capability attached.
 */
public class KafkaTopicIntegrationConnector extends DynamicIntegrationConnectorBase
{
    /**
     * Create a new catalog target processor (typically inherits from CatalogTargetProcessorBase).
     *
     * @param retrievedCatalogTarget details of the open metadata elements describing the catalog target
     * @param catalogTargetContext   specialized context for this catalog target
     * @param connectorToTarget      connector to access the target resource
     * @return new processor based on the catalog target information
     */
    @Override
    public RequestedCatalogTarget getNewRequestedCatalogTargetSkeleton(CatalogTarget        retrievedCatalogTarget,
                                                                       CatalogTargetContext catalogTargetContext,
                                                                       Connector            connectorToTarget)
    {
        return new KafkaTopicCatalogTargetProcessor(retrievedCatalogTarget,
                                                    catalogTargetContext,
                                                    connectorToTarget,
                                                    connectorName,
                                                    auditLog);
    }
}
