/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.governanceserver.outtopic;

import org.odpi.openmetadata.accessservices.governanceserver.connectors.outtopic.GovernanceServerOutTopicServerConnector;
import org.odpi.openmetadata.accessservices.governanceserver.events.*;
import org.odpi.openmetadata.accessservices.governanceserver.ffdc.GovernanceServerAuditCode;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

/**
 * GovernanceEngineOutTopicPublisher is responsible for pushing events to the Governance Server OMAS's out topic.
 */
public class GovernanceServerOutTopicPublisher
{
    private final GovernanceServerOutTopicServerConnector outTopicServerConnector;
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
    public GovernanceServerOutTopicPublisher(GovernanceServerOutTopicServerConnector outTopicServerConnector,
                                             String                                  outTopicName,
                                             AuditLog                                outTopicAuditLog)
    {
        this.outTopicServerConnector = outTopicServerConnector;
        this.outTopicAuditLog        = outTopicAuditLog;
        this.outTopicName            = outTopicName;

        if (outTopicAuditLog != null)
        {
            outTopicAuditLog.logMessage(actionDescription, GovernanceServerAuditCode.SERVICE_PUBLISHING.getMessageDefinition(outTopicName));
        }
    }


    /**
     * Send publish refresh event for the Integration Group.
     *
     * @param integrationGroupGUID unique identifier for the Integration Group
     * @param integrationGroupName unique name for the Integration Group
     */
    void publishRefreshIntegrationGroupEvent(String integrationGroupGUID,
                                             String integrationGroupName)
    {
        final String methodName = "publishRefreshIntegrationGroupEvent";

        if (outTopicServerConnector != null)
        {
            try
            {
                IntegrationGroupConfigurationEvent newEvent = new IntegrationGroupConfigurationEvent();

                newEvent.setEventType(GovernanceServerEventType.REFRESH_INTEGRATION_GROUP_EVENT);
                newEvent.setIntegrationGroupGUID(integrationGroupGUID);
                newEvent.setIntegrationGroupName(integrationGroupName);

                outTopicServerConnector.sendEvent(newEvent);

                if (outTopicAuditLog != null)
                {
                    outTopicAuditLog.logMessage(actionDescription,
                                                GovernanceServerAuditCode.REFRESH_INTEGRATION_DAEMON.getMessageDefinition(newEvent.getEventType().getEventTypeName(),
                                                                                                                          integrationGroupName,
                                                                                                                          integrationGroupGUID,
                                                                                                                          OpenMetadataType.INTEGRATION_GROUP.typeName));
                }
            }
            catch (Exception error)
            {
                logUnexpectedPublishingException(error, methodName);
            }
        }
    }


    /**
     * Send publish refresh event for the Integration Connector.
     *
     * @param integrationConnectorGUID unique identifier for the Integration Connector
     * @param integrationConnectorName unique name for the Integration Connector
     */
    void publishRefreshIntegrationConnectorEvent(String integrationConnectorGUID,
                                                 String integrationConnectorName)
    {
        final String methodName = "publishRefreshIntegrationConnectorEvent";

        if (outTopicServerConnector != null)
        {
            try
            {
                IntegrationConnectorConfigurationEvent newEvent = new IntegrationConnectorConfigurationEvent();

                newEvent.setEventType(GovernanceServerEventType.REFRESH_INTEGRATION_CONNECTOR_EVENT);
                newEvent.setIntegrationConnectorGUID(integrationConnectorGUID);
                newEvent.setIntegrationConnectorName(integrationConnectorName);

                outTopicServerConnector.sendEvent(newEvent);

                if (outTopicAuditLog != null)
                {
                    outTopicAuditLog.logMessage(actionDescription,
                                                GovernanceServerAuditCode.REFRESH_INTEGRATION_DAEMON.getMessageDefinition(newEvent.getEventType().getEventTypeName(),
                                                                                                                          integrationConnectorName,
                                                                                                                          integrationConnectorGUID,
                                                                                                                          OpenMetadataType.INTEGRATION_CONNECTOR.typeName));
                }
            }
            catch (Exception error)
            {
                logUnexpectedPublishingException(error, methodName);
            }
        }
    }


    /**
     * Send publish refresh event for the Governance Engine.
     *
     * @param governanceEngineGUID unique identifier for the Governance Engine
     * @param governanceEngineName unique name for the Governance Engine
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

                newEvent.setEventType(GovernanceServerEventType.REFRESH_GOVERNANCE_ENGINE_EVENT);
                newEvent.setGovernanceEngineGUID(governanceEngineGUID);
                newEvent.setGovernanceEngineName(governanceEngineName);

                outTopicServerConnector.sendEvent(newEvent);

                if (outTopicAuditLog != null)
                {
                    outTopicAuditLog.logMessage(actionDescription,
                                                GovernanceServerAuditCode.REFRESH_GOVERNANCE_ENGINE.getMessageDefinition(governanceEngineName,
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
     * @param governanceEngineGUID unique identifier for the Governance Server
     * @param governanceEngineName unique name for the Governance Server
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

                newEvent.setEventType(GovernanceServerEventType.REFRESH_GOVERNANCE_SERVICE_EVENT);
                newEvent.setGovernanceEngineGUID(governanceEngineGUID);
                newEvent.setGovernanceEngineName(governanceEngineName);
                newEvent.setRegisteredGovernanceServiceGUID(registeredGovernanceServiceGUID);
                newEvent.setRequestType(governanceRequestType);

                outTopicServerConnector.sendEvent(newEvent);

                if (outTopicAuditLog != null)
                {
                    outTopicAuditLog.logMessage(actionDescription,
                                                GovernanceServerAuditCode.REFRESH_GOVERNANCE_SERVICE.getMessageDefinition(governanceEngineName,
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
     * Publish an event to notify listeners that there is a new engine action available for processing.
     *
     * @param governanceEngineGUID unique identifier for the Governance Engine
     * @param governanceEngineName unique name for the Governance Engine
     * @param engineActionGUID element to execute
     */
    void publishNewEngineAction(String governanceEngineGUID,
                                String governanceEngineName,
                                String engineActionGUID)
    {
        final String methodName = "publishNewEngineAction";

        if (outTopicServerConnector != null)
        {
            try
            {
                EngineActionEvent newEvent = new EngineActionEvent();

                newEvent.setEventType(GovernanceServerEventType.REQUESTED_ENGINE_ACTION_EVENT);
                newEvent.setGovernanceEngineGUID(governanceEngineGUID);
                newEvent.setGovernanceEngineName(governanceEngineName);
                newEvent.setEngineActionGUID(engineActionGUID);

                outTopicServerConnector.sendEvent(newEvent);

                if (outTopicAuditLog != null)
                {
                    outTopicAuditLog.logMessage(actionDescription,
                                                GovernanceServerAuditCode.NEW_ENGINE_ACTION.getMessageDefinition(engineActionGUID,
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
     * Publish an event to notify listeners that there is an engine action has been cancelled.
     *
     * @param governanceEngineGUID unique identifier for the Governance Engine
     * @param governanceEngineName unique name for the Governance Engine
     * @param engineActionGUID element to execute
     */
    void publishCancelledEngineAction(String governanceEngineGUID,
                                      String governanceEngineName,
                                      String engineActionGUID)
    {
        final String methodName = "publishCancelledEngineAction";

        if (outTopicServerConnector != null)
        {
            try
            {
                EngineActionEvent newEvent = new EngineActionEvent();

                newEvent.setEventType(GovernanceServerEventType.CANCELLED_ENGINE_ACTION_EVENT);
                newEvent.setGovernanceEngineGUID(governanceEngineGUID);
                newEvent.setGovernanceEngineName(governanceEngineName);
                newEvent.setEngineActionGUID(engineActionGUID);

                outTopicServerConnector.sendEvent(newEvent);

                if (outTopicAuditLog != null)
                {
                    outTopicAuditLog.logMessage(actionDescription,
                                                GovernanceServerAuditCode.CANCELLED_ENGINE_ACTION.getMessageDefinition(engineActionGUID,
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
                                          GovernanceServerAuditCode.OUT_TOPIC_FAILURE.getMessageDefinition(outTopicName,
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
                outTopicAuditLog.logMessage(actionDescription, GovernanceServerAuditCode.PUBLISHING_SHUTDOWN.getMessageDefinition(outTopicName));
            }
        }
        catch (Exception error)
        {
            if (outTopicAuditLog != null)
            {
                outTopicAuditLog.logException(actionDescription,
                                              GovernanceServerAuditCode.PUBLISHING_SHUTDOWN_ERROR.getMessageDefinition(error.getClass().getName(),
                                                                                                                       outTopicName,
                                                                                                                       error.getMessage()),
                                              error);
            }
        }
    }
}
