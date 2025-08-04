/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.nannyconnectors.harvestopenmetadata;


import org.odpi.openmetadata.adapters.connectors.nannyconnectors.harvestopenmetadata.ffdc.HarvestOpenMetadataErrorCode;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.governanceaction.properties.CatalogTarget;
import org.odpi.openmetadata.frameworks.integration.connectors.CatalogTargetIntegrator;
import org.odpi.openmetadata.frameworks.integration.connectors.IntegrationConnectorBase;
import org.odpi.openmetadata.frameworks.integration.context.CatalogTargetContext;
import org.odpi.openmetadata.frameworks.integration.properties.RequestedCatalogTarget;


/**
 * HarvestOpenMetadataConnector extracts relevant metadata from the open metadata ecosystem into the ObservationsByEgeria database.
 * The open metadata ecosystem is the home copy so its values will be pushed to the database. The database design matches the
 * beans returned by the Open Metadata Store.
 */
public class HarvestOpenMetadataConnector extends IntegrationConnectorBase implements CatalogTargetIntegrator
{
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
        refreshCatalogTargets(this);
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
        if (requestedCatalogTarget instanceof HarvestOpenMetadataCatalogTargetProcessor harvestSurveysCatalogTargetProcessor)
        {
            harvestSurveysCatalogTargetProcessor.refresh();
        }
    }


    /**
     * Create a new catalog target processor (typically inherits from CatalogTargetProcessorBase).
     *
     * @param retrievedCatalogTarget details of the open metadata elements describing the catalog target
     * @param catalogTargetContext specialized context for this catalog target
     * @param connectorToTarget connector to access the target resource
     * @return new processor based on the catalog target information
     */
    @Override
    public RequestedCatalogTarget getNewRequestedCatalogTargetSkeleton(CatalogTarget retrievedCatalogTarget,
                                                                       CatalogTargetContext catalogTargetContext,
                                                                       Connector     connectorToTarget) throws ConnectorCheckedException
    {
        final String methodName = "getNewRequestedCatalogTargetSkeleton";

        try
        {
            return new HarvestOpenMetadataCatalogTargetProcessor(retrievedCatalogTarget,
                                                                 catalogTargetContext,
                                                                 connectorToTarget,
                                                                 connectorName,
                                                                 auditLog);
        }
        catch (Exception error)
        {
            throw new ConnectorCheckedException(HarvestOpenMetadataErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                                       error.getClass().getName(),
                                                                                                                       methodName,
                                                                                                                       error.getMessage()),
                                                this.getClass().getName(),
                                                methodName,
                                                error);
        }
    }
}
