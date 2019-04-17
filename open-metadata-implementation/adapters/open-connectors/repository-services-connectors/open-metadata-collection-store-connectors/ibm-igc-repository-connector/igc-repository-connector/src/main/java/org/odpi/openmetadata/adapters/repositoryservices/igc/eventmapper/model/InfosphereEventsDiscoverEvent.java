/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.eventmapper.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The payload contents of all Discovery-specific events on the InfosphereEvents topic.
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class InfosphereEventsDiscoverEvent extends InfosphereEvents {

    /**
     * The 'discoverOperationId' property gives the ID of the discovery operation that triggered this event (if any).
     */
    protected String discoverOperationId;

    /**
     * The 'timestampAsLong' property gives the timestamp of the discovery operation.
     */
    protected String timestampAsLong;

    /** @see #discoverOperationId */ @JsonProperty("discoverOperationId") public String getDiscoverOperationId() { return this.discoverOperationId; }
    /** @see #discoverOperationId */ @JsonProperty("discoverOperationId") public void setDiscoverOperationId(String discoverOperationId) { this.discoverOperationId = discoverOperationId; }

    /** @see #timestampAsLong */ @JsonProperty("timestampAsLong") public String getTimestampAsLong() { return this.timestampAsLong; }
    /** @see #timestampAsLong */ @JsonProperty("timestampAsLong") public void setTimestampAsLong(String timestampAsLong) { this.timestampAsLong = timestampAsLong; }

}
