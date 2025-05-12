/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.governanceserver.connectors.outtopic;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.odpi.openmetadata.accessservices.governanceserver.ffdc.GovernanceServerAuditCode;
import org.odpi.openmetadata.accessservices.governanceserver.events.GovernanceServerEvent;
import org.odpi.openmetadata.accessservices.governanceserver.ffdc.GovernanceServerErrorCode;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.OpenMetadataTopicSenderConnectorBase;

import java.util.concurrent.CompletionException;


/**
 * GovernanceEngineOutTopicServerConnector is the java implementation of
 * the server-side connector that send events to the Governance Server OMAS's OutTopic.
 */
public class GovernanceServerOutTopicServerConnector extends OpenMetadataTopicSenderConnectorBase
{

    private static final ObjectWriter OBJECT_WRITER = new ObjectMapper().writer();

    /**
     * Send the request to the embedded event bus connector(s).
     *
     * @param event event object
     * @throws InvalidParameterException the event is null
     * @throws ConnectorCheckedException there is a problem with the embedded event bus connector(s).
     */
    public void sendEvent(GovernanceServerEvent event) throws InvalidParameterException, ConnectorCheckedException
    {
        final String methodName = "sendEvent";

        try
        {
            String eventString = OBJECT_WRITER.writeValueAsString(event);
            super.sendEvent(eventString).join();

            if (super.auditLog != null)
            {
                super.auditLog.logMessage(methodName,
                                          GovernanceServerAuditCode.OUT_TOPIC_EVENT.getMessageDefinition(event.getEventType().getEventTypeName()),
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
            throw new ConnectorCheckedException(GovernanceServerErrorCode.UNABLE_TO_SEND_EVENT.getMessageDefinition(connectionName,
                                                                                                                    event.toString(),
                                                                                                                    error.getClass().getName(),
                                                                                                                    error.getMessage()),
                                                this.getClass().getName(),
                                                methodName,
                                                error);
        }
    }
}
