/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.eventmapper.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown=true)
public class InfosphereEventsDCEvent extends InfosphereEvents {

    @JsonIgnore public static final String ACTION_CREATE = "DC_CREATE_EVENT";
    @JsonIgnore public static final String ACTION_MODIFY = "DC_MERGED_EVENT";

    /**
     * The 'createdRID' property lists the RID of the data connection object that was created.
     */
    protected String createdRID;

    /**
     * The 'mergedRIDs' property lists (comma-separated) the RIDs of any objects that were merged.
     * Each comma-separated entry takes the form '<type>:RID'.
     */
    protected String mergedRID;

    /** @see #createdRID */ @JsonProperty("createdRID") public String getCreatedRID() { return this.createdRID; }
    /** @see #createdRID */ @JsonProperty("createdRID") public void setCreatedRID(String createdRID) { this.createdRID = createdRID; }

    /** @see #mergedRID */ @JsonProperty("mergedRID") public String getMergedRID() { return this.mergedRID; }
    /** @see #mergedRID */ @JsonProperty("mergedRID") public void setMergedRID(String mergedRID) { this.mergedRID = mergedRID; }

}
