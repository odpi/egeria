/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.eventmapper.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The payload contents of all Metadata Asset Manager-specific events on the InfosphereEvents topic.
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class InfosphereEventsIMAMEvent extends InfosphereEvents {

    /**
     * The 'createdRIDs' property lists (comma-separated) the RIDs of any objects that were created.
     * Each comma-separated entry takes the form {@code <type>:RID}.
     */
    protected String createdRIDs;

    /**
     * The 'mergedRIDs' property lists (comma-separated) the RIDs of any objects that were merged.
     * Each comma-separated entry takes the form {@code <type>:RID}.
     */
    protected String mergedRIDs;

    /**
     * The 'deletedRIDs' property lists (comma-separated) the RIDs of any objects that were deleted.
     * Each comma-separated entry takes the form {@code <type>:RID}
     */
    protected String deletedRIDs;

    /** @see #createdRIDs */ @JsonProperty("createdRIDs") public String getCreatedRIDs() { return this.createdRIDs; }
    /** @see #createdRIDs */ @JsonProperty("createdRIDs") public void setCreatedRIDs(String createdRIDs) { this.createdRIDs = createdRIDs; }

    /** @see #mergedRIDs */ @JsonProperty("mergedRIDs") public String getMergedRIDs() { return this.mergedRIDs; }
    /** @see #mergedRIDs */ @JsonProperty("mergedRIDs") public void setMergedRIDs(String mergedRIDs) { this.mergedRIDs = mergedRIDs; }

    /** @see #deletedRIDs */ @JsonProperty("deletedRIDs") public String getDeletedRIDs() { return this.deletedRIDs; }
    /** @see #deletedRIDs */ @JsonProperty("deletedRIDs") public void setDeletedRIDs(String deletedRIDs) { this.deletedRIDs = deletedRIDs; }

}
