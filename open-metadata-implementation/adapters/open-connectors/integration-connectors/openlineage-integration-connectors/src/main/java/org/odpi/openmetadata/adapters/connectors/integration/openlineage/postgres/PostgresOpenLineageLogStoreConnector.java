/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.openlineage.postgres;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.odpi.openmetadata.adapters.connectors.integration.openlineage.ffdc.OpenLineageIntegrationConnectorAuditCode;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.integration.connectors.DynamicIntegrationConnectorBase;
import org.odpi.openmetadata.frameworks.integration.context.CatalogTargetContext;
import org.odpi.openmetadata.frameworks.integration.openlineage.OpenLineageEventListener;
import org.odpi.openmetadata.frameworks.integration.openlineage.OpenLineageRunEvent;
import org.odpi.openmetadata.frameworks.integration.properties.RequestedCatalogTarget;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.opengovernance.properties.CatalogTarget;


/**
 * PostgresOpenLineageLogStoreConnector stores the open lineage events published to the integration daemon in the
 * PostgreSQL database schemas attached to it as catalog targets.  Each event is stored as received, along with a
 * digest of its run, datasets, data quality assertions and tests that the analysis services can query by time
 * window.  The schemas and their tables are created if they do not exist.  Each refresh deletes the records that
 * are older than the daysToCleanUp configuration property.
 */
public class PostgresOpenLineageLogStoreConnector extends DynamicIntegrationConnectorBase implements OpenLineageEventListener
{
    private static final ObjectWriter OBJECT_WRITER = new ObjectMapper().writer();


    /**
     * Register to receive the open lineage events.
     *
     * @throws ConnectorCheckedException the connector detected a problem.
     * @throws UserNotAuthorizedException the connector was disconnected before/during start
     */
    @Override
    public void start() throws ConnectorCheckedException, UserNotAuthorizedException
    {
        super.start();

        integrationContext.registerOpenLineageListener(this);
    }


    /**
     * Create a catalog target processor for each database schema.
     *
     * @param retrievedCatalogTarget details of the open metadata elements describing the catalog target
     * @param catalogTargetContext specialized context for this catalog target
     * @param connectorToTarget connector to access the target resource
     * @return new processor based on the catalog target information
     */
    @Override
    public RequestedCatalogTarget getNewRequestedCatalogTargetSkeleton(CatalogTarget        retrievedCatalogTarget,
                                                                       CatalogTargetContext catalogTargetContext,
                                                                       Connector            connectorToTarget)
    {
        return new PostgresOpenLineageLogStoreCatalogTargetProcessor(retrievedCatalogTarget,
                                                                     catalogTargetContext,
                                                                     connectorToTarget,
                                                                     connectorName,
                                                                     auditLog,
                                                                     connectionBean.getConfigurationProperties());
    }


    /**
     * Store each open lineage event in every database schema.  The known catalog targets are used rather than
     * refreshing the list for each event; the list is refreshed by refresh().
     *
     * @param event run event formatted using Egeria supplied beans (null if egeria can not format the event)
     * @param rawEvent json payload received for the event
     */
    @Override
    public void processOpenLineageRunEvent(OpenLineageRunEvent event,
                                           String              rawEvent)
    {
        final String methodName = "processOpenLineageRunEvent";

        if (event == null)
        {
            return;
        }

        try
        {
            String jsonEvent = (rawEvent != null) ? rawEvent : OBJECT_WRITER.writeValueAsString(event);

            for (RequestedCatalogTarget requestedCatalogTarget : catalogTargetsManager.getRequestedCatalogTargets())
            {
                if (requestedCatalogTarget instanceof PostgresOpenLineageLogStoreCatalogTargetProcessor logStore)
                {
                    logStore.storeEvent(event, jsonEvent);
                }
            }
        }
        catch (Exception error)
        {
            logExceptionRecord(methodName,
                               OpenLineageIntegrationConnectorAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                                  error.getClass().getName(),
                                                                                                                  methodName,
                                                                                                                  error.getMessage()),
                               error);
        }
    }
}
