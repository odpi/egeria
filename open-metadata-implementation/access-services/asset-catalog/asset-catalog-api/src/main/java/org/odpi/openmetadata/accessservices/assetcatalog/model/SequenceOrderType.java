/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.assetcatalog.model;

/**
 * The SequenceOrderType enum defining how the results should be ordered when they are returned from metadata collection.
 */
public enum SequenceOrderType {
    /**
     * "Any order.
     */
    ANY,

    /**
     * Order by GUID
     */
    GUID,

    /**
     * Most recently created first
     */
    CREATION_DATE_RECENT,

    /**
     * Most recently created last
     */
    CREATION_DATE_OLDEST,
    
    /**
     * Most recently updated first
     */
    LAST_UPDATE_RECENT,

    /**
     * Most recently updated last
     */
    LAST_UPDATE_OLDEST,

    /**
     * Order by property value, lowest value first
     */
    PROPERTY_ASCENDING,

    /**
     * Order by property value, lowest value last
     */
    PROPERTY_DESCENDING
}