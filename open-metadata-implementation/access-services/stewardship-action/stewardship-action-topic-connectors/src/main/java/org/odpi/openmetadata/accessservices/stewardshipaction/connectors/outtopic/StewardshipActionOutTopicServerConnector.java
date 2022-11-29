/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.stewardshipaction.connectors.outtopic;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.odpi.openmetadata.accessservices.stewardshipaction.events.StewardshipActionOutTopicEvent;
import org.odpi.openmetadata.accessservices.stewardshipaction.ffdc.StewardshipActionAuditCode;
import org.odpi.openmetadata.accessservices.stewardshipaction.ffdc.StewardshipActionErrorCode;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.OpenMetadataTopicSenderConnectorBase;

import java.util.concurrent.CompletionException;


/**
 * StewardshipActionOutTopicServerConnector is the java implementation of the
 * the server side connector that send events to the Stewardship Action OMAS's OutTopic.
 */
public class StewardshipActionOutTopicServerConnector extends OpenMetadataTopicSenderConnectorBase
{
    /**
     * Send the request to the embedded event bus connector(s).
     *
     * @param event event object
     * @throws InvalidParameterException the event is null
     * @throws ConnectorCheckedException there is a problem with the embedded event bus connector(s).
     */
    public void sendEvent(StewardshipActionOutTopicEvent event) throws InvalidParameterException, ConnectorCheckedException
    {
        final String methodName = "sendEvent";
        ObjectMapper objectMapper = new ObjectMapper();

        try
        {
            String eventString = objectMapper.writeValueAsString(event);
            super.sendEvent(eventString).join();

            if (super.auditLog != null)
            {
                super.auditLog.logMessage(methodName,
                                          StewardshipActionAuditCode.OUT_TOPIC_EVENT.getMessageDefinition(event.getEventType().getEventTypeName()),
                                          eventString);
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
            throw new ConnectorCheckedException(StewardshipActionErrorCode.UNABLE_TO_SEND_EVENT.getMessageDefinition(connectionName,
                                                                                                                 event.toString(),
                                                                                                                 error.getClass().getName(),
                                                                                                                 error.getMessage()),
                                                this.getClass().getName(),
                                                methodName,
                                                error);
        }
    }
}
