/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetlineage.model;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@ToString
/**
 *
 * LineagePublishSummary describes details about lineage processing and publish activity completed by Asset Lineage OMAS.
 *
 */
public class LineagePublishSummary {
    /**
     * The list GUIDs for the items published after lineage processing activity completed.
     * -- GETTER --
     * Get items published.
     * @return the collection of GUIDs representing items published.
     * -- SETTER --
     * Set items published.
     * @param items collection of GUIDs for the items published.
     */
    private List<String> items;
    /**
     * The point in time (cutoff time) when the entities used for lineage processing were retrieved from the cohort.
     * -- GETTER --
     * Gets the timestamp.
     * @return Long representing epoch time milliseconds.
     * -- SETTER --
     * Sets the timestamp.
     * @param  lineageTimestamp representing epoch time milliseconds.
     */
    private Long lineageTimestamp;
}
