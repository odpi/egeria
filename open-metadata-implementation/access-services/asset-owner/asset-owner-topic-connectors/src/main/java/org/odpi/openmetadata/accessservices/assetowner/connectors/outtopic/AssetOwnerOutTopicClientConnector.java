/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.assetowner.connectors.outtopic;

import org.odpi.openmetadata.accessservices.assetowner.api.AssetOwnerEventInterface;
import org.odpi.openmetadata.accessservices.assetowner.api.AssetOwnerEventListener;
import org.odpi.openmetadata.accessservices.assetowner.events.AssetOwnerOutTopicEvent;
import org.odpi.openmetadata.accessservices.assetowner.ffdc.AssetOwnerErrorCode;
import org.odpi.openmetadata.frameworks.connectors.ffdc.*;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.OpenMetadataTopicListenerConnectorBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * AssetOwnerOutTopicClientConnector is the java base class implementation of the
 * the client side connector that receives events from the Asset Owner OMAS's OutTopic.
 */
public class AssetOwnerOutTopicClientConnector extends OpenMetadataTopicListenerConnectorBase implements AssetOwnerEventInterface
{
    private static final Logger log = LoggerFactory.getLogger(AssetOwnerOutTopicClientConnector.class);

    private List<AssetOwnerEventListener> internalEventListeners = new ArrayList<>();


    /**
     * Register a listener object that will be passed each of the events published by
     * the Asset Owner OMAS.
     *
     * @param userId calling user
     * @param listener listener object
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     */
    @Override
    public  synchronized void registerListener(String                       userId,
                                               AssetOwnerEventListener listener) throws InvalidParameterException
    {
        final String methodName = "registerListener";
        final String parameterName = "listener";

        if (listener == null)
        {
            throw new InvalidParameterException(AssetOwnerErrorCode.NULL_LISTENER.getMessageDefinition(userId, methodName),
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
                AssetOwnerOutTopicEvent eventObject = super.getEventBean(event, AssetOwnerOutTopicEvent.class);

                for (AssetOwnerEventListener listener : internalEventListeners)
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
