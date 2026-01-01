/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.externalharvesters.harvestsurveys;


import org.odpi.openmetadata.adapters.connectors.externalharvesters.harvestsurveys.ffdc.HarvestSurveysAuditCode;
import org.odpi.openmetadata.adapters.connectors.externalharvesters.harvestsurveys.ffdc.HarvestSurveysErrorCode;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.integration.connectors.DynamicIntegrationConnectorBase;
import org.odpi.openmetadata.frameworks.integration.context.CatalogTargetContext;
import org.odpi.openmetadata.frameworks.integration.properties.RequestedCatalogTarget;
import org.odpi.openmetadata.frameworks.opengovernance.properties.CatalogTarget;
import org.odpi.openmetadata.frameworks.openmetadata.events.OpenMetadataEventListener;
import org.odpi.openmetadata.frameworks.openmetadata.events.OpenMetadataEventType;
import org.odpi.openmetadata.frameworks.openmetadata.events.OpenMetadataOutTopicEvent;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;


/**
 * HarvestSurveysConnector extracts relevant metadata from the open metadata ecosystem into the Observations database.
 * The open metadata ecosystem is the home copy so its values will be pushed to the database. The database design matches the
 * beans returned by theOpen Metadata Store.
 */
public class HarvestSurveysConnector extends DynamicIntegrationConnectorBase implements OpenMetadataEventListener
{
    /**
     * Create a new catalog target processor (typically inherits from CatalogTargetProcessorBase).
     *
     * @param retrievedCatalogTarget details of the open metadata elements describing the catalog target
     * @param catalogTargetContext specialized context for this catalog target
     * @param connectorToTarget connector to access the target resource
     * @return new processor based on the catalog target information
     */
    @Override
    public RequestedCatalogTarget getNewRequestedCatalogTargetSkeleton(CatalogTarget        retrievedCatalogTarget,
                                                                       CatalogTargetContext catalogTargetContext,
                                                                       Connector            connectorToTarget) throws ConnectorCheckedException
    {
        final String methodName = "getNewRequestedCatalogTargetSkeleton";

        try
        {
            return new HarvestSurveysCatalogTargetProcessor(retrievedCatalogTarget,
                                                            catalogTargetContext,
                                                            connectorToTarget,
                                                            connectorName,
                                                            auditLog);
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
    public void processEvent(OpenMetadataOutTopicEvent event)
    {
        final String methodName = "processEvent";

        if ((event.getEventType() == OpenMetadataEventType.NEW_ELEMENT_CREATED) && (event.getElementHeader().getType().getTypeName().equals(OpenMetadataType.SURVEY_REPORT.typeName)))
        {
            try
            {
                catalogTargetsManager.passEventToCatalogTargets(integrationContext, event);
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
}
