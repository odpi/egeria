/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.discoveryengine.connectors.outtopic;

import org.odpi.openmetadata.accessservices.discoveryengine.api.DiscoveryEngineEventInterface;
import org.odpi.openmetadata.accessservices.discoveryengine.api.DiscoveryEngineEventListener;
import org.odpi.openmetadata.accessservices.discoveryengine.events.DiscoveryEngineEvent;
import org.odpi.openmetadata.accessservices.discoveryengine.ffdc.DiscoveryEngineErrorCode;
import org.odpi.openmetadata.frameworks.connectors.ffdc.*;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.OpenMetadataTopicListenerConnectorBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * DiscoveryEngineOutTopicClientConnector is the java base class implementation of the
 * the client side connector that receives events from the Discovery Engine OMAS's OutTopic.
 */
public class DiscoveryEngineOutTopicClientConnector extends OpenMetadataTopicListenerConnectorBase implements DiscoveryEngineEventInterface
{
    private static final Logger log = LoggerFactory.getLogger(DiscoveryEngineOutTopicClientConnector.class);

    private List<DiscoveryEngineEventListener> internalEventListeners = new ArrayList<>();


    /**
     * Register a listener object that will be passed each of the events published by
     * the Discovery Engine OMAS.
     *
     * @param userId calling user
     * @param listener listener object
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     */
    public  synchronized void registerListener(String                       userId,
                                               DiscoveryEngineEventListener listener) throws InvalidParameterException
    {
        final String methodName = "registerListener";
        final String parameterName = "listener";

        if (listener == null)
        {
            throw new InvalidParameterException(DiscoveryEngineErrorCode.NULL_LISTENER.getMessageDefinition(userId, methodName),
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
                DiscoveryEngineEvent eventObject = super.getEventBean(event, DiscoveryEngineEvent.class);

                for (DiscoveryEngineEventListener listener : internalEventListeners)
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
