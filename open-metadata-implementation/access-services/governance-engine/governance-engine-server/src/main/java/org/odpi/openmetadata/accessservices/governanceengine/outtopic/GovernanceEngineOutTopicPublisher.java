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
     * Send publish refresh event for the governance engine.
     *
     * @param governanceEngineGUID unique identifier for the governance engine
     * @param governanceEngineName unique name for the governance engine
     */
    void publishRefreshGovernanceEngineEvent(String governanceEngineGUID,
                                             String governanceEngineName)
    {
        final String methodName = "publishRefreshGovernanceEngineEvent";

        if (outTopicServerConnector != null)
        {
            try
            {
                GovernanceEngineConfigurationEvent newEvent = new GovernanceEngineConfigurationEvent();

                newEvent.setEventType(GovernanceEngineEventType.REFRESH_GOVERNANCE_ENGINE_EVENT);
                newEvent.setGovernanceEngineGUID(governanceEngineGUID);
                newEvent.setGovernanceEngineName(governanceEngineName);

                outTopicServerConnector.sendEvent(newEvent);

                if (outTopicAuditLog != null)
                {
                    outTopicAuditLog.logMessage(actionDescription,
                                                GovernanceEngineAuditCode.REFRESH_GOVERNANCE_ENGINE.getMessageDefinition(governanceEngineName,
                                                                                                                         governanceEngineGUID));
                }
            }
            catch (Exception error)
            {
                logUnexpectedPublishingException(error, methodName);
            }
        }
    }


    /**
     * Send publish refresh event for the registered governance service.
     *
     * @param governanceEngineGUID unique identifier for the governance engine
     * @param governanceEngineName unique name for the governance engine
     * @param registeredGovernanceServiceGUID unique identifier for the registered governance service
     * @param governanceRequestType a governance request type that triggers the instantiation of the
     *                              governance service
     */
    void publishRefreshGovernanceServiceEvent(String       governanceEngineGUID,
                                              String       governanceEngineName,
                                              String       registeredGovernanceServiceGUID,
                                              String       governanceRequestType)
    {
        final String methodName = "publishRefreshGovernanceServiceEvent";

        if (outTopicServerConnector != null)
        {
            try
            {
                GovernanceServiceConfigurationEvent newEvent = new GovernanceServiceConfigurationEvent();

                newEvent.setEventType(GovernanceEngineEventType.REFRESH_GOVERNANCE_SERVICE_EVENT);
                newEvent.setGovernanceEngineGUID(governanceEngineGUID);
                newEvent.setGovernanceEngineName(governanceEngineName);
                newEvent.setRegisteredGovernanceServiceGUID(registeredGovernanceServiceGUID);
                newEvent.setRequestType(governanceRequestType);

                outTopicServerConnector.sendEvent(newEvent);

                if (outTopicAuditLog != null)
                {
                    outTopicAuditLog.logMessage(actionDescription,
                                                GovernanceEngineAuditCode.REFRESH_GOVERNANCE_SERVICE.getMessageDefinition(governanceEngineName,
                                                                                                                          governanceEngineGUID,
                                                                                                                          governanceRequestType,
                                                                                                                          registeredGovernanceServiceGUID));
                }
            }
            catch (Exception error)
            {
                logUnexpectedPublishingException(error, methodName);
            }
        }
    }


    /**
     * Publish an event to notify listeners that there is a new governance action available for processing.
     *
     * @param governanceEngineGUID unique identifier for the governance engine
     * @param governanceEngineName unique name for the governance engine
     * @param governanceActionGUID element to execute
     */
    void publishNewGovernanceAction(String governanceEngineGUID,
                                    String governanceEngineName,
                                    String governanceActionGUID)
    {
        final String methodName = "publishNewGovernanceAction";

        if (outTopicServerConnector != null)
        {
            try
            {
                GovernanceActionEvent newEvent = new GovernanceActionEvent();

                newEvent.setEventType(GovernanceEngineEventType.REQUESTED_GOVERNANCE_ACTION_EVENT);
                newEvent.setGovernanceEngineGUID(governanceEngineGUID);
                newEvent.setGovernanceEngineName(governanceEngineName);
                newEvent.setGovernanceActionGUID(governanceActionGUID);

                outTopicServerConnector.sendEvent(newEvent);

                if (outTopicAuditLog != null)
                {
                    outTopicAuditLog.logMessage(actionDescription,
                                                GovernanceEngineAuditCode.NEW_GOVERNANCE_ACTION.getMessageDefinition(governanceActionGUID,
                                                                                                                     governanceEngineName,
                                                                                                                     governanceEngineGUID));
                }
            }
            catch (Exception error)
            {
                logUnexpectedPublishingException(error, methodName);
            }
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
