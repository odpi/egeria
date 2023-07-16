/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.governanceengine.connectors.outtopic;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.odpi.openmetadata.accessservices.governanceengine.events.WatchdogGovernanceServiceEvent;
import org.odpi.openmetadata.accessservices.governanceengine.ffdc.GovernanceEngineAuditCode;
import org.odpi.openmetadata.accessservices.governanceengine.events.GovernanceEngineEvent;
import org.odpi.openmetadata.accessservices.governanceengine.ffdc.GovernanceEngineErrorCode;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.governanceaction.events.WatchdogMetadataElementEvent;
import org.odpi.openmetadata.frameworks.governanceaction.events.WatchdogRelatedElementsEvent;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.OpenMetadataTopicSenderConnectorBase;

import java.util.concurrent.CompletionException;


/**
 * GovernanceEngineOutTopicServerConnector is the java implementation of
 * the server-side connector that send events to the Governance Engine OMAS's OutTopic.
 */
public class GovernanceEngineOutTopicServerConnector extends OpenMetadataTopicSenderConnectorBase
{

    private static final ObjectWriter OBJECT_WRITER = new ObjectMapper().writer();

    /**
     * Send the request to the embedded event bus connector(s).
     *
     * @param event event object
     * @throws InvalidParameterException the event is null
     * @throws ConnectorCheckedException there is a problem with the embedded event bus connector(s).
     */
    public void sendEvent(GovernanceEngineEvent event) throws InvalidParameterException, ConnectorCheckedException
    {
        final String methodName = "sendEvent";

        try
        {
            String eventString = OBJECT_WRITER.writeValueAsString(event);
            super.sendEvent(eventString).join();

            if (super.auditLog != null)
            {
                if (event instanceof WatchdogGovernanceServiceEvent watchdogGovernanceServiceEvent)
                {
                    if (watchdogGovernanceServiceEvent.getWatchdogGovernanceEvent() instanceof WatchdogMetadataElementEvent watchdogMetadataElementEvent)
                    {
                        super.auditLog.logMessage(methodName,
                                                  GovernanceEngineAuditCode.OUT_TOPIC_WATCHDOG_EVENT.getMessageDefinition(
                                                          event.getEventType().getEventTypeName(),
                                                          watchdogMetadataElementEvent.getMetadataElement().getElementGUID(),
                                                          watchdogMetadataElementEvent.getMetadataElement().getType().getTypeName()),
                                                  eventString);
                    }
                    else if (watchdogGovernanceServiceEvent.getWatchdogGovernanceEvent() instanceof WatchdogRelatedElementsEvent watchdogRelatedElementsEvent)
                    {
                        super.auditLog.logMessage(methodName,
                                                  GovernanceEngineAuditCode.OUT_TOPIC_WATCHDOG_EVENT.getMessageDefinition(
                                                          event.getEventType().getEventTypeName(),
                                                          watchdogRelatedElementsEvent.getRelatedMetadataElements().getRelationshipGUID(),
                                                          watchdogRelatedElementsEvent.getRelatedMetadataElements().getType().getTypeName()),
                                                  eventString);
                    }
                    else
                    {
                        super.auditLog.logMessage(methodName,
                                                  GovernanceEngineAuditCode.OUT_TOPIC_EVENT.getMessageDefinition(event.getEventType().getEventTypeName()),
                                                  eventString);
                    }
                }
                else
                {
                    super.auditLog.logMessage(methodName,
                                              GovernanceEngineAuditCode.OUT_TOPIC_EVENT.getMessageDefinition(event.getEventType().getEventTypeName()),
                                              eventString);
                }
            }
        }
        catch (CompletionException error)
        {
            if (error.getCause() instanceof ConnectorCheckedException)
            {
                throw (ConnectorCheckedException) error.getCause();
            }
            else if (error.getCause() instanceof InvalidParameterException)
            {
                throw (InvalidParameterException) error.getCause();
            }
        }
        catch (InvalidParameterException | ConnectorCheckedException error)
        {
            throw error;
        }
        catch (Exception  error)
        {
            throw new ConnectorCheckedException(GovernanceEngineErrorCode.UNABLE_TO_SEND_EVENT.getMessageDefinition(connectionName,
                                                                                                                   event.toString(),
                                                                                                                   error.getClass().getName(),
                                                                                                                   error.getMessage()),
                                                this.getClass().getName(),
                                                methodName,
                                                error);
        }
    }
}
