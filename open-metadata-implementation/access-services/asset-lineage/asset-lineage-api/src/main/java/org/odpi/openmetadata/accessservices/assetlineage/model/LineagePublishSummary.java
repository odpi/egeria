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
 * LineagePublishSummary is used to describe summary as result of AssetLineage lineagePublish activity that completed.
 *
 */
public class LineagePublishSummary {
    /**
     * The list GUIDs for the items published as result of lineagePublish activity.
     * -- GETTER --
     * Get items published.
     * @return the list of GUIDs representing items published.
     * -- SETTER --
     * Set items published.
     * @param items list with GUIDs representing items published.
     */
    private List<String> items;
    /**
     * The point in time used to perform lineagePublish activity.
     * -- GETTER --
     * Gets the time when lineagePublish activity was performed.
     * @return Long representing epoch time milliseconds.
     * -- SETTER --
     * Sets the time when lineagePublish activity was performed.
     * @param  lineageTimestamp representing epoch time milliseconds
     */
    private Long lineageTimestamp;
}
