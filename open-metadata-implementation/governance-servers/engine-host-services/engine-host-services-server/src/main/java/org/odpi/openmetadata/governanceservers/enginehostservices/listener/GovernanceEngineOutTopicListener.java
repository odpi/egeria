/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.governanceservers.enginehostservices.listener;

import org.odpi.openmetadata.accessservices.governanceengine.api.GovernanceEngineEventListener;
import org.odpi.openmetadata.accessservices.governanceengine.events.*;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.governanceservers.enginehostservices.admin.GovernanceEngineHandler;
import org.odpi.openmetadata.governanceservers.enginehostservices.ffdc.EngineHostServicesAuditCode;

import java.util.Map;


/**
 * GovernanceEngineOutTopicListener is a class that is registered to listen on the Governance Engine OMAS's
 * out topic to receive any changes to the governance engines' configuration or new governance actions.
 */
public class GovernanceEngineOutTopicListener extends GovernanceEngineEventListener
{
    private Map<String, GovernanceEngineHandler> governanceEngineHandlers;
    private AuditLog                             auditLog;

    /**
     * Constructor for the listener.  Its job is to receive events and pass the information received on to the
     * appropriate governance engine handler.
     *
     * @param governanceEngineHandlers these are the handlers for all of the governance engines that are hosted by this
     *                                engine host server.
     * @param auditLog logging destination
     */
    public GovernanceEngineOutTopicListener(Map<String, GovernanceEngineHandler> governanceEngineHandlers,
                                            AuditLog                             auditLog)
    {
        this.governanceEngineHandlers = governanceEngineHandlers;
        this.auditLog = auditLog;
    }


    /**
     * Process an event that was published by the Governance Engine OMAS.  The events cover all defined governance engines and actions.
     * This method only needs to pass on the information to those governance engine hosted in this server.
     * Events relating to other governance engines can be ignored.  So can events that are for capabilities not supported by these engine
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
            if (event instanceof GovernanceServiceConfigurationEvent)
            {
                GovernanceServiceConfigurationEvent governanceServiceEvent = (GovernanceServiceConfigurationEvent) event;
                GovernanceEngineHandler             governanceEngineHandler =
                        governanceEngineHandlers.get(governanceServiceEvent.getGovernanceEngineName());

                if (governanceEngineHandler != null)
                {
                    try
                    {
                        governanceEngineHandler.refreshServiceConfig(governanceServiceEvent.getRegisteredGovernanceServiceGUID());
                    }
                    catch (Exception error)
                    {
                        auditLog.logException(actionDescription,
                                              EngineHostServicesAuditCode.GOVERNANCE_SERVICE_NO_CONFIG.getMessageDefinition(governanceServiceEvent.getRegisteredGovernanceServiceGUID(),
                                                                                                                            governanceServiceEvent.getRequestType(),
                                                                                                                            error.getClass().getName(),
                                                                                                                            error.getMessage()),
                                              governanceServiceEvent.toString(),
                                              error);
                    }
                }
            }
            else if (event instanceof GovernanceEngineConfigurationEvent)
            {
                GovernanceEngineConfigurationEvent governanceEngineEvent = (GovernanceEngineConfigurationEvent) event;
                GovernanceEngineHandler            governanceEngineHandler =
                        governanceEngineHandlers.get(governanceEngineEvent.getGovernanceEngineName());

                if (governanceEngineHandler != null)
                {
                    try
                    {
                        governanceEngineHandler.refreshConfig();
                    }
                    catch (Exception error)
                    {
                        auditLog.logException(actionDescription,
                                              EngineHostServicesAuditCode.GOVERNANCE_ENGINE_NO_CONFIG.getMessageDefinition(governanceEngineEvent.getGovernanceEngineName(),
                                                                                                                           error.getClass().getName(),
                                                                                                                           error.getMessage()),
                                              governanceEngineEvent.toString(),
                                              error);
                    }
                }
            }
            else if (event instanceof GovernanceActionEvent)
            {
                GovernanceActionEvent   governanceActionEvent = (GovernanceActionEvent)event;
                GovernanceEngineHandler governanceEngineHandler = governanceEngineHandlers.get(governanceActionEvent.getGovernanceEngineName());

                if (governanceEngineHandler != null)
                {
                    try
                    {
                        governanceEngineHandler.executeGovernanceAction(governanceActionEvent.getGovernanceActionGUID());
                    }
                    catch (Exception error)
                    {
                        auditLog.logException(actionDescription,
                                              EngineHostServicesAuditCode.GOVERNANCE_ACTION_FAILED.getMessageDefinition(governanceActionEvent.getGovernanceEngineName(),
                                                                                                                        error.getClass().getName(),
                                                                                                                        error.getMessage()),
                                              governanceActionEvent.toString(),
                                              error);
                    }
                }
            }
            else if (event instanceof WatchdogGovernanceServiceEvent)
            {
                /*
                 * The watchdog event is for all governance engines
                 */
                WatchdogGovernanceServiceEvent watchdogGovernanceServiceEvent = (WatchdogGovernanceServiceEvent)event;

                for (GovernanceEngineHandler governanceEngineHandler : governanceEngineHandlers.values())
                {
                    if (governanceEngineHandler != null)
                    {
                        try
                        {
                            governanceEngineHandler.publishWatchdogEvent(watchdogGovernanceServiceEvent.getWatchdogGovernanceEvent());
                        }
                        catch (Exception error)
                        {
                            auditLog.logException(actionDescription,
                                                  EngineHostServicesAuditCode.GOVERNANCE_ACTION_FAILED.getMessageDefinition(
                                                          watchdogGovernanceServiceEvent.getGovernanceEngineName(),
                                                          error.getClass().getName(),
                                                          error.getMessage()),
                                                  watchdogGovernanceServiceEvent.toString(),
                                                  error);
                        }
                    }
                }
            }
        }
    }
}
