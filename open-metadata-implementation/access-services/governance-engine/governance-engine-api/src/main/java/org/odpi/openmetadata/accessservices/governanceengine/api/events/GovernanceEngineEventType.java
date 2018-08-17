/* SPDX-License-Identifier: Apache-2.0 */

package org.odpi.openmetadata.accessservices.governanceengine.api.events;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * GovernanceEngineEventTypes describes the different types of events produced by the GovernanceEngine OMAS.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)

public enum GovernanceEngineEventType implements Serializable {
    UNKNOWN_GOVERNANCE_ENGINE_EVENT(0, "UnknownGovernanceEngineEvent", "A Governance Engine event that is not recognized by the local handlers."),
    NEW_TAG_EVENT(1, "NewTag", "A new tag has been defined."),
    UPDATED_TAG_EVENT(2, "UpdatedTag", "An existing tag has been updated."),
    DELETED_TAG_EVENT(3, "DeletedTag", "An existing tag has been deleted."),
    NEW_GOVERNEDASSETCOMP_EVENT(101, "NewGovernedAssetComponent", "An asset componenthas been linked to a tag."),
    UPDATED_GOVERNEDASSETCOMP_EVENT(102, "UpdatedGovernedAssetComponent", "An asset component's link to a tag has changed."),
    DELETED_GOVERNEDASSETOMP_EVENT(103, "DeletedGovernedAssetComponent", "An asset component has had it's tag removed");


    private static final long serialVersionUID = 1L;

    private int eventTypeCode;
    private String eventTypeName;
    private String eventTypeDescription;


    /**
     * Default Constructor - sets up the specific values for this instance of the enum.
     *
     * @param eventTypeCode        - int identifier used for indexing based on the enum.
     * @param eventTypeName        - string name used for messages that include the enum.
     * @param eventTypeDescription - default description for the enum value - used when natural resource
     *                             bundle is not available.
     */
    GovernanceEngineEventType(int eventTypeCode, String eventTypeName, String eventTypeDescription) {
        this.eventTypeCode = eventTypeCode;
        this.eventTypeName = eventTypeName;
        this.eventTypeDescription = eventTypeDescription;
    }


    /**
     * Return the int identifier used for indexing based on the enum.
     *
     * @return int identifier code
     */
    public int getEventTypeCode() {
        return eventTypeCode;
    }


    /**
     * Return the string name used for messages that include the enum.
     *
     * @return String name
     */
    public String getEventTypeName() {
        return eventTypeName;
    }


    /**
     * Return the default description for the enum value - used when natural resource
     * bundle is not available.
     *
     * @return String default description
     */
    public String getEventTypeDescription() {
        return eventTypeDescription;
    }
}
