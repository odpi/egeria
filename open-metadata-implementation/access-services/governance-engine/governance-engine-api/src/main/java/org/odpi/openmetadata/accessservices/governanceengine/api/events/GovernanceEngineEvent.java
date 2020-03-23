/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceengine.api.events;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.governanceengine.api.model.GovernedAsset;

import java.io.Serializable;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * GovernanceEngineEvent describes the structure of the events published by the Governance Engine OMAS.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class GovernanceEngineEvent implements Serializable {

    private static final long serialVersionUID = 1L;
    private GovernanceEngineEventType eventType = GovernanceEngineEventType.UNKNOWN_GOVERNANCE_ENGINE_EVENT;
    private GovernedAsset governedAsset;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public GovernanceEngineEventType getEventType() {
        return eventType;
    }

    public void setEventType(GovernanceEngineEventType eventType) {
        this.eventType = eventType;
    }

    public GovernedAsset getGovernedAsset() {
        return governedAsset;
    }

    public void setGovernedAsset(GovernedAsset governedAsset) {
        this.governedAsset = governedAsset;
    }

    @Override
    public String toString() {
        return "GovernanceEngineEvent{" +
                "eventType=" + eventType +
                ", governedAsset=" + governedAsset +
                '}';
    }
}
