/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworkservices.gaf.connectors.outtopic;

import org.odpi.openmetadata.frameworks.openmetadata.events.OpenMetadataEventInterface;
import org.odpi.openmetadata.frameworks.openmetadata.events.OpenMetadataEventListener;
import org.odpi.openmetadata.frameworks.openmetadata.events.OpenMetadataOutTopicEvent;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworkservices.gaf.ffdc.OpenGovernanceErrorCode;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.OpenMetadataTopicListenerConnectorBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * GAFOutTopicClientConnector is the java base class implementation of
 * the client side connector that receives events from the Open Metadata Store's OutTopic.
 */
public class GAFOutTopicClientConnector extends OpenMetadataTopicListenerConnectorBase implements OpenMetadataEventInterface
{
    private static final Logger log = LoggerFactory.getLogger(GAFOutTopicClientConnector.class);

    /*
     * The listeners are held in a copy-on-write list so that events can be delivered without holding this
     * connector's lock - see processEvent.
     */
    private final List<OpenMetadataEventListener> internalEventListeners = new CopyOnWriteArrayList<>();


    /**
     * Register a listener object that will be passed each of the events published by
     * the Open Metadata Store.
     *
     * @param userId calling user
     * @param listener listener object
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     */
    @Override
    public  synchronized void registerListener(String                       userId,
                                               OpenMetadataEventListener listener) throws InvalidParameterException
    {
        final String methodName = "registerListener";
        final String parameterName = "listener";

        if (listener == null)
        {
            throw new InvalidParameterException(OpenGovernanceErrorCode.NULL_LISTENER.getMessageDefinition(userId, methodName),
                                                this.getClass().getName(),
                                                methodName,
                                                parameterName);
        }

        internalEventListeners.add(listener);
    }


    /**
     * Method to pass an event received on the topic.
     *
     * @param event inbound event
     */
    /**
     * Pass an event received from the topic to each registered listener.
     * <br><br>
     * This method is deliberately not synchronized.  A listener may call into its server, and if that code holds
     * a lock of its own while disconnecting this connector - which needs this connector's lock - the two threads
     * deadlock.  That is what happened when an engine host shut down while an engine action event was being
     * delivered.  The events from a topic arrive on a single listener thread, so they are still delivered one at
     * a time and in order.
     *
     * @param event inbound event
     */
    @Override
    public void processEvent(String event)
    {
        if (event != null)
        {
            try
            {
                OpenMetadataOutTopicEvent eventObject = super.getEventBean(event, OpenMetadataOutTopicEvent.class);

                for (OpenMetadataEventListener listener : internalEventListeners)
                {
                    try
                    {
                        listener.processEvent(eventObject);
                    }
                    catch (Exception error)
                    {
                        log.error("Listener: " + listener.getClass().getName() + " cannot process event: " + event, error);
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
