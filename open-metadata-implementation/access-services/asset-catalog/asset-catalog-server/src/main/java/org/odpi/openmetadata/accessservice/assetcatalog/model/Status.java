/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservice.assetcatalog.model;

/**
 * The status enum is used for filtering the assets, relationships and classification
 * by the status when they are fetch from the from metadata collection.
 */
public enum Status {
    UNKNOWN,
    PROPOSED,
    DRAFT,
    PREPARED,
    ACTIVE,
    DELETED
}