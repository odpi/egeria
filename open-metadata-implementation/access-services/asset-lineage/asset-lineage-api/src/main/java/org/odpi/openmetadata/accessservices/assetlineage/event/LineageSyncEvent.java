/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetlineage.event;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.odpi.openmetadata.accessservices.assetlineage.model.LineagePublishSummary;
import org.odpi.openmetadata.accessservices.assetlineage.model.LineageSyncUpdateContext;


import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * The lineage event contains information used for internal processing of data
 *
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString
/**
 *
 * LineageSyncEvent is used to notify external consumers about AssetLineage internal processing.
 * It can contain details such as publishSummary and/or updateSummary created by corresponding internal processing phases of AssetLineage.
 *
 */
public class LineageSyncEvent extends AssetLineageEventHeader {

    /**
     * LineagePublishSummary is used to describe summary as result of AssetLineage lineagePublish activity that completed.
     */
    private LineagePublishSummary publishSummary;
    private LineageSyncUpdateContext syncUpdateContext;
}
