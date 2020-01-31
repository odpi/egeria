/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.discoveryengine.connectors.outtopic;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.odpi.openmetadata.accessservices.discoveryengine.auditlog.DiscoveryEngineAuditCode;
import org.odpi.openmetadata.accessservices.discoveryengine.events.DiscoveryEngineEvent;
import org.odpi.openmetadata.accessservices.discoveryengine.ffdc.DiscoveryEngineErrorCode;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.OpenMetadataTopicSenderConnectorBase;


/**
 * DiscoveryEngineOutTopicServerConnector is the java implementation of the
 * the server side connector that send events to the Discovery Engine OMAS's OutTopic.
 */
public class DiscoveryEngineOutTopicServerConnector extends OpenMetadataTopicSenderConnectorBase
{
    /**
     * Send the request to the embedded event bus connector(s).
     *
     * @param event event object
     * @throws InvalidParameterException the event is null
     * @throws ConnectorCheckedException there is a problem with the embedded event bus connector(s)./
     */
    public void sendEvent(DiscoveryEngineEvent event) throws InvalidParameterException, ConnectorCheckedException
    {
        final String methodName = "sendEvent";
        ObjectMapper objectMapper = new ObjectMapper();

        try
        {
            String eventString = objectMapper.writeValueAsString(event);
            super.sendEvent(eventString);

            if (super.auditLog != null)
            {
                DiscoveryEngineAuditCode auditCode = DiscoveryEngineAuditCode.OUT_TOPIC_EVENT;

                super.auditLog.logRecord(methodName,
                                         auditCode.getLogMessageId(),
                                         auditCode.getSeverity(),
                                         auditCode.getFormattedLogMessage(eventString),
                                         null,
                                         auditCode.getSystemAction(),
                                         auditCode.getUserAction());
            }
        }
        catch (InvalidParameterException | ConnectorCheckedException error)
        {
            throw error;
        }
        catch (Throwable  error)
        {
            DiscoveryEngineErrorCode errorCode    = DiscoveryEngineErrorCode.UNABLE_TO_SEND_EVENT;
            String                   errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(connectionName,
                                                                                                                       event.toString(),
                                                                                                                       error.getClass().getName(),
                                                                                                                       error.getMessage());

            throw new ConnectorCheckedException(errorCode.getHTTPErrorCode(),
                                                this.getClass().getName(),
                                                methodName,
                                                errorMessage,
                                                errorCode.getSystemAction(),
                                                errorCode.getUserAction(),
                                                error);
        }
    }
}
