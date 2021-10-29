/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.openlineage;

import org.odpi.openmetadata.accessservices.assetmanager.properties.ProcessProperties;
import org.odpi.openmetadata.accessservices.assetmanager.properties.ProcessStatus;
import org.odpi.openmetadata.adapters.connectors.integration.openlineage.ffdc.OpenLineageIntegrationConnectorAuditCode;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.properties.ConnectionProperties;
import org.odpi.openmetadata.frameworks.connectors.properties.ConnectorTypeProperties;
import org.odpi.openmetadata.integrationservices.lineage.connector.LineageIntegratorConnector;
import org.odpi.openmetadata.integrationservices.lineage.connector.LineageIntegratorContext;
import org.odpi.openmetadata.integrationservices.lineage.connector.OpenLineageEventListener;
import org.odpi.openmetadata.integrationservices.lineage.properties.OpenLineageRunEvent;


/**
 * OpenLineageCataloguerIntegrationConnector is an integration connector to register an OpenLineage listener with the Lineage Integrator OMIS
 * and to catalog any processes that are not already known to the open metadata ecosystem.
 */
public class OpenLineageCataloguerIntegrationConnector extends LineageIntegratorConnector implements OpenLineageEventListener
{
    protected String                   destinationName = "<Unknown";
    protected LineageIntegratorContext myContext       = null;


    /**
     * Default constructor
     */
    protected OpenLineageCataloguerIntegrationConnector()
    {
    }


    /**
     * Call made by the ConnectorProvider to initialize the Connector with the base services.
     *
     * @param connectorInstanceId   unique id for the connector instance   useful for messages etc
     * @param connectionProperties   POJO for the configuration used to create the connector.
     */
    @Override
    public void initialize(String               connectorInstanceId,
                           ConnectionProperties connectionProperties)
    {
        super.initialize(connectorInstanceId, connectionProperties);

        if (connectionProperties != null)
        {
            if (connectionProperties.getDisplayName() != null)
            {
                destinationName = connectionProperties.getDisplayName();
            }
            else if (connectionProperties.getConnectorType() != null)
            {
                ConnectorTypeProperties connectorType = connectionProperties.getConnectorType();

                if (connectorType.getDisplayName() != null)
                {
                    destinationName = connectorType.getDisplayName();
                }
            }
        }
    }


    /**
     * Indicates that the connector is completely configured and can begin processing.
     *
     * @throws ConnectorCheckedException there is a problem within the connector.
     */
    @Override
    public synchronized void start() throws ConnectorCheckedException
    {
        super.start();

        myContext = super.getContext();

        if (myContext != null)
        {
            myContext.registerListener(this);
        }
    }


    /**
     * Requests that the connector does a comparison of the metadata in the third party technology and open metadata repositories.
     * Refresh is called when the integration connector first starts and then at intervals defined in the connector's configuration
     * as well as any external REST API calls to explicitly refresh the connector.
     *
     * @throws ConnectorCheckedException there is a problem with the connector.  It is not able to refresh the metadata.
     */
    public void refresh() throws ConnectorCheckedException
    {
        // nothing to do
    }


    /**
     * Called each time an open lineage run event is published to the Lineage Integrator OMIS.  The integration connector is able to
     * work with the formatted event using the Egeria beans or reformat the open lineage run event using the supplied open lineage backend beans
     * or another set of beans.
     *
     * @param event run event formatted using Egeria supplied beans (null if egeria can not format the event)
     * @param rawEvent json payload received for the event
     */
    @Override
    public void processOpenLineageRunEvent(OpenLineageRunEvent event,
                                           String              rawEvent)
    {
        final String methodName = "processOpenLineageRunEvent";

        ProcessProperties processProperties = new ProcessProperties();

        try
        {
            myContext.createProcess(false, processProperties, ProcessStatus.ACTIVE);
        }
        catch (Exception error)
        {
            if (auditLog != null)
            {
                String stringEvent = rawEvent;

                if (rawEvent == null)
                {
                    stringEvent = event.toString();
                }

                auditLog.logException(methodName,
                                      OpenLineageIntegrationConnectorAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                                         error.getClass().getName(),
                                                                                                                         methodName,
                                                                                                                         error.getMessage()),
                                      stringEvent,
                                      error);
            }
        }
    }

}
