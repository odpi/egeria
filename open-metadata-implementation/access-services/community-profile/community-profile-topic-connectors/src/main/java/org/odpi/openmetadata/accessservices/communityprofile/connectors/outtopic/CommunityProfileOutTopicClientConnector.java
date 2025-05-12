/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.communityprofile.connectors.outtopic;

import org.odpi.openmetadata.accessservices.communityprofile.api.CommunityProfileEventInterface;
import org.odpi.openmetadata.accessservices.communityprofile.api.CommunityProfileEventListener;
import org.odpi.openmetadata.accessservices.communityprofile.events.CommunityProfileOutboundEvent;
import org.odpi.openmetadata.accessservices.communityprofile.ffdc.CommunityProfileErrorCode;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.OpenMetadataTopicListenerConnectorBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * CommunityProfileOutTopicClientConnector is the java base class implementation of the
 * the client side connector that receives events from the Community Profile OMAS's OutTopic.
 */
public class CommunityProfileOutTopicClientConnector extends OpenMetadataTopicListenerConnectorBase implements CommunityProfileEventInterface
{
    private static final Logger log = LoggerFactory.getLogger(CommunityProfileOutTopicClientConnector.class);

    private final List<CommunityProfileEventListener> internalEventListeners = new ArrayList<>();


    /**
     * Register a listener object that will be passed each of the events published by
     * the Community Profile OMAS.
     *
     * @param userId calling user
     * @param listener listener object
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     */
    @Override
    public  synchronized void registerListener(String                       userId,
                                               CommunityProfileEventListener listener) throws InvalidParameterException
    {
        final String methodName = "registerListener";
        final String parameterName = "listener";

        if (listener == null)
        {
            throw new InvalidParameterException(CommunityProfileErrorCode.NULL_LISTENER.getMessageDefinition(userId, methodName),
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
                CommunityProfileOutboundEvent eventObject = super.getEventBean(event, CommunityProfileOutboundEvent.class);

                for (CommunityProfileEventListener listener : internalEventListeners)
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
