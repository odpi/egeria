/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.assetlineage.event;


/**
 * AssetLineageEventListener is the interface that a client implements to
 * register to receive the events from the Asset Lineage's out topic.
 */
public abstract class AssetLineageEventListener
{
    /**
     * Process an event that was published by the Asset Lineage OMAS.
     *
     * @param event event object - call getEventType to find out what type of event.
     */
    public abstract void processEvent(String event);
}
