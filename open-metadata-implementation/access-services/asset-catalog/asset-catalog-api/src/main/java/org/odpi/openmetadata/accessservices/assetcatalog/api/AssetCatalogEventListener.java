/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.assetcatalog.api;


import org.odpi.openmetadata.accessservices.assetcatalog.model.AssetCatalogEvent;

/**
 * AssetCatalogEventListener is the interface that a client implements to
 * process the received events from the Asset Catalog OMAS's out topic.
 */
public interface AssetCatalogEventListener {
    /**
     * Process an event that was published by the Asset Catalog OMAS.
     *
     * @param event event object - call getEventType to find out what type of event.
     */
    void processEvent(AssetCatalogEvent event);


}
