/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.securityofficer.api.events;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.securityofficer.api.model.GovernedAsset;


import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * GovernedAssetEvent describes the structure of the Governance Asset events published by the Security Officer OMAS.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class GovernedAssetEvent extends SecurityOfficerEvent {

    private static final long                   serialVersionUID = 1L;
    private              GovernedAsset          governedAsset;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public GovernedAsset getGovernedAsset() {
        return governedAsset;
    }

    public void setGovernedAsset(GovernedAsset governedAsset) {
        this.governedAsset = governedAsset;
    }

    @Override
    public String toString() {
        return "GovernedAssetEvent{" +
                "eventType=" + getEventType() +
                ", governedAsset=" + governedAsset +
                '}';
    }
}
