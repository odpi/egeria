/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.openlineage;

import org.odpi.openmetadata.adapters.connectors.integration.openlineage.ffdc.OpenLineageIntegrationConnectorAuditCode;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.frameworks.integration.connectors.IntegrationConnectorBase;
import org.odpi.openmetadata.frameworks.integration.context.IntegrationContext;
import org.odpi.openmetadata.frameworks.integration.openlineage.*;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.AssetClient;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.ProcessProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.NewElementOptions;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.List;


/**
 * OpenLineageCataloguerIntegrationConnector is an integration connector to register an OpenLineage listener with the integration daemon
 * and to catalog any processes that are not already known to the open metadata ecosystem.
 */
public class OpenLineageCataloguerIntegrationConnector extends IntegrationConnectorBase implements OpenLineageEventListener
{
    protected String             destinationName = "Unknown";
    protected IntegrationContext myContext       = null;


    /**
     * Default constructor
     */
    public OpenLineageCataloguerIntegrationConnector()
    {
    }


    /**
     * Indicates that the connector is completely configured and can begin processing.
     *
     * @throws ConnectorCheckedException the connector detected a problem.
     * @throws UserNotAuthorizedException the connector was disconnected before/during start
     */
    @Override
    public synchronized void start() throws ConnectorCheckedException, UserNotAuthorizedException
    {
        super.start();

        if (connectionBean != null)
        {
            if (connectionBean.getDisplayName() != null)
            {
                destinationName = connectionBean.getDisplayName();
            }
            else if (connectionBean.getConnectorType() != null)
            {
                ConnectorType connectorType = connectionBean.getConnectorType();

                if (connectorType.getDisplayName() != null)
                {
                    destinationName = connectorType.getDisplayName();
                }
            }
        }

        myContext = integrationContext;

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
     * @throws ConnectorCheckedException a problem with the connector.  It is not able to refresh the metadata.
     */
    public void refresh() throws ConnectorCheckedException
    {
        // nothing to do
    }


    /**
     * Called each time an open lineage run event is published to the integration daemon.  The integration connector is able to
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

                AssetClient processClient = myContext.getAssetClient(OpenMetadataType.PROCESS.typeName);

                OpenLineageJob job = event.getJob();

                if (job != null)
                {
                    if (job.getName() != null)
                    {
                        String qualifiedName = "OpenLineageJob:" + job.getName();

                        List<OpenMetadataRootElement> existingProcesses = processClient.getAssetsByName(qualifiedName,
                                                                                             myContext.getOpenMetadataStore().getQueryOptions());

                        if ((existingProcesses == null) || (existingProcesses.isEmpty()))
                        {
                            ProcessProperties processProperties = new ProcessProperties();

                            if (job.getFacets() != null)
                            {
                                OpenLineageDocumentationJobFacet documentation = job.getFacets().getDocumentation();

                                if (documentation != null)
                                {
                                    processProperties.setDescription(documentation.getDescription());
                                }
                            }

                            processProperties.setTypeName(OpenMetadataType.DEPLOYED_SOFTWARE_COMPONENT.typeName);
                            processProperties.setQualifiedName(qualifiedName);

                            NewElementOptions newElementOptions = new NewElementOptions(processClient.getMetadataSourceOptions());

                            processGUID = processClient.createAsset(newElementOptions,
                                                                    null,
                                                                    processProperties,
                                                                    null);
                            processClient.publishElement(processGUID);
                        }
                        else if (existingProcesses.size() == 1)
                        {
                            OpenMetadataRootElement existingProcess = existingProcesses.get(0);

                            processGUID = existingProcess.getElementHeader().getGUID();

                            if ((existingProcess.getProperties() instanceof  ProcessProperties properties ) && (properties).getDescription() == null)
                            {
                                if (job.getFacets() != null)
                                {
                                    OpenLineageDocumentationJobFacet documentation = job.getFacets().getDocumentation();

                                    if (documentation != null)
                                    {
                                        ProcessProperties processProperties = new ProcessProperties();

                                        processProperties.setDescription(documentation.getDescription());

                                        processClient.updateAsset(processGUID,
                                                                  processClient.getUpdateOptions(true),
                                                                  processProperties);
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

                                List<OpenMetadataRootElement> existingProcesses = processClient.getAssetsByName(parentProcessName, processClient.getQueryOptions());


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
