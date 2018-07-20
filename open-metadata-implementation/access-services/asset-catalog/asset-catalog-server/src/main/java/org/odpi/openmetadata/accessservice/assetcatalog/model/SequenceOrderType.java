/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservice.assetcatalog.model;

/**
 * The SequenceOrderType enum defining how the results should be ordered when they are returned from metadata collection.
 */
public enum SequenceOrderType {
    ANY,
    GUID,
    CREATION_DATE_RECENT,
    CREATION_DATE_OLDEST,
    LAST_UPDATE_RECENT,
    LAST_UPDATE_OLDEST,
    PROPERTY_ASCENDING,
    PROPERTY_DESCENDING
}