/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.governanceservers.enginehostservices.listener;

import org.odpi.openmetadata.adminservices.configuration.registration.GovernanceServicesDescription;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.events.OpenMetadataEventListener;
import org.odpi.openmetadata.frameworks.openmetadata.events.OpenMetadataOutTopicEvent;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.governanceservers.enginehostservices.admin.GovernanceEngineHandler;
import org.odpi.openmetadata.governanceservers.enginehostservices.enginemap.GovernanceEngineMap;
import org.odpi.openmetadata.governanceservers.enginehostservices.ffdc.EngineHostServicesAuditCode;

import java.util.List;


/**
 * OpenMetadataOutTopicListener is a class that is registered to listen on the Governance Server OMAS's
 * out topic to receive any changes to the governance engines' configuration or new governance actions.
 */
public class OpenMetadataOutTopicListener implements OpenMetadataEventListener
{
    private final GovernanceEngineMap governanceEngineHandlers;
    private final AuditLog       auditLog;
    private final PropertyHelper propertyHelper = new PropertyHelper();


    /**
     * Constructor for the listener.  Its job is to receive events and pass the information received on to the
     * appropriate governance engine handler.
     *
     * @param governanceEngineHandlers these are the handlers for all the governance engines that are hosted by this
     *                                engine host server.
     * @param auditLog logging destination
     */
    public OpenMetadataOutTopicListener(GovernanceEngineMap governanceEngineHandlers,
                                        AuditLog            auditLog)
    {
        this.governanceEngineHandlers = governanceEngineHandlers;
        this.auditLog = auditLog;
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
            if (propertyHelper.isTypeOf(event.getElementHeader(), OpenMetadataType.GOVERNANCE_ENGINE.typeName))
            {
                processGovernanceEngineEvent(event);
            }
            else if (propertyHelper.isTypeOf(event.getElementHeader(), OpenMetadataType.ENGINE_ACTION.typeName))
            {
                processEngineActionEvent(event);
            }
            else if (propertyHelper.isTypeOf(event.getElementHeader(), OpenMetadataType.SUPPORTED_GOVERNANCE_SERVICE_RELATIONSHIP.typeName))
            {
                processGovernanceServiceEvent(event);
            }
        }
    }



    /**
     * Process a change to an engine action. Each engine is given an opportunity to claim it.
     *
     * @param event event bring news of the change
     */
    private void processEngineActionEvent(OpenMetadataOutTopicEvent event)
    {
        final String actionDescription = "processEngineActionEvent";

        List<GovernanceEngineHandler> governanceEngineHandlerList = governanceEngineHandlers.getGovernanceEngineHandlers();

        if (governanceEngineHandlerList != null)
        {
            for (GovernanceEngineHandler governanceEngineHandler : governanceEngineHandlerList)
            {
                if (governanceEngineHandler != null)
                {
                    try
                    {
                        governanceEngineHandler.executeEngineAction(event.getElementHeader().getGUID());
                    }
                    catch (Exception error)
                    {
                        auditLog.logException(actionDescription,
                                              EngineHostServicesAuditCode.ENGINE_ACTION_FAILED.getMessageDefinition(governanceEngineHandler.getGovernanceEngineName(),
                                                                                                                    error.getClass().getName(),
                                                                                                                    error.getMessage()),
                                              event.toString(),
                                              error);
                    }
                }
            }
        }
    }


    /**
     * Process a change to a governance engine.
     *
     * @param event event bring news of the change
     */
    private void processGovernanceEngineEvent(OpenMetadataOutTopicEvent event)
    {
        final String actionDescription = "processGovernanceEngineEvent";

        List<GovernanceEngineHandler> governanceEngineHandlerList = governanceEngineHandlers.getGovernanceEngineHandlers();

        /*
         * Try each of the governance engine handlers.  They may or may not know their GUID at this time.
         */
        if (governanceEngineHandlerList != null)
        {
            for (GovernanceEngineHandler governanceEngineHandler : governanceEngineHandlerList)
            {
                if (governanceEngineHandler != null)
                {
                    try
                    {
                        governanceEngineHandler.refreshConfig(event.getElementHeader().getGUID());
                    }
                    catch (Exception error)
                    {
                        auditLog.logException(actionDescription,
                                              EngineHostServicesAuditCode.GOVERNANCE_ENGINE_NO_CONFIG.getMessageDefinition(governanceEngineHandler.getGovernanceEngineName(),
                                                                                                                           error.getClass().getName(),
                                                                                                                           error.getMessage()),
                                              event.toString(),
                                              error);
                    }
                }
            }
        }
    }


    /**
     * Review the current values for a governance service definition in the metadata catalog.
     *
     * @param event full event
     */
    private void processGovernanceServiceEvent(OpenMetadataOutTopicEvent event)
    {
        final String actionDescription = "processGovernanceServiceEvent";

        List<GovernanceEngineHandler> governanceEngineHandlerList = governanceEngineHandlers.getGovernanceEngineHandlers();

        if (governanceEngineHandlerList != null)
        {
            for (GovernanceEngineHandler governanceEngineHandler : governanceEngineHandlerList)
            {
                if (governanceEngineHandler != null)
                {
                    try
                    {
                        governanceEngineHandler.refreshServiceConfig(event.getEndOneElementHeader().getGUID(),
                                                                     event.getEndTwoElementHeader().getGUID(),
                                                                     propertyHelper.getStringProperty(GovernanceServicesDescription.ENGINE_HOST_SERVICES.getServiceName(),
                                                                                                      OpenMetadataProperty.SERVICE_REQUEST_TYPE.name,
                                                                                                      event.getElementProperties(),
                                                                                                      actionDescription));
                    }
                    catch (Exception error)
                    {
                        String eventProperties = "<null>";

                        if (event.getElementProperties() != null)
                        {
                            eventProperties = event.getElementProperties().toString();
                        }
                        auditLog.logException(actionDescription,
                                              EngineHostServicesAuditCode.GOVERNANCE_SERVICE_NO_CONFIG.getMessageDefinition(event.getEndOneElementHeader().getGUID(),
                                                                                                                            event.getEndTwoElementHeader().getGUID(),
                                                                                                                            eventProperties,
                                                                                                                            error.getClass().getName(),
                                                                                                                            error.getMessage()),
                                              event.toString(),
                                              error);
                    }
                }
            }
        }
    }
}
