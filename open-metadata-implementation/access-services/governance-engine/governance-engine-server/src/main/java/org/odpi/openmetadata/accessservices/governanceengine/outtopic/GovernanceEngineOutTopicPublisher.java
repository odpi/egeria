/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.governanceengine.outtopic;

import org.odpi.openmetadata.accessservices.governanceengine.connectors.outtopic.GovernanceEngineOutTopicServerConnector;
import org.odpi.openmetadata.accessservices.governanceengine.events.GovernanceEngineConfigurationEvent;
import org.odpi.openmetadata.accessservices.governanceengine.events.GovernanceEngineEventType;
import org.odpi.openmetadata.accessservices.governanceengine.events.GovernanceServiceConfigurationEvent;
import org.odpi.openmetadata.accessservices.governanceengine.ffdc.GovernanceEngineAuditCode;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;

/**
 * GovernanceEngineOutTopicPublisher is responsible for pushing events to the Governance Engine OMAS's out topic.
 */
public class GovernanceEngineOutTopicPublisher
{
    private GovernanceEngineOutTopicServerConnector outTopicServerConnector;
    private AuditLog                                outTopicAuditLog;
    private String                                  outTopicName;

    private final String actionDescription = "Out topic configuration refresh event publishing";

    /**
     * Constructor for the publisher.
     *
     * @param outTopicServerConnector connector to the out topic
     * @param outTopicName name of the out topic
     * @param outTopicAuditLog logging destination if anything goes wrong.
     */
    public GovernanceEngineOutTopicPublisher(GovernanceEngineOutTopicServerConnector outTopicServerConnector,
                                             String                                 outTopicName,
                                             AuditLog                               outTopicAuditLog)
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
     * Send a publish refresh event for the governance engine.
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
                logUnexpectedPublishingException(governanceEngineGUID, governanceEngineName, error, methodName);
            }
        }
    }


    /**
     * Send a publish refresh event for the registered governance service.
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
                logUnexpectedPublishingException(governanceEngineGUID, governanceEngineName, error, methodName);
            }
        }
    }


    /**
     * Log any exceptions that have come from the publishing process.
     *
     * @param governanceEngineGUID unique identifier for the governance engine
     * @param governanceEngineName unique name for the governance engine
     * @param error this is the exception that was thrown
     * @param methodName this is the calling method
     */
    private void logUnexpectedPublishingException(String     governanceEngineGUID,
                                                  String     governanceEngineName,
                                                  Throwable  error,
                                                  String     methodName)
    {

        if (outTopicAuditLog != null)
        {
            outTopicAuditLog.logException(actionDescription,
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
        catch (Throwable error)
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
