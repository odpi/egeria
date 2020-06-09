/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.dataplatform.connectors.outtopic;

import org.odpi.openmetadata.accessservices.dataplatform.api.DataPlatformEventInterface;
import org.odpi.openmetadata.accessservices.dataplatform.api.DataPlatformEventListener;
import org.odpi.openmetadata.accessservices.dataplatform.events.DataPlatformOutTopicEvent;
import org.odpi.openmetadata.accessservices.dataplatform.ffdc.DataPlatformErrorCode;
import org.odpi.openmetadata.frameworks.connectors.ffdc.*;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.OpenMetadataTopicListenerConnectorBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * DataPlatformOutTopicClientConnector is the java base class implementation of the
 * the client side connector that receives events from the Data Platform OMAS's OutTopic.
 */
public class DataPlatformOutTopicClientConnector extends OpenMetadataTopicListenerConnectorBase implements DataPlatformEventInterface
{
    private static final Logger log = LoggerFactory.getLogger(DataPlatformOutTopicClientConnector.class);

    private List<DataPlatformEventListener> internalEventListeners = new ArrayList<>();


    /**
     * Register a listener object that will be passed each of the events published by
     * the Data Platform OMAS.
     *
     * @param userId calling user
     * @param listener listener object
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     */
    public  synchronized void registerListener(String                       userId,
                                               DataPlatformEventListener listener) throws InvalidParameterException
    {
        final String methodName = "registerListener";
        final String parameterName = "listener";

        if (listener == null)
        {
            throw new InvalidParameterException(DataPlatformErrorCode.NULL_LISTENER.getMessageDefinition(userId, methodName),
                                                this.getClass().getName(),
                                                methodName,
                                                parameterName);
        }

        internalEventListeners.add(listener);
    }


    /**
     * Method to pass an event received on topic.
     *
     * @param event inbound event
     */
    public synchronized void processEvent(String event)
    {
        if (event != null)
        {
            try
            {
                DataPlatformOutTopicEvent eventObject = super.getEventBean(event, DataPlatformOutTopicEvent.class);

                for (DataPlatformEventListener listener : internalEventListeners)
                {
                    try
                    {
                        listener.processEvent(eventObject);
                    }
                    catch (Exception error)
                    {
                        log.error("Listener: " + listener.getClass().getName() + " is unable to process event: " + event, error);
                    }
                }
            }
            catch (Exception error)
            {
                log.error("Unable to read event: " + event, error);
            }
        }
    }
}
