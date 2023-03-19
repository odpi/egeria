/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.governanceservers.integrationdaemonservices.listener;

import org.odpi.openmetadata.accessservices.governanceengine.api.GovernanceEngineEventListener;
import org.odpi.openmetadata.accessservices.governanceengine.client.IntegrationGroupConfigurationClient;
import org.odpi.openmetadata.accessservices.governanceengine.events.GovernanceEngineEvent;
import org.odpi.openmetadata.accessservices.governanceengine.events.WatchdogGovernanceServiceEvent;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.governanceaction.events.*;
import org.odpi.openmetadata.frameworks.governanceaction.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.governanceaction.search.PropertyValue;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.ffdc.IntegrationDaemonServicesAuditCode;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.handlers.IntegrationGroupHandler;

import java.util.List;
import java.util.Map;


/**
 * GovernanceEngineOutTopicListener is a class that is registered to listen on the Governance Engine OMAS's
 * out topic to receive any changes to the integration groups' configuration.
 */
public class GovernanceEngineOutTopicListener extends GovernanceEngineEventListener
{
    private final Map<String, IntegrationGroupHandler> integrationGroupHandlers;
    private final IntegrationGroupConfigurationClient  configurationClient;
    private final String                               userId;
    private final AuditLog                             auditLog;

    /**
     * Constructor for the listener.  Its job is to receive events and pass the information received on to the
     * appropriate integration group handler.
     *
     * @param integrationGroupHandlers these are the handlers for all the integration groups that are hosted by this
     *                                integration daemon.
     * @param configurationClient client to extract extra information from the metadata server
     * @param userId useRId to use when calling the metadata server
     * @param auditLog logging destination
     */
    public GovernanceEngineOutTopicListener(Map<String, IntegrationGroupHandler> integrationGroupHandlers,
                                            IntegrationGroupConfigurationClient  configurationClient,
                                            String                               userId,
                                            AuditLog                             auditLog)
    {
        this.integrationGroupHandlers = integrationGroupHandlers;
        this.configurationClient      = configurationClient;
        this.userId                   = userId;
        this.auditLog                 = auditLog;
    }


    /**
     * Process an event that was published by the Governance Engine OMAS.  The events cover all defined integration groups and actions.
     * This method only needs to pass on the information to those integration groups hosted in this server.
     * Events relating to other integration groups can be ignored.  So can events that are for capabilities not supported by these engine
     * services.
     *
     * @param event event object - call getEventType to find out what type of event.
     */
    @Override
    public void processEvent(GovernanceEngineEvent event)
    {
        final String actionDescription = "Process OutTopic event";

        if (event != null)
        {
            if (event instanceof WatchdogGovernanceServiceEvent)
            {
                /*
                 * The watchdog event is for all types of changes.  It is necessary to select only the events that concern
                 * an integration group
                 */
                WatchdogGovernanceServiceEvent watchdogGovernanceServiceEvent = (WatchdogGovernanceServiceEvent)event;

                if (watchdogGovernanceServiceEvent.getWatchdogGovernanceEvent() instanceof WatchdogClassificationEvent)
                {
                    WatchdogClassificationEvent watchdogClassificationEvent = (WatchdogClassificationEvent)watchdogGovernanceServiceEvent.getWatchdogGovernanceEvent();
                    String elementTypeName = watchdogClassificationEvent.getMetadataElement().getType().getTypeName();
                    if (elementTypeName.equals("IntegrationGroup"))
                    {
                        processIntegrationGroupEvent(watchdogClassificationEvent.getMetadataElement(), watchdogGovernanceServiceEvent);
                    }
                    else if (elementTypeName.equals("IntegrationConnector"))
                    {
                        processIntegrationConnectorEvent(watchdogClassificationEvent.getMetadataElement().getElementGUID(),
                                                         watchdogGovernanceServiceEvent);
                    }
                }
                else if (watchdogGovernanceServiceEvent.getWatchdogGovernanceEvent() instanceof WatchdogMetadataElementEvent)
                {
                    WatchdogMetadataElementEvent watchdogMetadataElementEvent = (WatchdogMetadataElementEvent)watchdogGovernanceServiceEvent.getWatchdogGovernanceEvent();
                    String elementTypeName = watchdogMetadataElementEvent.getMetadataElement().getType().getTypeName();
                    if (elementTypeName.equals("IntegrationGroup"))
                    {
                        processIntegrationGroupEvent(watchdogMetadataElementEvent.getMetadataElement(), watchdogGovernanceServiceEvent);
                    }
                    else if (elementTypeName.equals("IntegrationConnector"))
                    {
                        processIntegrationConnectorEvent(watchdogMetadataElementEvent.getMetadataElement().getElementGUID(),
                                                         watchdogGovernanceServiceEvent);
                    }
                }
                else if (watchdogGovernanceServiceEvent.getWatchdogGovernanceEvent() instanceof WatchdogRelatedElementsEvent)
                {
                    WatchdogRelatedElementsEvent watchdogMetadataElementEvent = (WatchdogRelatedElementsEvent)watchdogGovernanceServiceEvent.getWatchdogGovernanceEvent();
                    String elementTypeName = watchdogMetadataElementEvent.getRelatedMetadataElements().getType().getTypeName();
                    if (elementTypeName.equals("RegisteredIntegrationConnector"))
                    {
                        processIntegrationConnectorEvent(watchdogMetadataElementEvent.getRelatedMetadataElements().getElementGUIDAtEnd2(),
                                                         watchdogGovernanceServiceEvent);
                    }
                }
            }
        }
    }


    /**
     * Process a change to an integration group.
     *
     * @param integrationGroup metadata element representing an integration group.
     * @param event event bring news of the change
     */
    private void processIntegrationGroupEvent(OpenMetadataElement            integrationGroup,
                                              WatchdogGovernanceServiceEvent event)
    {
        final String actionDescription = "processIntegrationGroupEvent";

        PropertyValue elementQualifiedName = integrationGroup.getElementProperties().getPropertyValue("qualifiedName");

        if (elementQualifiedName != null)
        {
            IntegrationGroupHandler integrationGroupHandler = integrationGroupHandlers.get(elementQualifiedName.valueAsString());

            if (integrationGroupHandler != null)
            {
                try
                {
                    integrationGroupHandler.refreshConfig();
                }
                catch (Exception error)
                {
                    auditLog.logException(actionDescription,
                                          IntegrationDaemonServicesAuditCode.GROUP_CHANGE_FAILED.getMessageDefinition(
                                                  elementQualifiedName.valueAsString(),
                                                  error.getClass().getName(),
                                                  error.getMessage()),
                                          event.toString(),
                                          error);
                }
            }
        }
    }


    /**
     * Review the current values for an integration connector definition in the metadata catalog.
     *
     * @param integrationConnectorGUID unique identifier of the connector.
     * @param event triggering event
     */
    private void processIntegrationConnectorEvent(String                         integrationConnectorGUID,
                                                  WatchdogGovernanceServiceEvent event)
    {
        final String actionDescription = "processIntegrationConnectorEvent";

        try
        {
            List<String> registrations = configurationClient.getIntegrationConnectorRegistrations(userId, integrationConnectorGUID);

            if (registrations != null)
            {
                for (String groupGUID: registrations)
                {
                    if (groupGUID != null)
                    {
                        for (IntegrationGroupHandler handler : integrationGroupHandlers.values())
                        {
                            if (groupGUID.equals(handler.getIntegrationGroupGUID()))
                            {
                                handler.refreshConnectorConfig(integrationConnectorGUID);
                            }
                        }
                    }
                }
            }
        }
        catch (Exception error)
        {
            String eventString = null;

            if (event != null)
            {
                eventString = event.toString();
            }
            auditLog.logException(actionDescription,
                                  IntegrationDaemonServicesAuditCode.CONNECTOR_CHANGE_FAILED.getMessageDefinition(integrationConnectorGUID,
                                                                                                                  error.getClass().getName(),
                                                                                                                  error.getMessage()),
                                  eventString,
                                  error);
        }
    }
}
