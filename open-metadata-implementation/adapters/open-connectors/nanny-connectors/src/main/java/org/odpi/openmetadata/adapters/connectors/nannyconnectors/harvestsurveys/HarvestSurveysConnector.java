/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.nannyconnectors.harvestsurveys;


import org.odpi.openmetadata.accessservices.assetmanager.api.AssetManagerEventListener;
import org.odpi.openmetadata.accessservices.assetmanager.events.AssetManagerEventType;
import org.odpi.openmetadata.accessservices.assetmanager.events.AssetManagerOutTopicEvent;
import org.odpi.openmetadata.adapters.connectors.nannyconnectors.harvestsurveys.ffdc.HarvestSurveysAuditCode;
import org.odpi.openmetadata.adapters.connectors.nannyconnectors.harvestsurveys.ffdc.HarvestSurveysErrorCode;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.governanceaction.properties.CatalogTarget;
import org.odpi.openmetadata.frameworks.integration.connectors.CatalogTargetIntegrator;
import org.odpi.openmetadata.frameworks.integration.properties.RequestedCatalogTarget;
import org.odpi.openmetadata.frameworks.openmetadata.enums.PermittedSynchronization;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.integrationservices.catalog.connector.CatalogIntegratorConnector;
import org.odpi.openmetadata.integrationservices.catalog.connector.CatalogTargetEventProcessor;


/**
 * HarvestSurveysConnector extracts relevant metadata from the open metadata ecosystem into the Observations database.
 * The open metadata ecosystem is the home copy so its values will be pushed to the database. The database design matches the
 * beans returned by Asset Manager OMAS/Catalog Integrator OMIS.
 */
public class HarvestSurveysConnector extends CatalogIntegratorConnector implements CatalogTargetIntegrator,
                                                                                   AssetManagerEventListener,
                                                                                   CatalogTargetEventProcessor
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
        final String methodName = "refresh";

        refreshCatalogTargets(this);

        /*
         * Once the connector has completed a single refresh, it registered a listener with open metadata
         * to handle updates.  The delay in registering the listener is for efficiency-sake in that it
         * reduces the number of events coming in from updates to the open metadata ecosystem when the connector
         * is performing its first synchronization from Apache Atlas to Egeria.
         *
         * A listener is registered only if metadata is flowing from the open metadata ecosystem to Apache Atlas.
         */
        if ((! getContext().isListenerRegistered()) &&
                (getContext().getPermittedSynchronization() == PermittedSynchronization.BOTH_DIRECTIONS) ||
                (getContext().getPermittedSynchronization() == PermittedSynchronization.TO_THIRD_PARTY))
        {
            try
            {
                /*
                 * This request registers this connector to receive events from the open metadata ecosystem.  When an event occurs,
                 * the processEvent() method is called.
                 */
                getContext().registerListener(this);
            }
            catch (Exception error)
            {
                if (auditLog != null)
                {
                    auditLog.logException(methodName,
                                          HarvestSurveysAuditCode.UNABLE_TO_REGISTER_LISTENER.getMessageDefinition(connectorName,
                                                                                                                   error.getClass().getName(),
                                                                                                                   error.getMessage()),
                                          error);
                }

                throw new ConnectorCheckedException(HarvestSurveysErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                                        error.getClass().getName(),
                                                                                                                        methodName,
                                                                                                                        error.getMessage()),
                                                    this.getClass().getName(),
                                                    methodName,
                                                    error);
            }
        }
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
        if (requestedCatalogTarget instanceof HarvestSurveysCatalogTargetProcessor harvestSurveysCatalogTargetProcessor)
        {
            harvestSurveysCatalogTargetProcessor.refresh();
        }
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
                                                                       Connector     connectorToTarget) throws ConnectorCheckedException
    {
        final String methodName = "getNewRequestedCatalogTargetSkeleton";

        try
        {
            return new HarvestSurveysCatalogTargetProcessor(retrievedCatalogTarget,
                                                            connectorToTarget,
                                                            connectorName,
                                                            auditLog,
                                                            super.getContext().getIntegrationGovernanceContext().getOpenMetadataAccess(),
                                                            super.getContext().getConnectorFactoryService());
        }
        catch (Exception error)
        {
            throw new ConnectorCheckedException(HarvestSurveysErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                                  error.getClass().getName(),
                                                                                                                  methodName,
                                                                                                                  error.getMessage()),
                                                this.getClass().getName(),
                                                methodName,
                                                error);
        }
    }


    /**
     * Process an event that was published by the Asset Manager OMAS.  Only process new surveys
     *
     * @param event event object - call getEventType to find out what type of event.
     */
    @Override
    public void processEvent(AssetManagerOutTopicEvent event)
    {
        final String methodName = "processEvent";

        if ((event.getEventType() == AssetManagerEventType.NEW_ELEMENT_CREATED) && (event.getElementHeader().getType().getTypeName().equals(OpenMetadataType.SURVEY_REPORT.typeName)))
        {
            try
            {
                super.passEventToCatalogTargets(this, event);
            }
            catch (Exception error)
            {
                auditLog.logException(methodName,
                                      HarvestSurveysAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                        error.getClass().getName(),
                                                                                                        methodName,
                                                                                                        error.getMessage()),
                                      error);
            }
        }
    }


    /**
     * Perform the required integration logic for the assigned catalog target.
     *
     * @param requestedCatalogTarget the catalog target
     * @param event event to process
     */
    @Override
    public void passEventToCatalogTarget(RequestedCatalogTarget    requestedCatalogTarget,
                                         AssetManagerOutTopicEvent event)
    {
        if (requestedCatalogTarget instanceof HarvestSurveysCatalogTargetProcessor harvestSurveysCatalogTargetProcessor)
        {
            harvestSurveysCatalogTargetProcessor.processEvent(event);
        }
    }

}
