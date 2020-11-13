/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic;

import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.ffdc.OMRSErrorCode;

/**
 * OpenMetadataTopicConnectorBase is a base class to topic connectors that only send
 * events on the embedded event bus connector
 */
public class OpenMetadataTopicSenderConnectorBase extends OpenMetadataTopicConsumerBase
{
    /**
     * Send the request to the embedded event bus connector(s).
     *
     * @param event event as a string
     * @throws InvalidParameterException the event is null
     * @throws ConnectorCheckedException there is a problem with the embedded event bus connector(s)./
     */
    protected void sendEvent(String event) throws InvalidParameterException,
                                                  ConnectorCheckedException
    {
        final String methodName = "sendEvent";

        validateEventBusConnectors(methodName);

        if (event == null)
        {
            final String parameterName = "event";

            throw new InvalidParameterException(OMRSErrorCode.NULL_OUTBOUND_EVENT.getMessageDefinition(connectionName),
                                                this.getClass().getName(),
                                                methodName,
                                                parameterName);
        }

        /*
         * Each of the event bus connectors need to be passed the new event.
         */
        for (OpenMetadataTopicConnector eventBusConnector : eventBusConnectors)
        {
            eventBusConnector.sendEvent(event);
        }
    }
}
