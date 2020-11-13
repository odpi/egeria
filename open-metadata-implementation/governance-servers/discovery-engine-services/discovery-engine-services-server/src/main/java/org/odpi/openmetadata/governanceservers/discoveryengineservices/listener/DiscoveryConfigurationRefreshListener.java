/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.governanceservers.discoveryengineservices.listener;

import org.odpi.openmetadata.accessservices.discoveryengine.api.DiscoveryEngineEventListener;
import org.odpi.openmetadata.accessservices.discoveryengine.events.DiscoveryEngineConfigurationEvent;
import org.odpi.openmetadata.accessservices.discoveryengine.events.DiscoveryEngineEvent;
import org.odpi.openmetadata.accessservices.discoveryengine.events.DiscoveryServiceConfigurationEvent;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.governanceservers.discoveryengineservices.ffdc.DiscoveryEngineServicesAuditCode;
import org.odpi.openmetadata.governanceservers.discoveryengineservices.handlers.DiscoveryEngineHandler;

import java.util.Map;

/**
 * DiscoveryConfigurationRefreshListener is a class that is registered to listen on the Discovery Engine OMAS's
 * out topic to receive any changes to the discovery engines' configuration.
 */
public class DiscoveryConfigurationRefreshListener extends DiscoveryEngineEventListener
{
    private Map<String, DiscoveryEngineHandler> discoveryEngineHandlers;
    private AuditLog                            auditLog;

    /**
     * Constructor for the listener.  Its job is to receive events and pass the information received on to the
     * appropriate discovery engine handler.
     *
     * @param discoveryEngineHandlers these are the handlers for all of the discovery engines that are hosted by this
     *                                discovery server.
     * @param auditLog logging destination
     */
    public DiscoveryConfigurationRefreshListener(Map<String, DiscoveryEngineHandler> discoveryEngineHandlers,
                                                 AuditLog                            auditLog)
    {
        this.discoveryEngineHandlers = discoveryEngineHandlers;
        this.auditLog = auditLog;
    }


    /**
     * Process an event that was published by the Discovery Engine OMAS.  The events cover all defined discovery engines.
     * This method only needs to pass on the information to those discovery engine hosted in this server.
     * Events relating to other discovery engines can be ignored.
     *
     * @param event event object - call getEventType to find out what type of event.
     */
    @Override
    public void processEvent(DiscoveryEngineEvent event)
    {
        final String actionDescription = "Process configuration event";

        if (event != null)
        {
            if (event instanceof DiscoveryServiceConfigurationEvent)
            {
                DiscoveryServiceConfigurationEvent discoveryServiceEvent = (DiscoveryServiceConfigurationEvent) event;
                DiscoveryEngineHandler             discoveryEngineHandler =
                        discoveryEngineHandlers.get(discoveryServiceEvent.getDiscoveryEngineName());

                if (discoveryEngineHandler != null)
                {
                    try
                    {
                        discoveryEngineHandler.refreshServiceConfig(discoveryServiceEvent.getRegisteredDiscoveryServiceGUID());
                    }
                    catch (Exception error)
                    {
                        auditLog.logException(actionDescription,
                                              DiscoveryEngineServicesAuditCode.DISCOVERY_SERVICE_NO_CONFIG.getMessageDefinition(discoveryServiceEvent.getRegisteredDiscoveryServiceGUID(),
                                                                                                                                discoveryServiceEvent.getDiscoveryRequestTypes().toString(),
                                                                                                                                error.getClass().getName(),
                                                                                                                                error.getMessage()),
                                              discoveryServiceEvent.toString(),
                                              error);
                    }
                }
            }
            else if (event instanceof DiscoveryEngineConfigurationEvent)
            {
                DiscoveryEngineConfigurationEvent discoveryEngineEvent = (DiscoveryEngineConfigurationEvent) event;
                DiscoveryEngineHandler            discoveryEngineHandler =
                        discoveryEngineHandlers.get(discoveryEngineEvent.getDiscoveryEngineName());

                if (discoveryEngineHandler != null)
                {
                    try
                    {
                        discoveryEngineHandler.refreshConfig();
                    }
                    catch (Exception error)
                    {
                        auditLog.logException(actionDescription,
                                              DiscoveryEngineServicesAuditCode.DISCOVERY_ENGINE_NO_CONFIG.getMessageDefinition(discoveryEngineEvent.getDiscoveryEngineName(),
                                                                                                                               error.getClass().getName(),
                                                                                                                               error.getMessage()),
                                              discoveryEngineEvent.toString(),
                                              error);
                    }
                }
            }
        }
    }
}
