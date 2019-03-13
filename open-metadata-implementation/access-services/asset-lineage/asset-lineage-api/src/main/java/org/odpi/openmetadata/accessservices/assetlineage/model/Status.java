/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.assetlineage.model;

/**
 * The status enum is used for filtering the assets, relationships and classification
 * by the status when they are fetch from the from metadata collection.
 */
public enum Status {
    /**
     * Unknown status of the asset
     */
    UNKNOWN,

    /**
     * Assets with status in review
     */
    PROPOSED,

    /**
     * Assets with incomplete details
     */
    DRAFT,

    /**
     * Assets ready for review
     */
    PREPARED,

    /**
     * Assets in active state
     */
    ACTIVE,

    /**
     * Assets that have been deleted or are no longer available
     */
    DELETED
}