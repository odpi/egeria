/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.assetowner.api;

import org.odpi.openmetadata.accessservices.assetowner.events.AssetOwnerOutTopicEvent;

/**
 * AssetOwnerEventListener is the interface that a client implements to
 * register to receive the events from the Asset Owner OMAS.
 */
public abstract class AssetOwnerEventListener
{
    /**
     * Process an event that was published by the Asset Owner OMAS.
     *
     * @param event event object - call getEventType to find out what type of event.
     */
    public abstract void processEvent(AssetOwnerOutTopicEvent event);
}
