/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.assetconsumer.connectors.outtopic;

import org.odpi.openmetadata.accessservices.assetconsumer.api.AssetConsumerEventInterface;
import org.odpi.openmetadata.accessservices.assetconsumer.api.AssetConsumerEventListener;
import org.odpi.openmetadata.accessservices.assetconsumer.events.AssetConsumerEvent;
import org.odpi.openmetadata.accessservices.assetconsumer.ffdc.AssetConsumerErrorCode;
import org.odpi.openmetadata.frameworks.connectors.ffdc.*;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.OpenMetadataTopicListenerConnectorBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * AssetConsumerOutTopicClientConnector is the java base class implementation of the
 * the client side connector that receives events from the Asset Consumer OMAS's OutTopic.
 */
public class AssetConsumerOutTopicClientConnector extends OpenMetadataTopicListenerConnectorBase implements AssetConsumerEventInterface
{
    private static final Logger log = LoggerFactory.getLogger(AssetConsumerOutTopicClientConnector.class);

    private List<AssetConsumerEventListener> internalEventListeners = new ArrayList<>();


    /**
     * Register a listener object that will be passed each of the events published by
     * the Asset Consumer OMAS.
     *
     * @param userId calling user
     * @param listener listener object
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     */
    @Override
    public  synchronized void registerListener(String                       userId,
                                               AssetConsumerEventListener listener) throws InvalidParameterException
    {
        final String methodName = "registerListener";
        final String parameterName = "listener";

        if (listener == null)
        {
            throw new InvalidParameterException(AssetConsumerErrorCode.NULL_LISTENER.getMessageDefinition(userId, methodName),
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
                AssetConsumerEvent eventObject = super.getEventBean(event, AssetConsumerEvent.class);

                for (AssetConsumerEventListener listener : internalEventListeners)
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
