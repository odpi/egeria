/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.datamanager.connectors.outtopic;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.odpi.openmetadata.accessservices.datamanager.ffdc.DataManagerAuditCode;
import org.odpi.openmetadata.accessservices.datamanager.events.DataManagerOutTopicEvent;
import org.odpi.openmetadata.accessservices.datamanager.ffdc.DataManagerErrorCode;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.OpenMetadataTopicSenderConnectorBase;


/**
 * DataManagerOutTopicServerConnector is the java implementation of the
 * the server side connector that send events to the Data Manager OMAS's OutTopic.
 */
public class DataManagerOutTopicServerConnector extends OpenMetadataTopicSenderConnectorBase
{
    /**
     * Send the request to the embedded event bus connector(s).
     *
     * @param event event object
     * @throws InvalidParameterException the event is null
     * @throws ConnectorCheckedException there is a problem with the embedded event bus connector(s)./
     */
    public void sendEvent(DataManagerOutTopicEvent event) throws InvalidParameterException, ConnectorCheckedException
    {
        final String methodName = "sendEvent";
        ObjectMapper objectMapper = new ObjectMapper();

        try
        {
            String eventString = objectMapper.writeValueAsString(event);
            super.sendEvent(eventString);

            if (super.auditLog != null)
            {
                super.auditLog.logMessage(methodName, DataManagerAuditCode.OUT_TOPIC_EVENT.getMessageDefinition(eventString));
            }
        }
        catch (InvalidParameterException | ConnectorCheckedException error)
        {
            throw error;
        }
        catch (Exception  error)
        {
            throw new ConnectorCheckedException(DataManagerErrorCode.UNABLE_TO_SEND_EVENT.getMessageDefinition(connectionName,
                                                                                                                   event.toString(),
                                                                                                                   error.getClass().getName(),
                                                                                                                   error.getMessage()),
                                                this.getClass().getName(),
                                                methodName,
                                                error);
        }
    }
}
