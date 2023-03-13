/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.assetlineage.outtopic.connector;

import org.odpi.openmetadata.accessservices.assetlineage.event.AssetLineageEventInterface;
import org.odpi.openmetadata.accessservices.assetlineage.event.AssetLineageEventListener;
import org.odpi.openmetadata.accessservices.assetlineage.ffdc.AssetLineageErrorCode;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.OpenMetadataTopicListenerConnectorBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * AssetLineageOutTopicClientConnector is the java base class implementation of the
 * the client side connector that receives events from the Asset Lineage OMAS's OutTopic.
 */
public class AssetLineageOutTopicClientConnector extends OpenMetadataTopicListenerConnectorBase implements AssetLineageEventInterface
{
    private static final Logger log = LoggerFactory.getLogger(AssetLineageOutTopicClientConnector.class);

    private List<AssetLineageEventListener> internalEventListeners = new ArrayList<>();


    /**
     * Register a listener object that will be passed each of the events published by
     * the Asset Lineage OMAS.
     *
     * @param userId calling user
     * @param listener listener object
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     */
    @Override
    public  synchronized void registerListener(String                       userId,
                                               AssetLineageEventListener listener) throws InvalidParameterException
    {
        final String methodName = "registerListener";
        final String parameterName = "listener";

        if (listener == null)
        {
            throw new InvalidParameterException(AssetLineageErrorCode.NULL_LISTENER.getMessageDefinition(userId, methodName),
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
                for (AssetLineageEventListener listener : internalEventListeners)
                {
                    try
                    {
                        listener.processEvent(event);
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
