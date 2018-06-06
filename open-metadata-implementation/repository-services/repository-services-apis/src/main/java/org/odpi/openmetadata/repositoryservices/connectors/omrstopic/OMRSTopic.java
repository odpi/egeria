/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.repositoryservices.connectors.omrstopic;

import org.odpi.openmetadata.repositoryservices.events.v1.OMRSEventV1;

/**
 * OMRSTopic defines the interface to the messaging Topic for OMRS Events.  It implemented by the OMRSTopicConnector.
 */
public interface OMRSTopic
{
    /**
     * Register a listener object.  This object will be supplied with all of the events
     * received on the topic.
     *
     * @param newListener - object implementing the OMRSTopicListener interface
     */
    void registerListener(OMRSTopicListener  newListener);


    /**
     * Sends the supplied event to the topic.
     *
     * @param event - OMRSEvent object containing the event properties.
     */
    void sendEvent(OMRSEventV1 event);
}
