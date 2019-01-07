/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.eventmapper.model;

import com.fasterxml.jackson.annotation.*;

/**
 * The base class of all InfosphereEvents topic messages from IBM Information Server, including
 * Information Governance Catalog, Metadata Asset Manager and Information Analyzer.
 */
@JsonTypeInfo(use=JsonTypeInfo.Id.NAME, include=JsonTypeInfo.As.PROPERTY, property="eventType", visible=true, defaultImpl=InfosphereEventsAssetEvent.class)
@JsonSubTypes({
        @JsonSubTypes.Type(value = InfosphereEventsIMAMEvent.class, name = "IMAM_SHARE_EVENT"),
        @JsonSubTypes.Type(value = InfosphereEventsDCEvent.class, name = "DC_CREATE_EVENT"),
        @JsonSubTypes.Type(value = InfosphereEventsDCEvent.class, name = "DC_MERGED_EVENT"),
        @JsonSubTypes.Type(value = InfosphereEventsIAEvent.class, name = "IA_PROJECT_CREATED_EVENT"),
        @JsonSubTypes.Type(value = InfosphereEventsIAEvent.class, name = "IA_TABLE_ADDED_TO_PROJECT"),
        @JsonSubTypes.Type(value = InfosphereEventsIAEvent.class, name = "IA_TABLE_REMOVED_FROM_PROJECT"),
        @JsonSubTypes.Type(value = InfosphereEventsIAEvent.class, name = "IA_DATARULE_CREATED_EVENT"),
        @JsonSubTypes.Type(value = InfosphereEventsIAEvent.class, name = "IA_COLUMN_ANALYSIS_SUBMITTED_EVENT"),
        @JsonSubTypes.Type(value = InfosphereEventsIAEvent.class, name = "IA_DATAQUALITY_ANALYSIS_SUBMITTED"),
        @JsonSubTypes.Type(value = InfosphereEventsIAEvent.class, name = "IA_COLUMN_ANALYSIS_STARTED_EVENT"),
        @JsonSubTypes.Type(value = InfosphereEventsIAEvent.class, name = "IA_COLUMN_CLASSIFIED_EVENT"),
        @JsonSubTypes.Type(value = InfosphereEventsIAEvent.class, name = "IA_COLUMN_ANALYZED_EVENT"),
        @JsonSubTypes.Type(value = InfosphereEventsIAEvent.class, name = "IA_PROFILE_BATCH_COMPLETED_EVENT"),
        @JsonSubTypes.Type(value = InfosphereEventsIAEvent.class, name = "IA_DATAQUALITY_ANALYSIS_STARTED_EVENT"),
        @JsonSubTypes.Type(value = InfosphereEventsIAEvent.class, name = "IA_DATAQUALITY_ANALYSIS_FINISHED_EVENT"),
        @JsonSubTypes.Type(value = InfosphereEventsIAEvent.class, name = "IA_TABLE_RESULTS_PUBLISHED")
})
@JsonIgnoreProperties(ignoreUnknown=true)
public class InfosphereEvents {

    /**
     * The 'eventType' property of an event defines the type of event.
     * For example, 'IGC_BUSINESSTERM_EVENT' or 'IGC_BUSINESSCATEGORY_EVENT'.
     */
    protected String eventType;

    /** @see #eventType */ @JsonProperty("eventType") public String getEventType() { return this.eventType; }
    /** @see #eventType */ @JsonProperty("eventType") public void setEventType(String eventType) { this.eventType = eventType; }

}
