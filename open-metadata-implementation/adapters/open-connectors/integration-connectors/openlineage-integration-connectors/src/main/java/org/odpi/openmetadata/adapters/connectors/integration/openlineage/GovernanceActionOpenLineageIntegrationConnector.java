/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.openlineage;


import org.odpi.openmetadata.adapters.connectors.integration.openlineage.ffdc.OpenLineageIntegrationConnectorAuditCode;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.integration.connectors.IntegrationConnectorBase;
import org.odpi.openmetadata.frameworks.integration.openlineage.*;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ActivityStatus;
import org.odpi.openmetadata.frameworks.openmetadata.events.OpenMetadataEventListener;
import org.odpi.openmetadata.frameworks.openmetadata.events.OpenMetadataEventType;
import org.odpi.openmetadata.frameworks.openmetadata.events.OpenMetadataOutTopicEvent;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementHeader;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedMetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.actions.ActionTargetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.actions.EngineActionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.ElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.net.URI;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;


/**
 * GovernanceActionOpenLineageIntegrationConnector is an integration connector to listen for governance actions executing in the
 * open metadata ecosystem, generate open lineage events for them and publish them to any integration
 * connector running in the same instance of the integration daemon.
 */
public class GovernanceActionOpenLineageIntegrationConnector extends IntegrationConnectorBase implements OpenMetadataEventListener
{
    private static final URI    producer = URI.create("https://egeria-project.org/");
    private static final String defaultNameSpace = "GovernanceActions";
    private final        ZoneId zoneId   = ZoneId.systemDefault();

    private String namespace = defaultNameSpace;

    /**
     * Default constructor
     */
    public GovernanceActionOpenLineageIntegrationConnector()
    {
    }


    /**
     * Indicates that the connector is completely configured and can begin processing.
     *
     * @throws ConnectorCheckedException there is a problem within the connector.
     * @throws UserNotAuthorizedException the connector was disconnected before/during start
     */
    @Override
    public void start() throws ConnectorCheckedException, UserNotAuthorizedException
    {
        super.start();

        final String methodName = "start";

        namespace = super.getStringConfigurationProperty("namespace", connectionBean.getConfigurationProperties());

        if (namespace == null || namespace.isBlank())
        {
            namespace = defaultNameSpace;
        }

        try
        {
            integrationContext.registerListener(this);
        }
        catch (Exception error)
        {
            auditLog.logException(methodName,
                                  OpenLineageIntegrationConnectorAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                                     error.getClass().getName(),
                                                                                                                     methodName,
                                                                                                                     error.getMessage()),
                                  error);
        }
    }


    /**
     * Requests that the connector does a comparison of the metadata in the third party technology and open metadata repositories.
     * Refresh is called when the integration connector first starts and then at intervals defined in the connector's configuration
     * as well as any external REST API calls to explicitly refresh the connector.
     */
    public void refresh()
    {
        // nothing to do
    }


    /**
     * Process an event that was published by the Asset Manager OMAS.
     *
     * @param event event object - call getEventType to find out what type of event.
     */
    @Override
    public void processEvent(OpenMetadataOutTopicEvent event)
    {
        final String methodName = "processEvent";

        ElementHeader elementHeader = event.getElementHeader();

        if (((event.getEventType() == OpenMetadataEventType.NEW_ELEMENT_CREATED) ||
             (event.getEventType() == OpenMetadataEventType.REFRESH_ELEMENT_EVENT) ||
             (event.getEventType() == OpenMetadataEventType.ELEMENT_UPDATED)) &&
            (propertyHelper.isTypeOf(elementHeader, OpenMetadataType.ENGINE_ACTION.typeName)))
        {
            try
            {
                String previousActionStatus = getActivityStatus(event.getPreviousElementProperties());
                String currentActionStatus = getActivityStatus(event.getElementProperties());

                /*
                 * Only output an event if the status has changed.
                 */
                if (! previousActionStatus.equals(currentActionStatus))
                {
                    if ((ActivityStatus.IN_PROGRESS.getName().equals(currentActionStatus)) ||
                        (ActivityStatus.COMPLETED.getName().equals(currentActionStatus)) ||
                        (ActivityStatus.FAILED.getName().equals(currentActionStatus)) ||
                        (ActivityStatus.INVALID.getName().equals(currentActionStatus)))
                    {
                        OpenMetadataRootElement engineAction = integrationContext.getAssetClient().getAssetByGUID(elementHeader.getGUID(),
                                                                                                                  integrationContext.getAssetClient().getGetOptions());

                        publishOpenLineageEvent(currentActionStatus, event.getEventTime(), engineAction);
                    }
                }

            }
            catch (Exception error)
            {
                if (auditLog != null)
                {
                    String stringEvent = event.toString();

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

    /**
     * Return the action status from the event.
     *
     * @param elementProperties properties for the engine action
     * @return action status as a string
     */
    private String getActivityStatus(ElementProperties elementProperties)
    {
        final String methodName = "getActivityStatus";

        if (elementProperties != null)
        {
            return propertyHelper.getEnumPropertySymbolicName(connectorName,
                                                              OpenMetadataProperty.ACTIVITY_STATUS.name,
                                                              elementProperties,
                                                              methodName);
        }

        return "<null>";
    }


    /**
     * Add information about the governance action into an OpenLineage event and publish it.
     *
     * @param engineActionStatus the status from the entity at the time of the event
     * @param eventTime the time of the change to the entity
     * @param engineAction source information
     * @exception ConnectorCheckedException connector has been asked to stop
     */
    private void publishOpenLineageEvent(String                  engineActionStatus,
                                         Date                    eventTime,
                                         OpenMetadataRootElement engineAction) throws ConnectorCheckedException
    {
        OpenLineageRunEvent event = new OpenLineageRunEvent();

        if (engineAction.getProperties() instanceof EngineActionProperties engineActionProperties)
        {
            event.setProducer(producer);
            event.setEventTime(getTimeStamp(eventTime));

            if (ActivityStatus.IN_PROGRESS.getName().equals(engineActionStatus))
            {
                event.setEventType("START");
            }
            else if (ActivityStatus.COMPLETED.getName().equals(engineActionStatus))
            {
                event.setEventType("COMPLETE");
            }
            else if (ActivityStatus.FAILED.getName().equals(engineActionStatus))
            {
                event.setEventType("FAIL");
            }
            else if (ActivityStatus.INVALID.getName().equals(engineActionStatus))
            {
                event.setEventType("ABORT");
            }

            OpenLineageJob job = new OpenLineageJob();


            if (engineActionProperties.getProcessStepName() != null)
            {
                job.setName(engineActionProperties.getProcessStepName());
            }
            else
            {
                job.setName(engineActionProperties.getExecutorEngineName() + "::" + engineActionProperties.getRequestType());
            }


            job.setNamespace(namespace);

            event.setJob(job);

            OpenLineageRun run = new OpenLineageRun();

            run.setRunId(UUID.fromString(engineAction.getElementHeader().getGUID()));

            String anchorGUID = propertyHelper.getAnchorGUID(engineAction.getElementHeader());

            OpenLineageRunFacets runFacets = new OpenLineageRunFacets();

            if (anchorGUID != null)
            {
                OpenLineageParentRunFacet    parentRunFacet    = new OpenLineageParentRunFacet();
                OpenLineageParentRunFacetJob parentRunFacetJob = new OpenLineageParentRunFacetJob();
                OpenLineageParentRunFacetRun parentRunFacetRun = new OpenLineageParentRunFacetRun();

                parentRunFacet.set_producer(producer);

                parentRunFacetJob.setName(engineActionProperties.getProcessName());
                parentRunFacetJob.setNamespace(namespace);

                parentRunFacet.setJob(parentRunFacetJob);

                parentRunFacetRun.setRunId(UUID.fromString(anchorGUID));

                parentRunFacet.setRun(parentRunFacetRun);

                runFacets.setParent(parentRunFacet);
            }

            OpenLineageNominalTimeRunFacet nominalTimeRunFacet = new OpenLineageNominalTimeRunFacet();

            nominalTimeRunFacet.set_producer(producer);

            if (engineActionProperties.getStartTime() == null)
            {
                nominalTimeRunFacet.setNominalStartTime(getTimeStamp(engineActionProperties.getRequestedTime()));
            }
            else
            {
                nominalTimeRunFacet.setNominalStartTime(getTimeStamp(engineActionProperties.getStartTime()));
            }

            if (engineActionProperties.getCompletionTime() != null)
            {
                nominalTimeRunFacet.setNominalEndTime(getTimeStamp(engineActionProperties.getCompletionTime()));
            }

            runFacets.setNominalTime(nominalTimeRunFacet);

            run.setFacets(runFacets);

            event.setRun(run);

            List<OpenLineageInputDataSet> inputDataSets = new ArrayList<>();

            if (engineAction.getActionTargets() != null)
            {
                for (RelatedMetadataElementSummary actionTarget : engineAction.getActionTargets())
                {
                    if (actionTarget != null)
                    {
                        OpenLineageInputDataSet inputDataSet = new OpenLineageInputDataSet();

                        if (actionTarget.getRelationshipProperties() instanceof ActionTargetProperties actionTargetProperties)
                        {
                            inputDataSet.setName(actionTargetProperties.getActionTargetName());
                        }

                        inputDataSet.setNamespace(namespace);

                        inputDataSets.add(inputDataSet);
                    }
                }
            }

            if (engineActionProperties.getReceivedGuards() != null)
            {
                for (String guard : engineActionProperties.getReceivedGuards())
                {
                    if (guard != null)
                    {
                        OpenLineageInputDataSet inputDataSet = new OpenLineageInputDataSet();

                        inputDataSet.setName(guard);
                        inputDataSet.setNamespace(namespace);

                        inputDataSets.add(inputDataSet);
                    }
                }
            }

            if (engineActionProperties.getRequestParameters() != null)
            {
                for (String requestParameter : engineActionProperties.getRequestParameters().values())
                {
                    if (requestParameter != null)
                    {
                        OpenLineageInputDataSet inputDataSet = new OpenLineageInputDataSet();

                        inputDataSet.setName(requestParameter);
                        inputDataSet.setNamespace(namespace);

                        inputDataSets.add(inputDataSet);
                    }
                }
            }

            event.setInputs(inputDataSets);

            if (engineActionProperties.getCompletionTime() != null)
            {
                List<OpenLineageOutputDataSet> outputDataSets = new ArrayList<>();

                if (engineActionProperties.getCompletionGuards() != null)
                {
                    for (String guard : engineActionProperties.getCompletionGuards())
                    {
                        if (guard != null)
                        {
                            OpenLineageOutputDataSet outputDataSet = new OpenLineageOutputDataSet();

                            outputDataSet.setName(guard);
                            outputDataSet.setNamespace(namespace);

                            outputDataSets.add(outputDataSet);
                        }
                    }
                }

                event.setOutputs(outputDataSets);
            }

            integrationContext.publishOpenLineageRunEvent(event);
        }
    }


    /**
     * This turns a java Date into the right string format for an open lineage event.
     *
     * @param date date to convert
     * @return string formatted date
     */
    private String getTimeStamp(Date date)
    {
        ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(date.toInstant(), zoneId);
        String        zonedDateString = zonedDateTime.toString();

        /*
         * This is removing the time zone from the formatted date.  This should not be necessary and is hopefully temporary.
         */
        String []     dataTokens = zonedDateString.split("\\[");

        return dataTokens[0];
    }
}
