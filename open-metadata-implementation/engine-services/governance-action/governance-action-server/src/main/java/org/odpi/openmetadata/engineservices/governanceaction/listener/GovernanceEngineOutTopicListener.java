/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.engineservices.governanceaction.listener;

import org.odpi.openmetadata.accessservices.governanceengine.api.GovernanceEngineEventListener;
import org.odpi.openmetadata.accessservices.governanceengine.events.*;
import org.odpi.openmetadata.engineservices.governanceaction.ffdc.GovernanceActionAuditCode;
import org.odpi.openmetadata.engineservices.governanceaction.handlers.GovernanceActionEngineHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.governanceaction.events.WatchdogGovernanceEvent;
import org.odpi.openmetadata.governanceservers.enginehostservices.admin.GovernanceEngineHandler;
import org.odpi.openmetadata.governanceservers.enginehostservices.enginemap.GovernanceEngineMap;


/**
 * GovernanceEngineOutTopicListener is a class that is registered to listen on the Governance Engine OMAS's
 * out topic to receive any changes to metadata.
 */
public class GovernanceEngineOutTopicListener extends GovernanceEngineEventListener
{
    private final GovernanceEngineMap governanceEngineHandlers;
    private final AuditLog            auditLog;

    /**
     * Constructor for the listener.  Its job is to receive events and pass the information received on to the
     * appropriate governance engine handler.
     *
     * @param governanceEngineHandlers these are the handlers for all the governance engines that are hosted by this
     *                                engine host server.
     * @param auditLog logging destination
     */
    public GovernanceEngineOutTopicListener(GovernanceEngineMap governanceEngineHandlers,
                                            AuditLog            auditLog)
    {
        this.governanceEngineHandlers = governanceEngineHandlers;
        this.auditLog = auditLog;
    }


    /**
     * Process an event that was published by the Governance Engine OMAS.  The events cover all defined governance engines and actions.
     * This method only needs to pass on the information to those governance engines hosted in this server.
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
            if (event instanceof WatchdogGovernanceServiceEvent watchdogGovernanceServiceEvent)
            {
                if (watchdogGovernanceServiceEvent.getWatchdogGovernanceEvent() != null)
                {
                    WatchdogGovernanceEvent watchdogGovernanceEvent = watchdogGovernanceServiceEvent.getWatchdogGovernanceEvent();

                    /*
                     * The watchdog event is for all governance engines
                     */
                    for (GovernanceEngineHandler governanceEngineHandler : governanceEngineHandlers.getGovernanceEngineHandlers())
                    {
                        if (governanceEngineHandler instanceof GovernanceActionEngineHandler governanceActionEngineHandler)
                        {
                            try
                            {
                                governanceActionEngineHandler.publishWatchdogEvent(watchdogGovernanceEvent);
                            }
                            catch (Exception error)
                            {
                                auditLog.logException(actionDescription,
                                                      GovernanceActionAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(
                                                              error.getClass().getName(),
                                                              actionDescription,
                                                              error.getMessage()),
                                                      error);
                            }
                        }
                    }
                }
            }
        }
    }
}
