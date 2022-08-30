/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.openlineage;

import org.odpi.openmetadata.accessservices.assetmanager.api.AssetManagerEventListener;
import org.odpi.openmetadata.accessservices.assetmanager.events.AssetManagerEventType;
import org.odpi.openmetadata.accessservices.assetmanager.events.AssetManagerOutTopicEvent;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.ActionTargetElement;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.GovernanceActionElement;
import org.odpi.openmetadata.accessservices.assetmanager.properties.GovernanceActionProperties;
import org.odpi.openmetadata.adapters.connectors.integration.openlineage.ffdc.OpenLineageIntegrationConnectorAuditCode;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.properties.ConnectionProperties;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementClassification;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementHeader;
import org.odpi.openmetadata.integrationservices.lineage.connector.LineageIntegratorConnector;
import org.odpi.openmetadata.integrationservices.lineage.connector.LineageIntegratorContext;
import org.odpi.openmetadata.integrationservices.lineage.properties.OpenLineageInputDataSet;
import org.odpi.openmetadata.integrationservices.lineage.properties.OpenLineageJob;
import org.odpi.openmetadata.integrationservices.lineage.properties.OpenLineageNominalTimeRunFacet;
import org.odpi.openmetadata.integrationservices.lineage.properties.OpenLineageOutputDataSet;
import org.odpi.openmetadata.integrationservices.lineage.properties.OpenLineageParentRunFacet;
import org.odpi.openmetadata.integrationservices.lineage.properties.OpenLineageParentRunFacetJob;
import org.odpi.openmetadata.integrationservices.lineage.properties.OpenLineageParentRunFacetRun;
import org.odpi.openmetadata.integrationservices.lineage.properties.OpenLineageRun;
import org.odpi.openmetadata.integrationservices.lineage.properties.OpenLineageRunEvent;
import org.odpi.openmetadata.integrationservices.lineage.properties.OpenLineageRunFacets;

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
 * connector running in the same instance of Lineage Integration OMIS.
 */
public class GovernanceActionOpenLineageIntegrationConnector extends LineageIntegratorConnector implements AssetManagerEventListener
{
    private static final String inProgressGovernanceActionStatus = "InProgress";
    private static final String actionedGovernanceActionStatus   = "Actioned";
    private static final String invalidGovernanceActionStatus    = "Invalid";
    private static final String failedGovernanceActionStatus     = "Failed";

    private static URI                      producer  = URI.create("https://egeria-project.org/");
    private final  ZoneId                   zoneId    = ZoneId.systemDefault();
    private        LineageIntegratorContext myContext = null;


    /**
     * Default constructor
     */
    public GovernanceActionOpenLineageIntegrationConnector()
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

        final String methodName = "start";

        myContext = super.getContext();

        if (myContext != null)
        {
            try
            {
                myContext.registerListener(this);
            }
            catch (Exception error)
            {
                if (auditLog != null)
                {
                    auditLog.logException(methodName,
                                          OpenLineageIntegrationConnectorAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                                             error.getClass().getName(),
                                                                                                                             methodName,
                                                                                                                             error.getMessage()),
                                          error);
                }
            }
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
    public void processEvent(AssetManagerOutTopicEvent event)
    {
        final String methodName = "processEvent";

        ElementHeader elementHeader = event.getElementHeader();

        if (((event.getEventType() == AssetManagerEventType.NEW_ELEMENT_CREATED) ||
             (event.getEventType() == AssetManagerEventType.ELEMENT_UPDATED)) &&
            ("GovernanceAction".equals(elementHeader.getType().getTypeName())))
        {
            try
            {
                String previousActionStatus = "";

                if (event.getPreviousElementProperties() != null)
                {
                    previousActionStatus = event.getPreviousElementProperties().get("actionStatus").toString();
                }

                String currentActionStatus = event.getElementProperties().get("actionStatus").toString();

                /*
                 * Only output an event if the status has changed.
                 */
                if (! previousActionStatus.equals(currentActionStatus))
                {
                    if ((inProgressGovernanceActionStatus.equals(currentActionStatus)) ||
                                (actionedGovernanceActionStatus.equals(currentActionStatus)) ||
                                (failedGovernanceActionStatus.equals(currentActionStatus)) ||
                                (invalidGovernanceActionStatus.equals(currentActionStatus)))
                    {
                        GovernanceActionElement governanceAction = myContext.getGovernanceAction(elementHeader.getGUID());

                        publishOpenLineageEvent(currentActionStatus,
                                                event.getEventTime(),
                                                governanceAction);
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
     * Add information about the governance action into an OpenLineage event and publish it.
     *
     * @param governanceActionStatus the status from the entity at the time of the event
     * @param eventTime the time of the change to the entity
     * @param governanceAction source information
     */
    private void publishOpenLineageEvent(String                  governanceActionStatus,
                                         Date                    eventTime,
                                         GovernanceActionElement governanceAction)
    {
        OpenLineageRunEvent event = new OpenLineageRunEvent();

        event.setProducer(producer);
        event.setEventTime(getTimeStamp(eventTime));



        if (inProgressGovernanceActionStatus.equals(governanceActionStatus))
        {
            event.setEventType("START");
        }
        else if (actionedGovernanceActionStatus.equals(governanceActionStatus))
        {
            event.setEventType("COMPLETE");
        }
        else if (failedGovernanceActionStatus.equals(governanceActionStatus))
        {
            event.setEventType("FAIL");
        }
        else if (invalidGovernanceActionStatus.equals(governanceActionStatus))
        {
            event.setEventType("ABORT");
        }

        GovernanceActionProperties properties = governanceAction.getProperties();

        String namespace = properties.getProcessName();

        /*
         * This is a workaround for Marquez that limits its namespaces to 64 chars.
         */
        if (namespace.length() > 64)
        {
            String[] tokens = namespace.split(":");

            namespace = tokens[tokens.length - 1];
        }

        OpenLineageJob job = new OpenLineageJob();

        if (properties.getGovernanceActionTypeName() != null)
        {
            job.setName(properties.getGovernanceActionTypeName());
        }
        else
        {
            job.setName(properties.getGovernanceEngineName() + ":" + properties.getRequestType());
        }

        job.setNamespace(namespace);

        event.setJob(job);

        OpenLineageRun run = new OpenLineageRun();

        run.setRunId(UUID.fromString(governanceAction.getElementHeader().getGUID()));

        String anchorGUID = null;

        List<ElementClassification> classifications = governanceAction.getElementHeader().getClassifications();

        if (classifications != null)
        {
            for (ElementClassification classification : classifications)
            {
                if ((classification != null) && ("Anchors".equals(classification.getClassificationName())))
                {
                    if (classification.getClassificationProperties() != null)
                    {
                        anchorGUID = classification.getClassificationProperties().get("anchorGUID").toString();
                    }
                }
            }
        }

        OpenLineageRunFacets runFacets = new OpenLineageRunFacets();

        if (anchorGUID != null)
        {

            OpenLineageParentRunFacet    parentRunFacet    = new OpenLineageParentRunFacet();
            OpenLineageParentRunFacetJob parentRunFacetJob = new OpenLineageParentRunFacetJob();
            OpenLineageParentRunFacetRun parentRunFacetRun = new OpenLineageParentRunFacetRun();

            parentRunFacet.set_producer(producer);

            parentRunFacetJob.setName(properties.getProcessName());
            parentRunFacetJob.setNamespace(namespace);

            parentRunFacet.setJob(parentRunFacetJob);

            parentRunFacetRun.setRunId(UUID.fromString(anchorGUID));

            parentRunFacet.setRun(parentRunFacetRun);

            runFacets.setParent(parentRunFacet);
        }

        OpenLineageNominalTimeRunFacet nominalTimeRunFacet = new OpenLineageNominalTimeRunFacet();

        nominalTimeRunFacet.set_producer(producer);

        if (properties.getStartTime() == null)
        {
            nominalTimeRunFacet.setNominalStartTime(getTimeStamp(properties.getRequestedTime()));
        }
        else
        {
            nominalTimeRunFacet.setNominalStartTime(getTimeStamp(properties.getStartTime()));
        }

        if (properties.getCompletionTime() != null)
        {
            nominalTimeRunFacet.setNominalEndTime(getTimeStamp(properties.getCompletionTime()));
        }

        runFacets.setNominalTime(nominalTimeRunFacet);

        run.setFacets(runFacets);

        event.setRun(run);

        List<OpenLineageInputDataSet> inputDataSets = new ArrayList<>();

        if (governanceAction.getActionTargetElements() != null)
        {
            for (ActionTargetElement actionTarget : governanceAction.getActionTargetElements())
            {
                if (actionTarget != null)
                {
                    OpenLineageInputDataSet inputDataSet = new OpenLineageInputDataSet();

                    inputDataSet.setName(actionTarget.getActionTargetName());
                    inputDataSet.setNamespace(namespace);

                    inputDataSets.add(inputDataSet);
                }
            }
        }

        if (properties.getReceivedGuards() != null)
        {
            for (String guard : properties.getReceivedGuards())
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

        if (properties.getRequestParameters() != null)
        {
            for (String requestParameter : properties.getRequestParameters().values())
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

        if (properties.getCompletionTime() != null)
        {
            List<OpenLineageOutputDataSet> outputDataSets = new ArrayList<>();

            if (properties.getCompletionGuards() != null)
            {
                for (String guard : properties.getCompletionGuards())
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

        myContext.publishOpenLineageRunEvent(event);
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
