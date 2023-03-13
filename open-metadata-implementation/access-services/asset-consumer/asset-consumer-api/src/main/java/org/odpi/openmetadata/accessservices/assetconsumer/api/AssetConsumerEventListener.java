/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.assetconsumer.api;

import org.odpi.openmetadata.accessservices.assetconsumer.events.AssetConsumerOutTopicEvent;

/**
 * AssetConsumerEventListener is the interface that a client implements to
 * register to receive the events from the Asset Consumer OMAS.
 */
public abstract class AssetConsumerEventListener
{
    /**
     * Process an event that was published by the Asset Consumer OMAS.
     *
     * @param event event object - call getEventType to find out what type of event.
     */
    public abstract void processEvent(AssetConsumerOutTopicEvent event);
}
