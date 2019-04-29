/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.informationview.events;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class RegistrationRequestBody extends InformationViewHeader {

    SoftwareServerCapabilitySource softwareServerCapability;

    public SoftwareServerCapabilitySource getSoftwareServerCapability() {
        return softwareServerCapability;
    }

    public void setSoftwareServerCapability(SoftwareServerCapabilitySource softwareServerCapability) {
        this.softwareServerCapability = softwareServerCapability;
    }

    @Override
    public String toString() {
        return "{" +
                "softwareServerCapability=" + softwareServerCapability +
                '}';
    }
}
