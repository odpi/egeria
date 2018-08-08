/* SPDX-License-Identifier: Apache-2.0 */

package org.odpi.openmetadata.accessservices.governanceengine.api.events;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * GovernanceEngineEvent describes the structure of the events emitted by the Governance Engine OMAS.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class GovernanceEngineEvent implements Serializable {
    GovernanceEngineEventType eventType = GovernanceEngineEventType.UNKNOWN_GOVERNANCE_ENGINE_EVENT;

    //TODO: Very different style of events - revisit
    private static final long serialVersionUID = 1L;

    /**
     * Default constructor
     */
    public GovernanceEngineEvent() {
    }


    /**
     * Return the type of event.
     *
     * @return event type enum
     */
    public GovernanceEngineEventType getEventType() {
        return eventType;
    }


    /**
     * Set up the type of event.
     *
     * @param eventType - event type enum
     */
    public void setEventType(GovernanceEngineEventType eventType) {
        this.eventType = eventType;
    }


}
