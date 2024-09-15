/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.openlineage;

import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.ProcessElement;
import org.odpi.openmetadata.adapters.connectors.integration.openlineage.ffdc.OpenLineageIntegrationConnectorAuditCode;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.properties.ConnectorTypeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ProcessStatus;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.ProcessProperties;
import org.odpi.openmetadata.integrationservices.lineage.connector.LineageIntegratorConnector;
import org.odpi.openmetadata.integrationservices.lineage.connector.LineageIntegratorContext;
import org.odpi.openmetadata.integrationservices.lineage.connector.OpenLineageEventListener;
import org.odpi.openmetadata.integrationservices.lineage.properties.OpenLineageDocumentationJobFacet;
import org.odpi.openmetadata.integrationservices.lineage.properties.OpenLineageJob;
import org.odpi.openmetadata.integrationservices.lineage.properties.OpenLineageParentRunFacetJob;
import org.odpi.openmetadata.integrationservices.lineage.properties.OpenLineageParentRunFacetRun;
import org.odpi.openmetadata.integrationservices.lineage.properties.OpenLineageRun;
import org.odpi.openmetadata.integrationservices.lineage.properties.OpenLineageRunEvent;

import java.util.List;


/**
 * OpenLineageCataloguerIntegrationConnector is an integration connector to register an OpenLineage listener with the Lineage Integrator OMIS
 * and to catalog any processes that are not already known to the open metadata ecosystem.
 */
public class OpenLineageCataloguerIntegrationConnector extends LineageIntegratorConnector implements OpenLineageEventListener
{
    protected String                   destinationName = "Unknown";
    protected LineageIntegratorContext myContext       = null;


    /**
     * Default constructor
     */
    public OpenLineageCataloguerIntegrationConnector()
    {
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

        if (event != null)
        {
            try
            {
                String         processGUID = null;
                String         parentProcessName = null;
                String         parentProcessInstanceGUID = null;

                OpenLineageJob job = event.getJob();

                if (job != null)
                {
                    if (job.getName() != null)
                    {
                        String qualifiedName = "OpenLineageJob:" + job.getName();

                        List<ProcessElement> existingProcesses = myContext.getProcessesByName(qualifiedName, 0, 0, null);

                        if ((existingProcesses == null) || (existingProcesses.isEmpty()))
                        {
                            ProcessProperties processProperties = new ProcessProperties();

                            if (job.getFacets() != null)
                            {
                                OpenLineageDocumentationJobFacet documentation = job.getFacets().getDocumentation();

                                if (documentation != null)
                                {
                                    processProperties.setResourceDescription(documentation.getDescription());
                                }
                            }

                            processProperties.setTypeName("DeployedSoftwareComponent");
                            processProperties.setQualifiedName(qualifiedName);

                            processGUID = myContext.createProcess(false, ProcessStatus.ACTIVE, processProperties);
                            myContext.publishProcess(processGUID, null);
                        }
                        else if (existingProcesses.size() == 1)
                        {
                            ProcessElement existingProcess = existingProcesses.get(0);

                            processGUID = existingProcess.getElementHeader().getGUID();

                            if (existingProcess.getProcessProperties().getDisplayDescription() == null)
                            {
                                if (job.getFacets() != null)
                                {
                                    OpenLineageDocumentationJobFacet documentation = job.getFacets().getDocumentation();

                                    if (documentation != null)
                                    {
                                        ProcessProperties processProperties = new ProcessProperties();

                                        processProperties.setResourceDescription(documentation.getDescription());

                                        myContext.updateProcess(processGUID, true, processProperties, null);
                                    }
                                }
                            }
                        }

                        // ignoring the process if there are multiple definitions.
                    }

                }

                OpenLineageRun run = event.getRun();

                if (run != null)
                {
                    if (run.getFacets() != null)
                    {
                        if (run.getFacets().getParent() != null)
                        {
                            OpenLineageParentRunFacetJob parentJob = run.getFacets().getParent().getJob();
                            OpenLineageParentRunFacetRun parentRun = run.getFacets().getParent().getRun();

                            if (parentJob != null)
                            {
                                parentProcessName = parentJob.getName();

                                List<ProcessElement> existingProcesses = myContext.getProcessesByName(parentProcessName, 0 , 0, null);


                            }
                        }
                    }
                }

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
}
