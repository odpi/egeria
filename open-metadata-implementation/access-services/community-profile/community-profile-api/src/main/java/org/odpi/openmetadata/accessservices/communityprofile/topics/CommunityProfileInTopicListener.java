/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.topics;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.odpi.openmetadata.accessservices.communityprofile.events.CommunityProfileInboundEvent;
import org.odpi.openmetadata.accessservices.communityprofile.events.CommunityProfileInboundEventType;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.OpenMetadataTopicListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * CommunityProfileInTopicListener defines the interface for receiving events sent to the Community Profile
 * OMAS In Topic.
 */
public class CommunityProfileInTopicListener
{
    private static final Logger log = LoggerFactory.getLogger(CommunityProfileOutTopicPublisher.class);


    public CommunityProfileInTopicListener()
    {
    }


    /**
     * Process an event sent to the Community Profile OMAS.
     *
     * @param event event to process
     */
    public void processInTopicEvent(CommunityProfileInboundEvent event)
    {
        log.debug("Processing instance event: " + event);

        if (event == null)
        {
            log.debug("Null community profile event - ignoring event");
        }
        else
        {
            CommunityProfileInboundEventType eventType  = event.getEventType();

            if (eventType != null)
            {
                switch (eventType)
                {
                    case CREATE_PERSONAL_PROFILE_EVENT:
                        break;
                }
            }
            else
            {
                log.error("Ignored event - null type: " + event.toString());
            }
        }
    }


    /**
     * OpenMetadataInboundListener provides the bridge from the open metadata topic listener to an instance of the
     * outer class where the processor implementation is located.
     */
    class OpenMetadataInboundListener implements OpenMetadataTopicListener
    {
        private CommunityProfileInTopicListener communityProfileInTopicListener;
        private ObjectMapper                    objectMapper = new ObjectMapper();


        /**
         * The constructor takes an instance of the outer class which is where the logic is to process the event.
         *
         * @param communityProfileInTopicListener
         */
        public OpenMetadataInboundListener(CommunityProfileInTopicListener communityProfileInTopicListener)
        {
            this.communityProfileInTopicListener = communityProfileInTopicListener;
        }


        /**
         * Method to pass an event received on topic.
         *
         * @param eventPayload inbound event
         */
        public void processEvent(String eventPayload)
        {
            try
            {
                CommunityProfileInboundEvent event = objectMapper.readValue(eventPayload, CommunityProfileInboundEvent.class);
            }
            catch (Exception  exc)
            {

            }
        }
    }
}
