/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.discoveryengine.outtopic;

import org.odpi.openmetadata.accessservices.discoveryengine.ffdc.DiscoveryEngineAuditCode;
import org.odpi.openmetadata.accessservices.discoveryengine.connectors.outtopic.DiscoveryEngineOutTopicServerConnector;
import org.odpi.openmetadata.accessservices.discoveryengine.events.DiscoveryEngineConfigurationEvent;
import org.odpi.openmetadata.accessservices.discoveryengine.events.DiscoveryEngineEventType;
import org.odpi.openmetadata.accessservices.discoveryengine.events.DiscoveryServiceConfigurationEvent;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;

import java.util.List;

/**
 * DiscoveryEnginePublisher is responsible for pushing events to the Discovery Engine OMAS's out topic.
 */
public class DiscoveryEnginePublisher
{
    private DiscoveryEngineOutTopicServerConnector outTopicServerConnector;
    private AuditLog                               outTopicAuditLog;
    private String                                 outTopicName;

    private final String actionDescription = "Out topic configuration refresh event publishing";

    /**
     * Constructor for the publisher.
     *
     * @param outTopicServerConnector connector to the out topic
     * @param outTopicName name of the out topic
     * @param outTopicAuditLog logging destination if anything goes wrong.
     */
    public DiscoveryEnginePublisher(DiscoveryEngineOutTopicServerConnector outTopicServerConnector,
                                    String                                 outTopicName,
                                    AuditLog                               outTopicAuditLog)
    {
        this.outTopicServerConnector = outTopicServerConnector;
        this.outTopicAuditLog        = outTopicAuditLog;
        this.outTopicName            = outTopicName;

        if (outTopicAuditLog != null)
        {
            outTopicAuditLog.logMessage(actionDescription, DiscoveryEngineAuditCode.SERVICE_PUBLISHING.getMessageDefinition(outTopicName));
        }
    }


    /**
     * Send a publish refresh event for the discovery engine.
     *
     * @param discoveryEngineGUID unique identifier for the discovery engine
     * @param discoveryEngineName unique name for the discovery engine
     */
    void publishRefreshDiscoveryEngineEvent(String discoveryEngineGUID,
                                            String discoveryEngineName)
    {
        final String methodName = "publishRefreshDiscoveryEngineEvent";

        if (outTopicServerConnector != null)
        {
            try
            {
                DiscoveryEngineConfigurationEvent newEvent = new DiscoveryEngineConfigurationEvent();

                newEvent.setEventType(DiscoveryEngineEventType.REFRESH_DISCOVERY_ENGINE_EVENT);
                newEvent.setDiscoveryEngineGUID(discoveryEngineGUID);
                newEvent.setDiscoveryEngineName(discoveryEngineName);

                outTopicServerConnector.sendEvent(newEvent);

                if (outTopicAuditLog != null)
                {
                    outTopicAuditLog.logMessage(actionDescription,
                                                DiscoveryEngineAuditCode.REFRESH_DISCOVERY_ENGINE.getMessageDefinition(discoveryEngineName,
                                                                                                                       discoveryEngineGUID));
                }
            }
            catch (Exception error)
            {
                logUnexpectedPublishingException(discoveryEngineGUID, discoveryEngineName, error, methodName);
            }
        }
    }


    /**
     * Send a publish refresh event for the registered discovery service.
     *
     * @param discoveryEngineGUID unique identifier for the discovery engine
     * @param discoveryEngineName unique name for the discovery engine
     * @param registeredDiscoveryServiceGUID unique identifier for the registered discovery service
     * @param discoveryRequestTypes list of discovery request types that trigger the instantiation of the
     *                              discovery service
     */
    void publishRefreshDiscoveryServiceEvent(String       discoveryEngineGUID,
                                             String       discoveryEngineName,
                                             String       registeredDiscoveryServiceGUID,
                                             List<String> discoveryRequestTypes)
    {
        final String methodName = "publishRefreshDiscoveryServiceEvent";

        if (outTopicServerConnector != null)
        {
            try
            {
                DiscoveryServiceConfigurationEvent newEvent = new DiscoveryServiceConfigurationEvent();

                newEvent.setEventType(DiscoveryEngineEventType.REFRESH_DISCOVERY_SERVICE_EVENT);
                newEvent.setDiscoveryEngineGUID(discoveryEngineGUID);
                newEvent.setDiscoveryEngineName(discoveryEngineName);
                newEvent.setRegisteredDiscoveryServiceGUID(registeredDiscoveryServiceGUID);
                newEvent.setDiscoveryRequestTypes(discoveryRequestTypes);

                outTopicServerConnector.sendEvent(newEvent);

                if (outTopicAuditLog != null)
                {
                    outTopicAuditLog.logMessage(actionDescription,
                                                DiscoveryEngineAuditCode.REFRESH_DISCOVERY_SERVICE.getMessageDefinition(discoveryEngineName,
                                                                                                                        discoveryEngineGUID,
                                                                                                                        discoveryRequestTypes.toString(),
                                                                                                                        registeredDiscoveryServiceGUID));
                }
            }
            catch (Exception error)
            {
                logUnexpectedPublishingException(discoveryEngineGUID, discoveryEngineName, error, methodName);
            }
        }
    }


    /**
     * Log any exceptions that have come from the publishing process.
     *
     * @param discoveryEngineGUID unique identifier for the discovery engine
     * @param discoveryEngineName unique name for the discovery engine
     * @param error this is the exception that was thrown
     * @param methodName this is the calling method
     */
    private void logUnexpectedPublishingException(String     discoveryEngineGUID,
                                                  String     discoveryEngineName,
                                                  Throwable  error,
                                                  String     methodName)
    {

        if (outTopicAuditLog != null)
        {
            outTopicAuditLog.logException(actionDescription,
                                          DiscoveryEngineAuditCode.OUT_TOPIC_FAILURE.getMessageDefinition(outTopicName,
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
                outTopicAuditLog.logMessage(actionDescription, DiscoveryEngineAuditCode.PUBLISHING_SHUTDOWN.getMessageDefinition(outTopicName));
            }
        }
        catch (Throwable error)
        {
            if (outTopicAuditLog != null)
            {
                outTopicAuditLog.logException(actionDescription,
                                              DiscoveryEngineAuditCode.PUBLISHING_SHUTDOWN_ERROR.getMessageDefinition(error.getClass().getName(),
                                                                                                                      outTopicName,
                                                                                                                      error.getMessage()),
                                              error);
            }
        }
    }
}
