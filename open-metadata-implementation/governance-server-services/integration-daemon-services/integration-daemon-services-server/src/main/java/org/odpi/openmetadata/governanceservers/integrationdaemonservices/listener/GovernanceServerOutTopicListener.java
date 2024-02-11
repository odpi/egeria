/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.governanceservers.integrationdaemonservices.listener;


import org.odpi.openmetadata.accessservices.governanceserver.api.GovernanceServerEventListener;
import org.odpi.openmetadata.accessservices.governanceserver.client.IntegrationGroupConfigurationClient;
import org.odpi.openmetadata.accessservices.governanceserver.events.GovernanceServerEvent;
import org.odpi.openmetadata.accessservices.governanceserver.events.IntegrationConnectorConfigurationEvent;
import org.odpi.openmetadata.accessservices.governanceserver.events.IntegrationGroupConfigurationEvent;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.ffdc.IntegrationDaemonServicesAuditCode;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.handlers.IntegrationGroupHandler;

import java.util.List;


/**
 * GovernanceServerOutTopicListener is a class that is registered to listen on the Governance Server OMAS's
 * out topic to receive any changes to the integration groups' configuration.
 */
public class GovernanceServerOutTopicListener extends GovernanceServerEventListener
{
    private final String                               groupName;
    private final IntegrationGroupHandler             groupHandler;
    private final IntegrationGroupConfigurationClient configurationClient;
    private final String                              userId;
    private final AuditLog                             auditLog;

    /**
     * Constructor for the listener.  Its job is to receive events and pass the information received on to the
     * appropriate integration group handler.
     *
     * @param groupName name of the integration group
     * @param integrationGroupHandler the handler for an integration group that is hosted by this
     *                                integration daemon.
     * @param configurationClient client to extract extra information from the metadata server
     * @param userId useRId to use when calling the metadata server
     * @param auditLog logging destination
     */
    public GovernanceServerOutTopicListener(String                               groupName,
                                            IntegrationGroupHandler              integrationGroupHandler,
                                            IntegrationGroupConfigurationClient  configurationClient,
                                            String                               userId,
                                            AuditLog                             auditLog)
    {
        this.groupName                = groupName;
        this.groupHandler             = integrationGroupHandler;
        this.configurationClient      = configurationClient;
        this.userId                   = userId;
        this.auditLog                 = auditLog;
    }


    /**
     * Process an event that was published by the Governance Server OMAS.
     * The events cover all defined integration groups and connectors.
     * This method only needs to pass on the information to those integration groups hosted in this server.
     * Events relating to other integration groups can be ignored.
     * So can events that are for capabilities not supported by these integration daemon services.
     *
     * @param event event object - call getEventType to find out what type of event.
     */
    @Override
    public void processEvent(GovernanceServerEvent event)
    {
        if (event != null)
        {
            if (event instanceof IntegrationGroupConfigurationEvent integrationGroupConfigurationEvent)
            {
                processIntegrationGroupEvent(integrationGroupConfigurationEvent);
            }
            else if (event instanceof IntegrationConnectorConfigurationEvent integrationConnectorConfigurationEvent)
            {
                processIntegrationConnectorEvent(integrationConnectorConfigurationEvent);
            }
        }
    }


    /**
     * Process a change to an integration group.
     *
     * @param event event bring news of the change
     */
    private void processIntegrationGroupEvent(IntegrationGroupConfigurationEvent event)
    {
        final String actionDescription = "processIntegrationGroupEvent";

        if (event.getIntegrationGroupName() != null)
        {
            if (groupName.equals(event.getIntegrationGroupName()))
            {
                try
                {
                    groupHandler.refreshConfig();
                }
                catch (Exception error)
                {
                    auditLog.logException(actionDescription,
                                          IntegrationDaemonServicesAuditCode.GROUP_CHANGE_FAILED.getMessageDefinition(groupName,
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
     * @param event triggering event
     */
    private void processIntegrationConnectorEvent(IntegrationConnectorConfigurationEvent event)
    {
        final String actionDescription = "processIntegrationConnectorEvent";

        try
        {
            List<String> registrations = configurationClient.getIntegrationConnectorRegistrations(userId, event.getIntegrationConnectorGUID());

            if (registrations != null)
            {
                for (String groupGUID: registrations)
                {
                    if (groupGUID != null)
                    {
                        if (groupGUID.equals(groupHandler.getIntegrationGroupGUID()))
                        {
                            groupHandler.refreshConnectorConfig(event.getIntegrationConnectorGUID());
                        }
                    }
                }
            }
        }
        catch (Exception error)
        {
            String eventString = null;
            String integrationConnectorId = null;

            if (event != null)
            {
                eventString = event.toString();
                if (event.getIntegrationConnectorName() != null)
                {
                    integrationConnectorId = event.getIntegrationConnectorName();
                }
                else if (event.getIntegrationConnectorGUID() != null)
                {
                    integrationConnectorId = event.getIntegrationConnectorGUID();

                }
            }

            auditLog.logException(actionDescription,
                                  IntegrationDaemonServicesAuditCode.CONNECTOR_CHANGE_FAILED.getMessageDefinition(integrationConnectorId,
                                                                                                                  error.getClass().getName(),
                                                                                                                  error.getMessage()),
                                  eventString,
                                  error);
        }
    }
}
