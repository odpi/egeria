/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.assetmanager.api;


import org.odpi.openmetadata.accessservices.assetmanager.events.AssetManagerOutTopicEvent;

/**
 * AssetManagerEventListener is the interface that a client implements to
 * register to receive the events from the Asset Manager OMAS's out topic.
 */
public interface AssetManagerEventListener
{
    /**
     * Process an event that was published by the Asset Manager OMAS.
     *
     * @param event event object - call getEventType to find out what type of event.
     */
    void processEvent(AssetManagerOutTopicEvent event);
}
