/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.governanceservers.enginehostservices.listener;

import org.odpi.openmetadata.accessservices.governanceengine.api.GovernanceEngineEventListener;
import org.odpi.openmetadata.accessservices.governanceengine.events.GovernanceEngineConfigurationEvent;
import org.odpi.openmetadata.accessservices.governanceengine.events.GovernanceEngineEvent;
import org.odpi.openmetadata.accessservices.governanceengine.events.GovernanceServiceConfigurationEvent;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.governanceservers.enginehostservices.admin.GovernanceEngineHandler;
import org.odpi.openmetadata.governanceservers.enginehostservices.ffdc.EngineHostServicesAuditCode;

import java.util.Map;


/**
 * EngineConfigurationRefreshListener is a class that is registered to listen on the Governance Engine OMAS's
 * out topic to receive any changes to the governance engines' configuration.
 */
public class EngineConfigurationRefreshListener extends GovernanceEngineEventListener
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
    public EngineConfigurationRefreshListener(Map<String, GovernanceEngineHandler> governanceEngineHandlers,
                                              AuditLog                             auditLog)
    {
        this.governanceEngineHandlers = governanceEngineHandlers;
        this.auditLog = auditLog;
    }


    /**
     * Process an event that was published by the Governance Engine OMAS.  The events cover all defined governance engines.
     * This method only needs to pass on the information to those governance engine hosted in this server.
     * Events relating to other governance engines can be ignored.
     *
     * @param event event object - call getEventType to find out what type of event.
     */
    @Override
    public void processEvent(GovernanceEngineEvent event)
    {
        final String actionDescription = "Process configuration event";

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
        }
    }
}
