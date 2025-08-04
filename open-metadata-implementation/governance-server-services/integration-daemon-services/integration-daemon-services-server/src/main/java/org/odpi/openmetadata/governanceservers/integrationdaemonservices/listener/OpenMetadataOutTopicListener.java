/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.governanceservers.integrationdaemonservices.listener;


import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.events.OpenMetadataEventListener;
import org.odpi.openmetadata.frameworks.openmetadata.events.OpenMetadataOutTopicEvent;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementHeader;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworkservices.gaf.client.GovernanceConfigurationClient;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.ffdc.IntegrationDaemonServicesAuditCode;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.handlers.IntegrationGroupHandler;

import java.util.List;


/**
 * OpenMetadataOutTopicListener is a class that is registered to listen on the Open Metadata Services
 * out topic to receive any changes to its specific integration group's configuration.
 * At the start, only the name of the integration group is known.  The events include the
 * element header only - ie the guid not the name.  So while the GUID of the integration group
 * is not known, the listener kicks off a refresh to retrieve the configuration each time there
 * is a change to any integration group in the hope that it is the right one.
 *
 */
public class OpenMetadataOutTopicListener implements OpenMetadataEventListener
{
    private final String                        groupName;
    private final IntegrationGroupHandler       groupHandler;
    private final GovernanceConfigurationClient configurationClient;
    private final String                        userId;
    private final AuditLog                      auditLog;
    private final PropertyHelper                propertyHelper = new PropertyHelper();

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
    public OpenMetadataOutTopicListener(String                        groupName,
                                        IntegrationGroupHandler       integrationGroupHandler,
                                        GovernanceConfigurationClient configurationClient,
                                        String                        userId,
                                        AuditLog                      auditLog)
    {
        this.groupName                = groupName;
        this.groupHandler             = integrationGroupHandler;
        this.configurationClient      = configurationClient;
        this.userId                   = userId;
        this.auditLog                 = auditLog;
    }


    /**
     * Process an event that was published by the Open Metadata Services.
     * This method only needs to pass on the information to those integration groups hosted in this server.
     * There i
     * Events relating to other integration groups can be ignored.
     * So can events that are for capabilities not supported by these integration daemon services.
     *
     * @param event event object - call getEventType to find out what type of event.
     */
    @Override
    public void processEvent(OpenMetadataOutTopicEvent event)
    {
        if ((event != null) && event.getElementHeader() != null)
        {
            if (propertyHelper.isTypeOf(event.getElementHeader(), OpenMetadataType.INTEGRATION_GROUP.typeName))
            {
                if (groupHandler.getIntegrationGroupGUID() == null)
                {
                    processIntegrationGroupEvent(event);
                }
                else if (event.getElementHeader().getGUID().equals(groupHandler.getIntegrationGroupGUID()))
                {
                    processIntegrationGroupEvent(event);
                }
            }
            else if (propertyHelper.isTypeOf(event.getElementHeader(), OpenMetadataType.CATALOG_TARGET_RELATIONSHIP.typeName))
            {
                processIntegrationConnectorEvent(event.getEndOneElementHeader(), event);
            }
            else if (propertyHelper.isTypeOf(event.getElementHeader(), OpenMetadataType.REGISTERED_INTEGRATION_CONNECTOR_RELATIONSHIP.typeName))
            {
                processIntegrationConnectorEvent(event.getEndTwoElementHeader(), event);
            }
        }
    }


    /**
     * Process a change to an integration group.
     *
     * @param event event bring news of the change
     */
    private void processIntegrationGroupEvent(OpenMetadataOutTopicEvent event)
    {
        final String actionDescription = "processIntegrationGroupEvent";

        try
        {
            groupHandler.refreshConfig();
        }
        catch (InvalidParameterException error)
        {
            auditLog.logMessage(actionDescription,
                                IntegrationDaemonServicesAuditCode.GROUP_CHANGE_FAILED.getMessageDefinition(groupName,
                                                                                                            error.getClass().getName(),
                                                                                                            error.getMessage()),
                                event.toString());
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


    /**
     * Review the current values for an integration connector definition in the metadata catalog.
     *
     * @param integrationConnectorHeader triggering element
     * @param event full event
     */
    private void processIntegrationConnectorEvent(ElementHeader             integrationConnectorHeader,
                                                  OpenMetadataOutTopicEvent event)
    {
        final String actionDescription = "processIntegrationConnectorEvent";

        try
        {
            List<String> registrations = configurationClient.getIntegrationConnectorRegistrations(userId, integrationConnectorHeader.getGUID());

            if (registrations != null)
            {
                for (String groupGUID: registrations)
                {
                    if (groupGUID != null)
                    {
                        if (groupGUID.equals(groupHandler.getIntegrationGroupGUID()))
                        {
                            groupHandler.refreshConnectorConfig(integrationConnectorHeader.getGUID());
                        }
                    }
                }
            }
        }
        catch (Exception error)
        {
            String integrationConnectorId = null;

            if (integrationConnectorHeader != null)
            {
                integrationConnectorId = integrationConnectorHeader.getGUID();
            }

            auditLog.logException(actionDescription,
                                  IntegrationDaemonServicesAuditCode.CONNECTOR_CHANGE_FAILED.getMessageDefinition(integrationConnectorId,
                                                                                                                  error.getClass().getName(),
                                                                                                                  error.getMessage()),
                                  event.toString(),
                                  error);
        }
    }
}
