/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.engineservices.governanceaction.listener;

import org.odpi.openmetadata.engineservices.governanceaction.ffdc.GovernanceActionAuditCode;
import org.odpi.openmetadata.engineservices.governanceaction.handlers.GovernanceActionEngineHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.events.OpenMetadataEventListener;
import org.odpi.openmetadata.frameworks.openmetadata.events.OpenMetadataOutTopicEvent;
import org.odpi.openmetadata.governanceservers.enginehostservices.admin.GovernanceEngineHandler;
import org.odpi.openmetadata.governanceservers.enginehostservices.enginemap.GovernanceEngineMap;


/**
 * OpenMetadataOutTopicListener is a class that is registered to listen on the Governance Engine OMAS's
 * out topic to receive any changes to metadata.
 */
public class OpenMetadataOutTopicListener implements OpenMetadataEventListener
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
    public OpenMetadataOutTopicListener(GovernanceEngineMap governanceEngineHandlers,
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
    public void processEvent(OpenMetadataOutTopicEvent event)
    {
        final String actionDescription = "Process OutTopic event";

        if (event != null)
        {
            for (GovernanceEngineHandler governanceEngineHandler : governanceEngineHandlers.getGovernanceEngineHandlers())
            {
                if (governanceEngineHandler instanceof GovernanceActionEngineHandler governanceActionEngineHandler)
                {
                    try
                    {
                        governanceActionEngineHandler.publishWatchdogEvent(event);
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
