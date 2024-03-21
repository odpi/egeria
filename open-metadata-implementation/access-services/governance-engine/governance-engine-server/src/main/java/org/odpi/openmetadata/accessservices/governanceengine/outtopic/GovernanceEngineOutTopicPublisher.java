/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.governanceengine.outtopic;

import org.odpi.openmetadata.accessservices.governanceengine.connectors.outtopic.GovernanceEngineOutTopicServerConnector;
import org.odpi.openmetadata.accessservices.governanceengine.events.*;
import org.odpi.openmetadata.accessservices.governanceengine.ffdc.GovernanceEngineAuditCode;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.governanceaction.events.WatchdogGovernanceEvent;

/**
 * GovernanceEngineOutTopicPublisher is responsible for pushing events to the Governance Engine OMAS's out topic.
 */
public class GovernanceEngineOutTopicPublisher
{
    private final GovernanceEngineOutTopicServerConnector outTopicServerConnector;
    private final AuditLog                                outTopicAuditLog;
    private final String                                  outTopicName;

    private final String actionDescription = "Out topic event publishing";

    /**
     * Constructor for the publisher.
     *
     * @param outTopicServerConnector connector to the out topic
     * @param outTopicName name of the out topic
     * @param outTopicAuditLog logging destination if anything goes wrong.
     */
    public GovernanceEngineOutTopicPublisher(GovernanceEngineOutTopicServerConnector outTopicServerConnector,
                                             String                                  outTopicName,
                                             AuditLog                                outTopicAuditLog)
    {
        this.outTopicServerConnector = outTopicServerConnector;
        this.outTopicAuditLog        = outTopicAuditLog;
        this.outTopicName            = outTopicName;

        if (outTopicAuditLog != null)
        {
            outTopicAuditLog.logMessage(actionDescription, GovernanceEngineAuditCode.SERVICE_PUBLISHING.getMessageDefinition(outTopicName));
        }
    }


    /**
     * Publish an event for Open Watchdog Governance Action Services.
     *
     * @param watchdogGovernanceEvent GAF defined watchdog event
     */
    void publishWatchdogEvent(WatchdogGovernanceEvent  watchdogGovernanceEvent)
    {
        final String methodName = "publishWatchdogEvent";

        if (outTopicServerConnector != null)
        {
            try
            {
                WatchdogGovernanceServiceEvent newEvent = new WatchdogGovernanceServiceEvent();

                newEvent.setEventType(GovernanceEngineEventType.WATCHDOG_GOVERNANCE_SERVICE_EVENT);
                newEvent.settWatchdogGovernanceEvent(watchdogGovernanceEvent);

                outTopicServerConnector.sendEvent(newEvent);

                if (outTopicAuditLog != null)
                {
                    outTopicAuditLog.logMessage(actionDescription,
                                                GovernanceEngineAuditCode.WATCHDOG_EVENT.getMessageDefinition(watchdogGovernanceEvent.getEventType().getName()));
                }
            }
            catch (Exception error)
            {
                logUnexpectedPublishingException(error, methodName);
            }
        }
    }


    /**
     * Log any exceptions that have come from the publishing process.
     *
     * @param error this is the exception that was thrown
     * @param methodName this is the calling method
     */
    private void logUnexpectedPublishingException(Exception  error,
                                                  String     methodName)
    {
        if (outTopicAuditLog != null)
        {
            outTopicAuditLog.logException(methodName,
                                          GovernanceEngineAuditCode.OUT_TOPIC_FAILURE.getMessageDefinition(outTopicName,
                                                                                                           error.getClass().getName(),
                                                                                                           error.getMessage()),
                                          error);
        }
    }


    /**
     * Shutdown the publishing process.
     */
    public void disconnect()
    {
        try
        {
            outTopicServerConnector.disconnect();

            if (outTopicAuditLog != null)
            {
                outTopicAuditLog.logMessage(actionDescription, GovernanceEngineAuditCode.PUBLISHING_SHUTDOWN.getMessageDefinition(outTopicName));
            }
        }
        catch (Exception error)
        {
            if (outTopicAuditLog != null)
            {
                outTopicAuditLog.logException(actionDescription,
                                              GovernanceEngineAuditCode.PUBLISHING_SHUTDOWN_ERROR.getMessageDefinition(error.getClass().getName(),
                                                                                                                      outTopicName,
                                                                                                                      error.getMessage()),
                                              error);
            }
        }
    }
}
