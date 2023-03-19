/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.stewardshipaction.connectors.outtopic;

import org.odpi.openmetadata.accessservices.stewardshipaction.api.StewardshipActionEventInterface;
import org.odpi.openmetadata.accessservices.stewardshipaction.api.StewardshipActionEventListener;
import org.odpi.openmetadata.accessservices.stewardshipaction.events.StewardshipActionOutTopicEvent;
import org.odpi.openmetadata.accessservices.stewardshipaction.ffdc.StewardshipActionErrorCode;
import org.odpi.openmetadata.frameworks.connectors.ffdc.*;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.OpenMetadataTopicListenerConnectorBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * StewardshipActionOutTopicClientConnector is the java base class implementation of the
 * the client side connector that receives events from the Stewardship Action OMAS's OutTopic.
 */
public class StewardshipActionOutTopicClientConnector extends OpenMetadataTopicListenerConnectorBase implements StewardshipActionEventInterface
{
    private static final Logger log = LoggerFactory.getLogger(StewardshipActionOutTopicClientConnector.class);

    private List<StewardshipActionEventListener> internalEventListeners = new ArrayList<>();


    /**
     * Register a listener object that will be passed each of the events published by
     * the Stewardship Action OMAS.
     *
     * @param userId calling user
     * @param listener listener object
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     */
    @Override
    public  synchronized void registerListener(String                       userId,
                                               StewardshipActionEventListener listener) throws InvalidParameterException
    {
        final String methodName = "registerListener";
        final String parameterName = "listener";

        if (listener == null)
        {
            throw new InvalidParameterException(StewardshipActionErrorCode.NULL_LISTENER.getMessageDefinition(userId, methodName),
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
    @Override
    public synchronized void processEvent(String event)
    {
        if (event != null)
        {
            try
            {
                StewardshipActionOutTopicEvent eventObject = super.getEventBean(event, StewardshipActionOutTopicEvent.class);

                for (StewardshipActionEventListener listener : internalEventListeners)
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
